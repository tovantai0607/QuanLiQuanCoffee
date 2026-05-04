package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import DAO.Ban_DAO;
import Entity.Ban;
import java.time.LocalDate;
import java.time.LocalTime;

import DAO.PhieuDatBan_DAO;
import DAO.KhachHang_DAO;
import DAO.NhanVien_DAO;

import Entity.PhieuDatBan;
import Entity.ChiTietDatBan;
import Entity.KhachHang;
import Entity.NhanVien;

public class ChonBan_GUI extends JPanel implements ActionListener, ComponentListener {
	private MainFrame mainFrame;
	private JLabel title;
	private JComboBox<String> cbTrangThaiBan;
	private JButton btnChonBan, btnQuayLai;
	private Ban_DAO ban_dao;
	private PhieuDatBan_DAO pdb_dao;
	private KhachHang_DAO kh_dao;
	private NhanVien_DAO nv_dao;
	private ArrayList<JButton> tableButtons;
	private JPanel pnTableDisplay;
	private Ban ban_selected;
	private JButton btnLamTrong;

	
	private static final Color COLOR_TRONG = new Color(144, 238, 144);
	private static final Color COLOR_DA_DAT = new Color(211, 211, 211);
	private static final Color COLOR_DANG_PHUC_VU = new Color(135, 206, 250);
	private static final Color COLOR_SELECTED = new Color(255, 165, 0);

	public ChonBan_GUI(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		ban_dao = new Ban_DAO();
		pdb_dao = new PhieuDatBan_DAO();
		kh_dao = new KhachHang_DAO();
		nv_dao = new NhanVien_DAO();
		tableButtons = new ArrayList<>();

		setLayout(new BorderLayout(10, 10));

		// north
		JPanel pnNorth = new JPanel(new BorderLayout());
		title = new JLabel("CHỌN BÀN", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		pnNorth.add(title, BorderLayout.CENTER);
		add(pnNorth, BorderLayout.NORTH);

		// center
		JPanel pnCenter = new JPanel(new BorderLayout(10, 10));

		// filter
		JPanel pnFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		String[] trangThai_cb = { "Tất cả", "Trống", "Đã đặt", "Đang phục vụ" };
		cbTrangThaiBan = new JComboBox<>(trangThai_cb);
		pnFilter.add(new JLabel("Lọc theo trạng thái: "));
		pnFilter.add(cbTrangThaiBan);
		pnCenter.add(pnFilter, BorderLayout.NORTH);

		pnTableDisplay = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
		JScrollPane spTables = new JScrollPane(pnTableDisplay);
		spTables.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		pnCenter.add(spTables, BorderLayout.CENTER);
		add(pnCenter, BorderLayout.CENTER);

		// south
		JPanel pnSouth = new JPanel(new BorderLayout());
		btnQuayLai = new JButton("Quay lại");
		btnChonBan = new JButton("Chọn bàn");
		btnLamTrong = new JButton("Làm trống");
		JPanel pnBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnBtn.add(btnLamTrong);
		pnBtn.add(btnChonBan);
		pnSouth.add(btnQuayLai, BorderLayout.WEST);
		pnSouth.add(pnBtn, BorderLayout.EAST);
		add(pnSouth, BorderLayout.SOUTH);

		cbTrangThaiBan.addActionListener(this);
		btnChonBan.addActionListener(this);
		btnQuayLai.addActionListener(this);
		btnLamTrong.addActionListener(this);
		addComponentListener(this);
		// Load dữ liệu
		loadBanData();
	}

	// action
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == cbTrangThaiBan)
			filterBan();

		else if (o == btnChonBan)
			xuLyChonBan();

