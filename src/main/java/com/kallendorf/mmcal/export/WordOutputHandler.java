package com.kallendorf.mmcal.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.kallendorf.mmcal.MMAdminMain;
import com.kallendorf.mmcal.data.ObjectDienst;
import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.ObjectPlan;
import com.kallendorf.mmcal.options.OptionsHandler;

import word.api.interfaces.IDocument;
import word.w2004.Document2004;
import word.w2004.Document2004.Encoding;
import word.w2004.elements.Table;
import word.w2004.elements.tableElements.TableEle;

public class WordOutputHandler {
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
		fc.setFileFilter(new FileNameExtensionFilter("Word 2003 XML-Dateien (*.doc)", "doc"));
		fc.addChoosableFileFilter(new FileNameExtensionFilter("OpenOffice XML-Dateien (*.xml)", "xml"));
		fc.setMultiSelectionEnabled(false);

		if (fc.showSaveDialog(MMAdminMain.gui) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fc.getSelectedFile();

		String filePath = file.getAbsolutePath();
		FileNameExtensionFilter filter = (FileNameExtensionFilter) fc.getFileFilter();
		if (filter.getExtensions()[0].equals("doc") && !filter.accept(file)) {
			file = new File(filePath + ".doc");
		} else if (filter.getExtensions()[0].equals("xml") && !filter.accept(file)) {
			file = new File(filePath + ".xml");
		}

		IDocument doc = new Document2004();
		doc.encoding(Encoding.UTF_8);
		Table tbl = new Table();
		

		tbl.addTableEle(TableEle.TD, "KW " + kw + ":", "", "", "");
		tbl.addTableEle(TableEle.TD, "", "", "", "");

		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("EEEE, 'den' dd.MM.yyyy 'um' HH:mm");

		for (ObjectGoDi goDi : godis) {

			String dateString = sdf.format(goDi.getStart());

			for (ObjectDienst dienst : goDi.getDienste()) {
				String dienstString = dienst.getDisplayName();
				for (Iterator<String> pers = dienst.getPersons().iterator(); pers.hasNext();) {
					String p0 = pers.next();
					String p1 = pers.hasNext() ? pers.next() : "";
					tbl.addTableEle(TableEle.TD, dateString, dienstString, p0, p1);
					dateString = "";
					dienstString = "";
				}

			}
		}
		doc.addEle(tbl);

		String content = doc.getContent();

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			JOptionPane.showMessageDialog(MMAdminMain.gui, "Datei noch geöffnet?", "Speichern Fehlgeschlagen",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
