package core.builder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.builder.ui.embedded.content.NonLeafNodeBuilderEmbeddedUIContentController;
import core.builder.ui.integrative.NonLeafNodeBuilderIntegrativeUIController;
import javafx.fxml.FXMLLoader;


/**
 * base class for {@link NodeBuilder} type with at least one child {@link NodeBuilder};
 * 
 * EmbeddedUIContentController for all {@link NonLeafNodeBuilder} types are the same, which is {@link NonLeafNodeBuilderEmbeddedUIContentController}
 * 
 * NonLeafNodeBuilderIntegrativeUIController for all {@link NonLeafNodeBuilder} types are the same, which is {@link NonLeafNodeBuilderIntegrativeUIController}
 * 
 * @author tanxu
 * 
 * @param <T>
 * @param <E>
 * @param <I>
 */
public abstract class NonLeafNodeBuilder<T> extends NodeBuilderBase<T, NonLeafNodeBuilderEmbeddedUIContentController<T>> {
	///////////////////////
	protected NonLeafNodeBuilderIntegrativeUIController nonLeafNodeBuilderIntegrativeUIController;
	
	//////////////////////////////////////
	//fields that need to be set up before any UI related operation can be done;
	/**
	 * 
	 */
	private Map<String, NodeBuilder<?,?>> childrenNodeBuilderNameMap;
	
