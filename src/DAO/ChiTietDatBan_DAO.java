package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.Ban;
import Entity.ChiTietDatBan;
import Entity.PhieuDatBan;

public class ChiTietDatBan_DAO {
	private Connection con;

	public ChiTietDatBan_DAO() {
		con = ConnectDB.getInstance().getConnection();
	}

	public List<ChiTietDatBan> layChiTietTheoMaPDB(String maPDB) {
		List<ChiTietDatBan> ds = new ArrayList<>();
		String sql = "SELECT * FROM ChiTietDatBan WHERE maPhieuDat = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maPDB);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				PhieuDatBan pdb = new PhieuDatBan();
				pdb.setMaPhieuDat(maPDB);

				Ban ban = new Ban();
				ban.setMaBan(rs.getString("maBan"));

				ChiTietDatBan ct = new ChiTietDatBan(pdb, ban, rs.getString("ghiChu"));
				ds.add(ct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public boolean themChiTiet(ChiTietDatBan ct, Connection transCon) {
		String sql = "INSERT INTO ChiTietDatBan (maPhieuDat, maBan, ghiChu) VALUES (?, ?, ?)";
		try (PreparedStatement pstm = transCon.prepareStatement(sql)) {
			pstm.setString(1, ct.getPhieuDatBan().getMaPhieuDat());
			pstm.setString(2, ct.getBan().getMaBan());
			pstm.setString(3, ct.getGhiChu());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean xoaChiTietDatBan(String maPhieuDat, String maBan) {
		String sql = "delete from ChiTietDatBan where maPhieuDat = ? and maBan = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maPhieuDat);
			pstm.setString(2, maBan);
			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

}