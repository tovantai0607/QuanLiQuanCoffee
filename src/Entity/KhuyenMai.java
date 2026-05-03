package Entity;

import java.time.LocalDate;
import java.util.Objects;

public class KhuyenMai {
	private String maKM;
	private String tenKM;
	private double phanTramGiam;
	private String loaiKM;
	private LocalDate ngayBatDau;
	private LocalDate ngayKetThuc;
	private int trangThai;

	public KhuyenMai(String maKM, String tenKM, double phanTramGiam, String loaiKM, LocalDate ngayBatDau,
			LocalDate ngayKetThuc, int trangThai) {
		setMaKM(maKM);
		this.tenKM = tenKM;
		setphanTramGiam(phanTramGiam);
		setNgayBatDau(ngayBatDau);
		setNgayKetThuc(ngayKetThuc);
		this.trangThai = trangThai;
		this.loaiKM = loaiKM;
	}

	public KhuyenMai() {
		// TODO Auto-generated constructor stub
	}

	public double getPhanTramGiam() {
		return phanTramGiam;
	}

	public void setPhanTramGiam(double phanTramGiam) {
		this.phanTramGiam = phanTramGiam;
	}

	public String getloaiKM() {
		return loaiKM;
	}

	public void setloaiKM(String loaiKM) {
		this.loaiKM = loaiKM;
	}

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		if (maKM == null || maKM.isEmpty())
			throw new IllegalArgumentException("Mã khuyến mãi không được bỏ trống");
		if (!maKM.matches("^KM\\d{3}$")) {
			throw new IllegalArgumentException("Mã khuyến mãi phải theo dạng KMxxx");
		}
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public double getphanTramGiam() {
		return phanTramGiam;
	}

	public void setphanTramGiam(double phanTramGiam) {
		if (phanTramGiam <= 0) {
			throw new IllegalArgumentException("Giá trị khuyến mãi phải lớn hơn 0");
		}
		this.phanTramGiam = phanTramGiam;
	}

	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(LocalDate ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public LocalDate getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKM);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKM, other.maKM);
	}

	@Override
	public String toString() {
		return "KhuyenMai [maKM=" + maKM + ", tenKM=" + tenKM + ", phanTramGiam=" + phanTramGiam + ", ngayBatDau="
				+ ngayBatDau + ", ngayKetThuc=" + ngayKetThuc + ", trangThai=" + trangThai + "]";
	}

}
