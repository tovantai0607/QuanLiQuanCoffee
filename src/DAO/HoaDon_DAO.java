package DAO;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.Ban;
import Entity.ChiTietHoaDon;
import Entity.HoaDon;
import Entity.KhachHang; // Thêm import
import Entity.KhuyenMai; // Thêm import
import Entity.NhanVien; // Thêm import
import Entity.PhuongThucThanhToan; // Thêm import

public class HoaDon_DAO {
	private Connection con;

	private ChiTietHoaDon_DAO ctHoaDon_DAO;
	private KhachHang_DAO khachHang_DAO;
	private NhanVien_DAO nhanVien_DAO;
	private KhuyenMai_DAO khuyenMai_DAO;
	private PhuongThucThanhToan_DAO pttt_DAO;
	private Ban_DAO ban_dao;
	private boolean hasMaBanColumn;

	public HoaDon_DAO() {
		con = ConnectDB.getInstance().getConnection();

		ctHoaDon_DAO = new ChiTietHoaDon_DAO();
		khachHang_DAO = new KhachHang_DAO();
		nhanVien_DAO = new NhanVien_DAO();
		khuyenMai_DAO = new KhuyenMai_DAO();
		pttt_DAO = new PhuongThucThanhToan_DAO();
		this.ban_dao = new Ban_DAO();
		this.hasMaBanColumn = hasColumn("HoaDon", "maBan");
	}

	private boolean hasColumn(String tableName, String columnName) {
		if (con == null) {
			return false;
		}
		try {
			DatabaseMetaData metaData = con.getMetaData();
			try (ResultSet rs = metaData.getColumns(null, null, tableName, columnName)) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private HoaDon buildHoaDonFromResultSet(ResultSet rs) throws SQLException {
		String maHD = rs.getString("maHoaDon");
		java.sql.Date ngayTaoSql = rs.getDate("ngayTao");
		LocalDate ngayTao = (ngayTaoSql != null) ? ngayTaoSql.toLocalDate() : LocalDate.now();
		String ghiChu = rs.getString("ghiChu");

		int trangThai = (rs.getInt("trangThaiThanhToan"));

		KhachHang kh = khachHang_DAO.timKhachHangTheoMaKH(rs.getString("maKhachHang"));
		NhanVien nv = nhanVien_DAO.timTheoMa(rs.getString("maNhanVien"));
		KhuyenMai km = khuyenMai_DAO.timTheoMaKhuyenMai(rs.getString("maKM"));
		PhuongThucThanhToan pttt = pttt_DAO.layPTTTTheoMa(rs.getString("maPTTT"));
		Ban ban = null;
		if (hasMaBanColumn) {
			String maBan = rs.getString("maBan");
			ban = (maBan != null) ? ban_dao.timTheoMaBan(maBan) : null;
		}
		HoaDon hd = new HoaDon(maHD, ngayTao, ghiChu, trangThai, kh, nv, km, pttt, ban);

		return hd;
	}

	public List<HoaDon> layTatCa() {
		List<HoaDon> ds = new ArrayList<>();
		String sql = "SELECT * FROM HoaDon";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				HoaDon hd = buildHoaDonFromResultSet(rs);
				ds.add(hd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public boolean themHoaDon(HoaDon hd) {
		String sqlHD = hasMaBanColumn
				? "INSERT INTO HoaDon (maHoaDon, ngayTao, ghiChu, trangThaiThanhToan, maKhachHang, maNhanVien, maKM, maPTTT, maBan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
				: "INSERT INTO HoaDon (maHoaDon, ngayTao, ghiChu, trangThaiThanhToan, maKhachHang, maNhanVien, maKM, maPTTT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			con.setAutoCommit(false);

			try (PreparedStatement pstmHD = con.prepareStatement(sqlHD)) {
				pstmHD.setString(1, hd.getMaHoaDon());
				pstmHD.setDate(2, java.sql.Date.valueOf(hd.getNgayTao()));
				pstmHD.setString(3, hd.getGhiChu());

				pstmHD.setInt(4, hd.getTrangThaiThanhToan());

				pstmHD.setString(5, (hd.getKhachHang() != null) ? hd.getKhachHang().getMaKhachHang() : null);
				pstmHD.setString(6, (hd.getNhanVien() != null) ? hd.getNhanVien().getMaNhanVien() : null);
				pstmHD.setString(7, (hd.getKhuyenMai() != null) ? hd.getKhuyenMai().getMaKM() : null);
				pstmHD.setString(8,
						(hd.getPhuongThucThanhToan() != null) ? hd.getPhuongThucThanhToan().getMaPTTT() : null);
				if (hasMaBanColumn) {
					pstmHD.setString(9, (hd.getBan() != null) ? hd.getBan().getMaBan() : null);
				}

				pstmHD.executeUpdate();
			}

			for (ChiTietHoaDon ct : hd.getDsChiTiet()) {

				if (!ctHoaDon_DAO.themChiTiet(ct, con)) {
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

	public HoaDon timHoaDonTheoMa(String maHoaDon) {
		if (maHoaDon == null || maHoaDon.isEmpty())
			return null;
		String sql = "select * from HoaDon where maHoaDon = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maHoaDon.trim());
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					HoaDon hd = buildHoaDonFromResultSet(rs);

					List<ChiTietHoaDon> dsCT = ctHoaDon_DAO.layChiTietTheoMaHoaDon(hd.getMaHoaDon());
					hd.setDsChiTiet(dsCT);

					return hd;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<HoaDon> layHoaDonTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
		String sql = "Select * from HoaDon where ngayTao between ? and ?";
		ArrayList<HoaDon> dsHD = new ArrayList<HoaDon>();
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setDate(1, java.sql.Date.valueOf(tuNgay));
			pstm.setDate(2, java.sql.Date.valueOf(denNgay));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dsHD.add(buildHoaDonFromResultSet(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsHD;
	}

	public List<HoaDon> layTheoTrangThai(int trangThai) {
		List<HoaDon> ds = new ArrayList<>();
		String sql = "SELECT * FROM HoaDon WHERE trangThaiThanhToan = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setInt(1, trangThai);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ds.add(buildHoaDonFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public ArrayList<HoaDon> layHoaDonTheoMaKhachHang(String maKhachHang) {
		String sql = "select * from HoaDon where maKhachHang = ?";
		ArrayList<HoaDon> dsHD = new ArrayList<HoaDon>();
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maKhachHang);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dsHD.add(buildHoaDonFromResultSet(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsHD;
	}

	public String getMaHoaDonCuoiCung() {
		String sql = "select top 1 maHoaDon from HoaDon order by maHoaDon DESC";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getString("maHoaDon");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
