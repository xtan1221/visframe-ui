package builder.basic.collection.misc;

import java.util.LinkedHashSet;
import java.util.Set;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Pair;

public class PairSetSelectorTester extends Application{
	class Value{
		
		String value;
		int index;
		
		Value(String value, int index){
			this.value = value;
			this.index = index;
		}
		
		@Override
		public String toString() {
			return Integer.toString(index);
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		
		PairSetSelector<Value,String> builder = new PairSetSelector<>(
        		"test", "test", true, null, 
        		
        		e->{return e.toString();},//Function<T,R> returnedTypeFunction, 
        		e->{return e.value.concat(Integer.toString(e.index));},//Function<T,String> toStringFunction
//        		e->{return e.getFirst()>e.getSecond();},//Predicate<Pair<T,T>> pairConstraint,
        		e->{return true;},//Predicate<Pair<T,T>> pairConstraint,
    			"first must be larger than second"//String pairConstraintDescription
				);
        
        
        Set<Value> set = new LinkedHashSet<>();
//        for(int i=0;i<20;i++) {
//        	set.add(i);
//        }
        set.add(new Value("daffd", 1));
        set.add(new Value("@@@", 3));
        set.add(new Value("323", 2423));
        set.add(new Value("g23", 33333333));
        
        
        
        builder.setPool(set);
        
        Button btn = new Button();
        btn.setText("start");
        root.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
					root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
            }
        });
        
        
        //////////////////
        LinkedHashSet<String> first = new LinkedHashSet<>();
        first.add("1");
        LinkedHashSet<String> second  = new LinkedHashSet<>();
        second.add("3");
        
        Pair<LinkedHashSet<String>,LinkedHashSet<String>> newValue = new Pair<>(first, second);
        
//        Set<String> set2 = new LinkedHashSet<>();
//        set2.add("1");
//        set2.add("2");
//        set2.add("3");
//        set2.add("4");
        Button button2 = new Button();
        button2.setText("set to another non-null value");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
//        	builder.setValue(newValue, false);
        	builder.updateNonNullValueFromContentController(false);
        });
        
       
//        Button button3 = new Button();
//        button3.setText("pring current status");
//        root.getChildren().add(button3);
//        button3.setOnAction(e->{
//        	
//        	System.out.println(builder.getCurrentStatus());
//        	
//        });
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
