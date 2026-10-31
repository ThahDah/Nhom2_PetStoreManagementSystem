package main;

import com.formdev.flatlaf.FlatLightLaf;

import gui.LoginGUI;

public class App {
	public static void main(String[] args) {
		try {
			  FlatLightLaf.setup();
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
		new LoginGUI().setVisible(true);
	}
}
