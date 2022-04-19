package builder.visframe.context.scheme.applier.archive.mapping;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import builder.basic.collection.map.leaf.FixedKeySetFixedValueSetMapBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import context.project.VisProjectDBContext;
import context.scheme.appliedarchive.mapping.GenericGraphMapping;
import context.scheme.appliedarchive.mapping.GenericGraphMappingHelper;
import metadata.DataType;
import metadata.Metadata;
import metadata.MetadataID;
import metadata.graph.GraphDataMetadata;
import metadata.record.RecordDataMetadata;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableSchemaID;

/**
 * basic steps
 * 
 * 
 * 1. select a generic graph metadata from the host VisProjectdDBContext
 * 		note that need to check constraints 
 * 		1. the node 
 * 		also 
 * 		1. reset the map value set for the node ID column mapping and node additional feature column mapping;
 * 		2. reset the 
 * 
 * 2. for node feature record data
 * 		select the 
 * 
 * 
 * 3. for the edge feature record data 
 * 		create mapping for 
 * 			edge data id columns
 * 				the map key set is the edge data id columns of the target generic graph data;
 * 				the map value set is the edge data id columns of the source generic graph data;
 * 			edge data additional feature columns
 * 			
 * 		if edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets is true
 * 			create mapping for source node id column set
 * 				the map key set is the source node id column set of the edge record data of the target generic graph data;
 * 				the map value set is the source node id column set of the edge record data of the source generic graph data; 
 * 			create mapping for sink node id column set
 * 				the map key set is the sink node id column set of the edge record data of the target generic graph data;
 * 				the map value set is the sink node id column set of the edge record data of the source generic graph data; 
 * 
 * @author tanxu
 *
 */
public class GenericGraphMappingBuilder extends MetadataMappingBuilder<GenericGraphMapping>{
	public static final String NODE_NAME = "GenericGraphMapping";
	public static final String NODE_DESCRIPTION = "GenericGraphMapping";
	/////////////////////
	private final GenericGraphMappingHelper genericGraphMappingHelper;
	
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
	public GenericGraphMappingBuilder(
			VisProjectDBContext hostVisProjectDBContext,
			GenericGraphMappingHelper recordMappingHelper
			) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext);
		// TODO Auto-generated constructor stub
		
		this.genericGraphMappingHelper = recordMappingHelper;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	

	/**
	 * @return the genericGraphMappingHelper
	 */
	public GenericGraphMappingHelper getGenericGraphMappingHelper() {
		return genericGraphMappingHelper;
	}
	
	@Override
	protected Set<DataType> getAllowedMetadataDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.GRAPH);
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
				if(!metadata.getDataType().isGenericGraph()) {
					return false;
				}
				
				///////////////
				GraphDataMetadata graph = (GraphDataMetadata)metadata;
				
				//1. check node feature data
				//node id column set size
				if(this.genericGraphMappingHelper.getTargetNodeRecordDataNodeIDColumSet().size()!=graph.getGraphVertexFeature().getIDColumnNameSet().size())
					return false;
				
				//
				if(this.genericGraphMappingHelper.isTargetNodeRecordDataIncluded())
					//node additional feature column set size
					if(this.genericGraphMappingHelper.getTargetNodeRecordDataAdditionalFeatureColumSetToBeMapped().size()>graph.getGraphVertexFeature().getAdditionalFeatureColumnNameSet().size())
						return false;
				
				//2. check edge feature data only if edge record data included for mapping
				if(this.genericGraphMappingHelper.isTargetEdgeRecordDataIncluded()) {
					//check if edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets is the same
					//
					if(this.genericGraphMappingHelper.getTargetEdgeRecordDataEdgeIDColumSet().size()!=graph.getGraphEdgeFeature().getIDColumnNameSet().size())
						return false;
					if(this.genericGraphMappingHelper.getTargetEdgeRecordDataAdditionalFeatureColumSetToBeMapped().size()!=graph.getGraphEdgeFeature().getAdditionalFeatureColumnNameSet().size())
						return false;
					
					if(!this.genericGraphMappingHelper.getTargetGenericGraphEdgeRecordDataEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets().equals(graph.getGraphEdgeFeature().isEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets()))
						return false;
					
					//no need to check the source and sink node id column set size in edge feature data, since it is implicitly constrained by the node id column set size of the node feature data;
				}
				
				return true;
			};
		}
		return this.filteringCondition;
	}
	
	////////////////////////////////////////////////
	protected static final String sourceGenericGraphMetadataID = "sourceGenericGraphMetadataID";
	protected static final String sourceGenericGraphMetadataID_description = "sourceGenericGraphMetadataID";
	
	//node data related
	protected static final String targetSourceNodeRecordDataNodeIDColumMap = "targetSourceNodeRecordDataNodeIDColumMap";
	protected static final String targetSourceNodeRecordDataNodeIDColumMap_description = "targetSourceNodeRecordDataNodeIDColumMap";

	protected static final String targetSourceNodeRecordDataAdditionalFeatureColumMap = "targetSourceNodeRecordDataAdditionalFeatureColumMap";
	protected static final String targetSourceNodeRecordDataAdditionalFeatureColumMap_description = "targetSourceNodeRecordDataAdditionalFeatureColumMap";

	//edge
	protected static final String targetSourceEdgeRecordDataEdgeIdColumnMap = "targetSourceEdgeRecordDataEdgeIdColumnMap";
	protected static final String targetSourceEdgeRecordDataEdgeIdColumnMap_description = "targetSourceEdgeRecordDataEdgeIdColumnMap";
	
