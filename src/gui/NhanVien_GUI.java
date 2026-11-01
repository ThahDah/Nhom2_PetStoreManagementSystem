package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Struct;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import com.formdev.flatlaf.FlatLightLaf;

import dao.NhanVienDAO;
import entity.NhanVien;
import entity.TrangThai;


//import dao.NhanVien_dao;

public class NhanVien_GUI extends JFrame implements ActionListener, MouseListener{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton btnPickAvatar;
	
	private JTextField txtMNV;
	private JTextField txtTenNV;
	private JTextField txtSDTNV;
	private JTextField txtEmail;
	
	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLamMoi;
	
	private JTextField txtTimMaNV;
	private JTextField txtTimTenNV;
	private JTextField txtTimSDTNV;
	
	private DefaultTableModel model;
	private JTable table;

	private JButton btnTim;
	
	private NhanVienDAO nv_dao = new NhanVienDAO();
    private TableRowSorter<DefaultTableModel> rowSorter;


	private String duongDanAnh;

	private JLabel lblAvatar;

	private JComboBox<TrangThai> cbTrangThai;

	private AbstractButton btnPhatSinhMa;

	public NhanVien_GUI() {
		
		setSize(1450, 800);
		setLayout(new BorderLayout());
		setVisible(true);

		Box b = Box.createVerticalBox();
		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();

		// Tieu de Noth
		JPanel notharea = new JPanel();
		JLabel lblNTitile = new JLabel("QUẢN LÝ NHÂN VIÊN");
		lblNTitile.setFont(new Font("Arial", Font.BOLD, 25));
		lblNTitile.setForeground(Color.blue);
		notharea.add(lblNTitile);
		b1.add(notharea);
		b.add(b1);

		// Phan hinh anh ben trai
		Box bAva = Box.createVerticalBox();
		lblAvatar = new JLabel();
		Dimension size = new Dimension(150, 200);
		lblAvatar.setPreferredSize(size);
		lblAvatar.setMinimumSize(size);
		lblAvatar.setMaximumSize(size);
		lblAvatar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblAvatar.setAlignmentX(CENTER_ALIGNMENT);

		btnPickAvatar = new JButton("Chọn ảnh");
		btnPickAvatar.setAlignmentX(CENTER_ALIGNMENT);

		bAva.add(lblAvatar);
		bAva.add(Box.createVerticalStrut(10));
		bAva.add(btnPickAvatar);

		b2.add(Box.createHorizontalStrut(15));
		b2.add(bAva);
		b2.add(Box.createHorizontalStrut(15));
		
		
		// form ben phai
		JPanel form = new JPanel();
		// form, dung border
		form.setLayout(new BorderLayout(0, 20)); 
		form.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên:"));

		//chi tiet trong form
		JPanel pGridFirst = new JPanel(new GridLayout(6, 1, 0, 15));

		// Dòng 1
		JPanel pDong1 = new JPanel(new GridLayout(1, 4, 12, 0));
		pDong1.add(new JLabel("Mã nhân viên:"));
		pDong1.add(txtMNV = new JTextField(15));
		txtMNV.setEditable(false);
		pDong1.add(new JLabel("Tên nhân viên:"));
		pDong1.add(txtTenNV = new JTextField(15));

		// Dòng 2
		JPanel pDong2 = new JPanel(new GridLayout(1, 4, 15, 0));
		pDong2.add(new JLabel("Số điện thoại:"));
		pDong2.add(txtSDTNV = new JTextField(15));
		pDong2.add(new JLabel("Email:"));         
		pDong2.add(txtEmail = new JTextField(15));
		
		//Dòng 3
		JPanel pDong3 = new JPanel(new GridLayout(1, 4, 15, 0));
		pDong3.add(new JLabel("Trạng thái:"));
		cbTrangThai = new JComboBox<entity.TrangThai>(entity.TrangThai.values());
		pDong3.add(cbTrangThai);
		pDong3.add(Box.createHorizontalStrut(50));


		pGridFirst.add(pDong1); 
		pGridFirst.add(pDong2); 
		pGridFirst.add(pDong3); 

		// nut bam la flowlayout
		JPanel pGridSecond = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0)); 
		Dimension btnSize = new Dimension(120, 30); // thu nho nut

		btnPhatSinhMa = new JButton("Phát sinh mã"); btnPhatSinhMa.setPreferredSize(btnSize);
		btnThem = new JButton("Thêm"); btnThem.setPreferredSize(btnSize);
		btnSua = new JButton("Sửa"); btnSua.setPreferredSize(btnSize);
		btnXoa = new JButton("Xóa"); btnXoa.setPreferredSize(btnSize);
		btnLamMoi = new JButton("Làm mới"); btnLamMoi.setPreferredSize(btnSize);

		pGridSecond.add(btnPhatSinhMa);
		pGridSecond.add(btnThem);
		pGridSecond.add(btnSua);
		pGridSecond.add(btnXoa);
		pGridSecond.add(btnLamMoi);

		// them 2 khu vuc vao form
		form.add(pGridFirst, BorderLayout.CENTER); 
		form.add(pGridSecond, BorderLayout.SOUTH); 

		b2.add(form);
		b.add(b2);

		// Tim kiem
		JPanel pTim = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); //flow layout
		pTim.setBorder(BorderFactory.createTitledBorder("Tìm kiếm nhân viên"));
		
		pTim.add(new JLabel("Mã nhân viên:"));
		txtTimMaNV = new JTextField(10);
		pTim.add(txtTimMaNV);
		
		
		btnTim= new JButton("Tìm");
		pTim.add(btnTim);
		
		b3.add(pTim);
		b.add(b3);

		//Box noth
		this.add(b, BorderLayout.NORTH);

		// Phan center, table
		String[] tititleTable = {"Hình ảnh","Mã NV", "Tên NV", "SĐT", "Email", "Trang thai"};
		model = new DefaultTableModel(tititleTable, 0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // khong cho sua truc tiep
		        return false;
		    }
		};

		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		//xu ly su kien
		rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);
        
        //Them vao table
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMinWidth(60);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		
		//Hien thi hinh anh tren table
		// 1. Tăng chiều cao hàng để nhìn thấy ảnh 
		table.setRowHeight(60); 

		// 2. Thiết lập bộ vẽ (Renderer) cho cột số 0
		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		            boolean hasFocus, int row, int column) {
		        
		        // Kế thừa JLabel từ lớp cha
		        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		        label.setText(""); // Xóa đường dẫn text để hiện hình
		        label.setHorizontalAlignment(JLabel.CENTER);

		        if (value != null && !value.toString().trim().isEmpty()) {
		            String path = value.toString();
		            File fileAnh = new File(path);

		            if (fileAnh.exists()) {
		                ImageIcon icon = new ImageIcon(path);
		                // Scale ảnh cho vừa ô (40x55)
		                Image img = icon.getImage().getScaledInstance(40, 55, Image.SCALE_SMOOTH);
		                label.setIcon(new ImageIcon(img));
		            } else {
		                label.setIcon(null);
		                label.setText("Lỗi ảnh");
		            }
		        } else {
		            label.setIcon(null);
		        }
		        return label;
		    }
		});

        // sk click, nut bam
		btnPhatSinhMa.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTim.addActionListener(this);
        btnPickAvatar.addActionListener(this);      
        table.addMouseListener(this);

        // ket noi csdl
        try {
            connectDB.ConnectDB.getInstance().connect();
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
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
			new NhanVien_GUI().setVisible(true);
		});
	}
	
	private void DocDuLieuVaoTable() {
	    model.setRowCount(0); 
	    ArrayList<NhanVien> listNV = nv_dao.getTableNhanVien();
	    for (NhanVien nv : listNV) {
	        model.addRow(new Object[] { 
	            nv.getHinhAnh(),
	            nv.getMaNV(), 
	            nv.getTenNV(),
	            nv.getSoDienThoai(),
	            nv.getEmail(),
	            nv.getTrangThaiNV()
	        });
	    }
	}
	
	private String phatSinhMaNV() {
		ArrayList<NhanVien> listNV = nv_dao.getTableNhanVien();
		if (listNV.isEmpty()) {
			return "NV1";
		}
		int max = 0;
		for (NhanVien nv : listNV) {
			String ma = nv.getMaNV();
			try {
				int so = Integer.parseInt(ma.substring(2)); // cat chu NV ra, lay so tinh toan
				if (so > max) {
					max = so;
				}
			} catch (Exception e) {
				// bo qua loi
			}
		}
		return String.format("NV%d", max + 1);
	}
	
	private boolean validData() {
		String ten = txtTenNV.getText().trim();
		String sdt = txtSDTNV.getText().trim();
		String email = txtEmail.getText().trim();

		if (ten.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
			txtTenNV.requestFocus();
			return false;
		}
		if (!ten.matches("(?U)^\\p{Lu}[\\p{L}\\d]*(\\s\\p{Lu}[\\p{L}\\d]*)+$")) {
			JOptionPane.showMessageDialog(this, "Tên nhân viên phải viết hoa chữ cái đầu mỗi từ!");
			txtTenNV.requestFocus();
			txtTenNV.selectAll();
			return false;
		}

		if (sdt.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!");
			txtSDTNV.requestFocus();
			return false;
		}
		if (!sdt.matches("^0\\d{9}$")) {
			JOptionPane.showMessageDialog(this, "Số điện thoại phải bắt đầu bằng 0 và gồm 10 chữ số!");
			txtSDTNV.requestFocus();
			txtSDTNV.selectAll();
			return false;
		}

		if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
			txtEmail.requestFocus();
			txtEmail.selectAll();
			return false;
		}

		return true;
	}
	
	private void locDuLieu() {
		try {
			ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();
			
			String maNV = txtTimMaNV.getText().trim();
			if (!maNV.isEmpty()) {
				filters.add(RowFilter.regexFilter("(?i)" + maNV, 1)); // Cột 1: Mã NV
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		//btn lam moi
		if (o.equals(btnLamMoi)) {
			txtMNV.setText(phatSinhMaNV()); 
			txtMNV.setEditable(false);
			txtTenNV.setText("");
			txtSDTNV.setText("");
			txtEmail.setText("");
			duongDanAnh = null;
			lblAvatar.setIcon(null);
			cbTrangThai.setSelectedIndex(0);
			
			txtTimMaNV.setText("");
			
			
			if (rowSorter != null) {
			    rowSorter.setRowFilter(null); // bo bo loc
			}
			
			table.clearSelection();
			txtTenNV.requestFocus();
		} 
		
		else if (o.equals(btnPickAvatar)) {
		    javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
		    
		    // chi cho phep nhan vien nop file anh
		    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png"));
		    int result = fileChooser.showOpenDialog(this);

		    if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
		        java.io.File file = fileChooser.getSelectedFile();
		        duongDanAnh = file.getAbsolutePath(); // luu lai duong dan vao database

		        // chinh kich thuoc
		        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
		            new javax.swing.ImageIcon(duongDanAnh).getImage().getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH)
		        );
		        lblAvatar.setIcon(icon); 
		    }
		}
		
		//btn them
		else if (o.equals(btnThem)) {
		    if (validData()) {
		        try {
		            // phat sinh Ma nhan vien
		            String ma = txtMNV.getText().trim().isEmpty() ? phatSinhMaNV() : txtMNV.getText().trim();
		            String ten = txtTenNV.getText().trim();
		            String sdt = txtSDTNV.getText().trim();
		            String email = txtEmail.getText().trim();
		            TrangThai trangThai = (TrangThai)cbTrangThai.getSelectedItem();

		            // lay anh dung
		            String hinhAnh = duongDanAnh != null ? duongDanAnh : "";

		            NhanVien nv = new NhanVien(ma, ten, ten, email, trangThai, hinhAnh);


		            if (nv_dao.themNhanVien(nv)) {

		                model.addRow(new Object[]{
		                	    nv.getHinhAnh(), 
		                	    nv.getMaNV(), 
		                	    nv.getTenNV(),  
		                	    nv.getSoDienThoai(), 
		                	    nv.getEmail(), 
		                	    nv.getTrangThaiNV()
		                	});

		                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
		                btnLamMoi.doClick();
		            }

		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
		        }
		    }
		}
		else if (o.equals(btnPickAvatar)) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Chọn hình ảnh sản phẩm");
			// chi cho phep chon anh
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh (JPG, PNG, GIF)", "jpg", "jpeg",
					"png", "gif");
			fileChooser.setFileFilter(filter);

			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				try {
					File destFolder = new File("img");
					if (!destFolder.exists()) {
						destFolder.mkdir();
					}
					// coppy file vao img
					File destFile = new File(destFolder, selectedFile.getName());
					Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

					duongDanAnh = "img/" + selectedFile.getName();
					hienThiAnh(duongDanAnh);

				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "Lỗi khi lưu file ảnh!");
				}
			}

		} 
		// btn sua
		else if (o.equals(btnSua)) {
		    int viewRow = table.getSelectedRow();
		    if (viewRow < 0) {
		        JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
		        return;
		    }

		    if (validData()) {
		        int row = table.convertRowIndexToModel(viewRow);

		        try {
		            String ma = txtMNV.getText().trim();
		            String ten = txtTenNV.getText().trim();
		            String sdt = txtSDTNV.getText().trim();
		            String email = txtEmail.getText().trim();
		            TrangThai trangThai = (TrangThai)cbTrangThai.getSelectedItem();

		            // neu chon anh thi chon moi, khong thi chon cu
		            String hinhAnh = (duongDanAnh != null) 
		                    ? duongDanAnh 
		                    : (model.getValueAt(row, 0) != null ? model.getValueAt(row, 0).toString() : "");


		            NhanVien nv = new NhanVien(ma, ten, ten, email, trangThai, hinhAnh);

		            if (nv_dao.suaNhanVien(nv)) {

		                model.setValueAt(hinhAnh, row, 0);
		                model.setValueAt(ma, row, 1);
		                model.setValueAt(ten, row, 2);
		                model.setValueAt(sdt, row, 3);
		                model.setValueAt(email, row, 4);
		                model.setValueAt(trangThai.name(), row, 5);

		                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
		                btnLamMoi.doClick();
		            }

		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(this, "Lỗi cập nhật!");
		        }
		    }
		}
		
		//btn xoa
		else if (o.equals(btnXoa)) {
			int viewRow = table.getSelectedRow();
			if (viewRow < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
				return;
			}

			int row = table.convertRowIndexToModel(viewRow);
			String maNV = model.getValueAt(row, 1).toString();
			String tenNV = model.getValueAt(row, 2).toString();

			int confirm = JOptionPane.showConfirmDialog(this, 
				"Bạn có chắc muốn xóa nhân viên '" + tenNV + "'?", "Xác nhận", 
				JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try {
					if (nv_dao.xoaNhanVien(maNV)) {
						model.removeRow(row);
						JOptionPane.showMessageDialog(this, "Xóa thành công!");
						btnLamMoi.doClick();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		// btn tim
		else if (o.equals(btnTim)) {
		    locDuLieu(); 
		}
	}
	
	private void hienThiAnh(String path) {
		if (path == null || path.trim().isEmpty()) {
			lblAvatar.setIcon(null);
			return;
		}
		File fileAnh = new File(path);
		if (fileAnh.exists()) {
			ImageIcon icon = new ImageIcon(path);
			Image img = icon.getImage();
			Image scaledImg = img.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
			lblAvatar.setIcon(new ImageIcon(scaledImg));
		} else {
			lblAvatar.setIcon(null);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int viewRow = table.getSelectedRow();
	    if (viewRow >= 0) {
	        int row = table.convertRowIndexToModel(viewRow);

	        // 2. Cập nhật hình ảnh (Cột 0)
	        Object objAnh = model.getValueAt(row, 0);
	        duongDanAnh = (objAnh != null) ? objAnh.toString() : "";
	        hienThiAnh(duongDanAnh);

	        // 3. Đổ dữ liệu cơ bản lên TextField (Cột 1 -> Cột 4)
	        
	        txtMNV.setText(model.getValueAt(row, 1).toString());
	        txtTenNV.setText(model.getValueAt(row, 2).toString());
	        
	        
	        txtSDTNV.setText(model.getValueAt(row, 3).toString());
	        txtEmail.setText(model.getValueAt(row, 4).toString());

	        // 4. Đổ dữ liệu Enum lên ComboBox (Cột 8, 9)
	        try {
	            String trangThaiStr=model.getValueAt(row, 5).toString();
	            if(!trangThaiStr.isEmpty()) cbTrangThai.setSelectedItem(entity.TrangThai.valueOf(trangThaiStr));
	        } catch (Exception ex) {
	            // Tránh crash nếu dữ liệu Enum trong table bị trống
	        }

	        

	        // 7. Khóa Mã nhân viên 
	        txtMNV.setEditable(false);
	    }
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}