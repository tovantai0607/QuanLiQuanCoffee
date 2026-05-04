package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import DAO.ChiTietHoaDon_DAO;
import DAO.HoaDon_DAO;
import DAO.KhachHang_DAO;
import DAO.KhuyenMai_DAO;
import DAO.PhieuDatBan_DAO;
import DAO.PhuongThucThanhToan_DAO;
import DAO.SanPham_DAO;
import Entity.ChiTietHoaDon;
import Entity.HoaDon;
import Entity.KhachHang;
import Entity.KhuyenMai;
import Entity.PhieuDatBan;
import Entity.PhuongThucThanhToan;

public class HoaDon_GUI extends JPanel implements ActionListener {

	private KhuyenMai_DAO khuyenmai_dao;
	private PhuongThucThanhToan_DAO pttt_dao;
	private KhachHang_DAO kh_dao;
	private HoaDon_DAO hd_dao;
	private ChiTietHoaDon_DAO ct_dao;
	private SanPham_DAO sp_dao;

	private MainFrame mainFrame;
	private JTextField txtTichDiemSearch;
	private JButton btnTichDiemSearch;
	private JButton btnTaoTaiKhoan;
	private DefaultTableModel tichDiemModel;
	private JTable tichDiemTable;
	private JButton btnDungDiem;

	private DefaultTableModel hoadon_model;
	private JTable hoadon_table;
	private JComboBox<String> cbPTTT, cbKhuyenMai;
	private JLabel lblTongTienValue, lblTienGiamValue, lblThanhTienValue;
	private JButton btnThanhToan;
	private JLabel lblHoaDonText;
	private JLabel lblNgayTaoText;
	private HoaDon hoaDonHienTai;
	private PhieuDatBan_DAO pdb_dao;
	private JLabel lblNhanVienText;
	private JLabel lblMaBanText;
	private JLabel lblMaKhachHangText;
	private JLabel lblGhiChuText;

	private boolean isSearchKhachHang = false;

	public HoaDon_GUI(MainFrame mainFrame) {
		this.khuyenmai_dao = new KhuyenMai_DAO();
		this.pttt_dao = new PhuongThucThanhToan_DAO();
		this.kh_dao = new KhachHang_DAO();
		this.hd_dao = new HoaDon_DAO();
		this.ct_dao = new ChiTietHoaDon_DAO();
		this.sp_dao = new SanPham_DAO();
		this.pdb_dao = new PhieuDatBan_DAO();
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		// pnNorth
		JPanel pnNorth = new JPanel();
		JLabel lblTitle = new JLabel("Tạo Hóa Đơn");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
		pnNorth.add(lblTitle);
		add(pnNorth, BorderLayout.NORTH);

		// pnCenter
		JSplitPane pnCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pnCenter.setResizeWeight(0.4);
		JPanel pnTichDiem = pnTichDiem();
		pnCenter.setLeftComponent(pnTichDiem);
		JPanel pnHoaDon = pnHoaDon();
		pnCenter.setRightComponent(pnHoaDon);

		add(pnCenter, BorderLayout.CENTER);
		addListeners();

		loadDataToComboBoxPTTT();
		loadDataToComboBoxKhuyenMai();

		setTrangThaiMacDinh();
	}

	public void setHoaDonHienTai(HoaDon hoaDon) {
		if (hoaDon == null) {
			this.hoaDonHienTai = new HoaDon();
		} else {
			this.hoaDonHienTai = hoaDon;
		}

		String maHDMoi = taoMaHoaDon();
		this.hoaDonHienTai.setMaHoaDon(maHDMoi);

		lamMoiGiaoDienVoiHoaDon();
	}

