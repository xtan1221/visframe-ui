package viewer.visframe.function.component;

import java.util.HashMap;
import java.util.Map;

import function.component.ComponentFunction;
import function.component.SimpleFunction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import utils.LayoutCoordinateAndSizeUtils;
import utils.Pair;
import viewer.AbstractViewer;
import viewer.visframe.function.composition.CompositionFunctionViewer;
import viewer.visframe.function.evaluator.EvaluatorViewer;

/**
 * 
 * @author tanxu
 *
 * @param <F>
 * @param <C>
 */
public abstract class ComponentFunctionViewerBase<F extends ComponentFunction, C extends ComponentFunctionViewerControllerBase<F>> extends AbstractViewer<F, C>{
	
	private final CompositionFunctionViewer hostCompositionFunctionViewer;
	
	////////////////////////////////
	/**
	 * map from index of Evaluator of the ComponentFunction of this ComponentFunctionViewerBase to the corresponding EvaluatorViewer;
	 */
	private Map<Integer, EvaluatorViewer<?,?>> evaluatorIndexViewerMap;
	
	/**
	 * the real UI height
	 */
	protected double height;
	/**
	 * the real UI width
	 */
	protected double width;
	
	/**
	 * 
	 */
	protected int edgeNumToRoot;
	
	/**
	 * 
	 */
	protected double leafIndex;
	
	/**
	 * 
	 */
	private double centerX;
	
	private double centerY;
	
	
	private double layoutX;
	private double layoutY;
	
	/**
	 * 
	 */
	private CubicCurve curveToPrevious;
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected ComponentFunctionViewerBase(F value, String FXMLFileDirString, CompositionFunctionViewer hostCompositionFunctionViewer) {
		super(value, FXMLFileDirString);
		// TODO Auto-generated constructor stub
		
		this.hostCompositionFunctionViewer = hostCompositionFunctionViewer;
		
		
		///////////////////
		this.evaluatorIndexViewerMap = new HashMap<>();
		
		this.buildDownstreamComponentFunctionViewers();
		//
		this.hostCompositionFunctionViewer.getComponentFunctionIndexViewerMap().put(this.getValue().getIndexID(), this);
	}
	
	
	/**
	 * 
	 */
	protected abstract void buildDownstreamComponentFunctionViewers();
	
	/**
	 * 
	 */
	public abstract void calculateLeafIndex();
	
	/**
	 * 
	 * @param num
	 */
	public abstract void setEdgeNumToRoot(int num);
	
	/**
	 * calculate the center coordinate of this ComponentFunctionViewerBase;
	 * then invoke the same method of any incident downstream ComponentFunctionViewerBase;
	 */
	public void calculateCenterCoordinate() {
		double w = this.edgeNumToRoot==0?0:this.hostCompositionFunctionViewer.getEdgeNumToRootCumulativeNodeWidthMap().get(this.edgeNumToRoot-1);
		this.centerX = CompositionFunctionViewer.H_GAP * this.edgeNumToRoot + CompositionFunctionViewer.H_BOUND + w;
		this.centerY = CompositionFunctionViewer.V_GAP * this.leafIndex + CompositionFunctionViewer.V_BOUND;
	}
	
