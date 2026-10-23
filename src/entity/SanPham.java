package entity;

import java.util.Objects;

public class SanPham {
	private String maSP;
	private String tenSP;
	private double giaNY;
	private int soLuongTon;
	private DonViTinh donViTinh;
	
	public SanPham() {
		// TODO Auto-generated constructor stub
	}
	
	public SanPham(String maSP, String tenSP, double giaNY, int soLuongTon, DonViTinh donViTinh) {
		super();
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.giaNY = giaNY;
		this.soLuongTon = soLuongTon;
		this.donViTinh = donViTinh;
	}

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public double getGiaNY() {
		return giaNY;
	}

	public void setGiaNY(double giaNY) {
		this.giaNY = giaNY;
	}

	public int getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(int soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public DonViTinh getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(DonViTinh donViTinh) {
		this.donViTinh = donViTinh;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maSP);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SanPham other = (SanPham) obj;
		return Objects.equals(maSP, other.maSP);
	}
	
	
	
}
