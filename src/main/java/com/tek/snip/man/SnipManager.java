package com.tek.snip.man;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.tek.snip.objects.Snip;
import com.tek.snip.ui.MainController;

import javafx.application.Platform;

public class SnipManager {
	
	private static SnipManager instance;
	
	private ArrayList<Snip> loaded = new ArrayList<Snip>();
	
	public SnipManager() {
		instance = this;
	}
	
	public void add(BufferedImage image) {
		loaded.add(new Snip(image));
		
		Platform.runLater(() -> {
			MainController.getInstance().updateSnips();
		});
	}
	
	public void clear() {
		loaded.clear();
		
		Platform.runLater(() -> {
			MainController.getInstance().updateSnips();
		});
	}
	
	public ArrayList<Snip> getLoaded() {
		return loaded;
	}
	
	public static SnipManager getInstance() {
		return instance;
	}
	
}
