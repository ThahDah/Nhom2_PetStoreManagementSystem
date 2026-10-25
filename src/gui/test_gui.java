package gui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class test_gui extends JFrame {
	
	public test_gui() {
		setSize(500,300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		
		JPanel pnlTop = new JPanel();
		pnlTop.setBackground(Color.DARK_GRAY);
		pnlTop.setPreferredSize(new Dimension(50,50));
		add(pnlTop, BorderLayout.NORTH);
		
		JPanel pnlLeft = new JPanel();
		pnlLeft.setBackground(Color.GRAY);
		add(pnlLeft, BorderLayout.WEST);
		
		JButton btnTest = new JButton("Test");
		pnlLeft.add(btnTest);

		
	}
	public static void main(String[] args) {
		test_gui fr = new test_gui();
		fr.setVisible(true);
		
	}
}
