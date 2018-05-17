package com.tek.snip.objects;

import java.awt.image.BufferedImage;

public class Snip {
	
	private BufferedImage image;
	
	public Snip(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
}
