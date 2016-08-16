package org.freeshell.axe.cdc;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class Main {

    public static void main(String[] argv) {
        // Sanity check, if OS supports system tray
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray is not supported, exiting.");
            System.exit(1);
        }

        // Put UI on event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (AWTException e) {
                    System.err.println("System tray not available, exiting.");
                }
            }
        });
    }

    private static void createAndShowGUI() throws AWTException {
        final SystemTray tray = SystemTray.getSystemTray();

        // Create inner items
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = getTrayIcon();
        assert trayIcon != null;

        // Create contents and add items
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem startTimer = new MenuItem("Start timer");
        popup.add(exitItem);
        popup.add(startTimer);
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);

        // Action listeners
        trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null, "Hello, world!"));
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
        startTimer.addActionListener(e -> {
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Void doInBackground() throws Exception {
                    Timer timeout = new Timer(3000, e -> {
                        JOptionPane.showMessageDialog(null, "Timeout expired!");
                    });
                    timeout.setRepeats(false);
                    timeout.start();
                    return null;
                }
            };
            worker.execute();
        });
    }

    // FIXME: replace with own icon
    // FIXME: replace with property pointing to arbitrary icon / make configurable for user
    private static TrayIcon getTrayIcon() {
        Image image = createImage("/images/bulb.gif", "tray icon");
        TrayIcon trayIcon = null;
        if (image != null) {
            trayIcon = new TrayIcon(image);
        }
        else {
            System.err.println("Icon cannot be found, exiting.");
            System.exit(3);
        }
        return trayIcon;
    }

    /**
     * Creates an Image object from the given path location, reading an
     * icon.
     *
     * @param path the filesystem path to the icon file
     * @param description the optional description, may be null
     *
     * @return the Image object representing the icon
     *
     * @see ImageIcon#ImageIcon(URL, String)
     */
    private static Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

}
