package dk.developer.delta.api;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;

import static dk.developer.delta.api.CurrentTimeTimestamper.DATE_FORMAT;
import static dk.developer.testing.Truth.ASSERT;

public class CurrentTimeTimestamperTest {
    private Timestamper timestamper;

    @BeforeMethod
    public void setUp() throws Exception {
        this.timestamper = new CurrentTimeTimestamper();
    }

    @Test
    public void shouldTimestampWithSomePrecision() throws Exception {
        Date beforeDate = new Date();
        Date timestamp = DATE_FORMAT.parse(timestamper.timestamp());
        Date afterDate = new Date();

        ASSERT.that(beforeDate.before(timestamp));
        ASSERT.that(afterDate.after(timestamp));
    }
}