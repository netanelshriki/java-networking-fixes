package com.network.middleware.http;

import com.network.api.http.HttpMethod;
import com.network.api.http.HttpRequestContext;
import com.network.api.http.HttpResponse;
import com.network.api.http.middleware.HttpMiddleware;

import java.util.HashSet;
import java.util.Set;

/**
 * Middleware for handling HTTP request retries.
 */
public class RetryMiddleware implements HttpMiddleware {

    private final int maxRetries;
    private final Set<Integer> retryStatusCodes;
    private final Set<Class<? extends Throwable>> retryExceptions;
    private final Set<HttpMethod> retryMethods;

    /**
     * Creates a new retry middleware with default settings.
     */
    public RetryMiddleware() {
        this(3, 
             new HashSet<>(java.util.Arrays.asList(500, 502, 503, 504)), 
             new HashSet<>(java.util.Arrays.asList(java.io.IOException.class)), 
             new HashSet<>(java.util.Arrays.asList(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.OPTIONS)));
    }

    /**
     * Creates a new retry middleware with custom settings.
     * 
     * @param maxRetries the maximum number of retry attempts
     * @param retryStatusCodes the HTTP status codes to retry
     * @param retryExceptions the exception types to retry
     * @param retryMethods the HTTP methods to retry
     */
    public RetryMiddleware(int maxRetries, 
                          Set<Integer> retryStatusCodes, 
                          Set<Class<? extends Throwable>> retryExceptions,
                          Set<HttpMethod> retryMethods) {
        this.maxRetries = maxRetries;
        this.retryStatusCodes = retryStatusCodes;
        this.retryExceptions = retryExceptions;
        this.retryMethods = retryMethods;
    }

    @Override
    public void beforeRequest(HttpRequestContext context) {
        // Initialize retry count
        context.setAttribute("retry.count", 0);
    }

    @Override
    public void afterResponse(HttpRequestContext context, HttpResponse<?> response) {
        Integer retryCount = (Integer) context.getAttribute("retry.count");
        if (retryCount == null) {
            retryCount = 0;
        }

        // Check if retry is needed
        if (retryCount < maxRetries && shouldRetry(response)) {
            // Increment retry count
            context.setAttribute("retry.count", retryCount + 1);
            throw new RetryException("Retrying due to status code " + response.getStatusCode(), 
                                    retryCount + 1);
        }
    }

    /**
     * Checks if a response should be retried.
     * 
     * @param response the response to check
     * @return true if the response should be retried, false otherwise
     */
    private boolean shouldRetry(HttpResponse<?> response) {
        if (response == null) {
            return false;
        }

        if (retryStatusCodes.contains(response.getStatusCode())) {
            return true;
        }

        return false;
    }

    /**
     * Exception thrown to indicate that a request should be retried.
     */
    public static class RetryException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private final int attempt;

        /**
         * Creates a new retry exception.
         * 
         * @param message the detail message
         * @param attempt the retry attempt number
         */
        public RetryException(String message, int attempt) {
            super(message);
            this.attempt = attempt;
        }

        /**
         * Gets the retry attempt number.
         * 
         * @return the attempt number
         */
        public int getAttempt() {
            return attempt;
        }
    }

    /**
     * Builder for creating {@link RetryMiddleware} instances.
     */
    public static class Builder {
        private int maxRetries = 3;
        private final Set<Integer> retryStatusCodes = new HashSet<>(java.util.Arrays.asList(500, 502, 503, 504));
        private final Set<Class<? extends Throwable>> retryExceptions = new HashSet<>(java.util.Arrays.asList(java.io.IOException.class));
        private final Set<HttpMethod> retryMethods = new HashSet<>(java.util.Arrays.asList(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.OPTIONS));

        /**
         * Sets the maximum number of retry attempts.
         * 
         * @param maxRetries the maximum retry attempts
         * @return this builder
         */
        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        /**
         * Sets the HTTP status codes that should trigger a retry.
         * 
         * @param statusCodes the status codes
         * @return this builder
         */
        public Builder retryStatusCodes(Integer... statusCodes) {
            this.retryStatusCodes.clear();
            java.util.Collections.addAll(this.retryStatusCodes, statusCodes);
            return this;
        }

        /**
         * Sets the exception types that should trigger a retry.
         * 
         * @param exceptions the exception types
         * @return this builder
         */
        @SafeVarargs
        public final Builder retryExceptions(Class<? extends Throwable>... exceptions) {
            this.retryExceptions.clear();
            java.util.Collections.addAll(this.retryExceptions, exceptions);
            return this;
        }

        /**
         * Sets the HTTP methods that can be retried.
         * 
         * @param methods the HTTP methods
         * @return this builder
         */
        public Builder retryMethods(HttpMethod... methods) {
            this.retryMethods.clear();
            java.util.Collections.addAll(this.retryMethods, methods);
            return this;
        }

        /**
         * Builds a new {@link RetryMiddleware} with the current settings.
         * 
         * @return a new retry middleware
         */
        public RetryMiddleware build() {
            return new RetryMiddleware(maxRetries, retryStatusCodes, retryExceptions, retryMethods);
        }
    }

    /**
     * Creates a new builder for {@link RetryMiddleware}.
     * 
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }
}