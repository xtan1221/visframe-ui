package builder.visframe.context.scheme;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import context.project.VisProjectDBContext;
import context.scheme.VSComponent;
import context.scheme.VSComponentPrecedenceList;
import context.scheme.VisScheme;
import context.scheme.VisSchemeBuilderImpl;
import core.builder.LeafNodeBuilder;
import dependency.vcd.VCDGraph;
import dependency.vcd.VCDGraphBuilder;
import dependencygraph.vcd.VCDGraphViewerManager;
import dependencygraph.vcd.VCDNodeOrderingComparatorFactory;
import function.group.CompositionFunctionGroupID;

/**
 * builder class for a VisScheme;
 * 
 * note that this class will directly build an instance of {@link VisSchemeBuilderImpl}, 
 * then invoke {@link VisSchemeBuilderImpl#build()} method to build the target {@link VisScheme};
 * 
 * @author tanxu
 * 
 */
public class VisSchemeBuilder extends LeafNodeBuilder<VisScheme, VisSchemeBuilderEmbeddedUIContentController>{
	public static final String NODE_NAME = "CompositionFunction";
	public static final String NODE_DESCRIPTION = "CompositionFunction";
	
	
	////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	
	private final Set<CompositionFunctionGroupID> shapeCFGIDSetWithAllMandatoryTargetAssigned;
	
	
	////////////////////////////
	private List<VSComponentBuilder> componentBuilderList;
	
	/**
	 * currently selected set of ShapeCFG ID set of all VSComponentBuilder in the {@link #componentBuilderList};
	 */
	private Set<CompositionFunctionGroupID> selectedShapeCFGIDSet;
	
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @throws SQLException 
	 */
	public VisSchemeBuilder(VisProjectDBContext hostVisProjectDBContext) throws SQLException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, VisSchemeBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("given hostVisProjectDBContext cannot be null!");
		
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		
		this.componentBuilderList = new ArrayList<>();
		this.selectedShapeCFGIDSet = new HashSet<>();
		
