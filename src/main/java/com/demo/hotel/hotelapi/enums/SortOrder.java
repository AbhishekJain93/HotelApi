package com.demo.hotel.hotelapi.enums;

public enum SortOrder {
	ASC("asc"), DESC("desc");

	private String order;

	public String getOrder() {
		return order;
	}

	private SortOrder(String order) {
		this.order = order;
	}

	public static SortOrder getSortOrder(String order) {

		if (null != order) {
			for (SortOrder element : values()) {
				if (element.getOrder().equalsIgnoreCase(order)) {
					return element;
				}
			}
		}
		return null;

	}

}
