package cn.geekslife.designpattern.facade.computer;

/**
 * 硬盘子系统类
 */
public class HardDrive {
    public byte[] read(long lba, int size) {
        System.out.println("硬盘: 从LBA " + lba + " 读取 " + size + " 字节");
        return new byte[size];
    }
}