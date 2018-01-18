package com.googlecode.n_orm.accessor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtilsPropertyAccessor implements PropertyAccessor {
	
	private final PropertyDescriptor descriptor;
	private final Method read, write;

	public BeanUtilsPropertyAccessor(Field f) throws IntrospectionException {
		PropertyDescriptor _descriptor = null;
		for(PropertyDescriptor descr : PropertyUtils.getPropertyDescriptors(f.getDeclaringClass())) {
			if (f.getName().equals(descr.getName())) {
				_descriptor = descr;
				break;
			}
		}
		if (_descriptor == null) throw new IntrospectionException("No property " + f.getName() + " found in " + f.getDeclaringClass().getName());
		this.descriptor = _descriptor;
		this.read = PropertyUtils.getReadMethod(descriptor);
		this.write = PropertyUtils.getWriteMethod(descriptor);
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
