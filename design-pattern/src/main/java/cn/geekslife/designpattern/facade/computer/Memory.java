package cn.geekslife.designpattern.facade.computer;

/**
 * 内存子系统类
 */
public class Memory {
    public void load(long position, byte[] data) {
        System.out.println("内存: 在位置 " + position + " 加载数据");
    }
}