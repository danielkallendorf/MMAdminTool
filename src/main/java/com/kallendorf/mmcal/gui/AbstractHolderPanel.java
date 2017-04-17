package com.kallendorf.mmcal.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractHolderPanel<T extends AbstractHolderPanelComponent<G>, G> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3732679642270495662L;
	private JPanel panelHold;
	
	public AbstractHolderPanel(){
		setLayout(new BorderLayout(0, 0));

		panelHold = new JPanel();
		add(panelHold, BorderLayout.CENTER);
		panelHold.setLayout(new BoxLayout(panelHold,BoxLayout.Y_AXIS));

		JButton btnAdd = new JButton("Hinzufügen");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addItem(getDefItem());
				revalidate();
			}
		});
		add(btnAdd, BorderLayout.SOUTH);
	}

	public abstract T getDefItem();

	public void addItem(T component) {
		component.setAlignmentX(0.5f);
		panelHold.add(component);
		revalidate();
		repaint();
	}

	public List<G> generateItems() {
		Component[] comps=panelHold.getComponents();
		List<G> l=new ArrayList<G>();
		for (Component c : comps) {
			@SuppressWarnings("unchecked")
			T t=(T)c;
			G value=t.get();
			if(value!=null)
				l.add(value);
		}		
		return l;
	}

	public void removeItem(T component) {
		panelHold.remove(component);
		revalidate();
		repaint();
	}
	
	public void removeAllPanelComponents(){
		panelHold.removeAll();
		revalidate();
		repaint();
	}

}
