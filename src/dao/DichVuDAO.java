package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.DichVu;

public class DichVuDAO {

	public List<DichVu> layTatCa() throws SQLException {
		List<DichVu> dsDichVu = new ArrayList<>();
		String sql = "SELECT maDV, tenDV, giaTieuChuan FROM DichVu ORDER BY maDV";
		try (Connection con = ConnectDB.getConnection();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql)) {
			while (rs.next()) {
				dsDichVu.add(taoDichVu(rs));
			}
		}
		return dsDichVu;
	}

	static DichVu taoDichVu(ResultSet rs) throws SQLException {
		return new DichVu(rs.getString("maDV"), rs.getString("tenDV"), rs.getDouble("giaTieuChuan"));
	}
}
