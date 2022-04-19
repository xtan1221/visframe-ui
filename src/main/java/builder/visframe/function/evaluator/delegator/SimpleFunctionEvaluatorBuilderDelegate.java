package builder.visframe.function.evaluator.delegator;

import java.io.IOException;
import java.sql.SQLException;

import builder.basic.misc.SimpleTypeSelector;
import builder.visframe.function.component.SimpleFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.RecordwiseInputVariableIsNullValuedEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.SimpleRNGEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.SymjaExpressionEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringCaseChangeEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringConcatenationEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringContainsSubstringEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringEndsWithEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringEqualsEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringLengthEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringRemoveEmptySpaceEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringReplaceEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringSplitToTwoBySplitterStringEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringStartsWithEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.StringSubstringEvaluatorBuilder;
import builder.visframe.function.evaluator.sqlbased.SimpleSqlQueryEvaluatorBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.Evaluator;
import function.evaluator.nonsqlbased.RecordwiseInputVariableIsNullValuedEvaluator;
import function.evaluator.nonsqlbased.SymjaExpressionEvaluator;
import function.evaluator.nonsqlbased.rng.RNGEvaluator;
import function.evaluator.nonsqlbased.rng.SimpleRNGEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.SimpleStringProcessingEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringCaseChangeEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringConcatenationEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringContainsSubstringEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringEndsWithEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringEqualsEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringLengthEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringRemoveEmptySpaceEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringReplaceEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringSplitToTwoBySplitterStringEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringStartsWithEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringSubstringEvaluator;
import function.evaluator.sqlbased.SimpleSQLQueryEvaluator;
import function.evaluator.sqlbased.SQLQueryBasedEvaluator;

/**
 * delegate to a builder for an evaluator for SimpleFunction;
 * 
 * all types of evaluators can be used for SimpleFunction;
 * 
 * the only constraints is that the output variable types of such evaluators must be of ValueTableColumnOutputVariable type;
 * 
 * ===================
 * core functionalities:
 * 1. select a type of evaluator;
 * 2. create an evaluator with the evaluator builder of the selected type;
 * 
 * 
 * @author tanxu
 * 
 */
public final class SimpleFunctionEvaluatorBuilderDelegate extends NonLeafNodeBuilder<Evaluator>{
	
