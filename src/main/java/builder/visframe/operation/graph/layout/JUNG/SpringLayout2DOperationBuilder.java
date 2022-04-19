package builder.visframe.operation.graph.layout.JUNG;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import builder.basic.primitive.DoubleTypeBuilder;
import builder.basic.primitive.IntegerTypeBuilder;
import builder.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseBuilder;
import context.project.VisProjectDBContext;
import operation.graph.layout.JUNG.SpringLayout2DOperation;

import static operation.graph.layout.JUNG.SpringLayout2DOperation.*;



public final class SpringLayout2DOperationBuilder extends GraphNode2DLayoutOperationBaseBuilder<SpringLayout2DOperation>{
	public static final String NODE_NAME = "SpringLayout2DOperation";
	public static final String NODE_DESCRIPTION = "SpringLayout2DOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SpringLayout2DOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
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
	private Map<SimpleName, Object> buildSpringLayout2DOperationLevelSpecificParameterNameValueObjectMap(){
		double forceMultiplier = this.getChildrenNodeBuilderNameMap().get(FORCE_MULTIPLIER.getName().getStringValue()).getCurrentStatus().isSetToNull()?
				FORCE_MULTIPLIER.getDefaultValue():(double) this.getChildrenNodeBuilderNameMap().get(FORCE_MULTIPLIER.getName().getStringValue()).getCurrentValue();
				
		int repulsionRange = this.getChildrenNodeBuilderNameMap().get(REPULSION_RANGE.getName().getStringValue()).getCurrentStatus().isSetToNull()?
				REPULSION_RANGE.getDefaultValue():(int) this.getChildrenNodeBuilderNameMap().get(REPULSION_RANGE.getName().getStringValue()).getCurrentValue();
				
		double stretch = this.getChildrenNodeBuilderNameMap().get(STRETCH.getName().getStringValue()).getCurrentStatus().isSetToNull()?
				STRETCH.getDefaultValue():(double) this.getChildrenNodeBuilderNameMap().get(STRETCH.getName().getStringValue()).getCurrentValue();
				
		int iterations = this.getChildrenNodeBuilderNameMap().get(ITERATIONS.getName().getStringValue()).getCurrentStatus().isSetToNull()?
				ITERATIONS.getDefaultValue():(int) this.getChildrenNodeBuilderNameMap().get(ITERATIONS.getName().getStringValue()).getCurrentValue();
				
		return SpringLayout2DOperation.buildSpringLayout2DOperationLevelSpecificParameterNameValueObjectMap(forceMultiplier, repulsionRange, stretch, iterations);
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
		
		//FORCE_MULTIPLIER
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				FORCE_MULTIPLIER.getName().getStringValue(), FORCE_MULTIPLIER.getDescriptiveName(),
				FORCE_MULTIPLIER.canHaveNullValueObject(this.isForReproducing()), this,
				FORCE_MULTIPLIER.getNonNullValueAdditionalConstraints(),//no constraints
				""
				));
		
		//REPULSION_RANGE
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				REPULSION_RANGE.getName().getStringValue(), REPULSION_RANGE.getDescriptiveName(),
				REPULSION_RANGE.canHaveNullValueObject(this.isForReproducing()), this,
				
				REPULSION_RANGE.getNonNullValueAdditionalConstraints(),
				"REPULSION_RANGE must be positive integer!"
				));
		
		//STRETCH
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				STRETCH.getName().getStringValue(), STRETCH.getDescriptiveName(),
				STRETCH.canHaveNullValueObject(this.isForReproducing()), this,
				
				STRETCH.getNonNullValueAdditionalConstraints(),
				""
				));
		
		//ITERATIONS
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				ITERATIONS.getName().getStringValue(), ITERATIONS.getDescriptiveName(),
				ITERATIONS.canHaveNullValueObject(this.isForReproducing()), this,
				
				ITERATIONS.getNonNullValueAdditionalConstraints(),
				"ITERATIONS must be positive integer!"
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
			this.getChildrenNodeBuilderNameMap().get(FORCE_MULTIPLIER.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(REPULSION_RANGE.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(STRETCH.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(ITERATIONS.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				SpringLayout2DOperation springLayout2DOperation = (SpringLayout2DOperation)value;
				
				this.getChildrenNodeBuilderNameMap().get(FORCE_MULTIPLIER.getName().getStringValue()).setValue(
						springLayout2DOperation.getForceMultiplier(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(REPULSION_RANGE.getName().getStringValue()).setValue(
						springLayout2DOperation.getRepulsionRange(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(STRETCH.getName().getStringValue()).setValue(
						springLayout2DOperation.getStretch(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(ITERATIONS.getName().getStringValue()).setValue(
						springLayout2DOperation.getIterations(), isEmpty);
				/////////////////
				this.checkIfForReproducing(springLayout2DOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since SpringLayout2DOperation type does not have parameter dependent on input data table content; 
	}
	
	@Override
	protected SpringLayout2DOperation build() {
		return new SpringLayout2DOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSingleGenericGraphAsInputOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildInputGraphTypeBoundedOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildGraphNode2DLayoutOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildSpringLayout2DOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder
				);
	}

}
