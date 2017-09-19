package dk.developer.delta.api.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.DatabaseFront;
import dk.developer.delta.api.Application;
import dk.developer.delta.api.concepts.*;
import dk.developer.security.Credential;
import dk.developer.security.Security;
import dk.developer.server.Server;
import dk.developer.utility.Converter;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.developer.delta.api.Status.*;
import static dk.developer.delta.api.concepts.LogEventType.SEARCH_CATEGORY;
import static dk.developer.delta.api.concepts.LogEventType.SEARCH_TEXT;
import static dk.developer.security.Security.Mechanism.LOGIN;
import static dk.developer.utility.Convenience.set;

@Path("search")
public class SearchService {
    private final Converter converter;
    private final DatabaseFront database;

    public SearchService() {
        converter = Converter.converter();
        database = Application.database();
    }

    @POST
    @Path("plain")
    @Security(LOGIN)
    public Response plainSearch(PlainSearch search) {
        String query = plainQuery(search);
        SearchResults primaryResults = primarySearch(query);
        Set<BenefitTriple> secondaryBenefitTriples = secondarySearch(primaryResults);
        SearchResults results = new SearchResults(primaryResults.getBenefitLocations(), primaryResults.getPrimaryBenefitTriples(), secondaryBenefitTriples);

        return Server.respond(results).as(PLAIN_SEARCH);
    }

    @POST
    @Path("text")
    @Security(LOGIN)
    public Response textSearch(TextSearch search, @HeaderParam(Security.HEADER) Credential credential) {
        String physicalQuery = textQuery(search);
        SearchResults physicalResults = primarySearch(physicalQuery);

        String onlineQuery = onlineTextQuery(search);
        SearchResults onlineResults = primarySearch(onlineQuery);

        SearchResults primaryResults = union(physicalResults, onlineResults);
        Set<BenefitTriple> secondaryBenefitTriples = secondarySearch(primaryResults);
        SearchResults results = new SearchResults(primaryResults.getBenefitLocations(), primaryResults.getPrimaryBenefitTriples(), secondaryBenefitTriples);

        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        int numberOfResults = results.getPrimaryBenefitTriples().size() + results.getSecondaryBenefitTriples().size();
        database.save(new LogEvent(user.getId(), SEARCH_TEXT, search.query, numberOfResults));

        return Server.respond(results).as(TEXT_SERACH);
    }

    @POST
    @Path("category")
    @Security(LOGIN)
    public Response categorySearch(CategorySearch search, @HeaderParam(Security.HEADER) Credential credential) {
        String physicalQuery = categoryQuery(search);
        SearchResults physicalResults = primarySearch(physicalQuery);

        String onlineQuery = onlineCategoryQuery(search);
        SearchResults onlineResults = primarySearch(onlineQuery);

        SearchResults primaryResults = union(physicalResults, onlineResults);
        Set<BenefitTriple> secondaryBenefitTriples = secondarySearch(primaryResults);
        SearchResults results = new SearchResults(primaryResults.getBenefitLocations(), primaryResults.getPrimaryBenefitTriples(), secondaryBenefitTriples);

        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        int numberOfResults = results.getPrimaryBenefitTriples().size() + results.getSecondaryBenefitTriples().size();
        database.save(new LogEvent(user.getId(), SEARCH_CATEGORY, search.getCategoryType().json() + " id: " + search.categoryId, numberOfResults));

        return Server.respond(results).as(CATEGORY_SEARCH);
    }

    @POST
    @Path("benefit/club")
    @Security(LOGIN)
    public Response benefitClubSearch(BenefitClubSearch search) {
        String query = benefitClubQuery(search);
        SearchResults results = primarySearch(query);
        return Server.respond(results).as(BENEFIT_CLUB_SEARCH);
    }

