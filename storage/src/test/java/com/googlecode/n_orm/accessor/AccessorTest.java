package com.googlecode.n_orm.accessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.googlecode.n_orm.PersistingElement;
import com.googlecode.n_orm.utils.LongAdder;

@RunWith(Parameterized.class)
public class AccessorTest {
	
	public static interface Element {
		String value();
		
		void value(String val);
	}

	public static class SimpleClass implements Element {
		public String prop;

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
	}
	
	public static class BeanClass implements Element {
		private String prop;

		public String getProp() {
			return prop;
		}

		public void setProp(String prop) {
			this.prop = prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}

	public static class InheritingClass extends BeanClass implements Element {
	}
	
	public static class PrivateReadBeanClass implements Element {
		private String prop;

		private String getProp() {
			return prop;
		}

		public void setProp(String prop) {
			this.prop = prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class PrivateWriteBeanClass implements Element {
		private String prop;

		public String getProp() {
			return prop;
		}

		private void setProp(String prop) {
			this.prop = prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class ReadBeanClass implements Element {
		private String prop;

		public String getProp() {
			return prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class WriteBeanClass implements Element {
		private String prop;

		public void setProp(String prop) {
			this.prop = prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}

	public static class StaticSimpleClass implements Element {
		public static String prop;

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
	}
	
	public static class StaticBeanClass implements Element {
		private static String prop;

		public String getProp() {
			return prop;
		}

		public void setProp(String _prop) {
			prop = _prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class StaticStaticBeanClass implements Element {
		private static String prop;

		public static String getProp() {
			return prop;
		}

		public static void setProp(String _prop) {
			prop = _prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class StaticReadBeanClass implements Element {
		private static String prop;

		public String getProp() {
			return prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class StaticStaticReadBeanClass implements Element {
		private static String prop;

		public static String getProp() {
			return prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class StaticWriteBeanClass implements Element {
		private static String prop;

		public void setProp(String _prop) {
			prop = _prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}
	
	public static class StaticStaticWriteBeanClass implements Element {
		private static String prop;

		public static void setProp(String _prop) {
			prop = _prop;
		}

		@Override
		public String value() {
			return prop;
		}

		@Override
		public void value(String val) {
			prop = val;
		}
		
	}

	@Parameters
	public static List<Object[]> getParameters() {
		List<Class<? extends PropertyAccessor>> accessors = Arrays.asList(GencodePropertyAccessor.class, LambdaPropertyAccessor.class, ReflectPropertyAccessor.class, BeanUtilsPropertyAccessor.class);
		List<Class<? extends Element>> beans = Arrays.asList(SimpleClass.class, BeanClass.class, InheritingClass.class, PrivateReadBeanClass.class, PrivateWriteBeanClass.class, ReadBeanClass.class, WriteBeanClass.class, StaticSimpleClass.class, StaticBeanClass.class, StaticStaticBeanClass.class, StaticReadBeanClass.class, StaticStaticReadBeanClass.class, StaticWriteBeanClass.class, StaticStaticWriteBeanClass.class);
		int missing;
		assert (missing = AccessorTest.class.getDeclaredClasses().length - beans.size()) == 2 /* Element and Sample are not counted */ : "" + missing + " missing classes";
		
		Map<Class<? extends PropertyAccessor>, List<Class<? extends Element>>> successfulReaders = new HashMap<>();
		successfulReaders.put(GencodePropertyAccessor.class, Arrays.asList(SimpleClass.class, BeanClass.class, InheritingClass.class, PrivateWriteBeanClass.class, ReadBeanClass.class, StaticSimpleClass.class, StaticBeanClass.class, StaticReadBeanClass.class));
		successfulReaders.put(LambdaPropertyAccessor.class, Arrays.asList(SimpleClass.class, BeanClass.class, InheritingClass.class, PrivateReadBeanClass.class, PrivateWriteBeanClass.class, ReadBeanClass.class, WriteBeanClass.class, StaticSimpleClass.class, StaticBeanClass.class, StaticStaticBeanClass.class, StaticReadBeanClass.class, StaticStaticReadBeanClass.class, StaticWriteBeanClass.class, StaticStaticWriteBeanClass.class));
		successfulReaders.put(ReflectPropertyAccessor.class, Arrays.asList(SimpleClass.class, BeanClass.class, InheritingClass.class, PrivateReadBeanClass.class, PrivateWriteBeanClass.class, ReadBeanClass.class, WriteBeanClass.class, StaticSimpleClass.class, StaticBeanClass.class, StaticStaticBeanClass.class, StaticReadBeanClass.class, StaticStaticReadBeanClass.class, StaticWriteBeanClass.class, StaticStaticWriteBeanClass.class));
		successfulReaders.put(BeanUtilsPropertyAccessor.class, Arrays.asList(BeanClass.class, InheritingClass.class, PrivateWriteBeanClass.class, ReadBeanClass.class, StaticBeanClass.class, StaticReadBeanClass.class));
		
		Map<Class<? extends PropertyAccessor>, List<Class<? extends Element>>> successfulWriters = new HashMap<>();
		successfulWriters.put(GencodePropertyAccessor.class, Arrays.asList(SimpleClass.class, BeanClass.class, InheritingClass.class, PrivateReadBeanClass.class, WriteBeanClass.class, StaticSimpleClass.class, StaticBeanClass.class, StaticWriteBeanClass.class));
		successfulWriters.put(LambdaPropertyAccessor.class, Arrays.asList(SimpleClass.class, BeanClass.class, InheritingClass.class, PrivateReadBeanClass.class, PrivateWriteBeanClass.class, ReadBeanClass.class, WriteBeanClass.class, StaticSimpleClass.class, StaticBeanClass.class, StaticStaticBeanClass.class, StaticReadBeanClass.class, StaticStaticReadBeanClass.class, StaticWriteBeanClass.class, StaticStaticWriteBeanClass.class));
		successfulWriters.put(ReflectPropertyAccessor.class, Arrays.asList(SimpleClass.class, BeanClass.class, InheritingClass.class, PrivateReadBeanClass.class, PrivateWriteBeanClass.class, ReadBeanClass.class, WriteBeanClass.class, StaticSimpleClass.class, StaticBeanClass.class, StaticStaticBeanClass.class, StaticReadBeanClass.class, StaticStaticReadBeanClass.class, StaticWriteBeanClass.class, StaticStaticWriteBeanClass.class));
		successfulWriters.put(BeanUtilsPropertyAccessor.class, Arrays.asList(BeanClass.class, InheritingClass.class, PrivateReadBeanClass.class, WriteBeanClass.class, StaticBeanClass.class, StaticWriteBeanClass.class));

		List<Object[]> ret = new LinkedList<>();
		for (Class<? extends PropertyAccessor> accessor : accessors) {
			for (Class<? extends Element> bean : beans) {
				boolean reader = successfulReaders.get(accessor).contains(bean);
				boolean writer = successfulWriters.get(accessor).contains(bean);
				
				ret.add(new Object [] {accessor, bean, reader, writer});
			}
		}
		
		return ret;
	}
	
	private static class Sample {
		private LongAdder time = new LongAdder(), samples = new LongAdder();
		
		private void add(long duration) {
			time.add(duration);
			samples.add(1);
		}

		public double avg() {
			return time.doubleValue()/samples.doubleValue();
		}
		
		public String toString() {
			return Double.toString(this.avg());
		}
	}

	private static ConcurrentHashMap<Class<? extends PropertyAccessor>, Sample> readTime = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<Class<? extends PropertyAccessor>, Sample> writeTime = new ConcurrentHashMap<>();
	
	protected static void addSample(Class<? extends PropertyAccessor> accessor, boolean write, long duration) {
		ConcurrentHashMap<Class<? extends PropertyAccessor>, Sample> reg = write ? writeTime : readTime;
		Sample s = reg.get(accessor);
		if (s == null) {
			s = new Sample();
			Sample old = reg.putIfAbsent(accessor, s);
			if (old != null) s = old;
		}
		s.add(duration);
	}
	
	@AfterClass
	public static void printoutChampion() {
		double minRead = Double.MAX_VALUE, minWrite = Double.MAX_VALUE, min = Double.MAX_VALUE;
		
		Class<? extends PropertyAccessor> readChampion = null, writeChampion = null, champion = null;
		for (Entry<Class<? extends PropertyAccessor>, Sample> readStats : readTime.entrySet()) {
			double res = readStats.getValue().avg();
			if (minRead > res) {
				minRead = res;
				readChampion = readStats.getKey();
			}
		}
		
		System.out.println("Best read property accessor is " + readChampion + " with " + minRead);
		
		System.out.println(readTime);
		
		for (Entry<Class<? extends PropertyAccessor>, Sample> writeStats : writeTime.entrySet()) {
			double res = writeStats.getValue().avg();
			if (minWrite > res) {
				minWrite = res;
				writeChampion = writeStats.getKey();
			}
		}
		
		System.out.println("Best write property accessor is " + writeChampion + " with " + minWrite);
		
		System.out.println(writeTime);
	}
	
	private Field field;
	private Class<? extends PropertyAccessor> accessorClass;
	private Class<? extends Element> beanClass;
	private PropertyAccessor propertyAccessor;
	private Element bean;
	private boolean read, write;
	
	public AccessorTest(Class<? extends PropertyAccessor> accessorClass, Class<? extends Element> beanClass, boolean canRead, boolean canWrite) throws Exception {
		this.accessorClass = accessorClass;
		this.beanClass = beanClass;
		NoSuchFieldException nsf = null;
		try {
			Class<?> clazz = beanClass;
			Field f = null;
			while(f == null && Element.class.isAssignableFrom(clazz)) {
				try {
					f = clazz.getDeclaredField("prop");
				} catch (NoSuchFieldException x) {
					if (nsf == null) nsf = x;
					clazz = clazz.getSuperclass();
				}
			}
			if (f == null && nsf != null) throw nsf;
			this.field = f;
		} catch (Exception x) {
			throw new Exception("Cannot find field 'prop' in " + beanClass.getName(), x);
		}
		this.field.setAccessible(true);
		try {
			this.propertyAccessor = accessorClass.getConstructor(Field.class).newInstance(this.field);
			if (!canRead && !canWrite) {
				fail("" + accessorClass + " should not be build as long as it is not supposed to access " + field);
			}
		} catch (Exception x) {
			if (canRead || canWrite) {
				throw x;
			}
			this.propertyAccessor = null;
		}
		this.bean = beanClass.newInstance();
		
		this.read = canRead;
		this.write = canWrite;
	}
	
	@Test
	public void read() throws Exception {
		if (this.read) {
			String testValue = "gsdyfcinhufoz fuie gfefgfncio";
			this.bean.value(testValue);
			Object res = this.propertyAccessor.getValue(this.bean);
			assertEquals("" + this.beanClass + " did not properly read value of " + this.field.toGenericString(), testValue, res);
		} else if (this.propertyAccessor != null) {
			try {
				this.propertyAccessor.getValue(this.bean);
				fail("" + this.accessorClass + " is not supposed to read-access " + field);
			} catch (Exception x) {}
		}
	}
	
	@Test
	public void readNull() throws Exception {
		if (this.read) {
			this.bean.value(null);
			Object res = this.propertyAccessor.getValue(this.bean);
			assertNull("" + this.beanClass + " did not properly read null value of " + this.field.toGenericString(), res);
		}
	}
	
	@Test
	public void readPerf() throws Exception {
		if (this.read) {
			long start = System.nanoTime();
			for (int i = 0; i < 1_000_000; i++) {
				String expected = Integer.toString(i);
				Element bean = this.beanClass.newInstance();
				bean.value(expected);
				Object res = this.propertyAccessor.getValue(bean);
				assertEquals("" + this.beanClass + " did not properly read value of " + this.field.toGenericString(), expected, res);
			}
			long duration = System.nanoTime() - start;
			addSample(this.accessorClass, false, duration);
		}
	}
	
	@Test
	public void write() throws Exception {
		String testValue = "gsdyfci h ugiegzeiuEZ nhufoz fuie gfefgfncio";
		if (this.write) {
			this.bean.value("a stupid value");
			 this.propertyAccessor.setValue(this.bean, testValue);
			assertEquals("" + this.beanClass + " did not properly write value in " + this.field.toGenericString(), testValue, this.bean.value());
		} else if (this.propertyAccessor != null) {
			try {
				this.propertyAccessor.setValue(this.bean, testValue);
				fail("" + this.accessorClass + " is not supposed to write-access " + field);
			} catch (Exception x) {}
		}
	}
	
	@Test
	public void writeNull() throws Exception {
		if (this.write) {
			this.bean.value("a non-null value");
			this.propertyAccessor.setValue(this.bean, null);
			assertNull("" + this.beanClass + " did not properly write null value in " + this.field.toGenericString(), this.bean.value());
		}
	}
	
	@Test
	public void writePerf() throws Exception {
		if (this.write) {
			long start = System.nanoTime();
			for (int i = 0; i < 1_000_000; i++) {
				String expected = Integer.toString(i);
				Element bean = this.beanClass.newInstance();
				this.propertyAccessor.setValue(bean, expected);
				assertEquals("" + this.beanClass + " did not properly write value in " + this.field.toGenericString(), expected, bean.value());
			}
			long duration = System.nanoTime() - start;
			addSample(this.accessorClass, true, duration);
		}
	}
	
}
