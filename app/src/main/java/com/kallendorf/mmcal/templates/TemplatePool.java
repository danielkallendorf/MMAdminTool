package com.kallendorf.mmcal.templates;

import java.util.Calendar;
import java.util.HashMap;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.kallendorf.mmcal.data.TemplateDienst;
import com.kallendorf.mmcal.data.TemplateGoDi;
import com.kallendorf.mmcal.data.TemplatePlan;

@XmlRootElement
public class TemplatePool {

	public static TemplatePool localPool=TemplatePoolHandler.loadlocalPool();

	public Calendar lastUpdate = Calendar.getInstance();
	public HashMap<String, TemplateDienst> dienste = new HashMap<String, TemplateDienst>();
	public HashMap<String, TemplateGoDi> goDis = new HashMap<String, TemplateGoDi>();
	public HashMap<String, TemplatePlan> plaene = new HashMap<String, TemplatePlan>();
}
