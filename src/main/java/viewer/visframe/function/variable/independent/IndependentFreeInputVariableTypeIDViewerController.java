package viewer.visframe.function.variable.independent;

import java.sql.SQLException;

import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.independent.IndependentFreeInputVariableTypeID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import viewer.AbstractViewerController;
import viewer.visframe.function.composition.CompositionFunctionIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class IndependentFreeInputVariableTypeIDViewerController extends AbstractViewerController<IndependentFreeInputVariableTypeID>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/independent/IndependentFreeInputVariableTypeIDViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
		
		CompositionFunctionIDViewer compositionFunctionIDViewer = 
				new CompositionFunctionIDViewer(this.getViewer().getValue().getOwnerCompositionFunctionID(), this.getViewer().getHostVisframeContext());
		
		this.ownerCFIDHBox.getChildren().add(compositionFunctionIDViewer.getController().getRootContainerPane());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public IndependentFreeInputVariableTypeIDViewer getViewer() {
		return (IndependentFreeInputVariableTypeIDViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField aliasNameTextField;
	@FXML
	private HBox ownerCFIDHBox;
	@FXML
	private Button viewIndieFIVDetailButton;

	//////////////////////////////////////
	private IndependentFreeInputVariableTypeViewer independentFreeInputVariableTypeViewer;
	private Stage independentFreeInputVariableTypeStage;
	private Scene independentFreeInputVariableTypeScene;
	@FXML
	public void viewIndieFIVDetailButtonOnAction(ActionEvent event) throws SQLException {
		if(this.independentFreeInputVariableTypeViewer == null) {
			IndependentFreeInputVariableType type = this.getViewer().getHostVisframeContext().getIndependentFreeInputVariableTypeLookup().lookup(this.getViewer().getValue());
			
			this.independentFreeInputVariableTypeViewer = 
					new IndependentFreeInputVariableTypeViewer(type, this.getViewer().getHostVisframeContext());
		
			this.independentFreeInputVariableTypeScene = new Scene(this.independentFreeInputVariableTypeViewer.getController().getRootContainerPane());
			
			this.independentFreeInputVariableTypeStage = new Stage();
			this.independentFreeInputVariableTypeStage.setScene(this.independentFreeInputVariableTypeScene);
			this.independentFreeInputVariableTypeStage.initModality(Modality.WINDOW_MODAL);
			this.independentFreeInputVariableTypeStage.initOwner(this.getRootContainerPane().getScene().getWindow());
		}
		
		this.independentFreeInputVariableTypeStage.show();
	}
}
