package builder.visframe.function.variable.input.recordwise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.basic.misc.SimpleTypeSelector;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.input.recordwise.type.UpstreamValueTableColumnOutputVariableInputVariable;
import function.variable.output.type.ValueTableColumnOutputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 *  
 * 
 * @author tanxu
 *
 */
public final class UpstreamValueTableColumnOutputVariableInputVariableBuilder extends RecordwiseInputVariableBuilder<UpstreamValueTableColumnOutputVariableInputVariable>{
	
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
	 * @param mustBeOfSameOwnerRecordDataWithHostCompositionFunction 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public UpstreamValueTableColumnOutputVariableInputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints,
				//targetRecordDataMetadataIDCondition
				e->{return e.equals(hostEvaluatorBuilder.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}); 
			
		
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	////////////////////////////////////
	protected static final String upstreamValueTableColumnOutputVariable = "upstreamValueTableColumnOutputVariable";
	protected static final String upstreamValueTableColumnOutputVariable_description = "upstreamValueTableColumnOutputVariable";
	
	////////////////////////
	private SimpleTypeSelector<ValueTableColumnOutputVariable> upstreamValueTableColumnOutputVariableSelector;
	
	
	
	/**
	 * @return the upstreamValueTableColumnOutputVariableSelector
	 */
	public SimpleTypeSelector<ValueTableColumnOutputVariable> getUpstreamValueTableColumnOutputVariableSelector() {
		return upstreamValueTableColumnOutputVariableSelector;
	}
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		
		/////////////////////////////
		//upstreamValueTableColumnOutputVariable
		upstreamValueTableColumnOutputVariableSelector = new SimpleTypeSelector<>(
				upstreamValueTableColumnOutputVariable, upstreamValueTableColumnOutputVariable_description, false, this, 
				e->{return "component function index=".concat(Integer.toString(e.getHostComponentFunctionIndexID())).concat(";evaluator index=").concat(Integer.toString(e.getHostEvaluatorIndexID())).concat(";name=").concat(e.getAliasName().getStringValue());},//Function<T,String> typeToStringRepresentationFunction,
				e->{return "name=".concat(e.getAliasName().getStringValue());}//Function<T,String> typeToDescriptionFunction
				);
		
		Set<ValueTableColumnOutputVariable> pool = new LinkedHashSet<>();
		this.getHostComponentFunctionBuilder().getUpstreamValueTableColumnOutputVariableBuilderSet().forEach(e->{
			if(this.getDataTypeConstraints().test(e.getCurrentValue().getSQLDataType()))
				pool.add(e.getCurrentValue());
		});
		this.upstreamValueTableColumnOutputVariableSelector.setPool(pool);
		this.addChildNodeBuilder(upstreamValueTableColumnOutputVariableSelector);
	
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
			this.upstreamValueTableColumnOutputVariableSelector.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				UpstreamValueTableColumnOutputVariableInputVariable upstreamValueTableColumnOutputVariableInputVariable = (UpstreamValueTableColumnOutputVariableInputVariable)value;
				
				this.upstreamValueTableColumnOutputVariableSelector.setValue(upstreamValueTableColumnOutputVariableInputVariable.getUpstreamValueTableColumnOutputVariable(), isEmpty);
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
	protected UpstreamValueTableColumnOutputVariableInputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		/////
		ValueTableColumnOutputVariable upstreamValueTableColumnOutputVariable = this.upstreamValueTableColumnOutputVariableSelector.getCurrentValue();
		
		return new UpstreamValueTableColumnOutputVariableInputVariable(
//				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				hostCompositionFunctionID, hostComponentFunctionIndexID, hostEvaluatorIndexID,
				aliasNameValue, notesValue,
				
				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),//
				upstreamValueTableColumnOutputVariable
				);
	}		

}
