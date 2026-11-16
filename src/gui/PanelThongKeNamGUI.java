package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.ui.FlatLineBorder;

import dao.ThongKeTheoNamDAO;

@SuppressWarnings("serial")
public class PanelThongKeNamGUI extends JPanel {
    private ThongKeTheoNamDAO tkDao;
    
    // --- khai bao bien phan 1: tong quan
    private JLabel tq_DoanhThu, tq_ChiPhi, tq_LoiNhuan, tq_KhachHang, tq_HoaDon;
    private ChartPanel chartLineTongQuan;
    private JTextArea txtDeXuatTongQuan;
    
    // --- khai bao bien phan 2: san pham
    private JLabel sp_DoanhThu, sp_ChiPhi, sp_LoiNhuan, sp_KhachHang, sp_HoaDon, sp_SanPham;
    private ChartPanel chartLineSanPham;
    private JTextArea txtDeXuatSanPham;

    // --- khai bao bien phan 3: dich vu
    private JLabel dv_DoanhThu, dv_KhachHang, dv_HoaDon, dv_DichVu;
    private ChartPanel chartBarDichVu; 
    private JTextArea txtDeXuatDichVu;
    
    // --- BỘ LỌC NĂM ---
    private JComboBox<Integer> cboNam;
    private JButton btnThongKe;
    
    public PanelThongKeNamGUI() {
        tkDao = new ThongKeTheoNamDAO(); 
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);

        // =========================================================
        // 1. PHẦN HEADER & BỘ LỌC CHỌN NĂM
        // =========================================================
        JPanel pnlHeaderWrapper = new JPanel(new BorderLayout());
        pnlHeaderWrapper.setBackground(Color.WHITE);
        
        JLabel lblHeader = new JLabel("Thống kê Kinh Doanh: Theo Năm", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(189, 215, 238));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlHeaderWrapper.add(lblHeader, BorderLayout.NORTH);
        
