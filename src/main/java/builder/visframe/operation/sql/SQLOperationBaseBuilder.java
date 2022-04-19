package builder.visframe.operation.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import basic.SimpleName;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.operation.AbstractOperationBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import metadata.MetadataName;
import operation.sql.SQLOperationBase;
import static operation.sql.SQLOperationBase.*;

/**
 * 
 * @author tanxu
 * 
 * @param <T>
 */
public abstract class SQLOperationBaseBuilder<T extends SQLOperationBase> extends AbstractOperationBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected SQLOperationBaseBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
	}
	
	/////////////////////////////////////////////
	
	protected Map<SimpleName, Object> buildSQLOperationBaseLevelSpecificParameterNameValueObjectMap(){
		MetadataName outputRecordDataName = (MetadataName) this.getChildrenNodeBuilderNameMap().get(OUTPUT_RECORD_DATA_ID.getName().getStringValue()).getCurrentValue();
		
		return SQLOperationBase.buildSQLOperationBaseLevelSpecificParameterNameValueObjectMap(outputRecordDataName);
	}
	
	
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		
		///////////////////////////////////
		//OUTPUT_RECORD_DATA_NAME
		this.addChildNodeBuilder(new VfNameStringBuilder<MetadataName>(
				OUTPUT_RECORD_DATA_ID.getName().getStringValue(), OUTPUT_RECORD_DATA_ID.getDescriptiveName(),
				OUTPUT_RECORD_DATA_ID.canHaveNullValueObject(this.isForReproducing()), this,
				MetadataName.class
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
			this.getChildrenNodeBuilderNameMap().get(OUTPUT_RECORD_DATA_ID.getName().getStringValue()).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T SQLOperationBase = (T)value;
				
				///////////////////////
				this.getChildrenNodeBuilderNameMap().get(OUTPUT_RECORD_DATA_ID.getName().getStringValue()).setValue(
						SQLOperationBase.getOutputRecordDataID().getName(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected abstract T build();


}
