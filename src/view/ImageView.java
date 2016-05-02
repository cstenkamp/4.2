package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import model.ImageManager;

/**
 * Eine ColorPanelView stellt eine Zeichenfläche und Farbauswahlmöglichkeiten
 * bereit.
 * 
 * @author Nicolas Neubauer
 * 
 */
@SuppressWarnings("serial")
public class ImageView extends JPanel {	
	private JButton fc;
	private JButton rc;
	private JSpinner numCols;
	
	/**
	 * Konstruktor, der eine neue View anlegt.
	 */
	public ImageView(int width, int height, ImageManager img) {
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException(
					"Groesse der Bildanzeige muss 1x1 oder groesser sein.");
		}

		this.setLayout(new BorderLayout());
		
		JPanel pictureBox = new Preview(width, height, img);
		this.add(pictureBox, BorderLayout.NORTH);
		
		JPanel buttons = new JPanel(new GridLayout(1, 0));
		this.add(buttons, BorderLayout.CENTER);
		
		fc = new JButton("Datei auswählen");
		buttons.add(fc);
		
		rc = new JButton("Reduzierte Version anzeigen");
		buttons.add(rc);
		
		JPanel colorMenu = new JPanel(new GridLayout(1, 0));
		this.add(colorMenu, BorderLayout.SOUTH);
		colorMenu.setPreferredSize(new Dimension(width, 30));
		
		colorMenu.add(new JLabel("Anzahl Farben:"));
		
		SpinnerNumberModel model = new SpinnerNumberModel(16, 2, 1024, 1);
		numCols = new JSpinner(model);
		colorMenu.add(numCols);
	}
	
	public JButton getFileChooser() {
		return fc;
	}
	
	public JButton getReduceButton() {
		return rc;
	}
	
	public JSpinner getNumColors() {
		return numCols;
	}
}
