package core.pipeline.operation.utils;

import java.lang.reflect.Modifier;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import operation.AbstractOperation;
import operation.Operation;
import operation.graph.InputGraphTypeBoundedOperation;
import operation.graph.SingleGenericGraphAsInputOperation;
import operation.graph.build.BuildGraphFromExistingRecordOperationBase;
import operation.graph.build.BuildGraphFromSingleExistingRecordOperation;
import operation.graph.build.BuildGraphFromTwoExistingRecordOperation;
import operation.graph.layout.GraphNode2DLayoutOperationBase;
import operation.graph.layout.JUNG.FRLayout2DOperation;
import operation.graph.layout.JUNG.SpringLayout2DOperation;
import operation.graph.layout.jgrapht.CircularLayout2DOperation;
import operation.graph.transform.TransformGraphOperation;
import operation.sql.SQLOperationBase;
import operation.sql.generic.GenericSQLOperation;
import operation.sql.predefined.PredefinedSQLBasedOperation;
import operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperation;
import operation.sql.predefined.type.AddNumericCumulativeColumnOperation;
import operation.sql.predefined.type.GroupAndBinCountOperation;
import operation.vftree.VfTreeTrimmingOperationBase;
import operation.vftree.trim.RerootTreeOperation;
import operation.vftree.trim.SiblingNodesReorderOperation;
import operation.vftree.trim.SubTreeOperation;

/**
 * 
 * @author tanxu
 *
 */