	private void lamMoiGiaoDienVoiHoaDon() {
		if (this.hoaDonHienTai == null) {
			setTrangThaiMacDinh();
			return;
		}
		lblNhanVienText.setText("NV001");// mặc địch là NV001 vì hệ thống chưa có lưu tài khoản nhân viên
		lblHoaDonText.setText(this.hoaDonHienTai.getMaHoaDon());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		lblNgayTaoText.setText(dtf.format(LocalDateTime.now()));
		lblGhiChuText.setText(this.hoaDonHienTai.getGhiChu());
		if (mainFrame.getMaKhachHang() != null && !mainFrame.getMaKhachHang().isEmpty()) {
			KhachHang kh = kh_dao.timKhachHangTheoMaKH(mainFrame.getMaKhachHang());
			if (kh != null) {
				searchTichDiem(kh.getSoDienThoai());
			}
			mainFrame.setMaKhachHang("");
		}
		String dsMaBan = "";
		ArrayList<String> ds = mainFrame.getDsMaBan();
		if (ds != null) {
			for (String ma : ds) {
				dsMaBan += ma + ",";
			}
			dsMaBan = (dsMaBan.length() > 0) ? dsMaBan.substring(0, dsMaBan.length() - 1) : dsMaBan;
			lblMaBanText.setText(dsMaBan);
		} else {
			lblMaBanText.setText("");
		}

		hoadon_model.setRowCount(0);

		for (ChiTietHoaDon ct : this.hoaDonHienTai.getDsChiTiet()) {
			hoadon_model.addRow(new Object[] { ct.getSanPham().getTenSanPham(), ct.getSoLuong(),
					ct.getDonGia().doubleValue(), ct.tinhThanhTien().doubleValue() });
		}

		tichDiemModel.setRowCount(0);

		KhachHang kh = this.hoaDonHienTai.getKhachHang();
		lblMaKhachHangText.setText((kh != null) ? kh.getMaKhachHang() : "");
		if (kh != null) {
			tichDiemModel.addRow(new Object[] { kh.getMaKhachHang(), kh.getHoTen(), kh.getDiemTichLuy() });
			txtTichDiemSearch.setText(kh.getSoDienThoai());

			boolean chuaDungDiem = (hoaDonHienTai.getTienGiamTuDiem().compareTo(BigDecimal.ZERO) == 0);
			btnDungDiem.setEnabled(kh.getDiemTichLuy() > 0 && chuaDungDiem);
		} else {
			btnDungDiem.setEnabled(false);
		}

		KhuyenMai km = this.hoaDonHienTai.getKhuyenMai();
		if (km != null) {
			cbKhuyenMai.setSelectedItem(km.getTenKM());
		} else {
			cbKhuyenMai.setSelectedIndex(0);
		}

		PhuongThucThanhToan pttt = this.hoaDonHienTai.getPhuongThucThanhToan();
		if (pttt != null) {
			cbPTTT.setSelectedItem(pttt.getTenPTTT());
		} else {
			cbPTTT.setSelectedIndex(0);
		}

		double tongTien = hoaDonHienTai.tinhTongTien().doubleValue();
		double tienGiamKM = hoaDonHienTai.tinhTongGiamTuKM().doubleValue();
		double tienGiamDiem = hoaDonHienTai.getTienGiamTuDiem().doubleValue();
		double thanhTien = hoaDonHienTai.tinhTongThanhToan().doubleValue();

		lblTongTienValue.setText(String.format("%,.0f VND", tongTien));
		lblTienGiamValue.setText(String.format("%,.0f VND", (tienGiamKM + tienGiamDiem)));
		lblThanhTienValue.setText(String.format("%,.0f VND", thanhTien));

		btnThanhToan.setEnabled(tongTien > 0);
	}

