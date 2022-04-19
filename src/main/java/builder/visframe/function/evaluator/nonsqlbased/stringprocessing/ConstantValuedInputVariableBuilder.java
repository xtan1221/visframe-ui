package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.basic.primitive.StringTypeBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.input.nonrecordwise.type.ConstantValuedInputVariable;
import rdb.sqltype.SQLDataType;
import rdb.sqltype.SQLDataTypeFactory;
import rdb.sqltype.SQLStringType;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

/**
 * 
 * @author tanxu
 *
 */
public final class ConstantValuedInputVariableBuilder extends NonRecordwiseInputVariableBuilder<ConstantValuedInputVariable>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostEvaluatorBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public ConstantValuedInputVariableBuilder(
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
	
	protected static final String valueString = "valueString";
	protected static final String valueString_description = "valueString";
	
	////////////////////////
	private VfDefinedPrimitiveSQLDataTypeBuilder SQLDataTypeBuilder;
	
	private StringTypeBuilder valueStringBuilder;
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		/////////////////////////////

		//SQLDataType
		SQLDataTypeBuilder = new VfDefinedPrimitiveSQLDataTypeBuilder(SQLDataType, SQLDataType_description, false, this, this.getDataTypeConstraints());
		this.addChildNodeBuilder(SQLDataTypeBuilder);
		
		//valueString
		valueStringBuilder = new StringTypeBuilder(valueString, valueString_description, false, this);
		this.addChildNodeBuilder(valueStringBuilder);
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//given valueString must be consistent with the selected SQLDataType
		Set<String> set1 = new HashSet<>();
		set1.add(valueString);
		set1.add(SQLDataType);
		GenricChildrenNodeBuilderConstraint<ConstantValuedInputVariable> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "given valueString must be consistent with the selected SQLDataType!",
				set1, 
				e->{
					ConstantValuedInputVariableBuilder builder = (ConstantValuedInputVariableBuilder)e;
					SQLDataType dataType = builder.SQLDataTypeBuilder.getCurrentValue();
					String valueString = builder.valueStringBuilder.getCurrentValue();
					
					if(dataType.isGenericInt()) {//TODO int including INTEGER, BIGINT, SMALLINT, need to modify
						try {
							Integer.parseInt(valueString);
							return true;
						}catch(NumberFormatException ex) {
							return false;
						}
					}else if(dataType.equals(SQLDataTypeFactory.doubleType())){
						try {
							Double.parseDouble(valueString);
							return true;
						}catch(NumberFormatException ex) {
							return false;
						}
					}else if(dataType.equals(SQLDataTypeFactory.booleanType())) {
						return valueString.equalsIgnoreCase("true")||valueString.equalsIgnoreCase("false");
					}else if(dataType.isOfStringType()) {
						SQLStringType stringType = (SQLStringType)this.SQLDataTypeBuilder.getCurrentValue();
						return valueString.length()<=stringType.getMaxLength();
					}else {
						//?
						return false;//TODO
					}
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		// TODO Auto-generated method stub
	}
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			valueStringBuilder.setValue(null, isEmpty);
			SQLDataTypeBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				ConstantValuedInputVariable constantValuedInputVariable = (ConstantValuedInputVariable)value;
				valueStringBuilder.setValue(constantValuedInputVariable.getValueString(), isEmpty);
				SQLDataTypeBuilder.setValue(constantValuedInputVariable.getSQLDataType(), isEmpty);
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
	protected ConstantValuedInputVariable build() {
		
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		
		VfNotes notesValue = this.notesBuilder.getCurrentValue();
		VfDefinedPrimitiveSQLDataType SQLDataTypeValue = (VfDefinedPrimitiveSQLDataType) this.getChildrenNodeBuilderNameMap().get(SQLDataType).getCurrentValue();
		
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		/////
		String valueStringValue = valueStringBuilder.getCurrentValue();
		
		
		return new ConstantValuedInputVariable(
//				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				hostCompositionFunctionID, hostComponentFunctionIndexID, hostEvaluatorIndexID, 
				aliasNameValue, notesValue, SQLDataTypeValue,
				valueStringValue);
	}
}
