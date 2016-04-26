package com.dynatrace.diagnostics.report.json;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Proxy handler which intercepts all invocations on the Object and
 * ignores any UnsupportedOperationException being thrown.
 *
 * @author dominik.stadler
 */
class PreventUnsupportedOperationExceptionProxy implements InvocationHandler {

	private Object obj;

	public static Object newInstance(Object obj) {
		return java.lang.reflect.Proxy.newProxyInstance(
				obj.getClass().getClassLoader(),
				obj.getClass().getInterfaces(),
				new PreventUnsupportedOperationExceptionProxy(obj));
	}

	private PreventUnsupportedOperationExceptionProxy(Object obj) {
		this.obj = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			return method.invoke(obj, args);
		} catch (InvocationTargetException e) {
			if(e.getCause() instanceof UnsupportedOperationException) {
				return null;
			}

			throw e;
		}
	}
}
