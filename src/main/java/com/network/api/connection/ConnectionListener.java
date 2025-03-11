package com.network.api.connection;

/**
 * Listener for connection events.
 */
public interface ConnectionListener {
    
    /**
     * Called when a connection is established.
     * 
     * @param connection the connection
     */
    void onConnect(Connection connection);
    
    /**
     * Called when a connection is closed.
     * 
     * @param connection the connection
     */
    void onDisconnect(Connection connection);
    
    /**
     * Called when a connection is closed with a reason.
     * 
     * @param connection the connection
     * @param reason the reason for disconnection
     */
    default void onDisconnect(Connection connection, String reason) {
        onDisconnect(connection);
    }
    
    /**
     * Called when data is received on a connection.
     * 
     * @param connection the connection
     * @param data the received data
     */
    default void onDataReceived(Connection connection, byte[] data) {
        // Default implementation does nothing
    }
    
    /**
     * Called when data is sent on a connection.
     * 
     * @param connection the connection
     * @param size the size of data sent in bytes
     */
    default void onDataSent(Connection connection, int size) {
        // Default implementation does nothing
    }
    
    /**
     * Called when an error occurs on a connection.
     * 
     * @param connection the connection where the error occurred
     * @param throwable the error
     */
    void onError(Connection connection, Throwable throwable);
    
    /**
     * Called when an error occurs (simplified version).
     * 
     * @param throwable the error
     */
    default void onError(Throwable throwable) {
        onError(null, throwable);
    }
}