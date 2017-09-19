package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Collection("")
public class BenefitClub extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "Name was empty")
    private String name;

    @NotEmpty(message = "Description was empty")
    private String description;

    @Size(min = 1, message = "Identification was the empty string")
    private String identification;

    @NotEmpty(message = "Image was empty")
    private String image;

    @NotEmpty(message = "Organisation was empty")
    private String organisation;

    @NotNull(message = "Location count was missing")
    private Integer locationCount;

    @JsonCreator
    public BenefitClub(@JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("identification") String identification, @JsonProperty("image") String image, @JsonProperty("organisation") String organisation, @JsonProperty("locationCount") Integer locationCount) {
        this.name = name;
        this.description = description;
        this.identification = identification;
        this.image = image;
        this.organisation = organisation;
        this.locationCount = locationCount;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentification() {
        return identification;
    }

    public String getImage() {
        return image;
    }

    public String getOrganisation() {
        return organisation;
    }

    public Integer getLocationCount() {
        return locationCount;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        BenefitClub that = (BenefitClub) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(identification, that.identification) &&
                Objects.equals(image, that.image) &&
                Objects.equals(organisation, that.organisation) &&
                Objects.equals(locationCount, that.locationCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, identification, image, organisation, locationCount);
    }

    @Override
    public String toString() {
        return "BenefitClub{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", identification='" + identification + '\'' +
                ", image='" + image + '\'' +
                ", organisation='" + organisation + '\'' +
                ", locationCount=" + locationCount +
                '}';
    }

    /* ### HIBERNATE ### */

    private BenefitClub() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setIdentification(String identification) {
        this.identification = identification;
    }

    private void setImage(String image) {
        this.image = image;
    }

    private void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    private void setLocationCount(Integer locationCount) {
        this.locationCount = locationCount;
    }

    /* ### END OF HIBERNATE ### */
}
