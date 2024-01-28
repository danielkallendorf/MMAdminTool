package com.kallendorf.mmcal.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractHolderPanel<T extends AbstractHolderPanelComponent<G>, G> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3732679642270495662L;
	private JPanel panelHold;
	private ArrayList<T> items;

	public AbstractHolderPanel() {
		setLayout(new BorderLayout(0, 0));

		panelHold = new JPanel();
		items = new ArrayList<>();

		add(panelHold, BorderLayout.CENTER);
		panelHold.setLayout(new BoxLayout(panelHold, BoxLayout.Y_AXIS));

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

	public List<G> generateItems() {
		List<G> l = items.stream().map(i -> i.get()).filter(g -> g != null).collect(Collectors.toList());
		return l;
	}

	@SuppressWarnings("unchecked")
	public void addItem(T component) {
		component.setAlignmentX(0.5f);
		panelHold.add(component);
		items.add(component);
		AbstractHolderPanel<AbstractHolderPanelComponent<G>, G> holderPanel = (AbstractHolderPanel<AbstractHolderPanelComponent<G>, G>) this;
		component.setHolderPanel(holderPanel);
		revalidate();
		repaint();
	}

	@SuppressWarnings("unchecked")
	public void addItemAt(T component, int index) {
		component.setAlignmentX(0.5f);
		panelHold.add(component, index);
		items.add(index, component);
		component.setHolderPanel((AbstractHolderPanel<AbstractHolderPanelComponent<G>, G>) this);
		revalidate();
		repaint();
	}

	public void removeItem(T component) {
		panelHold.remove(component);
		items.remove(component);
		component.setHolderPanel(null);
		revalidate();
		repaint();
	}

	public void removeItemAt(int index) {
		panelHold.remove(index);
		T item = items.remove(index);
		if (item != null)
			item.setHolderPanel(null);

		revalidate();
		repaint();
	}

	public void removeAllPanelComponents() {
		panelHold.removeAll();
		for (T t : items) {
			t.setHolderPanel(null);
		}
		items = new ArrayList<>();
		revalidate();
		repaint();
	}

	public void onComparableChanged(T focusedItem) {

		EventQueue.invokeLater(() -> {
			int i = items.indexOf(focusedItem);

			int j = 0;
			if (i != -1) {
				while (items.get(j) == focusedItem || items.get(j).compareTo(focusedItem) < 0) {
					j++;
				}
			}
			if(j>i)j--;
			
			moveItem(i, j);

			revalidate();
			repaint();
			focusedItem.revalidate();
			
			EventQueue.invokeLater(()->{
				Rectangle bounds2 = focusedItem.getBounds();
				scrollRectToVisible(bounds2);
				});
		});
		
		
	}

	public void moveItem(int from, int to) {

		int steps;
		if (to < from)
			steps = from - to;
		else
			steps = to - from;

		for (int i = 0; i < steps; i++) {
			T comp = items.get(to);
			comp.invalidate();
			removeItemAt(to);
			addItemAt(comp, from);
		}
		revalidate();
		repaint();
	}
}
