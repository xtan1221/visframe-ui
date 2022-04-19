package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.list.ArrayListNonLeafNodeBuilder;
import builder.basic.primitive.StringTypeBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegateFactory;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.StringConcatenationEvaluator;
import function.variable.input.InputVariable;

/**
 * 
 * @author tanxu
 *
 */
public final class StringConcatenationEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringConcatenationEvaluator>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostComponentFunctionBuilder
	 * @param indexID
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public StringConcatenationEvaluatorBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder, int indexID) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	//////////////////////////////////
	@Override
	public Set<InputVariableBuilder<?>> getInputVariableBuilderSet() throws SQLException, IOException {
		Set<InputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		//
		this.concatenatedInputVariableListBuilder.getNodeBuilderSet().forEach(e->{
			if(e!=null) {
				InputVariableBuilderDelegate delegate = (InputVariableBuilderDelegate)e;
				if(delegate.getInputVariableBuilder()!=null) {
					ret.add(delegate.getInputVariableBuilder());
				}
			}
			
		});
		
		
		if(!this.concatenatingStringInputVariableBuilderDelegate.isSetToNull())
			if(this.concatenatingStringInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
				ret.add(this.concatenatingStringInputVariableBuilderDelegate.getInputVariableBuilder());
		
		
		return ret;
	}

	
	@Override
	public Set<OutputVariableBuilder<?>> getOutputVariableBuilderSet() {
		Set<OutputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		if(this.outputVariableBuilderDelegate.getValueTableColumnOutputVariableBuilder()!=null)
			ret.add(this.outputVariableBuilderDelegate.getValueTableColumnOutputVariableBuilder());
		
		return ret;
	}
	
	///////////////////////////////////////////
	protected static final String concatenatedInputVariableList = "concatenatedInputVariableList";
	protected static final String concatenatedInputVariableList_description = "concatenatedInputVariableList";

	protected static final String concatenatingStringInputVariable = "concatenatingStringInputVariable";
	protected static final String concatenatingStringInputVariable_description = "concatenatingStringInputVariable";

	protected static final String defaultConcatenatingString = "defaultConcatenatingString";
	protected static final String defaultConcatenatingString_description = "defaultConcatenatingString";
	
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";
	
	
	//////////////////////////
	//concatenatedInputVariableList
	private ArrayListNonLeafNodeBuilder<InputVariable> concatenatedInputVariableListBuilder;
	
	//concatenatingStringInputVariable
	private InputVariableBuilderDelegate concatenatingStringInputVariableBuilderDelegate;
	
	//defaultConcatenatingString
	private StringTypeBuilder defaultConcatenatingStringBuilder;
	
	//must be of string type
	private ValueTableColumnOutputVariableBuilderDelegate outputVariableBuilderDelegate;
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//concatenatedInputVariableList
		InputVariableBuilderDelegateFactory inputVariableBuilderDelegateFactory = new InputVariableBuilderDelegateFactory(
				"inputVariable","inputVariable",false, 
				this.getHostVisProjectDBContext(), //VisProjectDBContext hostVisProjectDBContext,
				this.getHostCompositionFunctionBuilder(),//CompositionFunctionBuilder hostCompositionFunctionBuilder,
				this.getHostComponentFunctionBuilder(),//ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder,
				this,//hostEvaluatorBuilder
				e->{return true;},
				true,//allowingNonRecordwiseInputVariableTypes
				true,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				true,//boolean allowingConstantValuedInputVariable
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.concatenatedInputVariableListBuilder = new ArrayListNonLeafNodeBuilder<>(
				concatenatedInputVariableList, concatenatedInputVariableList_description, false, this,
				inputVariableBuilderDelegateFactory, false//boolean duplicateElementAllowed
				);
		this.addChildNodeBuilder(this.concatenatedInputVariableListBuilder);
		
		//concatenatingStringInputVariable
		concatenatingStringInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
				concatenatingStringInputVariable, concatenatingStringInputVariable_description, true, this, //can be null
				this,
				e->{return true;},
				true,//allowingNonRecordwiseInputVariableTypes
				true,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				true, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.addChildNodeBuilder(this.concatenatingStringInputVariableBuilderDelegate);
		
		//defaultConcatenatingString
		defaultConcatenatingStringBuilder = new StringTypeBuilder(
					defaultConcatenatingString, defaultConcatenatingString_description, true, this); //can be null;
		this.addChildNodeBuilder(this.defaultConcatenatingStringBuilder);
		
		//outputVariable
		outputVariableBuilderDelegate = new ValueTableColumnOutputVariableBuilderDelegate(
				outputVariable, outputVariable_description, false, this, 
				this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
				e->{return e.isOfStringType();}//must be of string type
				);
		this.addChildNodeBuilder(this.outputVariableBuilderDelegate);
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//concatenatedInputVariableList must be non-empty;
		Set<String> set1 = new HashSet<>();
		set1.add(concatenatedInputVariableList);
		GenricChildrenNodeBuilderConstraint<StringConcatenationEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "concatenatedInputVariableList must be non-empty!",
				set1, 
				e->{
					StringConcatenationEvaluatorBuilder builder = (StringConcatenationEvaluatorBuilder)e;
					return !builder.concatenatedInputVariableListBuilder.getCurrentValue().isEmpty();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//concatenatingInputVariable and defaultConcatenatingString cannot both be null;
		Set<String> set2 = new HashSet<>();
		set2.add(concatenatingStringInputVariable);
		set2.add(defaultConcatenatingString);
		GenricChildrenNodeBuilderConstraint<StringConcatenationEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "concatenatingInputVariable and defaultConcatenatingString cannot both be null!",
				set2, 
				e->{
					StringConcatenationEvaluatorBuilder builder = (StringConcatenationEvaluatorBuilder)e;
					return !(builder.concatenatingStringInputVariableBuilderDelegate.getCurrentValue()==null && builder.defaultConcatenatingStringBuilder.getCurrentValue()==null);
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		

		
		//input variables must have different alias names;
		Set<String> set3 = new HashSet<>();
		set3.add(concatenatedInputVariableList);
		set3.add(concatenatingStringInputVariable);
		GenricChildrenNodeBuilderConstraint<StringConcatenationEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "input variables must have different alias names!",
				set3, 
				e->{
					StringConcatenationEvaluatorBuilder builder = (StringConcatenationEvaluatorBuilder)e;
					
					Set<SimpleName> aliasNameSet = new HashSet<>();
					//concatenatedInputVariableListBuilder
					for(InputVariable iv:builder.concatenatedInputVariableListBuilder.getCurrentValue()){
						if(aliasNameSet.contains(iv.getAliasName())) {
							return false;
						}else {
							aliasNameSet.add(iv.getAliasName());
						}
					}
					
					//concatenatingStringInputVariableBuilderDelegate
					if(builder.concatenatingStringInputVariableBuilderDelegate.getCurrentStatus().hasValidValue()) {
						return !aliasNameSet.contains(builder.concatenatingStringInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					}else {
						return true;
					}
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
		
		
		
		//selected outputVariable must be of string type;
		Set<String> set4 = new HashSet<>();
		set4.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<StringConcatenationEvaluator> c4 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of string type!",
				set4, 
				e->{
					StringConcatenationEvaluatorBuilder builder = (StringConcatenationEvaluatorBuilder)e;
					return builder.outputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isOfStringType();
				});
		this.addGenricChildrenNodeBuilderConstraint(c4);
	
		
	}


	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		// TODO Auto-generated method stub
		
	}
	
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.concatenatedInputVariableListBuilder.setValue(null, isEmpty);
			this.concatenatingStringInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.defaultConcatenatingStringBuilder.setValue(null, isEmpty);
			this.outputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringConcatenationEvaluator stringConcatenationEvaluator = (StringConcatenationEvaluator)value;
				this.concatenatedInputVariableListBuilder.setValue(stringConcatenationEvaluator.getConcatenatedInputVariableList(), isEmpty);
				this.concatenatingStringInputVariableBuilderDelegate.setValue(stringConcatenationEvaluator.getConcatenatingStringInputVariable(), isEmpty);
				this.defaultConcatenatingStringBuilder.setValue(stringConcatenationEvaluator.getDefaultConcatenatingString(), isEmpty);
				this.outputVariableBuilderDelegate.setValue(stringConcatenationEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringConcatenationEvaluator build() {
		return new StringConcatenationEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.concatenatedInputVariableListBuilder.getCurrentValue(),
				this.concatenatingStringInputVariableBuilderDelegate.getCurrentValue(),
				this.defaultConcatenatingStringBuilder.getCurrentValue(),
				this.outputVariableBuilderDelegate.getCurrentValue()
				);
	}

	
}
