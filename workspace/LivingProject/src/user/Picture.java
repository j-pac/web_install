package user;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Picture {
	
	private BufferedImage image;
	private String picture_name;
	
	public Picture(String name, BufferedImage image) {
		picture_name = name;
		this.image = image;
	}
	

}