	private final SimpleFunctionBuilder hostSimpleFunctionBuilder;
	private final int indexID;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostSimpleFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SimpleFunctionEvaluatorBuilderDelegate(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder,
			
			SimpleFunctionBuilder hostSimpleFunctionBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.hostSimpleFunctionBuilder = hostSimpleFunctionBuilder;
		this.indexID = this.hostSimpleFunctionBuilder.getNextAvailableEvaluatorIndexID();
		
		///////////////
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	/**
	 * @return the hostVisProjectDBContext
	 */
	protected VisProjectDBContext getHostVisProjectDBContext() {
		return this.getHostSimpleFunctionBuilder().getHostCompositionFunctionBuilder().getHostVisProjectDBContext();
	}
	
	/**
	 * @return the hostCompositionFunctionBuilder
	 */
	protected CompositionFunctionBuilder getHostCompositionFunctionBuilder() {
		return this.getHostSimpleFunctionBuilder().getHostCompositionFunctionBuilder();
	}
	
	/**
	 * @return the hostSimpleFunctionBuilder
	 */
	protected SimpleFunctionBuilder getHostSimpleFunctionBuilder() {
		return hostSimpleFunctionBuilder;
	}


	public int getIndexID() {
		return indexID;
	}
	///////////////////
	protected static final String evaluatorType = "evaluatorType";
	protected static final String evaluatorType_description = "evaluatorType";
	
	protected static final String evaluator = "evaluator";
	protected static final String evaluator_description = "evaluator";
	
	
	private SimpleTypeSelector<Class<? extends Evaluator>> evaluatorTypeSelector;
	private AbstractEvaluatorBuilder<?> evaluatorBuilder;
	

	/**
	 * @return the evaluatorBuilder
	 */
	public AbstractEvaluatorBuilder<?> getEvaluatorBuilder() {
		return evaluatorBuilder;
	}
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		
		//evaluatorType
		evaluatorTypeSelector = new SimpleTypeSelector<>(
				evaluatorType, evaluatorType_description, false, this,
				e->{return e.getSimpleName();},
				e->{return e.getSimpleName();}
				);
		this.evaluatorTypeSelector.setPool(EvaluatorTypes.getAllEvaluatorTypes());
		this.addChildNodeBuilder(this.evaluatorTypeSelector);
		
		
		
		//evaluator
		//not initialized here, but initialized to the corresponding evaluator builder after a valid type of evaluator is selected with evaluatorTypeSelector; 
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		
		//when evaluatorTypeSelector's status is changed
		//evaluatorBuilder is set to the corresponding sub-type of AbstractEvaluatorBuilder
		Runnable evaluatorTypeSelectorStatusChangeEventAction = ()->{
			try {
				if(this.evaluatorTypeSelector.getCurrentStatus().isDefaultEmpty()) {
					//need to remove the inputVariableBuilder if no type is selected;
					if(this.evaluatorBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.evaluatorBuilder.getName())) {
						
							this.removeChildNodeBuilder(this.evaluatorBuilder.getName());
						
					}
					
					this.evaluatorBuilder = null;
				}else if(evaluatorTypeSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					//remove it first
					if(this.evaluatorBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.evaluatorBuilder.getName())) {
						this.removeChildNodeBuilder(this.evaluatorBuilder.getName());
					}
					
					//create a corresponding builder to the selected type;
					//////SimpleStringProcessingEvaluator types
					if(SimpleStringProcessingEvaluator.class.isAssignableFrom(this.evaluatorTypeSelector.getCurrentValue())) {
						if(this.evaluatorTypeSelector.getCurrentValue().equals(StringCaseChangeEvaluator.class)) {
							this.evaluatorBuilder = new StringCaseChangeEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringConcatenationEvaluator.class)) {
							this.evaluatorBuilder = new StringConcatenationEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringContainsSubstringEvaluator.class)) {
							this.evaluatorBuilder = new StringContainsSubstringEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID,
									false);//boolean forPiecewiseFunctionCondition
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringEndsWithEvaluator.class)) {
							this.evaluatorBuilder = new StringEndsWithEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID,
									false);//boolean forPiecewiseFunctionCondition
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringEqualsEvaluator.class)) {
							this.evaluatorBuilder = new StringEqualsEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID,
									false);//boolean forPiecewiseFunctionCondition
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringLengthEvaluator.class)) {
							this.evaluatorBuilder = new StringLengthEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringRemoveEmptySpaceEvaluator.class)) {
							this.evaluatorBuilder = new StringRemoveEmptySpaceEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringReplaceEvaluator.class)) {
							this.evaluatorBuilder = new StringReplaceEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringSplitToTwoBySplitterStringEvaluator.class)) {
							this.evaluatorBuilder = new StringSplitToTwoBySplitterStringEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringStartsWithEvaluator.class)) {
							this.evaluatorBuilder = new StringStartsWithEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID,
									false);//boolean forPiecewiseFunctionCondition
						}else if(this.evaluatorTypeSelector.getCurrentValue().equals(StringSubstringEvaluator.class)) {
							this.evaluatorBuilder = new StringSubstringEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}
					}else if(RNGEvaluator.class.isAssignableFrom(this.evaluatorTypeSelector.getCurrentValue())){//RNGEvaluator type
						if(this.evaluatorTypeSelector.getCurrentValue().equals(SimpleRNGEvaluator.class)) {
							this.evaluatorBuilder = new SimpleRNGEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID);
						}
					}else if(SymjaExpressionEvaluator.class.isAssignableFrom(this.evaluatorTypeSelector.getCurrentValue())) {//SymjaExpressionEvaluator types
						if(this.evaluatorTypeSelector.getCurrentValue().equals(SymjaExpressionEvaluator.class)) {
							this.evaluatorBuilder = new SymjaExpressionEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID,
									false);//boolean forPiecewiseFunctionCondition
						}
					}else if(RecordwiseInputVariableIsNullValuedEvaluator.class.isAssignableFrom(this.evaluatorTypeSelector.getCurrentValue())) {//SymjaExpressionEvaluator types
						if(this.evaluatorTypeSelector.getCurrentValue().equals(RecordwiseInputVariableIsNullValuedEvaluator.class)) {
							this.evaluatorBuilder = new RecordwiseInputVariableIsNullValuedEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID,
									false);//boolean forPiecewiseFunctionCondition
						}
					}else if(SQLQueryBasedEvaluator.class.isAssignableFrom(this.evaluatorTypeSelector.getCurrentValue())) {//SqlQueryBasedEvaluator types
						if(this.evaluatorTypeSelector.getCurrentValue().equals(SimpleSQLQueryEvaluator.class)) {
							this.evaluatorBuilder = new SimpleSqlQueryEvaluatorBuilder(
									evaluator, evaluator_description, false, this,
									this.getHostSimpleFunctionBuilder(), this.indexID,
									false);//boolean forPiecewiseFunctionCondition
						}
					}
					
					
					//add to parent node builder after the evaluatorBuilder is initialized again;
					this.addChildNodeBuilder(this.evaluatorBuilder);
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		evaluatorTypeSelector.addStatusChangedAction(
				evaluatorTypeSelectorStatusChangeEventAction);
	}
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.evaluatorTypeSelector.setValue(null, isEmpty);
			
			if(this.evaluatorBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.evaluatorBuilder.getName())) {
				this.removeChildNodeBuilder(this.evaluatorBuilder.getName());
			}
			this.evaluatorBuilder = null;
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				Evaluator evaluator = (Evaluator)value;
				
				//this will trigger the action that create a corresponding subtype of variable builder and assign to inputVariableBuilder;
				this.evaluatorTypeSelector.setValue(evaluator.getClass(), isEmpty);
				//
				this.evaluatorBuilder.setValue(evaluator, isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected Evaluator build() {
		return this.evaluatorBuilder.getCurrentValue();
	}

	
	
}
