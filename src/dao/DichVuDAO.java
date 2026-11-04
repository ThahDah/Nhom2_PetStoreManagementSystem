package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import connectDB.ConnectDB;
import entity.DichVu;

public class DichVuDAO {

	// lay tat ca dich vu
	public List<DichVu> layTatCa() throws SQLException {
		List<DichVu> dsDichVu = new ArrayList<>();
		String sql = "SELECT maDV, tenDV, giaTieuChuan "
				+ "FROM DichVu "
				+ "ORDER BY maDV";
		Connection con = ConnectDB.getConnection();
		try (Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql)) {
			while (rs.next()) {
				dsDichVu.add(taoDichVu(rs));
			}
		}
		return dsDichVu;
	}

	/** maDV: "DV" + 4 chu so tang dan, bat dau tu 0001 */
	public String phatSinhMaDichVu() throws SQLException {
		int soLonNhat = 0;
		for (DichVu dichVu : layTatCa()) {
			String ma = dichVu.getMaDV();
			if (ma != null && ma.matches("DV\\d{4}")) {
				int so = Integer.parseInt(ma.substring(2));
				if (so > soLonNhat) {
					soLonNhat = so;
				}
			}
		}
		return String.format("DV%04d", soLonNhat + 1);
	}

	/**
	 * them dich vu moi
	 * ma: DV + 4 chu so, ten: khong rong va khong bat dau bang ky tu dac biet, gia > 0
	 */
	public void themDichVu(DichVu dichVu) throws NghiepVuException, SQLException {
		String ma = dichVu.getMaDV();
		if (ma == null || !ma.matches("DV\\d{4}")) {
			throw new NghiepVuException("Mã dịch vụ gồm 6 ký tự: \"DV\" và 4 chữ số. Ấn 'Tạo mã' để phát sinh mã");
		}
		kiemTraThongTin(dichVu);

		String sql = "INSERT INTO DichVu (maDV, tenDV, giaTieuChuan) "
				+ "VALUES (?, ?, ?)";
		Connection con = ConnectDB.getConnection();
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, ma);
			stm.setString(2, dichVu.getTenDV().trim());
			stm.setDouble(3, dichVu.getGiaTieuChuan());
			stm.executeUpdate();
		} catch (SQLException e) {
			// loi trung khoa chinh
			if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
				throw new NghiepVuException("Mã dịch vụ này đã tồn tại trong hệ thống. Ấn 'Tạo mã' để phát sinh mã mới");
			}
			throw e;
		}
	}

	// sua ten va gia tieu chuan, ma dich vu khong doi
	public void suaDichVu(DichVu dichVu) throws NghiepVuException, SQLException {
		kiemTraThongTin(dichVu);

		String sql = "UPDATE DichVu "
				+ "SET tenDV = ?, giaTieuChuan = ? "
				+ "WHERE maDV = ?";
		Connection con = ConnectDB.getConnection();
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, dichVu.getTenDV().trim());
			stm.setDouble(2, dichVu.getGiaTieuChuan());
			stm.setString(3, dichVu.getMaDV());
			int soDongSua = stm.executeUpdate();
			if (soDongSua == 0) {
				throw new NghiepVuException("Không tìm thấy dịch vụ cần sửa");
			}
		}
	}

	// xoa dich vu
	public void xoaDichVu(String maDV) throws NghiepVuException, SQLException {
		String sql = "DELETE FROM DichVu "
				+ "WHERE maDV = ?";
		Connection con = ConnectDB.getConnection();
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, maDV);
			int soDongXoa = stm.executeUpdate();
			if (soDongXoa == 0) {
				throw new NghiepVuException("Không tìm thấy dịch vụ cần xóa");
			}
		} catch (SQLException e) {
			// loi khoa ngoai: dich vu da duoc dung trong lich hen
			if (e.getErrorCode() == 547) {
				throw new NghiepVuException("Không thể xóa! Dịch vụ này đang có dữ liệu liên quan (lịch hẹn, hóa đơn).");
			}
			throw e;
		}
	}

	/**
	 * tim kiem theo ma va ten, bo qua hoa thuong, dau tieng viet va khoang trang thua
	 * o trong thi khong loc theo tieu chi do
	 */
	public List<DichVu> timKiem(String ma, String ten) throws SQLException {
		String maChuan = chuanHoa(ma);
		String tenChuan = chuanHoa(ten);
		List<DichVu> dsKetQua = new ArrayList<>();
		for (DichVu dichVu : layTatCa()) {
			boolean khopMa = chuanHoa(dichVu.getMaDV()).contains(maChuan);
			boolean khopTen = chuanHoa(dichVu.getTenDV()).contains(tenChuan);
			if (khopMa && khopTen) {
				dsKetQua.add(dichVu);
			}
		}
		return dsKetQua;
	}

	public static String chuanHoa(String s) {
		// 1. Kiểm tra trường hợp null
		if (s == null) {
			return ""; // Hoặc trả về null tùy logic nghiệp vụ của bạn
		}

		// 2. Chuyển thành chữ thường và loại bỏ khoảng trắng thừa đầu/cuối
		String result = s.toLowerCase().trim();

		// 3. Thay thế các khoảng trắng liên tiếp ở giữa thành một khoảng trắng duy nhất
		result = result.replaceAll("\\s+", " ");

		// 4. Xử lý riêng ký tự 'đ' và 'Đ' (vì Normalizer không tự chuyển 'đ' thành 'd')
		result = result.replace('đ', 'd');

		// 5. Loại bỏ dấu tiếng Việt sử dụng Normalizer
		String temp = Normalizer.normalize(result, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		result = pattern.matcher(temp).replaceAll("");

		return result;
	}

	// ------------------------------------------------------------------

	// ten khong rong, khong bat dau bang ky tu dac biet, gia tieu chuan > 0
	private void kiemTraThongTin(DichVu dichVu) throws NghiepVuException {
		String ten = dichVu.getTenDV();
		if (ten == null || ten.trim().isEmpty()) {
			throw new NghiepVuException("Tên dịch vụ không được để trống");
		}
		if (!ten.trim().matches("^[\\p{L}\\p{N}].*")) {
			throw new NghiepVuException("Tên dịch vụ không được bắt đầu bằng ký tự đặc biệt");
		}
		if (dichVu.getGiaTieuChuan() <= 0) {
			throw new NghiepVuException("Giá tiêu chuẩn phải lớn hơn 0");
		}
	}

	/** doc ResultSet co du cac cot cua bang DichVu thanh doi tuong */
	static DichVu taoDichVu(ResultSet rs) throws SQLException {
		String maDV = rs.getString("maDV");
		String tenDV = rs.getString("tenDV");
		double giaTieuChuan = rs.getDouble("giaTieuChuan");
		return new DichVu(
				maDV,
				tenDV,
				giaTieuChuan);
	}
}
