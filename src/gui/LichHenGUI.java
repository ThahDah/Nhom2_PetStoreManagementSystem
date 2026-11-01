package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietLichHenDAO;
import dao.LichHenDAO;
import entity.ChiTietLichHen;
import entity.LichHen;
import entity.NhanVien;

public class LichHenGUI extends JPanel implements ActionListener, MouseListener {

	private JButton btnTaoLichHen;
	private DefaultTableModel model;
	private JTable table;
	private LichHenDAO lichHenDAO;
	private ChiTietLichHenDAO chiTietLichHenDAO;
	private NhanVien nhanVienDangNhap;
	private ArrayList<LichHen> dsLichHen;

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
		lblTitle.setForeground(Color.BLUE);
		pnlHeader.add(lblTitle, BorderLayout.CENTER);

		// nut tao lich hen
		JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnTaoLichHen = new JButton("Tạo lịch hẹn");
		pnlHeaderRight.add(btnTaoLichHen);
		pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

		JPanel pnlHeaderLeft = new JPanel();
		pnlHeaderLeft.setPreferredSize(btnTaoLichHen.getPreferredSize());
		pnlHeader.add(pnlHeaderLeft, BorderLayout.WEST);

		pnlTopHalf.add(pnlHeader);
		add(pnlTopHalf, BorderLayout.NORTH);

		// table
		JPanel pnlTable = new JPanel();
		pnlTable.setLayout(new BorderLayout());
		pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		String col[] = { "Mã lịch hẹn", "Khách hàng", "Thú cưng", "Dịch vụ", "Ngày hẹn", "Trạng thái" };
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

		docDuLieuVaoTable();

		table.addMouseListener(this);
		btnTaoLichHen.addActionListener(this);
	}

	// ham doc du lieu vao bang
	public void docDuLieuVaoTable() {
		model.setRowCount(0);
		dsLichHen.clear();
		try {
			dsLichHen.addAll(lichHenDAO.layTatCa());
			for (LichHen lichHen : dsLichHen) {
				ArrayList<ChiTietLichHen> dsChiTiet = new ArrayList<ChiTietLichHen>(
						chiTietLichHenDAO.timTheoLichHen(lichHen));
//				StringBuilder tenThuCung = new StringBuilder();
//				StringBuilder tenDichVu = new StringBuilder();
//				for (ChiTietLichHen chiTiet : dsChiTiet) {
//					if (tenThuCung.length() > 0) {
//						tenThuCung.append(", ");
//						tenDichVu.append(", ");
//					}
//					tenThuCung.append(chiTiet.getThuCung().getTenThuCung());
//					tenDichVu.append(chiTiet.getDichVu().getTenDV());
//				}
				// moi ten thu cung / dich vu chi hien mot lan
				LinkedHashSet<String> dsTenThuCung = new LinkedHashSet<String>();
				LinkedHashSet<String> dsTenDichVu = new LinkedHashSet<String>();
				for (ChiTietLichHen chiTiet : dsChiTiet) {
					dsTenThuCung.add(chiTiet.getThuCung().getTenThuCung());
					dsTenDichVu.add(chiTiet.getDichVu().getTenDV());
				}
				
				String tenKhachHang = dsChiTiet.isEmpty() ? ""
						: dsChiTiet.get(0).getThuCung().getChuSoHuu().getTenKH();
//				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, tenThuCung.toString(),
//						tenDichVu.toString(), lichHen.getThoiGianHen().format(DinhDang.NGAY),
//						lichHen.getTrangThaiHen() });
				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, String.join(", ", dsTenThuCung),
						String.join(", ", dsTenDichVu), lichHen.getThoiGianHen().format(DinhDang.NGAY),
						lichHen.getTrangThaiHen() });
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTaoLichHen)) {
			TaoLichHenGUI taoLichHenGUI = new TaoLichHenGUI(SwingUtilities.getWindowAncestor(this),
					nhanVienDangNhap);
			taoLichHenGUI.setVisible(true);
			if (taoLichHenGUI.isDaTao()) {
				docDuLieuVaoTable();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// double click moi mo chi tiet
		if (e.getClickCount() == 2) {
			int row = table.rowAtPoint(e.getPoint());
			if (row >= 0 && row < dsLichHen.size()) {
				new ChiTietLichHenGUI(SwingUtilities.getWindowAncestor(this), dsLichHen.get(row)).setVisible(true);
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
