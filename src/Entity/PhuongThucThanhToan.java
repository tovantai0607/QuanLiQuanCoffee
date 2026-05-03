package Entity;

import java.util.Objects;

public class PhuongThucThanhToan {
	private String maPTTT;
	private String tenPTTT;
	private int trangThai;
	private String moTa;

	public PhuongThucThanhToan() {
	}

	public PhuongThucThanhToan(String maPTTT, String tenPTTT, int trangThai, String moTa) {
		super();
		setMaPTTT(maPTTT);
		setTenPTTT(tenPTTT);
		setTrangThai(trangThai);
		setMoTa(moTa);
	}

	public String getMaPTTT() {
		return maPTTT;
	}

	public void setMaPTTT(String maPTTT) {
		if (maPTTT == null || maPTTT.trim().isEmpty())
			throw new IllegalArgumentException("Mã phương thức thanh toán không được để trống!");
		this.maPTTT = maPTTT.trim();
	}

	public String getTenPTTT() {
		return tenPTTT;
	}

	public void setTenPTTT(String tenPTTT) {
		if (tenPTTT == null || tenPTTT.trim().isEmpty())
			throw new IllegalArgumentException("Tên phương thức thanh toán không được để trống!");
		this.tenPTTT = tenPTTT.trim();
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		if (trangThai < 0 || trangThai > 1)
			throw new IllegalArgumentException("Trạng thái chỉ có thể là 0 (không hoạt động) hoặc 1 (đang hoạt động)!");
		this.trangThai = trangThai;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		if (moTa == null)
			moTa = "";
		this.moTa = moTa.trim();
	}

	public void capNhatThongTin(String tenPTTT, String moTa) {
		setTenPTTT(tenPTTT);
		setMoTa(moTa);
	}

	public void thayDoiTrangThai(int trangThai) {
		setTrangThai(trangThai);
	}

	@Override
	public int hashCode() {
		return Objects.hash(maPTTT);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhuongThucThanhToan other = (PhuongThucThanhToan) obj;
		return Objects.equals(maPTTT, other.maPTTT);
	}
}
