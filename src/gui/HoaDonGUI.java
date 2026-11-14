
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox; 
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import common.DinhDang;
import dao.HoaDonDAO;
import entity.HoaDon;

public class HoaDonGUI extends JPanel implements ActionListener, MouseListener {

    private HoaDonDAO hoaDonDAO;
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblSoHoaDon;
    private JTextField txtTimKiem;
    private JButton btnTatCa;
    private JButton btnChuaThanhToan;
    private JButton btnDaThanhToan;
    private JComboBox<String> cboThoiGian;
    private JComboBox<String> cboLoaiHoaDon;
    private String loaiHoaDonDangLoc = null;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton btnApDungNgay;
    private JButton btnHuyNgay;
    private JDialog dialogChonNgay;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private LocalDate tuNgayTuyChon;
    private LocalDate denNgayTuyChon;
    private String trangThaiDangLoc = null;

    public HoaDonGUI() {
        hoaDonDAO = new HoaDonDAO();
        setLayout(new BorderLayout(10, 10));

        // Phan panel tren chua tieu de, tim kiem và bo loc
        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tieu de
        JPanel pnlHeader = new JPanel(new BorderLayout());
        JLabel lblTieuDe = new JLabel("Hóa đơn", JLabel.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 30));
        lblTieuDe.setForeground(new Color(232, 116, 59));
        pnlHeader.add(lblTieuDe, BorderLayout.CENTER);
        pnlTop.add(pnlHeader, BorderLayout.NORTH);

