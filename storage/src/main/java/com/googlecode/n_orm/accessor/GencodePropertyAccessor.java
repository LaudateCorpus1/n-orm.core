package com.googlecode.n_orm.accessor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.beanutils.PropertyUtils;
import org.mdkt.compiler.InMemoryJavaCompiler;

public class GencodePropertyAccessor implements PropertyAccessor {

	private PropertyAccessor accessor;

	public GencodePropertyAccessor(Field f) throws Exception {
		String readAccess = null, writeAccess = null;
		final String accessPackageName = "com.googlecode.n_orm.genaccess." + f.getDeclaringClass().getPackage().getName();
		final String sourceClassQName = f.getDeclaringClass().getCanonicalName();
		final String accessClassName = f.getDeclaringClass().getSimpleName() + '$' + f.getName() + "_Accessor";
		final String accessClassQName = accessPackageName + '.' + accessClassName;
		final String castedSelf = "((" + sourceClassQName + ") self)";
		final String castedValue = "((" + f.getType().getCanonicalName() + ")value)";

		if (Modifier.isPublic(f.getModifiers())) {
			String access = (Modifier.isStatic(f.getModifiers()) ? sourceClassQName : castedSelf) + '.' + f.getName();
			readAccess = "return " + access;
			writeAccess = Modifier.isFinal(f.getModifiers()) ? "throw new IllegalAccessError()" : access + " = " + castedValue;
		} else {
			for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(f.getDeclaringClass())) {
				if (pd.getName().equals(f.getName())) {
					Method readM = pd.getReadMethod(); if (readM != null && !Modifier.isPublic(readM.getModifiers())) readM = null;
					Method writeM = pd.getWriteMethod(); if (writeM != null && !Modifier.isPublic(writeM.getModifiers())) writeM = null;
					if (readM == null && writeM == null) throw new IllegalAccessException();
					if (readM == null) {
						readAccess = "throw new IllegalAccessException()";
					} else {
						readAccess = "return " + (Modifier.isStatic(readM.getModifiers()) ? sourceClassQName : castedSelf) + '.' + readM.getName() + "()";
					}
					
					if (writeM == null) {
						writeAccess = "throw new IllegalAccessException()";
					} else {
						writeAccess = (Modifier.isStatic(writeM.getModifiers()) ? sourceClassQName : castedSelf) + '.' + writeM.getName() + castedValue;
					}
					break;
				}
			}
			if (readAccess == null || writeAccess == null) throw new IllegalAccessException("Can't find any accessor for " + f);
		}
		
        final String source = "package " + accessPackageName + ";\n"
                + "public final class " + accessClassName + " implements com.googlecode.n_orm.accessor.PropertyAccessor {\n"
                        + "    public Object getValue(Object self) throws Exception {\n"
                        + "        " + readAccess + ";\n"
                        + "    }\n"
                        + "    @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n"
                        + "    public void setValue(Object self, Object value) throws Exception {\n"
                        + "        " + writeAccess + ";\n"
                        + "    }\n"
                + "}";
        
		@SuppressWarnings("unchecked")
		Class<? extends PropertyAccessor> compiledClass = (Class<? extends PropertyAccessor>) InMemoryJavaCompiler
			.newInstance().useParentClassLoader(GencodePropertyAccessor.class.getClassLoader())
			.compile(accessClassQName, source);
        accessor = compiledClass.newInstance();
	}

	@Override
	public Object getValue(Object self) throws Exception {
		return accessor.getValue(self);
	}

	@Override
	public void setValue(Object self, Object value) throws Exception {
		accessor.setValue(self, value);
	}

}
