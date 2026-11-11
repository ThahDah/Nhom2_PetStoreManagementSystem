
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
import entity.Loai;
import entity.SanPham;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class SanPhamGUI extends JPanel implements ActionListener, MouseListener,KeyListener{

    private JTextField txtMaSP, txtTenSP;
    private JTextField txtSoLuongTon, txtGiaNhap, txtGiaBan;
    private JTextField txtTimMaSP, txtTimTenSP, txtGiaTu, txtGiaDen;
    private SanPhamDAO sp_dao;
    private JComboBox<String> cboDonViTinh;

    private JButton  btnTaoMa;
    private JButton btnLuu, btnSua, btnXoa, btnLamMoi;

    private JTable tblSanPham;
    private DefaultTableModel model;
	private JComboBox<Loai> cboLoaiTC;
	
	private boolean dangNapForm = false;

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


        JPanel pnlForm = new JPanel(new BorderLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel pnlThongTin = new JPanel(new GridLayout(4, 4, 15, 15));
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
        
        cboLoaiTC = new JComboBox<Loai>(Loai.values());

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
        pnlThongTin.add(new JLabel("Loại thú cưng:"));
        pnlThongTin.add(cboLoaiTC);

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

        tblSanPham.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblSanPham.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblSanPham.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblSanPham.getColumnModel().getColumn(4).setPreferredWidth(130);
        tblSanPham.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        DefaultTableCellRenderer tien = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object v) {
                setText(v instanceof Number ? DinhDang.TIEN.format(((Number) v).doubleValue()) : "");
            }
        };
        tien.setHorizontalAlignment(SwingConstants.RIGHT);
        tblSanPham.getColumnModel().getColumn(4).setCellRenderer(tien);
        tblSanPham.getColumnModel().getColumn(5).setCellRenderer(tien);
        tblSanPham.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(tblSanPham);

        

        JPanel pnlBottom = new JPanel(new BorderLayout(0, 10));
        pnlBottom.setBackground(Color.WHITE);

        pnlBottom.add(pnlSearch, BorderLayout.NORTH);
        pnlBottom.add(scrollPane, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlBottom, BorderLayout.CENTER);
        
        btnTaoMa.addActionListener(this);
        btnLuu.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        tblSanPham.addMouseListener(this);
        txtTimMaSP.addKeyListener(this);
        txtTimTenSP.addKeyListener(this);
        btnLamMoi.addActionListener(this);
        txtGiaTu.addActionListener(this);
        txtGiaDen.addActionListener(this);
        cboLoaiTC.addActionListener(this);
    }
  
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		Object o = e.getSource();
	    if (o.equals(txtTimMaSP)) {
	            txtTimTenSP.setText("");
	            txtGiaTu.setText("");
	            txtGiaDen.setText("");
	        String key = chuanHoa(txtTimMaSP.getText());
	        model.setRowCount(0);
	        for (SanPham sp : sp_dao.getAllSanPham()) {
	            if (chuanHoa(sp.getMaSP()).contains(key)) {
	                themDongVaoBang(sp);
	            }
	        }
	    }  else if (o.equals(txtTimTenSP)) {
	        txtTimMaSP.setText("");
	        txtGiaTu.setText("");
	        txtGiaDen.setText("");
	        String key = chuanHoa(txtTimTenSP.getText());
	        model.setRowCount(0);
	        for (SanPham sp : sp_dao.getAllSanPham()) {
	            if (chuanHoa(sp.getTenSP()).contains(key)) {
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

	        String ma = tblSanPham.getValueAt(row, 0).toString();
	        try {
	            SanPham sp = sp_dao.timTheoMa(ma);
	            if (sp == null) return;
	            txtMaSP.setText(sp.getMaSP());
	            txtTenSP.setText(sp.getTenSP());
	            cboDonViTinh.setSelectedItem(donViTinhToText(sp.getDonViTinh()));
	            txtSoLuongTon.setText(String.valueOf(sp.getSoLuongTon()));
	            txtGiaNhap.setText(String.valueOf((long) sp.getGiaNhap()));
	            txtGiaBan.setText(String.valueOf((long) sp.getGiaBan()));
	            dangNapForm = true;
	            cboLoaiTC.setSelectedItem(sp.getMaSP().startsWith("SPCC") ? Loai.CHO : Loai.MEO);
	            dangNapForm = false;
	            btnTaoMa.setEnabled(false); 
	            cboLoaiTC.setEnabled(false);
	            btnLuu.setEnabled(false);
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
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
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnTaoMa)) {
			try {
		        Loai loai = (Loai) cboLoaiTC.getSelectedItem();
		        txtMaSP.setText(sp_dao.phatSinhMaSanPhamMoi(loai));
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this, "Không thể tạo mã: " + ex.getMessage(),
		                "Lỗi", JOptionPane.ERROR_MESSAGE);
		    }
        }
		else if(o.equals(cboLoaiTC)) {
			 if (dangNapForm) return; 
			try {
		        Loai loai = (Loai) cboLoaiTC.getSelectedItem();
		        txtMaSP.setText(sp_dao.phatSinhMaSanPhamMoi(loai));
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this, "Không thể tạo mã: " + ex.getMessage(),
		                "Lỗi", JOptionPane.ERROR_MESSAGE);
		    }
		}
		else if (o.equals(txtGiaTu) || o.equals(txtGiaDen)){
			 String tu = txtGiaTu.getText().trim(), den = txtGiaDen.getText().trim();
			    if (tu.isEmpty() && den.isEmpty()) { loadTableData(); return; }

			    double giaTu = 0, giaDen = Double.MAX_VALUE;
			    try {
			        if (!tu.isEmpty())  giaTu  = Double.parseDouble(tu);
			        if (!den.isEmpty()) giaDen = Double.parseDouble(den);
			    } catch (NumberFormatException ex) {
			        canhBao("Giá phải là số hợp lệ (không dùng dấu chấm/phẩy ngăn cách)!", null);
			        return;
			    }
			    if (giaTu > giaDen) { canhBao("Giá \"Từ\" không được lớn hơn giá \"Đến\"!", txtGiaTu); return; }

			    txtTimMaSP.setText(""); txtTimTenSP.setText("");
			    model.setRowCount(0);
			    for (SanPham sp : sp_dao.getAllSanPham())
			        if (sp.getGiaBan() >= giaTu && sp.getGiaBan() <= giaDen) themDongVaoBang(sp);
		
		    	
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

            String maSP = tblSanPham.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm có mã: " + maSP + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (sp_dao.xoaSanPham(maSP)) {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                        loadTableData();
                        xoaTrangForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm để xóa!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 547) {
                        JOptionPane.showMessageDialog(this,
                                "Sản phẩm " + maSP + " đã có trong hóa đơn nên không thể xóa!",
                                "Không thể xóa", JOptionPane.WARNING_MESSAGE);
                    } else {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        
       
        else if (o.equals(btnLamMoi)) {
            xoaTrangForm();
            loadTableData();
            txtTimMaSP.setText("");
            txtGiaTu.setText(""); 
            txtTimTenSP.setText("");    
            txtGiaDen.setText("");
        }
        else if (o.equals(btnSua)) {
            int row = tblSanPham.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa trên bảng!");
                return;
            }
            String maSP = tblSanPham.getValueAt(row, 0).toString();   // mã lấy từ bảng
            String tenSP = txtTenSP.getText().trim();
            if (tenSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
                txtTenSP.requestFocus();
                return;
            }

            int soLuongTon;
            double giaNhap, giaBan;
            try {
                soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
                giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
                giaBan = Double.parseDouble(txtGiaBan.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng tồn, giá nhập và giá bán phải là số (ví dụ: 120000)");
                return;
            }
            if (soLuongTon < 0 || giaNhap <= 0 || giaBan <= 0 || giaBan < giaNhap) {
                JOptionPane.showMessageDialog(this,
                        "Số lượng tồn phải >= 0, giá nhập và giá bán phải > 0, giá bán không nhỏ hơn giá nhập!");
                return;
            }

            DonViTinh dvt = textToDonViTinh(cboDonViTinh.getSelectedItem());
            SanPham sp = new SanPham(maSP, tenSP, giaNhap, giaBan, soLuongTon, dvt);

            if (sp_dao.suaSanPham(sp)) {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                loadTableData();
                xoaTrangForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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
		  dangNapForm = true;
		  cboLoaiTC.setSelectedIndex(0);
		  dangNapForm = false;
		  btnTaoMa.setEnabled(true); 
		  cboLoaiTC.setEnabled(true);
		  btnLuu.setEnabled(true);
    }

	private void loadTableData() {
	    model.setRowCount(0);
	    for (SanPham sp : sp_dao.getAllSanPham()) {
	        themDongVaoBang(sp);
	    }
	}
	private void themDongVaoBang(SanPham sp) {
	    model.addRow(new Object[] {
	        sp.getMaSP(),
	        sp.getTenSP(),
	        donViTinhToText(sp.getDonViTinh()),
	        sp.getSoLuongTon(),
	        sp.getGiaNhap(),
	        sp.getGiaBan()
	    });
	}
	private void canhBao(String text, JTextField focus) {
	    JOptionPane.showMessageDialog(this, text, "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
	    if (focus != null) focus.requestFocus();
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
}
