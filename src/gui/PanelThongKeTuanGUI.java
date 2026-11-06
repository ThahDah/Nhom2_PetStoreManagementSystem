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
import javax.swing.border.TitledBorder;

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
    
    // --- KHAI BÁO BIẾN CHO PHẦN 1: TỔNG QUAN
    private JLabel tq_DoanhThu, tq_ChiPhi, tq_LoiNhuan, tq_KhachHang, tq_HoaDon, tq_SanPham;
    private ChartPanel chartLineTongQuan;
    private JTextArea txtDeXuatTongQuan;
    
    // --- KHAI BÁO BIẾN CHO PHẦN 2: SẢN PHẨM
    private JLabel sp_DoanhThu, sp_ChiPhi, sp_LoiNhuan, sp_KhachHang, sp_HoaDon, sp_SanPham;
    private ChartPanel chartBarSanPham;
    private JTextArea txtDeXuatSanPham;

    // --- KHAI BÁO BIẾN CHO PHẦN 3: DỊCH VỤ
    private JLabel dv_DoanhThu, dv_ChiPhi, dv_LoiNhuan, dv_KhachHang, dv_HoaDon, dv_DichVu;
    private ChartPanel chartBarDichVu;
    private JTextArea txtDeXuatDichVu;
    
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

        // --- 1. KHỞI TẠO & THÊM SECTION TỔNG QUAN
        initTongQuanComponents();
        JPanel pnlSectionTongQuan = createThongKeSection(
            "1. THỐNG KÊ TỔNG QUAN", 
            new JLabel[]{tq_DoanhThu, tq_ChiPhi, tq_LoiNhuan, tq_KhachHang, tq_HoaDon, tq_SanPham}, 
            new String[]{"Tổng doanh thu", "Tổng chi phí", "Lợi nhuận", "Khách hàng", "Tổng hóa đơn", "Tổng SP & DV"}, 
            chartLineTongQuan, txtDeXuatTongQuan
        );

        // --- 2. KHỞI TẠO & THÊM SECTION SẢN PHẨM
        initSanPhamComponents();
        JPanel pnlSectionSanPham = createThongKeSection(
            "2. THỐNG KÊ HOẠT ĐỘNG BÁN SẢN PHẨM", 
            new JLabel[]{sp_DoanhThu, sp_ChiPhi, sp_LoiNhuan, sp_KhachHang, sp_HoaDon, sp_SanPham}, 
            new String[]{"Doanh thu SP", "Chi phí SP", "Lợi nhuận SP", "KH mua SP", "Hóa đơn SP", "Số SP bán ra"}, 
            chartBarSanPham, txtDeXuatSanPham
        );

        // --- 3. KHỞI TẠO & THÊM SECTION DỊCH VỤ
        initDichVuComponents();
        JPanel pnlSectionDichVu = createThongKeSection(
            "3. THỐNG KÊ HOẠT ĐỘNG DỊCH VỤ", 
            new JLabel[]{dv_DoanhThu, dv_ChiPhi, dv_LoiNhuan, dv_KhachHang, dv_HoaDon, dv_DichVu}, 
            new String[]{"Doanh thu DV", "Chi phí DV", "Lợi nhuận DV", "KH dùng DV", "Hóa đơn DV", "Số lượt DV"}, 
            chartBarDichVu, txtDeXuatDichVu
        );

        // Thêm cả 3 section vào màn hình chính
        pnlMainContent.add(pnlSectionTongQuan);
        pnlMainContent.add(Box.createVerticalStrut(20)); // Khoảng cách giữa các phần
        pnlMainContent.add(pnlSectionSanPham);
        pnlMainContent.add(Box.createVerticalStrut(20));
        pnlMainContent.add(pnlSectionDichVu);

        // Bọc toàn bộ vào JScrollPane để thanh cuộn hoạt động
        JScrollPane mainScrollPane = new JScrollPane(pnlMainContent);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Cuộn chuột mượt hơn
        mainScrollPane.setBorder(null);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    // ====================================================================
    // HÀM TẠO 1 KHỐI (SECTION) CHUẨN ĐỂ TÁI SỬ DỤNG CHO CẢ 3 PHẦN
    // ====================================================================
    private JPanel createThongKeSection(String titleStr, JLabel[] kpis, String[] kpiTitles, ChartPanel chart, JTextArea txtDeXuat) {
        JPanel pnlSection = new JPanel();
        pnlSection.setLayout(new BoxLayout(pnlSection, BoxLayout.Y_AXIS));
        pnlSection.setBackground(Color.WHITE);
        
        // Viền và tiêu đề cho từng Section
        pnlSection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1), titleStr,
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(0, 102, 204)
        ));

        // --- KHỐI KPI CARDS ---
        JPanel pnlCards = new JPanel(new GridLayout(1, 6, 15, 15));
        pnlCards.setBackground(Color.WHITE);
        pnlCards.setPreferredSize(new Dimension(0, 90));
        pnlCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        Color nenChanh = new Color(239, 227, 202);
        Color[] colors = {Color.GREEN, Color.ORANGE, new Color(0, 128, 0), new Color(100, 0, 100), new Color(50, 50, 50), new Color(50, 50, 50)};
        
        for (int i = 0; i < 6; i++) {
            pnlCards.add(createCard(kpiTitles[i], kpis[i], colors[i], nenChanh));
        }

        // --- KHỐI BIỂU ĐỒ & ĐỀ XUẤT ---
        JPanel pnlMiddle = new JPanel(new BorderLayout(15, 0));
        pnlMiddle.setBackground(Color.WHITE);
        pnlMiddle.setPreferredSize(new Dimension(0, 350));
        pnlMiddle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        
        pnlMiddle.add(chart, BorderLayout.CENTER);

        JPanel pnlDeXuat = new JPanel(new BorderLayout());
        pnlDeXuat.setPreferredSize(new Dimension(350, 0)); 
        pnlDeXuat.setBorder(BorderFactory.createTitledBorder("Đề xuất & Nhận xét"));
        pnlDeXuat.setBackground(Color.WHITE);
        
        JScrollPane scrollDeXuat = new JScrollPane(txtDeXuat);
        boGocComponent(scrollDeXuat, 15);
        pnlDeXuat.add(scrollDeXuat);
        pnlMiddle.add(pnlDeXuat, BorderLayout.EAST);

        // Ghép các thành phần vào Section
        pnlSection.add(Box.createVerticalStrut(10));
        pnlSection.add(pnlCards);
        pnlSection.add(Box.createVerticalStrut(15));
        pnlSection.add(pnlMiddle);
        pnlSection.add(Box.createVerticalStrut(10));

        return pnlSection;
    }

    // ====================================================================
    // CÁC HÀM KHỞI TẠO BIẾN CHO TỪNG PHẦN
    // ====================================================================
    private void initTongQuanComponents() {
        tq_DoanhThu = createLabelValue(); tq_ChiPhi = createLabelValue();
        tq_LoiNhuan = createLabelValue(); tq_KhachHang = createLabelValue();
        tq_HoaDon = createLabelValue(); tq_SanPham = createLabelValue();
        chartLineTongQuan = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Tổng)");
        txtDeXuatTongQuan = createTextArea();
    }

    private void initSanPhamComponents() {
        sp_DoanhThu = createLabelValue(); sp_ChiPhi = createLabelValue();
        sp_LoiNhuan = createLabelValue(); sp_KhachHang = createLabelValue();
        sp_HoaDon = createLabelValue(); sp_SanPham = createLabelValue();
        chartBarSanPham = createBarChartPanel("Top 5 Sản Phẩm Bán Chạy");
        txtDeXuatSanPham = createTextArea();
    }

    private void initDichVuComponents() {
        dv_DoanhThu = createLabelValue(); dv_ChiPhi = createLabelValue();
        dv_LoiNhuan = createLabelValue(); dv_KhachHang = createLabelValue();
        dv_HoaDon = createLabelValue(); dv_DichVu = createLabelValue();
        chartBarDichVu = createBarChartPanel("Top 5 Dịch Vụ Sử Dụng Nhiều");
        txtDeXuatDichVu = createTextArea();
    }

    // ====================================================================
    // CÁC HÀM TẠO UI PHỤ TRỢ
    // ====================================================================
    private JLabel createLabelValue() {
        JLabel lbl = new JLabel("0", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        return lbl;
    }

    private JTextArea createTextArea() {
        JTextArea txt = new JTextArea("Đang tải dữ liệu...");
        txt.setEditable(false);
        txt.setLineWrap(true); 
        txt.setWrapStyleWord(true);
        txt.setFont(new Font("Arial", Font.ITALIC, 14));
        return txt;
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

    private void boGocComponent(JComponent comp, int doCong) {
        comp.setBorder(new FlatLineBorder(new Insets(5, 10, 5, 10), new Color(200, 200, 200), 1, 15));
    }

    // ====================================================================
    // BIỂU ĐỒ
    // ====================================================================
    private ChartPanel createLineChartPanel(String title) {
        JFreeChart chart = ChartFactory.createLineChart(title, "Ngày", "Số tiền (VNĐ)", new DefaultCategoryDataset());
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
    
    private ChartPanel createBarChartPanel(String title) {
        JFreeChart chart = ChartFactory.createBarChart(title, "", "Doanh thu (VNĐ)", new DefaultCategoryDataset(), 
            PlotOrientation.HORIZONTAL, false, true, false); 
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 153, 204));
        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#,###"));
        return new ChartPanel(chart);
    }

    // ====================================================================
    // HÀM CHÍNH ĐỔ DỮ LIỆU LÊN DASHBOARD
    // ====================================================================
    public void updateDashboard(LocalDate start, LocalDate end) {
        txtDeXuatTongQuan.setText("Đang tải dữ liệu tổng quan...");
        txtDeXuatSanPham.setText("Đang tải dữ liệu sản phẩm...");
        txtDeXuatDichVu.setText("Đang tải dữ liệu dịch vụ...");
        
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            double dtTQ = 0, cpTQ = 0, lnTQ = 0;
            int khTQ = 0;
            Map<LocalDate, Double> mapDoanhThu, mapChiPhi;
            Map<String, Double> mapTopSP, mapTopDV;

            @Override
            protected Void doInBackground() throws Exception {
                // Lấy data Tổng Quan
                dtTQ = tkDao.getTongDoanhThu(start, end);
                cpTQ = tkDao.getTongChiPhi(start, end);
                lnTQ = tkDao.getLoiNhuan(start, end);
                khTQ = tkDao.getSoLuongKhachHang(start, end);
                
                mapDoanhThu = tkDao.getDoanhThuTheoNgay(start, end);
                mapChiPhi = tkDao.getChiPhiTheoNgay(start, end);
                
                // Data cho Biểu đồ Cột SP & DV
                mapTopSP = tkDao.getDoanhThuTop5SanPham(start, end);
                mapTopDV = tkDao.getDoanhThuTop5DichVu(start, end);
                
                // TODO: Gọi thêm các hàm DAO để lấy Doanh thu, Chi phí riêng cho Sản Phẩm và Dịch Vụ
                // Ví dụ: dtSP = tkDao.getDoanhThuSanPham(start, end);
                
                return null;
            }

            @Override
            protected void done() {
                DecimalFormat df = new DecimalFormat("#,### đ");
                
                // ================== CẬP NHẬT TỔNG QUAN ==================
                tq_DoanhThu.setText(df.format(dtTQ));
                tq_ChiPhi.setText(df.format(cpTQ));
                tq_LoiNhuan.setText(df.format(lnTQ));
                tq_KhachHang.setText(khTQ + " khách");
                tq_HoaDon.setText("N/A"); // Chờ gọi hàm DAO count hoá đơn
                tq_SanPham.setText("N/A"); 
                tq_LoiNhuan.setForeground(lnTQ >= 0 ? new Color(0, 150, 0) : Color.RED);
                
                if (lnTQ > 0) txtDeXuatTongQuan.setText("Doanh thu tốt, đạt lợi nhuận " + df.format(lnTQ) + ".");
                else if (lnTQ < 0) txtDeXuatTongQuan.setText("Lợi nhuận đang âm (" + df.format(lnTQ) + "), cần rà soát lại chi phí nhập hàng.");
                else txtDeXuatTongQuan.setText("Chưa có giao dịch trong tuần này.");

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

                // ================== CẬP NHẬT SẢN PHẨM ==================
                sp_DoanhThu.setText("N/A"); // Điền dtSP sau khi bạn cập nhật DAO
                sp_ChiPhi.setText("N/A"); 
                sp_LoiNhuan.setText("N/A");
                txtDeXuatSanPham.setText("Đề xuất riêng cho mảng bán sản phẩm: Nên đẩy mạnh các sản phẩm thuộc TOP bán chạy ở biểu đồ bên cạnh.");
                
                DefaultCategoryDataset barSpData = new DefaultCategoryDataset();
                for (Map.Entry<String, Double> entry : mapTopSP.entrySet()) {
                    barSpData.addValue(entry.getValue(), "Sản Phẩm", entry.getKey());
                }
                chartBarSanPham.getChart().getCategoryPlot().setDataset(barSpData);

                // ================== CẬP NHẬT DỊCH VỤ ==================
                dv_DoanhThu.setText("N/A"); // Điền dtDV sau khi bạn cập nhật DAO
                dv_ChiPhi.setText("N/A"); 
                dv_LoiNhuan.setText("N/A");
                txtDeXuatDichVu.setText("Đề xuất riêng cho mảng dịch vụ: Cải thiện chất lượng phục vụ của Top 5 dịch vụ phổ biến nhất.");
                
                DefaultCategoryDataset barDvData = new DefaultCategoryDataset();
                for (Map.Entry<String, Double> entry : mapTopDV.entrySet()) {
                    barDvData.addValue(entry.getValue(), "Dịch Vụ", entry.getKey());
                }
                chartBarDichVu.getChart().getCategoryPlot().setDataset(barDvData);
            }
        };
        worker.execute();
    }
}