package GUI;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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

import DAO.ChiTietDatBan_DAO;
import DAO.PhieuDatBan_DAO;
import Entity.ChiTietDatBan;
import Entity.PhieuDatBan;

public class DatBan_GUI extends JPanel implements ActionListener, ComponentListener {

	private JLabel title;
	private JButton btnSearch, btnChonBan, btnChonMon;
	private JTextField txtSearch;
	private DefaultTableModel modelTabel;
	private JTable table;
	private PhieuDatBan_DAO pdb_dao;
	private MainFrame mainFrame;
	private JLabel lblSearch;
	private ChiTietDatBan_DAO ctdb_dao;
	private JButton btnHuyPhieu;

	public DatBan_GUI(MainFrame mainFrame) {
		// TODO Auto-generated constructor stub
		this.mainFrame = mainFrame;
		this.pdb_dao = new PhieuDatBan_DAO();
		this.ctdb_dao = new ChiTietDatBan_DAO();
		this.setLayout(new BorderLayout());
		// pnNorth
		JPanel pnNorth = new JPanel();
		pnNorth.add(title = new JLabel("Quản lý Đặt bàn"));
		Font fnt = new Font("Arial", Font.BOLD, 20);
		title.setFont(fnt);
		// pnCenter
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new BoxLayout(pnCenter, BoxLayout.Y_AXIS));
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		searchPanel.add(lblSearch = new JLabel("Nhập số điện thoại: "));
		searchPanel.add(txtSearch = new JTextField(10));
		searchPanel.add(btnSearch = new JButton("Tìm kiếm"));
		pnCenter.add(searchPanel, BorderLayout.NORTH);
		pnCenter.add(Box.createVerticalStrut(10));
		JPanel tableData = new JPanel();
		tableData.setLayout(new BorderLayout());
		String[] cols = { "Mã phiếu đặt bàn", "Ngày đặt bàn", "Giờ băt đầu", "Giờ kết thúc", "Số người", "Ghi chú",
				"Trạng thái", "Mã khách hàng", "Mã nhân viên", "Số điện thoại" };
		modelTabel = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return col != 0;
			}
		};
		table = new JTable(modelTabel);

		table.setAutoCreateRowSorter(true);
		table.setRowSorter(new TableRowSorter<>(modelTabel));

		JScrollPane sp = new JScrollPane(table);
		sp.setBorder(BorderFactory.createEmptyBorder());
		tableData.add(sp, BorderLayout.CENTER);
		pnCenter.add(tableData, BorderLayout.CENTER);

		// pnSouth
		JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnSouth.add(btnHuyPhieu = new JButton("Hủy phiếu"));
		pnSouth.add(Box.createHorizontalStrut(10));
		pnSouth.add(btnChonBan = new JButton("Chọn bàn"));
		pnSouth.add(btnChonMon = new JButton("Chọn món"));

		add(pnNorth, BorderLayout.NORTH);
		add(pnCenter, BorderLayout.CENTER);
		add(pnSouth, BorderLayout.SOUTH);
		loadPhieuDatBan();
		this.addComponentListener(this);
		btnChonBan.addActionListener(this);
		btnChonMon.addActionListener(this);
		btnSearch.addActionListener(this);
		btnHuyPhieu.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o == btnChonMon) {
			chonMon();
		}
		if (o == btnChonBan) {
			chonBan();
		}
		if (o == btnSearch) {
			actionSearch();
		}
		if (o == btnHuyPhieu) {
			huyPhieu();
		}
	}

	private void huyPhieu() {
		// TODO Auto-generated method stub
		int row_selected = table.getSelectedRow();
		if (row_selected != -1) {
			int modelRow = table.convertRowIndexToModel(row_selected);
			String maPDB = modelTabel.getValueAt(modelRow, 0).toString();
			int hoiNhac = JOptionPane.showConfirmDialog(this, "Chắc chắn hủy phiếu " + maPDB, "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (hoiNhac == JOptionPane.YES_OPTION) {
				if (pdb_dao.capNhatTrangThaiPhieu(maPDB, 0)) {
					JOptionPane.showMessageDialog(this, "Đã xóa phiếu " + maPDB);
					loadPhieuDatBan();
				}
			}
		}
	}

	private void chonBan() {
		// TODO Auto-generated method stub
		mainFrame.switchToPanel(mainFrame.KEY_CHON_BAN);
	}

	private void chonMon() {
		// TODO Auto-generated method stub
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			int modelRow = table.convertRowIndexToModel(selectedRow);
			String maPhieu = modelTabel.getValueAt(modelRow, 0).toString();
			String trangThai = modelTabel.getValueAt(modelRow, 6).toString();

			if (trangThai.equals("Chưa sử dụng")) {
				try {
					pdb_dao.capNhatTrangThaiPhieu(maPhieu, 2); // trangThai = 2 (đã sử dụng)
					mainFrame.switchToPanel(mainFrame.KEY_BAN_HANG);
					modelTabel.setValueAt("Đã sử dụng", modelRow, 6); // set "Đã sử dụng" khi nhấn thanh toán thành
																			// công
					// lấy maBan, maPDB lưu vào trung gian MainFrame
					ArrayList<String> dsMaBan = layMaBanTuCTDB(maPhieu);
					String maKhachHang = modelTabel.getValueAt(modelRow, 7).toString();
					mainFrame.setMaKhachHang(maKhachHang);
					mainFrame.setMaPhieuDatBan(maPhieu);
					mainFrame.setDsMaBan(dsMaBan);

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(modelRow);
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Phiếu này đã được hoặc đang được sử dụng");
				return;
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu đặt bàn");
			return;
		}

	}

	private ArrayList<String> layMaBanTuCTDB(String maPhieu) {
		ArrayList<String> dsMaBan = new ArrayList<String>();
		ArrayList<ChiTietDatBan> dsCT = (ArrayList<ChiTietDatBan>) ctdb_dao.layChiTietTheoMaPDB(maPhieu);
		for (ChiTietDatBan ct : dsCT) {
			dsMaBan.add(ct.getBan().getMaBan());
		}
		return dsMaBan;
		// TODO Auto-generated method stub

	}

	public void loadPhieuDatBan() {
		@SuppressWarnings("unchecked")
		TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
		if (sorter != null) {
			sorter.setRowFilter(null);
		}

		modelTabel.setRowCount(0);
		try {
			ArrayList<PhieuDatBan> dsPDB = (ArrayList<PhieuDatBan>) pdb_dao.layTatCa();
			updateTableData(dsPDB);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void updateTableData(ArrayList<PhieuDatBan> dsPDB) {
		modelTabel.setRowCount(0);
		try {
			for (PhieuDatBan pdb : dsPDB) {
				String trangThai = "";
				int trangThai_pdb = pdb.getTrangThai();
				if (trangThai_pdb == 1) {
					trangThai = "Chưa sử dụng";
				} else if (trangThai_pdb == 0) {
					trangThai = "Đã hủy";
				} else if (trangThai_pdb == 2) {
					trangThai = "Đã sử dụng";
				} else if (trangThai_pdb == 3) {
					trangThai = "Hết hạn";
				}
				String maKhachHang = (pdb.getKhachHang() != null) ? pdb.getKhachHang().getMaKhachHang() : "";
				String maNhanVien = (pdb.getNhanVien() != null) ? pdb.getNhanVien().getMaNhanVien() : "";
				String soDienThoai = (pdb.getKhachHang() != null) ? pdb.getKhachHang().getSoDienThoai() : "";
				modelTabel.addRow(
						new Object[] { pdb.getMaPhieuDat(), pdb.getNgayDat(), pdb.getGioBatDau(), pdb.getGioKetThuc(),
								pdb.getSoNguoi(), pdb.getGhiChu(), trangThai, maKhachHang, maNhanVien, soDienThoai });
			}
		} catch (

		Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
			sorter = new TableRowSorter<>(modelTabel);
			table.setRowSorter(sorter);
		}
		RowFilter<TableModel, Integer> searchFilter = new RowFilter<TableModel, Integer>() {
			@Override
			public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
				String soDienThoai = entry.getStringValue(9);
				return soDienThoai.toLowerCase().contains(strSearch.toLowerCase());
			}
		};

		sorter.setRowFilter(searchFilter);

		if (table.getRowCount() > 0) {
			int viewIndex = 0;
			table.setRowSelectionInterval(viewIndex, viewIndex);
			table.scrollRectToVisible(table.getCellRect(viewIndex, 0, true));
		} else {
			JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu đặt bàn nào có số điện thoại:  " + strSearch,
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);

			sorter.setRowFilter(null);
			txtSearch.setText("");
			table.clearSelection();
		}
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
		txtSearch.setText("");
		loadPhieuDatBan();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
