package com.kallendorf.mmcal.gui;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JTextField;
import javax.swing.JButton;

public class TxtFieldPerson extends AbstractHolderPanelComponent<String> {

	private static final long serialVersionUID = 7216480673106160896L;
	private JTextField textField;
	private boolean recognized = false;

	public TxtFieldPerson() {
		textField = new JTextField();
		add(textField);
		textField.setColumns(20);
		
		JButton btnDel = createDeleteButton("X");
		add(btnDel);
		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				int caretPosition = textField.getCaretPosition();
				int length = textField.getText().length();

				if (e.getKeyCode() == KeyEvent.VK_RIGHT && caretPosition == length) {
					Collection<String> col = new HashSet<String>();
					col.addAll(MmGui.idMap.keySet());
					col.removeIf(s -> !s.toLowerCase().startsWith(textField.getText().toLowerCase()));

					String elem = col.iterator().next();

					for (int i = caretPosition; i < elem.length(); i++) {
						final String newText = elem.substring(0, i);
						if (col.removeIf(s -> !s.startsWith(newText))) {
							textField.setText(elem.substring(0, i - 1));
							if (!recognized)
								performCheck();
							return;
						}
					}
					textField.setText(elem);
					if (!recognized)
						performCheck();
				}
			}
		});

		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				performCheck();
			};
		});
	}

	public TxtFieldPerson(String name) {
		this();
		textField.setText(name);
		performCheck();
	}

	@Override
	public String get() {
		if (textField.getText().isEmpty())
			return null;
		return textField.getText();
	}

	public void performCheck() {
		if (MmGui.idMap.containsKey(textField.getText())) {
			textField.setBackground(Color.white);
			recognized = true;
		} else {
			textField.setBackground(new Color(255, 60, 60));
			recognized = false;
		}
	}
}
