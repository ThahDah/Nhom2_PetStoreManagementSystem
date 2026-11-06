
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.HoaDon;
import entity.KhachHang;
import entity.TrangThai;
import entity.PhuongThucTT;

public class HoaDonDAO {

    // Lay danh sach tat ca hoa don
    public List<HoaDon> getAllHoaDon() throws SQLException {
        String sql = "SELECT hd.maHD, kh.maKH, kh.tenKH, kh.soDienThoai, "
                + "kh.email, kh.diemTichLuy, hd.ngayLap, "
                + "hd.tongTien, hd.giamGia, hd.trangThaiHD, hd.phuongThucTT "
                + "FROM HoaDon hd JOIN KhachHang kh ON kh.maKH = hd.maKH "
                + "ORDER BY hd.ngayLap DESC, hd.maHD DESC";

        List<HoaDon> danhSach = new ArrayList<>();
        Connection connection = ConnectDB.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                KhachHang kh = new KhachHang(
                        resultSet.getString("maKH"),
                        resultSet.getString("tenKH"),
                        resultSet.getString("soDienThoai"),
                        resultSet.getString("email"),
                        resultSet.getInt("diemTichLuy")
                );
                String phuongThuc = resultSet.getString("phuongThucTT");
                HoaDon hd = new HoaDon(
                        resultSet.getString("maHD"),
                        resultSet.getTimestamp("ngayLap").toLocalDateTime().toLocalDate(),
                        resultSet.getDouble("tongTien"),
                        resultSet.getDouble("giamGia"),
                        TrangThai.valueOf(resultSet.getString("trangThaiHD")),
                        phuongThuc == null ? null: PhuongThucTT.valueOf(phuongThuc),
                        kh
                );
                danhSach.add(hd);
            }
        }
        return danhSach;
    }
    // Tim kiem va loc hoa don theo ma, trang thai, khoang ngay
    public List<HoaDon> timKiemHoaDon(String maHD, String trangThai,
            Date tuNgay, Date denNgay) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT hd.maHD, kh.maKH, kh.tenKH, kh.soDienThoai, "
                + "kh.email, kh.diemTichLuy, hd.ngayLap, "
                + "hd.tongTien, hd.giamGia, hd.trangThaiHD, hd.phuongThucTT "
                + "FROM HoaDon hd JOIN KhachHang kh ON kh.maKH = hd.maKH "
                + "WHERE 1=1");
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
        // Loc den ngay
        if (denNgay != null) {
            sql.append(" AND hd.ngayLap < DATEADD(DAY, 1, ?)");
        }
        sql.append(" ORDER BY hd.ngayLap DESC, hd.maHD DESC");
        List<HoaDon> danhSach = new ArrayList<>();
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
                    KhachHang kh = new KhachHang(
                            resultSet.getString("maKH"),
                            resultSet.getString("tenKH"),
                            resultSet.getString("soDienThoai"),
                            resultSet.getString("email"),
                            resultSet.getInt("diemTichLuy")
                    );

                    String phuongThuc = resultSet.getString("phuongThucTT");
                    HoaDon hd = new HoaDon(
                            resultSet.getString("maHD"),
                            resultSet.getTimestamp("ngayLap").toLocalDateTime().toLocalDate(),
                            resultSet.getDouble("tongTien"),
                            resultSet.getDouble("giamGia"),
                            TrangThai.valueOf(resultSet.getString("trangThaiHD")),
                            phuongThuc == null ? null: PhuongThucTT.valueOf(phuongThuc),
                            kh
                    );
                    danhSach.add(hd);
                }
            }
        }

        return danhSach;
    }
}
