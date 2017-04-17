package com.kallendorf.mmcal.data;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;

public class TemplateGoDi {
	private String listName = "#defaultName";
	private String displayName = "#defaultDisplayName";

	private Duration duration = Duration.ofHours(1);
	private DayOfWeek startDay;
	private LocalTime startTime;
	private List<TemplateDienst> dienste = new ArrayList<TemplateDienst>();

	private String description;;

	public String getDescription() {
		return this.description;
	}

	public String getDisplayName() {
		return this.displayName;
	}
	
	@XmlJavaTypeAdapter(com.migesok.jaxb.adapter.javatime.DurationXmlAdapter.class)
	public Duration getDuration() {
		return this.duration;
	}

	public String getListName() {
		return this.listName;
	}

	@XmlElementWrapper(name="dienste") @XmlElement(name="dienst")
	public List<TemplateDienst> getDienste() {
		if (this.dienste == null) {
			this.dienste = new ArrayList<TemplateDienst>();
		}
		return this.dienste;
	}

	public TemplateGoDi setDienste(List<TemplateDienst> dienste) {
		if (dienste == null) {
			this.dienste = new ArrayList<TemplateDienst>();
		} else {
			this.dienste = dienste;
		}
		return this;
	}

	public TemplateGoDi setDescription(String value) {
		this.description = value;
		return this;
	}

	public TemplateGoDi setDisplayName(String value) {
		this.displayName = value;
		return this;
	}

	public TemplateGoDi setDuration(Duration value) {
		this.duration = value;
		return this;
	}

	public TemplateGoDi setListName(String value) {
		this.listName = value;
		return this;
	}

	public DayOfWeek getStartDay() {
		return startDay;
	}

	public TemplateGoDi setStartDay(DayOfWeek startDate) {
		this.startDay = startDate;
		return this;
	}
	
	@XmlJavaTypeAdapter(com.migesok.jaxb.adapter.javatime.LocalTimeXmlAdapter.class)
	public LocalTime getStartTime() {
		return startTime;
	}

	public TemplateGoDi setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		return this;
	}

	@Override
	public String toString() {
		return getListName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof TemplateGoDi) {
			TemplateGoDi tg = (TemplateGoDi) obj;

			return equ(tg.listName, listName) 
					&& equ(tg.displayName, displayName) 
					&& equ(tg.duration, duration)
					&& equ(tg.startDay, startDay) 
					&& equ(tg.startTime, startTime) 
					&& equ(tg.dienste, dienste)
					&& equ(tg.description, description);
		}
		return false;
	}

	private boolean equ(Object obj1, Object obj2) {
		return obj1 == obj2 || (obj1 != null && obj2 != null && obj1.equals(obj2));
	}
}