//	protected static final String targetSourceEdgeDataSourceNodeIDColumnMap = "targetSourceEdgeDataSourceNodeIDColumnMap";
//	protected static final String targetSourceEdgeDataSourceNodeIDColumnMap_description = "targetSourceEdgeDataSourceNodeIDColumnMap";
//	
//	protected static final String targetSourceEdgeDataSinkNodeIDColumnMap = "targetSourceEdgeDataSinkNodeIDColumnMap";
//	protected static final String targetSourceEdgeDataSinkNodeIDColumnMap_description = "targetSourceEdgeDataSinkNodeIDColumnMap";
	
	protected static final String targetSourceEdgeRecordDataAdditionalFeatureColumMap = "targetSourceEdgeRecordDataAdditionalFeatureColumMap";
	protected static final String targetSourceEdgeRecordDataAdditionalFeatureColumMap_description = "targetSourceEdgeRecordDataAdditionalFeatureColumMap";
	
	/////////////////////////////////
	////
	private MetadataIDSelector sourceGenericGraphMetadataIDSelector;
	
	////node data
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceNodeRecordDataNodeIDColumMapBuilder;
	
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder;
	
	/////edge data
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceEdgeRecordDataEdgeIdColumnMapBuilder;
	
//	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceEdgeDataSourceNodeIDColumnMapBuilder;
//	
//	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceEdgeDataSinkNodeIDColumnMapBuilder;
	
	private FixedKeySetFixedValueSetMapBuilder<DataTableColumn, DataTableColumn> targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder;

	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		this.sourceGenericGraphMetadataIDSelector = new MetadataIDSelector(
				sourceGenericGraphMetadataID, sourceGenericGraphMetadataID_description, false, this,
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
				this.getAllowedMetadataDataTypeSet(),
				this.getMetadataFilteringCondition()
				);
		this.addChildNodeBuilder(this.sourceGenericGraphMetadataIDSelector);
		
		//////////////////node feature col map
		//node id columns - always be mapped no matter genericGraphMappingHelper.isTargetNodeRecordDataIncluded() is true or not!!!!!!!!!!
		this.targetSourceNodeRecordDataNodeIDColumMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
				targetSourceNodeRecordDataNodeIDColumMap, targetSourceNodeRecordDataNodeIDColumMap_description, 
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
		this.targetSourceNodeRecordDataNodeIDColumMapBuilder.setMapKeySet(this.genericGraphMappingHelper.getTargetNodeRecordDataNodeIDColumSet());
		this.addChildNodeBuilder(this.targetSourceNodeRecordDataNodeIDColumMapBuilder);
		
		////////node additional feature columns
		if(this.genericGraphMappingHelper.isTargetNodeRecordDataIncluded()) {//node feature additional columns only need to be mapped if ...
			//node feature additional column map
			this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
					targetSourceNodeRecordDataAdditionalFeatureColumMap, targetSourceNodeRecordDataAdditionalFeatureColumMap_description, 
					false, this,
					
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
					(targetCol,sourceCol)->{return targetCol.getSqlDataType().isMappableFrom(sourceCol.getSqlDataType());}, //keyValuePairBiPredicate
					false,//boolean allowingNullMapValue,
					false//boolean allowingDuplicateMapValue
					);
			this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.setMapKeySet(this.genericGraphMappingHelper.getTargetNodeRecordDataAdditionalFeatureColumSetToBeMapped());
			this.addChildNodeBuilder(this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder);
		}
		
		//////////////////////edge feature data column maps
		if(this.genericGraphMappingHelper.isTargetEdgeRecordDataIncluded()) {//edge feature columns only need to be mapped if ...
			//edge id columns
			this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
					targetSourceEdgeRecordDataEdgeIdColumnMap, targetSourceEdgeRecordDataEdgeIdColumnMap_description, 
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
			this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.setMapKeySet(this.genericGraphMappingHelper.getTargetEdgeRecordDataEdgeIDColumSet());
			this.addChildNodeBuilder(this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder);
			
			//edge additional feature columns
			this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
					targetSourceEdgeRecordDataAdditionalFeatureColumMap, targetSourceEdgeRecordDataAdditionalFeatureColumMap_description, 
					false, this,
					
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
					(targetCol,sourceCol)->{return targetCol.getSqlDataType().isMappableFrom(sourceCol.getSqlDataType());}, //keyValuePairBiPredicate
					false,//boolean allowingNullMapValue,
					false//boolean allowingDuplicateMapValue
					);
			this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.setMapKeySet(this.genericGraphMappingHelper.getTargetEdgeRecordDataAdditionalFeatureColumSetToBeMapped());
			this.addChildNodeBuilder(this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder);
			
		}
		
