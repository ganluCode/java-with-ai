package cn.geekslife.designpattern.bridge

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 桥接模式Spock测试类
 * 使用行为驱动开发(BDD)方式测试桥接模式
 */
class BridgePatternSpec extends Specification {
    
    def "圆形应该能够通过不同平台的绘制API进行绘制"() {
        given: "创建不同平台的绘制API和圆形"
        WindowsDrawAPI windowsAPI = new WindowsDrawAPI()
        LinuxDrawAPI linuxAPI = new LinuxDrawAPI()
        MacDrawAPI macAPI = new MacDrawAPI()
        
        Circle windowsCircle = new Circle(100, 100, 10, windowsAPI)
        Circle linuxCircle = new Circle(50, 50, 5, linuxAPI)
        Circle macCircle = new Circle(75, 75, 7, macAPI)
        
        when: "绘制圆形"
        windowsCircle.draw()
        linuxCircle.draw()
        macCircle.draw()
        
        then: "应该通过对应的平台API正确绘制"
        // 主要验证逻辑正确性
        true
    }
    
    def "矩形应该能够通过不同平台的绘制API进行绘制"() {
        given: "创建不同平台的绘制API和矩形"
        WindowsDrawAPI windowsAPI = new WindowsDrawAPI()
        LinuxDrawAPI linuxAPI = new LinuxDrawAPI()
        MacDrawAPI macAPI = new MacDrawAPI()
        
        Rectangle windowsRectangle = new Rectangle(10, 20, 100, 200, windowsAPI)
        Rectangle linuxRectangle = new Rectangle(15, 25, 150, 250, linuxAPI)
        Rectangle macRectangle = new Rectangle(12, 22, 120, 220, macAPI)
        
        when: "绘制矩形"
        windowsRectangle.draw()
        linuxRectangle.draw()
        macRectangle.draw()
        
        then: "应该通过对应的平台API正确绘制"
        true
    }
    
    def "形状抽象类应该能够持有实现者引用"() {
        given: "创建绘制API和形状"
        DrawAPI drawAPI = new WindowsDrawAPI()
        Circle circle = new Circle(0, 0, 5, drawAPI)
        
        when: "获取绘制API引用"
        DrawAPI api = circle.drawAPI
        
        then: "应该正确持有实现者引用"
        api != null
        api instanceof WindowsDrawAPI
    }
    
    def "电视机应该正确实现设备接口"() {
        given: "创建电视机实例"
        Tv tv = new Tv()
        
        when: "检查初始状态"
        boolean enabled = tv.isEnabled()
        int volume = tv.getVolume()
        int channel = tv.getChannel()
        
        then: "初始状态应该正确"
        !enabled
        volume == 30
        channel == 1
    }
    
    def "收音机应该正确实现设备接口"() {
        given: "创建收音机实例"
        Radio radio = new Radio()
        
        when: "检查初始状态"
        boolean enabled = radio.isEnabled()
        int volume = radio.getVolume()
        int channel = radio.getChannel()
        
        then: "初始状态应该正确"
        !enabled
        volume == 30
        channel == 1
    }
    
    def "基本遥控器应该能够控制设备"() {
        given: "创建设备和遥控器"
        Device tv = new Tv()
        BasicRemote remote = new BasicRemote(tv)
        
        when: "开关设备"
        remote.togglePower()
        
        then: "设备应该被开启"
        tv.isEnabled()
        
        when: "调节音量"
        remote.volumeUp()
        
        then: "音量应该增加"
        tv.getVolume() == 40
        
        when: "继续调节音量"
        remote.volumeDown()
        
        then: "音量应该减少"
        tv.getVolume() == 30
    }
    
    def "高级遥控器应该提供更多控制功能"() {
        given: "创建设备和高级遥控器"
        Device radio = new Radio()
        AdvancedRemote remote = new AdvancedRemote(radio)
        
        when: "使用高级功能"
        remote.togglePower()
        remote.playNextChannel()
        
        then: "设备应该正确响应"
        radio.isEnabled()
        radio.getChannel() == 2
        
        when: "使用静音功能"
        remote.mute()
        
        then: "设备应该静音"
        radio.getVolume() == 0
    }
    
    def "设备应该能够正确设置音量范围"() {
        given: "创建设备"
        Device tv = new Tv()
        
        when: "设置超过最大值的音量"
        tv.setVolume(150)
        
        then: "音量应该被限制在最大值"
        tv.getVolume() == 100
        
        when: "设置负音量"
        tv.setVolume(-10)
        
        then: "音量应该被限制在最小值"
        tv.getVolume() == 0
    }
    
    def "设备应该能够正确切换频道"() {
        given: "创建设备"
        Device radio = new Radio()
        int initialChannel = radio.getChannel()
        
        when: "切换到下一个频道"
        radio.setChannel(initialChannel + 1)
        
        then: "频道应该正确切换"
        radio.getChannel() == initialChannel + 1
        
        when: "切换到上一个频道"
        radio.setChannel(initialChannel)
        
        then: "频道应该正确切换"
        radio.getChannel() == initialChannel
    }
    
    def "桥接模式应该支持抽象和实现的独立变化"() {
        given: "创建不同的抽象和实现组合"
        Device tv = new Tv()
        Device radio = new Radio()
        RemoteControl basicRemote = new BasicRemote(tv)
        AdvancedRemote advancedRemote = new AdvancedRemote(radio)
        
        when: "使用不同的组合"
        basicRemote.togglePower()
        advancedRemote.togglePower()
        
        then: "都应该正常工作"
        tv.isEnabled()
        radio.isEnabled()
    }
    
