package com.kallendorf.mmcal.data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

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
	
	public ObjectPlan(TemplatePlan template, LocalDateTime start){
		this();
		int i = start.get(WeekFields.ISO.weekOfYear());
		int weekYear = LocalDateTime.now().get(WeekFields.ISO.weekOfYear());
		int weekshift=i-weekYear;
		if(template.getGoDis().get(0).getStartDay().getValue()<start.getDayOfWeek().getValue()) 
			weekshift++;

		DayOfWeek day=template.getGoDis().get(0).getStartDay();
		for (TemplateGoDi t: template.getGoDis()) {
			ObjectGoDi objectGoDi = new ObjectGoDi(t);
			
			if(day.compareTo(t.getStartDay())>0) 
				weekshift++;
			day=t.getStartDay();
			
			objectGoDi.setStart(objectGoDi.getStart().plusWeeks(weekshift));
			goDis.add(objectGoDi);
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