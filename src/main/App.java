package main;


import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.LichHenGUI;
import com.formdev.flatlaf.FlatLightLaf;


import gui.LoginGUI;

public class App {

	public static void main(String[] args) {
		try {
			  FlatLightLaf.setup();
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
		
//		SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true))
//		test UI lich hẹn
		 SwingUtilities.invokeLater(() -> moTrangLichHen());
//		 Test UI login
//		new LoginGUI().setVisible(true);

	}


	public static void moTrangLichHen() {
		try {
			NhanVien nhanVien = new NhanVienDAO().timTheoMa("NV2601001"); // thay TẠM bằng nhân viên vừa đăng nhập
			JFrame frmLichHen = new JFrame("PetShop LAND - Lịch hẹn");
			frmLichHen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmLichHen.add(new LichHenGUI(nhanVien));
			frmLichHen.setSize(1000, 600);
			frmLichHen.setLocationRelativeTo(null);
			frmLichHen.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}