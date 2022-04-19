package builder.visframe.symja;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;

import com.google.common.base.Objects;

import builder.basic.collection.map.leaf.FixedKeySetMapValueBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilderFactory;
import core.builder.NonLeafNodeBuilder;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;
import symja.VfSymjaSinglePrimitiveOutputExpression;
import symja.VfSymjaVariableName;


public final class VfSymjaSinglePrimitiveOutputExpressionBuilder extends NonLeafNodeBuilder<VfSymjaSinglePrimitiveOutputExpression>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public VfSymjaSinglePrimitiveOutputExpressionBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	////////////////////////////////////
	/**
	 * parameter indicating whether the underlying IndependentFreeInputVariableType should be built from scratch or selected from existing ones;
	 * 
	 * used to facilitate building a FreeInputVariable instance; but not a constructor parameter of FreeInputVariable class!!!!!!!
	 */
	protected static final String sqlDataType = "sqlDataType";
	protected static final String sqlDataType_description = "sqlDataType";
	
	protected static final String expressionString = "expressionString";
	protected static final String expressionString_description = "expressionString";
	
	protected static final String variableNameSQLDataTypeMap = "variableNameSQLDataTypeMap";
	protected static final String variableNameSQLDataTypeMap_description = "variableNameSQLDataTypeMap";
	
	
	////////////////////////
	private VfDefinedPrimitiveSQLDataTypeBuilder sqlDataTypeBuilder;
	//
	private VfSymjaExpressionStringBuilder expressionStringBuilder;
	//
	private FixedKeySetMapValueBuilder<VfSymjaVariableName, VfDefinedPrimitiveSQLDataType> variableNameSQLDataTypeMapBuilder;
	
	/**
	 * @return the sqlDataTypeBuilder
	 */
	public VfDefinedPrimitiveSQLDataTypeBuilder getSqlDataTypeBuilder() {
		return sqlDataTypeBuilder;
	}

	/**
	 * @return the expressionStringBuilder
	 */
	public VfSymjaExpressionStringBuilder getExpressionStringBuilder() {
		return expressionStringBuilder;
	}
	
	/**
	 * @return the variableNameSQLDataTypeMapBuilder
	 */
	public FixedKeySetMapValueBuilder<VfSymjaVariableName, VfDefinedPrimitiveSQLDataType> getVariableNameSQLDataTypeMapBuilder() {
		return variableNameSQLDataTypeMapBuilder;
	}

	//////////////////////////////////////////////
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		
		/////////////////////////////
		//sqlDataTypeBuilder
		sqlDataTypeBuilder = new VfDefinedPrimitiveSQLDataTypeBuilder(
				sqlDataType, sqlDataType_description, false, this, e->{return true;});
		this.addChildNodeBuilder(sqlDataTypeBuilder);
		
		//expressionStringBuilder
		expressionStringBuilder = new VfSymjaExpressionStringBuilder(
				expressionString, expressionString_description, false, this
				);
		this.addChildNodeBuilder(this.expressionStringBuilder);
		
		
		
		//variableNameSQLDataTypeMapBuilder
		VfDefinedPrimitiveSQLDataTypeBuilderFactory SQLDataTypeBuilderFactory = new VfDefinedPrimitiveSQLDataTypeBuilderFactory("variableSQLDataType","variableSQLDataType",false, e->{return true;});
		this.variableNameSQLDataTypeMapBuilder = new FixedKeySetMapValueBuilder<>(
				variableNameSQLDataTypeMap, variableNameSQLDataTypeMap_description, false, this,
				SQLDataTypeBuilderFactory,
				e->{return e.getValue();},
				e->{return "VfSymjaVariableName";},
				null,
				false, //boolean allowingNullValue
				true //boolean allowingDuplicateValue
				);
		this.addChildNodeBuilder(this.variableNameSQLDataTypeMapBuilder);
		
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//1. when expressionStringBuilder status changes, variableNameSQLDataTypeMapBuilder need to be updated accordingly
		Runnable expressionStringBuilderStatusChangeEventAction = ()->{
			try {
				if(expressionStringBuilder.getCurrentStatus().isDefaultEmpty()) {
					this.variableNameSQLDataTypeMapBuilder.setKeySet(new HashSet<>());
					
					this.variableNameSQLDataTypeMapBuilder.setToDefaultEmpty();
					
				}else if(expressionStringBuilder.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					//if variable name set is changed from previous
					if(!Objects.equal(this.variableNameSQLDataTypeMapBuilder.getKeySet(), this.expressionStringBuilder.getCurrentValue().getVfSymjaVariableNameSet())) {
						this.variableNameSQLDataTypeMapBuilder.setKeySet(this.expressionStringBuilder.getCurrentValue().getVfSymjaVariableNameSet());
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		};
		
		expressionStringBuilder.addStatusChangedAction(
				expressionStringBuilderStatusChangeEventAction);
		//
		
	}
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.sqlDataTypeBuilder.setValue(null, isEmpty);
			this.expressionStringBuilder.setValue(null, isEmpty);
			this.variableNameSQLDataTypeMapBuilder.setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				VfSymjaSinglePrimitiveOutputExpression vfSymjaSinglePrimitiveOutputExpression = (VfSymjaSinglePrimitiveOutputExpression)value;
				
				this.sqlDataTypeBuilder.setValue(vfSymjaSinglePrimitiveOutputExpression.getSqlDataType(),false);
				this.expressionStringBuilder.setValue(vfSymjaSinglePrimitiveOutputExpression.getExpressionString(),false);
				//do not need to invoke the setKeySet() method!!!!!
//				this.variableNameSQLDataTypeMapBuilder.setKeySet(vfSymjaSinglePrimitiveOutputExpression.getVariableNameSQLDataTypeMap().keySet());
				this.variableNameSQLDataTypeMapBuilder.setValue(vfSymjaSinglePrimitiveOutputExpression.getVariableNameSQLDataTypeMap(),false);
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
	protected VfSymjaSinglePrimitiveOutputExpression build() {
		
		return new VfSymjaSinglePrimitiveOutputExpression(
				this.sqlDataTypeBuilder.getCurrentValue(),
				this.expressionStringBuilder.getCurrentValue(),
				this.variableNameSQLDataTypeMapBuilder.getCurrentValue());
		
	}
	
	
	
}
