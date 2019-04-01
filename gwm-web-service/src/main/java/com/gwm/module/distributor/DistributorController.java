package com.gwm.module.distributor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gwm.db.dao.Ipage;
/**
 * 经销商模块
 * @author kaifa1
 *
 */
@RestController
@RequestMapping(value="/distributor/")
public class DistributorController {
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="findAll")
	public Ipage findAll(){
		Ipage ipage = new Ipage();
		return ipage;
	}
}
