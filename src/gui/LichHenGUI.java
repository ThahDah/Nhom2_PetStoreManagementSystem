package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import common.DinhDang;
import dao.ChiTietLichHenDAO;
import dao.LichHenDAO;
import dao.NghiepVuException;
import entity.ChiTietLichHen;
import entity.KhachHang;
import entity.LichHen;
import entity.NhanVien;
import entity.TrangThai;

public class LichHenGUI extends JPanel implements ActionListener, MouseListener {

	private static final DateTimeFormatter DINH_DANG_MA = DateTimeFormatter.ofPattern("ddMMyy");
	private JButton btnKhoangNgay;
	private JButton btnApDungNgay;
	private JButton btnHuyNgay;
	private JComboBox<String> cboLoaiNgay;
	private JDateChooser dateFrom;
	private JDateChooser dateTo;
	private JDialog dialogChonNgay;
	private LocalDate tuNgay;
	private LocalDate denNgay;
	private boolean locTheoNgayTao = false; // false: ngày hẹn, true: ngày tạo

	private JButton btnTaoLichHen;
	private DefaultTableModel model;
	private JTable table;
	private LichHenDAO lichHenDAO;
	private ChiTietLichHenDAO chiTietLichHenDAO;
	private NhanVien nhanVienDangNhap;
	private ArrayList<LichHen> dsLichHen;
	private JTextField txtTimKiem;
	private JTextField txtTimMaLichHen;
	private JButton btnTim;
	private JButton btnLamMoi;
	private JButton btnHuyLichHen;

	private static final int GIOI_HAN = 200;
	private JLabel lblCheDoXem;
	private JLabel lblSoLuong;
	private JLabel lblGioiHan;
	private JComboBox<String> cboTrangThai;

	public LichHenGUI(NhanVien nhanVienDangNhap) {
		this.nhanVienDangNhap = nhanVienDangNhap;
		lichHenDAO = new LichHenDAO();
		chiTietLichHenDAO = new ChiTietLichHenDAO();
		dsLichHen = new ArrayList<LichHen>();

		setLayout(new BorderLayout(10, 10));

		// nua tren
		JPanel pnlTopHalf = new JPanel();
		pnlTopHalf.setLayout(new BoxLayout(pnlTopHalf, BoxLayout.Y_AXIS));
		pnlTopHalf.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// header
		JPanel pnlHeader = new JPanel(new BorderLayout());
		JLabel lblTitle = new JLabel("Lịch hẹn", JLabel.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
		lblTitle.setForeground(new Color(232, 116, 59));
		pnlHeader.add(lblTitle, BorderLayout.CENTER);

		// nut tao lich hen
		JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnTaoLichHen = new JButton("Tạo lịch hẹn");
		btnHuyLichHen = new JButton("Hủy lịch hẹn");
		pnlHeaderRight.add(btnHuyLichHen);
		pnlHeaderRight.add(btnTaoLichHen);
		pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

		JPanel pnlHeaderLeft = new JPanel();
//		pnlHeaderLeft.setPreferredSize(btnTaoLichHen.getPreferredSize());
		pnlHeaderLeft.setPreferredSize(pnlHeaderRight.getPreferredSize());
		pnlHeader.add(pnlHeaderLeft, BorderLayout.WEST);

		JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
		JLabel lblTimKiem = new JLabel("Tìm khách hàng (MãKH/TênKH/SĐT):");
		lblTimKiem.setFont(lblTimKiem.getFont());
		txtTimKiem = new JTextField(20);
		JLabel lblTimMaLichHen = new JLabel("Tìm mã lịch hẹn:");
		txtTimMaLichHen = new JTextField(12);
		btnTim = new JButton("Tìm");
		btnLamMoi = new JButton("Làm mới");
		pnlTimKiem.add(lblTimKiem);
		pnlTimKiem.add(txtTimKiem);
		pnlTimKiem.add(lblTimMaLichHen);
		pnlTimKiem.add(txtTimMaLichHen);
		pnlTimKiem.add(btnTim);
		pnlTimKiem.add(btnLamMoi);
		pnlTimKiem.setMaximumSize(new Dimension(Integer.MAX_VALUE, pnlTimKiem.getPreferredSize().height));

		btnKhoangNgay = new JButton("Chọn khoảng ngày");
		JPanel pnlLocNgay = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
		pnlLocNgay.add(new JLabel("Lọc theo ngày:"));
		pnlLocNgay.add(btnKhoangNgay);
		pnlLocNgay.setMaximumSize(new Dimension(Integer.MAX_VALUE, pnlLocNgay.getPreferredSize().height));

		cboTrangThai = new JComboBox<String>(new String[] { "Mặc định (cần xử lý)", "Tất cả trạng thái", "Chờ đến hẹn",
				"Đã tiếp nhận", "Hoàn thành", "Đã hủy" });
		pnlLocNgay.add(new JLabel("Trạng thái:"));
		pnlLocNgay.add(cboTrangThai);

		pnlTopHalf.add(pnlHeader);
		pnlTopHalf.add(pnlTimKiem);
		pnlTopHalf.add(pnlLocNgay);

		lblCheDoXem = new JLabel();
		lblCheDoXem.setForeground(Color.GRAY);
		JPanel pnlCheDoXem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		pnlCheDoXem.add(lblCheDoXem);
		pnlCheDoXem.setMaximumSize(new Dimension(Integer.MAX_VALUE, pnlCheDoXem.getPreferredSize().height + 10));
		pnlTopHalf.add(pnlCheDoXem);
		
		lblSoLuong = new JLabel(" ");
		lblSoLuong.setFont(lblSoLuong.getFont().deriveFont(Font.BOLD, 16f));   // chữ to, đậm
		lblSoLuong.setForeground(new Color(0, 102, 204));                      // màu xanh dương cho dễ thấy
		lblGioiHan = new JLabel(" ");
		lblGioiHan.setForeground(Color.GRAY);
		JPanel pnlKetQua = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		pnlKetQua.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));     // cách dòng mô tả phía trên 10px
		pnlKetQua.add(lblSoLuong);
		pnlKetQua.add(lblGioiHan);
		pnlKetQua.setMaximumSize(new Dimension(Integer.MAX_VALUE, pnlKetQua.getPreferredSize().height));
		pnlTopHalf.add(pnlKetQua);
		add(pnlTopHalf, BorderLayout.NORTH);

