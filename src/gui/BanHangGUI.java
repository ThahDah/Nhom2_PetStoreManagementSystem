package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.ui.FlatLineBorder;

import java.sql.SQLException;
import java.text.DecimalFormat;

import dao.SanPhamDAO;
import entity.SanPham;
import dao.KhachHangDAO;
import entity.KhachHang;


public class BanHangGUI extends JPanel implements ActionListener {
	
	private static final int GIA_TRI_MOT_DIEM = 1;
	private JComponent txtTimKiemSP;
	private JButton btnSelected = null;
	private JButton btnProduct;
	private JButton btnService;
	private CardLayout cardLayout;
	
	
	private JPanel pnlMain;
	private DefaultTableModel model;
	private JTable tblSanPham;
	private JLabel lblAmountDetail;
	private JLabel lblTempDetail;
	private JButton btnConfirm;
	private JTextField txtTienNhan;
	private JTextField txtTienThua;
	private DefaultTableModel modelService;
	private JTable tblService;
	private JLabel lblServiceTempDetail;
	private JLabel lblServiceAmountDetail;
	private JTextField txtServiceTienNhan;
	private JTextField txtServiceTienThua;
	private JButton btnServiceConfirm;
	private JTextField txtSearchProduct;
	private SanPhamDAO sp_dao ;
	private JLabel lblTotalMoney;
	private JLabel lblNameDetail;
	private JLabel lblPointDetail;
	private JTextField txtSearch;
	private KhachHangDAO kh_dao;
	private JCheckBox chkPoints;
	private int diemTichLuyKhachHang = 0;
	private double tongTienHang = 0;
	private JButton btnXoa;

	public BanHangGUI() {
		//Dùng Panel để chuyển trang
		
		sp_dao = new SanPhamDAO();
		kh_dao = new KhachHangDAO();
		this.setLayout(new BorderLayout());
		
		//Phan nay can viet dao
		
		
		//Tao con tro thanh ban tay
		Cursor pointer = new Cursor(Cursor.HAND_CURSOR);
		
		//Phần nút chuyển chế độ sp/dịch vụ và thông tin khách hàng
		JPanel pnlTop = new JPanel(new BorderLayout());
		
		JPanel pnlButtonMode = new JPanel();
		btnProduct = new JButton("Hóa đơn Sản phẩm");
		btnService = new JButton("Hóa đơn Dịch vụ");
		pnlButtonMode.add(btnProduct);
		pnlButtonMode.add(btnService);
		
		JPanel pnlCustomerInfo = new JPanel();
		Box boxCustomerInfo = Box.createVerticalBox();


		Box boxSearchCustomerByID = Box.createHorizontalBox();

		JLabel lblSearch = new JLabel("SĐT Khách Hàng:");
		txtSearch = new JTextField(20);

		boxSearchCustomerByID.add(lblSearch);
		boxSearchCustomerByID.add(Box.createHorizontalStrut(10));
		boxSearchCustomerByID.add(txtSearch);

		Box boxCustomerDetail = Box.createHorizontalBox();
		JLabel lblName = new JLabel("Tên Khách hàng:");
		lblNameDetail = new JLabel("");
		JLabel lblPoint = new JLabel("Điểm tích lũy:");
		lblPointDetail = new JLabel("0");
		
		boxCustomerDetail.add(lblName);
		boxCustomerDetail.add(Box.createHorizontalStrut(5));
		boxCustomerDetail.add(lblNameDetail);
		boxCustomerDetail.add(Box.createHorizontalStrut(20));
		boxCustomerDetail.add(lblPoint);
		boxCustomerDetail.add(Box.createHorizontalStrut(5));
		boxCustomerDetail.add(lblPointDetail);
		
		boxCustomerInfo.add(boxSearchCustomerByID);
		boxCustomerInfo.add(Box.createVerticalStrut(10));
		boxCustomerInfo.add(boxCustomerDetail);
		
		pnlCustomerInfo.add(boxCustomerInfo);
		
		pnlTop.add(pnlButtonMode, BorderLayout.WEST);
		pnlTop.add(pnlCustomerInfo, BorderLayout.EAST);
		
		this.add(pnlTop, BorderLayout.NORTH);
		
		//Phần màn hình
		cardLayout = new CardLayout();
		pnlMain = new JPanel(cardLayout);
		
		//Panel HDSP
		 JPanel pnlSanPham = new JPanel(new BorderLayout());
	     
	     JPanel pnlHeaderProduct = new JPanel(new BorderLayout());
	     
	   
	     JPanel pnlTitle = new JPanel();
	     pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.Y_AXIS));
	 

	     JLabel lblTitle = new JLabel("Danh sách sản phẩm");
	     lblTitle.setFont(new Font("Arial", Font.BOLD, 16));

