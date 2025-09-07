package com.example.flyweight;

/**
 * 抽象享元类
 * 定义了享元对象的接口，通过这个接口可以接受并作用于外部状态
 */
public abstract class Flyweight {
    /**
     * 操作方法，接受外部状态
     * @param extrinsicState 外部状态
     */
    public abstract void operation(String extrinsicState);
}