        // Tim kiem va bo loc
        JPanel pnlChucNang = new JPanel();
        pnlChucNang.setLayout(new BoxLayout(pnlChucNang, BoxLayout.Y_AXIS));

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JLabel lblTimKiem = new JLabel("Tìm theo mã hóa đơn hoặc SĐT:");
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(500, 36));
        pnlTimKiem.add(lblTimKiem);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlChucNang.add(pnlTimKiem);
        pnlChucNang.add(Box.createVerticalStrut(20));

        // Bo loc: trai la trang thai, phai la khoang ngay
        JPanel pnlBoLoc = new JPanel(new BorderLayout(8, 0));
        JPanel pnlTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        btnTatCa = taoNut("Tất cả");
        btnChuaThanhToan = taoNut("Chưa thanh toán");
        btnDaThanhToan = taoNut("Đã thanh toán");
        // combobox chon thoi gian
        cboThoiGian = new JComboBox<>(new String[] {
        	    "Hôm nay",
        	    "7 ngày gần nhất",
        	    "30 ngày gần nhất",
        	    "Khoảng ngày tùy chọn",
        	    "Toàn bộ thời gian"
        	});
        cboThoiGian.setPreferredSize(new Dimension(180, 34));
        cboThoiGian.setFont(new Font("Arial", Font.BOLD, 11));
        cboThoiGian.setSelectedIndex(0); // Chon mat dinh hom nay
        pnlTrangThai.add(btnTatCa);
        pnlTrangThai.add(btnChuaThanhToan);
        pnlTrangThai.add(btnDaThanhToan);

        // Dropdown loc loai hoa don
        cboLoaiHoaDon = new JComboBox<>(new String[] {
                "Tất cả hóa đơn",
                "Hóa đơn bán hàng",
                "Hóa đơn dịch vụ"
        });
        cboLoaiHoaDon.setPreferredSize(new Dimension(165, 34));
        cboLoaiHoaDon.setFont(new Font("Arial", Font.BOLD, 10));
        pnlTrangThai.add(cboLoaiHoaDon);
        pnlBoLoc.add(pnlTrangThai, BorderLayout.WEST);
        pnlBoLoc.add(cboThoiGian, BorderLayout.EAST);
        pnlBoLoc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        pnlChucNang.add(pnlBoLoc);
        pnlTop.add(pnlChucNang, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // Bang hoa don
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        // Hien thi so luong hoa don
        lblSoHoaDon = new JLabel("Số hóa đơn: 0");
        lblSoHoaDon.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel pnlSoHoaDon = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        pnlSoHoaDon.add(lblSoHoaDon);
        pnlTable.add(pnlSoHoaDon, BorderLayout.NORTH);
        String[] col = {
                "Mã hóa đơn",
                "Khách hàng",
                "Số điện thoại",
                "Ngày lập",
                "Tổng tiền",
                "Trạng thái"
        };

        model = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane pane = new JScrollPane(table);
        pnlTable.add(pane, BorderLayout.CENTER);
        add(pnlTable, BorderLayout.CENTER);

        // Chon ngay
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateTo.setDateFormatString("dd/MM/yyyy");

        // Chi chon ngay, khong cho nhap
        ((JTextField) dateFrom.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateTo.getDateEditor().getUiComponent()).setEditable(false);

        // Dang ky su kien
        table.addMouseListener(this);
        txtTimKiem.addActionListener(this);
        btnTatCa.addActionListener(this);
        btnChuaThanhToan.addActionListener(this);
        btnDaThanhToan.addActionListener(this);
        cboThoiGian.addActionListener(this);
        cboLoaiHoaDon.addActionListener(this);
        capNhatNutTrangThai();
        taiDuLieu();
    }

    // Tao nut cho bo loc de dung chung
    private JButton taoNut(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 228, 220)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        Dimension size = button.getPreferredSize();
        button.setPreferredSize(new Dimension(size.width, 34));
        return button;
    }
    // Ham tao gach chan khi chon nut
    private void capNhatNutTrangThai() {
        JButton[] dsNut = {
            btnTatCa, btnChuaThanhToan, btnDaThanhToan
        };

        JButton nutDangChon;

        if ("CHUA_THANH_TOAN".equals(trangThaiDangLoc)) {
            nutDangChon = btnChuaThanhToan;
        } else if ("DA_THANH_TOAN".equals(trangThaiDangLoc)) {
            nutDangChon = btnDaThanhToan;
        } else {
            nutDangChon = btnTatCa;
        }

        for (JButton nut : dsNut) {
            nut.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                    0, 0, nut == nutDangChon ? 3 : 0, 0,
                    new Color(145, 190, 225)
                ),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
        }
    }
    // Doc du lieu hoa don vao bang
    private void taiDuLieu() {
        try {
            String tuKhoa = txtTimKiem.getText().trim();
            capNhatKhoangNgay();
            // Toan bo thoi gian phai co sdt hoac ma hd
            if (cboThoiGian.getSelectedIndex() == 4 && tuKhoa.isEmpty()) {
                model.setRowCount(0);
                lblSoHoaDon.setText("Số hóa đơn: 0");
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập mã hóa đơn hoặc số điện thoại "
                        + "để tìm kiếm trên toàn bộ thời gian.");
                return;
            }
            // Truy van theo cac dieu kien dang chon
            List<HoaDon> danhSachHoaDon = hoaDonDAO.timKiemHoaDon(tuKhoa,trangThaiDangLoc,tuNgay, denNgay,loaiHoaDonDangLoc);
            model.setRowCount(0);
            for (HoaDon hoaDon : danhSachHoaDon) {
                model.addRow(new Object[] {
                        hoaDon.getMaHD(),
                        hoaDon.getKhachHang().getTenKH(),
                        hoaDon.getKhachHang().getSoDienThoai(),
                        hoaDon.getNgayLap() == null ? ""
                                : DinhDang.NGAY.format(hoaDon.getNgayLap()),
                        DinhDang.TIEN.format(hoaDon.getTongTien()),
                        hoaDon.getTrangThaiHD().getMoTa()
                });
            }
            lblSoHoaDon.setText("Số hóa đơn: " + model.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Không thể tải hóa đơn: " + ex.getMessage(),
                    "Lỗi cơ sở dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    // Ham cap nhat khoang ngay lua chon
    private void capNhatKhoangNgay() {
        LocalDate homNay = LocalDate.now();

        switch (cboThoiGian.getSelectedIndex()) {
            case 0: // Hom nay
                tuNgay = homNay;
                denNgay = homNay;
                break;

            case 1: // 7 ngay gan nhat
                tuNgay = homNay.minusDays(6); // tinh 7 ngay tinh ca hom nay
                denNgay = homNay;
                break;

            case 2: // 30 ngay gan nhat
                tuNgay = homNay.minusDays(29); // tinh 30 ngay tinh ca hom nay
                denNgay = homNay;
                break;

            case 3: // Khoang ngay tuy chon
                break;

            case 4: // Toan bo thoi gian
                tuNgay = null;
                denNgay = null;
                break;
        }
    }

    // Mo hop thoai chon khoang ngay
    private void moChonKhoangNgay() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        dialogChonNgay = new JDialog(
                owner,
                "Chọn khoảng ngày",
                Dialog.ModalityType.APPLICATION_MODAL);

        JPanel pnlNgay = new JPanel();
        pnlNgay.setLayout(new BoxLayout(pnlNgay, BoxLayout.Y_AXIS));
        pnlNgay.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dateFrom.setDate(tuNgayTuyChon == null ? null :
                Date.from(tuNgayTuyChon.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dateTo.setDate(denNgayTuyChon == null ? null :
                Date.from(denNgayTuyChon.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        JLabel lblDenNgay = new JLabel("Đến ngày:");
        dateFrom.setPreferredSize(new Dimension(220, 30));
        dateFrom.setMaximumSize(new Dimension(220, 30));
        dateTo.setPreferredSize(new Dimension(220, 30));
        dateTo.setMaximumSize(new Dimension(220, 30));

        pnlNgay.add(lblTuNgay);
        pnlNgay.add(dateFrom);
        pnlNgay.add(lblDenNgay);
        pnlNgay.add(dateTo);

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnHuyNgay = new JButton("Hủy lọc");
        btnApDungNgay = new JButton("Áp dụng");

        btnHuyNgay.addActionListener(this);
        btnApDungNgay.addActionListener(this);
        pnlNut.add(btnHuyNgay);
        pnlNut.add(btnApDungNgay);
        pnlNgay.add(pnlNut);
        dialogChonNgay.add(pnlNgay);
        dialogChonNgay.pack();
        dialogChonNgay.setResizable(false);
     // Xu ly khi dong hop thoai bang dau X, dua bo loc ve "Hom nay"
        dialogChonNgay.setLocationRelativeTo(owner);
        dialogChonNgay.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogChonNgay.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cboThoiGian.removeActionListener(HoaDonGUI.this);

                cboThoiGian.setSelectedIndex(0);

                cboThoiGian.addActionListener(HoaDonGUI.this);

                dialogChonNgay.dispose();
                taiDuLieu();
            }
        });

        dialogChonNgay.setVisible(true);
    }

    // Ap dung khoang ngay
    private void apDungKhoangNgay() {
        Date ngayBatDau = dateFrom.getDate();
        Date ngayKetThuc = dateTo.getDate();

        if (ngayBatDau == null || ngayKetThuc == null) {
            JOptionPane.showMessageDialog(dialogChonNgay,
                    "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
            return;
        }

        LocalDate batDau = ngayBatDau.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate ketThuc = ngayKetThuc.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        if (batDau.isAfter(ketThuc)) {
            JOptionPane.showMessageDialog(dialogChonNgay,
                    "Ngày bắt đầu không được sau ngày kết thúc.");
            return;
        }
        tuNgayTuyChon = batDau;
        denNgayTuyChon = ketThuc;
        tuNgay = batDau;
        denNgay = ketThuc;
     // Cap nhat noi dung lua chon khoang ngay
        cboThoiGian.removeActionListener(this);
        cboThoiGian.removeItemAt(3);
        cboThoiGian.insertItemAt(
            DinhDang.NGAY.format(tuNgayTuyChon) + " - " + DinhDang.NGAY.format(denNgayTuyChon),
            3
        );
        cboThoiGian.setSelectedIndex(3);
        cboThoiGian.addActionListener(this);
        dialogChonNgay.dispose();
        taiDuLieu();
    }

    // Huy loc ngay
    private void huyLocNgay() {
        dateFrom.setDate(null);
        dateTo.setDate(null);
        tuNgayTuyChon = null;
        denNgayTuyChon = null;
        cboThoiGian.removeActionListener(this);
        cboThoiGian.removeItemAt(3);
        cboThoiGian.insertItemAt("Khoảng ngày tùy chọn", 3);
        cboThoiGian.setSelectedIndex(0);
        cboThoiGian.addActionListener(this);
        dialogChonNgay.dispose();
        taiDuLieu();
    }

    // Xu ly su kien
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == txtTimKiem) {
            taiDuLieu();

            if (model.getRowCount() == 0
                    && !txtTimKiem.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy hóa đơn phù hợp",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (o == btnTatCa) {
            trangThaiDangLoc = null;
            capNhatNutTrangThai();
            taiDuLieu();

        } else if (o == btnChuaThanhToan) {
            trangThaiDangLoc = "CHUA_THANH_TOAN";
            capNhatNutTrangThai();
            taiDuLieu();

        } else if (o == btnDaThanhToan) {
            trangThaiDangLoc = "DA_THANH_TOAN";
            capNhatNutTrangThai();
            taiDuLieu();

        // Xu ly khi chon loai hoa don
        } else if (o == cboLoaiHoaDon) {
            int loai = cboLoaiHoaDon.getSelectedIndex();

            if (loai == 1) {
                loaiHoaDonDangLoc = "HDBH";
            } else if (loai == 2) {
                loaiHoaDonDangLoc = "HDDV";
            } else {
                loaiHoaDonDangLoc = null;
            }

            taiDuLieu();

        } else if (o == cboThoiGian) {
        	if (cboThoiGian.getSelectedIndex() == 3) {
        		 moChonKhoangNgay();
        	 } else {
        		 taiDuLieu();
        	}

        } else if (o == btnApDungNgay) {
            apDungKhoangNgay();

        } else if (o == btnHuyNgay) {
            huyLocNgay();
        }
    }

    // Double click de xem chi tiet hoa don
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == table && e.getClickCount() == 2) {
            int row = table.rowAtPoint(e.getPoint());

            if (row < 0) {
                return;
            }

            String maHD = table.getValueAt(row, 0).toString();
            Window owner = SwingUtilities.getWindowAncestor(this);

            ChiTietHoaDonGUI dialog =
                    new ChiTietHoaDonGUI(owner, maHD);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            taiDuLieu();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
