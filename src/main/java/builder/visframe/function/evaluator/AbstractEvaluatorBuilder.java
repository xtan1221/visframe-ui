package builder.visframe.function.evaluator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.AbstractEvaluator;

public abstract class AbstractEvaluatorBuilder<T extends AbstractEvaluator> extends NonLeafNodeBuilder<T>{
	
	
	private final ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder;
	
	///////////////////////////////////
	private int indexID;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostComponentFunctionBuilder
	 * @param indexID
	 */
	protected AbstractEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder,
			int indexID
			) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.hostComponentFunctionBuilder = hostComponentFunctionBuilder;
		this.indexID = indexID;
	}
	
	/**
	 * return the current set of {@link InputVariableBuilder}s;
	 * 
	 * must be overridden by every subclass that has its own {@link InputVariableBuilder}s;
	 * 		note that the superclass's returned set (if not null) must be included;
	 * 
	 * note that for {@link SQLAggregateFunctionBasedInputVariableBuilder} type builders, DO NOT include their recordwiseInputVariable1Builder and recordwiseInputVariable2Builder if not null;
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public abstract Set<InputVariableBuilder<?>> getInputVariableBuilderSet() throws SQLException, IOException;
	
	/**
	 * return the current set of {@link OutputVariableBuilder}s;
	 * 
	 * must be overridden by every subclass that has its own {@link OutputVariableBuilder}s;
	 * 		note that the superclass's returned set (if not null) must be included;
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public abstract Set<OutputVariableBuilder<?>> getOutputVariableBuilderSet() throws SQLException, IOException;
	
	
	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return this.getHostComponentFunctionBuilder().getHostCompositionFunctionBuilder().getHostVisProjectDBContext();
	}
	
	/**
	 * @return the hostCompositionFunctionBuilder
	 */
	public CompositionFunctionBuilder getHostCompositionFunctionBuilder() {
		return this.getHostComponentFunctionBuilder().getHostCompositionFunctionBuilder();
	} 
	
	
	public ComponentFunctionBuilder<?,?> getHostComponentFunctionBuilder() {
		return hostComponentFunctionBuilder;
	}
	
	
	public int getIndexID() {
		return indexID;
	}
	
	/**
	 * @param indexID the indexID to set
	 */
	public void setIndexID(int indexID) {
		this.indexID = indexID;
	}
	
	//////////////////////////////
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	protected VfNotesBuilder notesBuilder;
	////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//notes,
		notesBuilder = new VfNotesBuilder(
				notes, notes_description, 
				false, this);
		this.addChildNodeBuilder(notesBuilder);
		
	}


	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.notesBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				@SuppressWarnings("unchecked")
				T abstractEvaluator = (T)value;
				this.notesBuilder.setValue(abstractEvaluator.getNotes(), isEmpty);
				
				//set the index ID;
				this.indexID = abstractEvaluator.getIndexID();
				
			}
		}
		
		return changed;
	}
}
