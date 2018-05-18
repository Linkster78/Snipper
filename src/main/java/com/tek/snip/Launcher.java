package com.tek.snip;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import com.tek.snip.man.CloudManager;
import com.tek.snip.man.FileManager;
import com.tek.snip.man.KeyManager;
import com.tek.snip.man.SnipManager;
import com.tek.snip.overlay.Overlay;
import com.tek.snip.ui.GUI;
import com.tek.snip.util.Reference;
import com.tek.snip.util.Util;

import javafx.application.Platform;

public class Launcher {
	
	KeyManager keyMan;
	SnipManager snipMan;
	FileManager fileMan;
	CloudManager cloudMan;
	
	public void start() {
		//Launch global managers
		keyMan = new KeyManager();
		snipMan = new SnipManager();
		fileMan = new FileManager();
		cloudMan = new CloudManager();
		
		//Load hotkeys from settings
		FileManager.getInstance().loadHotkeys();
		
		//Register keybinds
		KeyManager.getInstance().addKeyHandler(i -> {
			if(i == Reference.HOTKEY_SNIP) {
				Overlay.open();
			}else if(i == Reference.HOTKEY_GUI) {
				Platform.runLater(() -> {
					GUI.getInstance().getWindow().show();
				});
			}
		});
		
		//Initialize Tray Icon
		if(SystemTray.isSupported()) {
			PopupMenu popupmenu = new PopupMenu("Options");
			
			MenuItem snip = new MenuItem("Snip");
			snip.setActionCommand("snip");
			
			MenuItem gui = new MenuItem("GUI");
			gui.setActionCommand("gui");
			
			MenuItem exit = new MenuItem("Exit");
			exit.setActionCommand("exit");
			
			snip.addActionListener(l -> {
				Overlay.open();
			});
			
			gui.addActionListener(l -> {
				Platform.runLater(() -> {
					GUI.getInstance().getWindow().show();
				});
			});
			
			exit.addActionListener(l -> {
				Launcher.stop();
			});
			
			popupmenu.add(snip);
			popupmenu.add(gui);
			popupmenu.add(exit);
			
			SystemTray tray = SystemTray.getSystemTray();
			TrayIcon trayIcon = new TrayIcon(Util.fromPath("/res/icon_small.png"), "Snipper", popupmenu);
			trayIcon.setImageAutoSize(true);
			
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) { e1.printStackTrace(); }
		}
		
		//Initialize GUI
		Runnable r = () -> {
			GUI.main(new String[0]);
		};
		new Thread(r).start();
	}
	
	public static void stop() {
		KeyManager.getInstance().close();
		
		Platform.runLater(() -> {
			try {
				GUI.getInstance().stop();
				System.exit(-1);
			} catch (Exception e) { }
		});
		
		Overlay.close();
	}
	
	public static void main(String[] args) {
		new Launcher().start();
	}
	
}
