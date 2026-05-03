package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DAO.LoaiSanPham_DAO;
import DAO.SanPham_DAO;
import Entity.ChiTietHoaDon;
import Entity.HoaDon;
import Entity.LoaiSanPham;
import Entity.SanPham;

public class Menu_GUI extends JPanel implements ActionListener, ComponentListener {

	private MainFrame mainFrame;
	private JTable orderTable, menuTable;
	private DefaultTableModel orderModel, menuModel;
	private JLabel lblTotalOrder;
	private JButton btnDat, btnTaoHoaDon, btnTrangChu;
	private JComboBox<String> cbFilter;
	private JTextField txtSearch;

	private final int COL_TEN_SP = 0;
	private final int COL_SO_LUONG = 1;
	private final int COL_GIA = 2;
	private final int COL_TONG_TIEN = 3;
	private JButton btnXoa;
	private SanPham_DAO sp_dao;
	private LoaiSanPham_DAO loaiSP_dao;
	private JButton btnSearch;
	private JPanel pnFilterRight;
	private JPanel pnFilterLelf;
	private JTextArea txtGhiChu;
	private JLabel lblGhiChu;

	private HoaDon hoaDonHienTai;

	public Menu_GUI(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.sp_dao = new SanPham_DAO();
		this.loaiSP_dao = new LoaiSanPham_DAO();

		this.hoaDonHienTai = new HoaDon();
		this.hoaDonHienTai.setNgayTao(LocalDate.now());
		this.hoaDonHienTai.setTrangThaiThanhToan(0); // chưa thanh toán

		setLayout(new BorderLayout());

		// pnNorth
		JLabel title = new JLabel("Menu", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		add(title, BorderLayout.NORTH);

		// pnCenter
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createOrderPanel(), createMenuPanel());
		splitPane.setResizeWeight(0.4);
		add(splitPane, BorderLayout.CENTER);

		// pnSouth
		JPanel pnSouth = new JPanel(new BorderLayout());
		btnTrangChu = new JButton("Trang chủ");
		btnTaoHoaDon = new JButton("Tạo hóa đơn");
		pnSouth.add(btnTrangChu, BorderLayout.WEST);
		pnSouth.add(btnTaoHoaDon, BorderLayout.EAST);
		add(pnSouth, BorderLayout.SOUTH);

		btnDat.addActionListener(this);
		btnTaoHoaDon.addActionListener(this);
		btnTrangChu.addActionListener(this);
		btnXoa.addActionListener(this);
		cbFilter.addActionListener(this);
		addComponentListener(this);
	}

	// Panel "Order"
	private JPanel createOrderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Order", SwingConstants.CENTER), BorderLayout.NORTH);

