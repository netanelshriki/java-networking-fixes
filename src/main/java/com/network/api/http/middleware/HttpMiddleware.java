package com.network.api.http.middleware;

import java.util.concurrent.CompletableFuture;

import com.network.api.http.HttpRequest;
import com.network.api.http.HttpRequestContext;
import com.network.api.http.HttpResponse;

/**
 * Interface for HTTP middleware components.
 * 
 * <p>Middleware provides a mechanism to intercept and modify HTTP requests and responses.
 * Middleware components are executed in a pipeline, where each component can modify the
 * request before it is sent, and the response after it is received.
 * 
 * <p>Middleware is executed in the order it is added to the client for requests, and in
 * reverse order for responses. This allows for proper nesting of operations.
 */
public interface HttpMiddleware {

    /**
     * Gets the name of this middleware.
     * 
     * @return the middleware name
     */
    default String getName() {
        return getClass().getSimpleName();
    }
    
    /**
     * Gets the order of this middleware.
     * 
     * <p>Middleware with lower order values are executed before middleware
     * with higher order values.
     * 
     * @return the order value
     */
    default int getOrder() {
        return 500; // Default middle priority
    }
    
    /**
     * Called before a request is sent.
     * 
     * <p>This method can modify the request context before the request is sent.
     * 
     * @param context the request context
     */
    void beforeRequest(HttpRequestContext context);
    
    /**
     * Called after a response is received.
     * 
     * <p>This method can modify the response after it is received.
     * 
     * @param context the request context
     * @param response the response
     */
    void afterResponse(HttpRequestContext context, HttpResponse<?> response);
    
    /**
     * Processes an HTTP request through this middleware.
     * 
     * @param request the request to process
     * @param chain the middleware chain to continue processing
     * @return the HTTP response
     */
    default HttpResponse<?> process(HttpRequest request, HttpMiddlewareChain chain) {
        HttpRequestContext context = request.getContext();
        beforeRequest(context);
        HttpResponse<?> response = chain.next(request);
        afterResponse(context, response);
        return response;
    }
    
    /**
     * Processes an HTTP request through this middleware asynchronously.
     * 
     * @param request the request to process
     * @param chain the asynchronous middleware chain to continue processing
     * @return a CompletableFuture that completes with the HTTP response
     */
    default CompletableFuture<HttpResponse<?>> processAsync(HttpRequest request, HttpAsyncMiddlewareChain chain) {
        HttpRequestContext context = request.getContext();
        beforeRequest(context);
        return chain.next(request)
            .thenApply(response -> {
                afterResponse(context, response);
                return response;
            });
    }
}