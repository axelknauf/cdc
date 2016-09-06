package org.freeshell.axe.cdc;

import java.util.Map;

/**
 * Constants for configuration settings loaded from cdc.properties.
 */
public interface ConfigurationConstants {

    // internal config
    String CONFIG_FILE = "/cdc.properties";

    // public config, keys for cdc.properties
    String KEY_IMAGE_PROVIDER = "imageProvider";
    String KEY_TIMEOUT_MESSAGE = "timeout.message";
}

