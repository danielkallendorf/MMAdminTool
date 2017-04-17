package com.kallendorf.mmcal.data;

public class TemplateDienst {

	private String displayName;
	private String listName;
	private int size;

	public String getDisplayName() {
		return this.displayName;
	}

	public String getListName() {
		return this.listName;
	}

	public int getSize() {
		return this.size;
	}

	public TemplateDienst setDisplayName(String value) {
		this.displayName = value;
		return this;
	}

	public TemplateDienst setListName(String value) {
		this.listName = value;
		return this;
	}

	public TemplateDienst setSize(int value) {
		this.size = value;
		return this;
	}

	@Override
	public String toString() {
		return getListName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TemplateDienst) {
			TemplateDienst td = (TemplateDienst) obj;
			return td.displayName.equals(displayName) && td.listName.equals(listName) && td.size == size;
		}
		return false;
	}
}
