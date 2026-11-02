package common;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtils {
	public ImageIcon setImg(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image edit = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon editedImg = new ImageIcon(edit);
		return editedImg;
	}
}
