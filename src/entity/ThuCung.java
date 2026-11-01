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



	/** phu phi hung du: hien +0d, nhat +20.000d, du +50.000d */
	public double tinhPhuPhiDoHungDu() {
		if (doHungDu == null) {
			return 0;
		}
		return doHungDu.getPhuPhi();
	}

	/**
	 * phu phi can nang
	 * cho: ≤10kg 0d, >10-15kg 30.000d, >15-20kg 60.000d, >20kg 90.000d
	 * meo: ≤4kg 0d, >4-6kg 20.000d, >6-8kg 40.000d, >8kg 60.000d
	 */
	public double tinhPhuPhiCanNang() {
		if (loai == Loai.CHO) {
			if (canNang <= 10) {
				return 0;
			}
			if (canNang <= 15) {
				return 30000;
			}
			if (canNang <= 20) {
				return 60000;
			}
			return 90000;
		}
		if (canNang <= 4) {
			return 0;
		}
		if (canNang <= 6) {
			return 20000;
		}
		if (canNang <= 8) {
			return 40000;
		}
		return 60000;
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
