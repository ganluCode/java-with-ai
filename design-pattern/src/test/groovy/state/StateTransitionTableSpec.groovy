package state

import spock.lang.Specification

class StateTransitionTableSpec extends Specification {

    def "should define valid state transitions"() {
        given:
        StateTransitionTable transitionTable = new StateTransitionTable()
        
        when:
        boolean pendingToPaid = transitionTable.isValidTransition("pending", "pay")
        boolean paidToShipped = transitionTable.isValidTransition("paid", "ship")
        boolean shippedToDelivered = transitionTable.isValidTransition("shipped", "deliver")
        boolean invalidTransition = transitionTable.isValidTransition("pending", "deliver")
        
        then:
        pendingToPaid == true
        paidToShipped == true
        shippedToDelivered == true
        invalidTransition == false
    }
    
    def "should get correct next state"() {
        given:
        StateTransitionTable transitionTable = new StateTransitionTable()
        
        when:
        String nextState1 = transitionTable.getNextState("pending", "pay")
        String nextState2 = transitionTable.getNextState("paid", "ship")
        String nextState3 = transitionTable.getNextState("shipped", "deliver")
        String nextState4 = transitionTable.getNextState("pending", "invalid")
        
        then:
        nextState1 == "paid"
        nextState2 == "shipped"
        nextState3 == "delivered"
        nextState4 == null
    }
}