package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import java.util.Objects;

@Collection("")
public class Feedback extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "User id was empty")
    private String userId;

    @NotEmpty(message = "Message was empty")
    private String message;

    public Feedback(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id) &&
                Objects.equals(userId, feedback.userId) &&
                Objects.equals(message, feedback.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, message);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    /* ### HIBERNATE ### */

    private Feedback() {
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

    private void setMessage(String message) {
        this.message = message;
    }

    /* ### END OF HIBERNATE ### */
}
