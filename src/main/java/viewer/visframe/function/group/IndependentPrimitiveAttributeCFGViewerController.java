package viewer.visframe.function.group;

import java.util.ArrayList;
import java.util.List;

import function.group.IndependentPrimitiveAttributeCFG;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.TitledPaneToggleGroup;
import viewer.visframe.function.target.IndependentPrimitiveAttributeTargetViewer;
import viewer.visframe.metadata.MetadataIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class IndependentPrimitiveAttributeCFGViewerController extends CompositionFunctionGroupViewerControllerBase<IndependentPrimitiveAttributeCFG>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/group/IndependentPrimitiveAttributeCFGViewer.fxml";
	
	@Override
	protected void setup() {
		//set up viewer for information shared by all CFG types
		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.typeTextField.setText(this.getViewer().getValue().getTypeName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		MetadataIDViewer ownerRecordDataMetadataIDViewer = 
				new MetadataIDViewer(
						this.getViewer().getValue().getOwnerRecordDataMetadataID(), 
						this.getViewer().getHostVisframeContext());
		this.ownerRecordMetadataIDViewerHBox.getChildren().add(ownerRecordDataMetadataIDViewer.getController().getRootContainerPane());
		
		
		//
		this.setupTargets();
	}
	
	@SuppressWarnings("unused")
	private TitledPaneToggleGroup tpToggleGroup;
	private List<TitledPane> targetInforTitledPaneList;
	
	/**
	 * simply add viewer UI of each target to the targetsViewerVBox;
	 */
	protected void setupTargets() {
		this.targetInforTitledPaneList = new ArrayList<>();
		this.getViewer().getValue().getTargetNameMap().forEach((name, target)->{
			IndependentPrimitiveAttributeTargetViewer viewer = new IndependentPrimitiveAttributeTargetViewer(target);
			
			TitledPane tp = viewer.getController().getDetailInforTitledPane();
			this.targetsViewerVBox.getChildren().add(tp);
			
			
			targetInforTitledPaneList.add(tp);
			
		});
		
		this.tpToggleGroup = new TitledPaneToggleGroup(this.targetInforTitledPaneList);
	}
	
	
	@Override
	public IndependentPrimitiveAttributeCFGViewer getViewer() {
		return (IndependentPrimitiveAttributeCFGViewer)this.viewer;
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField typeTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private HBox ownerRecordMetadataIDViewerHBox;
	@FXML
	protected VBox targetsViewerVBox;
}
