package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.LoaiSanPham;

public class LoaiSanPham_DAO {
	private Connection con;

	public LoaiSanPham_DAO() {
		con = ConnectDB.getInstance().getConnection();
	}

	public List<LoaiSanPham> layTatCa() {
		List<LoaiSanPham> ds = new ArrayList<>();
		String sql = "SELECT * FROM LoaiSanPham";
		try (Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {

			while (rs.next()) {
				LoaiSanPham lsp = new LoaiSanPham(rs.getString("maLoaiSP"), rs.getString("tenLoai"),
						rs.getString("moTa"), rs.getInt("trangThai"));
				ds.add(lsp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public boolean themLoaiSanPham(LoaiSanPham lsp) {
		String sql = "INSERT INTO LoaiSanPham (maLoaiSP, tenLoai, moTa, trangThai) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, lsp.getMaLoaiSP());
			pstm.setString(2, lsp.getTenLoai());
			pstm.setString(3, lsp.getMoTa());
			pstm.setInt(4, lsp.getTrangThai());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public LoaiSanPham layTheoMaLoai(String maLoai) {
		String sql = "select * from LoaiSanPham where maLoaiSP = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maLoai);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				LoaiSanPham loaiSanPham = new LoaiSanPham(rs.getString("maLoaiSP"), rs.getString("tenLoai"),
						rs.getString("moTa"), rs.getInt("trangThai"));
				return loaiSanPham;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public LoaiSanPham layTheoTenLoai(String tenLoai) {
		String sql = "select * from LoaiSanPham where tenLoai = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, tenLoai);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				LoaiSanPham loaiSanPham = new LoaiSanPham(rs.getString("maLoaiSP"), rs.getString("tenLoai"),
						rs.getString("moTa"), rs.getInt("trangThai"));
				return loaiSanPham;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
