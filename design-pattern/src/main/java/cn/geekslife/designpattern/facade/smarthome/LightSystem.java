package cn.geekslife.designpattern.facade.smarthome;

/**
 * 灯光控制系统子系统类
 */
public class LightSystem {
    public void turnOnAllLights() {
        System.out.println("灯光系统: 打开所有灯光");
    }
    
    public void turnOffAllLights() {
        System.out.println("灯光系统: 关闭所有灯光");
    }
    
    public void setBrightness(int level) {
        System.out.println("灯光系统: 设置亮度为 " + level + "%");
    }
    
    public void createAmbientLighting() {
        System.out.println("灯光系统: 创建环境照明");
    }
}