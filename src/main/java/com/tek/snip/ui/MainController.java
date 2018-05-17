package com.tek.snip.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.tek.snip.man.CloudManager;
import com.tek.snip.man.FileManager;
import com.tek.snip.man.SnipManager;
import com.tek.snip.objects.Snip;
import com.tek.snip.util.Util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController {
	
	private static MainController instance;
	private ImageView selected;
	
	@FXML
	private MenuItem itemSave;
	
    @FXML
    private MenuItem itemSaveAll;
    
    @FXML
	private MenuItem itemUpload;
	
    @FXML
    private MenuItem itemClear;

    @FXML
    private VBox paneContent;
    
    @FXML
    private ScrollPane paneScroll;
	
	@FXML
	public void initialize() {
		instance = this;
		
		paneContent.setAlignment(Pos.TOP_LEFT);
		
		paneScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		paneScroll.addEventFilter(ScrollEvent.SCROLL, e -> {
			if(e.getDeltaX() != 0) {
				e.consume();
			}
		});
		
		itemSave.setOnAction(e -> {
			FileManager.getInstance().save();
		});
		
		itemSaveAll.setOnAction(e -> {
			FileManager.getInstance().saveAll();
		});
		
		itemUpload.setOnAction(e -> {
			CloudManager.getInstance().upload();
		});
		
		itemClear.setOnAction(e -> {
			SnipManager.getInstance().clear();
			selected = null;
		});
		
		updateSnips();
	}
	
	public ArrayList<ImageView> images = new ArrayList<ImageView>();
	
	public ImageView process(BufferedImage image) {
		ImageView view = new ImageView(SwingFXUtils.toFXImage(image, null));
		view.setFitWidth(140);
		view.setFitHeight(140);
		HBox.setMargin(view, new Insets(0, 10, 0, 0));
		
		view.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			Util.toClipboard(image);
			
			Util.showPopupMessage("Copied to your clipboard", GUI.getInstance().getWindow());
			
			view.setOpacity(0.25);
			
			for(ImageView img : this.images) {
				if(img != view) {
					Timeline timeline = new Timeline();
					timeline.setCycleCount(1);
					timeline.setAutoReverse(false);
					KeyValue kv = new KeyValue(img.rotateProperty(), 0);
					KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
					timeline.getKeyFrames().add(kf);
					timeline.play();
				}
			}
			
			Timeline timeline = new Timeline();
			timeline.setCycleCount(1);
			timeline.setAutoReverse(false);
			KeyValue kv = new KeyValue(view.opacityProperty(), 1);
			KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
			KeyValue kv1 = new KeyValue(view.rotateProperty(), 3);
			KeyFrame kf1 = new KeyFrame(Duration.millis(250), kv1);
			timeline.getKeyFrames().addAll(kf, kf1);
			timeline.play();
			
			selected = view;
		});
		
		return view;
	}
	
	public void updateSnips() {
		images.clear();
		paneContent.getChildren().clear();
		
		Label label = new Label("Taken Snips (" + SnipManager.getInstance().getLoaded().size() + ")");
		label.setStyle("-fx-font-size: 24px;");
		VBox.setMargin(label, new Insets(10, 10, 10, 10));
		paneContent.getChildren().add(label);
		
		HBox hbox = null;
		int i1 = 0;
		int i = 0;
		for(Snip snip : SnipManager.getInstance().getLoaded()) {
			if(i == 0) {
				hbox = new HBox();
				VBox.setMargin(hbox, new Insets(0, 0, 10, 0));
			}
			
			ImageView image = process(snip.getImage());
			
			hbox.getChildren().add(image);
			images.add(image);
			
			i++;
			i1++;
			
			if(i1 == SnipManager.getInstance().getLoaded().size()) {
				paneContent.getChildren().add(hbox);
				break;
			}
			
			if(i == 4) {
				paneContent.getChildren().add(hbox);
				i = 0;
			}
		}
	}
	
	public ImageView getSelected() {
		return selected;
	}
	
	public static MainController getInstance() {
		return instance;
	}
}