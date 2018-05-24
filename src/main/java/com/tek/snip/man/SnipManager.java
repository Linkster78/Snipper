package com.tek.snip.man;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.tek.snip.objects.Snip;
import com.tek.snip.ui.MainController;
import com.tek.snip.util.Util;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

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
	
	public void remove(Image image) {
		for(Snip snip : new ArrayList<Snip>(loaded)) {
			Image img = SwingFXUtils.toFXImage(snip.getImage(), null);
			
			if(Util.equalsImage(img, image)) {
				loaded.remove(snip);
			}
		}
		
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
	
	public Snip getSnipById(double id) {
		for(Snip snip : loaded) {
			if(snip.getId() == id) return snip;
		}
		
		return null;
	}
	
	public ArrayList<Snip> getLoaded() {
		return loaded;
	}
	
	public static SnipManager getInstance() {
		return instance;
	}
	
}
