package builder.visframe.function.component.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.component.PiecewiseFunctionBuilder;
import builder.visframe.function.evaluator.delegator.PiecewiseFunctionConditionEvaluatorBuilderDelegate;
import javafx.fxml.FXMLLoader;

public class PiecewiseFunctionConditionEntryBuilder {
	private final PiecewiseFunctionBuilder hostPiecewiseFunctionBuilder;
	
	private final PiecewiseFunctionConditionEvaluatorBuilderDelegate conditionEvaluatorBuilderDelegate;
	
	private ComponentFunctionBuilder<?,?> nextComponentFunctionBuilder;
	
	private PiecewiseFunctionConditionEntryUIController controller;
	
	
	private int precedenceIndex;
	
	/**
	 * constructor
	 * @param hostPiecewiseFunctionBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public PiecewiseFunctionConditionEntryBuilder(PiecewiseFunctionBuilder hostPiecewiseFunctionBuilder) throws SQLException, IOException{
		this.hostPiecewiseFunctionBuilder = hostPiecewiseFunctionBuilder;
		this.conditionEvaluatorBuilderDelegate = new PiecewiseFunctionConditionEvaluatorBuilderDelegate(
				"PiecewiseFunctionConditionEvaluator","PiecewiseFunctionConditionEvaluator",false, null, 
				this);
		
		//add listener to the status of the evaluator builder delegate
		this.conditionEvaluatorBuilderDelegate.addStatusChangedAction(
				()->{
					try {
						this.hostPiecewiseFunctionBuilder.getHostCompositionFunctionBuilder().update();
					} catch (SQLException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}
	
	
	/**
	 * @return the nextFunctionBuilder
	 */
	public ComponentFunctionBuilder<?, ?> getNextComponentFunctionBuilder() {
		return nextComponentFunctionBuilder;
	}
	
	public int getPrecedenceIndex() {
		return precedenceIndex;
		
	}

	/**
	 * set the precedence index as the given one; also set the index ID of the hosted evaluator builder to the same value;
	 * @param precedenceIndex
	 */
	public void setPrecedenceIndex(int precedenceIndex) {
		this.precedenceIndex = precedenceIndex;
		
		if(this.conditionEvaluatorBuilderDelegate.getEvaluatorBuilder()!=null)
			this.conditionEvaluatorBuilderDelegate.getEvaluatorBuilder().setIndexID(this.precedenceIndex);
	}
	
	/**
	 * @param nextFunctionBuilder the nextFunctionBuilder to set
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setNextComponentFunctionBuilder(ComponentFunctionBuilder<?, ?> nextFunctionBuilder) throws SQLException, IOException {
		//first remove the existing next one from the UI;
		this.getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().removeComponentFunctionBuilder(
				this.nextComponentFunctionBuilder);
		//
		this.nextComponentFunctionBuilder = nextFunctionBuilder;
		//add new next one to the UI
		this.getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().getEmbeddedUIContentController().addComponentFunctionBuilder(
				this.nextComponentFunctionBuilder);
		
		
		
		//
//		this.getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().update();
		this.getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().update();
	}

	/**
	 * @return the hostPiecewiseFunctionBuilder
	 */
	public PiecewiseFunctionBuilder getHostPiecewiseFunctionBuilder() {
		return hostPiecewiseFunctionBuilder;
	}

	/**
	 * @return the conditionEvaluatorBuilderDelegate
	 */
	public PiecewiseFunctionConditionEvaluatorBuilderDelegate getPiecewiseFunctionConditionEvaluatorBuilderDelegate() {
		return conditionEvaluatorBuilderDelegate;
	}

	/**
	 * @return the controller
	 */
	public PiecewiseFunctionConditionEntryUIController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(PiecewiseFunctionConditionEntryUIController.FXML_FILE_DIR_STRING));
			
	    	try {
				loader.load();
		    	this.controller = (PiecewiseFunctionConditionEntryUIController)loader.getController();
		    	this.controller.setOwnerBuilder(this);
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		return controller;
	}

	public void setDeleteEntryButtonDisable(boolean disable) {
		this.getController().setDeleteEntryButtonDisable(disable);
	}
	
	public void setDeleteNextFunctionButtonDisable(boolean disable) {
		this.getController().setDeleteNextFunctionButtonDisable(disable);
	}
	
	public void setMakeNextFunctionButtonDisable(boolean disable) {
		this.getController().setMakeNextFunctionButtonDisable(disable);
	}
	
	public void setEditButtonDisable(boolean disable) {
		this.getController().setEditButtonDisable(disable);
	}
	
	
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//1. set UI controller
		this.getController().setModifiable(modifiable);
		
		//2. set conditional evaluator builder
		if(this.conditionEvaluatorBuilderDelegate!=null)
			this.conditionEvaluatorBuilderDelegate.setModifiable(modifiable);
		
		//3. set conditional next function builder
		if(this.nextComponentFunctionBuilder!=null)
			this.nextComponentFunctionBuilder.setModifiable(modifiable);
	}
}
