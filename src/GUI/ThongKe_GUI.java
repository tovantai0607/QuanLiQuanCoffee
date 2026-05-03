package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.math.BigDecimal;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import DAO.ThongKe_DAO;
import Entity.SanPham;
public class ThongKe_GUI extends JPanel implements ActionListener, ComponentListener {
	private MainFrame mainFrame;

	private String[] cols = { "Sản phẩm", "Số lượng bán ra", "Doanh thu" };
	private JTable table;
	private DefaultTableModel tableModel;
	private ThongKe_DAO tk_dao;
	private JButton btnTrangChu;
	private JLabel lb1;
	private JLabel lb2;
	private JLabel lb3;
	private JLabel ansLb3;
	private JPanel pnlb2;
	private JLabel ansLb2;
	private JLabel ansLb1;

	public ThongKe_GUI(MainFrame mainFrame) {
		// TODO Auto-generated constructor stub
		this.mainFrame = mainFrame;
		tk_dao = new ThongKe_DAO();

		setLayout(new BorderLayout());

		JPanel pnNorth = new JPanel();
		JLabel title = new JLabel("THỐNG KÊ");
		title.setFont(new Font("Arial", Font.BOLD, 26));
		pnNorth.add(title);
		add(pnNorth, BorderLayout.NORTH);

		JPanel pnCenter = new JPanel();
		JSplitPane pnSplit = new JSplitPane();
		JPanel pnLeft = new JPanel();
		pnLeft.setPreferredSize(new Dimension(400, 500));
		JPanel pnRight = new JPanel();
		pnRight.setPreferredSize(new Dimension(400, 500));

		pnSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnLeft, pnRight);

		// left
		pnLeft.setLayout(new BorderLayout());
		JLabel titleLeft = new JLabel("Tổng quan trong tháng này");
		titleLeft.setFont(new Font("Arial", Font.BOLD, 20));
		JPanel pnTitleLeft = new JPanel();
		pnTitleLeft.add(titleLeft);
		pnLeft.add(pnTitleLeft, BorderLayout.NORTH);

		JPanel pnCenterLeft = new JPanel();
		pnCenterLeft.setLayout(new GridLayout(3, 1));

		lb1 = new JLabel("Tổng doanh thu: ");
		JPanel pnlb1 = new JPanel();
		pnlb1.add(lb1);
		lb1.setFont(new Font("Arial", Font.ITALIC, 20));

		lb2 = new JLabel("Tổng số lượng sản phẩm bán ra:");
		pnlb2 = new JPanel();
		pnlb2.add(lb2);
		lb2.setFont(new Font("Arial", Font.ITALIC, 20));

		lb3 = new JLabel("Sản phẩm bán chạy nhất: ");
		JPanel pnlb3 = new JPanel();
		pnlb3.add(lb3);
		lb3.setFont(new Font("Arial", Font.ITALIC, 20));

		ansLb1 = new JLabel("");
		ansLb1.setFont(new Font("Arial", Font.BOLD, 18));
		ansLb1.setForeground(Color.RED);
		JPanel pnAns1 = new JPanel();
		pnAns1.add(ansLb1);

		ansLb2 = new JLabel();
		ansLb2.setFont(new Font("Arial", Font.BOLD, 18));
		ansLb2.setForeground(Color.RED);
		JPanel pnAns2 = new JPanel();
		pnAns2.add(ansLb2);

		ansLb3 = new JLabel();
		ansLb3.setFont(new Font("Arial", Font.BOLD, 18));
		ansLb3.setForeground(Color.RED);
		JPanel pnAns3 = new JPanel();
		pnAns3.add(ansLb3);

		JPanel row1 = new JPanel();
		row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
		row1.add(pnlb1);
		row1.add(pnAns1);

		JPanel row2 = new JPanel();
		row2.setLayout(new BoxLayout(row2, BoxLayout.Y_AXIS));
		row2.add(pnlb2);
		row2.add(pnAns2);

		JPanel row3 = new JPanel();
		row3.setLayout(new BoxLayout(row3, BoxLayout.Y_AXIS));
		row3.add(pnlb3);
		row3.add(pnAns3);

		pnCenterLeft.add(row1);
		row1.setBorder(new LineBorder(Color.BLACK, 1));

		pnCenterLeft.add(row2);
		row2.setBorder(new LineBorder(Color.BLACK, 1));

		pnCenterLeft.add(row3);
		row3.setBorder(new LineBorder(Color.BLACK, 1));

		pnLeft.add(pnCenterLeft, BorderLayout.CENTER);

		// right
		pnRight.setLayout(new BorderLayout());
		JLabel titleRight = new JLabel("Danh sách 10 sản phẩm bán chạy");
		titleRight.setFont(new Font("Arial", Font.BOLD, 20));
		JPanel pnTitleRight = new JPanel();
		pnTitleRight.add(titleRight);

		pnRight.add(pnTitleRight, BorderLayout.NORTH);

		tableModel = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		table = new JTable(tableModel);
		JScrollPane pnScroll = new JScrollPane(table);

		pnRight.add(pnScroll);

		pnCenter.add(pnSplit);
		add(pnCenter, BorderLayout.CENTER);

		JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnSouth.add(btnTrangChu = new JButton("Trang chủ"));
		add(pnSouth, BorderLayout.SOUTH);

		// actionListerner
		addComponentListener(this);
		btnTrangChu.addActionListener(this);
		refreshData();
	}

	public void loadTable() {
		Map<SanPham, Integer> dsSP = tk_dao.get10SanPhamBanChayNhat();
		for (Map.Entry<SanPham, Integer> entry : dsSP.entrySet()) {

			SanPham sp = entry.getKey();
			Integer soLuong = entry.getValue();
			if (sp != null) {
				BigDecimal doanhThu = sp.getGia().multiply(BigDecimal.valueOf(soLuong));
				tableModel.addRow(new Object[] { sp.getTenSanPham(), soLuong, doanhThu });
			} else {
				System.out.println("Sản phẩm null");
			}
		}
	}

	private void refreshData() {
		tableModel.setRowCount(0);
		ansLb1.setText(String.valueOf(tk_dao.tongDoanhThu()));
		ansLb2.setText(String.valueOf(tk_dao.soLuongSP()));
		ansLb3.setText(tk_dao.sanPhamBanChayNhat());
		loadTable();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o == btnTrangChu) {
			mainFrame.switchToPanel(mainFrame.KEY_DAT_BAN);
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
		refreshData();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