//	     lblCount = new JLabel("2 mặt hàng đã chọn");
//	     lblCount.setFont(new Font("Arial", Font.PLAIN, 12));
//	     lblCount.setForeground(Color.GRAY);

	     pnlTitle.add(lblTitle);
	     pnlTitle.add(Box.createVerticalStrut(5));
//	     pnlTitle.add(lblCount);
	     
	     pnlHeaderProduct.add(pnlTitle, BorderLayout.WEST);
	     
	     //Phan thanh tim kiem
	     JPanel pnlSearchProduct = new JPanel(new BorderLayout());
	     pnlSearchProduct.setBorder(new EmptyBorder(8, 15, 15, 15));

	     JLabel lblSearchProduct = new JLabel("Nhập ID sản phẩm");
	     lblSearchProduct.setFont(new Font("Arial", Font.PLAIN, 13));

	     txtSearchProduct = new JTextField(8);
	     
	     //nut xoa tam thoi
	     btnXoa = new JButton("Xóa sản phẩm");
	     pnlSearchProduct.add(lblSearchProduct, BorderLayout.WEST);
	     pnlSearchProduct.add(txtSearchProduct, BorderLayout.CENTER);
	     pnlSearchProduct.add(btnXoa, BorderLayout.EAST);
	    

	        // Gộp Header và Search
	     JPanel pnlProductHeader = new JPanel();
	     pnlProductHeader.setLayout(new BorderLayout());
	     pnlProductHeader.setBackground(Color.WHITE);

	     pnlProductHeader.add(pnlHeaderProduct, BorderLayout.NORTH);
	     pnlProductHeader.add(pnlSearchProduct, BorderLayout.SOUTH);
	     
	     //Bang
	     String[] columns = {
	             "Sản phẩm",
	             "Mã sản phẩm",
	             "Số lượng",
	             "Đơn giá",
	             "Thành tiền"
	         };

	     model = new DefaultTableModel(columns, 0) {
	    	    @Override
	    	    public boolean isCellEditable(int row, int column) {
	    	        // Chỉ cho phép chỉnh sửa cột Số lượng
	    	        return column == 2;
	    	    }
	    	};
	    	model.addTableModelListener(e -> {

	    	    if (e.getType() == TableModelEvent.UPDATE
	    	            && e.getColumn() == 2) {

	    	        int row = e.getFirstRow();

	    	        if (row < 0) {
	    	            return;
	    	        }

	    	        try {
	    	            String giaTri = model.getValueAt(row, 2).toString().trim();

	    	            int soLuong = Integer.parseInt(giaTri);

	    	            if (soLuong <= 0) {
	    	                JOptionPane.showMessageDialog(
	    	                    this,
	    	                    "Số lượng phải lớn hơn 0!",
	    	                    "Thông báo",
	    	                    JOptionPane.WARNING_MESSAGE
	    	                );

	    	                model.setValueAt(1, row, 2);
	    	                return;
	    	            }

	    	            double donGia = Double.parseDouble(
	    	                model.getValueAt(row, 3).toString()
	    	            );

	    	          
	    	            model.setValueAt(soLuong * donGia, row, 4);

	    	            
	    	            capNhatThongTinThanhToan();

	    	        } catch (NumberFormatException ex) {

	    	            JOptionPane.showMessageDialog(
	    	                this,
	    	                "Vui lòng nhập số lượng là số nguyên hợp lệ!",
	    	                "Thông báo",
	    	                JOptionPane.WARNING_MESSAGE
	    	            );

	    	            model.setValueAt(1, row, 2);
	    	        }
	    	    }
	    	});
	     tblSanPham = new JTable(model);
	     JScrollPane scrollPane = new JScrollPane(tblSanPham);
	     scrollPane.setBorder(BorderFactory.createEmptyBorder());
	     
	     //Chi tiet Thanh toan
	     JPanel pnlPayment = new JPanel();
	     pnlPayment.setLayout(new BoxLayout(pnlPayment, BoxLayout.Y_AXIS));
	     
	     JPanel pnlPaymentHeader = new JPanel(new BorderLayout());
	     
	    
	     JLabel lblPaymentTitle = new JLabel("Chi tiết thanh toán");
	     lblPaymentTitle.setFont(new Font("Arial", Font.BOLD, 16));
	 
	     pnlPaymentHeader.add(lblPaymentTitle, BorderLayout.WEST);
	     
	     //Thong tin thanh toan(Can sua lại)
	     JPanel pnlInfo = new JPanel();
	     pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
	     
	     Box boxAmount = Box.createHorizontalBox();
	     JLabel lblAmount = new JLabel("Tổng số lượng: ");
	     lblAmountDetail = new JLabel("");
	     boxAmount.add(lblAmount);
	     boxAmount.add(Box.createHorizontalGlue());
	     boxAmount.add(lblAmountDetail);
	     pnlInfo.add(boxAmount);
	     pnlInfo.add(Box.createVerticalStrut(15));
	     
	     Box boxTemp = Box.createHorizontalBox();
	     JLabel lblTemp = new JLabel("Tạm tính: ");
	     lblTempDetail = new JLabel("");
	     boxTemp.add(lblTemp);
	     boxTemp.add(Box.createHorizontalGlue());
	     boxTemp.add(lblTempDetail);
	     pnlInfo.add(boxTemp);
	     pnlInfo.add(Box.createVerticalStrut(15));
	     
	     //Diểm tích lũy
	     Box boxPoints = Box.createHorizontalBox();

	     JPanel pnlPointText = new JPanel();
	     pnlPointText.setLayout(new BoxLayout(pnlPointText, BoxLayout.Y_AXIS));

	     JLabel lblPoints = new JLabel("Dùng điểm tích lũy");
	     lblPoints.setFont(new Font("Arial", Font.BOLD, 12));

