package com.network.impl.http;

import com.network.api.http.HttpClientBuilder;
import com.network.api.http.HttpClientConfig;
import com.network.api.http.middleware.HttpMiddleware;
import com.network.serialization.JsonSerializer;
import com.network.serialization.Serializer;

import java.net.ProxySelector;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Default implementation of the {@link HttpClientConfig} interface.
 */
public class DefaultHttpClientConfig implements HttpClientConfig {

    private final URL baseUrl;
    private final Map<String, String> defaultHeaders;
    private final List<HttpMiddleware> middlewares;
    private final Duration connectTimeout;
    private final Duration requestTimeout;
    private final boolean followRedirects;
    private final ProxySelector proxy;
    private final Executor executor;
    private final Serializer serializer;
    
    /**
     * Creates a new instance of DefaultHttpClientConfig.
     * 
     * @param builder the builder used to create this configuration
     */
    DefaultHttpClientConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.defaultHeaders = new ConcurrentHashMap<>(builder.defaultHeaders);
        this.middlewares = new ArrayList<>(builder.middlewares);
        this.connectTimeout = builder.connectTimeout;
        this.requestTimeout = builder.requestTimeout;
        this.followRedirects = builder.followRedirects;
        this.proxy = builder.proxy;
        this.executor = builder.executor;
        this.serializer = builder.serializer;
    }
    
    @Override
    public Optional<URL> getBaseUrl() {
        return Optional.ofNullable(baseUrl);
    }
    
    @Override
    public Map<String, String> getDefaultHeaders() {
        return Collections.unmodifiableMap(defaultHeaders);
    }

    @Override
    public Optional<String> getDefaultContentType() {
        return Optional.ofNullable(defaultHeaders.get("Content-Type"));
    }

    @Override
    public Optional<String> getDefaultAccept() {
        return Optional.ofNullable(defaultHeaders.get("Accept"));
    }

    @Override
    public Optional<String> getUserAgent() {
        return Optional.ofNullable(defaultHeaders.get("User-Agent"));
    }

    @Override
    public boolean isFollowRedirects() {
        return followRedirects;
    }

    @Override
    public int getMaxRedirects() {
        return 10; // Default value
    }

    @Override
    public boolean isVerifySsl() {
        return true; // Default value
    }

    @Override
    public Optional<javax.net.ssl.SSLContext> getSslContext() {
        return Optional.empty(); // Not implemented in this basic version
    }

    @Override
    public int getMaxConnectionsPerRoute() {
        return 20; // Default value
    }

    @Override
    public int getMaxTotalConnections() {
        return 100; // Default value
    }

    @Override
    public Duration getConnectionTimeToLive() {
        return Duration.ofMinutes(5); // Default value
    }
    
    /**
     * Gets the list of middlewares.
     * 
     * @return the list of middlewares
     */
    public List<HttpMiddleware> getMiddlewares() {
        return Collections.unmodifiableList(middlewares);
    }
    
    /**
     * Gets the connection timeout.
     * 
     * @return the connection timeout
     */
    public Duration getConnectTimeout() {
        return connectTimeout;
    }
    
    /**
     * Gets the request timeout.
     * 
     * @return the request timeout
     */
    public Duration getRequestTimeout() {
        return requestTimeout;
    }
    
    /**
     * Gets the proxy selector.
     * 
     * @return the proxy selector
     */
    public ProxySelector getProxy() {
        return proxy;
    }
    
    /**
     * Gets the executor service used for asynchronous operations.
     * 
     * @return the executor service
     */
    public Executor getExecutor() {
        return executor;
    }
    
    @Override
    public Optional<Serializer> getSerializer() {
        return Optional.ofNullable(serializer);
    }

    @Override
    public List<HttpMiddleware> getMiddleware() {
        return getMiddlewares();
    }

    @Override
    public Optional<String> getProxyHost() {
        return Optional.empty(); // Not implemented in this basic version
    }

    @Override
    public int getProxyPort() {
        return -1; // Not configured
    }
    
    @Override
    public HttpClientBuilder toBuilder() {
        return new Builder(this);
    }
    
    /**
     * Creates a new builder instance for creating HttpClientConfig objects.
     * 
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder for creating {@link DefaultHttpClientConfig} instances.
     */
    public static class Builder implements HttpClientBuilder {
        private URL baseUrl;
        private final Map<String, String> defaultHeaders = new ConcurrentHashMap<>();
        private final List<HttpMiddleware> middlewares = new ArrayList<>();
        private Duration connectTimeout;
        private Duration requestTimeout;
        private boolean followRedirects = true;
        private ProxySelector proxy;
        private Executor executor;
        private Serializer serializer = new JsonSerializer();
        
        public Builder() {
            // Default constructor
        }
        
        public Builder(HttpClientConfig config) {
            config.getBaseUrl().ifPresent(url -> this.baseUrl = url);
            this.defaultHeaders.putAll(config.getDefaultHeaders());
            this.middlewares.addAll(config.getMiddleware());
            this.followRedirects = config.isFollowRedirects();
            // Other properties would be copied here
        }

        /**
         * Builds a new {@link DefaultHttpClientConfig} instance with the current settings.
         * 
         * @return a new DefaultHttpClientConfig instance
         */
        @Override
        public HttpClientConfig build() {
            return new DefaultHttpClientConfig(this);
        }
        
        /**
         * Builds a new {@link DefaultHttpClient} instance.
         * 
         * @return a new DefaultHttpClient instance
         */
        public DefaultHttpClient buildClient() {
            DefaultHttpClientConfig config = new DefaultHttpClientConfig(this);
            return new DefaultHttpClient(config);
        }
        
        @Override
        public HttpClientBuilder withBaseUrl(String baseUrl) {
            try {
                this.baseUrl = new URL(baseUrl);
                return this;
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid URL: " + baseUrl, e);
            }
        }
        
        @Override
        public HttpClientBuilder withBaseUrl(URL baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        
        @Override
        public HttpClientBuilder withDefaultHeader(String name, String value) {
            this.defaultHeaders.put(name, value);
            return this;
        }

        @Override
        public HttpClientBuilder withDefaultHeaders(Map<String, String> headers) {
            this.defaultHeaders.putAll(headers);
            return this;
        }

        @Override
        public HttpClientBuilder withDefaultContentType(String contentType) {
            return withDefaultHeader("Content-Type", contentType);
        }

        @Override
        public HttpClientBuilder withDefaultAccept(String accept) {
            return withDefaultHeader("Accept", accept);
        }

        @Override
        public HttpClientBuilder withUserAgent(String userAgent) {
            return withDefaultHeader("User-Agent", userAgent);
        }

        @Override
        public HttpClientBuilder withFollowRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        @Override
        public HttpClientBuilder withMaxRedirects(int maxRedirects) {
            // Implement in actual code
            return this;
        }

        @Override
        public HttpClientBuilder withVerifySsl(boolean verify) {
            // Implement in actual code
            return this;
        }

        @Override
        public HttpClientBuilder withSslContext(javax.net.ssl.SSLContext sslContext) {
            // Implement in actual code
            return this;
        }

        @Override
        public HttpClientBuilder withTrustManagerFactory(javax.net.ssl.TrustManagerFactory trustManagerFactory) {
            // Implement in actual code
            return this;
        }

        @Override
        public HttpClientBuilder withTrustStore(java.security.KeyStore trustStore) {
            // Implement in actual code
            return this;
        }

        @Override
        public HttpClientBuilder withMaxConnectionsPerRoute(int maxConnections) {
            // Implement in actual code
            return this;
        }

        @Override
        public HttpClientBuilder withMaxTotalConnections(int maxConnections) {
            // Implement in actual code
            return this;
        }

        @Override
        public HttpClientBuilder withConnectionTimeToLive(Duration ttl) {
            // Implement in actual code
            return this;
        }
        
        @Override
        public HttpClientBuilder withTimeout(Duration timeout) {
            this.connectTimeout = timeout;
            this.requestTimeout = timeout;
            return this;
        }
        
        /**
         * Sets the connection timeout.
         * 
         * @param timeout the connection timeout
         * @return this builder instance
         */
        public Builder withConnectTimeout(Duration timeout) {
            this.connectTimeout = timeout;
            return this;
        }
        
        /**
         * Sets the request timeout.
         * 
         * @param timeout the request timeout
         * @return this builder instance
         */
        public Builder withRequestTimeout(Duration timeout) {
            this.requestTimeout = timeout;
            return this;
        }
        
        /**
         * Sets the proxy selector.
         * 
         * @param proxy the proxy selector
         * @return this builder instance
         */
        public Builder withProxy(ProxySelector proxy) {
            this.proxy = proxy;
            return this;
        }
        
        /**
         * Sets the executor service.
         * 
         * @param executor the executor service
         * @return this builder instance
         */
        public Builder withExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }
        
        @Override
        public HttpClientBuilder withSerializer(Serializer serializer) {
            this.serializer = serializer;
            return this;
        }
        
        @Override
        public HttpClientBuilder withMiddleware(HttpMiddleware middleware) {
            this.middlewares.add(middleware);
            return this;
        }

        @Override
        public HttpClientBuilder withProxy(String host, int port) {
            // Implement in actual code
            return this;
        }
        
        @Override
        public HttpClientBuilder configure(Consumer<HttpClientBuilder> configurer) {
            configurer.accept(this);
            return this;
        }
        
        /**
         * Adds multiple middlewares to the client.
         * 
         * @param middlewares the middlewares to add
         * @return this builder instance
         */
        public Builder withMiddlewares(List<HttpMiddleware> middlewares) {
            this.middlewares.addAll(middlewares);
            return this;
        }

        @Override
        public HttpClient build() {
            return buildClient();
        }
    }
}