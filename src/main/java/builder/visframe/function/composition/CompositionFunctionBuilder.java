package builder.visframe.function.composition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import basic.SimpleName;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder.ComponentFunctionType;
import builder.visframe.function.composition.originalIndieFIVTypeUtils.OriginalIndieFIVTypeEntryBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.input.nonrecordwise.FreeInputVariableBuilder;
import builder.visframe.function.variable.input.nonrecordwise.SQLAggregateFunctionBasedInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.CFGTargetInputVariableBuilder;
import context.project.VisProjectDBContext;
import core.builder.LeafNodeBuilder;
import function.composition.CompositionFunction;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroup;
import function.target.CFGTarget;
import function.variable.independent.IndependentFreeInputVariableType;
import metadata.MetadataID;
import misc.SelectTypePopupBuilder;

/**
 * builder for a CompositionFunction with a pre-selected owner CompositionFunctionGroup and an assigned index id;
 * 
 * @author tanxu
 *
 */
public final class CompositionFunctionBuilder extends LeafNodeBuilder<CompositionFunction, CompositionFunctionBuilderEmbeddedUIContentController> {
	public static final String NODE_NAME = "CompositionFunction";
	public static final String NODE_DESCRIPTION = "CompositionFunction";
	
	/////////////////////////
	public static final double NODE_DEFAULT_WIDTH = 300;
	
	public static final double NODE_LAYOUT_Y_DIST = 200;
	public static final double NODE_LAYOUT_X_DIST = 300;
	
	////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	
	private final CompositionFunctionGroup ownerCompositionFunctionGroup;
	//
	private final Integer indexID;
	
	
	/**
	 * whether this CompositionFunctionBuilder is finishable;
	 * 
	 * only true when all constraints are obeyed;
	 */
	private boolean finishable;
	
	
	private SelectTypePopupBuilder<ComponentFunctionType> componentFunctionTypeSelectionPopup;
	
	
	//////////////////////////////
	/**
	 * set of targets of the host {@link CompositionFunctionGroup} that are assigned to other existing {@link CompositionFunction}s
	 */
	private Set<SimpleName> targetNameSetAssignedToOtherCompositionFunctions;
	
	
	private Map<SimpleName, CFGTarget<?>> targetNameMapUnassignedToCF;
	/**
	 * assigned CFGTarget set of the ownerCompositionFunctionGroup that has not been assigned to any existing CompositionFunction
	 */
	private Map<SimpleName, CFGTarget<?>> assignedTargetNameMap;
	
	
	/**
	 * 
	 */
	private ComponentFunctionBuilder<?,?> rootComponentFunctionBuilder;
	private Set<Integer> currentComponentFunctionIndexIDSet;
	
	//////////////////
	private Map<SimpleName, OriginalIndieFIVTypeEntryBuilder> originalIndependentFreeInputVariableTypeNameEntryBuilderMap;
	
	/**
	 * always contains the current set of created IndependentFreeInputVariableType for the target CompositionFunction of this builder;
	 * help to check if a IndependentFreeInputVariableType is assigned an alias name same with existing one;
	 * 
	 */
	private Map<SimpleName,IndependentFreeInputVariableType> originalIndependentFreeInputVariableTypeNameMap;
	
	/**
	 * map from the name of original IndependentFreeInputVariableType to the number of FreeInputVariableBuilder that uses it;
	 * only those IndependentFreeInputVariableTypes with 0 map value can be deleted;
	 */
	private Map<SimpleName,Integer> originalIndependentFIVTypeNameNumberOfEmplyoerFIVMap;
	
	/**
	 * the set of record data that is depended by the built CompositionFunction;
	 * not including the owner record data of the host CompositionFunctionGroupBuilder
	 * 
	 * specifically, 
	 * 
	 * 1. owner record data of CompositionFunctionGroup of selected target of {@link CFGTargetInputVariableBuilder}
	 * 
	 * 2. record data of {@link RecordAttributeInputVariableBuilder};
	 * 
	 * 2. target record of any {@link SQLAggregateFunctionBasedInputVariableBuilder};
	 */
	private Set<MetadataID> dependedRecordDataMetadataIDSet;

	/**
	 * the set of CompositionFunction that is depended by the built CompositionFunction;
	 * 
	 * 1. CompositionFunction to which the target of {@link CFGTargetInputVariableBuilder} is assigned
	 * 
	 * 2. owner CompositionFunction of IndependentFreeInputVariableType of {@link FreeInputVariableBuilder}
	 */
	private Set<CompositionFunctionID> dependedCompositionFunctionIDSet;
	
	
	////////////////////////////////
	public int leafIndexCounter;
	public double previousLeafNodeBottomY;
	
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param ownerCompositionFunctionGroup
	 * @param indexID
	 * @throws SQLException 
	 */
	public CompositionFunctionBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			
			CompositionFunctionGroup ownerCompositionFunctionGroup,
			
