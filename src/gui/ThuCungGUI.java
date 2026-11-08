package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatLightLaf;

import entity.DoHungDu;
import entity.KhachHang;
import entity.Loai;
import entity.TrangThai;
import dao.ThuCungDAO;
import entity.ThuCung;
import dao.KhachHangDAO;

public class ThuCungGUI extends JPanel implements ActionListener, MouseListener {
    private JTextField txtMTC;
    private JTextField txtTenTC;
    private JComboBox<Loai> loaiTC;
    private JTextField txtCanNang;
    private JTextArea txtGhiChu;
    private JComboBox<TrangThai> trangThai;
    private JComboBox<DoHungDu> doHungDu;
    private JComboBox<KhachHang> chuSoHuu;
    private JButton btnThem;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnPhatSinhMa;
    private JButton btnTimKiem;
    private DefaultTableModel model;
    private Vector<?> strDSTC;
    private JTable table;
    private TableRowSorter<DefaultTableModel> rowSorter; // AI hỗ trợ sửa lỗi Thêm dấu <?> để tránh warning
	private JTextField txtTimMTC;
	
	private ThuCungDAO tc_dao=new ThuCungDAO();
	private JTextField txtTimTCTheoSdtKH;
	
    private KhachHangDAO kh_dao = new KhachHangDAO();

    public ThuCungGUI() {
        setSize(1200, 800);
        setLayout(new BorderLayout());
    
        // --- NORTH PANEL ---
        JPanel nothTitle = new JPanel();
        JLabel lblNTitile = new JLabel("Quản lý thú cưng");
        lblNTitile.setFont(new Font("Arial", Font.BOLD, 25));
        lblNTitile.setForeground(Color.blue);
        nothTitle.add(lblNTitile);
        this.add(nothTitle, BorderLayout.NORTH);

        // --- WEST PANEL---
        JPanel thongTinKH = new JPanel();
        thongTinKH.setBorder(BorderFactory.createTitledBorder("Thông tin thú cưng"));

        Box b,b1,b2,b3,b4,b5,b6,b7,b8,b9,b10;
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
		b10=Box.createHorizontalBox();

        
        JLabel maTC = new JLabel("Mã thú cưng:   ");
        txtMTC = new JTextField(10);
        txtMTC.setEditable(false);
        b1.add(maTC);
        b1.add(txtMTC);
        b.add(b1);
        b.add(Box.createVerticalStrut(20)); 

        JLabel tenTC = new JLabel("Tên thú cưng:");
        tenTC.setPreferredSize(maTC.getPreferredSize());
        txtTenTC = new JTextField(30);
        b2.add(tenTC);
        b2.add(txtTenTC);
        b.add(b2);
        b.add(Box.createVerticalStrut(20));

        JLabel lblLoaiTC = new JLabel("Loại thú cưng:");
        lblLoaiTC.setPreferredSize(maTC.getPreferredSize());
        loaiTC = new JComboBox<Loai>(Loai.values());
        b3.add(lblLoaiTC);
        b3.add(loaiTC);
        b.add(b3);
        b.add(Box.createVerticalStrut(20));

        JLabel canNang = new JLabel("Cân nặng:");
        canNang.setPreferredSize(maTC.getPreferredSize());
        txtCanNang = new JTextField(10);
        b4.add(canNang);
        b4.add(txtCanNang);
        b.add(b4);
        b.add(Box.createVerticalStrut(20));

        JLabel ghiChu = new JLabel("Ghi chú:");
        ghiChu.setPreferredSize(maTC.getPreferredSize());
        txtGhiChu = new JTextArea(10, 3);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);  

        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        b5.add(ghiChu);
        b5.add(txtGhiChu);
        b.add(b5);
        b.add(Box.createVerticalStrut(20));

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setPreferredSize(maTC.getPreferredSize());
        entity.TrangThai[] trangThaiThuCung = {
        	    entity.TrangThai.BINH_THUONG,
        	    entity.TrangThai.DANG_CHAM_SOC,
        	    entity.TrangThai.HOAN_THANH
        	};

        	trangThai = new JComboBox<entity.TrangThai>(trangThaiThuCung);
        b6.add(lblTrangThai);
        b6.add(trangThai);
        b.add(b6);
        b.add(Box.createVerticalStrut(20));

