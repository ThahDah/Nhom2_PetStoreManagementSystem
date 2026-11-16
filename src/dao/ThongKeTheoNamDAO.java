package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}