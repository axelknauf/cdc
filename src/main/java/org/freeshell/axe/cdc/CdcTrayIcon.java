package org.freeshell.axe.cdc;

import javax.swing.*;
import java.awt.*;

/**
 * UI class implementing the (runnable) System tray icon class.
 * FIXME: extract action listeners, make configurable etc.
 */
public class CdcTrayIcon implements Runnable, ConfigurationConstants {

    // Initial config
    private Configuration config = null;

    // Dependent fields
    private ImageProvider imageProvider = null;

    // UI elements
    private TrayIcon trayIcon = null;

    /**
     * Initializes a new instance of this class with the given Configuration.
     *
     * @param cfg the {@link Configuration} for this icon
     */
    public CdcTrayIcon(Configuration cfg) {
        this.config = cfg;
        initConfig();
    }

    private void initConfig() {
        try {
            String imageProviderClassName = config.getString(ConfigurationConstants.KEY_IMAGE_PROVIDER);
            @SuppressWarnings("unchecked") Class<ImageProvider> imageProviderClass = (Class<ImageProvider>) Class.forName(imageProviderClassName);
            imageProvider = imageProviderClass.newInstance();
        } catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            // FIXME: use fallback instead of crashing
            System.err.println("Cannot instantiate ImageProvider class: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private void createAndShowGUI() throws AWTException {
        final SystemTray tray = SystemTray.getSystemTray();

        Dimension trayIconSize = tray.getTrayIconSize();

        // Create inner items
        final PopupMenu popup = new PopupMenu();
        this.trayIcon = new TrayIcon(imageProvider.createImage(trayIconSize));

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
            SwingWorker worker = createTimeoutWorker(3000, config.getString(KEY_TIMEOUT_MESSAGE), 0);
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
