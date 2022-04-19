package builder.visframe.function.variable.output;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.output.type.TemporaryOutputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * select a target assigned to the host CompositionFunction that has not been assigned to 
 * any CFGTargetOutputVariableBuilder of EvaluatorBuilder of upstream ComponentFunctionBuilder or of the EvaluatorBuilders of host ComponentFunctionBuilder of this ComponentFunctionBuilder
 * 
 * @author tanxu
 *
 */
public final class TemporaryOutputVariableBuilder extends ValueTableColumnOutputVariableBuilder<TemporaryOutputVariable>{
	
	public TemporaryOutputVariableBuilder(
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

	protected static final String SQLDataType = "SQLDataType";
	protected static final String SQLDataType_description = "SQLDataType";
	
	private VfDefinedPrimitiveSQLDataTypeBuilder SQLDataTypeBuilder;
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		
		//SQLDataType
		SQLDataTypeBuilder = new VfDefinedPrimitiveSQLDataTypeBuilder(SQLDataType, SQLDataType_description, false, this, this.getDataTypeConstraints());
		this.addChildNodeBuilder(SQLDataTypeBuilder);
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
			SQLDataTypeBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				TemporaryOutputVariable temporaryOutputVariable = (TemporaryOutputVariable)value;
				SQLDataTypeBuilder.setValue(temporaryOutputVariable.getSQLDataType(), isEmpty);
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
	protected TemporaryOutputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		VfDefinedPrimitiveSQLDataType SQLDataTypeValue = (VfDefinedPrimitiveSQLDataType) this.getChildrenNodeBuilderNameMap().get(SQLDataType).getCurrentValue();
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		
		
		return new TemporaryOutputVariable(
//				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				hostCompositionFunctionID, hostComponentFunctionIndexID, hostEvaluatorIndexID,
				aliasNameValue, notesValue, SQLDataTypeValue
				);
	}	
}
