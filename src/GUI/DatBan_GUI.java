package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import DAO.ChiTietDatBan_DAO;
import DAO.PhieuDatBan_DAO;
import Entity.ChiTietDatBan;
import Entity.PhieuDatBan;

public class DatBan_GUI extends JPanel implements ActionListener, ComponentListener {

	private static final long serialVersionUID = 1L;

	private static final String LOC_TAT_CA = "T\u1ea5t c\u1ea3";
	private static final String LOC_HOM_NAY = "H\u00f4m nay";
	private static final String LOC_DA_QUA = "\u0110\u00e3 qua";

	private JLabel title;
	private JButton btnSearch;
	private JButton btnChonBan;
	private JButton btnChonMon;
	private JButton btnHuyPhieu;
	private JButton btnDonDep;
	private JTextField txtSearch;
	private DefaultTableModel modelTabel;
	private JTable table;
	private PhieuDatBan_DAO pdb_dao;
	private MainFrame mainFrame;
	private JLabel lblSearch;
	private ChiTietDatBan_DAO ctdb_dao;
	private JComboBox<String> cboLocPhieu;
	private ArrayList<PhieuDatBan> dsTatCaPhieuDat;

	public DatBan_GUI(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.pdb_dao = new PhieuDatBan_DAO();
		this.ctdb_dao = new ChiTietDatBan_DAO();
		this.dsTatCaPhieuDat = new ArrayList<PhieuDatBan>();
		setLayout(new BorderLayout());

		JPanel pnNorth = new JPanel();
		pnNorth.add(title = new JLabel("Qu\u1ea3n l\u00fd \u0110\u1eb7t b\u00e0n"));
		title.setFont(new Font("Arial", Font.BOLD, 20));

		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new BoxLayout(pnCenter, BoxLayout.Y_AXIS));

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		searchPanel.add(new JLabel("Xem phi\u1ebfu: "));
		searchPanel.add(cboLocPhieu = new JComboBox<>(new String[] { LOC_TAT_CA, LOC_HOM_NAY, LOC_DA_QUA }));
		searchPanel.add(Box.createHorizontalStrut(10));
		searchPanel.add(lblSearch = new JLabel("Nh\u1eadp s\u1ed1 \u0111i\u1ec7n tho\u1ea1i: "));
		searchPanel.add(txtSearch = new JTextField(10));
		searchPanel.add(btnSearch = new JButton("T\u00ecm ki\u1ebfm"));
		pnCenter.add(searchPanel, BorderLayout.NORTH);
		pnCenter.add(Box.createVerticalStrut(10));

		JPanel tableData = new JPanel(new BorderLayout());
		String[] cols = { "M\u00e3 phi\u1ebfu \u0111\u1eb7t b\u00e0n", "Ng\u00e0y \u0111\u1eb7t b\u00e0n",
				"Gi\u1edd b\u1eaft \u0111\u1ea7u", "Gi\u1edd k\u1ebft th\u00fac", "S\u1ed1 ng\u01b0\u1eddi", "Ghi ch\u00fa",
				"Tr\u1ea1ng th\u00e1i", "M\u00e3 kh\u00e1ch h\u00e0ng", "M\u00e3 nh\u00e2n vi\u00ean",
				"S\u1ed1 \u0111i\u1ec7n tho\u1ea1i" };
		modelTabel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

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

		JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnSouth.add(btnDonDep = new JButton("D\u1ecdn d\u1eb9p"));
		pnSouth.add(Box.createHorizontalStrut(10));
		pnSouth.add(btnHuyPhieu = new JButton("H\u1ee7y phi\u1ebfu"));
		pnSouth.add(Box.createHorizontalStrut(10));
		pnSouth.add(btnChonBan = new JButton("Ch\u1ecdn b\u00e0n"));
		pnSouth.add(btnChonMon = new JButton("Ch\u1ecdn m\u00f3n"));

		add(pnNorth, BorderLayout.NORTH);
		add(pnCenter, BorderLayout.CENTER);
		add(pnSouth, BorderLayout.SOUTH);

		loadPhieuDatBan();

