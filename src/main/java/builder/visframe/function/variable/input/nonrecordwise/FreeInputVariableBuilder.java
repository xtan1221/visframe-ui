package builder.visframe.function.variable.input.nonrecordwise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.basic.misc.SimpleTypeSelector;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.IndependentFreeInputVariableTypeSelector;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.input.nonrecordwise.type.FreeInputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * build a FreeInputVariable;
 * 
 * the contained IndependentFreeInputVariableType can be selected from existing ones in the host VisProjectDBContext or built from scratch;
 * 
 * note that the alias name of {@link FreeInputVariable} is different from the name of the underlying {@link IndependentFreeInputVariableType};
 * @author tanxu
 *
 */
public final class FreeInputVariableBuilder extends NonRecordwiseInputVariableBuilder<FreeInputVariable>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 * @param hostCompositionFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public FreeInputVariableBuilder(
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
	
	/**
	 * set the mode to build from scratch with the current IndependentFreeInputVariableType value;
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	public void setToBuildFromScratchModeWithCurrentIndependentFreeInputVariableType() throws SQLException, IOException {
		if(this.toSelectFromOtherCompositionFunctionBuilder.getCurrentValue()) {//already in build from scratch mode
			return;
		}else {//in select mode
			IndependentFreeInputVariableType type = this.indieFIVTypeFromOtherCompositionFunctionSelector.getCurrentValue();
			this.toSelectFromOtherCompositionFunctionBuilder.setValue(true, false);
			
			this.originalIndieFIVTypeSelector.setValue(type, false);
		}
	}
	
	/**
	 * set the mode to select with the current IndependentFreeInputVariableType value;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setToSelectModeWithCurrentIndependentFreeInputVariableType() throws SQLException, IOException {
		if(!this.toSelectFromOtherCompositionFunctionBuilder.getCurrentValue()) {//already in select mode;
			return;
		}else {//in build from scratch mode
			IndependentFreeInputVariableType type = this.originalIndieFIVTypeSelector.getCurrentValue();
			this.toSelectFromOtherCompositionFunctionBuilder.setValue(false, false);
			
			this.indieFIVTypeFromOtherCompositionFunctionSelector.setValue(type, false);
		}
	}
	
	////////////////////////////////////
	/**
	 * parameter indicating whether the underlying IndependentFreeInputVariableType should be built from scratch or selected from existing ones;
	 * 
	 * used to facilitate building a FreeInputVariable instance; but not a constructor parameter of FreeInputVariable class!!!!!!!
	 */
	protected static final String toSelectFromOtherCompositionFunction = "toSelectFromOtherCompositionFunction";
	protected static final String toSelectFromOtherCompositionFunction_description = "toSelectFromOtherCompositionFunction";
	
	protected static final String indieFIVTypeFromOtherCompositionFunction = "indieFIVTypeFromOtherCompositionFunction";
	protected static final String indieFIVTypeFromOtherCompositionFunction_description = "indieFIVTypeFromOtherCompositionFunction";
	
	protected static final String originalIndieFIVType = "originalIndieFIVType";
	protected static final String originalIndieFIVType_description = "originalIndieFIVType";
	
	
	////////////////////////
	private BooleanTypeBuilder toSelectFromOtherCompositionFunctionBuilder;
	
	//
	private IndependentFreeInputVariableTypeSelector indieFIVTypeFromOtherCompositionFunctionSelector;
	
	/**
	 * @return the independentFreeInputVariableTypeSelector
	 */
	public IndependentFreeInputVariableTypeSelector getIndependentFreeInputVariableTypeSelector() {
		return indieFIVTypeFromOtherCompositionFunctionSelector;
	}

	//
	private SimpleTypeSelector<IndependentFreeInputVariableType> originalIndieFIVTypeSelector;
	
	/**
	 * @return the originalIndieFIVTypeSelector
	 */
	public SimpleTypeSelector<IndependentFreeInputVariableType> getOriginalIndieFIVTypeSelector() {
		return originalIndieFIVTypeSelector;
	}

	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		/////////////////////////////
		//toBuildNewIndependentFreeInputVariableTypefromScratch
		toSelectFromOtherCompositionFunctionBuilder = new BooleanTypeBuilder(
				toSelectFromOtherCompositionFunction, 
				toSelectFromOtherCompositionFunction_description,
				false, this);
		this.addChildNodeBuilder(toSelectFromOtherCompositionFunctionBuilder);
		
		
		
		/////////////////////////////
		//independentFreeInputVariableTypeSelector and independentFreeInputVariableTypeBuilder should be added based on the current value of toBuildNewIndependentFreeInputVariableTypefromScratchBuilder;
		//see the {@link #addStatusChangeEventActionOfChildNodeBuilders}
		//still need to set the pool for host CompositionFunctionBuilder
		indieFIVTypeFromOtherCompositionFunctionSelector = new IndependentFreeInputVariableTypeSelector(
				indieFIVTypeFromOtherCompositionFunction, 
				indieFIVTypeFromOtherCompositionFunction_description,
				false, this,
				this,
				this.getDataTypeConstraints(),
				null
				);
//		indieFIVTypeFromOtherCompositionFunctionSelector.setHostCompositionFunctionBuilderTypesPool(new LinkedHashSet<>(this.getHostCompositionFunctionBuilder().getOriginalIndependentFreeInputVariableTypeNameMap().values()));
		
		//independentFreeInputVariableType_select
		//this.addChildNodeBuilder(this.independentFreeInputVariableTypeSelector);
		
		
		//independentFreeInputVariableType_new
		//do not add the independentFreeInputVariableTypeBuilder in initialization because the default value for toBuildNewIndependentFreeInputVariableTypefromScratch is false;
		originalIndieFIVTypeSelector = new SimpleTypeSelector<>(
				originalIndieFIVType,
				originalIndieFIVType_description,
				false, this,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSQLDataType().getSQLString());},//Function<T,String> typeToStringRepresentationFunction,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSQLDataType().getSQLString());}//Function<T,String> typeToDescriptionFunction
				);
		//initialize the pool with the current set of original IndependentFreeInputVariableTypes
		Set<IndependentFreeInputVariableType> pool = new LinkedHashSet<>();
		this.getHostCompositionFunctionBuilder().getOriginalIndependentFreeInputVariableTypeNameMap().values().forEach(e->{
			if(this.getDataTypeConstraints().test(e.getSQLDataType()))
				pool.add(e);
		});
		originalIndieFIVTypeSelector.setPool(pool);
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		//when toSelectFromOtherCompositionFunctionBuilder's status is changed
		//if true, add indieFIVTypeFromOtherCompositionFunctionSelector and remove originalIndieFIVTypeSelector
		//if false, add originalIndieFIVTypeSelector and remove indieFIVTypeFromOtherCompositionFunctionSelector
		
		Runnable toSelectFromOtherCompositionFunctionBuilderStatusChangeEventAction = ()->{
			try {
				if(toSelectFromOtherCompositionFunctionBuilder.getCurrentStatus().isDefaultEmpty()) {
					
				}else if(toSelectFromOtherCompositionFunctionBuilder.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					if(!toSelectFromOtherCompositionFunctionBuilder.getCurrentValue()) {//select from original ones
						if(this.getChildrenNodeBuilderNameMap().containsKey(indieFIVTypeFromOtherCompositionFunction)) {
							
							this.removeChildNodeBuilder(indieFIVTypeFromOtherCompositionFunction);
							
						}
						
						if(!this.getChildrenNodeBuilderNameMap().containsKey(originalIndieFIVType)) {
							this.addChildNodeBuilder(originalIndieFIVTypeSelector);
							//pool is updated in the {@link CompositionFunctionBuilder#update()} method whenever the getOriginalIndependentFreeInputVariableTypeNameMap() is changed
	//						originalIndieFIVTypeSelector.setPool(this.getHostCompositionFunctionBuilder().getOriginalIndependentFreeInputVariableTypeNameMap().values());
						}
						
					}else {//select from other existing composition functions
						if(this.getChildrenNodeBuilderNameMap().containsKey(originalIndieFIVType)) {
							this.removeChildNodeBuilder(originalIndieFIVType);
						}
						
						if(!this.getChildrenNodeBuilderNameMap().containsKey(indieFIVTypeFromOtherCompositionFunction)) {
							this.addChildNodeBuilder(indieFIVTypeFromOtherCompositionFunctionSelector);
						}
						
	//					if(this.getHostCompositionFunctionBuilder().getIndependentFreeInputVariableTypeSet().contains(this.independentFreeInputVariableTypeBuilder))
	//						this.getHostCompositionFunctionBuilder().getIndependentFreeInputVariableTypeSet().remove(this.independentFreeInputVariableTypeBuilder);
						
					}
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		toSelectFromOtherCompositionFunctionBuilder.addStatusChangedAction(
				toSelectFromOtherCompositionFunctionBuilderStatusChangeEventAction);
		//!!!!!!!!!!!!!!
		//initialize the builder's value to 'false'; this will trigger the above action to show the independentFreeInputVariableTypeSelector as the default 
		toSelectFromOtherCompositionFunctionBuilder.setValue(true, false);
	}
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.toSelectFromOtherCompositionFunctionBuilder.setValue(null, isEmpty);
			if(this.originalIndieFIVTypeSelector!=null)
				this.originalIndieFIVTypeSelector.setValue(null, isEmpty);
			if(this.indieFIVTypeFromOtherCompositionFunctionSelector!=null)
				this.indieFIVTypeFromOtherCompositionFunctionSelector.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				FreeInputVariable freeInputVariable = (FreeInputVariable)value;
				IndependentFreeInputVariableType independentFreeInputVariableType = freeInputVariable.getIndependentFreeInputVariableType();
				
				try {
					if(this.getHostVisProjectDBContext().getHasIDTypeManagerController().getIndependentFreeInputVariableTypeManager().lookup(independentFreeInputVariableType.getID())==null) {
						//the IndependentFreeInputVariableType is not present in the host VisProjectDBContext
						
//						if(this.getHostCompositionFunctionBuilder().getOriginalIndependentFIVTypeNameFirstFIVBuilderMap().get(independentFreeInputVariableType.getName())!=null
//								&&
//								AbstractVariableBuilder.areSameBuilders(this.getHostCompositionFunctionBuilder().getOriginalIndependentFIVTypeNameFirstFIVBuilderMap().get(independentFreeInputVariableType.getName()),this)) {
//							//this FreeInputVariableBuilder is the one that built the IndependentFreeInputVariableType from scratch
//							this.toSelectFromOtherCompositionFunctionBuilder.setValue(true, isEmpty);
//							this.originalIndieFIVTypeSelector.setValue(independentFreeInputVariableType, false);
//						}else {//
//							this.toSelectFromOtherCompositionFunctionBuilder.setValue(false, isEmpty);
//							this.indieFIVTypeFromOtherCompositionFunctionSelector.setValue(independentFreeInputVariableType, false);
//						}
						
						
						this.toSelectFromOtherCompositionFunctionBuilder.setValue(false, isEmpty);
						this.originalIndieFIVTypeSelector.setValue(independentFreeInputVariableType, false);
						
					}else {
						////the IndependentFreeInputVariableType already inserted into the host VisProjectDBContext, thus it is selected rather than built from scratch;
						this.toSelectFromOtherCompositionFunctionBuilder.setValue(true, isEmpty);
						this.indieFIVTypeFromOtherCompositionFunctionSelector.setValue(independentFreeInputVariableType, false);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(1);
				}
				
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
	protected FreeInputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		int hostComponentFunctionIndexID = this.getHostCompositionFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		IndependentFreeInputVariableType independentFreeInputVariableType;
		if(!this.toSelectFromOtherCompositionFunctionBuilder.getCurrentValue()) {
			independentFreeInputVariableType = this.originalIndieFIVTypeSelector.getCurrentValue();
		}else {
			independentFreeInputVariableType = this.indieFIVTypeFromOtherCompositionFunctionSelector.getCurrentValue();
		}
		
		return new FreeInputVariable(
//				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				hostCompositionFunctionID, hostComponentFunctionIndexID,hostEvaluatorIndexID,
				aliasNameValue, notesValue,
				independentFreeInputVariableType);
		
	}

	
	
}
