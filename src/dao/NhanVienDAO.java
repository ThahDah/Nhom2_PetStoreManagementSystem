package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TrangThai;

public class NhanVienDAO {
	// lay dsnv tu database
	public ArrayList<NhanVien> getTableNhanVien() {
		ArrayList<NhanVien> dsNV = new ArrayList<NhanVien>();
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "SELECT * FROM NhanVien";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				String maNV = rs.getString("maNV");
				String tenNV = rs.getString("tenNV");
				
				String soDienThoai = rs.getString("soDienThoai");
				String email = rs.getString("email");
				
				
				String hinhAnh = rs.getString("HinhAnh");

				String trangThaiStr = rs.getString("trangThaiNV");

				TrangThai trangThai = null;
				if (trangThaiStr != null) {
				    try { trangThai = TrangThai.valueOf(trangThaiStr); } catch (Exception e) {}
				}


				NhanVien nv = new NhanVien(maNV, tenNV, soDienThoai, email, trangThai, hinhAnh);

				dsNV.add(nv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsNV;
	}

	// them nhan vien
	public boolean themNhanVien(NhanVien nv) throws Exception {
	    Connection con = connectDB.ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    int n = 0;
	    try {
	    		String sql = "INSERT INTO NhanVien VALUES(?, ?, ?, ?, ?, ?)";
	        stmt = con.prepareStatement(sql);
	        
	        stmt.setString(1, nv.getMaNV());
	        stmt.setString(2, nv.getTenNV());
	        stmt.setString(3, nv.getSoDienThoai());
	        stmt.setString(4, nv.getEmail());
	        
	        // neu la enum thi .name(), neu object thi .getMaCa()
	        stmt.setString(5, nv.getTrangThaiNV().name()); 
	        stmt.setString(6, nv.getHinhAnh());
	        
	        
	        n = stmt.executeUpdate();
	    } catch (SQLException e) {
	    	if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
	            throw new Exception("Mã nhân viên này đã tồn tại trong hệ thống. Ấn 'Thêm' để phát sinh mã");
	        } else {
	            throw e;
	        }
	    }
	    return n > 0;
	}

	// sua nhan vien
	public boolean suaNhanVien(NhanVien nv) {
	    Connection con = connectDB.ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    int n = 0;
	    try {
	    	// sua mau don
	    	String sql = "UPDATE NhanVien SET TenNV = ?, SoDienThoai = ?, Email = ?, TrangThai = ?, hinhAnh = ? WHERE maNV = ?";	    
	    	stmt = con.prepareStatement(sql);

	    	stmt.setString(1, nv.getTenNV());
	    	
	        
	        stmt.setString(2, nv.getSoDienThoai());
	        stmt.setString(3, nv.getEmail());
	        
	        // neu ca lam bi bo trong
	        stmt.setString(4, nv.getTrangThaiNV() != null ? nv.getTrangThaiNV().name() : null);
	        stmt.setString(5, nv.getHinhAnh());
	        
	        // WHERE trong SQL
	        stmt.setString(6, nv.getMaNV());
	        
	        n = stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return n > 0;
	}

	//xoa nhan vien
	public boolean xoaNhanVien(String maNV) throws Exception {
		Connection con = connectDB.ConnectDB.getConnection();
		PreparedStatement stmt = null;
		int n = 0;
		try {
			String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, maNV);
			
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			//loi khoa ngoai, 547 nhan vien dang co hoa don
			if (e.getErrorCode() == 547) {
				throw new Exception("Không thể xóa! Nhân viên này đang có dữ liệu liên quan ở bảng khác (ví dụ: Hóa đơn/Phiếu nhập).");
			} else {
				throw new Exception("Lỗi ràng buộc dữ liệu " + e.getMessage());
			}
		}
		return n > 0;
	}
	
}
