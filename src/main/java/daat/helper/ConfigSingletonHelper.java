package daat.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigSingletonHelper {
    private Properties properties;
    private static ConfigSingletonHelper instance;
    private static final Logger logger = Logger.getLogger(ConfigSingletonHelper.class.getName());
    private static final String EXTERNAL_CONFIG_PATH = System.getProperty("user.dir") + File.separator
            + "configuration.properties";

    // Private constructor to enforce Singleton pattern
    private ConfigSingletonHelper() {
        properties = new Properties();
        try {
            InputStream input = null;
            try {
                input = new FileInputStream(EXTERNAL_CONFIG_PATH);
                logger.info("Loading configuration from external path: " + EXTERNAL_CONFIG_PATH);
            } catch (IOException e) {
                logger.info("External configuration not found, loading from classpath.");
                input = getClass().getClassLoader().getResourceAsStream("configuration.properties");
            }

            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("Sorry, unable to find configuration.properties");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error loading configuration.properties", ex);
        }
    }

    // Public method to provide access to the instance
    public static ConfigSingletonHelper getInstance() {
        if (instance == null) {
            synchronized (ConfigSingletonHelper.class) {
                if (instance == null) {
                    instance = new ConfigSingletonHelper();
                }
            }
        }
        return instance;
    }

    // Method to get property value by key
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
