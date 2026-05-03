package Entity;
import java.time.LocalDate;

public class NhanVien {
	private String maNhanVien;
	private String hoTen;
	private String soDienThoai;
	private String email;
	private String chucVu;
	private LocalDate ngayVaoLam;
	private int trangThai;

	public NhanVien(String maNhanVien, String hoTen, String soDienThoai, String email, String chucVu,
			LocalDate ngayVaoLam, int trangThai) {
		super();
		setMaNhanVien(maNhanVien);
		setHoTen(hoTen);
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.chucVu = chucVu;
		this.ngayVaoLam = ngayVaoLam;
		this.trangThai = trangThai;
	}

	public NhanVien() {
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		if (maNhanVien == null || maNhanVien.isEmpty())
			throw new IllegalArgumentException("Mã nhân viên không được rỗng");
		if (!maNhanVien.matches("^NV\\d{3}$")) {
			throw new IllegalArgumentException("Mã nhân viên phải có dạng NVxxx");
		}
		this.maNhanVien = maNhanVien;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		if (hoTen == null || hoTen.isEmpty())
			throw new IllegalArgumentException("Tên nhân viên không được rỗng");
		this.hoTen = hoTen;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public LocalDate getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(LocalDate ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

}