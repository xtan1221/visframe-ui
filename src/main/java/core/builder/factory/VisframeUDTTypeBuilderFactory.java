package core.builder.factory;

import java.io.IOException;
import java.sql.SQLException;

import basic.lookup.VisframeUDT;
import core.builder.NodeBuilder;


/**
 * interface for factory class that build a UI of any given valid value
 * 
 * 
 * @author tanxu
 *
 * @param <T>
 */
public interface VisframeUDTTypeBuilderFactory<T extends VisframeUDT>{
	
	/**
	 * return a node builder for the given entity of type U;
	 * @param u
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	NodeBuilder<? extends T,?> build(T u) throws IOException, SQLException;
	
}
