package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class ThuCung_GUI extends JFrame implements ActionListener, MouseListener {
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

    public ThuCung_GUI() {
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 

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
        trangThai = new JComboBox<TrangThai>(TrangThai.values());
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

  		
  		JLabel lblSDTKH=new JLabel("Nhập mã thú cưng:");
  		txtTimMTC=new JTextField(10);
  		pTimKH.add(lblSDTKH); pTimKH.add(txtTimMTC);
  		
  		
  		btnTimKiem = new JButton("Tìm kiếm");
  		pTimKH.add(btnTimKiem);
  		
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
  		
  		
  		this.add(c);

        //ket noi database
         try {
             connectDB.ConnectDB.getInstance().connect();
             System.out.println("Kết nối cơ sở dữ liệu thành công!");
             DocDuLieuVaoTable();
         } catch (java.sql.SQLException e) {
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Không thể kết nối tới CSDL!\n" + e.getMessage());
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
			new ThuCung_GUI().setVisible(true);
		});
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
 	
 	//Phat sinh ma: TC1 TC2
	private String phatSinhMaTC() {
		ArrayList<ThuCung> listTC = tc_dao.getTableThuCung();
		if (listTC.isEmpty()) {
			return "TC1";
		}
		int max = 0;
		for (ThuCung tc : listTC) {
			String ma = tc.getMaThuCung();
			try {
				int so = Integer.parseInt(ma.substring(2)); // Cat chu TC, lay so
				if (so > max) {
					max = so;
				}
			} catch (Exception e) {
				// Bỏ qua lỗi parse
			}
		}
		return String.format("TC%d", max + 1);
	}
	
	// loc du lieu tren table, btn tim
	private void locDuLieu() {
		try {
			ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();
			
			String maTC = txtTimMTC.getText().trim();
			if (!maTC.isEmpty()) {
				filters.add(RowFilter.regexFilter("(?i)" + maTC, 0)); // Cột 0: Mã KH
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
            txtMTC.setText(phatSinhMaTC()); 
            txtTenTC.setText("");
            txtTenTC.setEditable(true);
            txtCanNang.setText("");
            txtGhiChu.setText("");
        
            if (loaiTC.getItemCount() > 0) loaiTC.setSelectedIndex(0);
            if (trangThai.getItemCount() > 0) trangThai.setSelectedIndex(0);
            if (doHungDu.getItemCount() > 0) doHungDu.setSelectedIndex(0);
            if (chuSoHuu.getItemCount() > 0) chuSoHuu.setSelectedIndex(0);
            
            txtTimMTC.setText("");
            
            if (rowSorter != null) {
                rowSorter.setRowFilter(null);
            }
            table.clearSelection();
            
            txtTenTC.requestFocus();
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

                    if (kh == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn chủ sở hữu (Khách hàng)!");
                        return;
                    }

                    ThuCung tc = new ThuCung(ma, ten, loai, canNang, ghiChu, tt, doHD, kh);

                    if (tc_dao.suaThuCung(tc)) {
                        model.setValueAt(ten, row, 1);
                        model.setValueAt(loai, row, 2);
                        model.setValueAt(canNang, row, 3);
                        model.setValueAt(ghiChu, row, 4);
                        model.setValueAt(tt, row, 5);
                        model.setValueAt(doHD, row, 6);
                        model.setValueAt(kh.getTenKH(), row, 7);

                        JOptionPane.showMessageDialog(this, "Cập nhật thú cưng thành công!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            
            txtMTC.setText(model.getValueAt(row, 0) != null ? model.getValueAt(row, 0).toString() : "");
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

            if (model.getValueAt(row, 5) != null) {
                String ttStr = model.getValueAt(row, 5).toString();
                for (TrangThai tt : TrangThai.values()) {
                    if (tt.toString().equals(ttStr) || tt.name().equals(ttStr)) {
                        trangThai.setSelectedItem(tt);
                        break;
                    }
                }
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

            txtMTC.setEditable(false);
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