package com.gwm.db.dao.util;

import java.util.List;

import com.gwm.db.dao.util.callback.LoopCallBack;

public class Loop {
	/**
	 * @param t
	 * @param callBack
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T>  void loop(T t,LoopCallBack callBack){
		if(t instanceof List){
			List l = (List )t;
			for(int i = 0;i<l.size();i++){
				callBack.invoke(i, l.get(i));
			}
		}else if(t.getClass().isArray()){
			
		}
	}
}
