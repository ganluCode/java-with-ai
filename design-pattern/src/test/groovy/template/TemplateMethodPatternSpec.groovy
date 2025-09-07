package template

import spock.lang.Specification

class TemplateMethodPatternSpec extends Specification {

    def "should prevent overriding template method"() {
        given:
        Game game = new Chess()
        
        when:
        // 尝试反射调用模板方法
        def method = Game.class.getDeclaredMethod("play")
        
        then:
        method != null
        // 模板方法应该是final的，不能被重写
        method.isAnnotationPresent(java.lang.Override.class) == false
    }
    
    def "should allow overriding hook methods"() {
        given:
        Chess chess = new Chess()
        Poker poker = new Poker()
        Tetris tetris = new Tetris()
        
        when:
        boolean chessShowWinner = chess.showWinner()
        boolean pokerShowWinner = poker.showWinner()
        boolean tetrisShowWinner = tetris.showWinner()
        
        then:
        chessShowWinner == true
        pokerShowWinner == true
        tetrisShowWinner == false
    }
    
    def "should execute template method in correct order"() {
        given:
        Game game = new Chess()
        List<String> executionOrder = []
        
        // 创建一个测试用的Game子类来验证执行顺序
        Game testGame = new Game() {
            @Override
            protected void initialize() {
                executionOrder.add("initialize")
            }
            
            @Override
            protected void startPlay() {
                executionOrder.add("startPlay")
            }
            
            @Override
            protected void runGame() {
                executionOrder.add("runGame")
            }
            
            @Override
            protected void endPlay() {
                executionOrder.add("endPlay")
            }
        }
        
        when:
        testGame.play()
        
        then:
        executionOrder == ["initialize", "startPlay", "runGame", "endPlay"]
    }
}