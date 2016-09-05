package org.freeshell.axe.cdc;

import java.util.HashMap;
import java.util.Map;

/**
 * Java bean to hold all configuration settings and defaults for this
 * Countdown Clock.
 */
public class Configuration {

    public static final String IMAGE_PROVIDER = "imageProvider";

    // FIXME: which standard properties
    // FIXME: which defaults for them
    // FIXME: what shall be customizable? e. g. allow custom ImageIcon creator class? etc.
    // FIXME: read from properties file / store to properties file
    // FIXME: make CdcTrayIcon use this Configuration class for initialization
    // FIXME: allow custom LaF?

    private Map<String, Object> config = new HashMap<>();

    public Configuration() {
        config.put(IMAGE_PROVIDER, "org.freeshell.axe.cdc.GeneratingImageProvider");
    }

    public Object get(String key) {
        return config.get(key);
    }

    public String getString(String key) {
        return String.valueOf(get(key));
    }
}
