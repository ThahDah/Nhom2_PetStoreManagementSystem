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
import java.util.HashMap;
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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.ui.FlatLineBorder;
import dao.ThongKeTheoTuanDAO;

@SuppressWarnings("serial")
public class PanelThongKeTuanGUI extends JPanel {
    
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
    
    private ThongKeTheoTuanDAO tkDao;
    
    public PanelThongKeTuanGUI() {
        tkDao = new ThongKeTheoTuanDAO(); 
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);

        // --- Phan tieu de
        JLabel lblHeader = new JLabel("Thống kê Kinh Doanh: Tuần này", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(189, 215, 238));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        // =========================================================
        // CONTAINER CHINH CHUA MOI NOI DUNG TRONG THANH CUON
        // =========================================================
        JPanel pnlMainContent = new JPanel();
        pnlMainContent.setLayout(new BoxLayout(pnlMainContent, BoxLayout.Y_AXIS));
        pnlMainContent.setBackground(Color.WHITE);
        pnlMainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. KHOI TAO VA THEM SECTION TONG QUAN
        initTongQuanComponents();
        JPanel pnlSectionTongQuan = createThongKeSection(
            "1. THỐNG KÊ TỔNG QUAN", 
            new JLabel[]{tq_DoanhThu, tq_ChiPhi, tq_LoiNhuan, tq_KhachHang, tq_HoaDon}, 
            new String[]{"Tổng doanh thu", "Tổng chi phí", "Lợi nhuận", "Khách hàng", "Tổng hóa đơn"}, 
            chartLineTongQuan, txtDeXuatTongQuan
        );

        // --- 2. KHOI TAO VA THEM SECTION SAN PHAM
        initSanPhamComponents();
        JPanel pnlSectionSanPham = createThongKeSection(
            "2. THỐNG KÊ HOẠT ĐỘNG BÁN SẢN PHẨM", 
            new JLabel[]{sp_DoanhThu, sp_ChiPhi, sp_LoiNhuan, sp_KhachHang, sp_HoaDon, sp_SanPham}, 
            new String[]{"Doanh thu SP", "Chi phí SP", "Lợi nhuận SP", "KH mua SP", "Hóa đơn SP", "Số SP bán ra"}, 
            chartLineSanPham, txtDeXuatSanPham 
        );

        // --- 3. KHOI TAO VA THEM SECTION DICH VU
        initDichVuComponents();
        JPanel pnlSectionDichVu = createThongKeSection(
            "3. THỐNG KÊ HOẠT ĐỘNG DỊCH VỤ", 
            new JLabel[]{dv_DoanhThu, dv_KhachHang, dv_HoaDon, dv_DichVu}, // Truyền 4 Label
            new String[]{"Doanh thu DV", "KH dùng DV", "Hóa đơn DV", "Số lượt DV"}, // Truyền 4 Title
            chartBarDichVu, txtDeXuatDichVu 
        );

        // Them 3 section vao man hinh chinh
        pnlMainContent.add(pnlSectionTongQuan);
        pnlMainContent.add(Box.createVerticalStrut(20)); // Khoang giua phan 1 va 2
        pnlMainContent.add(pnlSectionSanPham);
        pnlMainContent.add(Box.createVerticalStrut(20));
        pnlMainContent.add(pnlSectionDichVu);

