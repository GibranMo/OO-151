import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SlideshowModel {  //-
    
	private List<Slide> slides = new ArrayList<>();
	private Slide currentSlide = null;
	private int currentIndex = 0;
	private String filePath = null;
	
	private List<ImageIcon> imageIcons = new ArrayList <> ();
	
	
    
	/*
	 * Create a new empty slideshow
	 */
	public void newSlideshow() {
		slides.clear();
		currentIndex = 0;
		filePath = null;
		
	}
    
	/*
	 * Opens an existing slideshow
	 */
	public void openSlideshow(Scanner in, Container frame) {
        
		slides.clear();
		currentIndex = 0;
		while (in.hasNextLine()) {
			/**
			String[] slideData = in.nextLine().split("\t");
			int x = Integer.parseInt(slideData[2]);
			int y = Integer.parseInt(slideData[3]);
			slides.add(new Slide(slideData[0], slideData[1], x, y));
			
			**/
			try {
			String[] slideData = in.nextLine().split("\\^");
			Integer id = Integer.parseInt(slideData[0]);
			String caption = slideData[1];
			Integer x = Integer.parseInt(slideData[2]);
			Integer y = Integer.parseInt(slideData[3]);
			String imagePath = slideData[4];
			slides.add(new Slide(caption, imagePath, x, y, id));
			
			//Image scaledImage = null;
			ImageIcon image = new ImageIcon(imagePath);
			
			imageIcons.add(image);
			}catch(Exception e){
				JOptionPane.showMessageDialog(frame, " ! There was an Error reading the file");
				return;
			}
			
		}
		
		if (slides.size() > 0)
			selectSlide(0);
		
		
	}
	
	public ImageIcon getCurrentImageIcon() {
		return imageIcons.get(currentIndex);
	}
	
    
	/*
	 * Saves the slideshow to a .slideshow file
	 */
	public void saveSlideshow(PrintWriter out) {
        
		StringBuilder sb;
		for (Slide slide : slides) {
			
			sb = new StringBuilder();
			sb.append(slide.caption).append("\t");
			
			if (slide.imagePath != null)
				sb.append(slide.imagePath).append("\t");
			else
				sb.append("").append("\t");
			
			sb.append(slide.x).append("\t");
			sb.append(slide.y).append("\t");
			
			out.println(sb.toString());
		}
	}
    
	public void addSlide() {
		Slide newSlide = new Slide();
		slides.add(newSlide);
		currentIndex = slides.size() - 1;
	}
    
	public void removeSlide() {
		if (currentSlide != null)
			slides.remove(currentSlide);
		if (slides.size() > 0)
			selectSlide(0);
	}
	
	public void selectSlide(int index) {
		currentSlide = slides.get(index);
		currentIndex = index;
	}
    
	public List<Slide> getAllSlides() {
		return slides;
	}
    
	public Slide getSelectedSlide() {
		return currentSlide;
	}
    
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setCaption(String caption) {
		currentSlide.caption = caption;
	}
	
	public void setImagePath(String imagePath) {
		currentSlide.imagePath = imagePath;
	}
	
	
	public void setSlideXY(int x, int y) {
		
		if (currentSlide == null)
			return;
		
		currentSlide.x = x;
		currentSlide.y = y;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