        // Panel chứa ComboBox
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        cboNam = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 3; i <= currentYear + 1; i++) {
            cboNam.addItem(i); 
        }
        
        btnThongKe = new JButton("Thống Kê");
        btnThongKe.setBackground(new Color(0, 153, 204));
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFont(new Font("Arial", Font.BOLD, 14));
        
        pnlFilter.add(new JLabel("Chọn năm:"));
        pnlFilter.add(cboNam);
        pnlFilter.add(btnThongKe);
        pnlHeaderWrapper.add(pnlFilter, BorderLayout.SOUTH);
        
        add(pnlHeaderWrapper, BorderLayout.NORTH);

        // =========================================================
        // 2. CONTAINER CHÍNH
        // =========================================================
        JPanel pnlMainContent = new JPanel();
        pnlMainContent.setLayout(new BoxLayout(pnlMainContent, BoxLayout.Y_AXIS));
        pnlMainContent.setBackground(Color.WHITE);
        pnlMainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initTongQuanComponents();
        JPanel pnlSectionTongQuan = createThongKeSection("1. THỐNG KÊ TỔNG QUAN", 
            new JLabel[]{tq_DoanhThu, tq_ChiPhi, tq_LoiNhuan, tq_KhachHang, tq_HoaDon}, 
            new String[]{"Tổng doanh thu", "Tổng chi phí", "Lợi nhuận", "Khách hàng", "Tổng hóa đơn"}, 
            chartLineTongQuan, txtDeXuatTongQuan);

        initSanPhamComponents();
        JPanel pnlSectionSanPham = createThongKeSection("2. THỐNG KÊ HOẠT ĐỘNG BÁN SẢN PHẨM", 
            new JLabel[]{sp_DoanhThu, sp_ChiPhi, sp_LoiNhuan, sp_KhachHang, sp_HoaDon, sp_SanPham}, 
            new String[]{"Doanh thu SP", "Chi phí SP", "Lợi nhuận SP", "KH mua SP", "Hóa đơn SP", "Số SP bán ra"}, 
            chartLineSanPham, txtDeXuatSanPham);

        initDichVuComponents();
        JPanel pnlSectionDichVu = createThongKeSection("3. THỐNG KÊ HOẠT ĐỘNG DỊCH VỤ", 
            new JLabel[]{dv_DoanhThu, dv_KhachHang, dv_HoaDon, dv_DichVu}, 
            new String[]{"Doanh thu DV", "KH dùng DV", "Hóa đơn DV", "Số lượt DV"}, 
            chartBarDichVu, txtDeXuatDichVu);

        pnlMainContent.add(pnlSectionTongQuan);
        pnlMainContent.add(Box.createVerticalStrut(20));
        pnlMainContent.add(pnlSectionSanPham);
        pnlMainContent.add(Box.createVerticalStrut(20));
        pnlMainContent.add(pnlSectionDichVu);

        JScrollPane mainScrollPane = new JScrollPane(pnlMainContent);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setBorder(null);
        add(mainScrollPane, BorderLayout.CENTER);

        // =========================================================
        // 3. XỬ LÝ SỰ KIỆN KHI BẤM NÚT THỐNG KÊ
        // =========================================================
        btnThongKe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thucHienThongKeTheoNam();
            }
        });
        
        // nam hien tai: mac dinh
        cboNam.setSelectedItem(LocalDate.now().getYear());
        thucHienThongKeTheoNam(); 
    }
    
    private void thucHienThongKeTheoNam() {
        int nam = (Integer) cboNam.getSelectedItem();
        updateDashboard(nam);
    }

    // ====================================================================
    // HAM TAO MOT KHOI (SECTION) CHUAN
    // ====================================================================
    private JPanel createThongKeSection(String titleStr, JLabel[] kpis, String[] kpiTitles, ChartPanel chart, JTextArea txtDeXuat) {
        JPanel pnlSection = new JPanel();
        pnlSection.setLayout(new BoxLayout(pnlSection, BoxLayout.Y_AXIS));
        pnlSection.setBackground(Color.WHITE);
        pnlSection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1), titleStr,
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(0, 102, 204)
        ));

        int soCot = kpis.length;
        JPanel pnlCards = new JPanel(new GridLayout(1, soCot, 15, 15));
        pnlCards.setBackground(Color.WHITE);
        pnlCards.setPreferredSize(new Dimension(0, 90));
        pnlCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        Color nenChanh = new Color(239, 227, 202);
        Color[] colors = {Color.GREEN, Color.ORANGE, new Color(0, 128, 0), new Color(100, 0, 100), new Color(50, 50, 50), new Color(50, 50, 50)};
        for (int i = 0; i < soCot; i++) pnlCards.add(createCard(kpiTitles[i], kpis[i], colors[i], nenChanh));

        JPanel pnlMiddle = new JPanel(new BorderLayout(15, 0));
        pnlMiddle.setBackground(Color.WHITE);
        JPanel pnlDeXuat = new JPanel(new BorderLayout());
        pnlDeXuat.setBorder(BorderFactory.createTitledBorder("Đề xuất & Nhận xét"));
        pnlDeXuat.setBackground(Color.WHITE);
        JScrollPane scrollDeXuat = new JScrollPane(txtDeXuat);
        boGocComponent(scrollDeXuat, 15);
        pnlDeXuat.add(scrollDeXuat);

        if (chart != null) {
            pnlMiddle.setPreferredSize(new Dimension(0, 350));
            pnlMiddle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
            pnlMiddle.add(chart, BorderLayout.CENTER);
            pnlDeXuat.setPreferredSize(new Dimension(350, 0)); 
            pnlMiddle.add(pnlDeXuat, BorderLayout.EAST);
        } else {
            pnlMiddle.setPreferredSize(new Dimension(0, 120)); 
            pnlMiddle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            pnlMiddle.add(pnlDeXuat, BorderLayout.CENTER);
        }

        pnlSection.add(Box.createVerticalStrut(10));
        pnlSection.add(pnlCards);
        pnlSection.add(Box.createVerticalStrut(15));
        pnlSection.add(pnlMiddle);
        pnlSection.add(Box.createVerticalStrut(10));

        return pnlSection;
    }

    // ====================================================================
    // CAC HAM KHOI TAO BIEN CHO TUNG PHAN
    // ====================================================================
    private void initTongQuanComponents() {
        tq_DoanhThu = createLabelValue(); tq_ChiPhi = createLabelValue();
        tq_LoiNhuan = createLabelValue(); tq_KhachHang = createLabelValue();
        tq_HoaDon = createLabelValue(); 
        chartLineTongQuan = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Tổng)", "Tháng");
        txtDeXuatTongQuan = createTextArea();
    }

    private void initSanPhamComponents() {
        sp_DoanhThu = createLabelValue(); sp_ChiPhi = createLabelValue();
        sp_LoiNhuan = createLabelValue(); sp_KhachHang = createLabelValue();
        sp_HoaDon = createLabelValue(); sp_SanPham = createLabelValue();
        chartLineSanPham = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Sản Phẩm)", "Tháng");
        txtDeXuatSanPham = createTextArea();
    }

    private void initDichVuComponents() {
        dv_DoanhThu = createLabelValue(); dv_KhachHang = createLabelValue(); 
        dv_HoaDon = createLabelValue(); dv_DichVu = createLabelValue();
        chartBarDichVu = createBarChartPanel("Top 5 Dịch Vụ Có Doanh Thu Cao Nhất");
        txtDeXuatDichVu = createTextArea();
    }

    private JLabel createLabelValue() {
        JLabel lbl = new JLabel("0", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        return lbl;
    }

    private JTextArea createTextArea() {
        JTextArea txt = new JTextArea("Đang tải dữ liệu...");
        txt.setEditable(false); txt.setLineWrap(true); txt.setWrapStyleWord(true);
        txt.setFont(new Font("Arial", Font.ITALIC, 14));
        return txt;
    }

    private JPanel createCard(String title, JLabel lblValue, Color titleColor, Color bgColor) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(bgColor);
        boGocComponent(p, 15);
        JLabel lblT = new JLabel(title, JLabel.CENTER);
        lblT.setForeground(titleColor); lblT.setFont(new Font("Arial", Font.BOLD, 14));
        p.add(lblT); p.add(lblValue); 
        return p;
    }

    private void boGocComponent(JComponent comp, int doCong) {
        comp.setBorder(new FlatLineBorder(new Insets(5, 10, 5, 10), new Color(200, 200, 200), 1, 15));
    }

    // ====================================================================
    // BIỂU ĐỒ
    // ====================================================================
    private ChartPanel createLineChartPanel(String title, String domainAxisLabel) {
        JFreeChart chart = ChartFactory.createLineChart(title, domainAxisLabel, "Số tiền (VNĐ)", new DefaultCategoryDataset());
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16)); 
        chart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);

        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        java.awt.Stroke stroke = new java.awt.BasicStroke(2.5f); 
        renderer.setSeriesStroke(0, stroke); renderer.setSeriesStroke(1, stroke); renderer.setSeriesStroke(2, stroke);
        renderer.setSeriesShapesVisible(0, true); renderer.setSeriesShapesVisible(1, true); renderer.setSeriesShapesVisible(2, true);
    
        renderer.setSeriesPaint(0, new Color(0, 150, 0)); 
        renderer.setSeriesPaint(1, new Color(255, 140, 0)); 
        renderer.setSeriesPaint(2, new Color(0, 100, 255)); 
        plot.setRenderer(renderer);
        
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false); 
        plot.setRangeGridlinePaint(new Color(220, 220, 220)); 
        
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 12));
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#,###"));
        
        ChartPanel cp = new ChartPanel(chart);
        cp.setMaximumDrawWidth(4000); cp.setMaximumDrawHeight(4000);
        cp.setMinimumDrawWidth(10); cp.setMinimumDrawHeight(10);
        return cp;
    }

    private ChartPanel createBarChartPanel(String title) {
        JFreeChart chart = ChartFactory.createBarChart(
            title, "", "Doanh thu (VNĐ)", new DefaultCategoryDataset(), 
            org.jfree.chart.plot.PlotOrientation.HORIZONTAL, false, true, false
        ); 
        chart.setAntiAlias(true); chart.setTextAntiAlias(true);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE); plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.BOLD, 12));
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
        
        org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 153, 204));
        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#,###"));
        
        ChartPanel cp = new ChartPanel(chart);
        cp.setMaximumDrawWidth(4000); cp.setMaximumDrawHeight(4000);
        cp.setMinimumDrawWidth(10); cp.setMinimumDrawHeight(10);
        return cp;
    }

    // ====================================================================
    // HÀM UPDATE DASHBOARD THEO NĂM
    // ====================================================================
    public void updateDashboard(int year) {
        txtDeXuatTongQuan.setText("Đang tải dữ liệu tổng quan...");
        txtDeXuatSanPham.setText("Đang tải dữ liệu sản phẩm...");
        txtDeXuatDichVu.setText("Đang tải dữ liệu dịch vụ...");
        
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            double dtTQ=0, cpTQ=0, lnTQ=0; int khTQ=0, hdTQ=0;
            double dtSP=0, cpSP=0, lnSP=0; int khSP=0, hdSP=0, slSP=0;
            double dtDV=0; int khDV=0, hdDV=0, slDV=0;
            Map<String, Double> mapTopDV;
            Map<Integer, Double> mapDoanhThuTQ, mapChiPhiTQ, mapLoiNhuanTQ;
            Map<Integer, Double> mapDoanhThuSP, mapChiPhiSP, mapLoiNhuanSP;

            @Override
            protected Void doInBackground() throws Exception {
                // Dùng DAO năm lấy dữ liệu
                dtTQ = tkDao.getTongDoanhThuTQ(year); cpTQ = tkDao.getTongChiPhiTQ(year); lnTQ = tkDao.getLoiNhuanTQ(year); khTQ = tkDao.getSoLuongKhachHangTQ(year); hdTQ = tkDao.getTongHoaDonTQ(year);
                mapDoanhThuTQ = tkDao.getDoanhThuTheoNamChartTQ(year); mapChiPhiTQ = tkDao.getChiPhiTheoNamChartTQ(year); mapLoiNhuanTQ = tkDao.getLoiNhuanTheoNamChartTQ(year);
                
                dtSP = tkDao.getTongDoanhThuSP(year); cpSP = tkDao.getTongChiPhiSP(year); lnSP = tkDao.getLoiNhuanSP(year); khSP = tkDao.getKhachHangMuaSP(year); hdSP = tkDao.getHoaDonSP(year); slSP = tkDao.getSoSpBanRa(year);
                mapDoanhThuSP = tkDao.getDoanhThuTheoNamChartSP(year); mapChiPhiSP = tkDao.getChiPhiTheoNamChartSP(year); mapLoiNhuanSP = tkDao.getLoiNhuanTheoNamChartSP(year);
                
                dtDV = tkDao.getTongDoanhThuDV(year); khDV = tkDao.getKhachHangDungDV(year); hdDV = tkDao.getHoaDonDV(year); slDV = tkDao.getSoLuotDV(year);
                mapTopDV = tkDao.getDoanhThuTop5DichVu(year);
                return null;
            }

            @Override
            protected void done() {
                try {
                    DecimalFormat df = new DecimalFormat("#,### đ");
                    
                    // --- Đổ dữ liệu KPI TỔNG QUAN
                    tq_DoanhThu.setText(df.format(dtTQ)); tq_ChiPhi.setText(df.format(cpTQ)); tq_LoiNhuan.setText(df.format(lnTQ)); tq_KhachHang.setText(khTQ + " khách"); tq_HoaDon.setText(hdTQ + " đơn");
                    tq_LoiNhuan.setForeground(lnTQ >= 0 ? new Color(0, 150, 0) : Color.RED);
                    
                    // --- Ve line chart tong quan 12 thang
                    DefaultCategoryDataset lineTqData = new DefaultCategoryDataset();
                    for (int thang = 1; thang <= 12; thang++) {
                        String ds = "T" + thang; 
                        lineTqData.addValue(mapDoanhThuTQ.getOrDefault(thang, 0.0), "Doanh thu", ds);
                        lineTqData.addValue(mapChiPhiTQ.getOrDefault(thang, 0.0), "Chi phí", ds);
                        lineTqData.addValue(mapLoiNhuanTQ.getOrDefault(thang, 0.0), "Lợi nhuận", ds);
                    }
                    chartLineTongQuan.getChart().getCategoryPlot().setDataset(lineTqData);

                    // --- do du lieu san pham vao bieu do
                    sp_DoanhThu.setText(df.format(dtSP)); sp_ChiPhi.setText(df.format(cpSP)); sp_LoiNhuan.setText(df.format(lnSP)); sp_KhachHang.setText(khSP + " khách"); sp_HoaDon.setText(hdSP + " đơn"); sp_SanPham.setText(slSP + " SP");
                    sp_LoiNhuan.setForeground(lnSP >= 0 ? new Color(0, 150, 0) : Color.RED);

                    // --- ve line chart san pham 12 thang
                    DefaultCategoryDataset lineSpData = new DefaultCategoryDataset();
                    for (int thang = 1; thang <= 12; thang++) {
                        String ds = "T" + thang; 
                        lineSpData.addValue(mapDoanhThuSP.getOrDefault(thang, 0.0), "Doanh thu SP", ds);
                        lineSpData.addValue(mapChiPhiSP.getOrDefault(thang, 0.0), "Chi phí SP", ds);
                        lineSpData.addValue(mapLoiNhuanSP.getOrDefault(thang, 0.0), "Lợi nhuận SP", ds);
                    }
                    chartLineSanPham.getChart().getCategoryPlot().setDataset(lineSpData);

                    // --- do du lieu dich vu vao bieu do
                    dv_DoanhThu.setText(df.format(dtDV)); dv_KhachHang.setText(khDV + " khách"); dv_HoaDon.setText(hdDV + " đơn"); dv_DichVu.setText(slDV + " lượt");

                    // ================= TẠO NHẬN XÉT ĐỘNG CHO TỔNG QUAN =================
                    if (hdTQ == 0) {
                        txtDeXuatTongQuan.setText("Năm nay chưa có giao dịch phát sinh. Cửa hàng cần lập tức xem xét lại chiến lược kinh doanh tổng thể.");
                    } else {
                        double aov = dtTQ / hdTQ;
                        double tySuatLN = dtTQ > 0 ? (lnTQ / dtTQ) * 100 : 0; 
                        double tanSuat = (double) hdTQ / khTQ;
                        
                        StringBuilder dx = new StringBuilder();
                        
                        dx.append("[*] TỔNG KẾT HIỆU QUẢ KINH DOANH NĂM:\n");
                        dx.append("  - Tỷ suất lợi nhuận: ").append(String.format("%.1f%%", tySuatLN));
                        if (tySuatLN >= 60) dx.append(" (Xuất Sắc)\n");
                        else if (tySuatLN >= 40) dx.append(" (Tốt)\n");
                        else if (tySuatLN >= 20) dx.append(" (Trung Bình)\n");
                        else if (tySuatLN > 0) dx.append(" (Cần Cải Thiện)\n");
                        else dx.append(" (Đang Lỗ)\n");
                        
                        dx.append("  - Khách chi tiêu TB: ").append(df.format(aov)).append("/đơn.\n");
                        dx.append("  - Tần suất quay lại TB: ").append(String.format("%.1f", tanSuat)).append(" lượt/khách/năm.\n\n");
                        
                        dx.append("[!] NHẬN ĐỊNH CHIẾN LƯỢC TỔNG THỂ:\n");
                        
                        // 2.1 Chiến lược theo Lợi nhuận
                        dx.append("\n1. Về Tỷ Suất Lợi Nhuận Năm:\n");
                        if (tySuatLN >= 60) {
                            dx.append("   + Cửa hàng đã kiểm soát chi phí rất xuất sắc trong năm qua. Dòng tiền dương khỏe mạnh là cơ sở để cân nhắc mở rộng quy mô hoặc đầu tư nâng cấp cơ sở vật chất.\n");
                        } else if (tySuatLN >= 40) {
                            dx.append("   + Biên lợi nhuận duy trì ở mức tốt trong năm. Cần tiếp tục duy trì và có thể đẩy mạnh ngân sách Marketing để mở rộng tệp khách hàng năm sau.\n");
                        } else if (tySuatLN >= 20) {
                            dx.append("   + Lợi nhuận cả năm ở mức trung bình, chủ yếu dòng tiền đến từ sản phẩm biên lợi nhuận thấp. Năm tới cần tối ưu hóa quy trình dịch vụ để tăng tỷ trọng mảng này.\n");
                        } else if (tySuatLN > 0) {
                            dx.append("   + BÁO ĐỘNG VÀNG: Cả năm kinh doanh có lãi nhưng quá mỏng, khó chống chịu rủi ro. Yêu cầu kiểm toán lại toàn bộ chi phí vận hành và giá vốn nhập hàng để cải thiện cho năm sau.\n");
                        } else {
                            dx.append("   + BÁO ĐỘNG ĐỎ: Năm qua cửa hàng hoạt động dưới mức hòa vốn. Cần tái cấu trúc mạnh mẽ, cắt bỏ các nhóm hàng/dịch vụ kém hiệu quả và xem xét lại định giá tổng thể.\n");
                        }
                        
                        // 2.2 Chiến lược theo AOV và Tần suất
                        dx.append("\n2. Về Hành Vi Tiêu Dùng:\n");
                        if (aov < 200000) {
                            dx.append("   + Mức chi tiêu trung bình năm khá thấp. Định hướng năm mới cần tập trung vào nghệ thuật bán chéo (Cross-selling) và tạo các Combo giá trị cao.\n");
                        } else {
                            dx.append("   + Khách hàng có xu hướng chi tiêu tốt. Cần phát triển thêm các gói dịch vụ Premium hoặc thẻ thành viên trả trước để chốt dòng tiền lớn.\n");
                        }
                        
                        txtDeXuatTongQuan.setText(dx.toString());
                        txtDeXuatTongQuan.setCaretPosition(0); 
                    }
                    
                    // ================= TẠO NHẬN XÉT ĐỘNG CHO SẢN PHẨM =================
                    if (dtSP == 0) {
                        txtDeXuatSanPham.setText("Năm nay không có doanh thu bán lẻ. Vui lòng kiểm tra lại việc cập nhật hệ thống hoặc xem xét tái cấu trúc mảng hàng hóa.");
                    } else {
                        double tySuatLnSP = (lnSP / dtSP) * 100; 
                        double spPerDon = (double) slSP / hdSP; 
                        double tyTrongSP = dtTQ > 0 ? (dtSP / dtTQ) * 100 : 100;
                        
                        StringBuilder dx = new StringBuilder();
                        
                        dx.append("[*] TỔNG KẾT MẢNG BÁN LẺ NĂM:\n");
                        dx.append("  - Tỷ trọng DT bán lẻ: ").append(String.format("%.1f%%", tyTrongSP)).append(" (so với tổng).\n");
                        dx.append("  - Tỷ suất LN bán lẻ: ").append(String.format("%.1f%%", tySuatLnSP));
                        if (tySuatLnSP >= 40) dx.append(" (Rất Tốt)\n");
                        else if (tySuatLnSP >= 20) dx.append(" (Ổn định)\n");
                        else if (tySuatLnSP > 0) dx.append(" (Lãi mỏng)\n");
                        else dx.append(" (Đang Lỗ)\n");
                        
                        dx.append("  - Khách mua trung bình: ").append(String.format("%.1f", spPerDon)).append(" món/đơn.\n\n");
                        
                        dx.append("[!] ĐÁNH GIÁ CƠ CẤU HÀNG HÓA:\n");
                        
                        if (tySuatLnSP >= 40) {
                            dx.append("   + Mảng hàng hóa sinh lời xuất sắc. Nguồn hàng phụ kiện/vật dụng hiện tại đang đem lại biên lợi nhuận cực tốt. Tiếp tục giữ mối với nhà cung cấp này.\n");
                        } else if (tySuatLnSP >= 20) {
                            dx.append("   + Hiệu quả bán lẻ ổn định, đóng vai trò quan trọng trong việc thu hút lượng traffic khách hàng đến shop thường xuyên.\n");
                        } else {
                            dx.append("   + Biên lợi nhuận bán lẻ mỏng. Có khả năng danh mục hàng hóa đang bị lệch về nhóm thức ăn/cát (giá vốn cao, lãi ít). Cần cân đối lại tỷ lệ nhập hàng hóa phụ kiện.\n");
                        }
                        
                        if (tyTrongSP > 70) {
                            dx.append("\n   + LƯU Ý: Cửa hàng đang quá thiên về bán lẻ. Đề xuất năm sau đẩy mạnh chuyển đổi tệp khách mua hàng sang sử dụng Dịch vụ để tối ưu lợi nhuận tổng thể.\n");
                        } else if (tyTrongSP < 30) {
                            dx.append("\n   + LƯU Ý: Không gian trưng bày có thể đang chưa tối ưu dẫn đến doanh thu bán lẻ khá khiêm tốn so với dịch vụ. Cần thiết kế lại kệ hàng thu hút hơn.\n");
                        }
                        
                        txtDeXuatSanPham.setText(dx.toString());
                        txtDeXuatSanPham.setCaretPosition(0); 
                    }
                    
                    // ================= TẠO NHẬN XÉT ĐỘNG CHO DỊCH VỤ =================
                    if (dtDV == 0) {
                        txtDeXuatDichVu.setText("Năm nay mảng dịch vụ không phát sinh doanh thu. Cần đánh giá lại nhu cầu khách hàng hoặc chất lượng dịch vụ hiện tại.");
                    } else {
                        double tyTrongDV = dtTQ > 0 ? (dtDV / dtTQ) * 100 : 100;
                        double tbLuot = slDV > 0 ? (dtDV / slDV) : 0; 
                        
                        String dvCaoNhat = ""; double tienCaoNhat = 0;
                        String dvThapNhat = ""; double tienThapNhat = 0;
                        
                        if (mapTopDV != null && !mapTopDV.isEmpty()) {
                            int count = 0;
                            int size = mapTopDV.size();
                            for (Map.Entry<String, Double> entry : mapTopDV.entrySet()) {
                                if (count == 0) {
                                    dvThapNhat = entry.getKey();
                                    tienThapNhat = entry.getValue();
                                }
                                if (count == size - 1) { 
                                    dvCaoNhat = entry.getKey();
                                    tienCaoNhat = entry.getValue();
                                }
                                count++;
                            }
                        }
                        
                        StringBuilder dx = new StringBuilder();
                        
                        dx.append("[*] TỔNG KẾT MẢNG DỊCH VỤ NĂM:\n");
                        dx.append("  - Tỷ trọng DT dịch vụ: ").append(String.format("%.1f%%", tyTrongDV)).append(" tổng DT.\n");
                        dx.append("  - Chi tiêu TB / lượt: ").append(df.format(tbLuot)).append(".\n");
                        dx.append("  - Dịch vụ TOP 1: ").append(dvCaoNhat).append(" (").append(df.format(tienCaoNhat)).append(")\n");
                        
                        if (!dvThapNhat.equals(dvCaoNhat)) {
                            dx.append("  - Dịch vụ kém nhất (Top 5): ").append(dvThapNhat).append(" (").append(df.format(tienThapNhat)).append(")\n\n");
                        } else {
                            dx.append("\n");
                        }
                        
                        dx.append("[!] ĐỊNH HƯỚNG MẢNG DỊCH VỤ:\n");
                        dx.append("   + Dịch vụ mũi nhọn ('").append(dvCaoNhat).append("') đang khẳng định sức hút thương hiệu. Đề xuất năm sau nâng cấp máy móc/vật tư cho mảng này để tăng giá trị trải nghiệm và có thể tăng nhẹ giá dịch vụ.\n\n");
                        
                        if (!dvThapNhat.equals(dvCaoNhat)) {
                            dx.append("   + Dịch vụ ('").append(dvThapNhat).append("') dù lọt Top 5 nhưng doanh thu khá xa so với Top 1. Cần khảo sát lại sự hài lòng của khách hàng để cải tiến quy trình hoặc đổi mới mỹ phẩm sử dụng.\n");
                        }
                        
                        txtDeXuatDichVu.setText(dx.toString());
                        txtDeXuatDichVu.setCaretPosition(0); 
                    }

                    // --- ve bar chart dich vu
                    DefaultCategoryDataset barDvData = new DefaultCategoryDataset();
                    for (Map.Entry<String, Double> entry : mapTopDV.entrySet()) {
                        barDvData.addValue(entry.getValue(), "Doanh thu", entry.getKey());
                    }
                    chartBarDichVu.getChart().getCategoryPlot().setDataset(barDvData);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}