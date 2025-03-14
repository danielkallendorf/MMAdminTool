package com.kallendorf.mmcal;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import javax.swing.JOptionPane;

import com.kallendorf.mmcal.options.OptionsHandler;

public class Updater {

	private static String[] lastFileContents;
	private static String versionFile = "Version.txt";

	public static int getCurrentVersion() throws IOException {
		File versionTxt = new File(versionFile);
		if (!versionTxt.exists()) {
			return 0;
		}
		BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(versionTxt)));
		String versionString = b.readLine();
		b.close();
		int version = Integer.parseInt(versionString);
		return version;
	}

	public static int getUpdateVersion() throws IOException {
		if (lastFileContents == null || lastFileContents.length == 0) {
			fetchUpdateFileContent();
		}
		String updateVersionString = lastFileContents[0];
		System.out.println("updateVersString: " + updateVersionString);
		return Integer.valueOf(updateVersionString.substring(9));
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
			int updateVersion = getUpdateVersion();
			int currentVersion = getCurrentVersion();
			if (updateVersion > currentVersion) {
				if (askForUpdate() == JOptionPane.YES_OPTION) {
					MMAdminMain.gui.dispose();
					download(getURL(), new File("MMCal_r" + updateVersion + ".jar"));
					removeVersionTxt();
					launchNextInstance(
							MMAdminMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString(),
							updateVersion);
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
		String command = "java -jar MMCal_r" + nextVersion + ".jar remove=" + oldJar + " new=" + nextVersion;
		Runtime.getRuntime().exec(command);
		System.out.println("Starting:" + command);
		System.exit(0);
	}

	private static void removeVersionTxt() {
		File f = new File(versionFile);
		f.delete();
	}

	static void createVersionFile(int version) {
		File f = new File(versionFile);
		try {
			f.createNewFile();
			PrintWriter w = new PrintWriter(f);
			w.print(version);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void deletOldFile(String uri) {
		File file;
		try {
			URI uri2 = new URI(uri);
			file = new File(uri2);
			file.delete();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static final File LAUNCH_WIN = new File("MMAdminTool.bat");
	private static final File LAUNCH_OSX = new File("MMAdminTool.command");
	private static final File LAUNCH_LNX = new File("MMAdminTool.sh");

	public static void pachtLaunchFiles(int version) {
		File[] files = { LAUNCH_WIN, LAUNCH_OSX, LAUNCH_LNX };
		for (File file : files) {

			try {
				file.createNewFile();

				PrintWriter p = new PrintWriter(file);
				p.printf("java -jar MMCal_r%d.jar", version);
				p.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
