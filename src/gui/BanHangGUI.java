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

import com.formdev.flatlaf.FlatLightLaf;
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
	private KhachHang khachHangHienTai;
	public BanHangGUI() {
		//Dùng Panel để chuyển trang
		FlatLightLaf.setup();
		sp_dao = new SanPhamDAO();
		kh_dao = new KhachHangDAO();
		this.setLayout(new BorderLayout());
		
		
		
		//Phần nút chuyển chế độ sp/dịch vụ và thông tin khách hàng
		JPanel pnlTop = new JPanel(new BorderLayout());
		
		JPanel pnlButtonMode = new JPanel();
		btnProduct = new JButton("Hóa đơn Sản phẩm");
		btnService = new JButton("Hóa đơn Dịch vụ");
		pnlButtonMode.add(btnProduct);
		pnlButtonMode.add(btnService);
		
		//KHU VUC THONG TIN KHACH HANG
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
		lblNameDetail = new JLabel("Vãng Lai");
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
		pnlTop.setBorder(BorderFactory.createLineBorder(new Color(220,220,220),1));
		
		this.add(pnlTop, BorderLayout.NORTH);
		
		//Phần màn hình
		cardLayout = new CardLayout();
		pnlMain = new JPanel(cardLayout);
		
		//Panel HDSP
		 JPanel pnlProduct = new JPanel(new BorderLayout());
	     
	     JPanel pnlHeaderProduct = new JPanel(new BorderLayout());
	     
	   
	     JPanel pnlTitle = new JPanel();
	     pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.Y_AXIS));
	 

	     JLabel lblTitle = new JLabel("Danh sách sản phẩm");
	     lblTitle.setFont(new Font("Arial", Font.BOLD, 16));



	     pnlTitle.add(lblTitle);
	     pnlTitle.add(Box.createVerticalStrut(5));

	     
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
	    	
	     tblSanPham = new JTable(model);
	     JScrollPane scrollPane = new JScrollPane(tblSanPham);
//	     scrollPane.setBorder(BorderFactory.createEmptyBorder());
	     
	     //Chi tiet Thanh toan
	     JPanel pnlPayment = new JPanel();
	     pnlPayment.setLayout(new BoxLayout(pnlPayment, BoxLayout.Y_AXIS));
	     
	     JPanel pnlPaymentHeader = new JPanel(new BorderLayout());
	     
	    
	     JLabel lblPaymentTitle = new JLabel("Chi tiết thanh toán");
	     lblPaymentTitle.setFont(new Font("Arial", Font.BOLD, 16));
	 
	     pnlPaymentHeader.add(lblPaymentTitle, BorderLayout.WEST);
	     
	     //Thong tin thanh toan
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
	     
	     //Điểm tích lũy
	     Box boxPoints = Box.createHorizontalBox();

	     JPanel pnlPointText = new JPanel();
	     pnlPointText.setLayout(new BoxLayout(pnlPointText, BoxLayout.Y_AXIS));

	     JLabel lblPoints = new JLabel("Dùng điểm tích lũy");
	     lblPoints.setFont(new Font("Arial", Font.BOLD, 12));

	     JLabel lblPointCount = new JLabel("Hiện có 0 điểm");
	     lblPointCount.setFont(new Font("Arial", Font.PLAIN, 11));
	     lblPointCount.setForeground(Color.GRAY);

	     pnlPointText.add(lblPoints);
	     pnlPointText.add(Box.createVerticalStrut(5));
	     pnlPointText.add(lblPointCount);

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
	     
	     pnlProduct.add(pnlProductMainContent);
	     pnlProduct.add(pnlPayment, BorderLayout.EAST);
	     
	     
	     
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
	    pnlMain.add(pnlProduct,"SAN_PHAM");
	    pnlMain.add(pnlService,"DICH_VU");
		this.add(pnlMain);
		
		txtSearchProduct.addActionListener(this);
		btnProduct.addActionListener(this);
		btnService.addActionListener(this);
		txtSearch.addActionListener(this);
		chkPoints.addActionListener(this);
		btnXoa.addActionListener(this);
		btnConfirm.addActionListener(this);
	}
	
	// Tùy chỉnh bo góc các component
			public void setRoundBorder(JComponent cmp, int arc) {
				cmp.setBorder(new FlatLineBorder(new Insets(5, 25, 5, 25), new Color(200, 200, 200), 1, arc));
			}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == btnProduct) {
			cardLayout.show(pnlMain,"SAN_PHAM");
		}
		else if(o == btnService)
		{
			cardLayout.show(pnlMain,"DICH_VU");
		}
		
	}
		
	
	
}
