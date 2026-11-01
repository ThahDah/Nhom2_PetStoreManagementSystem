package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietLichHen;
import entity.DichVu;
import entity.LichHen;
import entity.ThuCung;
import entity.TrangThai;


public class LichHenDAO {
	private static final DateTimeFormatter DINH_DANG_MA = DateTimeFormatter.ofPattern("ddMMyy");

	private final ChiTietLichHenDAO chiTietLichHenDAO = new ChiTietLichHenDAO();
	private final ThuCungDAO thuCungDAO = new ThuCungDAO();

	public List<LichHen> layTatCa() throws SQLException {
		List<LichHen> dsLichHen = new ArrayList<>();
		String sql = "SELECT maLH, thoiGianHen, ghiChu, trangThaiHen "
				+ "FROM LichHen "
				+ "ORDER BY thoiGianHen DESC, maLH DESC";
		try (Connection con = ConnectDB.getConnection();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql)) {
			while (rs.next()) {
				String maLH = rs.getString("maLH");
				LocalDate thoiGianHen = rs.getDate("thoiGianHen").toLocalDate();
				String ghiChu = rs.getString("ghiChu");
				TrangThai trangThaiHen = TrangThai.valueOf(rs.getString("trangThaiHen"));
				dsLichHen.add(new LichHen(
						maLH,
						thoiGianHen,
						ghiChu,
						trangThaiHen));
			}
		}
		return dsLichHen;
	}

	/** maLH: "LH" + ddMMyy (ngay tao) + stt trong ngay (3 chu so) */
	public String phatSinhMaLichHen() throws SQLException {
		String tienTo = "LH" + LocalDate.now().format(DINH_DANG_MA);
		String sql = "SELECT MAX(maLH) "
				+ "FROM LichHen "
				+ "WHERE maLH LIKE ?";
		try (Connection con = ConnectDB.getConnection();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, tienTo + "%");
			try (ResultSet rs = stm.executeQuery()) {
				int soThuTu = 1;
				if (rs.next() && rs.getString(1) != null) {
					String maLonNhat = rs.getString(1);
					String phanSo = maLonNhat.substring(tienTo.length());
					soThuTu = Integer.parseInt(phanSo) + 1;
				}
				return tienTo + String.format("%03d", soThuTu);
			}
		}
	}

	/**
	 * kiem tra thu cung co duoc dat dich vu k:
	 * can nang hop le, dat truoc it nhat 1 ngay, k trung
	 */
	public void kiemTraDatLich(ThuCung thuCung, DichVu dichVu, LocalDate ngayHen)
			throws NghiepVuException, SQLException {
		try (Connection con = ConnectDB.getConnection()) {
			kiemTraDatLich(con, thuCung, dichVu, ngayHen);
		}
	}

	/**
	 * dong gia duoc tinh lai bang tinhGiaChiTiet (phu phi chi tinh 1 lan cho moi thu cung)
	 * tt ban dau la "cho den hen"
	 */
	public void taoLichHen(LichHen lichHen, List<ChiTietLichHen> dsChiTiet) throws NghiepVuException, SQLException {
		if (dsChiTiet == null || dsChiTiet.isEmpty()) {
			throw new NghiepVuException("Lịch hẹn phải có ít nhất một thú cưng và dịch vụ");
		}
		try (Connection con = ConnectDB.getConnection()) {
			con.setAutoCommit(false);
			try {
				HashSet<String> dsDaChon = new HashSet<>();
				for (ChiTietLichHen chiTiet : dsChiTiet) {
					String khoa = chiTiet.getThuCung().getMaThuCung() + "|" + chiTiet.getDichVu().getMaDV();
					if (!dsDaChon.add(khoa)) {
						throw new NghiepVuException("Thú cưng " + chiTiet.getThuCung().getTenThuCung()
								+ " bị chọn trùng dịch vụ " + chiTiet.getDichVu().getTenDV());
					}
					kiemTraDatLich(con, chiTiet.getThuCung(), chiTiet.getDichVu(), lichHen.getThoiGianHen());
				}
				lichHen.setTrangThaiHen(TrangThai.CHO_DEN_HEN);
				themLichHen(con, lichHen);
				tinhGiaChiTiet(dsChiTiet);
				for (ChiTietLichHen chiTiet : dsChiTiet) {
					chiTiet.setLichHen(lichHen);
					thuCungDAO.capNhatThongTin(con, chiTiet.getThuCung());
					chiTietLichHenDAO.them(con, chiTiet);
				}
				con.commit();
			} catch (NghiepVuException | SQLException | RuntimeException ex) {
				con.rollback();
				throw ex;
			}
		}
	}

	/**
	 * tinh don gia cho tung don chi tiet
	 * phu phi (can nang + do hung du) chi tinh mot lan cho moi thu cung,o dong dich vu dau tien cua thu cung do
	 * cac dong sau chi tinh gia tieu chuan
	 */
	public void tinhGiaChiTiet(List<ChiTietLichHen> dsChiTiet) {
		HashSet<String> dsDaTinhPhuPhi = new HashSet<>();
		for (ChiTietLichHen chiTiet : dsChiTiet) {
			ThuCung thuCung = chiTiet.getThuCung();
			boolean dongDauCuaThuCung = dsDaTinhPhuPhi.add(thuCung.getMaThuCung());
			if (dongDauCuaThuCung) {
				chiTiet.setDonGia(chiTiet.tinhGiaDichVu(thuCung));
			} else {
				chiTiet.setDonGia(chiTiet.getDichVu().getGiaTieuChuan());
			}
		}
	}

	/** tong tien (gia du kien) cua cac dong chi tiet chua luu vao CSDL */
	public double tinhTongTien(List<ChiTietLichHen> dsChiTiet) {
		double tongTien = 0;
		for (ChiTietLichHen chiTiet : dsChiTiet) {
			tongTien += chiTiet.getDonGia();
		}
		return tongTien;
	}

	/** tong tien cua mot lich hen da luu */
	public double tinhTongTien(String maLH) throws SQLException {
		String sql = "SELECT SUM(donGia) "
				+ "FROM ChiTietLichHen "
				+ "WHERE maLH = ?";
		try (Connection con = ConnectDB.getConnection();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, maLH);
			try (ResultSet rs = stm.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble(1);
				}
				return 0;
			}
		}
	}

	// ------------------------------------------------------------------ 

	private void kiemTraDatLich(Connection con, ThuCung thuCung, DichVu dichVu, LocalDate ngayHen)
			throws NghiepVuException, SQLException {
		if (thuCung.getCanNang() <= 0) {
			throw new NghiepVuException("Cân nặng phải lớn hơn 0");
		}
		if (ngayHen.isBefore(LocalDate.now().plusDays(1))) {
			throw new NghiepVuException("Phải đặt lịch trước ngày hẹn ít nhất 1 ngày");
		}
		String sql = "SELECT COUNT(*) "
				+ "FROM ChiTietLichHen ct "
				+ "JOIN LichHen lh ON ct.maLH = lh.maLH "
				+ "WHERE ct.maThuCung = ? "
				+ "AND ct.maDV = ? "
				+ "AND lh.thoiGianHen = ? "
				+ "AND lh.trangThaiHen <> ?";
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, thuCung.getMaThuCung());
			stm.setString(2, dichVu.getMaDV());
			stm.setDate(3, Date.valueOf(ngayHen));
			stm.setString(4, TrangThai.DA_HUY.name());
			try (ResultSet rs = stm.executeQuery()) {
				if (rs.next() && rs.getInt(1) > 0) {
					throw new NghiepVuException("Trùng lịch hẹn! Thú cưng " + thuCung.getTenThuCung()
							+ " đã đặt dịch vụ " + dichVu.getTenDV() + " vào ngày này");
				}
			}
		}
	}

	private void themLichHen(Connection con, LichHen lichHen) throws SQLException {
		String sql = "INSERT INTO LichHen (maLH, thoiGianHen, ghiChu, trangThaiHen) "
				+ "VALUES (?, ?, ?, ?)";
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, lichHen.getMaLH());
			stm.setDate(2, Date.valueOf(lichHen.getThoiGianHen()));
			stm.setString(3, lichHen.getGhiChu());
			stm.setString(4, lichHen.getTrangThaiHen().name());
			stm.executeUpdate();
		}
	}
}
