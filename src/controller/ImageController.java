package controller;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import model.ImageManager;
import view.ImageView;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.*;


/**
 * Der Controller, der die View und Model verwaltet.
 * 
 * @author Lukas Kalbertodt
 * 
 */
public class ImageController {

	// View
	private ImageView imageView;
	private ImageManager model;

	/**
	 * Der Konstruktor initialisiert die View und das Model.
	 */
	public ImageController() {
		model = new ImageManager();
		imageView = new ImageView(640, 480, model);
		
		try {
			BufferedImage img = ImageIO.read(new URL("file:totoro.png"));
			model.setNewImage(img);
		} catch (Exception e1) {
			// if the file doesn't exist: it's ok
			System.out.println("Default image 'totoro.png' not found");
		}
		
		imageView.getFileChooser().addActionListener(
			e -> {
				JFileChooser fc = new JFileChooser();
				int ret = fc.showOpenDialog(imageView);
				if (ret == JFileChooser.APPROVE_OPTION) {
					try {
						BufferedImage img = 
								ImageIO.read(fc.getSelectedFile());
						model.setNewImage(img);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		);
		imageView.getReduceButton().addActionListener(
			e -> {
				model.toggleImages();

				// adjust text
				JButton but = (JButton) e.getSource();
				if (model.isOriginalActive()) {
					but.setText("Reduierte Version anzeigen");
				} else {
					but.setText("Original anzeigen");
				}

			}
		);
		imageView.getNumColors().addChangeListener(
			e -> {
				JSpinner src = (JSpinner) e.getSource();
				SpinnerNumberModel sm = 
						(SpinnerNumberModel) src.getModel();
				model.setNumColors(sm.getNumber().intValue());
			}
		);
	}
	
	public ImageView getView() {
		return imageView;
	}
}
