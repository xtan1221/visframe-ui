package viewer.visframe.function.component;

import function.component.SimpleFunction;
import function.variable.output.type.CFGTargetOutputVariable;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import utils.LayoutCoordinateAndSizeUtils;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.evaluator.EvaluatorViewerFactory;
import viewer.visframe.function.target.CFGTargetViewerBase;
import viewer.visframe.function.target.CFGTargetViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class SimpleFunctionViewerController extends ComponentFunctionViewerControllerBase<SimpleFunction>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/component/SimpleFunctionViewer.fxml";
	
	@Override
	protected void setup() {
		//first add the targets calculated by this SimpleFunction
		this.getViewer().getValue().getOutputVariableSet().forEach(ov->{
			if(ov instanceof CFGTargetOutputVariable) {
				CFGTargetOutputVariable tov = (CFGTargetOutputVariable)ov;
				
				CFGTargetViewerBase<?, ?> targetViewer = 
						CFGTargetViewerFactory.build(tov.getTarget(), this.getViewer().getHostCompositionFunctionViewer().getHostCompositionFunctionGroup());
			
				this.assignedTargetsVBox.getChildren().add(targetViewer.getController().getRootContainerPane());
			}
		});
		
		
		//then add the evaluators
		this.getViewer().getValue().getEvaluatorIndexIDMap().forEach((index, eval)->{
			TextField indexTextField = new TextField();
			indexTextField.setText(Integer.toString(index));
			indexTextField.setEditable(false);
			
			TextField basicInforTextField = new TextField();
			basicInforTextField.setText(eval.toString());//TODO
			basicInforTextField.setEditable(false);
			
			Button detailInforButton = new Button();
			detailInforButton.setText("view details");
			
			EvaluatorViewer<?,?> ev = EvaluatorViewerFactory.build(eval, this.getViewer());
			this.getViewer().addEvaluatorViewer(eval.getIndexID(), ev);
			
			detailInforButton.setOnMouseClicked(e->{
				ev.show(this.getRootContainerPane().getScene().getWindow());//
			});
			
			this.evaluatorGridPane.add(indexTextField, 0, index+1);
			this.evaluatorGridPane.add(basicInforTextField, 1, index+1);
			this.evaluatorGridPane.add(detailInforButton, 2, index+1);
		});
		
	}
	
	@Override
	public Pane getRootContainerPane() {
		return rootContainerHBox;
	}
	
	
	@Override
	public Point2D getGotoPreviousCircleCenterCoord() {
		return LayoutCoordinateAndSizeUtils.getLayoutPoint2D(
				this.getViewer().getHostCompositionFunctionViewer().getController().getComponentFunctionTreeAnchorPane(), 
				this.prevoiusCompnentFunctionCircle);
	}
	
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public SimpleFunctionViewer getViewer() {
		return (SimpleFunctionViewer)this.viewer;
	}
	
	
	/**
	 * return the center coordinate of the {@link #gotoNextComponentFunctionCircle} on the AnchorPane of the CompositionFunction's tree;
	 * can only be invoked after the SimpleFunction's viewer is added to the AnchorPane;
	 * @return
	 */
	public Point2D getGotoNextCircleCenterCoord() {
		return LayoutCoordinateAndSizeUtils.getLayoutPoint2D(
				this.getViewer().getHostCompositionFunctionViewer().getController().getComponentFunctionTreeAnchorPane(), 
				this.gotoNextComponentFunctionCircle);
	}
	
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private Circle prevoiusCompnentFunctionCircle;
	@FXML
	private VBox assignedTargetsVBox;
	@FXML
	private GridPane evaluatorGridPane;
	@FXML
	private Circle gotoNextComponentFunctionCircle;

}
