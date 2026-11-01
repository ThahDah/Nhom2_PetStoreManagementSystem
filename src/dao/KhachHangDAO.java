package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
	// doc du lieu tu database
	public ArrayList<KhachHang> getTableKhachHang() {
		ArrayList<KhachHang> dsKH = new ArrayList<KhachHang>();
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "SELECT * FROM KhachHang";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
					String ma = rs.getString("maKH"); // hoac  rs.getString(1)
					String ten = rs.getString("tenKH");
					String sdt = rs.getString("soDienThoai");
					String email=rs.getString("email");
					int diemTL = rs.getInt("diemTichLuy"); // Dùng rs.getInt chuan
					
				// constructor
				KhachHang kh = new KhachHang(ma, ten, sdt, email, diemTL);
				dsKH.add(kh);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsKH;
	}

		// them
		public boolean themKhachHang(KhachHang kh) throws Exception {
		    Connection con = connectDB.ConnectDB.getConnection();
		    PreparedStatement stmt = null;
		    int n = 0;
		    try {
		        String sql = "INSERT INTO KhachHang VALUES(?, ?, ?, ?, ?)";
		        stmt = con.prepareStatement(sql);
		        
		        stmt.setString(1, kh.getMaKH());
		        stmt.setString(2, kh.getTenKH());
		        stmt.setString(3, kh.getSoDienThoai());
		        stmt.setInt(4, kh.getDiemTichLuy()); // int
		        
		        n = stmt.executeUpdate();
		    } catch (SQLException e) {
		    	//loi trung khoa chinh
		    	if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
		            throw new Exception("Mã khách hàng này đã tồn tại trong hệ thống!");
		        } else {
		            throw e;
		        }
		    }
		    return n > 0;
		}

		// sua thong tin khach hang
		public boolean suaKhachHang(KhachHang kh) {
		    Connection con = connectDB.ConnectDB.getConnection();
		    PreparedStatement stmt = null;
		    int n = 0;
		    try {
		    	String sql = "UPDATE KhachHang SET tenKH = ?, soDienThoai = ?, diemTichLuy = ?, diemDaNhan = ?, loaiKH = ? WHERE maKH = ?";
		    	stmt = con.prepareStatement(sql);
		        
		    	stmt.setString(1, kh.getTenKH());
		    	stmt.setString(2, kh.getSoDienThoai());
		    	stmt.setString(3, kh.getEmail());
		    	stmt.setInt(4, kh.getDiemTichLuy());
		    	stmt.setString(5, kh.getMaKH()); // maKH dat o where    
		    	
		        n = stmt.executeUpdate();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return n > 0;
		}

		// xoa
		public boolean xoaKhachHang(String maKH) throws Exception {
			Connection con = connectDB.ConnectDB.getConnection();
			PreparedStatement stmt = null;
			int n = 0;
			try {
				String sql = "DELETE FROM KhachHang WHERE maKH = ?";
				stmt = con.prepareStatement(sql);
				stmt.setString(1, maKH);
				
				n = stmt.executeUpdate();
			} catch (SQLException e) {
				// Loi khoa ngoai (VD: Khach hang da tung dat hang)
				if (e.getErrorCode() == 547) {
					throw new Exception("Không thể xóa! Khách hàng này đang có dữ liệu hóa đơn liên quan.");
				} else {
					throw new Exception("Lỗi ràng buộc dữ liệu: " + e.getMessage());
				}
			}
			return n > 0;
		}
		public KhachHang timKiemKhachHangTheoSDT(String sdt) {
			Connection con  = ConnectDB.getConnection();
			PreparedStatement stmt = null;
			try {
				String query = "Select * From KhachHang Where SoDienThoai = ?";
						stmt = con.prepareStatement(query);
						stmt.setString(1, sdt);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					String maKH = rs.getString("maKH"); // hoac rs.getString(1)
					String ten = rs.getString("tenKH");
					String dt = rs.getString("soDienThoai");
					String email=rs.getString("email");
					int diemTL = rs.getInt("diemTichLuy"); // Dùng rs.getInt chuan

					
					
					KhachHang kh = new KhachHang(maKH, ten, dt, email, diemTL);
					return kh;
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
}
