package builder.visframe.function.group;

import java.io.IOException;
import java.sql.SQLException;

import basic.lookup.project.type.udt.VisProjectMetadataManager;
import core.builder.NonLeafNodeBuilder;
import function.group.GraphicsPropertyCFG;


/**
 * no level specific parameters
 * @author tanxu
 *
 * @param <T>
 */
public abstract class GraphicsPropertyCFGBuilder<T extends GraphicsPropertyCFG> extends AbstractCompositionFunctionGroupBuilder<T>{
	
	
	protected GraphicsPropertyCFGBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, VisProjectMetadataManager hostVisProjectMetadataManager) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectMetadataManager);
		// TODO Auto-generated constructor stub
	}
	
	
	///////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		//
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		return changed;
	}
	
	
	@Override
	protected abstract T build();
}
