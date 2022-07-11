package com.xj.tools.ftp;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author: xujie
 * @date: 2022/7/7
 **/
public class FtpConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static FtpConfig create() {
        return new FtpConfig();
    }

    /**
     * 主机ip
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 用户
     */
    private String user;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 编码
     */
    private Charset charset;

    /**
     * 连接超时时长,单位毫秒
     */
    private long connectionTimeout;

    /**
     * Socket连接超时时长,单位毫秒
     */
    private long socketTimeout;

    public FtpConfig() {
    }


    public FtpConfig(String host, int port, String user, String password, Charset charset) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.charset = charset;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(long socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
