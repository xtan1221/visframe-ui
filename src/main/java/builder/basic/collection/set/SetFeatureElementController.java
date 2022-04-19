package builder.basic.collection.set;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NodeBuilder;
import javafx.fxml.FXML;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;

public class SetFeatureElementController<E> {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/set/SetFeatureElement.fxml";
	
	private NodeBuilder<E,?> elementBuilder;
	
	/**
	 * set the builder for key and value;
	 * 
	 * must be invoked upon the controller is loaded;
	 * 
	 * @param keyBuilder
	 * @param valueBuilder
	 * @throws IOException
	 */
	public void setBuilder(NodeBuilder<E,?> elementBuilder) throws IOException {
		this.elementBuilder = elementBuilder;
		
		this.setElementHBox.getChildren().add(this.getElementBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane()); //
	}
	
	
	public NodeBuilder<E,?> getElementBuilder() {
		return elementBuilder;
	}
	
	public boolean isSelected() {
		return this.elementCheckBox.isSelected();
	}
	
	public void setSelected(boolean selected) {
		this.elementCheckBox.setSelected(selected);
	}
	
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.elementCheckBox.setVisible(modifiable);
		
		this.elementBuilder.setModifiable(modifiable);
	}

	public Pane getRootPane() {
		return this.rootHBox;
	}
	
	/////////////////////////////////
	@FXML
	public void initialize(){
		
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private HBox rootHBox;
	@FXML
	private CheckBox elementCheckBox;
	@FXML
	private HBox setElementHBox;

}
