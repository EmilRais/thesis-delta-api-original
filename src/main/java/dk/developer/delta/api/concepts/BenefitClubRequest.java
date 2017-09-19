package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Collection("")
public class BenefitClubRequest extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "User id was empty")
    private String userId;

    @NotEmpty(message = "Benefit club name was empty")
    private String benefitClubName;

    @NotNull(message = "Benefit club organisation was missing")
    private String benefitClubOrganisation;

    @NotNull(message = "Benefit club description was missing")
    private String benefitClubDescription;

    public BenefitClubRequest(String userId, String benefitClubName, String benefitClubOrganisation, String benefitClubDescription) {
        this.userId = userId;
        this.benefitClubName = benefitClubName;
        this.benefitClubOrganisation = benefitClubOrganisation;
        this.benefitClubDescription = benefitClubDescription;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBenefitClubName() {
        return benefitClubName;
    }

    public String getBenefitClubOrganisation() {
        return benefitClubOrganisation;
    }

    public String getBenefitClubDescription() {
        return benefitClubDescription;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        BenefitClubRequest that = (BenefitClubRequest) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(benefitClubName, that.benefitClubName) &&
                Objects.equals(benefitClubOrganisation, that.benefitClubOrganisation) &&
                Objects.equals(benefitClubDescription, that.benefitClubDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, benefitClubName, benefitClubOrganisation, benefitClubDescription);
    }

    @Override
    public String toString() {
        return "BenefitClubRequest{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", benefitClubName='" + benefitClubName + '\'' +
                ", benefitClubOrganisation='" + benefitClubOrganisation + '\'' +
                ", benefitClubDescription='" + benefitClubDescription + '\'' +
                '}';
    }

    /* ### HIBERNATE ### */

    private BenefitClubRequest() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setBenefitClubName(String benefitClubName) {
        this.benefitClubName = benefitClubName;
    }

    private void setBenefitClubOrganisation(String benefitClubOrganisation) {
        this.benefitClubOrganisation = benefitClubOrganisation;
    }

    private void setBenefitClubDescription(String benefitClubDescription) {
        this.benefitClubDescription = benefitClubDescription;
    }

    /* ### END OF HIBERNATE ### */
}
