package com.tek.snip.util;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tek.snip.objects.ImageSelection;

public class Util {
	
	public static void toClipboard(BufferedImage image) {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		c.setContents(new ImageSelection(image), null);
	}
	
	public static GraphicsDevice currentDevice() {
		return MouseInfo.getPointerInfo().getDevice();
	}
	
	public static GraphicsDevice deviceByID(String id) {
		for(GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			if(gd.getIDstring().equalsIgnoreCase(id)) return gd;
		}
		
		return null;
	}
	
	public static BufferedImage takeScreenshot(Rectangle area) {
		if(area.getWidth() <= 0) return null;
		if(area.getHeight() <= 0) return null;
		
		try {
			return new Robot().createScreenCapture(area);
		} catch (AWTException e) {
			return null;
		}
	}
	
	public static BufferedImage fromPath(String path) {
		try {
			return ImageIO.read(Util.class.getResourceAsStream(path));
		} catch (IOException e) { e.printStackTrace(); }
		return null;
	}
	
}
