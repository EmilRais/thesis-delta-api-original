package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Collection("")
public class Subscription extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "User id was empty")
    private String userId;

    @NotEmpty(message = "Benefit club id was empty")
    private String benefitClubId;

    @NotNull(message = "Membership number was missing")
    private String membershipNumber;

    @NotEmpty(message = "Membership level was empty")
    private String membershipLevel;

    @NotEmpty(message = "Date was empty")
    private String date;

    public Subscription(String userId, String benefitClubId, String membershipNumber, String membershipLevel, String date) {
        this.userId = userId;
        this.benefitClubId = benefitClubId;
        this.membershipNumber = membershipNumber;
        this.membershipLevel = membershipLevel;
        this.date = date;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getBenefitClubId() {
        return benefitClubId;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public String getDate() {
        return date;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public void setBenefitClubId(String benefitClubId) {
        this.benefitClubId = benefitClubId;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(benefitClubId, that.benefitClubId) &&
                Objects.equals(membershipNumber, that.membershipNumber) &&
                Objects.equals(membershipLevel, that.membershipLevel) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, benefitClubId, membershipNumber, membershipLevel, date);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", benefitClubId='" + benefitClubId + '\'' +
                ", membershipNumber='" + membershipNumber + '\'' +
                ", membershipLevel='" + membershipLevel + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    /* ### HIBERNATE ### */

    private Subscription() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    private void setDate(String date) {
        this.date = date;
    }

    /* ### END OF HIBERNATE ### */
}
