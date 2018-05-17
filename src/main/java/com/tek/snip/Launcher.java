package com.tek.snip;

import java.awt.event.KeyEvent;

import com.tek.snip.man.CloudManager;
import com.tek.snip.man.FileManager;
import com.tek.snip.man.KeyManager;
import com.tek.snip.man.SnipManager;
import com.tek.snip.overlay.Overlay;
import com.tek.snip.ui.GUI;

import javafx.application.Platform;

public class Launcher {
	
	public void start() {
		//Launch global managers
		new KeyManager();
		new SnipManager();
		new FileManager();
		new CloudManager();
		
		//Open snipper overlay
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_7, () -> {
			Overlay.open();
		});
		
		//Close snipper overlay
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_8, () -> {
			Overlay.close();
		});
		
		//GUI hotkey
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_9, () -> {
			Platform.runLater(() -> {
				GUI.getInstance().getWindow().show();
			});
		});
		
		//Initialize GUI
		Runnable r = () -> {
			GUI.main(new String[0]);
		};
		new Thread(r).start();
	}
	
	public void stop() {
		KeyManager.getInstance().close();
		GUI.getInstance().close();
		Overlay.close();
	}
	
	public static void main(String[] args) {
		new Launcher().start();
	}
	
}
