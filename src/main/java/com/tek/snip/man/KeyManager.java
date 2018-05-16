package com.tek.snip.man;

import java.util.HashMap;

import com.sun.jna.Native;
import com.tek.snip.callback.Callback;
import com.tek.snip.jna.User32;

public class KeyManager {
	
	private static KeyManager instance;
	
	private User32 user32 = (User32)Native.loadLibrary("user32", User32.class);
	private HashMap<Integer, Callback> keybinds = new HashMap<Integer, Callback>();
	private HashMap<Integer, Boolean> keystates = new HashMap<Integer, Boolean>();
	private volatile boolean closed = false;
	
	public KeyManager() {
		instance = this;
		
		initThread();
	}
	
	public void initThread() {
		for(int i = 0; i < 256; i++) {
			keystates.put(i, false);
		}
		
		Runnable r = () -> {
			while(true) {
				if(closed) return;
				
				for(int i = 0; i < 256; i++) {
					boolean oldState = keystates.get(i);
					boolean newState = getKey(i);
					
					if(oldState != newState && newState) {
						if(keybinds.containsKey(i)) {
							keybinds.get(i).call();
						}
					}
					
					keystates.put(i, newState);
				}
			}
		};
		
		new Thread(r).start();
	}
	
	public void close() {
		closed = true;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void addKeyBind(int vk, Callback callback) {
		keybinds.put(vk, callback);
	}
	
	public boolean getKey(int vk) {
		int i = user32.GetKeyState(vk) & 0xffff;
		return i >= 100;
	}
	
	public static KeyManager getInstance() {
		return instance;
	}
	
}
