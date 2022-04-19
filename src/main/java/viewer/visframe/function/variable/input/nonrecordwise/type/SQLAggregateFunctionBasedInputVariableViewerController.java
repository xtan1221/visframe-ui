package viewer.visframe.function.variable.input.nonrecordwise.type;

import function.variable.input.nonrecordwise.type.SQLAggregateFunctionBasedInputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableViewerController;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewer;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewerFactory;
import viewer.visframe.metadata.MetadataIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class SQLAggregateFunctionBasedInputVariableViewerController extends NonRecordwiseInputVariableViewerController<SQLAggregateFunctionBasedInputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/input/nonrecordwise/type/SQLAggregateFunctionBasedInputVariableViewer.fxml";
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
		this.SQLAggregateFunctionTypeTextField.setText(this.getViewer().getValue().getAggregateFunctionType().toString());
		
		MetadataIDViewer targetRecordMetadataIDViewer = 
				new MetadataIDViewer(
						this.getViewer().getValue().getTargetRecordMetadataID(), 
						this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer().getHostVisframeContext());
		this.targetRecordMetadataIDViewerHBox.getChildren().add(targetRecordMetadataIDViewer.getController().getRootContainerPane());
		
		//
		if(this.getViewer().getValue().getRecordwiseInputVariable1()!=null) {
			int hostComponentFunctionIndexID = this.getViewer().getValue().getRecordwiseInputVariable1().getHostComponentFunctionIndexID();
			int hostEvaluatorIndexID = this.getViewer().getValue().getRecordwiseInputVariable1().getHostEvaluatorIndexID();
			EvaluatorViewer<?,?> hostEvaluatorViewer = 
					this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer()
					.getComponentFunctionIndexViewerMap().get(hostComponentFunctionIndexID).getEvaluatorIndexViewerMap().get(hostEvaluatorIndexID);
			
			RecordwiseInputVariableViewer<?,?> recordwiseInputVariable1Viewer = 
					RecordwiseInputVariableViewerFactory.build(
							this.getViewer().getValue().getRecordwiseInputVariable1(), 
							hostEvaluatorViewer);
			
			this.recordwiseInputVariable1ViewerHBox.getChildren().add(recordwiseInputVariable1Viewer.getController().getRootContainerPane());
		}
		
		//
		if(this.getViewer().getValue().getRecordwiseInputVariable2()!=null) {
			int hostComponentFunctionIndexID = this.getViewer().getValue().getRecordwiseInputVariable2().getHostComponentFunctionIndexID();
			int hostEvaluatorIndexID = this.getViewer().getValue().getRecordwiseInputVariable2().getHostEvaluatorIndexID();
			EvaluatorViewer<?,?> hostEvaluatorViewer = 
					this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer()
					.getComponentFunctionIndexViewerMap().get(hostComponentFunctionIndexID).getEvaluatorIndexViewerMap().get(hostEvaluatorIndexID);
			
			RecordwiseInputVariableViewer<?,?> recordwiseInputVariable1Viewer = 
					RecordwiseInputVariableViewerFactory.build(
							this.getViewer().getValue().getRecordwiseInputVariable2(), 
							hostEvaluatorViewer);
			
			this.recordwiseInputVariable2ViewerHBox.getChildren().add(recordwiseInputVariable1Viewer.getController().getRootContainerPane());
		}
		
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public SQLAggregateFunctionBasedInputVariableViewer getViewer() {
		return (SQLAggregateFunctionBasedInputVariableViewer)this.viewer;
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
	private TextField SQLAggregateFunctionTypeTextField;
	@FXML
	private Button notesButton;
	@FXML
	private HBox targetRecordMetadataIDViewerHBox;
	@FXML
	private HBox recordwiseInputVariable1ViewerHBox;
	@FXML
	private HBox recordwiseInputVariable2ViewerHBox;
}
