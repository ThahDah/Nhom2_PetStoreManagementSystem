package entity;

import java.time.LocalDate;
import java.util.Objects;

public class HoaDon {
	private String maHD;
	private LocalDate ngayLap;
	private double tongTien;
	private double giamGia;
	private TrangThai trangThaiHD;
	private PhuongThucTT phuongThucTT;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	public HoaDon() {
		// TODO Auto-generated constructor stub
	}

	
	public HoaDon(String maHD, LocalDate ngayLap, double tongTien, double giamGia, TrangThai trangThaiHD,
			PhuongThucTT phuongThucTT, KhachHang khachHang, NhanVien nhanVien) {
		super();
		this.maHD = maHD;
		this.ngayLap = ngayLap;
		this.tongTien = tongTien;
		this.giamGia = giamGia;
		this.trangThaiHD = trangThaiHD;
		this.phuongThucTT = phuongThucTT;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
	}
	

	public String getMaHD() {
		return maHD;
	}


	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}


	public LocalDate getNgayLap() {
		return ngayLap;
	}


	public void setNgayLap(LocalDate ngayLap) {
		this.ngayLap = ngayLap;
	}


	public double getTongTien() {
		return tongTien;
	}


	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}


	public double getGiamGia() {
		return giamGia;
	}


	public void setGiamGia(double giamGia) {
		this.giamGia = giamGia;
	}


	public TrangThai getTrangThaiHD() {
		return trangThaiHD;
	}


	public void setTrangThaiHD(TrangThai trangThaiHD) {
		this.trangThaiHD = trangThaiHD;
	}


	public PhuongThucTT getPhuongThucTT() {
		return phuongThucTT;
	}


	public void setPhuongThucTT(PhuongThucTT phuongThucTT) {
		this.phuongThucTT = phuongThucTT;
	}


	public KhachHang getKhachHang() {
		return khachHang;
	}


	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}


	public NhanVien getNhanVien() {
		return nhanVien;
	}


	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}


	@Override
	public int hashCode() {
		return Objects.hash(maHD);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHD, other.maHD);
	}
	
	
}
