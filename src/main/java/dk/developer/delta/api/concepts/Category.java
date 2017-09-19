package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Category {
    @NotEmpty(message = "Id was empty")
    private final String id;

    @NotEmpty(message = "Name was empty")
    private final String name;

    @NotNull(message = "Type was missing")
    private final CategoryType type;

    @JsonCreator
    public Category(@JsonProperty("_id") String id, @JsonProperty("name") String name, @JsonProperty("type") CategoryType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) &&
                Objects.equals(type, category.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
