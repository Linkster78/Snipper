package com.tek.snip;

import java.awt.event.KeyEvent;

import com.tek.snip.man.CloudManager;
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
		new CloudManager();
		
		//Open snipper overlay
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_NUMPAD7, () -> {
			Overlay.open();
		});
		
		//Close snipper overlay
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_NUMPAD8, () -> {
			Overlay.close();
		});
		
		//GUI hotkey
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_NUMPAD9, () -> {
			Platform.runLater(() -> {
				GUI.getInstance().getWindow().show();
			});
		});
		
		//Initialize GUI
		GUI.main(new String[0]);
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
