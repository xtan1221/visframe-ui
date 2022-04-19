package viewer.visframe.generic.tree.trim.helper;

import generic.tree.trim.helper.SiblingReorderPattern;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class SiblingReorderPatternViewerController extends AbstractViewerController<SiblingReorderPattern>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/generic/tree/trim/helper/SiblingReorderPatternViewer.fxml";
	
	///////////////////////////
	@Override
	protected void setup() {
		int rowIndex = 1;
		for(int parentNodeID:this.getViewer().getValue().getParentNodeIDToOriginalSwappedIndexMapMap().keySet()) {
			TextField parentNodeIDTf = new TextField();
			parentNodeIDTf.setText(Integer.toString(parentNodeID));
			parentNodeIDTf.setEditable(false);
			this.rootContainerGridPane.add(parentNodeIDTf, 0, rowIndex);
			
			for(int originalIndex:this.getViewer().getValue().getParentNodeIDToOriginalSwappedIndexMapMap().get(parentNodeID).keySet()) {
				TextField originalIndexTf = new TextField();
				originalIndexTf.setText(Integer.toString(originalIndex));
				originalIndexTf.setEditable(false);
				
				TextField swappedIndexTf = new TextField();
				swappedIndexTf.setText(Integer.toString(this.getViewer().getValue().getParentNodeIDToOriginalSwappedIndexMapMap().get(parentNodeID).get(originalIndex)));
				swappedIndexTf.setEditable(false);
				
				this.rootContainerGridPane.add(originalIndexTf, 1, rowIndex);
				this.rootContainerGridPane.add(swappedIndexTf, 2, rowIndex);
				
				rowIndex++;
			}
		}
		
	}
	
	@Override
	public Pane getRootContainerPane() {
		return rootContainerGridPane;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public SiblingReorderPatternViewer getViewer() {
		return (SiblingReorderPatternViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}
	@FXML
	private GridPane rootContainerGridPane;
}
