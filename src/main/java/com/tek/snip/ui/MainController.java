package com.tek.snip.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;

import com.tek.snip.Launcher;
import com.tek.snip.man.CloudManager;
import com.tek.snip.man.FileManager;
import com.tek.snip.man.SnipManager;
import com.tek.snip.objects.MenuOption;
import com.tek.snip.objects.Snip;
import com.tek.snip.ui.editor.ImageEditor;
import com.tek.snip.util.PopupBuilder;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController {
	
	private static MainController instance;
	private ImageView selected;
	
    @FXML
    private MenuItem itemSaveAll;
    
    @FXML
    private MenuItem itemExit;
	
    @FXML
    private MenuItem itemClear;
    
    @FXML
    private MenuItem itemSettings;

    @FXML
    private VBox paneContent;
    
    @FXML
    private ScrollPane paneScroll;
	
	@FXML
	private void initialize() {
		instance = this;
		
		paneContent.setAlignment(Pos.TOP_LEFT);
		
		paneScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		paneScroll.addEventFilter(ScrollEvent.SCROLL, e -> {
			if(e.getDeltaX() != 0) {
				e.consume();
			}
		});
		
		itemSaveAll.setOnAction(e -> {
			FileManager.getInstance().saveAll();
		});
		
		itemExit.setOnAction(e -> {
			Launcher.stop();
		});
		
		itemClear.setOnAction(e -> {
			SnipManager.getInstance().clear();
			selected = null;
		});
		
		itemSettings.setOnAction(e -> {
			GUI.getInstance().getWindow().setScene(GUI.getInstance().getFXMLSettings());
		});
		
		updateSnips();
	}
	
	public ArrayList<ImageView> images = new ArrayList<ImageView>();
	
	public ImageView process(BufferedImage image, Snip snip) {
		ImageView view = new ImageView(SwingFXUtils.toFXImage(image, null));
		view.setFitWidth(130);
		view.setFitHeight(130);
		HBox.setMargin(view, new Insets(0, 5, 0, 5));
		
		Runnable save = () -> {
			FileManager.getInstance().save(view);
		};
		
		Runnable upload = () -> {
			CloudManager.getInstance().upload(view);
		};
		
		Runnable print = () -> {
			PrinterJob printJob = PrinterJob.getPrinterJob();
			printJob.setPrintable(new Printable() {
			        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
			                if (pageIndex != 0) {
			                    return NO_SUCH_PAGE;
			                }
			                
			                graphics.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
			                return PAGE_EXISTS;
			        }
			});     
			try {
			    printJob.print();
			} catch (PrinterException e1) {             
			    e1.printStackTrace();
			}
		};
		
		Runnable show = () -> {
			Util.showImage(view.getImage());
		};
		
		Runnable edit = () -> {
			double id = snip.getId();
			
			BufferedImage fromImage = SwingFXUtils.fromFXImage(view.getImage(), null);
			BufferedImage editImage = ImageEditor.edit(fromImage);
			
			Snip snipperooni = SnipManager.getInstance().getSnipById(id);
			
			if(snipperooni != null) {
				snipperooni.setImage(editImage);
			}
			
			updateSnips();
		};
		
		Runnable remove = () -> {
			SnipManager.getInstance().remove(view.getImage());
		};
		
		view.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				Util.toClipboard(image);
				PopupBuilder.showPopup("Snip!", "Copied to your clipboard");
				
				view.setOpacity(0.25);
				
				Timeline timeline = new Timeline();
				timeline.setCycleCount(1);
				timeline.setAutoReverse(false);
				KeyValue kv = new KeyValue(view.opacityProperty(), 1);
				KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
				
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		});
		
		view.setOnContextMenuRequested(e -> {
			Util.showContextMenu(view, new MenuOption("Save", save), new MenuOption("Upload", upload), new MenuOption("Print", print), new MenuOption("View Image", show), new MenuOption("Edit", edit), new MenuOption("Remove", remove));
			e.consume();
		});
		
		return view;
	}
	
	public void updateSnips() {
		if(SnipManager.getInstance() == null) return;
		
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
				VBox.setMargin(hbox, new Insets(0, 5, 10, 5));
			}
			
			ImageView image = process(snip.getImage(), snip);
			
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