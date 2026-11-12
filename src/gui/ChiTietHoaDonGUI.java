package gui;



import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.Dialog;

import java.awt.Dimension;

import java.awt.FlowLayout;

import java.awt.Font;

import java.awt.GridLayout;

import java.awt.Window;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.BorderFactory;

import javax.swing.JButton;

import javax.swing.JDialog;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;

import dao.ChiTietHoaDonDAO;

import entity.ChiTietHoaDon;

import entity.SanPham;

import entity.TrangThai;

import common.DinhDang;

import entity.HoaDon;

import entity.PhuongThucTT;

public class ChiTietHoaDonGUI extends JDialog implements ActionListener {

    private String maHD;

    private boolean laHoaDonBanHang;

    private JButton btnInHoaDon;

    private JButton btnThanhToan;

    private JLabel lblTenKH;

    private JLabel lblSDT;

    private JLabel lblEmail;

    private JLabel lblMaHD;

    private JLabel lblNgayLap;

    private JLabel lblTrangThai;

    private JLabel lblTamTinh;

    private JLabel lblGiamGia;

    private JLabel lblTongCong;

    private JLabel lblPhuongThuc;

    private DefaultTableModel model;

    private JTable table;

    private ChiTietHoaDonDAO chiTietHoaDonDAO;

