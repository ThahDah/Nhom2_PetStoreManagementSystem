package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.TrangThai;
public class HoaDonDAO {
//lay danh sach tat ca hoa don
	public List<Object[]> getAllHoaDon() throws SQLException {
		String sql = "SELECT hd.maHD, kh.tenKH, kh.soDienThoai, hd.ngayLap,"
				+ " hd.donGia, hd.trangThaiHD"
				+ " FROM HoaDon hd JOIN KhachHang kh ON kh.maKH = hd.maKH"
				+ " ORDER BY hd.ngayLap DESC, hd.maHD DESC";
		List<Object[]> danhSach = new ArrayList<Object[]>();
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				danhSach.add(new Object[] {
						resultSet.getString("maHD"),
						resultSet.getString("tenKH"),
						resultSet.getString("soDienThoai"),
						resultSet.getDate("ngayLap"),
						resultSet.getDouble("donGia"),
						TrangThai.valueOf(resultSet.getString("trangThaiHD"))
				});
			}
		}
		return danhSach;
	}
}
