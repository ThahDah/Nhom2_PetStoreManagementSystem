package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import dao.DichVuDAO;
import dao.NghiepVuException;
import entity.DichVu;

public class DichVuGUI extends JPanel implements ActionListener, MouseListener {

	private JTextField txtMaDV;
	private JTextField txtTenDV;
	private JTextField txtGiaTieuChuan;
	private JButton btnTaoMa;
	private JButton btnLuu;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLamMoi;
	private JTextField txtTimMaDV;
	private JTextField txtTimTenDV;
	private JButton btnTim;
	private DefaultTableModel model;
	private JTable table;
	private DichVuDAO dichVuDAO;
	private ArrayList<DichVu> dsHienThi;

	public DichVuGUI() {
		dichVuDAO = new DichVuDAO();
		dsHienThi = new ArrayList<DichVu>();

		setLayout(new BorderLayout(10, 10));

		// nua tren
		JPanel pnlTopHalf = new JPanel();
		pnlTopHalf.setLayout(new BoxLayout(pnlTopHalf, BoxLayout.Y_AXIS));
		pnlTopHalf.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// header
		JPanel pnlHeader = new JPanel(new BorderLayout());
		JLabel lblTitle = new JLabel("Dịch vụ", JLabel.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
		lblTitle.setForeground(Color.BLUE);
		pnlHeader.add(lblTitle, BorderLayout.CENTER);
		pnlTopHalf.add(pnlHeader);
		pnlTopHalf.add(Box.createVerticalStrut(15));

		// thong tin
		JPanel pnlThongTin = new JPanel();
		pnlThongTin.setLayout(new BoxLayout(pnlThongTin, BoxLayout.Y_AXIS));
		pnlThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin:"));

		int labelWidth = 110;
		int lableHeight = 25;
		Dimension kichThuocNhan = new Dimension(labelWidth, lableHeight);
		Color mauChiDoc = new Color(240, 240, 240);
		Color chuChiDoc = new Color(80, 80, 80);

		// ma dich vu
		JPanel pnlMaDV = new JPanel();
		pnlMaDV.setLayout(new BoxLayout(pnlMaDV, BoxLayout.X_AXIS));
		pnlMaDV.setBorder(BorderFactory.createEmptyBorder(8, 15, 4, 15));
		JLabel lblMaDV = new JLabel("Mã dịch vụ:");
		lblMaDV.setPreferredSize(kichThuocNhan);
		lblMaDV.setMinimumSize(kichThuocNhan);
		lblMaDV.setMaximumSize(kichThuocNhan);
		txtMaDV = new JTextField();
		txtMaDV.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		txtMaDV.setEditable(false);
		txtMaDV.setBackground(mauChiDoc);
		txtMaDV.setForeground(chuChiDoc);
		pnlMaDV.add(lblMaDV);
		pnlMaDV.add(txtMaDV);
		pnlThongTin.add(pnlMaDV);

		// ten dich vu
		JPanel pnlTenDV = new JPanel();
		pnlTenDV.setLayout(new BoxLayout(pnlTenDV, BoxLayout.X_AXIS));
		pnlTenDV.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
		JLabel lblTenDV = new JLabel("Tên dịch vụ:");
		lblTenDV.setPreferredSize(kichThuocNhan);
		lblTenDV.setMinimumSize(kichThuocNhan);
		lblTenDV.setMaximumSize(kichThuocNhan);
		txtTenDV = new JTextField();
		txtTenDV.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		pnlTenDV.add(lblTenDV);
		pnlTenDV.add(txtTenDV);
		pnlThongTin.add(pnlTenDV);

		// gia tieu chuan
		JPanel pnlGiaTieuChuan = new JPanel();
		pnlGiaTieuChuan.setLayout(new BoxLayout(pnlGiaTieuChuan, BoxLayout.X_AXIS));
		pnlGiaTieuChuan.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
		JLabel lblGiaTieuChuan = new JLabel("Giá tiêu chuẩn:");
		lblGiaTieuChuan.setPreferredSize(kichThuocNhan);
		lblGiaTieuChuan.setMinimumSize(kichThuocNhan);
		lblGiaTieuChuan.setMaximumSize(kichThuocNhan);
		txtGiaTieuChuan = new JTextField();
		txtGiaTieuChuan.setMaximumSize(new Dimension(Integer.MAX_VALUE, lableHeight));
		pnlGiaTieuChuan.add(lblGiaTieuChuan);
		pnlGiaTieuChuan.add(txtGiaTieuChuan);
		pnlThongTin.add(pnlGiaTieuChuan);

		// cac nut chuc nang
		JPanel pnlFormButtons = new JPanel();
		pnlFormButtons.setLayout(new BoxLayout(pnlFormButtons, BoxLayout.X_AXIS));
		pnlFormButtons.setBorder(BorderFactory.createEmptyBorder(8, 0, 10, 0));
		pnlFormButtons.add(Box.createHorizontalGlue());

		int btnWidth = 100;
		int btnHeight = 25;
		btnTaoMa = new JButton("Tạo mã");
		btnTaoMa.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlFormButtons.add(btnTaoMa);
		pnlFormButtons.add(Box.createHorizontalStrut(15));

		btnLuu = new JButton("Lưu");
		btnLuu.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlFormButtons.add(btnLuu);
		pnlFormButtons.add(Box.createHorizontalStrut(15));

		btnSua = new JButton("Sửa");
		btnSua.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlFormButtons.add(btnSua);
		pnlFormButtons.add(Box.createHorizontalStrut(15));

		btnXoa = new JButton("Xóa");
		btnXoa.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlFormButtons.add(btnXoa);
		pnlFormButtons.add(Box.createHorizontalStrut(15));

		btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlFormButtons.add(btnLamMoi);
		pnlFormButtons.add(Box.createHorizontalGlue());
		pnlThongTin.add(pnlFormButtons);

		pnlTopHalf.add(pnlThongTin);
		pnlTopHalf.add(Box.createVerticalStrut(15));

		// tim kiem
		JPanel pnlSearch = new JPanel();
		pnlSearch.setLayout(new BoxLayout(pnlSearch, BoxLayout.X_AXIS));
		pnlSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm:"));

		pnlSearch.add(new JLabel("Mã dịch vụ:"));
		pnlSearch.add(Box.createHorizontalStrut(5));
		txtTimMaDV = new JTextField();
		txtTimMaDV.setPreferredSize(new Dimension(160, lableHeight));
		txtTimMaDV.setMaximumSize(new Dimension(160, lableHeight));
		pnlSearch.add(txtTimMaDV);
		pnlSearch.add(Box.createHorizontalStrut(15));

		pnlSearch.add(new JLabel("Tên dịch vụ:"));
		pnlSearch.add(Box.createHorizontalStrut(5));
		txtTimTenDV = new JTextField();
		txtTimTenDV.setPreferredSize(new Dimension(255, lableHeight));
		txtTimTenDV.setMaximumSize(new Dimension(255, lableHeight));
		pnlSearch.add(txtTimTenDV);
		pnlSearch.add(Box.createHorizontalStrut(10));

		btnTim = new JButton("Tìm");
		btnTim.setPreferredSize(new Dimension(btnWidth, btnHeight));
		pnlSearch.add(btnTim);
		pnlSearch.add(Box.createHorizontalGlue());

		pnlTopHalf.add(pnlSearch);
		add(pnlTopHalf, BorderLayout.NORTH);

		// table
		JPanel pnlTable = new JPanel();
		pnlTable.setLayout(new BorderLayout());
		pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		String col[] = { "Mã dịch vụ", "Tên dịch vụ", "Giá tiêu chuẩn" };
		model = new DefaultTableModel(col, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(24);
		table.getTableHeader().setReorderingAllowed(false);

		// tieu de cot canh giua
		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer) {
			((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(JLabel.CENTER);
		}

		JScrollPane pane = new JScrollPane(table);
		pnlTable.add(pane);
		add(pnlTable, BorderLayout.CENTER);

		docDuLieuVaoTable();

		table.addMouseListener(this);
		btnTaoMa.addActionListener(this);
		btnLuu.addActionListener(this);
		btnSua.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnTim.addActionListener(this);
		txtTimMaDV.addActionListener(this);
		txtTimTenDV.addActionListener(this);
	}

	// ham doc du lieu vao bang
	public void docDuLieuVaoTable() {
		try {
			napBang(dichVuDAO.layTatCa());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// do danh sach dich vu len bang
	private void napBang(List<DichVu> dsDichVu) {
		model.setRowCount(0);
		dsHienThi.clear();
		dsHienThi.addAll(dsDichVu);
		for (DichVu dichVu : dsHienThi) {
			model.addRow(new Object[] { dichVu.getMaDV(), dichVu.getTenDV(),
					DinhDang.TIEN.format(dichVu.getGiaTieuChuan()) });
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTaoMa)) {
			table.clearSelection();
			txtTenDV.setText("");
			txtGiaTieuChuan.setText("");
			try {
				txtMaDV.setText(dichVuDAO.phatSinhMaDichVu());
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			txtTenDV.requestFocus();
		} else if (o.equals(btnLuu)) {
			String maDV = txtMaDV.getText().trim();
			if (maDV.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Mã dịch vụ không được rỗng. Hãy ấn 'Tạo mã' để phát sinh mã");
				return;
			}
			double giaTieuChuan;
			try {
				giaTieuChuan = Double.parseDouble(txtGiaTieuChuan.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Giá tiêu chuẩn phải là số (ví dụ: 120000)");
				txtGiaTieuChuan.requestFocus();
				txtGiaTieuChuan.selectAll();
				return;
			}
			DichVu dichVu = new DichVu(maDV, txtTenDV.getText().trim(), giaTieuChuan);
			try {
				dichVuDAO.themDichVu(dichVu);
			} catch (NghiepVuException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this, "Lưu dịch vụ thành công!");
			btnLamMoi.doClick();
		} else if (o.equals(btnSua)) {
			int row = table.getSelectedRow();
			if (row < 0 || row >= dsHienThi.size()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần sửa trên bảng!");
				return;
			}
			double giaTieuChuan;
			try {
				giaTieuChuan = Double.parseDouble(txtGiaTieuChuan.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Giá tiêu chuẩn phải là số (ví dụ: 120000)");
				txtGiaTieuChuan.requestFocus();
				txtGiaTieuChuan.selectAll();
				return;
			}
			DichVu dichVu = new DichVu(dsHienThi.get(row).getMaDV(), txtTenDV.getText().trim(), giaTieuChuan);
			try {
				dichVuDAO.suaDichVu(dichVu);
			} catch (NghiepVuException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!");
			btnLamMoi.doClick();
		} else if (o.equals(btnXoa)) {
			int row = table.getSelectedRow();
			if (row < 0 || row >= dsHienThi.size()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa trên bảng!");
				return;
			}
			DichVu dichVu = dsHienThi.get(row);
			int confirm = JOptionPane.showConfirmDialog(this,
					"Bạn có chắc chắn muốn xóa dịch vụ '" + dichVu.getTenDV() + " (" + dichVu.getMaDV() + ")' không?",
					"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}
			try {
				dichVuDAO.xoaDichVu(dichVu.getMaDV());
			} catch (NghiepVuException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi xóa dịch vụ", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this, "Đã xóa dịch vụ thành công!");
			btnLamMoi.doClick();
		} else if (o.equals(btnLamMoi)) {
			txtMaDV.setText("");
			txtTenDV.setText("");
			txtGiaTieuChuan.setText("");
			txtTimMaDV.setText("");
			txtTimTenDV.setText("");
			table.clearSelection();
			docDuLieuVaoTable();
		} else if (o.equals(btnTim) || o.equals(txtTimMaDV) || o.equals(txtTimTenDV)) {
			List<DichVu> dsKetQua;
			try {
				dsKetQua = dichVuDAO.timKiem(txtTimMaDV.getText(), txtTimTenDV.getText());
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			napBang(dsKetQua);
			if (dsKetQua.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy dịch vụ phù hợp");
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.rowAtPoint(e.getPoint());
		if (row >= 0 && row < dsHienThi.size()) {
			DichVu dichVu = dsHienThi.get(row);
			double gia = dichVu.getGiaTieuChuan();
			txtMaDV.setText(dichVu.getMaDV());
			txtTenDV.setText(dichVu.getTenDV());
			// gia nguyen thi bo phan thap phan
			if (gia == Math.rint(gia)) {
				txtGiaTieuChuan.setText(String.valueOf((long) gia));
			} else {
				txtGiaTieuChuan.setText(String.valueOf(gia));
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
