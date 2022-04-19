package builder.visframe.function.variable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Predicate;

import basic.SimpleName;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import function.variable.AbstractVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public abstract class AbstractVariableBuilder<T extends AbstractVariable> extends NonLeafNodeBuilder<T>{
	
	private final AbstractEvaluatorBuilder<?> hostEvaluatorBuilder;
	/**
	 * a predefined fixed alias name for this AbstractVariableBuilder;
	 * if non-null, {@link #aliasNameBuilder} will not be displayed;
	 */
	private final SimpleName predefinedAliasName;
	
	
	private Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints;
	
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostEvaluatorBuilder
	 * @param dataTypeConstraints
	 */
	protected AbstractVariableBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints
			) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		
		this.hostEvaluatorBuilder = hostEvaluatorBuilder;
		this.predefinedAliasName = predefinedAliasName;
		this.dataTypeConstraints = dataTypeConstraints;
	}
	
	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return this.getHostEvaluatorBuilder().getHostVisProjectDBContext();
	}
	
	/**
	 * @return the hostCompositionFunctionBuilder
	 */
	public CompositionFunctionBuilder getHostCompositionFunctionBuilder() {
		return this.getHostEvaluatorBuilder().getHostCompositionFunctionBuilder();
	}
	
	/**
	 * @return the hostComponentFunctionBuilder
	 */
	public ComponentFunctionBuilder<?, ?> getHostComponentFunctionBuilder() {
		return this.getHostEvaluatorBuilder().getHostComponentFunctionBuilder();
	}
	
	/**
	 * @return the hostEvaluatorBuilder
	 */
	public AbstractEvaluatorBuilder<?> getHostEvaluatorBuilder() {
		return hostEvaluatorBuilder;
	}
	
	/**
	 * @return the dataTypeConstraints
	 */
	public Predicate<VfDefinedPrimitiveSQLDataType> getDataTypeConstraints() {
		return dataTypeConstraints;
	}
	
	/**
	 * set the data type constraints and re-initialize this AbstractVariableBuilder
	 * @param dataTypeConstraints the dataTypeConstraints to set
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setDataTypeConstraints(Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) throws SQLException, IOException {
		this.dataTypeConstraints = dataTypeConstraints;
		//TODO - un-tested
		//
		this.removeAllChildrenNodeBuilders();
		
		
		//
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	public SimpleName getAliasName() {
		if(this.predefinedAliasName!=null) {
			return this.predefinedAliasName;
		}else {
			return this.aliasNameBuilder.getCurrentValue();
		}
	}
	
	////////////////////////////////////
	private static final String aliasName = "aliasName";
	protected static final String aliasName_description = "aliasName";
	
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	private VfNameStringBuilder<SimpleName> aliasNameBuilder = new VfNameStringBuilder<>(aliasName, aliasName_description, false, this, SimpleName.class);
	
	protected VfNotesBuilder notesBuilder = new VfNotesBuilder(notes, notes_description, false, this);
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		
		//SimpleName name, 
		if(this.predefinedAliasName==null)
			this.addChildNodeBuilder(aliasNameBuilder);
		
		
		//VfNotes notes,
		this.addChildNodeBuilder(notesBuilder);
		
		
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			if(this.predefinedAliasName==null)
				this.aliasNameBuilder.setValue(null, isEmpty);
			this.notesBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T variable = (T)value;
				if(this.predefinedAliasName==null)
					this.aliasNameBuilder.setValue(variable.getAliasName(), isEmpty);
				this.notesBuilder.setValue(variable.getNotes(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	////////////////////////////////////////
	//utility methods
	
	/**
	 * check if the given two AbstractVariableBuilders should be considered the same one in terms of the host CompositionFunctionBuilder;
	 * 
	 * 1. must be of the same type;
	 * 2. must be of the same host ComponentFunctionBuilder;
	 * 3. must be of the same host EvaluatorBuilder
	 * 4. must have same alias name;
	 * 
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	protected static boolean areSameBuilders(AbstractVariableBuilder<?> b1, AbstractVariableBuilder<?> b2) {
		if(!b1.getClass().equals(b2.getClass()))
			return false;
		
		if(b1.getHostComponentFunctionBuilder().getIndexID()!=b2.getHostComponentFunctionBuilder().getIndexID())
			return false;
		
		if(b1.getHostEvaluatorBuilder().getIndexID()!=b2.getHostEvaluatorBuilder().getIndexID())
			return false;
		
//		if(!b1.aliasNameBuilder.getCurrentValue().equals(b2.aliasNameBuilder.getCurrentValue()))
//			return false;
		
		if(!b1.getAliasName().equals(b2.getAliasName()))
			return false;
		
		
		return true;
	}
	
	
}
