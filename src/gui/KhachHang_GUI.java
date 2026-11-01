package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatLightLaf;

import entity.KhachHang;


import entity.KhachHang;
import connectDB.ConnectDB;
import dao.KhachHangDAO;


public class KhachHang_GUI extends JFrame implements ActionListener, MouseListener{
		
	private KhachHangDAO kh_DAO = new KhachHangDAO();
	private TableRowSorter<DefaultTableModel> rowSorter; //bo loc tim kiem
	
	private JTextField txtMKH;
	private JTextField txtTenKH;
	private JTextField txtSDT;
	private JTextField txtDiemTLKH;
	private JButton btnThem;
	private JButton btnCapNhat;
	private JButton btnXoa;
	private JTextField txtTimTenKH;
	private JTextField txtTimSDTKH;
	private JTextField txtTimMKH;
	private DefaultTableModel model;
	private JTable table;
	private JButton btnLamMoi;
	private JTextField txtEmail;
	private JButton btnTimKiem;


	public KhachHang_GUI() {
		
		setSize(1200,800);
		setLayout(new BorderLayout());
		
		//noth titile
		JPanel nothTitle=new JPanel();
		JLabel lblNTitile=new JLabel("Quản lý khách hàng");
		lblNTitile.setFont(new Font("Arial", Font.BOLD, 25));
		lblNTitile.setForeground(Color.blue);
		nothTitle.add(lblNTitile);
		this.add(nothTitle, BorderLayout.NORTH);
		
		
		
		//west panel
		JPanel thongTinKH = new JPanel();
		thongTinKH.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
		
		Box b,b1,b2,b3,b4,b5,b6,b7,b8,b9;
		b=Box.createVerticalBox();
		b1=Box.createHorizontalBox();
		b2=Box.createHorizontalBox();
		b3=Box.createHorizontalBox();
		b4=Box.createHorizontalBox();
		b5=Box.createHorizontalBox();
		b6=Box.createHorizontalBox();
		b7=Box.createHorizontalBox();
		b8=Box.createHorizontalBox();
		b9=Box.createHorizontalBox();
		
		JLabel maKH=new JLabel("Mã khách hàng:   ");
		txtMKH= new JTextField(10);
		b1.add(maKH); b1.add(txtMKH);
		b.add(b1);
		b.add(Box.createVerticalStrut(40));
		
		JLabel tenKH=new JLabel("Tên khách hàng:");
		tenKH.setPreferredSize(maKH.getPreferredSize());
		txtTenKH= new JTextField(30);
		b2.add(tenKH); b2.add(txtTenKH);
		b.add(b2);
		b.add(Box.createVerticalStrut(40));
		
		JLabel soDienThoaiKH=new JLabel("Số điện thoại:");
		soDienThoaiKH.setPreferredSize(maKH.getPreferredSize());
		txtSDT= new JTextField(10);
		b3.add(soDienThoaiKH); b3.add(txtSDT);
		b.add(b3);
		b.add(Box.createVerticalStrut(40));
		
		JLabel email=new JLabel("Email:");
		email.setPreferredSize(maKH.getPreferredSize());
		txtEmail= new JTextField(10);
		b6.add(email); b6.add(txtEmail);
		b.add(b6);
		b.add(Box.createVerticalStrut(40));
		
		JLabel diemTLKH=new JLabel("Điểm tích lũy:");
		diemTLKH.setPreferredSize(maKH.getPreferredSize());
		txtDiemTLKH= new JTextField(10);
		b4.add(diemTLKH); b4.add(txtDiemTLKH);
		b.add(b4);
		b.add(Box.createVerticalStrut(40));
		
		
		JMenuBar menu=new JMenuBar();
		btnThem=new JButton("Thêm");
		btnCapNhat=new JButton("Cập nhật");
		btnXoa=new JButton("Xóa");
		btnLamMoi=new JButton("Làm mới");
		menu.add(btnThem);
		menu.add(Box.createHorizontalStrut(5));
		menu.add(btnCapNhat);
		menu.add(Box.createHorizontalStrut(5));
		menu.add(btnXoa);
		menu.add(Box.createHorizontalStrut(5));
		menu.add(btnLamMoi);
		b9.add(menu);
		b.add(b9);
		
		
		thongTinKH.add(b);
		this.add(thongTinKH, BorderLayout.WEST);
		
		
		//center panel
		Box c,c1,c2;
		c=Box.createVerticalBox();
		c1=Box.createHorizontalBox();
		c2=Box.createHorizontalBox();
		
		JPanel pTimKH=new JPanel();
		pTimKH.setBorder(BorderFactory.createTitledBorder("Tìm kiếm khách hàng"));

		
		JLabel lblSDTKH=new JLabel("Nhập số điện thoại khách hàng:");
		txtTimSDTKH=new JTextField(10);
		pTimKH.add(lblSDTKH); pTimKH.add(txtTimSDTKH);
		
		
		btnTimKiem = new JButton("Tìm kiếm");
		pTimKH.add(btnTimKiem);
		
		c.add(pTimKH);
		
		
		JPanel dsKH=new JPanel();
		dsKH.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
		String[] strDSKH= {"Mã KH","Tên khách hàng","SĐT","Email","Điểm TL"};
		model = new DefaultTableModel(strDSKH, 0) {
		    @Override
		    public Class<?> getColumnClass(int columnIndex) {
		        // khai bao cot la so nguyen
		        if (columnIndex == 4) {
		            return Integer.class;
		        }
		        return String.class;
		    }
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // khong cho user sua
		    }
		};
		// ===================================
		

