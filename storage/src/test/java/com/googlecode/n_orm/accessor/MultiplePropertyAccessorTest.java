package com.googlecode.n_orm.accessor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Test;

public class MultiplePropertyAccessorTest {
	
	public static class WithProp {
		public String prop;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullpaegy() {
		new MultiplePropertyAccessor(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nopaegy() {
		new MultiplePropertyAccessor(Arrays.<PropertyAccessor>asList());
	}
	
	@Test(expected=Exception.class)
	public void oneGetFailing() throws Exception {
		WithProp o = new WithProp();
		PropertyAccessor pa = mock(PropertyAccessor.class);
		when(pa.getValue(o)).thenThrow(new Exception());
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa));
		sut.getValue(o);
	}
	
	@Test(expected=Exception.class)
	public void oneSetFailing() throws Exception {
		WithProp o = new WithProp();
		String val = "a good result";
		PropertyAccessor pa = mock(PropertyAccessor.class);
		doThrow(new Exception()).when(pa).setValue(o, val);
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa));
		sut.setValue(o, val);
	}
	
	@Test
	public void oneGetCorrect() throws Exception {
		String result = "a good result";
		PropertyAccessor pa = mock(PropertyAccessor.class);
		when(pa.getValue(pa)).thenReturn(result);
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa));
		assertEquals(result, sut.getValue(pa));
	}
	
	@Test
	public void oneSetCorrect() throws Exception {
		WithProp o = new WithProp();
		String val = "a good result";
		PropertyAccessor pa = mock(PropertyAccessor.class);
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa));
		sut.setValue(o, val);
		verify(pa).setValue(o, val);
		verifyNoMoreInteractions(pa);
	}
	
	@Test
	public void oneGetCorrectAppliedTwice() throws Exception {
		String result = "a good result";
		PropertyAccessor pa = mock(PropertyAccessor.class);
		when(pa.getValue(pa)).thenReturn(result);
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa));
		assertEquals(result, sut.getValue(pa));
		assertEquals(result, sut.getValue(pa));
	}
	
	@Test
	public void oneSetCorrectAppliedTwice() throws Exception {
		WithProp o = new WithProp();
		String val = "a good result";
		PropertyAccessor pa = mock(PropertyAccessor.class);
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa));
		sut.setValue(o, val);
		sut.setValue(o, val);
		verify(pa, times(2)).setValue(o, val);
	}
	
	@Test
	public void oneGetCorrectOnceAnotherAlways() throws Exception {
		WithProp o = new WithProp();
		String val = "a good result";
		PropertyAccessor pa1 = mock(PropertyAccessor.class);
		when(pa1.getValue(o)).thenReturn(val).thenThrow(new Exception());
		PropertyAccessor pa2 = mock(PropertyAccessor.class);
		when(pa2.getValue(o)).thenReturn(val);
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa1, pa2));
		verifyZeroInteractions(pa1, pa2);
		assertEquals(val, sut.getValue(o));
		verify(pa1, times(1)).getValue(o);
		verifyNoMoreInteractions(pa1, pa2);
		assertEquals(val, sut.getValue(o));
		verify(pa1, times(2)).getValue(o);
		verify(pa2, times(1)).getValue(o);
		verifyNoMoreInteractions(pa1, pa2);
		assertEquals(val, sut.getValue(o));
		verify(pa2, times(2)).getValue(o);
		verifyNoMoreInteractions(pa1, pa2);
	}
	
	@Test
	public void oneSetCorrectOnceAnotherAlways() throws Exception {
		WithProp o = new WithProp();
		String val = "a good result";
		PropertyAccessor pa1 = mock(PropertyAccessor.class);
		doNothing().doThrow(new Exception()).when(pa1).setValue(o, val);
		PropertyAccessor pa2 = mock(PropertyAccessor.class);
		MultiplePropertyAccessor sut = new MultiplePropertyAccessor(Arrays.asList(pa1, pa2));
		verifyZeroInteractions(pa1, pa2);
		sut.setValue(o, val);
		verify(pa1, times(1)).setValue(o, val);
		verifyNoMoreInteractions(pa1, pa2);
		sut.setValue(o, val);
		verify(pa1, times(2)).setValue(o, val);
		verify(pa2, times(1)).setValue(o, val);
		verifyNoMoreInteractions(pa1, pa2);
		sut.setValue(o, val);
		verify(pa2, times(2)).setValue(o, val);
		verifyNoMoreInteractions(pa1, pa2);
	}

}

