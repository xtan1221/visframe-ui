package core.builder.ui.embedded.content;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NodeBuilder;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * interface for fxml controller for the content part of a {@link NodeBuilder}
 * 
 * for each {@link LeafNodeBuilder} of a specific type T, the content UI should be different from each other and a subtype class should be implemented;
 * 
 * for each {@link NonLeafNodeBuilder} of a specific type T, the content UI will have the same set of features and do not need to build a subtype class
 * @author tanxu
 *
 */
public interface EmbeddedUIContentController<T> {
	
	/**
	 * set up the owner {@link NodeBuilder} of this {@link EmbeddedUIContentController}
	 * 
	 * @param ownerNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	void setOwnerNodeBuilder(NodeBuilder<T,?> ownerNodeBuilder) throws IOException, SQLException;
	
	
	/**
	 * set the owner {@link NodeBuilder}
	 * then perform any 
	 * @return
	 */
	NodeBuilder<T,?> getOwnerNodeBuilder();
	
	
	/**
	 * set whether the UI is modifiable or not;
	 * 
	 * @param modifiable
	 * @throws IOException 
	 * @throws SQLException 
	 */
	void setModifiable(boolean modifiable) throws SQLException, IOException;
	
	/////////////////////////////
	/**
	 * return the root {@link Parent} of this content UI;
	 * @return
	 */
	Parent getRootParentNode();
	
	/**
	 * return the stage
	 * @return
	 */
	default Stage getStage() {
		return (Stage) this.getRootParentNode().getScene().getWindow();
	}
	
	//////////////////////////
	@FXML
	void initialize();
	
}
