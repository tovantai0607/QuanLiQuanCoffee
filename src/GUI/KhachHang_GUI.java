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

import DAO.KhachHang_DAO;
import Entity.KhachHang;
public class KhachHang_GUI extends JPanel implements ActionListener, MouseListener, ComponentListener {

	private MainFrame mainFrame;

	private JTextField txtMaKH, txtHoTen, txtSDT, txtEmail, txtDiaChi, txtDiemTichLuy, txtNgayDangKy, txtSearch;
	private JButton btnSearch, btnDelete, btnHome, btnAdd, btnUpdate;

	private DefaultTableModel tableModel;
	private JTable table;
	private String[] headerTable = { "Mã KH", "Họ tên", "STĐ", "Email", "Địa chỉ", "Điểm tích lũy", "Ngày đăng ký" };
	private KhachHang_DAO kh_dao;

	private JLabel lblSearch;

	public KhachHang_GUI(MainFrame mainFrame) {
		// TODO Auto-generated constructor stub
		this.mainFrame = mainFrame;
		this.kh_dao = new KhachHang_DAO();

		setLayout(new BorderLayout());

		JPanel pnNorth = new JPanel();
		JLabel title = new JLabel("KHÁCH HÀNG");
		title.setFont(new Font("Arial", Font.BOLD, 26));
		pnNorth.add(title);
		add(pnNorth, BorderLayout.NORTH);

		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new BorderLayout());

		// up
		JPanel pnUp = new JPanel();
		pnUp.setLayout(new BorderLayout());
		pnUp.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

		JPanel pnForm = new JPanel();
		pnForm.setLayout(new BoxLayout(pnForm, BoxLayout.Y_AXIS));

		txtMaKH = new JTextField(50);
		txtMaKH.setEditable(false);
		txtHoTen = new JTextField(50);
		txtSDT = new JTextField(50);
		txtEmail = new JTextField(50);
		txtDiaChi = new JTextField(50);
		txtDiemTichLuy = new JTextField(20);
		txtDiemTichLuy.setEditable(false);
		txtNgayDangKy = new JTextField(20);
		txtNgayDangKy.setEditable(false);
		setTrangThaiMacDinh();

		JPanel row1 = new JPanel();
		row1.add(new JLabel("Mã khách hàng: "));
		row1.add(txtMaKH);
		pnForm.add(row1);

		JPanel row2 = new JPanel();
		row2.add(Box.createHorizontalStrut(45));
		row2.add(new JLabel("Họ tên: "));
		row2.add(txtHoTen);
		pnForm.add(row2);

		JPanel row3 = new JPanel();
		row3.add(Box.createHorizontalStrut(5));
		row3.add(new JLabel("Số điện thoại: "));
		row3.add(txtSDT);
		pnForm.add(row3);

		JPanel row4 = new JPanel();
		row4.add(Box.createHorizontalStrut(47));
		row4.add(new JLabel("Email: "));
		row4.add(txtEmail);
		pnForm.add(row4);

		JPanel row5 = new JPanel();
		row5.add(Box.createHorizontalStrut(42));
		row5.add(new JLabel("Địa chỉ: "));
		row5.add(txtDiaChi);
		pnForm.add(row5);

		JPanel row6 = new JPanel();
		row6.add(new JLabel("Điểm tích lũy: "));
		row6.add(txtDiemTichLuy);
		row6.add(new JLabel("Ngày đăng ký: "));
		row6.add(txtNgayDangKy);
		pnForm.add(row6);

		pnUp.add(pnForm, BorderLayout.CENTER);

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
		pnSearch.add(lblSearch = new JLabel("Nhập số điện thoại"));
		pnSearch.add(txtSearch = new JTextField(10));
		pnSearch.add(btnSearch = new JButton("Tìm kiếm"));
		row7.add(pnSearch, BorderLayout.EAST);

		pnUp.add(row7, BorderLayout.SOUTH);

		pnCenter.add(pnUp, BorderLayout.NORTH);

		// down
		JPanel pnDown = new JPanel();
		tableModel = new DefaultTableModel(headerTable, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table = new JTable(tableModel);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(6).setPreferredWidth(120);

		table.setAutoCreateRowSorter(true);
		table.setRowSorter(new TableRowSorter<>(tableModel));

		JScrollPane scroll = new JScrollPane(table);

		scroll.setPreferredSize(new Dimension(820, 300));

		pnDown.add(scroll);

		pnCenter.add(pnDown, BorderLayout.CENTER);

		add(pnCenter, BorderLayout.CENTER);

		JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnHome = new JButton("Trang chủ");
		pnSouth.add(btnHome);
		add(pnSouth, BorderLayout.SOUTH);

		loadDSKH();
		btnAdd.addActionListener(this);
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnSearch.addActionListener(this);
		btnHome.addActionListener(this);
		table.addMouseListener(this);
		addComponentListener(this);
	}

