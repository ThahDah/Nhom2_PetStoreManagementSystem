package dao;

import java.sql.Connection;

import java.sql.Date;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Timestamp;

import java.sql.Types;

import java.time.LocalDate;

import java.time.LocalTime;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import java.util.List;

import connectDB.ConnectDB;

import entity.ChiTietHoaDon;

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


                        resultSet.getDouble("giamGia"),

                        TrangThai.valueOf(resultSet.getString("trangThaiHD")),

                        phuongThuc == null ? null : PhuongThucTT.valueOf(phuongThuc),

                        kh,

                        null         

                );

                hd.setTongTien(resultSet.getDouble("tongTien"));


                danhSach.add(hd);

            }

        }

        return danhSach;

    }

    // Tim kiem va loc hoa don theo ma, trang thai, khoang ngay

    public List<HoaDon> timKiemHoaDon(String maHD, String trangThai,

            LocalDate tuNgay, LocalDate denNgay,String loaiHoaDon) throws SQLException {

        StringBuilder sql = new StringBuilder(

                "SELECT hd.maHD, kh.maKH, kh.tenKH, kh.soDienThoai, "

                + "kh.email, kh.diemTichLuy, hd.ngayLap, "

                + "hd.tongTien, hd.giamGia, hd.trangThaiHD, hd.phuongThucTT "

                + "FROM HoaDon hd JOIN KhachHang kh ON kh.maKH = hd.maKH "

                + "WHERE 1=1");

        String tuKhoa = maHD == null ? "" : maHD.trim();

	     //Loc theo ma hd hoac sdt khach hang

	     if (!tuKhoa.isEmpty()) {

	         sql.append(" AND (hd.maHD LIKE ? OR kh.soDienThoai LIKE ?)");

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
        //Loc theo loai hoa don
        if (loaiHoaDon != null) {
            sql.append(" AND hd.maHD LIKE ?");
        }

        sql.append(" ORDER BY hd.ngayLap DESC, hd.maHD DESC");

        List<HoaDon> danhSach = new ArrayList<>();

        Connection connection = ConnectDB.getConnection();

        try (PreparedStatement statement =

                connection.prepareStatement(sql.toString())) {

            int i = 1;

            if (!tuKhoa.isEmpty()) {

                statement.setString(i++, "%" + tuKhoa + "%");

                statement.setString(i++, "%" + tuKhoa + "%");

            }

            if (trangThai != null) {

                statement.setString(i++, trangThai);

            }

            if (tuNgay != null) {

                statement.setDate(i++, Date.valueOf(tuNgay));

            }

            if (denNgay != null) {

                statement.setDate(i++, Date.valueOf(denNgay));

            }
            if (loaiHoaDon != null) {
                statement.setString(i++, loaiHoaDon + "%");
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

                            resultSet.getDouble("giamGia"),

                            TrangThai.valueOf(resultSet.getString("trangThaiHD")),

                            phuongThuc == null ? null : PhuongThucTT.valueOf(phuongThuc),

                            kh,

                            null         

                    );
                    hd.setTongTien(resultSet.getDouble("tongTien"));


                    danhSach.add(hd);

                }

            }

        }

        return danhSach;

    }

    public void taoHoaDon(HoaDon hd, List<ChiTietHoaDon> dsCT, long diemDung,

            String loai, String maLH) throws SQLException {

        if (hd.getKhachHang() == null) throw new SQLException("Hóa đơn phải có khách hàng!");

        if (hd.getNhanVien() == null) throw new SQLException("Hóa đơn phải có nhân viên lập!");
        if (dsCT == null || dsCT.isEmpty()) throw new SQLException("Hóa đơn phải có ít nhất một chi tiết!");
        hd.setDsChiTiet(new ArrayList<>(dsCT));  
    
        for (ChiTietHoaDon ct : dsCT) {
            if (ct.getSoLuong() <= 0) throw new SQLException("Số lượng phải lớn hơn 0!");
            if (ct.getDonGia() < 0)   throw new SQLException("Đơn giá không hợp lệ!");
        }
        if (hd.getGiamGia() < 0 || hd.getGiamGia() > hd.getTongTienHang()) {
            throw new SQLException("Giảm giá không hợp lệ!");
        }
        double tongTien = hd.tinhTongTien(); 
        hd.setTongTien(tongTien);
        boolean daThanhToan = hd.getTrangThaiHD() == TrangThai.DA_THANH_TOAN;
        Connection con = ConnectDB.getConnection();

        boolean autoCu = con.getAutoCommit();

        try {

            con.setAutoCommit(false);

            // 1. Hóa đơn

            hd.setMaHD(phatSinhMaHD(con, loai, hd.getNgayLap()));

            String sqlHD = "INSERT INTO HoaDon (maHD, maKH, maNV, ngayLap, tongTien, giamGia, trangThaiHD, phuongThucTT) "

                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stm = con.prepareStatement(sqlHD)) {

                stm.setString(1, hd.getMaHD());

                stm.setString(2, hd.getKhachHang().getMaKH());

                stm.setString(3, hd.getNhanVien().getMaNV());

                stm.setTimestamp(4, Timestamp.valueOf(hd.getNgayLap().atTime(LocalTime.now())));
                stm.setDouble(5, tongTien);

                stm.setDouble(6, hd.getGiamGia());

                stm.setString(7, hd.getTrangThaiHD().name());

                if (hd.getPhuongThucTT() != null) stm.setString(8, hd.getPhuongThucTT().name());

                else stm.setNull(8, Types.VARCHAR);

                stm.executeUpdate();

            }
            
            
            // 2. Chi tiết (+ trừ tồn kho nếu là sản phẩm)

            String sqlCT = "INSERT INTO ChiTietHoaDon (maHD, maSP, maDV, maThuCung, soLuong, donGia, thanhTien) "

                         + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            String sqlTon = "UPDATE SanPham SET soLuongTon = soLuongTon - ? WHERE maSP = ? AND soLuongTon >= ?";

            try (PreparedStatement stmCT = con.prepareStatement(sqlCT);

                 PreparedStatement stmTon = con.prepareStatement(sqlTon)) {

                for (ChiTietHoaDon ct : dsCT) {

                    stmCT.setString(1, hd.getMaHD());

                    if (ct.getSanPham() != null) stmCT.setString(2, ct.getSanPham().getMaSP());

                    else stmCT.setNull(2, Types.VARCHAR);

                    if (ct.getDichVu() != null) stmCT.setString(3, ct.getDichVu().getMaDV());

                    else stmCT.setNull(3, Types.VARCHAR);

                    if (ct.getThuCung() != null) stmCT.setString(4, ct.getThuCung().getMaThuCung());

                    else stmCT.setNull(4, Types.VARCHAR);

                    stmCT.setInt(5, ct.getSoLuong());

                    stmCT.setDouble(6, ct.getDonGia());

                    stmCT.setDouble(7, ct.getThanhTien());

                    stmCT.executeUpdate();

                    if (daThanhToan && ct.getSanPham() != null) {

                        stmTon.setInt(1, ct.getSoLuong());

                        stmTon.setString(2, ct.getSanPham().getMaSP());

                        stmTon.setInt(3, ct.getSoLuong());

                        if (stmTon.executeUpdate() == 0) {

                            throw new SQLException("Sản phẩm " + ct.getSanPham().getMaSP() + " không đủ tồn kho!");

                        }

                    }

                }

            }

            // 3. Trừ điểm
            if (daThanhToan &&  diemDung > 0) {

                String sqlDiem = "UPDATE KhachHang SET diemTichLuy = diemTichLuy - ? WHERE maKH = ? AND diemTichLuy >= ?";

                try (PreparedStatement stm = con.prepareStatement(sqlDiem)) {

                    stm.setLong(1, diemDung);

                    stm.setString(2, hd.getKhachHang().getMaKH());

                    stm.setLong(3, diemDung);

                    if (stm.executeUpdate() == 0) throw new SQLException("Điểm tích lũy của khách không đủ!");

                }

            }

            // 4. Hóa đơn dịch vụ: chuyển lịch hẹn sang đã tiếp nhận

            if (daThanhToan && maLH != null) {


                String sqlLH = "UPDATE LichHen SET trangThaiHen = ? WHERE maLH = ? AND trangThaiHen = ?";

                try (PreparedStatement stm = con.prepareStatement(sqlLH)) {

                    stm.setString(1, TrangThai.DA_TIEP_NHAN.name());

                    stm.setString(2, maLH);

                    stm.setString(3, TrangThai.CHO_DEN_HEN.name());

                    if (stm.executeUpdate() == 0) throw new SQLException("Lịch hẹn không còn ở trạng thái chờ đến hẹn!");

                }

                // Thú cưng của lịch hẹn này -> đang chăm sóc
                String sqlTC = "UPDATE ThuCung SET trangThaiThuCung = ? "
                             + "WHERE maThuCung IN (SELECT maThuCung FROM ChiTietLichHen WHERE maLH = ?)";
                try (PreparedStatement stm = con.prepareStatement(sqlTC)) {
                    stm.setString(1, TrangThai.DANG_CHAM_SOC.name());
                    stm.setString(2, maLH);
                    stm.executeUpdate();
                }
            }
        


    

          

            con.commit();

        } catch (SQLException e) {

            con.rollback();

            throw e;

        } finally {

            con.setAutoCommit(autoCu);

        }

    }

    // Mã HD: "HD" + loại (BH/DV) + ddMMyy + STT trong ngày (2 số)

    // Ví dụ: HDBH07112601

    private String phatSinhMaHD(Connection con, String loai, LocalDate ngay) throws SQLException {

     String prefix = "HD" + loai + ngay.format(DateTimeFormatter.ofPattern("ddMMyy"));  // 10 ký tự

     // Lấy số thứ tự lớn nhất trong ngày của loại hóa đơn này

     String sql = "SELECT MAX(maHD) FROM HoaDon WITH (UPDLOCK, HOLDLOCK) WHERE maHD LIKE ?";

     int stt = 1;

     try (PreparedStatement stm = con.prepareStatement(sql)) {

         stm.setString(1, prefix + "%");

         try (ResultSet rs = stm.executeQuery()) {

             if (rs.next() && rs.getString(1) != null) {

                 stt = Integer.parseInt(rs.getString(1).substring(10)) + 1;

             }

         }

     }

     if (stt > 99) {

         throw new SQLException("Đã vượt quá 99 hóa đơn trong ngày!");

     }

     return prefix + String.format("%02d", stt);

 }

}
