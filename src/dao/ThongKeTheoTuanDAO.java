package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import connectDB.ConnectDB;

public class ThongKeTheoTuanDAO {

    // =========================================================================
    // 1. PHẦN TỔNG QUAN (Áp dụng cho toàn bộ Hóa Đơn)
    // =========================================================================

    public double getTongDoanhThuTQ(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT ISNULL(SUM(tongTien), 0) " +
                     "FROM HoaDon " +
                     "WHERE trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getDouble(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tong;
    }

    public double getTongChiPhiTQ(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT ISNULL(SUM(CT.soLuong * SP.giaNhap), 0) " +
                     "FROM ChiTietHoaDon CT " +
                     "JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "JOIN SanPham SP ON CT.maSP = SP.maSP " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getDouble(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tong;
    }

    public double getLoiNhuanTQ(LocalDate start, LocalDate end) {
        return getTongDoanhThuTQ(start, end) - getTongChiPhiTQ(start, end);
    }

    public int getSoLuongKhachHangTQ(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT maKH) FROM HoaDon " +
                     "WHERE trangThaiHD = 'DA_THANH_TOAN' AND CAST(ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
    
    public int getTongHoaDonTQ(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(maHD) FROM HoaDon " +
                     "WHERE trangThaiHD = 'DA_THANH_TOAN' AND CAST(ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    public Map<LocalDate, Double> getDoanhThuTheoNgayChartTQ(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new LinkedHashMap<>();
        String sql = "SELECT CAST(ngayLap AS DATE) AS Ngay, ISNULL(SUM(tongTien), 0) AS DoanhThu " +
                     "FROM HoaDon WHERE trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(ngayLap AS DATE) ORDER BY Ngay ASC";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) data.put(rs.getDate("Ngay").toLocalDate(), rs.getDouble("DoanhThu"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return data;
    }

    public Map<LocalDate, Double> getChiPhiTheoNgayChartTQ(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new LinkedHashMap<>();
        String sql = "SELECT CAST(HD.ngayLap AS DATE) AS Ngay, ISNULL(SUM(CT.soLuong * SP.giaNhap), 0) AS ChiPhi " +
                     "FROM ChiTietHoaDon CT JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "JOIN SanPham SP ON CT.maSP = SP.maSP " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(HD.ngayLap AS DATE) ORDER BY Ngay ASC";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) data.put(rs.getDate("Ngay").toLocalDate(), rs.getDouble("ChiPhi"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return data;
    }
    
    public Map<LocalDate, Double> getLoiNhuanTheoNgayChartTQ(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> mapLoiNhuan = new LinkedHashMap<>();
        Map<LocalDate, Double> mapDoanhThu = getDoanhThuTheoNgayChartTQ(start, end);
        Map<LocalDate, Double> mapChiPhi = getChiPhiTheoNgayChartTQ(start, end);
        
        // chay vong lap ngay dau den ngay cuoi
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            double dt = mapDoanhThu.getOrDefault(date, 0.0);
            double cp = mapChiPhi.getOrDefault(date, 0.0);
            mapLoiNhuan.put(date, dt - cp); // loi nhuan
        }
        return mapLoiNhuan;
    }


 // =========================================================================
    // 2. PHẦN SẢN PHẨM (Chỉ lọc các chi tiết hóa đơn có maSP IS NOT NULL)
    // =========================================================================

    // doanh thu san pham
    public double getTongDoanhThuSP(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT ISNULL(SUM(CT.thanhTien), 0) " +
                     "FROM ChiTietHoaDon CT JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maSP IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getDouble(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tong;
    }

    // chi phi san pham
    public double getTongChiPhiSP(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT ISNULL(SUM(CT.soLuong * SP.giaNhap), 0) " +
                     "FROM ChiTietHoaDon CT " +
                     "JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "JOIN SanPham SP ON CT.maSP = SP.maSP " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maSP IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getDouble(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tong;
    }

    // loi nhuan sp
    public double getLoiNhuanSP(LocalDate start, LocalDate end) {
        return getTongDoanhThuSP(start, end) - getTongChiPhiSP(start, end);
    }

    // khach hang mua sp
    public int getKhachHangMuaSP(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT HD.maKH) " +
                     "FROM HoaDon HD JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maSP IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // hoa don chua san pham
    public int getHoaDonSP(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT HD.maHD) " +
                     "FROM HoaDon HD JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maSP IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // so luong sp ban ra
    public int getSoSpBanRa(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT ISNULL(SUM(CT.soLuong), 0) " +
                     "FROM ChiTietHoaDon CT JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maSP IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // linechart san sp
    public Map<LocalDate, Double> getDoanhThuTheoNgayChartSP(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new LinkedHashMap<>();
        String sql = "SELECT CAST(HD.ngayLap AS DATE) AS Ngay, ISNULL(SUM(CT.thanhTien), 0) AS DoanhThu " +
                     "FROM ChiTietHoaDon CT JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maSP IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(HD.ngayLap AS DATE) ORDER BY Ngay ASC";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) data.put(rs.getDate("Ngay").toLocalDate(), rs.getDouble("DoanhThu"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return data;
    }

    // linechart sp
    public Map<LocalDate, Double> getChiPhiTheoNgayChartSP(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new LinkedHashMap<>();
        String sql = "SELECT CAST(HD.ngayLap AS DATE) AS Ngay, ISNULL(SUM(CT.soLuong * SP.giaNhap), 0) AS ChiPhi " +
                     "FROM ChiTietHoaDon CT JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "JOIN SanPham SP ON CT.maSP = SP.maSP " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maSP IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(HD.ngayLap AS DATE) ORDER BY Ngay ASC";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) data.put(rs.getDate("Ngay").toLocalDate(), rs.getDouble("ChiPhi"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return data;
    }

    // linechart sp
    public Map<LocalDate, Double> getLoiNhuanTheoNgayChartSP(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> mapLoiNhuan = new LinkedHashMap<>();
        Map<LocalDate, Double> mapDoanhThu = getDoanhThuTheoNgayChartSP(start, end);
        Map<LocalDate, Double> mapChiPhi = getChiPhiTheoNgayChartSP(start, end);
        
        // vong lap ngay dau den ngay cuoi
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            double dt = mapDoanhThu.getOrDefault(date, 0.0);
            double cp = mapChiPhi.getOrDefault(date, 0.0);
            mapLoiNhuan.put(date, dt - cp); //loi nhuan
        }
        return mapLoiNhuan;
    }


 // =========================================================================
    // 3. PHẦN DỊCH VỤ
    // =========================================================================

    // doanh thu dv
    public double getTongDoanhThuDV(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT ISNULL(SUM(CT.thanhTien), 0) " +
                     "FROM ChiTietHoaDon CT JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maDV IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getDouble(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tong;
    }

    // so khach hang dung dv
    public int getKhachHangDungDV(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT HD.maKH) " +
                     "FROM HoaDon HD JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maDV IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // so hoa don chua dv
    public int getHoaDonDV(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT HD.maHD) " +
                     "FROM HoaDon HD JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maDV IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // tong so dv da dung
    public int getSoLuotDV(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT ISNULL(SUM(CT.soLuong), 0) " +
                     "FROM ChiTietHoaDon CT JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' AND CT.maDV IS NOT NULL " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // barchart top 5 dv
    public Map<String, Double> getDoanhThuTop5DichVu(LocalDate start, LocalDate end) {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT TOP 5 DV.tenDV, ISNULL(SUM(CT.thanhTien), 0) AS DoanhThu " +
                     "FROM ChiTietHoaDon CT " +
                     "JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "JOIN DichVu DV ON CT.maDV = DV.maDV " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY DV.tenDV " +
                     "ORDER BY DoanhThu DESC"; 
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenDV"), rs.getDouble("DoanhThu"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return data;
    }
    
 // =========================================================================
    // PHẦN 4: HỖ TRỢ ĐẾM LƯỢT MUA SẢN PHẨM & DỊCH VỤ tim min max
    // =========================================================================

    private Map<String, Integer> getSoLuotMuaTungSP(LocalDate start, LocalDate end) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT sp.tenSP, SUM(ct.soLuong) AS TongSL " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE hd.ngayLap BETWEEN ? AND ? " +
                     "GROUP BY sp.tenSP " +
                     "ORDER BY TongSL DESC";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    private Map<String, Integer> getSoLuotDungTungDV(LocalDate start, LocalDate end) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT dv.tenDV, SUM(ct.soLuong) AS TongSL " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN DichVu dv ON ct.maDV = dv.maDV " +
                     "WHERE hd.ngayLap BETWEEN ? AND ? " +
                     "GROUP BY dv.tenDV " +
                     "ORDER BY TongSL DESC";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    // =========================================================================
    // PHẦN 5: TẠO ĐỀ XUẤT & NHẬN XÉT TỰ ĐỘNG THEO TUẦN
    // =========================================================================

    public String taoDeXuatTQ(LocalDate start, LocalDate end) {
        double dtTQ = getTongDoanhThuTQ(start, end);
        double lnTQ = getLoiNhuanTQ(start, end);
        int khTQ = getSoLuongKhachHangTQ(start, end);
        int hdTQ = getTongHoaDonTQ(start, end);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        String khoangTuan = start.format(fmt) + " - " + end.format(fmt);

        if (hdTQ == 0) {
            return "Tuần (" + khoangTuan + ") chưa có giao dịch phát sinh. Cần triển khai ngay chương trình khuyến mãi cuối tuần hoặc nhắn tin chăm sóc khách hàng cũ.";
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        double aov = dtTQ / hdTQ;
        double tySuatLN = dtTQ > 0 ? (lnTQ / dtTQ) * 100 : 0;
        double tanSuat = khTQ > 0 ? (double) hdTQ / khTQ : 0;

        StringBuilder dx = new StringBuilder();
        dx.append("[*] ĐÁNH GIÁ HIỆU QUẢ TUẦN (").append(khoangTuan).append("):\n");
        dx.append("  - Tỷ suất lợi nhuận: ").append(String.format("%.1f%%", tySuatLN));
        if (tySuatLN >= 60) dx.append(" (Xuất Sắc)\n");
        else if (tySuatLN >= 40) dx.append(" (Tốt)\n");
        else if (tySuatLN >= 20) dx.append(" (Trung Bình)\n");
        else if (tySuatLN > 0) dx.append(" (Cần Cải Thiện)\n");
        else dx.append(" (Đang Lỗ)\n");

        dx.append("  - Khách chi tiêu TB: ").append(df.format(aov)).append("/đơn.\n");
        dx.append("  - Tần suất mua trong tuần: ").append(String.format("%.1f", tanSuat)).append(" lượt/khách.\n\n");

        dx.append("[!] ĐỀ XUẤT HÀNH ĐỘNG TUẦN TỚI:\n");
        dx.append("\n1. Về Tỷ Suất Lợi Nhuận:\n");
        if (tySuatLN >= 60) {
            dx.append("   + Lợi nhuận tuần rất khỏe. Tiếp tục giữ vững phong độ và đẩy mạnh bài đăng Fanpage cho tuần tới.\n");
        } else if (tySuatLN >= 40) {
            dx.append("   + Biên lợi nhuận ổn định. Duy trì tốt sự cân bằng giữa bán lẻ sản phẩm và dịch vụ Spa.\n");
        } else if (tySuatLN >= 20) {
            dx.append("   + Lợi nhuận ở mức trung bình do thiên về bán hàng hóa. Tuần tới nhắc nhân viên tích cực tư vấn thêm dịch vụ Spa cho khách.\n");
        } else if (tySuatLN > 0) {
            dx.append("   + BÁO ĐỘNG VÀNG: Lãi tuần này quá mỏng. Kiểm tra lại các chương trình giảm giá hoặc thất thoát hàng hóa ngay.\n");
        } else {
            dx.append("   + BÁO ĐỘNG ĐỎ: Lợi nhuận âm! Cần rà soát ngay giá nhập và tạm dừng các mã giảm giá sâu.\n");
        }

        dx.append("\n2. Về Mức Chi Tiêu:\n");
        if (aov < 150000) {
            dx.append("   + Đơn hàng nhỏ lẻ nhiều. Tuần tới nên đặt kệ Combo (Hạt + Súp thưởng) ngay quầy thu ngân để kéo giá trị đơn lên.\n");
        } else if (aov < 350000) {
            double mucKichCau = aov + 100000;
            dx.append("   + Mức chi tiêu tầm trung. Gợi ý tặng quà nhỏ hoặc Freeship cho đơn từ ").append(df.format(mucKichCau)).append(" vào dịp cuối tuần.\n");
        } else {
            dx.append("   + Khách chịu chi tốt trong tuần. Chú ý chăm sóc hậu mãi và ưu tiên đặt lịch Spa cho nhóm khách này.\n");
        }

        return dx.toString();
    }

    public String taoDeXuatSP(LocalDate start, LocalDate end) {
        double dtTQ = getTongDoanhThuTQ(start, end);
        double dtSP = getTongDoanhThuSP(start, end);
        double lnSP = getLoiNhuanSP(start, end);
        int hdSP = getHoaDonSP(start, end);
        int slSP = getSoSpBanRa(start, end);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        String khoangTuan = start.format(fmt) + " - " + end.format(fmt);

        if (dtSP == 0 || hdSP == 0) {
            return "Tuần (" + khoangTuan + ") chưa có doanh thu bán lẻ. Cần kiểm tra lại trưng bày kệ hàng và chạy Flash Sale cuối tuần.";
        }

        double tySuatLnSP = (lnSP / dtSP) * 100;
        double spPerDon = (double) slSP / hdSP;
        double tyTrongSP = dtTQ > 0 ? (dtSP / dtTQ) * 100 : 100;

        // Tìm sản phẩm có số lượt mua MAX và MIN
        Map<String, Integer> mapSL = getSoLuotMuaTungSP(start, end);
        String spMax = "";
        int slMax = Integer.MIN_VALUE;
        String spMin = "";
        int slMin = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : mapSL.entrySet()) {
            String ten = entry.getKey();
            int sl = entry.getValue();
            if (sl > slMax) {
                slMax = sl;
                spMax = ten;
            }
            if (sl < slMin) {
                slMin = sl;
                spMin = ten;
            }
        }

        StringBuilder dx = new StringBuilder();
        dx.append("[*] ĐÁNH GIÁ SẢN PHẨM TUẦN (").append(khoangTuan).append("):\n");
        dx.append("  - Đóng góp doanh thu: ").append(String.format("%.1f%%", tyTrongSP)).append(" tổng DT.\n");
        dx.append("  - Tỷ suất LN sản phẩm: ").append(String.format("%.1f%%", tySuatLnSP)).append(".\n");
        dx.append("  - Trung bình: ").append(String.format("%.1f", spPerDon)).append(" món/đơn.\n");
        if (!spMax.isEmpty()) {
            dx.append("  - Bán chạy nhất: ").append(spMax).append(" (").append(slMax).append(" lượt).\n");
            if (!spMin.equals(spMax)) {
                dx.append("  - Bán chậm nhất: ").append(spMin).append(" (").append(slMin).append(" lượt).\n");
            }
        }

        dx.append("\n[!] ĐỀ XUẤT HÀNG HÓA TUẦN TỚI:\n");
        if (!spMax.isEmpty()) {
            dx.append("\n1. Nhóm bán chạy nhất ('").append(spMax).append("'):\n");
            dx.append("   + Sức mua tốt (").append(slMax).append(" lượt). Cần tiếp tục duy trì trưng bày ở vị trí đẹp và kiểm tra tồn kho để nhập bù kịp thời cho tuần tới.\n");
        }
        if (!spMin.isEmpty() && !spMin.equals(spMax)) {
            dx.append("\n2. Nhóm bán chậm nhất ('").append(spMin).append("'):\n");
            dx.append("   + Lượt mua còn ít (").append(slMin).append(" lượt). Tuần tới nên tăng các đợt khuyến mãi, giảm giá nhanh hoặc làm quà tặng kèm khi khách mua '")
              .append(spMax).append("' và các dịch vụ Spa khác.\n");
        }

        return dx.toString();
    }

    public String taoDeXuatDV(LocalDate start, LocalDate end) {
        double dtTQ = getTongDoanhThuTQ(start, end);
        double dtDV = getTongDoanhThuDV(start, end);
        int slDV = getSoLuotDV(start, end);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        String khoangTuan = start.format(fmt) + " - " + end.format(fmt);

        if (dtDV == 0 || slDV == 0) {
            return "Tuần (" + khoangTuan + ") chưa có lượt đặt dịch vụ nào. Khuyên gọi điện nhắc lịch tắm/tỉa lông cho các khách cũ.";
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        double tyTrongDV = dtTQ > 0 ? (dtDV / dtTQ) * 100 : 100;
        double tbLuot = dtDV / slDV;

        // Tìm dịch vụ có số lượt dùng MAX và MIN
        Map<String, Integer> mapSL = getSoLuotDungTungDV(start, end);
        String dvMax = "";
        int slMax = Integer.MIN_VALUE;
        String dvMin = "";
        int slMin = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : mapSL.entrySet()) {
            String ten = entry.getKey();
            int sl = entry.getValue();
            if (sl > slMax) {
                slMax = sl;
                dvMax = ten;
            }
            if (sl < slMin) {
                slMin = sl;
                dvMin = ten;
            }
        }

        StringBuilder dx = new StringBuilder();
        dx.append("[*] ĐÁNH GIÁ DỊCH VỤ TUẦN (").append(khoangTuan).append("):\n");
        dx.append("  - Đóng góp doanh thu: ").append(String.format("%.1f%%", tyTrongDV)).append(" tổng DT.\n");
        dx.append("  - Chi tiêu TB: ").append(df.format(tbLuot)).append("/lượt.\n");
        if (!dvMax.isEmpty()) {
            dx.append("  - Dùng nhiều nhất: ").append(dvMax).append(" (").append(slMax).append(" lượt).\n");
            if (!dvMin.equals(dvMax)) {
                dx.append("  - Dùng ít nhất: ").append(dvMin).append(" (").append(slMin).append(" lượt).\n");
            }
        }

        dx.append("\n[!] ĐỀ XUẤT DỊCH VỤ TUẦN TỚI:\n");
        if (!dvMax.isEmpty()) {
            dx.append("\n1. Dịch vụ mũi nhọn ('").append(dvMax).append("'):\n");
            dx.append("   + Đang thu hút khách nhất tuần (").append(slMax).append(" lượt). Cần tiếp tục duy trì chất lượng phục vụ và chuẩn bị đủ nhân sự trực vào các ngày cuối tuần.\n");
        }
        if (!dvMin.isEmpty() && !dvMin.equals(dvMax)) {
            dx.append("\n2. Dịch vụ ít khách ('").append(dvMin).append("'):\n");
            dx.append("   + Lượt sử dụng còn thấp (").append(slMin).append(" lượt). Tuần tới nên tăng khuyến mãi giờ vàng (Thứ 2 - Thứ 5) hoặc tặng kèm/giảm 50% khi khách đặt '")
              .append(dvMax).append("' và mua sản phẩm.\n");
        }

        return dx.toString();
    }
}