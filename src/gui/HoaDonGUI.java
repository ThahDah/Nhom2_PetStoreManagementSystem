package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;
import dao.HoaDonDAO;
import entity.TrangThai;
public class HoaDonGUI extends JPanel implements ActionListener, MouseListener {
	private Color MAU_XANH = new Color(103, 176, 157);
	private Color MAU_NEN = new Color(239, 248, 239);
	private Color MAU_CHU = new Color(45, 55, 45);
	private HoaDonDAO hoaDonDAO = new HoaDonDAO();

	// dinh dang ngay va tien 
	private  SimpleDateFormat dinhDangNgay = new SimpleDateFormat("dd/MM/yyyy");
	private  SimpleDateFormat dinhDangNgayNut = new SimpleDateFormat("dd/MM");
	private  DecimalFormat dinhDangTien = new DecimalFormat("#,##0");
	// tao model cho bang hien thi hoa don
	private  DefaultTableModel model = new DefaultTableModel(
			new Object[] { "Mã hóa đơn", "Khách hàng", "Số điện thoại", "Ngày lập", "Tổng tiền", "Trạng thái" },
			0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private  JTextField txtTimKiem = new JTextField();
	private  JButton btnTatCa = taoNut("Tất cả");
	private  JButton btnChuaThanhToan = taoNut("Chưa thanh toán");
	private  JButton btnDaThanhToan = taoNut("Đã thanh toán");
	private  JButton btnKhoangNgay = taoNut("Chọn khoảng ngày");
	private  JTable table = new JTable(model);
	private  JDateChooser dateFrom = new JDateChooser();
	private  JDateChooser dateTo = new JDateChooser();
	private JButton btnApDungNgay;
	private JButton btnHuyNgay;
	private JDialog dialogChonNgay;
	private Date tuNgay;
	private Date denNgay;

	public HoaDonGUI() {
		setLayout(new BorderLayout(0, 12));
		setBackground(MAU_NEN);
		setBorder(BorderFactory.createEmptyBorder(18, 48, 10, 34));
		add(taoPhanDau(), BorderLayout.NORTH);
		add(taoBangHoaDon(), BorderLayout.CENTER);
		dateFrom.setDateFormatString("dd/MM/yyyy");
		dateTo.setDateFormatString("dd/MM/yyyy");
		dangKySuKien();
		chonNutTrangThai(btnTatCa);
		taiDuLieu();
	}

	// panel dau chua tieu de va cac nut tim kiem, loc theo trang thai va khoang ngay
	private JPanel taoPhanDau() {
		// panel chua tieu de
		JPanel pnlHeader = new JPanel(new BorderLayout(0, 18));
		pnlHeader.setOpaque(false);
		JLabel lblTieuDe = new JLabel("Hóa đơn");
		lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
		lblTieuDe.setForeground(MAU_CHU);
		JLabel lblMoTa = new JLabel("Quản lý và tra cứu thông tin hóa đơn");
		lblMoTa.setFont(new Font("Arial", Font.PLAIN, 12));
		lblMoTa.setForeground(new Color(115, 125, 115));
		JPanel pnlTieuDe = new JPanel(new GridLayout(2, 1, 0, 4));
		pnlTieuDe.setOpaque(false);
		pnlTieuDe.add(lblTieuDe);
		pnlTieuDe.add(lblMoTa);
		pnlHeader.add(pnlTieuDe, BorderLayout.NORTH);
		// panel chua o tim kiem
		JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnlTimKiem.setOpaque(false);
		txtTimKiem.setPreferredSize(new Dimension(500, 36));
		pnlTimKiem.add(txtTimKiem);
		pnlHeader.add(pnlTimKiem, BorderLayout.CENTER);
		// panel chua cac nut loc theo trang thai va khoang ngay
		JPanel pnlBoLoc = new JPanel(new BorderLayout(8, 0));
		pnlBoLoc.setOpaque(false);
		JPanel pnlTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		pnlTrangThai.setOpaque(false);
		pnlTrangThai.add(btnTatCa);
		pnlTrangThai.add(btnChuaThanhToan);
		pnlTrangThai.add(btnDaThanhToan);
		pnlBoLoc.add(pnlTrangThai, BorderLayout.WEST);
		btnKhoangNgay.setPreferredSize(new Dimension(144, 34));
		pnlBoLoc.add(btnKhoangNgay, BorderLayout.EAST);
		pnlHeader.add(pnlBoLoc, BorderLayout.SOUTH);
		return pnlHeader;
	}
	//tao bang hien thi danh sach hoa don
	private JPanel taoBangHoaDon() {
		table.setRowHeight(42);
		table.setFont(new Font("Arial", Font.PLAIN, 11));
		table.setShowGrid(false);
		table.addMouseListener(this);
		table.setFocusable(false);
		table.setSelectionBackground(MAU_XANH);
		table.setSelectionForeground(Color.WHITE);
		table.setBackground(Color.WHITE);
		table.setIntercellSpacing(new Dimension(0, 6));
		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(0, 36));
		header.setFont(new Font("Arial", Font.BOLD, 10));
		header.setReorderingAllowed(false);
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		headerRenderer.setBackground(new Color(216, 216, 216));
		headerRenderer.setForeground(Color.BLACK);
		headerRenderer.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 0));
		header.setDefaultRenderer(headerRenderer);
		JPanel pnlBangHoaDon = new JPanel(new BorderLayout());
		pnlBangHoaDon.setBackground(Color.WHITE);
		pnlBangHoaDon.setBorder(BorderFactory.createLineBorder(new Color(220, 228, 220)));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Color.WHITE);
		pnlBangHoaDon.add(scrollPane, BorderLayout.CENTER);
		return pnlBangHoaDon;
	}

	// ham tao nut dinh dang dung chung
	private JButton taoNut(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 10));
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setBackground(Color.WHITE);
		button.setForeground(MAU_CHU);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(220, 228, 220), 1, true),
				BorderFactory.createEmptyBorder(6, 12, 6, 12)));
		return button;
	}

	//dang ky su kien cho cac nut va o tim kiem
	private void dangKySuKien() {
		txtTimKiem.addActionListener(this);
		btnTatCa.addActionListener(this);
		btnChuaThanhToan.addActionListener(this);
		btnDaThanhToan.addActionListener(this);
		btnKhoangNgay.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	// ham doi mau nut khi duoc chon
	private void chonNutTrangThai(JButton nutDuocChon) {
		for (JButton button : new JButton[] { btnTatCa, btnChuaThanhToan, btnDaThanhToan }) {
			if (button == nutDuocChon) {
				button.setBackground(MAU_XANH);
				button.setForeground(Color.WHITE);
				button.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(MAU_XANH, 1, true),
						BorderFactory.createEmptyBorder(6, 12, 6, 12)));
			} else {
				button.setBackground(Color.WHITE);
				if (button == btnChuaThanhToan) {
					button.setForeground(new Color(190, 125, 10));
				} else if (button == btnDaThanhToan) {
					button.setForeground(MAU_XANH);
				} else {
					button.setForeground(MAU_CHU);
				}
				button.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(new Color(220, 228, 220), 1, true),
						BorderFactory.createEmptyBorder(6, 12, 6, 12)));
			}
		}
	}

	//load danh sach hoa don
	private void taiDuLieu() {
		try {
			List<Object[]> danhSachHoaDon = hoaDonDAO.getAllHoaDon();
			model.setRowCount(0);
			for (Object[] hoaDon : danhSachHoaDon) {
				model.addRow(new Object[] {
						hoaDon[0],
						hoaDon[1],
						hoaDon[2],
						hoaDon[3] == null ? "" : dinhDangNgay.format((Date) hoaDon[3]),
						dinhDangTien.format((Double) hoaDon[4]),
						((TrangThai) hoaDon[5]).getMoTa()
				});
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Không thể tải hóa đơn: " + ex.getMessage(),
					"Lỗi cơ sở dữ liệu", JOptionPane.ERROR_MESSAGE);
		}
	}

	//mo hop chon khoang ngay
	private void moChonKhoangNgay() {
		Window owner = SwingUtilities.getWindowAncestor(this);
		dialogChonNgay = new JDialog(owner, "Chọn khoảng ngày", Dialog.ModalityType.APPLICATION_MODAL);
		JPanel pnlNgay = new JPanel();
		pnlNgay.setLayout(new BoxLayout(pnlNgay, BoxLayout.Y_AXIS));
		pnlNgay.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(211, 226, 241)),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)));

		dateFrom.setDate(tuNgay);
		dateTo.setDate(denNgay);
		JLabel lblTuNgay = new JLabel("Từ ngày");
		pnlNgay.add(lblTuNgay);
		dateFrom.setPreferredSize(new Dimension(210, 28));
		dateFrom.setMaximumSize(new Dimension(210, 28));
		pnlNgay.add(dateFrom);
		pnlNgay.add(Box.createVerticalStrut(8));
		JLabel lblDenNgay = new JLabel("Đến ngày");
		pnlNgay.add(lblDenNgay);
		dateTo.setPreferredSize(new Dimension(210, 28));
		dateTo.setMaximumSize(new Dimension(210, 28));
		pnlNgay.add(dateTo);
		pnlNgay.add(Box.createVerticalStrut(10));

		JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		btnHuyNgay = taoNut("Hủy");
		btnApDungNgay = taoNut("Áp dụng");
		btnHuyNgay.addActionListener(this);
		btnApDungNgay.addActionListener(this);
		pnlNut.add(btnHuyNgay);
		pnlNut.add(btnApDungNgay);
		pnlNgay.add(pnlNut);

		dialogChonNgay.setContentPane(pnlNgay);
		dialogChonNgay.setResizable(false);
		dialogChonNgay.pack();
		dialogChonNgay.setLocationRelativeTo(owner);
		dialogChonNgay.setVisible(true);
	}

	//kiem tra va ap dung khoang ngay duoc chon, neu hop le thi cap nhat nut va tai lai danh sach hoa don
	private void apDungKhoangNgay() {
		java.util.Date ngayBatDau = dateFrom.getDate();
		java.util.Date ngayKetThuc = dateTo.getDate();
		if (ngayBatDau == null || ngayKetThuc == null) {
			JOptionPane.showMessageDialog(dialogChonNgay,
					"Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
			return;
		}
		if (ngayBatDau.after(ngayKetThuc)) {
			JOptionPane.showMessageDialog(dialogChonNgay,
					"Ngày bắt đầu không được sau ngày kết thúc.");
			return;
		}

		tuNgay = new Date(ngayBatDau.getTime());
		denNgay = new Date(ngayKetThuc.getTime());
		btnKhoangNgay.setText(dinhDangNgayNut.format(tuNgay) + " - " + dinhDangNgayNut.format(denNgay));
		dialogChonNgay.dispose();
		taiDuLieu();
	}

	//huy bo khoang ngay duoc chon, cap nhat nut va tai lai danh sach hoa don
	private void huyLocNgay() {
		tuNgay = null;
		denNgay = null;
		dateFrom.setDate(null);
		dateTo.setDate(null);
		btnKhoangNgay.setText("Chọn khoảng ngày");
		dialogChonNgay.dispose();
		taiDuLieu();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
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
