package builder.visframe.operation;

import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import operation.AbstractOperation;
import operation.Operation;
import operation.OperationName;
import utils.FXUtils;

import static operation.AbstractOperation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import basic.VfNotes;

public abstract class AbstractOperationBuilder<T extends AbstractOperation> extends NonLeafNodeBuilder<T>{
	private final VisProjectDBContext hostVisProjectDBContext;
	/**
	 * if true, this builder is initialized for an reproduced Operation that needs to be inserted into host VisProjecectDBContext during process of building a VisSchemeAppliedArchiveReproducedAndInsertedInstance;
	 * 	 	after this builder is initialized, it's value should be set to the reproduced Operation with {@link #setValue(Object, boolean)} method;
	 * 			if the reproduced Operation has parameter dependent on input data table content, such parameters will have initial default empty values and can be set to different values;
	 * 			for other parameter-related children node builders, they cannot be modified;
	 * if false, this builder is 
	 * 		1. initialized to build a new operation from scratch;
	 * 		2. initialized to build a new operation with a selected existing Operation as template;
	 * 			note that no matter whether the template operation was reproduced or not, it does not affect the target Operation;
	 * 		3. initialized and set to the value of an existing Operation for view-only mode;
	 * ===============================================
	 * the returned Operation instance of the {@link #build()} should have its {@link Operation#isReproduced()} property the same with this field;
	 * 
	 */
	private final boolean forReproducing;
	
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	protected AbstractOperationBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean forReproducing) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.forReproducing = forReproducing;
	}
	
	
	protected VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	
	public boolean isForReproducing() {
		return forReproducing;
	}
	
	
	//////////////////////////////////////////
	
	protected Map<SimpleName, Object> buildAbstractOperationLevelSpecificParameterNameValueObjectMap(){
		OperationName name = (OperationName) this.getChildrenNodeBuilderNameMap().get(INSTANCE_ID.getName().getStringValue()).getCurrentValue();
		VfNotes notes = (VfNotes) this.getChildrenNodeBuilderNameMap().get(NOTES.getName().getStringValue()).getCurrentValue();
		
		return AbstractOperation.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(name, notes);
	}
	
	/////////////////////////////////////////////
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//INSTANCE_NAME
		this.addChildNodeBuilder(
				new VfNameStringBuilder<OperationName>(
						INSTANCE_ID.getName().getStringValue(), INSTANCE_ID.getDescriptiveName(), 
						INSTANCE_ID.canHaveNullValueObject(this.isForReproducing()), 
						this, OperationName.class)); //note that in AbstractOperation, the parameter is OperationID rather than OperationName;
		//NOTES,
		this.addChildNodeBuilder(new VfNotesBuilder(
				NOTES.getName().getStringValue(), NOTES.getDescriptiveName(), 
				NOTES.canHaveNullValueObject(this.isForReproducing()), this));
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		
	}
	
	
	////////////////////////////////////////
	/**
	 * generic method to set the value of this builder;
	 * 
	 * when the given operation object is non-null, 
	 * this method will not deal with whether the given Operation object (if not null) is reproduced or not, rather it will simply set every children nodes' value;
	 * 
	 * this is because even if the given Operation's {@link Operation#isReproduced()} returns true, it does not always indicate this builder is used for reproducing;
	 * 		for example, when building a new Operation with a template operation which was reproduced, all the children node builders of this builder should be modifiable;
	 *  
	 * to set value with a reproduced operation for reproducing purpose, use a different method? TODO
	 */
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(INSTANCE_ID.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(NOTES.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T abstractOperation = (T)value;
				
				this.getChildrenNodeBuilderNameMap().get(INSTANCE_ID.getName().getStringValue()).setValue(abstractOperation.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(NOTES.getName().getStringValue()).setValue(abstractOperation.getNotes(), isEmpty);
			}
		}
		
		return changed;
	}
	
	/**
	 * invoked whenever a non-null value is given to the {@link #setValue(Object, boolean)} method in each final subclass of {@link AbstractOperationBuilder};
	 * 
	 * this method will perform extra settings if this {@link AbstractOperationBuilder} is for reproducing;
	 * 1. set child nodes corresponding to parameter NOT dependent on input data table content to UN-modifiable;
	 * 2. set child nodes corresponding to parameter dependent on input data table content to default empty and modifiable;
	 * 3. set the UI features accordingly		
	 * @param hasInputDataTableContentDependentParameter
	 * @throws SQLException
	 * @throws IOException
	 */
	public void checkIfForReproducing(boolean hasInputDataTableContentDependentParameter) throws SQLException, IOException {
		if(this.isForReproducing()) {
			for(String name:this.getChildrenNodeBuilderNameMap().keySet()){//note that this will process all higher level child nodes of super classes;
				this.getChildrenNodeBuilderNameMap().get(name).setModifiable(false);//since there is no parameter dependent on input data table content for BuildGraphFromSingleExistingRecordOperation type, set all children nodes to un-modifiable
			}
			
			if(hasInputDataTableContentDependentParameter) {
				//if there is at least one parameter dependent on input data table content;
				//set the finish and cancel button of the integrative UI to enabled, while setToDefaultEmptyButton to disabled;
				FXUtils.set2Disable(this.getIntegrativeUIController().getSetToDefaultEmptyButton(), true);
				//then set all child node builders corresponding to these parameters to modifiable;
				//this step is operation type specific, each different type of operation should have its unique implementation;
				this.setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable();
			}else {
				//if there is no parameter dependent on input data table content, set the integrative UI controller to un-modifiable;
				//thus no changes can be made to the Operation instance;
				this.getIntegrativeUIController().setModifiable(false);//not modifiable
			}
		}
	}
	
	/**
	 * set the child node builders that build value for parameters that are dependent on input data table content;
	 * invoked from {@link #checkIfForReproducing(boolean)} method
	 * 
	 * must be implemented by each final sub class of {@link AbstractOperationBuilder};
	 * 
	 * note that child node builders of a {@link AbstractOperationBuilder} are not strictly one to one mapping to the parameters of the correspoding Operation type;
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	protected abstract void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() throws SQLException, IOException;
	
	
	
	
	//////////////////////////////////////////////
	@Override
	protected abstract T build() throws SQLException;

}
