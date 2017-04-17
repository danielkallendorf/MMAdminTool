package com.kallendorf.mmcal.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.calendar.CalendarRequest;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.AclRule.Scope;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.kallendorf.mmcal.MMAdminMain;
import com.kallendorf.mmcal.data.ObjectGoDi;
import com.kallendorf.mmcal.data.UploadGoDi;
import com.kallendorf.mmcal.data.UploadPlan;
import com.kallendorf.mmcal.gui.MmGui;
import com.kallendorf.mmcal.gui.UploadGui;

public class Uploader {

	public static boolean prepareUpload(UploadPlan upload) {
		List<String> names = new ArrayList<>(unknownNames(upload));
		names.sort(Comparator.naturalOrder());
		if (!names.isEmpty()) {
			String message = "Die folgenden Personen exitsieren (noch) nicht. Erstellen?";
			for (String string : names) {
				message.concat(string);
			}
			int ret = JOptionPane.showConfirmDialog(MMAdminMain.gui, message, "Erstellen?",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ret != JOptionPane.OK_OPTION)
				return false;

			for (String name : names) {
				try {
					com.google.api.services.calendar.model.Calendar calReq = new com.google.api.services.calendar.model.Calendar();
					calReq.setSummary(name);
					calReq.setDescription("Persönlicher Kalender, nur mit Gottesdiensten von " + name + ".");
					String id = MmGui.client.calendars().insert(calReq).execute().getId();

					MmGui.idMap.put(name, id);
					MmGui.namesMap.put(id, name);
					// Calendar Metadata
					CalendarListEntry calendarListEntry = new CalendarListEntry();
					calendarListEntry.setId(id);
					MmGui.client.calendarList().insert(calendarListEntry).execute();

					// Calendar Access Rule
					AclRule rule = new AclRule();
					Scope scope = new Scope();

					scope.setType("default");
					scope.setValue("");
					rule.setScope(scope);
					rule.setRole("reader");
					MmGui.client.acl().insert(id, rule).execute();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	static final int BATCH_LIMIT = 50;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void upload(UploadPlan upload) {

		UploadGui ug = new UploadGui();

		int batchCount = upload.getUploadGoDis().size() / BATCH_LIMIT+1;
		List<BatchRequest> batches = new ArrayList<>(batchCount);
		List<Set<RequestGroup>> requests= new ArrayList<>();
		for (int i = 0; i < batchCount; i++) {
			batches.add(MmGui.client.batch());
			requests.add(new HashSet<RequestGroup>());
		}

		for (int i = 0; i < upload.getUploadGoDis().size(); i++) {
			try {
				UploadGoDi uploadGoDi = upload.getUploadGoDis().get(i);
				CalendarRequest c = UploadUtil.createRequest(MmGui.client, uploadGoDi);
				ObjectGoDi o=uploadGoDi.getObjectGoDiOld();
				if(o==null){
					o=uploadGoDi.getObjectGoDiNew();
				};

				UploadGui.StatePanel statePanel = new UploadGui.StatePanel(o.getDisplayName(), o.getStart().toString());
				ug.addStatePanel(statePanel);
				RequestGroup req = new RequestGroup(c, statePanel);
				RequestGroupCallback rqc = new RequestGroupCallback(req);
				
				int batchNumber=i / BATCH_LIMIT;
				requests.get(batchNumber).add(req);
				c.queue(batches.get(batchNumber), rqc);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				// What could go wrong?
				e.printStackTrace();
			}

		}

		ug.setSize(200, 400);
		ug.setVisible(true);

		// actual uploading Code		
		batches.parallelStream().forEach(entry -> {
			int batchindex=batches.indexOf(entry);
			for (RequestGroup requestGroup : requests.get(batchindex)) {
				requestGroup.setInProgress();
			}
			try {
				entry.execute();
			} catch (IOException e) {
				for (RequestGroup req : requests.get(batchindex)) {
					req.setException(e);
				}
				e.printStackTrace();
			}
		});
		System.out.println("Ende");
	}

	public static Set<String> unknownNames(UploadPlan up) {
		Set<String> names = new HashSet<>();
		for (UploadGoDi ug : up.getUploadGoDis()) {
			names.addAll(UploadUtil.personList(ug));
		}
		names.removeIf(MmGui.idMap::containsKey);
		return names;
	}

	static class RequestGroupCallback extends JsonBatchCallback<Object> {

		final RequestGroup req;

		public RequestGroupCallback(RequestGroup req) {
			this.req = req;

		}

		@Override
		public void onSuccess(Object o, HttpHeaders responseHeaders) throws IOException {
			if (o instanceof Event) {

			} else if (o instanceof Void) {

			}
			req.setOK();
		}

		@Override
		public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
			req.setJSONError(e);
		}

	}

}