        JLabel lblDoHD = new JLabel("Độ hung dữ:");
        lblDoHD.setPreferredSize(maTC.getPreferredSize());
        doHungDu = new JComboBox<DoHungDu>(DoHungDu.values());
        b7.add(lblDoHD);
        b7.add(doHungDu);
        b.add(b7);
        b.add(Box.createVerticalStrut(20));

        JLabel lblChuSoHuu = new JLabel("Chủ sở hữu:");
        lblChuSoHuu.setPreferredSize(maTC.getPreferredSize());
        chuSoHuu = new JComboBox<KhachHang>();
        b8.add(lblChuSoHuu);
        b8.add(chuSoHuu);
        b.add(b8);
        b.add(Box.createVerticalStrut(20));

        JPanel menu = new JPanel();
        btnThem = new JButton("Thêm");
        btnCapNhat = new JButton("Cập nhật");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        
        // dk sk cho cac nut
        btnThem.addActionListener(this);
        btnCapNhat.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);

        menu.add(btnThem);
        menu.add(Box.createHorizontalStrut(5));
        menu.add(btnCapNhat);
        menu.add(Box.createHorizontalStrut(5));
        menu.add(btnXoa);
        menu.add(Box.createHorizontalStrut(5));
        menu.add(btnLamMoi);
        b9.add(menu);
        b.add(b9);
        b.add(Box.createVerticalStrut(10));

        // nut phat sinh ma
        JPanel menu2 = new JPanel();
        btnPhatSinhMa = new JButton("Phát sinh mã");
        btnPhatSinhMa.addActionListener(this); //dk su kien
        
        menu2.add(Box.createHorizontalStrut(5));
        menu2.add(btnPhatSinhMa);
        b10.add(menu2);
        b.add(b10);

        thongTinKH.add(b);
        this.add(thongTinKH, BorderLayout.WEST);
        
      //center panel
  		Box c,c1,c2;
  		c=Box.createVerticalBox();
  		c1=Box.createHorizontalBox();
  		c2=Box.createHorizontalBox();
  		
  		JPanel pTimKH=new JPanel();
  		pTimKH.setBorder(BorderFactory.createTitledBorder("Tìm kiếm thú cưng"));

  		
  		JLabel lblSDTKH=new JLabel("Nhập số điện thoại khách hàng:");
  		txtTimTCTheoSdtKH=new JTextField(10);
  		pTimKH.add(lblSDTKH); pTimKH.add(txtTimTCTheoSdtKH);
  		
  		
  		btnTimKiem = new JButton("Tìm kiếm");
  		pTimKH.add(btnTimKiem);
  		btnTimKiem.addActionListener(this);
  		
  		c.add(pTimKH);
  		
  		
  		JPanel dsKH=new JPanel();
  		dsKH.setBorder(BorderFactory.createTitledBorder("Danh sách thú cưng"));
  		String[] strDSKH= {"Mã TC","Tên TC","Loại","Cân nặng","Ghi chú","Trạng thái","Độ hung dữ","Chủ sở hữu"};
  		model = new DefaultTableModel(strDSKH, 0) {
  		    @Override
  		    public Class<?> getColumnClass(int columnIndex) {
  		        // khai bao cot la so nguyen
  		        if (columnIndex == 3 || columnIndex == 4) {
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
  		table.addMouseListener(this);
  		
  		loaiTC.addActionListener(e -> {
  		    // Chỉ tự đổi mã khi ô nhập mã đang có nội dung hoặc đang tạo mới
  		    if (!txtMTC.getText().trim().isEmpty()) {
  		        txtMTC.setText(phatSinhMaTC());
  		    }
  		});
  		
  		this.add(c);

        //ket noi database
         try {
             connectDB.ConnectDB.getInstance().connect();
             System.out.println("Kết nối cơ sở dữ liệu thành công!");
             loadComboBoxKhachHang();
             DocDuLieuVaoTable();
         } catch (java.sql.SQLException e) {
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Không thể kết nối tới CSDL!\n" + e.getMessage());
         }
    } 
    
    
    // doc du lieu tu table
 	private void DocDuLieuVaoTable() {
 		model.setRowCount(0); //xoa du lieu cu
 		ArrayList<ThuCung> listTC = tc_dao.getTableThuCung();
 		for (ThuCung tc : listTC) {
 			model.addRow(new Object[] { 
 				    tc.getMaThuCung(), 
 				    tc.getTenThuCung(),
 				    tc.getLoai(),
 				    tc.getCanNang(),
 				    tc.getGhiChu(),
 				    tc.getTrangThaiThuCung(),
 				    tc.getDoHungDu(),
 				    tc.getChuSoHuu().getTenKH()
 				    
 				});
 		}
 	}
 	
 	//Phat sinh ma thu cung
 	private String phatSinhMaTC() {
 	    // Xac dinh loai thu cung
 	    Loai loaiDuocChon = (Loai) loaiTC.getSelectedItem();
 	    String kyTuLoai = "C"; //mac dinh
 	    if (loaiDuocChon != null) {
 	        String tenLoai = loaiDuocChon.toString().trim().toLowerCase();
 	        if (tenLoai.contains("mèo") || tenLoai.contains("meo") || loaiDuocChon.name().equalsIgnoreCase("MEO")) {
 	            kyTuLoai = "M";
 	        } else {
 	            kyTuLoai = "C";
 	        }
 	    }

 	    // Lay chuoi ngay thang nam theo dinh dang ddMMyy (Vi du: 050226)
 	    LocalDate now = LocalDate.now();
 	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
 	    String ngayThangNam = now.format(formatter);

 	    // Tien to can tim: TC + C/M + ddMMyy (Ví dụ: "TCC050226")
 	    String prefix = "TC" + kyTuLoai + ngayThangNam;

 	    // Tim so thu tu lon nhat trong danh sach
 	    ArrayList<ThuCung> listTC = tc_dao.getTableThuCung();
 	    int maxSTT = 0;
 	    if (listTC != null) {
 	        for (ThuCung tc : listTC) {
 	            String ma = tc.getMaThuCung();
 	            if (ma != null && ma.startsWith(prefix) && ma.length() >= prefix.length() + 2) {
 	                try {
 	                    int stt = Integer.parseInt(ma.substring(prefix.length()));
 	                    if (stt > maxSTT) {
 	                        maxSTT = stt;
 	                    }
 	                } catch (NumberFormatException ignored) {}
 	            }
 	        }
 	    }

 	    // phat sinh ma ke tiep dinh dang 2 so
 	    return String.format("%s%02d", prefix, maxSTT + 1);
 	}
	
	// loc du lieu tren table, btn tim
	private void locDuLieu() {
	    String sdt = txtTimTCTheoSdtKH.getText().trim();
	    
	    if (sdt.isEmpty()) {
	        DocDuLieuVaoTable();
	    } else {
	        ArrayList<ThuCung> listTC = tc_dao.timThuCungTheoSoDienThoai(sdt);
	        
	        model.setRowCount(0); // Xóa dữ liệu cũ trên bảng đang hiển thị
	        
	        if (listTC.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Không tìm thấy thú cưng nào với số điện thoại: " + sdt);
	            
	        } else {
	            for (ThuCung tc : listTC) {
	                model.addRow(new Object[] { 
	                    tc.getMaThuCung(), 
	                    tc.getTenThuCung(),
	                    tc.getLoai(),
	                    tc.getCanNang(),
	                    tc.getGhiChu(),
	                    tc.getTrangThaiThuCung(),
	                    tc.getDoHungDu(),
	                    tc.getChuSoHuu().getTenKH()
	                });
	            }
	        }
	    }
	}
	
	private boolean validData() {
	    String ten = txtTenTC.getText().trim();
	    String canNang = txtCanNang.getText().trim();
	    
	    if (ten.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Tên thú cưng không được để trống!");
	        txtTenTC.requestFocus();
	        return false;
	    }
	    if (!ten.matches("(?U)^\\p{Lu}[\\p{L}\\d]*(\\s\\p{Lu}[\\p{L}\\d]*)*$")) {
	        JOptionPane.showMessageDialog(this, "Tên thú cưng phải viết hoa chữ cái đầu mỗi từ!");
	        txtTenTC.requestFocus();
	        txtTenTC.selectAll();
	        return false;
	    }

	    if (canNang.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Cân nặng không được để trống!");
	        txtCanNang.requestFocus();
	        return false;
	    }
	    
	    if (!canNang.matches("^\\d+(\\.\\d+)?$")) {
	        JOptionPane.showMessageDialog(this, "Cân nặng phải là số (VD: 5 hoặc 2.5)!");
	        txtCanNang.requestFocus();
	        txtCanNang.selectAll();
	        return false;
	    }
	    
	    // can nang AI hỗ trợ sửa lỗi
	    try {
	        float cn = Float.parseFloat(canNang);
	        if (cn <= 0) {
	            JOptionPane.showMessageDialog(this, "Cân nặng phải lớn hơn 0!");
	            txtCanNang.requestFocus();
	            txtCanNang.selectAll();
	            return false;
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Lỗi định dạng cân nặng!");
	        return false;
	    } 

	    return true;
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnLamMoi)) {
            txtMTC.setText(""); 
            txtTenTC.setText("");
            txtTenTC.setEditable(true);
            txtCanNang.setText("");
            txtGhiChu.setText("");
        
            if (loaiTC.getItemCount() > 0) loaiTC.setSelectedIndex(0);
            if (trangThai.getItemCount() > 0) trangThai.setSelectedIndex(0);
            if (doHungDu.getItemCount() > 0) doHungDu.setSelectedIndex(0);
            if (chuSoHuu.getItemCount() > 0) chuSoHuu.setSelectedIndex(0);
            
            txtTimTCTheoSdtKH.setText("");
            
            if (rowSorter != null) {
                rowSorter.setRowFilter(null);
            }
            table.clearSelection();
            
            txtTenTC.requestFocus();
        }
        
        else if(o.equals(btnPhatSinhMa)) {
        		txtMTC.setText(phatSinhMaTC()); 
            txtTenTC.setText("");
            txtTenTC.setEditable(true);
            txtCanNang.setText("");
            txtGhiChu.setText("");
        
            if (loaiTC.getItemCount() > 0) loaiTC.setSelectedIndex(0);
            if (trangThai.getItemCount() > 0) trangThai.setSelectedIndex(0);
            if (doHungDu.getItemCount() > 0) doHungDu.setSelectedIndex(0);
            if (chuSoHuu.getItemCount() > 0) chuSoHuu.setSelectedIndex(0);
            
            txtTimTCTheoSdtKH.setText("");
            
        }
        
        else if (o.equals(btnPhatSinhMa)) {
            txtMTC.setText(phatSinhMaTC());
            table.clearSelection();
            txtTenTC.requestFocus();
        }
		
        // nut them
        else if (o.equals(btnThem)) {
            if (validData()) {
                try {
                    String ma = txtMTC.getText().trim();
                    String ten = txtTenTC.getText().trim();
                    float canNang = Float.parseFloat(txtCanNang.getText().trim());
                    String ghiChu = txtGhiChu.getText().trim();

                    Loai loai = (Loai) loaiTC.getSelectedItem();
                    TrangThai tt = (TrangThai) trangThai.getSelectedItem();
                    DoHungDu doHD = (DoHungDu) doHungDu.getSelectedItem();
                    KhachHang kh = (KhachHang) chuSoHuu.getSelectedItem(); 

                    if (kh == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn chủ sở hữu (Khách hàng) cho thú cưng!");
                        return;
                    }

                    ThuCung tc = new ThuCung(ma, ten, loai, canNang, ghiChu, tt, doHD, kh);

                    if (tc_dao.themThuCung(tc)) {
                        model.addRow(new Object[] {
                            ma, 
                            ten, 
                            loai, 
                            canNang, 
                            ghiChu, 
                            tt, 
                            doHD, 
                            kh.getTenKH()
                        });
                        JOptionPane.showMessageDialog(this, "Thêm thú cưng thành công!");
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
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thú cưng cần sửa trên bảng!");
                return;
            }

            if (validData()) {
                int row = table.convertRowIndexToModel(viewRow);
                try {
                    String ma = txtMTC.getText().trim();
                    String ten = txtTenTC.getText().trim();
                    float canNang = Float.parseFloat(txtCanNang.getText().trim());
                    String ghiChu = txtGhiChu.getText().trim();

                    Loai loai = (Loai) loaiTC.getSelectedItem();
                    TrangThai tt = (TrangThai) trangThai.getSelectedItem();
                    DoHungDu doHD = (DoHungDu) doHungDu.getSelectedItem();
                    KhachHang kh = (KhachHang) chuSoHuu.getSelectedItem();

                    // Kiem tra: khach hang phai co maKH
                    if (kh == null || kh.getMaKH() == null || kh.getMaKH().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Chủ sở hữu (Khách hàng) chưa hợp lệ! Hãy kiểm tra lại danh sách khách hàng.", 
                            "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    ThuCung tc = new ThuCung(ma, ten, loai, canNang, ghiChu, tt, doHD, kh);

                    if (tc_dao.suaThuCung(tc)) {
                        // cap nhat tren model
                        model.setValueAt(ten, row, 1);
                        model.setValueAt(loai, row, 2);
                        model.setValueAt(canNang, row, 3);
                        model.setValueAt(ghiChu, row, 4);
                        model.setValueAt(tt, row, 5);
                        model.setValueAt(doHD, row, 6);
                        model.setValueAt(kh.getTenKH(), row, 7); 

                        JOptionPane.showMessageDialog(this, "Cập nhật thú cưng thành công!");
                        btnLamMoi.doClick();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại, không tìm thấy mã thú cưng!");
                    }
                    
                } catch (Exception ex) {
                    // Hiển thị chính xác LỖI TỪ SQL SERVER lên màn hình
                    ex.printStackTrace(); // In ra console để dev dễ nhìn
                    JOptionPane.showMessageDialog(this, 
                        "Lỗi cập nhật CSDL: \n" + ex.getMessage(), 
                        "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
		
		//xoa
		else if (o.equals(btnXoa)) {
			int viewRow = table.getSelectedRow();
			if (viewRow < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn thú cưng cần xóa!");
				return;
			}

			int row = table.convertRowIndexToModel(viewRow);
			String maTC = model.getValueAt(row, 0).toString();
			String tenTC = model.getValueAt(row, 1).toString();

			int confirm = JOptionPane.showConfirmDialog(this, 
				"Bạn có chắc muốn xóa khách hàng '" + tenTC + "'?", "Xác nhận", 
				JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try {
					if (tc_dao.xoaThuCung(maTC)) {
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
		    locDuLieu(); 
		}
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int row = table.convertRowIndexToModel(viewRow);
            
            txtTenTC.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
            txtCanNang.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
            txtGhiChu.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");

            if (model.getValueAt(row, 2) != null) {
                String loaiStr = model.getValueAt(row, 2).toString();
                for (Loai l : Loai.values()) {
                    if (l.toString().equals(loaiStr) || l.name().equals(loaiStr)) {
                        loaiTC.setSelectedItem(l);
                        break;
                    }
                }
            }

            try {
                Object objTrangThai = model.getValueAt(row, 5); 
                if (objTrangThai instanceof entity.TrangThai) {
                    trangThai.setSelectedItem(objTrangThai);
                } else if (objTrangThai != null) {
                    String trangThaiStr = objTrangThai.toString();
                    for (int i = 0; i < trangThai.getItemCount(); i++) {
                        entity.TrangThai item = trangThai.getItemAt(i);
                        if (item.name().equals(trangThaiStr) || item.toString().equals(trangThaiStr)) {
                            trangThai.setSelectedItem(item);
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (model.getValueAt(row, 6) != null) {
                String hdStr = model.getValueAt(row, 6).toString();
                for (DoHungDu hd : DoHungDu.values()) {
                    if (hd.toString().equals(hdStr) || hd.name().equals(hdStr)) {
                        doHungDu.setSelectedItem(hd);
                        break;
                    }
                }
            }

            if (model.getValueAt(row, 7) != null) {
                String tenKH = model.getValueAt(row, 7).toString();
                for (int i = 0; i < chuSoHuu.getItemCount(); i++) {
                    KhachHang kh = chuSoHuu.getItemAt(i);
                    if (kh != null && kh.getTenKH().equals(tenKH)) {
                        chuSoHuu.setSelectedIndex(i);
                        break;
                    }
                }
            }

            txtMTC.setText(model.getValueAt(row, 0) != null ? model.getValueAt(row, 0).toString() : "");
            txtMTC.setEditable(false);
        }
    }
    
    private void loadComboBoxKhachHang() {
        chuSoHuu.removeAllItems();
        ArrayList<KhachHang> dsKH = kh_dao.getTableKhachHang(); // Đổi tên hàm theo đúng tên hàm lấy DS trong KhachHangDAO của bạn
        if (dsKH != null) {
            for (KhachHang kh : dsKH) {
                chuSoHuu.addItem(kh);
            }
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