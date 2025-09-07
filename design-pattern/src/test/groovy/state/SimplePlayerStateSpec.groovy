package state

import spock.lang.Specification

class SimplePlayerStateSpec extends Specification {

    def "should handle player state transitions with enum"() {
        given:
        MediaPlayerContext context = new MediaPlayerContext()
        
        when:
        context.play()  // STOPPED -> PLAYING
        SimplePlayerState playingState = context.getState()
        
        context.pause() // PLAYING -> PAUSED
        SimplePlayerState pausedState = context.getState()
        
        context.play()  // PAUSED -> PLAYING
        SimplePlayerState playingState2 = context.getState()
        
        context.stop()  // PLAYING -> STOPPED
        SimplePlayerState stoppedState = context.getState()
        
        then:
        playingState == SimplePlayerState.PLAYING
        pausedState == SimplePlayerState.PAUSED
        playingState2 == SimplePlayerState.PLAYING
        stoppedState == SimplePlayerState.STOPPED
    }
    
    def "should prevent invalid operations"() {
        given:
        MediaPlayerContext context = new MediaPlayerContext()
        // 初始状态为STOPPED
        
        when:
        context.pause() // 尝试在停止状态下暂停
        SimplePlayerState state = context.getState()
        
        then:
        state == SimplePlayerState.STOPPED // 状态应该保持不变
    }
}