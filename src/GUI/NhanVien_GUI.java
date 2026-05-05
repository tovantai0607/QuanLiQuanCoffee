package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DAO.NhanVien_DAO;
import Entity.NhanVien;

public class NhanVien_GUI extends JPanel implements ActionListener, MouseListener, ComponentListener {

	private MainFrame mainFrame;

	private JTextField txtMaNV, txtHoTen, txtSDT, txtEmail, txtNgayVaoLam, txtSearch;
	private JComboBox<String> cboChucVu, cboTrangThai;
	private JButton btnSearch, btnDelete, btnHome, btnAdd, btnUpdate;

	private DefaultTableModel tableModel;
	private JTable table;
	private String[] headerTable = { "Mã NV", "Họ tên", "SĐT", "Email", "Chức vụ", "Ngày vào làm", "Trạng thái" };
	private NhanVien_DAO nv_dao;

	private JLabel lblSearch;

	public NhanVien_GUI(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.nv_dao = new NhanVien_DAO();

		setLayout(new BorderLayout());

		// ===== NORTH: Tiêu đề =====
		JPanel pnNorth = new JPanel();
		JLabel title = new JLabel("NHÂN VIÊN");
		title.setFont(new Font("Arial", Font.BOLD, 26));
		pnNorth.add(title);
		add(pnNorth, BorderLayout.NORTH);

		// ===== CENTER =====
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new BorderLayout());

		// --- Form nhập liệu ---
		JPanel pnUp = new JPanel();
		pnUp.setLayout(new BorderLayout());
		pnUp.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

		JPanel pnForm = new JPanel();
		pnForm.setLayout(new BoxLayout(pnForm, BoxLayout.Y_AXIS));

		txtMaNV = new JTextField(50);
		txtMaNV.setEditable(false);
		txtHoTen = new JTextField(50);
		txtSDT = new JTextField(50);
		txtEmail = new JTextField(50);
		txtNgayVaoLam = new JTextField(20);
		txtNgayVaoLam.setEditable(false);

		cboChucVu = new JComboBox<>(new String[] { "Nhân viên", "Quản lý", "Thu ngân", "Pha chế" });
		cboChucVu.setPreferredSize(new Dimension(200, 25));

		cboTrangThai = new JComboBox<>(new String[] { "Đang làm", "Đã nghỉ" });
		cboTrangThai.setPreferredSize(new Dimension(200, 25));

		setTrangThaiMacDinh();

		// Row 1: Mã nhân viên
		JPanel row1 = new JPanel();
		row1.add(new JLabel("Mã nhân viên: "));
		row1.add(txtMaNV);
		pnForm.add(row1);

		// Row 2: Họ tên
		JPanel row2 = new JPanel();
		row2.add(Box.createHorizontalStrut(45));
		row2.add(new JLabel("Họ tên: "));
		row2.add(txtHoTen);
		pnForm.add(row2);

		// Row 3: Số điện thoại
		JPanel row3 = new JPanel();
		row3.add(Box.createHorizontalStrut(5));
		row3.add(new JLabel("Số điện thoại: "));
		row3.add(txtSDT);
		pnForm.add(row3);

		// Row 4: Email
		JPanel row4 = new JPanel();
		row4.add(Box.createHorizontalStrut(47));
		row4.add(new JLabel("Email: "));
		row4.add(txtEmail);
		pnForm.add(row4);

		// Row 5: Chức vụ + Trạng thái
		JPanel row5 = new JPanel();
		row5.add(new JLabel("Chức vụ: "));
		row5.add(cboChucVu);
		row5.add(Box.createHorizontalStrut(20));
		row5.add(new JLabel("Trạng thái: "));
		row5.add(cboTrangThai);
		pnForm.add(row5);

		// Row 6: Ngày vào làm
		JPanel row6 = new JPanel();
		row6.add(new JLabel("Ngày vào làm: "));
		row6.add(txtNgayVaoLam);
		pnForm.add(row6);

		pnUp.add(pnForm, BorderLayout.CENTER);

		// --- Buttons + Search ---
		JPanel row7 = new JPanel(new BorderLayout());
		btnAdd = new JButton("Thêm");
		btnDelete = new JButton("Xóa");
		btnUpdate = new JButton("Cập nhật");
		JPanel pnButtons = new JPanel();
		pnButtons.add(btnAdd);
		pnButtons.add(btnDelete);
		pnButtons.add(btnUpdate);
		row7.add(pnButtons, BorderLayout.CENTER);

