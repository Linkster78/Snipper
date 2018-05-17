package com.tek.snip.util;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tek.snip.objects.ImageSelection;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Util {
	
	public static void toClipboard(BufferedImage image) {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		c.setContents(new ImageSelection(image), null);
	}
	
	public static void toClipboard(String str) {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		c.setContents(new StringSelection(str), null);
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
	
	public static Popup createPopup(final String message) {
	    final Popup popup = new Popup();
	    popup.setAutoFix(true);
	    popup.setAutoHide(true);
	    popup.setHideOnEscape(true);
	    Label label = new Label(message);
	    label.getStylesheets().add("/res/Dark.css");
	    label.getStyleClass().add("popup");
	    popup.getContent().add(label);
	    return popup;
	}

	public static void showPopupMessage(final String message, final Stage stage) {
	    final Popup popup = createPopup(message);
	    popup.setOnShown(new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent e) {
	            popup.setX(stage.getX() + stage.getWidth() - popup.getWidth() - 2);
	            popup.setY(stage.getY() + stage.getHeight() - popup.getHeight() - 2);
	            popup.setOpacity(0);
	            
	            Timeline timeline = new Timeline();
	    		timeline.setCycleCount(1);
	    		
	    		KeyValue opacityInk = new KeyValue(popup.opacityProperty(), 1);
	    		KeyValue opacityOutk = new KeyValue(popup.opacityProperty(), 0);
	    		KeyFrame opacityIn = new KeyFrame(Duration.millis(250), opacityInk);
	    		KeyFrame opacityOut = new KeyFrame(Duration.millis(250), opacityOutk);
	    		KeyFrame hide = new KeyFrame(Duration.millis(250), h -> {
	    			popup.hide();
	    		});
	    		KeyFrame triggerOut = new KeyFrame(Duration.millis(1750), e1 -> {
	    			Timeline t = new Timeline();
	    			t.setCycleCount(1);
	    			t.getKeyFrames().addAll(opacityOut, hide);
	    			t.play();
	    		});
	    		
	    		timeline.getKeyFrames().addAll(opacityIn, triggerOut);
	    		
	    		timeline.play();
	        }
	    });
	    popup.show(stage);
	}
	
}
