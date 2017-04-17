package com.kallendorf.mmcal.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UploadPlan {
	
	private List<UploadGoDi> uploadGoDis = new ArrayList<>();
	
	public UploadPlan(){
	}
	
	public UploadPlan(ObjectPlan plan){
		this();
		for (ObjectGoDi obj: plan.getGoDis()) {
			UploadGoDi u=new UploadGoDi();
			u.setObjectGoDiNew(obj);
			uploadGoDis.add(u);
		}
	}

	public List<UploadGoDi> getUploadGoDis() {
		return uploadGoDis;
	}

	public UploadPlan setUploadGoDis(List<UploadGoDi> uploadGoDis) {
		this.uploadGoDis = uploadGoDis;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UploadPlan){
			UploadPlan op=(UploadPlan) obj;
			return op.uploadGoDis.equals(uploadGoDis);
		}
		return false;
	}
}


