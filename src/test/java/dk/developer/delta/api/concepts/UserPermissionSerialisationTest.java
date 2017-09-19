package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class UserPermissionSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(UserPermission.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("UserPermissionInput.json");
        UserPermission permission = converter.fromJson(json, UserPermission.class);

        ASSERT.that(permission).isEqualTo(Factory.Permissions.thomasPermission());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.Permissions.thomasPermission());
        String expectedJson = tool.readFilteredJsonFile("UserPermissionOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
