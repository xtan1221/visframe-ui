package builder.basic.collection.map;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NodeBuilder;
import javafx.fxml.FXML;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;

public class MapFeatureEntryController<K,V>{
	public static final String FXML_FILE_DIR = "/builder/basic/collection/map/MapFeatureEntry.fxml";
	
	private NodeBuilder<K,?> keyBuilder;
	private NodeBuilder<V,?> valueBuilder;
	
	/**
	 * set the builder for key and value;
	 * 
	 * must be invoked upon the controller is loaded;
	 * 
	 * @param keyBuilder
	 * @param valueBuilder
	 * @throws IOException
	 */
	public void setBuilder(NodeBuilder<K,?> keyBuilder, NodeBuilder<V,?> valueBuilder) throws IOException {
		this.keyBuilder = keyBuilder;
		
		this.valueBuilder = valueBuilder;
		
		this.entryKeyHBox.getChildren().add(this.getKeyBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane()); //
		this.entryValueHBox.getChildren().add(this.getValueBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane()); //
	}
	
	
	public NodeBuilder<K,?> getKeyBuilder(){
		return this.keyBuilder;
	}
	
	public NodeBuilder<V,?> getValueBuilder(){
		return this.valueBuilder;
	}
	
	
	public boolean isSelected() {
		return this.entryCheckBox.isSelected();
	}
	
	public void setSelected(boolean selected) {
		this.entryCheckBox.setSelected(selected);
	}
	
	public Pane getRootPane() {
		return this.rootHBox;
	}
	
	
	/**
	 * 
	 * @param modifiable
	 * @throws SQLException 
	 * @throws IOException
	 */
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.entryCheckBox.setVisible(modifiable);
		this.keyBuilder.setModifiable(modifiable);
		this.valueBuilder.setModifiable(modifiable);
	}
	
	//////////////////////////
	@FXML
	public void initialize(){
		
		// TODO Auto-generated method stub
		
	}
	
	
	@FXML
	private HBox rootHBox;
	@FXML
	private CheckBox entryCheckBox;
	@FXML
	private HBox entryKeyHBox;
	@FXML
	private HBox entryValueHBox;

	
}
