package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

import connectDB.ConnectDB;

public class MainGUI extends JFrame implements ActionListener  {
	private JPanel pnlTab;
	private JPanel pnlRight;
	private CardLayout cardLayout;
	private BanHangGUI banHang_UI;
	private JButton btnBanHang;
	private final String PANEL_BAN_HANG = "BanHang";
	public MainGUI() {
		try {
			ConnectDB.getInstance().connect();
			System.out.println("Connected!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setSize(1400,800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		//Tao border xam
		Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1);
		
		//Custom lai cac thanh phan
		UIManager.put("Component.arc", 15);
		UIManager.put("TextComponent.arc", 15);
		UIManager.put("Button.arc", 15);
		
		JPanel pnlTop = new JPanel(new BorderLayout());
		pnlTop.setBackground(Color.white);
		pnlTop.setBorder(lineBorder);
		
		Box boxTop = Box.createHorizontalBox();
		boxTop.setOpaque(false);
		
		//Logo cửa hàng và tên cửa hàng
		ImageIcon logo = new ImageIcon("img/logo/logo_pet_shop.png");
		JLabel lblLogoCuaHang = new JLabel(setImg(logo, 100, 50));
		
		JLabel lblBrandName = new JLabel("PetShop");
		lblBrandName.setFont(new Font("Arial",Font.PLAIN,18));
		
		boxTop.add(lblLogoCuaHang);
		boxTop.add(lblBrandName);
		
		pnlTop.add(boxTop);
		this.add(pnlTop, BorderLayout.NORTH);
		
		//Thanh Menu
		JPanel pnlLeft = new JPanel();
		pnlLeft.setBackground(Color.WHITE);
		pnlLeft.setPreferredSize(new Dimension(150,0));
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBorder(lineBorder);
		
		pnlTab = new JPanel(new GridLayout(15,0,5,5));
		pnlTab.setOpaque(false);
		
		//Nút bán hàng
		ImageIcon cartIcon = new ImageIcon("img/icon/icon_cart_shopping.png");
		
		btnBanHang = new JButton("Bán hàng");
		btnBanHang.setIcon(setImg(cartIcon, 20, 20));
		btnBanHang.setBorderPainted(false);
		btnBanHang.setFocusPainted(false);
//		btnBanHang.setOpaque(true);
		btnBanHang.setBackground(new Color(173, 255, 47));
		btnBanHang.setHorizontalAlignment(SwingConstants.LEFT); 
		btnBanHang.setIconTextGap(15); 
		btnBanHang.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
		
		pnlTab.add(btnBanHang);
		pnlLeft.add(pnlTab);
		
		this.add(pnlLeft, BorderLayout.WEST);
		
		//Phần các màn hình
		pnlRight = new JPanel();
		pnlRight.setBorder(lineBorder);
		cardLayout = new CardLayout(); 
	    pnlRight.setLayout(cardLayout);
	    pnlRight.setBackground(Color.white);
	    
	    banHang_UI = new BanHangGUI();
	    pnlRight.add(banHang_UI, PANEL_BAN_HANG );
		this.add(pnlRight);
		
		btnBanHang.addActionListener(this);
	}
	
	public static void main(String[] args) {
		new MainGUI().setVisible(true);
		
	}
	//Resize ảnh và chuyển định dạng thành ImageIcon để chèn vô JLabel và JButton
	public ImageIcon setImg(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image edit = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon editedImg = new ImageIcon(edit);
		return editedImg;
	}
	
	private void setBanHang() {
	    cardLayout.show(pnlRight, PANEL_BAN_HANG);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == btnBanHang) {
			setBanHang();
		}
	}
}
