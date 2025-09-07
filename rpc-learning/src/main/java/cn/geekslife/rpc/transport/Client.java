package cn.geekslife.rpc.transport;

public interface Client {
    void send(Object message);
    void close();
}