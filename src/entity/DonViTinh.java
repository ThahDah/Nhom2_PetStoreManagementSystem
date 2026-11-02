
package entity;

public enum DonViTinh {

    CAI("Cái"),
    GOI("Gói"),
    TUI("Túi"),
    BAO("Bao"),
    HOP("Hộp"),
    CHAI("Chai"),
    LON("Lon"),
    KG("Kg"),
    GAM("Gam"),
    VIEN("Viên"),
    CUON("Cuộn"),
    BO("Bộ"),
    CAP("Cặp");

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
