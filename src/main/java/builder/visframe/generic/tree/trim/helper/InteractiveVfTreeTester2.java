package builder.visframe.generic.tree.trim.helper;

import java.sql.SQLException;
import java.util.LinkedHashSet;

import generic.tree.reader.filebased.newick.SimpleNewickFileTreeReader;
import generic.tree.reader.projectbased.VfDataTreeReader;
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

public class InteractiveVfTreeTester2{
	static void test() throws SQLException{
//		MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono1_revised2"), DataType.vfTREE);
      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono6813"), DataType.vfTREE);
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono251"), DataType.vfTREE); //structure is validated
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono32"), DataType.vfTREE);
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono32_revised"), DataType.vfTREE);//negative edge set to 0
//      MetadataID vfTreeMetadataID = new MetadataID(new MetadataName("mono10"), DataType.vfTREE);
      
      LinkedHashSet<DataTableColumnName> nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
      nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded.add(SimpleNewickFileTreeReader.newickTreeNodeLabelStringColumn().getName());
      LinkedHashSet<DataTableColumnName> nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded = new LinkedHashSet<>();
      VfDataTreeReader reader = new VfDataTreeReader(
      		VisProjectDBContextTest.getConnectedProject2(), 
      		vfTreeMetadataID, 
      		nonMandatoryNodeAdditionalFeaturesColNameSetToBeIncluded, 
      		nonMandatoryEdgeAdditionalFeaturesColNameSetToBeIncluded);
      
      reader.perform();
      
      
      InteractiveRectangleVfTreeGraphics builder = new InteractiveRectangleVfTreeGraphics(reader, true, true, true, true);
      
      
      System.out.println(builder);
	}
	
	
	public static void main(String[] args) throws SQLException {
		test();
	}
	
}
