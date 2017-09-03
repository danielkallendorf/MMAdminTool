package com.kallendorf.mmcal.data;

import java.util.ArrayList;
import java.util.List;

public class ObjectDienst {

	private String displayName;
	private String listName;
	private List<String> persons;

	public ObjectDienst() {
		persons = new ArrayList<String>();
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getListName() {
		return listName;
	}
	
	//@XmlList
	public List<String> getPersons() {
		return persons;
	}

	public ObjectDienst(TemplateDienst t) {
		this();
		setListName(t.getListName());
		String dsp = t.getDisplayName();
		if(dsp==null||dsp.equals(""))
			dsp=getListName();
		setDisplayName(dsp);
		
		for (int i = 0; i < t.getSize(); i++) {
			persons.add("");
		}
	}

	public ObjectDienst setDisplayName(String value) {
		this.displayName = value;
		return this;
	}

	public ObjectDienst setListName(String value) {
		this.listName = value;
		return this;
	}

	public ObjectDienst setPersons(List<String> value) {
		this.persons = value;
		return this;
	}
	
	public TemplateDienst extractTemplate(){
		return new TemplateDienst().setDisplayName(displayName).setListName(listName).setSize(persons.size());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectDienst){
			ObjectDienst od=(ObjectDienst) obj;
			return od.displayName.equals(displayName)&&od.listName.equals(listName)&&od.persons.equals(persons);
		}
		return false;
	}
}
