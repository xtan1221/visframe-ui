package builder.basic.collection.set.nonleaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import builder.basic.collection.map.nonleaf.HashMapNonLeafNodeBuilder;
import builder.basic.collection.map.nonleaf.HashMapNonLeafNodeBuilderIntegrativeUIController;
import builder.basic.collection.set.SetFeatureElementController;
import core.builder.NodeBuilder;
import core.builder.NonLeafNodeBuilder;
import core.builder.factory.NodeBuilderFactory;
import exception.VisframeException;
import javafx.fxml.FXMLLoader;

public class HashSetNonLeafNodeBuilder<E> extends NonLeafNodeBuilder<HashSet<E>>{
	
	private final NodeBuilderFactory<E,?> setElementNodeBuilderFactory;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param setElementNodeBuilderFactory
	 */
	public HashSetNonLeafNodeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			NodeBuilderFactory<E,?> setElementNodeBuilderFactory
			) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		
		this.setElementNodeBuilderFactory = setElementNodeBuilderFactory;
		
		this.setElementNodeBuilderFactory.setParent(this);
	}
	
	/**
	 * @return the setElementNodeBuilderFactory
	 */
	public NodeBuilderFactory<E, ?> getSetElementNodeBuilderFactory() {
		return setElementNodeBuilderFactory;
	}
	
	/**
	 * return the set of NodeBuilder of current set elements;
	 * 
	 * @return
	 */
	public Set<NodeBuilder<E,?>> getNodeBuilderSet(){
		Set<NodeBuilder<E,?>> ret = new LinkedHashSet<>();
		
		this.getIntegrativeUIController().getElementControllerParentNodeMap().keySet().forEach(e->{
			if(e.getElementBuilder()!=null)
				ret.add(e.getElementBuilder());
		});
		
		return ret;
	}
	
	//////////////////////////////////////////
	///////////////////////////////////////////////////////
	/**
	 * build (if not alreay) and return the {@link HashMapNonLeafNodeBuilderIntegrativeUIController} of this {@link HashMapNonLeafNodeBuilder};
	 * 
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashSetNonLeafNodeBuilderIntegrativeUIController<E> getIntegrativeUIController() {
		if(this.nonLeafNodeBuilderIntegrativeUIController == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(HashSetNonLeafNodeBuilderIntegrativeUIController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();

		    	this.nonLeafNodeBuilderIntegrativeUIController = (HashSetNonLeafNodeBuilderIntegrativeUIController<E>)loader.getController();
		    	
		    	this.nonLeafNodeBuilderIntegrativeUIController.setOwnerNonLeafNodeBuilder(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				System.exit(1);
			}
			
		}
		
		return (HashSetNonLeafNodeBuilderIntegrativeUIController<E>) this.nonLeafNodeBuilderIntegrativeUIController;
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
		this.getIntegrativeUIController().setModifiable(modifiable);
	}
	/**
	 * instead of set every child {@link NodeBuilder} to default empty, remove all the entries and delete all child {@link NodeBuilder}
	 * @throws IOException 
	 */
	@Override
	public final boolean setToDefaultEmpty() {
		boolean changed = this.getCurrentStatus().setToDefaultEmpty();
		
		this.getIntegrativeUIController().removeAllElements();
		
		this.getChildrenNodeBuilderNameMap().clear();
		
		return changed;
	}

	
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
				this.getIntegrativeUIController().setValue((HashSet<E>)value);
			}
			
		}
		
		return changed;
	}
	
	
	
	@Override
	protected HashSet<E> build() {
		HashSet<E> ret = new HashSet<>();
		
		for(SetFeatureElementController<E> element:this.getIntegrativeUIController().getElementControllerParentNodeMap().keySet()) {
			if(ret.contains(element.getElementBuilder().getCurrentValue())){
				throw new VisframeException("duplicate set element is found!");
			}
			
			ret.add(element.getElementBuilder().getCurrentValue());
		}
		
		return ret;
	}
}
