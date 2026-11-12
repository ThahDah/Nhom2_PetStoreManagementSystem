package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
        
        // Chạy vòng lặp từ ngày bắt đầu đến ngày kết thúc
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            double dt = mapDoanhThu.getOrDefault(date, 0.0);
            double cp = mapChiPhi.getOrDefault(date, 0.0);
            mapLoiNhuan.put(date, dt - cp); // Lợi nhuận = Doanh thu - Chi phí
        }
        return mapLoiNhuan;
    }


 // =========================================================================
    // 2. PHẦN SẢN PHẨM (Chỉ lọc các chi tiết hóa đơn có maSP IS NOT NULL)
    // =========================================================================

    // --- KPI 1: Doanh thu Sản phẩm
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

    // --- KPI 2: Chi phí Sản phẩm
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

    // --- KPI 3: Lợi nhuận Sản phẩm
    public double getLoiNhuanSP(LocalDate start, LocalDate end) {
        return getTongDoanhThuSP(start, end) - getTongChiPhiSP(start, end);
    }

    // --- KPI 4: Khách hàng mua Sản phẩm
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

    // --- KPI 5: Hóa đơn chứa Sản phẩm
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

    // --- KPI 6: Số lượng Sản phẩm đã bán ra
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

    // --- CHART 1: Biểu đồ đường Doanh thu Sản phẩm
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

    // --- CHART 2: Biểu đồ đường Chi phí Sản phẩm
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

    // --- CHART 3: Biểu đồ đường Lợi nhuận Sản phẩm
    public Map<LocalDate, Double> getLoiNhuanTheoNgayChartSP(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> mapLoiNhuan = new LinkedHashMap<>();
        Map<LocalDate, Double> mapDoanhThu = getDoanhThuTheoNgayChartSP(start, end);
        Map<LocalDate, Double> mapChiPhi = getChiPhiTheoNgayChartSP(start, end);
        
        // Chạy vòng lặp từ ngày bắt đầu đến ngày kết thúc
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            double dt = mapDoanhThu.getOrDefault(date, 0.0);
            double cp = mapChiPhi.getOrDefault(date, 0.0);
            mapLoiNhuan.put(date, dt - cp); // Lợi nhuận SP = Doanh thu SP - Chi phí SP
        }
        return mapLoiNhuan;
    }


 // =========================================================================
    // 3. PHẦN DỊCH VỤ (Chỉ lọc các chi tiết hóa đơn có maDV IS NOT NULL)
    // =========================================================================

    // --- KPI 1: Doanh thu Dịch vụ
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

    // --- KPI 2: Số Khách hàng dùng Dịch vụ
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

    // --- KPI 3: Số Hóa đơn chứa Dịch vụ
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

    // --- KPI 4: Tổng số lượt Dịch vụ đã phục vụ
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

    // --- CHART: Biểu đồ cột Top 5 Dịch vụ
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
}