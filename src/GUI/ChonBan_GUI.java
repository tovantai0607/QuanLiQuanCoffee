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

public class ChonBan_GUI extends JPanel implements ActionListener, ComponentListener {
	private MainFrame mainFrame;
	private JLabel title;
	private JComboBox<String> cbTrangThaiBan;
	private JButton btnChonBan, btnQuayLai;
	private Ban_DAO ban_dao;
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

		// trangThai = 0 (trống)
		if (ban_selected.getTrangThai() == 0) {
			int hoiNhac = JOptionPane.showConfirmDialog(this, "Chắc chắn chọn Bàn " + ban_selected.getMaBan() + "?",
					"Xác nhận", JOptionPane.YES_NO_OPTION);
			if (hoiNhac == JOptionPane.YES_OPTION) {
				// lưu mã bàn vào lớp trung gian MainFrame
				String maBan = ban_selected.getMaBan();
				ArrayList<String> dsMaBan = new ArrayList<String>();
				dsMaBan.add(maBan);
				mainFrame.setDsMaBan(dsMaBan);
				ban_dao.capNhatTrangThaiBan(ban_selected.getMaBan(), 1);
				loadBanData();
				mainFrame.switchToPanel(mainFrame.KEY_BAN_HANG);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Bàn này không thể chọn vì đang bận!");
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
