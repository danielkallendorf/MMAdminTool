package com.kallendorf.mmcal.gui;

import java.awt.BorderLayout;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.UploadGoDi;
import com.kallendorf.mmcal.data.UploadPlan;
import com.kallendorf.mmcal.export.UploadUtil;
import com.kallendorf.mmcal.export.Uploader;

public class UploadPanelPlan extends JFrame {

	private static final long serialVersionUID = 43835522360329302L;
	private JSpinner spinnerStart;
	private JSpinner spinnerEnd;
	private JScrollPane scrollPane;
	private AbstractHolderPanel<UploadGoDiPanel, UploadGoDi> holderPanel;

	public UploadPanelPlan() {

		setSize(600, 750);

		JPanel topBar = new JPanel();

		spinnerStart = new JSpinner();
		spinnerStart.setModel(
				new SpinnerDateModel(new Date(), new Date(946681200000L), null, Calendar.DAY_OF_YEAR));
		topBar.add(spinnerStart);

		spinnerEnd = new JSpinner();
		spinnerEnd.setModel(
				new SpinnerDateModel(new Date(), new Date(946681200000L), null, Calendar.DAY_OF_YEAR));
		topBar.add(spinnerEnd);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(arg0 -> {
			onReset();
		});
		topBar.add(btnReset);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(arg0 -> {
			onUpdate();
		});
		topBar.add(btnUpdate);
		getContentPane().add(topBar, BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		holderPanel = new AbstractHolderPanel<UploadGoDiPanel, UploadGoDi>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3599289725171922460L;

			public UploadGoDiPanel getDefItem() {
				return (UploadGoDiPanel) null;
			}
		};
		holderPanel.remove(holderPanel.getComponentCount()-1);
		scrollPane.setViewportView(holderPanel);
		setVisible(true);		
	}

	private void onReset() {
		List<UploadGoDi> list = new ArrayList<>();
		try {
			LocalDateTime start = LocalDateTime.ofInstant(((Date) spinnerStart.getValue()).toInstant(),
					ZoneId.systemDefault());
			LocalDateTime end = LocalDateTime.ofInstant(((Date) spinnerEnd.getValue()).toInstant(),
					ZoneId.systemDefault());
			String nextPageToken = null;
			do {
				Events events = MmGui.client.events().list("primary")
						.setTimeMin(UploadUtil.TimeConversion.toGDateTime(start))
						.setTimeMax(UploadUtil.TimeConversion.toGDateTime(end)).setPageToken(nextPageToken).execute();
				for (Event e : events.getItems()) {
					UploadGoDi u = new UploadGoDi();
					u.setObjectGoDiOld(UploadUtil.GoDifromEvent(e));
					if (u.getObjectGoDiOld() == null)
						continue;
					u.setId(e.getId());
					list.add(u);
				}
				nextPageToken = events.getNextPageToken();
			} while (nextPageToken != null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		holderPanel.removeAllPanelComponents();
		for (UploadGoDi uploadGoDi : list) {
			holderPanel.addItem(new UploadGoDiPanel(uploadGoDi));
		}
		revalidate();
		repaint();

	}

	private void onUpdate() {
		UploadPlan u = new UploadPlan();
		u.setUploadGoDis(holderPanel.generateItems());
		Uploader.upload(u);
	}

	static class UploadGoDiPanel extends AbstractHolderPanelComponent<UploadGoDi> {

		private static final long serialVersionUID = 5606595664938150644L;
		AbstractHolderPanelComponent<ObjectGoDi> objectPanel;
		final UploadGoDi source;
		boolean del = false;

		public UploadGoDiPanel(UploadGoDi source) {
			this.source = source;
			objectPanel = new ObjectGoDiPanel(source.getObjectGoDiOld()) {

				private static final long serialVersionUID = -6767704818295598481L;

				@Override
				protected void onDelete() {
					UploadGoDiPanel p = ((UploadGoDiPanel) getParent());
					ObjectGoDi o = objectPanel.get();
					//p.remove(objectPanel);
					
					p.objectPanel = new EmptyPanel(o.getDisplayName() + " " + o.getStart());
					p.add(p.objectPanel);
					super.onDelete();
				}
			};
			this.add(objectPanel);
		}

		@Override
		public UploadGoDi get() {
			source.setObjectGoDiNew(objectPanel.get());
			return source;
		}

		private class EmptyPanel extends AbstractHolderPanelComponent<ObjectGoDi> {

			private static final long serialVersionUID = -3165122179403333647L;

			public EmptyPanel(String descr) {
				add(new JLabel("gelöscht: " + descr));
			}

			@Override
			public ObjectGoDi get() {
				return null;
			}

		}
	}
}
