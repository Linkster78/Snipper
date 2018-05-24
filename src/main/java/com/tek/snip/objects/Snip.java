package com.tek.snip.objects;

import java.awt.image.BufferedImage;

public class Snip {
	
	private BufferedImage image;
	private double id;
	
	public Snip(BufferedImage image) {
		this.image = image;
		this.id = Math.random();
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public double getId() {
		return id;
	}
	
}
