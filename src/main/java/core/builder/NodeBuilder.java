package core.builder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import core.builder.ui.embedded.container.EmbeddedUIRootContainerNodeController;
import core.builder.ui.embedded.content.EmbeddedUIContentController;

/**
 * interface for a builder of a property of type T
 * @author tanxu
 *
 * @param <T>
 */
public interface NodeBuilder<T, E extends EmbeddedUIContentController<T>>{
//	Class<T> getType();
	/**
	 * the parent of this {@link NodeBuilder} of which the value of the property of this {@link NodeBuilder} is used as a field to build the value of the property
	 * 
	 * can be null for root property;
	 * @return
	 */
	NonLeafNodeBuilder<?> getParentNodeBuilder();
	
	/**
	 * the name of this {@link NodeBuilder} relative to its parent;
	 * 
	 * must be unique among all sibling {@link NodeBuilder}s of the same parent;
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * the description of the role of this {@link NodeBuilder} relative to the parent (if existing);
	 * 
	 * @return
	 */
	String getDescription();
	
	/**
	 * whether the this {@link NodeBuilder}'s property value can be null (either independently or conditionally) or not (must be non-null no matter what) for the parent
	 * 
	 * @return
	 */
	boolean canBeNull();
	
	///////////////////////////////////////////////
//	/**
//	 * return the set of sibling {@link NodeBuilder} names that are dependent on the value of the target property of this {@link NodeBuilder};
//	 * 
//	 * cannot be null but can be empty;
//	 * 
//	 * @return
//	 */
//	Set<String> getDependentSilbingNodeBuilderNameSet();
//	
//	
//	/**
//	 * add to the set of sibling {@link NodeBuilder} names that are dependent on the value of the target property of this {@link NodeBuilder};
//	 * 
//	 * need to check if this {@link NodeBuilder} has parent or not; if not, throw exception;
//	 * 
//	 * also check if there is a child of parent with the given name; if not, throw exception;
//	 * 
//	 * then if this {@link NodeBuilder} can be null, check if the given dependent sibling node can be null or not; if not, throw exception;
//	 * 
//	 * @param siblingName
//	 */
//	void addDependentSilbingNodeBuilderName(String siblingName);
	
	/**
	 * add the status change event action {@link Runnable} which should be run whenever the {@link Status} of this {@link NodeBuilder} is changed;
	 * 
	 * normally the action will update dependent sibling {@link NodeBuilder}s of this one according to the change;
	 * 
	 * @param siblingNodeBuilder
	 * @param changeEventAction
	 */
	void addStatusChangedAction(Runnable statusChangeEventAction);
	
	/**
	 * set of status change event actions of this {@link NodeBuilder}
	 * 
	 * note that for a set of sibling {@link NodeBuilder}s of the same parent, the dependency relationship between them should not contain any cycles, otherwise, endless  loop of status change event action will be resulted!
	 * @return
	 */
	Set<Runnable> getStatusChangedActionSet();
	///////////////////////////////////////////////
	
	/**
	 * 
	 * @return
	 */
	Status<T> getCurrentStatus();
	
	
	/**
	 * 
	 * @return
	 */
	default T getCurrentValue() {
		return this.getCurrentStatus().isSetToNull()?null:this.getCurrentStatus().getValue();
	}
	
	
	///////////////////////////////
	/**
	 * set the value to the given one;
	 * 
	 * need to modify the current {@link Status} then change the UI accordingly;
	 * 
	 * this method assumes that the current status is non-null and non-default empty (if not, need to process them before invoking this method);
	 * 
	 * should be implemented by each final sub-type of {@link LeafNodeBuilder} and {@link NonLeafNodeBuilder};
	 * 
	 * 
	 * @param value
	 * @return whether the status has been changed afterwards; if true, need to deal with dependent sibling {@link NodeBuilder}s in {@link #getDependentSilbingNodeBuilderNameSet()};
	 * @throws SQLException 
	 * @throws IOException 
	 */
	boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException;
	
	
	///////////////////////
	/**
	 * set the value of the property of this {@link NodeBuilder} to null;
	 * does NOT affect whether the current status is default empty or not;
	 * 		this is to facilitate reversal of this method;
	 * does NOT affect descendant {@link NodeBuilder}s if this is a {@link NonLeafNodeBuilder};
	 * can be reversed by {@link #setToNonNull()} method;
	 * 
	 * implementation strategy:
	 * 0. check if the value of the target property can be set to null or not by checking {@link #canBeNull()} and the current status {@link #getCurrentStatus()}
	 * 1. set the {@link #getCurrentStatus()} to null
	 * 2. invoke the {@link #getEmbeddedUIRootContainerNodeController()} {@link EmbeddedUIRootContainerNodeController#setUIToNullStatus()};
	 * 
	 * @return whether the status has been changed afterwards; if true, need to deal with dependent sibling {@link NodeBuilder}s in {@link #getDependentSilbingNodeBuilderNameSet()};
	 * @throws IOException 
	 */
	boolean setToNull();
	
