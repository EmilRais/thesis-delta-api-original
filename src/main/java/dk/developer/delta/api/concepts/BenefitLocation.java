package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class BenefitLocation {
    @JsonProperty("_id")
    private final String id;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final String url;
    private final boolean online;
    private final String street;
    private final String place;
    private final String city;
    private final String zipCode;
    private final String phone;

    public BenefitLocation(@JsonProperty("_id") String id,
                           @JsonProperty("name") String name,
                           @JsonProperty("latitude") double latitude,
                           @JsonProperty("longitude") double longitude,
                           @JsonProperty("url") String url,
                           @JsonProperty("online") boolean online,
                           @JsonProperty("street") String street,
                           @JsonProperty("place") String place,
                           @JsonProperty("city") String city,
                           @JsonProperty("zipCode") String zipCode,
                           @JsonProperty("phone") String phone) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.online = online;
        this.street = street;
        this.place = place;
        this.city = city;
        this.zipCode = zipCode;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        BenefitLocation that = (BenefitLocation) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                online == that.online &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(street, that.street) &&
                Objects.equals(place, that.place) &&
                Objects.equals(city, that.city) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latitude, longitude, url, online, street, place, city, zipCode, phone);
    }

    @Override
    public String toString() {
        return "BenefitLocation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", url='" + url + '\'' +
                ", online=" + online +
                ", street='" + street + '\'' +
                ", place='" + place + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getUrl() {
        return url;
    }

    public boolean isOnline() {
        return online;
    }

    public String getStreet() {
        return street;
    }

    public String getPlace() {
        return place;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhone() {
        return phone;
    }
}
