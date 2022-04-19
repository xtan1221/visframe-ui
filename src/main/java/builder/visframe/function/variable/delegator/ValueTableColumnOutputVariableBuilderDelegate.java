package builder.visframe.function.variable.delegator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import builder.basic.misc.SimpleTypeSelector;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.output.CFGTargetOutputVariableBuilder;
import builder.visframe.function.variable.output.TemporaryOutputVariableBuilder;
import builder.visframe.function.variable.output.ValueTableColumnOutputVariableBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import function.variable.output.type.CFGTargetOutputVariable;
import function.variable.output.type.TemporaryOutputVariable;
import function.variable.output.type.ValueTableColumnOutputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * delegate to a AbstractVariableBuilder whose type is selected and can be changed on the UI;
 * 
 * step 1. select an input variable type;
 * step 2. build an input variable of the selected type;
 * 
 * @author tanxu
 * 
 */
public final class ValueTableColumnOutputVariableBuilderDelegate extends NonLeafNodeBuilder<ValueTableColumnOutputVariable>{	
	private final VisProjectDBContext hostVisProjectDBContext;
	private final CompositionFunctionBuilder hostCompositionFunctionBuilder;
	
	private final ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder;
	private final AbstractEvaluatorBuilder<?> hostEvaluatorBuilder; 
	
	private Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints;
	private SimpleName predefinedAliasName = null;
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
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public ValueTableColumnOutputVariableBuilderDelegate(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			CompositionFunctionBuilder hostCompositionFunctionBuilder,
			ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder,
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints
			) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.hostCompositionFunctionBuilder = hostCompositionFunctionBuilder;
		this.hostComponentFunctionBuilder = hostComponentFunctionBuilder;
		this.hostEvaluatorBuilder = hostEvaluatorBuilder;
		this.dataTypeConstraints = dataTypeConstraints;
		
		///////////////
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	/**
	 * @return the hostVisProjectDBContext
	 */
	protected VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	/**
	 * @return the hostCompositionFunctionBuilder
	 */
	protected CompositionFunctionBuilder getHostCompositionFunctionBuilder() {
		return hostCompositionFunctionBuilder;
	}
	/**
	 * @return the hostComponentFunctionBuilder
	 */
	protected ComponentFunctionBuilder<?, ?> getHostComponentFunctionBuilder() {
		return hostComponentFunctionBuilder;
	}
	/**
	 * @return the hostEvaluatorBuilder
	 */
	protected AbstractEvaluatorBuilder<?> getHostEvaluatorBuilder() {
		return hostEvaluatorBuilder;
	}

	/**
	 * @return the dataTypeConstraints
	 */
	public Predicate<VfDefinedPrimitiveSQLDataType> getDataTypeConstraints() {
		return dataTypeConstraints;
	}
	
	/**
	 * @param predefinedAliasName the predefinedAliasName to set
	 */
	public void setPredefinedAliasName(SimpleName predefinedAliasName) {
		this.predefinedAliasName = predefinedAliasName;
	}
	
	/**
	 * @return the predefinedAliasName
	 */
	public SimpleName getPredefinedAliasName() {
		return predefinedAliasName;
	}
	/**
	 * set the data type constraints and re-initialize this ValueTableColumnOutputVariableBuilderDelegate
	 * @param dataTypeConstraints the dataTypeConstraints to set
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setDataTypeConstraints(Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) throws SQLException, IOException {
		this.dataTypeConstraints = dataTypeConstraints;
		
		
		//
		this.removeAllChildrenNodeBuilders();
		
		
		//
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	/////////////////
	private Set<Class<? extends ValueTableColumnOutputVariable>> getTypeSet(){
		Set<Class<? extends ValueTableColumnOutputVariable>> ret = new LinkedHashSet<>();
		
		ret.add(CFGTargetOutputVariable.class);
		ret.add(TemporaryOutputVariable.class);
		
		return ret;
	}

	///////////////////
	protected static final String valueTableColumnOutputVariableType = "valueTableColumnOutputVariableType";
	protected static final String valueTableColumnOutputVariableType_description = "valueTableColumnOutputVariableType";
	
	protected static final String valueTableColumnOutputVariable = "valueTableColumnOutputVariable";
	protected static final String valueTableColumnOutputVariable_description = "valueTableColumnOutputVariable";
	
	
	private SimpleTypeSelector<Class<? extends ValueTableColumnOutputVariable>> valueTableColumnOutputVariableTypeSelector;
	private ValueTableColumnOutputVariableBuilder<?> valueTableColumnOutputVariableBuilder;
	
	/**
	 * @return the valueTableColumnOutputVariableBuilder
	 */
	public ValueTableColumnOutputVariableBuilder<?> getValueTableColumnOutputVariableBuilder() {
		return valueTableColumnOutputVariableBuilder;
	}
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		
		//valueTableColumnOutputVariableType
		valueTableColumnOutputVariableTypeSelector = new SimpleTypeSelector<>(
				valueTableColumnOutputVariableType, valueTableColumnOutputVariableType_description, false, this,
				e->{return e.getSimpleName();},
				e->{return e.getSimpleName();}
				);
		this.valueTableColumnOutputVariableTypeSelector.setPool(this.getTypeSet());
		this.addChildNodeBuilder(valueTableColumnOutputVariableTypeSelector);
		
