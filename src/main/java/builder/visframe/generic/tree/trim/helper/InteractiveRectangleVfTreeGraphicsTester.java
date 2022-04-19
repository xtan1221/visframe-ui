package builder.visframe.generic.tree.trim.helper;

import java.util.LinkedHashSet;

import generic.tree.reader.filebased.newick.SimpleNewickFileTreeReader;
import generic.tree.reader.projectbased.VfDataTreeReader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import metadata.MetadataID;
import metadata.MetadataName;
import rdb.table.data.DataTableColumnName;
import test.VisProjectDBContextTest;

public class InteractiveRectangleVfTreeGraphicsTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {

//        MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono1_bootstrap100"), DataType.vfTREE);
//		MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono1_revised2"), DataType.vfTREE);
        MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono6813"), DataType.vfTREE);
//        MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono251"), DataType.vfTREE); //structure is validated
//        MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono32"), DataType.vfTREE);
//        MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono32_revised"), DataType.vfTREE);//negative edge set to 0
//        MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono10"), DataType.vfTREE);
        
		LinkedHashSet<DataTableColumnName> nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
	    nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded.add(SimpleNewickFileTreeReader.newickTreeNodeLabelStringColumn().getName());
	    LinkedHashSet<DataTableColumnName> nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
        VfDataTreeReader reader = new VfDataTreeReader(
        		VisProjectDBContextTest.getConnectedProject2(), 
        		vfTreeMetadataID, 
        		nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded, 
          		nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded);
        
        reader.perform();
        
        
        InteractiveRectangleVfTreeGraphics builder = new InteractiveRectangleVfTreeGraphics(reader, true, true, false, false);
        builder.setTreeInteractive(true);
        
        //////////////////////////////////////
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        HBox operationHBox = new HBox();
        
        root.getChildren().add(btn);
        root.getChildren().add(operationHBox);
        ScrollPane sp = new ScrollPane();
        root.getChildren().add(sp);
        
       
        sp.setContent(builder.getCanvass());
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sp.setContent(builder.getCanvass());
            }
        });
        
        
//        TagFormat tagFormat = new TagFormat(
//        		false, null, 
//        		SQLDataTypeFactory.booleanType(),
//        		new PlainStringMarker("=", false),
//        		0, 1, null);
//        		
//        
        Button button2 = new Button("zoom in");
        operationHBox.getChildren().add(button2);
        button2.setOnAction(e->{
			builder.zoomIn();
        });
        
        Button button3 = new Button("zoom out");
        operationHBox.getChildren().add(button3);
        button3.setOnAction(e->{
			builder.zoomOut();
        });
        
        
        Button button4 = new Button("x zoom in");
        operationHBox.getChildren().add(button4);
        button4.setOnAction(e->{
			builder.zoomInX();
        });
        
        
        Button button5 = new Button("x zoom out");
        operationHBox.getChildren().add(button5);
        button5.setOnAction(e->{
			builder.zoomOutX();
        });
        
        
        Button button6 = new Button("y zoom in");
        operationHBox.getChildren().add(button6);
        button6.setOnAction(e->{
			builder.zoomInY();;
        });
        
        
        Button button7 = new Button("y zoom out");
        operationHBox.getChildren().add(button7);
        button7.setOnAction(e->{
			builder.zoomOutY();;
        });
        
        Button button8 = new Button("Clear all selection");
        operationHBox.getChildren().add(button8);
        button8.setOnAction(e->{
			builder.clearTreeNodeMarkSet();
        });
        
        
        CheckBox showBootstrapCB = new CheckBox("show bootstrap");
        operationHBox.getChildren().add(showBootstrapCB);
        showBootstrapCB.selectedProperty().addListener(e->{
        	if(showBootstrapCB.isSelected()) {
        		builder.showBootstrap();
        	}else {
        		builder.hideBootstrap();
        	}
        });
        
        CheckBox showEdgeLengthCB = new CheckBox("show edge length");
        operationHBox.getChildren().add(showEdgeLengthCB);
        showEdgeLengthCB.selectedProperty().addListener(e->{
        	if(showEdgeLengthCB.isSelected()) {
        		builder.showEdgeLength();
        	}else {
        		builder.hideEdgeLength();
        	}
        });
        
        CheckBox showNodeFeatureCB = new CheckBox("show node feature");
        operationHBox.getChildren().add(showNodeFeatureCB);
        showNodeFeatureCB.selectedProperty().addListener(e->{
        	if(showNodeFeatureCB.isSelected()) {
        		builder.showNodeNonMandatoryAdditionalFeatures();
        	}else {
        		builder.hideNodeNonMandatoryAdditionalFeatures();
        	}
        });
        
        CheckBox showEdgeFeatureCB = new CheckBox("show edge feature");
        operationHBox.getChildren().add(showEdgeFeatureCB);
        showEdgeFeatureCB.selectedProperty().addListener(e->{
        	if(showEdgeFeatureCB.isSelected()) {
        		builder.showEdgeNonMandatoryAdditionalFeatures();
        	}else {
        		builder.hideEdgeNonMandatoryAdditionalFeatures();
        	}
        });
        
        
        
        ChoiceBox<Double> nodeRadiusChoiceBox = new ChoiceBox<>();
        nodeRadiusChoiceBox.getItems().add(5d);
        nodeRadiusChoiceBox.getItems().add(10d);
        nodeRadiusChoiceBox.getItems().add(15d);
        operationHBox.getChildren().add(nodeRadiusChoiceBox);
        nodeRadiusChoiceBox.getSelectionModel().selectedItemProperty().addListener(e->{
        	if(nodeRadiusChoiceBox.getSelectionModel().getSelectedItem()==null) {
        		
        	}else {
        		builder.setNodeEllipseRadius(nodeRadiusChoiceBox.getSelectionModel().getSelectedItem());
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
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
