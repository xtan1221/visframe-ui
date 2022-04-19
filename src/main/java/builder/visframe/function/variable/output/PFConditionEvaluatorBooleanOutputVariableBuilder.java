package builder.visframe.function.variable.output;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.output.type.PFConditionEvaluatorBooleanOutputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * simply remove the SQLDataTypeBuilder since it is always boolean type;
 * 
 * @author tanxu
 *
 */
public final class PFConditionEvaluatorBooleanOutputVariableBuilder extends OutputVariableBuilder<PFConditionEvaluatorBooleanOutputVariable>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 * @param hostCompositionFunctionBuilder
	 * @param hostComponentFunctionBuilder
	 * @param hostEvaluatorBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public PFConditionEvaluatorBooleanOutputVariableBuilder(
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
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
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
		
		return changed;
	}
	
	
	/**
	 * build and return a FreeInputVariable;
	 * 
	 * the IndependentFreeInputVariableTypeImpl parameter should be built based on the value of the toBuildNewIndependentFreeInputVariableTypefromScratchBuilder
	 */
	@Override
	protected PFConditionEvaluatorBooleanOutputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		return new PFConditionEvaluatorBooleanOutputVariable(
//				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				hostCompositionFunctionID, hostComponentFunctionIndexID, hostEvaluatorIndexID,
				aliasNameValue, notesValue
				
				);
	}		
}
