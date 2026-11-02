
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.TrangThai;

public class HoaDonDAO {

    // Lay danh sach tat ca hoa don
    public List<Object[]> getAllHoaDon() throws SQLException {
        String sql = "SELECT hd.maHD, kh.tenKH, kh.soDienThoai, hd.ngayLap,"
                + " hd.tongTien, hd.trangThaiHD"
                + " FROM HoaDon hd JOIN KhachHang kh ON kh.maKH = hd.maKH"
                + " ORDER BY hd.ngayLap DESC, hd.maHD DESC";

        List<Object[]> danhSach = new ArrayList<Object[]>();

        Connection connection = ConnectDB.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                danhSach.add(new Object[] {
                        resultSet.getString("maHD"),
                        resultSet.getString("tenKH"),
                        resultSet.getString("soDienThoai"),
                        resultSet.getDate("ngayLap"),
                        resultSet.getDouble("tongTien"),
                        TrangThai.valueOf(resultSet.getString("trangThaiHD"))
                });
            }
        }

        return danhSach;
    }

    // Tim kiem va loc hoa don theo ma, trang thai, khoang ngay
    public List<Object[]> timKiemHoaDon(String maHD, String trangThai,
            Date tuNgay, Date denNgay) throws SQLException {

        StringBuilder sql = new StringBuilder(
                "SELECT hd.maHD, kh.tenKH, kh.soDienThoai, hd.ngayLap,"
                + " hd.tongTien, hd.trangThaiHD"
                + " FROM HoaDon hd JOIN KhachHang kh ON kh.maKH = hd.maKH"
                + " WHERE 1=1");

        String tuKhoa = maHD == null ? "" : maHD.trim();

        // Loc theo ma hoa don
        if (!tuKhoa.isEmpty()) {
            sql.append(" AND hd.maHD LIKE ?");
        }

        // Loc theo trang thai
        if (trangThai != null) {
            sql.append(" AND hd.trangThaiHD = ?");
        }

        // Loc tu ngay
        if (tuNgay != null) {
            sql.append(" AND hd.ngayLap >= ?");
        }

        // Loc den ngay (bao gom ca ngay ket thuc)
        if (denNgay != null) {
            sql.append(" AND hd.ngayLap < DATEADD(DAY, 1, ?)");
        }

        sql.append(" ORDER BY hd.ngayLap DESC, hd.maHD DESC");

        List<Object[]> danhSach = new ArrayList<Object[]>();

        Connection connection = ConnectDB.getConnection();

        try (PreparedStatement statement =
                connection.prepareStatement(sql.toString())) {

            int i = 1;

            if (!tuKhoa.isEmpty()) {
                statement.setString(i++, "%" + tuKhoa + "%");
            }

            if (trangThai != null) {
                statement.setString(i++, trangThai);
            }

            if (tuNgay != null) {
                statement.setDate(i++, tuNgay);
            }

            if (denNgay != null) {
                statement.setDate(i++, denNgay);
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    danhSach.add(new Object[] {
                            resultSet.getString("maHD"),
                            resultSet.getString("tenKH"),
                            resultSet.getString("soDienThoai"),
                            resultSet.getDate("ngayLap"),
                            resultSet.getDouble("tongTien"),
                            TrangThai.valueOf(resultSet.getString("trangThaiHD"))
                    });
                }
            }
        }

        return danhSach;
    }
}
