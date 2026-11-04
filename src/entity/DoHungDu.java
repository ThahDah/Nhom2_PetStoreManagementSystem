package entity;

public enum DoHungDu {
	HIEN("Hiền", 0), NHAT("Nhát", 20000), DU("Dữ", 50000);

	private final String moTa;
	private final double phuPhi;

	private DoHungDu(String moTa, double phuPhi) {
		this.moTa = moTa;
		this.phuPhi = phuPhi;
	}

	public String getMoTa() {
		return moTa;
	}

	public double getPhuPhi() {
		return phuPhi;
	}

	@Override
	public String toString() {
		return moTa;
	}
}
