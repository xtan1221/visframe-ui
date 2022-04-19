package builder.visframe.function.variable.output;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.basic.misc.SimpleTypeSelector;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.target.CFGTarget;
import function.variable.output.type.CFGTargetOutputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * select a target assigned to the host CompositionFunction that has not been assigned to 
 * any CFGTargetOutputVariableBuilder of EvaluatorBuilder of upstream ComponentFunctionBuilder or of the EvaluatorBuilders of host ComponentFunctionBuilder of this ComponentFunctionBuilder
 * 
 * @author tanxu
 *
 */
public final class CFGTargetOutputVariableBuilder extends ValueTableColumnOutputVariableBuilder<CFGTargetOutputVariable>{
	
	public CFGTargetOutputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder, 
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	////////////////////////////////////
	protected static final String target = "target";
	protected static final String target_description = "target";
	
	////////////////////////
	
	private SimpleTypeSelector<CFGTarget<?>> targetSelector;
	
	/**
	 * @return the targetSelector
	 */
	public SimpleTypeSelector<CFGTarget<?>> getTargetSelector() {
		return targetSelector;
	}

	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//target
		targetSelector = new SimpleTypeSelector<>(
				target, target_description, false, this, 
				e->{return e.getName().getStringValue().concat(";").concat(e.getSQLDataType().getSQLString());},//Function<T,String> typeToStringRepresentationFunction,
				e->{return "name=".concat(e.getName().getStringValue()).concat("; data type=").concat(e.getSQLDataType().getSQLString());}//Function<T,String> typeToDescriptionFunction
				);
		List<CFGTarget<?>> pool = new ArrayList<>();
		this.getHostComponentFunctionBuilder().getUncalculatedCFGTargetNameMap().values().forEach(v->{
			if(this.getDataTypeConstraints().test(v.getSQLDataType()))
				pool.add(v);
		});
		this.targetSelector.setPool(pool);
		
		this.addChildNodeBuilder(targetSelector);
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
	}
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(target).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				CFGTargetOutputVariable CFGTargetOutputVariable = (CFGTargetOutputVariable)value;
				
				
				this.targetSelector.setValue(CFGTargetOutputVariable.getTarget(), isEmpty);
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
	protected CFGTargetOutputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		CFGTarget<?> target = this.targetSelector.getCurrentValue();
		
		
		return new CFGTargetOutputVariable(
//				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				hostCompositionFunctionID, hostComponentFunctionIndexID, hostEvaluatorIndexID,
				aliasNameValue, notesValue,
				
				target
				);
	}	
}
