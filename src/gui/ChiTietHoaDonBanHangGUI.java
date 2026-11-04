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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import common.DinhDang;
import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import entity.ChiTietHoaDon;
import entity.SanPham;
import entity.TrangThai;
public class ChiTietHoaDonBanHangGUI extends JDialog implements ActionListener {
    private final Color MAU_XANH = new Color(103, 176, 157);
    private final Color MAU_NEN = new Color(239, 248, 239);
    private final String maHD;
    private JButton btnInHoaDon = new JButton("In hóa đơn");
    private JLabel lblTenKH = new JLabel();
    private JLabel lblSDT = new JLabel();
    private JLabel lblEmail = new JLabel();
    private JLabel lblMaHD = new JLabel();
    private JLabel lblNgayLap = new JLabel();
    private JLabel lblTrangThai = new JLabel();
    private JLabel lblTamTinh = new JLabel();
    private JLabel lblGiamGia = new JLabel();
    private JLabel lblTongCong = new JLabel();
    private JLabel lblPhuongThuc = new JLabel();
    // Bang danh sach san pham
    private DefaultTableModel model = new DefaultTableModel(
            new Object[] {"STT", "Sản phẩm", "Đơn giá","Số lượng", "Thành tiền"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable table = new JTable(model);
    public ChiTietHoaDonBanHangGUI(Window owner, String maHD) {
        super(owner, "Chi tiết hóa đơn",Dialog.ModalityType.APPLICATION_MODAL);
        this.maHD = maHD;
        setSize(1050, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel pnlChinh = new JPanel(new BorderLayout(0, 15));
        pnlChinh.setBackground(MAU_NEN);
        pnlChinh.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlChinh.add(taoPhanDau(), BorderLayout.NORTH);
        pnlChinh.add(taoNoiDung(), BorderLayout.CENTER);
        setContentPane(pnlChinh);
        btnInHoaDon.addActionListener(this);
        taiDuLieu();
    }

    // Panel tieu de va in hoa don
    private JPanel taoPhanDau() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        JLabel lblTieuDe =new JLabel("Chi tiết hóa đơn " + maHD);
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
    // Noi dung chih
    private JPanel taoNoiDung() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        pnl.setOpaque(false);
        JPanel pnlTren = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlTren.add(taoThongTinKhachHang());
        pnlTren.add(taoThongTinHoaDon());
        pnlTren.setPreferredSize(new Dimension(0, 160));
        JPanel pnlDuoi = new JPanel(new BorderLayout(15, 0));
        pnlDuoi.add(taoBangSanPham(), BorderLayout.CENTER);
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

    // tao bang danh sach sp
    private JPanel taoBangSanPham() {
        table.setRowHeight(42);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setFocusable(false);
        table.setSelectionBackground(MAU_XANH);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 6));
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
        JLabel lblTieuDe = new JLabel("Danh sách sản phẩm");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        pnl.add(lblTieuDe, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);
        return pnl;
    }

    // Thong tin thanh toan
    private JPanel taoThongTinThanhToan() {
        JPanel pnl = new JPanel(new GridLayout(5, 1, 0, 10));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
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

    // load du lieu
    private void taiDuLieu() {
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        ChiTietHoaDonDAO chiTietDAO = new ChiTietHoaDonDAO();

        try {
            // Lay thong tin hoa don va khach hang
            Object[] hd = hoaDonDAO.timHoaDonTheoMa(maHD);
            if (hd == null) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy hóa đơn: " + maHD);
                return;
            }
            lblMaHD.setText((String) hd[0]);
            Timestamp ngayLap = (Timestamp) hd[1];
            lblNgayLap.setText(ngayLap.toLocalDateTime().toLocalDate().format(DinhDang.NGAY));
            TrangThai trangThai =TrangThai.valueOf((String) hd[3]);
            lblTrangThai.setText(trangThai.getMoTa());
            lblTenKH.setText((String) hd[5]);
            lblSDT.setText((String) hd[6]);
            String email = (String) hd[7];
            lblEmail.setText(email == null || email.trim().isEmpty()? "Không có" : email);
            // Lay ds san pham trong hoadon
            ArrayList<ChiTietHoaDon> danhSach =chiTietDAO.timTheoHoaDon(maHD);
            model.setRowCount(0);
            int stt = 1;
            double tamTinh = 0;
            for (ChiTietHoaDon ct : danhSach) {
                SanPham sp = ct.getSanPham();
                if (sp == null) {
                    continue;
                }
                model.addRow(new Object[] {
                    stt++,
                    sp.getTenSP(),
                    DinhDang.TIEN.format(ct.getDonGia()),
                    ct.getSoLuong(),
                    DinhDang.TIEN.format(ct.getThanhTien())
                });

                tamTinh += ct.getThanhTien();
            }
            double tongCong =((Number) hd[2]).doubleValue();
            lblTamTinh.setText( DinhDang.TIEN.format(tamTinh));
            lblTongCong.setText( DinhDang.TIEN.format(tongCong));
            lblGiamGia.setText("Chưa có dữ liệu");
            // Hien phuong thuc thanh toan
            String phuongThuc = (String) hd[4];
            if (phuongThuc == null) {
                lblPhuongThuc.setText("Chưa thanh toán");
            } else if ("TIEN_MAT".equals(phuongThuc)) {
                lblPhuongThuc.setText("Tiền mặt");
            } else if ("THE_TIN_DUNG".equals(phuongThuc)) {
                lblPhuongThuc.setText("Thẻ tín dụng");
            } else {
                lblPhuongThuc.setText(phuongThuc);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu hóa đơn: "
                            + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Trạng thái hóa đơn không hợp lệ: "
                            + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnInHoaDon) {
            JOptionPane.showMessageDialog(this,
                    "Chức năng in hóa đơn chưa được triển khai.");
        }
    }
}
