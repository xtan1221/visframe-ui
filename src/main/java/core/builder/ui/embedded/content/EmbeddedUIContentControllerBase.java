package core.builder.ui.embedded.content;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NodeBuilder;

public abstract class EmbeddedUIContentControllerBase<T> implements EmbeddedUIContentController<T> {
	
	protected NodeBuilder<T,?> ownerNodeBuilder;
	
	
	/**
	 * set the owner {@link NodeBuilder}
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setOwnerNodeBuilder(NodeBuilder<T,?> ownerNodeBuilder) throws IOException, SQLException {
		this.ownerNodeBuilder = ownerNodeBuilder;
		//initialized to default empty
		this.getOwnerNodeBuilder().setToDefaultEmpty();
	}
	
	
	@Override
	public abstract NodeBuilder<T,?> getOwnerNodeBuilder();
	
}
