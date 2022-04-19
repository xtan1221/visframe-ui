package dependencygraph.vcd.pre;

import dependency.vcd.VCDEdgeImpl;
import dependencygraph.DAGEdgeManager;
import dependencygraph.DAGEdgeManagerFactory;
import dependencygraph.DAGMainManager;
import javafx.geometry.Point2D;

public class VCDEdgeManagerFactory extends DAGEdgeManagerFactory<VCDEdgeImpl>{

	@Override
	public DAGEdgeManager<VCDEdgeImpl> build(DAGMainManager<?, VCDEdgeImpl> mainManager, VCDEdgeImpl edge,
			Point2D sourceNodeCenterCoord, Point2D sinkNodeCenterCoord) {
		return new VCDEdgeManager((VCDGraphMainManager) mainManager, edge, sourceNodeCenterCoord, sinkNodeCenterCoord);
	}
	
}
