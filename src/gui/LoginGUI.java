package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import custom_gui.BackgroundPanel;




public class LoginGUI extends JFrame implements ActionListener{
	


	private JLabel lblForgetPassword;
	private JButton btnLogin;
	private JButton btnExit;

	public LoginGUI() {
		setSize(500,300);
		setLocationRelativeTo(null);
		setUndecorated(true);
		UIManager.put("Component.arc", 15);
		UIManager.put("TextComponent.arc", 15);
		UIManager.put("Button.arc", 15);
		
		
		BackgroundPanel pnlMain = new BackgroundPanel("img/background/Background_Login.png") ;
		pnlMain.setLayout(new BorderLayout());
		add(pnlMain);
		
		JPanel pnlTitle = new JPanel();
		JLabel lblTitle = new JLabel("LAND");
		lblTitle.setFont(new Font("Arial", Font.BOLD,20));
		pnlTitle.add(lblTitle);
		pnlTitle.setOpaque(false);
		pnlMain.add(pnlTitle, BorderLayout.NORTH);
		
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.setBackground(new Color(0, 0, 0, 100)); 
		pnlCenter.setLayout(new BorderLayout());
		pnlCenter.setBorder(new EmptyBorder(50, 50, 70, 50)); 
		
		//Form đăng nhập
		Box boxForm = Box.createVerticalBox();
		Dimension sizeTxTField = new Dimension(200,20);
		
		Box boxUserName = Box.createHorizontalBox();
		JLabel lblUserName = new JLabel("Username:");
		lblUserName.setForeground(Color.white);
		JTextField txtUserName = new JTextField();
		txtUserName.setBorder(null);
		txtUserName.setMaximumSize(sizeTxTField);
		boxUserName.add(lblUserName);
		boxUserName.add(Box.createHorizontalStrut(10));
		boxUserName.add(txtUserName);
		
		Box boxPassword = Box.createHorizontalBox();		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.white);
		JTextField txtPassword = new JTextField();
		txtPassword.setBorder(null);
		txtPassword.setMaximumSize(sizeTxTField);
		boxPassword.add(lblPassword);
		boxPassword.add(Box.createHorizontalStrut(10));
		boxPassword.add(txtPassword);
		
		Box boxForgetPassword = Box.createHorizontalBox();
		lblForgetPassword = new JLabel("Quên mật khẩu?");
		lblForgetPassword.setForeground(Color.white);
		boxForgetPassword.setBorder(new EmptyBorder(0,180,0,0));
		boxForgetPassword.add(lblForgetPassword);
		
		
		boxForm.add(boxUserName);
		boxForm.add(boxPassword);
		boxForm.add(boxForgetPassword);
		
		
		pnlCenter.add(boxForm);
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		
		
		JPanel pnlFeature = new JPanel();
		pnlFeature.setOpaque(false);
		btnLogin = new JButton("Đăng nhập");
		btnExit = new JButton("Thoát");
		
		pnlFeature.add(btnLogin);
		pnlFeature.add(btnExit);
		pnlMain.add(pnlFeature, BorderLayout.SOUTH);
		
		btnExit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == btnExit) {
			System.exit(1);
		}
	}
	
}
