#!/bin/bash

# This script applies the fixes to the original repository

# Clone the original repository
git clone https://github.com/netanelshriki/advanced-java-networking.git
cd advanced-java-networking

# Create fixes branch
git checkout -b fixes

# Copy the fixed files
mkdir -p src/main/java/com/network/api/http
mkdir -p src/main/java/com/network/api/connection
mkdir -p src/main/java/com/network/api/http/middleware
mkdir -p src/main/java/com/network/impl/http
mkdir -p src/main/java/com/network/middleware/http
mkdir -p src/main/java/com/network/proxy

# Copy the fixed HttpResponse interface
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/http/HttpResponse.java > src/main/java/com/network/api/http/HttpResponse.java

# Copy the HttpMethod enum
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/http/HttpMethod.java > src/main/java/com/network/api/http/HttpMethod.java

# Copy the updated HttpRequest interface
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/http/HttpRequest.java > src/main/java/com/network/api/http/HttpRequest.java

# Copy the fixed DefaultHttpResponse implementation
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/impl/http/DefaultHttpResponse.java > src/main/java/com/network/impl/http/DefaultHttpResponse.java

# Copy the fixed DefaultHttpClientConfig implementation
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/impl/http/DefaultHttpClientConfig.java > src/main/java/com/network/impl/http/DefaultHttpClientConfig.java

# Copy the HttpResponseException to handle generic HttpResponse
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/http/HttpResponseException.java > src/main/java/com/network/api/http/HttpResponseException.java

# Copy the fixed DefaultHttpRequest implementation
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/impl/http/DefaultHttpRequest.java > src/main/java/com/network/impl/http/DefaultHttpRequest.java

# Copy the Protocol enum
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/connection/Protocol.java > src/main/java/com/network/api/connection/Protocol.java

# Copy the updated Connection interface
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/connection/Connection.java > src/main/java/com/network/api/connection/Connection.java

# Copy the updated ConnectionListener interface
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/connection/ConnectionListener.java > src/main/java/com/network/api/connection/ConnectionListener.java

# Copy the updated HttpRequestContext
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/http/HttpRequestContext.java > src/main/java/com/network/api/http/HttpRequestContext.java

# Copy the updated HttpMiddleware interface
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/api/http/middleware/HttpMiddleware.java > src/main/java/com/network/api/http/middleware/HttpMiddleware.java

# Copy the RetryMiddleware with RetryException
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/middleware/http/RetryMiddleware.java > src/main/java/com/network/middleware/http/RetryMiddleware.java

# Copy the MethodHandler interface
curl -s https://raw.githubusercontent.com/netanelshriki/java-networking-fixes/main/src/main/java/com/network/proxy/MethodHandler.java > src/main/java/com/network/proxy/MethodHandler.java

# Commit the changes
git add .
git commit -m "Apply fixes to compilation errors"

# Push the changes to a new branch
git push origin fixes

echo "Fixes have been applied to the 'fixes' branch in the original repository"
