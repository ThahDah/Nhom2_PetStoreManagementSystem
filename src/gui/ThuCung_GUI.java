package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatLightLaf;

import entity.DoHungDu;
import entity.KhachHang;
import entity.Loai;
import entity.TrangThai;

public class ThuCung_GUI extends JFrame implements ActionListener {
    private JTextField txtMTC;
    private JTextField txtTenTC;
    private JComboBox<Loai> loaiTC;
    private JTextField txtCanNang;
    private JTextField txtGhiChu;
    private JComboBox<TrangThai> trangThai;
    private JComboBox<DoHungDu> doHungDu;
    private JComboBox<KhachHang> chuSoHuu;
    private JButton btnThem;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnPhatSinhMa;
    private JButton btnTimKiem;
    private DefaultTableModel model;
    private Vector<?> strDSTC;
    private JTable table;
    private TableRowSorter<?> rowSorter; // AI hỗ trợ sửa lỗi Thêm dấu <?> để tránh warning
	private JTextField txtTimMTC;

    public ThuCung_GUI() {
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 

        // --- NORTH PANEL ---
        JPanel nothTitle = new JPanel();
        JLabel lblNTitile = new JLabel("Quản lý thú cưng");
        lblNTitile.setFont(new Font("Arial", Font.BOLD, 25));
        lblNTitile.setForeground(Color.blue);
        nothTitle.add(lblNTitile);
        this.add(nothTitle, BorderLayout.NORTH);

        // --- WEST PANEL---
        JPanel thongTinKH = new JPanel();
        thongTinKH.setBorder(BorderFactory.createTitledBorder("Thông tin thú cưng"));

        Box b,b1,b2,b3,b4,b5,b6,b7,b8,b9,b10;
		b=Box.createVerticalBox();
		b1=Box.createHorizontalBox();
		b2=Box.createHorizontalBox();
		b3=Box.createHorizontalBox();
		b4=Box.createHorizontalBox();
		b5=Box.createHorizontalBox();
		b6=Box.createHorizontalBox();
		b7=Box.createHorizontalBox();
		b8=Box.createHorizontalBox();
		b9=Box.createHorizontalBox();
		b10=Box.createHorizontalBox();

        
        JLabel maTC = new JLabel("Mã thú cưng:   ");
        txtMTC = new JTextField(10);
        txtMTC.setEditable(false);
        b1.add(maTC);
        b1.add(txtMTC);
        b.add(b1);
        b.add(Box.createVerticalStrut(20)); 

        JLabel tenTC = new JLabel("Tên thú cưng:");
        tenTC.setPreferredSize(maTC.getPreferredSize());
        txtTenTC = new JTextField(30);
        b2.add(tenTC);
        b2.add(txtTenTC);
        b.add(b2);
        b.add(Box.createVerticalStrut(20));

        JLabel lblLoaiTC = new JLabel("Loại thú cưng:");
        lblLoaiTC.setPreferredSize(maTC.getPreferredSize());
        loaiTC = new JComboBox<Loai>(Loai.values());
        b3.add(lblLoaiTC);
        b3.add(loaiTC);
        b.add(b3);
        b.add(Box.createVerticalStrut(20));

        JLabel canNang = new JLabel("Cân nặng:");
        canNang.setPreferredSize(maTC.getPreferredSize());
        txtCanNang = new JTextField(10);
        b4.add(canNang);
        b4.add(txtCanNang);
        b.add(b4);
        b.add(Box.createVerticalStrut(20));

        JLabel ghiChu = new JLabel("Ghi chú:");
        ghiChu.setPreferredSize(maTC.getPreferredSize());
        txtGhiChu = new JTextField(10);
        b5.add(ghiChu);
        b5.add(txtGhiChu);
        b.add(b5);
        b.add(Box.createVerticalStrut(20));

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setPreferredSize(maTC.getPreferredSize());
        trangThai = new JComboBox<TrangThai>(TrangThai.values());
        b6.add(lblTrangThai);
        b6.add(trangThai);
        b.add(b6);
        b.add(Box.createVerticalStrut(20));

        JLabel lblDoHD = new JLabel("Độ hung dữ:");
        lblDoHD.setPreferredSize(maTC.getPreferredSize());
        doHungDu = new JComboBox<DoHungDu>(DoHungDu.values());
        b7.add(lblDoHD);
        b7.add(doHungDu);
        b.add(b7);
        b.add(Box.createVerticalStrut(20));

        JLabel lblChuSoHuu = new JLabel("Chủ sở hữu:");
        lblChuSoHuu.setPreferredSize(maTC.getPreferredSize());
        chuSoHuu = new JComboBox<KhachHang>();
        b8.add(lblChuSoHuu);
        b8.add(chuSoHuu);
        b.add(b8);
        b.add(Box.createVerticalStrut(20));

        JMenuBar menu = new JMenuBar();
        btnThem = new JButton("Thêm");
        btnCapNhat = new JButton("Cập nhật");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        
        // dk sk cho cac nut
        btnThem.addActionListener(this);
        btnCapNhat.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);

