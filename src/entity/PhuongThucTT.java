package entity;

public enum PhuongThucTT {
	TIEN_MAT("Tiền mặt"), THE_TIN_DUNG("Thẻ tín dụng");
    private final String moTa;
    private PhuongThucTT(String moTa) {
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
