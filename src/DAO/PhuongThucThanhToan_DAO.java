package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.PhuongThucThanhToan;

public class PhuongThucThanhToan_DAO {
	private Connection con;

	public PhuongThucThanhToan_DAO() {
		con = ConnectDB.getInstance().getConnection();
	}

	public List<PhuongThucThanhToan> layTatCa() {
		List<PhuongThucThanhToan> ds = new ArrayList<>();
		String sql = "SELECT * FROM PhuongThucThanhToan WHERE trangThai = 1";
		try (Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {

			while (rs.next()) {
				PhuongThucThanhToan pttt = new PhuongThucThanhToan(rs.getString("maPTTT"), rs.getString("tenPTTT"),
						rs.getInt("trangThai"), rs.getString("moTa"));
				ds.add(pttt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public PhuongThucThanhToan layPTTTTheoTen(String tenPTTT) {
		String sql = "select * from PhuongThucThanhToan where tenPTTT = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, tenPTTT);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				PhuongThucThanhToan pttt = new PhuongThucThanhToan(rs.getString("maPTTT"), rs.getString("tenPTTT"),
						rs.getInt("trangThai"), rs.getString("moTa"));
				return pttt;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PhuongThucThanhToan layPTTTTheoMa(String maPTTT) {
		String sql = "select * from PhuongThucThanhToan where maPTTT = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maPTTT);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				PhuongThucThanhToan pttt = new PhuongThucThanhToan(rs.getString("maPTTT"), rs.getString("tenPTTT"),
						rs.getInt("trangThai"), rs.getString("moTa"));
				return pttt;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}