		JPanel pnSearch = new JPanel();
		pnSearch.add(lblSearch = new JLabel("Nhập tên nhân viên"));
		pnSearch.add(txtSearch = new JTextField(10));
		pnSearch.add(btnSearch = new JButton("Tìm kiếm"));
		row7.add(pnSearch, BorderLayout.EAST);

		pnUp.add(row7, BorderLayout.SOUTH);
		pnCenter.add(pnUp, BorderLayout.NORTH);

		// --- Bảng dữ liệu ---
		JPanel pnDown = new JPanel();
		tableModel = new DefaultTableModel(headerTable, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table = new JTable(tableModel);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		table.getColumnModel().getColumn(2).setPreferredWidth(110);
		table.getColumnModel().getColumn(3).setPreferredWidth(180);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(110);
		table.getColumnModel().getColumn(6).setPreferredWidth(90);

		table.setAutoCreateRowSorter(true);
		table.setRowSorter(new TableRowSorter<>(tableModel));

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(820, 300));
		pnDown.add(scroll);

		pnCenter.add(pnDown, BorderLayout.CENTER);
		add(pnCenter, BorderLayout.CENTER);

		// ===== SOUTH: Nút trang chủ =====
		JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnHome = new JButton("Trang chủ");
		pnSouth.add(btnHome);
		add(pnSouth, BorderLayout.SOUTH);

		// Load dữ liệu
		loadDSNV();

