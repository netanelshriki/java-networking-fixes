package com.network.proxy;

import java.lang.reflect.Method;

/**
 * Handler for method invocations on dynamic proxies.
 */
public interface MethodHandler {
    
    /**
     * Handles a method invocation on a proxy instance.
     * 
     * @param proxy the proxy instance on which the method was invoked
     * @param method the method that was invoked
     * @param args the arguments passed to the method
     * @return the result of the method invocation
     * @throws Throwable if the method invocation throws an exception
     */
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
