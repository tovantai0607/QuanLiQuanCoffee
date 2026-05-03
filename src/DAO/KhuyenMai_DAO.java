package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.KhuyenMai;

public class KhuyenMai_DAO {
	private Connection con;

	public KhuyenMai_DAO() {
		con = ConnectDB.getInstance().getConnection();
	}

	public List<KhuyenMai> layTatCa() {
		List<KhuyenMai> ds = new ArrayList<>();
		String sql = "SELECT * FROM KhuyenMai";
		try (Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {

			while (rs.next()) {
				KhuyenMai km = new KhuyenMai(rs.getString("maKM"), rs.getString("tenKM"), rs.getDouble("phanTramGiam"),
						rs.getString("loaiKM"), rs.getDate("ngayBatDau").toLocalDate(),
						rs.getDate("ngayKetThuc").toLocalDate(), rs.getInt("trangThai"));
				ds.add(km);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public boolean themKhuyenMai(KhuyenMai km) {
		String sql = "INSERT INTO KhuyenMai (maKM, tenKM, phanTramGiam, loaiKM, ngayBatDau, ngayKetThuc, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, km.getMaKM());
			pstm.setString(2, km.getTenKM());
			pstm.setDouble(3, km.getphanTramGiam());
			pstm.setString(4, km.getloaiKM());
			pstm.setDate(5, java.sql.Date.valueOf(km.getNgayBatDau()));
			pstm.setDate(6, java.sql.Date.valueOf(km.getNgayKetThuc()));
			pstm.setInt(7, km.getTrangThai());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean capNhatKhuyenMai(KhuyenMai khuyenMaiMoi) {
		String sql = "UPDATE KhuyenMai SET tenKM = ?, phanTramGiam = ?, loaiKM = ?, ngayBatDau = ?, ngayKetThuc = ?, trangThai = ? WHERE maKM = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, khuyenMaiMoi.getTenKM());
			pstm.setDouble(2, khuyenMaiMoi.getphanTramGiam());
			pstm.setString(3, khuyenMaiMoi.getloaiKM());
			pstm.setDate(4, java.sql.Date.valueOf(khuyenMaiMoi.getNgayBatDau()));
			pstm.setDate(5, java.sql.Date.valueOf(khuyenMaiMoi.getNgayKetThuc()));
			pstm.setInt(6, khuyenMaiMoi.getTrangThai());

			pstm.setString(7, khuyenMaiMoi.getMaKM());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean xoaKhuyenMai(String maKM) {
		String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
		try (PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, maKM);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public KhuyenMai timTheoMaKhuyenMai(String maKM) {
		String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maKM);

			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					KhuyenMai km = new KhuyenMai(rs.getString("maKM"), rs.getString("tenKM"),
							rs.getDouble("phanTramGiam"), rs.getString("loaiKM"),
							rs.getDate("ngayBatDau").toLocalDate(), rs.getDate("ngayKetThuc").toLocalDate(),
							rs.getInt("trangThai"));
					return km;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean capNhatPhanTramGiam(String maKM, double phanTramGiamMoi) {
		String sql = "UPDATE KhuyenMai SET phanTramGiam = ? WHERE maKM = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setDouble(1, phanTramGiamMoi);
			pstm.setString(2, maKM);

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public KhuyenMai timKhuyenMaiTheoTen(String tenKM) {
		String sql = "select * from KhuyenMai where tenKM = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, tenKM);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				KhuyenMai km = new KhuyenMai(rs.getString("maKM"), rs.getString("tenKM"), rs.getDouble("phanTramGiam"),
						rs.getString("loaiKM"), rs.getDate("ngayBatDau").toLocalDate(),
						rs.getDate("ngayKetThuc").toLocalDate(), rs.getInt("trangThai"));
				return km;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public String getMaKhuyenMaiCuoiCung() {
		// TODO Auto-generated method stub
		String sql = "select top 1 maKM from KhuyenMai order by maKM DESC";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getString("maKM");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}