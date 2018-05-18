package com.tek.snip.objects;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import com.jfoenix.controls.JFXButton;
import com.tek.snip.man.FileManager;
import com.tek.snip.ui.SettingsController;

public class Hotkey {
	
	private JFXButton button;
	private Field field;
	
	private boolean setting = false;
	
	public Hotkey(JFXButton button, Class<?> clazz, String field) {
		try{
			this.field = clazz.getField(field);
			this.button = button;
		}catch(Exception e) { }
	}
	
	public void init() {
		updateText();
		
		button.setOnAction(e -> {
			if(setting) return;
			setting = true;
			button.setText("???");
		});
	}
	
	public void updateText() {
		button.setText(KeyEvent.getKeyText(getFieldValue()));
	}
	
	public void keyPress(int vk) {
		if(setting) {
			setFieldValue(vk);
			updateText();
			setting = false;
		}
	}
	
	public int getFieldValue() {
		try {
			return (int) field.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return 0;
		}
	}
	
	public void setFieldValue(int vk) {
		try {
			field.set(null, vk);
		} catch (IllegalArgumentException | IllegalAccessException e) { }
		
		FileManager.getInstance().saveHotkeys();
	}
	
	public boolean isSetting() {
		return setting;
	}
	
	public boolean canSet() {
		for(Hotkey hotkey : SettingsController.getInstance().hotkeys) {
			if(hotkey.isSetting()) return false;
		}
		
		return true;
	}
	
	public JFXButton getButton() {
		return button;
	}
	
	public Field getField() {
		return field;
	}
	
}
