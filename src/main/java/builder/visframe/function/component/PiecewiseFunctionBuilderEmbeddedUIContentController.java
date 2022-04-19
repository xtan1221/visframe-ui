package builder.visframe.function.component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import basic.VfNotes;
import builder.visframe.function.component.ComponentFunctionBuilder.ComponentFunctionType;
import builder.visframe.function.component.utils.PiecewiseFunctionConditionEntryBuilder;
import function.component.ComponentFunction;
import function.component.PiecewiseFunction;
import function.component.SimpleFunction;
import function.component.PiecewiseFunction.ConditionalEvaluatorDelegator;
import function.composition.CompositionFunctionID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import utils.FXUtils;
import utils.Pair;

public class PiecewiseFunctionBuilderEmbeddedUIContentController extends ComponentFunctionBuilderEmbeddedUIContentController<PiecewiseFunction>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/function/component/PiecewiseFunctionBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		super.setupLogicToCheckEffectiveUIInput();
		//any specific initialization based on the owner builder
		
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public PiecewiseFunctionBuilder getOwnerNodeBuilder() {
		return (PiecewiseFunctionBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	/**
	 * build and 
	 */
	@Override
	public PiecewiseFunction build() {
		CompositionFunctionID hostCompositionFunctionID = this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getCompositionFunctionID();
		int ID = this.getOwnerNodeBuilder().getIndexID();
		VfNotes notes = VfNotes.makeVisframeDefinedVfNotes();
		
		ComponentFunction defaultNextFunction = this.getOwnerNodeBuilder().getDefaultNextComponentFunctionBuilder().getEmbeddedUIContentController().build();
		
		List<Pair<ConditionalEvaluatorDelegator,ComponentFunction>> orderedListOfConditionEvaluatorNextComponentFunctionPairByConditionPrecedenceIndex = new ArrayList<>();
		
		this.getOwnerNodeBuilder().getOrderedListOfConditionEntryBuilderByPrecedence().forEach(e->{
			ConditionalEvaluatorDelegator conditionalEvaluatorDelegator = new ConditionalEvaluatorDelegator(e.getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getCurrentValue());
			ComponentFunction nextComponentFunction = e.getNextComponentFunctionBuilder().getEmbeddedUIContentController().build();
			orderedListOfConditionEvaluatorNextComponentFunctionPairByConditionPrecedenceIndex.add(
					new Pair<>(conditionalEvaluatorDelegator, nextComponentFunction)
					);
		});
		
		return new PiecewiseFunction(
				hostCompositionFunctionID, ID, notes,
				defaultNextFunction, 
				orderedListOfConditionEvaluatorNextComponentFunctionPairByConditionPrecedenceIndex
				);
	}
	
	
	
	@Override
	public void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.deleteThisPiecewiseFunctionButton, !modifiable);
		FXUtils.set2Disable(this.makeDefaultNextFunctionButton, !modifiable);
		FXUtils.set2Disable(this.deleteDefaultNextFunctionButton, !modifiable);
		FXUtils.set2Disable(this.addConditionEntryButton, !modifiable);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		//TODO
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	/**
	 * 1. set index id
	 * 
	 * 
	 * 2. set conditions
	 * 		1. set precedence index
	 * 		2. set condition evaluator
	 * 		3. set condition next function
	 * 		
	 * 		note that evaluator must be set before next function for each condition
	 * 
	 * 3. set default next function
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(PiecewiseFunction value) throws SQLException, IOException {
		//1. set index id
		this.getOwnerNodeBuilder().setIndexID(value.getIndexID());
		
		//2. set conditions
		for(int i=1;i<=value.getConditionPrecedenceIndexConditionalEvaluatorMap().size();i++) {
			PiecewiseFunctionConditionEntryBuilder entryBuilder = new PiecewiseFunctionConditionEntryBuilder(this.getOwnerNodeBuilder());
			
			//1 precedence index
			entryBuilder.setPrecedenceIndex(i);
			
			//2 condition evaluator
			this.getOwnerNodeBuilder().getOrderedListOfConditionEntryBuilderByPrecedence().add(entryBuilder);
			this.getOwnerNodeBuilder().resetConditionEntries();
			
			entryBuilder.getPiecewiseFunctionConditionEvaluatorBuilderDelegate().setValue(
					value.getConditionPrecedenceIndexConditionalEvaluatorMap().get(i).getEvaluator(), false);
			
			
			//3 condition next function
			ComponentFunctionBuilder<?,?> nextCFBuiler;
			if(value.getConditionPrecedenceIndexNextFunctionMap().get(i) instanceof SimpleFunction) {
				nextCFBuiler = new SimpleFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder());
			
				//set next function builder before set the value of the next function 
				entryBuilder.setNextComponentFunctionBuilder(nextCFBuiler);
				
				//set value
				SimpleFunctionBuilderEmbeddedUIContentController controller = 
						(SimpleFunctionBuilderEmbeddedUIContentController)nextCFBuiler.getEmbeddedUIContentController();
				controller.setUIToNonNullValue((SimpleFunction)value.getConditionPrecedenceIndexNextFunctionMap().get(i));
			}else {
				nextCFBuiler = new PiecewiseFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder());
			
				//set next function builder before set the value of the next function 
				entryBuilder.setNextComponentFunctionBuilder(nextCFBuiler);
				
				//set value
				PiecewiseFunctionBuilderEmbeddedUIContentController controller = 
						(PiecewiseFunctionBuilderEmbeddedUIContentController)nextCFBuiler.getEmbeddedUIContentController();
				controller.setUIToNonNullValue((PiecewiseFunction)value.getConditionPrecedenceIndexNextFunctionMap().get(i));
			}
			
		}
	
		
		//3. set default next function
		ComponentFunctionBuilder<?,?> defaultNextCFBuiler;
		if(value.getDefaultNextFunction() instanceof SimpleFunction) {
			defaultNextCFBuiler = new SimpleFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder());

			//add default next function before set the value!!!!!!!!!
			this.getOwnerNodeBuilder().setDefaultNextComponentFunctionBuilder(defaultNextCFBuiler);
			
			//set value
			SimpleFunctionBuilderEmbeddedUIContentController controller = 
					(SimpleFunctionBuilderEmbeddedUIContentController)defaultNextCFBuiler.getEmbeddedUIContentController();
			controller.setUIToNonNullValue((SimpleFunction)value.getDefaultNextFunction());
		}else {
			defaultNextCFBuiler = new PiecewiseFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder());

			//add default next function before set the value!!!!!!!!!
			this.getOwnerNodeBuilder().setDefaultNextComponentFunctionBuilder(defaultNextCFBuiler);
			
			//set value
			PiecewiseFunctionBuilderEmbeddedUIContentController controller = 
					(PiecewiseFunctionBuilderEmbeddedUIContentController)defaultNextCFBuiler.getEmbeddedUIContentController();
			controller.setUIToNonNullValue((PiecewiseFunction)value.getDefaultNextFunction());
		}
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	/////////////////////////////////////////////////////////
	/**
	 * reset the conditionEntryVBox with the current list of condition entries;
	 */
	void resetConditionEntryVBox() {
		this.conditionEntryVBox.getChildren().clear();
		
		this.getOwnerNodeBuilder().getOrderedListOfConditionEntryBuilderByPrecedence().forEach(e->{
			e.getController().updatePrecedenceIndexTextField();
			this.conditionEntryVBox.getChildren().add(e.getController().getRootContainerNode());
			
		});
	}
	

	
	@Override
	public Circle getToPreviousCircle() {
		return this.gotoPreviousCircle;
	}
	

	@Override
	public Pane getMainContentPane() {
		return this.mainVBox;
	}
	/**
	 * 
	 * @param next
	 * @return
	 */
	public Circle getGoToNextCircle(ComponentFunctionBuilder<?,?> next) {
		if(next == this.getOwnerNodeBuilder().getDefaultNextComponentFunctionBuilder()) {
			return this.gotoDefaultNextCircle;
		}
		
		for(PiecewiseFunctionConditionEntryBuilder entry:this.getOwnerNodeBuilder().getOrderedListOfConditionEntryBuilderByPrecedence()){
			if(entry.getNextComponentFunctionBuilder()==next)
				return entry.getController().gotoNextFunctionCircle;
		}
		
		throw new IllegalArgumentException("given next function is not found for this PiecewiseFunctionBuilder");
	}
	
	void updateSummary() {
		//TODO
	}
	
	
	void setDeleteDefaultNextFunctionButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.deleteDefaultNextFunctionButton, disable);
	}
	
	void setMakeDefaultNextFunctionButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.makeDefaultNextFunctionButton, disable);
	}
	//////////////////////////
	
	@FXML
	public void initialize() {
		super.initialize();
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private VBox mainVBox;
	@FXML
	private Button deleteThisPiecewiseFunctionButton;
	@FXML
	private HBox mainContentHBox;
	@FXML
	private VBox contentVBox;
	@FXML
	private Button makeDefaultNextFunctionButton;
	@FXML
	private Button deleteDefaultNextFunctionButton;
	@FXML
	private Button addConditionEntryButton;
	@FXML
	public VBox conditionEntryVBox;
	@FXML
	private TitledPane inforTitledPane;
	
	@FXML
	private Circle gotoPreviousCircle;
	@FXML
	private Circle gotoDefaultNextCircle;

	// Event Listener on Button[#deleteThisPiecewiseFunctionButton].onAction
	@FXML
	public void deleteThisPiecewiseFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder()==null) {
			this.getOwnerNodeBuilder().deleteAllNextFunctions();
			this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().setRootComponentFunctionBuilder(null);
		}else {
			//first delete all next functions of this
			this.getOwnerNodeBuilder().deleteAllNextFunctions();
			//then delete this function from previous function
			this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder().deleteNextFunction(this.getOwnerNodeBuilder());
		}
		//
