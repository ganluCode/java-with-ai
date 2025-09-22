package cn.geekslife.rpc.common;

import java.util.Map;

public class URL {
    private String protocol;
    private String host;
    private int port;
    private String path;
    private Map<String, String> parameters;
    
    public URL() {}
    
    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
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
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public Map<String, String> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public String getParameter(String key) {
        return parameters != null ? parameters.get(key) : null;
    }
    
    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        return value != null ? value : defaultValue;
    }
    
    public int getParameter(String key, int defaultValue) {
        String value = getParameter(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
    
    public String getServiceInterface() {
        return getParameter("interface");
    }
    
    @Override
    public String toString() {
        return protocol + "://" + host + ":" + port + "/" + path + "?" + parameters;
    }
}