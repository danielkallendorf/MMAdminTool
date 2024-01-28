package com.kallendorf.mmcal.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.kallendorf.mmcal.Updater;
import com.kallendorf.mmcal.export.GoogleAuthHandler;

public class StatusBar extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7814214260235112390L;
	private JLabel lblStatusGoogle;
	private JLabel lblStatusDropbox;
	private final MouseListener lblDropboxListener = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			setDropboxConnecting();
			checkForUpdates();
		}
	};
	private final MouseListener lblGoogleListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			setGoogleConnecting();
			getGoogleData();
		}
	};

	public StatusBar() {
		super();

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gbl_panel);

		JLabel lblGoogle = new JLabel("Google");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		add(lblGoogle, gbc_lblNewLabel);

		lblStatusGoogle = new JLabel();
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblStatusGoogle, gbc_lblNewLabel_1);

		JLabel lblDropbox = new JLabel("Dropbox");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 3;
		gbc_lblNewLabel_2.gridy = 0;
		add(lblDropbox, gbc_lblNewLabel_2);

		lblStatusDropbox = new JLabel();
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		lblStatusDropbox.setForeground(Color.gray);
		gbc_lblNewLabel_3.gridx = 4;
		gbc_lblNewLabel_3.gridy = 0;
		add(lblStatusDropbox, gbc_lblNewLabel_3);

		setDropboxConnecting();
		checkForUpdates();

		setGoogleConnecting();
		getGoogleData();
	}

	public void setDropboxState(boolean state) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (state) {
					lblStatusDropbox.setToolTipText(null);
					lblStatusDropbox.setText("Checked");
					lblStatusDropbox.setForeground(Color.green);
				} else {
					lblStatusDropbox.setToolTipText("Click to retry");
					lblStatusDropbox.setText("Failed");
					lblStatusDropbox.setForeground(Color.red);
					lblStatusDropbox.addMouseListener(lblDropboxListener);
				}
			}
		});
	}

	public void setDropboxConnecting() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				lblStatusDropbox.setToolTipText(null);
				lblStatusDropbox.setText("Connecting..");
				lblStatusDropbox.setForeground(Color.gray);
				lblStatusDropbox.removeMouseListener(lblDropboxListener);
			}
		});
	}

	public void setGoogleState(boolean state) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (state) {
					lblStatusGoogle.setToolTipText(null);
					lblStatusGoogle.setText("Ready");
					lblStatusGoogle.setForeground(Color.green);
				} else {
					lblStatusGoogle.setToolTipText("Click to retry");
					lblStatusGoogle.setText("Failed");
					lblStatusGoogle.setForeground(Color.red);
					lblStatusGoogle.addMouseListener(lblGoogleListener);
				}
			}
		});
	}

	public void setGoogleConnecting() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				lblStatusGoogle.setToolTipText(null);
				lblStatusGoogle.setText("Connecting..");
				lblStatusGoogle.setForeground(Color.gray);
				lblStatusGoogle.removeMouseListener(lblGoogleListener);
			}
		});
	}

	public void getGoogleData() {
		new Thread() {
			@Override
			public void run() {
				try {
					MmGui.client = GoogleAuthHandler.getCalendarService();
					CalendarList list = MmGui.client.calendarList().list().execute();

					if (list.getItems().size() < 5) {
						Frame frame = new Frame();
						int n = JOptionPane.showConfirmDialog(frame,
								"Es wurden nur sehr wenige Unter-Kalender gefunden.\nIst das der richtige Google-Account? Soll der Login zurückgesetzt werden?",
								"",
								JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							GoogleAuthHandler.resetTokens();
							MmGui.client = GoogleAuthHandler.getCalendarService();
							list = MmGui.client.calendarList().list().execute();
						}

					}

					MmGui.idMap.put("MM", "primary");
					for (CalendarListEntry entry : list.getItems()) {
						MmGui.idMap.put(entry.getSummary(), entry.getId());
						MmGui.namesMap.put(entry.getId(), entry.getSummary());
					}

					setGoogleState(true);

				} catch (Exception e) {
					setGoogleState(false);
				}

			}
		}.start();
	}

	public void checkForUpdates() {
		new Thread() {
			@Override
			public void run() {
				try {
					Updater.fetchUpdateFileContent();
					setDropboxState(true);
					Updater.update();
				} catch (StringIndexOutOfBoundsException e) {
					setDropboxState(true);
				} catch (Exception e) {
					setDropboxState(false);
				}
			}
		}.start();
	}
}