    @SuppressWarnings("unchecked")
    private SearchResults primarySearch(String query) {
        Response response = searchRequest(query);
        Map<String, Object> data = response.readEntity(Map.class);
        response.close();

        Map<String, Object> hits = (Map<String, Object>) data.get("hits");
        List<Map<String, Object>> results = (List<Map<String, Object>>) hits.get("hits");

        Set<BenefitLocation> benefitLocations = results.stream()
                .map(hit -> ((Map<String, Object>) hit.get("_source")))
                .map(this::parseBenefitLocation)
                .collect(Collectors.toSet());

        Set<BenefitTriple> benefitTriples = results.stream()
                .map(hit -> {
                    Double score = (Double) hit.get("_score");
                    Map<String, Object> source = (Map<String, Object>) hit.get("_source");
                    return parseBenefitTriple(source, score);
                })
                .collect(Collectors.toSet());

        return new SearchResults(benefitLocations, benefitTriples);
    }

    @SuppressWarnings("unchecked")
    private BenefitLocation parseBenefitLocation(Map<String, Object> source) {
        String id = String.valueOf(source.get("locationid"));
        String name = (String) source.get("locationname");
        List<Double> location = (List<Double>) source.get("location");
        String url = (String) source.get("onlineshopwebaddress");
        url = url == null || url.isEmpty() ? null : url;
        boolean online = (boolean) source.get("onlineflag");
        String street = (String) source.get("locationaddressline1");
        String place = (String) source.get("locationaddressline2");
        String city = (String) source.get("locationaddresscity");
        String zipCode = (String) source.get("locationaddresszipcode");
        String phone = (String) source.get("locationcontactphone");
        return new BenefitLocation(id, name, location.get(1), location.get(0), url, online, street, place, city, zipCode,
                phone);
    }

    @SuppressWarnings("unchecked")
    private Set<BenefitTriple> secondarySearch(SearchResults primaryResults) {
        String query = locationQuery(primaryResults.getBenefitLocations());
        Response response = searchRequest(query);
        Map<String, Object> data = response.readEntity(Map.class);
        response.close();

        Map<String, Object> hits = (Map<String, Object>) data.get("hits");
        List<Map<String, Object>> results = (List<Map<String, Object>>) hits.get("hits");

        Set<BenefitTriple> secondaryTriples = results.stream()
                .map(hit -> (Map<String, Object>) hit.get("_source"))
                .map(source -> parseBenefitTriple(source, null))
                .collect(Collectors.toSet());
        secondaryTriples.removeAll(primaryResults.getPrimaryBenefitTriples());
        return secondaryTriples;
    }

    private BenefitTriple parseBenefitTriple(Map<String, Object> source, Double score) {
        String id = String.valueOf(source.get("indexid"));
        String benefitLocationId = String.valueOf(source.get("locationid"));
        String benefitClubId = String.valueOf(source.get("programid"));
        String benefitId = String.valueOf(source.get("programbenefitid"));
        return new BenefitTriple(id, benefitLocationId, benefitClubId, benefitId, score);
    }

    private String locationQuery(Set<BenefitLocation> benefitLocations) {
        Set<String> locationIds = benefitLocations.stream()
                .map(BenefitLocation::getId)
                .collect(Collectors.toSet());
        String json = converter.toJson(locationIds);

        return loadQuery("LocationQuery.json")
                .replace("#LOCATIONS#", json);
    }

    private <T> Set<T> union(Set<T> left, Set<T> right) {
        Set<T> union = new HashSet<>();
        union.addAll(left);
        union.addAll(right);
        return union;
    }

    private SearchResults union(SearchResults left, SearchResults right) {
        Set<BenefitLocation> benefitLocations = union(left.getBenefitLocations(), right.getBenefitLocations());
        Set<BenefitTriple> primaryBenefitTriples = union(left.getPrimaryBenefitTriples(), right.getPrimaryBenefitTriples());
        Set<BenefitTriple> secondaryBenefitTriples = union(left.getSecondaryBenefitTriples(), right.getSecondaryBenefitTriples());
        return new SearchResults(benefitLocations, primaryBenefitTriples, secondaryBenefitTriples);
    }

