package com.tek.snip.man;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.cloudinary.Cloudinary;
import com.tek.snip.ui.GUI;
import com.tek.snip.ui.MainController;
import com.tek.snip.util.Reference;
import com.tek.snip.util.Util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

public class CloudManager {
	
	private static CloudManager instance;
	
	private Cloudinary cloudinary = new Cloudinary();
	
	public CloudManager() {
		instance = this;
	}
	
	public void upload() {
		ImageView img = MainController.getInstance().getSelected();
		
		if(img == null) {
			Util.showPopupMessage("Error, please select an image", GUI.getInstance().getWindow());
			return;
		}
		
		BufferedImage image = SwingFXUtils.fromFXImage(img.getImage(), null);
		
		String url = upload(image);
		
		Util.toClipboard(url);
		Util.showPopupMessage("Uploaded to " + url + " | Copied url to clipboard", GUI.getInstance().getWindow());
	}
	
	@SuppressWarnings("rawtypes")
	private String upload(BufferedImage image) {
		File file = new File("tmp.png");
		try{
			ImageIO.write(image, "png", file);
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("api_key", Reference.KEY_CLOUDINARY);
			params.put("api_secret", Reference.SECRET_CLOUDINARY);
			params.put("cloud_name", Reference.NAME_CLOUDINARY);
			Map uploadResult = cloudinary.uploader().upload(file, params);
			
			file.delete();
			
			return (String) uploadResult.get("url").toString();
		}catch(Exception e) {
			file.delete();
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static CloudManager getInstance() {
		return instance;
	}
	
}