	// pnTichDiem
	private JPanel pnTichDiem() {
		JPanel pnTichDiem = new JPanel(new BorderLayout(10, 10));
		pnTichDiem.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel td_title = new JLabel("Tích điểm khách hàng");
		td_title.setFont(new Font("Arial", Font.BOLD, 16));
		JPanel tichdiem_title = new JPanel(new FlowLayout(FlowLayout.CENTER));
		tichdiem_title.add(td_title);
		pnTichDiem.add(tichdiem_title, BorderLayout.NORTH);

		JPanel tichdiem_content = new JPanel(new BorderLayout(5, 5));
		JPanel tichdiem_Search = new JPanel();
		tichdiem_Search.setLayout(new BoxLayout(tichdiem_Search, BoxLayout.X_AXIS));
		tichdiem_Search.add(new JLabel("Nhập SĐT:"));
		tichdiem_Search.add(txtTichDiemSearch = new JTextField(15));
		tichdiem_Search.add(btnTichDiemSearch = new JButton("Tìm"));
		tichdiem_content.add(tichdiem_Search, BorderLayout.NORTH);

		String[] cols = { "Mã khách hàng", "Tên khách hàng", "Điểm tích lũy" };
		tichDiemModel = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		tichDiemTable = new JTable(tichDiemModel);
		JScrollPane scrollTichDiem = new JScrollPane(tichDiemTable);
		scrollTichDiem.setPreferredSize(new Dimension(300, 200));
		tichdiem_content.add(scrollTichDiem, BorderLayout.CENTER);
		pnTichDiem.add(tichdiem_content, BorderLayout.CENTER);

		JPanel tichdiem_buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		tichdiem_buttons.add(btnTaoTaiKhoan = new JButton("Đăng ký"));
		tichdiem_buttons.add(btnDungDiem = new JButton("Dùng điểm"));
		pnTichDiem.add(tichdiem_buttons, BorderLayout.SOUTH);

		return pnTichDiem;
	}

