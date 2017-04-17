package com.kallendorf.mmcal.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TemplatePoolHandler {

	public static final String POOL_FILE_NAME = "Templates.mmtpool";
	public static final File POOL_FILE = new File(POOL_FILE_NAME);
	private static JAXBContext j;
	private static Marshaller m;
	private static Unmarshaller u;
	
	static{
		try{
		j = JAXBContext.newInstance(TemplatePool.class);
		m =j.createMarshaller();
		u = j.createUnmarshaller();
		}catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static TemplatePool loadlocalPool() {				
		try {
			return (TemplatePool) u.unmarshal(POOL_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			TemplatePool p=new TemplatePool();
			storeLocalPool(p);
			return p;
		}
	
	}
	
	public static void storeLocalPool(TemplatePool t){
		try {
			POOL_FILE.createNewFile();
			m.marshal(t, POOL_FILE);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
