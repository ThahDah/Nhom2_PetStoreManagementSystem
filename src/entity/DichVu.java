package entity;

public class DichVu {
	private String maDV;
	private String tenDV;
	private double giaTieuChuan;
	
	public DichVu() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public DichVu(String maDV, String tenDV, double giaTieuChuan) {
		super();
		this.maDV = maDV;
		this.tenDV = tenDV;
		this.giaTieuChuan = giaTieuChuan;
	}



	public String getMaDV() {
		return maDV;
	}
	public void setMaDV(String maDV) {
		this.maDV = maDV;
	}
	public String getTenDV() {
		return tenDV;
	}
	public void setTenDV(String tenDV) {
		this.tenDV = tenDV;
	}
	public double getGiaTieuChuan() {
		return giaTieuChuan;
	}
	public void setGiaTieuChuan(double giaTieuChuan) {
		this.giaTieuChuan = giaTieuChuan;
	}
	
	
}
