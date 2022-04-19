package builder.visframe.operation.sql.generic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.operation.sql.SQLOperationBaseBuilder;
import builder.visframe.operation.sql.generic.utils.GenericSqlQueryBuilder;
import context.project.VisProjectDBContext;
import operation.sql.generic.GenericSQLOperation;
import operation.sql.generic.GenericSQLQuery;
import static operation.sql.generic.GenericSQLOperation.*;

/**
 * 
 * @author tanxu
 *
 */
public class GenericSQLOperationBuilder extends SQLOperationBaseBuilder<GenericSQLOperation>{

	public static final String NODE_NAME = "GenericSQLOperation";
	public static final String NODE_DESCRIPTION = "GenericSQLOperation";
	
	//////////////////////////////////////
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param resultedFromReproducing
	 * @throws SQLException
	 * @throws IOException
	 */
	public GenericSQLOperationBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	/////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	private Map<SimpleName, Object> buildGenericSQLOperationLevelSpecificParameterNameValueObjectMap(){
		GenericSQLQuery genericSQLQuery = 
				(GenericSQLQuery) this.getChildrenNodeBuilderNameMap().get(GENERIC_SQL_QUERY.getName().getStringValue()).getCurrentValue();
		
		return GenericSQLOperation.buildGenericSQLOperationLevelSpecificParameterNameValueObjectMap(genericSQLQuery);
	}

	////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//GENERIC_SQL_QUERY
		this.addChildNodeBuilder(new GenericSqlQueryBuilder(
				GENERIC_SQL_QUERY.getName().getStringValue(), GENERIC_SQL_QUERY.getDescriptiveName(),
				GENERIC_SQL_QUERY.canHaveNullValueObject(this.isForReproducing()), this, this.getHostVisProjectDBContext()
				));
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
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(GENERIC_SQL_QUERY.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				GenericSQLOperation rerootTreeOperation = (GenericSQLOperation)value;
				
				//////////
				this.getChildrenNodeBuilderNameMap().get(GENERIC_SQL_QUERY.getName().getStringValue()).setValue(
						rerootTreeOperation.getGenericSQLQuery(), isEmpty);
				
				//////////////////////////////////////////////
				/////
				this.checkIfForReproducing(rerootTreeOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since GenericSQLOperation type does not have parameter dependent on input data table content; 
	}
	
	
	@Override
	protected GenericSQLOperation build() {
		return new GenericSQLOperation(
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSQLOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildGenericSQLOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder???
				);
	}

	
}
