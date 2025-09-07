package cn.geekslife.designpattern.prototype;

/**
 * 原型接口
 * 声明克隆方法
 */
public interface Prototype {
    /**
     * 克隆方法
     * @return 克隆的对象
     */
    Prototype clone();
}