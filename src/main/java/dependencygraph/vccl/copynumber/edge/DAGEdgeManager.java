package dependencygraph.vccl.copynumber.edge;

import dependencygraph.vccl.copynumber.DAGNodeCopyNumberAssignmentManager;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

/**
 * 
 * @author tanxu
 *
 * @param <E>
 */
public class DAGEdgeManager<V,E> {
	protected final DAGNodeCopyNumberAssignmentManager<V,E> hostDAGNodeCopyNumberAssignmentManager;
	protected final E edge;
	
	private final Point2D sourceNodeCenterCoord;
	private final Point2D sinkNodeCenterCoord;
	//////////////////////
	private CubicCurve curve;
	
	public DAGEdgeManager(
			DAGNodeCopyNumberAssignmentManager<V,E> hostDAGNodeCopyNumberAssignmentManager, 
			E edge, Point2D sourceNodeCenterCoord, Point2D sinkNodeCenterCoord){
		this.hostDAGNodeCopyNumberAssignmentManager = hostDAGNodeCopyNumberAssignmentManager;
		this.edge = edge;
		this.sourceNodeCenterCoord = sourceNodeCenterCoord;
		this.sinkNodeCenterCoord = sinkNodeCenterCoord;
		
		this.calculateAndSetLayout();
		
		this.setSpecificUIFeatures();
	}
	
	
//	public abstract C getController();
	
	
	/**
	 * calculate the {@link #curve}
	 */
	protected void calculateAndSetLayout() {
		this.curve = new CubicCurve();
		
		this.curve.setFill(null);
		this.curve.setStroke(Color.BLACK);
		this.curve.setStrokeWidth(3);
		
		
		this.curve.setStartX(sourceNodeCenterCoord.getX());
		this.curve.setStartY(sourceNodeCenterCoord.getY());
		
		this.curve.setEndX(sinkNodeCenterCoord.getX());
		this.curve.setEndY(sinkNodeCenterCoord.getY());
		
		this.curve.setControlX1(sourceNodeCenterCoord.getX());
		this.curve.setControlY1(sourceNodeCenterCoord.getY()-this.hostDAGNodeCopyNumberAssignmentManager.getDistBetweenLevels()/2);
		
		this.curve.setControlX2(sinkNodeCenterCoord.getX());
		this.curve.setControlY2(sinkNodeCenterCoord.getY()+this.hostDAGNodeCopyNumberAssignmentManager.getDistBetweenLevels()/2);
	}

	
	protected void setSpecificUIFeatures() {
		//TODO
	}
	
	
	/**
	 * 
	 * @return
	 */
	public CubicCurve getCurve() {
		return curve;
	}
}
