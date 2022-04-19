package core.builder.factory;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NodeBuilder;
import core.builder.NonLeafNodeBuilder;
import core.builder.ui.embedded.content.EmbeddedUIContentController;



/**
 * factory that can build {@link NodeBuilder} instances of the same type and with the same set of constructor parameters;
 * 
 * @author tanxu
 *
 * @param <T>
 * @param <E>
 */
public interface NodeBuilderFactory<T,E extends EmbeddedUIContentController<T>> {
	
	/**
	 * set parent NonLeafNodeBuilder, must be set before {@link #build()} is invoked
	 * @param parent
	 */
	void setParent(NonLeafNodeBuilder<?> parent);
	
	/**
	 * whether the built NodeBuilders can be null or not;
	 * @return
	 */
	boolean canBeNull();
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 */
	NodeBuilder<T,E> build() throws SQLException, IOException;
}
