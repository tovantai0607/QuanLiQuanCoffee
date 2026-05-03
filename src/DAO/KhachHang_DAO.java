package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.KhachHang;

public class KhachHang_DAO {
	private Connection con;

	public KhachHang_DAO() {
		con = ConnectDB.getInstance().getConnection();
	}

	public List<KhachHang> layTatCa() {
		List<KhachHang> ds = new ArrayList<>();
		try {
			String sql = "SELECT * FROM KhachHang";
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);

			while (rs.next()) {
				KhachHang kh = new KhachHang(rs.getString("maKhachHang"), rs.getString("hoTen"),
						rs.getString("soDienThoai"), rs.getString("email"), rs.getString("diaChi"),
						rs.getDouble("diemTichLuy"), rs.getDate("ngayDangKy").toLocalDate());
				ds.add(kh);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	public boolean themKhachHang(KhachHang kh) {
		String sql = "INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, email, diaChi, diemTichLuy, ngayDangKy) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, kh.getMaKhachHang());
			pstm.setString(2, kh.getHoTen());
			pstm.setString(3, kh.getSoDienThoai());
			pstm.setString(4, kh.getEmail());
			pstm.setString(5, kh.getDiaChi());
			pstm.setDouble(6, kh.getDiemTichLuy());
			pstm.setDate(7, java.sql.Date.valueOf(kh.getNgayDangKy()));
			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean kiemTraEmailTonTai(String email) {
		String sql = "select count(*) from KhachHang where email = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, email.trim());
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;
	}

	public boolean kiemTraSoDienThoaiTonTai(String soDienThoai) {
		String sql = "select count(*) from KhachHang where soDienThoai = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, soDienThoai.trim());
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;
	}

	public boolean xoaKhachHang(String maKH) {
		String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maKH.trim());
			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean capNhatKhachHang(KhachHang kh) {
		String sql = "UPDATE KhachHang SET hoTen = ?, soDienThoai = ?, email = ?, diaChi = ?, diemTichLuy = ? WHERE maKhachHang = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, kh.getHoTen());
			pstm.setString(2, kh.getSoDienThoai());
			pstm.setString(3, kh.getEmail());
			pstm.setString(4, kh.getDiaChi());
			pstm.setDouble(5, kh.getDiemTichLuy());
			pstm.setString(6, kh.getMaKhachHang());

			return pstm.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public KhachHang timKhachHangTheoMaKH(String maKH) {
		if (maKH == null || maKH.trim().isEmpty()) {
			return null;
		}
		String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, maKH.trim());
			ResultSet rs = pstm.executeQuery();

			if (rs.next()) {
				return new KhachHang(rs.getString("maKhachHang"), rs.getString("hoTen"), rs.getString("soDienThoai"),
						rs.getString("email"), rs.getString("diaChi"), rs.getDouble("diemTichLuy"),
						rs.getDate("ngayDangKy").toLocalDate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public KhachHang timKhachHangTheoSDT(String soDienThoai) {
		String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, soDienThoai.trim());
			ResultSet rs = pstm.executeQuery();

			if (rs.next()) {
				return new KhachHang(rs.getString("maKhachHang"), rs.getString("hoTen"), rs.getString("soDienThoai"),
						rs.getString("email"), rs.getString("diaChi"), rs.getDouble("diemTichLuy"),
						rs.getDate("ngayDangKy").toLocalDate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// lưu khách hàng
	public boolean luuTatCa(ArrayList<KhachHang> ds) {
		String sqlXoa = "DELETE FROM KhachHang";
		String sqlThem = "INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, email, diaChi, diemTichLuy, ngayDangKy) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			con.setAutoCommit(false);

			try (PreparedStatement pstmXoa = con.prepareStatement(sqlXoa)) {
				pstmXoa.executeUpdate();
			}

			try (PreparedStatement pstmThem = con.prepareStatement(sqlThem)) {
				for (KhachHang kh : ds) {
					pstmThem.setString(1, kh.getMaKhachHang());
					pstmThem.setString(2, kh.getHoTen());
					pstmThem.setString(3, kh.getSoDienThoai());
					pstmThem.setString(4, kh.getEmail());
					pstmThem.setString(5, kh.getDiaChi());
					pstmThem.setDouble(6, kh.getDiemTichLuy());
					pstmThem.setDate(7, java.sql.Date.valueOf(kh.getNgayDangKy()));
					pstmThem.addBatch();
				}
				pstmThem.executeBatch();
			}

			con.commit();
			con.setAutoCommit(true);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
	}

	public int getDiemTichLuyTheoMa(String soDienThoai) {
		String sql = "select diemTichLuy from KhachHang where soDienThoai = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setString(1, soDienThoai);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getInt("diemTichLuy");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public boolean capNhatDiemTichLuy(String maKhachHang, double diemTichLuyMoi) {
		String sql = "update KhachHang set diemTichLuy = ?  where maKhachHang = ?";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			pstm.setDouble(1, diemTichLuyMoi);
			pstm.setString(2, maKhachHang);
			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public String getMaKhachHangCuoiCung() {
		// TODO Auto-generated method stub
		String sql = "select top 1 maKhachHang from KhachHang order by maKhachHang DESC";
		try (PreparedStatement pstm = con.prepareStatement(sql)) {
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getString("maKhachHang");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
