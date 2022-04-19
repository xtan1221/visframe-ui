package builder.visframe.operation.sql.predefined;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.visframe.metadata.MetadataIDSelector;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import metadata.DataType;
import metadata.MetadataID;
import operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperation;
import static operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperation.*;

public abstract class SingleInputRecordDataPredefinedSQLOperationBuilder<T extends SingleInputRecordDataPredefinedSQLOperation> extends PredefinedSQLBasedOperationBuilder<T>{
	
	private final VisProjectDBContext hostVisProjectDBContext;
	
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected SingleInputRecordDataPredefinedSQLOperationBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing
			) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	

	protected VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	

	protected Set<DataType> getSelectedDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.RECORD);
		
		return ret;
	}
	
	/////////////////////////////////////////////
	
	protected Map<SimpleName, Object> buildSingleInputRecordDataPredefinedSQLOperationLevelSpecificParameterNameValueObjectMap(){
		MetadataID inputRecordDataMetadataID = (MetadataID) this.getChildrenNodeBuilderNameMap().get(INPUT_RECORD_METADATAID.getName().getStringValue()).getCurrentValue();
		return SingleInputRecordDataPredefinedSQLOperation.buildSingleInputRecordDataPredefinedSQLOperationLevelSpecificParameterNameValueObjectMap(inputRecordDataMetadataID);
	}
	
	
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		
		//INPUT_RECORD_METADATAID
		this.addChildNodeBuilder(
				new MetadataIDSelector(
						INPUT_RECORD_METADATAID.getName().getStringValue(), INPUT_RECORD_METADATAID.getDescriptiveName(), 
						INPUT_RECORD_METADATAID.canHaveNullValueObject(this.isForReproducing()), this, 
						this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
						this.getSelectedDataTypeSet(),
						null
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
			this.getChildrenNodeBuilderNameMap().get(INPUT_RECORD_METADATAID.getName().getStringValue()).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T singleInputRecordDataPredefinedSQLOperation = (T)value;
			
				///////////////////////
				this.getChildrenNodeBuilderNameMap().get(INPUT_RECORD_METADATAID.getName().getStringValue()).setValue(
						singleInputRecordDataPredefinedSQLOperation.getInputRecordDataMetadataID(), isEmpty);
				
			}
		}
		return changed;
	}
	
	
	@Override
	protected abstract T build();
}