		table=new JTable(model);

		// bo loc sort
		rowSorter = new TableRowSorter<>(model);
		table.setRowSorter(rowSorter);
		
		JScrollPane scrollPaneKH=new JScrollPane(table);
		c2.add(scrollPaneKH);
		c.add(c2);
		
		
		this.add(c);
		
		
		
		//dk sk nut bam
		btnThem.addActionListener(this);
		btnCapNhat.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnTimKiem.addActionListener(this);
		
		//dk sk click chuot
		table.addMouseListener(this);
		
		try {
			connectDB.ConnectDB.getInstance().connect();
			System.out.println("Kết nối cơ sở dữ liệu thành công!");
			
			// ghi mo app, du lieu hien len lien
			DocDuLieuVaoTable(); 
			
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Không thể kết nối tới Cơ sở dữ liệu!\n" + e.getMessage());
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			FlatLightLaf.setup();

	        UIManager.put("Button.arc", 15);
	        UIManager.put("Component.arc", 12);
	        UIManager.put("TextComponent.arc", 10);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			new KhachHang_GUI().setVisible(true);
		});
	}

	// doc du lieu tu table
	private void DocDuLieuVaoTable() {
		model.setRowCount(0); //xoa du lieu cu
		ArrayList<KhachHang> listKH = kh_DAO.getTableKhachHang();
		for (KhachHang kh : listKH) {
			model.addRow(new Object[] { 
				    kh.getMaKH(), 
				    kh.getTenKH(),
				    kh.getSoDienThoai(),
				    kh.getEmail(),
				    kh.getDiemTichLuy()
				});
		}
	}

	//Phat sinh ma: KH1 KH2
