package com.googlecode.n_orm.accessor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class MultiplePropertyAccessor implements PropertyAccessor {
	
	private final List<PropertyAccessor> propertyAccessors;
	private PropertyAccessor reader, writer;
	
	public MultiplePropertyAccessor(List<PropertyAccessor> accessors) {
		if (accessors == null || accessors.isEmpty()) throw new IllegalArgumentException();
		List<PropertyAccessor> _propertyAccessors = new LinkedList<>(accessors);
		this.propertyAccessors = Collections.unmodifiableList(_propertyAccessors);
	}

	@Override
	public Object getValue(Object self) throws Exception {
		PropertyAccessor tried = null;
		if (this.reader != null) {
			try {
				return this.reader.getValue(self);
			} catch (Throwable t) {
				tried = this.reader;
				this.reader = null;
			}
		}
		
		Exception t =  null;
		for (PropertyAccessor propertyAccessor : propertyAccessors) {
			if(propertyAccessor == tried) continue;
			try {
				Object ret = propertyAccessor.getValue(self);
				this.reader = propertyAccessor;
				return ret;
			} catch (Exception x) {
				t = x;
			}
		}
		throw t;
	}

	@Override
	public void setValue(Object self, Object value) throws Exception {
		PropertyAccessor tried = null;
		if (this.writer != null) {
			try {
				this.writer.setValue(self, value);
				return;
			} catch (Throwable t) {
				tried = this.writer;
				this.writer = null;
			}
		}
		
		Exception t =  null;
		for (PropertyAccessor propertyAccessor : propertyAccessors) {
			if(propertyAccessor == tried) continue;
			try {
				propertyAccessor.setValue(self, value);
				this.writer = propertyAccessor;
				return;
			} catch (Exception x) {
				t = x;
			}
		}
		throw t;
	}
 
}