//		//targetSourceEdgeDataSourceNodeIDColumnMapBuilder and targetSourceEdgeDataSinkNodeIDColumnMapBuilder
//		if(this.genericGraphMappingHelper.getTargetGenericGraphEdgeRecordDataEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets()) {
//			///source node id columns map
//			this.targetSourceEdgeDataSourceNodeIDColumnMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
//					targetSourceEdgeDataSourceNodeIDColumnMap, targetSourceEdgeDataSourceNodeIDColumnMap_description, 
//					false, this,
//					
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
//					(targetCol,sourceCol)->{return targetCol.getSqlDataType().isMappableFrom(sourceCol.getSqlDataType());}, //keyValuePairBiPredicate
//					false,//boolean allowingNullMapValue,
//					false//boolean allowingDuplicateMapValue
//					);
//			this.targetSourceEdgeDataSourceNodeIDColumnMapBuilder.setKeySet(this.genericGraphMappingHelper.getTargetEdgeRecordDataSourceNodeIDColumSet());
//			this.addChildNodeBuilder(this.targetSourceEdgeDataSourceNodeIDColumnMapBuilder);
//			
//			///sink node id columns map
//			this.targetSourceEdgeDataSinkNodeIDColumnMapBuilder = new FixedKeySetFixedValueSetMapBuilder<>(
//					targetSourceEdgeDataSinkNodeIDColumnMap, targetSourceEdgeDataSinkNodeIDColumnMap_description, 
//					false, this,
//					
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
//					e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},
//					(targetCol,sourceCol)->{return targetCol.getSqlDataType().isMappableFrom(sourceCol.getSqlDataType());}, //keyValuePairBiPredicate
//					false,//boolean allowingNullMapValue,
//					false//boolean allowingDuplicateMapValue
//					);
//			this.targetSourceEdgeDataSinkNodeIDColumnMapBuilder.setKeySet(this.genericGraphMappingHelper.getTargetEdgeRecordDataSinkNodeIDColumSet());
//			this.addChildNodeBuilder(this.targetSourceEdgeDataSinkNodeIDColumnMapBuilder);
//			
//		}else {
//			//do not need to add the targetSourceEdgeDataSourceNodeIDColumnMapBuilder and targetSourceEdgeDataSinkNodeIDColumnMapBuilder since they are logically covered by other maps and information in the target and source generic graph metadata;
//		}
		
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
				if(this.sourceGenericGraphMetadataIDSelector.isDefaultEmpty()) {
					//set the value set of all non-null map builder to empty set
					this.targetSourceNodeRecordDataNodeIDColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
					
					if(this.genericGraphMappingHelper.isTargetNodeRecordDataIncluded()) {
						this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
						this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
					}
					if(this.genericGraphMappingHelper.isTargetEdgeRecordDataIncluded()) {
						this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.setMapValueSet(new LinkedHashSet<>());
						this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
						
						this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.setToDefaultEmpty();
						this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
					}
//					if(this.genericGraphMappingHelper.getTargetGenericGraphEdgeRecordDataEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets()) {
//						this.targetSourceEdgeDataSourceNodeIDColumnMapBuilder.setValueSet(new LinkedHashSet<>());
//						this.targetSourceEdgeDataSinkNodeIDColumnMapBuilder.setValueSet(new LinkedHashSet<>());
//					}
				}else {//valid value
					GraphDataMetadata sourceGraphData = (GraphDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(this.sourceGenericGraphMetadataIDSelector.getCurrentValue());
					RecordDataMetadata nodeFeatureRecordData = (RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceGraphData.getNodeRecordMetadataID());
					RecordDataMetadata edgeFeatureRecordData = (RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceGraphData.getEdgeRecordMetadataID());
					//node feature map
					this.targetSourceNodeRecordDataNodeIDColumMapBuilder.setMapValueSet(nodeFeatureRecordData.getDataTableSchema().getPrimaryKeyColumnSet());
					
					if(this.genericGraphMappingHelper.isTargetNodeRecordDataIncluded())
						this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.setMapValueSet(nodeFeatureRecordData.getDataTableSchema().getNonRUIDNonPrimaryKeyColSet());
					
					if(this.genericGraphMappingHelper.isTargetEdgeRecordDataIncluded()) {
						//edge feature map
						this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.setMapValueSet(edgeFeatureRecordData.getDataTableSchema().getPrimaryKeyColumnSet());
						Set<DataTableColumn> sourceEdgeFeatureDataAdditionalFeatureColumnSet = new LinkedHashSet<>();
						sourceGraphData.getGraphEdgeFeature().getAdditionalFeatureColumnNameSet().forEach(colName->{
							sourceEdgeFeatureDataAdditionalFeatureColumnSet.add(edgeFeatureRecordData.getDataTableSchema().getColumn(colName));
						});
						this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.setMapValueSet(sourceEdgeFeatureDataAdditionalFeatureColumnSet);
					}
//					if(this.genericGraphMappingHelper.getTargetGenericGraphEdgeRecordDataEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets()) {
//						
//						this.targetSourceEdgeDataSourceNodeIDColumnMapBuilder.setValueSet(sourceGraphData.getGraphEdgeFeature().gets);
//						this.targetSourceEdgeDataSinkNodeIDColumnMapBuilder.setValueSet(new LinkedHashSet<>());
//					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		};
		
		this.sourceGenericGraphMetadataIDSelector.addStatusChangedAction(
				recordMetadataIDSelectorChangeEventAction);
	}
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.sourceGenericGraphMetadataIDSelector.setToDefaultEmpty();
			//set to empty value set? TODO note that the key set is fixed
			this.targetSourceNodeRecordDataNodeIDColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
			this.targetSourceNodeRecordDataNodeIDColumMapBuilder.setToDefaultEmpty();
			
			if(this.genericGraphMappingHelper.isTargetNodeRecordDataIncluded()) {
				this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
				this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
			}
			
			if(this.genericGraphMappingHelper.isTargetEdgeRecordDataIncluded()) {
				this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.setMapValueSet(new LinkedHashSet<>());
				this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.setMapValueSet(new LinkedHashSet<>());
				this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.setToDefaultEmpty();
				this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.setToDefaultEmpty();
			}
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				GenericGraphMapping genericGraphMapping = (GenericGraphMapping)value;
				
				//
				this.sourceGenericGraphMetadataIDSelector.setValue(genericGraphMapping.getSourceMetadataID(), isEmpty);
				//note that the above line will set the value set of all map builders; 
				//which is implemented in addStatusChangeEventActionOfChildNodeBuilders() method?
				
				//add linking lines 
				this.targetSourceNodeRecordDataNodeIDColumMapBuilder.setValue(genericGraphMapping.getTargetSourceNodeRecordDataNodeIDColumMap(), isEmpty);
				
				if(this.genericGraphMappingHelper.isTargetNodeRecordDataIncluded())
					this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.setValue(genericGraphMapping.getTargetSourceNodeRecordDataAdditionalFeatureColumMap(), isEmpty);
				
				if(this.genericGraphMappingHelper.isTargetEdgeRecordDataIncluded()) {
					this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.setValue(genericGraphMapping.getTargetSourceEdgeRecordDataEdgeIdColumnMap(), isEmpty);
					this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.setValue(genericGraphMapping.getTargetSourceEdgeRecordDataAdditionalFeatureColumMap(), isEmpty);
				}
			}
		}
		return changed;
	}
	
	
	/**
	 * need to build the targetSourceEdgeDataSourceNodeIDColumnMap and targetSourceEdgeDataSinkNodeIDColumnMap based on the created maps;
	 * 
	 */
	@Override
	protected GenericGraphMapping build() throws SQLException {
		GraphDataMetadata sourceGraphData = (GraphDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(this.sourceGenericGraphMetadataIDSelector.getCurrentValue());
		
		RecordDataMetadata nodeFeatureRecordData = 
				(RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceGraphData.getNodeRecordMetadataID());
		RecordDataMetadata edgeFeatureRecordData = 
				(RecordDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(sourceGraphData.getEdgeRecordMetadataID());
		
		///////////////////////////////////////
		MetadataID targetMetadataID = this.genericGraphMappingHelper.getTargetGenericGraphMetadataID(); 
		MetadataID sourceMetadataID = sourceGraphData.getID();
		
		boolean targetNodeRecordDataIncluded = this.genericGraphMappingHelper.isTargetNodeRecordDataIncluded();
		boolean targetEdgeRecordDataIncluded = this.genericGraphMappingHelper.isTargetEdgeRecordDataIncluded();
		
		MetadataID targetNodeRecordMetadataID = this.genericGraphMappingHelper.getTargetNodeRecordMetadataID();
		MetadataID targetEdgeRecordMetadataID = this.genericGraphMappingHelper.getTargetEdgeRecordMetadataID();
		MetadataID sourceNodeRecordMetadataID = sourceGraphData.getNodeRecordMetadataID();
		MetadataID sourceEdgeRecordMetadataID = sourceGraphData.getEdgeRecordMetadataID();
		DataTableSchemaID sourceNodeRecordDataTableSchemaID = nodeFeatureRecordData.getDataTableSchema().getID();
		DataTableSchemaID sourceEdgeRecordDataTableSchemaID = edgeFeatureRecordData.getDataTableSchema().getID();
		
		
		Map<DataTableColumn, DataTableColumn> targetSourceNodeRecordDataNodeIDColumMap = 
				this.targetSourceNodeRecordDataNodeIDColumMapBuilder.getCurrentValue();
		
		Map<DataTableColumn, DataTableColumn> targetSourceNodeRecordDataAdditionalFeatureColumMap = 
				targetNodeRecordDataIncluded?this.targetSourceNodeRecordDataAdditionalFeatureColumMapBuilder.getCurrentValue():null;
		
		
		Boolean edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets = this.genericGraphMappingHelper.getTargetGenericGraphEdgeRecordDataEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets();
		
		Map<DataTableColumn, DataTableColumn> targetSourceEdgeRecordDataEdgeIdColumnMap = 
				targetEdgeRecordDataIncluded?this.targetSourceEdgeRecordDataEdgeIdColumnMapBuilder.getCurrentValue():null;
		Map<DataTableColumn, DataTableColumn> targetSourceEdgeRecordDataAdditionalFeatureColumMap = 
				targetEdgeRecordDataIncluded?this.targetSourceEdgeRecordDataAdditionalFeatureColumMapBuilder.getCurrentValue():null;
		
		
		/////////////////
		Map<DataTableColumn, DataTableColumn> targetSourceEdgeDataSourceNodeIDColumnMap;
		Map<DataTableColumn, DataTableColumn> targetSourceEdgeDataSinkNodeIDColumnMap;
		
		if(targetEdgeRecordDataIncluded && 
				this.genericGraphMappingHelper.getTargetGenericGraphEdgeRecordDataEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets()) {
			targetSourceEdgeDataSourceNodeIDColumnMap = new LinkedHashMap<>();
			targetSourceEdgeDataSinkNodeIDColumnMap = new LinkedHashMap<>();
			this.genericGraphMappingHelper.getTargetGraphData().getGraphEdgeFeature().getNodeIDColumnNameEdgeSourceNodeIDColumnNameMap().forEach((nodeIDColName, edgeSourceNodeIDColName)->{
				DataTableColumn targetGraphDataNodeIDCol = this.genericGraphMappingHelper.getTargetNodeRecordMetadata().getDataTableSchema().getColumn(nodeIDColName);
				//
				DataTableColumn targetGraphDataEdgeFeatureSourceNodeIDCol = this.genericGraphMappingHelper.getTargetEdgeRecordMetadata().getDataTableSchema().getColumn(edgeSourceNodeIDColName);
				
				//the source graph node id column mapped to the targetGraphDataNodeIDCol
				DataTableColumn sourceGraphDataNodeIDCol = targetSourceNodeRecordDataNodeIDColumMap.get(targetGraphDataNodeIDCol);
				
				DataTableColumn sourceGraphDataEdgeFeatureSourceNodeIDCol = 
						edgeFeatureRecordData.getDataTableSchema().getColumn(
								sourceGraphData.getGraphEdgeFeature().getNodeIDColumnNameEdgeSourceNodeIDColumnNameMap().get(sourceGraphDataNodeIDCol.getName()));
				
				targetSourceEdgeDataSourceNodeIDColumnMap.put(targetGraphDataEdgeFeatureSourceNodeIDCol, sourceGraphDataEdgeFeatureSourceNodeIDCol);
			});
			
			this.genericGraphMappingHelper.getTargetGraphData().getGraphEdgeFeature().getNodeIDColumnNameEdgeSinkNodeIDColumnNameMap().forEach((nodeIDColName, edgeSinkNodeIDColName)->{
				DataTableColumn targetGraphDataNodeIDCol = this.genericGraphMappingHelper.getTargetNodeRecordMetadata().getDataTableSchema().getColumn(nodeIDColName);
				//
				DataTableColumn targetGraphDataEdgeFeatureSinkNodeIDCol = this.genericGraphMappingHelper.getTargetEdgeRecordMetadata().getDataTableSchema().getColumn(edgeSinkNodeIDColName);
				
				//the source graph node id column mapped to the targetGraphDataNodeIDCol
				DataTableColumn sourceGraphDataNodeIDCol = targetSourceNodeRecordDataNodeIDColumMap.get(targetGraphDataNodeIDCol);
				
				DataTableColumn sourceGraphDataEdgeFeatureSinkNodeIDCol = 
						edgeFeatureRecordData.getDataTableSchema().getColumn(
								sourceGraphData.getGraphEdgeFeature().getNodeIDColumnNameEdgeSinkNodeIDColumnNameMap().get(sourceGraphDataNodeIDCol.getName()));
				
				targetSourceEdgeDataSourceNodeIDColumnMap.put(targetGraphDataEdgeFeatureSinkNodeIDCol, sourceGraphDataEdgeFeatureSinkNodeIDCol);
			});
			
			
		}else {
			targetSourceEdgeDataSourceNodeIDColumnMap = null;
			targetSourceEdgeDataSinkNodeIDColumnMap = null;
		}
		
		
		
		
		
		
		//////////////////////
		return new GenericGraphMapping(
				targetMetadataID, sourceMetadataID,
				
				targetNodeRecordDataIncluded, targetEdgeRecordDataIncluded,
				
				targetNodeRecordMetadataID,targetEdgeRecordMetadataID,sourceNodeRecordMetadataID, sourceEdgeRecordMetadataID,
				sourceNodeRecordDataTableSchemaID,sourceEdgeRecordDataTableSchemaID,
				
				targetSourceNodeRecordDataNodeIDColumMap,
				targetSourceNodeRecordDataAdditionalFeatureColumMap,
				
				edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets,
				targetSourceEdgeRecordDataEdgeIdColumnMap,
				targetSourceEdgeDataSourceNodeIDColumnMap,
				targetSourceEdgeDataSinkNodeIDColumnMap,
				targetSourceEdgeRecordDataAdditionalFeatureColumMap
				);
	}
}