		//
		this.shapeCFGIDSetWithAllMandatoryTargetAssigned = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().getShapeCFGIDSetWithAllMandatoryTargetsAssignedToCF();

	}
	
	
	/**
	 * @return the selectedShapeCFGIDSet
	 */
	public Set<CompositionFunctionGroupID> getSelectedShapeCFGIDSet() {
		return selectedShapeCFGIDSet;
	}


	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	

	/**
	 * @return the shapeCFGIDSetWithAllMandatoryTargetAssigned
	 */
	public Set<CompositionFunctionGroupID> getShapeCFGIDSetWithAllMandatoryTargetAssigned() {
		return shapeCFGIDSetWithAllMandatoryTargetAssigned;
	}

	public List<VSComponentBuilder> getComponentBuilderList() {
		return componentBuilderList;
	}
	
	
	///////////////////////////////
	/**
	 * add a new VSComponentBuilder;
	 * 
	 * 1. add to the {@link #componentBuilderList}
	 * 
	 * 2. set the index of the new VSComponentBuilder
	 * 
	 * 3. add the new VSComponentBuilder to the UI
	 * 
	 */
	public void addVSComponentBuilder(VSComponentBuilder builder) {
		
		this.componentBuilderList.add(builder);
		
		builder.setPrecedenceIndex(this.componentBuilderList.size()-1);//
		
		if(!this.getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().contains(builder.getEmbeddedUIContentController().getRootParentNode()))
			this.getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().add(builder.getEmbeddedUIContentController().getRootParentNode());
		
		this.updateNonNullValueFromContentController(false);
	}
	
	/**
	 * 1. delete the VSComponentBuilder at the given index from the UI;
	 * 2. delete all the selected core ShapeCFGs of the given VSComponentBuilder from the {@link #selectedShapeCFGIDSet};
	 * 3. delete the given VSComponentBuilder from the {@link #componentBuilderList};
	 * 
	 * 4. explicitly reset the index of each {@link VSComponentBuilder#getPrecedenceIndex()} with list index equal to and larger than the given one;
	 * 
	 * 5. update VCD graph
	 * @param index
	 * @throws SQLException 
	 */
	public void deleteVSComponentBuilder(int index) throws SQLException {
		//1
		this.getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().remove(index);
		//2
		this.selectedShapeCFGIDSet.removeAll(this.componentBuilderList.get(index).getSelectedCoreShapeCFGIDEntryManagerMap().keySet());
		//3
		this.componentBuilderList.remove(index);
		//4
		for(int i=index;i<this.componentBuilderList.size();i++) {
			this.componentBuilderList.get(i).setPrecedenceIndex(i);
		}
		
		
		this.updateNonNullValueFromContentController(false);
		
		//5
		this.updateVCDGraphLayoutAndInvolvedVisframeEntities();
	}
	
	/**
	 * clear all VSComponent
	 * 
	 * update VCD graph
	 * @throws SQLException 
	 */
	public void deleteAllVSComponentBuilder() throws SQLException {
		this.componentBuilderList.clear();
		this.selectedShapeCFGIDSet.clear();
		
		this.getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().clear();
		
		this.updateNonNullValueFromContentController(false);
		
		
		this.updateVCDGraphLayoutAndInvolvedVisframeEntities();
	}
	
	
	/**
	 * change the VSComponentBuilders position at the given two indices;
	 * 
	 * 1. update the index of the two swapped VSComponentBuilders
	 * 
	 * 2. swap the two elements in {@link #componentBuilderList};
	 * 
	 * 3. swap the two elements in the UI;
	 * 
	 * 4. update VCD graph
	 * @param index1
	 * @param index2
	 * @throws SQLException 
	 */
	public void exchangeComponentPosition(int index1, int index2) throws SQLException {
		//1
		this.componentBuilderList.get(index1).setPrecedenceIndex(index2);
		
		this.componentBuilderList.get(index2).setPrecedenceIndex(index1);
		//2
		Collections.swap(this.componentBuilderList, index1, index2);
		
		//3
//		Collections.swap(this.getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren(), index1, index2);
		
		this.getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().clear();
		this.getComponentBuilderList().forEach(b->{
			this.getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().add(b.getEmbeddedUIContentController().getRootParentNode());
		});
		
		this.updateNonNullValueFromContentController(false);
		//4
		this.updateVCDGraphLayoutAndInvolvedVisframeEntities();
	}
	
	
	/**
	 * update the VCD graph layout based on current set of VSComponentBuilders and their selected core ShapeCFG set;
	 * note that if a VSComponentBuilder has no core ShapeCFG selected, it will be ignored when building the VCD graph;
	 * 
	 * also update the included set of visframe entities of each type in the visscheme;
	 * 
	 * this method should be invoked whenever a changed is made that would impact the VCD graph;
	 * @throws SQLException 
	 */
	public void updateVCDGraphLayoutAndInvolvedVisframeEntities() throws SQLException {
		//first clear any existing layout
		this.getEmbeddedUIContentController().getVCDGraphLayoutAnchorPaneContainerHBox().getChildren().clear();
		//also clear 
		this.getEmbeddedUIContentController().getMetadataListView().getItems().clear();
		this.getEmbeddedUIContentController().getOperationListView().getItems().clear();
		this.getEmbeddedUIContentController().getCompositionFunctionGroupListView().getItems().clear();
		this.getEmbeddedUIContentController().getCompositionFunctionListView().getItems().clear();
		
		//first build the vcd graph based on current list of VSComponents
		//note that empty vscompnents will be ignored
		List<VSComponent> componentList = new ArrayList<>();
		
		this.componentBuilderList.forEach(builder->{
			try{
				VSComponent c = builder.getEmbeddedUIContentController().build();
				componentList.add(c);
			}catch(Exception e) {
				//empty core shape cfg set, ignore
			}
		});
		
		//clear the graph layout if there is no valid vscomponent;
		if(componentList.isEmpty()) {
			return;
		}
		
		
		//
		VSComponentPrecedenceList precedenceList = new VSComponentPrecedenceList(componentList);
		
		
		VCDGraphBuilder builder = new VCDGraphBuilder(this.getHostVisProjectDBContext(), precedenceList);
		
		VCDGraph vcdGraph = builder.getVCDGraph();
		
		//create layout
//		VCDGraphMainManager vcdGraphLayoutManager = new VCDGraphMainManager(vcdGraph, this.hostVisProjectDBContext);
		
		VCDGraphViewerManager vcdGraphViewerManager = new VCDGraphViewerManager(
				vcdGraph.getUnderlyingGraph(),//SimpleDirectedGraph<VCDNodeImpl, VCDEdgeImpl> underlyingVCDGraph,
				VCDNodeOrderingComparatorFactory.getComparator(),//Comparator<VCDNodeImpl> nodeOrderComparator,
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(a)->{return Integer.toString(a.getPrecedenceIndex());}//Function<VCDNodeImpl,String> dagNodeInforStringFunction
				);
		
		
		//add the vcd graph layout to the UI
		this.getEmbeddedUIContentController().getVCDGraphLayoutAnchorPaneContainerHBox().getChildren().add(
				vcdGraphViewerManager.getController().getRootNodeContainer());
		
		
		//add the included visframe entities
		this.getEmbeddedUIContentController().getMetadataListView().getItems().addAll(vcdGraph.getMetadataIDAssignedVCDNodeMap().keySet());
		this.getEmbeddedUIContentController().getOperationListView().getItems().addAll(vcdGraph.getOperationIDAssignedVCDNodeMap().keySet());
		this.getEmbeddedUIContentController().getCompositionFunctionGroupListView().getItems().addAll(vcdGraph.getCFGIDAssignedVCDNodeMap().keySet());
		this.getEmbeddedUIContentController().getCompositionFunctionListView().getItems().addAll(vcdGraph.getCFIDAssignedVCDNodeMap().keySet());
		
	}
}
