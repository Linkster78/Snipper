package com.tek.snip.man;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.tek.snip.objects.Snip;

public class SnipManager {
	
	private static SnipManager instance;
	
	private ArrayList<Snip> loaded = new ArrayList<Snip>();
	
	public SnipManager() {
		instance = this;
	}
	
	public void add(BufferedImage image) {
		loaded.add(new Snip(image));
	}
	
	public ArrayList<Snip> getLoaded() {
		return loaded;
	}
	
	public static SnipManager getInstance() {
		return instance;
	}
	
}
