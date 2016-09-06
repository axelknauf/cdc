package org.freeshell.axe.cdc;

import java.awt.*;

/**
 * This interface declares a method on how to privide a custom image for
 * the system tray icon of this application.
 *
 * @see GeneratingImageProvider
 */
public interface ImageProvider {

    /**
     * Provides an {@link Image} object which will be used as icon
     * in the system tray. This Image should conform to the given {@link Dimension},
     * which will be dependent on the actual system's tray and dynamically
     * determined.
     *
     * @param trayIconSize the size of the image
     * @return the Image object to be displayed in the tray
     */
    Image createImage(Dimension trayIconSize);
}
