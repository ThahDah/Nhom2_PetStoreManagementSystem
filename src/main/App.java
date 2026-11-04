package main;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.LichHenGUI;

import com.formdev.flatlaf.FlatLightLaf;

import connectDB.ConnectDB;

import gui.LoginGUI;
import gui.DichVuGUI;
import gui.HoaDonGUI;

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