	/**
	 * set of {@link GenricChildrenNodeBuilderConstraint} that involves one or more children {@link NodeBuilder}s
	 */
	private Set<GenricChildrenNodeBuilderConstraint<T>> genricChildrenNodeBuilderConstraintSet;
	
	
	/////////////////////
	//fields related with UI operations
	/**
	 * store the previous value before a new editing session is started;
	 * to facilitate to reverse any changes that have been done;
	 */
	private Status<T> previousStatus;
	
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param embeddedUIContentController
	 * @param nonLeafNodeBuilderIntegrativeUIController
	 */
	protected NonLeafNodeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, NonLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		
		this.childrenNodeBuilderNameMap = new LinkedHashMap<>();
		this.genricChildrenNodeBuilderConstraintSet = new LinkedHashSet<>();
	}
	
	/**
	 * 
	 * set the {@link #childrenNodeBuilderNameMap} 
	 * if subclass is final type, must invoke this method at the end of the constructor;
	 * 
	 * when implementing, first invoke super.buildChildrenNodeBuilderNameMap() if it's super class also has implementation of this method;
	 * 
	 * =======================================================================
	 * !!!!!!regarding the order of adding child node builders!
	 * 
	 * if child node builder A's status change event will affect the status of another child node builder B (A's status change event action involves B), 
	 * A must be added before B in this method!!!!!!!
	 * 		the order of child node builders determines the order of value settings triggered by parent node builder;
	 * if B is somehow added after A, unexpected behavior will be result!!!!!!!!
	 * 
	 * this order should also be obeyed by {@link #setValue(Object, boolean)} method when setting values of child node builders;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	protected abstract void buildChildrenNodeBuilderNameMap() throws SQLException, IOException;
	
	
	/**
	 * set the {@link #genricChildrenNodeBuilderConstraintSet};
	 * 
	 * if subclass is final type, must invoke this method at the end of the constructor after {@link #buildChildrenNodeBuilderNameMap()};
	 * when implementing, first invoke super.buildInterChildrenNodeBuilderConstraintSet() if it's super class also has implementation of this method;
	 * 
	 * note that this method add those constraints that involve one or multiple children nodes of this one;
	 * 
	 * in theory, any type of constraints that can be validated in the constructor of the target property class can be added here!!!!!!
	 * 
	 */
	protected abstract void buildGenericChildrenNodeBuilderConstraintSet();
	
	
	/**
	 * set the {@link NodeBuilderBase#getDependentSilbingNodeBuilderNameSet()} for each children {@link NodeBuilder}
	 * 
	 * if subclass is final type, must invoke this method at the end of the constructor after {@link #buildChildrenNodeBuilderNameMap()};
	 * 
	 * when implementing, first invoke super.buildChildrenDependentSiblingNodeBuilderSet() if it's super class also has implementation of this method;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	protected abstract void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException;
	
	
	
	////////////////////////////////////////////
	
	public List<String> findConstraintViolation(){
		List<String> ret = new ArrayList<>();
		
		//first find out basic single child node builder constraints violations
		for(String childName:this.getChildrenNodeBuilderNameMap().keySet()) {
			NodeBuilder<?,?> childNodeBuilder = this.getChildrenNodeBuilderNameMap().get(childName);
			//can be null
			if(!childNodeBuilder.canBeNull()) {
				if(childNodeBuilder.getCurrentValue()==null) {
					ret.add(childName.concat(" cannot be null!"));
				}
			}
			
			//default empty and not set to null
			if(childNodeBuilder.getCurrentStatus().isDefaultEmpty() && !childNodeBuilder.isSetToNull()) {
				ret.add(childName.concat(" cannot be default empty!"));
			}
			
		}
		
		if(!ret.isEmpty()) {
			return ret;
		}
		
		////then check inter children node constraints
		ret.addAll(checkInterChildrenConstraints());
		
		if(!ret.isEmpty()) {
			return ret;
		}
		
		
		
		//check if any exception is thrown
		try {
			this.build();
			
		}catch(Exception e) {
			e.printStackTrace();
			ret.add(e.getClass().getSimpleName().concat(":").concat(e.getMessage()));
		}

		return ret;
	}
	
	/**
	 * check every inter-children sibling node constraints in {@link #genricChildrenNodeBuilderConstraintSet};
	 * return the set of violated constraints' description;
	 * 
	 * invoked when an editing session need to be finished and saved;
	 */
	protected List<String> checkInterChildrenConstraints(){
		List<String> ret = new ArrayList<>();
		
		this.getGenricChildrenNodeBuilderConstraint().forEach(e->{
			if(!e.validate()) {
				ret.add(e.getDescription());
			}
		});
		
		
		return ret;
	}
	
	//////////////////////////////////////////////
	/**
	 * try to build a valid non-null value of type T with the current value of each child {@link NodeBuilder};
	 * 
	 * 
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 */
	protected abstract T build() throws SQLException, IOException;
	
	//////////////////////////////////////////////

	/**
	 * invoked by embedded content UI controller;
	 * 
	 * invoked when a new editing session for this {@link NodeBuilder} is to start;
	 * can only be invoked when {@link #isSetToNull()} returns false; otherwise, throw {@link UnsupportedOperationException};
	 * 
	 */
	public void saveCurrentStatusAsPrevious() {
		if(this.isSetToNull()) {
			throw new UnsupportedOperationException("cannot start edit node builder when it is set to null!");
		}
		
		this.previousStatus = Status.copy(this.getCurrentStatus());
	}
	
	
	
	public void storePreviousStatus() {
		this.previousStatus = Status.copy(this.getCurrentStatus());
	}
	
	
	/**
	 * 
	 * @throws SQLException 
	 * @throws IOException
	 */
	public void restorePreviousValue() throws SQLException, IOException {
		if(this.previousStatus==null) {//no previous value, do nothing?
			return;
		}
		
		this.setValue(this.previousStatus.getValue(), this.previousStatus.isDefaultEmpty());
		//
		
		this.previousStatus = null;
	}
	
	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	public void saveNewValue() throws SQLException, IOException {
		//if a NonLeafNodeBuilder type child is set to null, reset all of it children NodeBuilders to default empty
//		this.getChildrenNodeBuilderNameMap().forEach((k,v)->{
//			if(v.isSetToNull()) {
////				if(v.getCurrentValue()!=null) {
////					v.setValue(null, false);
////				}
//				
//				if(v instanceof NonLeafNodeBuilder) {
//					NonLeafNodeBuilder<?> nnb = (NonLeafNodeBuilder<?>)v;
//					
//					//this will trigger ConcurrentModificationException since some child node may be set to null due to status change event listener between sibling nodes before it is iterated
////					nnb.getChildrenNodeBuilderNameMap().forEach((k2,v2)->{
////						v2.setToDefaultEmpty();
////					});
//					//use the following code to avoid ConcurrentModificationException;
//					Set<String> childNodeNameSet = new LinkedHashSet<>();
//					childNodeNameSet.addAll(nnb.getChildrenNodeBuilderNameMap().keySet());
//					
//					for(String name:childNodeNameSet) {
//						if(nnb.getChildrenNodeBuilderNameMap().get(name)!=null)
//							nnb.getChildrenNodeBuilderNameMap().get(name).setToDefaultEmpty();
//					}
//				}
////				
////				
//			}
//		});
		
//		this.getCurrentStatus().setValue(this.build());
		this.setValue(this.build(), false);
		
		this.previousStatus = null;
	}
	
	///////////////////////////////////
