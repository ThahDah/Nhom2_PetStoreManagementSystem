package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.ui.FlatLineBorder;
import dao.ThongKeTheoTuanDAO;

@SuppressWarnings("serial")
public class PanelThongKeTuanGUI extends JPanel {
    
    private JLabel lblDoanhThuValue, lblChiPhiValue, lblLoiNhuanValue, lblKhachHangValue, lblHoaDonValue, lblSanPhamValue;
    
    private ChartPanel chartLineTongQuan;
    private ChartPanel chartBarSanPham;
    private ChartPanel chartBarDichVu;
    
    private JTextArea txtDeXuat;
    private ThongKeTheoTuanDAO tkDao;
    
    public PanelThongKeTuanGUI() {
        tkDao = new ThongKeTheoTuanDAO(); 
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);

        // --- Tiêu đề (Nằm cố định bên ngoài ScrollPane)
        JLabel lblHeader = new JLabel("Thống kê Kinh Doanh: Tuần này", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(189, 215, 238));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        // =========================================================
        // CONTAINER CHÍNH CHỨA MỌI THỨ VÀ CÓ THỂ CUỘN LÊN XUỐNG
        // =========================================================
        JPanel pnlMainContent = new JPanel();
        pnlMainContent.setLayout(new BoxLayout(pnlMainContent, BoxLayout.Y_AXIS));
        pnlMainContent.setBackground(Color.WHITE);
        pnlMainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- KHỐI 1: CÁC THẺ CHỈ SỐ (KPI CARDS)
        lblDoanhThuValue = createLabelValue();
        lblChiPhiValue = createLabelValue();
        lblLoiNhuanValue = createLabelValue();
        lblKhachHangValue = createLabelValue();
        lblHoaDonValue = createLabelValue();
        lblSanPhamValue = createLabelValue();
        
        JPanel pnlCards = new JPanel(new GridLayout(1, 6, 15, 15));
        pnlCards.setPreferredSize(new Dimension(0, 100));
        pnlCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        pnlCards.setBackground(Color.WHITE);
        
        Color nenChanh = new Color(239, 227, 202);
        pnlCards.add(createCard("Tổng doanh thu", lblDoanhThuValue, Color.GREEN, nenChanh));
        pnlCards.add(createCard("Tổng chi phí", lblChiPhiValue, Color.ORANGE, nenChanh));
        pnlCards.add(createCard("Lợi nhuận", lblLoiNhuanValue, new Color(0, 128, 0), nenChanh));
        pnlCards.add(createCard("Khách hàng", lblKhachHangValue, new Color(100, 0, 100), nenChanh));
        pnlCards.add(createCard("Tổng hóa đơn", lblHoaDonValue, new Color(50, 50, 50), nenChanh));
        pnlCards.add(createCard("Tổng SP & DV", lblSanPhamValue, new Color(50, 50, 50), nenChanh));
        
        pnlMainContent.add(pnlCards);
        pnlMainContent.add(Box.createVerticalStrut(15)); // Khoảng cách

        // --- KHỐI 2: BIỂU ĐỒ ĐƯỜNG & ĐỀ XUẤT
        JPanel pnlMiddle = new JPanel(new BorderLayout(15, 0));
        pnlMiddle.setPreferredSize(new Dimension(0, 400));
        pnlMiddle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        pnlMiddle.setBackground(Color.WHITE);
        
        chartLineTongQuan = createLineChartPanel(); 
        pnlMiddle.add(chartLineTongQuan, BorderLayout.CENTER);

        JPanel pnlDeXuat = new JPanel(new BorderLayout());
        pnlDeXuat.setPreferredSize(new Dimension(350, 0)); 
        pnlDeXuat.setBorder(BorderFactory.createTitledBorder("Đề xuất kinh doanh"));
        pnlDeXuat.setBackground(Color.WHITE);
        
        txtDeXuat = new JTextArea("Đang tải dữ liệu...");
        txtDeXuat.setEditable(false);
        txtDeXuat.setLineWrap(true); 
        txtDeXuat.setWrapStyleWord(true);
        txtDeXuat.setFont(new Font("Arial", Font.ITALIC, 14));
        boGocComponent(txtDeXuat, 15);
        
        JScrollPane scrollDeXuat = new JScrollPane(txtDeXuat);
        boGocComponent(scrollDeXuat, 15);
        pnlDeXuat.add(scrollDeXuat);
        pnlMiddle.add(pnlDeXuat, BorderLayout.EAST);
        
        pnlMainContent.add(pnlMiddle);
        pnlMainContent.add(Box.createVerticalStrut(15)); // Khoảng cách

        // --- KHỐI 3: BIỂU ĐỒ TOP SẢN PHẨM & TOP DỊCH VỤ (Nằm ngang nhau)
        JPanel pnlBottom = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlBottom.setPreferredSize(new Dimension(0, 350));
        pnlBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        pnlBottom.setBackground(Color.WHITE);
        
        chartBarSanPham = createBarChartPanel("Top 5 Sản Phẩm Doanh Thu Cao");
        chartBarDichVu = createBarChartPanel("Top 5 Dịch Vụ Doanh Thu Cao");
        
        pnlBottom.add(chartBarSanPham);
        pnlBottom.add(chartBarDichVu);
        
        pnlMainContent.add(pnlBottom);

