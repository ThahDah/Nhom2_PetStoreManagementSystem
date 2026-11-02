
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.SanPhamDAO;
import entity.SanPham;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SanPhamGUI extends JFrame {

    private JTextField txtMaSP, txtTenSP;
    private JTextField txtSoLuongTon, txtGiaNiemYet, txtGiaBan;
    private JTextField txtTimMaSP, txtTimTenSP, txtGiaTu, txtGiaDen;
    private SanPhamDAO sp_dao;
    private JComboBox<String> cboDonViTinh;

    private JButton btnChonAnh, btnTaoMa;
    private JButton btnLuu, btnSua, btnXoa, btnLamMoi;

    private JTable tblSanPham;
    private DefaultTableModel model;

    public SanPhamGUI() {
    	try {
			ConnectDB.getInstance().connect();
			System.out.println("Connected!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        setLayout(new BorderLayout(10, 15));
        setBackground(Color.WHITE);

        sp_dao = new SanPhamDAO();
        JPanel pnlTop = new JPanel(new BorderLayout(15, 0));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setPreferredSize(new Dimension(0, 190));

        JPanel pnlImage = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        pnlImage.setBackground(Color.WHITE);
        pnlImage.setPreferredSize(new Dimension(145, 190));

        JLabel lblHinhAnh = new JLabel();
        lblHinhAnh.setOpaque(true);
        lblHinhAnh.setBackground(new Color(220, 220, 220));
        lblHinhAnh.setPreferredSize(new Dimension(100, 125));

        btnChonAnh = new JButton("Chọn ảnh");

        pnlImage.add(lblHinhAnh);
        pnlImage.add(btnChonAnh);

        pnlTop.add(pnlImage, BorderLayout.WEST);

        JPanel pnlForm = new JPanel(new BorderLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel pnlThongTin = new JPanel(new GridLayout(3, 4, 15, 15));
        pnlThongTin.setBackground(Color.WHITE);
        pnlThongTin.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        txtMaSP = new JTextField();
        txtTenSP = new JTextField();
        txtSoLuongTon = new JTextField();
        txtGiaNiemYet = new JTextField();
        txtGiaBan = new JTextField();

        cboDonViTinh = new JComboBox<>(new String[] {
                "Cái", "Kg", "Hộp", "Gói", "Chai"
        });

        pnlThongTin.add(new JLabel("Mã sản phẩm:"));
        pnlThongTin.add(txtMaSP);
        pnlThongTin.add(new JLabel("Số lượng tồn:"));
        pnlThongTin.add(txtSoLuongTon);

        pnlThongTin.add(new JLabel("Tên sản phẩm:"));
        pnlThongTin.add(txtTenSP);
        pnlThongTin.add(new JLabel("Giá niêm yết:"));
        pnlThongTin.add(txtGiaNiemYet);

        pnlThongTin.add(new JLabel("Đơn vị tính:"));
        pnlThongTin.add(cboDonViTinh);
        pnlThongTin.add(new JLabel("Giá bán:"));
        pnlThongTin.add(txtGiaBan);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlButton.setBackground(Color.WHITE);

        btnTaoMa = new JButton("Tạo mã");
        btnLuu = new JButton("Lưu");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        pnlButton.add(btnTaoMa);
        pnlButton.add(btnLuu);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLamMoi);

        pnlForm.add(pnlThongTin, BorderLayout.CENTER);
        pnlForm.add(pnlButton, BorderLayout.SOUTH);

        pnlTop.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        txtTimMaSP = new JTextField(12);
        txtTimTenSP = new JTextField(12);
        txtGiaTu = new JTextField(7);
        txtGiaDen = new JTextField(7);

        pnlSearch.add(new JLabel("Mã sản phẩm:"));
        pnlSearch.add(txtTimMaSP);

        pnlSearch.add(new JLabel("Tên sản phẩm:"));
        pnlSearch.add(txtTimTenSP);

        pnlSearch.add(new JLabel("Giá bán:"));
        pnlSearch.add(new JLabel("Từ:"));
        pnlSearch.add(txtGiaTu);

        pnlSearch.add(new JLabel("Đến:"));
        pnlSearch.add(txtGiaDen);

        String[] columns = {
                "Hình ảnh",
                "Mã sản phẩm",
                "Tên sản phẩm",
                "Đơn vị tính",
                "Số lượng tồn",
                "Giá niêm yết",
                "Giá bán"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblSanPham = new JTable(model);
        tblSanPham.setRowHeight(55);
        tblSanPham.setFont(new Font("Arial", Font.PLAIN, 13));
        tblSanPham.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblSanPham.setGridColor(Color.GRAY);
        ArrayList<SanPham> dsSP = sp_dao.getAllSanPham();

        for (SanPham sp : dsSP) {
            model.addRow(new Object[] {
                null,
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getDonViTinh(),
                sp.getSoLuongTon(),
                sp.getGiaNY(),
                sp.getGiaNY()
            });
        }

        tblSanPham.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblSanPham.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblSanPham.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblSanPham.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblSanPham.getColumnModel().getColumn(5).setPreferredWidth(130);
        tblSanPham.getColumnModel().getColumn(6).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(tblSanPham);

        

        JPanel pnlBottom = new JPanel(new BorderLayout(0, 10));
        pnlBottom.setBackground(Color.WHITE);

        pnlBottom.add(pnlSearch, BorderLayout.NORTH);
        pnlBottom.add(scrollPane, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlBottom, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
    	SanPhamGUI frame = new SanPhamGUI();
    	
		
        frame.setTitle("Quản lý sản phẩm");
        frame.setSize(1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
}
