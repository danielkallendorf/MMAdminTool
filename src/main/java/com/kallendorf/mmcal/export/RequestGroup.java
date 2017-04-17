package com.kallendorf.mmcal.export;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.services.calendar.CalendarRequest;
import com.kallendorf.mmcal.gui.UploadGui;

@SuppressWarnings("rawtypes")
class RequestGroup{
	
	final CalendarRequest req;
	final UploadGui.StatePanel guiCmp;
	private RequestState state=RequestState.NO_STATE;
	
	public RequestGroup(CalendarRequest req, UploadGui.StatePanel guiCmp){
		this.req=req;
		this.guiCmp=guiCmp;
	}
	
	public void setInProgress() {
		guiCmp.setToolTipText("In Progress");
		state=RequestState.IN_PROGRESS;
		updStateColor();
	}
	
	public void setOK() {
		guiCmp.setToolTipText("OK");
		state=RequestState.OK;
		updStateColor();
	}

	public void setException(Exception e) {
		guiCmp.setToolTipText(e.toString());
		state=RequestState.SOFT_ERROR;
		updStateColor();
		// TODO differ by Error Type
	}

	public void setJSONError(GoogleJsonError e) {
		try {
			guiCmp.setToolTipText(e.toPrettyString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		state=RequestState.HARD_ERROR;
		updStateColor();
		
		// TODO differ by HTTP CODE
	}
	
	public RequestState getState(){
		return state;
	}
	
	private void updStateColor(){
		guiCmp.setStateColor(state);
	}
}
