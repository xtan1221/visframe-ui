package viewer.visframe.function.variable.input.recordwise.type;

import java.sql.SQLException;

import function.group.CompositionFunctionGroup;
import function.variable.input.recordwise.type.CFGTargetInputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import viewer.visframe.function.group.CompositionFunctionGroupIDViewer;
import viewer.visframe.function.target.CFGTargetViewerBase;
import viewer.visframe.function.target.CFGTargetViewerFactory;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewerController;
import viewer.visframe.metadata.MetadataIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class CFGTargetInputVariableViewerController extends RecordwiseInputVariableViewerController<CFGTargetInputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/input/recordwise/type/CFGTargetInputVariableViewer.fxml";
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
		
		MetadataIDViewer targetRecordMetadataIDViewer = 
				new MetadataIDViewer(
						this.getViewer().getValue().getTargetRecordDataMetadataID(),
						this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer().getHostVisframeContext());
		this.targetRecordMetadataIDViewerHBox.getChildren().add(targetRecordMetadataIDViewer.getController().getRootContainerPane());
		
		CompositionFunctionGroupIDViewer targetCompositionFunctionGroupIDViewer = 
				new CompositionFunctionGroupIDViewer(
						this.getViewer().getValue().getTargetCompositionFunctionGroupID(), 
						this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer().getHostVisframeContext());
		this.targetCompositionFunctionGroupIDViewerHBox.getChildren().add(targetCompositionFunctionGroupIDViewer.getController().getRootContainerPane());
		
		
		try {
			CompositionFunctionGroup hostCompositionFunctionGroup = this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer().getHostVisframeContext().
			getCompositionFunctionGroupLookup().lookup(this.getViewer().getValue().getTargetCompositionFunctionGroupID());
			
			CFGTargetViewerBase<?,?> targetViewer = 
					CFGTargetViewerFactory.build(
							this.getViewer().getValue().getTarget(), 
							hostCompositionFunctionGroup);
			
			this.targetViewerHBox.getChildren().add(targetViewer.getController().getRootContainerPane());
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public CFGTargetInputVariableViewer getViewer() {
		return (CFGTargetInputVariableViewer)this.viewer;
	}


	@Override
	public Parent getRootContainerPane() {
		return rootContainerGridPane;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////
	@FXML
	private GridPane rootContainerGridPane;
	@FXML
	private TextField aliasNameTextField;
	@FXML
	private Button notesButton;
	@FXML
	private HBox targetRecordMetadataIDViewerHBox;
	@FXML
	private HBox targetCompositionFunctionGroupIDViewerHBox;
	@FXML
	private HBox targetViewerHBox;
}