		addComponentListener(this);
		btnChonBan.addActionListener(this);
		btnChonMon.addActionListener(this);
		btnSearch.addActionListener(this);
		btnHuyPhieu.addActionListener(this);
		btnDonDep.addActionListener(this);
		cboLocPhieu.addActionListener(this);
		txtSearch.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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
		if (o == btnDonDep) {
			donDepPhieuCu();
		}
		if (o == cboLocPhieu || o == txtSearch) {
			apDungBoLoc(false);
		}
	}

	private void donDepPhieuCu() {
		int soPhieuCu = pdb_dao.demPhieuDatCu();
		if (soPhieuCu <= 0) {
			JOptionPane.showMessageDialog(this, "Kh\u00f4ng c\u00f3 phi\u1ebfu \u0111\u1eb7t c\u0169 \u0111\u1ec3 d\u1ecdn d\u1eb9p.");
			return;
		}

		String thongBao = "C\u00f3 " + soPhieuCu
				+ " phi\u1ebfu \u0111\u1eb7t c\u0169 (\u0111\u1eb7t tr\u01b0\u1edbc h\u00f4m nay). B\u1ea1n c\u00f3 ch\u1eafc mu\u1ed1n x\u00f3a kh\u00f4ng?";
		int xacNhan = JOptionPane.showConfirmDialog(this, thongBao, "X\u00e1c nh\u1eadn d\u1ecdn d\u1eb9p",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		int soPhieuDaXoa = pdb_dao.xoaTatCaPhieuDatCu();
		if (soPhieuDaXoa >= 0) {
			JOptionPane.showMessageDialog(this, "\u0110\u00e3 x\u00f3a " + soPhieuDaXoa + " phi\u1ebfu \u0111\u1eb7t c\u0169.");
			loadPhieuDatBan();
			return;
		}

		JOptionPane.showMessageDialog(this, "D\u1ecdn d\u1eb9p phi\u1ebfu c\u0169 th\u1ea5t b\u1ea1i.", "L\u1ed7i",
				JOptionPane.ERROR_MESSAGE);
	}

	private void huyPhieu() {
		int rowSelected = table.getSelectedRow();
		if (rowSelected == -1) {
			JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn phi\u1ebfu \u0111\u1eb7t b\u00e0n.");
			return;
		}

		int modelRow = table.convertRowIndexToModel(rowSelected);
		String maPDB = modelTabel.getValueAt(modelRow, 0).toString();
		int hoiNhac = JOptionPane.showConfirmDialog(this, "Ch\u1eafc ch\u1eafn h\u1ee7y phi\u1ebfu " + maPDB,
				"X\u00e1c nh\u1eadn", JOptionPane.YES_NO_OPTION);
		if (hoiNhac == JOptionPane.YES_OPTION && pdb_dao.capNhatTrangThaiPhieu(maPDB, 0)) {
			JOptionPane.showMessageDialog(this, "\u0110\u00e3 h\u1ee7y phi\u1ebfu " + maPDB);
			loadPhieuDatBan();
		}
	}

	private void chonBan() {
		mainFrame.switchToPanel(mainFrame.KEY_CHON_BAN);
	}

	private void chonMon() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn phi\u1ebfu \u0111\u1eb7t b\u00e0n");
			return;
		}

		int modelRow = table.convertRowIndexToModel(selectedRow);
		String maPhieu = modelTabel.getValueAt(modelRow, 0).toString();
		String trangThai = modelTabel.getValueAt(modelRow, 6).toString();

		if (!"Ch\u01b0a s\u1eed d\u1ee5ng".equals(trangThai)) {
			JOptionPane.showMessageDialog(this,
					"Phi\u1ebfu n\u00e0y \u0111\u00e3 \u0111\u01b0\u1ee3c ho\u1eb7c \u0111ang \u0111\u01b0\u1ee3c s\u1eed d\u1ee5ng");
			return;
		}

		try {
			pdb_dao.capNhatTrangThaiPhieu(maPhieu, 2);
			mainFrame.switchToPanel(mainFrame.KEY_BAN_HANG);
			modelTabel.setValueAt("\u0110\u00e3 s\u1eed d\u1ee5ng", modelRow, 6);

			ArrayList<String> dsMaBan = layMaBanTuCTDB(maPhieu);
			String maKhachHang = modelTabel.getValueAt(modelRow, 7).toString();
			mainFrame.setMaKhachHang(maKhachHang);
			mainFrame.setMaPhieuDatBan(maPhieu);
			mainFrame.setDsMaBan(dsMaBan);
		} catch (Exception ex) {
			System.out.println(modelRow);
			ex.printStackTrace();
		}
	}

	private ArrayList<String> layMaBanTuCTDB(String maPhieu) {
		ArrayList<String> dsMaBan = new ArrayList<String>();
		ArrayList<ChiTietDatBan> dsCT = (ArrayList<ChiTietDatBan>) ctdb_dao.layChiTietTheoMaPDB(maPhieu);
		for (ChiTietDatBan ct : dsCT) {
			dsMaBan.add(ct.getBan().getMaBan());
		}
		return dsMaBan;
	}

	public void loadPhieuDatBan() {
		modelTabel.setRowCount(0);
		table.clearSelection();
		try {
			pdb_dao.capNhatTranThaiTuDong();
			dsTatCaPhieuDat = new ArrayList<PhieuDatBan>(pdb_dao.layTatCa());
			apDungBoLoc(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void updateTableData(ArrayList<PhieuDatBan> dsPDB) {
		modelTabel.setRowCount(0);
		try {
			for (PhieuDatBan pdb : dsPDB) {
				String maKhachHang = (pdb.getKhachHang() != null) ? pdb.getKhachHang().getMaKhachHang() : "";
				String maNhanVien = (pdb.getNhanVien() != null) ? pdb.getNhanVien().getMaNhanVien() : "";
				String soDienThoai = laySoDienThoaiHienThi(pdb);
				modelTabel.addRow(new Object[] { pdb.getMaPhieuDat(), pdb.getNgayDat(), pdb.getGioBatDau(),
						pdb.getGioKetThuc(), pdb.getSoNguoi(), pdb.getGhiChu(), chuyenTrangThai(pdb.getTrangThai()),
						maKhachHang, maNhanVien, soDienThoai });
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String chuyenTrangThai(int trangThai) {
		if (trangThai == 1) {
			return "Ch\u01b0a s\u1eed d\u1ee5ng";
		}
		if (trangThai == 0) {
			return "\u0110\u00e3 h\u1ee7y";
		}
		if (trangThai == 2) {
			return "\u0110\u00e3 s\u1eed d\u1ee5ng";
		}
		if (trangThai == 3) {
			return "H\u1ebft h\u1ea1n";
		}
		return "";
	}

	private String laySoDienThoaiHienThi(PhieuDatBan pdb) {
		if (pdb.getKhachHang() != null && pdb.getKhachHang().getSoDienThoai() != null) {
			return pdb.getKhachHang().getSoDienThoai();
		}
		return "";
	}

	private void actionSearch() {
		String strSearch = txtSearch.getText().trim();
		apDungBoLoc(!strSearch.isEmpty());
	}

	private void apDungBoLoc(boolean thongBaoKhiKhongTimThay) {
		ArrayList<PhieuDatBan> dsLoc = new ArrayList<PhieuDatBan>();
		LocalDate homNay = LocalDate.now();
		String soDienThoaiCanTim = txtSearch.getText().trim().toLowerCase();

		for (PhieuDatBan pdb : dsTatCaPhieuDat) {
			if (phuHopBoLocNgay(pdb, homNay) && phuHopTimTheoSoDienThoai(pdb, soDienThoaiCanTim)) {
				dsLoc.add(pdb);
			}
		}

		updateTableData(dsLoc);
		if (table.getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
			table.scrollRectToVisible(table.getCellRect(0, 0, true));
			return;
		}

		table.clearSelection();
		if (thongBaoKhiKhongTimThay) {
			JOptionPane.showMessageDialog(this,
					"Kh\u00f4ng t\u00ecm th\u1ea5y phi\u1ebfu \u0111\u1eb7t b\u00e0n n\u00e0o c\u00f3 s\u1ed1 \u0111i\u1ec7n tho\u1ea1i: "
							+ txtSearch.getText().trim(),
					"Th\u00f4ng b\u00e1o", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private boolean phuHopBoLocNgay(PhieuDatBan pdb, LocalDate homNay) {
		if (pdb == null || pdb.getNgayDat() == null) {
			return false;
		}

		String luaChon = (String) cboLocPhieu.getSelectedItem();
		if (LOC_HOM_NAY.equals(luaChon)) {
			return pdb.getNgayDat().isEqual(homNay);
		}
		if (LOC_DA_QUA.equals(luaChon)) {
			return pdb.getNgayDat().isBefore(homNay);
		}
		return true;
	}

	private boolean phuHopTimTheoSoDienThoai(PhieuDatBan pdb, String soDienThoaiCanTim) {
		if (soDienThoaiCanTim.isEmpty()) {
			return true;
		}

		String soDienThoai = laySoDienThoaiHienThi(pdb).trim().toLowerCase();
		return soDienThoai.contains(soDienThoaiCanTim);
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
		txtSearch.setText("");
		cboLocPhieu.setSelectedItem(LOC_TAT_CA);
		loadPhieuDatBan();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
