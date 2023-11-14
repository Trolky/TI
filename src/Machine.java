import java.util.*;
public class Machine {
	private static final String initialState = "a";
    private static final Set<String> acceptingStates = new HashSet<>(List.of("e", "f", "g"));
    private static final Map<String, Map<String, String>> transitions = new HashMap<>();

    static {
    	transitions.put("a", Map.of("1", "b", "0", "i"));
        transitions.put("b", Map.of("1", "c", "0", "i"));
        transitions.put("c", Map.of("1", "c", "0", "d"));
        transitions.put("d", Map.of("1", "e", "0", "j"));
        transitions.put("e", Map.of("1", "e", "0", "f"));
        transitions.put("f", Map.of("1", "e", "0", "g"));
        transitions.put("g", Map.of("1", "h", "0", "g"));
        transitions.put("h", Map.of("1", "e", "0", "f"));
        transitions.put("i", Map.of("1", "b", "0", "j"));
        transitions.put("j", Map.of("1", "k", "0", "j"));
        transitions.put("k", Map.of("1", "c", "0", "i"));
        
    }
    
    private String currentState;
    private Stack<String> historyStack;
    
	public Machine() {
		 this.currentState = initialState;
	     this.historyStack = new Stack<>();
    }
	
	 public boolean performTransition(String inputSymbol) {
	        if (transitions.containsKey(currentState) && transitions.get(currentState).containsKey(inputSymbol)) {
	            String newState = transitions.get(currentState).get(inputSymbol);
	            historyStack.push(currentState);
	            currentState = newState;
	            return true;
	        } else {
	            System.out.println("Chyba: Přechod pro symbol " + inputSymbol + " není definován ze stavu " + currentState + ".");
	            return false;
	        }
	    }
	 
	    public boolean stepBack() {
	        if (!historyStack.isEmpty()) {
	        	currentState = historyStack.pop();
	            return true;
	        } else {
	            System.out.println("Chyba: Nelze provést krok zpět, historie přechodů je prázdná.");
	            return false;
	        }
	    }

	    public boolean isAcceptingState() {
	        return acceptingStates.contains(currentState);
	    }

	    public void reset() {
	        currentState = initialState;
	        historyStack.clear();
	    }

	    public String getCurrentState() {
	        return currentState;
	    }
	  
	    public List<String> getHistoryStack() {
	    	 return new ArrayList<>(historyStack);
	    }
	
	
	
}
