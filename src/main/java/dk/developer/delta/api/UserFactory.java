package dk.developer.delta.api;

import dk.developer.delta.api.concepts.User;
import dk.developer.security.Credential;

public interface UserFactory {
    User create(Credential credential);
}