        // Boc man hinh chinh trong thanh cuon
        JScrollPane mainScrollPane = new JScrollPane(pnlMainContent);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setBorder(null);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    // ====================================================================
    // HAM TAO MOT KHOI (SECTION) CHUAN DE DUNG CHUNG CHO 3 PHAN
    // ====================================================================
    private JPanel createThongKeSection(String titleStr, JLabel[] kpis, String[] kpiTitles, ChartPanel chart, JTextArea txtDeXuat) {
        JPanel pnlSection = new JPanel();
        pnlSection.setLayout(new BoxLayout(pnlSection, BoxLayout.Y_AXIS));
        pnlSection.setBackground(Color.WHITE);
        
        // Vien va tieu de cho tung SECTION
        pnlSection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1), titleStr,
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(0, 102, 204)
        ));

        // --- Khoi KPI Card
        int soCot = kpis.length;
        JPanel pnlCards = new JPanel(new GridLayout(1, soCot, 15, 15));
        pnlCards.setBackground(Color.WHITE);
        pnlCards.setPreferredSize(new Dimension(0, 90));
        pnlCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        Color nenChanh = new Color(239, 227, 202);
        Color[] colors = {Color.GREEN, Color.ORANGE, new Color(0, 128, 0), new Color(100, 0, 100), new Color(50, 50, 50), new Color(50, 50, 50)};
        
        for (int i = 0; i < soCot; i++) {
            pnlCards.add(createCard(kpiTitles[i], kpis[i], colors[i], nenChanh));
        }

        // --- KHOI BIEU DO VA DE XUAT
        JPanel pnlMiddle = new JPanel(new BorderLayout(15, 0));
        pnlMiddle.setBackground(Color.WHITE);
        
        JPanel pnlDeXuat = new JPanel(new BorderLayout());
        pnlDeXuat.setBorder(BorderFactory.createTitledBorder("Đề xuất & Nhận xét"));
        pnlDeXuat.setBackground(Color.WHITE);
        
        JScrollPane scrollDeXuat = new JScrollPane(txtDeXuat);
        boGocComponent(scrollDeXuat, 15);
        pnlDeXuat.add(scrollDeXuat);

        // Neu co bieu do: Trai bieu do - Phai de xuat
        if (chart != null) {
            pnlMiddle.setPreferredSize(new Dimension(0, 350));
            pnlMiddle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
            
            pnlMiddle.add(chart, BorderLayout.CENTER);
            pnlDeXuat.setPreferredSize(new Dimension(350, 0)); 
            pnlMiddle.add(pnlDeXuat, BorderLayout.EAST);
        } 
        // Neu khong co bieu do: gian toan bo theo chieu ngang
        else {
            pnlMiddle.setPreferredSize(new Dimension(0, 120)); 
            pnlMiddle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            pnlMiddle.add(pnlDeXuat, BorderLayout.CENTER);
        }

        // Ghep cac thanh phan vao SECTION
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
        tq_DoanhThu = createLabelValue(); 
        tq_ChiPhi = createLabelValue();
        tq_LoiNhuan = createLabelValue(); 
        tq_KhachHang = createLabelValue();
        tq_HoaDon = createLabelValue(); 
        chartLineTongQuan = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Tổng)");
        txtDeXuatTongQuan = createTextArea();
    }

    private void initSanPhamComponents() {
        sp_DoanhThu = createLabelValue(); 
        sp_ChiPhi = createLabelValue();
        sp_LoiNhuan = createLabelValue(); 
        sp_KhachHang = createLabelValue();
        sp_HoaDon = createLabelValue(); 
        sp_SanPham = createLabelValue();
        chartLineSanPham = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Sản Phẩm)");
        txtDeXuatSanPham = createTextArea();
    }

    private void initDichVuComponents() {
        dv_DoanhThu = createLabelValue(); 
        dv_KhachHang = createLabelValue(); 
        dv_HoaDon = createLabelValue(); 
        dv_DichVu = createLabelValue();
        
        chartBarDichVu = createBarChartPanel("Top 5 Dịch Vụ Có Doanh Thu Cao Nhất");
        txtDeXuatDichVu = createTextArea();
    }

    // ====================================================================
    // CAC HAM TAO UI, can chinh cac thanh phan
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
    // BIEU DO DUONG
    // ====================================================================
    private ChartPanel createLineChartPanel(String title) {
        JFreeChart chart = ChartFactory.createLineChart(title, "Ngày", "Số tiền (VNĐ)", new DefaultCategoryDataset());
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
    
        renderer.setSeriesPaint(0, new Color(0, 150, 0)); // Mau duong Doanh thu
        renderer.setSeriesPaint(1, new Color(255, 140, 0)); // Mau duong Chi phi
        renderer.setSeriesPaint(2, new Color(0, 100, 255)); // Mau duong Loi nhuan
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#,###"));
        return new ChartPanel(chart);
    }

    // ====================================================================
    // HAM DO DU LIEU LEN DASHBOARD
    // ====================================================================
    public void updateDashboard(LocalDate start, LocalDate end) {
        txtDeXuatTongQuan.setText("Đang tải dữ liệu tổng quan...");
        txtDeXuatSanPham.setText("Đang tải dữ liệu sản phẩm...");
        txtDeXuatDichVu.setText("Đang tải dữ liệu dịch vụ...");
        
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            // Biến số liệu TỔNG QUAN
            double dtTQ = 0, cpTQ = 0, lnTQ = 0; 
            int khTQ = 0, hdTQ = 0;
            
            // Biến số liệu SẢN PHẨM
            double dtSP = 0, cpSP = 0, lnSP = 0;
            int khSP = 0, hdSP = 0, slSP = 0;
            
            // Khai báo biến số liệu DỊCH VỤ
            double dtDV = 0;
            int khDV = 0, hdDV = 0, slDV = 0;
            
            // Map Top 5 DV
            Map<String, Double> mapTopDV;
            
            // Map biểu đồ đường
            Map<LocalDate, Double> mapDoanhThuTQ, mapChiPhiTQ, mapLoiNhuanTQ;
            Map<LocalDate, Double> mapDoanhThuSP, mapChiPhiSP, mapLoiNhuanSP;
            Map<LocalDate, Double> mapDoanhThuDV, mapChiPhiDV, mapLoiNhuanDV;

            @Override
            protected Void doInBackground() throws Exception {
                // 1. Lấy Data TỔNG QUAN
                dtTQ = tkDao.getTongDoanhThuTQ(start, end);
                cpTQ = tkDao.getTongChiPhiTQ(start, end);
                lnTQ = tkDao.getLoiNhuanTQ(start, end);
                khTQ = tkDao.getSoLuongKhachHangTQ(start, end);
                hdTQ = tkDao.getTongHoaDonTQ(start, end);
                
                
                mapDoanhThuTQ = tkDao.getDoanhThuTheoNgayChartTQ(start, end);
                mapChiPhiTQ = tkDao.getChiPhiTheoNgayChartTQ(start, end);
                mapLoiNhuanTQ = tkDao.getLoiNhuanTheoNgayChartTQ(start, end);
                
                // 2. Lấy Data SẢN PHẨM 
                dtSP = tkDao.getTongDoanhThuSP(start, end);
                cpSP = tkDao.getTongChiPhiSP(start, end); 
                lnSP = tkDao.getLoiNhuanSP(start, end);
                khSP = tkDao.getKhachHangMuaSP(start, end);
                hdSP = tkDao.getHoaDonSP(start, end);
                slSP = tkDao.getSoSpBanRa(start, end);
                
                mapDoanhThuSP = tkDao.getDoanhThuTheoNgayChartSP(start, end);
                mapChiPhiSP   = tkDao.getChiPhiTheoNgayChartSP(start, end);
                mapLoiNhuanSP = tkDao.getLoiNhuanTheoNgayChartSP(start, end);
                
                // 3. DỊCH VỤ
                // ==========================================
                dtDV = tkDao.getTongDoanhThuDV(start, end);
                khDV = tkDao.getKhachHangDungDV(start, end);
                hdDV = tkDao.getHoaDonDV(start, end);
                slDV = tkDao.getSoLuotDV(start, end);
                
                mapTopDV = tkDao.getDoanhThuTop5DichVu(start, end);
                
                return null;
            }

            @Override
            protected void done() {
                try {
                    DecimalFormat df = new DecimalFormat("#,### đ");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
                    
                    // ================== CẬP NHẬT UI TỔNG QUAN ==================
                    tq_DoanhThu.setText(df.format(dtTQ));
                    tq_ChiPhi.setText(df.format(cpTQ));
                    tq_LoiNhuan.setText(df.format(lnTQ));
                    tq_KhachHang.setText(khTQ + " khách");
                    tq_HoaDon.setText(hdTQ + " đơn");
                    tq_LoiNhuan.setForeground(lnTQ >= 0 ? new Color(0, 150, 0) : Color.RED);
                    
                    if (lnTQ > 0) txtDeXuatTongQuan.setText("Doanh thu tốt, đạt lợi nhuận " + df.format(lnTQ) + ".");
                    else if (lnTQ < 0) txtDeXuatTongQuan.setText("Lợi nhuận đang âm (" + df.format(lnTQ) + "), cần rà soát lại chi phí.");
                    else txtDeXuatTongQuan.setText("Chưa có giao dịch trong tuần này.");

                    DefaultCategoryDataset lineTqData = new DefaultCategoryDataset();
                    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                        String ds = date.format(formatter); 
                        lineTqData.addValue(mapDoanhThuTQ.getOrDefault(date, 0.0), "Doanh thu", ds);
                        lineTqData.addValue(mapChiPhiTQ.getOrDefault(date, 0.0), "Chi phí", ds);
                        lineTqData.addValue(mapLoiNhuanTQ.getOrDefault(date, 0.0), "Lợi nhuận", ds);
                    }
                    chartLineTongQuan.getChart().getCategoryPlot().setDataset(lineTqData);

                    // ================== CẬP NHẬT UI SẢN PHẨM ==================
                    sp_DoanhThu.setText(df.format(dtSP)); 
                    sp_ChiPhi.setText(df.format(cpSP)); 
                    sp_LoiNhuan.setText(df.format(lnSP));
                    
                    // Gắn dữ liệu cho 3 thẻ KPI mới
                    sp_KhachHang.setText(khSP + " khách");
                    sp_HoaDon.setText(hdSP + " đơn");
                    sp_SanPham.setText(slSP + " SP");
                    
                    sp_LoiNhuan.setForeground(lnSP >= 0 ? new Color(0, 150, 0) : Color.RED);
                    txtDeXuatSanPham.setText("Đề xuất mảng sản phẩm: " + (dtSP > 0 ? "Tiếp tục đẩy mạnh bán hàng." : "Chưa có lượt mua."));

                    DefaultCategoryDataset lineSpData = new DefaultCategoryDataset();
                    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                        String ds = date.format(formatter); 
                        lineSpData.addValue(mapDoanhThuSP.getOrDefault(date, 0.0), "Doanh thu SP", ds);
                        lineSpData.addValue(mapChiPhiSP.getOrDefault(date, 0.0), "Chi phí SP", ds);
                        lineSpData.addValue(mapLoiNhuanSP.getOrDefault(date, 0.0), "Lợi nhuận SP", ds);
                    }
                    chartLineSanPham.getChart().getCategoryPlot().setDataset(lineSpData);

                    // ================== CẬP NHẬT UI DỊCH VỤ ==================
                    dv_DoanhThu.setText(df.format(dtDV)); 
                    dv_KhachHang.setText(khDV + " khách");
                    dv_HoaDon.setText(hdDV + " đơn");
                    dv_DichVu.setText(slDV + " lượt");
                    
                    txtDeXuatDichVu.setText("Đề xuất mảng dịch vụ: " + (dtDV > 0 ? "Tập trung chăm sóc khách hàng và nâng cao chất lượng các dịch vụ thuộc Top đầu." : "Chưa có lượt đặt dịch vụ nào trong thời gian này."));
                    
                    // Gắn data vào Biểu Đồ Cột Top 5
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
    
 // BIỂU ĐỒ CỘT (Dùng cho Top 5 Dịch Vụ)
    // ====================================================================
    private ChartPanel createBarChartPanel(String title) {
        JFreeChart chart = ChartFactory.createBarChart(
            title, 
            "", 
            "Doanh thu (VNĐ)", 
            new DefaultCategoryDataset(), 
            org.jfree.chart.plot.PlotOrientation.HORIZONTAL, // Đặt biểu đồ nằm ngang cho dễ đọc tên dịch vụ dài
            false, 
            true, 
            false
        ); 
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Chỉnh màu xanh dương cho các cột
        org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 153, 204)); 
        
        // Format số tiền (thêm dấu phẩy phân cách hàng nghìn)
        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#,###"));
        
        return new ChartPanel(chart);
    }
}