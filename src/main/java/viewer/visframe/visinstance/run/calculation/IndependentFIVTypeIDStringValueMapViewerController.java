package viewer.visframe.visinstance.run.calculation;

import function.variable.independent.IndependentFreeInputVariableTypeID;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import viewer.AbstractViewerController;
import viewer.visframe.function.variable.independent.IndependentFreeInputVariableTypeIDViewer;
import visinstance.run.calculation.IndependentFIVTypeIDStringValueMap;

/**
 * 
 * @author tanxu
 *
 */
public class IndependentFIVTypeIDStringValueMapViewerController extends AbstractViewerController<IndependentFIVTypeIDStringValueMap>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/visinstance/run/calculation/IndependentFIVTypeIDStringValueMapViewer.fxml";
	
	
	@Override
	protected void setup() {
		int i = 1;
		for(IndependentFreeInputVariableTypeID id:this.getViewer().getValue().getIndependentFreeInputVariableTypeIDAssignedStringValueMap().keySet()) {
			IndependentFreeInputVariableTypeIDViewer independentFreeInputVariableTypeIDViewer = 
					new IndependentFreeInputVariableTypeIDViewer(id, this.getViewer().getHostVisProjectDBContext());
			
			this.rootContainerGridPane.add(independentFreeInputVariableTypeIDViewer.getController().getRootContainerPane(), i, 0);
			TextField valueStringTextField = new TextField(this.getViewer().getValue().getIndependentFreeInputVariableTypeIDAssignedStringValueMap().get(id));
			this.rootContainerGridPane.add(valueStringTextField, i, 0);
			
			i++;
		}
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerGridPane;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public IndependentFIVTypeIDStringValueMapViewer getViewer() {
		return (IndependentFIVTypeIDStringValueMapViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private GridPane rootContainerGridPane;
}
