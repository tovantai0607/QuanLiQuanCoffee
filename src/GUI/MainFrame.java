package GUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import DAO.PhieuDatBan_DAO;
import Entity.HoaDon; // === THÊM IMPORT ===
public class MainFrame extends JFrame {

	public static final String KEY_DAT_BAN = "DatBan_GUI.java";

	public static final String KEY_BAN_HANG = "Menu_GUI.java";
	public static final String KEY_SAN_PHAM = "SanPham_GUI.java";
	public static final String KEY_GIAM_GIA = "KhuyenMai_GUI.java";
	public static final String KEY_KHACH_HANG = "KhachHang_GUI.java";
	public static final String KEY_THONG_KE = "ThongKe_GUI.java";
	public static final String KEY_CHON_BAN = "ChonBan_GUI.java";
	public static final String KEY_HOA_DON = "HoaDon_GUI.java";

	private CardLayout cardLayout;
	private JPanel contentPanel;
	private JPanel navPanel;
	private List<JButton> navButtons;
	private HoaDon_GUI hoadon_gui;
	private JButton btnNavHoaDon;
	private PhieuDatBan_DAO pdb_dao;

	// tạo biến tòa cục để lưu trữ maBan và PDB
	private ArrayList<String> dsMaBan;
	private String maPhieuDatBan;
	private String maKhachHang;

	public MainFrame() {
		dsMaBan = new ArrayList<String>();
		this.hoadon_gui = new HoaDon_GUI(this);
		this.pdb_dao = new PhieuDatBan_DAO();
		setTitle("Quản Lý Quán Coffee");
		setSize(1000, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new BorderLayout());

		// thanh điều hướng
		navPanel = new JPanel();
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		navPanel.setBackground(new Color(230, 240, 255));
		navPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		navButtons = new ArrayList<>();

		cardLayout = new CardLayout();
		contentPanel = new JPanel(cardLayout);

		ActionListener navListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = e.getActionCommand();
				cardLayout.show(contentPanel, key);
				updateNavButtonStyles((JButton) e.getSource());
			}
		};

		// cập nhật db
		pdb_dao.capNhatTranThaiTuDong();

		DatBan_GUI panelDatBan = new DatBan_GUI(this);
		contentPanel.add(panelDatBan, KEY_DAT_BAN);

		ChonBan_GUI panelChonBan = new ChonBan_GUI(this);
		contentPanel.add(panelChonBan, KEY_CHON_BAN);

		Menu_GUI panelBanHang = new Menu_GUI(this);
		contentPanel.add(panelBanHang, KEY_BAN_HANG);

		SanPham_GUI panelSanPham = new SanPham_GUI(this);
		contentPanel.add(panelSanPham, KEY_SAN_PHAM);

		KhachHang_GUI panelKhachHang = new KhachHang_GUI(this);
		contentPanel.add(panelKhachHang, KEY_KHACH_HANG);

		KhuyenMai_GUI panelKhuyenMai = new KhuyenMai_GUI(this);
		contentPanel.add(panelKhuyenMai, KEY_GIAM_GIA);

		ThongKe_GUI panelThongKe = new ThongKe_GUI(this);
		contentPanel.add(panelThongKe, KEY_THONG_KE);

		contentPanel.add(this.hoadon_gui, KEY_HOA_DON);

		// gán key
		// Thêm các nút vào navPanel
		addNavButton("Đặt bàn", "/img/icon-home.png", KEY_DAT_BAN, navListener);
		addNavButton("Menu", "/img/icon-menu.png", KEY_BAN_HANG, navListener);
		addNavButton("Sản phẩm", "/img/icon-product.png", KEY_SAN_PHAM, navListener);
		addNavButton("Khách hàng", "/img/icon-user.png", KEY_KHACH_HANG, navListener);
		addNavButton("Khuyến mãi", "/img/icon-discount.png", KEY_GIAM_GIA, navListener);
		addNavButton("Thống kê", "/img/icon-statistic.png", KEY_THONG_KE, navListener);
		btnNavHoaDon = addNavButton("Hóa đơn", "/img/icon-bill.png", KEY_HOA_DON, navListener);
		btnNavHoaDon.setVisible(false); // ẩn nav hóa đơn

		add(navPanel, BorderLayout.WEST);
		add(contentPanel, BorderLayout.CENTER);

		cardLayout.show(contentPanel, KEY_DAT_BAN);
		if (!navButtons.isEmpty()) {
			updateNavButtonStyles(navButtons.get(0));
		}

	}

	public ArrayList<String> getDsMaBan() {
		return dsMaBan;
	}

	public void setDsMaBan(ArrayList<String> dsMaBan) {
		this.dsMaBan = dsMaBan;
	}

	public String getMaPhieuDatBan() {
		return maPhieuDatBan;
	}

	public void setMaPhieuDatBan(String maPhieuDatBan) {
		this.maPhieuDatBan = maPhieuDatBan;
	}

	private JButton addNavButton(String text, String iconPath, String actionCommand, ActionListener listener) {
		ImageIcon icon = null;
		try {
			java.net.URL imgURL = getClass().getResource(iconPath);
			if (imgURL != null) {
				icon = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
			} else {
				System.err.println("Không tìm thấy file icon: " + iconPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JButton button = new JButton(text, icon);
		button.setActionCommand(actionCommand);
		button.addActionListener(listener);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		navPanel.add(button);
		navButtons.add(button);
		navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		return button;
	}

	// hàm hiển thị nav hóa đơn nếu hóa đã tạo
	public void setTrangThaiHoaDon(boolean trangThai) {
		btnNavHoaDon.setVisible(trangThai);
	}

	private void updateNavButtonStyles(JButton selectedButton) {
		for (JButton button : navButtons) {
			if (button == selectedButton) {
				button.setContentAreaFilled(true);
				button.setOpaque(true);
				button.setBackground(new Color(190, 215, 240));
				button.setFont(new Font("Arial", Font.BOLD, 14));
				button.setForeground(Color.BLACK);
			} else {
				button.setContentAreaFilled(false);
				button.setOpaque(false);
				button.setFont(new Font("Arial", Font.PLAIN, 14));
				button.setForeground(Color.BLACK);
			}
		}
	}

	public void switchToPanel(String key) {
		cardLayout.show(contentPanel, key);

		for (JButton btn : navButtons) {
			if (btn.getActionCommand().equals(key)) {
				updateNavButtonStyles(btn);
				break;
			}
		}
	}

	public void chuyenHoaDonSangManHinhThanhToan(HoaDon hoaDon) {
		hoadon_gui.setHoaDonHienTai(hoaDon);
	}

	public String getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}

}