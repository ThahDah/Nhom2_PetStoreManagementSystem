package entity;

public enum DonViTinh {
	GOI("Gói"), CHAI("Chai"), CAI("Cái"), TUI("Túi"), HOP("Hộp");
    private final String moTa;
    private DonViTinh(String moTa) {
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
