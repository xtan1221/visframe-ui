package builder.visframe.function.component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.visframe.function.component.utils.PiecewiseFunctionConditionEntryBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.input.nonrecordwise.FreeInputVariableBuilder;
import builder.visframe.function.variable.input.nonrecordwise.SQLAggregateFunctionBasedInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.CFGTargetInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.RecordAttributeInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.UpstreamValueTableColumnOutputVariableInputVariableBuilder;
import builder.visframe.function.variable.output.CFGTargetOutputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import builder.visframe.function.variable.output.ValueTableColumnOutputVariableBuilder;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import function.component.ComponentFunction;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import function.target.CFGTarget;
import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.input.recordwise.type.UpstreamValueTableColumnOutputVariableInputVariable;
import function.variable.output.type.ValueTableColumnOutputVariable;
import metadata.MetadataID;

public abstract class ComponentFunctionBuilder<T extends ComponentFunction, C extends ComponentFunctionBuilderEmbeddedUIContentController<T>> extends LeafNodeBuilder<T, C>{
	
	private final CompositionFunctionBuilder hostCompositionFunctionBuilder;
	
	private final ComponentFunctionBuilder<?,?> previousComponentFunctionBuilder;
	
	private int indexID;
	
	
	////////////////////////////////
	
	/**
	 * the set of {@link ValueTableColumnOutputVariableBuilder}s of all {@link AbstractEvaluator}s of all upstream {@link ComponentFunctionBuilder}s;
	 * 
	 * updated by the method {@link #update()}
	 */
	private Set<ValueTableColumnOutputVariableBuilder<?>> upstreamValueTableColumnOutputVariableBuilderSet;
	
	
	/**
	 * the set of {@link UpstreamValueTableColumnOutputVariableInputVariableBuilder}s of all {@link AbstractEvaluator}s of this {@link ComponentFunctionBuilder};
	 * updated by the method {@link #update()}
	 */
	private Set<UpstreamValueTableColumnOutputVariableInputVariableBuilder> upstreamValueTableColumnOutputVariableInputVariableBuilderSet;
	
	
	/**
	 * the set of {@link CFGTarget} names calculated by {@link OutputVariableBuilder}s of {@link AbstractEvaluator}s of this {@link ComponentFunctionBuilder};
	 * updated by the method {@link #update()};
	 */
	private Set<SimpleName> CFGTargetNameSetCalculatedByThisBuilder;

	
	/**
	 * the set of {@link CFGTarget}s not assigned to any {@link CFGTargetOutputVariableBuilder} on the path from root to this one(inclusive);
	 * updated by the method {@link #update()};
	 */
	private Map<SimpleName, CFGTarget<?>> uncalculatedCFGTargetNameMap;
	
	
	//////////////////////////////////
	
//	private Integer leafIndex;
	protected int edgeNumToRoot;
	//layout 
	protected double layoutX;
	protected double layoutY;
	
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param embeddedUIContentFXMLFileDirString
	 * @param hostCompositionFunctionBuilder
	 * @param previousComponentFunctionBuilder
	 * @param indexID
	 */
	protected ComponentFunctionBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, String embeddedUIContentFXMLFileDirString,
			
			CompositionFunctionBuilder hostCompositionFunctionBuilder,
			ComponentFunctionBuilder<?,?> previousComponentFunctionBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, embeddedUIContentFXMLFileDirString);
		
