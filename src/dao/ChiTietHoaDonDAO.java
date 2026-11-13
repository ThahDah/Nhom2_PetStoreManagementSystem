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
import entity.KhachHang;
import entity.TrangThai;
import entity.PhuongThucTT;
import entity.ThuCung;
public class ChiTietHoaDonDAO {
    // Lay danh sach chi tiet san pham hoac dich vu cua hoa don
    public ArrayList<ChiTietHoaDon> layDanhSachChiTietHoaDon(String maHD)
            throws SQLException {
        ArrayList<ChiTietHoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT ct.maHD, ct.maSP, ct.maDV, ct.maThuCung, "
                + "ct.soLuong, ct.donGia, "
                + "sp.tenSP, dv.tenDV, dv.giaTieuChuan, "
                + "tc.tenThuCung "
                + "FROM ChiTietHoaDon ct "
                + "LEFT JOIN SanPham sp ON ct.maSP = sp.maSP "
                + "LEFT JOIN DichVu dv ON ct.maDV = dv.maDV "
                + "LEFT JOIN ThuCung tc ON ct.maThuCung = tc.maThuCung "
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
                        dichVu.setGiaTieuChuan(rs.getDouble("giaTieuChuan"));
                    }
                    // Lay thong tin thu cung
                    ThuCung thuCung = null;
                    if (rs.getString("maThuCung") != null) {

                        thuCung = new ThuCung();
                        thuCung.setMaThuCung(rs.getString("maThuCung"));
                        thuCung.setTenThuCung(rs.getString("tenThuCung"));

                    }
                    ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                    chiTiet.setHoaDon(hoaDon);
                    chiTiet.setSanPham(sanPham);
                    chiTiet.setDichVu(dichVu);
                    chiTiet.setThuCung(thuCung);
                    chiTiet.setSoLuong(rs.getInt("soLuong"));
                    chiTiet.setDonGia(rs.getDouble("donGia"));
                    danhSach.add(chiTiet);

                }

            }

        }

        return danhSach;

    }
    public HoaDon timHoaDonTheoMa(String maHD) throws SQLException {
        String sql = "SELECT hd.maHD, hd.ngayLap, hd.giamGia, "
                + "hd.trangThaiHD, hd.phuongThucTT, "
                + "kh.maKH, kh.tenKH, kh.soDienThoai, "
                + "kh.email, kh.diemTichLuy "
                + "FROM HoaDon hd "
                + "JOIN KhachHang kh ON hd.maKH = kh.maKH "
                + "WHERE hd.maHD = ?";
        Connection con = ConnectDB.getConnection();
        HoaDon hoaDon = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("tenKH"),
                            rs.getString("soDienThoai"),
                            rs.getString("email"),
                            rs.getInt("diemTichLuy"));
                    String phuongThuc = rs.getString("phuongThucTT");

                    hoaDon = new HoaDon(
                            rs.getString("maHD"),
                            rs.getTimestamp("ngayLap").toLocalDateTime().toLocalDate(),
                            rs.getDouble("giamGia"),
                            TrangThai.valueOf(rs.getString("trangThaiHD")),
                            phuongThuc == null ? null : PhuongThucTT.valueOf(phuongThuc),
                            kh,
                            null);
                }
            }
        }

        if (hoaDon == null) return null;

        // Nạp chi tiết sau khi đã đóng ResultSet ở trên
        hoaDon.setDsChiTiet(layDanhSachChiTietHoaDon(maHD));
        return hoaDon;
    }

}
