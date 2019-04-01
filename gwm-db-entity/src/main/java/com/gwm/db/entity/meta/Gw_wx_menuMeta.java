package com.gwm.db.entity.meta;
import com.gwm.db.dao.condition.Col;
public class Gw_wx_menuMeta {
		public static Col id = new Col("ID","Gw_wx_menu");
		public static Col menu_content = new Col("MENU_CONTENT","Gw_wx_menu");
		public static Col menu_type = new Col("MENU_TYPE","Gw_wx_menu");
		public static Col menu_url = new Col("MENU_URL","Gw_wx_menu");
		public static Col menu_key = new Col("MENU_KEY","Gw_wx_menu");
		public static Col father_menu_id = new Col("FATHER_MENU_ID","Gw_wx_menu");
		public static Col function_id = new Col("FUNCTION_ID","Gw_wx_menu");
		public static Col function_title = new Col("FUNCTION_TITLE","Gw_wx_menu");
		public static Col menu_order = new Col("MENU_ORDER","Gw_wx_menu");
}