package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;

import java.util.Objects;

@Collection("")
public class MoodCategory extends DatabaseObject implements CategoryConvertible {
    private String id = "NOT-GENERATED-YET";
    private String name;

    public MoodCategory(String name) {
        this.name = name;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        MoodCategory that = (MoodCategory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "MoodCategory{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public Category toCategory() {
        return new Category(id, name, CategoryType.MOOD);
    }

    /* ### HIBERNATE ### */

    private MoodCategory() {
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

    /* ### END OF HIBERNATE ### */
}
