package hu.deniszbecskei;

import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(AppConfig.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProps() {
        return props;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
