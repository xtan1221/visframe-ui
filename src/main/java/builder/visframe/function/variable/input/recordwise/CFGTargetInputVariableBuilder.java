package builder.visframe.function.variable.input.recordwise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.basic.misc.SimpleTypeSelector;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.group.CompositionFunctionGroupIDSelector;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunction;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import function.target.CFGTarget;
import function.variable.input.recordwise.type.CFGTargetInputVariable;
import metadata.MetadataID;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * !!!!!!!!!note that the selected CFGTarget cannot be a target of the host CompositionFunctionGroup of this variable that is assigned to the host 
 * {@link CompositionFunction} of the built CFGTargetInputVariable;
 * 
 * also note that for targets not assigned to any cf of the selected cfg cannot be selected to make a CFGTargetInputVariable!
 * see {@link CFGTargetInputVariable}
 * @author tanxu
 *
 */
public final class CFGTargetInputVariableBuilder extends RecordwiseInputVariableBuilder<CFGTargetInputVariable>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 * @param hostCompositionFunctionBuilder
	 * @param hostComponentFunctionBuilder
	 * @param hostEvaluatorBuilder
	 * @param mustBeOfSameOwnerRecordDataWithHostCompositionFunction
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public CFGTargetInputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints,
			Predicate<MetadataID> targetRecordDataMetadataIDCondition
			) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints, targetRecordDataMetadataIDCondition);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
