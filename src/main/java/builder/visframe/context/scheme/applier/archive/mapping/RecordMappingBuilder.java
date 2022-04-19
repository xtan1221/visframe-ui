package builder.visframe.context.scheme.applier.archive.mapping;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import builder.basic.collection.map.leaf.FixedKeySetFixedValueSetMapBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import context.project.VisProjectDBContext;
import context.scheme.appliedarchive.mapping.RecordMapping;
import context.scheme.appliedarchive.mapping.RecordMappingHelper;
import metadata.DataType;
import metadata.Metadata;
import metadata.record.RecordDataMetadata;
import rdb.table.data.DataTableColumn;

/**
 * builder class for a RecordMapping;
 * 
 * major steps
 * 1. select a record data from host VisProjectDBContext that can be mapped to the target record data
 * 		Metadata selector
 * 		basic constraints:
 * 			1. the primary key column set size must be the same with the target record data's
 * 			2. the non-primary key column set must be equal or larger than the target record data's non-primary key column set to be mapped;
 * 
 * 2. map primary key columns
 * 		map builder
 * 3. map non-primary key columns
 * 		map builder
 * 
 * 
 * @author tanxu
 *
 */
public class RecordMappingBuilder extends MetadataMappingBuilder<RecordMapping>{
	public static final String NODE_NAME = "RecordMapping";
	public static final String NODE_DESCRIPTION = "RecordMapping";
	/////////////////////
	private final RecordMappingHelper recordMappingHelper;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 * @param recordMappingHelper
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public RecordMappingBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			RecordMappingHelper recordMappingHelper
			) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext);
		// TODO Auto-generated constructor stub
		
		this.recordMappingHelper = recordMappingHelper;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	/**
	 * @return the recordMappingHelper
	 */
	public RecordMappingHelper getRecordMappingHelper() {
		return recordMappingHelper;
	}
	
	@Override
	protected Set<DataType> getAllowedMetadataDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.RECORD);
		return ret;
	}
	
	/**
	 * build (if not already) and return the {@link #filteringCondition} that check if a Metadata can be used to create the wanted RecordMapping;
	 * 
	 * note that the returned Predicate only checks the basic constraints regarding the primary key column set size and non primary key column set size;
	 * column data type compatibility is not checked
	 * thus, even if a Metadata passes this filter condition, it does not guarantee to result in a success RecordMapping;
	 * 
	 * @return
	 */
	@Override
	protected Predicate<Metadata> getMetadataFilteringCondition(){
		if(this.filteringCondition==null) {
			this.filteringCondition = (metadata)->{
				//first check data type
				if(!metadata.getDataType().equals(DataType.RECORD)) {
					return false;
				}
				
				//then check the data table schema columns
				//note that this only checks the basic constraints regarding the primary key column set size and non primary key column set size;
				//column data type compatibility is not checked
				//thus, even if a Metadata passes this filter condition, it does not guarantee to result in a success RecordMapping;
				RecordDataMetadata rdmd = (RecordDataMetadata)metadata;
				if(rdmd.getDataTableSchema().getPrimaryKeyColumnNameSet().size()!=this.getRecordMappingHelper().getTargetRecordDataPrimaryKeyColumnSetToBeMapped().size() ||
						rdmd.getDataTableSchema().getNonRUIDNonPrimaryKeyColNameSet().size()<this.getRecordMappingHelper().getTargetRecordDataNonPrimaryKeyColumnSetToBeMapped().size()) {
					return false;
				}
				
				return true;
			};
		}
		return this.filteringCondition;
	}
	
	
	//////////////////////////////////
	protected static final String sourceRecordMetadataID = "sourceRecordMetadataID";
	protected static final String sourceRecordMetadataID_description = "sourceRecordMetadataID";
	
	protected static final String targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMap = "targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMap";
	protected static final String targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMap_description = "targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMap";

	protected static final String targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMap = "targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMap";
	protected static final String targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMap_description = "targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMap";
	
	private MetadataIDSelector sourceRecordMetadataIDSelector;
	
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder;
	
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		this.sourceRecordMetadataIDSelector = new MetadataIDSelector(
				sourceRecordMetadataID, sourceRecordMetadataID_description, false, this,
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
				this.getAllowedMetadataDataTypeSet(),
				this.getMetadataFilteringCondition()
				);
		this.addChildNodeBuilder(this.sourceRecordMetadataIDSelector);

		
		//primary key columns map builder
		this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
				targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMap, targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMap_description, 
				false, this,
				
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
				(targetCol,sourceCol)->{return targetCol.getSqlDataType().isMappableFrom(sourceCol.getSqlDataType());}, //keyValuePairBiPredicate
				false,//boolean allowingNullMapValue,
				false//boolean allowingDuplicateMapValue
				);
		
		//set the key set
		this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setMapKeySet(this.recordMappingHelper.getTargetRecordDataPrimaryKeyColumnSetToBeMapped());
		this.addChildNodeBuilder(this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder);
		
		////////////////
		//non-primary key columns map builder
		this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
				targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMap, targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMap_description, 
				false, this,
				
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
				(targetCol,sourceCol)->{return targetCol.getSqlDataType().isMappableFrom(sourceCol.getSqlDataType());}, //keyValuePairBiPredicate
				false,//boolean allowingNullMapValue,
				false//boolean allowingDuplicateMapValue
				);
		this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setMapKeySet(this.recordMappingHelper.getTargetRecordDataNonPrimaryKeyColumnSetToBeMapped());
		this.addChildNodeBuilder(this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder);
		
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//when sourceRecordMetadataIDSelector's status changes
		//set the value set of targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder and targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder
		Runnable recordMetadataIDSelectorChangeEventAction = ()->{
			try {
				if(sourceRecordMetadataIDSelector.isDefaultEmpty()) {
					
					//set the value set of targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder and targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder to empty set
					this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setMapValueSet(new LinkedHashSet<>());
					this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setMapValueSet(new LinkedHashSet<>());
					//
					this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setToDefaultEmpty();
					this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setToDefaultEmpty();
				}else {//valid value
					RecordDataMetadata recordData = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(this.sourceRecordMetadataIDSelector.getCurrentValue());
					
					this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setMapValueSet(recordData.getDataTableSchema().getPrimaryKeyColumnSet());
					this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setMapValueSet(recordData.getDataTableSchema().getNonRUIDNonPrimaryKeyColSet());
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				System.exit(1);
				e.printStackTrace();
			}
		};
		
		this.sourceRecordMetadataIDSelector.addStatusChangedAction(
				recordMetadataIDSelectorChangeEventAction);
	}
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
//		if(changed) {
			if(isEmpty) {
				this.sourceRecordMetadataIDSelector.setToDefaultEmpty();
				//set to empty value set? TODO note that the key set is fixed
	//			this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setKeySet(new LinkedHashSet<>());
				this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setMapValueSet(new LinkedHashSet<>());
				
	//			this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setKeySet(new LinkedHashSet<>());
				this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setMapValueSet(new LinkedHashSet<>());
				
				//
				this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setToDefaultEmpty();
				this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setToDefaultEmpty();
			}else {
				if(value==null) {
					this.setToNull();
				}else {
					RecordMapping recordMapping = (RecordMapping)value;
					
					this.sourceRecordMetadataIDSelector.setValue(recordMapping.getSourceMetadataID(), isEmpty);
					//note that the above line will set the value set of both targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder and targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder
					//which is implemented in addStatusChangeEventActionOfChildNodeBuilders() method?
					
					//set map
					this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.setValue(recordMapping.getTargetSourcePKColumMap(), isEmpty);
					
					this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.setValue(recordMapping.getTargetSourceNonPKColumMap(), isEmpty);
				}
			}
//		}
		
		return changed;
	}
	
	
	@Override
	protected RecordMapping build() throws SQLException {
		RecordDataMetadata selectedSourceRecordData = (RecordDataMetadata)this.getHostVisProjectDBContext().getMetadataLookup().lookup(this.sourceRecordMetadataIDSelector.getCurrentValue());
		
		return new RecordMapping(
				selectedSourceRecordData.getDataTableSchema().getID(),
				this.recordMappingHelper.getTargetRecordMetadataID(), selectedSourceRecordData.getID(),
				
				this.targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder.getCurrentValue(),
				this.targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder.getCurrentValue()
				);
	}
}