public class OperationTypeTreeViewUtils {
	private static TreeView<Class<? extends Operation>> treeView;
	
	
	/**
	 * return the selected final operation type;
	 * return null if no tree item is selected or if selected item is not final operation type;
	 * 
	 * @return
	 */
	public static Class<? extends Operation> selectedOperationType(){
		if(treeView==null) {
			throw new UnsupportedOperationException("tree view is null!");
		}
		
		TreeItem<Class<? extends Operation>> selectedItem = treeView.getSelectionModel().getSelectedItem();
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
	public static TreeView<Class<? extends Operation>> makeTreeView(TextField selectedTypeTextField){
		if(treeView==null) {
			treeView = new TreeView<>(buildAbstractOperationTreeItem());
	//		treeView.setShowRoot(false);

			//tree cell shows a Label with the simple name of the class contained by the tree item;
			treeView.setCellFactory(e->{
				return new TreeCell<Class<? extends Operation>>() {
		            @Override
		            protected void updateItem(Class<? extends Operation> operationType, boolean empty) {
		                super.updateItem(operationType, empty);
		                if (operationType == null || empty) {
		                    setGraphic(null);
		                } else {
		                	//set the Graphics for the tree cell here with a specific operationType
		                	Label label = new Label(operationType.getSimpleName());
		                	if(Modifier.isFinal(operationType.getModifiers())){//selectable
		                		label.setStyle("-fx-border-color:red; -fx-background-color: cyan;");
		                	}
		                	
//		                	label.setOnMouseClicked(e->{
//		                		if(Modifier.isFinal(operationType.getModifiers())){
//			                		System.out.println(operationType.getSimpleName());
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
			
			
			//only allow selection of leaf node to be updated on the given TextField;
			treeView.getSelectionModel().selectedItemProperty().addListener(e->{
				System.out.println("changed");
				TreeItem<Class<? extends Operation>> treeItem = treeView.getSelectionModel().getSelectedItem();
				if(treeItem==null) {
					selectedTypeTextField.setText("");
				}else {
					if(Modifier.isFinal(treeItem.getValue().getModifiers())){
//	            		System.out.println(treeItem.getValue().getSimpleName());
	            		selectedTypeTextField.setText(treeItem.getValue().getSimpleName());
	        		}else {
	        			selectedTypeTextField.setText("");
	        		}
				}
			});
		}else {
			treeView.getSelectionModel().clearSelection();
		}
		
		
		
		
		return treeView;
	}
	
	////////////////////////////////////
	//root tree item
	private static TreeItem<Class<? extends Operation>> buildAbstractOperationTreeItem(){
		TreeItem<Class<? extends Operation>> abstractOperation = new TreeItem<>(AbstractOperation.class);
		
		abstractOperation.getChildren().add(buildBuildGraphFromExistingRecordOperationBaseTreeItem());
		abstractOperation.getChildren().add(buildSingleGenericGraphAsInputOperationTreeItem());
		abstractOperation.getChildren().add(buildSQLOperationBaseTreeItem());
		
		return abstractOperation;
	}
	
	/////////////////////////////////////
	//1
	private static TreeItem<Class<? extends Operation>> buildBuildGraphFromExistingRecordOperationBaseTreeItem() {
		TreeItem<Class<? extends Operation>> buildGraphFromExistingRecordOperationBase = new TreeItem<>(BuildGraphFromExistingRecordOperationBase.class);
		
		TreeItem<Class<? extends Operation>> buildGraphFromSingleExistingRecordOperation = new TreeItem<>(BuildGraphFromSingleExistingRecordOperation.class);
		TreeItem<Class<? extends Operation>> buildGraphFromTwoExistingRecordOperation = new TreeItem<>(BuildGraphFromTwoExistingRecordOperation.class);
		
		buildGraphFromExistingRecordOperationBase.getChildren().add(buildGraphFromSingleExistingRecordOperation);
		buildGraphFromExistingRecordOperationBase.getChildren().add(buildGraphFromTwoExistingRecordOperation);
		
		return buildGraphFromExistingRecordOperationBase;
	}
	
	//////////////////////////////////////
	//2
	private static TreeItem<Class<? extends Operation>> buildSingleGenericGraphAsInputOperationTreeItem() {
		TreeItem<Class<? extends Operation>> singleGenericGraphAsInputOperation = new TreeItem<>(SingleGenericGraphAsInputOperation.class);
		
		singleGenericGraphAsInputOperation.getChildren().add(buildInputGraphTypeBoundedOperationTreeItem());
		singleGenericGraphAsInputOperation.getChildren().add(buildTransformGraphOperationTreeItem());
		singleGenericGraphAsInputOperation.getChildren().add(buildVfTreeTrimmingOperationBaseTreeItem());
		
		return singleGenericGraphAsInputOperation;
	}
	//2.1
	private static TreeItem<Class<? extends Operation>> buildInputGraphTypeBoundedOperationTreeItem() {
		TreeItem<Class<? extends Operation>> inputGraphTypeBoundedOperation = new TreeItem<>(InputGraphTypeBoundedOperation.class);
		inputGraphTypeBoundedOperation.getChildren().add(buildGraphNode2DLayoutOperationBaseTreeItem());
		
		return inputGraphTypeBoundedOperation;
	}
	
	//2.1.1
	private static TreeItem<Class<? extends Operation>> buildGraphNode2DLayoutOperationBaseTreeItem() {
		TreeItem<Class<? extends Operation>> graphNode2DLayoutOperationBase = new TreeItem<>(GraphNode2DLayoutOperationBase.class);
		
		TreeItem<Class<? extends Operation>> circularLayout2DOperation = new TreeItem<>(CircularLayout2DOperation.class);
		TreeItem<Class<? extends Operation>> FRLayout2DOperation = new TreeItem<>(FRLayout2DOperation.class);
		TreeItem<Class<? extends Operation>> springLayout2DOperation = new TreeItem<>(SpringLayout2DOperation.class);
		
		graphNode2DLayoutOperationBase.getChildren().add(circularLayout2DOperation);
		graphNode2DLayoutOperationBase.getChildren().add(FRLayout2DOperation);
		graphNode2DLayoutOperationBase.getChildren().add(springLayout2DOperation);
		
		return graphNode2DLayoutOperationBase;
	}
	
	//2.2
	private static TreeItem<Class<? extends Operation>> buildTransformGraphOperationTreeItem() {
		TreeItem<Class<? extends Operation>> transformGraphOperation = new TreeItem<>(TransformGraphOperation.class);
		return transformGraphOperation;
	}
	//2.3
	private static TreeItem<Class<? extends Operation>> buildVfTreeTrimmingOperationBaseTreeItem() {
		TreeItem<Class<? extends Operation>> vfTreeTrimmingOperationBase = new TreeItem<>(VfTreeTrimmingOperationBase.class);
		
		TreeItem<Class<? extends Operation>> rerootTreeOperation = new TreeItem<>(RerootTreeOperation.class);
		TreeItem<Class<? extends Operation>> siblingNodesReorderOperation = new TreeItem<>(SiblingNodesReorderOperation.class);
		TreeItem<Class<? extends Operation>> subTreeOperation = new TreeItem<>(SubTreeOperation.class);
		
		vfTreeTrimmingOperationBase.getChildren().add(rerootTreeOperation);
		vfTreeTrimmingOperationBase.getChildren().add(siblingNodesReorderOperation);
		vfTreeTrimmingOperationBase.getChildren().add(subTreeOperation);
		
		return vfTreeTrimmingOperationBase;
	}
	
	///////////////////////////////////////
	//3
	private static TreeItem<Class<? extends Operation>> buildSQLOperationBaseTreeItem() {
		TreeItem<Class<? extends Operation>> SQLOperationBase = new TreeItem<>(SQLOperationBase.class);
		
		SQLOperationBase.getChildren().add(buildGenericSQLOperationTreeItem());
		SQLOperationBase.getChildren().add(buildPredefinedSQLBasedOperationTreeItem());
		
		return SQLOperationBase;
	}
	
	//3.1
	private static TreeItem<Class<? extends Operation>> buildGenericSQLOperationTreeItem() {
		TreeItem<Class<? extends Operation>> genericSQLOperation = new TreeItem<>(GenericSQLOperation.class);
		return genericSQLOperation;
	}
	//3.2
	private static TreeItem<Class<? extends Operation>> buildPredefinedSQLBasedOperationTreeItem() {
		TreeItem<Class<? extends Operation>> predefinedSQLBasedOperation = new TreeItem<>(PredefinedSQLBasedOperation.class);
		
		predefinedSQLBasedOperation.getChildren().add(buildSingleInputRecordDataPredefinedSQLOperationTreeItem());
		
		return predefinedSQLBasedOperation;
	}
	
	//3.2.1
	private static TreeItem<Class<? extends Operation>> buildSingleInputRecordDataPredefinedSQLOperationTreeItem() {
		TreeItem<Class<? extends Operation>> singleInputRecordDataPredefinedSQLOperation = new TreeItem<>(SingleInputRecordDataPredefinedSQLOperation.class);
		
		TreeItem<Class<? extends Operation>> addNumericCumulativeColumnOperation = new TreeItem<>(AddNumericCumulativeColumnOperation.class);
		TreeItem<Class<? extends Operation>> groupAndBinCountOperation = new TreeItem<>(GroupAndBinCountOperation.class);
		
		singleInputRecordDataPredefinedSQLOperation.getChildren().add(addNumericCumulativeColumnOperation);
		singleInputRecordDataPredefinedSQLOperation.getChildren().add(groupAndBinCountOperation);
		
		return singleInputRecordDataPredefinedSQLOperation;
	}
	
}
