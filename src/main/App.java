
package main;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.LichHenGUI;
import gui.LoginGUI;

public class App {

	public static void main(String[] args) {
		// Dùng giao diện của hệ điều hành
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));

		 SwingUtilities.invokeLater(() -> moTrangLichHen());
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