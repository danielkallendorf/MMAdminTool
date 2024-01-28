package com.kallendorf.mmcal.templates;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import com.kallendorf.mmcal.data.TemplateDienst;

public class XmlTemplateEditWindowDienst extends XmlTemplateEditWindow<TemplateDienst> {
	private static final long serialVersionUID = 2854020649268120981L;
	private JTextField txt_InternalName;
	private JTextField txt_Name;
	private JSpinner spinnerPerson;

	public XmlTemplateEditWindowDienst() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_details.setLayout(gridBagLayout);
		JLabel lblListName = new JLabel("Listenname");
		GridBagConstraints gbc_lblListName = new GridBagConstraints();
		gbc_lblListName.insets = new Insets(0, 0, 5, 5);
		gbc_lblListName.anchor = GridBagConstraints.EAST;
		gbc_lblListName.gridx = 0;
		gbc_lblListName.gridy = 0;
		panel_details.add(lblListName, gbc_lblListName);
		txt_InternalName = new JTextField();
		GridBagConstraints gbc_txt_InternalName = new GridBagConstraints();
		gbc_txt_InternalName.insets = new Insets(0, 0, 5, 0);
		gbc_txt_InternalName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_InternalName.gridx = 1;
		gbc_txt_InternalName.gridy = 0;
		panel_details.add(txt_InternalName, gbc_txt_InternalName);
		txt_InternalName.setColumns(10);
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		panel_details.add(lblName, gbc_lblName);
		txt_Name = new JTextField();
		GridBagConstraints gbc_txt_Name = new GridBagConstraints();
		gbc_txt_Name.insets = new Insets(0, 0, 5, 0);
		gbc_txt_Name.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_Name.gridx = 1;
		gbc_txt_Name.gridy = 1;
		panel_details.add(txt_Name, gbc_txt_Name);
		txt_Name.setColumns(10);
		JLabel lblPersonen = new JLabel("Personen");
		GridBagConstraints gbc_lblPersonen = new GridBagConstraints();
		gbc_lblPersonen.insets = new Insets(0, 0, 0, 5);
		gbc_lblPersonen.gridx = 0;
		gbc_lblPersonen.gridy = 2;
		panel_details.add(lblPersonen, gbc_lblPersonen);
		spinnerPerson = new JSpinner();
		GridBagConstraints gbc_spinnerPerson = new GridBagConstraints();
		gbc_spinnerPerson.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPerson.gridx = 1;
		gbc_spinnerPerson.gridy = 2;
		panel_details.add(spinnerPerson, gbc_spinnerPerson);
	}

	@Override
	public void onItemLoad(TemplateDienst item) {
		if (item == null)
			item = new TemplateDienst().setDisplayName("").setListName("").setSize(0);
		txt_InternalName.setText(item.getListName());
		txt_Name.setText(item.getDisplayName());
		spinnerPerson.setValue(item.getSize());
	}

	@Override
	public Map<String, TemplateDienst> getItemsMap() {
		return TemplatePool.localPool.dienste;
	}

	@Override
	public String getKeyVal() {
		return txt_InternalName.getText();
	}

	@Override
	public TemplateDienst getItemFromFields() {
		TemplateDienst d = new TemplateDienst();
		d.setDisplayName(txt_Name.getText());
		d.setListName(txt_InternalName.getText());
		d.setSize(((Number) spinnerPerson.getValue()).intValue());
		return d;
	}
	
	@Override
	protected void lockHead() {
		txt_InternalName.setEnabled(false);
		super.lockHead();
	}
}