//	     JLabel lblPointCount = new JLabel("Hiện có 0 điểm");
//	     lblPointCount.setFont(new Font("Arial", Font.PLAIN, 11));
//	     lblPointCount.setForeground(Color.GRAY);

	     pnlPointText.add(lblPoints);
	     pnlPointText.add(Box.createVerticalStrut(5));
//	     pnlPointText.add(lblPointCount);

	      chkPoints = new JCheckBox();
	      chkPoints.setEnabled(false);

	     boxPoints.add(pnlPointText);
	     boxPoints.add(Box.createHorizontalGlue());
	     boxPoints.add(chkPoints);
	     
	     //Tổng thanh toán
	     JPanel pnlTotal = new JPanel(new BorderLayout());


	     JLabel lblTotal = new JLabel("Tổng thanh toán");
	     lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

	     lblTotalMoney = new JLabel("");
	     lblTotalMoney.setFont(new Font("Arial", Font.BOLD, 21));

	     pnlTotal.add(lblTotal, BorderLayout.WEST);
	     pnlTotal.add(lblTotalMoney, BorderLayout.EAST);
	     
	     //Tiền thừa
	     JPanel pnlCash = new JPanel(new GridLayout(2, 2, 10, 8));
	     
	     JLabel lblTienNhan = new JLabel("Tiền nhận:"); 
	     JLabel lblTienThua = new JLabel("Tiền thừa:");
	     txtTienNhan = new JTextField();
	 
			
	     txtTienThua = new JTextField();
	     txtTienThua.setEditable(false);
	     txtTienThua.setForeground(Color.RED);
			
	     pnlCash.add(lblTienNhan); pnlCash.add(txtTienNhan);
	     pnlCash.add(lblTienThua); pnlCash.add(txtTienThua);
	     
		
	     //Nút xác nhận
	     btnConfirm = new JButton("Xác nhận & tạo hóa đơn");
	     
	     pnlPayment.add(pnlPaymentHeader);
	     pnlPayment.add(new JSeparator());
	     pnlPayment.add(pnlInfo);
	     pnlPayment.add(boxPoints);
	     pnlPayment.add(Box.createVerticalStrut(15));
	     pnlPayment.add(new JSeparator());
	     pnlPayment.add(pnlTotal);
	     pnlPayment.add(new JSeparator());
	     pnlPayment.add(Box.createVerticalStrut(15));
	     pnlPayment.add(pnlCash);
	     pnlPayment.add(Box.createVerticalStrut(15));
	     pnlPayment.add(btnConfirm);
	     
	     JPanel pnlProductMainContent = new JPanel(new BorderLayout());
	     pnlProductMainContent.add(pnlProductHeader, BorderLayout.NORTH);
	     pnlProductMainContent.add(scrollPane);
	     pnlProductMainContent.setBorder(
	    		    BorderFactory.createMatteBorder(
	    		        0, 1, 1, 1,
	    		        new Color(200, 200, 200)
	    		    )
	    		);
	     
	     pnlSanPham.add(pnlProductMainContent);
	     pnlSanPham.add(pnlPayment, BorderLayout.EAST);
	     
	     
	     
	    //PANEL DICH VU
	     JPanel pnlService = new JPanel(new BorderLayout());
	     pnlService.setBackground(Color.LIGHT_GRAY);
	     JPanel pnlServiceHeader = new JPanel(new BorderLayout());
	        
	     JLabel lblServiceTitle = new JLabel("Dịch vụ trong Lulu");
	     lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
	     
	     pnlServiceHeader.add(lblServiceTitle, BorderLayout.WEST);
	     
	     String[] columnService = {
	             "Mã dịch vụ",
	             "Dịch vụ ",
	             "Thực hiện",
	             "Đơn giá"

	         };

	         modelService = new DefaultTableModel(columnService, 0) {
	             @Override
	             public boolean isCellEditable(int row, int column) {
	                 return false;
	             }
	         };
	     
	     tblService = new JTable(modelService);
	     JScrollPane serviceScrollPane = new JScrollPane(tblService);
	     scrollPane.setBorder(BorderFactory.createEmptyBorder());
	     
	     JPanel pnlServiceMain = new JPanel(new BorderLayout());
	     pnlServiceMain.add(pnlServiceHeader, BorderLayout.NORTH);
	     pnlServiceMain.add(serviceScrollPane);
	     
	     
	     //CHI TIET THANH TOAN DICH VU
	     JPanel pnlServicePayment = new JPanel();
	     pnlServicePayment.setLayout(new BoxLayout(pnlServicePayment, BoxLayout.Y_AXIS));
	     
	     JPanel pnlServicePaymentHeader = new JPanel(new BorderLayout());
	     
	    
	     JLabel lblServicePaymentTitle = new JLabel("Chi tiết thanh toán");
	     lblServicePaymentTitle.setFont(new Font("Arial", Font.BOLD, 16));
	 
	     pnlPaymentHeader.add(lblServicePaymentTitle, BorderLayout.WEST);
	     
	     //Thong tin thanh toan(Can sua lại)
	     JPanel pnlServiceInfo = new JPanel();
	     pnlServiceInfo.setLayout(new BoxLayout(pnlServiceInfo, BoxLayout.Y_AXIS));
	     
	     Box boxServiceAmount = Box.createHorizontalBox();
	     JLabel lblServiceAmount = new JLabel("Số dịch vụ: ");
	     lblServiceAmountDetail = new JLabel("3");
	     boxServiceAmount.add(lblServiceAmount);
	     boxServiceAmount.add(Box.createHorizontalGlue());
	     boxServiceAmount.add(lblServiceAmountDetail);
	     pnlServiceInfo.add(boxServiceAmount);
	     pnlServiceInfo.add(Box.createVerticalStrut(15));
	     
	     Box boxServiceTemp = Box.createHorizontalBox();
	     JLabel lblServiceTemp = new JLabel("Tạm tính: ");
	     lblServiceTempDetail = new JLabel("140.000đ");
	     boxServiceTemp.add(lblServiceTemp);
	     boxServiceTemp.add(Box.createHorizontalGlue());
	     boxServiceTemp.add(lblServiceTempDetail);
	     pnlServiceInfo.add(boxServiceTemp);
	     pnlServiceInfo.add(Box.createVerticalStrut(15));
	     
	     //Diểm tích lũy
	     Box boxServicePoints = Box.createHorizontalBox();

	     JPanel pnlServicePointText = new JPanel();
	     pnlServicePointText.setLayout(new BoxLayout(pnlServicePointText, BoxLayout.Y_AXIS));

	     JLabel lblServicePoints = new JLabel("Dùng điểm tích lũy");
	     lblServicePoints.setFont(new Font("Arial", Font.BOLD, 12));

	     JLabel lblSevicePointCount = new JLabel("Hiện có 0 điểm");
	     lblSevicePointCount.setFont(new Font("Arial", Font.PLAIN, 11));
	     lblSevicePointCount.setForeground(Color.GRAY);

	     pnlServicePointText.add(lblServicePoints);
	     pnlServicePointText.add(Box.createVerticalStrut(5));
	     pnlServicePointText.add(lblSevicePointCount);

	     JCheckBox chkServicePoints = new JCheckBox();
	     

	     boxServicePoints.add(pnlServicePointText);
	     boxServicePoints.add(Box.createHorizontalGlue());
	     boxServicePoints.add(chkServicePoints);
	     
	     //Tổng thanh toán
	     JPanel pnlServiceTotal = new JPanel(new BorderLayout());


	     JLabel lblServiceTotal = new JLabel("Tổng thanh toán");
	     lblServiceTotal.setFont(new Font("Arial", Font.BOLD, 14));

	     JLabel lblServiceTotalMoney = new JLabel("120.000đ");
	     lblServiceTotalMoney.setFont(new Font("Arial", Font.BOLD, 21));

	     pnlServiceTotal.add(lblServiceTotal, BorderLayout.WEST);
	     pnlServiceTotal.add(lblServiceTotalMoney, BorderLayout.EAST);
	     
	     //Tiền thừa
	     JPanel pnlServiceCash = new JPanel(new GridLayout(2, 2, 10, 8));
	     
	     JLabel lblServiceTienNhan = new JLabel("Tiền nhận:"); 
	     JLabel lblServiceTienThua = new JLabel("Tiền thừa:");
	     txtServiceTienNhan = new JTextField();
	 
			
	     txtServiceTienThua = new JTextField();
	     txtServiceTienThua.setEditable(false);
	     txtServiceTienThua.setForeground(Color.RED);
			
	     pnlServiceCash.add(lblServiceTienNhan); pnlServiceCash.add(txtServiceTienNhan);
	     pnlServiceCash.add(lblServiceTienThua); pnlServiceCash.add(txtServiceTienThua);
	     
		
	     //Nút xác nhận
	     btnServiceConfirm = new JButton("Xác nhận & tạo hóa đơn");
	     
	     pnlServicePayment.add(pnlServicePaymentHeader);
	     pnlServicePayment.add(new JSeparator());
	     pnlServicePayment.add(pnlServiceInfo);
	     pnlServicePayment.add(boxServicePoints);
	     pnlServicePayment.add(Box.createVerticalStrut(15));
	     pnlServicePayment.add(new JSeparator());
	     pnlServicePayment.add(pnlServiceTotal);
	     pnlServicePayment.add(new JSeparator());
	     pnlServicePayment.add(Box.createVerticalStrut(15));
	     pnlServicePayment.add(pnlServiceCash);
	     pnlServicePayment.add(Box.createVerticalStrut(15));
	     pnlServicePayment.add(btnServiceConfirm);
	     
	     pnlService.add(pnlServiceMain);
	     pnlServiceMain.setBorder(
	    		    BorderFactory.createMatteBorder(
	    		        0, 1, 1, 1,
	    		        new Color(200, 200, 200)
	    		    )
	    		);
	     pnlService.add(pnlServicePayment, BorderLayout.EAST);
	     
	    //Them 2 pnl SP va DV vo Main
	    pnlMain.add(pnlSanPham,"SAN_PHAM");
	    pnlMain.add(pnlService,"DICH_VU");
		this.add(pnlMain);
		
		txtSearchProduct.addActionListener(this);
		btnProduct.addActionListener(this);
		btnService.addActionListener(this);
		txtSearch.addActionListener(this);
		chkPoints.addActionListener(this);
		 btnXoa.addActionListener(this);
	}
	
	private void updateButtonColor(JButton clickedButton) {
	    //Reset màu cho nút đã chọn trước đó 
		
	    if (btnSelected  != null) {
	        btnSelected.setOpaque(false);
	        btnSelected.setBackground(Color.white);
	    }

	    //Thiết lập màu cho nút vừa click
	    clickedButton.setOpaque(true); 
	    clickedButton.setBackground(new Color(173, 255, 47)); 
	    
	    // Cập nhật nút hiện tại vào biến tạm
	    btnSelected = clickedButton;
	    
	    // Vẽ lại nút đang chọn
	    clickedButton.repaint();
	}
	
	// Tùy chỉnh bo góc các component
			public void setRoundBorder(JComponent cmp, int arc) {
				cmp.setBorder(new FlatLineBorder(new Insets(5, 25, 5, 25), new Color(200, 200, 200), 1, arc));
			}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == btnProduct) {
			cardLayout.show(pnlMain, "SAN_PHAM");
		}
		else if(o == btnService) {
			cardLayout.show(pnlMain, "DICH_VU");
		}
		else if(o == txtSearchProduct) {
			  String maSP = txtSearchProduct.getText();

			    if (!maSP.isEmpty()) {
			        try {
						themSanPhamVaoBang(maSP);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			    }
		}else if (o == txtSearch) {
		    String sdt = txtSearch.getText().trim();

		    if (sdt.isEmpty()) {
		        JOptionPane.showMessageDialog(
		            this,
		            "Vui lòng nhập số điện thoại khách hàng!",
		            "Thông báo",
		            JOptionPane.WARNING_MESSAGE
		        );
		        return;
		    }

		    KhachHang kh = kh_dao.timKiemKhachHangTheoSDT(sdt);

		    if (kh != null) {
		        lblNameDetail.setText(kh.getTenKH());
		        lblPointDetail.setText(String.valueOf(kh.getDiemTichLuy()));

		        // Lưu điểm tích lũy của khách hàng
		        diemTichLuyKhachHang = kh.getDiemTichLuy();

		        // Cho phép sử dụng điểm
		        chkPoints.setEnabled(diemTichLuyKhachHang > 0);
		        chkPoints.setSelected(false);

		        capNhatThongTinThanhToan();

		        

		    } else {
		        lblNameDetail.setText("Unknown");
		        lblPointDetail.setText("0");

		        diemTichLuyKhachHang = 0;

		        chkPoints.setSelected(false);
		        chkPoints.setEnabled(false);

		        capNhatThongTinThanhToan();

		        JOptionPane.showMessageDialog(
		            this,
		            "Không tìm thấy khách hàng với số điện thoại: " + sdt,
		            "Thông báo",
		            JOptionPane.WARNING_MESSAGE
		        );
		    }
		}
		else if (o == chkPoints) {
		    capNhatThongTinThanhToan();
		} else if (o == btnXoa) {
		    int selectedRow = tblSanPham.getSelectedRow();

		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(this,
		                "Vui lòng chọn sản phẩm cần xóa!");
		        return;
		    }

		
		    int modelRow = tblSanPham.convertRowIndexToModel(selectedRow);

		    model.removeRow(modelRow);

		    capNhatThongTinThanhToan();
		}
}
			

	private void themSanPhamVaoBang(String maSP) throws SQLException {
	    // Tìm sản phẩm theo mã trong database
		SanPham sp = sp_dao.timTheoMa(maSP);

		if (sp != null) {

		 
		    boolean daTonTai = false;

		    for (int i = 0; i < model.getRowCount(); i++) {
		        String maSPTrongBang = model.getValueAt(i, 1).toString();

		        if (maSPTrongBang.equals(sp.getMaSP())) {
		            int soLuong = Integer.parseInt(
		                model.getValueAt(i, 2).toString()
		            );

		           
		            model.setValueAt(soLuong + 1, i, 2);

		     
		            model.setValueAt(
		                sp.getGiaNY() * (soLuong + 1), i, 4
		            );

		            daTonTai = true;
		            break;
		        }
		    }

		  
		    if (!daTonTai) {
		        Object[] row = {
		            sp.getTenSP(),
		            sp.getMaSP(),
		            1,
		            sp.getGiaNY(),
		            sp.getGiaNY()
		        };

		        model.addRow(row);
		    }

		    capNhatThongTinThanhToan();

		    txtSearchProduct.setText("");

		} else {
		    JOptionPane.showMessageDialog(this,
		        "Không tìm thấy sản phẩm có mã: " + maSP);
		}
	}
	
	// cap nhat thong tin thanh toan
	private void capNhatThongTinThanhToan() {
	    int tongSoLuong = 0;
	    double tongTien = 0;

	    // Tính tổng tiền hàng từ bảng sản phẩm
	    for (int i = 0; i < model.getRowCount(); i++) {

	        int soLuong = Integer.parseInt(
	            model.getValueAt(i, 2).toString()
	        );

	        double donGia = Double.parseDouble(
	            model.getValueAt(i, 3).toString()
	        );

	        tongSoLuong += soLuong;
	        tongTien += soLuong * donGia;
	    }

	    // Lưu tổng tiền hàng trước khi giảm
	    tongTienHang = tongTien;

	    // Tính tiền giảm từ điểm tích lũy
	    double tienGiam = 0;

	    if (chkPoints != null && chkPoints.isSelected()) {

	        tienGiam = Math.min(
	            diemTichLuyKhachHang * GIA_TRI_MOT_DIEM,
	            tongTienHang
	        );
	    }

	    // Tổng tiền sau khi giảm
	    double tongThanhToan = tongTienHang - tienGiam;

	    DecimalFormat df = new DecimalFormat("#,##0");

	    // Cập nhật giao diện
	    lblAmountDetail.setText(String.valueOf(tongSoLuong));

	    lblTempDetail.setText(
	        df.format(tongTienHang) + "đ"
	    );

	    lblTotalMoney.setText(
	        df.format(tongThanhToan) + "đ"
	    );
	}
}
