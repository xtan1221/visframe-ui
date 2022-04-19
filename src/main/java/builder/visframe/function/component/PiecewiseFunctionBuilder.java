package builder.visframe.function.component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import builder.visframe.function.component.utils.PiecewiseFunctionConditionEntryBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import function.component.PiecewiseFunction;


public class PiecewiseFunctionBuilder extends ComponentFunctionBuilder<PiecewiseFunction, PiecewiseFunctionBuilderEmbeddedUIContentController>{

	private List<PiecewiseFunctionConditionEntryBuilder> orderedListOfConditionEntryBuilderByPrecedence;
	
	private ComponentFunctionBuilder<?,?> defaultNextComponentFunctionBuilder;
	
	/**
	 * constructor
	 * @param hostCompositionFunctionBuilder
	 * @param previousComponentFunctionBuilder
	 */
	public PiecewiseFunctionBuilder(
			CompositionFunctionBuilder hostCompositionFunctionBuilder, ComponentFunctionBuilder<?, ?> previousComponentFunctionBuilder) {
		super("PiecewiseFunction", "PiecewiseFunction", false, null, PiecewiseFunctionBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING,
				hostCompositionFunctionBuilder, previousComponentFunctionBuilder);
		// TODO Auto-generated constructor stub
		
		this.orderedListOfConditionEntryBuilderByPrecedence = new ArrayList<>();
	}

	/**
	 * reset the precedence index of each PiecewiseFunctionConditionEntryBuilder based on the current order;
	 * this will also update the condition evaluator builder's index id of each PiecewiseFunctionConditionEntryBuilder
	 * 		see in {@link PiecewiseFunctionConditionEntryBuilder#setPrecedenceIndex(int)} method
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void resetConditionEntries() throws SQLException, IOException {
		int i=1;
		for(PiecewiseFunctionConditionEntryBuilder entry:this.getOrderedListOfConditionEntryBuilderByPrecedence()) {
			entry.setPrecedenceIndex(i); //this will also update the index id of the delegated PiecewiseFunctionConditionEvaluatorBuilderDelegate
			i++;
		}
		
		this.getEmbeddedUIContentController().resetConditionEntryVBox();
		this.getHostCompositionFunctionBuilder().update();
	}
	
	/**
	 * @return the defaultNextComponentFunctionBuilder
	 */
	public ComponentFunctionBuilder<?, ?> getDefaultNextComponentFunctionBuilder() {
		return defaultNextComponentFunctionBuilder;
	}
	
	
	/**
	 * @param defaultNextComponentFunctionBuilder the defaultNextComponentFunctionBuilder to set
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setDefaultNextComponentFunctionBuilder(ComponentFunctionBuilder<?, ?> defaultNextComponentFunctionBuilder) throws SQLException, IOException {
		//first remove the existing default next one from the UI;
		this.getHostCompositionFunctionBuilder().getEmbeddedUIContentController().removeComponentFunctionBuilder(this.defaultNextComponentFunctionBuilder);
		
		
		this.defaultNextComponentFunctionBuilder = defaultNextComponentFunctionBuilder;
		
		//add new default next one to the UI
		this.getHostCompositionFunctionBuilder().getEmbeddedUIContentController().addComponentFunctionBuilder(this.defaultNextComponentFunctionBuilder);
		
		this.getHostCompositionFunctionBuilder().update();
	}
	
	public List<PiecewiseFunctionConditionEntryBuilder> getOrderedListOfConditionEntryBuilderByPrecedence() {
		return this.orderedListOfConditionEntryBuilderByPrecedence;
	}
	
	
	@Override
	protected Set<AbstractEvaluatorBuilder<?>> getEvaluatorBuilderSet() {
		Set<AbstractEvaluatorBuilder<?>> ret = new LinkedHashSet<>();
		
		this.orderedListOfConditionEntryBuilderByPrecedence.forEach(e->{
			if(e.getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getEvaluatorBuilder()!=null)
				ret.add(e.getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getEvaluatorBuilder());
		});
		
		return ret;
	}

	
	@Override
	protected Set<ComponentFunctionBuilder<?, ?>> getNextComponentFunctionBuilderSet() {
		Set<ComponentFunctionBuilder<?, ?>> ret = new LinkedHashSet<>();
		
		if(this.defaultNextComponentFunctionBuilder!=null)
			ret.add(this.defaultNextComponentFunctionBuilder);
		
		this.orderedListOfConditionEntryBuilderByPrecedence.forEach(e->{
			if(e.getNextComponentFunctionBuilder()!=null)
				ret.add(e.getNextComponentFunctionBuilder());
		});
		
		return ret;
	}


	
	/**
	 * delete the given next function (including both default next and condition next);
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void deleteNextFunction(ComponentFunctionBuilder<?,?> next) throws SQLException, IOException {
//		System.out.println(next);
//		System.out.println(this.getDefaultNextComponentFunctionBuilder());
		if(this.getDefaultNextComponentFunctionBuilder()==next) {
			this.deleteDefalutNextFunction();
		}else {
			this.deleteConditionNextFunction(next);
		}
		
		
	}
	
	
	private void deleteDefalutNextFunction() throws SQLException, IOException {
		if(this.getDefaultNextComponentFunctionBuilder()==null) {
			throw new IllegalArgumentException("default next function is already null!");
		}else {
			//remove from UI
			this.getHostCompositionFunctionBuilder().getEmbeddedUIContentController().removeComponentFunctionBuilder(this.defaultNextComponentFunctionBuilder);
//			this.getEmbeddedUIContentController().deleteDefaultNext();
			this.defaultNextComponentFunctionBuilder = null;
			
			this.getHostCompositionFunctionBuilder().update();
		}
	}
	
	
	/**
	 * set the next function of the 
	 * @param conditionNextFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void deleteConditionNextFunction(ComponentFunctionBuilder<?,?> conditionNextFunctionBuilder) throws SQLException, IOException {
		for(PiecewiseFunctionConditionEntryBuilder entryBuilder:this.getOrderedListOfConditionEntryBuilderByPrecedence()) {
			if(entryBuilder.getNextComponentFunctionBuilder()==conditionNextFunctionBuilder) {
				entryBuilder.setNextComponentFunctionBuilder(null);
				
				this.getHostCompositionFunctionBuilder().update();
				return;
			}
		}
		throw new IllegalArgumentException("given condition next function is not found!");
	}
	
	
	/**
	 * remove the given condition entry from this PiecewiseFunctionBuilder;
	 * @param condition
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void deleteCondition(PiecewiseFunctionConditionEntryBuilder condition) throws SQLException, IOException {
		this.getEmbeddedUIContentController().conditionEntryVBox.getChildren().remove(condition.getController().getRootContainerNode());
		
		this.getOrderedListOfConditionEntryBuilderByPrecedence().remove(condition);
		
		//
		this.resetConditionEntries();
	}
	
	
	
	
	//////////////////////////////////////
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	protected void prepareUpdate() throws SQLException, IOException {
		super.prepareUpdate();
		
		
	}
	/**
	 * check if every condition evaluator has a non-null next ComponentFunctionBuilder;
	 * 
	 * check if there is a non-null default next ComponentFunctionBuilder
	 */
	@Override
	protected void identifyViolatedConstraints() {
		super.identifyViolatedConstraints();
		// TODO Auto-generated method stub
		
	}


