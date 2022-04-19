package viewer.visframe.generic.tree.trim.helper;

import generic.tree.trim.helper.PositionOnTree;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class PositionOnTreeViewerController extends AbstractViewerController<PositionOnTree>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/generic/tree/trim/helper/PositionOnTreeViewer.fxml";
	
	///////////////////////////
	@Override
	protected void setup() {
		this.childNodeIDTextField.setText(Integer.toString(this.getViewer().getValue().getChildNodeID()));
		//
		if(this.getViewer().getValue().getParentNodeID()==null) {
			this.parentNodeIDTextField.setDisable(true);
		}else {
			this.parentNodeIDTextField.setText(Integer.toString(this.getViewer().getValue().getParentNodeID()));
		}
		//
		if(this.getViewer().getValue().getParentNodeID()==null) {
			this.positionTextField.setDisable(true);
		}else {
			this.positionTextField.setText(Double.toString(this.getViewer().getValue().getPos()));
		}
		
		//
		this.onRootNodeCheckBox.setSelected(this.getViewer().getValue().isOnRootNode());
		
	}
	
	@Override
	public Pane getRootContainerPane() {
		return rootContainerGridPane;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public PositionOnTreeViewer getViewer() {
		return (PositionOnTreeViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}
	@FXML
	private GridPane rootContainerGridPane;
	@FXML
	private TextField childNodeIDTextField;
	@FXML
	private TextField parentNodeIDTextField;
	@FXML
	private CheckBox onRootNodeCheckBox;
	@FXML
	private TextField positionTextField;
}
