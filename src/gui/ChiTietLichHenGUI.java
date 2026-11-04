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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietLichHenDAO;
import dao.LichHenDAO;
import entity.ChiTietLichHen;
import entity.LichHen;
import entity.ThuCung;

public class ChiTietLichHenGUI extends JDialog implements ActionListener {

	private JButton btnDong;
	private DefaultTableModel model;
	private JTable table;
	private ChiTietLichHenDAO chiTietLichHenDAO;
	private LichHenDAO lichHenDAO;

	public ChiTietLichHenGUI(Window owner, LichHen lichHen) {
		super(owner, "Chi tiết lịch hẹn", ModalityType.APPLICATION_MODAL);
		chiTietLichHenDAO = new ChiTietLichHenDAO();
		lichHenDAO = new LichHenDAO();

		// lay du lieu danh sach chi tiet va tong tien do DAO xu ly
		ArrayList<ChiTietLichHen> dsChiTiet = new ArrayList<ChiTietLichHen>();
		double tongTien = 0;
		try {
			dsChiTiet.addAll(chiTietLichHenDAO.timTheoLichHen(lichHen));
			tongTien = lichHenDAO.tinhTongTien(lichHen.getMaLH());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(owner, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}

		String tenKhachHang = "";
		String soDienThoai = "";
		if (!dsChiTiet.isEmpty()) {
			tenKhachHang = dsChiTiet.get(0).getThuCung().getChuSoHuu().getTenKH();
			soDienThoai = dsChiTiet.get(0).getThuCung().getChuSoHuu().getSoDienThoai();
		}

		setLayout(new BorderLayout());

		// thong tin chung
		JPanel pnlThongTin = new JPanel();
		pnlThongTin.setLayout(new BoxLayout(pnlThongTin, BoxLayout.Y_AXIS));
		pnlThongTin.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

		int labelWidth = 110;
		int labelHeight = 25;
		Dimension kichThuocNhan = new Dimension(labelWidth, labelHeight);

		// hai cot
		JPanel pnlHaiCot = new JPanel();
		pnlHaiCot.setLayout(new BoxLayout(pnlHaiCot, BoxLayout.X_AXIS));

		// col 1
		JPanel pnlCol1 = new JPanel();
		pnlCol1.setLayout(new BoxLayout(pnlCol1, BoxLayout.Y_AXIS));

		// ma lich hen
		JPanel pnlMaLichHen = new JPanel();
		pnlMaLichHen.setLayout(new BoxLayout(pnlMaLichHen, BoxLayout.X_AXIS));
		JLabel lblMaLichHen = new JLabel("Mã lịch hẹn:");
		lblMaLichHen.setFont(lblMaLichHen.getFont().deriveFont(Font.BOLD));
		lblMaLichHen.setPreferredSize(kichThuocNhan);
		lblMaLichHen.setMinimumSize(kichThuocNhan);
		lblMaLichHen.setMaximumSize(kichThuocNhan);
		pnlMaLichHen.add(lblMaLichHen);
		pnlMaLichHen.add(new JLabel(lichHen.getMaLH()));
		pnlMaLichHen.add(Box.createHorizontalGlue());
		pnlCol1.add(pnlMaLichHen);
		pnlCol1.add(Box.createVerticalStrut(10));

		// ngay hen
		JPanel pnlNgayHen = new JPanel();
		pnlNgayHen.setLayout(new BoxLayout(pnlNgayHen, BoxLayout.X_AXIS));
		JLabel lblNgayHen = new JLabel("Ngày hẹn:");
		lblNgayHen.setFont(lblNgayHen.getFont().deriveFont(Font.BOLD));
		lblNgayHen.setPreferredSize(kichThuocNhan);
		lblNgayHen.setMinimumSize(kichThuocNhan);
		lblNgayHen.setMaximumSize(kichThuocNhan);
		pnlNgayHen.add(lblNgayHen);
		pnlNgayHen.add(new JLabel(lichHen.getThoiGianHen().format(DinhDang.NGAY)));
		pnlNgayHen.add(Box.createHorizontalGlue());
		pnlCol1.add(pnlNgayHen);
		pnlCol1.add(Box.createVerticalStrut(10));

		// trang thai
		JPanel pnlTrangThai = new JPanel();
		pnlTrangThai.setLayout(new BoxLayout(pnlTrangThai, BoxLayout.X_AXIS));
		JLabel lblTrangThai = new JLabel("Trạng thái:");
		lblTrangThai.setFont(lblTrangThai.getFont().deriveFont(Font.BOLD));
		lblTrangThai.setPreferredSize(kichThuocNhan);
		lblTrangThai.setMinimumSize(kichThuocNhan);
		lblTrangThai.setMaximumSize(kichThuocNhan);
		pnlTrangThai.add(lblTrangThai);
		pnlTrangThai.add(new JLabel(String.valueOf(lichHen.getTrangThaiHen())));
		pnlTrangThai.add(Box.createHorizontalGlue());
		pnlCol1.add(pnlTrangThai);

		// col 2
		JPanel pnlCol2 = new JPanel();
		pnlCol2.setLayout(new BoxLayout(pnlCol2, BoxLayout.Y_AXIS));

		// khach hang
		JPanel pnlKhachHang = new JPanel();
		pnlKhachHang.setLayout(new BoxLayout(pnlKhachHang, BoxLayout.X_AXIS));
		JLabel lblKhachHang = new JLabel("Khách hàng:");
		lblKhachHang.setFont(lblKhachHang.getFont().deriveFont(Font.BOLD));
		lblKhachHang.setPreferredSize(kichThuocNhan);
		lblKhachHang.setMinimumSize(kichThuocNhan);
		lblKhachHang.setMaximumSize(kichThuocNhan);
		pnlKhachHang.add(lblKhachHang);
		pnlKhachHang.add(new JLabel(tenKhachHang));
		pnlKhachHang.add(Box.createHorizontalGlue());
		pnlCol2.add(pnlKhachHang);
		pnlCol2.add(Box.createVerticalStrut(10));

		// so dien thoai
		JPanel pnlSoDienThoai = new JPanel();
		pnlSoDienThoai.setLayout(new BoxLayout(pnlSoDienThoai, BoxLayout.X_AXIS));
		JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
		lblSoDienThoai.setFont(lblSoDienThoai.getFont().deriveFont(Font.BOLD));
		lblSoDienThoai.setPreferredSize(kichThuocNhan);
		lblSoDienThoai.setMinimumSize(kichThuocNhan);
		lblSoDienThoai.setMaximumSize(kichThuocNhan);
		pnlSoDienThoai.add(lblSoDienThoai);
		pnlSoDienThoai.add(new JLabel(soDienThoai));
		pnlSoDienThoai.add(Box.createHorizontalGlue());
		pnlCol2.add(pnlSoDienThoai);

		pnlCol1.setAlignmentY(Component.TOP_ALIGNMENT);
		pnlCol2.setAlignmentY(Component.TOP_ALIGNMENT);
		pnlHaiCot.add(pnlCol1);
		pnlHaiCot.add(Box.createHorizontalStrut(30)); // khoang cach giua col1 col2
		pnlHaiCot.add(pnlCol2);
		pnlThongTin.add(pnlHaiCot);
		pnlThongTin.add(Box.createVerticalStrut(10));

		// ghi chu chiem tron hang, tu xuong dong khi dai
		JPanel pnlGhiChu = new JPanel();
		pnlGhiChu.setLayout(new BoxLayout(pnlGhiChu, BoxLayout.X_AXIS));
		JLabel lblGhiChu = new JLabel("Ghi chú:");
		lblGhiChu.setFont(lblGhiChu.getFont().deriveFont(Font.BOLD));
		lblGhiChu.setPreferredSize(kichThuocNhan);
		lblGhiChu.setMinimumSize(kichThuocNhan);
		lblGhiChu.setMaximumSize(kichThuocNhan);
		lblGhiChu.setAlignmentY(Component.TOP_ALIGNMENT);

		String ghiChu = lichHen.getGhiChu();
		JTextArea txtGhiChu = new JTextArea(ghiChu == null || ghiChu.isEmpty() ? "(không có)" : ghiChu, 3, 20);
		txtGhiChu.setEditable(false);
		txtGhiChu.setFocusable(false);
		txtGhiChu.setOpaque(false);
		txtGhiChu.setLineWrap(true);
		txtGhiChu.setWrapStyleWord(true);
		txtGhiChu.setBorder(null);
		txtGhiChu.setFont(lblGhiChu.getFont().deriveFont(Font.PLAIN));
		txtGhiChu.setAlignmentY(Component.TOP_ALIGNMENT);
		pnlGhiChu.add(lblGhiChu);
		pnlGhiChu.add(txtGhiChu);
		pnlThongTin.add(pnlGhiChu);
		add(pnlThongTin, BorderLayout.NORTH);

		// table
		JPanel pnlTable = new JPanel(new BorderLayout());
		pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		String col[] = { "Thú cưng", "Loài", "Cân nặng (kg)", "Độ hung dữ", "Dịch vụ", "Giá tiêu chuẩn", "Phụ phí",
				"Giá dự kiến" };
		model = new DefaultTableModel(col, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(26);

		for (ChiTietLichHen chiTiet : dsChiTiet) {
			ThuCung thuCung = chiTiet.getThuCung();
			model.addRow(new Object[] { thuCung.getTenThuCung(), thuCung.getLoai(), thuCung.getCanNang(),
					thuCung.getDoHungDu(), chiTiet.getDichVu().getTenDV(),
					DinhDang.TIEN.format(chiTiet.getDichVu().getGiaTieuChuan()),
					DinhDang.TIEN.format(chiTiet.tinhPhuPhi()), DinhDang.TIEN.format(chiTiet.getDonGia()) });
		}
		pnlTable.add(new JScrollPane(table), BorderLayout.CENTER);
		add(pnlTable, BorderLayout.CENTER);

		// tong tien va nut dong
		JPanel pnlBottom = new JPanel(new BorderLayout());
		pnlBottom.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

		JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		JLabel lblTongTien = new JLabel("Tổng giá dự kiến:");
		lblTongTien.setFont(lblTongTien.getFont().deriveFont(Font.BOLD, 16f));
		JLabel lblGiaTriTongTien = new JLabel(DinhDang.TIEN.format(tongTien));
		lblGiaTriTongTien.setFont(lblGiaTriTongTien.getFont().deriveFont(Font.BOLD, 20f));
		lblGiaTriTongTien.setForeground(new Color(238, 77, 45));
		pnlTongTien.add(lblTongTien);
		pnlTongTien.add(lblGiaTriTongTien);
		pnlBottom.add(pnlTongTien, BorderLayout.WEST);

		btnDong = new JButton("Đóng");
		pnlBottom.add(btnDong, BorderLayout.EAST);
		add(pnlBottom, BorderLayout.SOUTH);

		btnDong.addActionListener(this);

		setSize(900, 470);
		setLocationRelativeTo(owner);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnDong)) {
			dispose();
		}
	}
}
