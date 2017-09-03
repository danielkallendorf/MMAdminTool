package com.kallendorf.mmcal;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import javax.swing.JOptionPane;

import com.kallendorf.mmcal.options.OptionsHandler;

public class Updater {

	private static String[] lastFileContents;
	private static String versionFile="Version.txt";

	
	public static int getCurrentVersion() throws IOException {
		File versionTxt= new File(versionFile);
		if(!versionTxt.exists()){	
			return 0;
		}
		BufferedReader b= new BufferedReader(new InputStreamReader(new FileInputStream(versionTxt)));
		String versionString=b.readLine();
		b.close();
		int version=Integer.parseInt(versionString);
		return version;
	}

	public static int getUpdateVersion() throws IOException {
		if (lastFileContents == null || lastFileContents.length == 0) {
			fetchUpdateFileContent();
		}
		String updateVersString = lastFileContents[0];
		System.out.println("updateVersString: " + updateVersString);
		return Integer.valueOf(updateVersString);
	}

	public static URL getURL() throws IOException {
		if (lastFileContents == null || lastFileContents.length == 0) {
			fetchUpdateFileContent();
		}
		return new URL(lastFileContents[1].substring(6));
	}

	public static String[] fetchUpdateFileContent() throws IOException {
		URL url = OptionsHandler.getcRInfoPath().toURL();
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String nextLine;
		String out = "";
		while ((nextLine = in.readLine()) != null) {
			out += nextLine;
		}
		in.close();
		String[] props = out.split(";");
		lastFileContents = props;
		return props;
	}

	private static int askForUpdate() {
		Frame frame = new Frame();
		try {
			int n = JOptionPane.showConfirmDialog(frame, "Neue Version gefunden \n" + "Jetzt installieren?",
					"Update: " + getCurrentVersion() + "\u2192" + getUpdateVersion(), JOptionPane.OK_CANCEL_OPTION);
			return n;
		} catch (HeadlessException | IOException e) {
			e.printStackTrace();
		} finally {
			frame.dispose();
		}
		return JOptionPane.CANCEL_OPTION;
	}

	public static void update() {
		try {
			if (getUpdateVersion() > getCurrentVersion()) {
				if (askForUpdate() == JOptionPane.YES_OPTION) {
					MMAdminMain.gui.dispose();
					download(getURL(), new File("MMCal_r"+getUpdateVersion()+".jar"));
					removeVersionTxt();
					launchNextInstance(MMAdminMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString(),getUpdateVersion());
				}
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static void download(URL from, File to) throws IOException {
		System.out.println("Downloading");
		ReadableByteChannel cIn = Channels.newChannel(getURL().openStream());
		FileOutputStream fos = new FileOutputStream(to);
		FileChannel cOut = fos.getChannel();
		cOut.transferFrom(cIn, 0, Long.MAX_VALUE);
		fos.flush();
		fos.close();
		cOut.close();
	}

	private static void launchNextInstance(String oldJar, int nextVersion) throws IOException {
		String command = "java -jar MMCal_r"+nextVersion+".jar remove=" + oldJar + " new="+ nextVersion;
		Runtime.getRuntime().exec(command);
		System.out.println("Starting:"+command);
		System.exit(0);
	}
	
	private static void removeVersionTxt(){
		File f=new File(versionFile);
		f.delete();
	}
	
	static void createVersionFile(String version){
		File f= new File(versionFile);
		f.delete();
		try {
			f.createNewFile();
			FileWriter w= new FileWriter(f);
			w.write(version);
			w.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	static void deletOldFile(String filename){
		File file = new File(filename);
		file.delete();
	}
	
	//TODO patchBatch
	static void patchBath(int newVersion){
		/*Linux like 
		* cd -- "$(dirname "$BASH_SOURCE")"
		* file="$(ls -v | tail -n 1)"
		* java -jar $file
		*/
		
		/*Windows .bat
		* @echo off
		* setlocal enableDelayedExpansion
		* set max=0
		* for /f "tokens=1* delims=-.0" %%A in ('dir /b /a-d MMCal_r*.jar') do if %%B gtr !max! set max=%%B
		* java -jar MMCal_r%max%.jar
		 */
		
		
		
	}
	
}
