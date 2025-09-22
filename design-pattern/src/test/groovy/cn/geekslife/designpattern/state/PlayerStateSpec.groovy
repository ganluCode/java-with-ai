package cn.geekslife.designpattern.state

import cn.geekslife.designpattern.state.MediaPlayer
import cn.geekslife.designpattern.state.PlayerState
import cn.geekslife.designpattern.state.StoppedState
import spock.lang.Specification

class PlayerStateSpec extends Specification {

    def "stopped state should handle operations correctly"() {
        given:
        MediaPlayer player = new MediaPlayer()
        PlayerState state = new StoppedState(player)
        
        when:
        state.play()
        String playResult = player.getState().getName()
        
        state.stop()
        String stopResult = player.getState().getName()
        
        then:
        playResult == "播放状态"
        stopResult == "停止状态"
    }
    
    def "playing state should handle operations correctly"() {
        given:
        MediaPlayer player = new MediaPlayer()
        player.play() // 转换到播放状态
        PlayerState state = player.getState()
        
        when:
        state.pause()
        String pauseResult = player.getState().getName()
        
        state.play()
        String playResult = player.getState().getName()
        
        then:
        pauseResult == "暂停状态"
        playResult == "播放状态"
    }
    
    def "paused state should handle operations correctly"() {
        given:
        MediaPlayer player = new MediaPlayer()
        player.play()  // 播放状态
        player.pause() // 暂停状态
        PlayerState state = player.getState()
        
        when:
        state.play()
        String playResult = player.getState().getName()
        
        state.pause()
        String pauseResult = player.getState().getName()
        
        then:
        playResult == "播放状态"
        pauseResult == "暂停状态"
    }
}