package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.Ban;

public class Ban_DAO {
	private Connection con;

	public Ban_DAO() {
		con = ConnectDB.getInstance().getConnection();
	}

	public List<Ban> layTatCa() {
		List<Ban> ds = new ArrayList<>();
		String sql = "SELECT * FROM Ban";
		try (Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {

			while (rs.next()) {
				Ban ban = new Ban(rs.getString("maBan"), rs.getString("tenBan"), rs.getInt("soLuongChoNgoi"),
						rs.getString("khuVuc"), rs.getInt("trangThai"), rs.getString("ghiChu"));
				ds.add(ban);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public List<Ban> layTheoTrangThai(int trangThai) {
		List<Ban> ds = new ArrayList<>();
		String sql = "SELECT * FROM Ban WHERE trangThai = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setInt(1, trangThai);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				Ban ban = new Ban(rs.getString("maBan"), rs.getString("tenBan"), rs.getInt("soLuongChoNgoi"),
						rs.getString("khuVuc"), rs.getInt("trangThai"), rs.getString("ghiChu"));
				ds.add(ban);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public boolean themBan(Ban ban) {
		String sql = "INSERT INTO Ban (maBan, tenBan, soLuongChoNgoi, khuVuc, trangThai, ghiChu) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, ban.getMaBan());
			pstm.setString(2, ban.getTenBan());
			pstm.setInt(3, ban.getSoLuongChoNgoi());
			pstm.setString(4, ban.getKhuVuc());
			pstm.setInt(5, ban.getTrangThai());
			pstm.setString(6, ban.getGhiChu());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean capNhatBan(Ban ban) {
		String sql = "UPDATE Ban SET tenBan = ?, soLuongChoNgoi = ?, khuVuc = ?, trangThai = ?, ghiChu = ? WHERE maBan = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, ban.getTenBan());
			pstm.setInt(2, ban.getSoLuongChoNgoi());
			pstm.setString(3, ban.getKhuVuc());
			pstm.setInt(4, ban.getTrangThai());
			pstm.setString(5, ban.getGhiChu());
			pstm.setString(6, ban.getMaBan());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Ban timTheoMaBan(String maBan) {
		String sql = "SELECT * FROM Ban WHERE maBan = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maBan);
			ResultSet rs = pstm.executeQuery();

			if (rs.next()) {
				return new Ban(rs.getString("maBan"), rs.getString("tenBan"), rs.getInt("soLuongChoNgoi"),
						rs.getString("khuVuc"), rs.getInt("trangThai"), rs.getString("ghiChu"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean xoaBan(String maBan) {
		String sql = "delete from Ban where maBan = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maBan);
			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public boolean capNhatTrangThaiBan(String maBan, int trangThai) {
		String sql = "update Ban set trangThai = ? where maBan = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setInt(1, trangThai);
			pstm.setString(2, maBan.trim());
			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

}