package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Collection("")
public class UserPermission extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "Facebook user id was empty")
    private String facebookUserId;

    @NotNull(message = "Permission id was missing")
    private Integer permissionId;

    @NotEmpty(message = "Date was empty")
    private String date;

    @NotNull(message = "Allowed was missing")
    private Boolean allowed;

    public UserPermission(String facebookUserId, Integer permissionId, String date, Boolean allowed) {
        this.facebookUserId = facebookUserId;
        this.permissionId = permissionId;
        this.date = date;
        this.allowed = allowed;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        UserPermission that = (UserPermission) o;
        return Objects.equals(facebookUserId, that.facebookUserId) &&
                Objects.equals(permissionId, that.permissionId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(allowed, that.allowed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facebookUserId, permissionId, date, allowed);
    }

    @Override
    public String toString() {
        return "UserPermission{" +
                "id='" + id + '\'' +
                ", facebookUserId='" + facebookUserId + '\'' +
                ", permissionId=" + permissionId +
                ", date='" + date + '\'' +
                ", allowed=" + allowed +
                '}';
    }

    /* ### HIBERNATE ### */

    private UserPermission() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    private void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    /* ### END OF HIBERNATE ### */
}
