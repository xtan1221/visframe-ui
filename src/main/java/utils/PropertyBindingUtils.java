package utils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PropertyBindingUtils {
	
	public static NumberBinding buildInnestLayoutXPropertyBinding(Pane outest, Node innest) {
		
		Pane outer = (Pane) innest.getParent();
		
		NumberBinding sum = null;
		while(outer!=null&&outer!=outest) {
			if(sum==null)
				sum = Bindings.add(innest.layoutXProperty(),outer.layoutXProperty());
			else
				sum = sum.add(outer.layoutXProperty());
			
			outer = (Pane) outer.getParent();
		}
		
		if(outer==outest) {//outest pane is reached
			return sum;
		}else {//outest pane is not reached but no furture parent pane can goto
			throw new IllegalArgumentException("given innest is not a descendent of the given outest");
		}
		
	}
	
	public static NumberBinding buildInnestLayoutYPropertyBinding(Pane outest, Node innest) {
		
		Pane outer = (Pane) innest.getParent();
		
		NumberBinding sum = null;
		while(outer!=null&&outer!=outest) {
			if(sum==null)
				sum = Bindings.add(innest.layoutYProperty(),outer.layoutYProperty());
			else
				sum = sum.add(outer.layoutYProperty());
			
			outer = (Pane) outer.getParent();
		}
		
		if(outer==outest) {//outest pane is reached
			return sum;
		}else {//outest pane is not reached but no furture parent pane can goto
			throw new IllegalArgumentException("given innest is not a descendent of the given outest");
		}
		
	}
}
