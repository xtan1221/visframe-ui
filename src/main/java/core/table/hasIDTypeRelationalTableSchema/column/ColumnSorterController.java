package core.table.hasIDTypeRelationalTableSchema.column;

import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * 
 * @author tanxu
 *
 */
public class ColumnSorterController {
	
	public static final String FXML_FILE_DIR_STRING = "/core/table/hasIDTypeRelationalTableSchema/column/ColumnSorter.fxml";
	
	//////////////////////////////////
	private ColumnSorterManager manager;
	
	/**
	 * set manager;
	 * 
	 * also set the TableView
	 * @param manager
	 * @throws SQLException 
	 */
	void setManager(ColumnSorterManager manager) throws SQLException {
		this.manager = manager;
		
		this.columnNameTextField.setText(this.manager.getColumnName());
		
		
		this.includedCheckBox.selectedProperty().addListener((obs,ov,nv)->{
			if(nv) {
				this.rootContainerHBox.setStyle("-fx-border-color:lightgreen");
			}else {
				this.rootContainerHBox.setStyle("-fx-border-color:red");
			}
		});
	}
	
	
	//////////////////////////////////////////
	public Pane getRootContainerNode() {
		return this.rootContainerHBox;
	}
	
	////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField columnNameTextField;
	@FXML
	ToggleButton sortByASCToggleButton;
	@FXML
	Button moveUpButton;
	@FXML
	Button moveDownButton;
	@FXML
	CheckBox includedCheckBox;

	// Event Listener on Button[#moveUpButton].onAction
	@FXML
	public void moveUpButtonOnAction(ActionEvent event) throws IOException, SQLException {
		this.manager.getHostHasIDTypeRelationalTableContentViewer().getController().switchColSorter(this.manager.getOrderIndex(), this.manager.getOrderIndex()-1);
	}
	
	// Event Listener on Button[#moveDownButton].onAction
	@FXML
	public void moveDownButtonOnAction(ActionEvent event) throws IOException, SQLException {
		this.manager.getHostHasIDTypeRelationalTableContentViewer().getController().switchColSorter(this.manager.getOrderIndex(), this.manager.getOrderIndex()+1);
	}
	
}