    public ChiTietHoaDonGUI(Window owner, String maHD) {

        super(owner, "Chi tiết hóa đơn",Dialog.ModalityType.APPLICATION_MODAL);

        this.maHD = maHD;

        this.laHoaDonBanHang = maHD.startsWith("HDBH");

        chiTietHoaDonDAO = new ChiTietHoaDonDAO();

        setSize(1050, 600);

        setLocationRelativeTo(owner);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel chinh

        JPanel pnlChinh = new JPanel(new BorderLayout(10, 10));

        pnlChinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tieu de

        JPanel pnlHeader = new JPanel(new BorderLayout());

        JLabel lblTieuDe = new JLabel("Chi tiết hóa đơn " + maHD, JLabel.CENTER);

        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 30));

        lblTieuDe.setForeground(Color.BLUE);

        btnInHoaDon = new JButton("In hóa đơn");

        btnInHoaDon.setPreferredSize(new Dimension(144, 34));

        btnInHoaDon.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel pnlNutIn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        pnlNutIn.add(btnInHoaDon);

        JPanel pnlBenTrai = new JPanel();

        pnlBenTrai.setPreferredSize(pnlNutIn.getPreferredSize());

        pnlHeader.add(lblTieuDe, BorderLayout.CENTER);

        pnlHeader.add(pnlNutIn, BorderLayout.EAST);

        pnlHeader.add(pnlBenTrai, BorderLayout.WEST);

        pnlChinh.add(pnlHeader, BorderLayout.NORTH);

        // Noi dung

        JPanel pnlNoiDung = new JPanel(new BorderLayout(10, 10));

        // Thong tin phia tren

        JPanel pnlTren = new JPanel(new GridLayout(1, 2, 10, 0));

        // THONG TIN KHACH HANG

        JPanel pnlKhachHang = new JPanel(new GridLayout(3, 1, 0, 5));

        pnlKhachHang.setBorder(BorderFactory.createTitledBorder(

                        BorderFactory.createEtchedBorder(),

                        "Thông tin khách hàng", 0, 0,

                        new Font("Arial", Font.BOLD, 14)));

        lblTenKH = new JLabel();

        lblSDT = new JLabel();

        lblEmail = new JLabel();

        JPanel pnlDongTen = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblTen = new JLabel("Tên khách hàng:");

        lblTen.setPreferredSize(new Dimension(120, 25));

        pnlDongTen.add(lblTen);

        pnlDongTen.add(lblTenKH);

        JPanel pnlDongSDT = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");

        lblSoDienThoai.setPreferredSize(new Dimension(120, 25));

        pnlDongSDT.add(lblSoDienThoai);

        pnlDongSDT.add(lblSDT);

        JPanel pnlDongEmail = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblEmailKH = new JLabel("Email:");

        lblEmailKH.setPreferredSize(new Dimension(120, 25));

        pnlDongEmail.add(lblEmailKH);

        pnlDongEmail.add(lblEmail);

        pnlKhachHang.add(pnlDongTen);

        pnlKhachHang.add(pnlDongSDT);

        pnlKhachHang.add(pnlDongEmail);

        // THONG TIN HOA DON

        JPanel pnlHoaDon = new JPanel(  new GridLayout(3, 1, 0, 5));

        pnlHoaDon.setBorder(BorderFactory.createTitledBorder(

                        BorderFactory.createEtchedBorder(),

                        "Thông tin hóa đơn",0, 0,

                        new Font("Arial", Font.BOLD, 14)));

        lblMaHD = new JLabel();

        lblNgayLap = new JLabel();

        lblTrangThai = new JLabel();

        JPanel pnlDongMaHD = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblMaHoaDon = new JLabel("Mã hóa đơn:");

        lblMaHoaDon.setPreferredSize(new Dimension(120, 25));

        pnlDongMaHD.add(lblMaHoaDon);

        pnlDongMaHD.add(lblMaHD);

        JPanel pnlDongNgayLap = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblNgayLapHD = new JLabel("Ngày lập:");

        lblNgayLapHD.setPreferredSize(new Dimension(120, 25));

        pnlDongNgayLap.add(lblNgayLapHD);

        pnlDongNgayLap.add(lblNgayLap);

        JPanel pnlDongTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblTrangThaiHD = new JLabel("Trạng thái:");

        lblTrangThaiHD.setPreferredSize(new Dimension(120, 25));

        pnlDongTrangThai.add(lblTrangThaiHD);

        pnlDongTrangThai.add(lblTrangThai);

        pnlHoaDon.add(pnlDongMaHD);

        pnlHoaDon.add(pnlDongNgayLap);

        pnlHoaDon.add(pnlDongTrangThai);

        pnlTren.add(pnlKhachHang);

        pnlTren.add(pnlHoaDon);

        pnlTren.setPreferredSize(new Dimension(0, 110));

        pnlNoiDung.add(pnlTren, BorderLayout.NORTH);

        // PHAN DUOI

        JPanel pnlDuoi = new JPanel(new BorderLayout(10, 0));

        // Bang chi tiet hoa don

        JPanel pnlBang = new JPanel(new BorderLayout(0, 10));

        pnlBang.setBorder(BorderFactory.createTitledBorder(

                        BorderFactory.createEtchedBorder(),

                        laHoaDonBanHang

                                ? "Danh sách sản phẩm"

                                : "Danh sách dịch vụ",

                        0, 0,

                        new Font("Arial", Font.BOLD, 14)));

        String[] col;

        if (laHoaDonBanHang) {

            col = new String[] {

                    "STT",

                    "Sản phẩm",

                    "Đơn giá",

                    "Số lượng",

                    "Thành tiền"

            };

        } else {

            col = new String[] {

                    "STT",

                    "Dịch vụ",

                    "Tên thú cưng",

                    "Giá tiêu chuẩn",

                    "Phụ phí",

                    "Thành tiền"

            };

        }

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

        JScrollPane scrollPane = new JScrollPane(table);

        pnlBang.add(scrollPane, BorderLayout.CENTER);

        pnlDuoi.add(pnlBang, BorderLayout.CENTER);

        // THONG TIN THANH TOAN

        JPanel pnlThanhToan = new JPanel(new BorderLayout());

        pnlThanhToan.setBorder(BorderFactory.createTitledBorder(

                        BorderFactory.createEtchedBorder(),

                        "Thông tin thanh toán", 0, 0,

                        new Font("Arial", Font.BOLD, 14)));

        JPanel pnlThongTin = new JPanel(new GridLayout(4, 1, 0, 12));

        lblTamTinh = new JLabel();

        lblGiamGia = new JLabel();

        lblTongCong = new JLabel();

        lblPhuongThuc = new JLabel();

        JPanel pnlDongTamTinh = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblTamTinhText = new JLabel("Tạm tính:");

        lblTamTinhText.setPreferredSize(new Dimension(100, 25));

        pnlDongTamTinh.add(lblTamTinhText);

        pnlDongTamTinh.add(lblTamTinh);

        JPanel pnlDongGiamGia = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblGiamGiaText = new JLabel("Giảm giá:");

        lblGiamGiaText.setPreferredSize(new Dimension(100, 25));

        pnlDongGiamGia.add(lblGiamGiaText);

        pnlDongGiamGia.add(lblGiamGia);

        JPanel pnlDongTongCong = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblTongCongText = new JLabel("Tổng cộng:");

        lblTongCongText.setPreferredSize(new Dimension(100, 25));

        pnlDongTongCong.add(lblTongCongText);

        pnlDongTongCong.add(lblTongCong);

        JPanel pnlDongPhuongThuc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblPhuongThucText = new JLabel("Phương thức:");

        lblPhuongThucText.setPreferredSize(new Dimension(100, 25));

        pnlDongPhuongThuc.add(lblPhuongThucText);

        pnlDongPhuongThuc.add(lblPhuongThuc);

        pnlThongTin.add(pnlDongTamTinh);

        pnlThongTin.add(pnlDongGiamGia);

        pnlThongTin.add(pnlDongTongCong);

        pnlThongTin.add(pnlDongPhuongThuc);

        JPanel pnlNoiDungThanhToan = new JPanel(new BorderLayout());

        pnlNoiDungThanhToan.add(pnlThongTin, BorderLayout.NORTH);

        pnlThanhToan.add(pnlNoiDungThanhToan, BorderLayout.CENTER);

        // PANEL BEN PHAI VA NUT THANH TOAN

        JPanel pnlBenPhai = new JPanel(new BorderLayout(0, 10));

        pnlBenPhai.setPreferredSize(new Dimension(280, 0));

        pnlBenPhai.add(pnlThanhToan, BorderLayout.CENTER);

        btnThanhToan = new JButton("Thanh toán ngay");

        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));

        btnThanhToan.setForeground(Color.WHITE);

        btnThanhToan.setBackground(new Color(46, 204, 113));

        btnThanhToan.setFocusPainted(false);

        btnThanhToan.setPreferredSize(new Dimension(175, 35));

        btnThanhToan.setVisible(false);

        JPanel pnlNutThanhToan = new JPanel( new FlowLayout(FlowLayout.CENTER, 0, 10));

        pnlNutThanhToan.add(btnThanhToan);

        pnlBenPhai.add(pnlNutThanhToan, BorderLayout.SOUTH);

        pnlDuoi.add(pnlBenPhai, BorderLayout.EAST);

        pnlNoiDung.add(pnlDuoi, BorderLayout.CENTER);

        pnlChinh.add(pnlNoiDung, BorderLayout.CENTER);

        setContentPane(pnlChinh);

        // Dang ky su kien

        btnInHoaDon.addActionListener(this);

        btnThanhToan.addActionListener(this);

        taiDuLieu();

    }

    // DOC DU LIEU HOA DON



