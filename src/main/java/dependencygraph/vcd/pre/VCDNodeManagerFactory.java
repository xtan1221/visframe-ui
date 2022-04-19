package dependencygraph.vcd.pre;

import dependency.vcd.VCDNodeImpl;
import dependencygraph.DAGMainManager;
import dependencygraph.DAGNodeManagerFactory;
import javafx.geometry.Point2D;

public class VCDNodeManagerFactory extends DAGNodeManagerFactory<VCDNodeImpl, VCDNodeController>{
	
	@Override
	public VCDNodeManager build(DAGMainManager<VCDNodeImpl, ?> mainManager, VCDNodeImpl node, Point2D centerCoord, double nodeRootNodeHeight, double nodeRootNodeWidth) {
		return new VCDNodeManager((VCDGraphMainManager) mainManager, node, centerCoord, nodeRootNodeHeight, nodeRootNodeWidth);
	}
}
