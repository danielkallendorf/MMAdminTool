package com.kallendorf.mmcal.gui;

import java.time.LocalDateTime;
import java.util.List;

import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.ObjectPlan;

public class ObjectPlanPanel extends AbstractHolderPanel<ObjectGoDiPanel, ObjectGoDi> {

	public ObjectPlanPanel() {

	}

	public ObjectPlanPanel(ObjectPlan op) {
		for (ObjectGoDi og : op.getGoDis()) {
			addItem(new ObjectGoDiPanel(og));
		}
	}

	private static final long serialVersionUID = 4770480652047964426L;

	@Override
	public ObjectGoDiPanel getDefItem() {
		List<ObjectGoDi> items = this.getPlan().getGoDis();
		try {
			if (items.size() > 0) {
				// Get Last end time
				LocalDateTime lastEndTime = items.get(items.size() - 1).getEnd();
				ObjectGoDi nextGoDi = new ObjectGoDi();
				nextGoDi.setStart(lastEndTime);
				return new ObjectGoDiPanel(nextGoDi);
			}
		} catch (NullPointerException e) {

		}

		return new ObjectGoDiPanel();
	}

	public ObjectPlan getPlan() {
		return new ObjectPlan().setGoDis(this.generateItems());
	}
}
