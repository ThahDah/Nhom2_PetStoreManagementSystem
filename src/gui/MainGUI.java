package gui;
	
	import java.awt.BorderLayout;
	import java.awt.CardLayout;
	import java.awt.Color;
	import java.awt.Dimension;
	import java.awt.Font;
	import java.awt.GridLayout;
	import java.awt.Image;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	
	import javax.swing.BorderFactory;
	import javax.swing.Box;
	import javax.swing.BoxLayout;
	import javax.swing.ImageIcon;
	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JPanel;
	import javax.swing.SwingConstants;
	import javax.swing.UIManager;
	import javax.swing.border.Border;
	
	import connectDB.ConnectDB;
	import entity.NhanVien;
	
	public class MainGUI extends JFrame implements ActionListener  {
		private JPanel pnlTab;
		private JPanel pnlRight;
		private CardLayout cardLayout;
		private BanHangGUI banHang_UI;
		private SanPhamGUI sanPham_UI;
		private HoaDonGUI hoaDon_UI;
		private KhachHangGUI khachHang_UI;
		private ThongKeTaiChinhGUI thongKe_UI;
		private DichVuGUI dichVu_UI;
		private ThuCungGUI thuCung_UI;
		private TaoLichHenGUI taoLichHen_UI;
		private LichHenGUI lichHen_UI;
		private NhanVienGUI nhanVien_UI;
		
		private NhanVien nhanVienDangNhap;
		
		private final String PANEL_BAN_HANG = "BanHang";
		private final String PANEL_SAN_PHAM = "SanPham";
		private final String PANEL_HOA_DON = "HoaDon";
		private final String PANEL_KHACH_HANG = "KhachHang";
		private final String PANEL_THONG_KE = "ThongKe";
		private final String PANEL_DICH_VU = "DichVu";
		private final String PANEL_THU_CUNG = "ThuCung";
		private final String PANEL_TAO_LICH_HEN = "TaoLichHen";
		private final String PANEL_LICH_HEN = "LichHen";
		private final String PANEL_NHAN_VIEN = "NhanVien";
		private JButton btnBanHang;
		private JButton btnHoaDon;
		private JButton btnNhanVien;
		private JButton btnSanPham;
		private JButton btnKhachHang;
		private JButton btnThongKe;
		private JButton btnDichVu;
		private JButton btnThuCung;
		private JButton btnTaoLichHen;
		private JButton btnLichHen;
		private JButton btnDangXuat;
		private JButton btnSelected = null;
		private Object color;
		public MainGUI(NhanVien nhanVienDangNhap) {
			this.nhanVienDangNhap = nhanVienDangNhap;
			try {
				ConnectDB.getInstance().connect();
				System.out.println("Connected!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			setSize(1400,800);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			
			//Tao border xam
			Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1);
			
			//Custom lai cac thanh phan
			UIManager.put("Component.arc", 15);
			UIManager.put("TextComponent.arc", 15);
			UIManager.put("Button.arc", 15);
			
			//========HEADER========
			JPanel pnlTop = new JPanel(new BorderLayout());
			pnlTop.setBackground(Color.white);
			pnlTop.setBorder(lineBorder);
			
			Box boxTop = Box.createHorizontalBox();
			boxTop.setOpaque(false);
			
			//Logo cửa hàng và tên cửa hàng
			ImageIcon logo = new ImageIcon("img/logo/logo_pet_shop.png");
			JLabel lblLogoCuaHang = new JLabel(setImg(logo, 40, 40));
			
			Box boxTitle = Box.createVerticalBox();
			JLabel lblBrandName = new JLabel("LAND");
			lblBrandName.setFont(new Font("Arial",Font.BOLD,18));
			JLabel lblSlogan = new JLabel("Nơi thú cưng được yêu thương");
			lblSlogan.setFont(new Font("Arial",Font.ITALIC,13));
			boxTitle.add(lblBrandName);
			boxTitle.add(lblSlogan);
			
			boxTop.add(lblLogoCuaHang);
			boxTop.add(boxTitle);
			
			pnlTop.add(boxTop);
			this.add(pnlTop, BorderLayout.NORTH);
			
			//========THANH MENU========
			JPanel pnlLeft = new JPanel();
			pnlLeft.setBackground(Color.WHITE);
			pnlLeft.setPreferredSize(new Dimension(150,0));
			pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
			pnlLeft.setBorder(lineBorder);
			
			pnlTab = new JPanel(new GridLayout(15,0,5,5));
			pnlTab.setOpaque(false);
			
			//Nút bán hàng
			ImageIcon cartIcon = new ImageIcon("img/icon/icon_cart_shopping.png");
			btnBanHang = new JButton("Bán hàng");
			btnBanHang.setIcon(setImg(cartIcon, 20, 20));
			btnBanHang.setBorderPainted(false);
			btnBanHang.setOpaque(false);
			btnBanHang.setContentAreaFilled(false);
			btnBanHang.setHorizontalAlignment(SwingConstants.LEFT); 
			btnBanHang.setIconTextGap(15); 
			btnBanHang.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			//Nút Sản phẩm
			ImageIcon productIcon = new ImageIcon("img/icon/icon_product.png");
			btnSanPham = new JButton("Sản phẩm");
			btnSanPham.setIcon(setImg(productIcon, 20, 20));
			btnSanPham.setBorderPainted(false);
			btnSanPham.setFocusPainted(false);
			btnSanPham.setContentAreaFilled(false);
			btnSanPham.setHorizontalAlignment(SwingConstants.LEFT); 
			btnSanPham.setIconTextGap(15); 
			btnSanPham.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));	
			
			//Nút hóa đơn
			ImageIcon invoiceIcon = new ImageIcon("img/icon/icon_invoice.png");
			btnHoaDon = new JButton("Hóa đơn");
			btnHoaDon.setIcon(setImg(invoiceIcon, 20, 20));
			btnHoaDon.setBorderPainted(false);
			btnHoaDon.setFocusPainted(false);
			btnHoaDon.setContentAreaFilled(false);
			btnHoaDon.setHorizontalAlignment(SwingConstants.LEFT); 
			btnHoaDon.setIconTextGap(15); 
			btnHoaDon.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			
			//Nút Khách hàng
			ImageIcon customerIcon = new ImageIcon("img/icon/icon_customer.png");
			btnKhachHang = new JButton("Khách hàng");
			btnKhachHang.setIcon(setImg(customerIcon, 20, 20));
			btnKhachHang.setBorderPainted(false);
			btnKhachHang.setFocusPainted(false);
			btnKhachHang.setContentAreaFilled(false);
			btnKhachHang.setHorizontalAlignment(SwingConstants.LEFT); 
			btnKhachHang.setIconTextGap(15); 
			btnKhachHang.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			//Nút Thống kê
			ImageIcon statisticalIcon = new ImageIcon("img/icon/icon_statistical.png");
			btnThongKe = new JButton("Thông kê");
			btnThongKe.setIcon(setImg(statisticalIcon, 20, 20));
			btnThongKe.setBorderPainted(false);
			btnThongKe.setFocusPainted(false);
			btnThongKe.setContentAreaFilled(false);
			btnThongKe.setHorizontalAlignment(SwingConstants.LEFT); 
			btnThongKe.setIconTextGap(15); 
			btnThongKe.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			//Nút Dịch vụ
			ImageIcon serviceIcon = new ImageIcon("img/icon/icon_service.png");
			btnDichVu = new JButton("Dịch vụ");
			btnDichVu.setIcon(setImg(serviceIcon, 20, 20));
			btnDichVu.setBorderPainted(false);
			btnDichVu.setFocusPainted(false);
			btnDichVu.setContentAreaFilled(false);
			btnDichVu.setHorizontalAlignment(SwingConstants.LEFT); 
			btnDichVu.setIconTextGap(15); 
			btnDichVu.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			//Nút thú cưng	
			ImageIcon petIcon = new ImageIcon("img/icon/icon_pet.png");
			btnThuCung = new JButton("Thú cưng");
			btnThuCung.setIcon(setImg(petIcon, 20, 20));
			btnThuCung.setBorderPainted(false);
			btnThuCung.setFocusPainted(false);
			btnThuCung.setContentAreaFilled(false);
			btnThuCung.setHorizontalAlignment(SwingConstants.LEFT); 
			btnThuCung.setIconTextGap(15); 
			btnThuCung.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			
			//Nút tạo lịch hẹn
			ImageIcon appointmentIcon = new ImageIcon("img/icon/icon_appointment.png");
			btnLichHen = new JButton("Lịch hẹn");
			btnLichHen.setIcon(setImg(appointmentIcon, 20, 20));
			btnLichHen.setBorderPainted(false);
			btnLichHen.setFocusPainted(false);
			btnLichHen.setContentAreaFilled(false);
			btnLichHen.setHorizontalAlignment(SwingConstants.LEFT); 
			btnLichHen.setIconTextGap(15); 
			btnLichHen.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			//Nút nhân viên
			ImageIcon empIcon = new ImageIcon("img/icon/icon_employee.png");
			btnNhanVien = new JButton("Nhân viên");
			btnNhanVien.setIcon(setImg(empIcon, 20, 20));
			btnNhanVien.setBorderPainted(false);
			btnNhanVien.setFocusPainted(false);
			btnNhanVien.setContentAreaFilled(false);
			btnNhanVien.setHorizontalAlignment(SwingConstants.LEFT); 
			btnNhanVien.setIconTextGap(15); 
			btnNhanVien.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
					
			ImageIcon iconExit = new ImageIcon("img/icon/icon_exit.png");
			btnDangXuat = new JButton("Đăng xuất");
			btnDangXuat.setIcon(setImg(iconExit, 15, 15));
			btnDangXuat.setBorderPainted(false);
			btnDangXuat.setContentAreaFilled(false);
			btnDangXuat.setForeground(Color.red);
			btnDangXuat.setHorizontalAlignment(SwingConstants.LEFT); 
			btnDangXuat.setIconTextGap(15); 
			btnDangXuat.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
			
			pnlTab.add(btnBanHang);
			pnlTab.add(btnSanPham);
			pnlTab.add(btnHoaDon);
			pnlTab.add(btnKhachHang);
			pnlTab.add(btnThongKe);
			pnlTab.add(btnDichVu);
			pnlTab.add(btnThuCung);
			pnlTab.add(btnLichHen);
			pnlTab.add(btnNhanVien);
			pnlTab.add(Box.createVerticalStrut(45));
			pnlTab.add(btnDangXuat);
			
			
			pnlLeft.add(pnlTab);
			this.add(pnlLeft, BorderLayout.WEST);
			
			//========MÀN HÌNH CHÍNH========
			pnlRight = new JPanel();
			pnlRight.setBorder(lineBorder);
			cardLayout = new CardLayout(); 
		    pnlRight.setLayout(cardLayout);
		    pnlRight.setBackground(Color.white);
		    
		    banHang_UI = new BanHangGUI(nhanVienDangNhap);
		    sanPham_UI = new SanPhamGUI();
		    hoaDon_UI= new HoaDonGUI();
		    khachHang_UI = new KhachHangGUI();
		    thongKe_UI = new ThongKeTaiChinhGUI();
		    dichVu_UI = new DichVuGUI();
		    thuCung_UI = new ThuCungGUI();
		    lichHen_UI = new LichHenGUI(nhanVienDangNhap);
		    nhanVien_UI = new NhanVienGUI();
		
		    pnlRight.add(banHang_UI, PANEL_BAN_HANG );
		    pnlRight.add(sanPham_UI, PANEL_SAN_PHAM );
		    pnlRight.add(hoaDon_UI, PANEL_HOA_DON );
		    pnlRight.add(khachHang_UI, PANEL_KHACH_HANG );
		    pnlRight.add(thongKe_UI, PANEL_THONG_KE );
		    pnlRight.add(dichVu_UI, PANEL_DICH_VU );
		    pnlRight.add(thuCung_UI, PANEL_THU_CUNG );
		 
		    pnlRight.add(lichHen_UI, PANEL_LICH_HEN );
		    pnlRight.add(nhanVien_UI, PANEL_NHAN_VIEN );
		    
			this.add(pnlRight);
			
			
			btnBanHang.addActionListener(this);
			btnSanPham.addActionListener(this);
			btnHoaDon.addActionListener(this);
			btnKhachHang.addActionListener(this);
			btnThongKe.addActionListener(this);
			btnDichVu.addActionListener(this);
			btnThuCung.addActionListener(this);
			btnLichHen.addActionListener(this);
			btnNhanVien.addActionListener(this);
			btnDangXuat.addActionListener(this);
			
		}
		
		//Resize ảnh và chuyển định dạng thành ImageIcon để chèn vô JLabel và JButton
		public ImageIcon setImg(ImageIcon icon, int width, int height) {
			Image img = icon.getImage();
			Image edit = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon editedImg = new ImageIcon(edit);
			return editedImg;
		}
		
		private void setBanHang() {
		    cardLayout.show(pnlRight, PANEL_BAN_HANG);
		}
		
		private void setSanPham() {
		    cardLayout.show(pnlRight, PANEL_SAN_PHAM);
		}
		
		private void setHoaDon() {
		    cardLayout.show(pnlRight, PANEL_HOA_DON);
		}
		
		private void setKhachHang() {
		    cardLayout.show(pnlRight, PANEL_KHACH_HANG);
		}
		
		private void setThongKe() {
		    cardLayout.show(pnlRight, PANEL_THONG_KE);
		}
		
		private void setDichVu() {
		    cardLayout.show(pnlRight, PANEL_DICH_VU);
		}
		
		private void setThuCung() {
		    cardLayout.show(pnlRight, PANEL_THU_CUNG);
		}
		
		
		private void setLichHen() {
		    cardLayout.show(pnlRight, PANEL_LICH_HEN);
		}
		
		private void setNhanVien() {
		    cardLayout.show(pnlRight, PANEL_NHAN_VIEN);
		}
		
		private void updateButtonColor(JButton clickedButton) {
		    //Reset màu cho nút đã chọn trước đó 
			
		    if (btnSelected != null) {
		    	btnSelected.setContentAreaFilled(false);
		    }
	
		    //Thiết lập màu cho nút vừa click
		    clickedButton.setContentAreaFilled(true);
		    clickedButton.setBackground(new Color(173, 255, 47)); 
		    
		    // Cập nhật nút hiện tại vào biến tạm
		    btnSelected = clickedButton;
		    
		    // Vẽ lại nút đang chọn
		    clickedButton.repaint();
			}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			
			if (o instanceof JButton && o != btnDangXuat) {
				btnBanHang.setOpaque(false);
				btnBanHang.setBackground(Color.white);
		        updateButtonColor((JButton) o);
		    }
			
			if(o == btnBanHang) {
				setBanHang();
			}
			else if(o == btnSanPham) {
				setSanPham();
			}
			else if(o == btnHoaDon) {
				setHoaDon();
			}
			else if(o == btnKhachHang) {
				setKhachHang();
			}
			else if(o == btnThongKe) {
				setThongKe();
			}
			else if(o == btnDichVu) {
				setDichVu();
			}
			else if(o == btnThuCung) {
				setThuCung();
			}
			else if(o == btnLichHen) {
				setLichHen();
			}
			else if(o == btnNhanVien) {
				setNhanVien();
			}
			else if(o == btnDangXuat) {
				dangXuat();
			}
			
		}
		
		public void dangXuat() {
			new LoginGUI().setVisible(true);
			this.dispose();
		}
	}
