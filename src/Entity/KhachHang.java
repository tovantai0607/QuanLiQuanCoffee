package Entity;

import java.time.LocalDate;
import java.util.Objects;

public class KhachHang {
	private String maKhachHang;
	private String hoTen;
	private String soDienThoai;
	private String email;
	private String diaChi;
	private double diemTichLuy;
	private LocalDate ngayDangKy;

	public KhachHang() {
	}

	public KhachHang(String maKhachHang, String hoTen, String soDienThoai, String email, String diaChi,
			double diemTichLuy, LocalDate ngayDangKy) {
		setMaKhachHang(maKhachHang);
		setHoTen(hoTen);
		setSoDienThoai(soDienThoai);
		setEmail(email);
		setDiemTichLuy(diemTichLuy);
		setNgayDangKy(ngayDangKy);
		this.diaChi = diaChi;

	}

	public KhachHang(String hoTen, String soDienThoai, String email, String diaChi, double diemTichLuy,
			LocalDate ngayDangKy) {
		setMaKhachHang(maKhachHang);
		setHoTen(hoTen);
		setSoDienThoai(soDienThoai);
		setEmail(email);
		setDiemTichLuy(diemTichLuy);
		setNgayDangKy(ngayDangKy);
		this.diaChi = diaChi;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(String maKhachHang) {
		if (maKhachHang == null || !maKhachHang.matches("^KH\\d{3}$"))
			throw new IllegalArgumentException("Mã khách hàng không hợp lệ (phải có dạng KHxxx)");
		this.maKhachHang = maKhachHang;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		if (hoTen == null || hoTen.trim().isEmpty())
			throw new IllegalArgumentException("Họ tên không được để trống");
		this.hoTen = hoTen.trim();
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		if (soDienThoai == null || !soDienThoai.matches("^0\\d{9}$"))
			throw new IllegalArgumentException("Số điện thoại không hợp lệ");
		this.soDienThoai = soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			throw new IllegalArgumentException("Email không hợp lệ");
		this.email = email;
	}

	public double getDiemTichLuy() {
		return diemTichLuy;
	}

	public void setDiemTichLuy(double diemTichLuy) {
		if (diemTichLuy < 0)
			throw new IllegalArgumentException("Điểm tích lũy không được âm");
		this.diemTichLuy = diemTichLuy;
	}

	public LocalDate getNgayDangKy() {
		return ngayDangKy;
	}

	public void setNgayDangKy(LocalDate ngayDangKy) {
		if (ngayDangKy == null)
			throw new IllegalArgumentException("Ngày đăng ký không được null");
		this.ngayDangKy = ngayDangKy;
	}

	public void tichDiem(double diem) {
		if (diem <= 0)
			throw new IllegalArgumentException("Điểm cộng phải lớn hơn 0");
		this.diemTichLuy += diem;
	}

	public void suDungDiem(double diem) {
		if (diem <= 0 || diem > this.diemTichLuy)
			throw new IllegalArgumentException("Số điểm sử dụng không hợp lệ");
		this.diemTichLuy -= diem;
	}

	public void capNhatThongTin(String hoTenMoi, String emailMoi, String sdtMoi) {
		if (hoTenMoi != null)
			setHoTen(hoTenMoi);
		if (emailMoi != null)
			setEmail(emailMoi);
		if (sdtMoi != null)
			setSoDienThoai(sdtMoi);
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKhachHang);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKhachHang, other.maKhachHang);
	}
}