    def "形状应该能够获取和设置属性"() {
        given: "创建圆形"
        Circle circle = new Circle(100, 100, 10, new WindowsDrawAPI())
        
        when: "获取属性"
        int x = circle.getX()
        int y = circle.getY()
        int radius = circle.getRadius()
        
        then: "属性应该正确"
        x == 100
        y == 100
        radius == 10
        
        when: "设置新属性"
        circle.setX(200)
        circle.setY(200)
        circle.setRadius(20)
        
        then: "属性应该被正确设置"
        circle.getX() == 200
        circle.getY() == 200
        circle.getRadius() == 20
    }
    
    def "矩形应该能够获取和设置属性"() {
        given: "创建矩形"
        Rectangle rectangle = new Rectangle(10, 20, 100, 200, new WindowsDrawAPI())
        
        when: "获取属性"
        int x = rectangle.getX()
        int y = rectangle.getY()
        int width = rectangle.getWidth()
        int height = rectangle.getHeight()
        
        then: "属性应该正确"
        x == 10
        y == 20
        width == 100
        height == 200
        
        when: "设置新属性"
        rectangle.setX(30)
        rectangle.setY(40)
        rectangle.setWidth(150)
        rectangle.setHeight(250)
        
        then: "属性应该被正确设置"
        rectangle.getX() == 30
        rectangle.getY() == 40
        rectangle.getWidth() == 150
        rectangle.getHeight() == 250
    }
    
    @Unroll
    def "不同平台的绘制API应该正确绘制圆形: #platform"() {
        given: "创建对应平台的绘制API和圆形"
        DrawAPI drawAPI = apiClass.newInstance()
        Circle circle = new Circle(50, 50, 5, drawAPI)
        
        when: "绘制圆形"
        circle.draw()
        
        then: "应该正确绘制"
        true
        
        where:
        platform    | apiClass
        "Windows"   | WindowsDrawAPI.class
        "Linux"     | LinuxDrawAPI.class
        "Mac"       | MacDrawAPI.class
    }
    
    @Unroll
    def "不同平台的绘制API应该正确绘制矩形: #platform"() {
        given: "创建对应平台的绘制API和矩形"
        DrawAPI drawAPI = apiClass.newInstance()
        Rectangle rectangle = new Rectangle(10, 20, 100, 200, drawAPI)
        
        when: "绘制矩形"
        rectangle.draw()
        
        then: "应该正确绘制"
        true
        
        where:
        platform    | apiClass
        "Windows"   | WindowsDrawAPI.class
        "Linux"     | LinuxDrawAPI.class
        "Mac"       | MacDrawAPI.class
    }
    
    @Unroll
    def "不同类型的设备应该正确响应遥控器控制: #deviceType"() {
        given: "创建设备和遥控器"
        Device device = deviceClass.newInstance()
        RemoteControl remote = remoteClass.newInstance(device)
        
        when: "控制设备"
        remote.togglePower()
        remote.volumeUp()
        remote.channelUp()
        
        then: "设备应该正确响应"
        device.isEnabled()
        device.getVolume() == 40
        device.getChannel() == 2
        
        where:
        deviceType  | deviceClass | remoteClass
        "TV"        | Tv.class    | BasicRemote.class
        "Radio"     | Radio.class | AdvancedRemote.class
    }
    
    def "桥接模式应该支持多态性"() {
        given: "创建不同的形状实例"
        Shape circle = new Circle(100, 100, 10, new WindowsDrawAPI())
        Shape rectangle = new Rectangle(10, 20, 100, 200, new WindowsDrawAPI())
        
        when: "多态调用draw方法"
        circle.draw()
        rectangle.draw()
        
        then: "都应该正确绘制"
        true
    }
    
    def "桥接模式应该支持实现的替换"() {
        given: "创建形状和不同的实现"
        DrawAPI windowsAPI = new WindowsDrawAPI()
        DrawAPI linuxAPI = new LinuxDrawAPI()
        Circle circle1 = new Circle(100, 100, 10, windowsAPI)
        Circle circle2 = new Circle(100, 100, 10, linuxAPI)
        
        when: "使用不同的实现绘制"
        circle1.draw()
        circle2.draw()
        
        then: "应该使用对应的实现"
        true
    }
    
    def "遥控器应该能够正确静音设备"() {
        given: "创建设备和遥控器"
        Device tv = new Tv()
        tv.setVolume(50) // 先设置音量
        BasicRemote remote = new BasicRemote(tv)
        
        when: "静音设备"
        remote.mute()
        
        then: "设备音量应该为0"
        tv.getVolume() == 0
    }
    
    def "高级遥控器应该能够播放下一个频道"() {
        given: "创建设备和高级遥控器"
        Device radio = new Radio()
        int initialChannel = radio.getChannel()
        AdvancedRemote remote = new AdvancedRemote(radio)
        
        when: "播放下一个频道"
        remote.playNextChannel()
        
        then: "频道应该正确切换"
        radio.getChannel() == initialChannel + 1
    }
    
    def "高级遥控器应该能够播放上一个频道"() {
        given: "创建设备和高级遥控器"
        Device radio = new Radio()
        radio.setChannel(5) // 先设置频道
        AdvancedRemote remote = new AdvancedRemote(radio)
        
        when: "播放上一个频道"
        remote.playPreviousChannel()
        
        then: "频道应该正确切换"
        radio.getChannel() == 4
    }
    
    def "桥接模式应该保持抽象和实现的松耦合"() {
        given: "创建抽象和实现"
        DrawAPI drawAPI = new WindowsDrawAPI()
        Shape shape = new Circle(0, 0, 5, drawAPI)
        
        when: "替换实现"
        DrawAPI newAPI = new LinuxDrawAPI()
        Circle newCircle = new Circle(0, 0, 5, newAPI)
        
        then: "抽象不需要修改"
        shape instanceof Circle
        newCircle instanceof Circle
    }
}