package entity;

public enum TrangThai {
	// Lịch hẹn
	CHO_DEN_HEN("Chờ đến hẹn"),
	DA_TIEP_NHAN("Đã tiếp nhận"),
	DA_HUY("Đã hủy"),
	// Nhân viên
	CON_LAM("Còn làm"),
	DA_NGHI("Đã nghỉ"),
	// Hóa đơn
	CHUA_THANH_TOAN("Chưa thanh toán"),
	DA_THANH_TOAN("Đã thanh toán"),
	// Thú cưng / dịch vụ
	BINH_THUONG("Bình thường"),
	DANG_CHAM_SOC("Đang chăm sóc"),
	HOAN_THANH("Hoàn thành");

	private final String moTa;

	private TrangThai(String moTa) {
		this.moTa = moTa;
	}

	public String getMoTa() {
		return moTa;
	}

	@Override
	public String toString() {
		return moTa;
	}
}
