package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryType {
    INDUSTRY("industry"), INTEREST("interest"), MOOD("mood"), PRODUCT("product"), WEATHER("weather");

    private final String json;

    @JsonCreator
    CategoryType(String json) {
        this.json = json;
    }

    @JsonValue
    public String json() {
        return json;
    }
}
