package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SubscriptionConstructor {
    @NotEmpty(message = "Benefit club id was empty")
    private String benefitClubId;

    @NotNull(message = "Membership number was missing")
    private String membershipNumber;

    @NotEmpty(message = "Membership level was empty")
    private String membershipLevel;

    @JsonCreator
    public SubscriptionConstructor(@JsonProperty("benefitClubId") String benefitClubId, @JsonProperty("membershipNumber") String membershipNumber, @JsonProperty("membershipLevel") String membershipLevel) {
        this.benefitClubId = benefitClubId;
        this.membershipNumber = membershipNumber;
        this.membershipLevel = membershipLevel;
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

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        SubscriptionConstructor that = (SubscriptionConstructor) o;
        return Objects.equals(benefitClubId, that.benefitClubId) &&
                Objects.equals(membershipNumber, that.membershipNumber) &&
                Objects.equals(membershipLevel, that.membershipLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitClubId, membershipNumber, membershipLevel);
    }

    @Override
    public String toString() {
        return "SubscriptionConstructor{" +
                "benefitClubId='" + benefitClubId + '\'' +
                ", membershipNumber='" + membershipNumber + '\'' +
                ", membershipLevel='" + membershipLevel + '\'' +
                '}';
    }
}
