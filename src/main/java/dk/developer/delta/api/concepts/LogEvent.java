package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Collection("")
public class LogEvent extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "User id was empty")
    private String userId;

    @NotNull(message = "Log event type was missing")
    private LogEventType type;

    @NotEmpty(message = "Value was empty")
    private String value;

    private Integer results;

    public LogEvent(String userId, LogEventType type, String value, Integer results) {
        this.userId = userId;
        this.type = type;
        this.value = value;
        this.results = results;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public LogEventType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Integer getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        LogEvent logEvent = (LogEvent) o;
        return type == logEvent.type &&
                Objects.equals(value, logEvent.value) &&
                Objects.equals(results, logEvent.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, results);
    }

    @Override
    public String toString() {
        return "LogEvent{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", results=" + results +
                '}';
    }

    /* ### HIBERNATE ### */

    private LogEvent() {
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

    private int getTypeId() {
        return type.ordinal() + 1;
    }

    private void setTypeId(int id) {
        type = LogEventType.values()[id - 1];
    }

    private void setValue(String value) {
        this.value = value;
    }

    private void setResults(Integer results) {
        this.results = results;
    }

    /* ### END OF HIBERNATE ### */
}
