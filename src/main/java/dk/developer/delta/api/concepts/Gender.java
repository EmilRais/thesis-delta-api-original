package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE("Male"), FEMALE("Female");

    private final String json;

    @JsonCreator
    Gender(String json) {
        this.json = json;
    }

    @JsonValue
    public String json() {
        return json;
    }
}
