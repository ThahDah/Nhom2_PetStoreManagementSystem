package entity;

import java.util.Objects;

public class ThuCung {
	private String maThuCung;
	private String tenThuCung;
	private Loai loai;
	private double canNang;
	private String ghiChu;
	private TrangThai trangThaiThuCung;
	private DoHungDu doHungDu ;
	private KhachHang chuSoHuu;
	
	public ThuCung() {
		
	}


	public ThuCung(String maThuCung, String tenThuCung, Loai loai, double canNang, String ghiChu,
			TrangThai trangThaiThuCung, DoHungDu doHungDu, KhachHang chuSoHuu) {
		super();
		this.maThuCung = maThuCung;
		this.tenThuCung = tenThuCung;
		this.loai = loai;
		this.canNang = canNang;
		this.ghiChu = ghiChu;
		this.trangThaiThuCung = trangThaiThuCung;
		this.doHungDu = doHungDu;
		this.chuSoHuu = chuSoHuu;
	}

	

	public String getMaThuCung() {
		return maThuCung;
	}



	public void setMaThuCung(String maThuCung) {
		this.maThuCung = maThuCung;
	}



	public String getTenThuCung() {
		return tenThuCung;
	}



	public void setTenThuCung(String tenThuCung) {
		this.tenThuCung = tenThuCung;
	}



	public Loai getLoai() {
		return loai;
	}



	public void setLoai(Loai loai) {
		this.loai = loai;
	}



	public double getCanNang() {
		return canNang;
	}



	public void setCanNang(double canNang) {
		this.canNang = canNang;
	}



	public String getGhiChu() {
		return ghiChu;
	}



	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}



	public TrangThai getTrangThaiThuCung() {
		return trangThaiThuCung;
	}



	public void setTrangThaiThuCung(TrangThai trangThaiThuCung) {
		this.trangThaiThuCung = trangThaiThuCung;
	}



	public DoHungDu getDoHungDu() {
		return doHungDu;
	}



	public void setDoHungDu(DoHungDu doHungDu) {
		this.doHungDu = doHungDu;
	}



	public KhachHang getChuSoHuu() {
		return chuSoHuu;
	}



	public void setChuSoHuu(KhachHang chuSoHuu) {
		this.chuSoHuu = chuSoHuu;
	}



	@Override
	public int hashCode() {
		return Objects.hash(maThuCung);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThuCung other = (ThuCung) obj;
		return Objects.equals(maThuCung, other.maThuCung);
	}
	

}
