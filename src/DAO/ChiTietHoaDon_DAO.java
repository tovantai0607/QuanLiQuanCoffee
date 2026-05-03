package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.ChiTietHoaDon;
import Entity.SanPham;

public class ChiTietHoaDon_DAO {

	private SanPham_DAO sanPham_DAO;

	public ChiTietHoaDon_DAO() {
		sanPham_DAO = new SanPham_DAO();
	}

	public boolean themChiTiet(ChiTietHoaDon ct, Connection con) throws SQLException {
		String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, ct.getHoaDon().getMaHoaDon());
			pstm.setString(2, ct.getSanPham().getMaSanPham());
			pstm.setInt(3, ct.getSoLuong());
			pstm.setBigDecimal(4, ct.getDonGia());

			return pstm.executeUpdate() > 0;
		}
	}

	public List<ChiTietHoaDon> layChiTietTheoMaHoaDon(String maHoaDon) {
		List<ChiTietHoaDon> ds = new ArrayList<>();
		String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";

		Connection con = ConnectDB.getInstance().getConnection();

		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maHoaDon);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				String maSP = rs.getString("maSanPham");
				SanPham sp = sanPham_DAO.timTheoMa(maSP);

				int soLuong = rs.getInt("soLuong");
				java.math.BigDecimal donGia = rs.getBigDecimal("donGia");

				ChiTietHoaDon ct = new ChiTietHoaDon(null, sp, soLuong, donGia);
				ds.add(ct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}
}