package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.DichVuDAO;
import dao.KhachHangDAO;
import dao.LichHenDAO;
import dao.NghiepVuException;
import dao.ThuCungDAO;
import entity.ChiTietLichHen;
import entity.DichVu;
import entity.DoHungDu;
import entity.KhachHang;
import entity.LichHen;
import entity.Loai;
import entity.NhanVien;
import entity.ThuCung;
import entity.TrangThai;

public class TaoLichHenGUI extends JDialog implements ActionListener {
	private JTextField txtMaLichHen;
	private JTextField txtMaNhanVien;
	private JTextField txtSoDienThoai;
	private JTextField txtTenKhachHang;
	private JComboBox<ThuCung> cbTenThuCung;
	private JComboBox<DichVu> cbDichVu;
	private JComboBox<Loai> cbLoai;
	private JTextField txtCanNang;
	private JTextField txtTrangThaiThuCung;
	private JComboBox<DoHungDu> cbDoHungDu;
	private JTextField txtNgayHen;
	private JTextField txtGhiChu;
	private JButton btnTimKhachHang;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnXacNhan;
	private JButton btnHuy;
	private JLabel lblTongTien;
	private JLabel lblGiaTriTongTien;
	private DefaultTableModel model;
	private JTable table;

	private KhachHangDAO khachHangDAO;
	private ThuCungDAO thuCungDAO;
	private DichVuDAO dichVuDAO;
	private LichHenDAO lichHenDAO;

	private NhanVien nhanVienDangNhap;
	private KhachHang khachHangHienTai;
	private String maLichHen;
	private ArrayList<ChiTietLichHen> dsChiTietTam;
	private boolean daTao;