	public void loadDSKH() {
		try {
			ArrayList<KhachHang> dsKhachHang = (ArrayList<KhachHang>) kh_dao.layTatCa();

			@SuppressWarnings("unchecked")
			TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
			if (sorter != null) {
				sorter.setRowFilter(null);
			}

			tableModel.setRowCount(0);
			for (KhachHang it : dsKhachHang) {
				tableModel.addRow(new Object[] { it.getMaKhachHang(), it.getHoTen(), it.getSoDienThoai(), it.getEmail(),
						it.getDiaChi(), it.getDiemTichLuy(), it.getNgayDangKy() });
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Có lỗi khi tải dữ liệu lên bảng");
		}
	}

	public String taoMaKhachHang() {
		String maKhachHangCuoi = kh_dao.getMaKhachHangCuoiCung();
		if (maKhachHangCuoi == null) {
			return "KH001";
		}
		try {
			String phanSo = maKhachHangCuoi.substring(2);
			int soHienTai = Integer.parseInt(phanSo);
			soHienTai++;
			String phanSoMoi = String.format("%03d", soHienTai);
			return "KH" + phanSoMoi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "KH_ERROR";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o.equals(btnAdd)) {
			themKhachHang();
			setTrangThaiMacDinh();
			loadDSKH();
		} else if (o.equals(btnDelete)) {
			xoaKH();
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

	private void setTrangThaiMacDinh() {
		txtMaKH.setText(taoMaKhachHang());
		txtNgayDangKy.setText(LocalDate.now().toString());
		txtDiemTichLuy.setText("0.0");
	}

	public void clearTXT() {
		for (JTextField txt : new JTextField[] { txtDiaChi, txtDiemTichLuy, txtEmail, txtHoTen, txtMaKH, txtNgayDangKy,
				txtSDT, txtSearch }) {
			txt.setText("");
		}
		txtHoTen.grabFocus();
	}

	public void themKhachHang() {
		try {
			String maKH = txtMaKH.getText();
			String hoTen = txtHoTen.getText();
			String std = txtSDT.getText();
			String email = txtEmail.getText();
			String diaChi = txtDiaChi.getText();

			if (maKH.isEmpty() || hoTen.isEmpty() || std.isEmpty() || email.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
				return;
			}

			if (kh_dao.kiemTraEmailTonTai(email)) {
				JOptionPane.showMessageDialog(this, "Email đã được đăng ký. Vui lòng chọn email khác");
				return;
			}

			if (kh_dao.kiemTraSoDienThoaiTonTai(std)) {
				JOptionPane.showMessageDialog(this, "Số điện thoại đã được đăng ký. Vui lòng chọn số điện thoại khác");
				return;
			}

			KhachHang kh = new KhachHang(maKH, hoTen, std, email, diaChi, 0, LocalDate.now());
			if (kh_dao.themKhachHang(kh)) {
				JOptionPane.showMessageDialog(this, "Thêm thành công");
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng");
			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "Lỗi nhập liệu");
			e.printStackTrace();
		}

	}

	public void xoaKH() {
		int row = table.getSelectedRow();
		if (row != -1) {
			String maKH = table.getModel().getValueAt(row, 0).toString();
			int hoiNhac = JOptionPane.showConfirmDialog(this, "Chắc chắn xóa không?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);

			if (hoiNhac == JOptionPane.YES_OPTION) {
				if (kh_dao.xoaKhachHang(maKH)) {
					tableModel.removeRow(row);
				}
			}
		}
	}

	public void capNhat() {
		try {
			String maKH = txtMaKH.getText();
			String hoTen = txtHoTen.getText();
			String std = txtSDT.getText();
			String email = txtEmail.getText();
			String diaChi = txtDiaChi.getText();
			double diemTichLuy = Double.parseDouble(txtDiemTichLuy.getText());
			LocalDate ngayDangKy = LocalDate.parse(txtNgayDangKy.getText());
			if (maKH.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Khách hàng chưa tồn tại");
				return;
			}

			KhachHang X = new KhachHang(maKH, hoTen, std, email, diaChi, diemTichLuy, ngayDangKy);
			kh_dao.capNhatKhachHang(X);

			loadDSKH();
			JOptionPane.showMessageDialog(null, "Cập nhật khách hàng thành công");
			clearTXT();
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
			clearTXT();
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Có lỗi khi cập nhật khách hàng");
		}
	}

	private void actionSearch() {
		String strSearch = txtSearch.getText().trim();
		if (strSearch.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa nhập số điện thoại");
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
				String soDienThoai = entry.getStringValue(2);
				return soDienThoai.toLowerCase().contains(strSearch.toLowerCase());
			}
		};

		sorter.setRowFilter(searchFilter);

		if (table.getRowCount() > 0) {
			int viewIndex = 0;
			table.setRowSelectionInterval(viewIndex, viewIndex);
			table.scrollRectToVisible(table.getCellRect(viewIndex, 0, true));
		} else {
			JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào có số điện thoại " + strSearch,
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);

			sorter.setRowFilter(null);
			txtSearch.setText("");
			table.clearSelection();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int row = table.getSelectedRow();
		if (row >= 0) {
			txtMaKH.setText(table.getValueAt(row, 0).toString());
			txtHoTen.setText(table.getValueAt(row, 1).toString());
			txtSDT.setText(table.getValueAt(row, 2).toString());
			txtEmail.setText(table.getValueAt(row, 3).toString());
			txtDiaChi.setText(table.getValueAt(row, 4).toString());
			txtDiemTichLuy.setText(table.getValueAt(row, 5).toString());
			txtNgayDangKy.setText(table.getValueAt(row, 6).toString());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

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
		loadDSKH();
		setTrangThaiMacDinh();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
