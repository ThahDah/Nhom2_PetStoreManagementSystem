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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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

import dao.ThongKeTheoThangDAO;

@SuppressWarnings("serial")
public class PanelThongKeThangGUI extends JPanel {
	private ThongKeTheoThangDAO tkDao;
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
    
    // --- BỘ LỌC THÁNG NĂM ---
    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private JButton btnThongKe;
    
    public PanelThongKeThangGUI() {
    		tkDao = new ThongKeTheoThangDAO(); 
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);

        // =========================================================
        // 1. PHẦN HEADER & BỘ LỌC CHỌN THÁNG/NĂM
        // =========================================================
        JPanel pnlHeaderWrapper = new JPanel(new BorderLayout());
        pnlHeaderWrapper.setBackground(Color.WHITE);
        
        JLabel lblHeader = new JLabel("Thống kê Kinh Doanh: Theo Tháng", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(189, 215, 238));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlHeaderWrapper.add(lblHeader, BorderLayout.NORTH);
        
        // Panel chứa 2 ComboBox
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        
        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);
        
        
        cboNam = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 3; i <= currentYear + 1; i++) cboNam.addItem(i); // Các năm từ quá khứ đến tương lai gần
        
        btnThongKe = new JButton("Thống Kê");
        btnThongKe.setBackground(new Color(0, 153, 204));
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFont(new Font("Arial", Font.BOLD, 14));
        
        pnlFilter.add(new JLabel("Chọn tháng:"));
        pnlFilter.add(cboThang);
        pnlFilter.add(new JLabel("Chọn năm:"));
        pnlFilter.add(cboNam);
        pnlFilter.add(btnThongKe);
        pnlHeaderWrapper.add(pnlFilter, BorderLayout.SOUTH);
        
        add(pnlHeaderWrapper, BorderLayout.NORTH);

        // =========================================================
        // 2. CONTAINER CHÍNH (GIỮ NGUYÊN)
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
                thucHienThongKeTheoThang();
            }
        });
        
        //mac dinh thang va nam hien tai
        LocalDate now = LocalDate.now();
        cboThang.setSelectedItem(now.getMonthValue());
        cboNam.setSelectedItem(now.getYear());
        thucHienThongKeTheoThang(); 
    }
    
    private void thucHienThongKeTheoThang() {
        int thang = (Integer) cboThang.getSelectedItem();
        int nam = (Integer) cboNam.getSelectedItem();
        
        YearMonth yearMonth = YearMonth.of(nam, thang);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth(); 
        
        //truyen vao thoi gian
        updateDashboard(start, end);
    }

    // ====================================================================
    // HAM TAO MOT KHOI (SECTION) CHUAN (Giữ nguyên)
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
        chartLineTongQuan = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Tổng)");
        txtDeXuatTongQuan = createTextArea();
    }

    private void initSanPhamComponents() {
        sp_DoanhThu = createLabelValue(); sp_ChiPhi = createLabelValue();
        sp_LoiNhuan = createLabelValue(); sp_KhachHang = createLabelValue();
        sp_HoaDon = createLabelValue(); sp_SanPham = createLabelValue();
        chartLineSanPham = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Sản Phẩm)");
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
//        txt.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
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
    // BIỂU ĐỒ (Đã bao gồm Code tối ưu nét vẽ như lần trước)
    // ====================================================================
    private ChartPanel createLineChartPanel(String title) {
        JFreeChart chart = ChartFactory.createLineChart(title, "Ngày", "Số tiền (VNĐ)", new DefaultCategoryDataset());
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16)); 
        chart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE); // Bỏ viền đen của legend

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
        
        // thu nho truc X
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
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
    // HÀM UPDATE DASHBOARD (Giữ nguyên cấu trúc, đổi format ngày)
    // ====================================================================
    public void updateDashboard(LocalDate start, LocalDate end) {
        txtDeXuatTongQuan.setText("Đang tải dữ liệu tổng quan...");
        txtDeXuatSanPham.setText("Đang tải dữ liệu sản phẩm...");
        txtDeXuatDichVu.setText("Đang tải dữ liệu dịch vụ...");
        
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            double dtTQ=0, cpTQ=0, lnTQ=0; int khTQ=0, hdTQ=0;
            double dtSP=0, cpSP=0, lnSP=0; int khSP=0, hdSP=0, slSP=0;
            double dtDV=0; int khDV=0, hdDV=0, slDV=0;
            Map<String, Double> mapTopDV;
            Map<LocalDate, Double> mapDoanhThuTQ, mapChiPhiTQ, mapLoiNhuanTQ;
            Map<LocalDate, Double> mapDoanhThuSP, mapChiPhiSP, mapLoiNhuanSP;

            @Override
            protected Void doInBackground() throws Exception {
                //dung DAO lay du lieu
                dtTQ = tkDao.getTongDoanhThuTQ(start, end); cpTQ = tkDao.getTongChiPhiTQ(start, end); lnTQ = tkDao.getLoiNhuanTQ(start, end); khTQ = tkDao.getSoLuongKhachHangTQ(start, end); hdTQ = tkDao.getTongHoaDonTQ(start, end);
                mapDoanhThuTQ = tkDao.getDoanhThuTheoNgayChartTQ(start, end); mapChiPhiTQ = tkDao.getChiPhiTheoNgayChartTQ(start, end); mapLoiNhuanTQ = tkDao.getLoiNhuanTheoNgayChartTQ(start, end);
                
                dtSP = tkDao.getTongDoanhThuSP(start, end); cpSP = tkDao.getTongChiPhiSP(start, end); lnSP = tkDao.getLoiNhuanSP(start, end); khSP = tkDao.getKhachHangMuaSP(start, end); hdSP = tkDao.getHoaDonSP(start, end); slSP = tkDao.getSoSpBanRa(start, end);
                mapDoanhThuSP = tkDao.getDoanhThuTheoNgayChartSP(start, end); mapChiPhiSP = tkDao.getChiPhiTheoNgayChartSP(start, end); mapLoiNhuanSP = tkDao.getLoiNhuanTheoNgayChartSP(start, end);
                
                dtDV = tkDao.getTongDoanhThuDV(start, end); khDV = tkDao.getKhachHangDungDV(start, end); hdDV = tkDao.getHoaDonDV(start, end); slDV = tkDao.getSoLuotDV(start, end);
                mapTopDV = tkDao.getDoanhThuTop5DichVu(start, end);
                return null;
            }

            @Override
            protected void done() {
                try {
                    DecimalFormat df = new DecimalFormat("#,### đ");
                    
                    // Do tháng có tới 31 ngày, hiển thị "dd/MM" sẽ làm trục X dài và đè chữ.
                    // Đổi sang chỉ hiện ngày (VD: "01", "02", "31") để thoáng biểu đồ.
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
                    
                    tq_DoanhThu.setText(df.format(dtTQ)); tq_ChiPhi.setText(df.format(cpTQ)); tq_LoiNhuan.setText(df.format(lnTQ)); tq_KhachHang.setText(khTQ + " khách"); tq_HoaDon.setText(hdTQ + " đơn");
                    tq_LoiNhuan.setForeground(lnTQ >= 0 ? new Color(0, 150, 0) : Color.RED);
                    
                    DefaultCategoryDataset lineTqData = new DefaultCategoryDataset();
                    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                        String ds = date.format(formatter); 
                        lineTqData.addValue(mapDoanhThuTQ.getOrDefault(date, 0.0), "Doanh thu", ds);
                        lineTqData.addValue(mapChiPhiTQ.getOrDefault(date, 0.0), "Chi phí", ds);
                        lineTqData.addValue(mapLoiNhuanTQ.getOrDefault(date, 0.0), "Lợi nhuận", ds);
                    }
                    chartLineTongQuan.getChart().getCategoryPlot().setDataset(lineTqData);

                    sp_DoanhThu.setText(df.format(dtSP)); sp_ChiPhi.setText(df.format(cpSP)); sp_LoiNhuan.setText(df.format(lnSP)); sp_KhachHang.setText(khSP + " khách"); sp_HoaDon.setText(hdSP + " đơn"); sp_SanPham.setText(slSP + " SP");
                    sp_LoiNhuan.setForeground(lnSP >= 0 ? new Color(0, 150, 0) : Color.RED);

                    DefaultCategoryDataset lineSpData = new DefaultCategoryDataset();
                    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                        String ds = date.format(formatter); 
                        lineSpData.addValue(mapDoanhThuSP.getOrDefault(date, 0.0), "Doanh thu SP", ds);
                        lineSpData.addValue(mapChiPhiSP.getOrDefault(date, 0.0), "Chi phí SP", ds);
                        lineSpData.addValue(mapLoiNhuanSP.getOrDefault(date, 0.0), "Lợi nhuận SP", ds);
                    }
                    chartLineSanPham.getChart().getCategoryPlot().setDataset(lineSpData);

                    dv_DoanhThu.setText(df.format(dtDV)); dv_KhachHang.setText(khDV + " khách"); dv_HoaDon.setText(hdDV + " đơn"); dv_DichVu.setText(slDV + " lượt");

                 // ================= TẠO NHẬN XÉT ĐỘNG CHO TỔNG QUAN =================
                    if (hdTQ == 0) {
                        txtDeXuatTongQuan.setText("Tháng này chưa có giao dịch phát sinh. Cửa hàng cần lập tức triển khai các chiến dịch quảng cáo, Flash Sale hoặc liên hệ lại tệp khách hàng cũ.");
                    } else {
                        double aov = dtTQ / hdTQ;
                        double tySuatLN = dtTQ > 0 ? (lnTQ / dtTQ) * 100 : 0; 
                        double tanSuat = (double) hdTQ / khTQ;
                        
                        StringBuilder dx = new StringBuilder();
                        
                        dx.append("[*] ĐÁNH GIÁ TỔNG QUAN HIỆU QUẢ:\n");
                        dx.append("  - Tỷ suất lợi nhuận: ").append(String.format("%.1f%%", tySuatLN));
                        if (tySuatLN >= 60) dx.append(" (Xuất Sắc)\n");
                        else if (tySuatLN >= 40) dx.append(" (Tốt)\n");
                        else if (tySuatLN >= 20) dx.append(" (Trung Bình)\n");
                        else if (tySuatLN > 0) dx.append(" (Cần Cải Thiện)\n");
                        else dx.append(" (Đang Lỗ)\n");
                        
                        dx.append("  - Khách chi tiêu TB: ").append(df.format(aov)).append("/đơn.\n");
                        dx.append("  - Tần suất quay lại: ").append(String.format("%.1f", tanSuat)).append(" lượt/khách.\n\n");
                        
                        dx.append("[!] ĐỀ XUẤT CHIẾN LƯỢC HÀNH ĐỘNG:\n");
                        
                        // 2.1 Chiến lược theo Lợi nhuận
                        dx.append("\n1. Về Tỷ Suất Lợi Nhuận:\n");
                        if (tySuatLN >= 60) {
                            dx.append("   + Cửa hàng đang tối ưu chi phí rất tốt. Có thể trích một phần lợi nhuận để chạy Ads đa kênh hoặc tổ chức Minigame mở rộng tệp khách hàng mới.\n");
                        } else if (tySuatLN >= 40) {
                            dx.append("   + Biên lợi nhuận ổn định. Tiếp tục duy trì cơ cấu tỷ trọng Sản phẩm / Dịch vụ hiện tại.\n");
                        } else if (tySuatLN >= 20) {
                            dx.append("   + Lợi nhuận trung bình, cho thấy shop đang bán nhiều hàng hóa hóa (lãi mỏng) hơn là Dịch vụ. Cần đào tạo nhân viên kịch bản Cross-sell: Khách mua sữa tắm -> Rủ trải nghiệm Spa cắt tỉa lông.\n");
                        } else if (tySuatLN > 0) {
                            dx.append("   + BÁO ĐỘNG VÀNG: Bán được hàng nhưng tiền lãi quá mỏng. Phải rà soát ngay hao hụt kho bãi, đàm phán lại giá nhập với Supplier, hoặc giảm tỷ lệ tặng quà/voucher.\n");
                        } else {
                            dx.append("   + BÁO ĐỘNG ĐỎ: Kinh doanh dưới mức hòa vốn! Ngừng ngay các chương trình giảm giá sâu. Rà soát xem dịch vụ nào đang tốn nhiều chi phí vật tư/nhân công mà giá thu về quá rẻ để điều chỉnh giá niêm yết.\n");
                        }
                        
                        // 2.2 Chiến lược Kích cầu theo AOV
                        dx.append("\n2. Về Mức Chi Tiêu:\n");
                        if (aov < 150000) {
                            dx.append("   + Khách chủ yếu mua lắt nhắt đồ lặt vặt rẻ tiền. Cần đóng gói COMBO (VD: Hạt + Cát vệ sinh + Súp thưởng) rẻ hơn mua lẻ 5% để đẩy mức chi tiêu lên 200k - 250k.\n");
                        } else if (aov < 350000) {
                            double mucKichCau = aov + 100000; 
                            dx.append("   + Mức chi tiêu tầm trung. Đặt chính sách 'Freeship / Tặng voucher 30k' cho hóa đơn từ ").append(df.format(mucKichCau)).append(" để dụ khách nhặt thêm đồ chơi hoặc phụ kiện.\n");
                        } else if (aov < 800000) {
                            dx.append("   + Khách chịu chi khá tốt. Nhân viên có thể mạnh dạn giới thiệu các gói Dịch vụ Spa cao cấp hoặc thức ăn phân khúc Premium (Royal Canin, Hạt không độn ngũ cốc).\n");
                        } else {
                            dx.append("   + Tệp khách hàng VIP. Không cần lạm dụng giảm giá, hãy tập trung vào Dịch Vụ Hậu Mãi, ưu tiên xếp lịch hẹn trước, tặng quà sinh nhật thú cưng để tối đa hóa trải nghiệm.\n");
                        }
                        
                        // 2.3 Chiến lược Giữ chân khách hàng theo Tần suất 
                        dx.append("\n3. Về Độ Trung Thành Của Khách Hàng:\n");
                        if (tanSuat == 1.0) {
                            dx.append("   + Khách vãng lai mua 1 lần rồi đi (Tỷ lệ rời bỏ cao). Cần gọi điện (Telesale) cho 10-20 khách gần nhất để xin Feedback chân thực về trải nghiệm và tặng mã giảm 15% mời họ quay lại.\n");
                        } else if (tanSuat < 1.3) {
                            dx.append("   + Tỷ lệ quay lại còn thấp. Nên áp dụng chiến thuật tặng Voucher 'Giảm giá 10% cho dịch vụ tiếp theo (HSD 30 ngày)' in trực tiếp trên hóa đơn giấy để hối thúc khách quay lại.\n");
                        } else if (tanSuat <= 2.0) {
                            dx.append("   + Tần suất quay lại khá khỏe (1-2 lần/kỳ). Hãy thúc đẩy khách mở Thẻ Thành Viên (Tích điểm quy đổi tiền trừ thẳng vào hóa đơn) để tạo thói quen mua sắm cố định tại cửa hàng.\n");
                        } else {
                            dx.append("   + Khách hàng cực kỳ trung thành (>2 lần/kỳ). Có thể mời họ mua Gói Dịch Vụ Trả Trước (VD: Mua 10 buổi tắm tặng 2 buổi) để thu về một lượng tiền mặt lớn cho cửa hàng.\n");
                        }
                        
                        txtDeXuatTongQuan.setText(dx.toString());
                        txtDeXuatTongQuan.setCaretPosition(0); 
                    }
                    
                 // ================= TẠO NHẬN XÉT ĐỘNG CHO SẢN PHẨM =================
                    if (dtSP == 0) {
                        txtDeXuatSanPham.setText("Tháng này mảng bán lẻ đang đóng băng. Cần kiểm tra lại tồn kho, rà soát hạn sử dụng hàng hóa (date) và chạy ngay chương trình Flash Sale để giải phóng mặt bằng.");
                    } else {
                        double tySuatLnSP = (lnSP / dtSP) * 100; 
                        double spPerDon = (double) slSP / hdSP; 
                        double tyTrongSP = dtTQ > 0 ? (dtSP / dtTQ) * 100 : 100;
                        
                        StringBuilder dx = new StringBuilder();
                        
                        dx.append("[*] ĐÁNH GIÁ HIỆU QUẢ BÁN LẺ:\n");
                        dx.append("  - Đóng góp doanh thu: ").append(String.format("%.1f%%", tyTrongSP)).append(" (so với tổng).\n");
                        dx.append("  - Tỷ suất lợi nhuận SP: ").append(String.format("%.1f%%", tySuatLnSP));
                        if (tySuatLnSP >= 40) dx.append(" (Rất Tốt)\n");
                        else if (tySuatLnSP >= 20) dx.append(" (Ổn định)\n");
                        else if (tySuatLnSP > 0) dx.append(" (Lãi mỏng)\n");
                        else dx.append(" (Đang Lỗ)\n");
                        
                        dx.append("  - Khách mua trung bình: ").append(String.format("%.1f", spPerDon)).append(" món/đơn.\n\n");
                        
                        // --- PHẦN 2: PHÂN TÍCH & ĐỀ XUẤT CHI TIẾT ---
                        dx.append("[!] ĐỀ XUẤT CHIẾN LƯỢC HÀNG HÓA:\n");
                        
                        // 2.1 Chiến lược theo Lợi nhuận (Biên độ lãi của phụ kiện vs thức ăn)
                        dx.append("\n1. Về Lợi Nhuận Bán Lẻ:\n");
                        if (tySuatLnSP >= 45) {
                            dx.append("   + Cửa hàng đang bán rất tốt các mặt hàng có biên siêu lợi nhuận (Phụ kiện, Đồ chơi, Quần áo, Sữa tắm). Cần đẩy mạnh trưng bày nhóm này ngay cạnh quầy thu ngân để kích thích mua ngẫu hứng.\n");
                        } else if (tySuatLnSP >= 20) {
                            dx.append("   + Sinh lời ổn định. Phù hợp với mặt bằng chung bán lẻ thức ăn/cát vệ sinh thú cưng hiện nay.\n");
                        } else if (tySuatLnSP > 0) {
                            dx.append("   + Lãi khá mỏng. Shop đang bị sa đà vào bán thức ăn hạt/pate (nhóm hàng thu hút khách nhưng ít lãi). Cần yêu cầu nhân viên tư vấn khách mua kèm thêm vitamin, canxi, hoặc phụ kiện để bù đắp lợi nhuận.\n");
                        } else {
                            dx.append("   + BÁO ĐỘNG ĐỎ: Mảng bán lẻ đang lỗ. Có thể do đang xả hàng cận date giá rẻ hoặc nhân viên nhập sai giá vốn. Cần rà soát lại bảng giá SanPham trong CSDL ngay lập tức.\n");
                        }
                        
                        // 2.2 Phân tích hành vi nhặt hàng (Cross-selling)
                        dx.append("\n2. Về Hành Vi Mua Sắm:\n");
                        if (spPerDon <= 1.2) {
                            dx.append("   + Khách chủ yếu vào nhặt đúng 1 món rồi về. Cần thiết kế ngay các gói COMBO (Ví dụ: 1 túi hạt + 1 gói súp thưởng giá rẻ hơn mua lẻ 5%) hoặc 'Mua 2 tặng 1' để đẩy số lượng tồn kho.\n");
                        } else if (spPerDon <= 2.5) {
                            dx.append("   + Khách có thói quen nhặt thêm 1-2 món lẻ. Nhân viên thu ngân nên áp dụng kịch bản hỏi thêm: 'Nhà mình còn cát vệ sinh không ạ?' trước khi bấm in hóa đơn để chốt thêm đồ.\n");
                        } else {
                            dx.append("   + Kỹ năng bán chéo (Cross-sell) xuất sắc. Khách đang mua sắm tại đây như một siêu thị thú cưng thu nhỏ. Chú ý theo dõi tồn kho các mã bán chạy (như SPCC0001, SPCM0003) để nhập bù kịp thời.\n");
                        }
                        
                        // 2.3 Phân tích cơ cấu (Bán chéo sang Dịch vụ)
                        dx.append("\n3. Về Cơ Cấu Kinh Doanh:\n");
                        if (tyTrongSP > 75) {
                            dx.append("   + Shop đang phụ thuộc quá mức vào bán lẻ. Hãy tận dụng tệp khách mua hàng này: Tặng họ voucher 'Giảm 20% lần đầu trải nghiệm Spa/Tỉa lông' để chuyển đổi họ sang dùng mảng Dịch vụ.\n");
                        } else if (tyTrongSP < 25) {
                            dx.append("   + Bán lẻ đang quá lép vế so với Dịch vụ. Hãy đào tạo thợ Spa kỹ năng tư vấn đồ ở nhà: Bé tắm xong lông rất mượt -> Mời khách mua luôn chai sữa tắm dưỡng lông đó mang về.\n");
                        } else {
                            dx.append("   + Tỷ trọng Doanh thu Sản phẩm và Dịch vụ đang cân bằng, mô hình kinh doanh vòng lặp (Mua sắm - Chăm sóc) hoạt động cực kỳ hiệu quả.\n");
                        }
                        
                        txtDeXuatSanPham.setText(dx.toString());
                        txtDeXuatSanPham.setCaretPosition(0); 
                    }
                    
                 // ================= TẠO NHẬN XÉT ĐỘNG CHO DỊCH VỤ =================
                    if (dtDV == 0) {
                        txtDeXuatDichVu.setText("Tháng này chưa có lượt đặt dịch vụ nào. Khuyên gọi điện chăm sóc lại tệp khách hàng cũ hoặc tặng mã giảm giá trải nghiệm Spa.");
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
                        
                        dx.append("[*] ĐÁNH GIÁ HIỆU QUẢ DỊCH VỤ:\n");
                        dx.append("  - Đóng góp doanh thu: ").append(String.format("%.1f%%", tyTrongDV)).append(" tổng DT.\n");
                        dx.append("  - Chi tiêu trung bình: ").append(df.format(tbLuot)).append("/lượt.\n");
                        dx.append("  - HOT nhất: ").append(dvCaoNhat).append(" (").append(df.format(tienCaoNhat)).append(")\n");
                        
                        if (!dvThapNhat.equals(dvCaoNhat)) {
                            dx.append("  - Ít khách nhất: ").append(dvThapNhat).append(" (").append(df.format(tienThapNhat)).append(")\n\n");
                        } else {
                            dx.append("\n");
                        }
                        
                        dx.append("[!] GỢI Ý CHIẾN LƯỢC THÁNG TỚI:\n");
                        
                        dx.append("  1. Tối ưu dịch vụ mũi nhọn ('").append(dvCaoNhat).append("'):\n");
                        dx.append("     + Đây là dịch vụ mang lại dòng tiền chính. Cần đảm bảo lịch trực của nhân viên đủ để đáp ứng. Đào tạo thợ Spa kịch bản Upsell: Khách làm xong '").append(dvCaoNhat).append("' thì mời mua thêm xịt dưỡng lông, sữa tắm mang về.\n\n");
                        
                        if (!dvThapNhat.equals(dvCaoNhat)) {
                            dx.append("  2. Cải thiện dịch vụ yếu ('").append(dvThapNhat).append("'):\n");
                            dx.append("     + Doanh thu quá thấp, có thể do giá chưa hợp lý hoặc khách không thấy cần thiết. Tháng tới nên tạo gói COMBO (Ví dụ: Đặt '").append(dvCaoNhat).append("' sẽ được giảm giá 50% khi mua kèm '").append(dvThapNhat).append("') để kéo lượt sử dụng lên.\n\n");
                        }
                        
                        dx.append("  3. Chăm sóc khách hàng Dịch vụ:\n");
                        if (slDV > hdDV) { 
                            dx.append("     + Khách đang có xu hướng mang nhiều thú cưng đến cùng lúc hoặc làm nhiều dịch vụ/đơn. Khuyên dùng chính sách ưu đãi 'Làm đẹp thú cưng thứ 2 giảm 10%' để duy trì thói quen này.");
                        } else {
                            dx.append("     + Cuối tháng nên lọc danh sách khách hàng đã làm dịch vụ cách đây > 3 tuần, nhân viên gọi điện nhắc lịch: 'Đã đến lúc dẫn bé đi tắm/cắt tỉa lông lại' để lấp đầy lịch trống tháng sau.");
                        }
                        
                        txtDeXuatDichVu.setText(dx.toString());
                        txtDeXuatDichVu.setCaretPosition(0); 
                    }

                    DefaultCategoryDataset barDvData = new DefaultCategoryDataset();
                    for (Map.Entry<String, Double> entry : mapTopDV.entrySet()) barDvData.addValue(entry.getValue(), "Doanh thu", entry.getKey());
                    chartBarDichVu.getChart().getCategoryPlot().setDataset(barDvData);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}