package com.gwm.db.dao.entity;

import com.gwm.db.dao.sql.ColType;


public class EntityField<T> {

    private Entity<T> entity;

    private String name;//列名称

    private ColType type;//列类型
    
    private String max_length;//类最大长度

    private String comment;
    
    private boolean nullable = true;
    
    public Entity<T> getEntity() {
		return entity;
	}

	public void setEntity(Entity<T> entity) {
		this.entity = entity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColType getType() {
		return type;
	}

	public void setType(ColType type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMax_length() {
		return max_length;
	}

	public void setMax_length(String max_length) {
		this.max_length = max_length;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String toString() {
        return String.format("'%s'(%s)", this.name, this.entity.getTab_name());
    }

}
