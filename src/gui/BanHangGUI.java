package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.formdev.flatlaf.icons.FlatSearchIcon;

import common.ImageUtils;
import dao.KhachHangDAO;
import dao.SanPhamDAO;
import entity.KhachHang;

public class BanHangGUI extends JPanel implements ChangeListener {

	   private JTabbedPane switchPane;
	   private ProductPanel pnlProduct;
	   private ServicePanel pnlService;
	   private SanPhamDAO sp_dao;
	   private KhachHangDAO kh_dao;
	   

	   public BanHangGUI() {
		   setLayout(new BorderLayout());
            switchPane = new JTabbedPane();
            sp_dao = new SanPhamDAO();
            kh_dao = new KhachHangDAO();
            
            pnlProduct = new ProductPanel();
            pnlService = new ServicePanel();
           
            switchPane.addTab("Sản phẩm", pnlProduct);
            switchPane.addTab("Dịch vụ", pnlService);
            
            add(switchPane, BorderLayout.NORTH);
            
            switchPane.addChangeListener(this);
       }

	   @Override
	   public void stateChanged(ChangeEvent e) {
		   switchPane.getTitleAt(switchPane.getSelectedIndex());
	   }
	   
	   
	   
	  // ========== TAB HÓA ĐƠN SẢN PHẨM ========== 
      private final class ProductPanel extends JPanel implements MouseListener, ActionListener  {
    	  	private static final Color DARK_TEXT    = new Color(0x111827);
    	  	private static final Color BORDER_COLOR = new Color(0xE5E7EB);
    	    private static final Color GRAY_TEXT    = new Color(0x6B7280);
    	    private static final Color ORANGE       = new Color(0xF26B3A);
    	    private static final Color INFO_BG      = new Color(0xF3F4F6);
			private JTextField txtPhone;
			private JLabel lblName;
			private JLabel lblPoints;
		
			private JLabel lblCountProduct = new JLabel("0 mặt hàng đã chọn");
			private JTextField txtProduct = new JTextField();
			private DefaultTableModel model;
			private JTable tblProductList;
			private JLabel lblTotalQty = new JLabel("0");
			private JLabel lblSubtotal = new JLabel("0đ");
			private JLabel lblDiscount = new JLabel("0đ");
			private JLabel lblPointsAvail = new JLabel("Hiện có 0 điểm");
			private JCheckBox chkUsePoints;
			private JTextField txtReceived;
			private JTextField txtChange;
			private JButton btnConfirm;
			private JLabel lblGrandTotal;
	    	  public ProductPanel() {
	    		 setLayout(new BorderLayout());
	    		 
	    		 JPanel pnlMain = new JPanel();
	    		 JPanel pnlPaymentDetail = new JPanel();
	    		 add(pnlMain);
	    		 add(pnlPaymentDetail, BorderLayout.EAST);
	    		 
	    		 // ====== PHẦN DANH SÁCH SẢN PHẨM (pnlMain) ======
	    		 pnlMain.setBorder(BorderFactory.createLineBorder(new Color(220,220,220),1));
	    		 pnlMain.setLayout(new BorderLayout());
	    		
	    	// ---- KHU VỰC THÔNG TIN KHÁCH HÀNG ----
	    		 JPanel pnlCustomerInfo = new JPanel();
	    		 pnlCustomerInfo.setLayout(new BoxLayout(pnlCustomerInfo, BoxLayout.Y_AXIS));
	    		 
	    		 // === TIÊU ĐỀ THÔNG TIN KHÁCH HÀNG === 
	    		 JLabel lblCustomerTitle = new JLabel("Thông tin khách hàng");
	    	        lblCustomerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    	        lblCustomerTitle.setForeground(DARK_TEXT);
	    	        lblCustomerTitle.setAlignmentX(LEFT_ALIGNMENT);

	    	        JLabel subtitle = new JLabel("Tìm khách hàng theo số điện thoại để xem điểm tích lũy ");
	    	        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    	        subtitle.setForeground(GRAY_TEXT);
	    	        subtitle.setAlignmentX(LEFT_ALIGNMENT);
	    	     // === THANH TÌM KIẾM KHÁCH HÀNG ===
	    	        JPanel cell = new JPanel(new BorderLayout(12, 0));
	    	        cell.setOpaque(false);
	    	        cell.setAlignmentX(LEFT_ALIGNMENT);
	    	        cell.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
	    	        txtPhone = new JTextField(20);
	    	        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    	        txtPhone.putClientProperty("JTextField.placeholderText", "Nhập số điện thoại");
	    	        txtPhone.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());
	    	        cell.add(txtPhone, BorderLayout.CENTER);
	    	     
	    	        
	    	    // === KHUNG THÔNG TIN KHÁCH HÀNG ===    
	    	        
