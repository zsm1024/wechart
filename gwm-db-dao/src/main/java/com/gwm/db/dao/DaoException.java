package com.gwm.db.dao;

public class DaoException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Exception ex = null;
	
	public DaoException(Exception ex){
		this.ex = ex;
	}
	public Exception getEx() {
		return ex;
	}

	public void setEx(Exception ex) {
		this.ex = ex;
	}
	
}
