package com.kallendorf.mmcal.gui;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class DurationSpinner extends JSpinner {

	private static final long serialVersionUID = -3573399746781385486L;

	public DurationSpinner(Duration val, Duration start, Duration end) {
		this();
		SpinnerDateModel mod = (SpinnerDateModel) super.getModel();
		super.setValue(durToDate(val));
		mod.setStart(durToDate(start));
		mod.setEnd(durToDate(end));
	}

	public DurationSpinner() {
		super(new SpinnerDateModel());
		setEditor(new DateEditor(this, "H:mm"));
	}

	private Date durToDate(Duration val) {
		if (val == null)
			return null;
		ZonedDateTime atStartOfDay = LocalDate.ofEpochDay(0).atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime plus = atStartOfDay.plus(val);
		Instant instant = plus.toInstant();
		return Date.from(instant);
	}

	private Duration dateToDur(Date d) {
		if (d == null)
			return null;
		ZonedDateTime zdt = d.toInstant().atZone(ZoneId.systemDefault());
		LocalDateTime mdn = zdt.toLocalDate().atStartOfDay();
		return (Duration.between(mdn, zdt));
	}

	public Duration getDuration() {
		return dateToDur((Date) super.getValue());
	}

	public void setDuration(Duration value) {
		super.setValue(durToDate((Duration) value));
	}
}