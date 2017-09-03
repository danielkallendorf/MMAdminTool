package com.kallendorf.mmcal.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class ObjectGoDi {
	
	private String displayName = "#defObjDisplayName";
	private String listName = "#defObjListName";

	private List<ObjectDienst> dienste = new ArrayList<ObjectDienst>();

	private LocalDateTime start = LocalDateTime.now();
	private Duration duration = Duration.ofHours(1);
	private String description;

	public ObjectGoDi() {
	}

	public ObjectGoDi(TemplateGoDi t) {
		this();
		setListName(t.getListName());
		
		String dsp = t.getDisplayName();
		if(dsp==null||dsp.equals(""))
			dsp=getListName();
		setDisplayName(dsp);
		
		if (t.getStartDay() != null)
			start=start.with(t.getStartDay());
		if (t.getStartTime() != null)
			start=start.withHour(t.getStartTime().getHour()).withMinute(t.getStartTime().getMinute());
		setStart(start);
		
		setDuration(t.getDuration());
		
		for (TemplateDienst td : t.getDienste()) {
			dienste.add(new ObjectDienst(td));
		}
	}

	public String getDescription() {
		return this.description;
	}
	
	@XmlElementWrapper(name="dienste") @XmlElement(name="dienst")
	public List<ObjectDienst> getDienste() {
		return this.dienste;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public LocalDateTime getEnd() {
		return this.start.plus(duration);
	}

	public String getListName() {
		return this.listName;
	}
	
	@XmlJavaTypeAdapter(com.migesok.jaxb.adapter.javatime.LocalDateTimeXmlAdapter.class)
	public LocalDateTime getStart() {
		return this.start;
	}

	@XmlJavaTypeAdapter(com.migesok.jaxb.adapter.javatime.DurationXmlAdapter.class)
	public Duration getDuration() {
		return duration;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ObjectGoDi setDescriptionText(String value) {
		this.description = value;
		return this;
	}

	public ObjectGoDi setDienste(List<ObjectDienst> value) {
		this.dienste = value;
		return this;
	}

	public ObjectGoDi setDisplayName(String value) {
		this.displayName = value;
		return this;
	}
	
	public ObjectGoDi setListName(String value) {
		this.listName = value;
		return this;
	}

	public ObjectGoDi setStart(LocalDateTime value) {
		this.start = value;
		return this;
	}
	
	public ObjectGoDi setDuration(Duration value) {
		this.duration = value;
		return this;
	}
	
	public TemplateGoDi extractTemplate() {
		List<TemplateDienst> list = new ArrayList<TemplateDienst>();
		dienste.stream().map(od -> od.extractTemplate()).forEachOrdered(list::add);
		TemplateGoDi t = new TemplateGoDi();
		t.setDisplayName(displayName).setListName(listName).setDienste(list).setStartDay(start.getDayOfWeek())
				.setStartTime(start.toLocalTime()).setDuration(duration)
				.setDescription(description);
		return t;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjectGoDi) {
			ObjectGoDi og = (ObjectGoDi) obj;
			return og.displayName.equals(displayName) && og.listName.equals(listName) && og.dienste.equals(dienste)
					&& og.start.equals(start) && og.duration.equals(duration) && og.description.equals(description);
		}
		return false;
	}
}
