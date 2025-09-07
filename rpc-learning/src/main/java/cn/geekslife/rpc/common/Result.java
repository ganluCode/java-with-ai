package cn.geekslife.rpc.common;

public class Result {
    private Object value;
    private Throwable exception;
    
    public Result() {}
    
    public Result(Object value) {
        this.value = value;
    }
    
    public Result(Throwable exception) {
        this.exception = exception;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
    
    public Throwable getException() {
        return exception;
    }
    
    public void setException(Throwable exception) {
        this.exception = exception;
    }
    
    public boolean hasException() {
        return exception != null;
    }
    
    public Object recreate() throws Throwable {
        if (exception != null) {
            throw exception;
        }
        return value;
    }
}