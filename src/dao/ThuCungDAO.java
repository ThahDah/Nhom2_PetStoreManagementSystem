package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.DoHungDu;
import entity.KhachHang;
import entity.Loai;
import entity.ThuCung;
import entity.TrangThai;

public class ThuCungDAO {

	public List<ThuCung> timTheoChuSoHuu(KhachHang khachHang) throws SQLException {
		List<ThuCung> dsThuCung = new ArrayList<>();
		String sql = "SELECT maThuCung, tenThuCung, loai, canNang, ghiChu, trangThaiThuCung, doHungDu "
				+ "FROM ThuCung WHERE maKH = ? ORDER BY tenThuCung";
		Connection con = ConnectDB.getConnection();
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, khachHang.getMaKH());
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					dsThuCung.add(taoThuCung(rs, khachHang));
				}
			}
		}
		return dsThuCung;
	}

	/* cap nhat lai tt thu cung trong luc tao lich hen */
	void capNhatThongTin(Connection con, ThuCung thuCung) throws SQLException {
		String sql = "UPDATE ThuCung SET loai = ?, canNang = ?, doHungDu = ? WHERE maThuCung = ?";
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, thuCung.getLoai().name());
			stm.setDouble(2, thuCung.getCanNang());
			stm.setString(3, thuCung.getDoHungDu().name());
			stm.setString(4, thuCung.getMaThuCung());
			stm.executeUpdate();
		}
	}
	
	/* doc ResultSet de chuyen ThuCung thanh doi tuong */
	static ThuCung taoThuCung(ResultSet rs, KhachHang chuSoHuu) throws SQLException {
		return new ThuCung(rs.getString("maThuCung"), rs.getString("tenThuCung"),
				Loai.valueOf(rs.getString("loai")), rs.getDouble("canNang"), rs.getString("ghiChu"),
				TrangThai.valueOf(rs.getString("trangThaiThuCung")), DoHungDu.valueOf(rs.getString("doHungDu")),
				chuSoHuu);
	}
	
	// doc du lieu tu database
	public ArrayList<ThuCung> getTableThuCung() {
		ArrayList<ThuCung> dsTC = new ArrayList<ThuCung>();
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "SELECT tc.*, kh.tenKH " +
		             "FROM ThuCung tc " +
		             "JOIN KhachHang kh ON tc.maKH = kh.maKH";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				String ma = rs.getString("maThuCung");
	            String ten = rs.getString("tenThuCung");

	            String loaiStr = rs.getString("loai"); 
	            Loai loai = Loai.valueOf(loaiStr.trim()); 
	            
	            double canNang = rs.getDouble("canNang");
	            String ghiChu = rs.getString("ghiChu");
	            
	            String trangThaiTCStr = rs.getString("trangThaiThuCung");
	            TrangThai trangThai = TrangThai.valueOf(trangThaiTCStr.trim());
	            
	            String doHungDuStr = rs.getString("doHungDu");
	            DoHungDu doHungDu = DoHungDu.valueOf(doHungDuStr.trim());
	            
	            String maKH = rs.getString("maKH");
	            String tenKH = rs.getString("tenKH");
	            
	            KhachHang khachHang = new KhachHang(maKH, tenKH);

	            ThuCung tc = new ThuCung(ma, ten, loai, canNang, ghiChu, trangThai, doHungDu, khachHang);
	            
				dsTC.add(tc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsTC;
	}
	
	// them
	public boolean themThuCung(ThuCung tc) throws Exception {
	    Connection con = connectDB.ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    int n = 0;
	    try {
	        String sql = "INSERT INTO ThuCung VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	        stmt = con.prepareStatement(sql);
	        
	        stmt.setString(1, tc.getMaThuCung());
	        stmt.setString(2, tc.getTenThuCung());
	        stmt.setString(3, tc.getLoai().name());
	        stmt.setDouble(4, tc.getCanNang()); // double
	        stmt.setString(5, tc.getGhiChu());  
	        stmt.setString(6, tc.getTrangThaiThuCung().name()); // Luu Enum
	        stmt.setString(7, tc.getDoHungDu().name());
	        stmt.setString(8, tc.getChuSoHuu().getMaKH());
	        
	        n = stmt.executeUpdate();
	    } catch (SQLException e) {
	    	//loi trung khoa chinh
	    	if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
	            throw new Exception("Mã thú cưng này đã tồn tại trong hệ thống!");
	        } else {
	            throw e;
	        }
	    }
	    return n > 0;
	}
	
	// xoa
	public boolean xoaThuCung(String maTC) throws Exception {
		Connection con = connectDB.ConnectDB.getConnection();
		PreparedStatement stmt = null;
		int n = 0;
		try {
			String sql = "DELETE FROM ThuCung WHERE maThuCung = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, maTC);
			
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			// Loi khoa ngoai
			if (e.getErrorCode() == 547) {
				throw new Exception("Không thể xóa! Thú cưng này đang có dữ liệu hóa đơn liên quan.");
			} else {
				throw new Exception("Lỗi ràng buộc dữ liệu: " + e.getMessage());
			}
		}
		return n > 0;
	}
	
	public ThuCung timKiemThuCungTheoMaTC(String maTC) {
		Connection con  = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "SELECT tc.*, kh.tenKH " +
		               "FROM ThuCung tc " +
		               "JOIN KhachHang kh ON tc.maKH = kh.maKH " +
		               "WHERE tc.maThuCung = ?";
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String ma = rs.getString("maThuCung");
	            String ten = rs.getString("tenThuCung");

	            String loaiStr = rs.getString("loai"); 
	            Loai loai = Loai.valueOf(loaiStr.trim()); 
	            
	            double canNang = rs.getDouble("canNang");
	            String ghiChu = rs.getString("ghiChu");
	            
	            String trangThaiTCStr = rs.getString("trangThaiThuCung");
	            TrangThai trangThai = TrangThai.valueOf(trangThaiTCStr.trim());
	            
	            String doHungDuStr = rs.getString("doHungDu");
	            DoHungDu doHungDu = DoHungDu.valueOf(doHungDuStr.trim());
	            
	            String maKH = rs.getString("maKH");
	            String tenKH = rs.getString("tenKH");
	            
	            KhachHang khachHang = new KhachHang(maKH, tenKH);

	            ThuCung tc = new ThuCung(ma, ten, loai, canNang, ghiChu, trangThai, doHungDu, khachHang);
	            return tc;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// sua thong tin thu cung
	public boolean suaThuCung(ThuCung tc) {
	    Connection con = connectDB.ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    int n = 0;
	    try {
	        String sql = "UPDATE ThuCung SET tenThuCung = ?, loai = ?, canNang = ?, ghiChu = ?, trangThaiThuCung = ?, doHungDu = ?, maKH = ? WHERE maThuCung = ?";
	        stmt = con.prepareStatement(sql);
	        
	        stmt.setString(1, tc.getTenThuCung());
	        stmt.setString(2, tc.getLoai().name()); 
	        stmt.setDouble(3, tc.getCanNang()); 
	        stmt.setString(4, tc.getGhiChu());
	        stmt.setString(5, tc.getTrangThaiThuCung().name());
	        stmt.setString(6, tc.getDoHungDu().name()); 
	        stmt.setString(7, tc.getChuSoHuu().getMaKH());
	        stmt.setString(8, tc.getMaThuCung()); 
	        
	        n = stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return n > 0;
	}
}