		else if (o == btnQuayLai)
			mainFrame.switchToPanel(mainFrame.KEY_DAT_BAN);
		else if (o == btnLamTrong) {
			if (ban_selected == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn");
				return;
			}
			String maBan = ban_selected.getMaBan().trim();
			int hoiNhac = JOptionPane.showConfirmDialog(this, "Chắc chắn làm trống " + maBan + " ?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (hoiNhac == JOptionPane.YES_OPTION) {
				if (ban_dao.capNhatTrangThaiBan(maBan, 0))
					loadBanData();
			}
		}
	}

	private void xuLyChonBan() {
		if (ban_selected == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn!");
			return;
		}

		if (ban_selected.getTrangThai() != 0) {
			JOptionPane.showMessageDialog(this, "Bàn này không thể chọn vì đang bận!");
			return;
		}

		int hoiNhac = JOptionPane.showConfirmDialog(
				this,
				"Chắc chắn chọn Bàn " + ban_selected.getMaBan() + "?",
				"Xác nhận",
				JOptionPane.YES_NO_OPTION
		);

		if (hoiNhac != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			// 1. Nhập mã khách hàng
			String maKH = JOptionPane.showInputDialog(this, "Nhập mã khách hàng:", "KH001");

			if (maKH == null || maKH.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Bạn chưa nhập mã khách hàng!");
				return;
			}

			maKH = maKH.trim();

			KhachHang kh = kh_dao.timKhachHangTheoMaKH(maKH);

			if (kh == null) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có mã: " + maKH);
				return;
			}

			// 2. Lấy nhân viên mặc định
			// Nếu app của bạn có đăng nhập thì sau này nên lấy mã nhân viên đang đăng nhập.
			NhanVien nv = nv_dao.timTheoMa("NV001");

			if (nv == null) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên NV001 trong database!");
				return;
			}

			// 3. Tạo mã phiếu mới
			String maPDB = pdb_dao.taoMaPhieuDatMoi();

			// 4. Tạo phiếu đặt bàn
			LocalDate ngayDat = LocalDate.now();
			LocalTime gioBatDau = LocalTime.now().withSecond(0).withNano(0);
			LocalTime gioKetThuc = gioBatDau.plusHours(2);

			PhieuDatBan pdb = new PhieuDatBan(
					maPDB,
					ngayDat,
					gioBatDau,
					gioKetThuc,
					1,
					"Đặt bàn trực tiếp",
					1,
					kh,
					nv
			);

			// 5. Tạo chi tiết đặt bàn cho bàn vừa chọn
			ChiTietDatBan ct = new ChiTietDatBan(
					pdb,
					ban_selected,
					"Đặt bàn trực tiếp"
			);

			pdb.themChiTiet(ct);

			// 6. Lưu phiếu đặt bàn xuống database
			boolean kq = pdb_dao.themPhieuDatBan(pdb);

			if (!kq) {
				JOptionPane.showMessageDialog(this, "Tạo phiếu đặt bàn thất bại!");
				return;
			}

			// 7. Lưu dữ liệu trung gian vào MainFrame
			String maBan = ban_selected.getMaBan();

			ArrayList<String> dsMaBan = new ArrayList<String>();
			dsMaBan.add(maBan);

			mainFrame.setDsMaBan(dsMaBan);
			mainFrame.setMaPhieuDatBan(maPDB);
			mainFrame.setMaKhachHang(maKH);

			// 8. Cập nhật trạng thái bàn
			ban_dao.capNhatTrangThaiBan(maBan, 1);

			loadBanData();

			JOptionPane.showMessageDialog(this, "Đã tạo phiếu đặt bàn: " + maPDB);

			// 9. Chuyển sang màn hình menu
			mainFrame.switchToPanel(mainFrame.KEY_BAN_HANG);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tạo phiếu đặt bàn: " + e.getMessage());
		}
	}

	private void loadBanData() {
		pnTableDisplay.removeAll();
		tableButtons.clear();
		ban_selected = null;

		ArrayList<Ban> dsBan = (ArrayList<Ban>) ban_dao.layTatCa();
		for (Ban ban : dsBan) {
			JButton btnBan = taoButtonBan(ban);
			pnTableDisplay.add(btnBan);
			tableButtons.add(btnBan);
		}

		pnTableDisplay.revalidate();
		pnTableDisplay.repaint();
	}

	private JButton taoButtonBan(Ban ban) {
		JButton btnBan = new JButton("Bàn " + ban.getMaBan());
		btnBan.setFont(new Font("Arial", Font.BOLD, 14));
		btnBan.setPreferredSize(new Dimension(120, 90));
		btnBan.setOpaque(true);
		btnBan.setBorderPainted(false);

		setBanStyle(btnBan, ban.getTrangThai());
		btnBan.putClientProperty("BanObject", ban);

		btnBan.addActionListener(e -> {
			ban_selected = (Ban) btnBan.getClientProperty("BanObject");
			capNhatMauChon(btnBan);
		});
		return btnBan;
	}

	private void capNhatMauChon(JButton selectedButton) {
		for (JButton btn : tableButtons) {
			Ban ban = (Ban) btn.getClientProperty("BanObject");
			setBanStyle(btn, ban.getTrangThai());
		}
		selectedButton.setBackground(COLOR_SELECTED);
		selectedButton.setForeground(Color.WHITE);
	}

	private void filterBan() {
		String filter = (String) cbTrangThaiBan.getSelectedItem();
		for (JButton btn : tableButtons) {
			Ban ban = (Ban) btn.getClientProperty("BanObject");
			boolean visible = switch (filter) {
			case "Tất cả" -> true;
			case "Trống" -> ban.getTrangThai() == 0;
			case "Đã đặt" -> ban.getTrangThai() == 1;
			case "Đang phục vụ" -> ban.getTrangThai() == 2;
			default -> true;
			};
			setBanStyle(btn, ban.getTrangThai());
			btn.setVisible(visible);
		}
		pnTableDisplay.revalidate();
		pnTableDisplay.repaint();
	}

	private void setBanStyle(JButton btnBan, int trangThai) {
		btnBan.setForeground(Color.BLACK);
		btnBan.setBackground(switch (trangThai) {
		case 0 -> COLOR_TRONG;
		case 1 -> COLOR_DA_DAT;
		case 2 -> COLOR_DANG_PHUC_VU;
		default -> Color.GRAY;
		});
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
		loadBanData();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
