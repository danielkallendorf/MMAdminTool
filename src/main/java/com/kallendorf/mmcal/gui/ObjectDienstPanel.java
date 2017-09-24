package com.kallendorf.mmcal.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.kallendorf.mmcal.data.ObjectDienst;
import com.kallendorf.mmcal.data.TemplateDienst;
import com.kallendorf.mmcal.templates.TemplatePool;
import com.kallendorf.mmcal.templates.XmlTemplateEditWindowDienst;

public class ObjectDienstPanel extends AbstractHolderPanelComponent<ObjectDienst> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 122107298277699025L;
	private JTextField textField;
	private DynamicComboBox<TemplateDienst> comboBox;
	private AbstractHolderPanel<TxtFieldPerson, String> panelPers;
	private JButton btnDel;

	public ObjectDienstPanel() {
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		comboBox = new DynamicComboBox<TemplateDienst>() {
			private static final long serialVersionUID = 5166070990807933386L;

			@Override
			public List<TemplateDienst> getDataSet() {
				return new ArrayList<TemplateDienst>(TemplatePool.localPool.dienste.values());
			}
		};
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					TemplateDienst t = (TemplateDienst) e.getItem();
					ObjectDienst o = new ObjectDienst(t);
					ObjectDienstPanel.this.onLoad(o);
				}
			}
		});

		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		add(comboBox, gbc_comboBox);

		panelPers = new AbstractHolderPanel<TxtFieldPerson, String>(){
			private static final long serialVersionUID = 7189659929404796385L;

			@Override
			public TxtFieldPerson getDefItem() {
				return new TxtFieldPerson();
			}

		};
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		add(panelPers, gbc_panel);

		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		add(textField, gbc_textField);
		textField.setColumns(10);

		JButton btnProp = new JButton("Eigenschaften");
		btnProp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TemplateDienst old = (TemplateDienst) comboBox.getSelectedItem();
				TemplateDienst t = new XmlTemplateEditWindowDienst().edit(old);
				if (old.equals(t)) {
					return;
				} else {
					t.setListName(t.getListName()+"*");
				}
				comboBox.setDefGenerator(()->t);
				comboBox.setSelectedIndex(0);
			}
		});
		
		GridBagConstraints gbc_btnProp = new GridBagConstraints();
		gbc_btnProp.insets = new Insets(0, 0, 5, 5);
		gbc_btnProp.gridx = 0;
		gbc_btnProp.gridy = 2;
		add(btnProp, gbc_btnProp);
		
		btnDel = createDeleteButton("X");
		GridBagConstraints gbc_btnDel = new GridBagConstraints();
		gbc_btnDel.fill = GridBagConstraints.BOTH;
		gbc_btnDel.gridheight = 3;
		gbc_btnDel.insets = new Insets(0, 0, 5, 0);
		gbc_btnDel.gridx = 2;
		gbc_btnDel.gridy = 0;
		add(btnDel, gbc_btnDel);
	}

	public void set(ObjectDienst o) {
		TemplateDienst td = TemplatePool.localPool.dienste.get(o.getListName());
		comboBox.setDefGenerator(() -> td != null ? td : o.extractTemplate());
		onLoad(o);
		//TODO Fix it
	}

	private void onLoad(ObjectDienst o) {
		textField.setText(o.getDisplayName());
		panelPers.removeAllPanelComponents();
		o.getPersons().stream().forEachOrdered(p -> panelPers.addItem(new TxtFieldPerson(p)));
	}

	@Override
	public ObjectDienst get() {
	
		TemplateDienst t = (TemplateDienst) comboBox.getSelectedItem();
		ObjectDienst obj = new ObjectDienst(t);
		
		if(!textField.getText().equals(""))
			obj.setDisplayName(textField.getText());
		
		obj.setPersons(panelPers.generateItems());
		return obj;
	}
	
	@Override
	public Insets getInsets() {
		return new Insets(5, 5, 5, 5);
	}
}
