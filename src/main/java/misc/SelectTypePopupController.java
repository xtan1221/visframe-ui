package misc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class SelectTypePopupController<T> {
	public static final String FXML_FILE_DIR_STRING = "/misc/SelectTypePopup.fxml";
	
	
	private SelectTypePopupBuilder<T> ownerBuilder;
	
	void setOwnerBuilder(SelectTypePopupBuilder<T> ownerBuilder) {
		this.ownerBuilder = ownerBuilder;
		
		this.selectionComboBox.getItems().addAll(this.ownerBuilder.getPool());
		
		this.selectionComboBox.setCellFactory(new Callback<ListView<T>,ListCell<T>>(){
            @Override
            public ListCell<T> call(ListView<T> l){
                return new ListCell<T>(){
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(SelectTypePopupController.this.ownerBuilder.getToStringFunction().apply(item));
                        }
                    }
                };
            }
        });
		
		//set the first as the default one
		this.selectionComboBox.setValue(this.ownerBuilder.getPool().iterator().next());
	}
	
	
	Parent getRootContainerNode() {
		return rootContainerVBox;
	}
	
	
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private ComboBox<T> selectionComboBox;
	@FXML
	private Button cancelButton;
	@FXML
	private Button confirmButton;

	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancelButtonOnAction(ActionEvent event) {
		this.ownerBuilder.setSelectedType(null);
		this.ownerBuilder.getPopupStage().close();
	}
	
	
	// Event Listener on Button[#confirmButton].onAction
	@FXML
	public void confirmButtonOnAction(ActionEvent event) {
		this.ownerBuilder.setSelectedType(this.selectionComboBox.getSelectionModel().getSelectedItem());
		this.ownerBuilder.getPopupStage().close();
	}
}
