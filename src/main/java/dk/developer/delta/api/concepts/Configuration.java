package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Collection("")
public class Configuration extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "App version was empty")
    private String appVersion;

    @NotNull(message = "Default only own benefits setting was missing")
    private Boolean defaultOnlyOwnBenefits;

    @NotNull(message = "Default number of locations setting was missing")
    private Integer defaultNumberOfLocations;

    @NotEmpty(message = "Elastic search url was empty")
    private String elasticSearchUrl;

    @NotEmpty(message = "Terms and conditions were empty")
    private String termsAndConditions;

    public Configuration(String appVersion, Boolean defaultOnlyOwnBenefits, Integer defaultNumberOfLocations, String elasticSearchUrl, String termsAndConditions) {
        this.appVersion = appVersion;
        this.defaultOnlyOwnBenefits = defaultOnlyOwnBenefits;
        this.defaultNumberOfLocations = defaultNumberOfLocations;
        this.elasticSearchUrl = elasticSearchUrl;
        this.termsAndConditions = termsAndConditions;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Boolean getDefaultOnlyOwnBenefits() {
        return defaultOnlyOwnBenefits;
    }

    public Integer getDefaultNumberOfLocations() {
        return defaultNumberOfLocations;
    }

    public String getElasticSearchUrl() {
        return elasticSearchUrl;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(appVersion, that.appVersion) &&
                Objects.equals(defaultOnlyOwnBenefits, that.defaultOnlyOwnBenefits) &&
                Objects.equals(defaultNumberOfLocations, that.defaultNumberOfLocations) &&
                Objects.equals(elasticSearchUrl, that.elasticSearchUrl) &&
                Objects.equals(termsAndConditions, that.termsAndConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appVersion, defaultOnlyOwnBenefits, defaultNumberOfLocations, elasticSearchUrl, termsAndConditions);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id='" + id + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", defaultOnlyOwnBenefits=" + defaultOnlyOwnBenefits +
                ", defaultNumberOfLocations=" + defaultNumberOfLocations +
                ", elasticSearchUrl='" + elasticSearchUrl + '\'' +
                ", termsAndConditions='" + termsAndConditions + '\'' +
                '}';
    }

    /* ### HIBERNATE ### */

    private Configuration() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    private void setDefaultOnlyOwnBenefits(Boolean defaultOnlyOwnBenefits) {
        this.defaultOnlyOwnBenefits = defaultOnlyOwnBenefits;
    }

    private void setDefaultNumberOfLocations(Integer defaultNumberOfLocations) {
        this.defaultNumberOfLocations = defaultNumberOfLocations;
    }

    private void setElasticSearchUrl(String elasticSearchUrl) {
        this.elasticSearchUrl = elasticSearchUrl;
    }

    private void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    /* ### END OF HIBERNATE ### */
}
