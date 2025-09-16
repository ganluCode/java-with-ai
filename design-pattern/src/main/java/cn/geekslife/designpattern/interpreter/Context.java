package cn.geekslife.designpattern.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文类
 * 包含解释器之外的一些全局信息
 */
public class Context {
    private Map<String, Object> variables = new HashMap<>();
    private String input;
    private String output;
    
    public Context() {
    }
    
    public Context(String input) {
        this.input = input;
    }
    
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }
    
    public Object getVariable(String name) {
        return variables.get(name);
    }
    
    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }
    
    public String getInput() {
        return input;
    }
    
    public void setInput(String input) {
        this.input = input;
    }
    
    public String getOutput() {
        return output;
    }
    
    public void setOutput(String output) {
        this.output = output;
    }
    
    public Map<String, Object> getVariables() {
        return variables;
    }
}