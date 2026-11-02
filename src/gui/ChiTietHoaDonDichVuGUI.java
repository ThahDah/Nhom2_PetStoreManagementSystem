package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ChiTietHoaDonDichVuGUI extends JDialog implements ActionListener {

    private Color MAU_XANH = new Color(103, 176, 157);
    private Color MAU_NEN = new Color(239, 248, 239);
    private String maHD;
    private JButton btnInHoaDon = new JButton("In hóa đơn");
    // Thong tin khach hang
    private JLabel lblTenKH = new JLabel();
    private JLabel lblSDT = new JLabel();
    private JLabel lblEmail = new JLabel();
    // Cac JLabel hien thi thong tin khach hang, hoa don, thanh toan
    private JLabel lblMaHD = new JLabel();
    private JLabel lblNgayLap = new JLabel();
    private JLabel lblTrangThai = new JLabel();
    private JLabel lblTenThuCung = new JLabel();
    private JLabel lblLoai = new JLabel();
    private JLabel lblTamTinh = new JLabel();
    private JLabel lblGiamGia = new JLabel();
    private JLabel lblTongCong = new JLabel();
    private JLabel lblPhuongThuc = new JLabel();
    //Tao model cho bang danh sach dich vu
    private DefaultTableModel model = new DefaultTableModel(
            new Object[] {"STT", "Dịch vụ", "Giá tiêu chuẩn", "Phụ phí", "Thành tiền"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable table = new JTable(model);
    public ChiTietHoaDonDichVuGUI(Window owner, String maHD) {
        super(owner, "Chi tiết hóa đơn dịch vụ",
                Dialog.ModalityType.APPLICATION_MODAL);
        this.maHD = maHD;
        setSize(1150, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        // Tao panel chinh su dung BorderLayout
        JPanel pnlChinh = new JPanel(new BorderLayout(0, 15));
        pnlChinh.setBackground(MAU_NEN);
        pnlChinh.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlChinh.add(taoPhanDau(), BorderLayout.NORTH);
        pnlChinh.add(taoNoiDung(), BorderLayout.CENTER);
        setContentPane(pnlChinh);
        // Dang ky su kien cho nut in hoa don
        btnInHoaDon.addActionListener(this);
    }
    private JPanel taoPhanDau() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Chi tiết hóa đơn " + maHD);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
        btnInHoaDon.setBackground(Color.WHITE);
        btnInHoaDon.setForeground(Color.BLACK);
        btnInHoaDon.setOpaque(true);
        btnInHoaDon.setFocusPainted(false);
        btnInHoaDon.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btnInHoaDon.setPreferredSize(new Dimension(130, 36));
        pnl.add(lblTieuDe, BorderLayout.WEST);
        pnl.add(btnInHoaDon, BorderLayout.EAST);
        return pnl;
    }
    private JPanel taoNoiDung() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        pnl.setOpaque(false);
        JPanel pnlTren = new JPanel(new GridLayout(1, 3, 15, 0));
        pnlTren.add(taoThongTinKhachHang());
        pnlTren.add(taoThongTinHoaDon());
        pnlTren.add(taoThongTinThuCung());
        pnlTren.setPreferredSize(new Dimension(0, 160));
        JPanel pnlDuoi = new JPanel(new BorderLayout(15, 0));
        pnlDuoi.add(taoBangDichVu(), BorderLayout.CENTER);
        JPanel pnlThanhToan = taoThongTinThanhToan();
        pnlThanhToan.setPreferredSize(new Dimension(280, 0));
        pnlDuoi.add(pnlThanhToan, BorderLayout.EAST);
        pnl.add(pnlTren, BorderLayout.NORTH);
        pnl.add(pnlDuoi, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoThongTinKhachHang() {
        JPanel pnl = new JPanel(new GridLayout(4, 1, 0, 8));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTieuDe = new JLabel("Thông tin khách hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        pnl.add(lblTieuDe);
        pnl.add(taoDong("Tên khách hàng:", lblTenKH));
        pnl.add(taoDong("Số điện thoại:", lblSDT));
        pnl.add(taoDong("Email:", lblEmail));
        return pnl;
    }
    private JPanel taoThongTinHoaDon() {
        JPanel pnl = new JPanel(new GridLayout(4, 1, 0, 8));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTieuDe = new JLabel("Thông tin hóa đơn");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        pnl.add(lblTieuDe);
        pnl.add(taoDong("Mã hóa đơn:", lblMaHD));
        pnl.add(taoDong("Ngày lập:", lblNgayLap));
        pnl.add(taoDong("Trạng thái:", lblTrangThai));
        return pnl;
    }
    private JPanel taoThongTinThuCung() {
        JPanel pnl = new JPanel(new GridLayout(3, 1, 0, 8));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTieuDe = new JLabel("Thông tin thú cưng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        pnl.add(lblTieuDe);
        pnl.add(taoDong("Tên thú cưng:", lblTenThuCung));
        pnl.add(taoDong("Loài:", lblLoai));

        return pnl;
    }
    private JPanel taoBangDichVu() {
        // Dinh dang bang
        table.setRowHeight(42);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setShowGrid(false);
        table.setFocusable(false);
        // Mau khi chon dong
        table.setSelectionBackground(MAU_XANH);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 6));
        // Dinh dang header
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 36));
        header.setFont(new Font("Arial", Font.BOLD, 10));
        header.setReorderingAllowed(false);
        DefaultTableCellRenderer renderer =new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        renderer.setBackground(new Color(216, 216, 216));
        renderer.setForeground(Color.BLACK);
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 0));
        header.setDefaultRenderer(renderer);
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(15, 12, 12, 12));
        JLabel lblTieuDe = new JLabel("Danh sách dịch vụ");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        pnl.add(lblTieuDe, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);
        return pnl;
    }
    private JPanel taoThongTinThanhToan() {
        JPanel pnl = new JPanel(new GridLayout(5, 1, 0, 10));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTieuDe = new JLabel("Thông tin thanh toán");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        pnl.add(lblTieuDe);
        pnl.add(taoDong("Tạm tính:", lblTamTinh));
        pnl.add(taoDong("Giảm giá:", lblGiamGia));
        pnl.add(taoDong("Tổng cộng:", lblTongCong));
        pnl.add(taoDong("Phương thức:", lblPhuongThuc));
        lblTongCong.setForeground(MAU_XANH);
        lblTongCong.setFont(new Font("Arial", Font.BOLD, 13));
        return pnl;
    }

    private JPanel taoDong(String ten, JLabel giaTri) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        JLabel lblTen = new JLabel(ten);
        lblTen.setFont(new Font("Arial", Font.PLAIN, 12));
        giaTri.setFont(new Font("Arial", Font.PLAIN, 12));
        giaTri.setHorizontalAlignment(SwingConstants.RIGHT);
        pnl.add(lblTen, BorderLayout.WEST);
        pnl.add(giaTri, BorderLayout.CENTER);
        return pnl;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnInHoaDon) {
            JOptionPane.showMessageDialog(this,
                    "Chức năng in hóa đơn chưa được triển khai.");
        }
    }
}
