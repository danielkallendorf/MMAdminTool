package com.kallendorf.mmcal.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement
public class ObjectPlan {

	
	private List<ObjectGoDi> goDis;

	public ObjectPlan() {
		goDis = new ArrayList<ObjectGoDi>();
	}
	
	public ObjectPlan(TemplatePlan template){
		this();
		for (TemplateGoDi t: template.getGoDis()) {
			goDis.add(new ObjectGoDi(t));
		}
	}

	@XmlElementWrapper(name="goDis") @XmlElement(name="godi")
	public List<ObjectGoDi> getGoDis() {
		return this.goDis;
	}

	public ObjectPlan setGoDis(List<ObjectGoDi> list) {
		this.goDis = list;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectPlan){
			ObjectPlan op=(ObjectPlan) obj;
			return op.goDis.equals(goDis);
		}
		return false;
	}
}