        // Bọc toàn bộ vào JScrollPane để thanh cuộn hoạt động
        JScrollPane mainScrollPane = new JScrollPane(pnlMainContent);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Cuộn chuột mượt hơn
        mainScrollPane.setBorder(null);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    // Các hàm tạo UI phụ trợ
    private JLabel createLabelValue() {
        JLabel lbl = new JLabel("0", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        return lbl;
    }

    private JPanel createCard(String title, JLabel lblValue, Color titleColor, Color bgColor) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(bgColor);
        boGocComponent(p, 15);
        p.setBorder(new FlatLineBorder(new Insets(5, 5, 5, 5), new Color(200, 200, 200), 1, 15));
        
        JLabel lblT = new JLabel(title, JLabel.CENTER);
        lblT.setForeground(titleColor); 
        lblT.setFont(new Font("Arial", Font.BOLD, 14));
        
        p.add(lblT);
        p.add(lblValue); 
        return p;
    }

    // TẠO BIỂU ĐỒ ĐƯỜNG
    private ChartPanel createLineChartPanel() {
        JFreeChart chart = ChartFactory.createLineChart("Xu hướng Doanh thu, Chi phí, Lợi nhuận", 
            "Ngày", "Số tiền (VNĐ)", new DefaultCategoryDataset());
        
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
    
        renderer.setSeriesPaint(0, new Color(0, 150, 0)); 
        renderer.setSeriesPaint(1, new Color(255, 140, 0)); 
        renderer.setSeriesPaint(2, new Color(0, 100, 255)); 
        plot.setRenderer(renderer);
        
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#,###"));
        return new ChartPanel(chart);
    }
    
    // TẠO BIỂU ĐỒ CỘT (Cho Sản Phẩm & Dịch Vụ)
    private ChartPanel createBarChartPanel(String title) {
        JFreeChart chart = ChartFactory.createBarChart(title, 
            "", "Doanh thu (VNĐ)", new DefaultCategoryDataset(), 
            PlotOrientation.HORIZONTAL, false, true, false); // Cột ngang
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 153, 204)); // Màu xanh dương nhạt

        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#,###"));
        return new ChartPanel(chart);
    }

    // HÀM CHÍNH ĐỔ DỮ LIỆU LÊN DASHBOARD
    public void updateDashboard(LocalDate start, LocalDate end) {
        txtDeXuat.setText("Đang tải dữ liệu và phân tích...");
        
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            double doanhThu = 0, chiPhi = 0, loiNhuan = 0;
            int soKhach = 0;
            Map<LocalDate, Double> mapDoanhThu, mapChiPhi;
            Map<String, Double> mapTopSP, mapTopDV;

            @Override
            protected Void doInBackground() throws Exception {
                doanhThu = tkDao.getTongDoanhThu(start, end);
                chiPhi = tkDao.getTongChiPhi(start, end);
                loiNhuan = tkDao.getLoiNhuan(start, end);
                soKhach = tkDao.getSoLuongKhachHang(start, end);
                
                // Lấy data biểu đồ
                mapDoanhThu = tkDao.getDoanhThuTheoNgay(start, end);
                mapChiPhi = tkDao.getChiPhiTheoNgay(start, end);
                mapTopSP = tkDao.getDoanhThuTop5SanPham(start, end);
                mapTopDV = tkDao.getDoanhThuTop5DichVu(start, end);
                return null;
            }

            @Override
            protected void done() {
                DecimalFormat df = new DecimalFormat("#,### đ");
                
                lblDoanhThuValue.setText(df.format(doanhThu));
                lblChiPhiValue.setText(df.format(chiPhi));
                lblLoiNhuanValue.setText(df.format(loiNhuan));
                lblKhachHangValue.setText(soKhach + " khách");
                lblHoaDonValue.setText("N/A"); // Bạn có thể viết thêm hàm Count HoaDon ở DAO
                lblSanPhamValue.setText("N/A"); 
                
                lblLoiNhuanValue.setForeground(loiNhuan >= 0 ? new Color(0, 150, 0) : Color.RED);
                
                // Cập nhật Đề xuất
                if (loiNhuan > 0) txtDeXuat.setText("Doanh thu tốt, đạt lợi nhuận " + df.format(loiNhuan) + ".");
                else if (loiNhuan < 0) txtDeXuat.setText("Lợi nhuận đang âm (" + df.format(loiNhuan) + "), cần rà soát lại chi phí nhập hàng.");
                else txtDeXuat.setText("Chưa có giao dịch.");

                // 1. Đổ data Biểu đồ đường (Line)
                DefaultCategoryDataset lineData = new DefaultCategoryDataset();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    String ds = date.format(formatter); 
                    double dt = mapDoanhThu.getOrDefault(date, 0.0);
                    double cp = mapChiPhi.getOrDefault(date, 0.0);
                    lineData.addValue(dt, "Doanh thu", ds);
                    lineData.addValue(cp, "Chi phí", ds);
                    lineData.addValue(dt - cp, "Lợi nhuận", ds);
                }
                chartLineTongQuan.getChart().getCategoryPlot().setDataset(lineData);

                // 2. Đổ data Biểu đồ cột ngang (Top Sản phẩm)
                DefaultCategoryDataset barSpData = new DefaultCategoryDataset();
                for (Map.Entry<String, Double> entry : mapTopSP.entrySet()) {
                    barSpData.addValue(entry.getValue(), "Sản Phẩm", entry.getKey());
                }
                chartBarSanPham.getChart().getCategoryPlot().setDataset(barSpData);

                // 3. Đổ data Biểu đồ cột ngang (Top Dịch vụ)
                DefaultCategoryDataset barDvData = new DefaultCategoryDataset();
                for (Map.Entry<String, Double> entry : mapTopDV.entrySet()) {
                    barDvData.addValue(entry.getValue(), "Dịch Vụ", entry.getKey());
                }
                chartBarDichVu.getChart().getCategoryPlot().setDataset(barDvData);
            }
        };
        worker.execute();
    }
	
    private void boGocComponent(JComponent comp, int doCong) {
        comp.setBorder(new FlatLineBorder(new Insets(5, 10, 5, 10), new Color(200, 200, 200), 1, 15));
    }
}