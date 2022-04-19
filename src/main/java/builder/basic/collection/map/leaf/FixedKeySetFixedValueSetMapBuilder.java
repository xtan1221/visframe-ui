package builder.basic.collection.map.leaf;

import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

/**
 * 
 * note that map key type K and value type V should override {@link Object#equals(Object)} and {@link Object#hashCode()} methods;
 * 
 * @author tanxu
 *
 * @param <K>
 * @param <V>
 */
public class FixedKeySetFixedValueSetMapBuilder<K,V> extends LeafNodeBuilder<Map<K,V>, FixedKeySetFixedValueSetMapBuilderEmbeddedUIContentController<K,V>>{

	private final Function<K,String> mapKeyToStringRepresentationFunction;
	private final Function<K,String> mapKeyToDescriptionFunction;
	
	private final Function<V,String> mapValueToStringRepresentationFunction;
	private final Function<V,String> mapValueToDescriptionFunction;
	
	/**
	 * filtering condition to check if a pair of map key and value can be put in a map entry;
	 * if null, no constraints;
	 */
	private final BiPredicate<K,V> keyValuePairBiPredicate;
	
	/**
	 * 
	 */
	private final boolean allowingDuplicateMapValue;
	
	/**
	 * 
	 */
	private final boolean allowingNullMapValue;
	
	/////////////////////////////
	private Set<K> keySet;
	private Set<V> valueSet;
	
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param mapKeyToStringRepresentationFunction
	 * @param mapKeyToDescriptionFunction
	 * @param mapValueToStringRepresentationFunction
	 * @param mapValueToDescriptionFunction
	 * @param keyValuePairBiPredicate
	 * @param allowingNullMapValue
	 * @param allowingDuplicateMapValue
	 */
	public FixedKeySetFixedValueSetMapBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Function<K,String> mapKeyToStringRepresentationFunction,
			Function<K,String> mapKeyToDescriptionFunction,
			Function<V,String> mapValueToStringRepresentationFunction,
			Function<V,String> mapValueToDescriptionFunction,
			
			BiPredicate<K,V> keyValuePairBiPredicate,
			
			boolean allowingNullMapValue,
			boolean allowingDuplicateMapValue
			) {
		super(name, description, canBeNull, parentNodeBuilder, FixedKeySetFixedValueSetMapBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
		
		this.mapKeyToStringRepresentationFunction = mapKeyToStringRepresentationFunction;
		this.mapKeyToDescriptionFunction = mapKeyToDescriptionFunction;
		this.mapValueToStringRepresentationFunction = mapValueToStringRepresentationFunction;
		this.mapValueToDescriptionFunction = mapValueToDescriptionFunction;
		
		this.keyValuePairBiPredicate = keyValuePairBiPredicate;
		
		this.allowingNullMapValue = allowingNullMapValue;
		this.allowingDuplicateMapValue = allowingDuplicateMapValue;
	}
	
	
	
	/**
	 * @return the keySet
	 */
	public Set<K> getKeySet() {
		return keySet;
	}

	/**
	 * @return the valueSet
	 */
	public Set<V> getValueSet() {
		return valueSet;
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
	 * @return the mapValueToStringRepresentationFunction
	 */
	public Function<V, String> getMapValueToStringRepresentationFunction() {
		return mapValueToStringRepresentationFunction;
	}

	/**
	 * @return the mapValueToDescriptionFunction
	 */
	public Function<V, String> getMapValueToDescriptionFunction() {
		return mapValueToDescriptionFunction;
	}

	/**
	 * @return the keyValuePairBiPredicate
	 */
	public BiPredicate<K, V> getKeyValuePairBiPredicate() {
		return keyValuePairBiPredicate;
	}

	/**
	 * @return the allowingDuplicateMapValue
	 */
	public boolean isAllowingDuplicateMapValue() {
		return allowingDuplicateMapValue;
	}

	/**
	 * @return the allowingNullMapValue
	 */
	public boolean isAllowingNullMapValue() {
		return allowingNullMapValue;
	}

	
	////////////////////////////////
	
	
	
	/**
	 * set the key set, also trigger resetting of the UI
	 * @param keySet the keySet to set
	 */
	public void setMapKeySet(Set<K> keySet) {
//		if(this.valueSet!=null)
//			if(!allowingNullMapValue && allowingDuplicateMapValue)
//				if(valueSet.size()<keySet.size())
//					throw new IllegalArgumentException("given value set size is less than the key set size while not allowing null nor duplicate map value!");
		
		if(this.keySet!=null && this.keySet.equals(keySet))
			return;
		
		
		this.keySet = keySet;
		
		
		this.getEmbeddedUIContentController().updateMapKeySet();
	}
	
	
	
	/**
	 * set the value set, also trigger resetting of the UI
	 * @param valueSet the valueSet to set
	 */
	public void setMapValueSet(Set<V> valueSet) {
//		if(this.keySet!=null)
//			if(!allowingNullMapValue && allowingDuplicateMapValue)
//				if(valueSet.size()<keySet.size())
//					throw new IllegalArgumentException("given value set size is less than the key set size while not allowing null nor duplicate map value!");
		
		if(this.valueSet!=null && this.valueSet.equals(valueSet))
			return;
		
		
		this.valueSet = valueSet;
		
		this.getEmbeddedUIContentController().updateMapValueSet();
	}
	
	
	/////////////////////////////
//	@Override
//	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
//		this.setValue(value, isEmpty)
//	}
}
