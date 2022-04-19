package builder.basic.misc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;

import core.builder.factory.NodeBuilderFactoryBase;

/**
 * factory class for {@link SimpleTypeSelector} with a fixed pool for all instances
 * @author tanxu
 *
 * @param <T>
 */
public class SimpleTypeSelectorFactory<T> extends NodeBuilderFactoryBase<T, SimpleTypeSelectorEmbeddedUIContentController<T>>{
	/**
	 * function that return a String representation for a given type value;
	 */
	private final Function<T,String> typeToStringRepresentationFunction;
	
	/**
	 * function that return a String representation for a given type value;
	 */
	private final Function<T,String> typeToDescriptionFunction;
	
	//
	private Collection<T> pool;
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param selectionSource
	 * @param typeToStringRepresentationFunction
	 * @param typeToDescriptionFunction
	 */
	public SimpleTypeSelectorFactory(
			String name, String description, boolean canBeNull,
			
//			Collection<T> pool,
			Function<T,String> typeToStringRepresentationFunction,
			Function<T,String> typeToDescriptionFunction
			) {
		super(name, description, canBeNull, SimpleTypeSelectorEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
		
//		this.pool = pool;
		this.typeToStringRepresentationFunction = typeToStringRepresentationFunction;
		this.typeToDescriptionFunction = typeToDescriptionFunction;
	}
	
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

	public Collection<T> getPool() {
		return pool;
	}
	
	/**
	 * @param pool the pool to set
	 */
	public void setPool(Collection<T> pool) {
		this.pool = pool;
	}
	
	/////////////////////////
	@Override
	public SimpleTypeSelector<T> build() throws SQLException, IOException {
		SimpleTypeSelector<T> ret = new SimpleTypeSelector<>(
				this.getName(), this.getDescription(), this.canBeNull(), this.getParentNodeBuilder(),
				this.getTypeToStringRepresentationFunction(), this.getTypeToDescriptionFunction()
				);
		ret.setPool(this.getPool());
		
		return ret;
	}

}
