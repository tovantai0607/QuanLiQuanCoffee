package Entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhieuDatBan {
	private String maPhieuDat;
	private LocalDate ngayDat;
	private LocalTime gioBatDau;
	private LocalTime gioKetThuc;
	private int soNguoi;
	private String ghiChu;
	private int trangThai;

	private KhachHang khachHang;
	private NhanVien nhanVien;

	private List<ChiTietDatBan> dsChiTiet;

	public PhieuDatBan() {
		this.dsChiTiet = new ArrayList<>();
	}

	public PhieuDatBan(String maPhieuDat, LocalDate ngayDat, LocalTime gioBatDau, LocalTime gioKetThuc, int soNguoi,
			String ghiChu, int trangThai, KhachHang khachHang, NhanVien nhanVien) {
		super();
		setMaPhieuDat(maPhieuDat);
		setNgayDat(ngayDat);
		setGioBatDau(gioBatDau);
		setGioKetThuc(gioKetThuc);
		setSoNguoi(soNguoi);
		setGhiChu(ghiChu);
		setTrangThai(trangThai);
		setKhachHang(khachHang);
		setNhanVien(nhanVien);
		this.dsChiTiet = new ArrayList<>();
	}

	// Dùng để hydrate dữ liệu lịch sử từ DB mà không văng validate nghiệp vụ nhập mới.
	public static PhieuDatBan taoTuDuLieuDB(String maPhieuDat, LocalDate ngayDat, LocalTime gioBatDau,
			LocalTime gioKetThuc, int soNguoi, String ghiChu, int trangThai, KhachHang khachHang, NhanVien nhanVien) {
		PhieuDatBan pdb = new PhieuDatBan();
		pdb.maPhieuDat = maPhieuDat;
		pdb.ngayDat = ngayDat;
		pdb.gioBatDau = gioBatDau;
		pdb.gioKetThuc = gioKetThuc;
		pdb.soNguoi = soNguoi;
		pdb.ghiChu = (ghiChu == null) ? "" : ghiChu.trim();
		pdb.trangThai = trangThai;
		pdb.khachHang = khachHang;
		pdb.nhanVien = nhanVien;
		return pdb;
	}

	public List<ChiTietDatBan> getDsChiTiet() {
		return dsChiTiet;
	}

	public void themChiTiet(ChiTietDatBan ct) {
		this.dsChiTiet.add(ct);
	}

	public void xoaChiTiet(ChiTietDatBan ct) {
		this.dsChiTiet.remove(ct);
	}

	public String getMaPhieuDat() {
		return maPhieuDat;
	}

	public void setMaPhieuDat(String maPhieuDat) {
		if (maPhieuDat == null || !maPhieuDat.matches("^PDB\\d{3,}$"))
			throw new IllegalArgumentException("Mã phiếu đặt không hợp lệ (phải có dạng PDBxxx)");
		this.maPhieuDat = maPhieuDat;
	}

	public LocalDate getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDate ngayDat) {
		if (ngayDat == null || ngayDat.isBefore(LocalDate.now()))
			throw new IllegalArgumentException("Ngày đặt phải là hôm nay hoặc một ngày trong tương lai");
		this.ngayDat = ngayDat;
	}

	public LocalTime getGioBatDau() {
		return gioBatDau;
	}

	public void setGioBatDau(LocalTime gioBatDau) {
		if (gioBatDau == null)
			throw new IllegalArgumentException("Giờ bắt đầu không được để trống");
		this.gioBatDau = gioBatDau;
	}

	public LocalTime getGioKetThuc() {
		return gioKetThuc;
	}

	public void setGioKetThuc(LocalTime gioKetThuc) {
		if (gioKetThuc == null)
			throw new IllegalArgumentException("Giờ kết thúc không được để trống");
		this.gioKetThuc = gioKetThuc;
	}

	public int getSoNguoi() {
		return soNguoi;
	}

	public void setSoNguoi(int soNguoi) {
		if (soNguoi <= 0)
			throw new IllegalArgumentException("Số người phải lớn hơn 0");
		this.soNguoi = soNguoi;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = (ghiChu == null) ? "" : ghiChu.trim();
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		if (trangThai < 0 || trangThai > 3) // 0 = đã hủy, 1 = chưa sử dụng, 2 = hoàn thành, 3 là hết hạn
			throw new IllegalArgumentException("Trạng thái là 0,1,2 hoặc 3");
		this.trangThai = trangThai;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		if (nhanVien == null)
			throw new IllegalArgumentException("Nhân viên không được để trống");
		this.nhanVien = nhanVien;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		if (khachHang == null)
			throw new IllegalArgumentException("Khách hàng không được để trống");
		this.khachHang = khachHang;
	}

	public void setDsChiTiet(List<ChiTietDatBan> dsChiTiet) {
		this.dsChiTiet = dsChiTiet;
	}

	public void capNhatThongTin(LocalDate ngayDatMoi, int soNguoiMoi, LocalTime gioBatDauMoi) {
		if (ngayDatMoi != null)
			setNgayDat(ngayDatMoi);
		if (soNguoiMoi > 0)
			setSoNguoi(soNguoiMoi);
		if (gioBatDauMoi != null)
			setGioBatDau(gioBatDauMoi);
	}

	public void huyPhieu() {
		setTrangThai(0); // 0 = đã hủy
	}

	public void nhanBan() {
		setTrangThai(2); // 2 = hoàn thành
	}

	@Override
	public int hashCode() {
		return Objects.hash(maPhieuDat);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhieuDatBan other = (PhieuDatBan) obj;
		return Objects.equals(maPhieuDat, other.maPhieuDat);
	}
}
