package builder.basic.collection.misc;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import builder.visframe.operation.graph.build.BuildGraphFromTwoExistingRecordOperationBuilder;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import operation.graph.build.BuildGraphFromTwoExistingRecordOperation;
import utils.Pair;

/**
 * specifically designed for 
 * {@link BuildGraphFromTwoExistingRecordOperation#NODE_ID_COLUMN_NAME_EDGE_SOURCE_NODE_ID_COLUMN_NAME_MAP} and
 * {@link BuildGraphFromTwoExistingRecordOperation#NODE_ID_COLUMN_NAME_EDGE_SINK_NODE_ID_COLUMN_NAME_MAP}
 * 
 * for a {@link BuildGraphFromTwoExistingRecordOperationBuilder}
 * 
 * build two maps with map keys predefined and map values selected from the same pool and the values of the two maps should be disjoint;
 * 
 * =====================
 * only when a valid value for all values of both maps are selected, the {@link Status} of owner {@link DoubleMapValueSelector} will be updated!
 * 
 * @author tanxu
 *
 * @param <K1> input map key type
 * @param <V1> input type for map value pool
 * @param <K2> map key type for built map
 * @param <V2> map value type for built map
 */
public class DoubleMapValueSelector<K1, V1, K2, V2> extends LeafNodeBuilder<Pair<LinkedHashMap<K2,V2>, LinkedHashMap<K2,V2>>, DoubleMapValueSelectorEmbeddedUIContentController<K1,V1, K2,V2>>{
	
	private final Function<K1,K2> mapKeyFunction;
	private final Function<V1,V2> mapValueFunction;
	private final Function<K1,String> inputMapKeyToStringFunction;
	private final Function<V1,String> inputMapValueToStringFunction;
	
	private final Predicate<Pair<K1,V1>> mapKeyValueConstraint;
	private final String mapKeyValueConstraintDescription;
	
	
	/////////////////////
	public DoubleMapValueSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Function<K1,K2> mapKeyFunction,
			Function<V1,V2> mapValueFunction,
			Function<K1,String> inputMapKeyToStringFunction,
			Function<V1,String> inputMapValueToStringFunction,
			Predicate<Pair<K1,V1>> mapKeyValueConstraint,
			String mapKeyValueConstraintDescription
			) {
		super(name, description, canBeNull, parentNodeBuilder, DoubleMapValueSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.mapKeyFunction = mapKeyFunction;
		this.mapValueFunction = mapValueFunction;
		this.inputMapKeyToStringFunction = inputMapKeyToStringFunction;
		this.inputMapValueToStringFunction = inputMapValueToStringFunction;
		this.mapKeyValueConstraint = mapKeyValueConstraint;
		this.mapKeyValueConstraintDescription = mapKeyValueConstraintDescription;
	}
	
	
	public void setPool(Collection<K1> predefinedMapKeys, Collection<V1> selectionMapValuePool) {
		this.getEmbeddedUIContentController().setPool(predefinedMapKeys, selectionMapValuePool);
	}
	
	
	
	//////////////////////////////
	/**
	 * @return the mapKeyFunction
	 */
	public Function<K1, K2> getMapKeyFunction() {
		return mapKeyFunction;
	}


	/**
	 * @return the mapValueFunction
	 */
	public Function<V1, V2> getMapValueFunction() {
		return mapValueFunction;
	}


	/**
	 * @return the mapKeyValueConstraint
	 */
	public Predicate<Pair<K1, V1>> getMapKeyValueConstraint() {
		return mapKeyValueConstraint;
	}


	/**
	 * @return the mapKeyValueConstraintDescription
	 */
	public String getMapKeyValueConstraintDescription() {
		return mapKeyValueConstraintDescription;
	}


	public Function<K1,String> getInputMapKeyToStringFunction() {
		return inputMapKeyToStringFunction;
	}


	public Function<V1,String> getInputMapValueToStringFunction() {
		return inputMapValueToStringFunction;
	}
	
}
