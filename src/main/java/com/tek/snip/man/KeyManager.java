package com.tek.snip.man;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.jna.Native;
import com.tek.snip.callback.Callback;
import com.tek.snip.jna.User32;

public class KeyManager {
	
	private static KeyManager instance;
	
	private User32 user32 = (User32)Native.loadLibrary("user32", User32.class);
	private HashMap<Integer, Runnable> keybinds = new HashMap<Integer, Runnable>();
	private HashMap<Integer, Boolean> keystates = new HashMap<Integer, Boolean>();
	private ArrayList<Callback<Integer>> keyhandlers = new ArrayList<Callback<Integer>>();
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
						//PRESS
						if(keybinds.containsKey(i)) {
							keybinds.get(i).run();
						}
						
						for(Callback<Integer> c : keyhandlers) {
							c.call(i);
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
	
	public void addKeyBind(int vk, Runnable r) {
		keybinds.put(vk, r);
	}
	
	public void addKeyHandler(Callback<Integer> callback) {
		keyhandlers.add(callback);
	}
	
	public HashMap<Integer, Runnable> getKeybinds() {
		return keybinds;
	}
	
	public HashMap<Integer, Boolean> getKeystates() {
		return keystates;
	}
	
	public ArrayList<Callback<Integer>> getKeyhandlers() {
		return keyhandlers;
	}
	
	public boolean getKey(int vk) {
		int i = user32.GetKeyState(vk) & 0xffff;
		return i >= 100;
	}
	
	public static KeyManager getInstance() {
		return instance;
	}
	
}