//		this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().update();
	}
	// Event Listener on Button[#makeDefaultNextFunctionButton].onAction
	@FXML
	public void makeDefaultNextFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		ComponentFunctionType selectedType = this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getComponentFunctionTypeSelectionPopup().showAndWait();
		
		if(selectedType!=null) {
			if(selectedType.equals(ComponentFunctionType.SIMPLE)) {
				this.getOwnerNodeBuilder().setDefaultNextComponentFunctionBuilder(new SimpleFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder()));
			}else if(selectedType.equals(ComponentFunctionType.PIECEWISE)) {
				this.getOwnerNodeBuilder().setDefaultNextComponentFunctionBuilder(new PiecewiseFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder()));
			}
			
		}else {
			//
		}
	}
	// Event Listener on Button[#deleteNextFunctionButton].onAction
	@FXML
	public void deleteDefaultNextFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerNodeBuilder().setDefaultNextComponentFunctionBuilder(null);
		
	}
	

	// Event Listener on Button[#addConditionEntryButton].onAction
	@FXML
	public void addConditionEntryButtonOnAction(ActionEvent event) throws SQLException, IOException {
		PiecewiseFunctionConditionEntryBuilder entryBuilder = new PiecewiseFunctionConditionEntryBuilder(this.getOwnerNodeBuilder());
		
		this.getOwnerNodeBuilder().getOrderedListOfConditionEntryBuilderByPrecedence().add(entryBuilder);
		
		this.getOwnerNodeBuilder().resetConditionEntries();
	}

}
