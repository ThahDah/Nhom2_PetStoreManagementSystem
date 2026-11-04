package entity;

public class ChiTietLichHen {
	private LichHen lichHen;
	private ThuCung thuCung;
	private DichVu dichVu;
	private double donGia;
	
	public ChiTietLichHen() {
		// TODO Auto-generated constructor stub
	}

	public ChiTietLichHen(LichHen lichHen, ThuCung thuCung, DichVu dichVu, double donGia) {
		super();
		this.lichHen = lichHen;
		this.thuCung = thuCung;
		this.dichVu = dichVu;
		this.donGia = donGia;
	}

	public LichHen getLichHen() {
		return lichHen;
	}

	public void setLichHen(LichHen lichHen) {
		this.lichHen = lichHen;
	}

	public ThuCung getThuCung() {
		return thuCung;
	}

	public void setThuCung(ThuCung thuCung) {
		this.thuCung = thuCung;
	}

	public DichVu getDichVu() {
		return dichVu;
	}

	public void setDichVu(DichVu dichVu) {
		this.dichVu = dichVu;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}
	
	/** gia thuc te = gia tieu chuan + phu phi can nang + phu phi do hung du */
	public double tinhGiaDichVu(ThuCung thuCung) {
		return dichVu.getGiaTieuChuan() + thuCung.tinhPhuPhiCanNang() + thuCung.tinhPhuPhiDoHungDu();
	}

	/** tong phu phu phi = don gia da tinh tru gia tieu chuan dich vu  */
	public double tinhPhuPhi() {
		return donGia - dichVu.getGiaTieuChuan();
	}
	
	
}
