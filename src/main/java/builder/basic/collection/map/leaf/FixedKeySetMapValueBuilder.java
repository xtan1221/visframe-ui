package builder.basic.collection.map.leaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import core.builder.LeafNodeBuilder;
import core.builder.NodeBuilder;
import core.builder.NonLeafNodeBuilder;
import core.builder.factory.NodeBuilderFactory;

/**
 * builder for a map whose map key set is pre-defined and fixed and a non-null value for each key is built with a {@link NodeBuilder}
 * 
 * 
 * value type V's equals() and hashCode() methods will be used if {@link #allowingDuplicateValue} is false!!!
 * 
 * @author tanxu
 * 
 */
public class FixedKeySetMapValueBuilder<K,V> extends LeafNodeBuilder<Map<K,V>, FixedKeySetMapValueBuilderEmbeddedUIContentController<K,V>>{
	
	private final NodeBuilderFactory<V,?> mapValueNodeBuilderFactory;
	private final Function<K,String> mapKeyToStringRepresentationFunction;
	private final Function<K,String> mapKeyToDescriptionFunction;
	
	/**
	 * any action need to be done for each map entry
	 */
	private final BiConsumer<K,NodeBuilder<V,?>> mapValueBuilderAction;
	
	private final boolean allowingNullValue;
	private final boolean allowingDuplicateValue;
	
	//////////////////
	private Set<K> keySet;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param mapValueNodeBuilderFactory
	 * @param mapKeyToStringRepresentationFunction
	 * @param mapKeyToDescriptionFunction
	 * @param mapEntryAction can be null
	 * @param allowingNullMapValue
	 * @param allowingDuplicateMapValue
	 */
	public FixedKeySetMapValueBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			NodeBuilderFactory<V,?> mapValueNodeBuilderFactory,
			Function<K,String> mapKeyToStringRepresentationFunction,
			Function<K,String> mapKeyToDescriptionFunction,
			BiConsumer<K,NodeBuilder<V,?>> mapValueBuilderAction,
			
			boolean allowingNullMapValue,
			boolean allowingDuplicateMapValue
			) {
		super(name, description, canBeNull, parentNodeBuilder, FixedKeySetMapValueBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
		
		this.mapValueNodeBuilderFactory = mapValueNodeBuilderFactory;
		
		this.mapKeyToStringRepresentationFunction = mapKeyToStringRepresentationFunction;
		this.mapKeyToDescriptionFunction = mapKeyToDescriptionFunction;
		this.mapValueBuilderAction = mapValueBuilderAction;
		
		this.allowingNullValue = allowingNullMapValue;
		this.allowingDuplicateValue = allowingDuplicateMapValue;
	}
	

	public void setKeySet(Set<K> keySet) throws SQLException, IOException {
		this.keySet = keySet;
		this.getEmbeddedUIContentController().setMapKeySet(keySet);
	}
	
	/////////////////////////////////////////////////////////////////
	/**
	 * return the current set of key values and the node builders
	 * @return
	 */
	public Map<K, NodeBuilder<V,?>> getMapKeyValueBuilderMap(){
		Map<K, NodeBuilder<V,?>> ret = new LinkedHashMap<>();
		
		this.getEmbeddedUIContentController().getMapKeyEntryHBoxMap().forEach((k,v)->{
			ret.put(k, v.valueBuilder);
		});
		
		return ret;
	}
	
	/**
	 * @return the keySet
	 */
	public Set<K> getKeySet() {
		return keySet;
	}

	/**
	 * @return the mapValueNodeBuilderFactory
	 */
	public NodeBuilderFactory<V, ?> getMapValueNodeBuilderFactory() {
		return mapValueNodeBuilderFactory;
	}
	
	/**
	 * return the current set of map value NodeBuilders
	 * @return
	 */
	public Set<? extends NodeBuilder<V,?>> getValueNodeBuilderSet(){
		return this.getEmbeddedUIContentController().getValueNodeBuilderSet();
	}
	
	/**
	 * @return the mapKeyToStringRepresentationFunction
	 */
	public Function<K, String> getMapKeyToStringRepresentationFunction() {
		return mapKeyToStringRepresentationFunction;
	}

	/**
	 * @return the mapKeyToDescriptionFunction
	 */
	public Function<K, String> getMapKeyToDescriptionFunction() {
		return mapKeyToDescriptionFunction;
	}

	
	/**
	 * @return the allowingNullValue
	 */
	public boolean isAllowingNullValue() {
		return allowingNullValue;
	}

	/**
	 * @return the allowingDuplicateValue
	 */
	public boolean isAllowingDuplicateValue() {
		return allowingDuplicateValue;
	}


	public BiConsumer<K,NodeBuilder<V,?>> getMapValueBuilderAction() {
		return mapValueBuilderAction;
	}

}
