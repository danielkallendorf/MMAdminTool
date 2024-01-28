package com.kallendorf.mmcal.templates;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import com.kallendorf.mmcal.data.TemplateDienst;
import com.kallendorf.mmcal.data.TemplateGoDi;
import com.kallendorf.mmcal.gui.AbstractHolderPanelComponent;
import com.kallendorf.mmcal.gui.DurationSpinner;
import com.kallendorf.mmcal.gui.DynamicComboBox;
import com.kallendorf.mmcal.gui.AbstractHolderPanel;

public class XmlTemplateEditWindowGoDi extends XmlTemplateEditWindow<TemplateGoDi> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -621062776410066185L;
	private JTextField textListName;
	private JTextField textDisplayName;
	private JToggleButton tglbtnBeschreibung;
	private DurationSpinner spinnerDuration;
	private JCheckBox checkboxTime;
	private JSpinner spinnerStartTime;
	private JCheckBox checkBoxDay;
	private JSpinner spinnerDay;
	private AbstractHolderPanel<AbstractHolderPanelComponent<TemplateDienst>, TemplateDienst> panelHolder;
	private JTextPane textPaneDescr;

	@SuppressWarnings("serial")
	public XmlTemplateEditWindowGoDi() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_details.setLayout(gridBagLayout);
		
		JLabel lblListenname = new JLabel("ListenName");
		GridBagConstraints gbc_lblListenname = new GridBagConstraints();
		gbc_lblListenname.insets = new Insets(0, 0, 5, 5);
		gbc_lblListenname.gridx = 0;
		gbc_lblListenname.gridy = 0;
		panel_details.add(lblListenname, gbc_lblListenname);
		
		textListName = new JTextField();
		GridBagConstraints gbc_textListName = new GridBagConstraints();
		gbc_textListName.insets = new Insets(0, 0, 5, 5);
		gbc_textListName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textListName.gridx = 1;
		gbc_textListName.gridy = 0;
		panel_details.add(textListName, gbc_textListName);
		
		textListName.setColumns(10);
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		panel_details.add(lblName, gbc_lblName);
		
		textDisplayName = new JTextField();
		GridBagConstraints gbc_textName = new GridBagConstraints();
		gbc_textName.insets = new Insets(0, 0, 5, 5);
		gbc_textName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textName.gridx = 1;
		gbc_textName.gridy = 1;
		panel_details.add(textDisplayName, gbc_textName);
		
		textDisplayName.setColumns(10);
		JLabel lblDay = new JLabel("Wochentag");
		GridBagConstraints gbc_lblDay = new GridBagConstraints();
		gbc_lblDay.insets = new Insets(0, 0, 5, 5);
		gbc_lblDay.gridx = 0;
		gbc_lblDay.gridy = 2;
		panel_details.add(lblDay, gbc_lblDay);
		
		spinnerDay = new JSpinner();
		spinnerDay.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		DateEditor editSDay = new DateEditor(spinnerDay, "EEEE");
		spinnerDay.setEditor(editSDay);
		spinnerDay.setEnabled(false);
		GridBagConstraints gbc_spinnerDay = new GridBagConstraints();
		gbc_spinnerDay.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerDay.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerDay.gridx = 1;
		gbc_spinnerDay.gridy = 2;
		panel_details.add(spinnerDay, gbc_spinnerDay);
		
		checkBoxDay = new JCheckBox("");
		checkBoxDay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				spinnerDay.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		GridBagConstraints gbc_checkBoxDay = new GridBagConstraints();
		gbc_checkBoxDay.insets = new Insets(0, 0, 5, 5);
		gbc_checkBoxDay.gridx = 2;
		gbc_checkBoxDay.gridy = 2;
		panel_details.add(checkBoxDay, gbc_checkBoxDay);
		
		JLabel lblStartTime = new JLabel("Uhrzeit");
		GridBagConstraints gbc_lblTime = new GridBagConstraints();
		gbc_lblTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblTime.gridx = 0;
		gbc_lblTime.gridy = 3;
		panel_details.add(lblStartTime, gbc_lblTime);
		
		spinnerStartTime = new JSpinner();
		spinnerStartTime.setModel(new SpinnerDateModel());
		DateEditor editSST = new DateEditor(spinnerStartTime, "H:mm");
		spinnerStartTime.setEditor(editSST);
		spinnerStartTime.setEnabled(false);
		GridBagConstraints gbc_spinnerTime = new GridBagConstraints();
		gbc_spinnerTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerTime.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTime.gridx = 1;
		gbc_spinnerTime.gridy = 3;
		panel_details.add(spinnerStartTime, gbc_spinnerTime);
		
		checkboxTime = new JCheckBox("");
		checkboxTime.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				spinnerStartTime.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		GridBagConstraints gbc_checkboxTime = new GridBagConstraints();
		gbc_checkboxTime.insets = new Insets(0, 0, 5, 5);
		gbc_checkboxTime.gridx = 2;
		gbc_checkboxTime.gridy = 3;
		panel_details.add(checkboxTime, gbc_checkboxTime);
		
		JLabel lblDuration = new JLabel("Dauer");
		GridBagConstraints gbc_lblDuration = new GridBagConstraints();
		gbc_lblDuration.insets = new Insets(0, 0, 5, 5);
		gbc_lblDuration.gridx = 0;
		gbc_lblDuration.gridy = 4;
		panel_details.add(lblDuration, gbc_lblDuration);
		
		spinnerDuration = new DurationSpinner(Duration.ofHours(1), Duration.ZERO, Duration.ofDays(1).minusMinutes(1));
		GridBagConstraints gbc_spinnerDuration = new GridBagConstraints();
		gbc_spinnerDuration.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerDuration.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerDuration.gridx = 1;
		gbc_spinnerDuration.gridy = 4;
		panel_details.add(spinnerDuration, gbc_spinnerDuration);
		
		tglbtnBeschreibung = new JToggleButton("Beschreibung");
		tglbtnBeschreibung.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				textPaneDescr.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		GridBagConstraints gbc_tglbtnBeschreibung = new GridBagConstraints();
		gbc_tglbtnBeschreibung.gridwidth = 2;
		gbc_tglbtnBeschreibung.insets = new Insets(0, 0, 5, 5);
		gbc_tglbtnBeschreibung.gridx = 0;
		gbc_tglbtnBeschreibung.gridy = 5;
		panel_details.add(tglbtnBeschreibung, gbc_tglbtnBeschreibung);
		
		textPaneDescr = new JTextPane();
		textPaneDescr.setEnabled(false);
		GridBagConstraints gbc_textPaneDescr = new GridBagConstraints();
		gbc_textPaneDescr.gridwidth = 2;
		gbc_textPaneDescr.insets = new Insets(0, 0, 0, 5);
		gbc_textPaneDescr.fill = GridBagConstraints.BOTH;
		gbc_textPaneDescr.gridx = 0;
		gbc_textPaneDescr.gridy = 6;
		panel_details.add(textPaneDescr, gbc_textPaneDescr);
		panelHolder = new AbstractHolderPanel<AbstractHolderPanelComponent<TemplateDienst>, TemplateDienst>() {
			@Override
			public AbstractHolderPanelComponent<TemplateDienst> getDefItem() {
				return new DienstTemplateComponent();
			}
		};
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 7;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 0;
		panel_details.add(panelHolder, gbc_panel);
		setVisible(true);
	}

	@Override
	public void onItemLoad(TemplateGoDi item) {
		if (item == null)
			item = new TemplateGoDi().setDisplayName("").setListName("");
		textListName.setText(item.getListName());
		textDisplayName.setText(item.getDisplayName());
		spinnerDuration.setDuration(item.getDuration());
		if (item.getStartDay() == null)
			checkBoxDay.setSelected(false);
		else {
			checkBoxDay.setSelected(true);
			LocalDateTime ldt = LocalDateTime.now().with(item.getStartDay());
			Date d = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			spinnerDay.setValue(d);
		}
		if (item.getStartTime() == null)
			checkboxTime.setSelected(false);
		else {
			checkboxTime.setSelected(true);
			LocalDateTime atDate = item.getStartTime().atDate(LocalDate.now());
			Instant instant = atDate.atZone(ZoneId.systemDefault()).toInstant();
			Date date = Date.from(instant);
			spinnerStartTime.setValue(date);
		}
		panelHolder.removeAllPanelComponents();
		List<TemplateDienst> l = item.getDienste();
		for (TemplateDienst templateDienst : l) {
			panelHolder.addItem(new DienstTemplateComponent(templateDienst));
		}
	}

	@Override
	public Map<String, TemplateGoDi> getItemsMap() {
		return TemplatePool.localPool.goDis;
	}

	@Override
	public String getKeyVal() {
		return textListName.getText();
	}

	@Override
	public TemplateGoDi getItemFromFields() {
		TemplateGoDi t = new TemplateGoDi().setListName(textListName.getText()).setDisplayName(textDisplayName.getText());
		t.setDienste(panelHolder.generateItems());
		if (tglbtnBeschreibung.isSelected())
			t.setDescription(textPaneDescr.getText());
		if (checkboxTime.isSelected()) {
			SpinnerDateModel mod = (SpinnerDateModel) spinnerStartTime.getModel();
			// TODO Check for correct implementation
			ZonedDateTime atZone = mod.getDate().toInstant().atZone(ZoneId.systemDefault());
			LocalTime d = LocalTime.from(atZone);
			t.setStartTime(d);
		}
		if (checkBoxDay.isSelected()) {
			Instant i = ((SpinnerDateModel) spinnerDay.getModel()).getDate().toInstant();
			LocalDate ld = LocalDate.from(i.atZone(ZoneId.systemDefault()));
			DayOfWeek d = ld.getDayOfWeek();
			t.setStartDay(d);
		}
		t.setDuration(spinnerDuration.getDuration());
		return t;
	}
	
	@Override
	protected void lockHead() {
		spinnerStartTime.setEnabled(false);
		textListName.setEnabled(false);
		checkBoxDay.setEnabled(false);
		checkboxTime.setEnabled(false);
		super.lockHead();
	}

	@SuppressWarnings({ "serial" })
	public class DienstTemplateComponent extends AbstractHolderPanelComponent<TemplateDienst> {
		private JSpinner spinner;
		private JComboBox<String> comboBox;

		public DienstTemplateComponent() {
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
			gridBagLayout.rowHeights = new int[] { 0, 0 };
			gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
			gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			setLayout(gridBagLayout);
			comboBox = new DynamicComboBox<String>() {
				@Override
				public List<String> getDataSet() {
					return new ArrayList<>(TemplatePool.localPool.dienste.keySet());
				}
			};
			comboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						TemplateDienst t = TemplatePool.localPool.dienste.get(e.getItem());
						spinner.setValue(t.getSize());
					} else {
						if (!TemplatePool.localPool.dienste.containsKey(e.getItem())) {
							comboBox.removeItem(e.getItem());
						}
					}
				}
			});
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.insets = new Insets(0, 0, 0, 5);
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = 0;
			add(comboBox, gbc_comboBox);
			spinner = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
			GridBagConstraints gbc_spinner = new GridBagConstraints();
			gbc_spinner.insets = new Insets(0, 0, 0, 5);
			gbc_spinner.gridx = 1;
			gbc_spinner.gridy = 0;
			add(spinner, gbc_spinner);
			JButton btnDel = createDeleteButton("X");
			GridBagConstraints gbc_btnDel = new GridBagConstraints();
			gbc_btnDel.gridx = 2;
			gbc_btnDel.gridy = 0;
			add(btnDel, gbc_btnDel);
		}

		public DienstTemplateComponent(TemplateDienst item) {
			this();
			comboBox.setEditable(true);
			comboBox.setSelectedItem(item.getListName());
			comboBox.setEditable(false);
			spinner.setValue(item.getSize());
		}

		@Override
		public TemplateDienst get() {
			Number n = (Number) spinner.getValue();
			return TemplatePool.localPool.dienste.get(comboBox.getSelectedItem()).setSize(n.intValue());
		}
	}
}
