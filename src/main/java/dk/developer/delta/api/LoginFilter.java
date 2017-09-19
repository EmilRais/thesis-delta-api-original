package dk.developer.delta.api;

import dk.developer.database.DatabaseFront;
import dk.developer.security.AbstractLoginFilter;
import dk.developer.security.Credential;
import dk.developer.server.Application;
import dk.developer.delta.api.concepts.User;
import dk.developer.utility.Converter;

import javax.ws.rs.ext.Provider;

@Provider
public class LoginFilter extends AbstractLoginFilter {
    private final DatabaseFront database;
    private final Converter converter;

    public LoginFilter() {
        database = Application.database();
        converter = Converter.converter();
    }

    @Override
    protected Validity authenticate(String json) {
        Credential credential = converter.fromJson(json, Credential.class);
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        if ( user == null )
            return Validity.INVALID;

        boolean valid = Application.security().isValid(credential);
        return valid ? Validity.VALID : Validity.INVALID;
    }
}
