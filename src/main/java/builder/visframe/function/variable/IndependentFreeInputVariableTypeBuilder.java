package builder.visframe.function.variable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.independent.IndependentFreeInputVariableTypeImpl;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * builder for a IndependentFreeInputVariableTypeImpl instance from scratch belonging to the host CompositionFunction during building process of a CompositionFunction;
 * 
 * @author tanxu
 *
 */
public final class IndependentFreeInputVariableTypeBuilder extends NonLeafNodeBuilder<IndependentFreeInputVariableType>{
	private final CompositionFunctionBuilder ownerCompositionFunctionBuilder;
	
	private final Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param ownerCompositionFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public IndependentFreeInputVariableTypeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			CompositionFunctionBuilder ownerCompositionFunctionBuilder,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.ownerCompositionFunctionBuilder = ownerCompositionFunctionBuilder;
		this.dataTypeConstraints = dataTypeConstraints;
		///////////////
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	protected CompositionFunctionBuilder getOwnerCompositionFunctionBuilder() {
		return ownerCompositionFunctionBuilder;
	}
	/**
	 * @return the dataTypeConstraints
	 */
	protected Predicate<VfDefinedPrimitiveSQLDataType> getDataTypeConstraints() {
		return dataTypeConstraints;
	}
	
//	/**
//	 * @param dataTypeConstraints the dataTypeConstraints to set
//	 */
//	public void setDataTypeConstraints(Predicate<SQLDataType> dataTypeConstraints) {
//		this.dataTypeConstraints = dataTypeConstraints;
//	}
	////////////////////////////////////////
	protected static final String name = "name";
	protected static final String name_description = "name";
	
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	protected static final String SQLDataType = "SQLDataType";
	protected static final String SQLDataType_description = "SQLDataType";
	
	//////////////////
	private VfNameStringBuilder<SimpleName> aliasNameBuilder;
	
	private VfNotesBuilder notesBuilder;
	
	private VfDefinedPrimitiveSQLDataTypeBuilder SQLDataTypeBuilder;
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//name
		aliasNameBuilder = new VfNameStringBuilder<>(
				name, name_description,
				false, this, SimpleName.class);
		this.addChildNodeBuilder(aliasNameBuilder);
		
		//notes,
		notesBuilder = new VfNotesBuilder(
				notes, notes_description, 
				false, this);
		this.addChildNodeBuilder(notesBuilder);
		
		//SQLDataType
		SQLDataTypeBuilder = new VfDefinedPrimitiveSQLDataTypeBuilder(
				SQLDataType, SQLDataType_description,
				false, this, this.getDataTypeConstraints());
		this.addChildNodeBuilder(SQLDataTypeBuilder);
		
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		
		//alias name of the IndependentFreeInputVariableType is duplicate with existing one; must be different from all other IndependentFreeInputVariableTypes
		Set<String> set1 = new HashSet<>();
		set1.add(name);
		GenricChildrenNodeBuilderConstraint<IndependentFreeInputVariableType> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "alias name of the IndependentFreeInputVariableType is duplicate with existing one; must be different from all other IndependentFreeInputVariableTypes!",
				set1,
				e->{
					IndependentFreeInputVariableTypeBuilder parentbuilder = (IndependentFreeInputVariableTypeBuilder)e;
					
					return !parentbuilder.ownerCompositionFunctionBuilder.getOriginalIndependentFreeInputVariableTypeNameMap().containsKey(aliasNameBuilder.getCurrentValue());
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		
		
//		//when aliasName's status is changed, 
//		//if not null and not default empty, check if the name is duplicate with any existing ones in the ownerCompositionFunctionBuilder's independentFreeInputVariableTypeNameBuilderMap
//		Runnable aliasNameBuilderStatusChangeEventAction = ()->{
//			if(aliasNameBuilder.getCurrentStatus().isDefaultEmpty()) {
//				
//			}else if(aliasNameBuilder.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
//				//skip since this will never happen;
//			}else {//non-null valid value
//				for(IndependentFreeInputVariableTypeBuilder builder:this.ownerCompositionFunctionBuilder.getIndependentFreeInputVariableTypeBuilderSet()) {
//					if(builder.getCurrentStatus().hasValidValue()) {
//						if(builder.getCurrentValue().getName().equals(aliasNameBuilder.getCurrentValue())) {
//							
//						}
//					}
//					
//					
//				}
//				
//			}
//		};
//		
//		aliasNameBuilder.addStatusChangeEventAction(
//				aliasNameBuilderStatusChangeEventAction);
		
		
	}

	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(name).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(SQLDataType).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				IndependentFreeInputVariableType independentFreeInputVariableType = (IndependentFreeInputVariableType)value;
				this.getChildrenNodeBuilderNameMap().get(name).setValue(independentFreeInputVariableType.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(independentFreeInputVariableType.getNotes(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(SQLDataType).setValue(independentFreeInputVariableType.getSQLDataType(), isEmpty);
			}
		}
		return changed;
	}
	
	
	
	@Override
	protected IndependentFreeInputVariableType build() {
		SimpleName aliasNameValue = (SimpleName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		CompositionFunctionID ownerCompositionFunctionID = this.ownerCompositionFunctionBuilder.getCompositionFunctionID();
		VfDefinedPrimitiveSQLDataType SQLDataTypeValue = (VfDefinedPrimitiveSQLDataType) this.getChildrenNodeBuilderNameMap().get(SQLDataType).getCurrentValue();
		return new IndependentFreeInputVariableTypeImpl(aliasNameValue, notesValue, ownerCompositionFunctionID, SQLDataTypeValue);
	}

	
}
