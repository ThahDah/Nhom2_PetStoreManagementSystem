package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jfree.chart.plot.CategoryPlot;

import connectDB.ConnectDB;

public class ThongKeTheoTuanDAO {

    // 1. Tính tổng doanh thu (Chỉ tính hóa đơn ĐÃ THANH TOÁN)
    public double getTongDoanhThu(LocalDate start, LocalDate end) {
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
                if (rs.next()) {
                    tong = rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }

    // 2. Tính tổng chi phí / giá vốn (Tính dựa trên số lượng SP bán ra * giaNhap)
    public double getTongChiPhi(LocalDate start, LocalDate end) {
        double tong = 0;
        // Đã đổi giaNY thành giaNhap để tính chính xác giá vốn hàng bán
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
                if (rs.next()) {
                    tong = rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }

    // Hàm gọi phụ trợ cho giá vốn
    public double getGiaVon(LocalDate start, LocalDate end) {
        return getTongChiPhi(start, end);
    }

    // 3. Tính lợi nhuận = Tổng doanh thu - Tổng chi phí (giá vốn)
    public double getLoiNhuan(LocalDate start, LocalDate end) {
        double doanhThu = getTongDoanhThu(start, end);
        double chiPhi = getTongChiPhi(start, end);
        return doanhThu - chiPhi;
    }

    // 4. Đếm số lượng khách hàng mua hàng trong khoảng thời gian
    public int getSoLuongKhachHang(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT maKH) " +
                     "FROM HoaDon " +
                     "WHERE trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(ngayLap AS DATE) BETWEEN ? AND ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 5. Lấy doanh thu theo từng ngày
    public Map<LocalDate, Double> getDoanhThuTheoNgay(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new LinkedHashMap<>();

        String sql = "SELECT CAST(ngayLap AS DATE) AS Ngay, ISNULL(SUM(tongTien), 0) AS DoanhThu " +
                     "FROM HoaDon " +
                     "WHERE trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(ngayLap AS DATE) " +
                     "ORDER BY Ngay ASC";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getDate("Ngay").toLocalDate(), rs.getDouble("DoanhThu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // 6. Lấy chi phí (giá vốn) theo từng ngày
    public Map<LocalDate, Double> getChiPhiTheoNgay(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new LinkedHashMap<>();

        String sql = "SELECT CAST(HD.ngayLap AS DATE) AS Ngay, ISNULL(SUM(CT.soLuong * SP.giaNhap), 0) AS ChiPhi " +
                     "FROM ChiTietHoaDon CT " +
                     "JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "JOIN SanPham SP ON CT.maSP = SP.maSP " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(HD.ngayLap AS DATE) " +
                     "ORDER BY Ngay ASC";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getDate("Ngay").toLocalDate(), rs.getDouble("ChiPhi"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
 // 7. Lấy Top 5 Sản phẩm mang lại doanh thu cao nhất
    public Map<String, Double> getDoanhThuTop5SanPham(LocalDate start, LocalDate end) {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT TOP 5 SP.tenSP, ISNULL(SUM(CT.thanhTien), 0) AS DoanhThu " +
                     "FROM ChiTietHoaDon CT " +
                     "JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "JOIN SanPham SP ON CT.maSP = SP.maSP " +
                     "WHERE HD.trangThaiHD = 'DA_THANH_TOAN' " +
                     "AND CAST(HD.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY SP.tenSP " +
                     "ORDER BY DoanhThu DESC";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenSP"), rs.getDouble("DoanhThu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // 8. Lấy Top 5 Dịch vụ mang lại doanh thu cao nhất
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
 
}