    private Response searchRequest(String json) {
        Configuration configuration = database.loadAll(Configuration.class).everything().get(0);
        String elasticSearchUrl = configuration.getElasticSearchUrl();

        Client client = ClientBuilder.newClient();
        return client.target(elasticSearchUrl)
                .request()
                .post(Entity.json(json));
    }

    private String loadQuery(String fileName) {
        Client client = ClientBuilder.newClient();
        return client.target("http://localhost/search/" + fileName)
                .request()
                .get(String.class);
    }

    private String plainQuery(PlainSearch search) {
        return loadQuery("PlainQuery.json")
                .replace("#SIZE#", String.valueOf(search.getSize()))
                .replace("#LATITUDE#", String.valueOf(search.getLatitude()))
                .replace("#LONGITUDE#", String.valueOf(search.getLongitude()))
                .replace("#BENEFIT_CLUBS#", converter.toJson(search.getBenefitClubIds()));
    }

    private String textQuery(TextSearch search) {
        return loadQuery("TextQuery.json")
                .replace("#SIZE#", String.valueOf(search.getSize()))
                .replace("#QUERY#", search.getQuery().toLowerCase())
                .replace("#LATITUDE#", String.valueOf(search.getLatitude()))
                .replace("#LONGITUDE#", String.valueOf(search.getLongitude()))
                .replace("#BENEFIT_CLUBS#", converter.toJson(search.getBenefitClubIds()));
    }

    private String onlineTextQuery(TextSearch search) {
        return loadQuery("TextQueryOnline.json")
                .replace("#SIZE#", String.valueOf(search.getSize()))
                .replace("#QUERY#", search.getQuery().toLowerCase())
                .replace("#BENEFIT_CLUBS#", converter.toJson(search.getBenefitClubIds()));
    }

    private String categoryQuery(CategorySearch search) {
        return loadQuery("CategoryQuery.json")
                .replace("#SIZE#", String.valueOf(search.getSize()))
                .replace("#FIELD#", search.fieldName())
                .replace("#CATEGORY#", search.getCategoryId())
                .replace("#LATITUDE#", String.valueOf(search.getLatitude()))
                .replace("#LONGITUDE#", String.valueOf(search.getLongitude()))
                .replace("#BENEFIT_CLUBS#", converter.toJson(search.getBenefitClubIds()));
    }

    private String onlineCategoryQuery(CategorySearch search) {
        return loadQuery("CategoryQueryOnline.json")
                .replace("#SIZE#", String.valueOf(search.getSize()))
                .replace("#FIELD#", search.fieldName())
                .replace("#CATEGORY#", search.getCategoryId())
                .replace("#BENEFIT_CLUBS#", converter.toJson(search.getBenefitClubIds()));
    }

    private String benefitClubQuery(BenefitClubSearch search) {
        return loadQuery("BenefitClubQuery.json")
                .replace("#BENEFIT_CLUB#", search.getBenefitClubId());
    }

    public static class PlainSearch {
        private final int size;
        private final double latitude;
        private final double longitude;
        private final Set<String> benefitClubIds;

        public PlainSearch(@JsonProperty("size") int size,
                           @JsonProperty("latitude") double latitude,
                           @JsonProperty("longitude") double longitude,
                           @JsonProperty("benefitClubIds") Set<String> benefitClubIds) {
            this.size = size;
            this.latitude = latitude;
            this.longitude = longitude;
            this.benefitClubIds = benefitClubIds;
        }

        public int getSize() {
            return size;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public Set<String> getBenefitClubIds() {
            return benefitClubIds;
        }
    }

    public static class TextSearch {
        private final int size;
        private final String query;
        private final double latitude;
        private final double longitude;
        private final Set<String> benefitClubIds;

