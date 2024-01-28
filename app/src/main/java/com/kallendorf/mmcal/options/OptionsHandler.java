package com.kallendorf.mmcal.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class OptionsHandler {

	private static Properties options;
	private static FileInputStream inputStream;
	private static FileOutputStream outputStream;

	private static final String OPTIONS_FILE_NAME = "MMAdminTool.cfg";
	private static final String KEY_CRINFOPATH = "crinfp";
	private static final String KEY_WORDDEFAULTPATH = "worddefp";
	private static final String KEY_WIDTH = "width";
	private static final String KEY_HEIGHT = "height";

	static {
		init();
	}

	public static void init() {
		options = new Properties();
		try {
			inputStream = new FileInputStream(OPTIONS_FILE_NAME);
			options.loadFromXML(inputStream);
		} catch (FileNotFoundException e) {
			try {
				(new File(OPTIONS_FILE_NAME)).createNewFile();
				inputStream = new FileInputStream(OPTIONS_FILE_NAME);
				outputStream = new FileOutputStream(OPTIONS_FILE_NAME);
				options.storeToXML(outputStream, "//BlaBla");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try {
			outputStream = new FileOutputStream(OPTIONS_FILE_NAME);
			options.storeToXML(outputStream, "//BlaBla");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fullReset() {
		(new File(OPTIONS_FILE_NAME)).delete();
		init();
	}

	private static String provide(String key, String def) {
		String prop = null;
		try {
			prop = options.getProperty(key);
		} catch (Exception e) {

		}
		if (prop == null) {
			options.put(key, def);
			save();
			prop = def;
		}
		return prop;
	}

	public static URI getcRInfoPath() {
		try {
			return new URI(
					provide(KEY_CRINFOPATH, "https://www.dropbox.com/s/70uaq67mlfrefxi/CurrentRelease.info?dl=1"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setcRInfoPath(String cRInfoPath) throws URISyntaxException {
		URI uri = new URI(cRInfoPath);
		options.setProperty(KEY_CRINFOPATH, uri.toASCIIString());
		save();
	}

	public static File getWordDefaultPath() {
		return new File(provide(KEY_WORDDEFAULTPATH, System.getProperty("user.home").replace('\\', '/') + "/Desktop/"));
	}

	public static void setWordDefaultPath(String wordDefaultPath) throws URISyntaxException, IllegalArgumentException {
		File file = new File(wordDefaultPath);
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("Not a directory!");
		}
		options.setProperty(KEY_WORDDEFAULTPATH, wordDefaultPath);
	}

	public static int getWidth() {
		return Integer.parseInt(provide(KEY_WIDTH, "720"));
	}

	public static void setWidth(int width) {
		if (width > 0) {
			options.setProperty(KEY_WIDTH, width + "");
		} else {
			throw new IllegalArgumentException("Width cannot be less than 0!");
		}
	}

	public static int getHeight() {
		return Integer.parseInt(provide(KEY_HEIGHT, "380"));
	}

	public static void setHeight(int height) {
		if (height > 0) {
			options.setProperty(KEY_HEIGHT, height + "");
		} else {
			throw new IllegalArgumentException("Height cannot be less than 0!");
		}
	}
}
