package com.kallendorf.mmcal.export;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import com.kallendorf.mmcal.MMAdminMain;
import com.kallendorf.mmcal.data.ObjectDienst;
import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.ObjectPlan;
import com.kallendorf.mmcal.options.OptionsHandler;


public class WordOutputHandler {
	
	private static WordprocessingMLPackage doc;
	private static MainDocumentPart mainPart;
	private static ObjectFactory factory;
	
	public static void create(ObjectPlan plan) {

		List<ObjectGoDi> godis = plan.getGoDis();

		LocalDateTime ldt = godis.get(godis.size() - 1).getStart();
		int kw = ldt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

		JFileChooser fc = new JFileChooser();
		Formatter formatter = new Formatter();
		String path = formatter.format("%s\\Plan%dKW%2d", OptionsHandler.getWordDefaultPath(), ldt.getYear(), kw)
				.toString();
		formatter.close();

		fc.setSelectedFile(new File(path));
		
		FileNameExtensionFilter docxFilter = new FileNameExtensionFilter("Word-Dokument (*.docx)", "docx");
		FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
		
		fc.setFileFilter(docxFilter);
		fc.addChoosableFileFilter(pdfFilter);
		fc.setMultiSelectionEnabled(false);

		if (fc.showSaveDialog(MMAdminMain.gui) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fc.getSelectedFile();

		String filePath = file.getAbsolutePath();
		FileNameExtensionFilter filter = (FileNameExtensionFilter) fc.getFileFilter();
		if (!filter.accept(file)) {
			file = new File(filePath + "."+filter.getExtensions()[0]);
		}

		
		//Create File
		
		try {
			doc = WordprocessingMLPackage.createPackage();
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}
		mainPart = doc.getMainDocumentPart();
		factory = Context.getWmlObjectFactory();

		int cellWidth = new Double(Math
				.floor((doc.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips() / 4)))
						.intValue();

		Tbl tbl = TblFactory.createTable(1, 4, cellWidth);

		Tr head = factory.createTr();
		addText(head,"KW " + kw + ":", "", "", "");
		tbl.getContent().add(head);
		
		Tr blank= factory.createTr();
		addText(blank, "","","","");
		tbl.getContent().add(blank);
		
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("EEEE, 'den' dd.MM.yyyy 'um' HH:mm");

		for (ObjectGoDi goDi : godis) {

			String dateString = sdf.format(goDi.getStart());

			for (ObjectDienst dienst : goDi.getDienste()) {
				String dienstString = dienst.getDisplayName();
				for (Iterator<String> pers = dienst.getPersons().iterator(); pers.hasNext();) {
					String p0 = pers.next();
					String p1 = pers.hasNext() ? pers.next() : "";
					
					Tr tr = factory.createTr();
					addText(tr,dateString, dienstString, p0, p1);
					tbl.getContent().add(tr);
					
					dateString = "";
					dienstString = "";
				}
			}
		}
		mainPart.getContent().add(tbl);

		try {
			FileOutputStream fos=new FileOutputStream(file);
			if(filter==docxFilter){
				Docx4J.save(doc, fos);
			}else if(filter== pdfFilter){
				FOSettings settings=Docx4J.createFOSettings();
				settings.setWmlPackage(doc);
				Docx4J.toFO(settings, fos, 0);
			}	
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MMAdminMain.gui, "Datei noch geöffnet?", "Speichern Fehlgeschlagen",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void setCellText(Tc tc, String s) {
		Text text = factory.createText();
		text.setValue(s);

		R run = factory.createR();
		run.getContent().add(text);

		P paragraph = factory.createP();
		paragraph.getContent().add(run);

		tc.getContent().add(paragraph);
	}

	private static Tc createCell(String s) {
		Tc tc = factory.createTc();
		setCellText(tc, s);
		return tc;
	}

	private static void addText(Tr tr, String... strings) {
		for (String string : strings) {
			Tc tc = createCell(string);
			tr.getContent().add(tc);
		}
	}
}