        @JsonCreator
        public TextSearch(@JsonProperty("size") int size,
                          @JsonProperty("query") String query,
                          @JsonProperty("latitude") double latitude,
                          @JsonProperty("longitude") double longitude,
                          @JsonProperty("benefitClubIds") Set<String> benefitClubIds) {
            this.size = size;
            this.query = query;
            this.latitude = latitude;
            this.longitude = longitude;
            this.benefitClubIds = benefitClubIds;
        }

        public int getSize() {
            return size;
        }

        public String getQuery() {
            return query;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public Set<String> getBenefitClubIds() {
            return benefitClubIds;
        }
    }

    public static class CategorySearch {
        private final int size;
        private final String categoryId;
        private final CategoryType categoryType;
        private final double latitude;
        private final double longitude;
        private final Set<String> benefitClubIds;

        public CategorySearch(@JsonProperty("size") int size,
                              @JsonProperty("categoryId") String categoryId,
                              @JsonProperty("categoryType") CategoryType categoryType,
                              @JsonProperty("latitude") double latitude,
                              @JsonProperty("longitude") double longitude,
                              @JsonProperty("benefitClubIds") Set<String> benefitClubIds) {
            this.size = size;
            this.categoryId = categoryId;
            this.categoryType = categoryType;
            this.latitude = latitude;
            this.longitude = longitude;
            this.benefitClubIds = benefitClubIds;
        }

        public int getSize() {
            return size;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public CategoryType getCategoryType() {
            return categoryType;
        }

        public double getLatitude() {
            return latitude;
        }

        public String fieldName() {
            switch ( categoryType ) {
                case INDUSTRY:
                    return "benefitindustry.industryid";
                case INTEREST:
                    return "benefitinterest.interestid";
                case MOOD:
                    return "benefitmood.moodid";
                case PRODUCT:
                    return "benefitproductcategory.productcategoryid";
                case WEATHER:
                    return "benefitweather.weatherid";
                default:
                    throw new RuntimeException("Unexpected category type");
            }
        }

        public double getLongitude() {
            return longitude;
        }

        public Set<String> getBenefitClubIds() {
            return benefitClubIds;
        }
    }

    public static class BenefitClubSearch {
        private final String benefitClubId;

        public BenefitClubSearch(@JsonProperty("benefitClubId") String benefitClubId) {
            this.benefitClubId = benefitClubId;
        }

        public String getBenefitClubId() {
            return benefitClubId;
        }
    }

    public static class SearchResults {
        private final Set<BenefitLocation> benefitLocations;
        private final Set<BenefitTriple> primaryBenefitTriples;
        private final Set<BenefitTriple> secondaryBenefitTriples;

        @JsonCreator
        public SearchResults(@JsonProperty("benefitLocations") Set<BenefitLocation> benefitLocations,
                             @JsonProperty("primaryBenefitTriples") Set<BenefitTriple> primaryBenefitTriples,
                             @JsonProperty("secondaryBenefitTriples") Set<BenefitTriple> secondaryBenefitTriples) {
            this.benefitLocations = benefitLocations;
            this.primaryBenefitTriples = primaryBenefitTriples;
            this.secondaryBenefitTriples = secondaryBenefitTriples;
        }

        public SearchResults(Set<BenefitLocation> benefitLocations, Set<BenefitTriple> primaryBenefitTriples) {
            this.benefitLocations = benefitLocations;
            this.primaryBenefitTriples = primaryBenefitTriples;
            this.secondaryBenefitTriples = set();
        }

        public Set<BenefitLocation> getBenefitLocations() {
            return benefitLocations;
        }

        public Set<BenefitTriple> getPrimaryBenefitTriples() {
            return primaryBenefitTriples;
        }

        public Set<BenefitTriple> getSecondaryBenefitTriples() {
            return secondaryBenefitTriples;
        }
    }
}
