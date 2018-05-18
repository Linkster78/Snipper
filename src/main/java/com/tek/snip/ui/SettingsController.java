package com.tek.snip.ui;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.tek.snip.man.KeyManager;
import com.tek.snip.objects.Hotkey;
import com.tek.snip.util.Reference;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class SettingsController {
	
	private static SettingsController instance;
	
	@FXML
	private JFXButton btnSnip;
	
	@FXML
	private JFXButton btnGui;
	
	@FXML
	private JFXButton btnBack;
	
	public ArrayList<Hotkey> hotkeys = new ArrayList<Hotkey>();
	
	@FXML
	private void initialize() {
		instance = this;
		
		hotkeys.clear();
		
		btnBack.setOnAction(e -> {
			GUI.getInstance().getWindow().setScene(GUI.getInstance().getFXML());
		});
		
		registerHotkey(btnSnip, Reference.class, "HOTKEY_SNIP");
		registerHotkey(btnGui, Reference.class, "HOTKEY_GUI");
		
		KeyManager.getInstance().addKeyHandler(vk -> {
			hotkeys.forEach(c -> {
				Platform.runLater(() -> {
					if(c.isSetting()) c.keyPress(vk);
				});
			});
		});
	}
	
	public void registerHotkey(JFXButton button, Class<?> clazz, String field) {
		Hotkey hotkey = new Hotkey(button, clazz, field);
		hotkey.init();
		hotkeys.add(hotkey);
	}
	
	public static SettingsController getInstance() {
		return instance;
	}
	
}