			int indexID
			) throws SQLException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, CompositionFunctionBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.ownerCompositionFunctionGroup = ownerCompositionFunctionGroup;
		this.indexID = indexID;
		
		/////////////
		this.targetNameSetAssignedToOtherCompositionFunctions = new LinkedHashSet<>();
		this.currentComponentFunctionIndexIDSet = new LinkedHashSet<>();
		this.assignedTargetNameMap = new LinkedHashMap<>();
		
		this.originalIndependentFreeInputVariableTypeNameEntryBuilderMap = new LinkedHashMap<>();
		this.originalIndependentFreeInputVariableTypeNameMap = new LinkedHashMap<>();
		this.originalIndependentFIVTypeNameNumberOfEmplyoerFIVMap = new LinkedHashMap<>();
		
		this.dependedRecordDataMetadataIDSet = new LinkedHashSet<>();
		this.dependedCompositionFunctionIDSet = new LinkedHashSet<>();
		
		
		this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionManager().getTargetNameAssignedCFIDMap(this.getOwnerCompositionFunctionGroup().getID()).forEach((k,v)->{
			if(v.equals(this.getCompositionFunctionID())) {
				//
			}else {
				this.targetNameSetAssignedToOtherCompositionFunctions.add(k);
			}
			
		});
	}
	
	
	
	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}

	/**
	 * @return the ownerCompositionFunctionGroup
	 */
	public CompositionFunctionGroup getOwnerCompositionFunctionGroup() {
		return ownerCompositionFunctionGroup;
	}
	
	//
	public Integer getIndexID() {
		return indexID;
	}
	
	
	public int getNextAvailableComponentFunctionIndexID() {
		int i = 0;
		while(this.currentComponentFunctionIndexIDSet.contains(i)) {
			i++;
		}
		this.currentComponentFunctionIndexIDSet.add(i);
		return i;
	}
	
	
	public void addOriginalIndieFIVTypeEntryBuilder(OriginalIndieFIVTypeEntryBuilder entryBuilder) throws SQLException, IOException {
		this.originalIndependentFreeInputVariableTypeNameEntryBuilderMap.put(
				entryBuilder.getIndependentFreeInputVariableTypeBuilder().getCurrentValue().getName(), 
				entryBuilder);
		
		this.update();
	}
	
	public void deleteOriginalIndieFIVTypeEntryBuilder(SimpleName typeName) throws SQLException, IOException {
		this.originalIndependentFreeInputVariableTypeNameEntryBuilderMap.remove(typeName);
		
		this.update();
	}
	
	public void deleteAllOriginalIndieFIVTypes() throws SQLException, IOException {
		this.originalIndependentFreeInputVariableTypeNameEntryBuilderMap.clear();
		this.update();
	}
	
	/**
	 * @return the originalIndependentFreeInputVariableTypeNameEntryBuilderMap
	 */
	public Map<SimpleName, OriginalIndieFIVTypeEntryBuilder> getOriginalIndependentFreeInputVariableTypeNameEntryBuilderMap() {
		return originalIndependentFreeInputVariableTypeNameEntryBuilderMap;
	}

	/**
	 * return the current set of {@link IndependentFreeInputVariableType}s that are owned by the {@link CompositionFunction} to be built by this builders;
	 * 
	 * @return
	 */
	public Map<SimpleName,IndependentFreeInputVariableType> getOriginalIndependentFreeInputVariableTypeNameMap() {
		return originalIndependentFreeInputVariableTypeNameMap;
	}
	
	public Map<SimpleName,Integer> getOriginalIndependentFIVTypeNameNumberOfEmplyoerFIVMap() {
		return originalIndependentFIVTypeNameNumberOfEmplyoerFIVMap;
	}
	
	/**
	 * @return the dependedRecordDataMetadataSet
	 */
	public Set<MetadataID> getDependedRecordDataMetadataIDSet() {
		return dependedRecordDataMetadataIDSet;
	}
	
	/**
	 * @return the dependedCompositionFunctionGroupSet
	 */
	public Set<CompositionFunctionID> getDependedCompositionFunctionIDSet() {
		return dependedCompositionFunctionIDSet;
	}

	/**
	 * @return the targetNameSetAssignedToOtherCompositionFunctions
	 */
	public Set<SimpleName> getTargetNameSetAssignedToOtherCompositionFunctions() {
		return targetNameSetAssignedToOtherCompositionFunctions;
	}



	public Map<SimpleName, CFGTarget<?>> getAssignedTargetNameMap() {
		return assignedTargetNameMap;
	}
	
	public void addAssignedTarget(CFGTarget<?> target) {
		this.assignedTargetNameMap.put(target.getName(), target);
	}
	
	public void removeAssignedTarget(SimpleName targetName) {
		this.assignedTargetNameMap.remove(targetName);
	}
	
	
	
	public ComponentFunctionBuilder<?,?> getRootComponentFunctionBuilder() {
		return rootComponentFunctionBuilder;
	}
	
	
	public void setRootComponentFunctionBuilder(ComponentFunctionBuilder<?,?> rootComponentFunctionBuilder) throws SQLException, IOException {
		this.getEmbeddedUIContentController().removeComponentFunctionBuilder(this.rootComponentFunctionBuilder);
		
		this.rootComponentFunctionBuilder = rootComponentFunctionBuilder;
		//
		this.getEmbeddedUIContentController().addComponentFunctionBuilder(this.rootComponentFunctionBuilder);
		
		//disable/enable the target selection
		//when there is no root function, target selection is enabled; otherwise, target selection is disabled
		this.getEmbeddedUIContentController().setTargetSelectionDisable(this.rootComponentFunctionBuilder!=null);
		
		this.update();
	}
	
	/**
	 * initialize(if not already) and return the SelectTypePopupBuilder
	 * @return
	 */
	public SelectTypePopupBuilder<ComponentFunctionType> getComponentFunctionTypeSelectionPopup() {
		if(this.componentFunctionTypeSelectionPopup == null) {
			Set<ComponentFunctionType> types = new LinkedHashSet<>();
			types.add(ComponentFunctionType.SIMPLE);
			types.add(ComponentFunctionType.PIECEWISE);
			Function<ComponentFunctionType,String> toStringFunction = e->{return e.getDescription();};
			
			
			this.componentFunctionTypeSelectionPopup = new SelectTypePopupBuilder<>(
					this.getEmbeddedUIContentController().getStage(),toStringFunction, types);
		}
		
		return componentFunctionTypeSelectionPopup;
	}
	

	
	/**
	 * @return the finishable
	 */
	public boolean isFinishable() {
		return finishable;
	}

	/**
	 * set the {@link #finishable} to false
	 * invoked whenever a constraint is found violoated during the process of {@link #update()};
	 * 
	 * @param finishable the finishable to set
	 */
	public void setUnfinishable() {
		this.finishable = false;
	}
	
	///////////////////////
	public CompositionFunctionID getCompositionFunctionID() {
		return new CompositionFunctionID(this.getOwnerCompositionFunctionGroup().getID(), this.getIndexID());
	}
	
	/**
	 * lookup and return the set of CFGTargets of the {@link #ownerCompositionFunctionGroup} not assigned to any existing {@link CompositionFunction}
	 * @return
	 * @throws SQLException 
	 */
	public Map<SimpleName, CFGTarget<?>> getCFGTargetNameMapUnassignedToCF() throws SQLException{
		if(this.targetNameMapUnassignedToCF == null) {
//			try {
				this.targetNameMapUnassignedToCF = 
						this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().getCFGTargetNameMapUnassignedToCF(this.getOwnerCompositionFunctionGroup().getID());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.exit(1);
//			}
		}
		
		return this.targetNameMapUnassignedToCF;//TODO
	}

	//////////////////////////
	/**
	 * invoked whenever a change is made on the ComponentFunction tree or the assigned set of CFGTargets;
	 * 
	 * this will trigger a invocation chain to each node on the current tree from root to each leaf to find out if any constraint is violated for each node;
	 * 
	 * also update displayed information for the current status of each node;
	 * 
	 * also reset the layout of the ComponentFunction tree;
	 * 
	 * also will detect whether this {@link CompositionFunctionBuilder} is finishable or not;
	 * 
	 * ================================================================
	 * directly listened events/status (this method is directly invoked from where the change is made)
	 * 1. change on the selected set of targets
	 * 		implemented mainly in {@link CompositionFunctionBuilderEmbeddedUIContentController}
	 * 2. the tree structure change event
	 * 		add/remove node(s) or subtree
	 * 			root node
	 * 			non-root node
	 * 		
	 * 3. ComponentFunction content change event
	 * 		SimpleFunction: add/delete an evaluator
	 * 		PiecewiseFunction:add/delete a condition/evaluator
	 * 
	 * 4. evaluator status change event
	 * 		every {@link AbstractEvaluatorBuilder} on current ComponentFunctionBuilder tree
	 * 		
	 * 5. whenever a FreeInputVariableBuilder's status is changed
	 * 		
	 * 6. whenever a CFGTargetOutputVariableBuilder's status is changed
	 * 		
	 * ================================================================
	 * implementation
	 * 
	 * 1. reset fields that are to be updated 
	 * 
	 * 		1. the {@link #originalIndependentFreeInputVariableTypeNameMap} to empty;
	 * 		2. {@link #finishable} to true;
	 * 
	 * 
	 * Following steps (2-3) are invoked only when root {@link ComponentFunctionBuilder} is not null;
	 * 
	 * 2. invoke the root {@link ComponentFunctionBuilder#update()} method to trigger a updating chain reaction of all current {@link ComponentFunctionBuilder}s on the tree;
	 * 
	 * 		note that this step will update {@link #originalIndependentFreeInputVariableTypeNameMap} with current set of {@link IndependentFreeInputVariableType}s
	 * 
	 * 
	 * 3. invoke {@link #updateComponentFunctionTreeLayout()} method
	 * 
	 * 
	 * 4. update UI information
	 * 		1. update list of original IndependentFreeInputVariableType entry list
	 * 		
	 * 		2. update the summary information
	 * 
	 * ==========================================
	 * constraints need to be checked to determine whether this {@link CompositionFunctionBuilder} is finishable or not
	 * 
	 * see {@link ComponentFunctionBuilder#identifyViolatedConstraints()} method
	 * @throws IOException 
	 * @throws SQLException 
	 * 		
	 * 		
	 * 		
	 * 
	 */
	public void update() throws SQLException, IOException {
		
		System.out.println("update tree!");
		
		//1
		this.finishable = true;
		
		this.originalIndependentFreeInputVariableTypeNameMap.clear();
		this.originalIndependentFIVTypeNameNumberOfEmplyoerFIVMap.clear();
		this.originalIndependentFreeInputVariableTypeNameEntryBuilderMap.forEach((k,v)->{
			this.originalIndependentFreeInputVariableTypeNameMap.put(k, v.getIndependentFreeInputVariableTypeBuilder().getCurrentValue());
			this.originalIndependentFIVTypeNameNumberOfEmplyoerFIVMap.put(k, 0);
		});
		
		
		
		this.dependedCompositionFunctionIDSet.clear();
		this.dependedRecordDataMetadataIDSet.clear();
		
		
		if(this.getRootComponentFunctionBuilder()!=null) {
			
			//2
			this.getRootComponentFunctionBuilder().update();
			
			
			//3
//			this.getRootComponentFunctionBuilder().resetFreeInputVariableBuilders();
			
			
			//3
			this.updateComponentFunctionTreeLayout();
			
		}else {
			this.finishable = false;
		}
		
		
		//4 
		this.updateStatusInforOnUI();
		
		//
		this.getEmbeddedUIContentController().setFinishableLabe();
	}
	
	
	
	/**
	 * 1. update list of original IndependentFreeInputVariableType entry list
	 * 		1. for each entry, update the type name and number of employer FIVBuilder
	 * 		2. if number of employer FIVBuilder is 0, delete button should be enabled;
	 * 		3. if number of employer FIVBuilder is positive, delete button should be disabled
	 * 		4. add each entry builder to the independentFreeInputVariableTypeVBox
	 * 
	 * 2. update summary information of the tree on the UI
	 * 
	 */
	private void updateStatusInforOnUI() {
		this.getEmbeddedUIContentController().independentFreeInputVariableTypeVBox.getChildren().clear();
		
		for(SimpleName typeName:this.originalIndependentFreeInputVariableTypeNameEntryBuilderMap.keySet()){
			OriginalIndieFIVTypeEntryBuilder entryBuilder = this.originalIndependentFreeInputVariableTypeNameEntryBuilderMap.get(typeName);
			entryBuilder.setEmployerFreeInputVariableNum(
					this.originalIndependentFIVTypeNameNumberOfEmplyoerFIVMap.get(typeName));
			entryBuilder.setTypeName(typeName.getStringValue());
			
			entryBuilder.setDeleteButtonDisable(this.originalIndependentFIVTypeNameNumberOfEmplyoerFIVMap.get(typeName)>0);
			
			this.getEmbeddedUIContentController().independentFreeInputVariableTypeVBox.getChildren().add(entryBuilder.getController().getRootContainerNode());
		}
		
		
		//1
		this.getEmbeddedUIContentController().updateSummary();
	}
	
	
	
	
	
	/**
	 * update the layout
	 */
	public void updateComponentFunctionTreeLayout() {
		this.leafIndexCounter = 0;
		this.previousLeafNodeBottomY = 0;
		
		this.rootComponentFunctionBuilder.calculateLayout();
		
		this.rootComponentFunctionBuilder.setLayout();
		
	}
	
	
}
