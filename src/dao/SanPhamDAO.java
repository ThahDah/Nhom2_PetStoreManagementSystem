package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.DonViTinh;
import entity.SanPham;
import entity.TrangThai;

public class SanPhamDAO {
	 public ArrayList<SanPham> getAllSanPham() {
		 ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
		 try {
				ConnectDB.getInstance();
				Connection con = ConnectDB.getConnection();
				String sql = "Select * from SanPham";
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				
				while (rs.next()) {
						String maSP = rs.getString(1);
						String tenSP = rs.getString(2);
						double giaNY = rs.getDouble(3);
						int soLuongTon = rs.getInt(4);
						String donViTinh = rs.getString(5);
						DonViTinh dv = null;
						if (donViTinh != null) {
						    try { dv = DonViTinh.valueOf(donViTinh); } catch (Exception e) {}
						}
						SanPham sp = new SanPham(maSP, tenSP, giaNY, soLuongTon, dv);
						dsSP.add(sp);
				}
		 } catch (Exception e) {
				e.printStackTrace();
			}
			return dsSP;
	 	}
	// tim san pham theo ma
	 public SanPham timTheoMa(String maSP) throws SQLException {
	     ConnectDB.getInstance();
	     Connection con = ConnectDB.getConnection();

	     String sql = "SELECT maSP, tenSP, giaNY, soLuongTon, donViTinh "
	                + "FROM SanPham WHERE maSP = ?";

	     try (PreparedStatement stm = con.prepareStatement(sql)) {
	         stm.setString(1, maSP);

	         try (ResultSet rs = stm.executeQuery()) {
	             if (rs.next()) {
	                 String ma = rs.getString("maSP");
	                 String ten = rs.getString("tenSP");
	                 double gia = rs.getDouble("giaNY");
	                 int soLuong = rs.getInt("soLuongTon");

	                 String donViTinhStr = rs.getString("donViTinh");
	                 DonViTinh donViTinh = DonViTinh.valueOf(donViTinhStr);

	                 return new SanPham(ma, ten, gia, soLuong, donViTinh);
	             }
	         }
	     }

	     return null;
	 }
}
