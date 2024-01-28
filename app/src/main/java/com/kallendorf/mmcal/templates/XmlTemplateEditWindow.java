package com.kallendorf.mmcal.templates;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kallendorf.mmcal.MMAdminMain;
import com.kallendorf.mmcal.gui.DynamicComboBox;

public abstract class XmlTemplateEditWindow<T> extends JFrame {
	private static final long serialVersionUID = 906212868605308340L;
	protected JComboBox<String> comboBox;
	protected JPanel panel_details;
	private JButton btnSave;
	private JButton btnReset;

	public XmlTemplateEditWindow() {
		Rectangle rect;
		try {
			rect = MMAdminMain.gui.getBounds();
		} catch (NullPointerException e) {
			rect = new Rectangle(new Point(100, 100));
		}
		rect.x += 50;
		rect.y += 50;
		rect.width = 500;
		rect.height = 300;
		setBounds(rect);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		JPanel panel_head = new JPanel();
		panel.add(panel_head, BorderLayout.NORTH);
		panel_head.setLayout(new GridLayout(0, 3, 0, 0));
		comboBox = new DynamicComboBox<String>("Neu") {

			private static final long serialVersionUID = 4304405901438324093L;

			@Override
			public List<String> getDataSet() {
				return new ArrayList<String>(getItemsMap().keySet());
			}
		};
		panel_head.add(comboBox);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Map<String, T> itemsMap = getItemsMap();
					T item = itemsMap.get(e.getItem());
					onItemLoad(item);
				}
			}
		});
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onSave();
			}
		});
		panel_head.add(btnSave);
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onItemLoad(getItemsMap().get(comboBox.getSelectedItem()));
			}
		});
		panel_head.add(btnReset);
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		panel_details = new JPanel();
		scrollPane.setViewportView(panel_details);

		setVisible(true);
	}

	public void onSave() {
		String key = getKeyVal();
		if (key.equals(comboBox.getSelectedItem())) {
			getItemsMap().put(key, getItemFromFields());
		} else {
			if (getItemsMap().containsKey(getKeyVal())) {
				JOptionPane.showMessageDialog(MMAdminMain.gui, "Name bereits vergeben", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				getItemsMap().remove(comboBox.getSelectedItem());
				getItemsMap().put(key, getItemFromFields());
			}
		}
		TemplatePoolHandler.storeLocalPool(TemplatePool.localPool);
	}

	public abstract void onItemLoad(T item);

	public abstract Map<String, T> getItemsMap();

	public abstract String getKeyVal();

	public abstract T getItemFromFields();

	protected void lockHead() {
		comboBox.setEnabled(false);
		ActionListener[] alS = btnSave.getActionListeners();
		for (ActionListener actionListener : alS) {
			btnSave.removeActionListener(actionListener);
		}
		ActionListener[] alR = btnReset.getActionListeners();
		for (ActionListener actionListener : alR) {
			btnReset.removeActionListener(actionListener);
		}
	}

	public T edit(T t) {
		onItemLoad(t);
		comboBox.setSelectedItem(getKeyVal() + "*");
		lockHead();
		CountDownLatch c = new CountDownLatch(1);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				c.countDown();
			}
		});

		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.countDown();
			}
		});

		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onItemLoad(t);
			}
		});

		try {
			c.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		return getItemFromFields();
	}
}
