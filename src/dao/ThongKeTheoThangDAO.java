package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}