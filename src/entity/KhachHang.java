package entity;


import java.util.Objects;

public class KhachHang {
	private String maKH;
	private String tenKH;
	private String soDienThoai;
	private String email;
	private int diemTichLuy;
	
	
	
	
	public KhachHang() {
		super();
	}



	public KhachHang(String maKH, String tenKH, String soDienThoai, String email, int diemTichLuy) {
		
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.diemTichLuy = diemTichLuy;
		
	}

	


	public String getMaKH() {
		return maKH;
	}



	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}



	public String getTenKH() {
		return tenKH;
	}



	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
	}



	public String getSoDienThoai() {
		return soDienThoai;
	}



	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public int getDiemTichLuy() {
		return diemTichLuy;
	}



	public void setDiemTichLuy(int diemTichLuy) {
		this.diemTichLuy = diemTichLuy;
	}



	



	



	@Override
	public int hashCode() {
		return Objects.hash(maKH);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKH, other.maKH);
	}
	
	
}
