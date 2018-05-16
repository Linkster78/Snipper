package com.tek.snip.overlay;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Window;

import javax.swing.SwingUtilities;

import com.tek.snip.util.Reference;

@SuppressWarnings("deprecation")
public class Overlay extends Window{

	public static Overlay instance;
	
	public static void open() {
		if(instance != null) return;
		
		SwingUtilities.invokeLater(() -> {
			instance = new Overlay();
		});
	}
	
	public static void close() {
		if(instance == null) return;
		
		instance.disable();
		instance.setVisible(false);
		instance.dispose();
		instance = null;
	}
	
	//Actual class code
	
	private static final long serialVersionUID = 1L;

	private Point origin;
	
	public Overlay() {
		super(null);
		
		setAlwaysOnTop(true);
		setBounds(getGraphicsConfiguration().getBounds());
		setBackground(new Color(0, true));
		setVisible(true);
		
		setIgnoreRepaint(false);
	}
	
	@Override
	public boolean mouseDown(Event evt, int x, int y) {
		origin = new Point(x, y);
		return super.mouseDown(evt, x, y);
	}
	
	@Override
	public boolean mouseUp(Event evt, int x, int y) {
		origin = null;
		return super.mouseUp(evt, x, y);
	}
	
	@Override
	public boolean mouseMove(Event evt, int x, int y) {
		return super.mouseMove(evt, x, y);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Reference.DARK_OVERLAY);
		g.fillRect(0, 0, 500, 500);
		if(origin == null) return;
		g.setColor(Reference.LIGHT_OVERLAY);
		g.clearRect(origin.x, origin.y, this.getMousePosition().x, this.getMousePosition().y);
		g.fillRect(origin.x, origin.y, this.getMousePosition().x, this.getMousePosition().y);
	}
	
	@Override
	public void update(Graphics g) {
		super.update(g);
		paint(g);
	}

}
