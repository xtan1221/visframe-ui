package core.builder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.base.Objects;

import core.builder.ui.embedded.container.EmbeddedUIRootContainerNodeController;
import core.builder.ui.embedded.content.EmbeddedUIContentController;
import javafx.fxml.FXMLLoader;

public abstract class NodeBuilderBase<T, E extends EmbeddedUIContentController<T>> implements NodeBuilder<T, E>{
	
	////////fields to be set by constructor
//	private final Class<T> type;
	private final String name;
	private final String description;
	private final boolean canBeNull;
	private final NonLeafNodeBuilder<?> parentNodeBuilder;
	private final String embeddedUIContentFXMLFileDirString;
	
	//////////////////////////
	/**
	 * automatically built
	 */
	private EmbeddedUIRootContainerNodeController embeddedUIRootContainerNodeController;
	
	/**
	 * 
	 */
	private E embeddedUIContentController;
	
	//////////////////////////////////////
//	/**
//	 * the set of sibling {@link NodeBuilder} whose non-null value can only be set when the value of this {@link NodeBuilder} is valid (non-null and non-default empty);
//	 * 
//	 * if the status of this {@link NodeBuilder} is changed to null, 
//	 * 		all the dependent sibling will also be set to null if its {@link #canBeNull} is true, otherwise, set to default empty;
//	 * 		this can only happen if {@link #canBeNull} is true for this {@link NodeBuilder};
//	 * else if the status of this {@link NodeBuilder} is changed to default empty, all the dependent sibling will also be set to default empty;
//	 * else if the status is changed from one valid value to another, all the dependent sibling will be set to default empty;
//	 * 
//	 */
//	private Set<String> dependentSilbingNodeBuilderNameSet;
	
	
	//////fields related with the current status of this {@link NodeBuilderBase}
	private Status<T> currentStatus;
	
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	protected NodeBuilderBase(
//			Class<T> type,
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			String embeddedUIContentFXMLFileDirString){
		//validations
		//if parentNodeBuilder is null, canBeNull must be false??
		
		
		
		
//		this.type = type;
		this.name = name;
		this.description = description;
		this.canBeNull = canBeNull;
		this.parentNodeBuilder = parentNodeBuilder;
		this.embeddedUIContentFXMLFileDirString = embeddedUIContentFXMLFileDirString;
		
//		this.dependentSilbingNodeBuilderNameSet = new LinkedHashSet<>();
		//initial status should be default empty;
		this.currentStatus = new Status<>();
		
	}
	
	
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.getEmbeddedUIRootContainerNodeController().setModifiable(modifiable);
		
		this.getEmbeddedUIContentController().setModifiable(modifiable);
	}
	
	
	@Override
	public final EmbeddedUIRootContainerNodeController getEmbeddedUIRootContainerNodeController(){
		
		if(this.embeddedUIRootContainerNodeController==null) {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(EmbeddedUIRootContainerNodeController.FXML_FILE_DIR));
			
	    	try {
				loader.load();
//		    	System.out.println("111111111111111111111111111111111111111111");
		    	this.embeddedUIRootContainerNodeController = (EmbeddedUIRootContainerNodeController)loader.getController();
//		    	this.embeddedController.initialize();
		    	
//		    	System.out.println("22222222222222222222222222222222222222222222");
		    	this.embeddedUIRootContainerNodeController.setOwnerNodeBuilder(this);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1); //if a FXML file is not found, terminate the VM debug
			}
		}
		
		return this.embeddedUIRootContainerNodeController;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public final E getEmbeddedUIContentController() {
		if(this.embeddedUIContentController==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getEmbeddedUIContentFXMLFileDirString()));
			
	    	try {
				loader.load();
//		    	System.out.println("111111111111111111111111111111111111111111");
		    	this.embeddedUIContentController = (E)loader.getController();
//		    	this.embeddedController.initialize();
		    	
//		    	System.out.println("22222222222222222222222222222222222222222222");
		    	this.embeddedUIContentController.setOwnerNodeBuilder(this);
		    	
			} catch (SQLException|IOException e) {
				e.printStackTrace();
				System.exit(1); //debug
			}
		}
		
		return this.embeddedUIContentController;
	}
	
	
	
	@Override
	public String getEmbeddedUIContentFXMLFileDirString() {
		return this.embeddedUIContentFXMLFileDirString;
	}
	
	
	///////////////////////////////////////////////////////////
	@Override
	public Status<T> getCurrentStatus(){
		return this.currentStatus;
	}
	
//	@Override
//	public Class<T> getType(){
//		return this.type;
//	}
	
	@Override
	public NonLeafNodeBuilder<?> getParentNodeBuilder() {
		return this.parentNodeBuilder;
	}

	
	@Override
	public String getName() {
		return this.name;
	}


	@Override
	public String getDescription() {
		return this.description;
	}

	
	@Override
	public boolean canBeNull() {
		return this.canBeNull;
	}


	//////////////////////////////////////////////////////////
