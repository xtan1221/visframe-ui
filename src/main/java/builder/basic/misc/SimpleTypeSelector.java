package builder.basic.misc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

/**
 * selector of a value of type T;
 * 
 * the pool of possible values of T should be set by {@link #setPool(Collection)} method after {@link SimpleTypeSelector} instance is initialized;
 * 
 * @author tanxu
 *
 * @param <T>
 */
public class SimpleTypeSelector<T> extends LeafNodeBuilder<T, SimpleTypeSelectorEmbeddedUIContentController<T>> {
	
	/**
	 * function that return a String representation for a given type value;
	 */
	private final Function<T,String> typeToStringRepresentationFunction;
	
	/**
	 * function that return a String representation for a given type value;
	 */
	private final Function<T,String> typeToDescriptionFunction;
	
	//////////////////////////////////
	private Collection<T> pool;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param selectionSource
	 * @param typeToStringRepresentationFunction
	 * @param typeToDescriptionFunction
	 */
	public SimpleTypeSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Function<T,String> typeToStringRepresentationFunction,
			Function<T,String> typeToDescriptionFunction
			) {
		super(name, description, canBeNull, parentNodeBuilder, SimpleTypeSelectorEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
		this.typeToStringRepresentationFunction = typeToStringRepresentationFunction;
		this.typeToDescriptionFunction = typeToDescriptionFunction;
	}
	
	/**
	 * set the pool to the given one;
	 * 
	 * this will set the current {@link Status} to default empty;
	 * @param pool
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void setPool(Collection<T> pool) throws SQLException, IOException {
		if(pool==null) {
			throw new IllegalArgumentException("given pool cannot be null!");
		}
		this.setToDefaultEmpty();
		
		this.pool = pool;
		
		this.getEmbeddedUIContentController().setPool(pool);
	}
	
	
	/**
	 * @return the pool
	 */
	public Collection<T> getPool() {
		return pool;
	}
	
	
	//////////////////////////
	/**
	 * @return the typeToStringRepresentationFunction
	 */
	public Function<T, String> getTypeToStringRepresentationFunction() {
		return typeToStringRepresentationFunction;
	}

	/**
	 * @return the typeToDescriptionFunction
	 */
	public Function<T, String> getTypeToDescriptionFunction() {
		
		return typeToDescriptionFunction;
	}
	


}
