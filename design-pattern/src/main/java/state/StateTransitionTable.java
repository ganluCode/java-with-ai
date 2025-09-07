package state;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态转换表 - 解决状态转换逻辑分散问题
 * 集中管理状态转换逻辑
 */
public class StateTransitionTable {
    // 状态转换映射表
    private Map<String, Map<String, String>> transitionTable;
    
    public StateTransitionTable() {
        transitionTable = new HashMap<>();
        initTransitionTable();
    }
    
    // 初始化状态转换表
    private void initTransitionTable() {
        // 订单状态转换表
        Map<String, String> pendingTransitions = new HashMap<>();
        pendingTransitions.put("pay", "paid");
        pendingTransitions.put("cancel", "cancelled");
        transitionTable.put("pending", pendingTransitions);
        
        Map<String, String> paidTransitions = new HashMap<>();
        paidTransitions.put("ship", "shipped");
        paidTransitions.put("cancel", "cancelled");
        transitionTable.put("paid", paidTransitions);
        
        Map<String, String> shippedTransitions = new HashMap<>();
        shippedTransitions.put("deliver", "delivered");
        shippedTransitions.put("return", "return_requested");
        transitionTable.put("shipped", shippedTransitions);
        
        Map<String, String> deliveredTransitions = new HashMap<>();
        deliveredTransitions.put("return", "return_requested");
        transitionTable.put("delivered", deliveredTransitions);
    }
    
    // 获取下一个状态
    public String getNextState(String currentState, String event) {
        Map<String, String> transitions = transitionTable.get(currentState);
        if (transitions != null) {
            return transitions.get(event);
        }
        return null;
    }
    
    // 检查状态转换是否有效
    public boolean isValidTransition(String currentState, String event) {
        return getNextState(currentState, event) != null;
    }
}