package Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
	private String maHoaDon;
	private LocalDate ngayTao;
	private String ghiChu;
	private int trangThaiThanhToan;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	private KhuyenMai khuyenMai;
	private PhuongThucThanhToan phuongThucThanhToan;
	private List<ChiTietHoaDon> dsChiTiet;
	private BigDecimal tienGiamTuDiem;
	private Ban ban;

	public HoaDon() {
		this.dsChiTiet = new ArrayList<>();
		this.ngayTao = LocalDate.now();
		this.trangThaiThanhToan = 0;
		this.tienGiamTuDiem = BigDecimal.ZERO;
		this.ghiChu = "";
		this.nhanVien = null;
	}

	public HoaDon(String maHoaDon, LocalDate ngayTao, String ghiChu, int trangThaiThanhToan, KhachHang khachHang,
			NhanVien nhanVien, KhuyenMai khuyenMai, PhuongThucThanhToan phuongThucThanhToan, Ban ban) {
		this();
		this.maHoaDon = maHoaDon;
		this.ngayTao = ngayTao;
		this.ghiChu = ghiChu;
		this.trangThaiThanhToan = trangThaiThanhToan;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
		this.khuyenMai = khuyenMai;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.ban = ban;
	}

	public Ban getBan() {
		return ban;
	}

	public void setBan(Ban ban) {
		this.ban = ban;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	public PhuongThucThanhToan getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}

	public void setPhuongThucThanhToan(PhuongThucThanhToan pttt) {
		this.phuongThucThanhToan = pttt;
	}

	public List<ChiTietHoaDon> getDsChiTiet() {
		return dsChiTiet;
	}

	public void setDsChiTiet(List<ChiTietHoaDon> dsChiTiet) {
		this.dsChiTiet = dsChiTiet;
	}

	public int getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}

	// 0 là chưa thanh toán, 1 là đã thanh toán
	public void setTrangThaiThanhToan(int trangThai) {
		this.trangThaiThanhToan = trangThai;
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public LocalDate getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(LocalDate ngayTao) {
		this.ngayTao = ngayTao;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public BigDecimal getTienGiamTuDiem() {
		return tienGiamTuDiem;
	}

	public void setTienGiamTuDiem(BigDecimal tienGiamTuDiem) {
		this.tienGiamTuDiem = tienGiamTuDiem;
	}

	public boolean themChiTiet(SanPham sp, int soLuong) {
		if (sp == null || soLuong <= 0)
			return false;

		for (ChiTietHoaDon ct : dsChiTiet) {
			if (ct.getSanPham().getMaSanPham().equalsIgnoreCase(sp.getMaSanPham())) {
				ct.setSoLuong(ct.getSoLuong() + soLuong);
				return true;
			}
		}

		BigDecimal donGiaLucMua = sp.getGia();
		ChiTietHoaDon ctHD = new ChiTietHoaDon(this, sp, soLuong, donGiaLucMua);
		dsChiTiet.add(ctHD);
		return true;
	}

	public boolean xoaChiTiet(String maSanPham) {
		if (maSanPham == null || maSanPham.isEmpty()) {
			return false;
		}
		return dsChiTiet.removeIf(ct -> ct.getSanPham().getMaSanPham().equalsIgnoreCase(maSanPham));
	}

	public BigDecimal tinhTongTien() {
		BigDecimal tong = BigDecimal.ZERO;
		for (ChiTietHoaDon ct : dsChiTiet) {
			tong = tong.add(ct.tinhThanhTien());
		}
		return tong;
	}

	public BigDecimal tinhTongGiamTuKM() {
		if (this.khuyenMai == null) {
			return BigDecimal.ZERO;
		}
		BigDecimal tongTien = tinhTongTien();
		BigDecimal phanTramGiam = BigDecimal.valueOf(this.khuyenMai.getphanTramGiam());
		return tongTien.multiply(phanTramGiam);
	}

	public BigDecimal tinhTongThanhToan() {
		BigDecimal tongTien = tinhTongTien();
		BigDecimal giamKM = tinhTongGiamTuKM();
		BigDecimal giamDiem = getTienGiamTuDiem();
		BigDecimal thanhTien = tongTien.subtract(giamKM).subtract(giamDiem);

		if (thanhTien.compareTo(BigDecimal.ZERO) < 0) {
			return BigDecimal.ZERO;
		}
		return thanhTien;
	}
}