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
	 * Gibt die Farbe der erstellten Farbtabelle zurück, mit der
	 * die originale Farbe dargestellt werden soll.
	 * @param orig Die originale Farbe
	 * @return
	 */
	public Color getReducedColor(Color orig) {
		Color returner = (RGBCube.whoContains(orig, subcubes)).getMeanColor();
		return returner;
	}

    //dazu w�re eine klasse zur darstellung einer box im rgb-raum sinnvoll
	//Mediancut: 1) Figure out which of the 3 channels has biggest range
	//			 2) Sort all pixels according to the respective channel
	//			 3) Divide in first and second half, recursively call on both halves.
	
//	Idee: Zerlege den RGB-W�rfel mit den beobachteten Farbh�ufigkeiten solange sukzessive in Unterw�rfel durch
//	Aufsplitten an einer Trennfl�che, bis K Unterw�rfel entstanden sind.
//	Initialisiere RGB-Wuerfel mit H�ufigkeiten der beobachteten Farbtupel
//	Initialisiere Wurzel des Schnittbaums mit Gesamtzahl der Pixel
//	While noch_nicht_gen�gend_Bl�tter do
//	 W�hle Blatt mit der gr��ten Pixelzahl
//	 Bestimme umschliessende Box
//	 Bestimme Achse mit gr��tem Wertebereich
//	 Durchlaufe Box l�ngs dieser Achse
//	 Teile am Median in zwei H�lften
//	 Trage H�lften als Soehne ein
//	end
//	F�r jedes Blatt w�hle den Mittelwert aller in ihm liegenden Farben
	
}
