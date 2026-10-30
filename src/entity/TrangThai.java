package entity;

public enum TrangThai {
	DANG_LAM("Đang làm"),
    TAM_NGHI("Tạm nghỉ"),
    NGHI_VIEC("Nghỉ việc");

	private final String moTa;

	TrangThai(String moTa) {
		this.moTa = moTa;
	}

	public String getMoTa() {
		return moTa;
	}

	@Override
	public String toString() {
		return this.moTa;
	}
}