		// table
		JPanel pnlTable = new JPanel();
		pnlTable.setLayout(new BorderLayout());
		pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//		String col[] = { "Mã lịch hẹn", "Khách hàng", "Thú cưng", "Dịch vụ", "Ngày hẹn", "Trạng thái" };
		String col[] = { "Mã lịch hẹn", "Khách hàng", "Số điện thoại", "Thú cưng", "Dịch vụ", "Ngày hẹn",
				"Trạng thái" };
		model = new DefaultTableModel(col, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(26);
		table.getTableHeader().setReorderingAllowed(false);

		JScrollPane pane = new JScrollPane(table);
		pnlTable.add(pane);
		add(pnlTable, BorderLayout.CENTER);

		// chon ngay
		dateFrom = new JDateChooser();
		dateTo = new JDateChooser();
		dateFrom.setDateFormatString("dd/MM/yyyy");
		dateTo.setDateFormatString("dd/MM/yyyy");
		// chi cho chon ngay, khong cho go
		((JTextField) dateFrom.getDateEditor().getUiComponent()).setEditable(false);
		((JTextField) dateTo.getDateEditor().getUiComponent()).setEditable(false);

		docDuLieuVaoTable();

		table.addMouseListener(this);
		btnTaoLichHen.addActionListener(this);

		btnTim.addActionListener(this);
		btnLamMoi.addActionListener(this);
		txtTimKiem.addActionListener(this);
		txtTimMaLichHen.addActionListener(this);
		btnHuyLichHen.addActionListener(this);
		btnKhoangNgay.addActionListener(this);
		cboTrangThai.addActionListener(this);
	}

