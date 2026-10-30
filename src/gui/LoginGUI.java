package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;




public class LoginGUI extends JFrame implements ActionListener{
	


	private JLabel lblForgetPassword;
	private JButton btnLogin;
	private JButton btnExit;

	public LoginGUI() {
		setSize(500,300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		JPanel pnlMain = new JPanel();
//		pnlMain.setBackground();
		pnlMain.setLayout(new BorderLayout());
		add(pnlMain);
		
		JPanel pnlTitle = new JPanel();
		JLabel lblTitle = new JLabel("PSLAND");
		pnlTitle.add(lblTitle);
		pnlMain.add(pnlTitle, BorderLayout.NORTH);
		
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.setBackground(Color.lightGray);
		pnlCenter.setLayout(new BorderLayout());
		pnlCenter.setBorder(new EmptyBorder(50, 50, 70, 50)); 
		
		//Login Form
		Box boxForm = Box.createVerticalBox();
		Dimension sizeTxTField = new Dimension(200,20);
		
		Box boxUserName = Box.createHorizontalBox();
		JLabel lblUserName = new JLabel("Username:");
		JTextField txtUserName = new JTextField();
		txtUserName.setMaximumSize(sizeTxTField);
		boxUserName.add(lblUserName);
		boxUserName.add(Box.createHorizontalStrut(10));
		boxUserName.add(txtUserName);
		
		Box boxPassword = Box.createHorizontalBox();		
		JLabel lblPassword = new JLabel("Password:");
		JTextField txtPassword = new JTextField();
		txtPassword.setMaximumSize(sizeTxTField);
		boxPassword.add(lblPassword);
		boxPassword.add(Box.createHorizontalStrut(10));
		boxPassword.add(txtPassword);
		
		Box boxForgetPassword = Box.createHorizontalBox();
		lblForgetPassword = new JLabel("Quên mật khẩu?");
		boxForgetPassword.setBorder(new EmptyBorder(0,200,0,0));
		boxForgetPassword.add(lblForgetPassword);
		
		
		boxForm.add(boxUserName);
		boxForm.add(boxPassword);
		boxForm.add(boxForgetPassword);
		
		
		pnlCenter.add(boxForm);
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		
		
		JPanel pnlFeature = new JPanel();
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