	public TaoLichHenGUI(Window owner, NhanVien nhanVienDangNhap) {
		super(owner, "Tạo lịch hẹn", ModalityType.APPLICATION_MODAL);
		this.nhanVienDangNhap = nhanVienDangNhap;
		khachHangDAO = new KhachHangDAO();
		thuCungDAO = new ThuCungDAO();
		dichVuDAO = new DichVuDAO();
		lichHenDAO = new LichHenDAO();
		dsChiTietTam = new ArrayList<ChiTietLichHen>();
		daTao = false;
		maLichHen = "";

		setLayout(new BorderLayout(10, 10));

		// nua tren
		JPanel pnlTopHalf = new JPanel();
		pnlTopHalf.setLayout(new BoxLayout(pnlTopHalf, BoxLayout.Y_AXIS));
		pnlTopHalf.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

		// thong tin --- form nhap lieu
		JPanel pnlFormWrapper = new JPanel(new BorderLayout());
		pnlFormWrapper.setBorder(BorderFactory.createTitledBorder("Thông tin lịch hẹn:"));

		JPanel pnlFormFields = new JPanel();
		pnlFormFields.setLayout(new BoxLayout(pnlFormFields, BoxLayout.X_AXIS));
		pnlFormFields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		int labelWidth = 110;
		int lableHeight = 25;
		Dimension kichThuocNhan = new Dimension(labelWidth, lableHeight);
		Color mauChiDoc = new Color(240, 240, 240);
		Color chuChiDoc = new Color(80, 80, 80);
		// col 1
		JPanel pnlCol1 = new JPanel();
		pnlCol1.setLayout(new BoxLayout(pnlCol1, BoxLayout.Y_AXIS));

		// ma lich hen
		JPanel pnlMaLichHen = new JPanel();
		pnlMaLichHen.setLayout(new BoxLayout(pnlMaLichHen, BoxLayout.X_AXIS));
		JLabel lblMaLichHen = new JLabel("Mã lịch hẹn:");
		lblMaLichHen.setPreferredSize(kichThuocNhan);
		lblMaLichHen.setMinimumSize(kichThuocNhan);
		lblMaLichHen.setMaximumSize(kichThuocNhan);
		txtMaLichHen = new JTextField();
		txtMaLichHen.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		txtMaLichHen.setEditable(false);
		txtMaLichHen.setBackground(mauChiDoc);
		txtMaLichHen.setForeground(chuChiDoc);
		pnlMaLichHen.add(lblMaLichHen);
		pnlMaLichHen.add(txtMaLichHen);
		pnlCol1.add(pnlMaLichHen);
		pnlCol1.add(Box.createVerticalStrut(10));

		// ma nhan vien
		JPanel pnlMaNhanVien = new JPanel();
		pnlMaNhanVien.setLayout(new BoxLayout(pnlMaNhanVien, BoxLayout.X_AXIS));
		JLabel lblMaNhanVien = new JLabel("Mã nhân viên:");
		lblMaNhanVien.setPreferredSize(kichThuocNhan);
		lblMaNhanVien.setMinimumSize(kichThuocNhan);
		lblMaNhanVien.setMaximumSize(kichThuocNhan);
		txtMaNhanVien = new JTextField();
		txtMaNhanVien.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		txtMaNhanVien.setEditable(false);
		txtMaNhanVien.setBackground(mauChiDoc);
		txtMaNhanVien.setForeground(chuChiDoc);
		pnlMaNhanVien.add(lblMaNhanVien);
		pnlMaNhanVien.add(txtMaNhanVien);
		pnlCol1.add(pnlMaNhanVien);
		pnlCol1.add(Box.createVerticalStrut(10));

		// so dien thoai
		JPanel pnlSoDienThoai = new JPanel();
		pnlSoDienThoai.setLayout(new BoxLayout(pnlSoDienThoai, BoxLayout.X_AXIS));
		JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
		lblSoDienThoai.setPreferredSize(kichThuocNhan);
		lblSoDienThoai.setMinimumSize(kichThuocNhan);
		lblSoDienThoai.setMaximumSize(kichThuocNhan);
		txtSoDienThoai = new JTextField();
		txtSoDienThoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		btnTimKhachHang = new JButton("Tìm");
		pnlSoDienThoai.add(lblSoDienThoai);
		pnlSoDienThoai.add(txtSoDienThoai);
		pnlSoDienThoai.add(Box.createHorizontalStrut(5));
		pnlSoDienThoai.add(btnTimKhachHang);
		pnlCol1.add(pnlSoDienThoai);
		pnlCol1.add(Box.createVerticalStrut(10));

		// ten khach hang
		JPanel pnlTenKhachHang = new JPanel();
		pnlTenKhachHang.setLayout(new BoxLayout(pnlTenKhachHang, BoxLayout.X_AXIS));
		JLabel lblTenKhachHang = new JLabel("Tên khách hàng:");
		lblTenKhachHang.setPreferredSize(kichThuocNhan);
		lblTenKhachHang.setMinimumSize(kichThuocNhan);
		lblTenKhachHang.setMaximumSize(kichThuocNhan);
		txtTenKhachHang = new JTextField();
		txtTenKhachHang.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		txtTenKhachHang.setEditable(false);
		txtTenKhachHang.setBackground(mauChiDoc);
		txtTenKhachHang.setForeground(chuChiDoc);
		pnlTenKhachHang.add(lblTenKhachHang);
		pnlTenKhachHang.add(txtTenKhachHang);
		pnlCol1.add(pnlTenKhachHang);
		pnlCol1.add(Box.createVerticalStrut(10));

		// ten thu cung
		JPanel pnlTenThuCung = new JPanel();
		pnlTenThuCung.setLayout(new BoxLayout(pnlTenThuCung, BoxLayout.X_AXIS));
		JLabel lblTenThuCung = new JLabel("Tên thú cưng:");
		lblTenThuCung.setPreferredSize(kichThuocNhan);
		lblTenThuCung.setMinimumSize(kichThuocNhan);
		lblTenThuCung.setMaximumSize(kichThuocNhan);
		cbTenThuCung = new JComboBox<ThuCung>();
		cbTenThuCung.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		cbTenThuCung.setEnabled(false);
		cbTenThuCung.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof ThuCung) {
					setText(((ThuCung) value).getTenThuCung());
				}
				return this;
			}
		});
		pnlTenThuCung.add(lblTenThuCung);
		pnlTenThuCung.add(cbTenThuCung);
		pnlCol1.add(pnlTenThuCung);
		pnlCol1.add(Box.createVerticalStrut(10));

		// dich vu
		JPanel pnlDichVu = new JPanel();
		pnlDichVu.setLayout(new BoxLayout(pnlDichVu, BoxLayout.X_AXIS));
		JLabel lblDichVu = new JLabel("Mã dịch vụ:");
		lblDichVu.setPreferredSize(kichThuocNhan);
		lblDichVu.setMinimumSize(kichThuocNhan);
		lblDichVu.setMaximumSize(kichThuocNhan);
		cbDichVu = new JComboBox<DichVu>();
		cbDichVu.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		cbDichVu.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof DichVu) {
					DichVu dichVu = (DichVu) value;
					setText(dichVu.getMaDV() + " - " + dichVu.getTenDV() + " ("
							+ DinhDang.TIEN.format(dichVu.getGiaTieuChuan()) + ")");
				}
				return this;
			}
		});
		pnlDichVu.add(lblDichVu);
		pnlDichVu.add(cbDichVu);
		pnlCol1.add(pnlDichVu);

		// col 2
		JPanel pnlCol2 = new JPanel();
		pnlCol2.setLayout(new BoxLayout(pnlCol2, BoxLayout.Y_AXIS));

		// loai
		JPanel pnlLoai = new JPanel();
		pnlLoai.setLayout(new BoxLayout(pnlLoai, BoxLayout.X_AXIS));
		JLabel lblLoai = new JLabel("Loài:");
		lblLoai.setPreferredSize(kichThuocNhan);
		lblLoai.setMinimumSize(kichThuocNhan);
		lblLoai.setMaximumSize(kichThuocNhan);
		cbLoai = new JComboBox<Loai>(Loai.values());
		cbLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		pnlLoai.add(lblLoai);
		pnlLoai.add(cbLoai);
		pnlCol2.add(pnlLoai);
		pnlCol2.add(Box.createVerticalStrut(10));

		// can nang
		JPanel pnlCanNang = new JPanel();
		pnlCanNang.setLayout(new BoxLayout(pnlCanNang, BoxLayout.X_AXIS));
		JLabel lblCanNang = new JLabel("Cân nặng (kg):");
		lblCanNang.setPreferredSize(kichThuocNhan);
		lblCanNang.setMinimumSize(kichThuocNhan);
		lblCanNang.setMaximumSize(kichThuocNhan);
		txtCanNang = new JTextField();
		txtCanNang.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		pnlCanNang.add(lblCanNang);
		pnlCanNang.add(txtCanNang);
		pnlCol2.add(pnlCanNang);
		pnlCol2.add(Box.createVerticalStrut(10));

		// trang thai thu cung
		JPanel pnlTrangThai = new JPanel();
		pnlTrangThai.setLayout(new BoxLayout(pnlTrangThai, BoxLayout.X_AXIS));
		JLabel lblTrangThai = new JLabel("Trạng thái:");
		lblTrangThai.setPreferredSize(kichThuocNhan);
		lblTrangThai.setMinimumSize(kichThuocNhan);
		lblTrangThai.setMaximumSize(kichThuocNhan);
		txtTrangThaiThuCung = new JTextField();
		txtTrangThaiThuCung.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		txtTrangThaiThuCung.setEditable(false);
		txtTrangThaiThuCung.setBackground(mauChiDoc);
		txtTrangThaiThuCung.setForeground(chuChiDoc);
		pnlTrangThai.add(lblTrangThai);
		pnlTrangThai.add(txtTrangThaiThuCung);
		pnlCol2.add(pnlTrangThai);
		pnlCol2.add(Box.createVerticalStrut(10));

		// do hung du
		JPanel pnlDoHungDu = new JPanel();
		pnlDoHungDu.setLayout(new BoxLayout(pnlDoHungDu, BoxLayout.X_AXIS));
		JLabel lblDoHungDu = new JLabel("Độ hung dữ:");
		lblDoHungDu.setPreferredSize(kichThuocNhan);
		lblDoHungDu.setMinimumSize(kichThuocNhan);
		lblDoHungDu.setMaximumSize(kichThuocNhan);
		cbDoHungDu = new JComboBox<DoHungDu>(DoHungDu.values());
		cbDoHungDu.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		pnlDoHungDu.add(lblDoHungDu);
		pnlDoHungDu.add(cbDoHungDu);
		pnlCol2.add(pnlDoHungDu);
		pnlCol2.add(Box.createVerticalStrut(10));

		// ngay hen
		JPanel pnlNgayHen = new JPanel();
		pnlNgayHen.setLayout(new BoxLayout(pnlNgayHen, BoxLayout.X_AXIS));
		JLabel lblNgayHen = new JLabel("Ngày hẹn:");
		lblNgayHen.setPreferredSize(kichThuocNhan);
		lblNgayHen.setMinimumSize(kichThuocNhan);
		lblNgayHen.setMaximumSize(kichThuocNhan);
		txtNgayHen = new JTextField();
		txtNgayHen.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		JLabel lblDinhDangNgay = new JLabel("dd/MM/yyyy");
		lblDinhDangNgay.setForeground(Color.GRAY);
		pnlNgayHen.add(lblNgayHen);
		pnlNgayHen.add(txtNgayHen);
		pnlNgayHen.add(Box.createHorizontalStrut(5));
		pnlNgayHen.add(lblDinhDangNgay);
		pnlCol2.add(pnlNgayHen);
		pnlCol2.add(Box.createVerticalStrut(10));

		// ghi chu
		JPanel pnlGhiChu = new JPanel();
		pnlGhiChu.setLayout(new BoxLayout(pnlGhiChu, BoxLayout.X_AXIS));
		JLabel lblGhiChu = new JLabel("Ghi chú:");
		lblGhiChu.setPreferredSize(kichThuocNhan);
		lblGhiChu.setMinimumSize(kichThuocNhan);
		lblGhiChu.setMaximumSize(kichThuocNhan);
		txtGhiChu = new JTextField();
		txtGhiChu.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		pnlGhiChu.add(lblGhiChu);
		pnlGhiChu.add(txtGhiChu);
		pnlCol2.add(pnlGhiChu);

		pnlCol1.setAlignmentY(Component.TOP_ALIGNMENT);
		pnlCol2.setAlignmentY(Component.TOP_ALIGNMENT);
		pnlFormFields.add(pnlCol1);
		pnlFormFields.add(Box.createHorizontalStrut(20)); // khoang cach giua col1 col2
		pnlFormFields.add(pnlCol2);
		pnlFormWrapper.add(pnlFormFields, BorderLayout.CENTER);

		// cac nut them / xoa dong
		JPanel pnlFormButtons = new JPanel();
		pnlFormButtons.setLayout(new BoxLayout(pnlFormButtons, BoxLayout.X_AXIS));
		pnlFormButtons.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		pnlFormButtons.add(Box.createHorizontalGlue());

		int btnWidth = 90;
		int btnHeight = 25;
		btnThem = new JButton("Thêm");
		btnThem.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlFormButtons.add(btnThem);
		pnlFormButtons.add(Box.createHorizontalStrut(15));

		btnXoa = new JButton("Xóa dòng");
		btnXoa.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlFormButtons.add(btnXoa);
		pnlFormButtons.add(Box.createHorizontalGlue());
		pnlFormWrapper.add(pnlFormButtons, BorderLayout.SOUTH);

		pnlTopHalf.add(pnlFormWrapper);
		add(pnlTopHalf, BorderLayout.NORTH);

		// table
		JPanel pnlTable = new JPanel();
		pnlTable.setLayout(new BorderLayout());
		pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		String col[] = { "Thú cưng", "Loài", "Cân nặng (kg)", "Độ hung dữ", "Dịch vụ", "Giá tiêu chuẩn", "Phụ phí",
				"Giá dự kiến" };
		model = new DefaultTableModel(col, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(24);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(860, 150));
		pnlTable.add(pane);
		add(pnlTable, BorderLayout.CENTER);

		// nua duoi: tong tien + nut xac nhan / huy
		JPanel pnlBottom = new JPanel(new BorderLayout());
		pnlBottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		lblTongTien = new JLabel("Tổng giá dự kiến:");
		lblGiaTriTongTien = new JLabel("0 đ");
		lblGiaTriTongTien.setForeground(new Color(238, 77, 45));
		pnlTongTien.add(lblTongTien);
		pnlTongTien.add(lblGiaTriTongTien);
		lblGiaTriTongTien.setFont(lblGiaTriTongTien.getFont().deriveFont(Font.BOLD));
		pnlBottom.add(pnlTongTien, BorderLayout.WEST);

		JPanel pnlBottomButtons = new JPanel();
		pnlBottomButtons.setLayout(new BoxLayout(pnlBottomButtons, BoxLayout.X_AXIS));
		btnHuy = new JButton("Hủy");
		btnXacNhan = new JButton("Xác nhận tạo lịch hẹn");
		pnlBottomButtons.add(btnHuy);
		pnlBottomButtons.add(Box.createHorizontalStrut(10));
		pnlBottomButtons.add(btnXacNhan);
		pnlBottom.add(pnlBottomButtons, BorderLayout.EAST);
		add(pnlBottom, BorderLayout.SOUTH);

		// du lieu ban dau
		try {
			maLichHen = lichHenDAO.phatSinhMaLichHen();
			for (DichVu dichVu : dichVuDAO.layTatCa()) {
				cbDichVu.addItem(dichVu);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
		txtMaLichHen.setText(maLichHen);
		txtMaNhanVien.setText(nhanVienDangNhap.getMaNV());

		btnTimKhachHang.addActionListener(this);
		txtSoDienThoai.addActionListener(this);
		cbTenThuCung.addActionListener(this);
		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnXacNhan.addActionListener(this);
		btnHuy.addActionListener(this);

		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo(owner);
	}

	public boolean isDaTao() {
		return daTao;
	}

	// ham doc ngay hen chi kiem tra dinh dang, quy tac nghiep vu do DAO xu ly
	private LocalDate docNgayHen() {
		try {
			return LocalDate.parse(txtNgayHen.getText().trim(), DinhDang.NGAY);
		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Ngày hẹn không hợp lệ. Định dạng: dd/MM/yyyy");
			return null;
		}
	}

	// tinh lai gia cho ca danh sach, phu phi chi tinh mot lan cho moi thu cung roi do lai bang
	private void napLaiBang() {
		lichHenDAO.tinhGiaChiTiet(dsChiTietTam);
		model.setRowCount(0);
		for (ChiTietLichHen chiTiet : dsChiTietTam) {
			ThuCung thuCung = chiTiet.getThuCung();
			model.addRow(new Object[] { thuCung.getTenThuCung(), thuCung.getLoai(), thuCung.getCanNang(),
					thuCung.getDoHungDu(), chiTiet.getDichVu().getTenDV(),
					DinhDang.TIEN.format(chiTiet.getDichVu().getGiaTieuChuan()),
					DinhDang.TIEN.format(chiTiet.tinhPhuPhi()), DinhDang.TIEN.format(chiTiet.getDonGia()) });
		}
		capNhatTongTien();
	}

	private void capNhatTongTien() {
		double tongTien = lichHenDAO.tinhTongTien(dsChiTietTam);
		lblGiaTriTongTien.setText(DinhDang.TIEN.format(tongTien));
		lblGiaTriTongTien.setFont(lblGiaTriTongTien.getFont().deriveFont(Font.BOLD));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTimKhachHang) || o.equals(txtSoDienThoai)) {
			String soDienThoai = txtSoDienThoai.getText().trim();
			if (soDienThoai.length() != 10) {
				JOptionPane.showMessageDialog(this, "Số điện thoại phải đủ 10 ký tự");
				txtSoDienThoai.requestFocus();
				return;
			}
			try {
				khachHangHienTai = khachHangDAO.timTheoSoDienThoai(soDienThoai);
				cbTenThuCung.removeAllItems();
				if (khachHangHienTai == null) {
					txtTenKhachHang.setText("");
					cbTenThuCung.setEnabled(false);
					JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có số điện thoại này");
					return;
				}
				txtTenKhachHang.setText(khachHangHienTai.getTenKH());
				ArrayList<ThuCung> dsThuCung = new ArrayList<ThuCung>(
						thuCungDAO.timTheoChuSoHuu(khachHangHienTai));
				for (ThuCung thuCung : dsThuCung) {
					cbTenThuCung.addItem(thuCung);
				}
				cbTenThuCung.setEnabled(!dsThuCung.isEmpty());
				if (dsThuCung.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Khách hàng chưa đăng ký thú cưng nào");
				}
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (o.equals(cbTenThuCung)) {
			ThuCung thuCung = (ThuCung) cbTenThuCung.getSelectedItem();
			if (thuCung == null) {
				txtCanNang.setText("");
				txtTrangThaiThuCung.setText("");
				return;
			}
			cbLoai.setSelectedItem(thuCung.getLoai());
			txtCanNang.setText(String.valueOf(thuCung.getCanNang()));
			txtTrangThaiThuCung.setText(String.valueOf(thuCung.getTrangThaiThuCung()));
			cbDoHungDu.setSelectedItem(thuCung.getDoHungDu());
		} else if (o.equals(btnThem)) {
			ThuCung thuCung = (ThuCung) cbTenThuCung.getSelectedItem();
			DichVu dichVu = (DichVu) cbDichVu.getSelectedItem();
			if (thuCung == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng tìm khách hàng và chọn thú cưng");
				return;
			}
			if (dichVu == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ");
				return;
			}
			double canNang;
			try {
				canNang = Double.parseDouble(txtCanNang.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Cân nặng phải là số");
				txtCanNang.requestFocus();
				txtCanNang.selectAll();
				return;
			}
			LocalDate ngayHen = docNgayHen();
			if (ngayHen == null) {
				txtNgayHen.requestFocus();
				return;
			}
			for (ChiTietLichHen chiTiet : dsChiTietTam) {
				boolean cungThuCung = chiTiet.getThuCung().equals(thuCung);
				boolean cungDichVu = chiTiet.getDichVu().getMaDV().equals(dichVu.getMaDV());
				if (cungThuCung && cungDichVu) {
					JOptionPane.showMessageDialog(this, "Thú cưng này đã chọn dịch vụ này rồi");
					return;
				}
			}

			// gan thong tin nhan vien vua nhap, du lieu chi duoc luu khi xac nhan tao lich hen
			thuCung.setLoai((Loai) cbLoai.getSelectedItem());
			thuCung.setCanNang(canNang);
			thuCung.setDoHungDu((DoHungDu) cbDoHungDu.getSelectedItem());

			try {
				lichHenDAO.kiemTraDatLich(thuCung, dichVu, ngayHen);
			} catch (NghiepVuException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			dsChiTietTam.add(new ChiTietLichHen(null, thuCung, dichVu, 0));
			napLaiBang();
		} else if (o.equals(btnXoa)) {
			int row = table.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa");
				return;
			}
			dsChiTietTam.remove(row);
			napLaiBang();
		} else if (o.equals(btnXacNhan)) {
			if (dsChiTietTam.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một thú cưng và dịch vụ");
				return;
			}
			LocalDate ngayHen = docNgayHen();
			if (ngayHen == null) {
				txtNgayHen.requestFocus();
				return;
			}
			LichHen lichHen = new LichHen(maLichHen, ngayHen, txtGhiChu.getText().trim(), TrangThai.CHO_DEN_HEN);
			try {
				lichHenDAO.taoLichHen(lichHen, dsChiTietTam);
			} catch (NghiepVuException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			daTao = true;
			JOptionPane.showMessageDialog(this, "Tạo lịch hẹn thành công! Mã lịch hẹn: " + maLichHen);
			dispose();
		} else if (o.equals(btnHuy)) {
			dispose();
		}
	}
}
