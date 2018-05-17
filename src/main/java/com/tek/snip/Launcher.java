package com.tek.snip;

import java.awt.event.KeyEvent;

import com.tek.snip.man.CloudManager;
import com.tek.snip.man.KeyManager;
import com.tek.snip.man.SnipManager;
import com.tek.snip.overlay.Overlay;

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
		
		//Program Kill-Switch (Incase something goes wrong)
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_NUMPAD9, () -> {
			Overlay.close();
			KeyManager.getInstance().close();
			System.exit(-1);
		});
	}
	
	public static void main(String[] args) {
		new Launcher().start();
	}
	
}
