package builder.basic.collection.list2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import builder.basic.collection.list2.element.FixedSizeArrayListElementController;
import core.builder.NodeBuilder;
import core.builder.NonLeafNodeBuilder;
import core.builder.factory.NodeBuilderFactory;
import exception.VisframeException;
import javafx.fxml.FXMLLoader;

/**
 * builder for an ArrayList with a fixed size;
 * 
 * list element value cannot be null;
 * 
 * whether or not duplicate element value is allowed is based on {@link #duplicateElementAllowed};
 * 
 * 
 * @author tanxu
 *
 * @param <E>
 */
public class FixedSizeArrayListNonLeafNodeBuilder<E> extends NonLeafNodeBuilder<ArrayList<E>>{
	/**
	 * 
	 */
	private final NodeBuilderFactory<E,?> listElementNodeBuilderFactory;
	private final boolean duplicateElementAllowed;
	//////////////////////////
	private int size = 0; //
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param listElementNodeBuilderFactory
	 * @param duplicateElementAllowed
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public FixedSizeArrayListNonLeafNodeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			NodeBuilderFactory<E,?> listElementNodeBuilderFactory,
			boolean duplicateElementAllowed
			) throws IOException, SQLException {
		super(name, description, canBeNull, parentNodeBuilder);
		if(listElementNodeBuilderFactory==null)
			throw new IllegalArgumentException("given listElementNodeBuilderFactory cannot be null!");
		
		this.listElementNodeBuilderFactory = listElementNodeBuilderFactory;
		
		this.duplicateElementAllowed = duplicateElementAllowed;
		
		
		//////////////////////////////////
		this.listElementNodeBuilderFactory.setParent(this);
		
		this.getIntegrativeUIController().resetChildrenListElementBuilders();
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
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * set the size of the list;
	 * also reset the children list element builers to default empty;
	 * 
	 * @param size
	 * @throws IOException
	 * @throws SQLException
	 */
	public void setSize(int size) throws IOException, SQLException {
		if(size<0)
			throw new IllegalArgumentException("given size cannot be negative!");
		
		this.size = size;
		
		this.getIntegrativeUIController().resetChildrenListElementBuilders();
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
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FixedSizeArrayListNonLeafNodeBuilderIntegrativeUIController<E> getIntegrativeUIController() throws SQLException, IOException {
		if(this.nonLeafNodeBuilderIntegrativeUIController == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(FixedSizeArrayListNonLeafNodeBuilderIntegrativeUIController.FXML_FILE_DIR_STRING));
			
			loader.load();
			
			this.nonLeafNodeBuilderIntegrativeUIController = (FixedSizeArrayListNonLeafNodeBuilderIntegrativeUIController<E>)loader.getController();
	    	
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
		}
		
		return (FixedSizeArrayListNonLeafNodeBuilderIntegrativeUIController<E>) this.nonLeafNodeBuilderIntegrativeUIController;
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
	}
	
	/**
	 * set every list element node builder to default empty;
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Override
	public final boolean setToDefaultEmpty() throws SQLException, IOException {
		boolean changed = super.setToDefaultEmpty();
//		boolean changed = this.getCurrentStatus().setToDefaultEmpty();
		
		for(NodeBuilder<E,?> nb:this.getNodeBuilderSet()) {
			nb.setToDefaultEmpty();
		}
		
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
		
	}
	/**
	 * no explicit inter children node constraints
	 */
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//
	}
	/**
	 * no children node dependency
	 */
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		
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
				ArrayList<E> list = (ArrayList<E>)value;
				//first set the size
				this.setSize(list.size());
				this.getIntegrativeUIController().setValue(list);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected ArrayList<E> build() throws SQLException, IOException {
		ArrayList<E> ret = new ArrayList<>();
		
		for(FixedSizeArrayListElementController<E> element:this.getIntegrativeUIController().getElementControllerList()) {
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
