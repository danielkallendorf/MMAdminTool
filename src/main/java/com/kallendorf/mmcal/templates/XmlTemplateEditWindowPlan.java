package com.kallendorf.mmcal.templates;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import com.kallendorf.mmcal.data.TemplateGoDi;
import com.kallendorf.mmcal.data.TemplatePlan;
import com.kallendorf.mmcal.gui.AbstractHolderPanel;
import com.kallendorf.mmcal.gui.AbstractHolderPanelComponent;
import com.kallendorf.mmcal.gui.DynamicComboBox;

public class XmlTemplateEditWindowPlan extends XmlTemplateEditWindow<TemplatePlan> {

	private static final long serialVersionUID = -2386751556914803383L;
	private JTextField txt_InternalName;
	private AbstractHolderPanel<GoDiTemplateComponent, TemplateGoDi> panelHolder;

	@SuppressWarnings("serial")
	public XmlTemplateEditWindowPlan() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel_details.setLayout(gridBagLayout);

		JLabel lblListenname = new JLabel("Listenname");
		GridBagConstraints gbc_lblListenname = new GridBagConstraints();
		gbc_lblListenname.anchor = GridBagConstraints.EAST;
		gbc_lblListenname.insets = new Insets(0, 0, 5, 5);
		gbc_lblListenname.gridx = 0;
		gbc_lblListenname.gridy = 0;
		panel_details.add(lblListenname, gbc_lblListenname);

		txt_InternalName = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel_details.add(txt_InternalName, gbc_textField);
		txt_InternalName.setColumns(10);

		panelHolder = new AbstractHolderPanel<GoDiTemplateComponent, TemplateGoDi>() {
			@Override
			public GoDiTemplateComponent getDefItem() {
				return new GoDiTemplateComponent();
			}

		};
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		panel_details.add(panelHolder, gbc_panel);

		setVisible(true);
	}

	@Override
	public void onItemLoad(TemplatePlan item) {
		if (item == null)
			item = new TemplatePlan().setName("");
		txt_InternalName.setText(item.getListName());

		panelHolder.removeAllPanelComponents();
		List<TemplateGoDi> l = item.getGoDis();
		for (TemplateGoDi templateGoDi : l) {
			panelHolder.addItem(new GoDiTemplateComponent(templateGoDi));
		}
	}

	@Override
	public Map<String, TemplatePlan> getItemsMap() {
		return TemplatePool.localPool.plaene;
	}

	@Override
	public String getKeyVal() {
		return txt_InternalName.getText();
	}

	@Override
	public TemplatePlan getItemFromFields() {
		return new TemplatePlan().setName(txt_InternalName.getName()).setGoDis(panelHolder.generateItems());
	}

	class GoDiTemplateComponent extends AbstractHolderPanelComponent<TemplateGoDi> {

		private static final long serialVersionUID = -7136727460552155158L;
		private JTextField txtDisplayName;
		private JSpinner spinnerDate;
		private DynamicComboBox<TemplateGoDi> comboBox;

		@SuppressWarnings("serial")
		public GoDiTemplateComponent() {
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
			gridBagLayout.rowHeights = new int[] { 0, 0 };
			gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			setLayout(gridBagLayout);
			comboBox = new DynamicComboBox<TemplateGoDi>() {
				@Override
				public List<TemplateGoDi> getDataSet() {
					return new ArrayList<TemplateGoDi>(TemplatePool.localPool.goDis.values());
				}
			};
			comboBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					LocalDateTime ldt = LocalDateTime.now();
					if (e.getStateChange() == ItemEvent.SELECTED) {
						TemplateGoDi item = (TemplateGoDi) e.getItem();
						if (item.getStartDay() != null)
							ldt = ldt.with(item.getStartDay());
						if (item.getStartTime() != null)
							ldt = ldt.with(item.getStartTime());

						Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
						Date date = Date.from(instant);
						spinnerDate.setValue(date);
					}
				}
			});

			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.insets = new Insets(0, 0, 0, 5);
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = 0;
			add(comboBox, gbc_comboBox);
			txtDisplayName = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 0, 5);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 0;
			add(txtDisplayName, gbc_textField);
			txtDisplayName.setColumns(10);
			spinnerDate = new JSpinner();
			spinnerDate.setModel(new SpinnerDateModel());
			spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "EEEE, H:mm"));
			GridBagConstraints gbc_spinnerDate = new GridBagConstraints();
			gbc_spinnerDate.insets = new Insets(0, 0, 0, 5);
			gbc_spinnerDate.gridx = 2;
			gbc_spinnerDate.gridy = 0;
			add(spinnerDate, gbc_spinnerDate);
			JButton btnDel = createDeleteButton("x");
			GridBagConstraints gbc_btnDel = new GridBagConstraints();
			gbc_btnDel.gridx = 3;
			gbc_btnDel.gridy = 0;
			add(btnDel, gbc_btnDel);
		}

		public GoDiTemplateComponent(TemplateGoDi t) {
			this();
			set(t);
		}

		public void set(TemplateGoDi t) {
			comboBox.setEditable(true);
			comboBox.setSelectedItem(t);
			comboBox.setEditable(false);
		}

		@Override
		public TemplateGoDi get() {
			TemplateGoDi t = (TemplateGoDi) comboBox.getSelectedItem();

			t.setDisplayName(txtDisplayName.getText());

			Date d = (Date) spinnerDate.getValue();
			LocalDateTime ldt = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
			t.setStartDay(ldt.getDayOfWeek());
			t.setStartTime(ldt.toLocalTime());
			return t;
		}
	}
}