	    	        // --- Khung ---
	    	        JPanel box = new JPanel(new GridLayout(1, 3, 40, 0));
	    	        box.setBackground(INFO_BG);
	    	        box.setBorder(new EmptyBorder(18, 24, 18, 24));
	    	        box.setAlignmentX(LEFT_ALIGNMENT);
	    	        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
	    	        box.putClientProperty("FlatLaf.style", "arc: 16");
	    	        
	    	        //--- Các cột thông tin ---
	    	        lblName = new JLabel("-");
	    	       
	    	        lblPoints = new JLabel("-");
	    	        box.add(createInfoColumn("Khách hàng", lblName, DARK_TEXT));
	    	       
	    	        box.add(createInfoColumn("Điểm tích lũy", lblPoints, ORANGE));
	    	        
	    	        pnlCustomerInfo.add(lblCustomerTitle);
	    	        pnlCustomerInfo.add(Box.createVerticalStrut(4));
	    	        pnlCustomerInfo.add(subtitle);
	    	        pnlCustomerInfo.add(Box.createVerticalStrut(16));
	    	        pnlCustomerInfo.add(cell);
	    	        pnlCustomerInfo.add(Box.createVerticalStrut(10));
	    	        pnlCustomerInfo.add(box);
	    	     pnlMain.add(pnlCustomerInfo);
	    	     
	    	     // ---- KHU VỰC THÔNG TIN SẢN PHẨN ----
	    	     JPanel pnlProductList = new JPanel();
	    	     pnlProductList.setLayout(new BoxLayout(pnlProductList, BoxLayout.Y_AXIS));
	    	     pnlProductList.setOpaque(false);
	    	     pnlProductList.setAlignmentX(LEFT_ALIGNMENT);
	    	     
	    	     JLabel lblProductTitle = new JLabel("Sản phẩm trong hóa đơn");
	    	     lblProductTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    	     lblProductTitle.setForeground(DARK_TEXT);
	    	     lblProductTitle.setAlignmentX(LEFT_ALIGNMENT);
	    	     
	    	     lblCountProduct.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    	     lblCountProduct.setForeground(GRAY_TEXT);
	    	     lblCountProduct.setAlignmentX(LEFT_ALIGNMENT);
	    	     
	    	     txtProduct.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    	     txtProduct.putClientProperty("JTextField.placeholderText", "Quét mã hoặc nhập mã sản phẩm...");
	    	     txtProduct.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());
	    	     txtProduct.setAlignmentX(LEFT_ALIGNMENT);
	    	     txtProduct.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
	    	     
	    	     // === Phần bảng danh sách sản phẩm ===
	    	     String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Tồn kho", "Số lượng", "Đơn giá", "Thành tiền", ""};
	    	     model = new DefaultTableModel(cols, 0) {
	    	         @Override
	    	         public boolean isCellEditable(int row, int col) {
	    	             return col == 3;   // chỉ cột Số lượng được sửa
	    	         }
	    	     };
	    	     