//	/**
//	 * build the sql where condition for all allowed record MetadataIDs;
//	 * 
//	 * @return
//	 */
//	private String getSQLFilterConditionStringForManagementTable() {
//		if(this.mustBeOfSameOwnerRecordDataWithHostCompositionFunction()) {
//			
//			return TableContentSQLStringFactory.buildColumnValueEquityCondition(
//					VisProjectCompositionFunctionGroupManager.OWNER_RECORD_DATA_NAME_COL.getName().getStringValue(), 
//					this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID().getName().getStringValue(),
//					true);
//			
//		}else {
//			return null;
//		}
//	}
	
	////////////////////////////////////
	protected static final String compositionFunctionGroupID = "compositionFunctionGroupID";
	protected static final String compositionFunctionGroupID_description = "compositionFunctionGroupID";
	
	protected static final String target = "target";
	protected static final String target_description = "target";
	
	////////////////////////
	private CompositionFunctionGroupIDSelector compositionFunctionGroupIDSelector;
	
	
	private SimpleTypeSelector<CFGTarget<?>> targetSelector;

	/**
	 * @return the compositionFunctionGroupIDSelector
	 */
	public CompositionFunctionGroupIDSelector getCompositionFunctionGroupIDSelector() {
		return compositionFunctionGroupIDSelector;
	}
	
	/**
	 * @return the targetSelector
	 */
	public SimpleTypeSelector<CFGTarget<?>> getTargetSelector() {
		return targetSelector;
	}

	///////////////////////////////////////
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		/////////////////////////////
		//compositionFunctionGroupID
		compositionFunctionGroupIDSelector = new CompositionFunctionGroupIDSelector(
				compositionFunctionGroupID, compositionFunctionGroupID_description, false, this,
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager(),
				null,//String conditionSQLString;  this.getSQLFilterConditionStringForManagementTable(),
				e->{return this.getTargetRecordDataMetadataIDCondition().test(e.getOwnerRecordDataMetadataID());}//Predicate<CompositionFunctionGroup> filteringCondition
				);
		this.addChildNodeBuilder(compositionFunctionGroupIDSelector);
		
		
		//target
		targetSelector = new SimpleTypeSelector<>(
				target, target_description, false, this, 
				e->{return e.getName().getStringValue().concat(";").concat(e.getSQLDataType().getSQLString());},//Function<T,String> typeToStringRepresentationFunction,
				e->{return "name=".concat(e.getName().getStringValue()).concat("; data type=").concat(e.getSQLDataType().getSQLString());}//Function<T,String> typeToDescriptionFunction
				);
		this.addChildNodeBuilder(targetSelector);
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		//when compositionFunctionGroupIDSelector's status is changed
		//targetSelector should change accordingly based on the status of compositionFunctionGroupIDSelector accordingly;
		Runnable compositionFunctionGroupIDSelectorStatusChangeEventAction = ()->{
			try {
				if(compositionFunctionGroupIDSelector.getCurrentStatus().isDefaultEmpty()) {
					//no cfgID selected, set the pool of the target selector to empty set;
					
					targetSelector.setPool(new ArrayList<>());
					
					targetSelector.setToDefaultEmpty();
					
				}else if(compositionFunctionGroupIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					CompositionFunctionGroupID selectedCFGID = this.compositionFunctionGroupIDSelector.getCurrentValue();
					
	//				try {
					CompositionFunctionGroup cfg = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(selectedCFGID);
					
					Map<SimpleName, CompositionFunctionID> targetNameAssignedCFIDMap = 
							this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionManager().getTargetNameAssignedCFIDMap(selectedCFGID);
					
					//the pool is the set of targets assigned to existing CompositionFunctions of the selected cfg;
					//note that for targets not assigned to any cf of the selected cfg cannot be selected to make a CFGTargetInputVariable!
					Set<CFGTarget<?>> targetPool = new LinkedHashSet<>();
					
					targetNameAssignedCFIDMap.keySet().forEach(e->{
						targetPool.add(cfg.getTargetNameMap().get(e));
					});
					
					
					this.targetSelector.setPool(targetPool);
					
						
	//					if(selectedCFGID.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getID())) {
	//						//only add those targets assigned to other CompositionFunctions but NOT assigned to host CompositionFunction of this variable builder
	//						
	//						
	//					}else {
	//						//add all targets
	//						
	//						Set<CFGTarget<?>> targetPool = new LinkedHashSet<>();
	//						cfg.getTargetNameMap().values().forEach(e->{
	//							if(this.getDataTypeConstraints().test(e.getSQLDataType()))
	//								targetPool.add(e);
	//						});
	//						
	//						this.targetSelector.setPool(targetPool);
	//					}
	//					
						
	//				} catch (SQLException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
					
				}
			} catch (SQLException |IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		compositionFunctionGroupIDSelector.addStatusChangedAction(
				compositionFunctionGroupIDSelectorStatusChangeEventAction);
		
	}
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(compositionFunctionGroupID).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(target).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				CFGTargetInputVariable CFGTargetInputVariable = (CFGTargetInputVariable)value;
				
				this.getChildrenNodeBuilderNameMap().get(compositionFunctionGroupID).setValue(CFGTargetInputVariable.getTargetCompositionFunctionGroupID(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(target).setValue(CFGTargetInputVariable.getTarget(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	/**
	 * build and return a FreeInputVariable;
	 * 
	 * the IndependentFreeInputVariableTypeImpl parameter should be built based on the value of the toBuildNewIndependentFreeInputVariableTypefromScratchBuilder
	 */
	@Override
	protected CFGTargetInputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		
		CompositionFunctionGroupID compositionFunctionGroupIDValue = this.compositionFunctionGroupIDSelector.getCurrentValue();
		CFGTarget<?> targetValue = this.targetSelector.getCurrentValue();
		
		try {
			CompositionFunctionGroup cfg = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(compositionFunctionGroupIDValue);
			MetadataID targetRecordDataID = cfg.getOwnerRecordDataMetadataID();
			
			return new CFGTargetInputVariable(
//					this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
					hostCompositionFunctionID, hostComponentFunctionIndexID, hostEvaluatorIndexID,
					aliasNameValue, notesValue,
					
					targetRecordDataID,
					compositionFunctionGroupIDValue, targetValue
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		//never reached
		return null;
		
	}		

}