//		this.currentVisframeExecptionSet = new LinkedHashSet<>();
		this.hostCompositionFunctionBuilder = hostCompositionFunctionBuilder;
		this.previousComponentFunctionBuilder = previousComponentFunctionBuilder;
		
		this.indexID = this.hostCompositionFunctionBuilder.getNextAvailableComponentFunctionIndexID();
		
		
		this.upstreamValueTableColumnOutputVariableBuilderSet = new LinkedHashSet<>();
		this.upstreamValueTableColumnOutputVariableInputVariableBuilderSet = new LinkedHashSet<>();
		this.CFGTargetNameSetCalculatedByThisBuilder = new LinkedHashSet<>();
		this.uncalculatedCFGTargetNameMap = new LinkedHashMap<>();
		
		
		if(this.getPreviousComponentFunctionBuilder()==null) {
			this.edgeNumToRoot = 0;
		}else {
			this.edgeNumToRoot = this.getPreviousComponentFunctionBuilder().edgeNumToRoot+1;
		}
	}

	
	/**
	 * @return the hostCompositionFunctionBuilder
	 */
	public CompositionFunctionBuilder getHostCompositionFunctionBuilder() {
		return hostCompositionFunctionBuilder;
	}

	
	/**
	 * @return the previousComponentFunctionBuilder
	 */
	public ComponentFunctionBuilder<?, ?> getPreviousComponentFunctionBuilder() {
		return previousComponentFunctionBuilder;
	}

	/**
	 * delete the given next SimpleFunctionBuilder of this ComponentFunctionBuilder;
	 * @param next
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public abstract void deleteNextFunction(ComponentFunctionBuilder<?,?> nextFunctionBuilder) throws SQLException, IOException;
	
	/**
	 * delete all next functions as well as their descendants
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void deleteAllNextFunctions() throws SQLException, IOException {
		if(this instanceof SimpleFunctionBuilder) {
			SimpleFunctionBuilder sfb = (SimpleFunctionBuilder)this;
			if(sfb.getNextComponentFunctionBuilder()!=null) {
				sfb.getNextComponentFunctionBuilder().deleteAllNextFunctions();
				this.deleteNextFunction(sfb.getNextComponentFunctionBuilder());
			}
			
		}else {
			PiecewiseFunctionBuilder pfb = (PiecewiseFunctionBuilder)this;
			if(pfb.getDefaultNextComponentFunctionBuilder()!=null) {
				pfb.getDefaultNextComponentFunctionBuilder().deleteAllNextFunctions();
				this.deleteNextFunction(pfb.getDefaultNextComponentFunctionBuilder());
			}
			for(PiecewiseFunctionConditionEntryBuilder e: pfb.getOrderedListOfConditionEntryBuilderByPrecedence()){
				if(e.getNextComponentFunctionBuilder()!=null) {
					e.getNextComponentFunctionBuilder().deleteAllNextFunctions();
					this.deleteNextFunction(e.getNextComponentFunctionBuilder());
				}
					
			}
		}
		
		
	}
	/**
	 * @return the indexID
	 */
	public int getIndexID() {
		return indexID;
	}
	
	
	/**
	 * @param indexID the indexID to set
	 */
	public void setIndexID(int indexID) {
		this.indexID = indexID;
	}


	/**
	 * return the current set of {@link AbstractEvaluatorBuilder}
	 * @return
	 */
	protected abstract Set<AbstractEvaluatorBuilder<?>> getEvaluatorBuilderSet();
	
	/**
	 * return the current set of all non-null next {@link ComponentFunctionBuilder}s of this {@link ComponentFunctionBuilder}
	 * 
	 * for SimpleFunctionBuilder, return the non-null next 
	 * for PiecewiseFunctionBuilder, return the non-null default next and non-null condition next ones;
	 * 
	 * @return
	 */
	protected abstract Set<ComponentFunctionBuilder<?,?>> getNextComponentFunctionBuilderSet();
	
	///////////////////////////////////////////////
	/**
	 * return the all ValueTableColumnOutputVariable of upstream ComponentFunctionBuilders of this one;
	 * facilitate building of {@link UpstreamValueTableColumnOutputVariableInputVariable}s for evaluator builders of this {@link ComponentFunctionBuilder}
	 * @return
	 */
	public Set<ValueTableColumnOutputVariableBuilder<?>> getUpstreamValueTableColumnOutputVariableBuilderSet(){
		return this.upstreamValueTableColumnOutputVariableBuilderSet;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<UpstreamValueTableColumnOutputVariableInputVariableBuilder> getUpstreamValueTableColumnOutputVariableInputVariableBuilderSet() {
		return upstreamValueTableColumnOutputVariableInputVariableBuilderSet;
	}
	
	
	public Set<SimpleName> getCFGTargetNameSetCalculatedByThisBuilder() {
		return CFGTargetNameSetCalculatedByThisBuilder;
	}
	
	/**
	 * return the set of CFGTargets that is not assigned to any CFGTargetOutputVariableBuilder on the path from root ComponentFunctionBuilder to this one(inclusive), thus un-calculated;
	 * 
	 * facilitate to build CFGTargetOutputVariableBuilder of EvaluatorBuiders of this ComponentFunctionBuilder;
	 * @return
	 */
	public Map<SimpleName, CFGTarget<?>> getUncalculatedCFGTargetNameMap(){
		return this.uncalculatedCFGTargetNameMap;
	}
	
	
	/**
	 * update {@link #upstreamValueTableColumnOutputVariableSet} and {@link #uncalculatedCFGTargetSet};
	 * 
	 * also update any subclass specific features as needed;
	 * 
	 * then invoke the {@link #identifyViolatedConstraints()} and {@link #updateUI()} of this {@link ComponentFunctionBuilder};
	 * 
	 * then invoke the {@link #update()} method of each incident next {@link ComponentFunctionBuilder};
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void update() throws SQLException, IOException {
		
		this.prepareUpdate();
		
		this.identifyViolatedConstraints();
		
		this.updateUI();
		
		for(ComponentFunctionBuilder<?, ?> cfb:this.getNextComponentFunctionBuilderSet()){
			cfb.update();
		}
	}
	
	
	/**
	 * update the following:
	 * 
	 * 1. update the {@link #CFGTargetNameSetCalculatedByThisBuilder} and {@link #uncalculatedCFGTargetSet};
	 * 		then reset the pool of targetSelector of all {@link CFGTargetOutputVariableBuilder} of {@link AbstractEvaluatorBuilder}s of 
	 * 		this {@link ComponentFunctionBuilder} with updated {@link #uncalculatedCFGTargetSet};
	 * 		
	 * 		also adjust the UI based on whether {@link #uncalculatedCFGTargetSet} is empty or not (implemented in subclass)
	 * 
	 * 2. {@link #upstreamValueTableColumnOutputVariableBuilderSet}
	 * 		then reset the pool of upstreamValueTableColumnOutputVariableSelector of all {@link UpstreamValueTableColumnOutputVariableInputVariableBuilder}s of
	 * 		{@link AbstractEvaluatorBuilder}s of this {@link ComponentFunctionBuilder} with updated {@link #upstreamValueTableColumnOutputVariableBuilderSet}
	 * 
	 * 3. {@link #upstreamValueTableColumnOutputVariableInputVariableBuilderSet}
	 * 
	 * 4. update the pool of each FreeInputVariableBuilder's originalIndieFIVTypeSelector with the current host {@link CompositionFunctionBuilder#getOriginalIndependentFreeInputVariableTypeNameMap()}
	 * 		also update the host {@link CompositionFunctionBuilder#getOriginalIndependentFIVTypeNameNumberOfEmplyoerFIVMap()} if the FreeInputVariableBuilder employs an original IndependentFreeInputVariableType
	 * 
	 * 
	 * 5. update the host 
	 * 		
	 * 		{@link CompositionFunctionBuilder#dependedCompositionFunctionGroupSet}; 
	 * 		{@link CompositionFunctionBuilder#dependedRecordDataMetadataSet}
	 * 	also, for each original IndependentFreeInputVariableType, always set the FreeInputVariableBuilder that is encountered first to the build from scratch mode;
	 *  and set other FreeInputVariableBuilders to the select mode;
	 *  
	 *  this is to deal with the possibilities that during the process of building the CompositionFunction, the FreeInputVariableBuilder that build the IndependentFreeInputVariableType might be deleted while
	 *  there are still other FreeInputVariableBuilders uses the built IndependentFreeInputVariableType
	 * @throws SQLException 
	 * @throws IOException 
	 */
	protected void prepareUpdate() throws SQLException, IOException {
		//1
		this.CFGTargetNameSetCalculatedByThisBuilder.clear();
		this.uncalculatedCFGTargetNameMap.clear();
		
		if(this instanceof SimpleFunctionBuilder) {
			for(AbstractEvaluatorBuilder<?> eb:this.getEvaluatorBuilderSet()){
				eb.getOutputVariableBuilderSet().forEach(ovb->{
					if(ovb instanceof CFGTargetOutputVariableBuilder) {
						CFGTargetOutputVariableBuilder cfgtovb = (CFGTargetOutputVariableBuilder) ovb;
						if(cfgtovb.getCurrentStatus().hasValidValue())
							this.CFGTargetNameSetCalculatedByThisBuilder.add(cfgtovb.getCurrentValue().getTargetName());
					}
				});
			}
			
			//
			if(this.getPreviousComponentFunctionBuilder()==null) {
				this.uncalculatedCFGTargetNameMap.putAll(this.getHostCompositionFunctionBuilder().getAssignedTargetNameMap());
			}else {
				this.getPreviousComponentFunctionBuilder().getUncalculatedCFGTargetNameMap().keySet().forEach(tn->{
					this.uncalculatedCFGTargetNameMap.put(tn, this.getHostCompositionFunctionBuilder().getAssignedTargetNameMap().get(tn));
				});
			}
			
			this.CFGTargetNameSetCalculatedByThisBuilder.forEach(tn->{
				this.uncalculatedCFGTargetNameMap.remove(tn);
			});
			
		}else {//PiecewiseFunctionBuilder
			if(this.getPreviousComponentFunctionBuilder()==null) {
				this.uncalculatedCFGTargetNameMap.putAll(this.getHostCompositionFunctionBuilder().getAssignedTargetNameMap());
			}else {
				this.uncalculatedCFGTargetNameMap.putAll(this.getPreviousComponentFunctionBuilder().getUncalculatedCFGTargetNameMap());
			}
		}
		
		//update pool of targetSelector of all {@link CFGTargetOutputVariableBuilder}
		for(AbstractEvaluatorBuilder<?> eb:this.getEvaluatorBuilderSet()){
			for(OutputVariableBuilder<?> ovb:eb.getOutputVariableBuilderSet()){
				if(ovb instanceof CFGTargetOutputVariableBuilder) {
					CFGTargetOutputVariableBuilder cfgtovb = (CFGTargetOutputVariableBuilder)ovb;
					Set<CFGTarget<?>> pool = new LinkedHashSet<>();
					
					//add the target that is selected by cfgtovb
					CFGTarget<?> selectedTarget = null;
					if(cfgtovb.getCurrentStatus().hasValidValue()) {
						selectedTarget = cfgtovb.getCurrentValue().getTarget();
						pool.add(selectedTarget);
					}
					
					pool.addAll(this.uncalculatedCFGTargetNameMap.values());
					
					cfgtovb.getTargetSelector().setPool(pool);
					if(selectedTarget!=null)
						cfgtovb.getTargetSelector().setValue(selectedTarget, false);
				}
			}
		}
		
		//////////////////////////////////////////////////////////////////////
		//2 
		//note that if previous ComponentFunctionBuilder is not null, 
		//upstreamValueTableColumnOutputVariableBuilderSet is composed of the upstreamValueTableColumnOutputVariableBuilderSet of previous ComponentFunctionBuilder 
		//if previous ComponentFunctionBuilder is of SimpleFunctionBuilder type, also include the set of ValueTableColumnOutputVariableBuilder of previous ComponentFunctionBuilder
		this.upstreamValueTableColumnOutputVariableBuilderSet.clear();
		
		
		if(this.getPreviousComponentFunctionBuilder()!=null) {
			this.upstreamValueTableColumnOutputVariableBuilderSet.addAll(this.getPreviousComponentFunctionBuilder().getUpstreamValueTableColumnOutputVariableBuilderSet());
			//previous is SimpleFunctionBuilder
			if(this.getPreviousComponentFunctionBuilder() instanceof SimpleFunctionBuilder) {
				SimpleFunctionBuilder sfb = (SimpleFunctionBuilder)this.getPreviousComponentFunctionBuilder();
				for(AbstractEvaluatorBuilder<?> eb:sfb.getEvaluatorBuilderSet()){
					eb.getOutputVariableBuilderSet().forEach(ovb->{
						this.upstreamValueTableColumnOutputVariableBuilderSet.add((ValueTableColumnOutputVariableBuilder<?>) ovb);
					});
				}
			}
		}
		
		
		//3 + 4
		//2 also reset the pool of upstreamValueTableColumnOutputVariableSelector of all {@link UpstreamValueTableColumnOutputVariableInputVariableBuilder}s
		this.upstreamValueTableColumnOutputVariableInputVariableBuilderSet.clear();
		
		for(AbstractEvaluatorBuilder<?> eb:this.getEvaluatorBuilderSet()){
			for(InputVariableBuilder<?> ivb:eb.getInputVariableBuilderSet()){
				if(ivb instanceof UpstreamValueTableColumnOutputVariableInputVariableBuilder) {
					UpstreamValueTableColumnOutputVariableInputVariableBuilder uvtcovivb = (UpstreamValueTableColumnOutputVariableInputVariableBuilder)ivb;
					this.upstreamValueTableColumnOutputVariableInputVariableBuilderSet.add(uvtcovivb);
					
					Set<ValueTableColumnOutputVariable> pool = new LinkedHashSet<>();
					this.upstreamValueTableColumnOutputVariableBuilderSet.forEach(e->{
						if(uvtcovivb.getDataTypeConstraints().test(e.getCurrentValue().getSQLDataType()))
							pool.add(e.getCurrentValue());
					});
					uvtcovivb.getUpstreamValueTableColumnOutputVariableSelector().setPool(pool);
				}else if(ivb instanceof SQLAggregateFunctionBasedInputVariableBuilder) {
					SQLAggregateFunctionBasedInputVariableBuilder safbivb = (SQLAggregateFunctionBasedInputVariableBuilder)ivb;
					if(!safbivb.getRecordwiseInputVariable1Builder().isSetToNull()) {
						if(safbivb.getRecordwiseInputVariable1Builder().getInputVariableTypeSelector().getCurrentStatus().hasValidValue()) {
							if(safbivb.getRecordwiseInputVariable1Builder().getInputVariableTypeSelector().getCurrentValue().equals(UpstreamValueTableColumnOutputVariableInputVariable.class)){
								UpstreamValueTableColumnOutputVariableInputVariableBuilder builder = (UpstreamValueTableColumnOutputVariableInputVariableBuilder)safbivb.getRecordwiseInputVariable1Builder().getInputVariableBuilder();
								
								Set<ValueTableColumnOutputVariable> pool = new LinkedHashSet<>();
								this.upstreamValueTableColumnOutputVariableBuilderSet.forEach(e->{
									if(builder.getDataTypeConstraints().test(e.getCurrentValue().getSQLDataType()))
										pool.add(e.getCurrentValue());
								});
								
								builder.getUpstreamValueTableColumnOutputVariableSelector().setPool(pool);
							}
						}
						
						if(safbivb.getRecordwiseInputVariable2Builder().getInputVariableTypeSelector().getCurrentStatus().hasValidValue()) {
							if(safbivb.getRecordwiseInputVariable2Builder().getInputVariableTypeSelector().getCurrentValue().equals(UpstreamValueTableColumnOutputVariableInputVariable.class)){
								UpstreamValueTableColumnOutputVariableInputVariableBuilder builder = (UpstreamValueTableColumnOutputVariableInputVariableBuilder)safbivb.getRecordwiseInputVariable2Builder().getInputVariableBuilder();
								
								Set<ValueTableColumnOutputVariable> pool = new LinkedHashSet<>();
								this.upstreamValueTableColumnOutputVariableBuilderSet.forEach(e->{
									if(builder.getDataTypeConstraints().test(e.getCurrentValue().getSQLDataType()))
										pool.add(e.getCurrentValue());
								});
								
								builder.getUpstreamValueTableColumnOutputVariableSelector().setPool(pool);
							}
						}
					}
					
					//update the depended record data set
					if(safbivb.getTargetRecordMetadataIDSelector().getCurrentStatus().hasValidValue()) {
						MetadataID recordDataId = safbivb.getTargetRecordMetadataIDSelector().getCurrentValue();
						if(recordDataId.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID())) {
							//
						}else {
							this.getHostCompositionFunctionBuilder().getDependedRecordDataMetadataIDSet().add(recordDataId);
						}
					}
					
				}else if(ivb instanceof FreeInputVariableBuilder) {
					FreeInputVariableBuilder fivb = (FreeInputVariableBuilder)ivb;
					
					if(fivb.getCurrentStatus().hasValidValue()&&fivb.getCurrentValue().getIndependentFreeInputVariableType().getOwnerCompositionFunctionID().equals(this.getHostCompositionFunctionBuilder().getCompositionFunctionID())) {
						IndependentFreeInputVariableType type = fivb.getCurrentValue().getIndependentFreeInputVariableType();

						//update the pool and set the value
						Set<IndependentFreeInputVariableType> pool = new LinkedHashSet<>();
						this.getHostCompositionFunctionBuilder().getOriginalIndependentFreeInputVariableTypeNameMap().values().forEach(e->{
							if(fivb.getDataTypeConstraints().test(e.getSQLDataType()))
								pool.add(e);
						});
						
						fivb.getOriginalIndieFIVTypeSelector().setPool(pool);
						fivb.getOriginalIndieFIVTypeSelector().setValue(type, false);
						
						//update the number of employer FreeInputVariableBuilder
						this.getHostCompositionFunctionBuilder().getOriginalIndependentFIVTypeNameNumberOfEmplyoerFIVMap().put(
								type.getName(), 
								this.getHostCompositionFunctionBuilder().getOriginalIndependentFIVTypeNameNumberOfEmplyoerFIVMap().get(type.getName())+1
								);
						
						
						//update depended cf
						if(type.getOwnerCompositionFunctionID().equals(this.getHostCompositionFunctionBuilder().getCompositionFunctionID())){
							//original IndependentFreeInputVariableType
						}else {
							this.getHostCompositionFunctionBuilder().getDependedCompositionFunctionIDSet().add(type.getOwnerCompositionFunctionID());
						}
						
					}else {
						//simply update the pool
						Set<IndependentFreeInputVariableType> pool = new LinkedHashSet<>();
						this.getHostCompositionFunctionBuilder().getOriginalIndependentFreeInputVariableTypeNameMap().values().forEach(e->{
							if(fivb.getDataTypeConstraints().test(e.getSQLDataType()))
								pool.add(e);
						});
						
						fivb.getOriginalIndieFIVTypeSelector().setPool(pool);
					}
					
					
					//
					
				}else if(ivb instanceof CFGTargetInputVariableBuilder) {//update the depended record data
					CFGTargetInputVariableBuilder cfgtivb = (CFGTargetInputVariableBuilder)ivb;
					if(cfgtivb.getCompositionFunctionGroupIDSelector().getCurrentStatus().hasValidValue() && cfgtivb.getTargetSelector().getCurrentStatus().hasValidValue()) {
						CompositionFunctionGroupID cfgid = cfgtivb.getCompositionFunctionGroupIDSelector().getCurrentValue();
						
						CFGTarget<?> target = cfgtivb.getTargetSelector().getCurrentValue();
						
						try {
							//update depended record data;
							if(cfgid.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getID())) {
								//not add
							}else {
								CompositionFunctionGroup cfg = 
										this.getHostCompositionFunctionBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(cfgid);
								this.getHostCompositionFunctionBuilder().getDependedRecordDataMetadataIDSet().add(cfg.getOwnerRecordDataMetadataID());
							}
							
							//update depended cf
							CompositionFunctionID cfid = 
									this.getHostCompositionFunctionBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().
									getCompositionFunctionManager().getTargetNameAssignedCFIDMap(cfgid).get(target.getName());
							
							this.getHostCompositionFunctionBuilder().getDependedCompositionFunctionIDSet().add(cfid);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.exit(1);
						}
					}
					
				}else if(ivb instanceof RecordAttributeInputVariableBuilder) {//update the depended record data
					RecordAttributeInputVariableBuilder rdtcivb = (RecordAttributeInputVariableBuilder)ivb;
					
					if(rdtcivb.getTargetRecordDataMetadataIDSelector().getCurrentStatus().hasValidValue()) {
						MetadataID recordDataId = rdtcivb.getTargetRecordDataMetadataIDSelector().getCurrentValue();
						if(recordDataId.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID())) {
							//
						}else {
							this.getHostCompositionFunctionBuilder().getDependedRecordDataMetadataIDSet().add(recordDataId);
						}
					}
				}
			}
		}
		
		
		//
	}
	
	/**
	 * 
	 * invoke host {@link CompositionFunctionBuilder#setUnfinishable()} method if any constraint violation is found
	 * 
	 * implementation in {@link ComponentFunctionBuilder#update()} method
	 * 
	 * 1. for SimpleFunctionBuilder
	 * 		invoke {@link #setUnfinishable()} method in following cases:
	 * 		1. evaluator set is empty
	 * 		2 .at least one evaluator builder does not have valid value;
	 * 		3. unassigned Target is non-empty but next function is null;
	 * 2. for PiecewiseFunctionBuilder
	 * 		invoke {@link #setUnfinishable()} method in following cases:
	 * 		1. default next function is null;
	 * 		2. condition set is empty
	 * 		3. at least one condition's evaluator builder does not have valid value or next function is null;
	 */
	protected void identifyViolatedConstraints() {
		if(this instanceof SimpleFunctionBuilder) {
			SimpleFunctionBuilder sfb = (SimpleFunctionBuilder)this;
			
			//1. evaluator set is empty
			if(sfb.getEvaluatorIndexIDEntryBuilderMap().isEmpty()) {
				this.getHostCompositionFunctionBuilder().setUnfinishable();
				return;
			}
			
			//2 .at least one evaluator builder does not have valid value;
			sfb.getEvaluatorBuilderSet().forEach(e->{
				if(!e.getCurrentStatus().hasValidValue()) {
					this.getHostCompositionFunctionBuilder().setUnfinishable();
					return;
				}
			});
			
			//3. unassigned Target is non-empty but next function is null;
			if(!sfb.getUncalculatedCFGTargetNameMap().isEmpty() && sfb.getNextComponentFunctionBuilder()==null) {
				this.getHostCompositionFunctionBuilder().setUnfinishable();
				return;
			}
			
		}else {
			PiecewiseFunctionBuilder pfb = (PiecewiseFunctionBuilder)this;
			
			//1. default next function is null;
			if(pfb.getDefaultNextComponentFunctionBuilder()==null) {
				this.getHostCompositionFunctionBuilder().setUnfinishable();
				return;
			}
			
			//2. condition set is empty
			if(pfb.getOrderedListOfConditionEntryBuilderByPrecedence().isEmpty()) {
				this.getHostCompositionFunctionBuilder().setUnfinishable();
				return;
			}
			
			//3. at least one condition's evaluator builder does not have valid value or next function is null;
			pfb.getOrderedListOfConditionEntryBuilderByPrecedence().forEach(e->{
				if(!e.getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getCurrentStatus().hasValidValue() || e.getNextComponentFunctionBuilder()==null) {
					this.getHostCompositionFunctionBuilder().setUnfinishable();
					return;
				}
				
			});
		}
		
		
	}
	
	
	/**
	 * update UI related information and features;
	 * 
	 * must be invoked after {@link #prepareUpdate()}
	 */
	protected abstract void updateUI();
	
	
	
	
	///////////////////////////////////////////////
	/**
	 * delete this ComponentFunctionBuilder and all of its descendants from the tree;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void deleteFromTree() throws SQLException, IOException {
		if(this.isLeaf()) {
			//do not go further down
		}else {//go to next level
			if(this instanceof SimpleFunctionBuilder) {
				SimpleFunctionBuilder sfb = (SimpleFunctionBuilder)this;
				
				sfb.getNextComponentFunctionBuilder().deleteFromTree();
				
			}else {
				PiecewiseFunctionBuilder pfb = (PiecewiseFunctionBuilder)this;
				
				if(pfb.getDefaultNextComponentFunctionBuilder()!=null)
					pfb.getDefaultNextComponentFunctionBuilder().deleteFromTree();
				
				for(PiecewiseFunctionConditionEntryBuilder e:pfb.getOrderedListOfConditionEntryBuilderByPrecedence()){
					if(e.getNextComponentFunctionBuilder()!=null)
						e.getNextComponentFunctionBuilder().deleteFromTree();
				}
			}
		}
		
		if(this.getPreviousComponentFunctionBuilder()!=null){//this node is non-root node
			this.getPreviousComponentFunctionBuilder().deleteNextFunction(this);
		}else {//this node is root node;
			this.getHostCompositionFunctionBuilder().setRootComponentFunctionBuilder(null);
		}
	}
	
	
	

	
	/////////////////////////////////////
	//layout
//	private double getWidth() {
//		return this.getEmbeddedUIContentController().getRootParentNode().getWidth();
//	}
	
	private double getHeight() {
//		double height = this.getEmbeddedUIContentController().getRootParentNode().getHeight();
//		double height2 = this.getEmbeddedUIContentController().getRootParentNode().getLayoutBounds().getHeight();
//		System.out.println(this.getHostCompositionFunctionBuilder().getEmbeddedUIContentController().componentFunctionTreeAnchorPane.getChildren().size());
//		System.out.println("height!!!!!!!!!"+this.getEmbeddedUIContentController().getMainContentPane().getHeight());
		return this.getEmbeddedUIContentController().getMainContentPane().getHeight();//
	}
	
	public void setLayout() {
//		System.out.println("before set layout; height!!!!!!!!!"+this.getEmbeddedUIContentController().getMainContentPane().getHeight());
//		System.out.println("calculated layoutx:"+this.layoutX);
//		System.out.println("calculated layouty:"+this.layoutY);
		
		this.getEmbeddedUIContentController().getRootParentNode().setLayoutX(this.layoutX);
		this.getEmbeddedUIContentController().getRootParentNode().setLayoutY(this.layoutY);
//		System.out.println("after set layout; height!!!!!!!!!"+this.getEmbeddedUIContentController().getMainContentPane().getHeight());
//		System.out.println("real layoutx:"+this.getEmbeddedUIContentController().getRootParentNode().getLayoutX());
//		System.out.println("real layouty:"+this.getEmbeddedUIContentController().getRootParentNode().getLayoutY());
//		System.out.println("height:"+this.getEmbeddedUIContentController().getRootParentNode().getHeight());
		
		if(this.isLeaf()) {
			//
		}else {
			if(this instanceof SimpleFunctionBuilder) {
				SimpleFunctionBuilder sfb = (SimpleFunctionBuilder)this;
				sfb.getNextComponentFunctionBuilder().setLayout();
			}else {
				PiecewiseFunctionBuilder pfb = (PiecewiseFunctionBuilder)this;
				if(pfb.getDefaultNextComponentFunctionBuilder()!=null)
					pfb.getDefaultNextComponentFunctionBuilder().setLayout();
				
				pfb.getOrderedListOfConditionEntryBuilderByPrecedence().forEach(e->{
					if(e.getNextComponentFunctionBuilder()!=null)
						e.getNextComponentFunctionBuilder().setLayout();
				});
			}
		}
		
		
	}
	
	/**
	 * whether this is a leaf node on the current tree;
	 * note that both PiecewiseFunctionBuilder and SimpleFunctionBuilder can be leaf on the tree during the process of building the composition function;
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		if(this instanceof SimpleFunctionBuilder) {
			return ((SimpleFunctionBuilder)this).getNextComponentFunctionBuilder()==null;
		}else {//piecewise function; only be leaf if there is no default next function created and every condition has no next function created yet
			PiecewiseFunctionBuilder pfb = (PiecewiseFunctionBuilder) this;
			
			if(pfb.getDefaultNextComponentFunctionBuilder()!=null) {
				return false;
			}
			
			for(PiecewiseFunctionConditionEntryBuilder condition:pfb.getOrderedListOfConditionEntryBuilderByPrecedence()) {
				if(condition.getNextComponentFunctionBuilder()!=null)
					return false;
			}
			
			return true;
		}
	}
	
	/**
	 * calculate and set the layout
	 * 
	 * 1. {@link #edgeNumToRoot}; not needed? initialized and fixed during the life span of this ComponentFunctionBuilder;
	 * 		thus calculated in constructor;
	 * 2. if leaf 
	 * 		1. calculate leaf index and update the {@link CompositionFunctionBuilder#leafIndexCounter} of host CompositionFunctionBuilder;
	 * 3. layout x and y
	 * 
	 * 4. if leaf
	 * 		update the {@link CompositionFunctionBuilder#previousLeafNodeBottomY} of host CompositionFunctionBuilder
	 * 
	 * calculate leaf index of this node if isLeaf() returns true;
	 * 
	 * 
	 * otherwise, invoke the next function builder's {@link #calculateLayout()} method;
	 */
	public void calculateLayout() {
		//edgeNumToRoot calculated in constructor
		
		//calculate layout x;
		this.layoutX = this.edgeNumToRoot*(CompositionFunctionBuilder.NODE_LAYOUT_X_DIST+CompositionFunctionBuilder.NODE_DEFAULT_WIDTH);
		
		
		//
		if(this.isLeaf()) {
//			this.leafIndex = this.getHostCompositionFunctionBuilder().leafIndexCounter++;
			
			//
			this.layoutY = CompositionFunctionBuilder.NODE_LAYOUT_Y_DIST+this.hostCompositionFunctionBuilder.previousLeafNodeBottomY;
			
			//update
			this.hostCompositionFunctionBuilder.previousLeafNodeBottomY = this.layoutY+this.getHeight();
			
		}else {//non leaf
			if(this instanceof SimpleFunctionBuilder) {
				SimpleFunctionBuilder sfb = (SimpleFunctionBuilder)this;
				sfb.getNextComponentFunctionBuilder().calculateLayout();
				
				this.layoutY = sfb.getNextComponentFunctionBuilder().layoutY+sfb.getNextComponentFunctionBuilder().getHeight()/2-this.getHeight()/2;
//				this.layoutY = (sfb.getNextComponentFunctionBuilder().layoutY+sfb.getNextComponentFunctionBuilder().getHeight())/2;
//				System.out.println("calculated layoutY:"+layoutY);
			}else {//piecewise function; only be leaf if there is no default next function created and every condition has no next function created yet
				PiecewiseFunctionBuilder pfb = (PiecewiseFunctionBuilder) this;
				
				Double firstChildLayoutY = null;
				
				double lastChildLayoutY = 0;
				double lastChildHeight = 0;
				if(pfb.getDefaultNextComponentFunctionBuilder()!=null) {
					pfb.getDefaultNextComponentFunctionBuilder().calculateLayout();
					
					firstChildLayoutY = pfb.getDefaultNextComponentFunctionBuilder().layoutY;
					lastChildLayoutY = pfb.getDefaultNextComponentFunctionBuilder().layoutY;
					lastChildHeight = pfb.getDefaultNextComponentFunctionBuilder().getHeight();
				}
				
				for(PiecewiseFunctionConditionEntryBuilder condition:pfb.getOrderedListOfConditionEntryBuilderByPrecedence()) {
					if(condition.getNextComponentFunctionBuilder()!=null) {
						condition.getNextComponentFunctionBuilder().calculateLayout();
						if(firstChildLayoutY==null) {
							firstChildLayoutY = condition.getNextComponentFunctionBuilder().layoutY;
						}
						lastChildLayoutY = condition.getNextComponentFunctionBuilder().layoutY;
						lastChildHeight = condition.getNextComponentFunctionBuilder().getHeight();
					}
				}
				
				
				this.layoutY = (firstChildLayoutY+lastChildLayoutY+lastChildHeight)/2-this.getHeight()/2;
			}
		}
		
		//
//		this.setLayout();
	}
	
//	
//	public void printlnLayout() {
//		System.out.println("=======");
//		System.out.println("calcualted x layout:"+this.layoutX);
//		System.out.println("calcualted Y layout:"+this.layoutY);
//		System.out.println("real x layout:"+this.getEmbeddedUIContentController().getRootParentNode().getLayoutX());
//		System.out.println("real y layout:"+this.getEmbeddedUIContentController().getRootParentNode().getLayoutY());
//		if(this.isLeaf()) {
//			//do nothing
//		}else {
//			
//			if(this instanceof SimpleFunctionBuilder) {
//				SimpleFunctionBuilder sfb = (SimpleFunctionBuilder)this;
//				sfb.getNextComponentFunctionBuilder().printlnLayout();
//			}else {
//			}
//		}
//	
//	}
	
	
	/////////////////////////////////
	public static enum ComponentFunctionType{
		SIMPLE("Simple Function"),
		PIECEWISE("Piecewise Function");
		
		private final String description;
		
		ComponentFunctionType(String description){
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
		
	}
}
