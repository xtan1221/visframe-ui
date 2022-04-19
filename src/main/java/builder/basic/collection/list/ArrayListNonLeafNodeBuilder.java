package builder.basic.collection.list;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import builder.basic.collection.list.element.ListFeatureElementController;
import builder.basic.collection.map.nonleaf.HashMapNonLeafNodeBuilder;
import builder.basic.collection.map.nonleaf.HashMapNonLeafNodeBuilderIntegrativeUIController;
import core.builder.NodeBuilder;
import core.builder.NonLeafNodeBuilder;
import core.builder.factory.NodeBuilderFactory;
import exception.VisframeException;
import javafx.fxml.FXMLLoader;

/**
 * builder for a general ArrayList of a type of E;
 * 
 * the size of the ArrayList is not fixed and changeable during the process;
 * 
 * @author tanxu
 *
 * @param <E>
 */
public class ArrayListNonLeafNodeBuilder<E> extends NonLeafNodeBuilder<ArrayList<E>>{
	private final NodeBuilderFactory<E,?> listElementNodeBuilderFactory;
	private final boolean duplicateElementAllowed;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param listElementNodeBuilderFactory
	 * @param duplicateElementAllowed
	 */
	public ArrayListNonLeafNodeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			NodeBuilderFactory<E,?> listElementNodeBuilderFactory,
			boolean duplicateElementAllowed) {
		super(name, description, canBeNull, parentNodeBuilder);
		
		
		this.listElementNodeBuilderFactory = listElementNodeBuilderFactory;
		
		this.duplicateElementAllowed = duplicateElementAllowed;
		
		this.listElementNodeBuilderFactory.setParent(this);
	}
	
	/**
	 * @return the listElementNodeBuilderFactory
	 */
	public NodeBuilderFactory<E, ?> getListElementNodeBuilderFactory() {
		return listElementNodeBuilderFactory;
	}
	
	public boolean isDuplicateElementAllowed() {
		return duplicateElementAllowed;
	}
	
	
	/**
	 * return the current set of NodeBuilders for all current list elements
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public Set<? extends NodeBuilder<E,?>> getNodeBuilderSet() throws SQLException, IOException{
		Set<NodeBuilder<E,?>> ret = new LinkedHashSet<>();
		
		this.getIntegrativeUIController().getElementControllerList().forEach(e->{
			if(e.getElementBuilder()!=null)
				ret.add(e.getElementBuilder());
		});
		
		return ret;
	}
	
	
	///////////////////////////////////////////////////////
	/**
	 * build (if not alreay) and return the {@link HashMapNonLeafNodeBuilderIntegrativeUIController} of this {@link HashMapNonLeafNodeBuilder};
	 * 
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayListNonLeafNodeBuilderIntegrativeUIController<E> getIntegrativeUIController() throws SQLException, IOException {
		if(this.nonLeafNodeBuilderIntegrativeUIController == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ArrayListNonLeafNodeBuilderIntegrativeUIController.FXML_FILE_DIR_STRING));
			
//			try {
				loader.load();
				this.nonLeafNodeBuilderIntegrativeUIController = (ArrayListNonLeafNodeBuilderIntegrativeUIController<E>)loader.getController();
		    	
				//
		    	this.nonLeafNodeBuilderIntegrativeUIController.setOwnerNonLeafNodeBuilder(this);
		    	
		    	//
		    	if(this.getCurrentStatus().isDefaultEmpty()) {
		    		//
		    	}else {
		    		if(this.getCurrentStatus().isSetToNull()) {
		    			
		    		}else {
		    			this.setValue(this.getCurrentValue(), false);
		    		}
		    	}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.exit(1);
//			}
			
	    	
		}
		
		return (ArrayListNonLeafNodeBuilderIntegrativeUIController<E>) this.nonLeafNodeBuilderIntegrativeUIController;
	}
	
	/**
	 * set whether this UI is modifiable or not;
	 * @param modifiable
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		super.setModifiable(modifiable);
		
		//set each entry
//		this.getIntegrativeUIController().setModifiable(modifiable);
	}
	
	/**
	 * instead of set every child {@link NodeBuilder} to default empty, remove all the entries and delete all child {@link NodeBuilder}
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Override
	public final boolean setToDefaultEmpty() throws SQLException, IOException {
		boolean changed = this.getCurrentStatus().setToDefaultEmpty();
		
		this.getIntegrativeUIController().removeAllElements();
		
		this.getChildrenNodeBuilderNameMap().clear();
		
		return changed;
	}

	///////////////////////
	public List<String> findConstraintViolation(){
		//this should always be empty
		List<String> ret = checkInterChildrenConstraints();
		
		if(!ret.isEmpty()) {
			return ret;
		}
		
		//check if any exception is thrown
		try {
			this.build();
			
		}catch(Exception e) {
			ret.add(e.getClass().getSimpleName().concat(":").concat(e.getMessage()));
			
		}

		return ret;
	}
	
	/**
	 * children node are added by the HashMapNonLeafBuilderIntegrativeUIController
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * no explicit inter children node constraints
	 */
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * no children node dependency
	 */
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		
	}
	
	
	//////////////////////////////////////////////////
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		if(isEmpty) {
			this.getIntegrativeUIController().setToDefaultEmptyButtonOnAction(null);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				this.getIntegrativeUIController().setValue((ArrayList<E>)value);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected ArrayList<E> build() throws SQLException, IOException {
		ArrayList<E> ret = new ArrayList<>();
		
		for(ListFeatureElementController<E> element:this.getIntegrativeUIController().getElementControllerList()) {
			if(ret.contains(element.getElementBuilder().getCurrentValue()) && !this.isDuplicateElementAllowed()){
				throw new VisframeException("duplicate list element is found which is not allowed: "+element.toString());
			}
			
			if(element.getElementBuilder().getCurrentValue()==null&&!this.listElementNodeBuilderFactory.canBeNull())
				throw new VisframeException("Null valued list element is found which is not allowed!");
			ret.add(element.getElementBuilder().getCurrentValue());
			
		}
		
		return ret;
	}
}
