package org.freeshell.axe.cdc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ax on 19.08.16.
 */
public class CdcTrayIcon implements Runnable {

    private TrayIcon trayIcon = null;

    private void createAndShowGUI() throws AWTException {
        final SystemTray tray = SystemTray.getSystemTray();

        Dimension trayIconSize = tray.getTrayIconSize();

        // Create inner items
        final PopupMenu popup = new PopupMenu();
        this.trayIcon = new TrayIcon(createImage(trayIconSize));

        // Create contents and add items
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem startTimer = new MenuItem("Start timer");
        popup.add(startTimer);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);

        // Action listeners
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
        startTimer.addActionListener(e -> {
            SwingWorker worker = createTimeoutWorker(3000, "The delay has expired.", 0);
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
    private SwingWorker createTimeoutWorker(final int delaySecs, String finishMessage, int repeatDelay) {
        return new SwingWorker() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Timer timeout = new Timer(delaySecs, e -> {
                            trayIcon.displayMessage("Timer expired!", finishMessage, TrayIcon.MessageType.INFO);
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
    private Image createImage(Dimension trayIconSize) {
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

    @Override
    public void run() {
        try {
            createAndShowGUI();
        } catch (AWTException e) {
            System.err.println("Error creating UI: " + e.getMessage());
            System.exit(1);
        }
    }
}
