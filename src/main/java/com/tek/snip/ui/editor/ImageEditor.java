package com.tek.snip.ui.editor;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.jfoenix.controls.JFXColorPicker;
import com.tek.snip.ui.GUI;
import com.tek.snip.util.Util;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ImageEditor {
	
	public static BufferedImage edit(BufferedImage from) {
		defImage = from;
		
		Stage stage = new Stage();
		stage.setTitle("Edit Image");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.getIcons().add(new Image("/res/icon.png"));
		stage.setResizable(false);
		
		stage.setScene(GUI.getInstance().getFXMLEditor());
		
		stage.showAndWait();
		
		return SwingFXUtils.fromFXImage(instance.image.getImage(), null);
	}
	
	private static BufferedImage defImage;
	private static ImageEditor instance;
	
	public HashMap<Button, Tool> buttons = new HashMap<Button, Tool>();
	public Tool currentTool = Tool.RECT;
	public Point base;
	
	public BufferedImage current;
	
	@FXML
	public ImageView image;
	
	@FXML
	public HBox toolbar;
	
	public JFXColorPicker colorPicker;
	
	@FXML
	public void initialize() {
		instance = this;
		
		toolbar.setAlignment(Pos.CENTER);
		
		Image rectImage = new Image("/res/icon_rectangle.png");
		Image frectImage = new Image("/res/icon_frectangle.png");
		Image ovalImage = new Image("/res/icon_oval.png");
		Image fovalImage = new Image("/res/icon_foval.png");
		Image lineImage = new Image("/res/icon_line.png");
		Image drawImage = new Image("/res/icon_draw.png");
		
		Button rectBtn = new Button("", new ImageView(rectImage));
		Button frectBtn = new Button("", new ImageView(frectImage));
		Button ovalBtn = new Button("", new ImageView(ovalImage));
		Button fovalBtn = new Button("", new ImageView(fovalImage));
		Button lineBtn = new Button("", new ImageView(lineImage));
		Button drawBtn = new Button("", new ImageView(drawImage));
		
		buttons.put(rectBtn, Tool.RECT);
		buttons.put(frectBtn, Tool.FRECT);
		buttons.put(ovalBtn, Tool.OVAL);
		buttons.put(fovalBtn, Tool.FOVAL);
		buttons.put(lineBtn, Tool.LINE);
		buttons.put(drawBtn, Tool.DRAW);
		
		processTools(rectBtn, frectBtn, ovalBtn, fovalBtn, lineBtn, drawBtn);
		
		this.colorPicker = new JFXColorPicker();
		this.colorPicker.setStyle("-fx-font-size: 13px;");
		HBox.setMargin(this.colorPicker, new Insets(0, 10, 0, 0));
		this.toolbar.getChildren().add(this.colorPicker);
		
		current = defImage;
		
		image.setImage(SwingFXUtils.toFXImage(defImage, null));
		image.setPreserveRatio(true);
		
		image.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if(e.getX() >= 0 && e.getX() <= image.getLayoutBounds().getWidth() && e.getY() >= 0 && e.getY() <= image.getLayoutBounds().getHeight()) {
				base = mapPoint(new Point((int)e.getX(), (int)e.getY()));
			}else {
				base = null;
			}
		});
		
		image.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			if(base != null) {
				Point loc = mapPoint(new Point((int)e.getX(), (int)e.getY()));
				
				BufferedImage imgOver = Util.deepCopy(current);
				
				Color color = colorPicker.getValue();
				
				java.awt.Color c = new java.awt.Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getOpacity());
				
				Graphics g = imgOver.getGraphics();
				g.setColor(c);
				
				int x = Math.min(loc.x, base.x);
				int y = Math.min(loc.y, base.y);
				int w = Math.abs(loc.x - base.x);
				int h = Math.abs(loc.y - base.y);
				
				switch(currentTool) {
				case RECT:
					g.drawRect(x, y, w, h);
					break;
				case FRECT:
					g.fillRect(x, y, w, h);
					break;
				case OVAL:
					g.drawOval(x, y, w, h);
					break;
				case FOVAL:
					g.fillOval(x, y, w, h);
					break;
				case LINE:
					g.drawLine(base.x, base.y, loc.x, loc.y);
					break;
				case DRAW:
					g.drawLine(base.x, base.y, loc.x, loc.y);
					
					base = loc;
					current = Util.deepCopy(imgOver);
					break;
				default:
					break;
				}
				
				this.image.setImage(SwingFXUtils.toFXImage(imgOver, null));
			}
		});
		
		image.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			if(base == null) return;
			
			Point loc = mapPoint(new Point((int)e.getX(), (int)e.getY()));
			
			Color color = this.colorPicker.getValue();
				
			Graphics g = current.getGraphics();
			java.awt.Color c = new java.awt.Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getOpacity());
			g.setColor(c);
				
			int x = Math.min(loc.x, base.x);
			int y = Math.min(loc.y, base.y);
			int w = Math.abs(loc.x - base.x);
			int h = Math.abs(loc.y - base.y);
			
			switch(currentTool) {
			case RECT:
				g.drawRect(x, y, w, h);
				break;
			case FRECT:
				g.fillRect(x, y, w, h);
				break;
			case OVAL:
				g.drawOval(x, y, w, h);
				break;
			case FOVAL:
				g.fillOval(x, y, w, h);
				break;
			case LINE:
				g.drawLine(base.x, base.y, loc.x, loc.y);
				break;
			default:
				break;
			}
			
			updateImage();
			
			base = null;
		});
		
		updateTool();
	}
	
	public Point mapPoint(Point point) {
		return new Point((int)(point.x / image.getLayoutBounds().getWidth() * defImage.getWidth()), (int)(point.y / image.getLayoutBounds().getHeight() * defImage.getHeight()));
	}
	
	public void updateImage() {
		this.image.setImage(SwingFXUtils.toFXImage(current, null));
	}
	
	public void processTools(Button... btns) {
		for(Button button : btns) {
			button.setOnAction(e -> {
				currentTool = buttons.get(button);
				
				updateTool();
			});
			
			toolbar.getChildren().add(button);
		}
	}
	
	public void updateTool() {
		for(Button button : buttons.keySet()) {
			if(currentTool == buttons.get(button)) {
				button.setOpacity(0.5);
			}else {
				button.setOpacity(1);
			}
		}
	}
	
}
