package Entity;

import java.math.BigDecimal;
import java.util.Objects;

public class SanPham {
	private String maSanPham;
	private String tenSanPham;
	private String donViTinh;
	private BigDecimal gia;
	private String hinhAnh;
	private int trangThai;
	private LoaiSanPham loaiSanPham;

	public SanPham() {
//		super();
	}

	public SanPham(String maSanPham, String tenSanPham, String donViTinh, BigDecimal gia, String hinhAnh, int trangThai,
			LoaiSanPham loaiSanPham) {
		setMaSanPham(maSanPham);
		this.loaiSanPham = loaiSanPham;
		setTenSanPham(tenSanPham);
		this.donViTinh = donViTinh;
		setGia(gia);
		this.hinhAnh = hinhAnh;
		this.trangThai = trangThai;
	}

	public LoaiSanPham getLoaiSP() {
		return loaiSanPham;
	}

	public void setLoaiSP(LoaiSanPham loaiSanPham) {
		this.loaiSanPham = loaiSanPham;
	}

	public String getMaSanPham() {
		return maSanPham;
	}

	public void setMaSanPham(String maSanPham) {
		if (maSanPham == null || maSanPham.isEmpty())
			throw new IllegalArgumentException("Mã sản phẩm không được bỏ trống");
		if (!maSanPham.matches("^SP\\d{3}$")) {
			throw new IllegalArgumentException("Mã sản phẩm phải theo dạng SPxxx");
		}
		this.maSanPham = maSanPham;
	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public void setTenSanPham(String tenSanPham) {
		if (tenSanPham == null || tenSanPham.isEmpty())
			throw new IllegalArgumentException("Tên sản phẩm không được bỏ trống");

		this.tenSanPham = tenSanPham;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public BigDecimal getGia() {
		return gia;
	}

	public void setGia(BigDecimal gia) {
		if (gia.intValue() < 0)
			throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn bằng 0");

		this.gia = gia;
	}

	public String gethinhAnh() {
		return hinhAnh;
	}

	public void sethinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

	public void capNhatGia(BigDecimal newGia) {
		if (newGia.equals(BigDecimal.ZERO)) {
			throw new IllegalArgumentException("Giá mới phải lớn hơn hoặc bằng 0");
		}
		this.gia = newGia;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maSanPham);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SanPham other = (SanPham) obj;
		return Objects.equals(maSanPham, other.maSanPham);
	}

	@Override
	public String toString() {
		return "SanPham [maSanPham=" + maSanPham + ", tenSanPham=" + tenSanPham + ", donViTinh=" + donViTinh + ", gia="
				+ gia + ", hinhAnh=" + hinhAnh + ", trangThai=" + trangThai + "]";
	}

}
