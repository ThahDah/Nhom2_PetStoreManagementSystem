package gui;

import java.awt.BorderLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;
import connectDB.ConnectDB; 
import gui.PanelThongKeTuanGUI;

@SuppressWarnings("serial")
public class ThongKeTaiChinhGUI extends JPanel {

	private PanelThongKeTuanGUI pnlTuan;
	private PanelThongKeThangGUI pnlThang;
	private PanelThongKeNamGUI pnlNam;

	public ThongKeTaiChinhGUI() {
		setSize(1400,700);
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        pnlTuan = new PanelThongKeTuanGUI();
        
		pnlThang = new PanelThongKeThangGUI();
		pnlNam = new PanelThongKeNamGUI();

        
        // Danh sách tab con
        tabbedPane.addTab("Theo Tuần", pnlTuan);
        tabbedPane.addTab("Theo Tháng", pnlThang);
        tabbedPane.addTab("Theo Năm", pnlNam);

        // Xử lý sự kiện chuyển tab (Đã mở comment)
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            LocalDate now = LocalDate.now();

            if (selectedIndex == 0 && pnlTuan != null) { 
                LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                pnlTuan.updateDashboard(startOfWeek, now);
            } 
            else if (selectedIndex == 1 && pnlThang != null) {
				// pnlThang.updateThongKeTheoBoChon();
			}
            else if (selectedIndex == 2 && pnlNam != null) {
				// pnlNam.updateThongKeTheoBoChon();
			}
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Lần chạy đầu tiên khi mở tab Tuần (Đã mở comment)
        SwingUtilities.invokeLater(() -> {
            LocalDate now = LocalDate.now();
            LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            
            if(pnlTuan != null) {
                pnlTuan.updateDashboard(startOfWeek, now);
            }
        });
    }

    public static void main(String[] args) {

	    	FlatLightLaf.setup();
	    	try {
	            // Mở comment kết nối Database
	            ConnectDB.getInstance().connect(); 
	            System.out.println("Đã kết nối Database thành công!");
	        } catch (Exception e) {
	            System.out.println("Lỗi kết nối Database. Vui lòng kiểm tra lại SQL Server!");
	            e.printStackTrace();
	        }
	        
	    	SwingUtilities.invokeLater(() -> {
	            JFrame frame = new JFrame("Thống Kê Tài Chính");
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            frame.setSize(1400, 700);
	            frame.setLocationRelativeTo(null);
	            
	            ThongKeTaiChinhGUI mainPanel = new ThongKeTaiChinhGUI();
	            frame.add(mainPanel);
	            frame.setVisible(true);
	        });
    }
}