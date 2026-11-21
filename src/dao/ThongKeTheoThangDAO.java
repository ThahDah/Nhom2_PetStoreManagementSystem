package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import connectDB.ConnectDB; 

public class ThongKeTheoThangDAO {

    // =========================================================================
    // PHẦN 1: THỐNG KÊ TỔNG QUAN
    // =========================================================================
    
    public double getTongDoanhThuTQ(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT SUM(tongTien) FROM HoaDon WHERE ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) tong = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return tong;
    }

    public double getTongChiPhiTQ(LocalDate start, LocalDate end) {
        double chiPhi = 0;
        String sql = "SELECT SUM(ct.soLuong * sp.giaNhap) " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) chiPhi = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return chiPhi;
    }

    public double getLoiNhuanTQ(LocalDate start, LocalDate end) {
        return getTongDoanhThuTQ(start, end) - getTongChiPhiTQ(start, end);
    }

    public int getSoLuongKhachHangTQ(LocalDate start, LocalDate end) {
        int soKhach = 0;
        String sql = "SELECT COUNT(DISTINCT maKH) FROM HoaDon WHERE ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) soKhach = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soKhach;
    }

    public int getTongHoaDonTQ(LocalDate start, LocalDate end) {
        int soHD = 0;
        String sql = "SELECT COUNT(maHD) FROM HoaDon WHERE ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) soHD = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soHD;
    }

    // =========================================================================
    // CHART TỔNG QUAN
    // =========================================================================

    private Map<LocalDate, Double> createEmptyMap(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> map = new LinkedHashMap<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            map.put(date, 0.0);
        }
        return map;
    }

    public Map<LocalDate, Double> getDoanhThuTheoNgayChartTQ(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> map = createEmptyMap(start, end);
        String sql = "SELECT CAST(ngayLap AS DATE), SUM(tongTien) FROM HoaDon " +
                     "WHERE ngayLap BETWEEN ? AND ? GROUP BY CAST(ngayLap AS DATE)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getDate(1).toLocalDate(), rs.getDouble(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public Map<LocalDate, Double> getChiPhiTheoNgayChartTQ(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> map = createEmptyMap(start, end);
        String sql = "SELECT CAST(hd.ngayLap AS DATE), SUM(ct.soLuong * sp.giaNhap) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE hd.ngayLap BETWEEN ? AND ? GROUP BY CAST(hd.ngayLap AS DATE)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getDate(1).toLocalDate(), rs.getDouble(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public Map<LocalDate, Double> getLoiNhuanTheoNgayChartTQ(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> mapDT = getDoanhThuTheoNgayChartTQ(start, end);
        Map<LocalDate, Double> mapCP = getChiPhiTheoNgayChartTQ(start, end);
        Map<LocalDate, Double> mapLN = new LinkedHashMap<>();
        
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            double dt = mapDT.getOrDefault(date, 0.0);
            double cp = mapCP.getOrDefault(date, 0.0);
            mapLN.put(date, dt - cp);
        }
        return mapLN;
    }

    // =========================================================================
    // PHẦN 2: THỐNG KÊ SẢN PHẨM
    // =========================================================================

    public double getTongDoanhThuSP(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT SUM(ct.soLuong * sp.giaBan) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) tong = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return tong;
    }

    public double getTongChiPhiSP(LocalDate start, LocalDate end) {
        return getTongChiPhiTQ(start, end);
    }

    public double getLoiNhuanSP(LocalDate start, LocalDate end) {
        return getTongDoanhThuSP(start, end) - getTongChiPhiSP(start, end);
    }

    public int getKhachHangMuaSP(LocalDate start, LocalDate end) {
        int soKhach = 0;
        String sql = "SELECT COUNT(DISTINCT hd.maKH) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maSP IS NOT NULL AND hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) soKhach = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soKhach;
    }

    public int getHoaDonSP(LocalDate start, LocalDate end) {
        int soHD = 0;
        String sql = "SELECT COUNT(DISTINCT ct.maHD) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maSP IS NOT NULL AND hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) soHD = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soHD;
    }

    public int getSoSpBanRa(LocalDate start, LocalDate end) {
        int soLuong = 0;
        String sql = "SELECT SUM(ct.soLuong) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maSP IS NOT NULL AND hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) soLuong = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soLuong;
    }

    // CHART SẢN PHẨM
    public Map<LocalDate, Double> getDoanhThuTheoNgayChartSP(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> map = createEmptyMap(start, end);
        String sql = "SELECT CAST(hd.ngayLap AS DATE), SUM(ct.soLuong * sp.giaBan) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE hd.ngayLap BETWEEN ? AND ? GROUP BY CAST(hd.ngayLap AS DATE)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) map.put(rs.getDate(1).toLocalDate(), rs.getDouble(2));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public Map<LocalDate, Double> getChiPhiTheoNgayChartSP(LocalDate start, LocalDate end) {
        return getChiPhiTheoNgayChartTQ(start, end);
    }

    public Map<LocalDate, Double> getLoiNhuanTheoNgayChartSP(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> mapDT = getDoanhThuTheoNgayChartSP(start, end);
        Map<LocalDate, Double> mapCP = getChiPhiTheoNgayChartSP(start, end);
        Map<LocalDate, Double> mapLN = new LinkedHashMap<>();
        
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            mapLN.put(date, mapDT.getOrDefault(date, 0.0) - mapCP.getOrDefault(date, 0.0));
        }
        return mapLN;
    }

    // =========================================================================
    // PHẦN 3: THỐNG KÊ DỊCH VỤ
    // =========================================================================

    public double getTongDoanhThuDV(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT SUM(ct.soLuong * dv.giaTieuChuan) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN DichVu dv ON ct.maDV = dv.maDV " +
                     "WHERE hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) tong = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return tong;
    }

    public int getKhachHangDungDV(LocalDate start, LocalDate end) {
        int soKhach = 0;
        String sql = "SELECT COUNT(DISTINCT hd.maKH) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maDV IS NOT NULL AND hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) soKhach = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soKhach;
    }

    public int getHoaDonDV(LocalDate start, LocalDate end) {
        int soHD = 0;
        String sql = "SELECT COUNT(DISTINCT ct.maHD) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maDV IS NOT NULL AND hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) soHD = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soHD;
    }

    public int getSoLuotDV(LocalDate start, LocalDate end) {
        int soLuot = 0;
        String sql = "SELECT SUM(ct.soLuong) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maDV IS NOT NULL AND hd.ngayLap BETWEEN ? AND ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start)); pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery(); if (rs.next()) soLuot = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soLuot;
    }

    // CHART TOP 5 DỊCH VỤ
    public Map<String, Double> getDoanhThuTop5DichVu(LocalDate start, LocalDate end) {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT TOP 5 dv.tenDV, SUM(ct.soLuong * dv.giaTieuChuan) AS DoanhThu " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN DichVu dv ON ct.maDV = dv.maDV " +
                     "WHERE hd.ngayLap BETWEEN ? AND ? " +
                     "GROUP BY dv.tenDV " +
                     "ORDER BY DoanhThu DESC";
                     
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
    
    // =========================================================================
    // PHẦN 4: HỖ TRỢ ĐẾM LƯỢT MUA SẢN PHẨM & DỊCH VỤ (ĐỂ TÌM MAX / MIN)
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
    // PHẦN 5: TẠO ĐỀ XUẤT & NHẬN XÉT TỰ ĐỘNG THEO THÁNG
    // =========================================================================

    public String taoDeXuatTQ(LocalDate start, LocalDate end) {
        double dtTQ = getTongDoanhThuTQ(start, end);
        double lnTQ = getLoiNhuanTQ(start, end);
        int khTQ = getSoLuongKhachHangTQ(start, end);
        int hdTQ = getTongHoaDonTQ(start, end);
        String thangNam = start.getMonthValue() + "/" + start.getYear();

        if (hdTQ == 0) {
            return "Tháng " + thangNam + " chưa có giao dịch phát sinh. Cửa hàng cần lập tức triển khai các chiến dịch quảng cáo, Flash Sale hoặc liên hệ lại tệp khách hàng cũ.";
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        double aov = dtTQ / hdTQ;
        double tySuatLN = dtTQ > 0 ? (lnTQ / dtTQ) * 100 : 0;
        double tanSuat = khTQ > 0 ? (double) hdTQ / khTQ : 0;

        StringBuilder dx = new StringBuilder();
        dx.append("[*] ĐÁNH GIÁ HIỆU QUẢ THÁNG ").append(thangNam).append(":\n");
        dx.append("  - Tỷ suất lợi nhuận: ").append(String.format("%.1f%%", tySuatLN));
        if (tySuatLN >= 60) dx.append(" (Xuất Sắc)\n");
        else if (tySuatLN >= 40) dx.append(" (Tốt)\n");
        else if (tySuatLN >= 20) dx.append(" (Trung Bình)\n");
        else if (tySuatLN > 0) dx.append(" (Cần Cải Thiện)\n");
        else dx.append(" (Đang Lỗ)\n");

        dx.append("  - Khách chi tiêu TB: ").append(df.format(aov)).append("/đơn.\n");
        dx.append("  - Tần suất quay lại: ").append(String.format("%.1f", tanSuat)).append(" lượt/khách.\n\n");

        dx.append("[!] ĐỀ XUẤT CHIẾN LƯỢC THÁNG TỚI:\n");
        dx.append("\n1. Về Tỷ Suất Lợi Nhuận:\n");
        if (tySuatLN >= 60) {
            dx.append("   + Tối ưu chi phí rất tốt. Có thể trích một phần lợi nhuận chạy Ads hoặc tổ chức Minigame thu hút khách mới trong tháng tới.\n");
        } else if (tySuatLN >= 40) {
            dx.append("   + Biên lợi nhuận ổn định. Tiếp tục duy trì cơ cấu tỷ trọng Sản phẩm / Dịch vụ hiện tại.\n");
        } else if (tySuatLN >= 20) {
            dx.append("   + Lợi nhuận trung bình do thiên về bán lẻ. Cần đào tạo nhân viên bán chéo (Cross-sell): Khách mua sữa tắm -> Mời trải nghiệm Spa.\n");
        } else if (tySuatLN > 0) {
            dx.append("   + BÁO ĐỘNG VÀNG: Lãi quá mỏng. Cần rà soát ngay hao hụt kho, đàm phán lại giá nhập hoặc giảm tỷ lệ tặng voucher.\n");
        } else {
            dx.append("   + BÁO ĐỘNG ĐỎ: Kinh doanh dưới mức hòa vốn! Ngừng ngay các chương trình giảm giá sâu và kiểm tra lại giá niêm yết.\n");
        }

        dx.append("\n2. Về Mức Chi Tiêu & Giữ Chân Khách:\n");
        if (aov < 150000) {
            dx.append("   + Khách chủ yếu mua lẻ giá trị thấp. Cần đóng gói COMBO (Hạt + Cát + Súp thưởng) rẻ hơn 5% để đẩy mức chi tiêu lên.\n");
        } else if (aov < 350000) {
            dx.append("   + Mức chi tiêu tầm trung. Áp dụng chính sách Freeship/Tặng quà cho hóa đơn cao hơn để kích thích khách mua thêm phụ kiện.\n");
        } else {
            dx.append("   + Tệp khách chịu chi tốt. Tập trung vào chăm sóc hậu mãi, ưu tiên đặt lịch Spa và gợi ý các dòng thức ăn Premium.\n");
        }

        if (tanSuat <= 1.2) {
            dx.append("   + Tỷ lệ quay lại trong tháng còn thấp. Nên in Voucher giảm 10% (HSD 30 ngày) trực tiếp trên hóa đơn để kéo khách quay lại tháng sau.\n");
        } else {
            dx.append("   + Khách hàng có thói quen quay lại tốt. Nên mời khách đăng ký Thẻ Thành Viên hoặc gói Spa trả trước.\n");
        }

        return dx.toString();
    }

    public String taoDeXuatSP(LocalDate start, LocalDate end) {
        double dtTQ = getTongDoanhThuTQ(start, end);
        double dtSP = getTongDoanhThuSP(start, end);
        double lnSP = getLoiNhuanSP(start, end);
        int hdSP = getHoaDonSP(start, end);
        int slSP = getSoSpBanRa(start, end);
        String thangNam = start.getMonthValue() + "/" + start.getYear();

        if (dtSP == 0 || hdSP == 0) {
            return "Tháng " + thangNam + " mảng bán lẻ đang đóng băng. Cần kiểm tra tồn kho, hạn sử dụng hàng hóa và chạy Flash Sale xả hàng.";
        }

        double tySuatLnSP = (lnSP / dtSP) * 100;
        double spPerDon = (double) slSP / hdSP;
        double tyTrongSP = dtTQ > 0 ? (dtSP / dtTQ) * 100 : 100;

        // tim min max luot mua sp
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
        dx.append("[*] ĐÁNH GIÁ BÁN LẺ THÁNG ").append(thangNam).append(":\n");
        dx.append("  - Đóng góp doanh thu: ").append(String.format("%.1f%%", tyTrongSP)).append(" (so với tổng).\n");
        dx.append("  - Tỷ suất LN sản phẩm: ").append(String.format("%.1f%%", tySuatLnSP)).append(".\n");
        dx.append("  - Trung bình: ").append(String.format("%.1f", spPerDon)).append(" món/đơn.\n");
        if (!spMax.isEmpty()) {
            dx.append("  - Bán chạy nhất: ").append(spMax).append(" (").append(slMax).append(" lượt).\n");
            if (!spMin.equals(spMax)) {
                dx.append("  - Bán chậm nhất: ").append(spMin).append(" (").append(slMin).append(" lượt).\n");
            }
        }

        dx.append("\n[!] ĐỀ XUẤT HÀNG HÓA THÁNG TỚI:\n");
        if (!spMax.isEmpty()) {
            dx.append("\n1. Nhóm bán chạy nhất ('").append(spMax).append("'):\n");
            dx.append("   + Sức mua rất tốt (").append(slMax).append(" lượt). Cần tiếp tục duy trì lượng tồn kho ổn định, kiểm tra nhập bù sớm để tránh đứt hàng trong tháng tới.\n");
        }
        if (!spMin.isEmpty() && !spMin.equals(spMax)) {
            dx.append("\n2. Nhóm bán chậm nhất ('").append(spMin).append("'):\n");
            dx.append("   + Lượt mua còn thấp (").append(slMin).append(" lượt). Tháng tới cần tăng các đợt khuyến mãi, giảm giá kích cầu hoặc làm quà tặng kèm khi khách mua '")
              .append(spMax).append("' và các dịch vụ Spa khác.\n");
        }

        return dx.toString();
    }

    public String taoDeXuatDV(LocalDate start, LocalDate end) {
        double dtTQ = getTongDoanhThuTQ(start, end);
        double dtDV = getTongDoanhThuDV(start, end);
        int slDV = getSoLuotDV(start, end);
        String thangNam = start.getMonthValue() + "/" + start.getYear();

        if (dtDV == 0 || slDV == 0) {
            return "Tháng " + thangNam + " chưa có lượt đặt dịch vụ nào. Khuyên gọi điện chăm sóc khách cũ hoặc tặng voucher trải nghiệm Spa.";
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        double tyTrongDV = dtTQ > 0 ? (dtDV / dtTQ) * 100 : 100;
        double tbLuot = dtDV / slDV;

        // tim min max luot dung dich vu
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
        dx.append("[*] ĐÁNH GIÁ DỊCH VỤ THÁNG ").append(thangNam).append(":\n");
        dx.append("  - Đóng góp doanh thu: ").append(String.format("%.1f%%", tyTrongDV)).append(" tổng DT.\n");
        dx.append("  - Chi tiêu TB: ").append(df.format(tbLuot)).append("/lượt.\n");
        if (!dvMax.isEmpty()) {
            dx.append("  - Dùng nhiều nhất: ").append(dvMax).append(" (").append(slMax).append(" lượt).\n");
            if (!dvMin.equals(dvMax)) {
                dx.append("  - Dùng ít nhất: ").append(dvMin).append(" (").append(slMin).append(" lượt).\n");
            }
        }

        dx.append("\n[!] ĐỀ XUẤT DỊCH VỤ THÁNG TỚI:\n");
        if (!dvMax.isEmpty()) {
            dx.append("\n1. Dịch vụ mũi nhọn ('").append(dvMax).append("'):\n");
            dx.append("   + Đang hút khách nhất (").append(slMax).append(" lượt). Cần tiếp tục duy trì chất lượng, bố trí đủ lịch trực nhân viên và kết hợp tư vấn bán thêm sữa tắm/xịt dưỡng.\n");
        }
        if (!dvMin.isEmpty() && !dvMin.equals(dvMax)) {
            dx.append("\n2. Dịch vụ ít khách ('").append(dvMin).append("'):\n");
            dx.append("   + Lượt đặt còn thấp (").append(slMin).append(" lượt). Tháng tới nên tăng các đợt khuyến mãi hoặc tạo gói tặng kèm/giảm 50% khi khách đặt '")
              .append(dvMax).append("' hoặc mua sản phẩm tại cửa hàng.\n");
        }

        return dx.toString();
    }
}