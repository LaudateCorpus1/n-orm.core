package com.googlecode.n_orm;

import java.util.Properties;

import com.googlecode.n_orm.PersistingElement;
import com.googlecode.n_orm.StoreSelector;


public abstract class StoreTestLauncher {
	public static StoreTestLauncher INSTANCE = new MemoryStoreTestLauncher();

	public static void registerStorePropertiesForInnerClasses(Class<?> clazz) {
		Properties props = INSTANCE.prepare(clazz);
		registerStorePropertiesForInnerClasses(clazz, props);
	}

	@SuppressWarnings("unchecked")
	public static void registerStorePropertiesForInnerClasses(Class<?> clazz,
			Properties props) {
		for (Class<?> c : clazz.getDeclaredClasses()) {
			if (PersistingElement.class.isAssignableFrom(c))
				StoreSelector.aspectOf().setPropertiesFor(
						(Class<? extends PersistingElement>) c, props);
		}
	}
	
	public abstract Properties prepare(Class<?> testClass);
}
