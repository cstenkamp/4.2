package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

public class ColorTable {
	
	private List<RGBCube> subcubes;
	
	public ColorTable(BufferedImage img, int numColors) {
		System.out.println("Pixels : "+(img.getHeight()*img.getWidth()));
		RGBCube cube = new RGBCube(img);
		System.out.println("Mean Color: "+cube.getMeanColor().toString());
		cube.sort(cube.maxRange());
		subcubes = cube.divide();
		int amount = 2;
		while (amount++ < numColors){
			int max = 0; int maxindex = 0;
			for (RGBCube curcube : subcubes) {
				if (curcube.getAmount() > max) {
					max = curcube.getAmount();
					maxindex = subcubes.indexOf(curcube);
				}
			}
			cube = subcubes.get(maxindex);
			cube.sort(cube.maxRange());
			subcubes.addAll(cube.divide());
			subcubes.remove(maxindex);
			System.out.println("Iteration: "+(amount-1));
		}
		System.out.println("With: " + numColors);
	}
	
	/**
	 * Gibt die Farbe der erstellten Farbtabelle zurÃ¼ck, mit der
	 * die originale Farbe dargestellt werden soll.
	 * @param orig Die originale Farbe
	 * @return
	 */
	public Color getReducedColor(Color orig) {
		Color returner = (RGBCube.whoContains(orig, subcubes)).getMeanColor();
		return returner;
	}

    //dazu wäre eine klasse zur darstellung einer box im rgb-raum sinnvoll
	//Mediancut: 1) Figure out which of the 3 channels has biggest range
	//			 2) Sort all pixels according to the respective channel
	//			 3) Divide in first and second half, recursively call on both halves.
	
//	Idee: Zerlege den RGB-Würfel mit den beobachteten Farbhäufigkeiten solange sukzessive in Unterwürfel durch
//	Aufsplitten an einer Trennfläche, bis K Unterwürfel entstanden sind.
//	Initialisiere RGB-Wuerfel mit Häufigkeiten der beobachteten Farbtupel
//	Initialisiere Wurzel des Schnittbaums mit Gesamtzahl der Pixel
//	While noch_nicht_genügend_Blätter do
//	 Wähle Blatt mit der größten Pixelzahl
//	 Bestimme umschliessende Box
//	 Bestimme Achse mit größtem Wertebereich
//	 Durchlaufe Box längs dieser Achse
//	 Teile am Median in zwei Hälften
//	 Trage Hälften als Soehne ein
//	end
//	Für jedes Blatt wähle den Mittelwert aller in ihm liegenden Farben
	
}
