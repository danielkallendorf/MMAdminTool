package com.kallendorf.mmcal.options;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class OptionFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_WordDefaultPath;
	private JTextField textField_CRInfoPath;
	private JSpinner spinner_Height;
	private JSpinner spinner_Width;

	/**
	 * Create the frame.
	 */
	public OptionFrame() {
		setTitle("Optionen");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		JLabel lblCRInfoPath = new JLabel("\"CurrenReleas.info\" Pfad:\r\n");
		GridBagConstraints gbc_lblCRInfoPath = new GridBagConstraints();
		gbc_lblCRInfoPath.anchor = GridBagConstraints.EAST;
		gbc_lblCRInfoPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblCRInfoPath.gridx = 0;
		gbc_lblCRInfoPath.gridy = 0;
		contentPane.add(lblCRInfoPath, gbc_lblCRInfoPath);
		textField_CRInfoPath = new JTextField(OptionsHandler.getcRInfoPath().toASCIIString());
		GridBagConstraints gbc_textField_CRInfoPath = new GridBagConstraints();
		gbc_textField_CRInfoPath.gridwidth = 2;
		gbc_textField_CRInfoPath.insets = new Insets(0, 0, 5, 0);
		gbc_textField_CRInfoPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_CRInfoPath.gridx = 1;
		gbc_textField_CRInfoPath.gridy = 0;
		contentPane.add(textField_CRInfoPath, gbc_textField_CRInfoPath);
		textField_CRInfoPath.setColumns(10);
		JLabel lblWordDefaultPath = new JLabel("Word Standart Ordner:");
		GridBagConstraints gbc_lblWordDefaultPath = new GridBagConstraints();
		gbc_lblWordDefaultPath.anchor = GridBagConstraints.EAST;
		gbc_lblWordDefaultPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblWordDefaultPath.gridx = 0;
		gbc_lblWordDefaultPath.gridy = 1;
		contentPane.add(lblWordDefaultPath, gbc_lblWordDefaultPath);
		textField_WordDefaultPath = new JTextField(OptionsHandler.getWordDefaultPath().toString());
		GridBagConstraints gbc_textField_WordDefaultPath = new GridBagConstraints();
		gbc_textField_WordDefaultPath.gridwidth = 2;
		gbc_textField_WordDefaultPath.insets = new Insets(0, 0, 5, 0);
		gbc_textField_WordDefaultPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_WordDefaultPath.gridx = 1;
		gbc_textField_WordDefaultPath.gridy = 1;
		contentPane.add(textField_WordDefaultPath, gbc_textField_WordDefaultPath);
		textField_WordDefaultPath.setColumns(10);
		JLabel lblHeight = new JLabel("Fensterhöhe:");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.anchor = GridBagConstraints.EAST;
		gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeight.gridx = 0;
		gbc_lblHeight.gridy = 2;
		contentPane.add(lblHeight, gbc_lblHeight);
		spinner_Height = new JSpinner();
		spinner_Height.setModel(
				new SpinnerNumberModel(Integer.valueOf(OptionsHandler.getHeight()), 0, null, 20));
		GridBagConstraints gbc_spinner_Height = new GridBagConstraints();
		gbc_spinner_Height.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_Height.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_Height.gridx = 2;
		gbc_spinner_Height.gridy = 2;
		contentPane.add(spinner_Height, gbc_spinner_Height);
		JLabel lblWidth = new JLabel("Fensterbreite");
		GridBagConstraints gbc_lblWidth = new GridBagConstraints();
		gbc_lblWidth.anchor = GridBagConstraints.EAST;
		gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblWidth.gridx = 0;
		gbc_lblWidth.gridy = 3;
		contentPane.add(lblWidth, gbc_lblWidth);
		spinner_Width = new JSpinner();
		spinner_Width.setModel(
				new SpinnerNumberModel(Integer.valueOf(OptionsHandler.getWidth()), 0, null, 20));
		GridBagConstraints gbc_spinner_Width = new GridBagConstraints();
		gbc_spinner_Width.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_Width.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_Width.gridx = 2;
		gbc_spinner_Width.gridy = 3;
		contentPane.add(spinner_Width, gbc_spinner_Width);
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTH;
		gbc_panel.gridwidth = 3;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		contentPane.add(panel, gbc_panel);
		JButton btnSaveOptions = new JButton("Speichern");
		btnSaveOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean failed = false;
				try {
					OptionsHandler.setcRInfoPath(textField_CRInfoPath.getText());
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(OptionFrame.this,
							"\"" + textField_CRInfoPath.getText() + "\"\n"
									+ " ist keine zulässig Eingabe für \"CurrentRelase.info Pfad\"",
							"Fehlerhafte Eingabe", JOptionPane.ERROR_MESSAGE);
					failed = true;
				}
				try {
					OptionsHandler.setWordDefaultPath(textField_WordDefaultPath.getText());
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(OptionFrame.this,
							"\"" + textField_WordDefaultPath.getText() + "\"\n"
									+ " ist keine zulässig Eingabe für \"Word Standard Ordner\"",
							"Fehlerhafte Eingabe", JOptionPane.ERROR_MESSAGE);
					failed = true;
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(OptionFrame.this,
							"\"" + textField_WordDefaultPath.getText() + "\"\n"
									+ " ist keine Ordner \n \"Word Standard Ordner\" muss ein Ordner sein.",
							"Kein Ordner", JOptionPane.ERROR_MESSAGE);
					failed = true;
				}
				OptionsHandler.setWidth((Integer) spinner_Width.getValue());
				OptionsHandler.setHeight((Integer) spinner_Height.getValue());
				if (!failed) {
					OptionsHandler.save();
					OptionFrame.this.dispose();
				}
			}
		});
		panel.add(btnSaveOptions);
		JButton btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsHandler.init();
				OptionFrame.this.dispose();
			}
		});
		panel.add(btnCancel);
		JButton btnReset = new JButton("Standart-Werte");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsHandler.fullReset();
				OptionFrame.this.dispose();
				new OptionFrame();
			}
		});
		panel.add(btnReset);
		setVisible(true);
	}
}
