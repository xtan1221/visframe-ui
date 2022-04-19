package builder.visframe.operation.graph.transform;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import basic.SimpleName;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.metadata.graph.type.GraphTypeEnforcerBuilder;
import builder.visframe.operation.graph.SingleGenericGraphAsInputOperationBuilder;
import context.project.VisProjectDBContext;
import metadata.MetadataName;
import metadata.graph.type.GraphTypeEnforcer;
import operation.graph.transform.TransformGraphOperation;
import static operation.graph.transform.TransformGraphOperation.*;


public final class TransformGraphOperationBuilder extends SingleGenericGraphAsInputOperationBuilder<TransformGraphOperation>{
	public static final String NODE_NAME = "TransformGraphOperation";
	public static final String NODE_DESCRIPTION = "TransformGraphOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public TransformGraphOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	////////////////////////////////////////////
	private Map<SimpleName, Object> buildTransformGraphOperationLevelSpecificParameterNameValueObjectMap(){
		GraphTypeEnforcer graphTypeEnforcer = (GraphTypeEnforcer) this.getChildrenNodeBuilderNameMap().get(GRAPH_TYPE_ENFORCER.getName().getStringValue()).getCurrentValue();
		MetadataName outputGraphDataName = (MetadataName) this.getChildrenNodeBuilderNameMap().get(OUTPUT_GRAPH_DATA_ID.getName().getStringValue()).getCurrentValue();
		return TransformGraphOperation.buildTransformGraphOperationLevelSpecificParameterNameValueObjectMap(graphTypeEnforcer, outputGraphDataName);
	}
	
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//GRAPH_TYPE_ENFORCER
		this.addChildNodeBuilder(new GraphTypeEnforcerBuilder(
				GRAPH_TYPE_ENFORCER.getName().getStringValue(), GRAPH_TYPE_ENFORCER.getDescriptiveName(),
				GRAPH_TYPE_ENFORCER.canHaveNullValueObject(this.isForReproducing()), this
				));
		
		//OUTPUT_GRAPH_DATA_NAME
		this.addChildNodeBuilder(new VfNameStringBuilder<MetadataName>(
				OUTPUT_GRAPH_DATA_ID.getName().getStringValue(), OUTPUT_GRAPH_DATA_ID.getDescriptiveName(),
				OUTPUT_GRAPH_DATA_ID.canHaveNullValueObject(this.isForReproducing()), this,
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
			this.getChildrenNodeBuilderNameMap().get(GRAPH_TYPE_ENFORCER.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(OUTPUT_GRAPH_DATA_ID.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				TransformGraphOperation transformGraphOperation = (TransformGraphOperation)value;
				
				this.getChildrenNodeBuilderNameMap().get(GRAPH_TYPE_ENFORCER.getName().getStringValue()).setValue(
						transformGraphOperation.getGraphTypeEnforcer(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(OUTPUT_GRAPH_DATA_ID.getName().getStringValue()).setValue(
						transformGraphOperation.getOutputGraphDataID().getName(), isEmpty);
				
				/////////////////
				this.checkIfForReproducing(transformGraphOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since TransformGraphOperation type does not have parameter dependent on input data table content; 
	}
	
	@Override
	protected TransformGraphOperation build() {
		return new TransformGraphOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSingleGenericGraphAsInputOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildTransformGraphOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder ???
				);
	}

}
