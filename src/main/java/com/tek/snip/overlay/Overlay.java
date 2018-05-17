package com.tek.snip.overlay;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.tek.snip.man.SnipManager;
import com.tek.snip.util.PopupBuilder;
import com.tek.snip.util.Util;

import javafx.application.Platform;

@SuppressWarnings({ "deprecation", "serial" })
public class Overlay extends JFrame implements MouseListener, MouseMotionListener{

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

	private OverlayComponent comp;
	private Point origin;
	
	public Overlay() {
		setAlwaysOnTop(true);
		setBounds(getGraphicsConfiguration().getBounds());
		setUndecorated(true);
		setBackground(new Color(0, true));
		pack();
		setVisible(true);
		
		setIgnoreRepaint(false);
		
		GraphicsDevice gd = Util.currentDevice();
		setLocation(gd.getDefaultConfiguration().getBounds().x, getY());
		setSize(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
		
		comp = new OverlayComponent();
		add(comp);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) { 
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		origin = new Point(e.getX(), e.getY());
		comp.setOrigin(origin);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point n = this.getMousePosition();
		int x = (int)(Math.min(n.getX(), origin.getX()) + getLocation().getX());
		int y = (int) Math.min(n.getY(), origin.getY());
		int w = (int) Math.abs(n.getX() - origin.getX());
		int h = (int) Math.abs(n.getY() - origin.getY());
		Rectangle r = new Rectangle(x, y, w, h);
		
		this.setVisible(false);
		
		BufferedImage screenshot = Util.takeScreenshot(r);
		
		if(screenshot != null) {
			Platform.runLater(() -> {
				PopupBuilder.showPopup("Snip!", "Your screenshot has been copied to your clipboard");
			});
			
			Util.toClipboard(screenshot);
			SnipManager.getInstance().add(screenshot);
			Overlay.close();
			return;
		}
		
		this.setVisible(true);
		
		origin = null;
		comp.setOrigin(null);
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {
		if(!getBounds().contains(e.getLocationOnScreen())) {
			Overlay.close();
		}
	}

}
