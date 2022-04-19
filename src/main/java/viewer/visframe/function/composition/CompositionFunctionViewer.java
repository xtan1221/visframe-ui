package viewer.visframe.function.composition;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.VisframeContext;
import function.composition.CompositionFunction;
import function.group.CompositionFunctionGroup;
import viewer.AbstractViewer;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.component.ComponentFunctionViewerFactory;

public class CompositionFunctionViewer extends AbstractViewer<CompositionFunction, CompositionFunctionViewerController>{
	
	public static final double H_GAP = 50;
	public static final double V_GAP = 200;
	public static final double H_BOUND = 400;
	public static final double V_BOUND = 200;
	///////////////////////////////////////
	public static int LEAF_COUNTER;
	
	//////////////////
	private final VisframeContext hostVisframeContext;
	
	///////////////////////////////
	private ComponentFunctionViewerBase<?,?> rootComponentFunctionViewer;
	
	/**
	 * 
	 */
	private Map<Integer, ComponentFunctionViewerBase<?,?>> componentFunctionIndexViewerMap;
	
	/**
	 * map from the edge number to root to the value of the largest value among all the ComponentFunctionViewer's root pane's width;
	 */
	private Map<Integer, Double> edgeNumToRootLargestNodeWidthMap;
	
	/**
	 * for map key = n, its value is calculated by adding the corresponding map value of {@link #edgeNumToRootLargestNodeWidthMap} with key = n and the map value of this map with key = n-1;
	 * for map key = 0, its value is the same with map value of {@link #edgeNumToRootLargestNodeWidthMap} with key = 0;
	 * this map can only be calculated after all ComponentFunctionViewer's root pane's real width are calculated and {@link #edgeNumToRootLargestNodeWidthMap} is fully calculated.
	 * 
	 */
	private Map<Integer, Double> edgeNumToRootCumulativeNodeWidthMap;
	

	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public CompositionFunctionViewer(CompositionFunction value, VisframeContext hostVisframeContext) {
		super(value, CompositionFunctionViewerController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	
		this.hostVisframeContext = hostVisframeContext;
		
		
		///////////////////////////
		this.componentFunctionIndexViewerMap = new HashMap<>();
		this.edgeNumToRootLargestNodeWidthMap = new HashMap<>();
		this.edgeNumToRootCumulativeNodeWidthMap = new HashMap<>();
		
		////////////////////
		//
		this.rootComponentFunctionViewer = ComponentFunctionViewerFactory.build(this.getValue().getRootFunction(), this);
		
		
		this.calculateLeafIndex();
		
		this.rootComponentFunctionViewer.setEdgeNumToRoot(0);
		
		this.rootComponentFunctionViewer.calculateRealSize();
		
		this.calculateEdgeNumToRootCumulativeNodeWidthMap();
		
		this.rootComponentFunctionViewer.calculateCenterCoordinate();
		
		
		
	}
	
	
	/**
	 * 
	 */
	private void calculateLeafIndex() {
		LEAF_COUNTER = 0;
		
		this.rootComponentFunctionViewer.calculateLeafIndex();
	}
	
	
	/**
	 * calculate {@link #edgeNumToRootCumulativeNodeWidthMap} with the {@link #edgeNumToRootLargestNodeWidthMap}
	 */
	private void calculateEdgeNumToRootCumulativeNodeWidthMap() {
		int largestEdgeNumToRoot = this.edgeNumToRootLargestNodeWidthMap.size()-1;
		
		this.edgeNumToRootCumulativeNodeWidthMap.put(0, this.edgeNumToRootLargestNodeWidthMap.get(0));
		
		if(largestEdgeNumToRoot>=1) {
			for(int i = 1;i<=largestEdgeNumToRoot;i++) {
				this.edgeNumToRootCumulativeNodeWidthMap.put(i, this.edgeNumToRootCumulativeNodeWidthMap.get(i-1)+this.edgeNumToRootLargestNodeWidthMap.get(i));
			}
		}
	}
	
	
	void buildLinkingCurves() {
		this.rootComponentFunctionViewer.buildLinkingCurveToPreviousComponentFunction();
	}
	
	///////////////////////////////////////
	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}


	/**
	 * @return the componentFunctionIndexViewerMap
	 */
	public Map<Integer, ComponentFunctionViewerBase<?,?>> getComponentFunctionIndexViewerMap() {
		return componentFunctionIndexViewerMap;
	}
	
	/**
	 * 
	 * @return
	 */
	public CompositionFunctionGroup getHostCompositionFunctionGroup() {
		try {
			return this.getHostVisframeContext().getCompositionFunctionGroupLookup().lookup(this.getValue().getHostCompositionFunctionGroupID());
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}


	/**
	 * @return the edgeNumToRootLargestNodeWidthMap
	 */
	public Map<Integer, Double> getEdgeNumToRootLargestNodeWidthMap() {
		return edgeNumToRootLargestNodeWidthMap;
	}
	
	
	public void setEdgeNumToRootLargestNodeWidthMap(int edgeNum2Root, double width) {
		this.edgeNumToRootLargestNodeWidthMap.put(edgeNum2Root, width);
	}
	

	/**
	 * @return the edgeNumToRootCumulativeNodeWidthMap
	 */
	public Map<Integer, Double> getEdgeNumToRootCumulativeNodeWidthMap() {
		return edgeNumToRootCumulativeNodeWidthMap;
	}

}
