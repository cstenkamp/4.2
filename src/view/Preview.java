package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.ImageManager;

@SuppressWarnings("serial")
public class Preview extends JPanel implements Observer {
	
	private ImageManager image;
	
	public Preview(int width, int height, ImageManager img) {
		img.addObserver(this);
		image = img;
		
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException(
					"Groesse muss >= 1x1 Pixel sein.");
		}

		// Setze die bevorzugte Größe
		Dimension d = new Dimension(width, height);
		setPreferredSize(d);

		setForeground(Color.RED);
	}
	
	public void paint(Graphics g) {
		BufferedImage img = image.getActiveImage();
		g.drawImage(img, 0, 0, null);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}
}
