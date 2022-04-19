package viewer.visframe.metadata;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import metadata.Metadata;
import metadata.MetadataID;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class MetadataIDViewerController extends AbstractViewerController<MetadataID>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/MetadataIDViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.typeTextField.setText(this.getViewer().getValue().getDataType().toString());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public MetadataIDViewer getViewer() {
		return (MetadataIDViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField typeTextField;
	@FXML
	private Button viewDetailsButton;

	
	private MetadataViewerBase<?,?> metadataViewer;
	private Stage stage;
	private Scene scene;
	@FXML
	public void viewDetailsButtonOnAction(ActionEvent event) throws SQLException {		
		if(this.metadataViewer==null) {
			Metadata md = this.getViewer().getHostVisframeContext().getMetadataLookup().lookup(this.getViewer().getValue());
			this.metadataViewer = MetadataViewerFactory.singleton(this.getViewer().getHostVisframeContext()).build(md);
			
			this.scene = new Scene(this.metadataViewer.getController().getRootContainerPane());
			this.stage = new Stage();
			this.stage.setScene(this.scene);
			
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(this.getRootContainerPane().getScene().getWindow());
		}
		
		this.stage.show();
	}
}
