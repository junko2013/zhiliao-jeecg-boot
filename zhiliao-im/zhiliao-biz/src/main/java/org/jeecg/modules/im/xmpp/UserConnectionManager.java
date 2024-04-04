package org.jeecg.modules.im.xmpp;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;

import java.util.HashMap;

@Slf4j
public class UserConnectionManager {

    private static final HashMap<String,XMPPTCPConnection> connections = new HashMap<>();

    public static synchronized XMPPTCPConnection getConnection(String username, String password,XMPPTCPConnectionConfiguration configuration) {
        final int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                XMPPTCPConnection connection = connections.get(username);
                if (connection == null || !connection.isConnected()) {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    connection = new XMPPTCPConnection(configuration);
                    connection.connect();
                    connection.login(username, password);
                    connection.addConnectionListener(new ConnectionListener() {
                        @Override
                        public void connecting(XMPPConnection connection) {
                            log.info("{}正在连接...", username);
                        }

                        @Override
                        public void connected(XMPPConnection connection) {
                            log.info("{}已连接", username);
                        }

                        @Override
                        public void authenticated(XMPPConnection connection, boolean resumed) {
                            log.info("{}的smack连接已登录...", username);
                        }

                        @Override
                        public void connectionClosed() {
                            log.info("{}的smack连接已关闭...", username);
                            disconnect(username);
                        }

                        @Override
                        public void connectionClosedOnError(Exception e) {
                            log.error("{}的smack连接遇到异常时关闭: ", username, e);
                            // 如果需要，这里可以添加重连逻辑
                        }
                    });
                    connections.put(username, connection);
                    return connection;
                }
                return connection;
            }catch (Exception e){
                log.error("连接失败，重试次数： " + retryCount, e);
                retryCount++;
                if (retryCount >= maxRetries) {
                    log.error("达到最大重试次数，停止重试");
                    break; // 达到最大重试次数，跳出循环
                }
                try {
                    Thread.sleep(1000 * retryCount); // 指数退避策略
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试过程中线程被中断", ie);
                }
            }
        }
        return null;
    }
    // 确保断开连接的方法
    public static synchronized void disconnect(String username) {
        XMPPTCPConnection connection = connections.get(username);
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
        connections.remove(username);
    }
}