	// ham doc du lieu vao bang
//	public void docDuLieuVaoTable() {
//		String tuKhoa = chuanHoa(txtTimKiem.getText());
//		String maLichHenTim = chuanHoa(txtTimMaLichHen.getText());
//		model.setRowCount(0);
//		dsLichHen.clear();
//		try {
//			for (LichHen lichHen : lichHenDAO.layTatCa()) {
//				if (!chuanHoa(lichHen.getMaLH()).contains(maLichHenTim)) {
//					continue;
//				}
//				if (tuNgay != null && denNgay != null) {
//					LocalDate ngayLoc = locTheoNgayTao ? layNgayTao(lichHen) : lichHen.getThoiGianHen();
//					if (ngayLoc == null || ngayLoc.isBefore(tuNgay) || ngayLoc.isAfter(denNgay)) {
//						continue;
//					}
//				}
//				ArrayList<ChiTietLichHen> dsChiTiet = new ArrayList<ChiTietLichHen>(
//						chiTietLichHenDAO.timTheoLichHen(lichHen));
//				KhachHang khachHang = dsChiTiet.isEmpty() ? null : dsChiTiet.get(0).getThuCung().getChuSoHuu();
//				if (!tuKhoa.isEmpty()) {
//					if (khachHang == null) {
//						continue;
//					}
//					if (!chuanHoa(khachHang.getMaKH()).contains(tuKhoa)
//							&& !chuanHoa(khachHang.getTenKH()).contains(tuKhoa)
//							&& !chuanHoa(khachHang.getSoDienThoai()).contains(tuKhoa)) {
//						continue;
//					}
//				}
//
////				StringBuilder tenThuCung = new StringBuilder();
////				StringBuilder tenDichVu = new StringBuilder();
////				for (ChiTietLichHen chiTiet : dsChiTiet) {
////					if (tenThuCung.length() > 0) {
////						tenThuCung.append(", ");
////						tenDichVu.append(", ");
////					}
////					tenThuCung.append(chiTiet.getThuCung().getTenThuCung());
////					tenDichVu.append(chiTiet.getDichVu().getTenDV());
////				}
//				// moi ten thu cung / dich vu chi hien mot lan
//				LinkedHashSet<String> dsTenThuCung = new LinkedHashSet<String>();
//				LinkedHashSet<String> dsTenDichVu = new LinkedHashSet<String>();
//				for (ChiTietLichHen chiTiet : dsChiTiet) {
//					dsTenThuCung.add(chiTiet.getThuCung().getTenThuCung());
//					dsTenDichVu.add(chiTiet.getDichVu().getTenDV());
//				}
//
//				String tenKhachHang = dsChiTiet.isEmpty() ? "" : dsChiTiet.get(0).getThuCung().getChuSoHuu().getTenKH();
////				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, tenThuCung.toString(),
////						tenDichVu.toString(), lichHen.getThoiGianHen().format(DinhDang.NGAY),
////						lichHen.getTrangThaiHen() });
//
////				dsLichHen.add(lichHen);
////				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, String.join(", ", dsTenThuCung),
////						String.join(", ", dsTenDichVu), lichHen.getThoiGianHen().format(DinhDang.NGAY),
////						lichHen.getTrangThaiHen() });
//				String soDienThoai = khachHang == null ? "" : khachHang.getSoDienThoai();
//				dsLichHen.add(lichHen);
//				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, soDienThoai,
//						String.join(", ", dsTenThuCung), String.join(", ", dsTenDichVu),
//						lichHen.getThoiGianHen().format(DinhDang.NGAY), lichHen.getTrangThaiHen() });
//			}
//		} catch (SQLException ex) {
//			JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
//					JOptionPane.ERROR_MESSAGE);
//		}
//	}

