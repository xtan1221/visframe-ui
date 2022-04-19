package builder.basic.collection.misc;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;

import com.google.common.base.Predicate;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import operation.graph.build.BuildGraphFromSingleExistingRecordOperation;
import utils.Pair;

/**
 * specifically designed for building of 
 * {@link BuildGraphFromSingleExistingRecordOperation#SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET} and 
 * {@link BuildGraphFromSingleExistingRecordOperation#SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET}
 * 
 * for a {@link BuildGraphFromSingleExistingRecordOperationBuilder_dump}
 * 
 * 
 * ===============================
 * create a list of Pairs of type T from a pool of type T;
 * each value from the pool can only be selected as the first or second element of one single Pair;
 * 
 * two list of type R will be built from the list of Pairs, each represent the first and second element of all pairs;
 * 
 * 
 * note that each T in the same pool must have unique R value;
 * 
 * 
 * ===============================
 * note that empty set is allowed
 * @author tanxu
 *
 * @param <T> 
 * @param <R>
 */
public class PairSetSelector<T, R> extends LeafNodeBuilder<Pair<LinkedHashSet<R>, LinkedHashSet<R>>, PairSetSelectorEmbeddedUIContentController<T, R>>{
	/**
	 * from T to R function;
	 * R is the returned type
	 * note that each T in a pool must have unique R value;
	 * 
	 */
	private final Function<T,R> returnedTypeFunction;
	private final Function<T,String> toStringFunction;
	private final Predicate<Pair<T,T>> pairConstraint;
	private final String pairConstraintDescription;
	

	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param returnedTypeFunction
	 * @param toStringFunction
	 * @param pairConstraint
	 * @param pairConstraintDescription
	 */
	public PairSetSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Function<T,R> returnedTypeFunction, 
			Function<T,String> toStringFunction,
			Predicate<Pair<T,T>> pairConstraint,
			String pairConstraintDescription
			
			) {
		super(name, description, canBeNull, parentNodeBuilder, PairSetSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		this.returnedTypeFunction = returnedTypeFunction;
		this.toStringFunction = toStringFunction;
		this.pairConstraint = pairConstraint;
		this.pairConstraintDescription = pairConstraintDescription;
		
		
	}
	
	
	public void setPool(Collection<T> pool) {
		this.getEmbeddedUIContentController().setPool(pool);
	}

	
	///////////////
	public Predicate<Pair<T,T>> getPairConstraint() {
		return pairConstraint;
	}
	

	public String getPairConstraintDescription() {
		return pairConstraintDescription;
	}

	
	public Function<T,R> getReturnedTypeFunction() {
		return returnedTypeFunction;
	}


	public Function<T,String> getToStringFunction() {
		return toStringFunction;
	}

}
