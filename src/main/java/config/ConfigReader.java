package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigReader {

    private final static File FILE = new File("/home/dev/NetBeansProjects/bankdemoapp/Bank/src/main/java/config/config.properties");
    private final static Logger LOGGER = LoggerConfig.getLoggerInstance();
    private static final String CLASS_NAME = "ConfigReader";

    private ConfigReader() {

    }

    public static String readProperty(String key) {
        String methodName = "readProperty()";
        LOGGER.entering(CLASS_NAME, methodName);
        try ( Reader reader = new FileReader(FILE)) {
            LOGGER.fine("Reading property");
            Properties properties = new Properties();
            properties.load(reader);
            String value = properties.getProperty(key, "");
            LOGGER.fine("Read property");
            LOGGER.exiting(CLASS_NAME, methodName);
            return value;
        } catch (FileNotFoundException e) {
            LOGGER.severe("FileNotFoundException");
        } catch (IOException ex) {
            LOGGER.severe("IOException");
        }
        LOGGER.exiting(CLASS_NAME, methodName);
        return "";
    }

}