	    	     tblProductList = new JTable(model);
	    	     tblProductList.setRowHeight(44);
	    	     tblProductList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    	     tblProductList.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
	    	     tblProductList.getTableHeader().setReorderingAllowed(false);
	    	     tblProductList.setShowVerticalLines(false);
	    	     tblProductList.setGridColor(BORDER_COLOR);
	    	     tblProductList.setBackground(Color.WHITE);
	    	     tblProductList.setFocusable(false);
	    	     tblProductList.putClientProperty("terminateEditOnFocusLost", true);
	    	     tblProductList.addMouseListener(this);
	    	     
	    	     ImageUtils IU = new ImageUtils();
	    	     ImageIcon deleteIcon = new ImageIcon("img/icon/icon_delete.png");
	    	     deleteIcon = IU.setImg(deleteIcon, 20, 20); 
	    	     DefaultTableCellRenderer trash = new DefaultTableCellRenderer();
	    	     trash.setHorizontalAlignment(SwingConstants.CENTER);
	    	     trash.setIcon(deleteIcon);
	    	     trash.setText("");
	    	     
	    	     DefaultTableCellRenderer center = new DefaultTableCellRenderer();
	    	     center.setHorizontalAlignment(SwingConstants.CENTER);
	    	     DefaultTableCellRenderer right = new DefaultTableCellRenderer();
	    	     right.setHorizontalAlignment(SwingConstants.RIGHT);
	    	     
	    	     TableColumnModel cm = tblProductList.getColumnModel();
	    	     cm.getColumn(2).setCellRenderer(center);
	    	     cm.getColumn(3).setCellRenderer(center);
	    	     cm.getColumn(4).setCellRenderer(right);
	    	     cm.getColumn(5).setCellRenderer(right);
	    	     cm.getColumn(6).setCellRenderer(trash);
	    	     cm.getColumn(6).setMaxWidth(50); 
	    	     
	    	     cm.getColumn(0).setPreferredWidth(110);
	    	     cm.getColumn(1).setPreferredWidth(300);
	    	     cm.getColumn(2).setPreferredWidth(80);
	    	     cm.getColumn(3).setPreferredWidth(100);
	    	     cm.getColumn(4).setPreferredWidth(110);
	    	     cm.getColumn(5).setPreferredWidth(120);
	    	     cm.getColumn(6).setPreferredWidth(50);
	    	     cm.getColumn(6).setMaxWidth(50);
	    	     
	    	     JScrollPane scroll = new JScrollPane(tblProductList);
	    	     scroll.getViewport().setBackground(Color.WHITE);
	    	     scroll.setAlignmentX(LEFT_ALIGNMENT);
	    	     
	    	     model.addRow(new Object[]{"SP001", "Hạt Ganador Adult vị gà quay", 42, 1, "20.000đ", "20.000đ", ""});
	    	     model.addRow(new Object[]{"SP002", "Hạt cho mèo mọi lứa tuổi Catsrang", 18, 2, "60.000đ", "120.000đ", ""});
	    	     lblCountProduct.setText(model.getRowCount() + " mặt hàng đã chọn");

