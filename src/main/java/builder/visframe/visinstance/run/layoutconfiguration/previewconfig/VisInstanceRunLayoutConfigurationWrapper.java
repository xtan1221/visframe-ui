package builder.visframe.visinstance.run.layoutconfiguration.previewconfig;

import java.util.List;


import function.group.CompositionFunctionGroupID;
import javafx.geometry.Point2D;

/**
 * contains a temporary valid configuration for a VisInstanceRunLayoutConfiguration;
 * 
 * @author tanxu
 */
public class VisInstanceRunLayoutConfigurationWrapper {
	
	//////////////
	private final boolean fullRegionSelected;
	private final double x1;
	private final double y1;
	private final double x2;
	private final double y2;
	
	private final List<CompositionFunctionGroupID> selectedCoreShapeCFGIDListInLayoutOrder;
	
	/**
	 * 
	 * @param fullRegionSelected
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param selectedCoreShapeCFGIDListInLayoutOrder
	 */
	public VisInstanceRunLayoutConfigurationWrapper(
			boolean fullRegionSelected,
			double x1,
			double y1,
			double x2,
			double y2,
			List<CompositionFunctionGroupID> selectedCoreShapeCFGIDListInLayoutOrder
			){
		if(selectedCoreShapeCFGIDListInLayoutOrder==null || selectedCoreShapeCFGIDListInLayoutOrder.isEmpty())
			throw new IllegalArgumentException("given coreShapeCFGIDListInLayoutOrder cannot be null or empty!");
		
		
		this.fullRegionSelected = fullRegionSelected;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.selectedCoreShapeCFGIDListInLayoutOrder = selectedCoreShapeCFGIDListInLayoutOrder;
	}
	
	/**
	 * @return the fullRegion
	 */
	public boolean isFullRegionSelected() {
		return fullRegionSelected;
	}
	
	/**
	 * @return the x1
	 */
	public double getX1() {
		return x1;
	}

	/**
	 * @return the y1
	 */
	public double getY1() {
		return y1;
	}

	/**
	 * @return the x2
	 */
	public double getX2() {
		return x2;
	}

	/**
	 * @return the y2
	 */
	public double getY2() {
		return y2;
	}

	/**
	 * @return the selectedCoreShapeCFGIDListInLayoutOrder
	 */
	public List<CompositionFunctionGroupID> getSelectedCoreShapeCFGIDListInLayoutOrder() {
		return selectedCoreShapeCFGIDListInLayoutOrder;
	}
	


	//////////////////////////////////////
	public Point2D getLayoutRegionUpperLeftCoord() {
		return new Point2D(
				this.x1<this.x2?this.x1:this.x2,
				this.y1<this.y2?this.y1:this.y2);
	}
	
	public Point2D getLayoutRegionBottomRightCoord() {
		return new Point2D(
				this.x1<this.x2?this.x2:this.x1,
				this.y1<this.y2?this.y2:this.y1);
	}

}
