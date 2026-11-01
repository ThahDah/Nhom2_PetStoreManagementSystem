package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.DoHungDu;
import entity.KhachHang;
import entity.Loai;
import entity.ThuCung;
import entity.TrangThai;

public class ThuCungDAO {

	public List<ThuCung> timTheoChuSoHuu(KhachHang khachHang) throws SQLException {
		List<ThuCung> dsThuCung = new ArrayList<>();
		String sql = "SELECT maThuCung, tenThuCung, loai, canNang, ghiChu, trangThaiThuCung, doHungDu "
				+ "FROM ThuCung WHERE maKH = ? ORDER BY tenThuCung";
		try (Connection con = ConnectDB.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, khachHang.getMaKH());
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					dsThuCung.add(taoThuCung(rs, khachHang));
				}
			}
		}
		return dsThuCung;
	}

	/* cap nhat lai tt thu cung trong luc tao lich hen */
	void capNhatThongTin(Connection con, ThuCung thuCung) throws SQLException {
		String sql = "UPDATE ThuCung SET loai = ?, canNang = ?, doHungDu = ? WHERE maThuCung = ?";
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, thuCung.getLoai().name());
			stm.setDouble(2, thuCung.getCanNang());
			stm.setString(3, thuCung.getDoHungDu().name());
			stm.setString(4, thuCung.getMaThuCung());
			stm.executeUpdate();
		}
	}
	
	/* doc ResultSet de chuyen ThuCung thanh doi tuong */
	static ThuCung taoThuCung(ResultSet rs, KhachHang chuSoHuu) throws SQLException {
		return new ThuCung(rs.getString("maThuCung"), rs.getString("tenThuCung"),
				Loai.valueOf(rs.getString("loai")), rs.getDouble("canNang"), rs.getString("ghiChu"),
				TrangThai.valueOf(rs.getString("trangThaiThuCung")), DoHungDu.valueOf(rs.getString("doHungDu")),
				chuSoHuu);
	}
}
