package core.pipeline.cfg.utils;

import java.lang.reflect.Modifier;

import function.group.AbstractCompositionFunctionGroup;
import function.group.CompositionFunctionGroup;
import function.group.GraphicsPropertyCFG;
import function.group.IndependentGraphicsPropertyCFG;
import function.group.IndependentPrimitiveAttributeCFG;
import function.group.ShapeCFG;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;


public class CompositionFunctionGroupTypeTreeViewUtils {
	private static TreeView<Class<? extends CompositionFunctionGroup>> treeView;
	
	
	/**
	 * return the selected final operation type;
	 * return null if no tree item is selected or if selected item is not final operation type;
	 * 
	 * @return
	 */
	public static Class<? extends CompositionFunctionGroup> selectedOperationType(){
		if(treeView==null) {
			throw new UnsupportedOperationException("tree view is null!");
		}
		
		TreeItem<Class<? extends CompositionFunctionGroup>> selectedItem = treeView.getSelectionModel().getSelectedItem();
		if(selectedItem==null) {//no tree item is selected
			return null;
		}
		
		if(Modifier.isFinal(selectedItem.getValue().getModifiers())) {
			return selectedItem.getValue();
		}else {
			return null;
		}
	}
	
	
	/**
	 * return the TreeView for all operation types that have been implemented and tested;
	 * 
	 * @return
	 */
	public static TreeView<Class<? extends CompositionFunctionGroup>> makeTreeView(TextField selectedTypeTextField){
		if(treeView==null) {
			treeView = new TreeView<>(buildAbstractCompositionFunctionGroupTreeItem());
	//		treeView.setShowRoot(false);
			
			//tree cell shows a Label with the simple name of the class contained by the tree item;
			treeView.setCellFactory(e->{
				return new TreeCell<Class<? extends CompositionFunctionGroup>>() {
		            @Override
		            protected void updateItem(Class<? extends CompositionFunctionGroup> cfgType, boolean empty) {
		                super.updateItem(cfgType, empty);
		                if (cfgType == null || empty) {
		                    setGraphic(null);
		                } else {
		                	//set the Graphics for the tree cell here with a specific operationType
		                	Label label = new Label(cfgType.getSimpleName());
		                	if(Modifier.isFinal(cfgType.getModifiers())){//selectable
		                		label.setStyle("-fx-border-color:red; -fx-background-color: cyan;");
		                	}
		                	
//		                	label.setOnMouseClicked(e->{
//		                		if(Modifier.isFinal(operationType.getModifiers())){
////			                		System.out.println(operationType.getSimpleName());
//			                		selectedTypeTextField.setText(operationType.getSimpleName());
//		                		}else {
//		                			selectedTypeTextField.setText("");
//		                		}
//		                	});
		                	
		                	Tooltip tp = new Tooltip("at stack tool");
		                	Tooltip.install(label, tp);
		                	
		                    setGraphic(label);
		                }
		            }
		        };
			});
			
			
			treeView.getSelectionModel().selectedItemProperty().addListener(e->{
				
				selectedTypeTextField.setText(treeView.getSelectionModel().getSelectedItem()==null?"":treeView.getSelectionModel().getSelectedItem().getValue().getSimpleName());
				
			});
		}else {
			treeView.getSelectionModel().clearSelection();
		}
		
		
		
		return treeView;
	}

	////////////////////////////////////
	//root tree item
	private static TreeItem<Class<? extends CompositionFunctionGroup>> buildAbstractCompositionFunctionGroupTreeItem(){
		TreeItem<Class<? extends CompositionFunctionGroup>> abstractCompositionFunctionGroup = new TreeItem<>(AbstractCompositionFunctionGroup.class);
		
		abstractCompositionFunctionGroup.getChildren().add(buildGraphicsPropertyCFGTreeItem());
		abstractCompositionFunctionGroup.getChildren().add(buildIndependentPrimitiveAttributeCFGTreeItem());
		
		return abstractCompositionFunctionGroup;
	}
	
	/////////////////////////////////////
	//1
	private static TreeItem<Class<? extends CompositionFunctionGroup>> buildGraphicsPropertyCFGTreeItem() {
		TreeItem<Class<? extends CompositionFunctionGroup>> graphicsPropertyCFG = new TreeItem<>(GraphicsPropertyCFG.class);
		
		TreeItem<Class<? extends CompositionFunctionGroup>> independentGraphicsPropertyCFG = new TreeItem<>(IndependentGraphicsPropertyCFG.class);
		TreeItem<Class<? extends CompositionFunctionGroup>> shapeCFG = new TreeItem<>(ShapeCFG.class);
		
		graphicsPropertyCFG.getChildren().add(independentGraphicsPropertyCFG);
		graphicsPropertyCFG.getChildren().add(shapeCFG);
		
		return graphicsPropertyCFG;
	}
	
	//////////////////////////////////////
	//2
	private static TreeItem<Class<? extends CompositionFunctionGroup>> buildIndependentPrimitiveAttributeCFGTreeItem() {
		TreeItem<Class<? extends CompositionFunctionGroup>> independentPrimitiveAttributeCFG = new TreeItem<>(IndependentPrimitiveAttributeCFG.class);
		
		return independentPrimitiveAttributeCFG;
	}
	
}
