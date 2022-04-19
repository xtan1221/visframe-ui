package dependencygraph.vcd.pre;

import java.util.Comparator;

import context.VisframeContext;
import dependency.vcd.VCDEdgeImpl;
import dependency.vcd.VCDGraph;
import dependency.vcd.VCDNodeImpl;
import dependencygraph.DAGMainManager;

class VCDGraphMainManager extends DAGMainManager<VCDNodeImpl, VCDEdgeImpl>{
	static final Comparator<VCDNodeImpl> nodeOrderComparator =  //order by the precedence index
			(n1,n2)->{
				//TODO
				return n1.getPrecedenceIndex()-n2.getPrecedenceIndex();
			};
	
	static double distBetweenLevels = 400;
	static double distBetweenNodesOnSameLevel = 400;
	static double nodeRootNodeHeight = 30;
	static double nodeRootNodeWidth = 30;
	
	
	private final VisframeContext visframeContext;
	public VCDGraphMainManager(
			VCDGraph VCDGraph,
			
			VisframeContext visframeContext
//			double distBetweenLevels,
//			double distBetweenNodesOnSameLevel, 
//			double nodeRootNodeHeight, 
//			double nodeRootNodeWidth
			) {
		super(VCDGraph.getUnderlyingGraph(), nodeOrderComparator, new VCDNodeManagerFactory(), new VCDEdgeManagerFactory(), 
				distBetweenLevels, distBetweenNodesOnSameLevel,
				nodeRootNodeHeight, nodeRootNodeWidth);
		// TODO Auto-generated constructor stub
		
		this.visframeContext = visframeContext;
	}
	public VisframeContext getVisframeContext() {
		return visframeContext;
	}
	
	
	
}
