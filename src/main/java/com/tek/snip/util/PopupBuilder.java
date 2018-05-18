package com.tek.snip.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PopupBuilder {
	
	public static void showPopup(String title, String message) {
		Stage stage = new Stage();
		stage.setAlwaysOnTop(true);
		stage.setOpacity(0.75);
		stage.initStyle(StageStyle.UNDECORATED);
		
		int width = getWidth(message) + 40;
		int height = 60;
		
		Label lbltitle = new Label(title);
		lbltitle.setStyle("-fx-font-size: 20px;");
		Label lbltext = new Label(message);
		
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		VBox.setMargin(lbltitle, new Insets(10, 10, 0, 10));
		VBox.setMargin(lbltext, new Insets(0, 10, 10, 10));
		
		root.getStylesheets().add("/res/Dark.css");
		root.getChildren().addAll(lbltitle, lbltext);
		Scene scene = new Scene(root, width, height);
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		
		stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - width);
		stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - height);
			
		stage.setScene(scene);
		stage.setOpacity(0);
		stage.show();
		
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		
		KeyValue opacityInk = new KeyValue(stage.opacityProperty(), 1);
		KeyValue opacityOutk = new KeyValue(stage.opacityProperty(), 0);
		KeyFrame opacityIn = new KeyFrame(Duration.millis(250), opacityInk);
		KeyFrame opacityOut = new KeyFrame(Duration.millis(250), opacityOutk);
		KeyFrame hide = new KeyFrame(Duration.millis(250), h -> {
			stage.hide();
			stage.close();
		});
		KeyFrame triggerOut = new KeyFrame(Duration.millis(1250), e -> {
			Timeline t = new Timeline();
			t.setCycleCount(1);
			t.getKeyFrames().addAll(opacityOut, hide);
			t.play();
		});
		
		timeline.getKeyFrames().addAll(opacityIn, triggerOut);
		
		timeline.play();
	}
	
	@SuppressWarnings("unused")
	private static int getWidth(String txt) {
		Group root = new Group();
		root.getStylesheets().add("/res/Dark.css");
	    Label label = new Label(txt);
	    root.getChildren().add(label);
	    Scene scene = new Scene(root);

	    root.applyCss();
	    root.layout();
	    
	    return (int) label.getWidth();
	}
	
}