		// Đăng ký sự kiện
		btnAdd.addActionListener(this);
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnSearch.addActionListener(this);
		btnHome.addActionListener(this);
		table.addMouseListener(this);
		addComponentListener(this);
	}

	// ===== Load danh sách nhân viên lên bảng =====
	public void loadDSNV() {
		try {
			ArrayList<NhanVien> dsNhanVien = (ArrayList<NhanVien>) nv_dao.layTatCa();

			@SuppressWarnings("unchecked")
			TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
			if (sorter != null) {
				sorter.setRowFilter(null);
			}

			tableModel.setRowCount(0);
			for (NhanVien nv : dsNhanVien) {
				String trangThai = nv.getTrangThai() == 1 ? "Đang làm" : "Đã nghỉ";
				tableModel.addRow(new Object[] {
					nv.getMaNhanVien(),
					nv.getHoTen(),
					nv.getSoDienThoai(),
					nv.getEmail(),
					nv.getChucVu(),
					nv.getNgayVaoLam(),
					trangThai
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Có lỗi khi tải dữ liệu nhân viên lên bảng");
		}
	}

	// ===== Tạo mã nhân viên tự động =====
	public String taoMaNhanVien() {
		String maNVCuoi = nv_dao.getMaNhanVienCuoiCung();
		if (maNVCuoi == null) {
			return "NV001";
		}
		try {
			String phanSo = maNVCuoi.substring(2);
			int soHienTai = Integer.parseInt(phanSo);
			soHienTai++;
			String phanSoMoi = String.format("%03d", soHienTai);
			return "NV" + phanSoMoi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "NV_ERROR";
	}

	// ===== Set trạng thái mặc định cho form =====
	private void setTrangThaiMacDinh() {
		txtMaNV.setText(taoMaNhanVien());
		txtNgayVaoLam.setText(LocalDate.now().toString());
		cboChucVu.setSelectedIndex(0);
		cboTrangThai.setSelectedIndex(0);
	}

	// ===== Xóa trắng form =====
	public void clearTXT() {
		for (JTextField txt : new JTextField[] { txtHoTen, txtSDT, txtEmail, txtSearch }) {
			txt.setText("");
		}
		cboChucVu.setSelectedIndex(0);
		cboTrangThai.setSelectedIndex(0);
		txtHoTen.grabFocus();
	}

	// ===== Thêm nhân viên =====
	public void themNhanVien() {
		try {
			String maNV = txtMaNV.getText().trim();
			String hoTen = txtHoTen.getText().trim();
			String sdt = txtSDT.getText().trim();
			String email = txtEmail.getText().trim();
			String chucVu = cboChucVu.getSelectedItem().toString();
			int trangThai = cboTrangThai.getSelectedIndex() == 0 ? 1 : 0;

			if (maNV.isEmpty() || hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
				return;
			}

			if (nv_dao.kiemTraEmailTonTai(email)) {
				JOptionPane.showMessageDialog(this, "Email đã được đăng ký. Vui lòng chọn email khác");
				return;
			}

			if (nv_dao.kiemTraSoDienThoaiTonTai(sdt)) {
				JOptionPane.showMessageDialog(this, "Số điện thoại đã được đăng ký. Vui lòng chọn số khác");
				return;
			}

			NhanVien nv = new NhanVien(maNV, hoTen, sdt, email, chucVu, LocalDate.now(), trangThai);
			if (nv_dao.themNhanVien(nv)) {
				JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công");
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên");
			}
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi nhập liệu");
			e.printStackTrace();
		}
	}

	// ===== Xóa nhân viên =====
	public void xoaNV() {
		int row = table.getSelectedRow();
		if (row != -1) {
			String maNV = table.getValueAt(row, 0).toString();
			int hoiNhac = JOptionPane.showConfirmDialog(this, "Chắc chắn xóa nhân viên " + maNV + "?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);

			if (hoiNhac == JOptionPane.YES_OPTION) {
				if (nv_dao.xoaNhanVien(maNV)) {
					tableModel.removeRow(row);
					JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công");
				} else {
					JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên (có thể đang có ràng buộc dữ liệu)");
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa");
		}
	}

	// ===== Cập nhật nhân viên =====
	public void capNhat() {
		try {
			String maNV = txtMaNV.getText().trim();
			String hoTen = txtHoTen.getText().trim();
			String sdt = txtSDT.getText().trim();
			String email = txtEmail.getText().trim();
			String chucVu = cboChucVu.getSelectedItem().toString();
			LocalDate ngayVaoLam = LocalDate.parse(txtNgayVaoLam.getText().trim());
			int trangThai = cboTrangThai.getSelectedIndex() == 0 ? 1 : 0;

			if (maNV.isEmpty() || hoTen.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin");
				return;
			}

			NhanVien nv = new NhanVien(maNV, hoTen, sdt, email, chucVu, ngayVaoLam, trangThai);
			if (nv_dao.capNhatNhanVien(nv)) {
				loadDSNV();
				JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thành công");
				clearTXT();
			} else {
				JOptionPane.showMessageDialog(null, "Có lỗi khi cập nhật nhân viên");
			}
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Có lỗi khi cập nhật nhân viên");
			e.printStackTrace();
		}
	}

	// ===== Tìm kiếm nhân viên theo tên =====
	private void actionSearch() {
		String strSearch = txtSearch.getText().trim();
		if (strSearch.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa nhập tên nhân viên cần tìm");
			return;
		}

		@SuppressWarnings("unchecked")
		TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();

		if (sorter == null) {
			sorter = new TableRowSorter<>(tableModel);
			table.setRowSorter(sorter);
		}
		RowFilter<TableModel, Integer> searchFilter = new RowFilter<TableModel, Integer>() {
			@Override
			public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
				String hoTen = entry.getStringValue(1);
				return hoTen.toLowerCase().contains(strSearch.toLowerCase());
			}
		};

		sorter.setRowFilter(searchFilter);

		if (table.getRowCount() > 0) {
			int viewIndex = 0;
			table.setRowSelectionInterval(viewIndex, viewIndex);
			table.scrollRectToVisible(table.getCellRect(viewIndex, 0, true));
		} else {
			JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào có tên \"" + strSearch + "\"",
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);

			sorter.setRowFilter(null);
			txtSearch.setText("");
			table.clearSelection();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnAdd)) {
			themNhanVien();
			setTrangThaiMacDinh();
			loadDSNV();
		} else if (o.equals(btnDelete)) {
			xoaNV();
			setTrangThaiMacDinh();
		} else if (o.equals(btnUpdate)) {
			capNhat();
			setTrangThaiMacDinh();
		} else if (o.equals(btnSearch)) {
			actionSearch();
		} else if (o.equals(btnHome)) {
			mainFrame.switchToPanel(mainFrame.KEY_DAT_BAN);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		if (row >= 0) {
			txtMaNV.setText(table.getValueAt(row, 0).toString());
			txtHoTen.setText(table.getValueAt(row, 1).toString());
			txtSDT.setText(table.getValueAt(row, 2).toString());
			txtEmail.setText(table.getValueAt(row, 3).toString());

			// Set chức vụ combo box
			String chucVu = table.getValueAt(row, 4).toString();
			cboChucVu.setSelectedItem(chucVu);

			txtNgayVaoLam.setText(table.getValueAt(row, 5).toString());

			// Set trạng thái combo box
			String trangThai = table.getValueAt(row, 6).toString();
			cboTrangThai.setSelectedItem(trangThai);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
		loadDSNV();
		setTrangThaiMacDinh();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

}
