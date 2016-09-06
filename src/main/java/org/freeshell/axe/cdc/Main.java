package org.freeshell.axe.cdc;

import java.awt.*;
import javax.swing.*;

public class Main {

    public static void main(String[] argv) throws AWTException {
        // Sanity check, if OS supports system tray
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray is not supported, exiting.");
            System.exit(1);
        }

        // Put UI on event-dispatching thread
        SwingUtilities.invokeLater(new CdcTrayIcon(new Configuration()));
    }

}