	/**
	 * reverse the effect of {@link #setToNull()} method thus to change the current status to the previous one before it was set to null;
	 * 
	 * implementation strategy:
	 * 1. set the {@link #getCurrentStatus()} to non-null
	 * 2. invoke the {@link #getEmbeddedUIRootContainerNodeController()} {@link EmbeddedUIRootContainerNodeController#setUIToNonNullStatus()};
	 * 
	 *@return whether the status has been changed afterwards; if true, need to deal with dependent sibling {@link NodeBuilder}s in {@link #getDependentSilbingNodeBuilderNameSet()};
	 * @throws IOException 
	 */
	void setToNonNull();
	
	/**
	 * whether the value of the property is set to null
	 * @return
	 */
	default boolean isSetToNull() {
		return this.getCurrentStatus().isSetToNull();
	}
	
	
	///////////////////////////////////////////////
	/**
	 * set the {@link #getCurrentStatus()} of this {@link NodeBuilder} to default empty, so that the value of the property can be created from scratch;
	 * 
	 * implementation strategy:
	 * 		1. set the {@link #getCurrentStatus()} to default empty;
	 * 		2. set the UI to default empty status by invoke {@link EmbeddedUIRootContainerNodeController#setUIToDefaultEmptyStatus()};
	 * 			
	 * 		3. for {@link NonLeafNodeBuilder} only, 
	 * 			this will also recursively trigger the {@link #setToDefaultEmpty()} method of all children {@link NodeBuilder}s until all {@link LeafNodeBuilder}s are reached;
	 * 			thus to make the full subtree rooted on this {@link NodeBuilder} to default empty;
	 * 		
	 * CANNOT be reversed;
	 * 
	 * 
	 * @return whether the status has been changed afterwards; if true, need to deal with dependent sibling {@link NodeBuilder}s in {@link #getDependentSilbingNodeBuilderNameSet()};
	 * @throws SQLException 
	 * @throws IOException 
	 */
	boolean setToDefaultEmpty() throws SQLException, IOException;
	
	
	/**
	 * return whether 
	 */
	default boolean isDefaultEmpty() {
		return this.getCurrentStatus().isDefaultEmpty();
	}
	
	/////////////////////////////////////////////////
	
//	/**
//	 * update the current value of this {@link NodeBuilder};
//	 * 
//	 * if newValue is not null, update the value to it;
//	 * 
//	 * else, 
//	 * 		if isEmpty is true, set the defaultEmpty to true and the current value to null;
//	 * 		else, only set the current value to null;
//	 * 
//	 * @param newValue
//	 * @param isEmpty
//	 */
//	void update(T newValue, boolean isEmpty);
	
	///////////////////////////////////////////////////
	///UI related
	
	/**
	 * set whether the UI of this {@link NodeBuilder} is modifiable or view only;
	 * 
	 * by default, the UI is modifiable;
	 * @param modifiable
	 * @throws SQLException 
	 * @throws IOException 
	 */
	void setModifiable(boolean modifiable) throws SQLException, IOException;
	
	
	/**
	 * 
	 * @return
	 */
	String getEmbeddedUIContentFXMLFileDirString();
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	EmbeddedUIRootContainerNodeController getEmbeddedUIRootContainerNodeController();
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	E getEmbeddedUIContentController();
	
	
	/**
	 * set the Visual effect according to whether the current data in the content UI is valid or not;
	 * @param built
	 */
	default void setUIVisualEffect(boolean built) {
//		System.out.println("set visual effect");
		if(built) {
			this.getEmbeddedUIContentController().getRootParentNode().setStyle("-fx-border-color:#1FDE5E;" + "-fx-border-size:5;");
			
//			this.getEmbeddedUIRootContainerNodeController().getRootContainerPane().setStyle("-fx-border-color:#1FDE5E;" + "-fx-border-size:5");
			
		}else {
			
			this.getEmbeddedUIContentController().getRootParentNode().setStyle("-fx-border-color:red;" + "-fx-border-size:5;");
			
//			this.getEmbeddedUIRootContainerNodeController().getRootContainerPane().setStyle("-fx-border-color:red;" + "-fx-border-size:5;");
			
		}
	}
}
