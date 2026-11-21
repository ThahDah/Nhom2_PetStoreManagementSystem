package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import connectDB.ConnectDB; 

public class ThongKeTheoNamDAO {

    // =========================================================================
    // PHẦN 1: THỐNG KÊ TỔNG QUAN
    // =========================================================================
    
    public double getTongDoanhThuTQ(int year) {
        double tong = 0;
        String sql = "SELECT SUM(tongTien) FROM HoaDon WHERE YEAR(ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) tong = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return tong;
    }

    public double getTongChiPhiTQ(int year) {
        double chiPhi = 0;
        String sql = "SELECT SUM(ct.soLuong * sp.giaNhap) " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) chiPhi = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return chiPhi;
    }

    public double getLoiNhuanTQ(int year) {
        return getTongDoanhThuTQ(year) - getTongChiPhiTQ(year);
    }

    public int getSoLuongKhachHangTQ(int year) {
        int soKhach = 0;
        String sql = "SELECT COUNT(DISTINCT maKH) FROM HoaDon WHERE YEAR(ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) soKhach = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soKhach;
    }

    public int getTongHoaDonTQ(int year) {
        int soHD = 0;
        String sql = "SELECT COUNT(maHD) FROM HoaDon WHERE YEAR(ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) soHD = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soHD;
    }

    // =========================================================================
    // CHART TỔNG QUAN (THEO 12 THÁNG)
    // =========================================================================

    private Map<Integer, Double> createEmptyMap() {
        Map<Integer, Double> map = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put(i, 0.0);
        }
        return map;
    }

    public Map<Integer, Double> getDoanhThuTheoNamChartTQ(int year) {
        Map<Integer, Double> map = createEmptyMap();
        String sql = "SELECT MONTH(ngayLap), SUM(tongTien) FROM HoaDon " +
                     "WHERE YEAR(ngayLap) = ? GROUP BY MONTH(ngayLap)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt(1), rs.getDouble(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public Map<Integer, Double> getChiPhiTheoNamChartTQ(int year) {
        Map<Integer, Double> map = createEmptyMap();
        String sql = "SELECT MONTH(hd.ngayLap), SUM(ct.soLuong * sp.giaNhap) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE YEAR(hd.ngayLap) = ? GROUP BY MONTH(hd.ngayLap)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt(1), rs.getDouble(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public Map<Integer, Double> getLoiNhuanTheoNamChartTQ(int year) {
        Map<Integer, Double> mapDT = getDoanhThuTheoNamChartTQ(year);
        Map<Integer, Double> mapCP = getChiPhiTheoNamChartTQ(year);
        Map<Integer, Double> mapLN = new LinkedHashMap<>();
        
        for (int i = 1; i <= 12; i++) {
            double dt = mapDT.getOrDefault(i, 0.0);
            double cp = mapCP.getOrDefault(i, 0.0);
            mapLN.put(i, dt - cp);
        }
        return mapLN;
    }

    // =========================================================================
    // PHẦN 2: THỐNG KÊ SẢN PHẨM
    // =========================================================================

    public double getTongDoanhThuSP(int year) {
        double tong = 0;
        String sql = "SELECT SUM(ct.soLuong * sp.giaBan) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); if (rs.next()) tong = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return tong;
    }

    public double getTongChiPhiSP(int year) {
        return getTongChiPhiTQ(year);
    }

    public double getLoiNhuanSP(int year) {
        return getTongDoanhThuSP(year) - getTongChiPhiSP(year);
    }

    public int getKhachHangMuaSP(int year) {
        int soKhach = 0;
        String sql = "SELECT COUNT(DISTINCT hd.maKH) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maSP IS NOT NULL AND YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); if (rs.next()) soKhach = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soKhach;
    }

    public int getHoaDonSP(int year) {
        int soHD = 0;
        String sql = "SELECT COUNT(DISTINCT ct.maHD) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maSP IS NOT NULL AND YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); if (rs.next()) soHD = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soHD;
    }

    public int getSoSpBanRa(int year) {
        int soLuong = 0;
        String sql = "SELECT SUM(ct.soLuong) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maSP IS NOT NULL AND YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); if (rs.next()) soLuong = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soLuong;
    }

    // CHART SẢN PHẨM
    public Map<Integer, Double> getDoanhThuTheoNamChartSP(int year) {
        Map<Integer, Double> map = createEmptyMap();
        String sql = "SELECT MONTH(hd.ngayLap), SUM(ct.soLuong * sp.giaBan) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE YEAR(hd.ngayLap) = ? GROUP BY MONTH(hd.ngayLap)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) map.put(rs.getInt(1), rs.getDouble(2));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public Map<Integer, Double> getChiPhiTheoNamChartSP(int year) {
        return getChiPhiTheoNamChartTQ(year);
    }

    public Map<Integer, Double> getLoiNhuanTheoNamChartSP(int year) {
        Map<Integer, Double> mapDT = getDoanhThuTheoNamChartSP(year);
        Map<Integer, Double> mapCP = getChiPhiTheoNamChartSP(year);
        Map<Integer, Double> mapLN = new LinkedHashMap<>();
        
        for (int i = 1; i <= 12; i++) {
            mapLN.put(i, mapDT.getOrDefault(i, 0.0) - mapCP.getOrDefault(i, 0.0));
        }
        return mapLN;
    }

    // =========================================================================
    // PHẦN 3: THỐNG KÊ DỊCH VỤ
    // =========================================================================

    public double getTongDoanhThuDV(int year) {
        double tong = 0;
        String sql = "SELECT SUM(ct.soLuong * dv.giaTieuChuan) " +
                     "FROM ChiTietHoaDon ct JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN DichVu dv ON ct.maDV = dv.maDV " +
                     "WHERE YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); 
            if (rs.next()) tong = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return tong;
    }

    public int getKhachHangDungDV(int year) {
        int soKhach = 0;
        String sql = "SELECT COUNT(DISTINCT hd.maKH) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maDV IS NOT NULL AND YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); if (rs.next()) soKhach = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soKhach;
    }

    public int getHoaDonDV(int year) {
        int soHD = 0;
        String sql = "SELECT COUNT(DISTINCT ct.maHD) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maDV IS NOT NULL AND YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); 
            if (rs.next()) soHD = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soHD;
    }

    public int getSoLuotDV(int year) {
        int soLuot = 0;
        String sql = "SELECT SUM(ct.soLuong) FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "WHERE ct.maDV IS NOT NULL AND YEAR(hd.ngayLap) = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery(); 
            if (rs.next()) soLuot = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return soLuot;
    }

    // CHART TOP 5 DỊCH VỤ
    public Map<String, Double> getDoanhThuTop5DichVu(int year) {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT TOP 5 dv.tenDV, SUM(ct.soLuong * dv.giaTieuChuan) AS DoanhThu " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN DichVu dv ON ct.maDV = dv.maDV " +
                     "WHERE YEAR(hd.ngayLap) = ? " +
                     "GROUP BY dv.tenDV " +
                     "ORDER BY DoanhThu DESC";
                     
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
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

    private Map<String, Integer> getSoLuotMuaTungSP(int year) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT sp.tenSP, SUM(ct.soLuong) AS TongSL " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE YEAR(hd.ngayLap) = ? " +
                     "GROUP BY sp.tenSP " +
                     "ORDER BY TongSL DESC";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    private Map<String, Integer> getSoLuotDungTungDV(int year) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT dv.tenDV, SUM(ct.soLuong) AS TongSL " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
                     "JOIN DichVu dv ON ct.maDV = dv.maDV " +
                     "WHERE YEAR(hd.ngayLap) = ? " +
                     "GROUP BY dv.tenDV " +
                     "ORDER BY TongSL DESC";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    // =========================================================================
    // PHẦN 5: TẠO ĐỀ XUẤT & NHẬN XÉT TỰ ĐỘNG
    // =========================================================================

    public String taoDeXuatTQ(int year) {
        double dtTQ = getTongDoanhThuTQ(year);
        double lnTQ = getLoiNhuanTQ(year);
        int khTQ = getSoLuongKhachHangTQ(year);
        int hdTQ = getTongHoaDonTQ(year);

        if (hdTQ == 0) {
            return "Năm " + year + " chưa có giao dịch phát sinh. Cửa hàng cần lập tức xem xét lại chiến lược kinh doanh tổng thể.";
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        double aov = dtTQ / hdTQ;
        double tySuatLN = dtTQ > 0 ? (lnTQ / dtTQ) * 100 : 0;
        double tanSuat = khTQ > 0 ? (double) hdTQ / khTQ : 0;

        StringBuilder dx = new StringBuilder();
        dx.append("[*] TỔNG KẾT HIỆU QUẢ KINH DOANH NĂM ").append(year).append(":\n");
        dx.append("  - Tỷ suất lợi nhuận: ").append(String.format("%.1f%%", tySuatLN));
        if (tySuatLN >= 60) dx.append(" (Xuất Sắc)\n");
        else if (tySuatLN >= 40) dx.append(" (Tốt)\n");
        else if (tySuatLN >= 20) dx.append(" (Trung Bình)\n");
        else if (tySuatLN > 0) dx.append(" (Cần Cải Thiện)\n");
        else dx.append(" (Đang Lỗ)\n");

        dx.append("  - Khách chi tiêu TB: ").append(df.format(aov)).append("/đơn.\n");
        dx.append("  - Tần suất quay lại TB: ").append(String.format("%.1f", tanSuat)).append(" lượt/khách/năm.\n\n");

        dx.append("[!] NHẬN ĐỊNH CHIẾN LƯỢC TỔNG THỂ:\n");
        dx.append("\n1. Về Tỷ Suất Lợi Nhuận Năm:\n");
        if (tySuatLN >= 60) {
            dx.append("   + Kiểm soát chi phí xuất sắc. Có thể cân nhắc mở rộng quy mô hoặc nâng cấp cơ sở vật chất.\n");
        } else if (tySuatLN >= 40) {
            dx.append("   + Biên lợi nhuận duy trì ở mức tốt. Tiếp tục duy trì và đẩy mạnh Marketing mở rộng tệp khách hàng.\n");
        } else if (tySuatLN >= 20) {
            dx.append("   + Lợi nhuận trung bình, cần tối ưu hóa mảng Dịch vụ và tăng bán chéo (Cross-sell).\n");
        } else if (tySuatLN > 0) {
            dx.append("   + BÁO ĐỘNG VÀNG: Lãi quá mỏng. Cần rà soát lại chi phí vận hành và đàm phán lại giá nhập hàng.\n");
        } else {
            dx.append("   + BÁO ĐỘNG ĐỎ: Hoạt động dưới mức hòa vốn. Cần tái cấu trúc và cắt bỏ các mặt hàng/dịch vụ kém hiệu quả.\n");
        }

        dx.append("\n2. Về Hành Vi Tiêu Dùng:\n");
        if (aov < 200000) {
            dx.append("   + Mức chi tiêu trung bình thấp. Cần đẩy mạnh các gói Combo sản phẩm + dịch vụ để tăng giá trị đơn hàng.\n");
        } else {
            dx.append("   + Khách hàng chi tiêu tốt. Nên phát triển thẻ thành viên hoặc gói dịch vụ trả trước để giữ chân khách.\n");
        }

        return dx.toString();
    }

    public String taoDeXuatSP(int year) {
        double dtTQ = getTongDoanhThuTQ(year);
        double dtSP = getTongDoanhThuSP(year);
        double lnSP = getLoiNhuanSP(year);
        int hdSP = getHoaDonSP(year);
        int slSP = getSoSpBanRa(year);

        if (dtSP == 0 || hdSP == 0) {
            return "Năm " + year + " chưa có doanh thu bán lẻ sản phẩm. Cần kiểm tra lại tồn kho và đẩy mạnh chương trình khuyến mãi.";
        }

        double tySuatLnSP = (lnSP / dtSP) * 100;
        double spPerDon = (double) slSP / hdSP;
        double tyTrongSP = dtTQ > 0 ? (dtSP / dtTQ) * 100 : 100;
        
        //tim min max
        Map<String, Integer> mapSL = getSoLuotMuaTungSP(year);
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
        dx.append("[*] TỔNG KẾT MẢNG SẢN PHẨM NĂM ").append(year).append(":\n");
        dx.append("  - Tỷ trọng DT bán lẻ: ").append(String.format("%.1f%%", tyTrongSP)).append(" (so với tổng).\n");
        dx.append("  - Tỷ suất LN sản phẩm: ").append(String.format("%.1f%%", tySuatLnSP)).append(".\n");
        dx.append("  - Trung bình: ").append(String.format("%.1f", spPerDon)).append(" sản phẩm/đơn.\n");
        if (!spMax.isEmpty()) {
            dx.append("  - Mua nhiều nhất: ").append(spMax).append(" (").append(slMax).append(" lượt).\n");
            if (!spMin.equals(spMax)) {
                dx.append("  - Mua ít nhất: ").append(spMin).append(" (").append(slMin).append(" lượt).\n");
            }
        }

        dx.append("\n[!] ĐỀ XUẤT CHIẾN LƯỢC SẢN PHẨM:\n");
        if (!spMax.isEmpty()) {
            dx.append("\n1. Nhóm bán chạy nhất ('").append(spMax).append("'):\n");
            dx.append("   + Sản phẩm đang có sức mua rất tốt (").append(slMax).append(" lượt). Cần tiếp tục duy trì nguồn hàng ổn định, trưng bày ở vị trí dễ thấy và tránh để đứt hàng.\n");
        }
        if (!spMin.isEmpty() && !spMin.equals(spMax)) {
            dx.append("\n2. Nhóm bán chậm nhất ('").append(spMin).append("'):\n");
            dx.append("   + Lượt mua còn thấp (").append(slMin).append(" lượt). Cần tăng các đợt khuyến mãi, giảm giá kích cầu hoặc đưa vào chương trình tặng kèm khi khách mua '")
              .append(spMax).append("' và các dịch vụ Spa khác để đẩy hàng tồn.\n");
        }

        return dx.toString();
    }

    public String taoDeXuatDV(int year) {
        double dtTQ = getTongDoanhThuTQ(year);
        double dtDV = getTongDoanhThuDV(year);
        int slDV = getSoLuotDV(year);

        if (dtDV == 0 || slDV == 0) {
            return "Năm " + year + " mảng dịch vụ chưa phát sinh doanh thu. Cần gọi chăm sóc khách cũ và tung voucher trải nghiệm.";
        }

        DecimalFormat df = new DecimalFormat("#,### đ");
        double tyTrongDV = dtTQ > 0 ? (dtDV / dtTQ) * 100 : 100;
        double tbLuot = dtDV / slDV;

        // tim min max
        Map<String, Integer> mapSL = getSoLuotDungTungDV(year);
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
        dx.append("[*] TỔNG KẾT MẢNG DỊCH VỤ NĂM ").append(year).append(":\n");
        dx.append("  - Tỷ trọng DT dịch vụ: ").append(String.format("%.1f%%", tyTrongDV)).append(" tổng DT.\n");
        dx.append("  - Chi tiêu TB: ").append(df.format(tbLuot)).append("/lượt.\n");
        if (!dvMax.isEmpty()) {
            dx.append("  - Dùng nhiều nhất: ").append(dvMax).append(" (").append(slMax).append(" lượt).\n");
            if (!dvMin.equals(dvMax)) {
                dx.append("  - Dùng ít nhất: ").append(dvMin).append(" (").append(slMin).append(" lượt).\n");
            }
        }

        dx.append("\n[!] ĐỀ XUẤT CHIẾN LƯỢC DỊCH VỤ:\n");
        if (!dvMax.isEmpty()) {
            dx.append("\n1. Dịch vụ mũi nhọn ('").append(dvMax).append("'):\n");
            dx.append("   + Đang thu hút lượng khách lớn nhất (").append(slMax).append(" lượt). Cần tiếp tục duy trì chất lượng phục vụ, đảm bảo đủ nhân sự trực và giữ vững trải nghiệm khách hàng.\n");
        }
        if (!dvMin.isEmpty() && !dvMin.equals(dvMax)) {
            dx.append("\n2. Dịch vụ ít khách ('").append(dvMin).append("'):\n");
            dx.append("   + Lượt sử dụng còn thấp (").append(slMin).append(" lượt). Cần tăng các đợt khuyến mãi giảm giá, hoặc tạo gói tặng kèm/giảm 50% khi khách đặt '")
              .append(dvMax).append("' hoặc mua sản phẩm tại cửa hàng.\n");
        }

        return dx.toString();
    }
}