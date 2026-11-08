package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import common.DinhDang;
import dao.ChiTietLichHenDAO;
import dao.LichHenDAO;
import entity.ChiTietLichHen;
import entity.KhachHang;
import entity.LichHen;
import entity.NhanVien;

public class LichHenGUI extends JPanel implements ActionListener, MouseListener {

	private JButton btnTaoLichHen;
	private DefaultTableModel model;
	private JTable table;
	private LichHenDAO lichHenDAO;
	private ChiTietLichHenDAO chiTietLichHenDAO;
	private NhanVien nhanVienDangNhap;
	private ArrayList<LichHen> dsLichHen;
	private JTextField txtTimKiem;
	private JTextField txtTimMaLichHen;
	private JButton btnTim;
	private JButton btnLamMoi;

	public LichHenGUI(NhanVien nhanVienDangNhap) {
		this.nhanVienDangNhap = nhanVienDangNhap;
		lichHenDAO = new LichHenDAO();
		chiTietLichHenDAO = new ChiTietLichHenDAO();
		dsLichHen = new ArrayList<LichHen>();

		setLayout(new BorderLayout(10, 10));

		// nua tren
		JPanel pnlTopHalf = new JPanel();
		pnlTopHalf.setLayout(new BoxLayout(pnlTopHalf, BoxLayout.Y_AXIS));
		pnlTopHalf.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// header
		JPanel pnlHeader = new JPanel(new BorderLayout());
		JLabel lblTitle = new JLabel("Lịch hẹn", JLabel.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
		lblTitle.setForeground(Color.BLUE);
		pnlHeader.add(lblTitle, BorderLayout.CENTER);

		// nut tao lich hen
		JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnTaoLichHen = new JButton("Tạo lịch hẹn");
		pnlHeaderRight.add(btnTaoLichHen);
		pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

		JPanel pnlHeaderLeft = new JPanel();
		pnlHeaderLeft.setPreferredSize(btnTaoLichHen.getPreferredSize());
		pnlHeader.add(pnlHeaderLeft, BorderLayout.WEST);

		JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
		JLabel lblTimKiem = new JLabel("Tìm khách hàng (MãKH/TênKH/SĐT):");
		lblTimKiem.setFont(lblTimKiem.getFont());
		txtTimKiem = new JTextField(20);
		JLabel lblTimMaLichHen = new JLabel("Tìm mã lịch hẹn:");
		txtTimMaLichHen = new JTextField(12);
		btnTim = new JButton("Tìm");
		btnLamMoi = new JButton("Làm mới");
		pnlTimKiem.add(lblTimKiem);
		pnlTimKiem.add(txtTimKiem);
		pnlTimKiem.add(lblTimMaLichHen);
		pnlTimKiem.add(txtTimMaLichHen);
		pnlTimKiem.add(btnTim);
		pnlTimKiem.add(btnLamMoi);
		pnlTimKiem.setMaximumSize(new Dimension(Integer.MAX_VALUE, pnlTimKiem.getPreferredSize().height));
		
		
		pnlTopHalf.add(pnlHeader);
		pnlTopHalf.add(pnlTimKiem);
		add(pnlTopHalf, BorderLayout.NORTH);

		// table
		JPanel pnlTable = new JPanel();
		pnlTable.setLayout(new BorderLayout());
		pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//		String col[] = { "Mã lịch hẹn", "Khách hàng", "Thú cưng", "Dịch vụ", "Ngày hẹn", "Trạng thái" };
		String col[] = { "Mã lịch hẹn", "Khách hàng", "Số điện thoại", "Thú cưng", "Dịch vụ", "Ngày hẹn", "Trạng thái" };
		model = new DefaultTableModel(col, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(26);
		table.getTableHeader().setReorderingAllowed(false);

		JScrollPane pane = new JScrollPane(table);
		pnlTable.add(pane);
		add(pnlTable, BorderLayout.CENTER);

		docDuLieuVaoTable();

		table.addMouseListener(this);
		btnTaoLichHen.addActionListener(this);
		
		btnTim.addActionListener(this);
		btnLamMoi.addActionListener(this);
		txtTimKiem.addActionListener(this);
		txtTimMaLichHen.addActionListener(this);
	}


	// ham doc du lieu vao bang
	public void docDuLieuVaoTable() {
		String tuKhoa = chuanHoa(txtTimKiem.getText());
		String maLichHenTim = chuanHoa(txtTimMaLichHen.getText());
		model.setRowCount(0);
		dsLichHen.clear();
		try {
			for (LichHen lichHen : lichHenDAO.layTatCa()) {
				if (!chuanHoa(lichHen.getMaLH()).contains(maLichHenTim)) {
					continue;
				}
				ArrayList<ChiTietLichHen> dsChiTiet = new ArrayList<ChiTietLichHen>(
						chiTietLichHenDAO.timTheoLichHen(lichHen));
				KhachHang khachHang = dsChiTiet.isEmpty() ? null : dsChiTiet.get(0).getThuCung().getChuSoHuu();
				if (!tuKhoa.isEmpty()) {
				    if (khachHang == null) {
				        continue;
				    }
				    if (!chuanHoa(khachHang.getMaKH()).contains(tuKhoa)
				            && !chuanHoa(khachHang.getTenKH()).contains(tuKhoa) && !chuanHoa(khachHang.getSoDienThoai()).contains(tuKhoa)) {
				        continue;
				    }
				}
				
				
//				StringBuilder tenThuCung = new StringBuilder();
//				StringBuilder tenDichVu = new StringBuilder();
//				for (ChiTietLichHen chiTiet : dsChiTiet) {
//					if (tenThuCung.length() > 0) {
//						tenThuCung.append(", ");
//						tenDichVu.append(", ");
//					}
//					tenThuCung.append(chiTiet.getThuCung().getTenThuCung());
//					tenDichVu.append(chiTiet.getDichVu().getTenDV());
//				}
				// moi ten thu cung / dich vu chi hien mot lan
				LinkedHashSet<String> dsTenThuCung = new LinkedHashSet<String>();
				LinkedHashSet<String> dsTenDichVu = new LinkedHashSet<String>();
				for (ChiTietLichHen chiTiet : dsChiTiet) {
					dsTenThuCung.add(chiTiet.getThuCung().getTenThuCung());
					dsTenDichVu.add(chiTiet.getDichVu().getTenDV());
				}
				
				String tenKhachHang = dsChiTiet.isEmpty() ? ""
						: dsChiTiet.get(0).getThuCung().getChuSoHuu().getTenKH();
//				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, tenThuCung.toString(),
//						tenDichVu.toString(), lichHen.getThoiGianHen().format(DinhDang.NGAY),
//						lichHen.getTrangThaiHen() });
				
//				dsLichHen.add(lichHen);
//				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, String.join(", ", dsTenThuCung),
//						String.join(", ", dsTenDichVu), lichHen.getThoiGianHen().format(DinhDang.NGAY),
//						lichHen.getTrangThaiHen() });
				String soDienThoai = khachHang == null ? "" : khachHang.getSoDienThoai();
				dsLichHen.add(lichHen);
				model.addRow(new Object[] { lichHen.getMaLH(), tenKhachHang, soDienThoai, String.join(", ", dsTenThuCung),
						String.join(", ", dsTenDichVu), lichHen.getThoiGianHen().format(DinhDang.NGAY),
						lichHen.getTrangThaiHen() });
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi truy cập CSDL: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Hàm AI tạo
	//Viết hàm Java tên chuanHoa(String s) dùng để chuẩn hóa chuỗi tiếng Việt thành chữ thường không dấu, đã xử lý khoảng trắng thừa và trường hợp null
	public static String chuanHoa(String s) {
        // 1. Kiểm tra trường hợp null
        if (s == null) {
            return ""; // Hoặc trả về null tùy logic nghiệp vụ của bạn
        }

        // 2. Chuyển thành chữ thường và loại bỏ khoảng trắng thừa đầu/cuối
        String result = s.toLowerCase().trim();

        // 3. Thay thế các khoảng trắng liên tiếp ở giữa thành một khoảng trắng duy nhất
        result = result.replaceAll("\\s+", " ");

        // 4. Xử lý riêng ký tự 'đ' và 'Đ' (vì Normalizer không tự chuyển 'đ' thành 'd')
        result = result.replace('đ', 'd');

        // 5. Loại bỏ dấu tiếng Việt sử dụng Normalizer
        String temp = Normalizer.normalize(result, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        result = pattern.matcher(temp).replaceAll("");

        return result;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTaoLichHen)) {
			TaoLichHenGUI taoLichHenGUI = new TaoLichHenGUI(SwingUtilities.getWindowAncestor(this),
					nhanVienDangNhap);
			taoLichHenGUI.setVisible(true);
			if (taoLichHenGUI.isDaTao()) {
				docDuLieuVaoTable();
			}
//		}else if (o.equals(btnTim) || o.equals(txtTimKiem)) {
//		    docDuLieuVaoTable();
//		    if (model.getRowCount() == 0 && !txtTimKiem.getText().trim().isEmpty()) {
//		        JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn của khách hàng này",
//		                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//		    }
//		} else if (o.equals(btnLamMoi)) {
//		    txtTimKiem.setText("");
//		    docDuLieuVaoTable();
//		}
		}else if (o.equals(btnTim) || o.equals(txtTimKiem) || o.equals(txtTimMaLichHen)) {
		    docDuLieuVaoTable();
		    if (model.getRowCount() == 0 && (!txtTimKiem.getText().trim().isEmpty()
		            || !txtTimMaLichHen.getText().trim().isEmpty())) {
		        JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn phù hợp",
		                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		    }
		} else if (o.equals(btnLamMoi)) {
		    txtTimKiem.setText("");
		    txtTimMaLichHen.setText("");
		    docDuLieuVaoTable();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// double click moi mo chi tiet
		if (e.getClickCount() == 2) {
			int row = table.rowAtPoint(e.getPoint());
			if (row >= 0 && row < dsLichHen.size()) {
				new ChiTietLichHenGUI(SwingUtilities.getWindowAncestor(this), dsLichHen.get(row)).setVisible(true);
			}
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
