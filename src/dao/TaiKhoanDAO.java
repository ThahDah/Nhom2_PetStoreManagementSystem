package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TrangThai;

public class TaiKhoanDAO {

	/** dang nhap bang tenTaiKhoan + matKhau, thanh cong thi tra ve nhan vien tuong ung */
	public NhanVien dangNhap(String tenTaiKhoan, String matKhau) throws NghiepVuException, SQLException {
		String sql = "SELECT tk.matKhau, tk.trangThaiTK, "
				+ "nv.maNV, nv.tenNV, nv.soDienThoai, nv.email, nv.trangThaiNV, nv.hinhAnh "
				+ "FROM TaiKhoan tk "
				+ "JOIN NhanVien nv ON tk.maNV = nv.maNV "
				+ "WHERE tk.tenTaiKhoan = ?";
		Connection con = ConnectDB.getConnection();
		try (PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, tenTaiKhoan);
			try (ResultSet rs = stm.executeQuery()) {
				// sai ten hoac sai mat khau deu bao chung mot thong bao
				if (!rs.next() || !matKhau.equals(rs.getString("matKhau").trim())) {
					throw new NghiepVuException("Sai tên tài khoản hoặc mật khẩu");
				}
				if (!"HOAT_DONG".equals(rs.getString("trangThaiTK").trim())) {
					throw new NghiepVuException("Tài khoản đã bị khóa");
				}
				String trangThaiNV = rs.getString("trangThaiNV").trim();
				if ("DA_NGHI".equals(trangThaiNV)) {
					throw new NghiepVuException("Nhân viên này đã nghỉ việc");
				}
				return new NhanVien(rs.getString("maNV").trim(), rs.getString("tenNV"),
						rs.getString("soDienThoai"), rs.getString("email"),
						TrangThai.valueOf(trangThaiNV), rs.getString("hinhAnh"));
			}
		}
	}
}