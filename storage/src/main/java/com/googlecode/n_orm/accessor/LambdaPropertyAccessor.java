package com.googlecode.n_orm.accessor;

import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LambdaPropertyAccessor implements PropertyAccessor {

	private final Field field;
	private final boolean staticGetter, staticSetter;
	private final MethodHandle getter;
	private final MethodHandle setter;
	
	public LambdaPropertyAccessor(Field f) throws IllegalAccessException {
		this(f, AccessorUtils.getPropertyDescriptor(f));
	}

	public LambdaPropertyAccessor(Field f, PropertyDescriptor pd) throws IllegalAccessException {
		this.field = f;
		boolean staticField = Modifier.isStatic(f.getModifiers());

		MethodHandles.Lookup lookup = MethodHandles.lookup();
		
		boolean shouldTryBeanFunctions = pd != null && !Modifier.isPublic(f.getModifiers());
		
		if (shouldTryBeanFunctions && pd.getReadMethod() != null && Modifier.isPublic(pd.getReadMethod().getModifiers())) {
			getter = lookup.unreflect(pd.getReadMethod());
			staticGetter = Modifier.isStatic(pd.getReadMethod().getModifiers());
		} else {
			getter = lookup.unreflectGetter(this.field);
			staticGetter = staticField;
		}
		

		if (shouldTryBeanFunctions && pd.getWriteMethod() != null && Modifier.isPublic(pd.getWriteMethod().getModifiers())) {
			setter = lookup.unreflect(pd.getWriteMethod());
			staticSetter = Modifier.isStatic(pd.getWriteMethod().getModifiers());
		} else {
			setter = lookup.unreflectSetter(this.field);
			staticSetter = staticField;
		}

	}

	@Override
	public Object getValue(Object obj) throws Exception {
		try {
			return staticGetter ? getter.invoke() :  getter.invoke(obj);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	@Override
	public void setValue(Object obj, Object value) throws Exception {
		try {
			if (staticSetter) setter.invoke(value); else setter.invoke(obj, value);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
}