		String[] orderCols = { "Tên sản phẩm", "Số lượng", "Đơn Giá", "Tổng tiền" };
		orderModel = new DefaultTableModel(orderCols, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == COL_SO_LUONG;
			}

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				if (column == COL_SO_LUONG) {
					int quantity = 1;
					try {
						quantity = Integer.parseInt(aValue.toString());
					} catch (NumberFormatException e) {
						quantity = 1;
					}
					if (quantity <= 0)
						quantity = 1;

					String tenSP = (String) getValueAt(row, COL_TEN_SP);

					for (ChiTietHoaDon ct : hoaDonHienTai.getDsChiTiet()) {
						if (ct.getSanPham().getTenSanPham().equalsIgnoreCase(tenSP)) {
							ct.setSoLuong(quantity);
							break;
						}
					}
					refreshOrderTable();

				} else {
					super.setValueAt(aValue, row, column);
				}
			}
		};

		orderTable = new JTable(orderModel);

		orderTable.getColumnModel().getColumn(COL_SO_LUONG).setCellRenderer(new QuantityCellRenderer());
		orderTable.getColumnModel().getColumn(COL_SO_LUONG).setCellEditor(new QuantityCellEditor());
		orderTable.setRowHeight(35);

		orderTable.getColumnModel().removeColumn(orderTable.getColumn("Đơn Giá"));

		orderModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE || e.getType() == TableModelEvent.INSERT
						|| e.getType() == TableModelEvent.DELETE) {
					updateTotalOrder();
				}
			}
		});

		JPanel pnCenter = new JPanel(new BorderLayout());
		pnCenter.add(new JScrollPane(orderTable), BorderLayout.CENTER);

		lblGhiChu = new JLabel("Ghi chú: ");
		lblGhiChu.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel pnGhiChu = new JPanel();
		pnGhiChu.setLayout(new BoxLayout(pnGhiChu, BoxLayout.Y_AXIS));
		pnGhiChu.add(lblGhiChu);
		pnGhiChu.add(Box.createVerticalStrut(5));
		txtGhiChu = new JTextArea();
		txtGhiChu.setRows(5);
		txtGhiChu.setLineWrap(true);
		txtGhiChu.setWrapStyleWord(true);
		JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
		scrollGhiChu.setPreferredSize(new Dimension(300, 150));
		scrollGhiChu.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnGhiChu.add(scrollGhiChu);
		pnCenter.add(pnGhiChu, BorderLayout.SOUTH);
		panel.add(pnCenter, BorderLayout.CENTER);
		btnXoa = new JButton("Xóa");
		lblTotalOrder = new JLabel("Tổng tiền: 0 VND", SwingConstants.RIGHT);
		lblTotalOrder.setFont(new Font("Arial", Font.BOLD, 16));
		JPanel pnSouth = new JPanel(new BorderLayout());
		pnSouth.add(btnXoa, BorderLayout.WEST);
		pnSouth.add(lblTotalOrder, BorderLayout.EAST);
		panel.add(pnSouth, BorderLayout.SOUTH);

		return panel;
	}

	// menu
	private JPanel createMenuPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel pnFilter = new JPanel(new BorderLayout());
		ArrayList<LoaiSanPham> dsLSP = (ArrayList<LoaiSanPham>) loaiSP_dao.layTatCa();
		String loaiSP = "Tất cả,";
		for (LoaiSanPham lsp : dsLSP) {
			loaiSP += lsp.getTenLoai() + ",";
		}
		loaiSP = (String) loaiSP.substring(0, loaiSP.length());
		String[] cbLoai = loaiSP.split(",");
		cbFilter = new JComboBox<>(cbLoai);
		pnFilterLelf = new JPanel();
		pnFilterLelf.add(cbFilter);
		pnFilter.add(pnFilterLelf, BorderLayout.WEST);

		pnFilterRight = new JPanel();
		pnFilterRight.add(new JLabel("Tìm kiếm:"));
		txtSearch = new JTextField(15);
		pnFilterRight.add(txtSearch);
		pnFilterRight.add(btnSearch = new JButton("Tìm"));
		pnFilter.add(pnFilterRight, BorderLayout.EAST);

		panel.add(pnFilter, BorderLayout.NORTH);

		btnSearch.addActionListener(this);
		String[] menuCols = { "Hình ảnh", "Tên sản phẩm", "Đơn vị tính", "Loại", "Giá" };
		menuModel = new DefaultTableModel(menuCols, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// TODO Auto-generated method stub
				if (columnIndex == 0) {
					return Icon.class;
				}
				return super.getColumnClass(columnIndex);
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		menuTable = new JTable(menuModel);
		menuTable.setRowHeight(50);
		menuTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		DefaultTableCellRenderer contenntRender = new DefaultTableCellRenderer();
		contenntRender.setHorizontalAlignment(JLabel.CENTER);
		menuTable.setDefaultRenderer(Object.class, contenntRender);
		// tạo filter cho Jtable
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(menuModel);
		menuTable.setRowSorter(sorter);
		panel.add(new JScrollPane(menuTable), BorderLayout.CENTER);

		JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnDat = new JButton("Đặt");
		pnSouth.add(btnDat);
		panel.add(pnSouth, BorderLayout.SOUTH);

		loadMenuData();
		return panel;
	}

	private ImageIcon editAnhSanPham(String imgPath, int width, int height) {
		try {
			java.net.URL imgURL = getClass().getResource(imgPath);
			if (imgURL == null) {
				System.err.println("Không tìm thấy file" + imgPath);
				return null;
			}

			ImageIcon anhGoc = new ImageIcon(imgURL);
			Image img = anhGoc.getImage();

			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bi.createGraphics();

			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g2d.drawImage(img, 0, 0, width, height, null);
			g2d.dispose();
			return new ImageIcon(bi);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	private void loadMenuData() {
		ArrayList<SanPham> dsSP = (ArrayList<SanPham>) sp_dao.layTatCa();
		menuModel.setRowCount(0);
		@SuppressWarnings("unchecked")
		TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) menuTable.getRowSorter();
		if (sorter != null) {
			sorter.setRowFilter(null);
		}
		for (SanPham sp : dsSP) {
			String imgPath = "/img/" + sp.gethinhAnh().trim();
			ImageIcon icon = editAnhSanPham(imgPath, 50, 45);
			LoaiSanPham lsp = sp.getLoaiSP();
			String tenlsp;
			if (lsp == null)
				tenlsp = "";
			else {
				tenlsp = lsp.getTenLoai();
			}

			menuModel.addRow(new Object[] { icon, sp.getTenSanPham(), sp.getDonViTinh(), tenlsp, sp.getGia() });
		}
	}

	private void refreshOrderTable() {
		TableModelListener[] listeners = orderModel.getTableModelListeners();
		for (TableModelListener l : listeners) {
			orderModel.removeTableModelListener(l);
		}

		orderModel.setRowCount(0);

		List<ChiTietHoaDon> dsChiTiet = hoaDonHienTai.getDsChiTiet();
		for (ChiTietHoaDon ct : dsChiTiet) {
			SanPham sp = ct.getSanPham();
			orderModel.addRow(new Object[] { sp.getTenSanPham(), ct.getSoLuong(), ct.getDonGia().doubleValue(),
					ct.tinhThanhTien().doubleValue() });
		}

		for (TableModelListener l : listeners) {
			orderModel.addTableModelListener(l);
			orderTable.revalidate();
			orderTable.repaint();
		}
		updateTotalOrder();
	}

	private void updateTotalOrder() {
		BigDecimal total = hoaDonHienTai.tinhTongTien();
		lblTotalOrder.setText(String.format("Tổng tiền: %,.0f VND", total.doubleValue()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == btnDat) {
			int selectedRow = menuTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn một món trong Menu trước.");
				return;
			}
			int selectedRowInModel = menuTable.convertRowIndexToModel(selectedRow);

			String tenSP = menuModel.getValueAt(selectedRowInModel, 1).toString().trim();

			SanPham sp = sp_dao.timSanPhamTheoTen(tenSP);

			if (sp == null) {
				JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy sản phẩm '" + tenSP + "' trong CSDL.");
				return;
			}

			hoaDonHienTai.themChiTiet(sp, 1);
			refreshOrderTable();

		} else if (o == btnTrangChu) {
			mainFrame.switchToPanel(MainFrame.KEY_DAT_BAN);

		} else if (o == btnXoa) {
			int[] selected_rows = orderTable.getSelectedRows();
			if (selected_rows.length == 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết để xóa");
				return;
			}

			ArrayList<String> tenSanPhamCanXoa = new ArrayList<>();
			for (int i = selected_rows.length - 1; i >= 0; i--) {
				int row_index = selected_rows[i];
				String tenSP = orderModel.getValueAt(row_index, COL_TEN_SP).toString();
				tenSanPhamCanXoa.add(tenSP);
			}

			for (String tenSP : tenSanPhamCanXoa) {
				SanPham sp = sp_dao.timSanPhamTheoTen(tenSP);
				if (sp != null) {
					hoaDonHienTai.xoaChiTiet(sp.getMaSanPham());
				}
			}

			refreshOrderTable();

		} else if (o == cbFilter) {
			actionFilter();
		} else if (o == btnTaoHoaDon) {
			mainFrame.setTrangThaiHoaDon(true);
			taoHoaDon();
		} else if (o == btnSearch) {
			actionSearch();
		}
	}

	private void actionSearch() {
		String strSearch = txtSearch.getText().trim();
		if (strSearch.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa nhập tên sản phẩm");
			return;
		}

		@SuppressWarnings("unchecked")
		TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) menuTable.getRowSorter();

		if (sorter == null) {
			sorter = new TableRowSorter<>(menuModel);
			menuTable.setRowSorter(sorter);
		}
		RowFilter<TableModel, Integer> searchFilter = new RowFilter<TableModel, Integer>() {
			@Override
			public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
				String tenSP = entry.getStringValue(1);
				return tenSP.toLowerCase().contains(strSearch.toLowerCase());
			}
		};

		sorter.setRowFilter(searchFilter);

		if (menuTable.getRowCount() > 0) {
			int viewIndex = 0;
			menuTable.setRowSelectionInterval(viewIndex, viewIndex);
			menuTable.scrollRectToVisible(menuTable.getCellRect(viewIndex, 0, true));
		} else {
			JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào có tên '" + strSearch + "'", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);

			sorter.setRowFilter(null);
			txtSearch.setText("");
			menuTable.clearSelection();
		}
	}

	private void taoHoaDon() {
		if (hoaDonHienTai.getDsChiTiet().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa chọn sản phẩm nào");
			return;
		}

		hoaDonHienTai.setGhiChu(txtGhiChu.getText().trim());

		mainFrame.chuyenHoaDonSangManHinhThanhToan(hoaDonHienTai);

		mainFrame.switchToPanel(mainFrame.KEY_HOA_DON);

		this.hoaDonHienTai = new HoaDon();
		this.hoaDonHienTai.setNgayTao(LocalDate.now());
		this.hoaDonHienTai.setTrangThaiThanhToan(0);

		refreshOrderTable();
		txtGhiChu.setText("");
	}

	private void actionFilter() {
		// TODO Auto-generated method stub
		menuTable.clearSelection(); // làm mới dòng được chọn
		@SuppressWarnings("unchecked")
		TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) menuTable.getRowSorter();
		RowFilter<DefaultTableModel, Object> rf = null;
		String filter = (String) cbFilter.getSelectedItem();
		int filterColumn = 3;
		if (filter.equals("Tất cả")) {
			sorter.setRowFilter(null);
			return;
		}
		try {
			rf = RowFilter.regexFilter(filter, filterColumn);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		sorter.setRowFilter(rf);
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
		loadMenuData();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}