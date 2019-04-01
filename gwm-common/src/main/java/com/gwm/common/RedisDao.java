package com.gwm.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
/**
 * 功能描述：REDIS操作接口
 * @author tianming
 *
 */
public class RedisDao {
		private StringRedisTemplate redisTemplate;
		
		
		
		 /** 
	     * 重试时间 
	     */  
	    private static final int DEFAULT_ACQUIRY_RETRY_MILLIS = 100;  
	    /** 
	     * 锁的后缀 
	     */  
	    private static final String LOCK_SUFFIX = "_redis_lock";  
	    /** 
	     * 锁的key 
	     */  
	    private String lockKey;  
	    /** 
	     * 锁超时时间，防止线程在入锁以后，防止阻塞后面的线程无法获取锁 
	     */  
	    private int expireMsecs = 60 * 1000;  
	    /** 
	     * 线程获取锁的等待时间 
	     */  
	    private int timeoutMsecs = 20 * 1000;  
	    /** 
	     * 是否锁定标志 
	     */  
	    private volatile boolean locked = false;  
	  
	  
	    public String getLockKey() {  
	        return lockKey;  
	    }  
	    /** 
	     * 封装和jedis方法 
	     * @param key 
	     * @param value 
	     * @return 
	     */  
	    private String getSet(final String key, final String value) {  
	        Object obj = redisTemplate.opsForValue().getAndSet(key,value);  
	        return obj != null ? (String) obj : null;  
	    }  
	    private boolean setNX(final String key, final String value) {  
	        return redisTemplate.opsForValue().setIfAbsent(key,value);  
	    }  
	    public synchronized boolean lock(String lkey) throws InterruptedException {  
	        int timeout = timeoutMsecs;  
	        this.lockKey=lkey+LOCK_SUFFIX;
	        while (timeout >= 0) {  
	            long expires = System.currentTimeMillis() + expireMsecs + 1;  
	            String expiresStr = String.valueOf(expires); //锁到期时间  
	            if (this.setNX(lockKey, expiresStr)) {  
	                locked = true;  
	                return true;  
	            }  
	            //redis里key的时间  
	            String currentValue = this.getString(lockKey);  
	            //判断锁是否已经过期，过期则重新设置并获取  
	            if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {  
	                //设置锁并返回旧值  
	                String oldValue = this.getSet(lockKey, expiresStr);  
	                //比较锁的时间，如果不一致则可能是其他锁已经修改了值并获取  
	                if (oldValue != null && oldValue.equals(currentValue)) {  
	                    locked = true;  
	                    return true;  
	                }  
	            }  
	            timeout -= DEFAULT_ACQUIRY_RETRY_MILLIS;  
	            //延时  
	            Thread.sleep(DEFAULT_ACQUIRY_RETRY_MILLIS);  
	        }  
	        return false;  
	    }  
	    /** 
	     * 释放获取到的锁 
	     */  
	    public synchronized void unlock() {  
	        if (locked) {  
	            redisTemplate.delete(lockKey);  
	            locked = false;  
	        }  
	    }  
		
		/**
		 * 功能描述：设置单个值
		 * @param key
		 * @param value
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void save(String key,String value){
			redisTemplate.opsForValue().set(key, value);
		}
		/**
		 * 功能描述：设置单个值
		 * @param key
		 * @param value
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void save(String key,String value,long exptime){
			save(key,value);
			redisTemplate.expire(key, exptime, TimeUnit.SECONDS);
		}
		/**
		 * 功能描述：设置多个值
		 * @param key
		 * @param values
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void save(String key,List<String> values){
			ListOperations<String, String> lop = redisTemplate.opsForList();
		    for(int i = 0 ;i<values.size();i++){
		    	lop.rightPush(key, values.get(i));
		    }
		}
		/**
		 * 功能描述：设置多个值
		 * @param key
		 * @param values
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void save(String key,List<String> values,long timeout){
			save(key,values);
			redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
		/**
		 * 功能描述：设置多个值
		 * @param key
		 * @param values
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void batchSave(Map<String,List<String>> map){
			Set<String> keys = map.keySet();
			for(String key:keys){
				ListOperations<String, String> lop = redisTemplate.opsForList();
				List<String> values = map.get(key);
				for(int i = 0 ;i<values.size();i++){
					lop.rightPush(key, values.get(i));
				}  
			}
		}
		/**
		 * 功能描述：保存map信息
		 * @param key
		 * @param values
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void save(String key,Map<String,String> values){
			HashOperations<String, String, String> opt = redisTemplate.opsForHash();
			Set<String> keys = values.keySet();
			for(String k:keys){
				opt.put(key,k,values.get(k));
			}
		}
		/**
		 * 功能描述：保存map信息
		 * @param key
		 * @param values
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void save(String key,Map<String,String> values,long timeout){
			save(key,values);
			redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	/**
	 *  功能：添加列表-map信息
	 *
	 * @Author Tao JinSong <taojinsong@dhcc.com.cn>
	 * @Date 2016/5/16 - 13:49
	 * @param key
	 * @param values
     */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void saveMapWithList(String key,Map<String,List<Map<String,String>>> values){
			ListOperations opt = redisTemplate.opsForList();
			Set<String> keys = values.keySet();
			for(String k:keys){
				opt.leftPush(key,k,values.get(k));
			}
		}
		/**
		 *  功能：添加列表-map信息
		 *
		 * @Author Tao JinSong <taojinsong@dhcc.com.cn>
		 * @Date 2016/5/16 - 13:49
		 * @param key
		 * @param values
	     */
		 public void saveMapWithList(String key,Map<String,List<Map<String,String>>> values,long timeout){
				saveMapWithList(key,values);
				redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		 }
		/**
		 * 功能描述：获取hash表中某个字段的值
		 * @param id
		 * @param key
		 * @return
		 */
		public String getHashField(String id,String key){
			HashOperations<String, String, String> opt = redisTemplate.opsForHash();
			return opt.get(id, key);
		}
		/**
		 * 功能描述：根据Key查询
		 * @param key
		 * @return
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public String getString(String key){
			return redisTemplate.opsForValue().get(key);
		}
		/**
		 * 功能描述：按照指定的格式取得符合条件的key
		 * @param str
		 * @return
		 * @author tianming
		 * @date 2015年12月24日
		 */
		public Set<String> getKeys(String pattern){
			return redisTemplate.keys(pattern);
		}

		/**
		 * 功能描述：根据Key查询
		 * @param key
		 * @return
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public Map<Object,Object> getMap(String key){
			return redisTemplate.opsForHash().entries(key);
		}
		/**
		 * 功能描述：根据Key查询
		 * @param key
		 * @return
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public List<String> getList(String key){
			 List<String> listCache = redisTemplate.opsForList().range(key, 0, -1);
			return listCache;
		}
		/**
		 * 功能描述：删除指定的key
		 * @param key
		 * @author tianming
		 * @date 2015年12月22日
		 */
		public void delete(String key){
			redisTemplate.delete(key);
		}
		/**
		 * 功能描述：判断key是否存在
		 * @param key
		 * @return
		 * @author tianming
		 * @date 2015年12月22日
		 */
	    public Boolean exists(String key) {
	    	return redisTemplate.hasKey(key);
	    }
		public StringRedisTemplate getRedisTemplate() {
			return redisTemplate;
		}
		public void setRedisTemplate(StringRedisTemplate redisTemplate) {
			this.redisTemplate = redisTemplate;
		}
	
}
