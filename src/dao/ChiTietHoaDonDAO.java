
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.DichVu;
import entity.HoaDon;
import entity.SanPham;

public class ChiTietHoaDonDAO {
	//tim theo ma hoadon
    public ArrayList<ChiTietHoaDon> timTheoHoaDon(String maHD)
            throws SQLException {

        ArrayList<ChiTietHoaDon> danhSach = new ArrayList<>();

        String sql = "SELECT ct.maHD, ct.maSP, ct.maDV, "
                + "ct.soLuong, ct.donGia, ct.thanhTien, "
                + "sp.tenSP, dv.tenDV "
                + "FROM ChiTietHoaDon ct "
                + "LEFT JOIN SanPham sp ON ct.maSP = sp.maSP "
                + "LEFT JOIN DichVu dv ON ct.maDV = dv.maDV "
                + "WHERE ct.maHD = ? "
                + "ORDER BY ct.maSP, ct.maDV";

        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHD);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setMaHD(rs.getString("maHD"));

                    SanPham sanPham = null;
                    DichVu dichVu = null;

                    if (rs.getString("maSP") != null) {
                        sanPham = new SanPham();
                        sanPham.setMaSP(rs.getString("maSP"));
                        sanPham.setTenSP(rs.getString("tenSP"));
                    }

                    if (rs.getString("maDV") != null) {
                        dichVu = new DichVu();
                        dichVu.setMaDV(rs.getString("maDV"));
                        dichVu.setTenDV(rs.getString("tenDV"));
                    }

                    ChiTietHoaDon chiTiet = new ChiTietHoaDon();

                    chiTiet.setHoaDon(hoaDon);
                    chiTiet.setSanPham(sanPham);
                    chiTiet.setDichVu(dichVu);
                    chiTiet.setSoLuong(rs.getInt("soLuong"));
                    chiTiet.setDonGia(rs.getDouble("donGia"));
                    chiTiet.setThanhTien(rs.getDouble("thanhTien"));

                    danhSach.add(chiTiet);
                }
            }
        }

        return danhSach;
    }
}
