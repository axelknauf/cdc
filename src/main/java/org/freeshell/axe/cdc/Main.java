package org.freeshell.axe.cdc;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class Main {

  public static void main(String[] argv) {
    if (!SystemTray.isSupported()) {
      System.err.println("SystemTray is not supported, exiting.");
      System.exit(1);
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  private static void createAndShowGUI() {
    final PopupMenu popup = new PopupMenu();
    // FIXME: replace with own icon
    final TrayIcon trayIcon =
      new TrayIcon(createImage("/images/bulb.gif", "tray icon"));
    final SystemTray tray = SystemTray.getSystemTray();

    MenuItem exitItem = new MenuItem("Exit");
    popup.add(exitItem);
    trayIcon.setPopupMenu(popup);

    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
      System.err.println("TrayIcon could not be added.");
      return;
    }
    trayIcon.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,
            "This dialog box is run from System Tray");
      }
    });

    exitItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tray.remove(trayIcon);
        System.exit(0);
      }
    });
  }

  protected static Image createImage(String path, String description) {
    URL imageURL = Main.class.getResource(path);

    if (imageURL == null) {
      System.err.println("Resource not found: " + path);
      return null;
    } else {
      return (new ImageIcon(imageURL, description)).getImage();
    }
  }

}
