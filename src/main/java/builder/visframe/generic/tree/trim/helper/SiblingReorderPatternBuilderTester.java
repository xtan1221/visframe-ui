package builder.visframe.generic.tree.trim.helper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import generic.tree.reader.filebased.newick.SimpleNewickFileTreeReader;
import generic.tree.reader.projectbased.VfDataTreeReader;
import generic.tree.trim.helper.SiblingReorderPattern;
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
import rdb.table.data.DataTableColumnName;
import test.VisProjectDBContextTest;

public class SiblingReorderPatternBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono1_bootstrap100"), DataType.vfTREE);
//		MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono1_revised2"), DataType.vfTREE);
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono6813"), DataType.vfTREE);
      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono15"), DataType.vfTREE); //structure is validated
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono32"), DataType.vfTREE);
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono32_revised"), DataType.vfTREE);//negative edge set to 0
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono10"), DataType.vfTREE);
      	
		LinkedHashSet<DataTableColumnName> nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
		nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded.add(SimpleNewickFileTreeReader.newickTreeNodeLabelStringColumn().getName());
	    LinkedHashSet<DataTableColumnName> nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
	    VfDataTreeReader reader = new VfDataTreeReader(
      		VisProjectDBContextTest.getConnectedProject11(), 
      		vfTreeMetadataID, 
      		nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded, 
      		nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded);
      
	    reader.perform();
      
	    InteractiveRectangleVfTreeGraphics tree = new InteractiveRectangleVfTreeGraphics(reader,true, true, false, false);
	    
	    SiblingReorderPatternBuilder builder = new SiblingReorderPatternBuilder(
        		"test", "test", true, null);
        
        
        //////////////////////
        VBox root = new VBox();
        
        
        //////////////////
        Button btn = new Button();
        root.getChildren().add(btn);
        btn.setText("start");
 
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
            }
        });
        
        //////////////////////////
        Button button2 = new Button("set tree");
        root.getChildren().add(button2);
        
        button2.setOnAction(e->{
        	builder.setInteractiveTreeGraphics(tree);
        });
        
        /////////////////////////////
        Map<Integer,Map<Integer,Integer>> map = new HashMap<>();
        Map<Integer,Integer> swapMap = new HashMap<>();
        swapMap.put(0, 0);
        swapMap.put(1, 2);
        swapMap.put(2, 1);
        map.put(0, swapMap);
        
        SiblingReorderPattern pattern = new SiblingReorderPattern(map);
        
        //////////////////////////
        Button button3 = new Button("set pattern");
        root.getChildren().add(button3);
        
        button3.setOnAction(e->{
        	builder.getEmbeddedUIContentController().setUIToNonNullValue(pattern);
        });
        
        ///////////////////
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
