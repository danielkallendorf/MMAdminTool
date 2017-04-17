package com.kallendorf.mmcal.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class TemplatePlan {
	private List<TemplateGoDi> goDis = new ArrayList<TemplateGoDi>();
	private String listName = "#defaultName";

	public String getListName() {
		return listName;
	}
	
	@XmlElementWrapper(name="goDis") @XmlElement(name="goDi")
	public List<TemplateGoDi> getGoDis() {
		return this.goDis;
	}

	public TemplatePlan setGoDis(List<TemplateGoDi> list) {
		this.goDis = list;
		return this;
	}

	public TemplatePlan setName(String name) {
		this.listName = name;
		return this;
	}

	@Override
	public String toString() {
		return listName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TemplatePlan) {
			TemplatePlan tp = (TemplatePlan) obj;
			return tp.goDis.equals(goDis) && tp.listName.equals(listName);
		}
		return false;
	}

}
