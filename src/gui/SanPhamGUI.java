
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import common.DinhDang;
import connectDB.ConnectDB;
import dao.SanPhamDAO;
import entity.DonViTinh;
import entity.SanPham;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SanPhamGUI extends JPanel implements ActionListener, MouseListener,KeyListener{

    private JTextField txtMaSP, txtTenSP;
    private JTextField txtSoLuongTon, txtGiaNhap, txtGiaBan;
    private JTextField txtTimMaSP, txtTimTenSP, txtGiaTu, txtGiaDen;
    private SanPhamDAO sp_dao;
    private JComboBox<String> cboDonViTinh;

    private JButton btnChonAnh, btnTaoMa;
    private JButton btnLuu, btnSua, btnXoa, btnLamMoi;

    private JTable tblSanPham;
    private DefaultTableModel model;

    public SanPhamGUI() {
    	try {
			ConnectDB.getInstance().connect();
			System.out.println("Connected!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        setLayout(new BorderLayout(10, 15));
        setBackground(Color.WHITE);

        sp_dao = new SanPhamDAO();
        JPanel pnlTop = new JPanel(new BorderLayout(15, 0));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setPreferredSize(new Dimension(0, 190));

        JPanel pnlImage = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        pnlImage.setBackground(Color.WHITE);
        pnlImage.setPreferredSize(new Dimension(145, 190));

        JLabel lblHinhAnh = new JLabel();
        lblHinhAnh.setOpaque(true);
        lblHinhAnh.setBackground(new Color(220, 220, 220));
        lblHinhAnh.setPreferredSize(new Dimension(100, 125));

        btnChonAnh = new JButton("Chọn ảnh");

        pnlImage.add(lblHinhAnh);
        pnlImage.add(btnChonAnh);

        pnlTop.add(pnlImage, BorderLayout.WEST);

        JPanel pnlForm = new JPanel(new BorderLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel pnlThongTin = new JPanel(new GridLayout(3, 4, 15, 15));
        pnlThongTin.setBackground(Color.WHITE);
        pnlThongTin.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        txtMaSP = new JTextField();
        txtMaSP.setEditable(false);
        txtTenSP = new JTextField();
        txtSoLuongTon = new JTextField();
        txtGiaNhap = new JTextField();  
        txtGiaBan = new JTextField();

        cboDonViTinh = new JComboBox<>(new String[] {
                "Cái", "Hộp", "Gói", "Chai", "Túi"
        });

        pnlThongTin.add(new JLabel("Mã sản phẩm:"));
        pnlThongTin.add(txtMaSP);
        pnlThongTin.add(new JLabel("Số lượng tồn:"));
        pnlThongTin.add(txtSoLuongTon);

        pnlThongTin.add(new JLabel("Tên sản phẩm:"));
        pnlThongTin.add(txtTenSP);
        pnlThongTin.add(new JLabel("Giá nhập:"));
        pnlThongTin.add(txtGiaNhap);

        pnlThongTin.add(new JLabel("Đơn vị tính:"));
        pnlThongTin.add(cboDonViTinh);
        pnlThongTin.add(new JLabel("Giá bán:"));
        pnlThongTin.add(txtGiaBan);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlButton.setBackground(Color.WHITE);

        btnTaoMa = new JButton("Tạo mã");
        btnLuu = new JButton("Lưu");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        pnlButton.add(btnTaoMa);
        pnlButton.add(btnLuu);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLamMoi);

        pnlForm.add(pnlThongTin, BorderLayout.CENTER);
        pnlForm.add(pnlButton, BorderLayout.SOUTH);

        pnlTop.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        txtTimMaSP = new JTextField(12);
        txtTimTenSP = new JTextField(12);
        txtGiaTu = new JTextField(7);
        txtGiaDen = new JTextField(7);

        pnlSearch.add(new JLabel("Mã sản phẩm:"));
        pnlSearch.add(txtTimMaSP);

        pnlSearch.add(new JLabel("Tên sản phẩm:"));
        pnlSearch.add(txtTimTenSP);

        pnlSearch.add(new JLabel("Giá bán:"));
        pnlSearch.add(new JLabel("Từ:"));
        pnlSearch.add(txtGiaTu);

        pnlSearch.add(new JLabel("Đến:"));
        pnlSearch.add(txtGiaDen);

        String[] columns = {
                "Hình ảnh",
                "Mã sản phẩm",
                "Tên sản phẩm",
                "Đơn vị tính",
                "Số lượng tồn",
                "Giá nhập",
                "Giá bán"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblSanPham = new JTable(model);
        tblSanPham.setRowHeight(55);
        tblSanPham.setFont(new Font("Arial", Font.PLAIN, 13));
        tblSanPham.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblSanPham.setGridColor(Color.GRAY);
        ArrayList<SanPham> dsSP = sp_dao.getAllSanPham();

        loadTableData();

        tblSanPham.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblSanPham.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblSanPham.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblSanPham.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblSanPham.getColumnModel().getColumn(5).setPreferredWidth(130);
        tblSanPham.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        DefaultTableCellRenderer tien = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object v) {
                setText(v instanceof Number ? DinhDang.TIEN.format(((Number) v).doubleValue()) : "");
            }
        };
        tien.setHorizontalAlignment(SwingConstants.RIGHT);
        tblSanPham.getColumnModel().getColumn(5).setCellRenderer(tien);
        tblSanPham.getColumnModel().getColumn(6).setCellRenderer(tien);
        JScrollPane scrollPane = new JScrollPane(tblSanPham);

        

        JPanel pnlBottom = new JPanel(new BorderLayout(0, 10));
        pnlBottom.setBackground(Color.WHITE);

        pnlBottom.add(pnlSearch, BorderLayout.NORTH);
        pnlBottom.add(scrollPane, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlBottom, BorderLayout.CENTER);
        
        btnTaoMa.addActionListener(this);
        btnLuu.addActionListener(this);
        btnChonAnh.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        tblSanPham.addMouseListener(this);
        txtTimMaSP.addKeyListener(this);
    }
  
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
	    if (e.getSource().equals(txtTimMaSP)) {
	        String key = txtTimMaSP.getText().trim().toLowerCase();
	        model.setRowCount(0);
	        for (SanPham sp : sp_dao.getAllSanPham()) {
	            if (sp.getMaSP().toLowerCase().contains(key)) {
	                themDongVaoBang(sp);
	            }
	        }
	    }
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(tblSanPham)) {
	        int row = tblSanPham.getSelectedRow();
	        if (row == -1) return;

	        String ma = tblSanPham.getValueAt(row, 1).toString();
	        try {
	            SanPham sp = sp_dao.timTheoMa(ma);
	            if (sp == null) return;
	            txtMaSP.setText(sp.getMaSP());
	            txtTenSP.setText(sp.getTenSP());
	            cboDonViTinh.setSelectedItem(donViTinhToText(sp.getDonViTinh()));
	            txtSoLuongTon.setText(String.valueOf(sp.getSoLuongTon()));
	            txtGiaNhap.setText(String.valueOf((long) sp.getGiaNhap()));
	            txtGiaBan.setText(String.valueOf((long) sp.getGiaBan()));
	        } catch (SQLException ex) {
	            ex.printStackTrace();
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
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnTaoMa)) {
            String maMoi = sp_dao.phatSinhMaSanPhamMoi();
            txtMaSP.setText(maMoi);
            txtMaSP.setEditable(false);
        }
		else if (o.equals(btnLuu)) {
		    try {
		        String maSP = txtMaSP.getText().trim();
		        String tenSP = txtTenSP.getText().trim();
		        String slStr = txtSoLuongTon.getText().trim();
		        String giaNhapStr = txtGiaNhap.getText().trim();
		        String giaBanStr = txtGiaBan.getText().trim();

		        if (maSP.isEmpty() || tenSP.isEmpty() || slStr.isEmpty()
		                || giaNhapStr.isEmpty() || giaBanStr.isEmpty()) {
		            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!",
		                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        int soLuongTon = Integer.parseInt(slStr);
		        double giaNhap = Double.parseDouble(giaNhapStr);
		        double giaBan = Double.parseDouble(giaBanStr);

		        if (soLuongTon < 0) {
		            JOptionPane.showMessageDialog(this, "Số lượng tồn không hợp lệ (phải >= 0)!",
		                    "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
		            txtSoLuongTon.requestFocus();
		            return;
		        }
		        if (giaNhap <= 0) {
		            JOptionPane.showMessageDialog(this, "Giá nhập phải lớn hơn 0!",
		                    "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
		            txtGiaNhap.requestFocus();
		            return;
		        }
		        if (giaBan <= 0) {
		            JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn 0!",
		                    "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
		            txtGiaBan.requestFocus();
		            return;
		        }
		        if (giaBan < giaNhap) {
		            JOptionPane.showMessageDialog(this, "Giá bán không được nhỏ hơn giá nhập!",
		                    "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
		            txtGiaBan.requestFocus();
		            return;
		        }

		        DonViTinh dvt = textToDonViTinh(cboDonViTinh.getSelectedItem());
		        SanPham sp = new SanPham(maSP, tenSP, giaNhap, giaBan, soLuongTon, dvt);

		        if (sp_dao.themSanPham(sp)) {
		            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
		            loadTableData();
		            xoaTrangForm();
		        } else {
		            JOptionPane.showMessageDialog(this,
		                    "Thêm sản phẩm thất bại (Có thể bị trùng mã hoặc lỗi CSDL)!",
		                    "Lỗi", JOptionPane.ERROR_MESSAGE);
		        }
		    } catch (NumberFormatException ex) {
		        JOptionPane.showMessageDialog(this, "Số lượng tồn, giá nhập và giá bán phải là số hợp lệ!",
		                "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
		    }
		}
        
        
        else if (o.equals(btnXoa)) {
            int row = tblSanPham.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa trên bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maSP = tblSanPham.getValueAt(row, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm có mã: " + maSP + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (sp_dao.xoaSanPham(maSP)) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    loadTableData();
                    xoaTrangForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
       
        else if (o.equals(btnLamMoi)) {
            xoaTrangForm();
            loadTableData();
            txtTimMaSP.setText("");
        }
	}
	private void xoaTrangForm() {
		  txtMaSP.setText("");
		  txtTenSP.setText("");
		  txtSoLuongTon.setText("");
		  txtGiaNhap.setText("");
		  txtGiaBan.setText("");
		  cboDonViTinh.setSelectedIndex(0);
		  tblSanPham.clearSelection();
    }

	private void loadTableData() {
	    model.setRowCount(0);
	    for (SanPham sp : sp_dao.getAllSanPham()) {
	        themDongVaoBang(sp);
	    }
	}
	private void themDongVaoBang(SanPham sp) {
	    model.addRow(new Object[] {
	        null,
	        sp.getMaSP(),
	        sp.getTenSP(),
	        donViTinhToText(sp.getDonViTinh()),
	        sp.getSoLuongTon(),
	        sp.getGiaNhap(),
	        sp.getGiaBan()
	    });
	}

	// Enum -> chữ hiển thị (khớp với combo box)
	private String donViTinhToText(DonViTinh dv) {
	    if (dv == null) return "Cái";
	    switch (dv) {
	        case GOI:  return "Gói";
	        case HOP:  return "Hộp";
	        case CHAI: return "Chai";
	        case TUI:  return "Túi";
	        default:   return "Cái";
	    }
	}

	// Chữ trong combo box -> enum
	private DonViTinh textToDonViTinh(Object text) {
	    if (text == null) return DonViTinh.CAI;
	    switch (text.toString()) {
	        case "Gói":  return DonViTinh.GOI;
	        case "Hộp":  return DonViTinh.HOP;
	        case "Chai": return DonViTinh.CHAI;
	        case "Túi":  return DonViTinh.TUI;
	        default:     return DonViTinh.CAI;
	    }
	}
}
