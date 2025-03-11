package com.network.api.http;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP request to be sent by an {@link HttpClient}.
 * <p>
 * This interface provides methods for accessing information about the request,
 * such as the URI, method, headers, and body.
 * </p>
 */
public interface HttpRequest {

    /**
     * Gets the URI for this request.
     * 
     * @return the URI
     */
    URI getUri();
    
    /**
     * Gets the HTTP method for this request.
     * 
     * @return the method
     */
    HttpMethod getMethod();
    
    /**
     * Gets all headers for this request.
     * 
     * @return the headers
     */
    Map<String, String> getHeaders();
    
    /**
     * Gets a specific header value.
     * 
     * @param name the header name
     * @return the header value, or null if not present
     */
    String getHeader(String name);
    
    /**
     * Checks if a specific header is present.
     * 
     * @param name the header name
     * @return true if the header is present, false otherwise
     */
    boolean hasHeader(String name);
    
    /**
     * Gets the timeout for this request.
     * 
     * @return the timeout, or null if not set
     */
    Duration getTimeout();
    
    /**
     * Gets the body of the request.
     * 
     * @return the body, or null if there is no body
     */
    byte[] getBody();
    
    /**
     * Checks if the request has a body.
     * 
     * @return true if the request has a body, false otherwise
     */
    default boolean hasBody() {
        byte[] body = getBody();
        return body != null && body.length > 0;
    }
    
    /**
     * Gets the body as a string using UTF-8 encoding.
     * 
     * @return the body as string, or empty string if no body
     */
    default String getBodyAsString() {
        byte[] body = getBody();
        return body != null ? new String(body) : "";
    }
    
    /**
     * Gets the context for this request.
     * 
     * @return the context
     */
    default HttpRequestContext getContext() {
        return new HttpRequestContext();
    }
}
