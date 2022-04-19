package builder.basic.collection.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import operation.sql.predefined.type.AddNumericCumulativeColumnOperation;
import utils.Pair;



/**
 * select a subset from a pool for type E1;
 * 
 * then for each selected element from E1, select a value from a pool for type E2
 * 
 * it is allowed that the selected set from pool of E1 be empty, thus the built set and list will both be empty;
 * =====================================
 * this is specifically for building value for {@link AddNumericCumulativeColumnOperation}'s
 * 
 * 		{@link AddNumericCumulativeColumnOperation#ORDER_BY_COLUMN_NAME_SET} and
 * 		{@link AddNumericCumulativeColumnOperation#ORDER_BY_COLUMN_SORT_TYPE_LIST}
 * 
 * together as they are closely correlated;
 * 
 * @author tanxu
 *
 * @param <E1>
 * @param <E2>
 */
public class DoubleListSelector<E1,E2> extends LeafNodeBuilder<Pair<LinkedHashSet<E1>, ArrayList<E2>>, DoubleListSelectorEmbeddedUIContentController<E1,E2>>{
	private final Function<E1,String> e1ToStringFunction;
	private final Function<E2,String> e2ToStringFunction;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param e1ToStringFunction
	 * @param e2ToStringFunction
	 */
	public DoubleListSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Function<E1,String> e1ToStringFunction,
			Function<E2,String> e2ToStringFunction
			) {
		super(name, description, canBeNull, parentNodeBuilder, DoubleListSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		
		this.e1ToStringFunction = e1ToStringFunction;
		this.e2ToStringFunction = e2ToStringFunction;
	}
	
	/**
	 * 
	 * @param e1Pool
	 * @param e2Pool
	 */
	public void setPools(Collection<E1> e1Pool, Collection<E2> e2Pool) {
		if(e1Pool==null||e1Pool.isEmpty()) {
			throw new IllegalArgumentException("given e1Pool cannot be null or empty!");
		}
		if(e2Pool==null||e2Pool.isEmpty()) {
			throw new IllegalArgumentException("given e2Pool cannot be null or empty!");
		}
		this.getEmbeddedUIContentController().setPools(e1Pool, e2Pool);
	}


	/////////////////////////////
	public Function<E2,String> getE2ToStringFunction() {
		return e2ToStringFunction;
	}

	public Function<E1,String> getE1ToStringFunction() {
		return e1ToStringFunction;
	}
}