	// ham doc du lieu vao bang (ban moi: mac dinh chi hien lich can xu ly, loc tren
	// SQL)
	public void docDuLieuVaoTable() {
		String maLichHenTim = txtTimMaLichHen.getText().trim();
		String tuKhoa = txtTimKiem.getText().trim();
		boolean coLocNgay = tuNgay != null && denNgay != null;
		int viTriTrangThai = cboTrangThai.getSelectedIndex();
		boolean macDinh = maLichHenTim.isEmpty() && tuKhoa.isEmpty() && !coLocNgay && viTriTrangThai == 0;

		// 0: mac dinh, 1: tat ca, 2..5: tung trang thai
		TrangThai trangThaiLoc = null;
		if (viTriTrangThai == 2) {
			trangThaiLoc = TrangThai.CHO_DEN_HEN;
		} else if (viTriTrangThai == 3) {
			trangThaiLoc = TrangThai.DA_TIEP_NHAN;
		} else if (viTriTrangThai == 4) {
			trangThaiLoc = TrangThai.HOAN_THANH;
		} else if (viTriTrangThai == 5) {
			trangThaiLoc = TrangThai.DA_HUY;
		}

		model.setRowCount(0);
		dsLichHen.clear();
		try {
			List<LichHen> dsTim = lichHenDAO.timLichHen(maLichHenTim, tuKhoa, tuNgay, denNgay, locTheoNgayTao,
					trangThaiLoc, macDinh, GIOI_HAN+1);
			boolean biCat = dsTim.size() > GIOI_HAN;
			if (biCat) {
				dsTim = dsTim.subList(0, GIOI_HAN);
			}
			
			for (LichHen lichHen : dsTim) {
				List<ChiTietLichHen> dsChiTiet = chiTietLichHenDAO.timTheoLichHen(lichHen);
				KhachHang khachHang = dsChiTiet.isEmpty() ? null : dsChiTiet.get(0).getThuCung().getChuSoHuu();

				// moi ten thu cung / dich vu chi hien mot lan
				LinkedHashSet<String> dsTenThuCung = new LinkedHashSet<String>();
				LinkedHashSet<String> dsTenDichVu = new LinkedHashSet<String>();
				for (ChiTietLichHen chiTiet : dsChiTiet) {
					dsTenThuCung.add(chiTiet.getThuCung().getTenThuCung());
					dsTenDichVu.add(chiTiet.getDichVu().getTenDV());
				}

				String tenKhachHang = khachHang == null ? "" : khachHang.getTenKH();
				String soDienThoai = khachHang == null ? "" : khachHang.getSoDienThoai();
				dsLichHen.add(lichHen);
				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, soDienThoai,
						String.join(", ", dsTenThuCung), String.join(", ", dsTenDichVu),
						lichHen.getThoiGianHen().format(DinhDang.NGAY), lichHen.getTrangThaiHen() });
			}

//			String moTa = macDinh
//					? "Đang hiển thị lịch hẹn hôm nay và lịch cũ chưa xử lý xong. Chọn trạng thái, lọc ngày hoặc tìm kiếm để xem thêm."
//					: "Đang hiển thị kết quả tìm kiếm / lọc. Giới hạn (200) kết quả.";
//			// if (dsTim.size() >= GIOI_HAN) {
//			if (biCat) {
//				moTa += " Chỉ hiện tối đa " + GIOI_HAN + " lịch mới nhất, hãy thu hẹp điều kiện.";
//			}
//			lblCheDoXem.setText(moTa);
			String moTa = macDinh
					? "Đang hiển thị lịch hẹn hôm nay và lịch cũ chưa xử lý xong. Chọn trạng thái, lọc ngày hoặc tìm kiếm để xem thêm."
					: "Đang hiển thị kết quả tìm kiếm / lọc.";
			lblCheDoXem.setText(moTa);
			if (biCat) {
				lblSoLuong.setText("Tìm thấy hơn " + GIOI_HAN + " lịch hẹn (đang hiển thị " + GIOI_HAN + ")");
				lblGioiHan.setText("| Còn lịch hẹn cũ hơn chưa hiển thị, hãy thu hẹp điều kiện");
				lblGioiHan.setForeground(Color.RED);
			} else {
				lblSoLuong.setText("Tìm thấy " + dsTim.size() + " lịch hẹn");
				lblGioiHan.setText("| Mỗi lần hiển thị tối đa " + GIOI_HAN + " lịch hẹn mới nhất");
				lblGioiHan.setForeground(Color.GRAY);
			}
			
			
			
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Hàm AI tạo
	// Viết hàm Java tên chuanHoa(String s) dùng để chuẩn hóa chuỗi tiếng Việt thành
	// chữ thường không dấu, đã xử lý khoảng trắng thừa và trường hợp null
	public static String chuanHoa(String s) {
		// 1. Kiểm tra trường hợp null
		if (s == null) {
			return ""; // Hoặc trả về null tùy logic nghiệp vụ của bạn
		}

		// 2. Chuyển thành chữ thường và loại bỏ khoảng trắng thừa đầu/cuối
		String result = s.toLowerCase().trim();

		// 3. Thay thế các khoảng trắng liên tiếp ở giữa thành một khoảng trắng duy nhất
		result = result.replaceAll("\\s+", " ");

		// 4. Xử lý riêng ký tự 'đ' và 'Đ' (vì Normalizer không tự chuyển 'đ' thành 'd')
		result = result.replace('đ', 'd');

		// 5. Loại bỏ dấu tiếng Việt sử dụng Normalizer
		String temp = Normalizer.normalize(result, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		result = pattern.matcher(temp).replaceAll("");

		return result;
	}

	// ngay tao lay tu ma lich hen: "LH" + ddMMyy + stt
	private LocalDate layNgayTao(LichHen lichHen) {
		String ma = lichHen.getMaLH() == null ? "" : lichHen.getMaLH().trim();
		if (ma.length() < 8) {
			return null;
		}
		try {
			return LocalDate.parse(ma.substring(2, 8), DINH_DANG_MA);
		} catch (DateTimeParseException ex) {
			return null;
		}
	}

	// mo hop thoai chon loai ngay va khoang ngay
	private void moChonKhoangNgay() {
		Window owner = SwingUtilities.getWindowAncestor(this);
		dialogChonNgay = new JDialog(owner, "Chọn khoảng ngày", Dialog.ModalityType.APPLICATION_MODAL);

		JPanel pnlNgay = new JPanel();
		pnlNgay.setLayout(new BoxLayout(pnlNgay, BoxLayout.Y_AXIS));
		pnlNgay.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		cboLoaiNgay = new JComboBox<String>(new String[] { "Ngày hẹn", "Ngày tạo" });
		cboLoaiNgay.setSelectedIndex(locTheoNgayTao ? 1 : 0);
		cboLoaiNgay.setPreferredSize(new Dimension(220, 30));
		cboLoaiNgay.setMaximumSize(new Dimension(220, 30));

		dateFrom.setDate(tuNgay == null ? null : Date.from(tuNgay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dateTo.setDate(denNgay == null ? null : Date.from(denNgay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dateFrom.setPreferredSize(new Dimension(220, 30));
		dateFrom.setMaximumSize(new Dimension(220, 30));
		dateTo.setPreferredSize(new Dimension(220, 30));
		dateTo.setMaximumSize(new Dimension(220, 30));

		pnlNgay.add(new JLabel("Lọc theo:"));
		pnlNgay.add(cboLoaiNgay);
		pnlNgay.add(new JLabel("Từ ngày:"));
		pnlNgay.add(dateFrom);
		pnlNgay.add(new JLabel("Đến ngày:"));
		pnlNgay.add(dateTo);

		JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnHuyNgay = new JButton("Hủy lọc");
		btnApDungNgay = new JButton("Áp dụng");
		btnHuyNgay.addActionListener(this);
		btnApDungNgay.addActionListener(this);
		pnlNut.add(btnHuyNgay);
		pnlNut.add(btnApDungNgay);
		pnlNgay.add(pnlNut);

		dialogChonNgay.add(pnlNgay);
		dialogChonNgay.pack();
		dialogChonNgay.setResizable(false);
		dialogChonNgay.setLocationRelativeTo(owner);
		dialogChonNgay.setVisible(true);
	}

	// ap dung khoang ngay
	private void apDungKhoangNgay() {
		Date ngayBatDau = dateFrom.getDate();
		Date ngayKetThuc = dateTo.getDate();
		if (ngayBatDau == null || ngayKetThuc == null) {
			JOptionPane.showMessageDialog(dialogChonNgay, "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
			return;
		}
		LocalDate batDau = ngayBatDau.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate ketThuc = ngayKetThuc.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (batDau.isAfter(ketThuc)) {
			JOptionPane.showMessageDialog(dialogChonNgay, "Ngày bắt đầu không được sau ngày kết thúc.");
			return;
		}
		tuNgay = batDau;
		denNgay = ketThuc;
		locTheoNgayTao = cboLoaiNgay.getSelectedIndex() == 1;
		btnKhoangNgay.setText((locTheoNgayTao ? "Ngày tạo: " : "Ngày hẹn: ") + DinhDang.NGAY.format(tuNgay) + " - "
				+ DinhDang.NGAY.format(denNgay));
		dialogChonNgay.dispose();
		docDuLieuVaoTable();
		if (model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn phù hợp", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// bo loc ngay (khi dong hop thoai dang mo)
	private void huyLocNgay() {
		datLaiLocNgay();
		dialogChonNgay.dispose();
		docDuLieuVaoTable();
	}

	private void datLaiLocNgay() {
		tuNgay = null;
		denNgay = null;
		locTheoNgayTao = false;
		dateFrom.setDate(null);
		dateTo.setDate(null);
		btnKhoangNgay.setText("Chọn khoảng ngày");
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTaoLichHen)) {
			TaoLichHenGUI taoLichHenGUI = new TaoLichHenGUI(SwingUtilities.getWindowAncestor(this), nhanVienDangNhap);
			taoLichHenGUI.setVisible(true);
			if (taoLichHenGUI.isDaTao()) {
				docDuLieuVaoTable();
			}
//		}else if (o.equals(btnTim) || o.equals(txtTimKiem)) {
//		    docDuLieuVaoTable();
//		    if (model.getRowCount() == 0 && !txtTimKiem.getText().trim().isEmpty()) {
//		        JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn của khách hàng này",
//		                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//		    }
//		} else if (o.equals(btnLamMoi)) {
//		    txtTimKiem.setText("");
//		    docDuLieuVaoTable();
//		}
		} else if (o.equals(cboTrangThai)) {
			docDuLieuVaoTable();
		} else if (o.equals(btnKhoangNgay)) {
			moChonKhoangNgay();
		} else if (o.equals(btnApDungNgay)) {
			apDungKhoangNgay();
		} else if (o.equals(btnHuyNgay)) {
			huyLocNgay();
		} else if (o.equals(btnTim) || o.equals(txtTimKiem) || o.equals(txtTimMaLichHen)) {
			docDuLieuVaoTable();
			if (model.getRowCount() == 0
					&& (!txtTimKiem.getText().trim().isEmpty() || !txtTimMaLichHen.getText().trim().isEmpty())) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn phù hợp", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (o.equals(btnLamMoi)) {
			txtTimKiem.setText("");
			txtTimMaLichHen.setText("");
			datLaiLocNgay();
			cboTrangThai.setSelectedIndex(0);
			docDuLieuVaoTable();
		} else if (o.equals(btnHuyLichHen)) {
			int row = table.getSelectedRow();
			if (row < 0 || row >= dsLichHen.size()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch hẹn cần hủy trên bảng!");
				return;
			}
			LichHen lichHen = dsLichHen.get(row);
			int confirm = JOptionPane.showConfirmDialog(this,
					"Bạn có chắc chắn muốn hủy lịch hẹn " + lichHen.getMaLH() + " không?", "Xác nhận hủy",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}
			try {
				lichHenDAO.huyLichHen(lichHen.getMaLH());
			} catch (NghiepVuException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this, "Đã hủy lịch hẹn thành công!");
			docDuLieuVaoTable();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// double click moi mo chi tiet
		if (e.getClickCount() == 2) {
			int row = table.rowAtPoint(e.getPoint());
			if (row >= 0 && row < dsLichHen.size()) {
//				new ChiTietLichHenGUI(SwingUtilities.getWindowAncestor(this), dsLichHen.get(row)).setVisible(true);
				ChiTietLichHenGUI chiTietLichHenGUI = new ChiTietLichHenGUI(SwingUtilities.getWindowAncestor(this),
						dsLichHen.get(row));
				chiTietLichHenGUI.setVisible(true);
				// da hoan thanh dich vu thi nap lai bang de cap nhat trang thai
				if (chiTietLichHenGUI.isDaCapNhat()) {
					docDuLieuVaoTable();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
