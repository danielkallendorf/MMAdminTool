package com.kallendorf.mmcal.export;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.ExtendedProperties;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.kallendorf.mmcal.data.ObjectDienst;
import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.ObjectPlan;
import com.kallendorf.mmcal.data.UploadGoDi;
import com.kallendorf.mmcal.gui.MmGui;

public class UploadUtil {

	public static JAXBContext j;
	public static Marshaller m;
	public static Unmarshaller u;

	private static final String OBJKEY = "ObjectPart";

	static {
		try {
			j = JAXBContext.newInstance(ObjectPlan.class);
			m = j.createMarshaller();
			u = j.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("rawtypes")
	public static CalendarRequest createRequest(Calendar cal, UploadGoDi goDi) throws IOException {
		if (goDi.getObjectGoDiNew() == null && goDi.getObjectGoDiOld() == null) {
			return null;
		} else if (goDi.getObjectGoDiNew() == null) {
			return cal.events().delete("primary", goDi.getId());
		}

		Event eventN = eventFromGoDi(goDi.getObjectGoDiNew());
		if (goDi.getObjectGoDiOld() == null) {
			return cal.events().insert("primary", eventN);
		} else {
			// TODO remove overhead
			return cal.events().update("primary", goDi.getId(), eventN);
		}
	}

	public static ObjectGoDi GoDifromEvent(Event e) {
		ExtendedProperties ep=e.getExtendedProperties();
		Map<String,String> map;
		if (ep != null&&(map=ep.getPrivate())!=null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; map.containsKey(OBJKEY + i); i++) {
				sb.append(map.get(OBJKEY + i));
			}

			if (sb.length() > 0) {
				StringReader sr = new StringReader(sb.toString());
				try {
					return (ObjectGoDi) u.unmarshal(sr);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		System.out.printf("Fallback on \"%s\" at %s\n", e.getSummary(), e.getStart());
		return null;
		// TODO Fallback
	}

	public static Event eventFromGoDi(ObjectGoDi o) {
		Event e = new Event();
		e.setVisibility("public");
		e.setSummary(o.getDisplayName());
		e.setStart(new EventDateTime().setDateTime(TimeConversion.toGDateTime(o.getStart())));
		e.setEnd(new EventDateTime().setDateTime(TimeConversion.toGDateTime(o.getEnd())));
		// TODO DescriptionWriting
		String description=o.getDescription();
		if(o.getDescription()==null||o.getDescription().equals("")){
			description=generateDescription(o);
		}
		e.setDescription(description);

		List<EventAttendee> attendees = new ArrayList<>();
		Set<String> idSet = idSet(o);
		idSet.remove("primary");
		for (String id : idSet) {
			EventAttendee a = new EventAttendee();
			a.setEmail(id);
			a.setResponseStatus("accepted");
			attendees.add(a);
		}
		e.setAttendees(attendees);

		StringWriter sw = new StringWriter();
		try {
			m.marshal(o, sw);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		String s = sw.toString();
		int LIM = 1024;

		ExtendedProperties ep = new ExtendedProperties();
		Map<String, String> privMap = new HashMap<>();
		int i = 0;
		while (i * LIM < s.length()) {
			privMap.put("ObjectPart" + i, s.substring(i * LIM, Math.min((i + 1) * LIM, s.length())));
			i++;
		}
		ep.setPrivate(privMap);
		e.setExtendedProperties(ep);

		return e;
	}
	
	private static String generateDescription(ObjectGoDi o){
		StringBuilder sb= new StringBuilder();
		for (ObjectDienst d : o.getDienste()) {
			sb.append(d.getDisplayName()).append("\n");
			for (String p : d.getPersons()) {
				sb.append("\t").append(p).append("\n");
			}
		}
		return sb.toString();
	}

	public static Set<String> idSet(ObjectGoDi goDi) {
		Set<String> set = personList(goDi).stream().map(MmGui.idMap::get).collect(Collectors.toSet());
		set.add("primary");
		return set;
	}

	public static Set<String> idSet(UploadGoDi goDi) {
		Set<String> set = new HashSet<>();
		set.add("primary");
		if (goDi.getObjectGoDiOld() != null)
			set.addAll(idSet(goDi.getObjectGoDiOld()));
		if (goDi.getObjectGoDiNew() != null)
			set.addAll(idSet(goDi.getObjectGoDiNew()));

		return set;
	}

	public static List<String> personList(ObjectGoDi o) {
		List<String> names = new ArrayList<>();
		for (ObjectDienst oD : o.getDienste()) {
			names.addAll(oD.getPersons());
		}
		return names;
	}

	public static List<String> personList(UploadGoDi goDi) {
		List<String> l = new LinkedList<>();
		if (goDi.getObjectGoDiOld() != null)
			l.addAll(personList(goDi.getObjectGoDiOld()));
		if (goDi.getObjectGoDiNew() != null)
			l.addAll(personList(goDi.getObjectGoDiNew()));
		return l;
	}

	public static class TimeConversion {

		public static DateTime toGDateTime(LocalDateTime date) {
			// TODO ZoneID as option
			return DateTime
					.parseRfc3339(date.withSecond(1).atZone(ZoneId.systemDefault()).toOffsetDateTime().toString());
		}

		public static LocalDateTime toJDateTime(DateTime date) {
			// TODO ZoneID as option
			return OffsetDateTime.parse(date.toStringRfc3339()).atZoneSameInstant(ZoneId.systemDefault())
					.toLocalDateTime();
		}

	}
}