	// pnHoaDon
	private JPanel pnHoaDon() {
		JPanel pnHoaDon = new JPanel(new BorderLayout(10, 10));
		pnHoaDon.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel hd_title = new JLabel("Hóa Đơn");
		hd_title.setFont(new Font("Arial", Font.BOLD, 16));
		JPanel hoadon_title = new JPanel(new FlowLayout(FlowLayout.CENTER));
		hoadon_title.add(hd_title);
		pnHoaDon.add(hoadon_title, BorderLayout.NORTH);

		JPanel hoadon_content = new JPanel(new BorderLayout(10, 10));
		JPanel hoadon_info = new JPanel(new BorderLayout());

		Box boxInfoLeft = Box.createVerticalBox();
		Box row0 = Box.createHorizontalBox();
		row0.add(new JLabel("Mã hóa đơn: "));
		row0.add(lblHoaDonText = new JLabel(""));
		row0.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxInfoLeft.add(row0);
		boxInfoLeft.add(Box.createVerticalStrut(10));

		Box row1 = Box.createHorizontalBox();
		row1.add(new JLabel("Mã nhân viên: "));
		row1.add(lblNhanVienText = new JLabel("NV001"));
		row1.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxInfoLeft.add(row1);
		boxInfoLeft.add(Box.createVerticalStrut(10));

		Box row2 = Box.createHorizontalBox();
		row2.add(new JLabel("Ngày tạo: "));
		row2.add(lblNgayTaoText = new JLabel(""));
		row2.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxInfoLeft.add(row2);
		boxInfoLeft.add(Box.createVerticalStrut(10));

		Box row3 = Box.createHorizontalBox();
		row3.add(new JLabel("Mã bàn: "));
		row3.add(lblMaBanText = new JLabel(""));
		row3.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxInfoLeft.add(row3);
		boxInfoLeft.add(Box.createVerticalStrut(10));

		Box row4 = Box.createHorizontalBox();
		row4.add(new JLabel("Mã khách hàng: "));
		row4.add(lblMaKhachHangText = new JLabel(""));
		row4.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxInfoLeft.add(row4);
		boxInfoLeft.add(Box.createVerticalStrut(10));

		Box row5 = Box.createHorizontalBox();
		row5.add(new JLabel("Ghi chú: "));
		row5.add(lblGhiChuText = new JLabel(""));
		row5.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxInfoLeft.add(row5);
		boxInfoLeft.add(Box.createVerticalStrut(10));

		hoadon_info.add(boxInfoLeft, BorderLayout.WEST);

		JPanel pnKhuyenMai = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnKhuyenMai.add(new JLabel("Khuyến mãi:"));
		cbKhuyenMai = new JComboBox<String>();
		pnKhuyenMai.add(cbKhuyenMai);
		hoadon_info.add(pnKhuyenMai, BorderLayout.EAST);
		hoadon_content.add(hoadon_info, BorderLayout.NORTH);

		String[] hoadon_cols = { "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" };
		hoadon_model = new DefaultTableModel(hoadon_cols, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		hoadon_table = new JTable(hoadon_model);
		hoadon_content.add(new JScrollPane(hoadon_table), BorderLayout.CENTER);
		pnHoaDon.add(hoadon_content, BorderLayout.CENTER);

		JPanel hoaDon_south = new JPanel(new BorderLayout(10, 5));
		hoaDon_south.setBorder(BorderFactory.createTitledBorder("Thanh toán"));

		JPanel pnPTTT = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnPTTT.add(new JLabel("Chọn PTTT:"));
		cbPTTT = new JComboBox<String>();
		cbPTTT.setPreferredSize(new Dimension(150, 25));
		pnPTTT.add(cbPTTT);
		hoaDon_south.add(pnPTTT, BorderLayout.WEST);

		Box boxTongTien = Box.createVerticalBox();
		boxTongTien.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Font fontThuong = new Font("Arial", Font.PLAIN, 14);
		Font fontDam = new Font("Arial", Font.BOLD, 16);

		Box rowTong = Box.createHorizontalBox();
		rowTong.add(new JLabel("Tổng tiền:"));
		rowTong.add(Box.createHorizontalGlue());
		lblTongTienValue = new JLabel("0 VND");
		lblTongTienValue.setFont(fontThuong);
		rowTong.add(lblTongTienValue);
		boxTongTien.add(rowTong);
		boxTongTien.add(Box.createVerticalStrut(5));

		Box rowGiam = Box.createHorizontalBox();
		rowGiam.add(new JLabel("Giảm giá (KM + Điểm):"));
		rowGiam.add(Box.createHorizontalGlue());
		lblTienGiamValue = new JLabel("0 VND");
		lblTienGiamValue.setFont(fontThuong);
		lblTienGiamValue.setForeground(Color.BLUE);
		rowGiam.add(lblTienGiamValue);
		boxTongTien.add(rowGiam);
		boxTongTien.add(Box.createVerticalStrut(5));

		Box rowThanhTien = Box.createHorizontalBox();
		rowThanhTien.add(new JLabel("THÀNH TIỀN:"));
		rowThanhTien.add(Box.createHorizontalGlue());
		lblThanhTienValue = new JLabel("0 VND");
		lblThanhTienValue.setFont(fontDam);
		lblThanhTienValue.setForeground(Color.RED);
		rowThanhTien.add(lblThanhTienValue);
		boxTongTien.add(rowThanhTien);

		hoaDon_south.add(boxTongTien, BorderLayout.CENTER);

		JPanel pnThanhToanBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnThanhToan = new JButton("Thanh Toán");
		btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
		btnThanhToan.setPreferredSize(new Dimension(120, 40));
		pnThanhToanBtn.add(btnThanhToan);
		hoaDon_south.add(pnThanhToanBtn, BorderLayout.EAST);

		pnHoaDon.add(hoaDon_south, BorderLayout.SOUTH);

		return pnHoaDon;
	}

	private void loadDataToComboBoxPTTT() {
		ArrayList<PhuongThucThanhToan> dsPTTT = (ArrayList<PhuongThucThanhToan>) pttt_dao.layTatCa();
		DefaultComboBoxModel<String> ptttModel = new DefaultComboBoxModel<>();
		for (PhuongThucThanhToan pttt : dsPTTT) {
			ptttModel.addElement(pttt.getTenPTTT());
		}
		cbPTTT.setModel(ptttModel);
	}

	private void loadDataToComboBoxKhuyenMai() {
		ArrayList<KhuyenMai> dsKM = (ArrayList<KhuyenMai>) khuyenmai_dao.layTatCa();
		DefaultComboBoxModel<String> kmModel = new DefaultComboBoxModel<>();
		kmModel.addElement("Chọn khuyến mãi");
		for (KhuyenMai km : dsKM) {
			kmModel.addElement(km.getTenKM());
		}
		cbKhuyenMai.setModel(kmModel);
	}

	public void setTrangThaiMacDinh() {
		this.hoaDonHienTai = new HoaDon();
		this.hoaDonHienTai.setMaHoaDon(taoMaHoaDon());
		this.hoaDonHienTai.setNgayTao(LocalDate.now());
		this.isSearchKhachHang = false;
		lamMoiGiaoDienVoiHoaDon();
		txtTichDiemSearch.setText("");
	}

	private void clearUI() {
		for (JLabel lbl : new JLabel[] { lblGhiChuText, lblHoaDonText, lblMaBanText, lblMaKhachHangText, lblNgayTaoText,
				lblNhanVienText, lblThanhTienValue, lblTienGiamValue, lblTongTienValue }) {
			lbl.setText("");
		}
		if (cbKhuyenMai.getItemCount() > 0) {
			cbKhuyenMai.setSelectedIndex(0);
		}
		if (cbPTTT.getItemCount() > 0) {
			cbPTTT.setSelectedIndex(0);
		}
		tichDiemModel.setRowCount(0);
	}

	private void addListeners() {
		btnTichDiemSearch.addActionListener(this);
		btnDungDiem.addActionListener(this);
		btnThanhToan.addActionListener(this);
		btnTaoTaiKhoan.addActionListener(this);
		cbKhuyenMai.addActionListener(this);
		cbPTTT.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTichDiemSearch)) {
			String txtSearch = txtTichDiemSearch.getText().trim();
			this.hoaDonHienTai.setTienGiamTuDiem(BigDecimal.ZERO);
			searchTichDiem(txtSearch);
			lamMoiGiaoDienVoiHoaDon();
		} else if (o.equals(btnThanhToan)) {
			xuLyThanhToan();

		} else if (o == btnDungDiem)

