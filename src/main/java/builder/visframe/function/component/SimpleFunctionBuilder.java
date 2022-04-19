package builder.visframe.function.component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import builder.visframe.function.component.utils.SimpleFunctionEvaluatorEntryBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import function.component.SimpleFunction;
import utils.FXUtils;

public class SimpleFunctionBuilder extends ComponentFunctionBuilder<SimpleFunction, SimpleFunctionBuilderEmbeddedUIContentController>{
	
	private ComponentFunctionBuilder<?,?> nextComponentFunctionBuilder;
	
	private Map<Integer, SimpleFunctionEvaluatorEntryBuilder> evaluatorIndexIDEntryBuilderMap;
	
	private Set<Integer> currentEvaluatorIndexIDSet;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param embeddedUIContentFXMLFileDirString
	 * @param hostCompositionFunctionBuilder
	 * @param previousComponentFunctionBuilder
	 * @param indexID
	 */
	public SimpleFunctionBuilder(
			CompositionFunctionBuilder hostCompositionFunctionBuilder,
			ComponentFunctionBuilder<?, ?> previousComponentFunctionBuilder) {
		super("", "", false, null, SimpleFunctionBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING,
				hostCompositionFunctionBuilder, previousComponentFunctionBuilder);
		// TODO Auto-generated constructor stub
		
		this.evaluatorIndexIDEntryBuilderMap = new LinkedHashMap<>();
		this.currentEvaluatorIndexIDSet = new HashSet<>();
	}
	
	
	/**
	 * get the next available integer that is not used as index ID by any existing SimpleFunctionEvaluatorBuilderDelegate
	 * @return
	 */
	public int getNextAvailableEvaluatorIndexID() {
		int i = 0;
		while(this.currentEvaluatorIndexIDSet.contains(i)) {
			i++;
		}
		
		this.currentEvaluatorIndexIDSet.add(i);
		
		return i;
	}

	
	public ComponentFunctionBuilder<?,?> getNextComponentFunctionBuilder() {
		return nextComponentFunctionBuilder;
	}


	@Override
	protected Set<AbstractEvaluatorBuilder<?>> getEvaluatorBuilderSet() {
		Set<AbstractEvaluatorBuilder<?>> ret = new LinkedHashSet<>();
		
		this.evaluatorIndexIDEntryBuilderMap.forEach((k,v)->{
			if(v.getSimpleFunctionEvaluatorBuilderDelegate().getEvaluatorBuilder()!=null)
				ret.add(v.getSimpleFunctionEvaluatorBuilderDelegate().getEvaluatorBuilder());
		});
		
		return ret;
	}
	

	@Override
	protected Set<ComponentFunctionBuilder<?, ?>> getNextComponentFunctionBuilderSet() {
		Set<ComponentFunctionBuilder<?, ?>> ret = new LinkedHashSet<>();
		
		if(this.nextComponentFunctionBuilder!=null)
			ret.add(this.nextComponentFunctionBuilder);
		
		return ret;
	}


	
	@Override
	public void deleteNextFunction(ComponentFunctionBuilder<?,?> next) throws SQLException, IOException {
		if(this.nextComponentFunctionBuilder==next) {
			if(this.getNextComponentFunctionBuilder()!=null)
				
			
			this.getHostCompositionFunctionBuilder().getEmbeddedUIContentController().removeComponentFunctionBuilder(this.nextComponentFunctionBuilder);
//			this.getEmbeddedUIContentController().deleteNext();
			
			
			this.nextComponentFunctionBuilder = null;
			this.getHostCompositionFunctionBuilder().update();
		}else {
			throw new IllegalArgumentException("given next SimpleFunctionBuilder is not valid!");
		}
	}
	
