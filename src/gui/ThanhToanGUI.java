package gui;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import common.DinhDang;
import dao.ChiTietHoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import dao.HoaDonDAO;
public class ThanhToanGUI extends JDialog implements ActionListener {
    private JTextField txtTienKhachDua, txtTienThua;
    private JLabel lblTamTinh, lblGiamGia, lblTongTien;
    private JRadioButton radTienMat, radChuyenKhoan;
    private JPanel pnlNoiDungPhuongThuc;
    private CardLayout cardLayout;
    private JButton btnXacNhan;
    private DefaultTableModel tableModel;
    private double tongTien;
    private boolean laHoaDonBanHang;
    private String maHD;
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    public ThanhToanGUI(Window owner, String maHD) {
        super(owner, "Xác nhận thanh toán", Dialog.ModalityType.APPLICATION_MODAL);
        this.maHD = maHD;
        this.laHoaDonBanHang = maHD.startsWith("HDBH");
        setSize(950, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        khoiTaoGiaoDien();
        taiDuLieu(maHD);
    }
    // Ham khoi tao giao dien
    private void khoiTaoGiaoDien() {
        JPanel pnlChinh = new JPanel(new BorderLayout(10, 10));
        pnlChinh.setBackground(Color.WHITE);
        pnlChinh.setBorder(BorderFactory.createEmptyBorder(12, 15, 10, 15));
        JLabel lblTieuDe = new JLabel("Xác nhận thanh toán");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        pnlChinh.add(lblTieuDe, BorderLayout.NORTH);
        // Bang chi tiet hoa don
        String[] col;
        if (laHoaDonBanHang) {
            col = new String[]{
                "STT", "Sản phẩm", "SL", "Đơn giá", "Thành tiền"
            };
        } else {
            col = new String[]{
                "STT", "Dịch vụ", "Tên thú cưng",
                "Giá tiêu chuẩn", "Phụ phí", "Thành tiền"
            };
        }
        tableModel = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scroll = new JScrollPane(
            table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        JPanel pnlBang = new JPanel(new BorderLayout(0, 7));
        pnlBang.setBackground(Color.WHITE);
        pnlBang.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(185, 185, 185)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel lblDanhSach = new JLabel(
            laHoaDonBanHang ? "Danh sách sản phẩm" : "Danh sách dịch vụ"
        );
        lblDanhSach.setFont(new Font("Arial", Font.BOLD, 13));

        pnlBang.add(lblDanhSach, BorderLayout.NORTH);
        pnlBang.add(scroll, BorderLayout.CENTER);
        // Tong tien
        JPanel pnlTongTien = new JPanel(new GridLayout(0, 1, 3, 8));
        pnlTongTien.setBackground(Color.WHITE);
        pnlTongTien.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(185, 185, 185)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JLabel lblTieuDeTong = new JLabel("Tổng tiền");
        lblTieuDeTong.setFont(new Font("Arial", Font.BOLD, 15));
        lblTamTinh = new JLabel("0");
        lblGiamGia = new JLabel("0");
        lblTongTien = new JLabel("0");
        pnlTongTien.add(lblTieuDeTong);
        // Tam tinh
        JPanel pnlTamTinh = new JPanel(new BorderLayout());
        pnlTamTinh.setBackground(Color.WHITE);
        JLabel lblTenTamTinh = new JLabel("Tạm tính:");
        lblTenTamTinh.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTamTinh.setFont(new Font("Arial", Font.PLAIN, 12));
        pnlTamTinh.add(lblTenTamTinh, BorderLayout.WEST);
        pnlTamTinh.add(lblTamTinh, BorderLayout.EAST);
        pnlTongTien.add(pnlTamTinh);
        // Giam gia
        JPanel pnlGiamGia = new JPanel(new BorderLayout());
        pnlGiamGia.setBackground(Color.WHITE);
        JLabel lblTenGiamGia = new JLabel("Giảm giá:");
        lblTenGiamGia.setFont(new Font("Arial", Font.PLAIN, 12));
        lblGiamGia.setFont(new Font("Arial", Font.PLAIN, 12));
        pnlGiamGia.add(lblTenGiamGia, BorderLayout.WEST);
        pnlGiamGia.add(lblGiamGia, BorderLayout.EAST);
        pnlTongTien.add(pnlGiamGia);
        // Tong tien can thanh toan
        JPanel pnlTong = new JPanel(new BorderLayout(10, 0));
        pnlTong.setBackground(new Color(225, 225, 225));
        pnlTong.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JLabel lblNhanTong = new JLabel("Tổng thanh toán");
        lblNhanTong.setFont(new Font("Arial", Font.BOLD, 15));
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 15));
        lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlTong.add(lblNhanTong, BorderLayout.WEST);
        pnlTong.add(lblTongTien, BorderLayout.EAST);
        pnlTongTien.add(pnlTong);
        JPanel pnlThongTin = new JPanel(new BorderLayout(10, 0));
        pnlThongTin.setBackground(Color.WHITE);
        pnlTongTien.setPreferredSize(new Dimension(230, 0));
        pnlThongTin.add(pnlBang, BorderLayout.CENTER);
        pnlThongTin.add(pnlTongTien, BorderLayout.EAST);
        // Phuong thuc thanh toan
        JPanel pnlThanhToan = new JPanel(new BorderLayout(0, 10));
        pnlThanhToan.setBackground(Color.WHITE);
        pnlThanhToan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(185, 185, 185)),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        JLabel lblPhuongThuc = new JLabel("Phương thức thanh toán");
        lblPhuongThuc.setFont(new Font("Arial", Font.BOLD, 14));
        radTienMat = new JRadioButton("Tiền mặt", true);
        radChuyenKhoan = new JRadioButton("Chuyển khoản/QR");
        radTienMat.setFont(new Font("Arial", Font.BOLD, 13));
        radChuyenKhoan.setFont(new Font("Arial", Font.BOLD, 13));
        radTienMat.setBackground(Color.WHITE);
        radChuyenKhoan.setBackground(Color.WHITE);
        radTienMat.setFocusPainted(false);
        radChuyenKhoan.setFocusPainted(false);
        ButtonGroup group = new ButtonGroup();
        group.add(radTienMat);
        group.add(radChuyenKhoan);
        JPanel pnlLuaChon = new JPanel(new GridLayout(1, 2, 35, 0));
        pnlLuaChon.setBackground(Color.WHITE);
        pnlLuaChon.add(radTienMat);
        pnlLuaChon.add(radChuyenKhoan);
        // Noi dung thanh toan
        cardLayout = new CardLayout();
        pnlNoiDungPhuongThuc = new JPanel(cardLayout);
        pnlNoiDungPhuongThuc.setBackground(Color.WHITE);
        JPanel pnlTienMat = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlTienMat.setBackground(Color.WHITE);
        txtTienKhachDua = new JTextField();
        txtTienKhachDua.setFont(new Font("Arial", Font.PLAIN, 12));
        txtTienThua = new JTextField("0");
        txtTienThua.setFont(new Font("Arial", Font.PLAIN, 12));
        txtTienThua.setEditable(false);
        txtTienThua.setBackground(new Color(220, 220, 220));
        // O tien nhan
        JPanel pnlTienNhan = new JPanel(new BorderLayout(0, 5));
        pnlTienNhan.setBackground(Color.WHITE);
        JLabel lblTienNhan = new JLabel("Tiền nhận");
        lblTienNhan.setFont(new Font("Arial", Font.BOLD, 13));
        pnlTienNhan.add(lblTienNhan, BorderLayout.NORTH);
        pnlTienNhan.add(txtTienKhachDua, BorderLayout.CENTER);
        // O tien thua
        JPanel pnlTienThua = new JPanel(new BorderLayout(0, 5));
        pnlTienThua.setBackground(Color.WHITE);
        JLabel lblTienThua = new JLabel("Tiền thừa");
        lblTienThua.setFont(new Font("Arial", Font.BOLD, 13));
        pnlTienThua.add(lblTienThua, BorderLayout.NORTH);
        pnlTienThua.add(txtTienThua, BorderLayout.CENTER);
        pnlTienMat.add(pnlTienNhan);
        pnlTienMat.add(pnlTienThua);
        JPanel pnlChuyenKhoan = new JPanel(new BorderLayout());
        pnlChuyenKhoan.setBackground(Color.WHITE);
        pnlChuyenKhoan.add(new JLabel(
            "Đang cập nhật",
            SwingConstants.CENTER
        ));
        pnlNoiDungPhuongThuc.add(pnlTienMat, "TIEN_MAT");
        pnlNoiDungPhuongThuc.add(pnlChuyenKhoan, "CHUYEN_KHOAN");
        JPanel pnlLuaChonNoiDung = new JPanel(new BorderLayout(0, 10));
        pnlLuaChonNoiDung.setBackground(Color.WHITE);
        pnlLuaChonNoiDung.add(pnlLuaChon, BorderLayout.NORTH);
        pnlLuaChonNoiDung.add(pnlNoiDungPhuongThuc, BorderLayout.CENTER);
        pnlThanhToan.add(lblPhuongThuc, BorderLayout.NORTH);
        pnlThanhToan.add(pnlLuaChonNoiDung, BorderLayout.CENTER);
        pnlThanhToan.setPreferredSize(new Dimension(700, 125));
        JPanel pnlTrungTam = new JPanel(new BorderLayout(0, 10));
        pnlTrungTam.setBackground(Color.WHITE);
        pnlTrungTam.add(pnlThongTin, BorderLayout.CENTER);
        pnlTrungTam.add(pnlThanhToan, BorderLayout.SOUTH);
        pnlChinh.add(pnlTrungTam, BorderLayout.CENTER);
        // Nut xac nhan thanh toan
        btnXacNhan = new JButton("Xác nhận thanh toán");
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 12));
        btnXacNhan.setPreferredSize(new Dimension(180, 32));
        btnXacNhan.setBackground(new Color(46, 190, 95));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setBorderPainted(false);
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlNut.setBackground(Color.WHITE);
        pnlNut.add(btnXacNhan);
        pnlChinh.add(pnlNut, BorderLayout.SOUTH);
        setContentPane(pnlChinh);
        // Gan su kien
        radTienMat.addActionListener(this);
        radChuyenKhoan.addActionListener(this);
        txtTienKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                tinhTienThua();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                tinhTienThua();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                tinhTienThua();
            }
        });
        btnXacNhan.addActionListener(this);
    }
    // Tai du lieu cua hoa don
    private void taiDuLieu(String maHD) {
        try {
            ChiTietHoaDonDAO dao = new ChiTietHoaDonDAO();
            HoaDon hd = dao.timHoaDonTheoMa(maHD);
            if (hd == null) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy hóa đơn: " + maHD);
                dispose();
                return;
            }
            ArrayList<ChiTietHoaDon> danhSach =
                    dao.layDanhSachChiTietHoaDon(maHD);
            double tamTinh = 0;
            int stt = 1;
            for (ChiTietHoaDon ct : danhSach) {
                if (laHoaDonBanHang) {
                    if (ct.getSanPham() == null) {
                        continue;
                    }
                    tableModel.addRow(new Object[]{
                        stt++,
                        ct.getSanPham().getTenSP(),
                        ct.getSoLuong(),
                        DinhDang.TIEN.format(ct.getDonGia()),
                        DinhDang.TIEN.format(ct.getThanhTien())
                    });
                } else {
                    if (ct.getDichVu() == null) {
                        continue;
                    }
                    String tenThuCung = ct.getThuCung() == null
                            ? "Chưa có dữ liệu"
                            : ct.getThuCung().getTenThuCung();
                    double giaTieuChuan =
                            ct.getDichVu().getGiaTieuChuan();
                    double phuPhi = Math.max(
                        0, ct.getDonGia() - giaTieuChuan
                    );
                    tableModel.addRow(new Object[]{
                        stt++,
                        ct.getDichVu().getTenDV(),
                        tenThuCung,
                        DinhDang.TIEN.format(giaTieuChuan),
                        DinhDang.TIEN.format(phuPhi),
                        DinhDang.TIEN.format(ct.getThanhTien())
                    });
                }
                tamTinh += ct.getThanhTien();
            }
            // Hien thi thong tin thanh toan
            tongTien = hd.getTongTien();
            lblTamTinh.setText(DinhDang.TIEN.format(tamTinh));
            lblGiamGia.setText(DinhDang.TIEN.format(hd.getGiamGia()));
            lblTongTien.setText(DinhDang.TIEN.format(tongTien));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu hóa đơn: " + e.getMessage());
            dispose();
        }
    }
    // Ham doi phuong thuc thanh toan
    private void doiPhuongThuc() {
        cardLayout.show(
            pnlNoiDungPhuongThuc,
            radTienMat.isSelected() ? "TIEN_MAT" : "CHUYEN_KHOAN"
        );
    }
    // Ham tinh tien thua
    private void tinhTienThua() {
        try {
            String text = txtTienKhachDua.getText().trim();
            if (text.isEmpty()) {
                txtTienThua.setText("0");
                return;
            }
            long tienKhachDua = Long.parseLong(text);
            long tienThua = tienKhachDua - (long) Math.ceil(tongTien);
            if (tienThua >= 0) {
                txtTienThua.setText(DinhDang.TIEN.format(tienThua));
            } else {
                txtTienThua.setText(
                    "Thiếu " + DinhDang.TIEN.format(-tienThua)
                );
            }
        } catch (NumberFormatException e) {
            txtTienThua.setText("0");
        }
    }
    // Xac nhan thanh toan
    private void xacNhanThanhToan() {
        if (radTienMat.isSelected()) {
            try {
                String text = txtTienKhachDua.getText().trim();
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập số tiền khách đưa.");
                    txtTienKhachDua.requestFocus();
                    return;
                }
                long tienKhachDua = Long.parseLong(text);
                if (tienKhachDua < Math.ceil(tongTien)) {
                    JOptionPane.showMessageDialog(this,
                        "Số tiền khách đưa chưa đủ!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Số tiền không hợp lệ. Chỉ nhập chữ số.");
                return;
            }
        }
        // Cap nhat trang thai thanh toan
        String phuongThuc = radTienMat.isSelected()
                ? "TIEN_MAT"
                : "CHUYEN_KHOAN";

        boolean thanhCong = hoaDonDAO.capNhatTrangThaiThanhToan(maHD, phuongThuc);
        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            // Lay cua so man chi tiet hoa don de thanh toan thanh cong se dong luon man thanh toan va man cthd
            Window owner = getOwner();
            // Dong man thanh toan
            dispose();
            // Dong man chi tiet hoa don
            if (owner != null) {
                owner.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thanh toán thất bại!");
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == radTienMat || e.getSource() == radChuyenKhoan) {
            doiPhuongThuc();
        }
        if (e.getSource() == btnXacNhan) {
            xacNhanThanhToan();
        }
    }
}
