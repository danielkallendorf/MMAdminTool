package com.kallendorf.mmcal.data;

public class UploadGoDi {

	private ObjectGoDi objectGoDiOld;
	private ObjectGoDi objectGoDiNew;
	private String id;

	public ObjectGoDi getObjectGoDiOld() {
		return objectGoDiOld;
	}

	public UploadGoDi setObjectGoDiOld(ObjectGoDi objectGoDiOld) {
		this.objectGoDiOld = objectGoDiOld;
		return this;
	}

	public ObjectGoDi getObjectGoDiNew() {
		return objectGoDiNew;
	}

	public UploadGoDi setObjectGoDiNew(ObjectGoDi objectGoDiNew) {
		this.objectGoDiNew = objectGoDiNew;
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UploadGoDi) {
			UploadGoDi ug = (UploadGoDi) obj;
			return ug.objectGoDiOld.equals(objectGoDiOld) && ug.objectGoDiNew.equals(objectGoDiNew);
		}
		return false;
	}
}
