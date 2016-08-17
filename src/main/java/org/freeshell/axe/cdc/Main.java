package org.freeshell.axe.cdc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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

        Dimension trayIconSize = tray.getTrayIconSize();

        // Create inner items
        final PopupMenu popup = new PopupMenu();
        TrayIcon trayIcon = new TrayIcon(createImage(trayIconSize));

        // Create contents and add items
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem startTimer = new MenuItem("Start timer");
        popup.add(startTimer);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);

        // Action listeners
        trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null, "Hello, world!"));
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
        startTimer.addActionListener(e -> {
            SwingWorker worker = createTimeoutWorker(3000, "Timeout expired!", 0);
            worker.execute();
        });
    }

    /**
     * Creates a new instance of {@link SwingWorker} executing a Swing timer with the
     * specified parameters.
     *
     * TODO use arbitrary finish action instead of JOptionPane message
     * TODO make parameters configurable in UI (wizard?)
     *
     * @param delaySecs how long the timer shall wait until firing in seconds
     * @param finishMessage which message to display when finished
     * @param repeatDelay if the timeout shall be repeated, set this to > 0, given in seconds.
     *
     * @return the {@link SwingWorker} instance
     */
    private static SwingWorker createTimeoutWorker(final int delaySecs, String finishMessage, int repeatDelay) {
        return new SwingWorker() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Timer timeout = new Timer(delaySecs, e -> {
                            JOptionPane.showMessageDialog(null, finishMessage);
                        });
                        timeout.setRepeats(repeatDelay > 0);
                        if (repeatDelay > 0) {
                            timeout.setDelay(repeatDelay);
                        }
                        timeout.start();
                        return null;
                    }
                };
    }

    /**
     * Creates a tray icon for the give size dimensions.
     *
     * @param trayIconSize the system tray's dimension
     *
     * @return a generated image
     */
    private static Image createImage(Dimension trayIconSize) {
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
