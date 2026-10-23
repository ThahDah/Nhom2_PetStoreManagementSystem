package gui;

import javax.swing.JFrame;

public class test_gui extends JFrame {
	
	public test_gui() {
		setSize(500,300);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		test_gui fr = new test_gui();
		fr.setVisible(true);
		
	}
}
