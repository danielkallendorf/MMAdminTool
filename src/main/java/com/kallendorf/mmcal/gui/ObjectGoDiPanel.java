package com.kallendorf.mmcal.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.kallendorf.mmcal.data.ObjectDienst;
import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.TemplateGoDi;
import com.kallendorf.mmcal.templates.TemplatePool;
import com.kallendorf.mmcal.templates.XmlTemplateEditWindowGoDi;

public class ObjectGoDiPanel extends AbstractHolderPanelComponent<ObjectGoDi> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5560584803369806544L;
	private JTextField textFieldName;
	private DynamicComboBox<TemplateGoDi> comboBox;
	private JSpinner spinnerDate;
	private JLabel lblWeekDay;
	private AbstractHolderPanel<ObjectDienstPanel, ObjectDienst> panelHold;
	private JButton btnEdit;
	private JTextField txtDescr;
	private JButton btnDel;

	public ObjectGoDiPanel(ObjectGoDi objectGoDi) {
		this();
		set(objectGoDi);
	}

	public ObjectGoDiPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		// TODO: Erstauswahl
		comboBox = new DynamicComboBox<TemplateGoDi>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<TemplateGoDi> getDataSet() {
				return new ArrayList<TemplateGoDi>(TemplatePool.localPool.goDis.values());
			}
		};
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					TemplateGoDi t = (TemplateGoDi) e.getItem();
					ObjectGoDi o = new ObjectGoDi(t);
					ObjectGoDiPanel.this.onLoad(o);
				}
			}
		});

		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		add(comboBox, gbc_comboBox);
		
		btnDel = createDeleteButton("X");
		GridBagConstraints gbc_btnDel = new GridBagConstraints();
		gbc_btnDel.fill = GridBagConstraints.BOTH;
		gbc_btnDel.gridheight = 3;
		gbc_btnDel.insets = new Insets(0, 0, 5, 0);
		gbc_btnDel.gridx = 2;
		gbc_btnDel.gridy = 0;
		add(btnDel, gbc_btnDel);

		textFieldName = new JTextField();
		GridBagConstraints gbc_textFieldName = new GridBagConstraints();
		gbc_textFieldName.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldName.gridx = 0;
		gbc_textFieldName.gridy = 1;
		add(textFieldName, gbc_textFieldName);
		textFieldName.setColumns(10);

		panelHold = new AbstractHolderPanel<ObjectDienstPanel, ObjectDienst>() {

			private static final long serialVersionUID = 1L;

			@Override
			public ObjectDienstPanel getDefItem() {
				return new ObjectDienstPanel();
			}
		};
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridheight = 7;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		add(panelHold, gbc_panel);

		spinnerDate = new JSpinner();
		spinnerDate.setModel(new SpinnerDateModel(Calendar.getInstance().getTime(), null, null, Calendar.MILLISECOND));
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "dd.MM.yyyy HH:mm"));

		GridBagConstraints gbc_spinnerDate = new GridBagConstraints();
		gbc_spinnerDate.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerDate.gridx = 0;
		gbc_spinnerDate.gridy = 2;
		add(spinnerDate, gbc_spinnerDate);

		lblWeekDay = new JLabel("tag");

		GridBagConstraints gbc_lblWeekDay = new GridBagConstraints();
		gbc_lblWeekDay.insets = new Insets(0, 0, 5, 5);
		gbc_lblWeekDay.gridx = 0;
		gbc_lblWeekDay.gridy = 3;
		add(lblWeekDay, gbc_lblWeekDay);

		spinnerDate.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Formatter f = new Formatter();
				f.format("%tA", spinnerDate.getValue());
				lblWeekDay.setText(f.toString());
				f.close();
			}
		});

		btnEdit = new JButton("Eigenschaften");
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TemplateGoDi old = (TemplateGoDi) comboBox.getSelectedItem();
				TemplateGoDi t = new XmlTemplateEditWindowGoDi().edit(old);
				if (old.equals(t)) {
					return;
				} else {
					t.setListName(t.getListName() + "*");
				}
				comboBox.setDefGenerator(() -> t);
				comboBox.setSelectedIndex(0);
			}
		});
		GridBagConstraints gbc_btnEigenschaften = new GridBagConstraints();
		gbc_btnEigenschaften.insets = new Insets(0, 0, 5, 5);
		gbc_btnEigenschaften.gridx = 0;
		gbc_btnEigenschaften.gridy = 4;
		add(btnEdit, gbc_btnEigenschaften);
		btnEdit.setToolTipText("Dauer: " + comboBox.getSelectedItem());

		txtDescr = new JTextField();
		txtDescr.setToolTipText("Alternativtext");
		GridBagConstraints gbc_txtDescr = new GridBagConstraints();
		gbc_txtDescr.insets = new Insets(0, 0, 5, 5);
		gbc_txtDescr.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDescr.gridx = 0;
		gbc_txtDescr.gridy = 5;
		add(txtDescr, gbc_txtDescr);
		txtDescr.setColumns(10);
		txtDescr.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				ObjectGoDiPanel.this.requestFocus();
				JFrame txtFrame = new JFrame();
				txtFrame.setUndecorated(true);
				txtFrame.setLocation(txtDescr.getLocationOnScreen());
				txtFrame.setSize(500, 300);
				JTextArea txt = new JTextArea(txtDescr.getText());
				txtFrame.getContentPane().add(txt);
				txt.setCaretPosition(txt.getText().length());
				txtFrame.addWindowFocusListener(new WindowAdapter() {

					@Override
					public void windowLostFocus(WindowEvent e) {
						txtDescr.setText(txt.getText());
						txtFrame.dispose();
					}
				});
				txtFrame.setVisible(true);
			}
		});
		txtDescr.setEnabled(true);
	}

	public void set(ObjectGoDi o) {
		TemplateGoDi t = TemplatePool.localPool.goDis.get(o.getListName());
		comboBox.setDefGenerator(() -> t != null ? t : o.extractTemplate());
		comboBox.setSelectedIndex(0);
		onLoad(o);
	}

	private void onLoad(ObjectGoDi o) {
		textFieldName.setText(o.getDisplayName());
		
		Date date = Date.from(o.getStart().atZone(ZoneId.systemDefault()).toInstant());
		spinnerDate.setValue(date);

		panelHold.removeAllPanelComponents();
		o.getDienste().stream().forEachOrdered(p -> {
			ObjectDienstPanel op = new ObjectDienstPanel();
			op.set(p);
			panelHold.addItem(op);
		});
		panelHold.revalidate();
	}

	@Override
	public ObjectGoDi get() {
		TemplateGoDi t = (TemplateGoDi) comboBox.getSelectedItem();

		ObjectGoDi o = new ObjectGoDi(t);
		Date d = (Date) spinnerDate.getValue();
		LocalDateTime ldt = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		o.setStart(ldt);
		o.setDuration(t.getDuration());
		o.setDienste(panelHold.generateItems());
		String txt = txtDescr.getText();
		if (txt.equals("") || !txtDescr.isEnabled())
			txt = null;
		o.setDescriptionText(txt);
		return o;
	}

}
