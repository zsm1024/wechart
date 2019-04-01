package com.gwm.db.dao.util.callback;

public interface LoopCallBack<T> {
	public void invoke(int index,T obj);
}
