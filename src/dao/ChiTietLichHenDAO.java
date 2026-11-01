package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietLichHen;
import entity.DichVu;
import entity.KhachHang;
import entity.LichHen;
import entity.ThuCung;

public class ChiTietLichHenDAO {

	public List<ChiTietLichHen> timTheoLichHen(LichHen lichHen) throws SQLException {
		List<ChiTietLichHen> dsChiTiet = new ArrayList<>();
		String sql = "SELECT ct.donGia, "
				+ "t.maThuCung, t.tenThuCung, t.loai, t.canNang, t.ghiChu, t.trangThaiThuCung, t.doHungDu, "
				+ "k.maKH, k.tenKH, k.soDienThoai, k.email, k.diemTichLuy, "
				+ "d.maDV, d.tenDV, d.giaTieuChuan "
				+ "FROM ChiTietLichHen ct "
				+ "JOIN ThuCung t ON ct.maThuCung = t.maThuCung "
				+ "JOIN KhachHang k ON t.maKH = k.maKH "
				+ "JOIN DichVu d ON ct.maDV = d.maDV "
				+ "WHERE ct.maLH = ? ORDER BY t.tenThuCung, d.maDV";
		try (Connection con = ConnectDB.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, lichHen.getMaLH());
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					KhachHang khachHang = KhachHangDAO.taoKhachHang(rs);
					ThuCung thuCung = ThuCungDAO.taoThuCung(rs, khachHang);
					DichVu dichVu = DichVuDAO.taoDichVu(rs);
					dsChiTiet.add(new ChiTietLichHen(lichHen, thuCung, dichVu, rs.getDouble("donGia")));
				}
			}
		}
		return dsChiTiet;
	}

	/** them mot dong chi tiet trong mot giao dich co san */
	void them(Connection con, ChiTietLichHen chiTiet) throws SQLException {
		String sql = "INSERT INTO ChiTietLichHen (maLH, maThuCung, maDV, donGia) VALUES (?, ?, ?, ?)";
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, chiTiet.getLichHen().getMaLH());
			stm.setString(2, chiTiet.getThuCung().getMaThuCung());
			stm.setString(3, chiTiet.getDichVu().getMaDV());
			stm.setDouble(4, chiTiet.getDonGia());
			stm.executeUpdate();
		}
	}
}
