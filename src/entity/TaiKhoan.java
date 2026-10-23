package entity;

import java.util.Objects;

public class TaiKhoan {
	private String tenTK;
	private String matKhau;
	private VaiTroTK vaiTroTK;
	private TrangThai TrangThaiTK;
	private NhanVien nhanVien;
	
	public TaiKhoan() {
		
	}

	public TaiKhoan(String tenTK, String matKhau, VaiTroTK vaiTroTK, TrangThai trangThaiTK, NhanVien nhanVien) {
		this.tenTK = tenTK;
		this.matKhau = matKhau;
		this.vaiTroTK = vaiTroTK;
		TrangThaiTK = trangThaiTK;
		this.nhanVien = nhanVien;
	}

	public String getTenTK() {
		return tenTK;
	}

	public void setTenTK(String tenTK) {
		this.tenTK = tenTK;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public VaiTroTK getVaiTroTK() {
		return vaiTroTK;
	}

	public void setVaiTroTK(VaiTroTK vaiTroTK) {
		this.vaiTroTK = vaiTroTK;
	}

	public TrangThai getTrangThaiTK() {
		return TrangThaiTK;
	}

	public void setTrangThaiTK(TrangThai trangThaiTK) {
		TrangThaiTK = trangThaiTK;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tenTK);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(tenTK, other.tenTK);
	}
	
	
}
