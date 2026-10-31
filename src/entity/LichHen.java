package entity;

import java.time.LocalDate;
import java.util.Objects;

public class LichHen {
	private String maLH;
	private LocalDate thoiGianHen;
	private String ghiChu;
	private TrangThai trangThaiHen;
	
	public LichHen() {
	}

	public LichHen(String maLH, LocalDate thoiGianHen, String ghiChu, TrangThai trangThaiHen) {
		this.maLH = maLH;
		this.thoiGianHen = thoiGianHen;
		this.ghiChu = ghiChu;
		this.trangThaiHen = trangThaiHen;
	}

	public String getMaLH() {
		return maLH;
	}

	public void setMaLH(String maLH) {
		this.maLH = maLH;
	}

	public LocalDate getThoiGianHen() {
		return thoiGianHen;
	}

	public void setThoiGianHen(LocalDate thoiGianHen) {
		this.thoiGianHen = thoiGianHen;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public TrangThai getTrangThaiHen() {
		return trangThaiHen;
	}

	public void setTrangThaiHen(TrangThai trangThaiHen) {
		this.trangThaiHen = trangThaiHen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maLH);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LichHen other = (LichHen) obj;
		return Objects.equals(maLH, other.maLH);
	}
	
	
	
}
