package builder.visframe.operation.graph.layout.JUNG;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import builder.basic.primitive.DoubleTypeBuilder;
import builder.basic.primitive.IntegerTypeBuilder;
import builder.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseBuilder;
import context.project.VisProjectDBContext;
import operation.graph.layout.JUNG.FRLayout2DOperation;
import static operation.graph.layout.JUNG.FRLayout2DOperation.*;

public final class FRLayout2DOperationBuilder extends GraphNode2DLayoutOperationBaseBuilder<FRLayout2DOperation>{
	public static final String NODE_NAME = "FRLayout2DOperation";
	public static final String NODE_DESCRIPTION = "FRLayout2DOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public FRLayout2DOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	////////////////////////////////////////////
	/**
	 * !!!!!!!!need to check if the value of the parameters is set to null;
	 * since those parameters are mandatory, thus, need to set the value to default value if the parameter are set to null on the UI;
	 * @return
	 */
	private Map<SimpleName, Object> buildFRLayout2DOperationLevelSpecificParameterNameValueObjectMap(){
		double attractionMultiplier = this.getChildrenNodeBuilderNameMap().get(ATTRACTION_MULTIPLIER.getName().getStringValue()).getCurrentStatus().isSetToNull()?
				ATTRACTION_MULTIPLIER.getDefaultValue():(double) this.getChildrenNodeBuilderNameMap().get(ATTRACTION_MULTIPLIER.getName().getStringValue()).getCurrentValue();
				
		double repulsion = this.getChildrenNodeBuilderNameMap().get(REPULSION.getName().getStringValue()).getCurrentStatus().isSetToNull()?
				REPULSION.getDefaultValue():(double) this.getChildrenNodeBuilderNameMap().get(REPULSION.getName().getStringValue()).getCurrentValue();
				
		int maxIterations = this.getChildrenNodeBuilderNameMap().get(MAX_ITERATION.getName().getStringValue()).getCurrentStatus().isSetToNull()?
				MAX_ITERATION.getDefaultValue():(int) this.getChildrenNodeBuilderNameMap().get(MAX_ITERATION.getName().getStringValue()).getCurrentValue();
				
		return FRLayout2DOperation.buildFRLayout2DOperationLevelSpecificParameterNameValueObjectMap(attractionMultiplier, repulsion, maxIterations);
	}
	
	
	/////////////////////////////////////////////
	/**
	 * note that the three parameters have default value thus can be set to null on the UI;
	 * 
	 * if set to null, the default value should be used;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//ATTRACTION_MULTIPLIER
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				ATTRACTION_MULTIPLIER.getName().getStringValue(), ATTRACTION_MULTIPLIER.getDescriptiveName(),
				ATTRACTION_MULTIPLIER.canHaveNullValueObject(this.isForReproducing()), this,
				
				ATTRACTION_MULTIPLIER.getNonNullValueAdditionalConstraints(),
				"attraction multiplier must be positive number!"
				));
		//REPULSION
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				REPULSION.getName().getStringValue(), REPULSION.getDescriptiveName(),
				REPULSION.canHaveNullValueObject(this.isForReproducing()), this,
				
				REPULSION.getNonNullValueAdditionalConstraints(),
				"REPULSION must be positive number!"
				));
		
		//MAX_ITERATION
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				MAX_ITERATION.getName().getStringValue(), MAX_ITERATION.getDescriptiveName(),
				MAX_ITERATION.canHaveNullValueObject(this.isForReproducing()), this,
				
				MAX_ITERATION.getNonNullValueAdditionalConstraints(),
				"MAX ITERATION must be positive integer!"
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
			this.getChildrenNodeBuilderNameMap().get(ATTRACTION_MULTIPLIER.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(REPULSION.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(MAX_ITERATION.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				FRLayout2DOperation FRLayout2DOperation = (FRLayout2DOperation)value;
				
				this.getChildrenNodeBuilderNameMap().get(ATTRACTION_MULTIPLIER.getName().getStringValue()).setValue(
						FRLayout2DOperation.getAttractionMultiplier(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(REPULSION.getName().getStringValue()).setValue(
						FRLayout2DOperation.getRepulsion(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(MAX_ITERATION.getName().getStringValue()).setValue(
						FRLayout2DOperation.getMaxIterations(), isEmpty);
				
				/////////////////
				this.checkIfForReproducing(FRLayout2DOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since FRLayout2DOperation type does not have parameter dependent on input data table content; 
	}
	
	@Override
	protected FRLayout2DOperation build() {
		return new FRLayout2DOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSingleGenericGraphAsInputOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildInputGraphTypeBoundedOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildGraphNode2DLayoutOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildFRLayout2DOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder ???
				);
	}

}
