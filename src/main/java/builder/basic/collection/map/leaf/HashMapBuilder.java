package builder.basic.collection.map.leaf;

import java.util.HashMap;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import core.builder.factory.NodeBuilderFactory;


/**
 * builder a map with the default constraints and key value {@link NodeBuilderFactory};
 * 
 * @author tanxu
 *
 * @param <K>
 * @param <V>
 */
public class HashMapBuilder<K,V> extends LeafNodeBuilder<HashMap<K,V>, HashMapBuilderEmbeddedUIContentController<K,V>>{
	
	private final NodeBuilderFactory<K,?> mapKeyNodeBuilderFactory;
	private final NodeBuilderFactory<V,?> mapValueNodeBuilderFactory;
	
	protected HashMapBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			NodeBuilderFactory<K,?> mapKeyNodeBuilderFactory,
			NodeBuilderFactory<V,?> mapValueNodeBuilderFactory) {
		
		super(name, description, canBeNull, parentNodeBuilder, HashMapBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
		this.mapKeyNodeBuilderFactory = mapKeyNodeBuilderFactory;
		this.mapValueNodeBuilderFactory = mapValueNodeBuilderFactory;
	}
	
	
	
	
	public NodeBuilderFactory<K,?> getMapKeyNodeBuilderFactory() {
		return mapKeyNodeBuilderFactory;
	}

	public NodeBuilderFactory<V,?> getMapValueNodeBuilderFactory() {
		return mapValueNodeBuilderFactory;
	}
	
}
