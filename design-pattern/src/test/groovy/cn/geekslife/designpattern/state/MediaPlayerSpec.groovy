package cn.geekslife.designpattern.state

import cn.geekslife.designpattern.state.MediaPlayer
import spock.lang.Specification

class MediaPlayerSpec extends Specification {

    def "should start in stopped state"() {
        given:
        MediaPlayer player = new MediaPlayer()
        
        when:
        String initialState = player.getState().getName()
        
        then:
        initialState == "停止状态"
    }
    
    def "should transition from stopped to playing state"() {
        given:
        MediaPlayer player = new MediaPlayer()
        
        when:
        player.play()
        String currentState = player.getState().getName()
        
        then:
        currentState == "播放状态"
    }
    
    def "should transition from playing to paused state"() {
        given:
        MediaPlayer player = new MediaPlayer()
        player.play() // 先转换到播放状态
        
        when:
        player.pause()
        String currentState = player.getState().getName()
        
        then:
        currentState == "暂停状态"
    }
    
    def "should transition from paused to playing state"() {
        given:
        MediaPlayer player = new MediaPlayer()
        player.play()  // 播放状态
        player.pause() // 暂停状态
        
        when:
        player.play()
        String currentState = player.getState().getName()
        
        then:
        currentState == "播放状态"
    }
    
    def "should transition from playing to stopped state"() {
        given:
        MediaPlayer player = new MediaPlayer()
        player.play() // 先转换到播放状态
        
        when:
        player.stop()
        String currentState = player.getState().getName()
        
        then:
        currentState == "停止状态"
    }
}