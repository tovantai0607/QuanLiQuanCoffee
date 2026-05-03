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
import Entity.SanPham;
public class SanPham_DAO {
	private Connection con;
	private LoaiSanPham_DAO lsp_dao;

	public SanPham_DAO() {
		con = ConnectDB.getInstance().getConnection();
		this.lsp_dao = new LoaiSanPham_DAO();
	}

	public List<SanPham> layTatCa() {
		List<SanPham> ds = new ArrayList<>();
		String sql = "SELECT * FROM SanPham";
		try (Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
			while (rs.next()) {
				LoaiSanPham lsp = lsp_dao.layTheoMaLoai(rs.getString("maLoaiSP"));

				SanPham sp = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("donViTinh"), rs.getBigDecimal("gia"), rs.getString("hinhAnh"),
						rs.getInt("trangThai"), lsp);
				ds.add(sp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public boolean themSanPham(SanPham sp) {
		String sql = "INSERT INTO SanPham (maSanPham, tenSanPham, donViTinh, gia, hinhAnh, trangThai,maLoaiSP) VALUES (?, ?, ?, ?, ?, ?,?)";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, sp.getMaSanPham());
			pstm.setString(2, sp.getTenSanPham());
			pstm.setString(3, sp.getDonViTinh());
			pstm.setBigDecimal(4, sp.getGia());
			pstm.setString(5, sp.gethinhAnh());
			pstm.setInt(6, sp.getTrangThai());
			pstm.setString(7, sp.getLoaiSP().getMaLoaiSP());
			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean capNhatSanPham(SanPham sp) {
		if (sp.getLoaiSP() == null) {
			System.out.println(sp);
			return false;
		}
		String sql = "UPDATE SanPham SET tenSanPham = ?, donViTinh = ?, gia = ?, hinhAnh = ?, trangThai = ?, maLoaiSP = ? WHERE maSanPham = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, sp.getTenSanPham());
			pstm.setString(2, sp.getDonViTinh());
			pstm.setBigDecimal(3, sp.getGia());
			pstm.setString(4, sp.gethinhAnh());
			pstm.setInt(5, sp.getTrangThai());
			pstm.setString(6, sp.getLoaiSP().getMaLoaiSP());
			pstm.setString(7, sp.getMaSanPham());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public SanPham timTheoMa(String maSP) {
		String sql = "SELECT * FROM SanPham WHERE maSanPham = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maSP);
			ResultSet rs = pstm.executeQuery();

			if (rs.next()) {
				LoaiSanPham lsp = lsp_dao.layTheoMaLoai(rs.getString("maLoaiSP"));

				return new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"), rs.getString("donViTinh"),
						rs.getBigDecimal("gia"), rs.getString("hinhAnh"), rs.getInt("trangThai"), lsp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean xoaSanPham(String maSP) {
		String sql = "DELETE FROM SanPham WHERE maSanPham = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maSP);
			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<SanPham> timTheoLoai(String maLoaiSP) {
		ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
		String sql = "select * from SanPham where maLoaiSP = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maLoaiSP);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				SanPham sp = new SanPham();
				LoaiSanPham lsp = lsp_dao.layTheoMaLoai(rs.getString("maLoaiSP"));
				sp.setMaSanPham(rs.getString("maSanPham"));
				sp.setTenSanPham(rs.getString("tenSanPham"));
				sp.setDonViTinh(rs.getString("donViTinh"));
				sp.setGia(rs.getBigDecimal("gia"));
				sp.sethinhAnh(rs.getString("hinhAnh"));
				sp.setTrangThai(rs.getInt("trangThai"));
				sp.setLoaiSP(lsp);
				dsSP.add(sp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Không tìm theo loại được");
			e.printStackTrace();
		}

		return dsSP;
	}

	public SanPham timSanPhamTheoTen(String tenSanPham) {
		String sql = "select * from SanPham where tenSanPham = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, tenSanPham);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				SanPham sp = new SanPham();
				LoaiSanPham lsp = lsp_dao.layTheoMaLoai(rs.getString("maLoaiSP"));

				sp.setMaSanPham(rs.getString("maSanPham"));
				sp.setTenSanPham(rs.getString("tenSanPham"));
				sp.setDonViTinh(rs.getString("donViTinh"));
				sp.setGia(rs.getBigDecimal("gia"));
				sp.sethinhAnh(rs.getString("hinhAnh"));
				sp.setTrangThai(rs.getInt("trangThai"));
				sp.setLoaiSP(lsp);
				return sp;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Không tìm theo tên được");
			e.printStackTrace();
		}
		return null;
	}

	public String getMaSanPhamCuoiCung() {
		String sql = "select top 1 maSanPham from SanPham order by maSanPham DESC";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getString("maSanPham");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
