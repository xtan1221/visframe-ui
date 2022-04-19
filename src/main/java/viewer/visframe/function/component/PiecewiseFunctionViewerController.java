package viewer.visframe.function.component;

import java.util.HashMap;
import java.util.Map;

import function.component.ComponentFunction;
import function.component.PiecewiseFunction;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utils.LayoutCoordinateAndSizeUtils;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.evaluator.EvaluatorViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class PiecewiseFunctionViewerController extends ComponentFunctionViewerControllerBase<PiecewiseFunction>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/component/PiecewiseFunctionViewer.fxml";
	
	private Map<Integer, Circle> conditionPrecedenceIndexGoToNextCircleMap;
	
	@Override
	protected void setup() {
		this.conditionPrecedenceIndexGoToNextCircleMap = new HashMap<>();
		
		//add the conditional evaluators
		this.getViewer().getValue().getConditionPrecedenceIndexConditionalEvaluatorMap().forEach((index, evalDelegate)->{
			TextField indexTextField = new TextField();
			indexTextField.setText(Integer.toString(index));
			indexTextField.setEditable(false);
			
			TextField basicInforTextField = new TextField();
			basicInforTextField.setText(evalDelegate.getEvaluator().toString());//TODO
			basicInforTextField.setEditable(false);
			
			Button detailInforButton = new Button();
			detailInforButton.setText("view details");
			
			EvaluatorViewer<?,?> ev = EvaluatorViewerFactory.build(evalDelegate.getEvaluator(), this.getViewer());
			this.getViewer().addEvaluatorViewer(evalDelegate.getEvaluator().getIndexID(), ev);
			
			detailInforButton.setOnMouseClicked(e->{
				ev.show(this.getRootContainerPane().getScene().getWindow());//
			});
			
			this.conditionEvaluatorGridPane.add(indexTextField, 0, index+1);
			this.conditionEvaluatorGridPane.add(basicInforTextField, 1, index+1);
			this.conditionEvaluatorGridPane.add(detailInforButton, 2, index+1);
			
			////add the go to next circle as the last column
			Circle nextCircle = new Circle();
			nextCircle.setRadius(20);
			nextCircle.setFill(Color.RED);
			
			this.conditionEvaluatorGridPane.add(nextCircle, 3, index+1);
			
			this.conditionPrecedenceIndexGoToNextCircleMap.put(index, nextCircle);
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
				this.gotoPreviousCircle);
	}
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public PiecewiseFunctionViewer getViewer() {
		return (PiecewiseFunctionViewer)this.viewer;
	}
	
	
	/**
	 * return the coordinate of the circle that directs to the given next ComponentFunction;
	 * note that the next function of a PiecewiseFunction include both default next one and all condition-based next ones;
	 * 
	 * also note that this method can only be invoked after all involved ComponentFunctions' viewer UI are laid out on the component function tree AnchorPane of the owner CompositionFunction's viewer;
	 * 
	 * @return
	 */
	public Point2D getGotoNextCircleCenterCoord(ComponentFunction next) {
		if(this.getViewer().getValue().getDefaultNextFunction().equals(next)) {//given next function is the default next one
			return LayoutCoordinateAndSizeUtils.getLayoutPoint2D(
					this.getViewer().getHostCompositionFunctionViewer().getController().getComponentFunctionTreeAnchorPane(), 
					this.gotoDefaultNextCircle);
		}else {
			int conditionPrecedencIndex = 0;//
			for(int p:this.getViewer().getValue().getConditionPrecedenceIndexNextFunctionMap().keySet()) {
				if(this.getViewer().getValue().getConditionPrecedenceIndexNextFunctionMap().get(p).equals(next)) {
					conditionPrecedencIndex = p;
					break;
				}
			}
			
			return LayoutCoordinateAndSizeUtils.getLayoutPoint2D(
					this.getViewer().getHostCompositionFunctionViewer().getController().getComponentFunctionTreeAnchorPane(), 
					this.conditionPrecedenceIndexGoToNextCircleMap.get(conditionPrecedencIndex));
		}
		
	}
	
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@FXML
	private HBox rootContainerHBox;
	@FXML
	private Circle gotoPreviousCircle;
	@FXML
	private VBox contentVBox;
	@FXML
	private Circle gotoDefaultNextCircle;
	@FXML
	private GridPane conditionEvaluatorGridPane;

}
