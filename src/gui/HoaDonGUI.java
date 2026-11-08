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
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import entity.TrangThai;
import entity.HoaDon;
public class HoaDonGUI extends JPanel implements ActionListener, MouseListener {
    private HoaDonDAO hoaDonDAO;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTimKiem;
    private JButton btnTatCa;
    private JButton btnChuaThanhToan;
    private JButton btnDaThanhToan;
    private JButton btnKhoangNgay;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton btnApDungNgay;
    private JButton btnHuyNgay;
    private JDialog dialogChonNgay;
    private Date tuNgay;
    private Date denNgay;
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
        lblTieuDe.setForeground(Color.BLUE);
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
        JPanel pnlTrangThai = new JPanel( new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnTatCa = taoNut("Tất cả");
        btnChuaThanhToan = taoNut("Chưa thanh toán");
        btnDaThanhToan = taoNut("Đã thanh toán");
        btnKhoangNgay = taoNut("Chọn khoảng ngày");
        pnlTrangThai.add(btnTatCa);
        pnlTrangThai.add(btnChuaThanhToan);
        pnlTrangThai.add(btnDaThanhToan);
        pnlBoLoc.add(pnlTrangThai, BorderLayout.WEST);
        btnKhoangNgay.setPreferredSize(new Dimension(144, 34));
        pnlBoLoc.add(btnKhoangNgay, BorderLayout.EAST);
        pnlBoLoc.setMaximumSize(new Dimension( Integer.MAX_VALUE, 40));
        pnlChucNang.add(pnlBoLoc);
        pnlTop.add(pnlChucNang, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);
        // Bang hoa don
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder( BorderFactory.createEmptyBorder(0, 10, 10, 10));
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
        // CChi chi chon ngay , khong cho nhap
        ((JTextField) dateFrom.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateTo.getDateEditor().getUiComponent()).setEditable(false);
        // Dang ky su kien
        table.addMouseListener(this);
        txtTimKiem.addActionListener(this);
        btnTatCa.addActionListener(this);
        btnChuaThanhToan.addActionListener(this);
        btnDaThanhToan.addActionListener(this);
        btnKhoangNgay.addActionListener(this);
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

        return button;
    }
    // Doc du lieu hoa don vao bang

private void taiDuLieu() {
    try {
        String tuKhoa = txtTimKiem.getText().trim();

        List<HoaDon> danhSachHoaDon;

        if (tuKhoa.isEmpty()
                && trangThaiDangLoc == null
                && tuNgay == null
                && denNgay == null) {

            danhSachHoaDon = hoaDonDAO.getAllHoaDon();

        } else {

            danhSachHoaDon = hoaDonDAO.timKiemHoaDon(
                    tuKhoa, trangThaiDangLoc, tuNgay, denNgay);
        }

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

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Không thể tải hóa đơn: " + ex.getMessage(),
                "Lỗi cơ sở dữ liệu",
                JOptionPane.ERROR_MESSAGE);
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
        dateFrom.setDate(tuNgay);
        dateTo.setDate(denNgay);
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
        dialogChonNgay.setLocationRelativeTo(owner);
        dialogChonNgay.setVisible(true);
    }

    // Ap dung khoang ngay
    private void apDungKhoangNgay() {
        java.util.Date ngayBatDau = dateFrom.getDate();
        java.util.Date ngayKetThuc = dateTo.getDate();
        if (ngayBatDau == null || ngayKetThuc == null) {
            JOptionPane.showMessageDialog(dialogChonNgay,
                    "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
            return;
        }

        Date batDau = new Date(ngayBatDau.getTime());
        Date ketThuc = new Date(ngayKetThuc.getTime());
        if (batDau.after(ketThuc)) {
            JOptionPane.showMessageDialog(dialogChonNgay,
                    "Ngày bắt đầu không được sau ngày kết thúc.");
            return;
        }
        tuNgay = batDau;
        denNgay = ketThuc;
        btnKhoangNgay.setText(
                DinhDang.NGAY.format(tuNgay.toLocalDate())
                + " - "
                + DinhDang.NGAY.format(denNgay.toLocalDate()));
        dialogChonNgay.dispose();
        taiDuLieu();
    }

    // Huy loc ngay
    private void huyLocNgay() {
        tuNgay = null;
        denNgay = null;
        dateFrom.setDate(null);
        dateTo.setDate(null);
        btnKhoangNgay.setText("Chọn khoảng ngày");
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
            taiDuLieu();
        } else if (o == btnChuaThanhToan) {
            trangThaiDangLoc = "CHUA_THANH_TOAN";
            taiDuLieu();
        } else if (o == btnDaThanhToan) {
            trangThaiDangLoc = "DA_THANH_TOAN";
            taiDuLieu();
        } else if (o == btnKhoangNgay) {
            moChonKhoangNgay();
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
