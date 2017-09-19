package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class BenefitTriple {
    @JsonProperty("_id")
    private final String id;
    private final String benefitLocationId;
    private final String benefitClubId;
    private final String benefitId;
    private final Double score;

    @JsonCreator
    public BenefitTriple(@JsonProperty("_id") String id,
                         @JsonProperty("benefitLocationId") String benefitLocationId,
                         @JsonProperty("benefitClubId") String benefitClubId,
                         @JsonProperty("benefitId") String benefitId,
                         @JsonProperty("score") Double score) {
        this.id = id;
        this.benefitLocationId = benefitLocationId;
        this.benefitClubId = benefitClubId;
        this.benefitId = benefitId;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getBenefitLocationId() {
        return benefitLocationId;
    }

    public String getBenefitClubId() {
        return benefitClubId;
    }

    public String getBenefitId() {
        return benefitId;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        BenefitTriple that = (BenefitTriple) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(benefitLocationId, that.benefitLocationId) &&
                Objects.equals(benefitClubId, that.benefitClubId) &&
                Objects.equals(benefitId, that.benefitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, benefitLocationId, benefitClubId, benefitId);
    }

    @Override
    public String toString() {
        return "BenefitTriple{" +
                "id='" + id + '\'' +
                ", benefitLocationId='" + benefitLocationId + '\'' +
                ", benefitClubId='" + benefitClubId + '\'' +
                ", benefitId='" + benefitId + '\'' +
                ", score=" + score +
                '}';
    }
}
