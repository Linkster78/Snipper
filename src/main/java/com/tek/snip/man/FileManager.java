package com.tek.snip.man;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.tek.snip.objects.Snip;
import com.tek.snip.ui.GUI;
import com.tek.snip.util.PopupBuilder;
import com.tek.snip.util.Reference;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileManager {
	
	private static FileManager instance;
	
	public FileManager() {
		instance = this;
	}
	
	public void save(ImageView img) {
		BufferedImage toSave = SwingFXUtils.fromFXImage(img.getImage(), null);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Images (*.png)", "png"));
		File sfile = fileChooser.showSaveDialog(GUI.getInstance().getWindow());
		
		if(sfile != null) {
			String selectedFile = sfile.getPath();
			if(!selectedFile.endsWith(".png")) selectedFile += ".png";
			File file = new File(selectedFile);
			
			try {
				ImageIO.write(toSave, "png", file);
				PopupBuilder.showPopup("Snip!", "Saved snip to " + file.getPath());
			} catch (IOException e) { 
				e.printStackTrace();
				PopupBuilder.showPopup("Error", "Internal Error when saving");
			}
		}else {
			PopupBuilder.showPopup("Error", "No file selected");
		}
	}
	
	public void saveAll() {
		if(SnipManager.getInstance().getLoaded().isEmpty()) {
			PopupBuilder.showPopup("Error", "No snips to save");
			return;
		}
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(GUI.getInstance().getWindow());
        
        if(selectedDirectory != null) {
        	TextInputDialog dialog = new TextInputDialog("snip");
        	dialog.setTitle("?");
        	dialog.setHeaderText("Enter a file prefix:");
        	Optional<String> result = dialog.showAndWait();
        	
        	String prefix;
        	if(result.isPresent()) {
        		prefix = result.get();
        	}else {
        		prefix = "snip";
        		PopupBuilder.showPopup("Info", "No prefix specified, using \"snip\"");
        	}
        	
        	int i = 0;
        	for(Snip snip : SnipManager.getInstance().getLoaded()) {
        		BufferedImage image = snip.getImage();
        		File toSave = new File(selectedDirectory.getPath() + "/" + prefix + "_" + i + ".png");
        		
        		try {
					ImageIO.write(image, "png", toSave);
				} catch (IOException e) { 
					e.printStackTrace();
				}
        		
        		i++;
        	}
        	
        	PopupBuilder.showPopup("Snip!", "Saved snips to " + selectedDirectory.getPath());
        }else {
        	PopupBuilder.showPopup("Error", "No directory selected");
        }
	}
	
	public void loadHotkeys() {
		try{
			File in = new File("settings");
			
			if(!in.exists()) in.createNewFile();
			
			Properties props = new Properties();
			props.load(new FileInputStream(in));
			
			if(!props.containsKey("snip")) props.setProperty("snip", Reference.HOTKEY_SNIP + "");
			if(!props.containsKey("gui")) props.setProperty("gui", Reference.HOTKEY_GUI + "");
			
			Reference.HOTKEY_SNIP = Integer.parseInt(props.getProperty("snip"));
			Reference.HOTKEY_GUI = Integer.parseInt(props.getProperty("gui"));
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public void saveHotkeys() {
		File out = new File("settings");
		Properties props = new Properties();
		props.setProperty("snip", Reference.HOTKEY_SNIP + "");
		props.setProperty("gui", Reference.HOTKEY_GUI + "");
		try {
			props.store(new FileOutputStream(out), null);
		} catch (IOException e) { }
	}
	
	public static FileManager getInstance() {
		return instance;
	}
	
}
