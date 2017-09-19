package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Collection("")
public class MembershipLevel extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "Benefit club id was empty")
    private String benefitClubId;

    @NotEmpty(message = "Name was empty")
    private String name;

    @NotEmpty(message = "Condition was empty")
    private String condition;

    @NotNull(message = "Sort key was missing")
    private Integer sortKey;

    @NotEmpty(message = "Image was empty")
    private String image;

    public MembershipLevel(String benefitClubId, String name, String condition, Integer sortKey, String image) {
        this.benefitClubId = benefitClubId;
        this.name = name;
        this.condition = condition;
        this.sortKey = sortKey;
        this.image = image;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getBenefitClubId() {
        return benefitClubId;
    }

    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }

    public Integer getSortKey() {
        return sortKey;
    }

    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        MembershipLevel that = (MembershipLevel) o;
        return Objects.equals(benefitClubId, that.benefitClubId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(condition, that.condition) &&
                Objects.equals(sortKey, that.sortKey) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitClubId, name, condition, sortKey, image);
    }

    @Override
    public String toString() {
        return "MembershipLevel{" +
                "id='" + id + '\'' +
                ", benefitClubId='" + benefitClubId + '\'' +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                ", sortKey=" + sortKey +
                ", image='" + image + '\'' +
                '}';
    }

    /* ### HIBERNATE ### */

    private MembershipLevel() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setBenefitClubId(String benefitClubId) {
        this.benefitClubId = benefitClubId;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setCondition(String condition) {
        this.condition = condition;
    }

    private void setSortKey(Integer sortKey) {
        this.sortKey = sortKey;
    }

    private void setImage(String image) {
        this.image = image;
    }

    /* ### END OF HIBERNATE ### */
}
