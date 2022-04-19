package viewer.visframe.function.group;

import java.util.ArrayList;
import java.util.List;

import function.group.ShapeCFG;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.TitledPaneToggleGroup;
import viewer.visframe.function.target.LeafGraphicsPropertyCFGTargetViewer;
import viewer.visframe.graphics.shape.VfShapeTypeViewer;
import viewer.visframe.metadata.MetadataIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class ShapeCFGViewerController extends CompositionFunctionGroupViewerControllerBase<ShapeCFG>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/group/ShapeCFGViewer.fxml";
	
	//////////////////////////////////////
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
		
		///////////////////
		this.shapeTypeNameTextField.setText(this.getViewer().getValue().getShapeType().getName().getStringValue());
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
			
			LeafGraphicsPropertyCFGTargetViewer viewer = 
					new LeafGraphicsPropertyCFGTargetViewer(
							target, 
							this.getViewer().getValue().getGraphicsPropertyTree(name));
			
			TitledPane tp = viewer.getController().getDetailInforTitledPane();
			this.targetsViewerVBox.getChildren().add(tp);
			
			targetInforTitledPaneList.add(tp);
			
		});
		
		this.tpToggleGroup = new TitledPaneToggleGroup(this.targetInforTitledPaneList);
	}
	
	
	@Override
	public ShapeCFGViewer getViewer() {
		return (ShapeCFGViewer)this.viewer;
	}
	
	@Override
	public Parent getRootContainerPane() {
		return this.rootContainerScrollPane;
	}
	
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private ScrollPane rootContainerScrollPane;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField typeTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private HBox ownerRecordMetadataIDViewerHBox;
	@FXML
	private TextField shapeTypeNameTextField;
	@FXML
	private Button viewShapeTypeDetailButton;
	@FXML
	private VBox targetsViewerVBox;

	/////////////////////////////
	private VfShapeTypeViewer shapeTypeViewer;
	// Event Listener on Button[#viewShapeTypeDetailButton].onAction
	@FXML
	public void viewShapeTypeDetailButtonOnAction(ActionEvent event) {
		if(this.shapeTypeViewer == null)
			this.shapeTypeViewer = new VfShapeTypeViewer(this.getViewer().getValue().getShapeType());
		
		this.shapeTypeViewer.show(this.getRootContainerPane().getScene().getWindow());
	}
}
