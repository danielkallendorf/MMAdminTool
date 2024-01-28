package com.kallendorf.mmcal.gui;

import java.awt.event.ItemListener;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.google.common.base.Supplier;

public abstract class DynamicComboBox<T> extends JComboBox<T> {
	private static final long serialVersionUID = 2701067371593233676L;
	private Comparator<T> c;
	private Supplier<T> def;

	public DynamicComboBox() {
		this((Supplier<T>) null);
	}

	public DynamicComboBox(T def) {
		this(() -> def);
	}

	public DynamicComboBox(Supplier<T> defGen) {
		super();
		this.def = defGen;
		addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				Object selected = getModel().getSelectedItem();
				updateListData();
				unnotifiedSet(selected);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
		updateListData();
		if (defGen == null) {
			setSelectedIndex(-1);
		} else {
			setSelectedIndex(0);
		}
	}

	public abstract List<T> getDataSet();

	public DynamicComboBox<T> setComparator(Comparator<T> c) {
		this.c = c;
		return this;
	}

	public void setDefGenerator(Supplier<T> generator) {
		this.def = generator;
		updateListData();

	}

	public void unnotifiedSet(Object o) {
		ItemListener[] listeners = getItemListeners();
		for (ItemListener itemListener : listeners) {
			removeItemListener(itemListener);
		}
		setSelectedItem(o);
		for (ItemListener itemListener : listeners) {
			addItemListener(itemListener);
		}
	}

	@SuppressWarnings("unchecked")
	private void updateListData() {
		List<T> l = getDataSet();
		if (c != null) {
			l.sort(c);
		}

		if (DynamicComboBox.this.def != null) {
			l.add(0, DynamicComboBox.this.def.get());
		}

		setModel(new DefaultComboBoxModel<T>((T[]) l.toArray()));
	}

}
