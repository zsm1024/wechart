package com.gwm.module.wechat.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTimer1 {
	Logger log = LoggerFactory.getLogger(TestTimer1.class);
	@Transactional
	public boolean testTimer(String taskname){
		log.info("=========你执行了一个定时任务========="+taskname);
		return true;
	}
}
