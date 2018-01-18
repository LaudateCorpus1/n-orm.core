package com.googlecode.n_orm.accessor;

import java.lang.reflect.Field;

public class ReflectPropertyAccessor implements PropertyAccessor {

	private final Field field;

	public ReflectPropertyAccessor(Field field) {
		this.field = field;
	}

	@Override
	public Object getValue(Object self) throws Exception {
		return this.field.get(self);
	}

	@Override
	public void setValue(Object self, Object value) throws Exception {
		this.field.set(self, value);
	}

}
