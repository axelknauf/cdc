package org.freeshell.axe.cdc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Java bean to hold all configuration settings and defaults for this
 * Countdown Clock.
 */
public class Configuration implements ConfigurationConstants {

    private static final Logger logger = LogManager.getLogger(Configuration.class);

    private Properties props = new Properties();

    public Configuration() {
        logger.info("Initalizing configuration");
        try {
            InputStream cfgFile = this.getClass().getResourceAsStream(CONFIG_FILE);
            initDefaults(props);
            props.load(cfgFile);
            logger.info("Successfully loaded configuration file " + CONFIG_FILE);
        } catch (IOException e) {
            logger.warn("Unable to load configuration file [" + CONFIG_FILE + "], using defaults");
        }

        // Invariant: properties have been loaded and exist
        assert props != null;
        assert props.size() > 0;
    }

    private void initDefaults(Properties props) {
        props.put(KEY_IMAGE_PROVIDER, "org.freeshell.axe.cdc.GeneratingImageProvider");
        props.put(KEY_TIMEOUT_MESSAGE, "The timer has expired.");
    }

    public String getString(String key) {
        return props.getProperty(key);
    }

}
