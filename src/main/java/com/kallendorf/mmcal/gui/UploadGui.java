package com.kallendorf.mmcal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.kallendorf.mmcal.export.RequestState;

public class UploadGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7740399518844133987L;
	private FlowLayout flowLayout;

	public UploadGui() {
		flowLayout = new FlowLayout(FlowLayout.LEFT);
		getContentPane().setLayout(flowLayout);
	}

	public void addStatePanel(StatePanel p) {
		getContentPane().add(p);
	}

	public static class StatePanel extends JComponent {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2033559389246255440L;
		Color c = Color.WHITE;
		final String HEAD;
		final String END;

		public StatePanel(String goDiName, String time) {
			HEAD = "<html>" + goDiName + "<br>" + time + "<br>";
			END = "</html>";
			setToolTipText("");
			setPreferredSize(new Dimension(40, 40));
			setMinimumSize(new Dimension(20, 20));
		}

		@Override
		public void setToolTipText(String text) {
			text = text.replace("\n", "<br>");
			super.setToolTipText(HEAD + text + END);

		}

		public void setStateColor(RequestState req) {
			SwingUtilities.invokeLater(() -> {
				switch (req) {
				case IN_PROGRESS:
					c = Color.ORANGE;
					break;
				case OK:
					c = Color.GREEN;
					break;
				case SOFT_ERROR:
					c = Color.RED;
					break;
				case HARD_ERROR:
					c = Color.RED;
					break;
				default:
					break;
				}
				repaint();
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(c);
			g.fillRect(5, 5, getWidth() - 10, getWidth() - 10);
			super.paintComponent(g);
		}
	}
}
