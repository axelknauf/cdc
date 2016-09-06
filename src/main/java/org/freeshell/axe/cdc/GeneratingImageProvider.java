package org.freeshell.axe.cdc;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides an ImageIcon for the system tray.
 *
 * @see ImageProvider
 */
public class GeneratingImageProvider implements ImageProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public Image createImage(Dimension trayIconSize) {
        int w = (int) trayIconSize.getWidth();
        int h = (int) trayIconSize.getHeight();

        Image image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.GREEN);
        graphics.fillRect(0, 0, w, h);

        graphics.setColor(Color.RED);
        graphics.fillArc(w / 2, h / 2, (w / 2) - (w / 10) , (h / 2) - (h / 10), 0, 360);

        image.flush();

        return image;
    }

}
