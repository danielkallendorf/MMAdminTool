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
				System.out.println("DebugMode");
				setDebugServer();
			} else if (args[i].startsWith("file=")) {
				System.out.println("File argument:");
				filepath=args[i].substring(5);
				System.out.println(filepath);
			} else if (args[i].startsWith("remove=")) {
				String delFile = args[i].substring(7);
				System.out.println("Deletion:");
				System.out.println(delFile);
				Updater.deletOldFile(delFile);
			} else if (args[i].startsWith("new=")) {
				int nextVers=Integer.valueOf(args[i].substring(4));
				System.out.printf("New Version: %d%n",nextVers);
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
