package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.DonViTinh;
import entity.Loai;
import entity.SanPham;

public class SanPhamDAO {

    // Lấy toàn bộ sản phẩm
    public ArrayList<SanPham> getAllSanPham() {
        ArrayList<SanPham> dsSP = new ArrayList<>();

        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        String sql = "SELECT maSP, tenSP, giaNhap, giaBan, soLuongTon, donViTinh "
                   + "FROM SanPham";

        try (Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP");
                double giaNhap = rs.getDouble("giaNhap");
                double giaBan = rs.getDouble("giaBan");
                int soLuongTon = rs.getInt("soLuongTon");

                String donViTinhStr = rs.getString("donViTinh");
                DonViTinh donViTinh = null;

                if (donViTinhStr != null) {
                    donViTinh = DonViTinh.valueOf(donViTinhStr);
                }

                SanPham sp = new SanPham(
                    maSP, tenSP, giaNhap, giaBan, soLuongTon, donViTinh
                );

                dsSP.add(sp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsSP;
    }

    // Tìm sản phẩm theo mã
    public SanPham timTheoMa(String maSP) throws SQLException {

        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        String sql = "SELECT maSP, tenSP, giaNhap, giaBan, soLuongTon, donViTinh "
                   + "FROM SanPham WHERE maSP = ?";

        try (PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, maSP);

            try (ResultSet rs = stm.executeQuery()) {

                if (rs.next()) {
                    String ma = rs.getString("maSP");
                    String ten = rs.getString("tenSP");
                    double giaNhap = rs.getDouble("giaNhap");
                    double giaBan = rs.getDouble("giaBan");
                    int soLuongTon = rs.getInt("soLuongTon");

                    String donViTinhStr = rs.getString("donViTinh");
                    DonViTinh donViTinh = null;

                    if (donViTinhStr != null) {
                        donViTinh = DonViTinh.valueOf(donViTinhStr);
                    }

                    return new SanPham(
                        ma, ten, giaNhap, giaBan, soLuongTon, donViTinh
                    );
                }
            }
        }

        return null;
    }

    // Thêm sản phẩm
    public boolean themSanPham(SanPham sp) {

        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        String sql = "INSERT INTO SanPham "
                   + "(maSP, tenSP, giaNhap, giaBan, soLuongTon, donViTinh) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, sp.getMaSP());
            stm.setString(2, sp.getTenSP());
            stm.setDouble(3, sp.getGiaNhap());
            stm.setDouble(4, sp.getGiaBan());
            stm.setInt(5, sp.getSoLuongTon());

            if (sp.getDonViTinh() != null) {
                stm.setString(6, sp.getDonViTinh().name());
            } else {
                stm.setString(6, "CAI");
            }

            return stm.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Phát sinh mã sản phẩm mới
    public String phatSinhMaSanPhamMoi(Loai loai) throws SQLException {

    
    	
    	 String tienTo = (loai == Loai.CHO) ? "SPCC" : "SPCM";

    	    ConnectDB.getInstance();
    	    Connection con = ConnectDB.getConnection();

    	    String sql = "SELECT MAX(maSP) FROM SanPham WHERE maSP LIKE ?";
    	    int so = 0;

    	    try (PreparedStatement ps = con.prepareStatement(sql)) {
    	        ps.setString(1, tienTo + "%");
    	        try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) {
    	                String maCu = rs.getString(1);          // null nếu chưa có sản phẩm nào
    	                if (maCu != null) {
    	                    so = Integer.parseInt(maCu.substring(4));   // lấy 4 số cuối
    	                }
    	            }
    	        }
    	    }
    	    return String.format("%s%04d", tienTo, so + 1);
    }

    // Xóa sản phẩm
    public boolean xoaSanPham(String maSP) throws SQLException {
        String sql = "DELETE FROM SanPham WHERE maSP = ?";
        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        }
    }
    public boolean suaSanPham(SanPham sp) {
        String sql = "UPDATE SanPham SET tenSP=?, giaNhap=?, giaBan=?, soLuongTon=?, donViTinh=? WHERE maSP=?";
        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
            ps.setNString(1, sp.getTenSP());
            ps.setDouble(2, sp.getGiaNhap());
            ps.setDouble(3, sp.getGiaBan());
            ps.setInt(4, sp.getSoLuongTon());
            ps.setString(5, sp.getDonViTinh().name());   // đổi cho giống cách themSanPham lưu
            ps.setString(6, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
