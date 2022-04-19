package dependencygraph.vcd.pre;

import dependency.vcd.VCDEdgeImpl;
import dependencygraph.DAGEdgeManager;
import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;

public class VCDEdgeManager extends DAGEdgeManager<VCDEdgeImpl> {
	
	protected VCDEdgeManager(
			VCDGraphMainManager mainManager, VCDEdgeImpl edge,
			Point2D sourceNodeCenterCoord, Point2D sinkNodeCenterCoord) {
		super(mainManager, edge, sourceNodeCenterCoord, sinkNodeCenterCoord);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void setSpecificUIFeatures() {
		Tooltip tooltip = new Tooltip(this.edge.toString());
		
		Tooltip.install(this.getCurve(), tooltip);
	}
	
}
