package builder.basic.collection.map.nonleaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import builder.basic.collection.map.MapFeatureEntryController;
import core.builder.NonLeafNodeBuilder;
import core.builder.factory.NodeBuilderFactory;
import exception.VisframeException;
import javafx.fxml.FXMLLoader;

/**
 * builder for a HashMap;
 * 
 * whether map key and value can be null or not is implicitly defined by the NodeBuilderFactory for map key and value?
 * 
 * @author tanxu
 *
 * @param <K>
 * @param <V>
 */
public class HashMapNonLeafNodeBuilder<K,V> extends NonLeafNodeBuilder<HashMap<K,V>>{
	
	private final NodeBuilderFactory<K,?> mapKeyNodeBuilderFactory;
	private final NodeBuilderFactory<V,?> mapValueNodeBuilderFactory;
	
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param mapKeyNodeBuilderFactory
	 * @param mapValueNodeBuilderFactory
	 */
	public HashMapNonLeafNodeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			NodeBuilderFactory<K,?> mapKeyNodeBuilderFactory,
			NodeBuilderFactory<V,?> mapValueNodeBuilderFactory
			) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		
		this.mapKeyNodeBuilderFactory = mapKeyNodeBuilderFactory;
		this.mapValueNodeBuilderFactory = mapValueNodeBuilderFactory;
		
		//
		this.mapKeyNodeBuilderFactory.setParent(this);
		this.mapValueNodeBuilderFactory.setParent(this);
	}
	

	public NodeBuilderFactory<K,?> getMapKeyNodeBuilderFactory() {
		return mapKeyNodeBuilderFactory;
	}


	public NodeBuilderFactory<V,?> getMapValueNodeBuilderFactory() {
		return mapValueNodeBuilderFactory;
	}
	
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
	public HashMapNonLeafNodeBuilderIntegrativeUIController<K,V> getIntegrativeUIController() {
		if(this.nonLeafNodeBuilderIntegrativeUIController == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(HashMapNonLeafNodeBuilderIntegrativeUIController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
				this.nonLeafNodeBuilderIntegrativeUIController = (HashMapNonLeafNodeBuilderIntegrativeUIController<K,V>)loader.getController();
		    	
		    	this.nonLeafNodeBuilderIntegrativeUIController.setOwnerNonLeafNodeBuilder(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
	    	
		}
		
		return (HashMapNonLeafNodeBuilderIntegrativeUIController<K, V>) this.nonLeafNodeBuilderIntegrativeUIController;
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
		
		this.getIntegrativeUIController().removeAllEntry();
		
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
	
	
	///////////////////////////////
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
				this.getIntegrativeUIController().setValue((HashMap<K,V>)value);
			}
			
		}
		
		return changed;
	}
	
	
	
	@Override
	protected HashMap<K, V> build() {
		HashMap<K, V> ret = new HashMap<>();
		
		for(MapFeatureEntryController<K, V> key:this.getIntegrativeUIController().getEntryControllerParentNodeMap().keySet()) {
			if(ret.containsKey(key.getKeyBuilder().getCurrentValue())){
				throw new VisframeException("duplicate map key is found!");
			}
			
			ret.put(key.getKeyBuilder().getCurrentValue(), key.getValueBuilder().getCurrentValue());
		}
		
		// TODO Auto-generated method stub
		return ret;
	}
	
}
