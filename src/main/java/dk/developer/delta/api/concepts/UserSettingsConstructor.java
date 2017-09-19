package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserSettingsConstructor {
    @NotNull(message = "Only own benefits was missing")
    private Boolean onlyOwnBenefits;

    @NotNull(message = "Number of locations was missing")
    private Integer numberOfLocations;

    @JsonCreator
    public UserSettingsConstructor(@JsonProperty("onlyOwnBenefits") Boolean onlyOwnBenefits,
                                   @JsonProperty("numberOfLocations") Integer numberOfLocations,
                                   @JsonProperty("notifyOnBenefits") Boolean notifyOnBenefits) {
        this.onlyOwnBenefits = onlyOwnBenefits;
        this.numberOfLocations = numberOfLocations;
    }

    public Boolean getOnlyOwnBenefits() {
        return onlyOwnBenefits;
    }

    public Integer getNumberOfLocations() {
        return numberOfLocations;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        UserSettingsConstructor that = (UserSettingsConstructor) o;
        return Objects.equals(onlyOwnBenefits, that.onlyOwnBenefits) &&
                Objects.equals(numberOfLocations, that.numberOfLocations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(onlyOwnBenefits, numberOfLocations);
    }

    @Override
    public String toString() {
        return "UserSettingsConstructor{" +
                "onlyOwnBenefits=" + onlyOwnBenefits +
                ", numberOfLocations=" + numberOfLocations +
                '}';
    }
}
