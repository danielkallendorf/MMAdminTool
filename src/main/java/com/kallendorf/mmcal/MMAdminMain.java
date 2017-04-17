package com.kallendorf.mmcal;

import java.awt.EventQueue;
import java.io.File;
import java.io.FilePermission;

import com.kallendorf.mmcal.gui.MmGui;

public class MMAdminMain {

	public static MmGui gui;

	public static void main(String[] args) {
		
		String filepath="";
		
		for (int i = 0; i < args.length; i++) {
			
			if (args[i].equalsIgnoreCase("dbg")) {
				System.setProperty("http.proxyHost", "127.0.0.1");
				System.setProperty("http.proxyPort", "8888");
				System.setProperty("https.proxyHost", "127.0.0.1");
				System.setProperty("https.proxyPort", "8888");
			} else if (args[i].startsWith("file=")) {
				filepath=args[i].substring(5);
			} else if (args[i].startsWith("old=")) {
				String numStr = args[i].substring(4);
				File file = new File("MMCal_r" + numStr + ".jar");
				file.deleteOnExit();
			}
		}
		
		final File file=new File(filepath);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				gui = new MmGui();
				
				if(file.exists())
					gui.setCurrentPlanFromFile(file);
			}
		});
	}
}
