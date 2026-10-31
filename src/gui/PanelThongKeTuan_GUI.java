package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.ui.FlatLineBorder;
import org.jfree.chart.axis.NumberAxis;

//import dao.ThongKeTaiChinhNgay_dao;

@SuppressWarnings("serial")
public class PanelThongKeTuan_GUI extends JPanel {
	
	private JLabel lblDoanhThuValue, lblChiPhiValue, lblLoiNhuanValue, lblKhachHangValue, lblHoaDonValue, lblSanPhamValue;
    private ChartPanel chartPanel;
//    private ThongKeTaiChinhNgay_dao tkDao;
	private JTextArea txtDeXuat;
	
	public PanelThongKeTuan_GUI() {
//		tkDao = new ThongKeTaiChinhNgay_dao(); //khoi tao dao
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // --- tieu de ben tren
        JLabel lblHeader = new JLabel("Thống kê Doanh thu: Tuần này", JLabel.CENTER);
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
//      p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
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

    // tao bieu do cot rong ban dau
//	private ChartPanel createEmptyBarChartPanel() {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        JFreeChart chart = ChartFactory.createBarChart(
//            "Chi tiết Doanh thu và Chi phí (Triệu đồng)", 
//            "Ngày", "Số tiền", dataset);
//     // --- HIEN THI DINH DANG TRUC Y
//        CategoryPlot plot = chart.getCategoryPlot();
//        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
//        
//        // phan cach hang nghin (VD: 1,000,000)
//        DecimalFormat df = new DecimalFormat("#,###"); 
//        yAxis.setNumberFormatOverride(df);
//        // -------------------------------------------------
//        return new ChartPanel(chart);
//    }
    
    //xu ly lay data va tinh toan tu dao
//	public void updateDashboard(LocalDate start, LocalDate end) {
//        // lay data tren dao
////        double doanhThu = tkDao.getTongDoanhThu(start, end);
////        double chiPhi = tkDao.getTongChiPhi(start, end);
////        double loiNhuan = tkDao.getLoiNhuan(start, end); // goi dao tinh loi nhuan
////        int soKhach = tkDao.getSoLuongKhachHang(start, end);
//
//        // dinh dang tien
//        DecimalFormat df = new DecimalFormat("#,### đ");
//        
//        // gan len UI
//        lblDoanhThuValue.setText(df.format(doanhThu));
//        lblChiPhiValue.setText(df.format(chiPhi));
//        lblLoiNhuanValue.setText(df.format(loiNhuan));
//        lblKhachHangValue.setText(soKhach + " khách");
//
//        // doi mau tang:xanh; giam:do
//        lblLoiNhuanValue.setForeground(loiNhuan >= 0 ? new Color(0, 150, 0) : Color.RED);
//
//        // de xuat tren UI
//        if (loiNhuan > 0) {
//            txtDeXuat.setText("Tuần này kinh doanh có lãi. Lượng khách đạt " + soKhach + " người.\n\n"
//                            + "Cửa hàng đang duy trì phong độ tốt. Đề xuất chuẩn bị sẵn sàng nguồn hàng "
//                            + "cho các ngày cuối tuần để tối ưu hóa doanh thu.");
//        } else if (loiNhuan < 0) {
//            txtDeXuat.setText("Tuần này đang ghi nhận âm vốn (" + df.format(loiNhuan) + ").\n\n"
//                            + "Khả năng cao do cửa hàng vừa thực hiện nhập lượng lớn hàng hóa đầu kỳ. "
//                            + "Cần rà soát lại và đẩy mạnh bán ra trong những ngày tới để nhanh chóng xoay vòng vốn.");
//        } else if (doanhThu == 0 && chiPhi == 0) {
//            txtDeXuat.setText("Hệ thống chưa ghi nhận dữ liệu giao dịch nào trong tuần này.");
//        } else {
//            txtDeXuat.setText("Tuần này cửa hàng đang đạt mức hòa vốn.\n\n"
//                            + "Chú ý theo dõi các danh mục hàng bán chậm và có phương án cắt giảm các chi phí vận hành không cần thiết.");
//        }
//        
//        // thanh cuon tren cung
//        txtDeXuat.setCaretPosition(0);
//
//        // cap nhat bieu do
////        updateChart(start, end);
//    }
	
	// cap nhat bieu do tu dao
//	private void updateChart(LocalDate start, LocalDate end) {
//	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//	    
//	    // lay 2 map, cho vao dao
//	    Map<LocalDate, Double> dataDoanhThu = tkDao.getDoanhThuTheoNgay(start, end);
//	    Map<LocalDate, Double> dataChiPhi = tkDao.getChiPhiTheoNgay(start, end);
//
//	    // dung vong lap, chon ngay
//	    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
//	        
//	        // chuyen thanh ngay tren truc X (VD: 2026-05-04)
//	        String dayString = date.toString(); 
//	        
//	        // lay data tu map, khong co gi thi return 0
//	        double doanhThu = dataDoanhThu.getOrDefault(date, 0.0);
//	        double chiPhi = dataChiPhi.getOrDefault(date, 0.0);
//	        
//	        // dua 2 cot vao bieu do
//	        dataset.addValue(doanhThu, "Doanh thu", dayString);
//	        dataset.addValue(chiPhi, "Chi phí", dayString);
//	    }
//
//	    // set data cho bieu do
//	    CategoryPlot plot = (CategoryPlot) chartPanel.getChart().getPlot();
//	    plot.setDataset(dataset);
//	}
	
	//bo goc cho the
	private void boGocComponent(JComponent comp, int doCong) {
		comp.setBorder(new FlatLineBorder(
		        new Insets(5, 10, 5, 10), new Color(200, 200, 200), 1, 15));
	}
}
