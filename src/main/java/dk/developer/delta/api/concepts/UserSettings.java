package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Collection("")
// The Hibernate proxy adds these fields and causes errors with Jackson if not ignored
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class UserSettings extends DatabaseObject implements Serializable {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotNull(message = "Only own benefits was missing")
    private Boolean onlyOwnBenefits;

    @NotNull(message = "Number of locations was missing")
    private Integer numberOfLocations;

    public UserSettings(Boolean onlyOwnBenefits, Integer numberOfLocations) {
        this.onlyOwnBenefits = onlyOwnBenefits;
        this.numberOfLocations = numberOfLocations;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public Boolean getOnlyOwnBenefits() {
        return onlyOwnBenefits;
    }

    public Integer getNumberOfLocations() {
        return numberOfLocations;
    }

    public void setOnlyOwnBenefits(Boolean onlyOwnBenefits) {
        this.onlyOwnBenefits = onlyOwnBenefits;
    }

    public void setNumberOfLocations(Integer numberOfLocations) {
        this.numberOfLocations = numberOfLocations;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        UserSettings that = (UserSettings) o;
        return Objects.equals(onlyOwnBenefits, that.onlyOwnBenefits) &&
                Objects.equals(numberOfLocations, that.numberOfLocations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(onlyOwnBenefits, numberOfLocations);
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id='" + id + '\'' +
                ", onlyOwnBenefits=" + onlyOwnBenefits +
                ", numberOfLocations=" + numberOfLocations +
                '}';
    }

    /* ### HIBERNATE ### */

    public UserSettings() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    /* ### END OF HIBERNATE ### */
}