        menu.add(btnThem);
        menu.add(Box.createHorizontalStrut(5));
        menu.add(btnCapNhat);
        menu.add(Box.createHorizontalStrut(5));
        menu.add(btnXoa);
        menu.add(Box.createHorizontalStrut(5));
        menu.add(btnLamMoi);
        b9.add(menu);
        b.add(b9);
        b.add(Box.createVerticalStrut(10));

        // nut phat sinh ma
        JMenuBar menu2 = new JMenuBar();
        btnPhatSinhMa = new JButton("Phát sinh mã");
        btnPhatSinhMa.addActionListener(this); //dk su kien
        
        menu2.add(Box.createHorizontalStrut(5));
        menu2.add(btnPhatSinhMa);
        b10.add(menu2);
        b.add(b10);

        thongTinKH.add(b);
        this.add(thongTinKH, BorderLayout.WEST);
        
      //center panel
  		Box c,c1,c2;
  		c=Box.createVerticalBox();
  		c1=Box.createHorizontalBox();
  		c2=Box.createHorizontalBox();
  		
  		JPanel pTimKH=new JPanel();
  		pTimKH.setBorder(BorderFactory.createTitledBorder("Tìm kiếm thú cưng"));

  		
  		JLabel lblSDTKH=new JLabel("Nhập mã thú cưng:");
  		txtTimMTC=new JTextField(10);
  		pTimKH.add(lblSDTKH); pTimKH.add(txtTimMTC);
  		
  		
  		btnTimKiem = new JButton("Tìm kiếm");
  		pTimKH.add(btnTimKiem);
  		
  		c.add(pTimKH);
  		
  		
  		JPanel dsKH=new JPanel();
  		dsKH.setBorder(BorderFactory.createTitledBorder("Danh sách thú cưng"));
  		String[] strDSKH= {"Mã TC","Tên TC","Loại","Cân nặng","Ghi chú","Trạng thái","Độ hung dữ","Chủ sở hữu"};
  		model = new DefaultTableModel(strDSKH, 0) {
  		    @Override
  		    public Class<?> getColumnClass(int columnIndex) {
  		        // khai bao cot la so nguyen
  		        if (columnIndex == 3 || columnIndex == 4) {
  		            return Integer.class;
  		        }
  		        return String.class;
  		    }
  		    @Override
  		    public boolean isCellEditable(int row, int column) {
  		        return false; // khong cho user sua
  		    }
  		};
  		// ===================================
  		

  		table=new JTable(model);

  		// bo loc sort
  		rowSorter = new TableRowSorter<>(model);
  		table.setRowSorter(rowSorter);
  		
  		JScrollPane scrollPaneKH=new JScrollPane(table);
  		c2.add(scrollPaneKH);
  		c.add(c2);
  		
  		
  		this.add(c);

        //ket noi database
         try {
             connectDB.ConnectDB.getInstance().connect();
             System.out.println("Kết nối cơ sở dữ liệu thành công!");
             DocDuLieuVaoTable();
         } catch (java.sql.SQLException e) {
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Không thể kết nối tới CSDL!\n" + e.getMessage());
         }
    } 
    
    public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			FlatLightLaf.setup();

	        UIManager.put("Button.arc", 15);
	        UIManager.put("Component.arc", 12);
	        UIManager.put("TextComponent.arc", 10);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			new ThuCung_GUI().setVisible(true);
		});
	}

    private void DocDuLieuVaoTable() {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnThem)) {
           
        } else if (o.equals(btnLamMoi)) {
            
        }
        
    }
}