package cn.geekslife.designpattern.adapter;

/**
 * 目标接口 - 客户期望的接口
 */
public interface MediaPlayer {
    void play(String audioType, String fileName);
}