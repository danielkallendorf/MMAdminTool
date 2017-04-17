package com.kallendorf.mmcal.gui;

import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.ObjectPlan;

public class ObjectPlanPanel extends AbstractHolderPanel<ObjectGoDiPanel, ObjectGoDi> {

	public ObjectPlanPanel(){
		
	}
	
	public ObjectPlanPanel(ObjectPlan op){
		for (ObjectGoDi og : op.getGoDis()) {
			addItem(new ObjectGoDiPanel(og));
		}
	}
	
	private static final long serialVersionUID = 4770480652047964426L;

	@Override
	public ObjectGoDiPanel getDefItem() {
		return new ObjectGoDiPanel();
	}
	
	public ObjectPlan getPlan(){
		return new ObjectPlan().setGoDis(this.generateItems());
	}
}
