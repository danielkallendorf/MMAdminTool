package com.kallendorf.mmcal.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import com.google.api.services.calendar.Calendar;
import com.kallendorf.mmcal.data.ObjectPlan;
import com.kallendorf.mmcal.data.TemplatePlan;
import com.kallendorf.mmcal.data.UploadPlan;
import com.kallendorf.mmcal.export.Uploader;
import com.kallendorf.mmcal.export.WordOutputHandler;
import com.kallendorf.mmcal.options.OptionFrame;
import com.kallendorf.mmcal.options.OptionsHandler;
import com.kallendorf.mmcal.templates.TemplatePool;
import com.kallendorf.mmcal.templates.XmlTemplateEditWindowDienst;
import com.kallendorf.mmcal.templates.XmlTemplateEditWindowGoDi;
import com.kallendorf.mmcal.templates.XmlTemplateEditWindowPlan;

public class MmGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	ObjectPlanPanel panelHoldPlan;
	private JScrollPane scrollPane;
	public static HashMap<String, String> idMap = new HashMap<String, String>();
	public static HashMap<String, String> namesMap = new HashMap<String, String>();
	public static Calendar client;

	public MmGui() {
		initMain();
	}

	public void initMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, OptionsHandler.getWidth(), OptionsHandler.getHeight());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				menu_exit();
			}

		});

		setTitle("MM Plan Creator 2 - Version 24");

		menuBar.add(createMenuData());
		menuBar.add(createMenuTemplates());

		panelHoldPlan = new ObjectPlanPanel();
		scrollPane = new JScrollPane(panelHoldPlan);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		contentPane.add(new StatusBar(), BorderLayout.SOUTH);

		this.setVisible(true);
	}

	private void menu_load() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("MM-Plan Dateien (*.plandat)", "plandat"));
		fc.setMultiSelectionEnabled(false);
		int ret = fc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			setCurrentPlanFromFile(file);
		}
	}

	private void menu_loadTemplate(TemplatePlan t) {
		
		SpinnerDateModel sModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
		JSpinner spinner = new JSpinner(sModel);
		spinner.setEditor(new JSpinner.DateEditor(spinner, new SimpleDateFormat("dd.MM.yy").toPattern()));
		int option = JOptionPane.showOptionDialog(null, spinner, "Plan erstellen ab", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		
		if (option == JOptionPane.OK_OPTION)
		{
			Date d=(Date)spinner.getValue();
			LocalDateTime ldt= LocalDateTime.ofInstant(d.toInstant(),ZoneId.systemDefault());
			ObjectPlan op= new ObjectPlan(t,ldt);
			scrollPane.setViewportView(panelHoldPlan = new ObjectPlanPanel(op));
		}
		
		
	}
	


	public void setCurrentPlanFromFile(File file) {
		if (file != null) {
			try {
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				JAXBContext j = JAXBContext.newInstance(ObjectPlan.class);
				Unmarshaller u = j.createUnmarshaller();
				ObjectPlan plan = (ObjectPlan) u.unmarshal(is);
				scrollPane.setViewportView(panelHoldPlan = new ObjectPlanPanel(plan));
				is.close();
				revalidate();
				repaint();
				pack();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void menu_save() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("MM-Plan Dateien (*.plandat)", "plandat"));
		fc.setMultiSelectionEnabled(false);
		int ret = fc.showSaveDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			String filePath = file.getAbsolutePath();
			if (!filePath.endsWith(".plandat")) {
				file = new File(filePath + ".plandat");
			}

			if (file != null) {
				try {
					OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
					JAXBContext j = JAXBContext.newInstance(ObjectPlan.class);
					Marshaller m = j.createMarshaller();
					ObjectPlan saveplan = panelHoldPlan.getPlan();
					m.marshal(saveplan, os);

					os.flush();
					os.close();
					System.out.println("Test");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "", "Speichern Fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}
	}

	private void menu_edit() {
		new UploadPanelPlan();
	}

	private void menu_clear() {
		int n = JOptionPane.showConfirmDialog(this, "Alles Löschen?", "Warnung", JOptionPane.OK_CANCEL_OPTION);
		if (n == 0) {
			scrollPane.setViewportView(panelHoldPlan = new ObjectPlanPanel());
			revalidate();
			repaint();
		}
	}

	private void menu_upload() {
		int n = JOptionPane.showConfirmDialog(this, "Wirklich Hochladen?", "Warnung", JOptionPane.OK_CANCEL_OPTION);
		if (n == 0) {
			ObjectPlan obPlan = panelHoldPlan.getPlan();
			UploadPlan plan = new UploadPlan(obPlan);
			new Thread(() -> {
				Uploader.prepareUpload(plan);
				Uploader.upload(plan);
			}).start();
		}
	}

	private void menu_wexport() {
		WordOutputHandler.create(panelHoldPlan.getPlan());
	}

	private void menu_templates_Plan() {
		new XmlTemplateEditWindowPlan();
	}

	private void menu_templates_GoDis() {
		new XmlTemplateEditWindowGoDi();
	}

	private void menu_templates_Dienste() {
		new XmlTemplateEditWindowDienst();
	}

	private void menu_options() {
		new OptionFrame();
	}

	private void menu_exit() {
		System.exit(0);
	}

	private JMenu createMenuData() {
		JMenu mnDataMenu = new JMenu("Datei");

		JMenuItem mntmNew = new JMenuItem("Neu");
		mntmNew.addActionListener(arg0 -> menu_clear());

		JMenu mntmTmp = new JMenu("Neu aus...");
		populateTemplateMenu(mntmTmp);

		JMenuItem mntmLoad = new JMenuItem("Laden");
		mntmLoad.addActionListener(arg0 -> menu_load());

		JMenuItem mntmEdit = new JMenuItem("Bearbeiten");
		mntmEdit.addActionListener(arg0 -> menu_edit());

		JMenuItem mntmSave = new JMenuItem("Speichern");
		mntmSave.addActionListener(arg0 -> menu_save());

		JMenuItem mntmUpload = new JMenuItem("Hochladen");
		mntmUpload.addActionListener(arg0 -> menu_upload());

		JMenuItem mntmExport = new JMenuItem("Dokumente erzeugen");
		mntmExport.addActionListener(arg0 -> menu_wexport());

		JMenuItem mntmOptions = new JMenuItem("Optionen");
		mntmOptions.addActionListener(arg0 -> menu_options());

		JMenuItem mntmExit = new JMenuItem("Verlassen");
		mntmExit.addActionListener(arg0 -> menu_exit());

		mnDataMenu.add(mntmNew);
		mnDataMenu.add(mntmTmp);
		mnDataMenu.addSeparator();

		mnDataMenu.add(mntmLoad);
		mnDataMenu.add(mntmEdit);
		mnDataMenu.addSeparator();

		mnDataMenu.add(mntmSave);
		mnDataMenu.add(mntmUpload);
		mnDataMenu.add(mntmExport);
		mnDataMenu.addSeparator();

		mnDataMenu.add(mntmOptions);
		mnDataMenu.add(mntmExit);

		return mnDataMenu;
	}



	private void populateTemplateMenu(JMenu mntmTmp) {
		
		for (String s : TemplatePool.localPool.plaene.keySet()) {
			JMenuItem jMenuItem = new JMenuItem(s);
			jMenuItem.addActionListener(evt->menu_loadTemplate(TemplatePool.localPool.plaene.get(s)));
			mntmTmp.add(jMenuItem);
		}	
	}

	public JMenu createMenuTemplates() {
		JMenu mnTemplates = new JMenu("Templates");

		JMenuItem mntmWochentemplate = new JMenuItem("Wochentemplate");
		mntmWochentemplate.addActionListener(e -> menu_templates_Plan());

		JMenuItem mntmGottesdienste = new JMenuItem("Gottesdienste");
		mntmGottesdienste.addActionListener(e -> menu_templates_GoDis());

		JMenuItem mntmDienste = new JMenuItem("Dienste");
		mntmDienste.addActionListener(e -> menu_templates_Dienste());

		mnTemplates.add(mntmWochentemplate);
		mnTemplates.add(mntmGottesdienste);
		mnTemplates.add(mntmDienste);

		return mnTemplates;
	}
}
