package GUI;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class QuantityCellRenderer extends JPanel implements TableCellRenderer {

	private JButton btnMinus;
	private JButton btnPlus;
	private JLabel lblQuantity;

	public QuantityCellRenderer() {
		super(new FlowLayout(FlowLayout.CENTER, 5, 0));
		btnMinus = new JButton("-");
		btnPlus = new JButton("+");
		lblQuantity = new JLabel("1");

		add(btnMinus);
		add(lblQuantity);
		add(btnPlus);

		btnMinus.setEnabled(false);
		btnPlus.setEnabled(false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		lblQuantity.setText(value != null ? value.toString() : "0");

		if (isSelected) {
			setBackground(table.getSelectionBackground());
		} else {
			setBackground(table.getBackground());
		}

		return this;
	}
}
