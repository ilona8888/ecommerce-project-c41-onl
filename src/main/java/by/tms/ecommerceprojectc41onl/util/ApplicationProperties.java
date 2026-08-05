package by.tms.ecommerceprojectc41onl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApplicationProperties {

    private static final String PROPERTIES_FILE = "application.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ApplicationProperties() {
    }

    public static String loadProperties(String key) {
        return getRequired(key);
    }

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue).trim();
    }

    public static String getRequired(String key) {
        String value = get(key);

        if (value.isEmpty()) {
            throw new IllegalStateException("Property " + key + " is required");
        }

        return value;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = ApplicationProperties.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException(PROPERTIES_FILE + " not found in classpath");
            }

            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + PROPERTIES_FILE, e);
        }

        return properties;
    }
}
