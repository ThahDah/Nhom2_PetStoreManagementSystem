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
    private JLabel tq_DoanhThu, tq_ChiPhi, tq_LoiNhuan, tq_KhachHang, tq_HoaDon, tq_SanPham;
    private ChartPanel chartLineTongQuan;
    private JTextArea txtDeXuatTongQuan;
    
    // --- khai bao bien phan 2: san pham
    private JLabel sp_DoanhThu, sp_ChiPhi, sp_LoiNhuan, sp_KhachHang, sp_HoaDon, sp_SanPham;
    private ChartPanel chartLineSanPham; // Đã thêm lại Chart cho SP
    private JTextArea txtDeXuatSanPham;

    // --- khai bao bien phan 3: dich vu
    private JLabel dv_DoanhThu, dv_ChiPhi, dv_LoiNhuan, dv_KhachHang, dv_HoaDon, dv_DichVu;
    private ChartPanel chartLineDichVu; // Đã thêm lại Chart cho DV
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
            new JLabel[]{tq_DoanhThu, tq_ChiPhi, tq_LoiNhuan, tq_KhachHang, tq_HoaDon, tq_SanPham}, 
            new String[]{"Tổng doanh thu", "Tổng chi phí", "Lợi nhuận", "Khách hàng", "Tổng hóa đơn", "Tổng SP & DV"}, 
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
            new JLabel[]{dv_DoanhThu, dv_ChiPhi, dv_LoiNhuan, dv_KhachHang, dv_HoaDon, dv_DichVu}, 
            new String[]{"Doanh thu DV", "Chi phí DV", "Lợi nhuận DV", "KH dùng DV", "Hóa đơn DV", "Số lượt DV"}, 
            chartLineDichVu, txtDeXuatDichVu 
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
        JPanel pnlCards = new JPanel(new GridLayout(1, 6, 15, 15));
        pnlCards.setBackground(Color.WHITE);
        pnlCards.setPreferredSize(new Dimension(0, 90));
        pnlCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        Color nenChanh = new Color(239, 227, 202);
        Color[] colors = {Color.GREEN, Color.ORANGE, new Color(0, 128, 0), new Color(100, 0, 100), new Color(50, 50, 50), new Color(50, 50, 50)};
        
        for (int i = 0; i < 6; i++) {
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
        tq_SanPham = createLabelValue();
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
        dv_ChiPhi = createLabelValue();
        dv_LoiNhuan = createLabelValue(); 
        dv_KhachHang = createLabelValue();
        dv_HoaDon = createLabelValue(); 
        dv_DichVu = createLabelValue();
        chartLineDichVu = createLineChartPanel("Xu hướng Doanh thu, Chi phí, Lợi nhuận (Dịch Vụ)");
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
            double dtTQ = 0, cpTQ = 0, lnTQ = 0;
            int khTQ = 0;
            
            // Map bieu do duong
            Map<LocalDate, Double> mapDoanhThuTQ, mapChiPhiTQ;
            Map<LocalDate, Double> mapDoanhThuSP, mapChiPhiSP;
            Map<LocalDate, Double> mapDoanhThuDV, mapChiPhiDV;

            @Override
            protected Void doInBackground() throws Exception {
                // 1. Lay data Tong quan
                dtTQ = tkDao.getTongDoanhThu(start, end);
                cpTQ = tkDao.getTongChiPhi(start, end);
                lnTQ = tkDao.getLoiNhuan(start, end);
                khTQ = tkDao.getSoLuongKhachHang(start, end);
                
                mapDoanhThuTQ = tkDao.getDoanhThuTheoNgay(start, end);
                mapChiPhiTQ = tkDao.getChiPhiTheoNgay(start, end);
                
                // 2. Lấy data Biểu đồ đường Sản Phẩm (TODO: Cập nhật gọi hàm trong DAO)
                // mapDoanhThuSP = tkDao.getDoanhThuSanPhamTheoNgay(start, end);
                // mapChiPhiSP = tkDao.getChiPhiSanPhamTheoNgay(start, end);
                mapDoanhThuSP = new HashMap<>(); // Khởi tạo rỗng để tránh NullPointer khi chưa có hàm DAO
                mapChiPhiSP = new HashMap<>();
                
                // 3. Lấy data Biểu đồ đường Dịch Vụ (TODO: Cập nhật gọi hàm trong DAO)
                // mapDoanhThuDV = tkDao.getDoanhThuDichVuTheoNgay(start, end);
                // mapChiPhiDV = tkDao.getChiPhiDichVuTheoNgay(start, end);
                mapDoanhThuDV = new HashMap<>();
                mapChiPhiDV = new HashMap<>();
                
                return null;
            }

            @Override
            protected void done() {
                DecimalFormat df = new DecimalFormat("#,### đ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
                
                // ================== CAP NHAT TONG QUAN ==================
                tq_DoanhThu.setText(df.format(dtTQ));
                tq_ChiPhi.setText(df.format(cpTQ));
                tq_LoiNhuan.setText(df.format(lnTQ));
                tq_KhachHang.setText(khTQ + " khách");
                tq_HoaDon.setText("N/A"); // Cho goi ham DAO count HoaDon
                tq_SanPham.setText("N/A"); 
                tq_LoiNhuan.setForeground(lnTQ >= 0 ? new Color(0, 150, 0) : Color.RED);
                
                if (lnTQ > 0) txtDeXuatTongQuan.setText("Doanh thu tốt, đạt lợi nhuận " + df.format(lnTQ) + ".");
                else if (lnTQ < 0) txtDeXuatTongQuan.setText("Lợi nhuận đang âm (" + df.format(lnTQ) + "), cần rà soát lại chi phí nhập hàng.");
                else txtDeXuatTongQuan.setText("Chưa có giao dịch trong tuần này.");

                // Data bieu dong duong tong quan
                DefaultCategoryDataset lineTqData = new DefaultCategoryDataset();
                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    String ds = date.format(formatter); 
                    double dt = mapDoanhThuTQ.getOrDefault(date, 0.0);
                    double cp = mapChiPhiTQ.getOrDefault(date, 0.0);
                    lineTqData.addValue(dt, "Doanh thu", ds);
                    lineTqData.addValue(cp, "Chi phí", ds);
                    lineTqData.addValue(dt - cp, "Lợi nhuận", ds);
                }
                chartLineTongQuan.getChart().getCategoryPlot().setDataset(lineTqData);

                // ================== CAP NHAT SAN PHAM ==================
                sp_DoanhThu.setText("N/A"); 
                sp_ChiPhi.setText("N/A"); 
                sp_LoiNhuan.setText("N/A");
                txtDeXuatSanPham.setText("Đề xuất riêng cho mảng bán sản phẩm: Nên đẩy mạnh các sản phẩm thuộc TOP bán chạy.");

                // Do Data cho bieu do duong SanPham
                DefaultCategoryDataset lineSpData = new DefaultCategoryDataset();
                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    String ds = date.format(formatter); 
                    double dt = mapDoanhThuSP.getOrDefault(date, 0.0);
                    double cp = mapChiPhiSP.getOrDefault(date, 0.0);
                    lineSpData.addValue(dt, "Doanh thu SP", ds);
                    lineSpData.addValue(cp, "Chi phí SP", ds);
                    lineSpData.addValue(dt - cp, "Lợi nhuận SP", ds);
                }
                chartLineSanPham.getChart().getCategoryPlot().setDataset(lineSpData);

                // ================== CAP NHAT DICH VU ==================
                dv_DoanhThu.setText("N/A"); 
                dv_ChiPhi.setText("N/A"); 
                dv_LoiNhuan.setText("N/A");
                txtDeXuatDichVu.setText("Đề xuất riêng cho mảng dịch vụ: Cải thiện chất lượng phục vụ của các dịch vụ phổ biến nhất.");
                
                // Do Data bieu do duong cap nhat dich vu
                DefaultCategoryDataset lineDvData = new DefaultCategoryDataset();
                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    String ds = date.format(formatter); 
                    double dt = mapDoanhThuDV.getOrDefault(date, 0.0);
                    double cp = mapChiPhiDV.getOrDefault(date, 0.0);
                    lineDvData.addValue(dt, "Doanh thu DV", ds);
                    lineDvData.addValue(cp, "Chi phí DV", ds);
                    lineDvData.addValue(dt - cp, "Lợi nhuận DV", ds);
                }
                chartLineDichVu.getChart().getCategoryPlot().setDataset(lineDvData);
            }
        };
        worker.execute();
    }
}