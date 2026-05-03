package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import ConnectDB.ConnectDB;
import Entity.SanPham;
public class ThongKe_DAO {
	private Connection con;
	private ChiTietHoaDon_DAO ct_dao;
	private SanPham_DAO sp_dao;
	private HoaDon_DAO hd_dao;

	public ThongKe_DAO() {
		con = ConnectDB.getInstance().getConnection();
		this.ct_dao = new ChiTietHoaDon_DAO();
		this.sp_dao = new SanPham_DAO();
		this.hd_dao = new HoaDon_DAO();
	}

	public double tongDoanhThu() {
		String sql = "SELECT SUM(cthd.soLuong * cthd.donGia * (1 - ISNULL(km.phanTramGiam, 0))) AS TongDoanhThu "
				+ "FROM HoaDon hd " + "INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon "
				+ "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " + "WHERE YEAR(hd.ngayTao) = YEAR(GETDATE()) "
				+ "AND MONTH(hd.ngayTao) = MONTH(GETDATE()) " + "AND hd.trangThaiThanhToan = 1 ";
		try (PreparedStatement pstm = con.prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {
			if (rs.next()) {
				return rs.getDouble("TongDoanhThu");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0.0;
	}

	public int soLuongSP() {
		String sql = "SELECT SUM(cthd.soLuong) AS TongSoLuongSanPham " + "FROM HoaDon hd "
				+ "INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon "
				+ "WHERE YEAR(hd.ngayTao) = YEAR(GETDATE()) " + "AND MONTH(hd.ngayTao) = MONTH(GETDATE()) "
				+ "AND hd.trangThaiThanhToan = 1 ";
		try (PreparedStatement pstm = con.prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("TongSoLuongSanPham");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public String sanPhamBanChayNhat() {
		String sql = "SELECT TOP 1 sp.tenSanPham " + "FROM HoaDon hd "
				+ "INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon "
				+ "INNER JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham "
				+ "WHERE YEAR(hd.ngayTao) = YEAR(GETDATE()) " + "AND MONTH(hd.ngayTao) = MONTH(GETDATE()) "
				+ "AND hd.trangThaiThanhToan = 1 " + "GROUP BY sp.maSanPham, sp.tenSanPham "
				+ "ORDER BY SUM(cthd.soLuong) DESC";
		try (PreparedStatement pstm = con.prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {
			if (rs.next()) {
				return rs.getString("tenSanPham");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "Không tìm thấy";
	}

	public Map<SanPham, Integer> get10SanPhamBanChayNhat() {
		Map<SanPham, Integer> dsSP = new LinkedHashMap<SanPham, Integer>();
		String sql = "SELECT TOP 10 cthd.maSanPham, SUM(cthd.soLuong) AS TongSoLuong " + "FROM ChiTietHoaDon cthd "
				+ "INNER JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon " + "WHERE "
				+ "    YEAR(hd.ngayTao) = YEAR(GETDATE()) " + "    AND MONTH(hd.ngayTao) = MONTH(GETDATE()) "
				+ "    AND hd.trangThaiThanhToan = 1 " + "GROUP BY " + "    cthd.maSanPham " + "ORDER BY "
				+ "    TongSoLuong DESC";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				SanPham sp = sp_dao.timTheoMa(rs.getString("maSanPham"));
				Integer soLuong = rs.getInt("TongSoLuong");
				dsSP.put(sp, soLuong);
			}
			return dsSP;
		} catch (Exception e) {
			// TODO: handle exception\
			e.printStackTrace();
		}
		return null;
	}

}
