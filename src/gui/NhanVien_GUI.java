package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.Struct;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import com.formdev.flatlaf.FlatLightLaf;

import entity.TrangThai;


//import dao.NhanVien_dao;

public class NhanVien_GUI extends JFrame implements ActionListener, MouseListener{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton btnPickAvatar;
	
	private JTextField txtMNV;
	private JTextField txtTenNV;
	private JTextField txtSDTNV;
	private JTextField txtEmail;
	
	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLamMoi;
	
	private JTextField txtTimMaNV;
	private JTextField txtTimTenNV;
	private JTextField txtTimSDTNV;
	
	private DefaultTableModel model;
	private JTable table;

	private JButton btnTim;
	
//	private NhanVien_dao nv_dao = new NhanVien_dao();
    private TableRowSorter<DefaultTableModel> rowSorter;

	private JComboBox<String> cbTimCaLam;

	private String duongDanAnh;

	private JLabel lblAvatar;

	private JComboBox<TrangThai> cbTrangThai;

	private AbstractButton btnPhatSinhMa;

	public NhanVien_GUI() {
		
		setSize(1450, 800);
		setLayout(new BorderLayout());
		setVisible(true);

		Box b = Box.createVerticalBox();
		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();

		// Tieu de Noth
		JPanel notharea = new JPanel();
		JLabel lblNTitile = new JLabel("QUẢN LÝ NHÂN VIÊN");
		lblNTitile.setFont(new Font("Arial", Font.BOLD, 25));
		lblNTitile.setForeground(Color.blue);
		notharea.add(lblNTitile);
		b1.add(notharea);
		b.add(b1);

		// Phan hinh anh ben trai
		Box bAva = Box.createVerticalBox();
		lblAvatar = new JLabel();
		Dimension size = new Dimension(150, 200);
		lblAvatar.setPreferredSize(size);
		lblAvatar.setMinimumSize(size);
		lblAvatar.setMaximumSize(size);
		lblAvatar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblAvatar.setAlignmentX(CENTER_ALIGNMENT);

		btnPickAvatar = new JButton("Chọn ảnh");
		btnPickAvatar.setAlignmentX(CENTER_ALIGNMENT);

		bAva.add(lblAvatar);
		bAva.add(Box.createVerticalStrut(10));
		bAva.add(btnPickAvatar);

		b2.add(Box.createHorizontalStrut(15));
		b2.add(bAva);
		b2.add(Box.createHorizontalStrut(15));
		
		
		// form ben phai
		JPanel form = new JPanel();
		// form, dung border
		form.setLayout(new BorderLayout(0, 20)); 
		form.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên:"));

		//chi tiet trong form
		JPanel pGridFirst = new JPanel(new GridLayout(6, 1, 0, 15));

		// Dòng 1
		JPanel pDong1 = new JPanel(new GridLayout(1, 4, 12, 0));
		pDong1.add(new JLabel("Mã nhân viên:"));
		pDong1.add(txtMNV = new JTextField(15));
		txtMNV.setEditable(false);
		pDong1.add(new JLabel("Tên nhân viên:"));
		pDong1.add(txtTenNV = new JTextField(15));

		// Dòng 2
		JPanel pDong2 = new JPanel(new GridLayout(1, 4, 15, 0));
		pDong2.add(new JLabel("Số điện thoại:"));
		pDong2.add(txtSDTNV = new JTextField(15));
		pDong2.add(new JLabel("Email:"));         
		pDong2.add(txtEmail = new JTextField(15));
		
		//Dòng 3
		JPanel pDong3 = new JPanel(new GridLayout(1, 4, 15, 0));
		pDong3.add(new JLabel("Trạng thái:"));
		cbTrangThai = new JComboBox<entity.TrangThai>(entity.TrangThai.values());
		pDong3.add(cbTrangThai);
		pDong3.add(Box.createHorizontalStrut(50));


		pGridFirst.add(pDong1); 
		pGridFirst.add(pDong2); 
		pGridFirst.add(pDong3); 

		// nut bam la flowlayout
		JPanel pGridSecond = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0)); 
		Dimension btnSize = new Dimension(120, 30); // thu nho nut

		btnPhatSinhMa = new JButton("Phát sinh mã"); btnPhatSinhMa.setPreferredSize(btnSize);
		btnThem = new JButton("Thêm"); btnThem.setPreferredSize(btnSize);
		btnSua = new JButton("Sửa"); btnSua.setPreferredSize(btnSize);
		btnXoa = new JButton("Xóa"); btnXoa.setPreferredSize(btnSize);
		btnLamMoi = new JButton("Làm mới"); btnLamMoi.setPreferredSize(btnSize);

		pGridSecond.add(btnPhatSinhMa);
		pGridSecond.add(btnThem);
		pGridSecond.add(btnSua);
		pGridSecond.add(btnXoa);
		pGridSecond.add(btnLamMoi);

		// them 2 khu vuc vao form
		form.add(pGridFirst, BorderLayout.CENTER); 
		form.add(pGridSecond, BorderLayout.SOUTH); 

		b2.add(form);
		b.add(b2);

		// Tim kiem
		JPanel pTim = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); //flow layout
		pTim.setBorder(BorderFactory.createTitledBorder("Tìm kiếm nhân viên"));
		
		pTim.add(new JLabel("Mã nhân viên:"));
		txtTimMaNV = new JTextField(10);
		pTim.add(txtTimMaNV);
		
		
		btnTim= new JButton("Tìm");
		pTim.add(btnTim);
		
		b3.add(pTim);
		b.add(b3);

		//Box noth
		this.add(b, BorderLayout.NORTH);

		// Phan center, table
		String[] tititleTable = {"Hình ảnh","Mã NV", "Tên NV", "SĐT", "Email", "Trang thai"};
		model = new DefaultTableModel(tititleTable, 0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // khong cho sua truc tiep
		        return false;
		    }
		};

		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		//xu ly su kien
		rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);
        
        //Them vao table
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMinWidth(60);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		
		//Hien thi hinh anh tren table
		// 1. Tăng chiều cao hàng để nhìn thấy ảnh 
		table.setRowHeight(60); 

		// 2. Thiết lập bộ vẽ (Renderer) cho cột số 0
		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		            boolean hasFocus, int row, int column) {
		        
		        // Kế thừa JLabel từ lớp cha
		        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		        label.setText(""); // Xóa đường dẫn text để hiện hình
		        label.setHorizontalAlignment(JLabel.CENTER);

		        if (value != null && !value.toString().trim().isEmpty()) {
		            String path = value.toString();
		            File fileAnh = new File(path);

		            if (fileAnh.exists()) {
		                ImageIcon icon = new ImageIcon(path);
		                // Scale ảnh cho vừa ô (40x55)
		                Image img = icon.getImage().getScaledInstance(40, 55, Image.SCALE_SMOOTH);
		                label.setIcon(new ImageIcon(img));
		            } else {
		                label.setIcon(null);
		                label.setText("Lỗi ảnh");
		            }
		        } else {
		            label.setIcon(null);
		        }
		        return label;
		    }
		});

        // sk click, nut bam
		btnPhatSinhMa.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTim.addActionListener(this);
        btnPickAvatar.addActionListener(this);      
        table.addMouseListener(this);

        // ket noi csdl
//        try {
//            connectDB.ConnectDB.getInstance().connect();
//            System.out.println("Kết nối cơ sở dữ liệu thành công!");
//            DocDuLieuVaoTable();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Không thể kết nối tới Cơ sở dữ liệu!\n" + e.getMessage());
//        }
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
			new NhanVien_GUI().setVisible(true);
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}