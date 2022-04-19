package utils;

import java.util.Collection;
import javafx.scene.control.TitledPane;

public class TitledPaneToggleGroup {
	private final Collection<TitledPane> titledPaneSet;
	
	private TitledPane openedTitledPane;
	
	public TitledPaneToggleGroup(Collection<TitledPane> titledPaneSet){
		this.titledPaneSet = titledPaneSet;
		this.setup();
		
//		System.out.print("");
	}
	
	private void setup() {
		this.titledPaneSet.forEach(tp->{
			tp.expandedProperty().addListener((obs,ov,nv)->{
				if(nv) {//expanded
					if(openedTitledPane!=null && openedTitledPane!=tp) {
						openedTitledPane.setExpanded(false);
						openedTitledPane = tp;
					}
					
				}else {
					openedTitledPane = null;
				}
			});
		});
	}
}
