package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {

	/** tra ve khach hang co sdt tuong ung, hoac null neu khong co */
	public KhachHang timTheoSoDienThoai(String soDienThoai) throws SQLException {
		String sql = "SELECT maKH, tenKH, soDienThoai, email, diemTichLuy FROM KhachHang WHERE soDienThoai = ?";
		try (Connection con = ConnectDB.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, soDienThoai);
			try (ResultSet rs = stm.executeQuery()) {
				return rs.next() ? taoKhachHang(rs) : null;
			}
		}
	}

	/** doc mot dong ResultSet co du cac cot cua bang KhachHang thanh doi tuong */
	static KhachHang taoKhachHang(ResultSet rs) throws SQLException {
		return new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("soDienThoai"),
				rs.getString("email"), rs.getInt("diemTichLuy"));
	}
}
