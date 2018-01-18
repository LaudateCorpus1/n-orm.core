package com.googlecode.n_orm.accessor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LambdaPropertyAccessor implements PropertyAccessor {

	private final Field field;
	private final boolean staticField;
	private final MethodHandle getter;
	private final MethodHandle setter;

	public LambdaPropertyAccessor(Field f) throws IllegalAccessException {
		this.field = f;
		this.staticField = Modifier.isStatic(f.getModifiers());

		MethodHandles.Lookup lookup = MethodHandles.lookup();
		getter = lookup.unreflectGetter(this.field);
		setter = lookup.unreflectSetter(this.field);
	}

	@Override
	public Object getValue(Object obj) throws Exception {
		try {
			return staticField ? getter.invoke() :  getter.invoke(obj);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	@Override
	public void setValue(Object obj, Object value) throws Exception {
		try {
			if (staticField) setter.invoke(value); else setter.invoke(obj, value);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
}