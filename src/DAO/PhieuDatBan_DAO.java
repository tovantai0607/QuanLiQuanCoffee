package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.ChiTietDatBan;
import Entity.KhachHang;
import Entity.NhanVien;
import Entity.PhieuDatBan;

public class PhieuDatBan_DAO {
	private Connection con;
	private ChiTietDatBan_DAO ctDatBan_DAO;
	private NhanVien_DAO nv_dao;
	private KhachHang_DAO kh_dao;

	public PhieuDatBan_DAO() {
		con = ConnectDB.getInstance().getConnection();
		ctDatBan_DAO = new ChiTietDatBan_DAO();
		this.nv_dao = new NhanVien_DAO();
		this.kh_dao = new KhachHang_DAO();
	}

	public List<PhieuDatBan> layTatCa() {
		List<PhieuDatBan> ds = new ArrayList<>();
		String sql = "SELECT * FROM PhieuDatBan";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NhanVien nv = nv_dao.timTheoMa(rs.getString("maNhanVien"));
				KhachHang kh = kh_dao.timKhachHangTheoMaKH(rs.getString("maKhachHang"));

				PhieuDatBan pdb = PhieuDatBan.taoTuDuLieuDB(rs.getString("maPhieuDat"),
						rs.getDate("ngayDat").toLocalDate(), rs.getTime("gioBatDau").toLocalTime(),
						rs.getTime("gioKetThuc").toLocalTime(), rs.getInt("soNguoi"), rs.getString("ghiChu"),
						rs.getInt("trangThai"), kh, nv);

				ds.add(pdb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public ArrayList<PhieuDatBan> layPhieuDatBanHopLe() {
		String sql = "select * from PhieuDatBan where ngayDat >= CAST(GETDATE() AS DATE) and trangThai = 1"; // 1 là
																												// chưa
																												// dùng
		ArrayList<PhieuDatBan> dsPB = new ArrayList<PhieuDatBan>();
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NhanVien nv = nv_dao.timTheoMa(rs.getString("maNhanVien"));
				KhachHang kh = kh_dao.timKhachHangTheoMaKH(rs.getString("maKhachHang"));

				PhieuDatBan pdb = PhieuDatBan.taoTuDuLieuDB(rs.getString("maPhieuDat"),
						rs.getDate("ngayDat").toLocalDate(), rs.getTime("gioBatDau").toLocalTime(),
						rs.getTime("gioKetThuc").toLocalTime(), rs.getInt("soNguoi"), rs.getString("ghiChu"),
						rs.getInt("trangThai"), kh, nv);

				dsPB.add(pdb);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dsPB;
	}

	// 3 là hết hạn
	public void capNhatTranThaiTuDong() {
		String sql = "update PhieuDatBan set trangThai = 3 where trangThai = 1  and ((ngayDat < CAST(GETDATE() AS DATE)) or (ngayDat = CAST(GETDATE() AS DATE) and gioKetThuc < CAST(GETDATE() AS TIME)))";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			int soDongUpdated = pstm.executeUpdate();
			if (soDongUpdated != 0) {
				System.out.println("Đã tự động cập nhật " + soDongUpdated + " phiếu");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean themPhieuDatBan(PhieuDatBan pdb) {
		String sqlPDB = "INSERT INTO PhieuDatBan (maPhieuDat, ngayDat, gioBatDau, gioKetThuc, soNguoi, ghiChu, trangThai, maKhachHang, maNhanVien, soDienThoai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			con.setAutoCommit(false); // Bắt đầu Transaction

			// 1. Thêm Phiếu Đặt Bàn
			try (PreparedStatement pstm = con.prepareStatement(sqlPDB)) {
				pstm.setString(1, pdb.getMaPhieuDat());
				pstm.setDate(2, java.sql.Date.valueOf(pdb.getNgayDat()));
				pstm.setTime(3, java.sql.Time.valueOf(pdb.getGioBatDau()));
				pstm.setTime(4, java.sql.Time.valueOf(pdb.getGioKetThuc()));
				pstm.setInt(5, pdb.getSoNguoi());
				pstm.setString(6, pdb.getGhiChu());
				pstm.setInt(7, pdb.getTrangThai());
				pstm.setString(8, pdb.getKhachHang().getMaKhachHang());
				pstm.setString(9, pdb.getNhanVien().getMaNhanVien());
				pstm.setString(10, pdb.getKhachHang().getSoDienThoai());
				pstm.executeUpdate();
			}

			for (ChiTietDatBan ct : pdb.getDsChiTiet()) {
				ct.setPhieuDatBan(pdb);

				if (!ctDatBan_DAO.themChiTiet(ct, con)) {
					con.rollback();
					return false;
				}
			}

			con.commit();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<PhieuDatBan> layPhieuDatBanTheoNgayDat(LocalDate ngayDat) {
		String sql = "select * from PhieuDatBan where ngayDat = ?";
		ArrayList<PhieuDatBan> dsPDB = new ArrayList<PhieuDatBan>();
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setDate(1, java.sql.Date.valueOf(ngayDat));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NhanVien nv = nv_dao.timTheoMa(rs.getString("maNhanVien"));
				KhachHang kh = kh_dao.timKhachHangTheoMaKH(rs.getString("maKhachHang"));

				PhieuDatBan pdb = PhieuDatBan.taoTuDuLieuDB(rs.getString("maPhieuDat"),
						rs.getDate("ngayDat").toLocalDate(), rs.getTime("gioBatDau").toLocalTime(),
						rs.getTime("gioKetThuc").toLocalTime(), rs.getInt("soNguoi"), rs.getString("ghiChu"),
						rs.getInt("trangThai"), kh, nv);
				dsPDB.add(pdb);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dsPDB;
	}

	public ArrayList<PhieuDatBan> layPhieuDatBanTheoSoDienThoai(String soDienThoai) {
		String sql = "select * from PhieuDatBan where soDienThoai = ?";
		ArrayList<PhieuDatBan> dsPDB = new ArrayList<PhieuDatBan>();
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, soDienThoai.trim());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NhanVien nv = nv_dao.timTheoMa(rs.getString("maNhanVien"));
				KhachHang kh = kh_dao.timKhachHangTheoMaKH(rs.getString("maKhachHang"));

				PhieuDatBan pdb = PhieuDatBan.taoTuDuLieuDB(rs.getString("maPhieuDat"),
						rs.getDate("ngayDat").toLocalDate(), rs.getTime("gioBatDau").toLocalTime(),
						rs.getTime("gioKetThuc").toLocalTime(), rs.getInt("soNguoi"), rs.getString("ghiChu"),
						rs.getInt("trangThai"), kh, nv);
				System.out.println(pdb + "\n");
				dsPDB.add(pdb);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dsPDB;
	}

	public PhieuDatBan layPhieuDatBanTheoMaPhieu(String maPhieuDat) {
		String sql = "select * from PhieuDatBan where maPhieuDat = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maPhieuDat.trim());
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				NhanVien nv = nv_dao.timTheoMa(rs.getString("maNhanVien"));
				KhachHang kh = kh_dao.timKhachHangTheoMaKH(rs.getString("maKhachHang"));

				PhieuDatBan pdb = PhieuDatBan.taoTuDuLieuDB(rs.getString("maPhieuDat"),
						rs.getDate("ngayDat").toLocalDate(), rs.getTime("gioBatDau").toLocalTime(),
						rs.getTime("gioKetThuc").toLocalTime(), rs.getInt("soNguoi"), rs.getString("ghiChu"),
						rs.getInt("trangThai"), kh, nv);
				return pdb;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public boolean xoaPhieuDatBan(String maPhieuDat) {
		String sql = "delete from PhieuDatBan where maPhieuDat = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maPhieuDat);
			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public boolean capNhatTrangThaiPhieu(String maPhieu, int trangThai) {
		// TODO Auto-generated method stub
		if (maPhieu == null) {
			return false;
		}
		maPhieu = maPhieu.trim();
		if (maPhieu.isEmpty()) {
			return false;
		}
		if (trangThai < 0 || trangThai > 3) {
			return false;
		}
		String sql = "update PhieuDatBan set trangThai = ? where maPhieuDat = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setInt(1, trangThai);
			pstm.setString(2, maPhieu);
			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	public String taoMaPhieuDatMoi() {
		String sql = "SELECT TOP 1 maPhieuDat FROM PhieuDatBan ORDER BY maPhieuDat DESC";

		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();

			if (rs.next()) {
				String maCuoi = rs.getString("maPhieuDat"); // ví dụ PDB009
				int so = Integer.parseInt(maCuoi.substring(3));
				return String.format("PDB%03d", so + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "PDB001";
	}
}
