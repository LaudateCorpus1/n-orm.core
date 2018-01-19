package com.googlecode.n_orm.accessor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

public class AccessorUtils {

	public static PropertyDescriptor getPropertyDescriptor(Field f) {

		for (PropertyDescriptor pd : org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(f.getDeclaringClass())) {
			if (pd.getName().equals(f.getName())) {
				return pd;
			}
		}
		
		return null;
	}
}
