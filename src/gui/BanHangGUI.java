package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import com.formdev.flatlaf.icons.FlatSearchIcon;

import common.DinhDang;
import common.ImageUtils;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.SanPhamDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.Loai;
import entity.NhanVien;
import entity.SanPham;
import entity.TrangThai;
import dao.LichHenDAO;
import entity.ChiTietLichHen;
import entity.DichVu;
import entity.ThuCung;

public class BanHangGUI extends JPanel implements ChangeListener, ActionListener {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private static final Color DARK_TEXT    = new Color(0x111827);
	  	private static final Color BORDER_COLOR = new Color(0xE5E7EB);
	    private static final Color GRAY_TEXT    = new Color(0x6B7280);
	    private static final Color ORANGE       = new Color(0xF26B3A);
	    private static final Color INFO_BG      = new Color(0xF3F4F6);
		
	    private JTextField txtPhone;
	    private JLabel lblName;
	    private JLabel lblPoints;
	    private KhachHang khachHienTai = null;

	    private JTabbedPane switchPane;
	    private ProductPanel pnlProduct;
	    private ServicePanel pnlService;
	    private SanPhamDAO sp_dao;
	    private KhachHangDAO kh_dao;
	    private NhanVien nhanVienDangNhap;

	   public BanHangGUI(NhanVien nhanVienDangNhap) {
		   this.nhanVienDangNhap = nhanVienDangNhap;  
		   setLayout(new BorderLayout());
           switchPane = new JTabbedPane();
           sp_dao = new SanPhamDAO();
           kh_dao = new KhachHangDAO();
            
            // ===== KHÁCH HÀNG DÙNG CHUNG CHO 2 TAB =====
           JPanel pnlCustomerBar = new JPanel(new BorderLayout(16, 0));
 		   pnlCustomerBar.setBorder(BorderFactory.createCompoundBorder(
 				   BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
 				   new EmptyBorder(6, 16, 6, 16)));
 		   
 		   // ---Tiêu đề---
 		   JLabel lblCustomerTitle = new JLabel("Khách hàng");
 		   lblCustomerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
 		   lblCustomerTitle.setForeground(DARK_TEXT);

 		   txtPhone = new JTextField(20);
 		   txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 15));
 		   txtPhone.setPreferredSize(new Dimension(200, 36));   // chiều cao cố định
 		   txtPhone.putClientProperty("JTextField.placeholderText", "Nhập số điện thoại rồi nhấn Enter");
 		   txtPhone.putClientProperty("JTextField.selectAllOnFocusPolicy", "always");
 		   txtPhone.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());

 		   // --- Ô nhập số điện thoại khách hàng---
 		   JPanel pnlPhone = new JPanel(new GridBagLayout());
 		   pnlPhone.setOpaque(false);
 		   GridBagConstraints gbc = new GridBagConstraints();
 		   gbc.fill = GridBagConstraints.HORIZONTAL;
 		   gbc.weightx = 1;
 		   pnlPhone.add(txtPhone, gbc);
 		   
 		   // --- Tên khách hàng và điểm tích lũy ---
 		   lblName = new JLabel("-");
 		   lblPoints = new JLabel("-");
 		   JPanel boxCustomer = new JPanel(new GridLayout(1, 2, 30, 0));
 		   boxCustomer.setBackground(INFO_BG);
 		   boxCustomer.setBorder(new EmptyBorder(2, 20, 0, 20));
 		   boxCustomer.setPreferredSize(new Dimension(380, 50));
 		   boxCustomer.putClientProperty("FlatLaf.style", "arc: 16");
 		   boxCustomer.add(createInfoColumn("Khách hàng", lblName, DARK_TEXT));
 		   boxCustomer.add(createInfoColumn("Điểm tích lũy", lblPoints, ORANGE));

 		   pnlCustomerBar.add(lblCustomerTitle, BorderLayout.WEST);
 		   pnlCustomerBar.add(pnlPhone, BorderLayout.CENTER);
 		   pnlCustomerBar.add(boxCustomer, BorderLayout.EAST);
 		  // ===== 2 TAB =====
            pnlProduct = new ProductPanel();
            pnlService = new ServicePanel();
            switchPane.addTab("Sản phẩm", pnlProduct);
            switchPane.addTab("Dịch vụ", pnlService);
            
            add(pnlCustomerBar, BorderLayout.NORTH);
            add(switchPane, BorderLayout.CENTER);
            
            txtPhone.addActionListener(this);
            switchPane.addChangeListener(this);
       }

	   @Override
	   public void stateChanged(ChangeEvent e) {
		   switchPane.getTitleAt(switchPane.getSelectedIndex());
	   }
	   
	   private void resetCustomer(boolean xoaSDT) {
		   khachHienTai = null;
		   lblName.setText("-");
		   lblPoints.setText("-");
		   if (xoaSDT) txtPhone.setText("");
		   pnlProduct.capNhatKhachHangProduct();
		   pnlService.updateServiceCustomer();
	   }
	   @Override
	   public void actionPerformed(ActionEvent e) {
		   Object o = e.getSource();

		   if (o == txtPhone) {
			   String sdt = txtPhone.getText().trim();
			   KhachHang kh = sdt.isEmpty() ? null : kh_dao.timKiemKhachHangTheoSDT(sdt);

			   if (kh == null) {
				   JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!",
						   "Lỗi", JOptionPane.ERROR_MESSAGE);
				   resetCustomer(false);
				   return;
			   }

			   khachHienTai = kh;
			   lblName.setText(kh.getTenKH());
			   lblPoints.setText(String.valueOf(kh.getDiemTichLuy()));
			   pnlProduct.capNhatKhachHangProduct();
			   pnlService.updateServiceCustomer();
		   }
	   }
	   
	   
	  // ========== TAB HÓA ĐƠN SẢN PHẨM ========== 
      private final class ProductPanel extends JPanel implements MouseListener, ActionListener, TableModelListener  {
		
    	  /**
		 * 
		 */
    	  private static final long serialVersionUID = 1L;
		  private DefaultTableModel model;
    	  private JTable tblProductList;
    	  
    	  private JLabel lblCountProduct;
    	  private JLabel lblTotalQty;
    	  private JLabel lblSubtotal ;
    	  private JLabel lblDiscount;
    	  private JLabel lblPointsAvail ;
    	  private JLabel lblGrandTotal;
    	  
    	  private JCheckBox chkUsePoints;
    	  
    	  private JTextField txtFindProduct;
    	  private JTextField txtReceived;
    	  private JTextField txtChange;
    	  
    	  private JButton btnConfirm;
    	  private JButton btnSaveProductInvoice;
    	  
    	  private long customerPoints = 0;
    	  private long discount = 0; 
    	  private static final long POINT_VALUE = 1;// giá trị của diểm tích lũy
    	  
    	  private HoaDonDAO hd_dao;
			
	    	public ProductPanel() {
	    		 setLayout(new BorderLayout());
	    		 
	    		 hd_dao = new HoaDonDAO();
	    		 
	    		 // --- Danh sách sản phẩm khách mua ---
	    		 JPanel pnlMainProduct = new JPanel(new BorderLayout(0, 12));
	    		 pnlMainProduct.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
	    		            new EmptyBorder(12, 16, 12, 16)));
	    		 add(pnlMainProduct);
	    		 	    		 
	    		 // --- Chi tiết hóa đơn ---
	    		 JPanel pnlProductPaymentDetail = new JPanel();
	    		 JScrollPane scrollPayment = new JScrollPane(pnlProductPaymentDetail,
	    					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
	    					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    		 scrollPayment.setBorder(null);
	    		 scrollPayment.setPreferredSize(new Dimension(320, 0));   // 300 chiều rộng + chỗ cho thanh cuộn
	    		 add(scrollPayment, BorderLayout.EAST);
	    		 
	    		 // ====== PHẦN DANH SÁCH SẢN PHẨM (pnlMainProduct) ======

	    	     // ---- KHU VỰC THÔNG TIN SẢN PHẨN ----
	    	     JPanel pnlProductList = new JPanel();
	    	     pnlProductList.setLayout(new BoxLayout(pnlProductList, BoxLayout.Y_AXIS));
	    	     pnlProductList.setOpaque(false);
	    	     pnlProductList.setAlignmentX(LEFT_ALIGNMENT);
	    	     
	    	     JLabel lblProductListTitle = new JLabel("Sản phẩm trong hóa đơn");
	    	     lblProductListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    	     lblProductListTitle.setForeground(DARK_TEXT);
	    	     lblProductListTitle.setAlignmentX(LEFT_ALIGNMENT);
	    	     
	    	     // --- Hiển thị số sản phẩm đang được chọn trong danh sách ---
	    	     lblCountProduct = new JLabel("0 mặt hàng đã chọn");
	    	     lblCountProduct.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    	     lblCountProduct.setForeground(GRAY_TEXT);
	    	     lblCountProduct.setAlignmentX(LEFT_ALIGNMENT);
	    	     
	    	     txtFindProduct = new JTextField();
	    	     txtFindProduct.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    	     txtFindProduct.putClientProperty("JTextField.placeholderText", "Quét mã hoặc nhập mã sản phẩm...");
	    	     txtFindProduct.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());
	    	     txtFindProduct.setAlignmentX(LEFT_ALIGNMENT);
	    	     txtFindProduct.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
	    	     
	    	     // ---  Bảng danh sách sản phẩm đang chọn ---
	    	     String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Tồn kho", "Số lượng", "Đơn giá", "Thành tiền", ""};
	    	     model = new DefaultTableModel(cols, 0) {
	    	         @Override
	    	         public boolean isCellEditable(int row, int col) {
	    	             return col == 3;   // Thiết lập chỉ cột Số lượng được sửa
	    	         }
	    	     };
	    	     
	    	     tblProductList = new JTable(model);
	    	     tblProductList.setRowHeight(44);
	    	     tblProductList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    	     tblProductList.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
	    	     tblProductList.getTableHeader().setReorderingAllowed(false); 	//Không cho phép người dùng kéo thả để
	    	     																//thay đổi vị trí các cột
	    	     tblProductList.getTableHeader().setResizingAllowed(false);
	    	     tblProductList.setShowVerticalLines(false);
	    	     tblProductList.setGridColor(BORDER_COLOR);
	    	     tblProductList.setBackground(Color.WHITE);
	    	     tblProductList.setFocusable(false);
	    	     tblProductList.putClientProperty("terminateEditOnFocusLost",
	    	    		 							true); // Kết thúc việc edit khi focus vào ô khác
	    	     tblProductList.addMouseListener(this);
	    	     
	    	     ImageUtils IU = new ImageUtils();
	    	     ImageIcon deleteIcon = new ImageIcon("img/icon/icon_delete.png");
	    	     deleteIcon = IU.setImg(deleteIcon, 20, 20); 
	    	     DefaultTableCellRenderer trash = new DefaultTableCellRenderer(); //CellRenderer là quy định các ô đó hiển thị
	    	     trash.setHorizontalAlignment(SwingConstants.CENTER);
	    	     trash.setIcon(deleteIcon);
	    	     trash.setText("");
	    	     
	    	     DefaultTableCellRenderer center = new DefaultTableCellRenderer();
	    	     center.setHorizontalAlignment(SwingConstants.CENTER);
	    	     DefaultTableCellRenderer right = new DefaultTableCellRenderer() {
	    	    	 	// Anonymous Class chỉnh sửa trực tiếp Class DefaultTableCellRenderer
	    	    	    @Override
	    	    	    protected void setValue(Object v) {
	    	    	        setText(v instanceof Number ? DinhDang.TIEN.format(((Number) v).longValue()) : "");
	    	    	    }
	    	    	};
	    	     right.setHorizontalAlignment(SwingConstants.RIGHT);
	    	     
	    	     TableColumnModel cm = tblProductList.getColumnModel();
	    	     cm.getColumn(2).setCellRenderer(center);
	    	     cm.getColumn(3).setCellRenderer(center);
	    	     cm.getColumn(4).setCellRenderer(right);
	    	     cm.getColumn(5).setCellRenderer(right);
	    	     cm.getColumn(6).setCellRenderer(trash); 
	    	     
	    	     cm.getColumn(0).setPreferredWidth(110);
	    	     cm.getColumn(1).setPreferredWidth(300);
	    	     cm.getColumn(2).setPreferredWidth(80);
	    	     cm.getColumn(3).setPreferredWidth(100);
	    	     cm.getColumn(4).setPreferredWidth(110);
	    	     cm.getColumn(5).setPreferredWidth(120);
	    	     cm.getColumn(6).setPreferredWidth(30);
	    	     cm.getColumn(6).setMaxWidth(30);
	    	     
	    	     JScrollPane scroll = new JScrollPane(tblProductList);
	    	     scroll.getViewport().setBackground(Color.WHITE);
	    	     scroll.setAlignmentX(LEFT_ALIGNMENT);
	    	     

	    	     pnlProductList.add(lblProductListTitle);
	    	     pnlProductList.add(Box.createVerticalStrut(4));
	    	     pnlProductList.add(lblCountProduct);
	    	     pnlProductList.add(Box.createVerticalStrut(16));
	    	     pnlProductList.add(txtFindProduct);
	    	     pnlProductList.add(Box.createVerticalStrut(16));
	    	     pnlProductList.add(scroll);

	    	     
	    	     pnlMainProduct.add(pnlProductList, BorderLayout.CENTER);
	    	     
	    	     
	    		 
	    		 // ====== PHẦN CHI TIẾT THANH TOÁN ======
	    		 pnlProductPaymentDetail.setPreferredSize(new Dimension(300,0));
	    		 pnlProductPaymentDetail.setLayout(new BoxLayout(pnlProductPaymentDetail, BoxLayout.Y_AXIS));
	    		 
	    		 // ===== Tiêu đề =====
	    		 JPanel pnlHeaderProductPaymentDetail = new JPanel() {
	    			    @Override
	    			    public Dimension getMaximumSize() {
	    			        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	    			    }
	    			};
	    		    pnlHeaderProductPaymentDetail.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER_COLOR),
	    		            new EmptyBorder(10, 24, 8, 24)));
	    		    pnlHeaderProductPaymentDetail.setLayout(new BorderLayout());

	    		    JLabel lblTitle = new JLabel("Chi tiết thanh toán");
	    		    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    		    lblTitle.setForeground(DARK_TEXT);
	    		    JLabel lblSub = new JLabel("HĐ mới · Sản phẩm");
	    		    lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    		    lblSub.setForeground(GRAY_TEXT);

	    		    JPanel boxTitle = new JPanel();
	    		    boxTitle.setOpaque(false);
	    		    boxTitle.setLayout(new BoxLayout(boxTitle, BoxLayout.Y_AXIS));
	    		    boxTitle.add(lblTitle);
	    		    boxTitle.add(Box.createVerticalStrut(4));
	    		    boxTitle.add(lblSub);

	    		    pnlHeaderProductPaymentDetail.add(boxTitle, BorderLayout.CENTER);
	    		    
	    		 // === Tổng số lượng / Tạm tính / Giảm giá / Dùng điểm ===
	    		    JPanel pnlProductSummary = new JPanel() {
	    			    @Override
	    			    public Dimension getMaximumSize() {
	    			        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	    			    }
	    			};
	    		    pnlProductSummary.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
	    		            new EmptyBorder(10, 10, 10, 10)));
	    		    pnlProductSummary.setLayout(new BoxLayout(pnlProductSummary, BoxLayout.Y_AXIS));
	    		
	    		    lblTotalQty = new JLabel("0");
	    		    lblTotalQty.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblTotalQty.setForeground(DARK_TEXT);
	    		    
	    		    lblSubtotal = new JLabel("0 đ");
	    		    lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblSubtotal.setForeground(DARK_TEXT);
	    		    
	    		    lblDiscount = new JLabel("0 đ");
	    		    lblDiscount.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblDiscount.setForeground(Color.GREEN);

	    		    pnlProductSummary.add(createSummaryRow("Tổng số lượng", lblTotalQty));
	    		    pnlProductSummary.add(Box.createVerticalStrut(8));
	    		    pnlProductSummary.add(createSummaryRow("Tạm tính", lblSubtotal));
	    		    pnlProductSummary.add(Box.createVerticalStrut(8));
	    		    pnlProductSummary.add(createSummaryRow("Giảm giá", lblDiscount));
	    		    pnlProductSummary.add(Box.createVerticalStrut(8));
	    		    
	    		    // --- Box điểm tích lũy của khách hàng ---
	    		    JPanel pnlPoint = new JPanel(new BorderLayout()) {
	    		        @Override
	    		        public Dimension getMaximumSize() {
	    		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	    		        }
	    		    };
	    		    pnlPoint.setBackground(INFO_BG);
	    		    pnlPoint.setBorder(new EmptyBorder(8, 0, 8, 0));
	    		    pnlPoint.putClientProperty("FlatLaf.style", "arc: 16");
	    		  

	    		    JLabel lblPointTitle = new JLabel("Dùng điểm tích lũy");
	    		    lblPointTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblPointTitle.setForeground(DARK_TEXT);
	    		    
	    		    lblPointsAvail = new JLabel("Hiện có 0 điểm");
	    		    lblPointsAvail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    		    lblPointsAvail.setForeground(GRAY_TEXT);

	    		    JPanel pointText = new JPanel();
	    		    pointText.setOpaque(false);
	    		    pointText.setLayout(new BoxLayout(pointText, BoxLayout.Y_AXIS));
	    		    pointText.add(lblPointTitle);
	    		    pointText.add(Box.createVerticalStrut(3));
	    		    pointText.add(lblPointsAvail);
	    		    
	    		    chkUsePoints = new JCheckBox();
	    		    chkUsePoints.setOpaque(false);
	    		    chkUsePoints.setEnabled(false);

	    		    pnlPoint.add(pointText, BorderLayout.CENTER);
	    		    pnlPoint.add(chkUsePoints, BorderLayout.EAST);
	    		    pnlProductSummary.add(pnlPoint);
	    		    
	    		 // === Phần tổng thanh toán ===
	    		    JPanel boxTotal = new JPanel();
	    		    boxTotal.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
	    		            new EmptyBorder(10, 10, 10, 10)));
	    		    boxTotal.setLayout(new BoxLayout(boxTotal, BoxLayout.X_AXIS));
	    		    JLabel lblTotalCaption = new JLabel("Tổng thanh toán");
	    		    lblTotalCaption.setFont(new Font("Segoe UI", Font.BOLD, 17));
	    		    lblTotalCaption.setForeground(DARK_TEXT);
	    		    lblGrandTotal = new JLabel("0đ");
	    		    lblGrandTotal .setFont(new Font("Segoe UI", Font.BOLD, 28));
	    		    lblGrandTotal.setForeground(DARK_TEXT);
	    		    boxTotal.add(lblTotalCaption);
	    		    boxTotal.add(Box.createHorizontalGlue());
	    		    boxTotal.add(lblGrandTotal);
	    		    
	    		// === Phần tiền nhận + tiền thừa + nút tạo hóa đơn ===
	    		    JPanel pnlBottom = new JPanel();
	    		    pnlBottom.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
	    		            new EmptyBorder(10, 10, 10, 10)));
	    		    pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
	    		    
	    		    Box boxReceived = Box.createHorizontalBox();
	    		    
	    		    JLabel lblReceived = new JLabel("Tiền nhận");
	    		    lblReceived.setFont(new Font("Segoe UI", Font.BOLD, 13));
	    		    lblReceived.setForeground(GRAY_TEXT);
	    		    
	    		    txtReceived = new JTextField("");
	    		    txtReceived .setFont(new Font("Segoe UI", Font.BOLD, 18));
	    		    txtReceived.setHorizontalAlignment(SwingConstants.RIGHT);
	    		    txtReceived.putClientProperty("JComponent.roundRect", true);
	    		    txtReceived.setPreferredSize(new Dimension(200, 40));
	    		    txtReceived.setMaximumSize(new Dimension(200, 40));
	    		    
	    		    boxReceived.add(lblReceived);
	    		    boxReceived.add(Box.createHorizontalGlue());
	    		    boxReceived.add(txtReceived);
	    		    
	    		    Box boxChange = Box.createHorizontalBox();
	    		    
	    		    JLabel lblChange = new JLabel("Tiền thừa");
	    		    lblChange.setFont(new Font("Segoe UI", Font.BOLD, 13));
	    		    lblChange.setForeground(GRAY_TEXT);
	    		    lblChange.setPreferredSize(lblReceived.getPreferredSize());
	    		    
	    		    txtChange = new JTextField("");
	    		    txtChange .setFont(new Font("Segoe UI", Font.BOLD, 18));
	    		    txtChange.setHorizontalAlignment(SwingConstants.RIGHT);
	    		    txtChange.putClientProperty("JComponent.roundRect", true);
	    		    txtChange.setPreferredSize(new Dimension(200, 50));
	    		    txtChange.setMaximumSize(new Dimension(200, 50));
	    		    txtChange.setEnabled(false);
	    		 
	    		    
	    		    boxChange.add(lblChange);
	    		    boxChange.add(Box.createHorizontalGlue());
	    		    boxChange.add(txtChange);
	    		    
	    		    // --- Box nút Thanh toán và Lưu hóa đơn ---
	    		    Box boxButtonProduct = Box.createVerticalBox();
	    		    
	    		    ImageIcon receipt = new ImageIcon("img/icon/icon_receipt.png");
	    		    btnConfirm = new JButton("Xác nhận & tạo hóa đơn");
	    		    btnConfirm.setIcon(IU.setImg(receipt, 30, 30));
	    		    btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    		    btnConfirm.setForeground(Color.WHITE);
	    		    btnConfirm.setBackground(ORANGE);
	    		    btnConfirm.setAlignmentX(CENTER_ALIGNMENT);
	    		    btnConfirm.putClientProperty("FlatLaf.style", "arc: 16; borderWidth: 0");
	    		    btnConfirm.setPreferredSize(new Dimension(100, 46));
	    		    
	    		    ImageIcon save = new ImageIcon("img/icon/icon_save.png");
	    		    btnSaveProductInvoice = new JButton("Lưu hóa đơn");
	    		    btnSaveProductInvoice.setIcon(IU.setImg(save, 30, 30));
	    		    btnSaveProductInvoice.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    		    btnSaveProductInvoice.setForeground(Color.WHITE);
	    		    btnSaveProductInvoice.setBackground(Color.BLUE);
	    		    btnSaveProductInvoice.setAlignmentX(CENTER_ALIGNMENT);
	    		    btnSaveProductInvoice.putClientProperty("FlatLaf.style", "arc: 16; borderWidth: 0");
	    		    btnSaveProductInvoice.setPreferredSize(new Dimension(100,46));
	    	
	    		    
	    		    boxButtonProduct.add(btnConfirm);
	    		    boxButtonProduct.add(Box.createVerticalStrut(3));
	    		    boxButtonProduct.add(btnSaveProductInvoice);
	    		    
	    		    pnlBottom.add(new JLabel("Đang thiết kế lại!"));
	    		    pnlBottom.add(boxReceived);
	    		    pnlBottom.add(Box.createVerticalStrut(8));
	    		    pnlBottom.add(boxChange);
	    		    pnlBottom.add(Box.createVerticalStrut(8));
	    		    pnlBottom.add(boxButtonProduct);
	    		    
	    		
	    		pnlProductPaymentDetail.add(pnlHeaderProductPaymentDetail);
	    		pnlProductPaymentDetail.add(pnlProductSummary);
	    		pnlProductPaymentDetail.add(boxTotal);
	    		pnlProductPaymentDetail.add(pnlBottom);
	    		
	    		 // === KHU VỰC BẮT SỰ KIỆN MÀN PRODUCT ===
	    		btnSaveProductInvoice.addActionListener(this);
	    		btnConfirm.addActionListener(this);
	    		model.addTableModelListener(this);
	    		chkUsePoints.addActionListener(this);
	    		txtFindProduct.addActionListener(this);
	    		txtReceived.getDocument().addDocumentListener(new DocumentListener() {
	    			@Override public void insertUpdate(DocumentEvent e)  { updateChange(); }
	    			@Override public void removeUpdate(DocumentEvent e)  { updateChange(); }
	    			@Override public void changedUpdate(DocumentEvent e) { updateChange(); }
	    		});
	    	  }
	    	  
	    	
			  @Override
			  public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				
				if(o == chkUsePoints) {
					recalcTotals();
				}
				else if (o == txtFindProduct) {
				    addProductByCode();
				}
				else if (o == btnConfirm) {
				    createProductInvoice();
				}
				else if (o == btnSaveProductInvoice) {
					saveProductInvoice();
				}
			  }
			  
			  // --- Hàm thêm sản phẩm bằng mã vào danh sách sản phẩm ---
			  private void addProductByCode() {
				    String ma = txtFindProduct.getText().trim().toUpperCase();
				    if (ma.isEmpty()) return;

				    SanPham sp = null;
				    try {
				        sp = sp_dao.timTheoMa(ma);
				    } catch (Exception ex) {
				        ex.printStackTrace();
				    }

				    if (sp == null) {
				        JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm có mã: " + ma,
				                "Lỗi", JOptionPane.ERROR_MESSAGE);
				        txtFindProduct.selectAll();
				        return;
				    }
				    if (sp.getSoLuongTon() <= 0) {
				        JOptionPane.showMessageDialog(null, "Sản phẩm đã hết hàng!",
				                "Lỗi", JOptionPane.ERROR_MESSAGE);
				        return;
				    }

				    // Đã có trong bảng -> tăng số lượng thêm 1
				    for (int i = 0; i < model.getRowCount(); i++) {
				        if (model.getValueAt(i, 0).equals(sp.getMaSP())) {
				            int qty = Integer.parseInt(model.getValueAt(i, 3).toString());
				            if (qty >= sp.getSoLuongTon()) {
				                JOptionPane.showMessageDialog(null, "Số lượng đã đạt tối đa tồn kho!");
				            } else {
				                model.setValueAt(qty + 1, i, 3);
				            }
				            txtFindProduct.setText("");
				            txtFindProduct.requestFocusInWindow();
				            return;
				        }
				    }

				    // Chưa có -> thêm hàng mới (đơn giá = giaBan)
				    long gia = Math.round(sp.getGiaBan());
				    model.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuongTon(), 1, gia, gia, ""});
				    txtFindProduct.setText("");
				    txtFindProduct.requestFocusInWindow();
				}

			  void capNhatKhachHangProduct() {
					customerPoints = (khachHienTai == null) ? 0 : (long) khachHienTai.getDiemTichLuy();
					lblPointsAvail.setText("Hiện có " + customerPoints + " điểm");
					chkUsePoints.setSelected(false);
					chkUsePoints.setEnabled(customerPoints > 0);
					recalcTotals();
				}

			  
			  @Override
			  public void mouseClicked(MouseEvent e) {
				  int row = tblProductList.rowAtPoint(e.getPoint());
			        int col = tblProductList.columnAtPoint(e.getPoint());
			        if (row >= 0 && col == 6) {
			        	int ok = JOptionPane.showConfirmDialog(null,
			        	            "Xóa sản phẩm này khỏi hóa đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
			        	if (ok != JOptionPane.YES_OPTION) return;
			            if (tblProductList.isEditing()) tblProductList.getCellEditor().stopCellEditing();
			            model.removeRow(row);
			        }
			  }
			  @Override public void mousePressed(MouseEvent e) {}
			  @Override public void mouseReleased(MouseEvent e) {}
			  @Override public void mouseEntered(MouseEvent e) {}
			  @Override public void mouseExited(MouseEvent e) {}

			  @Override
			  public void tableChanged(TableModelEvent e) {
				  if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3 && e.getFirstRow() >= 0) {
				        int row = e.getFirstRow();
				        int stock = Integer.parseInt(model.getValueAt(row, 2).toString());

				        int qty;
				        try {
				            qty = Integer.parseInt(model.getValueAt(row, 3).toString().trim());
				        } catch (NumberFormatException ex) {
				            qty = 1;   
				        }
				        
				        //Giá trị thay vào nếu nhập sai qty
				        int fixed = Math.max(1, Math.min(qty, stock)); 

				        // Ô đang chứa String hoặc số sai -> ghi lại thành Integer chuẩn
				        if (!(model.getValueAt(row, 3) instanceof Integer) || fixed != qty) {
				            model.setValueAt(fixed, row, 3);   // kích hoạt lại listener
				            return;
				        }

				        long price = Long.parseLong(model.getValueAt(row, 4).toString());
				        model.setValueAt(price * fixed, row, 5);
				        return;
				    }
				    recalcTotals();
			  }
			// ===== CÔNG THỨC ĐỂ HIỂN THỊ  =====
			  private long getSubtotal() {
			      long s = 0;
			      for (int i = 0; i < model.getRowCount(); i++) {
			          s += Long.parseLong(model.getValueAt(i, 5).toString());
			      }
			      return s;
			  }

			  // Giảm giá từ điểm, tối đa bằng tạm tính
			  private long getDiscount(long tamTinh) {
			      if (chkUsePoints.isSelected() && customerPoints > 0) {
			          return Math.min(customerPoints * POINT_VALUE, tamTinh);
			      }
			      return 0;
			  }

			  // Đọc tiền nhận (gõ 200.000 hoặc 200000 đều được)
			  private long parseReceived() {
			      try {
			          return Long.parseLong(txtReceived.getText().trim().replace(".", "").replace(",", ""));
			      } catch (NumberFormatException e) {
			          return 0;
			      }
			  }

			  // ===== CẬP NHẬT GIAO DIỆN =====
			  private void recalcTotals() {
			      int totalQty = 0;
			      for (int i = 0; i < model.getRowCount(); i++) {
			          totalQty += Integer.parseInt(model.getValueAt(i, 3).toString());
			      }
			      long subTotal = getSubtotal();
			      discount = getDiscount(subTotal);

			      lblCountProduct.setText(model.getRowCount() + " mặt hàng đã chọn");
			      lblTotalQty.setText(String.valueOf(totalQty));
			      lblSubtotal.setText(DinhDang.TIEN.format(subTotal));
			      lblDiscount.setText(discount > 0 ? "- " + DinhDang.TIEN.format(discount)
			                                       : DinhDang.TIEN.format(0));
			      lblGrandTotal.setText(DinhDang.TIEN.format(subTotal - discount));
			      updateChange();
			  }

			  private void updateChange() {
			      if (txtReceived.getText().trim().isEmpty()) {
			          txtChange.setText("");
			          return;
			      }
			      long tamTinh = getSubtotal();
			      long change = parseReceived() - (tamTinh - getDiscount(tamTinh));
			      txtChange.setText(change >= 0 ? DinhDang.TIEN.format(change)
			                                    : "Thiếu " + DinhDang.TIEN.format(-change));
			  }

			// Kiểm tra chung: có khách, có sản phẩm
			  private boolean checkAvailable() {
			      if (khachHienTai == null) {
			          JOptionPane.showMessageDialog(null,
			                  "Vui lòng tìm và chọn khách hàng trước khi tạo hóa đơn!",
			                  "Lỗi", JOptionPane.ERROR_MESSAGE);
			          txtPhone.requestFocusInWindow();
			          return false;
			      }
			      if (tblProductList.isEditing()) tblProductList.getCellEditor().stopCellEditing();
			      if (model.getRowCount() == 0) {
			          JOptionPane.showMessageDialog(null, "Hóa đơn chưa có sản phẩm!",
			                  "Lỗi", JOptionPane.ERROR_MESSAGE);
			          return false;
			      }
			      return true;
			  }

			  // Lấy danh sách chi tiết từ bảng
			  private List<ChiTietHoaDon> getProductListDetail() {
			      List<ChiTietHoaDon> dsCT = new ArrayList<>();
			      for (int i = 0; i < model.getRowCount(); i++) {
			          SanPham sp = new SanPham();
			          sp.setMaSP(model.getValueAt(i, 0).toString());
			          int soLuong = Integer.parseInt(model.getValueAt(i, 3).toString());
			          double donGia = Double.parseDouble(model.getValueAt(i, 4).toString());
			          dsCT.add(new ChiTietHoaDon(null, null, sp, soLuong, donGia));
			      }
			      return dsCT;
			  }

			  // Ghi vào CSDL (dùng chung cho tạo và lưu)
			  private void writeProductInvoiceToDB(TrangThai trangThai, long giamGia, String tenThaoTac) {
			      List<ChiTietHoaDon> dsCT = getProductListDetail();
			      HoaDon hd = new HoaDon(null, LocalDate.now(), giamGia,
			              trangThai, null, khachHienTai, nhanVienDangNhap);
			       hd.setDsChiTiet(dsCT);   
			      try {
			          hd_dao.taoHoaDon(hd, dsCT, giamGia / POINT_VALUE, "BH", null);
			          JOptionPane.showMessageDialog(null, tenThaoTac + " hóa đơn " + hd.getMaHD() + " thành công!");
			          resetInvoice();
			      } catch (SQLException ex) {
			          ex.printStackTrace();
			          JOptionPane.showMessageDialog(null, tenThaoTac + " hóa đơn thất bại: " + ex.getMessage(),
			                  "Lỗi", JOptionPane.ERROR_MESSAGE);
			      }
			  }

			  // ===== THANH TOÁN HÓA ĐƠN =====
			  private void createProductInvoice() {
			      if (!checkAvailable()) return;

			      long tamTinh = getSubtotal();
			      long giamGia = getDiscount(tamTinh);
			      long phaiTra = tamTinh - giamGia;

			      if (parseReceived() < phaiTra) {
			          JOptionPane.showMessageDialog(null, "Tiền nhận chưa đủ!",
			                  "Lỗi", JOptionPane.ERROR_MESSAGE);
			          txtReceived.requestFocusInWindow();
			          return;
			      }

			      int ok = JOptionPane.showConfirmDialog(null,
			              "Tạo hóa đơn với tổng thanh toán " + DinhDang.TIEN.format(phaiTra) + "?",
			              "Xác nhận", JOptionPane.YES_NO_OPTION);
			      if (ok != JOptionPane.YES_OPTION) return;

			      writeProductInvoiceToDB(TrangThai.DA_THANH_TOAN, giamGia, "Tạo");
			  }

			  // ===== LƯU HÓA ĐƠN (chưa thanh toán, không cần tiền nhận) =====
			  private void saveProductInvoice() {
			      if (!checkAvailable()) return;

			      long tamTinh = getSubtotal();
			      long giamGia = getDiscount(tamTinh);
			      long phaiTra = tamTinh - giamGia;

			      int ok = JOptionPane.showConfirmDialog(null,
			              "Lưu hóa đơn (chưa thanh toán) với tổng " + DinhDang.TIEN.format(phaiTra) + "?",
			              "Xác nhận", JOptionPane.YES_NO_OPTION);
			      if (ok != JOptionPane.YES_OPTION) return;

			      writeProductInvoiceToDB(TrangThai.CHUA_THANH_TOAN, giamGia, "Lưu");
			  }
			  // Xóa trắng để bán hóa đơn mới
			  private void resetInvoice() {
					model.setRowCount(0);         
					txtFindProduct.setText("");
					txtReceived.setText("");
					txtChange.setText("");
					resetCustomer(true);       
					txtFindProduct.requestFocusInWindow();
				}

      }
      
      // ========== TAB HÓA ĐƠN DỊCH VỤ ==========
      private final class ServicePanel extends JPanel implements ActionListener, ListSelectionListener  {
    	
    	  /**
		 * 
		 */
    	  private static final long serialVersionUID = 1L;
		  // Lịch hẹn
    	  private DefaultTableModel modelAppointmentService;
    	  private JTable tblAppointmentService;

    	  // Dịch vụ của lịch hẹn đã chọn
    	  private JLabel lblServiceTitle = new JLabel("Dịch vụ của lịch hẹn");
    	  private JLabel lblServiceSub = new JLabel("Chọn một lịch hẹn ở trên để xem dịch vụ");
    	  private DefaultTableModel modelService;
    	  private JTable tblService;

    	  // Thanh toán
    	  private JLabel lblTotalService = new JLabel("0");
    	  private JLabel lblSubtotalService = new JLabel("0 đ");
    	  private JLabel lblDiscountService = new JLabel("0 đ");
    	  private JLabel lblPointsAvailService = new JLabel("Hiện có 0 điểm");
    	  private JCheckBox chkUsePointsService;
    	  private JLabel lblGrandTotalService = new JLabel("0 đ");
    	  private JTextField txtReceivedService;
    	  private JTextField txtChangeService;
    	  private JButton btnConfirmService;    
    	  
    	  private static final long POINT_VALUE_SERVICE = 1;      // 1 điểm = 1 đ
    	  private final LichHenDAO lh_dao = new LichHenDAO();
    	  private long customerPointsService = 0;
    	  private long discountService = 0;
    	  // maLH -> các dòng chi tiết của lịch hẹn đó
    	  private final Map<String, List<ChiTietLichHen>> dsTheoLichHen = new LinkedHashMap<>();
    	  // dịch vụ đang hiện ở bảng dưới (xóa dòng chỉ xóa khỏi hóa đơn, không đụng CSDL)
    	  private List<ChiTietLichHen> dsChiTietHienTai = new ArrayList<>();
    	  
    	  private String maLHDangChon = null;   // lịch hẹn đang được thanh toán
    	  private final HoaDonDAO hd_daoService = new HoaDonDAO();
		  private JButton btnSaveServiceInvoice;
    	  public ServicePanel() {
    		    setLayout(new BorderLayout());

    		    JPanel pnlMainService = new JPanel(new BorderLayout(0, 12));
    		    pnlMainService.setBorder(BorderFactory.createCompoundBorder(
    		            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
    		            new EmptyBorder(12, 16, 12, 16)));
    		    JPanel pnlPaymentDetailService = new JPanel();
    		    add(pnlMainService);
    		    JScrollPane scrollPaymentService = new JScrollPane(pnlPaymentDetailService,
    		    		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
    		    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    		    scrollPaymentService.setBorder(null);
    		    scrollPaymentService.setPreferredSize(new Dimension(320, 0));
    		    add(scrollPaymentService, BorderLayout.EAST);

    		    ImageUtils IUService = new ImageUtils();

    		    // ==================== CHỌN LỊCH HẸN ====================
    		    JPanel pnlAppointmentService = new JPanel(new BorderLayout(0, 8));
    		    pnlAppointmentService.setOpaque(false);

    		    JPanel headAppointmentService = new JPanel();
    		    headAppointmentService.setOpaque(false);
    		    headAppointmentService.setLayout(new BoxLayout(headAppointmentService, BoxLayout.Y_AXIS));
    		    JLabel lblAppointmentTitleService = new JLabel("Chọn lịch hẹn");
    		    lblAppointmentTitleService.setFont(new Font("Segoe UI", Font.BOLD, 20));
    		    lblAppointmentTitleService.setForeground(DARK_TEXT);
    		    JLabel lblAppointmentSubService = new JLabel("Các lịch hẹn đang chờ của khách hàng này");
    		    lblAppointmentSubService.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    		    lblAppointmentSubService.setForeground(GRAY_TEXT);
    		    headAppointmentService.add(lblAppointmentTitleService);
    		    headAppointmentService.add(Box.createVerticalStrut(4));
    		    headAppointmentService.add(lblAppointmentSubService);

    		    String[] colsAppointmentService = {"Mã lịch hẹn", "Ngày hẹn", "Thú cưng", "Giống / cân nặng", "Dịch vụ"};
    		    modelAppointmentService = new DefaultTableModel(colsAppointmentService, 0) {
    		        @Override public boolean isCellEditable(int r, int c) { return false; }
    		    };
    		    tblAppointmentService = new JTable(modelAppointmentService);
    		    tblAppointmentService.setRowHeight(44);
    		    tblAppointmentService.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    		    tblAppointmentService.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    		    tblAppointmentService.getTableHeader().setReorderingAllowed(false);
    		    tblAppointmentService.setShowVerticalLines(false);
    		    tblAppointmentService.setGridColor(BORDER_COLOR);
    		    tblAppointmentService.setBackground(Color.WHITE);
    		    tblAppointmentService.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		    tblAppointmentService.setRowSelectionAllowed(true);
    		    tblAppointmentService.setFocusable(true);
    		    tblAppointmentService.setSelectionBackground(new Color(0xFFEDE5));
    		    tblAppointmentService.setSelectionForeground(DARK_TEXT);

    		    TableColumnModel cmAppointmentService = tblAppointmentService.getColumnModel();
    		    cmAppointmentService.getColumn(0).setPreferredWidth(110);
    		    cmAppointmentService.getColumn(1).setPreferredWidth(130);
    		    cmAppointmentService.getColumn(2).setPreferredWidth(110);
    		    cmAppointmentService.getColumn(3).setPreferredWidth(160);
    		    cmAppointmentService.getColumn(4).setPreferredWidth(260);

    		    JScrollPane scrollAppointmentService = new JScrollPane(tblAppointmentService);
    		    scrollAppointmentService.getViewport().setBackground(Color.WHITE);

    		    pnlAppointmentService.add(headAppointmentService, BorderLayout.NORTH);
    		    pnlAppointmentService.add(scrollAppointmentService, BorderLayout.CENTER);
    		    
    		 // ==================== DỊCH VỤ CỦA LỊCH HẸN ====================
    		    JPanel pnlServiceList = new JPanel(new BorderLayout(0, 8));
    		    pnlServiceList.setOpaque(false);

    		    lblServiceTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
    		    lblServiceTitle.setForeground(DARK_TEXT);
    		    lblServiceSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    		    lblServiceSub.setForeground(GRAY_TEXT);

    		    JPanel titleBoxService = new JPanel();
    		    titleBoxService.setOpaque(false);
    		    titleBoxService.setLayout(new BoxLayout(titleBoxService, BoxLayout.Y_AXIS));
    		    titleBoxService.add(lblServiceTitle);
    		    titleBoxService.add(Box.createVerticalStrut(4));
    		    titleBoxService.add(lblServiceSub);

    		    String[] colsService = {"Mã dịch vụ", "Tên dịch vụ", "Thú cưng", "Đơn giá", ""};
    		    modelService = new DefaultTableModel(colsService, 0) {
    		        @Override public boolean isCellEditable(int r, int c) { return false; }
    		    };
    		    tblService = new JTable(modelService);
    		    tblService.setRowHeight(44);
    		    tblService.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    		    tblService.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    		    tblService.getTableHeader().setReorderingAllowed(false);
    		    tblService.setShowVerticalLines(false);
    		    tblService.setGridColor(BORDER_COLOR);
    		    tblService.setBackground(Color.WHITE);
    		    tblService.setFocusable(false);
    		    tblService.setRowSelectionAllowed(false);

    		    DefaultTableCellRenderer rightService = new DefaultTableCellRenderer() {
    		        @Override protected void setValue(Object v) {
    		            setText(v instanceof Number ? DinhDang.TIEN.format(((Number) v).longValue()) : "");
    		        }
    		    };
    		    rightService.setHorizontalAlignment(SwingConstants.RIGHT);

    		    ImageIcon deleteIconService = IUService.setImg(new ImageIcon("img/icon/icon_delete.png"), 20, 20);
    		    DefaultTableCellRenderer trashService = new DefaultTableCellRenderer();
    		    trashService.setHorizontalAlignment(SwingConstants.CENTER);
    		    trashService.setIcon(deleteIconService);
    		    trashService.setText("");

    		    TableColumnModel cmService = tblService.getColumnModel();
    		    cmService.getColumn(3).setCellRenderer(rightService);
    		    cmService.getColumn(4).setCellRenderer(trashService);
    		    cmService.getColumn(0).setPreferredWidth(100);
    		    cmService.getColumn(1).setPreferredWidth(240);
    		    cmService.getColumn(2).setPreferredWidth(130);
    		    cmService.getColumn(3).setPreferredWidth(120);
    		    cmService.getColumn(4).setPreferredWidth(50);
    		    cmService.getColumn(4).setMaxWidth(50);

    		    JScrollPane scrollService = new JScrollPane(tblService);
    		    scrollService.getViewport().setBackground(Color.WHITE);

    		    pnlServiceList.add(titleBoxService, BorderLayout.NORTH);
    		    pnlServiceList.add(scrollService, BorderLayout.CENTER);
    		    
    		    JPanel pnlCenterService = new JPanel(new GridLayout(2, 1, 0, 12));
    		    pnlCenterService.setOpaque(false);
    		    pnlCenterService.add(pnlAppointmentService);
    		    pnlCenterService.add(pnlServiceList);

    		    pnlMainService.add(pnlCenterService, BorderLayout.CENTER);

    		    // ==================== CHI TIẾT THANH TOÁN ====================
    		    pnlPaymentDetailService.setPreferredSize(new Dimension(300, 0));
    		    pnlPaymentDetailService.setLayout(new BoxLayout(pnlPaymentDetailService, BoxLayout.Y_AXIS));

    		    // --- Tiêu đề ---
    		    JPanel pnlHeaderService = new JPanel(new BorderLayout()) {
    		        @Override public Dimension getMaximumSize() {
    		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    		        }
    		    };
    		    pnlHeaderService.setBorder(BorderFactory.createCompoundBorder(
    		            BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER_COLOR),
    		            new EmptyBorder(10, 24, 8, 24)));
    		    
    		    JLabel lblTitleService = new JLabel("Chi tiết thanh toán");
    		    lblTitleService.setFont(new Font("Segoe UI", Font.BOLD, 20));
    		    lblTitleService.setForeground(DARK_TEXT);
    		    
    		    JLabel lblSubTitleService = new JLabel("HĐ mới · Dịch vụ");
    		    lblSubTitleService.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    		    lblSubTitleService.setForeground(GRAY_TEXT);
    		    
    		    JPanel boxTitleService = new JPanel();
    		    boxTitleService.setOpaque(false);
    		    boxTitleService.setLayout(new BoxLayout(boxTitleService, BoxLayout.Y_AXIS));
    		    boxTitleService.add(lblTitleService);
    		    boxTitleService.add(Box.createVerticalStrut(4));
    		    boxTitleService.add(lblSubTitleService);
    		    
    		    pnlHeaderService.add(boxTitleService, BorderLayout.CENTER);

    		    // --- Số dịch vụ / Tạm tính / Giảm giá / Dùng điểm ---
    		    JPanel pnlSummaryService = new JPanel() {
    		        @Override public Dimension getMaximumSize() {
    		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    		        }
    		    };
    		    pnlSummaryService.setBorder(BorderFactory.createCompoundBorder(
    		            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
    		            new EmptyBorder(10, 10, 10, 10)));
    		    pnlSummaryService.setLayout(new BoxLayout(pnlSummaryService, BoxLayout.Y_AXIS));

    		    for (JLabel l : new JLabel[]{lblTotalService, lblSubtotalService, lblDiscountService}) {
    		        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
    		        l.setForeground(DARK_TEXT);
    		    }
    		    lblDiscountService.setForeground(new Color(0x16A34A));

    		    pnlSummaryService.add(createSummaryRow("Số dịch vụ", lblTotalService));
    		    pnlSummaryService.add(Box.createVerticalStrut(8));
    		    pnlSummaryService.add(createSummaryRow("Tạm tính", lblSubtotalService));
    		    pnlSummaryService.add(Box.createVerticalStrut(8));
    		    pnlSummaryService.add(createSummaryRow("Giảm giá", lblDiscountService));
    		    pnlSummaryService.add(Box.createVerticalStrut(8));

    		    JPanel pnlPointService = new JPanel(new BorderLayout()) {
    		        @Override public Dimension getMaximumSize() {
    		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    		        }
    		    };
    		    pnlPointService.setBackground(INFO_BG);
    		    pnlPointService.setBorder(new EmptyBorder(8, 0, 8, 0));
    		    pnlPointService.putClientProperty("FlatLaf.style", "arc: 16");

    		    JLabel lblPointTitleService = new JLabel("Dùng điểm tích lũy");
    		    lblPointTitleService.setFont(new Font("Segoe UI", Font.BOLD, 15));
    		    lblPointTitleService.setForeground(DARK_TEXT);
    		    lblPointsAvailService.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    		    lblPointsAvailService.setForeground(GRAY_TEXT);
    		    JPanel pointTextService = new JPanel();
    		    pointTextService.setOpaque(false);
    		    pointTextService.setLayout(new BoxLayout(pointTextService, BoxLayout.Y_AXIS));
    		    pointTextService.add(lblPointTitleService);
    		    pointTextService.add(Box.createVerticalStrut(3));
    		    pointTextService.add(lblPointsAvailService);

    		    chkUsePointsService = new JCheckBox();
    		    chkUsePointsService.setOpaque(false);
    		    chkUsePointsService.setEnabled(false);

    		    pnlPointService.add(pointTextService, BorderLayout.CENTER);
    		    pnlPointService.add(chkUsePointsService, BorderLayout.EAST);
    		    pnlSummaryService.add(pnlPointService);

    		    // --- Tổng thanh toán ---
    		    JPanel boxTotalService = new JPanel() {
    		        @Override public Dimension getMaximumSize() {
    		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    		        }
    		    };
    		    boxTotalService.setBorder(BorderFactory.createCompoundBorder(
    		            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
    		            new EmptyBorder(10, 10, 10, 10)));
    		    boxTotalService.setLayout(new BoxLayout(boxTotalService, BoxLayout.X_AXIS));
    		    JLabel lblTotalCaptionService = new JLabel("Tổng thanh toán");
    		    lblTotalCaptionService.setFont(new Font("Segoe UI", Font.BOLD, 17));
    		    lblTotalCaptionService.setForeground(DARK_TEXT);
    		    lblGrandTotalService.setFont(new Font("Segoe UI", Font.BOLD, 24));
    		    lblGrandTotalService.setForeground(DARK_TEXT);
    		    boxTotalService.add(lblTotalCaptionService);
    		    boxTotalService.add(Box.createHorizontalGlue());
    		    boxTotalService.add(lblGrandTotalService);

    		    // --- Tiền nhận + tiền thừa + nút xác nhận ---
    		    JPanel pnlBottomService = new JPanel() {
    		        @Override public Dimension getMaximumSize() {
    		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    		        }
    		    };
    		    pnlBottomService.setBorder(new EmptyBorder(10, 10, 10, 10));
    		    pnlBottomService.setLayout(new BoxLayout(pnlBottomService, BoxLayout.Y_AXIS));

    		    Box boxReceivedService = Box.createHorizontalBox();
    		    JLabel lblReceivedService = new JLabel("Tiền nhận");
    		    lblReceivedService.setFont(new Font("Segoe UI", Font.BOLD, 13));
    		    lblReceivedService.setForeground(GRAY_TEXT);
    		    txtReceivedService = new JTextField();
    		    txtReceivedService.setFont(new Font("Segoe UI", Font.BOLD, 18));
    		    txtReceivedService.setHorizontalAlignment(SwingConstants.RIGHT);
    		    txtReceivedService.putClientProperty("JComponent.roundRect", true);
    		    txtReceivedService.setPreferredSize(new Dimension(180, 40));
    		    txtReceivedService.setMaximumSize(new Dimension(180, 40));
    		    boxReceivedService.add(lblReceivedService);
    		    boxReceivedService.add(Box.createHorizontalGlue());
    		    boxReceivedService.add(txtReceivedService);

    		    Box boxChangeService = Box.createHorizontalBox();
    		    JLabel lblChangeService = new JLabel("Tiền thừa");
    		    lblChangeService.setFont(new Font("Segoe UI", Font.BOLD, 13));
    		    lblChangeService.setForeground(GRAY_TEXT);
    		    txtChangeService = new JTextField();
    		    txtChangeService.setFont(new Font("Segoe UI", Font.BOLD, 18));
    		    txtChangeService.setHorizontalAlignment(SwingConstants.RIGHT);
    		    txtChangeService.putClientProperty("JComponent.roundRect", true);
    		    txtChangeService.setPreferredSize(new Dimension(180, 40));
    		    txtChangeService.setMaximumSize(new Dimension(180, 40));
    		    txtChangeService.setEditable(false);
    		    boxChangeService.add(lblChangeService);
    		    boxChangeService.add(Box.createHorizontalGlue());
    		    boxChangeService.add(txtChangeService);

    		    ImageIcon receiptService = new ImageIcon("img/icon/icon_receipt.png");
    		    btnConfirmService = new JButton("Xác nhận & tạo hóa đơn");
    		    btnConfirmService.setIcon(IUService.setImg(receiptService, 30, 30));
    		    btnConfirmService.setFont(new Font("Segoe UI", Font.BOLD, 16));
    		    btnConfirmService.setForeground(Color.WHITE);
    		    btnConfirmService.setBackground(ORANGE);
    		    btnConfirmService.setAlignmentX(CENTER_ALIGNMENT);
    		    btnConfirmService.putClientProperty("FlatLaf.style", "arc: 16; borderWidth: 0");
    		    btnConfirmService.setPreferredSize(new Dimension(100, 46));
    		    
    		   
    		    ImageIcon saveService = new ImageIcon("img/icon/icon_save.png");
    		    btnSaveServiceInvoice = new JButton("Lưu hóa đơn");
    		    btnSaveServiceInvoice.setIcon(IUService.setImg(saveService, 30, 30));
    		    btnSaveServiceInvoice.setFont(new Font("Segoe UI", Font.BOLD, 16));
    		    btnSaveServiceInvoice.setForeground(Color.WHITE);
    		    btnSaveServiceInvoice.setBackground(Color.BLUE);
    		    btnSaveServiceInvoice.setAlignmentX(CENTER_ALIGNMENT);
    		    btnSaveServiceInvoice.putClientProperty("FlatLaf.style", "arc: 16; borderWidth: 0");
    		    btnSaveServiceInvoice.setPreferredSize(new Dimension(100,46));
    		    
    		    Box boxButtonService = Box.createVerticalBox();
    		    boxButtonService.add(btnConfirmService);
    		    boxButtonService.add(Box.createVerticalStrut(3));
    		    boxButtonService.add(btnSaveServiceInvoice);
    		    
    		    pnlBottomService.add(new JLabel("Đang thiết kế lại!"));
    		    pnlBottomService.add(boxReceivedService);
    		    pnlBottomService.add(Box.createVerticalStrut(8));
    		    pnlBottomService.add(boxChangeService);
    		    pnlBottomService.add(Box.createVerticalStrut(8));
    		    pnlBottomService.add(boxButtonService);

    		    pnlPaymentDetailService.add(pnlHeaderService);
    		    pnlPaymentDetailService.add(pnlSummaryService);
    		    pnlPaymentDetailService.add(boxTotalService);
    		    pnlPaymentDetailService.add(pnlBottomService);
    		    
    		 // === KHU VỰC BẮT SỰ KIỆN MÀN DỊCH VỤ ===
    		    btnConfirmService.addActionListener(this);
    		    btnSaveServiceInvoice.addActionListener(this);
    		    chkUsePointsService.addActionListener(this);
    		    tblAppointmentService.getSelectionModel().addListSelectionListener(this);
    		    txtReceivedService.getDocument().addDocumentListener(new DocumentListener() {
    		        @Override public void insertUpdate(DocumentEvent e)  { updateChangeService(); }
    		        @Override public void removeUpdate(DocumentEvent e)  { updateChangeService(); }
    		        @Override public void changedUpdate(DocumentEvent e) { updateChangeService(); }
    		});
    		    tblService.addMouseListener(new MouseAdapter() {
    		        @Override
    		        public void mouseClicked(MouseEvent e) {
    		            int row = tblService.rowAtPoint(e.getPoint());
    		            int col = tblService.columnAtPoint(e.getPoint());
    		            if (row < 0 || col != 4) return;
    		            int ok = JOptionPane.showConfirmDialog(null,
    		                    "Bỏ dịch vụ này khỏi hóa đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    		            if (ok != JOptionPane.YES_OPTION) return;
    		            dsChiTietHienTai.remove(row);
    		            loadDataIntoServiceTable();
    		        }
    		    });
    	 
    	  }
    	  @Override
    	  public void actionPerformed(ActionEvent e) {
    	      Object o = e.getSource();
    	      if (o == chkUsePointsService) {
    	          recalcTotalsService();
    	      }else if (o == btnConfirmService) {
    	    	    createServiceInvoice();
    	      }else if(o == btnSaveServiceInvoice) {
    	    	   saveServiceInvoice();
    	      }
    	  }
    	  void updateServiceCustomer() {
    			// xóa dữ liệu của khách cũ
    			dsTheoLichHen.clear();
    			modelAppointmentService.setRowCount(0);
    			dsChiTietHienTai = new ArrayList<>();
    			maLHDangChon = null;
    			lblServiceTitle.setText("Dịch vụ của lịch hẹn");
    			lblServiceSub.setText("Chọn một lịch hẹn ở trên để xem dịch vụ");

    			customerPointsService = (khachHienTai == null) ? 0 : (long) khachHienTai.getDiemTichLuy();
    			lblPointsAvailService.setText("Hiện có " + customerPointsService + " điểm");
    			chkUsePointsService.setSelected(false);
    			chkUsePointsService.setEnabled(customerPointsService > 0);

    			if (khachHienTai != null) {
    				try {
    					List<ChiTietLichHen> dsChiTiet = lh_dao.layChiTietChoTheoKhachHang(khachHienTai.getMaKH());

    					for (ChiTietLichHen ct : dsChiTiet) {
    					    String maLH = ct.getLichHen().getMaLH();

    					    // Nếu lịch hẹn này chưa có trong map thì tạo danh sách rỗng cho nó
    					    if (!dsTheoLichHen.containsKey(maLH)) {
    					        dsTheoLichHen.put(maLH, new ArrayList<>());
    					    }

    					    // Thêm dịch vụ này vào danh sách của lịch hẹn
    					    dsTheoLichHen.get(maLH).add(ct);
    					}
    				} catch (SQLException ex) {
    					ex.printStackTrace();
    					JOptionPane.showMessageDialog(this, "Lỗi tải lịch hẹn: " + ex.getMessage(),
    							"Lỗi", JOptionPane.ERROR_MESSAGE);
    				}
    				loadDataIntoAppointmentTable();
    				if (dsTheoLichHen.isEmpty()) {
    					// không dùng popup vì khách có thể đang ở tab Sản phẩm
    					lblServiceSub.setText("Khách hàng này chưa có lịch hẹn đang chờ");
    				} else {
    					tblAppointmentService.setRowSelectionInterval(0, 0);   // tự chọn lịch hẹn đầu tiên
    					return;   // valueChanged đã nạp bảng dịch vụ và tính tiền
    				}
    			}
    			loadDataIntoServiceTable();
    		}
    	 
		  // Chọn dòng lịch hẹn -> nạp dịch vụ của lịch hẹn đó
    	  @Override
    	  public void valueChanged(ListSelectionEvent e) {
    	      if (e.getValueIsAdjusting()) return;
    	      int row = tblAppointmentService.getSelectedRow();
    	      if (row < 0) {
    	    	  maLHDangChon = null;
    	          dsChiTietHienTai = new ArrayList<>();
    	          lblServiceTitle.setText("Dịch vụ của lịch hẹn");
    	          lblServiceSub.setText("Chọn một lịch hẹn ở trên để xem dịch vụ");
    	          loadDataIntoServiceTable();
    	          return;
    	      }
    	      String maLH = modelAppointmentService.getValueAt(row, 0).toString();
    	      maLHDangChon = maLH;
    	      List<ChiTietLichHen> ds = dsTheoLichHen.get(maLH);
    	      dsChiTietHienTai = ds == null ? new ArrayList<>() : new ArrayList<>(ds);   // bản sao để xóa dòng không ảnh hưởng gốc
    	      lblServiceTitle.setText("Dịch vụ của lịch hẹn " + maLH);
    	      lblServiceSub.setText("Thú cưng: " + tenThuCungCuaLich(dsChiTietHienTai));
    	      loadDataIntoServiceTable();
    	  }

    	

    	  private void loadDataIntoAppointmentTable() {
    	      modelAppointmentService.setRowCount(0);
    	      for (Map.Entry<String, List<ChiTietLichHen>> en : dsTheoLichHen.entrySet()) {
    	          List<ChiTietLichHen> ds = en.getValue();
    	          ChiTietLichHen dau = ds.get(0);

    	          // Giống / cân nặng của từng thú cưng (không lặp)
    	          StringBuilder giong = new StringBuilder();
    	          Set<String> daCo = new LinkedHashSet<>();
    	          for (ChiTietLichHen ct : ds) {
    	              if (daCo.add(ct.getThuCung().getMaThuCung())) {
    	                  if (giong.length() > 0) giong.append("; ");
    	                  giong.append(ct.getThuCung().getLoai() == Loai.CHO ? "Chó" : "Mèo")
    	                       .append(" · ").append(ct.getThuCung().getCanNang()).append("kg");
    	              }
    	          }
    	          // Tên dịch vụ
    	          StringBuilder dichVu = new StringBuilder();
    	          for (ChiTietLichHen ct : ds) {
    	              if (dichVu.length() > 0) dichVu.append(", ");
    	              dichVu.append(ct.getDichVu().getTenDV());
    	          }

    	          modelAppointmentService.addRow(new Object[]{
    	                  en.getKey(),
    	                  DinhDang.NGAY.format(dau.getLichHen().getThoiGianHen()),
    	                  tenThuCungCuaLich(ds),
    	                  giong.toString(),
    	                  dichVu.toString()
    	          });
    	      }
    	  }

    	  private String tenThuCungCuaLich(List<ChiTietLichHen> ds) {
    	      Set<String> ten = new LinkedHashSet<>();
    	      for (ChiTietLichHen ct : ds) ten.add(ct.getThuCung().getTenThuCung());
    	      return String.join(", ", ten);
    	  }

    	  private void loadDataIntoServiceTable() {
    	      modelService.setRowCount(0);
    	      for (ChiTietLichHen ct : dsChiTietHienTai) {
    	          modelService.addRow(new Object[]{
    	                  ct.getDichVu().getMaDV(),
    	                  ct.getDichVu().getTenDV(),
    	                  ct.getThuCung().getTenThuCung(),
    	                  Math.round(ct.getDonGia()),
    	                  ""
    	          });
    	      }
    	      recalcTotalsService();
    	  }


    	  // ===== TÍNH TIỀN =====
    	  private long caculateSubTotalService() {
    	      long s = 0;
    	      for (ChiTietLichHen ct : dsChiTietHienTai) s += Math.round(ct.getDonGia());
    	      return s;
    	  }

    	  private long caculateDiscountService(long tamTinh) {
    	      if (chkUsePointsService.isSelected() && customerPointsService > 0) {
    	          return Math.min(customerPointsService * POINT_VALUE_SERVICE, tamTinh);
    	      }
    	      return 0;
    	  }

    	  private long parseReceivedService() {
    	      try {
    	          return Long.parseLong(txtReceivedService.getText().trim().replace(".", "").replace(",", ""));
    	      } catch (NumberFormatException ex) {
    	          return 0;
    	      }
    	  }

    	  private void recalcTotalsService() {
    	      long tamTinh = caculateSubTotalService();
    	      discountService = caculateDiscountService(tamTinh);

    	      lblTotalService.setText(String.valueOf(dsChiTietHienTai.size()));
    	      lblSubtotalService.setText(DinhDang.TIEN.format(tamTinh));
    	      lblDiscountService.setText(discountService > 0 ? "- " + DinhDang.TIEN.format(discountService)
    	                                                     : DinhDang.TIEN.format(0));
    	      lblGrandTotalService.setText(DinhDang.TIEN.format(tamTinh - discountService));
    	      updateChangeService();
    	  }

    	  private void updateChangeService() {
    	      if (txtReceivedService.getText().trim().isEmpty()) {
    	          txtChangeService.setText("");
    	          return;
    	      }
    	      long tamTinh = caculateSubTotalService();
    	      long change = parseReceivedService() - (tamTinh - caculateDiscountService(tamTinh));
    	      txtChangeService.setText(change >= 0 ? DinhDang.TIEN.format(change)
    	                                           : "Thiếu " + DinhDang.TIEN.format(-change));
    	  }
    	  private boolean checkAvailableService() {
    		    if (khachHienTai == null) {
    		        JOptionPane.showMessageDialog(this, "Vui lòng tìm và chọn khách hàng!",
    		                "Lỗi", JOptionPane.ERROR_MESSAGE);
    		        txtPhone.requestFocusInWindow();  
    		        return false;
    		    }
    		    if (maLHDangChon == null || dsChiTietHienTai.isEmpty()) {
    		        JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch hẹn có dịch vụ cần thanh toán!",
    		                "Lỗi", JOptionPane.ERROR_MESSAGE);
    		        return false;
    		    }
    		    return true;
    		}

    		// Mỗi dịch vụ là một dòng chi tiết: số lượng 1, đơn giá đã gồm phụ phí
    		private List<ChiTietHoaDon> getServiceListDetail() {
    		    List<ChiTietHoaDon> dsCT = new ArrayList<>();
    		    for (ChiTietLichHen ct : dsChiTietHienTai) {
    		        DichVu dv = new DichVu();
    		        dv.setMaDV(ct.getDichVu().getMaDV());
    		        ThuCung tc = new ThuCung();
    		        tc.setMaThuCung(ct.getThuCung().getMaThuCung());

    		        ChiTietHoaDon cthd = new ChiTietHoaDon(null, dv, null, 1, Math.round(ct.getDonGia()) );
    		        cthd.setThuCung(tc);
    		        dsCT.add(cthd);
    		    }
    		    return dsCT;
    		}

    		private void writeServiceInvoiceToDB(TrangThai trangThai, long giamGia, String tenThaoTac) {
    		    List<ChiTietHoaDon> dsCT = getServiceListDetail();
    		    HoaDon hd = new HoaDon(null, LocalDate.now(), giamGia,
    		            trangThai, null, khachHienTai, nhanVienDangNhap);
    		    hd.setDsChiTiet(dsCT);   // bắt buộc
    		    try {
    		        hd_daoService.taoHoaDon(hd, dsCT, giamGia / POINT_VALUE_SERVICE, "DV", maLHDangChon);
    		        JOptionPane.showMessageDialog(this, tenThaoTac + " hóa đơn " + hd.getMaHD() + " thành công!");
    		        resetInvoiceService();
    		    } catch (SQLException ex) {
    		        ex.printStackTrace();
    		        JOptionPane.showMessageDialog(this, tenThaoTac + " hóa đơn thất bại: " + ex.getMessage(),
    		                "Lỗi", JOptionPane.ERROR_MESSAGE);
    		    }
    		}

    		// ===== THANH TOÁN HÓA ĐƠN =====
    		private void createServiceInvoice() {
    		    if (!checkAvailableService()) return;

    		    long tamTinh = caculateSubTotalService();
    		    long giamGia = caculateDiscountService(tamTinh);
    		    long phaiTra = tamTinh - giamGia;

    		    if (parseReceivedService() < phaiTra) {
    		        JOptionPane.showMessageDialog(this, "Tiền nhận chưa đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    		        txtReceivedService.requestFocusInWindow();
    		        return;
    		    }

    		    int ok = JOptionPane.showConfirmDialog(this,
    		            "Tạo hóa đơn dịch vụ với tổng thanh toán " + DinhDang.TIEN.format(phaiTra) + "?",
    		            "Xác nhận", JOptionPane.YES_NO_OPTION);
    		    if (ok != JOptionPane.YES_OPTION) return;

    		    writeServiceInvoiceToDB(TrangThai.DA_THANH_TOAN, giamGia, "Tạo");
    		}

    		// ===== LƯU HÓA ĐƠN =====
    		private void saveServiceInvoice() {
    		    if (!checkAvailableService()) return;

    		    long tamTinh = caculateSubTotalService();
    		    long giamGia = caculateDiscountService(tamTinh);
    		    long phaiTra = tamTinh - giamGia;

    		    int ok = JOptionPane.showConfirmDialog(this,
    		            "Lưu hóa đơn dịch vụ (chưa thanh toán) với tổng " + DinhDang.TIEN.format(phaiTra) + "?",
    		            "Xác nhận", JOptionPane.YES_NO_OPTION);
    		    if (ok != JOptionPane.YES_OPTION) return;

    		    writeServiceInvoiceToDB(TrangThai.CHUA_THANH_TOAN, giamGia, "Lưu");
    		}
    	 

    		private void resetInvoiceService() {
    			txtReceivedService.setText("");
    			txtChangeService.setText("");
    			resetCustomer(true);          // xóa khách + ô SĐT chung, tự xóa lịch hẹn/dịch vụ
    			txtPhone.requestFocusInWindow();
    		}
      }
      
      // ==== CÁC HÀM DÙNG CHUNG TRONG BANHANGGUI ====
      // --- Tạo nhanh các hàng chi tiết thanh toán ---
	  private JPanel createSummaryRow(String caption, JLabel value) {
		    JPanel row = new JPanel() {
		        @Override
		        public Dimension getMaximumSize() {
		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
		        }
		    };
		  	
		    row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		    row.setOpaque(false);
		   
		    JLabel cap = new JLabel(caption);
		    cap.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		    cap.setForeground(GRAY_TEXT);

		    row.add(cap);
		    row.add(Box.createHorizontalGlue());
		    row.add(value);
		    return row;
		    
		
		   
		}
	  	
		  // --- Tạo nhanh các thông tin khách hàng ---
		  private JPanel createInfoColumn(String caption, JLabel value, Color valueColor) {
	          JPanel col = new JPanel();
	          col.setOpaque(false);
	          col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
	
	          JLabel cap = new JLabel(caption);
	          cap.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	          cap.setForeground(GRAY_TEXT);
	
	          value.setFont(new Font("Segoe UI", Font.BOLD, 16));
	          value.setForeground(valueColor);
	
	          col.add(cap);
	          col.add(Box.createVerticalStrut(2));
	          col.add(value);
	          return col;
	      }

}