	/**
	 * 
	 */
	public void calculateRealSize() {
		Pair<Double, Double> realSize = LayoutCoordinateAndSizeUtils.findOutRealWidthAndHeight(this.getController().getRootContainerPane());
		
		this.width = realSize.getFirst();
		this.height = realSize.getSecond();
		
		//update the edgeNumToRootLargestNodeWidthMap of host CompositionFunctionViewer
		if(this.getHostCompositionFunctionViewer().getEdgeNumToRootLargestNodeWidthMap().containsKey(this.edgeNumToRoot) && 
				this.getHostCompositionFunctionViewer().getEdgeNumToRootLargestNodeWidthMap().get(this.edgeNumToRoot)>=this.width) {
			//do nothing
		}else {
			this.getHostCompositionFunctionViewer().setEdgeNumToRootLargestNodeWidthMap(this.edgeNumToRoot, width);
		}
	}
	
	
	/**
	 * 
	 */
	public void calculateAndSetLayoutCoordinate() {
		this.layoutX = this.centerX - this.width/2;
		this.layoutY = this.centerY - this.height/2;
		
		this.getController().getRootContainerPane().setLayoutX(this.layoutX);
		this.getController().getRootContainerPane().setLayoutY(this.layoutY);
	}
	
	
	/**
	 * initialize and build the {@link #curveToPrevious};
	 * also invoke the same method of all incident downstream ComponentFunctionViewers;
	 */
	public void buildLinkingCurveToPreviousComponentFunction() {
		if(this.getHostCompositionFunctionViewer().getValue().getPreviousComponentFunction(this.getValue())==null) {
			//the contained ComponentFunction is the root; do nothing
		}else {//
			ComponentFunction previousComponentFunction = this.getHostCompositionFunctionViewer().getValue().getPreviousComponentFunction(this.getValue());
			ComponentFunctionViewerBase<?,?> previousViewer = this.getHostCompositionFunctionViewer().getComponentFunctionIndexViewerMap().get(previousComponentFunction.getIndexID());
			
			double thisCircleGotoPreviousCenterX = this.getController().getGotoPreviousCircleCenterCoord().getX();
			double thisCircleGotoPreviousCenterY = this.getController().getGotoPreviousCircleCenterCoord().getY();
			
			double previousGotoThisCircleCenterX;
			double previousGotoThisCircleCenterY;
			
			if(previousComponentFunction instanceof SimpleFunction) {
				SimpleFunctionViewer psfv = (SimpleFunctionViewer)previousViewer;
				
				Point2D coord = psfv.getController().getGotoNextCircleCenterCoord();
				previousGotoThisCircleCenterX = coord.getX();
				previousGotoThisCircleCenterY = coord.getY();
			}else {
				PiecewiseFunctionViewer pfsfv = (PiecewiseFunctionViewer)previousViewer;
				
				Point2D coord = pfsfv.getController().getGotoNextCircleCenterCoord(this.getValue());
				previousGotoThisCircleCenterX = coord.getX();
				previousGotoThisCircleCenterY = coord.getY();
			}
			
			
			this.curveToPrevious = new CubicCurve();
			this.curveToPrevious.setStartX(previousGotoThisCircleCenterX);
			this.curveToPrevious.setStartY(previousGotoThisCircleCenterY);
			this.curveToPrevious.setEndX(thisCircleGotoPreviousCenterX);
			this.curveToPrevious.setEndY(thisCircleGotoPreviousCenterY);
			
			this.curveToPrevious.setControlX1(previousGotoThisCircleCenterX+50);
			this.curveToPrevious.setControlY1(previousGotoThisCircleCenterY);
			this.curveToPrevious.setControlX2(thisCircleGotoPreviousCenterX-50);
			this.curveToPrevious.setControlY2(thisCircleGotoPreviousCenterY);
			
			this.curveToPrevious.setFill(null);
			this.curveToPrevious.setStroke(Color.BLACK);
		}
	}
	
	
	
	/////////////////////////////////
	/**
	 * @return the hostCompositionFunctionViewer
	 */
	public CompositionFunctionViewer getHostCompositionFunctionViewer() {
		return hostCompositionFunctionViewer;
	}

	
	/**
	 * @return the layoutX
	 */
	public double getLayoutX() {
		return layoutX;
	}


	/**
	 * @return the layoutY
	 */
	public double getLayoutY() {
		return layoutY;
	}
	

	/**
	 * @return the curveToPrevious
	 */
	public CubicCurve getCurveToPrevious() {
		return curveToPrevious;
	}


	/**
	 * @return the evaluatorIndexViewerMap
	 */
	public Map<Integer, EvaluatorViewer<?,?>> getEvaluatorIndexViewerMap() {
		return evaluatorIndexViewerMap;
	}
	
	/**
	 * 
	 * @param index
	 * @param viewer
	 */
	public void addEvaluatorViewer(int index, EvaluatorViewer<?,?> viewer) {
		this.evaluatorIndexViewerMap.put(index, viewer);
	}
	
}
