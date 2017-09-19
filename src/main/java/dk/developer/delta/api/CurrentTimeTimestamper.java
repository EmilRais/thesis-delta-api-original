package dk.developer.delta.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimeTimestamper implements Timestamper {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");

    @Override
    public String timestamp() {
        Date currentDate = new Date();
        return DATE_FORMAT.format(currentDate);
    }
}