	/**
	 * 1. for each condition, 
	 * 		1. the make next function button should be disabled if the condition evaluator does not have valid value or next function is not null;
	 * 			Equivalently, next function can only be added if evaluator has valid value AND next function is null
	 * 		2. the condition evaluator cannot be edited(evaluator edit button should be disabled) if the next function is not null;
	 * 		3. the delete next function button should be disabled if the next function is null;
	 * 		4. the delete condition button should be disabled if next function is not null (this is for safety)
	 * 			thus to delete a condition, need to delete the next function first;
	 * 
	 * 2. make default next function button should be disabled if default next function is not null;
	 * 
	 * 
	 * 3. delete default next function button should be disabled if default next function is null;
	 * 
	 * 
	 * 4. 
	 */
	@Override
	protected void updateUI() {
		this.getEmbeddedUIContentController().updateSummary();

		//1
		this.orderedListOfConditionEntryBuilderByPrecedence.forEach(e->{
			//1.1
			e.setMakeNextFunctionButtonDisable(!e.getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getCurrentStatus().hasValidValue()||e.getNextComponentFunctionBuilder()!=null);
			//1.2
			e.setEditButtonDisable(e.getNextComponentFunctionBuilder()!=null);
			//1.3
			e.setDeleteNextFunctionButtonDisable(e.getNextComponentFunctionBuilder()==null);
			
			//1.4
			e.setDeleteEntryButtonDisable(e.getNextComponentFunctionBuilder()!=null);
			
		});
		
		
		//2
		this.getEmbeddedUIContentController().setMakeDefaultNextFunctionButtonDisable(this.getDefaultNextComponentFunctionBuilder()!=null);
		
		
		//3. 
		this.getEmbeddedUIContentController().setDeleteDefaultNextFunctionButtonDisable(this.getDefaultNextComponentFunctionBuilder()==null);
		
		
		
		
		//
		this.orderedListOfConditionEntryBuilderByPrecedence.forEach(e->{
			e.getController().setUIEffect();
		});
	}

	
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//1. set this PiecewiseFunction's embedded UI
		this.getEmbeddedUIContentController().setModifiable(modifiable);
		
		//2. set each PiecewiseFunctionConditionEntryBuilder
		for(PiecewiseFunctionConditionEntryBuilder e:this.orderedListOfConditionEntryBuilderByPrecedence){
			e.setModifiable(modifiable);
		}
		
		//3. set default next function builder
		this.getDefaultNextComponentFunctionBuilder().setModifiable(modifiable);
	}
}
