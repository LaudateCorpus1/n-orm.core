package com.mt.storage.query;

import java.lang.reflect.Field;

import com.mt.storage.PersistingElement;

public class RangeKeyConstraintBuilder<T extends PersistingElement> {
	private final ClassConstraintBuilder<T> classConstraintBuilder;
	private final Field key;
	private final Object startValue;

	RangeKeyConstraintBuilder(ClassConstraintBuilder<T> constraintBuilder, Field key, Object startValue) {
		this.classConstraintBuilder = constraintBuilder;
		this.key = key;
		this.startValue = startValue;
	}
	
	public ClassConstraintBuilder<T> and(Object includedEndValue) {
		this.classConstraintBuilder.setSearchedKey(key, this.startValue, includedEndValue);
		return this.classConstraintBuilder;
	}

}
