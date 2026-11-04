package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import connectDB.ConnectDB;

public class ThongKeTheoTuanDAO {
	//de tinh loi nhuan phai tinh gia von
	public double getGiaVon(LocalDate start, LocalDate end) {
	    double tong = 0;

	    String sql = "SELECT SUM(CT.SoLuong * SP.GiaNhap) " +
	                 "FROM ChiTietHoaDon CT " +
	                 "JOIN HoaDon HD ON CT.MaHD = HD.MaHD " +
	                 "JOIN SanPham SP ON CT.MaSP = SP.MaSP " +
	                 "WHERE HD.TrangThaiHD = N'DA_THANH_TOAN' " +
	                 "AND CAST(HD.NgayLap AS DATE) BETWEEN ? AND ?";
	    Connection con = ConnectDB.getConnection();
	    try (	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setDate(1, Date.valueOf(start));
	        ps.setDate(2, Date.valueOf(end));

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) tong = rs.getDouble(1);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return tong;
	}
	
	//ham tinh loi nhuan theo cong thuc
	public double getTongDoanhThu(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT SUM(TongTien) FROM HoaDon " +
        //chi tinh loi nhuan hoa don da thanh toan
                     "WHERE TrangThaiHD = N'DA_THANH_TOAN' " +
                     "AND CAST(NgayLap AS DATE) BETWEEN ? AND ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) tong = rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }

    // tinh tong chi phi dua tren data PhieuNhap
	public double getTongChiPhi(LocalDate start, LocalDate end) {
        double tong = 0;
        String sql = "SELECT SUM(TongTien) FROM PhieuNhap " +
                     "WHERE CAST(ThoiGianNhap AS DATE) BETWEEN ? AND ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) tong = rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }
	
//		public double getLoiNhuan(LocalDate start, LocalDate end) {
//		    double doanhThu = getTongDoanhThu(start, end);
//		    double giaVon = getGiaVon(start, end);
//		    return doanhThu - giaVon;
//		}
	
	public double getLoiNhuan(LocalDate start, LocalDate end) {
	    double doanhThu = getTongDoanhThu(start, end);
	    
	    double chiPhiNhapHang = getTongChiPhi(start, end); 
	    return doanhThu - chiPhiNhapHang;
	}

 // dem so luong khach hang, ep kieu Date
	public int getSoLuongKhachHang(LocalDate start, LocalDate end) {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT MaKH) FROM HoaDon " +
                     "WHERE TrangThaiHD = N'DA_THANH_TOAN' " +
                     "AND CAST(NgayLap AS DATE) BETWEEN ? AND ?";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // doanh thu theo ngay
    public Map<LocalDate, Double> getDoanhThuTheoNgay(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new HashMap<>();

        String sql = "SELECT CAST(NgayLap AS DATE), SUM(TongTien) " +
                     "FROM HoaDon " +
                     "WHERE TrangThaiHD = N'DA_THANH_TOAN' " +
                     "AND CAST(NgayLap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(NgayLap AS DATE)";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getDate(1).toLocalDate(), rs.getDouble(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // chi phi theo ngay
    public Map<LocalDate, Double> getChiPhiTheoNgay(LocalDate start, LocalDate end) {
        Map<LocalDate, Double> data = new HashMap<>();

        String sql = "SELECT CAST(ThoiGianNhap AS DATE), SUM(TongTien) " +
                     "FROM PhieuNhap " +
                     "WHERE CAST(ThoiGianNhap AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY CAST(ThoiGianNhap AS DATE)";
        Connection con = ConnectDB.getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getDate(1).toLocalDate(), rs.getDouble(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