	/**
	 * set the next ComponentFunctionBuilder to the given one;
	 * 
	 * if given value is null, the effect is to remove the existing one;
	 * 
	 * also when next function is not null, no more evaluator can be added!
	 * 		this is to avoid same CFGTarget assigned to multiple OuptutVariableBuilders on the same root path
	 * 
	 * @param nextComponentFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setNextComponentFunctionBuilder(ComponentFunctionBuilder<?,?> nextComponentFunctionBuilder) throws SQLException, IOException {
		
		//first remove the existing next one from the UI;
		this.getHostCompositionFunctionBuilder().getEmbeddedUIContentController().removeComponentFunctionBuilder(this.nextComponentFunctionBuilder);
		//
		this.nextComponentFunctionBuilder = nextComponentFunctionBuilder;
		//add new next one to the UI
		this.getHostCompositionFunctionBuilder().getEmbeddedUIContentController().addComponentFunctionBuilder(this.nextComponentFunctionBuilder);
		
		
		//
		this.getHostCompositionFunctionBuilder().update();
	}

	
	
	public Map<Integer, SimpleFunctionEvaluatorEntryBuilder> getEvaluatorIndexIDEntryBuilderMap() {
		return evaluatorIndexIDEntryBuilderMap;
	}

	
	/**
	 * create and add an evaluator;
	 * 
	 * return the SimpleFunctionEvaluatorEntryBuilder of the newly added evaluator
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SimpleFunctionEvaluatorEntryBuilder addEvaluator() throws SQLException, IOException {
		SimpleFunctionEvaluatorEntryBuilder entryBuilder = new SimpleFunctionEvaluatorEntryBuilder(this);
		
		this.evaluatorIndexIDEntryBuilderMap.put(entryBuilder.getSimpleFunctionEvaluatorBuilderDelegate().getIndexID(), entryBuilder);
		
		//whenever the SimpleFunctionEvaluatorBuilderDelegate's status is changed, invoke the update() method of host CompositionFunctionBuilder
		entryBuilder.getSimpleFunctionEvaluatorBuilderDelegate().addStatusChangedAction(
				()->{
					try {
						this.getHostCompositionFunctionBuilder().update();
					} catch (SQLException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
				);
		
		return entryBuilder;
	}
	
	/**
	 * delete the evaluator;
	 * 
	 * if there is no evaluator left, need to set the makeNextFunctionButton to disabled;
	 * since 
	 * 
	 * @param evaluator
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void deleteEvaluator(SimpleFunctionEvaluatorEntryBuilder evaluatorEntryBuilder) throws SQLException, IOException {
		this.getEmbeddedUIContentController().evaluatorListVBox.getChildren().remove(evaluatorEntryBuilder.getController().getRootContainerNode());
		
		this.evaluatorIndexIDEntryBuilderMap.remove(evaluatorEntryBuilder.getSimpleFunctionEvaluatorBuilderDelegate().getIndexID());
		
//		this.evaluatorBuilderDelegateEntryBuilderMap.keySet().forEach(e->{
//			if(e == evaluatorEntryBuilder.getSimpleFunctionEvaluatorBuilderDelegate())
//				this.evaluatorBuilderDelegateEntryBuilderMap.remove(e);
//		});
		
//		if(this.evaluatorBuilderDelegateEntryBuilderMap.remove(evaluatorEntryBuilder.getSimpleFunctionEvaluatorBuilderDelegate())==null) {
//			throw new VisframeException("given evaluator is not found in this SimpleFunctionBuilder!");
//		}
		
		
		
		this.getHostCompositionFunctionBuilder().update();
	}
	///////////////////////////////
	
	/**
	 * if {@link #uncalculatedCFGTargetSet} is empty, next function cannot be added (modify the UI accordingly);
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void prepareUpdate() throws SQLException, IOException {
		super.prepareUpdate();
		
		if(this.getUncalculatedCFGTargetNameMap().isEmpty()) {
			FXUtils.set2Disable(this.getEmbeddedUIContentController().deleteNextFunctionButton, false);
		}else {
			
		}
	}
	/**
	 * 1. if any CFGTargets not selected for host CompositionFunctionBuilder is assigned to CFGTargetOuptutVariable of AbstractEvaluators of this SimpleFunctionBuilder
	 */
	@Override
	protected void identifyViolatedConstraints() {
		super.identifyViolatedConstraints();
		
		
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * 
	 * 1. makeNextFunctionButton should be disabled if one or more of the following is true; 
	 * 		1. no unassigned target left or 
	 * 		2. next function is not null or 
	 * 		3. evaluator set is empty or 
	 * 		4. at least one evaluator builder does not have valid value;
	 * 	on the contrary, makeNextFunctionButton should be enabled when all of the above are false;
	 * 
	 * 2. when next function is not null or there is on unassigned target left(all target are assigned), no more evaluator can be added; 
	 * 
	 * 
	 * 3. when next function is not null, no existing evaluator can be deleted or edited;
	 * 
	 * 4. deleteNextFunctionButton should be disabled when next function is null;
	 */
	@Override
	protected void updateUI() {
		this.getEmbeddedUIContentController().updateSummary();
		
		//1
		boolean allEvaluatorBuildersHaveValidValue=true;
		
		for(SimpleFunctionEvaluatorEntryBuilder eeb:this.evaluatorIndexIDEntryBuilderMap.values()) {
			if(!eeb.getSimpleFunctionEvaluatorBuilderDelegate().getCurrentStatus().hasValidValue()) {
				allEvaluatorBuildersHaveValidValue = false;
				break;
			}
		}
		
		//makeNextFunctionButton should be disabled if no unassigned target left or next function is not null or evaluator set is empty or at least one evaluator builder does not have valid value;
		this.getEmbeddedUIContentController().setMakeNextFunctionButtonDisable(
				this.nextComponentFunctionBuilder!=null || 
				this.getUncalculatedCFGTargetNameMap().isEmpty()||
				this.evaluatorIndexIDEntryBuilderMap.isEmpty()||
				!allEvaluatorBuildersHaveValidValue);
		
		
		//2 !!!!!when next function is not null or there is on unassigned target left(all target are assigned), no more evaluator can be added; 
		this.getEmbeddedUIContentController().setAddEvaluatorButtonDisable(this.nextComponentFunctionBuilder!=null || this.getUncalculatedCFGTargetNameMap().isEmpty());
		
		//3 when next function is not null, no existing evaluator can be deleted or edited;
		this.evaluatorIndexIDEntryBuilderMap.values().forEach(e->{
			e.setDeleteDisable(this.nextComponentFunctionBuilder!=null);
			e.setEditDisable(this.nextComponentFunctionBuilder!=null);
		});
		
		//deleteNextFunctionButton should be disabled when next function is null;
		this.getEmbeddedUIContentController().setDeleteNextFunctionButtonDisable(this.nextComponentFunctionBuilder==null);
		
		
		
		//
		this.getEvaluatorIndexIDEntryBuilderMap().forEach((k,v)->{
			v.getController().setUIEffect();
		});
	}
	
	
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//set this SimpleFunctionBuilder
		//1. set the embedded UI
		this.getEmbeddedUIContentController().setModifiable(modifiable);
		
		//2. set the SimpleFunctionEvaluatorEntryBuilders
		for(SimpleFunctionEvaluatorEntryBuilder e:this.getEvaluatorIndexIDEntryBuilderMap().values()){
			e.setModifiable(modifiable);
		}
		
		//set next ComponentFunctionBuilder if not null
		if(this.getNextComponentFunctionBuilder()!=null)
			this.getNextComponentFunctionBuilder().setModifiable(modifiable);
	}

}
