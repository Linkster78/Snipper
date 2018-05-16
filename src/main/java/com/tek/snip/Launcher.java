package com.tek.snip;

import java.awt.event.KeyEvent;

import com.tek.snip.man.KeyManager;
import com.tek.snip.overlay.Overlay;

public class Launcher {
	
	public void start() {
		//Launch global managers
		new KeyManager();
		
		//Open snipping overlay
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_ALT, () -> {
			Overlay.open();
		});
		
		//Close program as a whole
		KeyManager.getInstance().addKeyBind(KeyEvent.VK_ESCAPE, () -> {
			KeyManager.getInstance().close();
			Overlay.close();
		});
	}
	
	public static void main(String[] args) {
		new Launcher().start();
	}
	
}
