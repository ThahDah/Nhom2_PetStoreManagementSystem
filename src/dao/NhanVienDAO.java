package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TrangThai;

public class NhanVienDAO {

	/* tra ve nhan vien theo ma hoac null neu k ton tai */
	public NhanVien timTheoMa(String maNV) throws SQLException {
		String sql = "SELECT maNV, tenNV, soDienThoai, email, trangThaiNV, hinhAnh FROM NhanVien WHERE maNV = ?";
		try (Connection con = ConnectDB.getConnection();PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, maNV);
			try (ResultSet rs = stm.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				return new NhanVien(rs.getString("maNV"), rs.getString("tenNV"), rs.getString("soDienThoai"),
						rs.getString("email"), TrangThai.valueOf(rs.getString("trangThaiNV")),
						rs.getString("hinhAnh"));
			}
		}
	}
}
