package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LogEventType {
    SEARCH_TEXT("SearchText"), // Id: 1
    SEARCH_CATEGORY("SearchCategory"), // Id: 2
    NAVIGATED_TO_BENEFIT_PAIR("NavigatedToBenefitPair"), // Id: 3
    DATE("Date"); // Id: 4

    private final String value;

    @JsonCreator
    LogEventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
} 
