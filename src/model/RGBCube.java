package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// Ein RGBCube enthält eine Hashmap, die einer Farbe (ein 3-Tupel aus Ints) eine Häufigkeit zuweist.
// Wir können die Hashmap, aus der der Cube besteht, anhand jedem der 3 Tupelwerte neu sortieren
// Desweiteren können wir 2 Subcubes erstellen, gesplittet anhand gegebener Dimension + gegebenem Wert
// Außerdem muss eine static Methode existieren die einem Pixel seinem Cube zuweist...
// Und eine Methode, welche den Durchschnittspixelwert für alle Pixel in der Box errechnet
public class RGBCube {
	private LinkedHashMap<Color, Integer> cube = new LinkedHashMap<Color, Integer>();
	private int amountpixels = 0;
	private Color averagecolor = null;
	
	private final int RED = 0;
	private final int GREEN = 1;
	private final int BLUE = 2;
	
	private int minred, mingreen, minblue, maxred, maxgreen, maxblue;
	
	
	public RGBCube(BufferedImage img) {
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color col = new Color(img.getRGB(x, y) & 0xffffff);
				if (cube.get(col) == null) {
					cube.put(col, 1);
				} else {
					int amount = cube.get(col);
					cube.put(col,++amount); //in this case put overwrites the original value.
				}
			}
		}
		amountpixels = img.getWidth()*img.getHeight();
		sort(RED);
		findBorders();
	}
	
	private RGBCube(LinkedHashMap<Color, Integer> vals) {
		cube = vals;
		for (Iterator<Integer> iter = cube.values().iterator(); iter.hasNext(); ) {
		    amountpixels += iter.next();
		}
		sort(RED);
		findBorders();
	}
	
	public int maxRange(){
		int minr = 1024; int ming = 1024; int minb = 1024;
		int maxr = 0; int maxg = 0; int maxb = 0;
		for (Iterator<Color> iter = cube.keySet().iterator(); iter.hasNext(); ) {
		    Color col = iter.next();
		    minr = col.getRed() < minr ? col.getRed() : minr;
		    ming = col.getGreen() < ming ? col.getGreen() : ming;
		    minb = col.getBlue() < minb ? col.getBlue() : minb;
		    maxr = col.getRed() > maxr ? col.getRed() : maxr;
		    maxg = col.getGreen() > maxg ? col.getGreen() : maxg;
		    maxb = col.getBlue() > maxb ? col.getBlue() : maxb;
		}
		if ((maxr-minr >= maxg-minb) && (maxr-minr >= maxg-minb)) {
			return RED;
		}
		if ((maxg-ming >= maxr-minr) && (maxg-ming >= maxg-minb)) {
			return GREEN;
		}
		return BLUE;
	}
	
	
	
	//Sorts INPLACE!
	@SuppressWarnings("unchecked") //you don't tell me what to do Java!
	public void sort(int wonach){
		//happens according to http://stackoverflow.com/questions/12184378/sorting-linkedhashmap. 
		//mit Java8-Streams wäre es so viel schöner... TODO besser machen.
		List<Map.Entry<Color, Integer>> entries = new ArrayList<Map.Entry<Color, Integer>>(cube.entrySet());
		@SuppressWarnings("rawtypes")
		Comparator myComparator = new Comparator<Map.Entry<Color, Integer>>() {
			  public int compare(Map.Entry<Color, Integer> a, Map.Entry<Color, Integer> b){
			    switch(wonach) {
				    case RED: return (a.getKey().getRed()) - ((b.getKey().getRed()));
				    case GREEN: return (a.getKey().getGreen()) - ((b.getKey().getGreen()));
				    default: return (a.getKey().getBlue()) - ((b.getKey().getBlue()));
			    }	    
			  }
		};		
		Collections.sort(entries, myComparator);
		LinkedHashMap<Color, Integer> sortedMap = new LinkedHashMap<Color, Integer>();
		for (Map.Entry<Color, Integer> entry : entries) {
		  sortedMap.put(entry.getKey(), entry.getValue());
		}
		cube = sortedMap;
	}
	
	
	
	public List<RGBCube> divide() {
		int bisherso = 0;
		List<LinkedHashMap<Color, Integer>> returner = new LinkedList<LinkedHashMap<Color, Integer>>();
		returner.add(new LinkedHashMap<Color, Integer>());
		returner.add(new LinkedHashMap<Color, Integer>());		
		for (Iterator<Entry<Color, Integer>> iter = cube.entrySet().iterator(); iter.hasNext(); ) {
		    Entry<Color, Integer> entry = iter.next();
		    bisherso += entry.getValue();
		    if (bisherso < amountpixels/2) {
		    	returner.get(0).put(entry.getKey(), entry.getValue());
		    } else {
		    	returner.get(1).put(entry.getKey(), entry.getValue());
		    }
		}
		List<RGBCube> returner2 = new LinkedList<RGBCube>();
		returner2.add(new RGBCube(returner.get(0)));
		returner2.add(new RGBCube(returner.get(1)));
		return returner2;
	}
	
	
	public Color getMeanColor() {
		if (averagecolor == null) {
			int red = 0; int green = 0; int blue = 0; int amount = 0;
			for (Iterator<Entry<Color, Integer>> iter = cube.entrySet().iterator(); iter.hasNext(); ) {
			    Entry<Color, Integer> entry = iter.next();
			    for (int i = 0; i < entry.getValue(); i++) {
					red += entry.getKey().getRed();
					green += entry.getKey().getGreen();
					blue += entry.getKey().getBlue();
					amount++;
			    }
			}
			red /= amount;
			green /= amount;
			blue /= amount;
			averagecolor = new Color(red, green, blue);
		}
		return averagecolor;
	}
	

	public void findBorders(){
		minred = 1025; mingreen = 1025; minblue = 1025; maxred = 0; maxgreen = 0; maxblue = 0;
		for (Iterator<Color> iter = cube.keySet().iterator(); iter.hasNext(); ) {
		    Color col = iter.next();
		    minred = (minred < col.getRed() ? minred : col.getRed());
		    mingreen = (mingreen < col.getGreen() ? mingreen : col.getGreen());
		    minblue = (minblue < col.getBlue() ? minblue : col.getBlue());
		    maxred = (maxred > col.getRed() ? maxred : col.getRed());
		    maxgreen = (maxgreen > col.getGreen() ? maxgreen : col.getGreen());
		    maxblue = (maxblue > col.getBlue() ? maxblue : col.getBlue());
		}
	}	
	
	
	public boolean doIContain(Color col) {
		if ((minred <= col.getRed()) && (maxred >= col.getRed()) && (mingreen <= col.getGreen()) && (maxgreen >= col.getGreen()) 
			&& (minblue <= col.getBlue()) && (maxblue >= col.getBlue())) {
			return true;
		}
		return false;
	}
		
	
	public static RGBCube whoContains(Color col, List<RGBCube> allboxes) {
		for (RGBCube cube : allboxes) {
			if (cube.doIContain(col)) {
				return cube;
			}
		}
		return null;
	}
	
	public int getAmount() {
		return amountpixels;
	}
}
