package com.tek.snip.overlay;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

import com.tek.snip.util.Reference;

@SuppressWarnings({"serial"})
public class OverlayComponent extends JComponent{
	
	private Point origin;
	
	public void setOrigin(Point origin) {
		this.origin = origin;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Reference.DARK_OVERLAY);
		g.fillRect(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		if(origin == null) return;
		
		Point n = this.getMousePosition();
		int x = (int) Math.min(n.getX(), origin.getX());
		int y = (int) Math.min(n.getY(), origin.getY());
		int w = (int) Math.abs(n.getX() - origin.getX());
		int h = (int) Math.abs(n.getY() - origin.getY());
		
		g.setColor(Reference.LIGHT_OVERLAY);
		g.clearRect(x, y, w, h);
		g.fillRect(x, y, w, h);
	}
	
}
