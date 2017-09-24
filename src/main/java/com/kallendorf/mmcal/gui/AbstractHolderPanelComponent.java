package com.kallendorf.mmcal.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractHolderPanelComponent<G> extends JPanel
		implements Supplier<G>, Comparable<AbstractHolderPanelComponent<G>> {
	private static final long serialVersionUID = 462938900492289503L;

	protected AbstractHolderPanel<AbstractHolderPanelComponent<G>, G> holderPanel;

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

	protected void onDelete() {
		this.holderPanel.removeItem(this);
	}

	public int compareTo(AbstractHolderPanelComponent<G> o) {
		return 0;
	}

	public void setHolderPanel(AbstractHolderPanel<AbstractHolderPanelComponent<G>, G> holderPanel) {
		this.holderPanel = holderPanel;
	}
}
