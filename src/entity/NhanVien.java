package entity;

import java.util.Objects;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private String soDienThoai;
	private String email;
	private TrangThai trangThaiNV;
	private String hinhAnh;
	
	
	
	public NhanVien() {
	}



	public NhanVien(String maNV, String tenNV, String soDienThoai, String email, TrangThai trangThaiNV,
			String hinhAnh) {
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.trangThaiNV = trangThaiNV;
		this.hinhAnh = hinhAnh;
	}



	public String getMaNV() {
		return maNV;
	}



	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}



	public String getTenNV() {
		return tenNV;
	}



	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
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



	public TrangThai getTrangThaiNV() {
		return trangThaiNV;
	}



	public void setTrangThaiNV(TrangThai trangThaiNV) {
		this.trangThaiNV = trangThaiNV;
	}



	public String getHinhAnh() {
		return hinhAnh;
	}



	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}



	@Override
	public int hashCode() {
		return Objects.hash(maNV);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNV, other.maNV);
	}
	
	
	
}
