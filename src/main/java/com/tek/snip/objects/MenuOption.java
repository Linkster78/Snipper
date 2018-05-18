package com.tek.snip.objects;

public class MenuOption {
	
	private String text;
	private Runnable r;
	
	public MenuOption(String text, Runnable r) {
		this.text = text;
		this.r = r;
	}
	
	public String getText() {
		return text;
	}
	
	public Runnable getRunnable() {
		return r;
	}
	
}
