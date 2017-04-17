package com.kallendorf.mmcal;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
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

	public static int getCurrentVersion() throws URISyntaxException {
		File jar = new File(MMAdminMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		String jarName = jar.getName();
		String thisVersString = jarName.substring(7, jarName.length() - 4);
		System.out.println("thisVersString: " + thisVersString);
		return Integer.valueOf(thisVersString);
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
		} catch (HeadlessException | URISyntaxException | IOException e) {
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

					download(getURL(), new File("MMCal_r" + getUpdateVersion() + ".jar"));
					patchBatch(getUpdateVersion());
					lanuchNextInstance(getCurrentVersion(), getUpdateVersion());
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

	private static void patchBatch(int to) throws IOException {
		File bat = new File("start.bat");
		FileWriter batfw = new FileWriter(bat);
		batfw.write("java -jar MMCal_r" + to + ".jar");
		batfw.close();
	}

	private static void lanuchNextInstance(int old, int next) throws IOException {
		String command = "java -jar MMCal_r" + next + ".jar -old=" + old;
		Runtime.getRuntime().exec(command);
		System.exit(0);
	}
}