	    	     pnlProductList.add(lblProductTitle);
	    	     pnlProductList.add(Box.createVerticalStrut(4));
	    	     pnlProductList.add(lblCountProduct);
	    	     pnlProductList.add(Box.createVerticalStrut(16));
	    	     pnlProductList.add(txtProduct);
	    	     pnlProductList.add(Box.createVerticalStrut(16));
	    	     pnlProductList.add(scroll);

	    	     
	    	     pnlMain.add(pnlProductList, BorderLayout.SOUTH);
	    	     
	    		 
	    		 // ====== PHẦN CHI TIẾT THANH TOÁN ======
	    		 pnlPaymentDetail.setPreferredSize(new Dimension(300,0));
	    		 pnlPaymentDetail.setLayout(new BoxLayout(pnlPaymentDetail, BoxLayout.Y_AXIS));
	    		 // ===== Tiêu đề =====
	    		 JPanel pnlHeader = new JPanel() {
	    			    @Override
	    			    public Dimension getMaximumSize() {
	    			        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	    			    }
	    			};
	    		    pnlHeader.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER_COLOR),
	    		            new EmptyBorder(20, 24, 16, 24)));
	    		    pnlHeader.setLayout(new BorderLayout());

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

	    		    pnlHeader.add(boxTitle, BorderLayout.CENTER);
	    		    
	    		 // === Tổng số lượng / Tạm tính / Giảm giá / Dùng điểm ===
	    		    JPanel pnlSummary = new JPanel() {
	    			    @Override
	    			    public Dimension getMaximumSize() {
	    			        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	    			    }
	    			};
	    		    pnlSummary.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
	    		            new EmptyBorder(20, 10, 16, 10)));
	    		    pnlSummary.setLayout(new BoxLayout(pnlSummary, BoxLayout.Y_AXIS));
	    		
	    		    
	    		    lblTotalQty.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblTotalQty.setForeground(DARK_TEXT);
	    		    lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblSubtotal.setForeground(DARK_TEXT);
	    		    lblDiscount.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblDiscount.setForeground(Color.GREEN);

	    		    pnlSummary.add(createSummaryRow("Tổng số lượng", lblTotalQty));
	    		    pnlSummary.add(Box.createVerticalStrut(14));
	    		    pnlSummary.add(createSummaryRow("Tạm tính", lblSubtotal));
	    		    pnlSummary.add(Box.createVerticalStrut(14));
	    		    pnlSummary.add(createSummaryRow("Giảm giá", lblDiscount));
	    		    pnlSummary.add(Box.createVerticalStrut(16));
	    		    
	    		    JPanel pnlPoint = new JPanel(new BorderLayout()) {
	    		        @Override
	    		        public Dimension getMaximumSize() {
	    		            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	    		        }
	    		    };
	    		    pnlPoint.setBackground(INFO_BG);
	    		    pnlPoint.setBorder(new EmptyBorder(14, 0, 14, 0));
	    		    pnlPoint.putClientProperty("FlatLaf.style", "arc: 16");
	    		  

	    		    JLabel lblPointTitle = new JLabel("Dùng điểm tích lũy");
	    		    lblPointTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    		    lblPointTitle.setForeground(DARK_TEXT);
	    		    lblPointsAvail .setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    		    lblPointsAvail.setForeground(GRAY_TEXT);

	    		    JPanel pointText = new JPanel();
	    		    pointText.setOpaque(false);
	    		    pointText.setLayout(new BoxLayout(pointText, BoxLayout.Y_AXIS));
	    		    pointText.add(lblPointTitle);
	    		    pointText.add(Box.createVerticalStrut(3));
	    		    pointText.add(lblPointsAvail);
	    		    
	    		    chkUsePoints = new JCheckBox();
	    		    chkUsePoints.setOpaque(false);


	    		    pnlPoint.add(pointText, BorderLayout.CENTER);
	    		    pnlPoint.add(chkUsePoints, BorderLayout.EAST);
	    		    pnlSummary.add(pnlPoint);
	    		    
	    		 // === Phần tổng thanh toán ===
	    		    JPanel boxTotal = new JPanel();
	    		    boxTotal.setBorder(BorderFactory.createCompoundBorder(
	    		            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
	    		            new EmptyBorder(20, 10, 20, 10)));
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
	    		            new EmptyBorder(20, 10, 16, 10)));
	    		    pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
	    		    
	    		    Box boxReceived = Box.createHorizontalBox();
	    		    
	    		    JLabel lblReceived = new JLabel("Tiền nhận");
	    		    lblReceived.setFont(new Font("Segoe UI", Font.BOLD, 13));
	    		    lblReceived.setForeground(GRAY_TEXT);
	    		    
	    		    txtReceived = new JTextField("0");
	    		    txtReceived .setFont(new Font("Segoe UI", Font.BOLD, 18));
	    		    txtReceived.setHorizontalAlignment(SwingConstants.RIGHT);
	    		    txtReceived.putClientProperty("JComponent.roundRect", true);
	    		    txtReceived.setPreferredSize(new Dimension(200, 50));
	    		    txtReceived.setMaximumSize(new Dimension(200, 50));
	    		   
	    		    
	    		    boxReceived.add(lblReceived);
	    		    boxReceived.add(Box.createHorizontalGlue());
	    		    boxReceived.add(txtReceived);
	    		    
	    		    Box boxChange = Box.createHorizontalBox();
	    		    
	    		    JLabel lblChange = new JLabel("Tiền thừa");
	    		    lblChange.setFont(new Font("Segoe UI", Font.BOLD, 13));
	    		    lblChange.setForeground(GRAY_TEXT);
	    		    lblChange.setPreferredSize(lblReceived.getPreferredSize());
	    		    
	    		    txtChange = new JTextField("0");
	    		    txtChange .setFont(new Font("Segoe UI", Font.BOLD, 18));
	    		    txtChange.setHorizontalAlignment(SwingConstants.RIGHT);
	    		    txtChange.putClientProperty("JComponent.roundRect", true);
	    		    txtChange.setPreferredSize(new Dimension(200, 50));
	    		    txtChange.setMaximumSize(new Dimension(200, 50));
	    		    txtChange.setEnabled(false);
	    		 
	    		    
	    		    boxChange.add(lblChange);
	    		    boxChange.add(Box.createHorizontalGlue());
	    		    boxChange.add(txtChange);
	    		    
	    		    ImageIcon receipt = new ImageIcon("img/icon/icon_receipt.png");
	    		    btnConfirm = new JButton("Xác nhận & tạo hóa đơn");
	    		    btnConfirm.setIcon(IU.setImg(receipt, 30, 30));
	    		    btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    		    btnConfirm.setForeground(Color.WHITE);
	    		    btnConfirm.setBackground(ORANGE);
	    		    btnConfirm.setAlignmentX(CENTER_ALIGNMENT);
	    		    btnConfirm.putClientProperty("FlatLaf.style", "arc: 16; borderWidth: 0");
	    		    btnConfirm.setPreferredSize(new Dimension(100, 52));
	    		    
	    		    
	    		    pnlBottom.add(boxReceived);
	    		    pnlBottom.add(Box.createVerticalStrut(16));
	    		    pnlBottom.add(boxChange);
	    		    pnlBottom.add(Box.createVerticalStrut(16));
	    		    pnlBottom.add(btnConfirm);
	    		    
	    		
	    		pnlPaymentDetail.add(pnlHeader);
	    		pnlPaymentDetail.add(pnlSummary);
	    		pnlPaymentDetail.add(boxTotal);
	    		pnlPaymentDetail.add(pnlBottom);
	    		
	    		 // === KHU VỰC BẮT SỰ KIỆN MÀN PRODUCT ===
	    		txtPhone.addActionListener(this);
	    		txtProduct.addActionListener(this);
	    	  }
	    	  
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

	              value.setFont(new Font("Segoe UI", Font.BOLD, 17));
	              value.setForeground(valueColor);

	              col.add(cap);
	              col.add(Box.createVerticalStrut(4));
	              col.add(value);
	              return col;
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
			            lblCountProduct.setText(model.getRowCount() + " mặt hàng đã chọn");
			        }
			  }
			  @Override
			  public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				
				if(o == txtPhone) {
					searchCustomer();
				}
				
			  }

			  private void searchCustomer() {
				String sdt = txtPhone.getText();
				KhachHang kh = kh_dao.timKiemKhachHangTheoSDT(sdt);
				lblName.setText(kh.getTenKH());
				lblPoints.setText(String.valueOf(kh.getDiemTichLuy()));
				lblPointsAvail.setText("Hiện có " + kh.getDiemTichLuy()+" điểm");
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
      
      // ========== TAB HÓA ĐƠN DỊCH VỤ ==========
      private final class ServicePanel extends JPanel  {
		    
    	  public ServicePanel() {
		    	
    }
    	 

		   
      }
      
      
    

}