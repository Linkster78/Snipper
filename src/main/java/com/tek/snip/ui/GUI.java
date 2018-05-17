package com.tek.snip.ui;

import java.io.IOException;

import com.tek.snip.util.Util;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUI extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	private static GUI instance;
	
	private Stage window;
	
	@Override
	public void start(Stage window) throws Exception {
		instance = this;
		this.window = window;
		
		window.getIcons().add(SwingFXUtils.toFXImage(Util.fromPath("/res/icon.png"), null));
		window.setTitle("Snipper");
		window.setResizable(false);
		
		Platform.setImplicitExit(false);
		
		window.setOnCloseRequest(e -> {
			e.consume();
			window.hide();
		});
		
		window.setScene(getFXML());
	}
	
	public Scene getFXML() {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("MainScene.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            
           return new Scene(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
	}
	
	public Stage getWindow() {
		return window;
	}
	
	public void close() {
		window.close();
	}
	
	public static GUI getInstance() {
		return instance;
	}
	
}
