package builder.visframe.operation.sql.predefined;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.operation.sql.SQLOperationBaseBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import operation.sql.predefined.PredefinedSQLBasedOperation;


/**
 * no level specific parameter
 * @author tanxu
 *
 * @param <T>
 */
public abstract class PredefinedSQLBasedOperationBuilder<T extends PredefinedSQLBasedOperation> extends SQLOperationBaseBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected PredefinedSQLBasedOperationBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
	}
	
	/////////////////////////////////////////////
	
	protected Map<SimpleName, Object> buildPredefinedSQLBasedOperationLevelSpecificParameterNameValueObjectMap(){
		return PredefinedSQLBasedOperation.buildPredefinedSQLBasedOperationLevelSpecificParameterNameValueObjectMap();
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
