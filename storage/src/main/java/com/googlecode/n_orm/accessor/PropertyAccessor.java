package com.googlecode.n_orm.accessor;

public interface PropertyAccessor {
	
	Object getValue(Object self) throws Exception;

	void setValue(Object self, Object value) throws Exception;

}