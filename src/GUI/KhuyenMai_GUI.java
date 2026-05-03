package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DAO.KhuyenMai_DAO;
import Entity.KhuyenMai;

public class KhuyenMai_GUI extends JPanel implements ActionListener, ComponentListener {
	private MainFrame mainFrame;
	private KhuyenMai_DAO khuyenMaiDAO;
	private JLabel title;
	private JLabel lblMaKM;
	private JTextField txtMaKM;
	private JLabel lblTenKM;
	private JTextField txtTenKM;
	private JLabel lblPhanTram;
	private JTextField txtPhanTram;
	private JLabel lblLoaiKM;
	private JComboBox cboLoaiKM;
	private JLabel lblNgayBD;
	private JSpinner spNgayBD;
	private JLabel lblNgayKT;
	private JSpinner spNgayKT;
	private JLabel lblTrangThai;
	private JComboBox cboTrangThai;
	private JButton btnThem;
	private JButton btnClear;
	private JButton btnCapNhat;
	private DefaultTableModel model;
	private JTable tblKM;
	private JButton btnXoa;
	private JButton btnTrangChu;
	private JLabel lblSearch;
	private JTextField txtSearch;
	private JButton btnSearch;

	public KhuyenMai_GUI(MainFrame mainFrame) {
		// TODO Auto-generated constructor stub
		this.mainFrame = mainFrame;
		this.khuyenMaiDAO = new KhuyenMai_DAO();
		setLayout(new BorderLayout());

		JPanel pnNorth = new JPanel();
		title = new JLabel("Quản lý khuyến mãi");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		pnNorth.add(title);
		add(pnNorth, BorderLayout.NORTH);

		JPanel pnCenter = new JPanel(new BorderLayout(10, 10));
		add(pnCenter, BorderLayout.CENTER);

		JPanel pnInput = new JPanel(new GridLayout(7, 2, 10, 10));
		pnInput.setBorder(BorderFactory.createTitledBorder("Thông tin khuyến mãi"));

		pnInput.add(lblMaKM = new JLabel("Mã khuyến mãi:"));
		txtMaKM = new JTextField(20);
		txtMaKM.setEditable(false);
		pnInput.add(txtMaKM);

		pnInput.add(lblTenKM = new JLabel("Tên khuyến mãi:"));
		txtTenKM = new JTextField(20);
		pnInput.add(txtTenKM);

		pnInput.add(lblPhanTram = new JLabel("Phần trăm giảm (%):"));
		txtPhanTram = new JTextField(20);
		pnInput.add(txtPhanTram);

		pnInput.add(lblLoaiKM = new JLabel("Loại khuyến mãi:"));
		cboLoaiKM = new JComboBox<>(new String[] { "Loại khuyến mãi", "Theo sản phẩm", "Theo hóa đơn" });
		pnInput.add(cboLoaiKM);

		pnInput.add(lblNgayBD = new JLabel("Ngày bắt đầu:"));
		spNgayBD = new JSpinner(new SpinnerDateModel());
		spNgayBD.setEditor(new JSpinner.DateEditor(spNgayBD, "yyyy-MM-dd"));
		pnInput.add(spNgayBD);

		pnInput.add(lblNgayKT = new JLabel("Ngày kết thúc:"));
		spNgayKT = new JSpinner(new SpinnerDateModel());
		spNgayKT.setEditor(new JSpinner.DateEditor(spNgayKT, "yyyy-MM-dd"));
		pnInput.add(spNgayKT);

		pnInput.add(lblTrangThai = new JLabel("Trạng thái:"));
		cboTrangThai = new JComboBox<>(new String[] { "Chọn trạng thái", "Còn hiệu lực", "Hết hiệu lực" });
		pnInput.add(cboTrangThai);

		JPanel pnFormButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		btnThem = new JButton("Thêm");
		btnClear = new JButton("Làm mới");
		btnCapNhat = new JButton("Cập nhật");
		Dimension btnSize = new Dimension(100, 30);
		btnThem.setPreferredSize(btnSize);
		btnClear.setPreferredSize(btnSize);
		btnCapNhat.setPreferredSize(btnSize);
		pnFormButtons.add(btnThem);
		pnFormButtons.add(btnClear);
		pnFormButtons.add(btnCapNhat);
		pnFormButtons.add(Box.createHorizontalStrut(20));

		JPanel pnSearch = new JPanel();
		pnSearch.add(lblSearch = new JLabel("Nhập mã khuyến mãi: "));
		pnSearch.add(txtSearch = new JTextField(10));
		pnSearch.add(btnSearch = new JButton("Tìm kiếm"));
		pnFormButtons.add(pnSearch);
		JPanel pnTop = new JPanel(new BorderLayout());
		pnTop.add(pnInput, BorderLayout.CENTER);
		pnTop.add(pnFormButtons, BorderLayout.SOUTH);

		pnCenter.add(pnTop, BorderLayout.NORTH);

		String[] columnNames = { "Mã KM", "Tên KM", "% Giảm", "Loại KM", "Ngày bắt đầu", "Ngày kết thúc",
				"Trạng thái" };
		model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		tblKM = new JTable(model);
		JScrollPane sp = new JScrollPane(tblKM);
		sp.setBorder(BorderFactory.createTitledBorder("Danh sách khuyến mãi"));
		pnCenter.add(sp, BorderLayout.CENTER);

		hienThiDanhSach();

		JPanel pnSouth = new JPanel(new BorderLayout());
		btnTrangChu = new JButton("Trang chủ");
		btnXoa = new JButton("Xóa");
		btnXoa.setPreferredSize(btnSize);
		btnTrangChu.setPreferredSize(btnSize);
		pnSouth.add(btnTrangChu, BorderLayout.WEST);
		pnSouth.add(btnXoa, BorderLayout.EAST);
		add(pnSouth, BorderLayout.SOUTH);

		btnThem.addActionListener(this);
		btnClear.addActionListener(this);
		btnCapNhat.addActionListener(this);
		btnXoa.addActionListener(this);
		btnTrangChu.addActionListener(this);
		btnSearch.addActionListener(this);
		addComponentListener(this);
		tblKM.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting())
				hienThiChiTietKhuyenMai();
		});

		refreshMa();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnThem)) {
			themKhuyenMai();
		} else if (o.equals(btnXoa)) {
			xoaKhuyenMai();
		} else if (o.equals(btnClear)) {
			xuLyLamMoi();
		} else if (o.equals(btnCapNhat)) {
			capNhatKhuyenMai();
		} else if (o.equals(btnTrangChu)) {
			mainFrame.switchToPanel(mainFrame.KEY_DAT_BAN);
		} else if (o.equals(btnSearch)) {
			actionSearch();
		}
	}

	private void actionSearch() {
		String strSearch = txtSearch.getText().trim();
		if (strSearch.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa mã khuyến mãi");
			return;
		}

		@SuppressWarnings("unchecked")
		TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tblKM.getRowSorter();

		if (sorter == null) {
			sorter = new TableRowSorter<>(model);
			tblKM.setRowSorter(sorter);
		}
		RowFilter<TableModel, Integer> searchFilter = new RowFilter<TableModel, Integer>() {
			@Override
			public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
				String tenSP = entry.getStringValue(1);
				return tenSP.toLowerCase().contains(strSearch.toLowerCase());
			}
		};

		sorter.setRowFilter(searchFilter);

		if (tblKM.getRowCount() > 0) {
			int viewIndex = 0;
			tblKM.setRowSelectionInterval(viewIndex, viewIndex);
			tblKM.scrollRectToVisible(tblKM.getCellRect(viewIndex, 0, true));
		} else {
			JOptionPane.showMessageDialog(this, "Không tìm thấy khuyến mãi có mã '" + strSearch + "'", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);

			sorter.setRowFilter(null);
			txtSearch.setText("");
			tblKM.clearSelection();
		}
	}

	private void themKhuyenMai() {
		String maKM = txtMaKM.getText().trim();
		String tenKM = txtTenKM.getText().trim();
		String phanTramStr = txtPhanTram.getText().trim();
		String loaiKM = (String) cboLoaiKM.getSelectedItem();
		String trangThaiStr = (String) cboTrangThai.getSelectedItem();

		if (maKM.isEmpty() || tenKM.isEmpty() || phanTramStr.isEmpty() || cboLoaiKM.getSelectedIndex() == 0
				|| cboTrangThai.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (khuyenMaiDAO.timTheoMaKhuyenMai(maKM) != null) {
			JOptionPane.showMessageDialog(this, "Mã khuyến mãi đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		double phanTramGiam;
		try {
			phanTramGiam = Double.parseDouble(phanTramStr);
			if (phanTramGiam < 0 || phanTramGiam > 100) {
				JOptionPane.showMessageDialog(this, "Phần trăm giảm phải nằm trong khoảng 0 - 100!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Phần trăm giảm phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		java.util.Date utilDateBD = (java.util.Date) spNgayBD.getValue();
		java.util.Date utilDateKT = (java.util.Date) spNgayKT.getValue();

		LocalDate ngayBD = utilDateBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate ngayKT = utilDateKT.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (ngayKT.isBefore(ngayBD)) {
			JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int trangThai = trangThaiStr.equalsIgnoreCase("Còn hiệu lực") ? 1 : 0;

		KhuyenMai km = new KhuyenMai(maKM, tenKM, phanTramGiam, loaiKM, ngayBD, ngayKT, trangThai);

		if (khuyenMaiDAO.themKhuyenMai(km)) {
			JOptionPane.showMessageDialog(this, "Đã thêm khuyến mãi mới!");
			hienThiDanhSach();
			xuLyLamMoi();
			refreshMa();
		} else {
			JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thất bại!");
		}
	}

	private void refreshMa() {
		txtMaKM.setText(taoMaKhuyenMai());
	}

	private void xoaKhuyenMai() {
		int row = tblKM.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi này không?", "Xác nhận xóa",
				JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		String maKM = tblKM.getValueAt(row, 0).toString();
		if (khuyenMaiDAO.xoaKhuyenMai(maKM)) {
			JOptionPane.showMessageDialog(this, "Đã xóa khuyến mãi!");
			hienThiDanhSach();
			xuLyLamMoi();
			refreshMa();
		} else {
			JOptionPane.showMessageDialog(this, "Xóa thất bại!");
		}
	}

	private void hienThiDanhSach() {
		@SuppressWarnings("unchecked")
		TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tblKM.getRowSorter();
		if (sorter != null) {
			sorter.setRowFilter(null);
		}
		model.setRowCount(0);
		List<KhuyenMai> ds = khuyenMaiDAO.layTatCa();
		for (KhuyenMai km : ds) {
			model.addRow(new Object[] { km.getMaKM(), km.getTenKM(), km.getPhanTramGiam(), km.getloaiKM(),
					km.getNgayBatDau(), km.getNgayKetThuc(),
					km.getTrangThai() == 1 ? "Còn hiệu lực" : "Hết hiệu lực" });
		}
	}

	private void hienThiChiTietKhuyenMai() {
		int row = tblKM.getSelectedRow();
		if (row >= 0) {
			txtMaKM.setText(tblKM.getValueAt(row, 0).toString());

			txtTenKM.setText(tblKM.getValueAt(row, 1).toString());
			txtPhanTram.setText(tblKM.getValueAt(row, 2).toString());
			cboLoaiKM.setSelectedItem(tblKM.getValueAt(row, 3).toString());

			LocalDate ngayBD = (LocalDate) tblKM.getValueAt(row, 4);
			LocalDate ngayKT = (LocalDate) tblKM.getValueAt(row, 5);
			spNgayBD.setValue(java.sql.Date.valueOf(ngayBD));
			spNgayKT.setValue(java.sql.Date.valueOf(ngayKT));

			cboTrangThai.setSelectedItem(tblKM.getValueAt(row, 6).toString());

		}
	}

	private void capNhatKhuyenMai() {
		try {
			String maKM = txtMaKM.getText().trim();
			String tenKM = txtTenKM.getText().trim();
			double phanTramGiam = Double.parseDouble(txtPhanTram.getText().trim());
			String loaiKM = cboLoaiKM.getSelectedItem().toString();
			int trangThai = cboTrangThai.getSelectedItem().toString().equals("Còn hiệu lực") ? 1 : 0;

			java.util.Date ngayBDUtil = (java.util.Date) spNgayBD.getValue();
			java.util.Date ngayKTUtil = (java.util.Date) spNgayKT.getValue();
			LocalDate ngayBD = ngayBDUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate ngayKT = ngayKTUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (ngayKT.isBefore(ngayBD)) {
				JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			KhuyenMai km = new KhuyenMai(maKM, tenKM, phanTramGiam, loaiKM, ngayBD, ngayKT, trangThai);

			if (khuyenMaiDAO.capNhatKhuyenMai(km)) {
				JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công!");
				hienThiDanhSach();
				xuLyLamMoi();
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Phần trăm giảm phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + e.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public String taoMaKhuyenMai() {
		String maKMCuoi = khuyenMaiDAO.getMaKhuyenMaiCuoiCung();
		if (maKMCuoi == null) {
			return "KM001";
		}
		try {
			String phanSo = maKMCuoi.substring(2);
			int soHienTai = Integer.parseInt(phanSo);
			soHienTai++;
			String phanSoMoi = String.format("%03d", soHienTai);
			return "KM" + phanSoMoi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "KM_ERROR";
	}

	private void xuLyLamMoi() {
		txtMaKM.setText(taoMaKhuyenMai());
		txtTenKM.setText("");
		txtPhanTram.setText("");
		cboLoaiKM.setSelectedIndex(0);
		cboTrangThai.setSelectedIndex(0);
		txtSearch.setText("");
		spNgayBD.setValue(new java.util.Date());
		spNgayKT.setValue(new java.util.Date());

		tblKM.clearSelection();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		xuLyLamMoi();
		refreshMa();
		hienThiDanhSach();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
