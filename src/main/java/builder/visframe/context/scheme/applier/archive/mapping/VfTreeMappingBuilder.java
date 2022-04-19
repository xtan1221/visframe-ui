package builder.visframe.context.scheme.applier.archive.mapping;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import builder.basic.collection.map.leaf.FixedKeySetFixedValueSetMapBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import context.project.VisProjectDBContext;
import context.scheme.appliedarchive.mapping.VfTreeMapping;
import context.scheme.appliedarchive.mapping.VfTreeMappingHelper;
import metadata.DataType;
import metadata.Metadata;
import metadata.MetadataID;
import metadata.graph.vftree.VfTreeDataMetadata;
import metadata.record.RecordDataMetadata;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableSchemaID;

public class VfTreeMappingBuilder extends MetadataMappingBuilder<VfTreeMapping>{
	public static final String NODE_NAME = "VfTreeMapping";
	public static final String NODE_DESCRIPTION = "VfTreeMapping";
	////////////////////
	private final VfTreeMappingHelper vfTreeMappingHelper;
	
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
	public VfTreeMappingBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			VfTreeMappingHelper recordMappingHelper
			) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext);
		// TODO Auto-generated constructor stub
		
		this.vfTreeMappingHelper = recordMappingHelper;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	

	/**
	 * @return the genericGraphMappingHelper
	 */
	public VfTreeMappingHelper getGenericGraphMappingHelper() {
		return vfTreeMappingHelper;
	}
	
	@Override
	protected  Set<DataType> getAllowedMetadataDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.vfTREE);
		return ret;
	}

	/**
	 * build (if not already) and return the {@link #filteringCondition} that check if a Metadata can be used to create the wanted RecordMapping;
	 * 
	 * note that the returned Predicate only checks the basic constraints;
	 * @return
	 */
	@Override
	protected Predicate<Metadata> getMetadataFilteringCondition(){
		if(this.filteringCondition==null) {
			this.filteringCondition = (metadata)->{
				//first check data type
				if(!metadata.getDataType().equals(DataType.vfTREE)) {
					return false;
				}
				
				///////////////
				VfTreeDataMetadata graph = (VfTreeDataMetadata)metadata;
				
				//1. check node feature data
				if(this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded())
					//check size of non mandatory additional feature column set
					if(this.vfTreeMappingHelper.getTargetNodeRecordDataNonMandatoryAdditionalFeatureColumSetToBeMapped().size()>graph.getGraphVertexFeature().getNonMandatoryAdditionalColumnNameSet().size())
						return false;
				
				//2. check edge feature data only if edge record data included for mapping
				if(this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded()) {
					//check size of non mandatory additional feature column set
					if(this.vfTreeMappingHelper.getTargetEdgeRecordDataNonMandatoryAdditionalFeatureColumSetToBeMapped().size()>graph.getGraphEdgeFeature().getNonMandatoryAdditionalColumnNameSet().size())
						return false;
				}
				return true;
			};
		}
		return this.filteringCondition;
	}
	
	////////////////////////////////////////////////
	protected static final String sourceVfTreeMetadataID = "sourceVfTreeMetadataID";
	protected static final String sourceVfTreeMetadataID_description = "sourceVfTreeMetadataID";
	
	//node data related
	protected static final String targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMap = "targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMap";
	protected static final String targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMap_description = "targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMap";
	
	protected static final String targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMap = "targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMap";
	protected static final String targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMap_description = "targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMap";
	
	
	////
	private MetadataIDSelector sourceVfTreeMetadataIDSelector;
	
	////node data
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder;
	/////edge data
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder;
	
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		this.sourceVfTreeMetadataIDSelector = new MetadataIDSelector(
				sourceVfTreeMetadataID, sourceVfTreeMetadataID_description, false, this,
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
				this.getAllowedMetadataDataTypeSet(),
				this.getMetadataFilteringCondition()
				);
		
		this.addChildNodeBuilder(this.sourceVfTreeMetadataIDSelector);
		
		//////////////////node feature col map
		if(this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded()) {
			this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
					targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMap, targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMap_description, 
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
			this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapKeySet(this.vfTreeMappingHelper.getTargetNodeRecordDataNonMandatoryAdditionalFeatureColumSetToBeMapped());
			this.addChildNodeBuilder(this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder);
		}
		
		
		//edge feature additional column map
		if(this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded()) {//node feature additional columns only need to be mapped if ...
			this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
					targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMap, targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMap_description, 
					false, this,
					
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
					(targetCol,sourceCol)->{return targetCol.getSqlDataType().isMappableFrom(sourceCol.getSqlDataType());}, //keyValuePairBiPredicate
					false,//boolean allowingNullMapValue,
					false//boolean allowingDuplicateMapValue
					);
			this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapKeySet(this.vfTreeMappingHelper.getTargetEdgeRecordDataNonMandatoryAdditionalFeatureColumSetToBeMapped());
			this.addChildNodeBuilder(this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder);
		}
		
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//when sourceRecordMetadataIDSelector's status changes
		//set the value set of targetRecordDataPrimaryKeyColsSourceRecordDataPrimaryKeyColsMapBuilder and targetRecordDataNonPrimaryKeyColsSourceRecordDataNonPrimaryKeyColsMapBuilder
		Runnable recordMetadataIDSelectorChangeEventAction = ()->{
			try {
				if(this.sourceVfTreeMetadataIDSelector.isDefaultEmpty()) {
					//set the value set of all non-null map builder to empty set
					
					if(this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded()) {
						this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
						this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
					}
					if(this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded()) {
						this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
						this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
					}
				}else {//valid value
					VfTreeDataMetadata sourceVfTreeData = (VfTreeDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(this.sourceVfTreeMetadataIDSelector.getCurrentValue());
					RecordDataMetadata nodeFeatureRecordData = (RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceVfTreeData.getNodeRecordMetadataID());
					RecordDataMetadata edgeFeatureRecordData = (RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceVfTreeData.getEdgeRecordMetadataID());
					
					//node feature map
					if(this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded()) {
						Set<DataTableColumn> nonMandatoryAdditionalColumnSet = new LinkedHashSet<>();
						sourceVfTreeData.getGraphVertexFeature().getNonMandatoryAdditionalColumnNameSet().forEach(colName->{
							nonMandatoryAdditionalColumnSet.add(nodeFeatureRecordData.getDataTableSchema().getColumn(colName));
						});
						this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapValueSet(nonMandatoryAdditionalColumnSet);
					}
					
					if(this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded()) {
						//edge feature map
						Set<DataTableColumn> nonMandatoryAdditionalColumnSet = new LinkedHashSet<>();
						sourceVfTreeData.getGraphEdgeFeature().getNonMandatoryAdditionalColumnNameSet().forEach(colName->{
							nonMandatoryAdditionalColumnSet.add(edgeFeatureRecordData.getDataTableSchema().getColumn(colName));
						});
						this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapValueSet(nonMandatoryAdditionalColumnSet);
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		};
		
		this.sourceVfTreeMetadataIDSelector.addStatusChangedAction(
				recordMetadataIDSelectorChangeEventAction);
	}
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.sourceVfTreeMetadataIDSelector.setToDefaultEmpty();
			//set to empty value set? TODO note that the key set is fixed
			
			if(this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded()) {
				this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
				this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
			}
			
			if(this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded()) {
				this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
				this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
			}
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				VfTreeMapping vfTreeMapping = (VfTreeMapping)value;
				
				//
				this.sourceVfTreeMetadataIDSelector.setValue(vfTreeMapping.getSourceMetadataID(), isEmpty);
				//note that the above line will set the value set of all map builders; 
				//which is implemented in addStatusChangeEventActionOfChildNodeBuilders() method?
				
				if(this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded())
					this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setValue(
							vfTreeMapping.getTargetSourceNodeRecordDataAdditionalFeatureColumMap(), isEmpty);
				
				if(this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded())
					this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.setValue(
							vfTreeMapping.getTargetSourceEdgeRecordDataAdditionalFeatureColumMap(), isEmpty);
				
			}
		}
		return changed;
	}
	
	
	/**
	 * need to build the targetSourceEdgeDataSourceNodeIDColumnMap and targetSourceEdgeDataSinkNodeIDColumnMap based on the created maps;
	 * 
	 */
	@Override
	protected VfTreeMapping build() throws SQLException {
		VfTreeDataMetadata sourceGraphData = (VfTreeDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(this.sourceVfTreeMetadataIDSelector.getCurrentValue());
		
		RecordDataMetadata nodeFeatureRecordData = 
				(RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceGraphData.getNodeRecordMetadataID());
		RecordDataMetadata edgeFeatureRecordData = 
				(RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceGraphData.getEdgeRecordMetadataID());
		
		///////////////////////////////////////
		MetadataID targetMetadataID = this.vfTreeMappingHelper.getTargetGenericGraphMetadataID(); 
		MetadataID sourceMetadataID = sourceGraphData.getID();
		
		boolean targetNodeRecordDataIncluded = this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded();
		boolean targetEdgeRecordDataIncluded = this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded();
		
		MetadataID targetNodeRecordMetadataID = this.vfTreeMappingHelper.getTargetNodeRecordMetadataID();
		MetadataID targetEdgeRecordMetadataID = this.vfTreeMappingHelper.getTargetEdgeRecordMetadataID();
		MetadataID sourceNodeRecordMetadataID = sourceGraphData.getNodeRecordMetadataID();
		MetadataID sourceEdgeRecordMetadataID = sourceGraphData.getEdgeRecordMetadataID();
		DataTableSchemaID sourceNodeRecordDataTableSchemaID = nodeFeatureRecordData.getDataTableSchema().getID();
		DataTableSchemaID sourceEdgeRecordDataTableSchemaID = edgeFeatureRecordData.getDataTableSchema().getID();
		
		Map<DataTableColumn, DataTableColumn> targetSourceNodeRecordNonMandatoryAdditionalFeatureColumnMap = 
				this.vfTreeMappingHelper.isTargetNodeRecordDataIncluded()?this.targetSourceNodeRecordNonMandatoryAdditionalFeatureColumMapBuilder.getCurrentValue():null;
		
		
		Map<DataTableColumn, DataTableColumn> targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumnMap = 
				this.vfTreeMappingHelper.isTargetEdgeRecordDataIncluded()?this.targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumMapBuilder.getCurrentValue():null;
		
		//////////////////////
		return new VfTreeMapping(
				targetMetadataID, sourceMetadataID,
				
				targetNodeRecordDataIncluded, targetEdgeRecordDataIncluded,
				
				targetNodeRecordMetadataID,targetEdgeRecordMetadataID,sourceNodeRecordMetadataID, sourceEdgeRecordMetadataID,
				sourceNodeRecordDataTableSchemaID,sourceEdgeRecordDataTableSchemaID,
				
				targetSourceNodeRecordNonMandatoryAdditionalFeatureColumnMap,
				targetSourceEdgeRecordNonMandatoryAdditionalFeatureColumnMap
				);
	}
}