		{
			KhachHang kh = hoaDonHienTai.getKhachHang();
			if (kh == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng trước khi dùng điểm");
				return;
			}
			double diemHienCo = kh.getDiemTichLuy();
			if (diemHienCo <= 0) {
				JOptionPane.showMessageDialog(this, "Khách hàng không có điểm để sử dụng.");
				return;
			}
			double diemQuyDoiThanhTien = diemHienCo;

			double tienSauKhiGiamKM = hoaDonHienTai.tinhTongTien().subtract(hoaDonHienTai.tinhTongGiamTuKM())
					.doubleValue();

			if (tienSauKhiGiamKM <= 0) {
				JOptionPane.showMessageDialog(this, "Hóa đơn đã được giảm tối đa, không thể dùng điểm.");
				return;
			}

			double tienGiamTuDiemToiDa = Math.min(diemQuyDoiThanhTien, tienSauKhiGiamKM);
			hoaDonHienTai.setTienGiamTuDiem(BigDecimal.valueOf(tienGiamTuDiemToiDa));
			JOptionPane.showMessageDialog(this,
					String.format("Đã áp dụng điểm tích lũy vào hóa đơn.", tienGiamTuDiemToiDa, tienGiamTuDiemToiDa));

			lamMoiGiaoDienVoiHoaDon();
		} else if (o == btnTaoTaiKhoan) {
			mainFrame.switchToPanel(mainFrame.KEY_KHACH_HANG);

		} else if (o.equals(cbKhuyenMai)) {
			String selected = cbKhuyenMai.getSelectedItem().toString().trim();

			if (cbKhuyenMai.getSelectedIndex() == 0) {
				this.hoaDonHienTai.setKhuyenMai(null);
			} else {
				KhuyenMai km = khuyenmai_dao.timKhuyenMaiTheoTen(selected);
				this.hoaDonHienTai.setKhuyenMai(km);
			}
			lamMoiGiaoDienVoiHoaDon();

		} else if (o.equals(cbPTTT)) {
			String selected = cbPTTT.getSelectedItem().toString().trim();
			PhuongThucThanhToan pttt = pttt_dao.layPTTTTheoTen(selected);
			this.hoaDonHienTai.setPhuongThucThanhToan(pttt);
		}
	}

	private void searchTichDiem(String txtSearch) {
		// TODO Auto-generated method stub
		if (txtSearch.isEmpty()) {
			this.hoaDonHienTai.setKhachHang(null);
			lblMaKhachHangText.setText("");
			isSearchKhachHang = false;
		} else {
			KhachHang kh = kh_dao.timKhachHangTheoSDT(txtSearch);
			if (kh == null) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng. Tìm lại hoặc Đăng ký");
				this.hoaDonHienTai.setKhachHang(null);
				lblMaKhachHangText.setText("");
				isSearchKhachHang = false;
				return;
			} else {
				this.hoaDonHienTai.setKhachHang(kh);
				lblMaKhachHangText.setText(hoaDonHienTai.getKhachHang().getMaKhachHang());
				isSearchKhachHang = true;
			}
		}

	}

	public String taoMaHoaDon() {
		String maHoaDonCuoi = hd_dao.getMaHoaDonCuoiCung();
		if (maHoaDonCuoi == null) {
			return "HD001";
		}
		try {
			String phanSo = maHoaDonCuoi.substring(2);
			int soHienTai = Integer.parseInt(phanSo);
			soHienTai++;
			String phanSoMoi = String.format("%03d", soHienTai);
			return "HD" + phanSoMoi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "HD_ERROR";
	}

	private void xuLyThanhToan() {
		if (hoaDonHienTai.getDsChiTiet().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Hóa đơn rỗng, không thể thanh toán");
			return;
		}
		if (hoaDonHienTai.getPhuongThucThanhToan() == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn Phương thức thanh toán");
			return;
		}
		
		hoaDonHienTai.setTrangThaiThanhToan(1);

		if (hd_dao.themHoaDon(hoaDonHienTai)) {

			KhachHang kh = hoaDonHienTai.getKhachHang();
			String message = "Thanh toán thành công!";

			if (kh != null) {
				double diemDaSuDung = hoaDonHienTai.getTienGiamTuDiem().doubleValue();
				double diemHienCo = kh.getDiemTichLuy();
				double diemMoi;

				if (diemDaSuDung > 0) {
					diemMoi = diemHienCo - diemDaSuDung;
					kh_dao.capNhatDiemTichLuy(kh.getMaKhachHang(), diemMoi);
					message = "Thanh toán thành công!\nĐã cập nhật (trừ) điểm của khách hàng.";

				} else {
					double thanhTien = hoaDonHienTai.tinhTongThanhToan().doubleValue();
					double diemCong = Math.floor(thanhTien / 10);

					if (diemCong > 0) {
						diemMoi = diemHienCo + diemCong;
						kh_dao.capNhatDiemTichLuy(kh.getMaKhachHang(), diemMoi);
						message = String.format("Thanh toán thành công!\nĐã cộng %,.0f điểm cho khách hàng.", diemCong);
					} else {
						message = "Thanh toán thành công!";
					}
				}
			}

			JOptionPane.showMessageDialog(this, message);

			xuatHoaDonThanhFileTxt(hoaDonHienTai);

			setTrangThaiMacDinh();
			mainFrame.setTrangThaiHoaDon(false); // ẩn nav hóa đơn khi thanh toán xong
			// cập nhật trạng thái cho PDB
			String maPDB = mainFrame.getMaPhieuDatBan();
			if (maPDB != null && !maPDB.isEmpty()) {
				PhieuDatBan pdb = pdb_dao.layPhieuDatBanTheoMaPhieu(maPDB);
				if (pdb != null) {
					pdb_dao.capNhatTrangThaiPhieu(pdb.getMaPhieuDat(), 2); // 2 (đã sử dụng)
				}
			}
			mainFrame.setMaPhieuDatBan("");
			mainFrame.setDsMaBan(new ArrayList<String>());
			mainFrame.setMaKhachHang("");
		} else {
			JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi lưu. Giao dịch đã được hủy. Vui lòng thử lại.");
			hoaDonHienTai.setTrangThaiThanhToan(0);
		}
	}

	public void xuatHoaDonThanhFileTxt(HoaDon hoaDon) {
		String projectPath = System.getProperty("user.dir");
		String folderPath = projectPath + File.separator + "file_hoadon";

		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String filePath = folderPath + File.separator + hoaDon.getMaHoaDon() + ".txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {

			writer.write("**************************************************\n");
			writer.write("                 QUÁN COOFFE LMK\n");
			writer.write("      12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp, TPHCM\n");
			writer.write("**************************************************\n\n");

			writer.write("             HÓA ĐƠN BÁN HÀNG\n\n");
			writer.write("Mã HĐ:       " + hoaDon.getMaHoaDon() + "\n");
			writer.write("Mã NV:       " + "NV001" + "\n");

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			writer.write("Ngày tạo:    " + dtf.format(LocalDateTime.now()));
			KhachHang kh = hoaDon.getKhachHang();
			String maKH = (kh != null) ? kh.getMaKhachHang() : "";
			writer.write("\nMã KH:       " + maKH + "\n");
			if (kh != null) {
				writer.write("\nTên khách hàng:  " + kh.getHoTen() + "\n");
			} else {
				writer.write("\nTên khách hàng:  Khách vãng lai\n");
			}
			writer.write("Mã bàn:       " + lblMaBanText.getText() + "\n");
			writer.write("--------------------------------------------------\n");
			writer.write(String.format("%-25s %3s %10s %10s\n", "Tên Sản Phẩm", "SL", "Đơn Giá", "Thành Tiền"));
			writer.write("--------------------------------------------------\n");

			for (ChiTietHoaDon ct : hoaDon.getDsChiTiet()) {
				String tenSP = ct.getSanPham().getTenSanPham();
				if (tenSP.length() > 25) {
					tenSP = tenSP.substring(0, 22) + "...";
				}
				int sl = ct.getSoLuong();
				double donGia = ct.getDonGia().doubleValue();
				double thanhTien = ct.tinhThanhTien().doubleValue();
				writer.write(String.format("%-25s %3d %10.0f %10.0f\n", tenSP, sl, donGia, thanhTien));
			}
			writer.write("--------------------------------------------------\n");

			double tongTien = hoaDon.tinhTongTien().doubleValue();
			double giamGia = hoaDon.tinhTongGiamTuKM().add(hoaDon.getTienGiamTuDiem()).doubleValue();
			double thanhTien = hoaDon.tinhTongThanhToan().doubleValue();

			writer.write(String.format("%-38s %12.0f VND\n", "Tổng tiền:", tongTien));
			writer.write(String.format("%-38s %12.0f VND\n", "Giảm giá:", giamGia));
			writer.write(String.format("%-38s %12.0f VND\n", "Thành tiền:", thanhTien));

			writer.write(String.format("%-38s %12s\n", "Phương thức thanh toán:",
					hoaDon.getPhuongThucThanhToan().getTenPTTT()));
			writer.write(String.format("%-38s %12s\n", "Ghi chú:", hoaDon.getGhiChu()));

			writer.write("\n\n             Cảm ơn quý khách!\n");

			JOptionPane.showMessageDialog(this, "Xuất hóa đơn TXT thành công!\nĐã lưu tại: " + filePath);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi xuất file TXT: " + e.getMessage());
		}
	}
}