//	protected Parent integrativeUIRootNode;
	
	/**
	 * set children 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		super.setModifiable(modifiable);
		
		this.getIntegrativeUIController().setModifiable(modifiable);
		
		this.setChildrenModifiable(modifiable);
	}
	
	public void setChildrenModifiable(boolean modifiable) throws SQLException, IOException {
		for(String name:this.getChildrenNodeBuilderNameMap().keySet()) {
			this.getChildrenNodeBuilderNameMap().get(name).setModifiable(modifiable);
		}
	}
	
	
	
//	protected Stage integrativeUIWindow;//
	/**
	 * create and show the main window of this builder
	 * @param modifiable
	 * @throws SQLException 
	 * @throws IOException
	 */
	public void showMainWindow() throws SQLException, IOException {
    	this.getIntegrativeUIController().showAndWait(this.getEmbeddedUIRootContainerNodeController().getRootContainerPane().getScene().getWindow(), this.getDescription());
	}

	
	
	
	
	////////////////////////////////
	
	/**
	 * invoked by integrative UI controller;
	 * 
	 * 
	 * check if the current UI contains a set of input data to build a valid value object for the target property of this {@link NonLeafNodeBuilder};
	 * 
	 * 1. if {@link #isDefaultEmpty()}, do nothing;
	 * 2. else if {@link #isSetToNull()}, do nothing;
	 * 3. else, a valid object for the target property is wanted, thus
	 * 		3.1. check all the constraints that should be obeyed to build a valid object, 
	 * 				3.1.1. for every child {@link NodeBuilder}, if its {@link #isDefaultEmpty()} method returns true, add them to the violation information;
	 * 				3.1.2. if none violation is found in 3.1.1., 
	 * 							for every {@link InterChildNodeBuilderConstraint}, check if the constraint is violated, if true, add to the violation set;
	 * 					else, go to 3.2
	 * 				3.1.3. if none violation is found in 3.1.2., go to 3.3, else, go to 3.2
	 * 
	 * 		3.2. display an alert window and wait for feed back;
	 * 				3.2.1. continue editing to fixed the violations;
	 * 					simply close the window;
	 * 				3.2.2. set the UI to the default empty;
	 * 					invoke the {@link #setToDefaultEmpty()} method
	 * 					
	 * 				3.2.3. reset the UI to the previous state before this editing session is started;
	 * 
	 * 		3.3. save the valid object constructed based on current UI input data to the {@link #currentValue};
	 * 		
	 */
	protected void commit() {
		
		
		
		
		
		
	}
	
	
	////////////////////////////////////////
	/**
	 * must be implemented by each subtype class if there is at least one level specific child {@link NodeBuilder}
	 * 
	 * implementation strategy
	 * 
	 * 1. call super.setValue(T)
	 * 
	 * 2. then for each child {@link NodeBuilder}, invoke their setValue() method with the corresponding value;
	 * 
	 * 
	 * ==========================================
	 * !!!!!!!!regarding the order of setting value of child node builders
	 * see {@link #buildChildrenNodeBuilderNameMap};
	 * @throws SQLException 
	 * 
	 * 
	 * @throws IOException 
	 * 
	 */
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		//invoke setValue(T) method of children node builders
		//implemented by each subtype class
		
		return changed;
	}
	
	
	
	
	/////////////////////////
	
	/**
	 * if {@link #isDefaultEmpty()} is true, do nothing and return;
	 * else
	 * 		1. if {@link #isSetToNull()} returns true, invoke the {@link #setToNonNull()} method;
	 * 		2. for every child {@link NodeBuilder}, invoke their {@link #setToDefaultEmpty()} method
	 * 		3. set the {@link #defaultEmpty} to true;
	 * @throws SQLException 
	 * @throws IOException 
	 * 		
	 */
	@Override
	public boolean setToDefaultEmpty() throws SQLException, IOException {
		boolean changed = super.setToDefaultEmpty();
		
		//this is to avoid ConcurrentModificationException
		Set<String> childNodeNameSet = new LinkedHashSet<>();
		childNodeNameSet.addAll(this.childrenNodeBuilderNameMap.keySet());
		for(String childName:childNodeNameSet) {
			if(this.childrenNodeBuilderNameMap.get(childName)!=null) {
				this.childrenNodeBuilderNameMap.get(childName).setToDefaultEmpty();
			}
		}
		
		return changed;
	}
	
	/////////////////////////////////////////////
	/**
	 * build (if not alreay) and return the {@link NonLeafNodeBuilderIntegrativeUIController} of this {@link NonLeafNodeBuilder};
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws IOException
	 */
	public NonLeafNodeBuilderIntegrativeUIController getIntegrativeUIController() throws SQLException, IOException {
		if(this.nonLeafNodeBuilderIntegrativeUIController == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(NonLeafNodeBuilderIntegrativeUIController.FXML_FILE_DIR));
			
			try {
				loader.load();
//		    	System.out.println("111111111111111111111111111111111111111111");
		    	this.nonLeafNodeBuilderIntegrativeUIController = (NonLeafNodeBuilderIntegrativeUIController)loader.getController();
//		    	this.embeddedController.initialize();
		    	
//		    	System.out.println("22222222222222222222222222222222222222222222");
		    	this.nonLeafNodeBuilderIntegrativeUIController.setOwnerNonLeafNodeBuilder(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}

		}
		
		return this.nonLeafNodeBuilderIntegrativeUIController;
	}
	
	
	
	//////////////////////////////
	public Status<T> getPreviousStatus() {
		return previousStatus;
	}
	
	
	//////////////////////////////////////
	/**
	 * return the set of children {@link NodeBuilder} with the same order as they are added;
	 * @return
	 */
	public Map<String, NodeBuilder<?,?>> getChildrenNodeBuilderNameMap(){
		return this.childrenNodeBuilderNameMap;
	}
	
	/**
	 * add the given child node builder to {@link #childrenNodeBuilderNameMap}
	 * 
	 * if the name of the given child node builder is already present in the {@link #childrenNodeBuilderNameMap}, throw IllegalArgumentException;
	 * 
	 * @param child
	 * @param name
	 * @param description
	 * @param canBeNull whether the child's property value can be null (either independently or conditionally) or not (must be non-null no matter what)
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void addChildNodeBuilder(NodeBuilder<?,?> child) throws SQLException, IOException {
		if(this.childrenNodeBuilderNameMap.containsKey(child.getName())) {
			throw new IllegalArgumentException("name of the given child NodeBuilder is already existing!");
		}
		
		this.childrenNodeBuilderNameMap.put(child.getName(), child);
		
		
		child.getEmbeddedUIRootContainerNodeController().setStatusVisualEffect();;
		
//		this.getIntegrativeUIController();
		
//		Pane pane = this.getIntegrativeUIController().getChildrenEmbeddedNodeContainerPane();
//		pane.getChildren().forEach(e->{
//			System.out.println(e.getId());
//		});
		
//		child.getEmbeddedUIRootContainerNodeController().getRootContainerPane();
		
		this.getIntegrativeUIController().getChildrenEmbeddedNodeContainerPane().getChildren().add(child.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		
		
	}
	
	public void removeChildNodeBuilder(String childNodeBuilderName) throws SQLException, IOException {
		if(!this.childrenNodeBuilderNameMap.containsKey(childNodeBuilderName)) {
			throw new IllegalArgumentException("name of the given child NodeBuilder is not found!");
		}
		
		this.getIntegrativeUIController().getChildrenEmbeddedNodeContainerPane().getChildren().remove(this.childrenNodeBuilderNameMap.get(childNodeBuilderName).getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		this.childrenNodeBuilderNameMap.remove(childNodeBuilderName);
		
		
	}
	
	
	/**
	 * remove all child node builders
	 * 1. remove from the {@link #childrenNodeBuilderNameMap}
	 * 2. remove from the pane of the integrative UI;
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	public void removeAllChildrenNodeBuilders() throws SQLException, IOException {
		for(String name:this.childrenNodeBuilderNameMap.keySet()){
			this.getIntegrativeUIController().getChildrenEmbeddedNodeContainerPane().getChildren().remove(this.childrenNodeBuilderNameMap.get(name).getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		}
		
		this.childrenNodeBuilderNameMap.clear();
	}
	/////////////////////////////////////////////
	
	public Set<GenricChildrenNodeBuilderConstraint<T>> getGenricChildrenNodeBuilderConstraint(){
		return this.genricChildrenNodeBuilderConstraintSet;
	}
	/**
	 * add the given {@link GenricChildrenNodeBuilderConstraint} to the {@link #genricChildrenNodeBuilderConstraintSet}
	 * @param constraint
	 */
	public void addGenricChildrenNodeBuilderConstraint(GenricChildrenNodeBuilderConstraint<T> constraint) {
		this.genricChildrenNodeBuilderConstraintSet.add(constraint);
	}
	
	
}
