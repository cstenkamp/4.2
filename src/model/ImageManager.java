package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Observable;

/**
 * Manages the original and the reduced image.
 * @author Lukas Kalbertodt
 *
 */
public class ImageManager extends Observable {
	private BufferedImage original;
	private BufferedImage reduced;
	private boolean originalShown = true;
	private int numColors = 16;
	
	public void setNewImage(BufferedImage newImg) {
		original = newImg;
		reduced = null;
		originalShown = true;
		
		changeNotify();
	}
	
	/**
	 * Toggles the active image and calculates the reduced image of
	 * it wasn't done yet.
	 */
	public void toggleImages() {
		if (reduced == null) {
			calculateReducedImage();
		}
		
		originalShown = !originalShown;
		changeNotify();
	}
	
	/**
	 * Returns the active image
	 */
	public BufferedImage getActiveImage() {
		if (originalShown) {
			return original;
		} else {
			return reduced;
		}
	}
	
	/**
	 * Returns if the original image is the active one
	 */
	public boolean isOriginalActive() {
		return originalShown;
	}
	
	public void setNumColors(int num) {
		numColors = num;
		if (!isOriginalActive()) {
			calculateReducedImage();
			changeNotify();
		} else {
			reduced = null;
		}
	}
	
	private void calculateReducedImage() {
		// Create new image with the same meta properties as the original
		reduced = new BufferedImage(
			original.getWidth(), 
			original.getHeight(), 
			original.getType()
		);
	
		// create color table and fill new image
		ColorTable table = new ColorTable(original, numColors);
		HashSet<Color> colorProof = new HashSet<>();
		
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				Color origCol = new Color(original.getRGB(x, y));
				Color redCol = table.getReducedColor(origCol);
				colorProof.add(redCol);
				if (colorProof.size() > numColors) {
					throw new RuntimeException("Too many colors!");
				}
				reduced.setRGB(x, y, redCol.getRGB());
			}
		}
	}
	
	private void changeNotify() {
		setChanged();
		notifyObservers();
	}
}
