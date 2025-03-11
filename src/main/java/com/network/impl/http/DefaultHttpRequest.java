package com.network.impl.http;

import com.network.api.http.HttpMethod;
import com.network.api.http.HttpRequest;
import com.network.api.http.HttpRequestContext;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link HttpRequest} interface.
 */
class DefaultHttpRequest implements HttpRequest, MutableHttpRequest {

    private final URI uri;
    private final HttpMethod method;
    private final Map<String, String> headers;
    private final byte[] body;
    private final Duration timeout;
    private final HttpRequestContext context;
    
    /**
     * Creates a new DefaultHttpRequest.
     * 
     * @param uri     the request URI
     * @param method  the HTTP method
     * @param headers the request headers
     * @param body    the request body
     * @param timeout the request timeout
     */
    DefaultHttpRequest(URI uri, HttpMethod method, Map<String, String> headers, byte[] body, Duration timeout) {
        this.uri = uri;
        this.method = method;
        this.headers = new HashMap<>(headers);
        this.body = body;
        this.timeout = timeout;
        this.context = new HttpRequestContext();
    }

    /**
     * Gets the client that created this request.
     * 
     * @return the client
     */
    DefaultHttpClient getClient() {
        // This is just a stub for compilation
        return null;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    @Override
    public Duration getTimeout() {
        return timeout;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public HttpRequestContext getContext() {
        return context;
    }

    @Override
    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    @Override
    public String toString() {
        return method + " " + uri + ", headers: " + headers.size() + 
               ", body: " + (body == null ? "null" : body.length + " bytes");
    }
}

/**
 * Interface for modifiable HTTP requests.
 */
interface MutableHttpRequest {
    /**
     * Adds a header to the request.
     * 
     * @param name the header name
     * @param value the header value
     */
    void addHeader(String name, String value);
}