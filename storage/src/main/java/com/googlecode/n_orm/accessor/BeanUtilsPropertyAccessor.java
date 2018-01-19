package com.googlecode.n_orm.accessor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtilsPropertyAccessor implements PropertyAccessor {
	
	private final Method read, write;
	
	public BeanUtilsPropertyAccessor(Field f) throws IntrospectionException {
		this(f, AccessorUtils.getPropertyDescriptor(f));
	}

	public BeanUtilsPropertyAccessor(Field f, PropertyDescriptor descriptor) throws IntrospectionException {
		if (descriptor == null) throw new IntrospectionException("No property " + f.getName() + " found in " + f.getDeclaringClass().getName());
		assert descriptor == null || descriptor.getName().equals(f.getName());
		this.read = PropertyUtils.getReadMethod(descriptor);
		this.write = PropertyUtils.getWriteMethod(descriptor);
		if (this.read == null && this.write == null) throw new IntrospectionException("No read nor write method for " + f.toGenericString());
	}

	@Override
	public Object getValue(Object self) throws Exception {
		return this.read.invoke(self);
	}

	@Override
	public void setValue(Object self, Object value) throws Exception {
		this.write.invoke(self, value);
	}

}
