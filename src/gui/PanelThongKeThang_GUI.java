package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.chart.ChartPanel;

import com.formdev.flatlaf.ui.FlatLineBorder;

public class PanelThongKeThang_GUI extends JPanel{
	private JLabel lblDoanhThuValue, lblChiPhiValue, lblLoiNhuanValue, lblKhachHangValue, lblHoaDonValue, lblSanPhamValue;
    private ChartPanel chartPanel;
//    private ThongKeTaiChinhThang_dao tkDao;
	private JTextArea txtDeXuat;
	public PanelThongKeThang_GUI() {
//		tkDao = new ThongKeTaiChinhThang_dao(); //khoi tao dao
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // --- tieu de ben tren
        JLabel lblHeader = new JLabel("Thống kê Doanh thu: Theo tháng", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(189, 215, 238));
        add(lblHeader, BorderLayout.NORTH);

        // --- phan o giua gom chi so va bieu do
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        
        // the top, khoi tao cac panel trong
        lblDoanhThuValue = new JLabel("0 đ", JLabel.CENTER);
        lblDoanhThuValue.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblChiPhiValue = new JLabel("0 đ", JLabel.CENTER);
        lblChiPhiValue.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblLoiNhuanValue = new JLabel("0 đ", JLabel.CENTER);
        lblLoiNhuanValue.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblKhachHangValue = new JLabel("0 khách", JLabel.CENTER);
        lblKhachHangValue.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblHoaDonValue = new JLabel("0 hóa đơn", JLabel.CENTER);
        lblHoaDonValue.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblSanPhamValue = new JLabel("0 sản phẩm", JLabel.CENTER);
        lblSanPhamValue.setFont(new Font("Arial", Font.BOLD, 20));
        
        //gan lable vao card
        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 15, 15));
        pnlCards.setPreferredSize(new Dimension(0, 150));
        
        Color nenChanh = new Color(239, 227, 202);
        
        // doi mau nen
        pnlCards.add(createCard("Tổng doanh thu", lblDoanhThuValue, Color.GREEN, nenChanh));
        pnlCards.add(createCard("Tổng chi phí", lblChiPhiValue, Color.ORANGE, nenChanh));
        pnlCards.add(createCard("Lợi nhuận", lblLoiNhuanValue, new Color(0, 128, 0), nenChanh));
        
        pnlCards.add(createCard("Khách hàng", lblKhachHangValue, new Color(0, 128, 0), nenChanh));
        pnlCards.add(createCard("Tổng số hóa đơn", lblHoaDonValue, new Color(0, 128, 0), nenChanh));
        pnlCards.add(createCard("Tổng số sản phẩm", lblSanPhamValue, new Color(0, 128, 0), nenChanh));
        
        centerPanel.add(pnlCards, BorderLayout.NORTH);

        // bieu do cot o center
//        chartPanel = createEmptyBarChartPanel(); // khoi tao va goi ham bieu do rong
//        centerPanel.add(chartPanel, BorderLayout.CENTER);
        
        JPanel pnlBottomSplit = new JPanel(new BorderLayout(10, 0)); 
        pnlBottomSplit.setBackground(Color.WHITE);
        
        add(centerPanel, BorderLayout.CENTER);

        JPanel pnlDeXuat = new JPanel(new BorderLayout());
        pnlDeXuat.setPreferredSize(new Dimension(400, 0)); 
        pnlDeXuat.setBorder(BorderFactory.createTitledBorder("Đề xuất kinh doanh"));
        pnlDeXuat.setBackground(Color.WHITE);
        
        txtDeXuat = new JTextArea("Đang tải dữ liệu...");
        txtDeXuat.setEditable(false);
//        txtDeXuat.setLineWrap(true); 
//        txtDeXuat.setWrapStyleWord(true);
        txtDeXuat.setFont(new Font("Arial", Font.ITALIC, 14));
        txtDeXuat.setForeground(new Color(50, 50, 50));
        boGocComponent(txtDeXuat, 15);
        
        JScrollPane scrollDeXuat = new JScrollPane(txtDeXuat);
        boGocComponent(scrollDeXuat, 15);
        pnlDeXuat.add(scrollDeXuat);

        pnlBottomSplit.add(pnlDeXuat, BorderLayout.EAST);

        centerPanel.add(pnlBottomSplit, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
	}
	
	// khoi tao the thong tin
	private JPanel createCard(String title, JLabel lblValue, Color titleColor, Color bgColor) {
        JPanel p = new JPanel(new GridLayout(2, 1));
//	      p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        p.setBackground(bgColor);
        boGocComponent(p, 15);
     // Dùng FlatLineBorder
        p.setBorder(new FlatLineBorder(
            new Insets(10, 10, 10, 10), new Color(200, 200, 200), 1, 15                                   
        ));
        
        JLabel lblT = new JLabel(title, JLabel.CENTER);
        lblT.setForeground(titleColor); // mau tieu de
        lblT.setFont(new Font("Arial", Font.BOLD, 16));
        
        p.add(lblT);
        p.add(lblValue); 
        return p;
    }
		
	//bo goc cho the
	private void boGocComponent(JComponent comp, int doCong) {
		comp.setBorder(new FlatLineBorder(
		        new Insets(5, 10, 5, 10), new Color(200, 200, 200), 1, 15));
	}	
	
}
