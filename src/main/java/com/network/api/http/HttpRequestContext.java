package com.network.api.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Context for HTTP requests.
 * 
 * <p>This class provides a way to store and retrieve arbitrary data associated
 * with an HTTP request. It can be used by middleware and other components to
 * share data during request processing.
 */
public class HttpRequestContext {
    
    private final Map<String, Object> attributes = new HashMap<>();
    private HttpRequest request;
    private HttpClient client;
    
    /**
     * Creates a new empty context.
     */
    public HttpRequestContext() {
        // Empty constructor
    }
    
    /**
     * Creates a new context with the specified request and client.
     * 
     * @param request the HTTP request
     * @param client the HTTP client
     */
    public HttpRequestContext(HttpRequest request, HttpClient client) {
        this.request = request;
        this.client = client;
    }
    
    /**
     * Sets the request for this context.
     * 
     * @param request the HTTP request
     */
    public void setRequest(HttpRequest request) {
        this.request = request;
    }
    
    /**
     * Gets the request associated with this context.
     * 
     * @return the HTTP request
     */
    public HttpRequest getRequest() {
        return request;
    }
    
    /**
     * Sets the client for this context.
     * 
     * @param client the HTTP client
     */
    public void setClient(HttpClient client) {
        this.client = client;
    }
    
    /**
     * Gets the client associated with this context.
     * 
     * @return the HTTP client
     */
    public HttpClient getClient() {
        return client;
    }
    
    /**
     * Sets an attribute in this context.
     * 
     * @param key the attribute key
     * @param value the attribute value
     * @return this context for method chaining
     * @throws IllegalArgumentException if key is null
     */
    public HttpRequestContext setAttribute(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Attribute key must not be null");
        }
        attributes.put(key, value);
        return this;
    }
    
    /**
     * Gets an attribute from this context.
     * 
     * @param <T> the expected type of the attribute value
     * @param key the attribute key
     * @param type the class of the expected type
     * @return an Optional containing the attribute value, or empty if not found or of wrong type
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttribute(String key, Class<T> type) {
        if (key == null || type == null) {
            return Optional.empty();
        }
        
        Object value = attributes.get(key);
        if (value == null) {
            return Optional.empty();
        }
        
        if (type.isInstance(value)) {
            return Optional.of((T) value);
        }
        
        return Optional.empty();
    }
    
    /**
     * Gets an attribute from this context.
     * 
     * @param key the attribute key
     * @return the attribute value, or null if not found
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    /**
     * Removes an attribute from this context.
     * 
     * @param key the attribute key
     * @return the previous value of the attribute, or null if not found
     */
    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }
    
    /**
     * Checks if this context contains an attribute with the specified key.
     * 
     * @param key the attribute key
     * @return true if the attribute exists, false otherwise
     */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
    
    /**
     * Gets all attributes in this context.
     * 
     * @return an unmodifiable view of the attributes
     */
    public Map<String, Object> getAttributes() {
        return Map.copyOf(attributes);
    }
    
    /**
     * Clears all attributes from this context.
     */
    public void clear() {
        attributes.clear();
    }
}