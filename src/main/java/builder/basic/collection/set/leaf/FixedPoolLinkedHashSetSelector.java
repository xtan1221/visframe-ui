package builder.basic.collection.set.leaf;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

/**
 * builder for a LinkedHashSet by selecting from a pre-defined fixed pool;
 * 
 * the pool should be set by {@link #setPool(Collection)} method after the {@link FixedPoolLinkedHashSetSelector} instance is initialized;
 * 
 * 
 * 
 * the built LinkedHashSet is allowed to be empty!
 * @author tanxu
 * 
 */
public class FixedPoolLinkedHashSetSelector<T> extends LeafNodeBuilder<LinkedHashSet<T>, FixedPoolLinkedHashSetSelectorEmbeddedUIContentController<T>>{
	/**
	 * 
	 */
	private final Function<T,String> elementToStringFunction;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param elementToStringFunction
	 */
	public FixedPoolLinkedHashSetSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Function<T,String> elementToStringFunction
			) {
		super(name, description, canBeNull, parentNodeBuilder, FixedPoolLinkedHashSetSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.elementToStringFunction = elementToStringFunction;
	}

	public Function<T,String> getElementToStringFunction() {
		return elementToStringFunction;
	}
	
	//////////////////////
	public void setPool(Collection<T> elements) {
		this.getEmbeddedUIContentController().resetPool(elements);
	}

}
