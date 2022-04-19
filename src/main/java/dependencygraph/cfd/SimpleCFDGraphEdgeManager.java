package dependencygraph.cfd;

import dependency.cfd.CFDEdgeImpl;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

/**
 * 
 * @author tanxu
 *
 * @param <E>
 */
public class SimpleCFDGraphEdgeManager {
	protected final SimpleCFDGraphViewerManager hostSimpleCFDGraphViewerManager;
	protected final CFDEdgeImpl edge;
	
	private final Point2D dependingNodeUpperCenterCoord;
	private final Point2D dependedNodeBottomCenterCoord;
	//////////////////////
	private CubicCurve curve;
	
	
	/**
	 * 
	 * @param hostCopyLinkAssingerManager
	 * @param edge
	 * @param dependingNodeUpperCenterCoord
	 * @param dependedNodeBottomCenterCoord
	 */
	public SimpleCFDGraphEdgeManager(
			SimpleCFDGraphViewerManager hostSimpleCFDGraphViewerManager, 
			CFDEdgeImpl edge, 
			Point2D dependingNodeUpperCenterCoord, 
			Point2D dependedNodeBottomCenterCoord){
		
		
		this.hostSimpleCFDGraphViewerManager = hostSimpleCFDGraphViewerManager;
		this.edge = edge;
		this.dependingNodeUpperCenterCoord = dependingNodeUpperCenterCoord;
		this.dependedNodeBottomCenterCoord = dependedNodeBottomCenterCoord;
		
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
		
		this.curve.setStartX(dependingNodeUpperCenterCoord.getX());
		this.curve.setStartY(dependingNodeUpperCenterCoord.getY());
		
		this.curve.setEndX(dependedNodeBottomCenterCoord.getX());
		this.curve.setEndY(dependedNodeBottomCenterCoord.getY());
		
		this.curve.setControlX1(dependingNodeUpperCenterCoord.getX());
		this.curve.setControlY1(dependingNodeUpperCenterCoord.getY()-this.hostSimpleCFDGraphViewerManager.getDistBetweenLevels()/2);
		
		this.curve.setControlX2(dependedNodeBottomCenterCoord.getX());
		this.curve.setControlY2(dependedNodeBottomCenterCoord.getY()+this.hostSimpleCFDGraphViewerManager.getDistBetweenLevels()/2);
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
