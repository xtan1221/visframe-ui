package builder.visframe.operation.sql.predefined.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

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
import metadata.record.RecordDataMetadata;
import operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegate;
import rdb.sqltype.SQLDataTypeFactory;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;
import rdb.table.data.DataTableColumnName;
import symja.VfSymjaExpressionString;
import symja.VfSymjaSinglePrimitiveOutputExpression;
import symja.VfSymjaVariableName;
import test.VisProjectDBContextTest;

public class CumulativeColumnSymjaExpressionDelegateBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        
        VBox root = new VBox();
        
        
        CumulativeColumnSymjaExpressionDelegateBuilder builder = new CumulativeColumnSymjaExpressionDelegateBuilder(
        		"test", "test", true, null);
        
        ///
        
  
        
        //////////////////////
        RecordDataMetadata recordData = 
        		(RecordDataMetadata) VisProjectDBContextTest.getConnectedProject1().getHasIDTypeManagerController().getMetadataManager()
        		.lookup(new MetadataID(new MetadataName("gff3_23"), DataType.RECORD));
        
        
        Button btn2 = new Button();
        btn2.setText("set data table schema");
        root.getChildren().add(btn2);
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	builder.setDataTableSchema(recordData.getDataTableSchema());
            }
        });
        
        ///////////////////////////////////
        String expression = "a+b-5*c"; //b ==> score, double; c==> start, int
        
        Map<VfSymjaVariableName, VfDefinedPrimitiveSQLDataType> variableNameSQLDataTypeMap = new HashMap<>();
        
        variableNameSQLDataTypeMap.put(new VfSymjaVariableName("a"), SQLDataTypeFactory.doubleType());
        variableNameSQLDataTypeMap.put(new VfSymjaVariableName("b"), SQLDataTypeFactory.doubleType());
        variableNameSQLDataTypeMap.put(new VfSymjaVariableName("c"), SQLDataTypeFactory.integerType());
        
        
        VfSymjaSinglePrimitiveOutputExpression symjaExpression = new VfSymjaSinglePrimitiveOutputExpression(
        		SQLDataTypeFactory.doubleType(),
        		new VfSymjaExpressionString(expression),
        		variableNameSQLDataTypeMap
        		);
		
		BiMap<DataTableColumnName,VfSymjaVariableName> columnSymjaVariableNameMap  = HashBiMap.create();
		columnSymjaVariableNameMap.put(new DataTableColumnName("score"), new VfSymjaVariableName("b"));
		columnSymjaVariableNameMap.put(new DataTableColumnName("start"), new VfSymjaVariableName("c"));
		
		
		VfSymjaVariableName previouseRecordCumulativeColumnSymjaVariableName = new VfSymjaVariableName("a");
        
		
		////////////////
        CumulativeColumnSymjaExpressionDelegate value = new CumulativeColumnSymjaExpressionDelegate(
        		symjaExpression,
        		columnSymjaVariableNameMap,
        		previouseRecordCumulativeColumnSymjaVariableName
        		);
        
        
        
		Button btn = new Button();
		btn.setText("set value");
		root.getChildren().add(btn);
		  
		btn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		     public void handle(ActionEvent event) {
		   	 	try {
					builder.setValue(value, false);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		});
      
        ///////////////
        root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
        
        
        
        //////////////////////
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
