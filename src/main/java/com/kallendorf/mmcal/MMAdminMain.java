package com.kallendorf.mmcal;

import java.awt.EventQueue;
import java.io.File;

import com.kallendorf.mmcal.gui.MmGui;

public class MMAdminMain {

	public static MmGui gui;

	public static void main(String[] args) {
		
		String filepath="";
		
		for (int i = 0; i < args.length; i++) {
			
			if (args[i].equalsIgnoreCase("dbg")) {
				setDebugServer();
			} else if (args[i].startsWith("file=")) {
				filepath=args[i].substring(5);
			} else if (args[i].startsWith("remove=")) {
				Updater.deletOldFile(args[i].substring(7));
			} else if (args[i].startsWith("new=")) {
				int nextVers=Integer.valueOf(args[i].substring(4));
				Updater.createVersionFile(nextVers);
				Updater.pachtLaunchFiles(nextVers);
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
	
	private static void setDebugServer(){
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "8888");
	}
	
	
}
