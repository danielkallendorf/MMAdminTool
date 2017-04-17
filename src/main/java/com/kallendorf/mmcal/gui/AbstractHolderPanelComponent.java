package com.kallendorf.mmcal.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractHolderPanelComponent<T> extends JPanel implements Supplier<T> {
	private static final long serialVersionUID = 462938900492289503L;

	public JButton createDeleteButton(String title) {
		JButton b = new JButton(title);
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onDelete();
			}
		});
		return b;
	}
	
	protected void onDelete(){
		Component c = getParent().getParent();
		if (c instanceof AbstractHolderPanel) {
			@SuppressWarnings("unchecked")
			AbstractHolderPanel<AbstractHolderPanelComponent<T>, T> h = (AbstractHolderPanel<AbstractHolderPanelComponent<T>, T>) c;
			h.removeItem(AbstractHolderPanelComponent.this);
		}
	}
}
