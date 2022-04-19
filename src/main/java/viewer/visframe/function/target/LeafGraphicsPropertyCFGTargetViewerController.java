package viewer.visframe.function.target;

import function.target.LeafGraphicsPropertyCFGTarget;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.FXUtils;
import viewer.visframe.graphics.property.tree.GraphicsPropertyTreeViewer;

/**
 * 
 * @author tanxu
 *
 */
public class LeafGraphicsPropertyCFGTargetViewerController extends CFGTargetViewerControllerBase<LeafGraphicsPropertyCFGTarget<?>>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/target/LeafGraphicsPropertyCFGTargetViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.detailInforTitledPane.setText(this.getViewer().getValue().getName().toString());
		//////////////////
		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.dataTypeTextField.setText(this.getViewer().getValue().getSQLDataType().getSQLString());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		this.canBeNullRadioButton.setSelected(this.getViewer().getValue().canBeNull());
		
		this.hasDefaultValueRadioButton.setSelected(this.getViewer().getValue().hasNonNullDefaultValue());
		if(this.getViewer().getValue().hasNonNullDefaultValue()) {
			this.defaultValueTextField.setText(this.getViewer().getValue().getDefaultStringValue());
		}else {
			FXUtils.set2Disable(this.defaultValueTextField, true);
		}
		
		this.isMandatoryRadioButton.setSelected(this.getViewer().getValue().isMandatory());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public LeafGraphicsPropertyCFGTargetViewer getViewer() {
		return (LeafGraphicsPropertyCFGTargetViewer)this.viewer;
	}
	
	@Override
	public TitledPane getDetailInforTitledPane() {
		return this.detailInforTitledPane;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TitledPane detailInforTitledPane;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private RadioButton canBeNullRadioButton;
	@FXML
	private TextField defaultValueTextField;
	@FXML
	private TextField dataTypeTextField;
	@FXML
	private RadioButton hasDefaultValueRadioButton;
	@FXML
	private RadioButton isMandatoryRadioButton;
	@FXML
	private Button viewGraphicsPropertyTreeButton;
	
	
	
	// Event Listener on Button[#viewGraphicsPropertyTreeButton].onAction
	private GraphicsPropertyTreeViewer graphicsPropertyTreeViewer;
	@FXML
	public void viewGraphicsPropertyTreeButtonOnAction(ActionEvent event) {
		if(graphicsPropertyTreeViewer == null) {
			this.graphicsPropertyTreeViewer = new GraphicsPropertyTreeViewer(this.getViewer().getOwnerGraphicsPropertyTree());
		}
		
		this.graphicsPropertyTreeViewer.show(this.getRootContainerPane().getScene().getWindow());
	}
}
