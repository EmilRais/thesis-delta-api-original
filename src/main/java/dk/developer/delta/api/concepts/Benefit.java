package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Objects;

@Collection("")
public class Benefit extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "Benefit id was empty")
    private String benefitId;

    @NotEmpty(message = "Membership level id was empty")
    private String membershipLevelId;

    @NotEmpty(message = "Description was empty")
    private String description;

    @Size(min = 1, message = "Condition was the empty string")
    private String condition;

    public Benefit(String benefitId, String membershipLevelId, String description, String condition) {
        this.benefitId = benefitId;
        this.membershipLevelId = membershipLevelId;
        this.description = description;
        this.condition = condition;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getBenefitId() {
        return benefitId;
    }

    public void setBenefitId(String benefitId) {
        this.benefitId = benefitId;
    }

    public String getMembershipLevelId() {
        return membershipLevelId;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Benefit benefit = (Benefit) o;
        return Objects.equals(benefitId, benefit.benefitId) &&
                Objects.equals(membershipLevelId, benefit.membershipLevelId) &&
                Objects.equals(description, benefit.description) &&
                Objects.equals(condition, benefit.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitId, membershipLevelId, description, condition);
    }

    @Override
    public String toString() {
        return "Benefit{" +
                "id='" + id + '\'' +
                ", benefitId='" + benefitId + '\'' +
                ", membershipLevelId='" + membershipLevelId + '\'' +
                ", description='" + description + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }

    /* ### HIBERNATE ### */

    private Benefit() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setMembershipLevelId(String membershipLevelId) {
        this.membershipLevelId = membershipLevelId;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setCondition(String condition) {
        this.condition = condition;
    }

    /* ### END OF HIBERNATE ### */
}
