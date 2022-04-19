package builder.visframe.operation.graph;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import basic.SimpleName;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import operation.graph.InputGraphTypeBoundedOperation;


/**
 * no level specific parameter
 * @author tanxu
 *
 * @param <T>
 */
public abstract class InputGraphTypeBoundedOperationBuilder<T extends InputGraphTypeBoundedOperation> extends SingleGenericGraphAsInputOperationBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected InputGraphTypeBoundedOperationBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing
			) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectDBContext, resultedFromReproducing);
	}
	
	/////////////////////////////////
	protected Map<SimpleName, Object> buildInputGraphTypeBoundedOperationLevelSpecificParameterNameValueObjectMap(){
		return InputGraphTypeBoundedOperation.buildInputGraphTypeBoundedOperationLevelSpecificParameterNameValueObjectMap();
	}
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
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
