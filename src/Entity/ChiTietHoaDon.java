package Entity;

import java.math.BigDecimal;

public class ChiTietHoaDon {

	private HoaDon hoaDon;
	private SanPham sanPham;
	private int soLuong;
	private BigDecimal donGia;

	public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong, BigDecimal donGia) {
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
		this.donGia = donGia;
		setSoLuong(soLuong);
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong <= 0) {
			this.soLuong = 1;
		} else {
			this.soLuong = soLuong;
		}
	}

	public BigDecimal getDonGia() {
		return donGia;
	}

	public void setDonGia(BigDecimal donGia) {
		this.donGia = donGia;
	}

	public BigDecimal tinhThanhTien() {
		if (donGia == null) {
			return BigDecimal.ZERO;
		}
		return donGia.multiply(new BigDecimal(soLuong));
	}
}