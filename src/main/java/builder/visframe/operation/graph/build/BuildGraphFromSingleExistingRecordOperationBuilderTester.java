package builder.visframe.operation.graph.build;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import basic.SimpleName;
import basic.VfNotes;
import generic.graph.DirectedType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import metadata.MetadataID;
import metadata.MetadataName;
import metadata.graph.type.GraphTypeEnforcer;
import operation.OperationName;
import operation.graph.build.BuildGraphFromSingleExistingRecordOperation;
import rdb.table.data.DataTableColumnName;
import test.VisProjectDBContextTest;

public class BuildGraphFromSingleExistingRecordOperationBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        BuildGraphFromSingleExistingRecordOperationBuilder builder = new BuildGraphFromSingleExistingRecordOperationBuilder(
        		VisProjectDBContextTest.getConnectedProject3(),
        		false
        		);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());

            }
        });
        
        
//        SQLDataType SQLDataType = SQLDataTypeFactory.longIntegerType();
//        
        Button button2 = new Button("set to a pre-defined value");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
				try {
					builder.setValue(make(), false);
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        });
        
//        builder.setModifiable(false);
        
//        Button button3 = new Button("set to a pre-defined value");
//        root.getChildren().add(button2);
//        button2.setOnAction(e->{
//        
//        	try {
//				builder.setValue(graphTypeEnforcer, false);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//        	
//        });
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	
	static BuildGraphFromSingleExistingRecordOperation make() {
		///////////////////////////////
		OperationName name = new OperationName("test");
		VfNotes notes = VfNotes.makeVisframeDefinedVfNotes();
		
		Map<SimpleName, Object> operationLevelParameterObjectValueMap = 
				BuildGraphFromSingleExistingRecordOperation.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(name, notes);
		
		//////////////////////
		MetadataID inputEdgeRecordDataMetadataID = new MetadataID(new MetadataName("mono233_NODE"), DataType.RECORD);
		
		LinkedHashSet<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeID = new LinkedHashSet<>();
		inputEdgeDataTableColumnSetAsEdgeID.add(new DataTableColumnName("NODE_ID"));
		inputEdgeDataTableColumnSetAsEdgeID.add(new DataTableColumnName("PARENT_ID"));
		
		LinkedHashSet<DataTableColumnName> inputEdgeDataTableColumnSetAsAdditionalFeature = new LinkedHashSet<>();
		inputEdgeDataTableColumnSetAsAdditionalFeature.add(new DataTableColumnName("LEAF_INDEX"));
		
		boolean edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets = false;
		
		GraphTypeEnforcer graphTypeEnforcer = new GraphTypeEnforcer(false, null, true, true, true);
		boolean hasDirectedTypeIndicatorColumn = false;
		DataTableColumnName directedTypeIndicatorColumnName = null;
		HashMap<String,DirectedType> directedIndicatorColumnStringValueDirectedTypeMap = null;
		DirectedType defaultDirectedType = DirectedType.UNDIRECTED;
		
		MetadataName outputGraphDataName = new MetadataName("test_out");
		
		Map<SimpleName, Object> buildGraphFromExistingRecordOperationBaseLevelParameterObjectValueMap = 
				BuildGraphFromSingleExistingRecordOperation.buildBuildGraphFromExistingRecordOperationBaseLevelSpecificParameterNameValueObjectMap(
						inputEdgeRecordDataMetadataID, inputEdgeDataTableColumnSetAsEdgeID, inputEdgeDataTableColumnSetAsAdditionalFeature, 
						edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets, graphTypeEnforcer, hasDirectedTypeIndicatorColumn, 
						directedTypeIndicatorColumnName, directedIndicatorColumnStringValueDirectedTypeMap, defaultDirectedType, 
						outputGraphDataName);
		
		
		
		
		
		////////////////////////////
		LinkedHashSet<DataTableColumnName> sourceVertexIDColumnNameLinkedHashSet = new LinkedHashSet<>();
		sourceVertexIDColumnNameLinkedHashSet.add(new DataTableColumnName("NODE_ID"));
		LinkedHashSet<DataTableColumnName> sinkVertexIDColumnNameLinkedHashSet = new LinkedHashSet<>();
		sinkVertexIDColumnNameLinkedHashSet.add(new DataTableColumnName("PARENT_ID"));
		
		Map<SimpleName, Object> buildGraphFromSingleExistingRecordOperationLevelParameterObjectValueMap = 
				BuildGraphFromSingleExistingRecordOperation.buildBuildGraphFromSingleExistingRecordOperationLevelSpecificParameterNameValueObjectMap(
						sourceVertexIDColumnNameLinkedHashSet, sinkVertexIDColumnNameLinkedHashSet);
		
		
		
		return new BuildGraphFromSingleExistingRecordOperation(
				operationLevelParameterObjectValueMap,
				buildGraphFromExistingRecordOperationBaseLevelParameterObjectValueMap,
				buildGraphFromSingleExistingRecordOperationLevelParameterObjectValueMap,
				true
				);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