//	private String phatSinhMaKH() {
//		ArrayList<KhachHang> listKH = kh_dao.getTableKhachHang();
//		if (listKH.isEmpty()) {
//			return "KH1";
//		}
//		int max = 0;
//		for (KhachHang kh : listKH) {
//			String ma = kh.getMaKH();
//			try {
//				int so = Integer.parseInt(ma.substring(2)); // Cat chu KH, lay so
//				if (so > max) {
//					max = so;
//				}
//			} catch (Exception e) {
//				// Bỏ qua lỗi parse
//			}
//		}
//		return String.format("KH%d", max + 1);
//	}

	//Bieu thuc chinh quy
	private boolean validData() {
		String ten = txtTenKH.getText().trim();
		String sdt = txtSDT.getText().trim();
		String email = txtEmail.getText().trim();
		String diemTL = txtDiemTLKH.getText().trim();

		if (ten.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tên khách hàng không được để trống!");
			txtTenKH.requestFocus();
			return false;
		}
		if (!ten.matches("(?U)^\\p{Lu}[\\p{L}\\d]*(\\s\\p{Lu}[\\p{L}\\d]*)+$")) {
			JOptionPane.showMessageDialog(this, "Tên khách hàng phải viết hoa chữ cái đầu mỗi từ!");
			txtTenKH.requestFocus();
			txtTenKH.selectAll();
			return false;
		}

		if (sdt.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!");
			txtSDT.requestFocus();
			return false;
		}
		if (!sdt.matches("^0\\d{9}$")) {
			JOptionPane.showMessageDialog(this, "Số điện thoại phải bắt đầu bằng 0 và gồm 10 chữ số!");
			txtSDT.requestFocus();
			txtSDT.selectAll();
			return false;
		}
		
		if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
			JOptionPane.showMessageDialog(this, "Email không hợp lệ! Vui lòng nhập đúng định dạng (ví dụ: ten@gmail.com)");
			txtEmail.requestFocus();
			txtEmail.selectAll();
			return false;
		}

		if (diemTL.isEmpty() || !diemTL.matches("^\\d+$")) {
			JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số nguyên!");
			txtDiemTLKH.requestFocus();
			txtDiemTLKH.selectAll();
			return false;
		}
		

		return true;
	}

	// loc du lieu tren table, btn tim
	private void locDuLieu() {
		try {
			ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();
			
			

			String sdtKH = txtTimSDTKH.getText().trim();
			if (!sdtKH.isEmpty()) {
				filters.add(RowFilter.regexFilter("(?i)" + sdtKH, 2)); // Cột 2: SĐT
			}

			if (filters.isEmpty()) {
				rowSorter.setRowFilter(null);
			} else {
				rowSorter.setRowFilter(RowFilter.andFilter(filters));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// su kien nut bam
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		
		if (o.equals(btnLamMoi)) {
//			txtMKH.setText(phatSinhMaKH()); 
			txtMKH.setEditable(false);
			txtTenKH.setText("");
			txtSDT.setText("");
			txtEmail.setText("");
			txtDiemTLKH.setText("0"); // Mặc định điểm là 0
			
			
			txtTimSDTKH.setText("");
			if (rowSorter != null) {
			    rowSorter.setRowFilter(null); //bo bo loc, hien thi tat ca
			}
			
			table.clearSelection();
			txtTenKH.requestFocus();
		} 
		
		// NÚT THÊM
		else if (o.equals(btnThem)) {
			if (validData()) {
				try {
					String ma = txtMKH.getText().trim();
					String ten = txtTenKH.getText().trim();
					String sdt = txtSDT.getText().trim();
					String email = txtEmail.getText().trim();
					int diemTL = Integer.parseInt(txtDiemTLKH.getText().trim()); //ep kieu

					
					KhachHang kh = new KhachHang(ma, ten, sdt, email, diemTL);

					if (kh_DAO.themKhachHang(kh)) {
						model.addRow(new Object[] {ma, ten, sdt, email, diemTL});
						JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
						btnLamMoi.doClick(); 
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}
		} 
		
		// NÚT CẬP NHẬT
		else if (o.equals(btnCapNhat)) {
			int viewRow = table.getSelectedRow();
			if (viewRow < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa trên bảng!");
				return;
			}

			if (validData()) {
				int row = table.convertRowIndexToModel(viewRow);
				try {
					String ma = txtMKH.getText().trim();
					String ten = txtTenKH.getText().trim();
					String sdt = txtSDT.getText().trim();
					String email=txtEmail.getText().trim();
					int diemTL = Integer.parseInt(txtDiemTLKH.getText().trim()); //ep kieu

				
				KhachHang kh = new KhachHang(ma, ten, sdt, email, diemTL);

				if (kh_DAO.suaKhachHang(kh)) {
					model.setValueAt(ten, row, 1);
					model.setValueAt(sdt, row, 2);
					model.setValueAt(email, row, 3);
					model.setValueAt(diemTL, row, 4);
					JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}
		} 
		
		//xoa
		else if (o.equals(btnXoa)) {
			int viewRow = table.getSelectedRow();
			if (viewRow < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
				return;
			}

			int row = table.convertRowIndexToModel(viewRow);
			String maKH = model.getValueAt(row, 0).toString();
			String tenKH = model.getValueAt(row, 1).toString();

			int confirm = JOptionPane.showConfirmDialog(this, 
				"Bạn có chắc muốn xóa khách hàng '" + tenKH + "'?", "Xác nhận", 
				JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try {
					if (kh_DAO.xoaKhachHang(maKH)) {
						model.removeRow(row);
						JOptionPane.showMessageDialog(this, "Xóa thành công!");
						btnLamMoi.doClick();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if (o.equals(btnTimKiem)) {
		    locDuLieu(); // goi ham locDuLieu
		}
	}

	//su kien click chuot de hien len txt
	@Override
	public void mouseClicked(MouseEvent e) {
		int viewRow = table.getSelectedRow();
		if (viewRow >= 0) {
			int row = table.convertRowIndexToModel(viewRow);
			txtMKH.setText(model.getValueAt(row, 0) != null ? model.getValueAt(row, 0).toString() : "");
			txtTenKH.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
			txtSDT.setText(model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "");
			txtEmail.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
			txtDiemTLKH.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
			
			
			txtMKH.setEditable(false); // khong duoc sua ma khi xem
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
