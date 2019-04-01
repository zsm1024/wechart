package com.gwm.db.dao.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
	private int slavelength;

	public int getSlavelength() {
		return slavelength;
	}

	public void setSlavelength(int slavelength) {
		this.slavelength = slavelength;
		DataSourceSwitcher.setSlaveno(slavelength);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceSwitcher.getDataSource();
	}
}