		//valueTableColumnOutputVariable
		//not initialized here, but initialized to the corresponding Variable builder after a valid type of Variable is selected with valueTableColumnOutputVariableTypeSelector; 
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		
		//when valueTableColumnOutputVariableTypeSelector's status is changed
		//valueTableColumnOutputVariableBuilder is set to the corresponding sub-type of AbstractVariableBuilder
		Runnable valueTableColumnOutputVariableTypeSelectorStatusChangeEventAction = ()->{
			try {
				if(valueTableColumnOutputVariableTypeSelector.getCurrentStatus().isDefaultEmpty()) {
					//need to remove the inputVariableBuilder if no type is selected;
					if(this.valueTableColumnOutputVariableBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.valueTableColumnOutputVariableBuilder.getName())) {
						
						this.removeChildNodeBuilder(this.valueTableColumnOutputVariableBuilder.getName());
						
					}
					this.valueTableColumnOutputVariableBuilder = null;
				}else if(valueTableColumnOutputVariableTypeSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					//remove it first
					if(this.valueTableColumnOutputVariableBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.valueTableColumnOutputVariableBuilder.getName()))
						this.removeChildNodeBuilder(this.valueTableColumnOutputVariableBuilder.getName());
					
					//create a corresponding builder to the selected type;
					if(this.valueTableColumnOutputVariableTypeSelector.getCurrentValue().equals(CFGTargetOutputVariable.class)) {
						this.valueTableColumnOutputVariableBuilder = new CFGTargetOutputVariableBuilder(
								valueTableColumnOutputVariable, valueTableColumnOutputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints());
						
						//whenever a CFGTargetOutputVariable's status changes, update the full tree
						//this is for evaluator types with multiple output variables assigned to CFGTargets
						this.valueTableColumnOutputVariableBuilder.addStatusChangedAction(()->{
							try {
								this.getHostCompositionFunctionBuilder().update();
							} catch (SQLException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
						
					}else if(this.valueTableColumnOutputVariableTypeSelector.getCurrentValue().equals(TemporaryOutputVariable.class)) {
						this.valueTableColumnOutputVariableBuilder = new TemporaryOutputVariableBuilder(
								valueTableColumnOutputVariable, valueTableColumnOutputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints());
					}else {
						//
					}
					
					this.addChildNodeBuilder(this.valueTableColumnOutputVariableBuilder);
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		valueTableColumnOutputVariableTypeSelector.addStatusChangedAction(
				valueTableColumnOutputVariableTypeSelectorStatusChangeEventAction);
	}
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.valueTableColumnOutputVariableTypeSelector.setValue(null, isEmpty);
			
			if(this.valueTableColumnOutputVariableBuilder!=null) {
				if(this.getChildrenNodeBuilderNameMap().containsKey(this.valueTableColumnOutputVariableBuilder.getName()))
					this.getChildrenNodeBuilderNameMap().remove(this.valueTableColumnOutputVariableBuilder.getName());
				
				this.valueTableColumnOutputVariableBuilder = null;
			}
			
			
		}else {
			if(value==null) {
				
				this.setToNull();
			}else {
				ValueTableColumnOutputVariable valueTableColumnOutputVariable = (ValueTableColumnOutputVariable)value;
				
				//this will trigger the action that create a corresponding subtype of variable builder and assign to inputVariableBuilder;
				this.valueTableColumnOutputVariableTypeSelector.setValue(valueTableColumnOutputVariable.getClass(), isEmpty);
				//
				this.valueTableColumnOutputVariableBuilder.setValue(valueTableColumnOutputVariable, isEmpty);
			}
		}
		return changed;
	}
	
	
	@Override
	protected ValueTableColumnOutputVariable build() {
		return this.valueTableColumnOutputVariableBuilder.getCurrentValue();
	}
	
}
