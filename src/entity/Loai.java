package entity;

public enum Loai {
	CHO("Chó"), MEO("Mèo");

	private final String moTa;

	private Loai(String moTa) {
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
