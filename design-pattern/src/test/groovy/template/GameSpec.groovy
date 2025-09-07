package template

import spock.lang.Specification

class GameSpec extends Specification {

    def "should execute chess game template method correctly"() {
        given:
        Game chess = new Chess()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        chess.play()
        String output = out.toString()
        
        then:
        output.contains("国际象棋游戏初始化")
        output.contains("开始国际象棋游戏")
        output.contains("运行国际象棋游戏")
        output.contains("国际象棋游戏结束")
        output.contains("显示胜利者: 白方获胜")
    }
    
    def "should execute poker game template method correctly"() {
        given:
        Game poker = new Poker()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        poker.play()
        String output = out.toString()
        
        then:
        output.contains("纸牌游戏初始化")
        output.contains("开始纸牌游戏")
        output.contains("运行纸牌游戏")
        output.contains("纸牌游戏结束")
        output.contains("显示胜利者: 玩家3获胜")
    }
    
    def "should execute tetris game template method correctly"() {
        given:
        Game tetris = new Tetris()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        tetris.play()
        String output = out.toString()
        
        then:
        output.contains("俄罗斯方块游戏初始化")
        output.contains("开始俄罗斯方块游戏")
        output.contains("运行俄罗斯方块游戏")
        output.contains("俄罗斯方块游戏结束")
        output.contains("最终得分: 1500分")
        // 俄罗斯方块不显示胜利者
        !output.contains("显示胜利者")
    }
}