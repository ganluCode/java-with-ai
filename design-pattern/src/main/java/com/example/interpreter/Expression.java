package com.example.interpreter;

/**
 * 抽象表达式接口
 * 声明一个抽象的解释操作
 */
public interface Expression {
    /**
     * 解释操作
     * @param context 上下文环境
     * @return 解释结果
     */
    boolean interpret(Context context);
}