//	@Override
//	public Set<String> getDependentSilbingNodeBuilderNameSet() {
//		return this.dependentSilbingNodeBuilderNameSet;
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void addDependentSilbingNodeBuilderName(String siblingName) {
//		if(this.getParentNodeBuilder()==null) {
//			throw new UnsupportedOperationException("cannot add dependent sibling nodes when this node builder has no parent!");
//		}
//		
//		if(!this.getParentNodeBuilder().getChildrenNodeBuilderNameMap().containsKey(siblingName)) {
//			throw new IllegalArgumentException("given dependent sibling node name is not found in the parent node's children node map!");
//		}
//		
//		if(this.canBeNull && !this.getParentNodeBuilder().getChildrenNodeBuilderNameMap().get(siblingName).canBeNull()) {
//			throw new IllegalArgumentException("depended node can be null but dependent silbing node cannot be null, which is not allowed!");
//		}
//		
//		this.dependentSilbingNodeBuilderNameSet.add(siblingName);
//	}
	
	////////////////////////////
	/**
	 * actions to take when the status of this {@link NodeBuilderBase} is changed;
	 * can be 
	 * 1. actions of depending {@link NodeBuilderBase} of this one to be taken
	 * 2. any other actions of the invoker of this {@link NodeBuilderBase};
	 */
	private Set<Runnable> statusChangedActionSet;
	
	/**
	 * add an dependent sibling {@link NodeBuilder} of this one and the Runnable that should be performed when the {@link Status} of this {@link NodeBuilder} is changed;
	 * 
	 * @param siblingNodeBuilder
	 * @param changeEventAction
	 */
	@Override
	public void addStatusChangedAction(Runnable statusChangedAction) {
		if(this.statusChangedActionSet == null) {
			this.statusChangedActionSet = new LinkedHashSet<>();
		}
		
		this.statusChangedActionSet.add(statusChangedAction);
	}
	
	
	@Override
	public Set<Runnable> getStatusChangedActionSet(){
		return this.statusChangedActionSet;
	}
	
	////////////////////////////////////////
	/**
	 * set the current {@link Status};
	 * 
	 * also update dependent sibling {@link NodeBuilder}s if any;
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed;
		if(isEmpty) {
			changed = this.setToDefaultEmpty();
		}else {
			if(value == null) {
//				if(this.canBeNull) {
					changed = this.setToNull();
//				}else {
//					changed = this.setToDefaultEmpty();
//				}
			}else {
				//a non-null value is given
				if(this.getCurrentStatus().hasValidValue() && Objects.equal(this.getCurrentValue(), value)) {//the value is same with current value;
					changed = false;
				}else {
					this.getEmbeddedUIRootContainerNodeController().setUIToNonNullStatus();
					changed = this.getCurrentStatus().setValue((T)value);
				}
			}
		}
		
		if(changed) {
			this.triggerStatusChangedActions();
		}
		
		return changed;
		
//		if(value == null) {
////			throw new IllegalArgumentException("given value cannot be null!");
//			return this.setToNull();
//		}
//		
//		//
//		if(!this.getType().isAssignableFrom(value.getClass())) {
//			throw new IllegalArgumentException("given value's type is not of type: "+this.getType().getSimpleName());
//		}
//		
//		
//		///
//		boolean changed = this.getCurrentStatus().setValue((T)value);
//		
//		if(changed) {
//			this.updateDependentSilbingNodeBuilderStatus();
//		}
//		
//		return changed;
	}
	
	///////////////////////////////////////////
	/**
	 * {@inheritDoc}
	 * this will trigger {@link #triggerStatusChangedActions()} if the status changed afterwards;
	 * @throws IOException 
	 */
	@Override
	public final boolean setToNull(){
		if(!this.canBeNull) {
			throw new UnsupportedOperationException("cannot set to null when canBeNull is false!");
		}
		
		if(this.getCurrentStatus().isSetToNull()) {//already null, do nothing?
//			throw new UnsupportedOperationException("cannot set to NULL when the Node builder is already null;");
			return false;
		}else {//
			//1. need to set the embedded container UI to null
			this.getEmbeddedUIRootContainerNodeController().setUIToNullStatus();
			
			//2. set the status
			this.getCurrentStatus().setToNull();
			
			//3. deal with dependent siblings
			this.triggerStatusChangedActions();
			
			return true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * this will NOT trigger {@link #triggerStatusChangedActions()} if the status changed afterwards;
	 * @throws IOException 
	 */
	@Override
	public final void setToNonNull() {
		if(!this.isSetToNull()) {//already not null, do nothing?
//			throw new UnsupportedOperationException("cannot set to NON-NULL when the Node builder is already not null;");
			
		} else {
			
			//1 
			this.getCurrentStatus().setToNonNull();
			
			//2 set the UI status
			this.getEmbeddedUIRootContainerNodeController().setUIToNonNullStatus();
			
			
			//3
			
		}
		
	}


	///////////////////////////////////////
	/**
	 * {@inheritDoc}
	 * this will trigger {@link #triggerStatusChangedActions()} if the status changed afterwards;
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Override
	public boolean setToDefaultEmpty() throws SQLException, IOException {
		boolean changed =  this.getCurrentStatus().setToDefaultEmpty();
		
		//dependent sibling nodes
		if(changed) {
			this.triggerStatusChangedActions();
		}
		
		return changed;
	}

	
	////////////////////////////////////////
	/**
	 * update dependent silbings of this {@link NodeBuilder} once the status of this {@link NodeBuilder} is changed;
	 * @throws IOException 
	 * 
	 * 
	 */
	private void triggerStatusChangedActions() {
		
		this.getEmbeddedUIRootContainerNodeController().setStatusVisualEffect();
			
		//////////////////////
		if(this.getStatusChangedActionSet()!=null) {
			for(Runnable runnable:this.getStatusChangedActionSet()) {
				runnable.run();
			}
		}
	}
	
}
