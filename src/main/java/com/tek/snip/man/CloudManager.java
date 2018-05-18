package com.tek.snip.man;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.cloudinary.Cloudinary;
import com.tek.snip.util.PopupBuilder;
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
	
	public void upload(ImageView img) {
		BufferedImage image = SwingFXUtils.fromFXImage(img.getImage(), null);
		
		String url = upload(image);
		
		Util.toClipboard(url);
		PopupBuilder.showPopup("Snip!", "Copied the url to your clipboard");
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