private void taiDuLieu() {

    try {

    	HoaDon hd = chiTietHoaDonDAO.timHoaDonTheoMa(maHD);



        if (hd == null) {

            JOptionPane.showMessageDialog(this,

                    "Không tìm thấy hóa đơn: " + maHD);

            return;

        }



        // Thong tin hoa don

        lblMaHD.setText(hd.getMaHD());



        lblNgayLap.setText(

                hd.getNgayLap() == null ? ""

                        : DinhDang.NGAY.format(hd.getNgayLap())

        );



        TrangThai trangThai = hd.getTrangThaiHD();



        lblTrangThai.setText(trangThai.getMoTa());



        // Chi hien nut thanh toan khi hoa don chua thanh toan

        btnThanhToan.setVisible(

                trangThai == TrangThai.CHUA_THANH_TOAN

        );



        // Thong tin khach hang

        lblTenKH.setText(hd.getKhachHang().getTenKH());



        lblSDT.setText(hd.getKhachHang().getSoDienThoai());



        String email = hd.getKhachHang().getEmail();



        lblEmail.setText(

                email == null || email.trim().isEmpty()

                        ? "Không có" : email

        );



        // Tai bang chi tiet hoa don

        model.setRowCount(0);



        if (laHoaDonBanHang) {

            taiChiTietBanHang();

        } else {

            taiChiTietDichVu();

        }



        // Thong tin thanh toan

        double tongCong = hd.getTongTien();



        lblTongCong.setText(

                DinhDang.TIEN.format(tongCong)

        );



        PhuongThucTT phuongThuc = hd.getPhuongThucTT();



        if (phuongThuc == null) {

            lblPhuongThuc.setText("Chưa thanh toán");

        } else if (phuongThuc == PhuongThucTT.TIEN_MAT) {

            lblPhuongThuc.setText("Tiền mặt");

        } else if (phuongThuc == PhuongThucTT.THE_TIN_DUNG) {

            lblPhuongThuc.setText("Thẻ tín dụng");

        } else {

            lblPhuongThuc.setText(phuongThuc.toString());

        }



        lblGiamGia.setText(DinhDang.TIEN.format(hd.getGiamGia()));



    } catch (SQLException e) {

        JOptionPane.showMessageDialog(this,

                "Lỗi tải dữ liệu hóa đơn: " + e.getMessage(),

                "Lỗi",

                JOptionPane.ERROR_MESSAGE);



    } catch (IllegalArgumentException e) {

        JOptionPane.showMessageDialog(this,

                "Dữ liệu hóa đơn không hợp lệ: " + e.getMessage(),

                "Lỗi",

                JOptionPane.ERROR_MESSAGE);

    }

}



    // chi tiet hoa don ban hang

    private void taiChiTietBanHang() throws SQLException {

        ArrayList<ChiTietHoaDon> danhSach =chiTietHoaDonDAO.layDanhSachChiTietHoaDon(maHD);

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

        lblTamTinh.setText(

                DinhDang.TIEN.format(tamTinh));

    }

    // chi tiet hoa don dich vu

    private void taiChiTietDichVu() throws SQLException {
       ArrayList<ChiTietHoaDon> danhSach = chiTietHoaDonDAO.layDanhSachChiTietHoaDon(maHD);
       int stt = 1;
       double tamTinh = 0;
       for (ChiTietHoaDon ct : danhSach) {
           if (ct.getDichVu() == null) {
               continue;
           }
           String tenThuCung = ct.getThuCung() == null
                   ? "Chưa có dữ liệu"
                   : ct.getThuCung().getTenThuCung();

           double giaTieuChuan = ct.getDichVu().getGiaTieuChuan();
           double phuPhi = Math.max(0, ct.getDonGia() - giaTieuChuan);

           model.addRow(new Object[] {
                   stt++,
                   ct.getDichVu().getTenDV(),
                   tenThuCung,
                   DinhDang.TIEN.format(giaTieuChuan),
                   DinhDang.TIEN.format(phuPhi),
                   DinhDang.TIEN.format(ct.getThanhTien())
           });

           tamTinh += ct.getThanhTien();
       }

       lblTamTinh.setText(DinhDang.TIEN.format(tamTinh));
   }

   @Override

    public void actionPerformed(ActionEvent e) {



        if (e.getSource() == btnInHoaDon) {



            JOptionPane.showMessageDialog(this,

                    "Chức năng in hóa đơn chưa được triển khai.");



        } else if (e.getSource() == btnThanhToan) {



            JOptionPane.showMessageDialog(this,

                    "Chuyển đến màn hình thanh toán hóa đơn " + maHD);

        }

    }

}
