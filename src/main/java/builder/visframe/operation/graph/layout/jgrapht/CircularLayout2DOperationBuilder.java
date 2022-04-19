package builder.visframe.operation.graph.layout.jgrapht;

import builder.basic.primitive.DoubleTypeBuilder;
import builder.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseBuilder;
import context.project.VisProjectDBContext;
import operation.graph.layout.jgrapht.CircularLayout2DOperation;
import static operation.graph.layout.jgrapht.CircularLayout2DOperation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;


public class CircularLayout2DOperationBuilder extends GraphNode2DLayoutOperationBaseBuilder<CircularLayout2DOperation>{
	public static final String NODE_NAME = "CircularLayout2DOperation";
	public static final String NODE_DESCRIPTION = "CircularLayout2DOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public CircularLayout2DOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	////////////////////////////////////////////
	private Map<SimpleName, Object> buildCircularLayout2DOperationLevelSpecificParameterNameValueObjectMap(){
		double radius = (double) this.getChildrenNodeBuilderNameMap().get(RADIUS.getName().getStringValue()).getCurrentValue();
		
		return CircularLayout2DOperation.buildCircularLayout2DOperationLevelSpecificParameterNameValueObjectMap(radius);
	}
	
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//RADIUS
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				RADIUS.getName().getStringValue(), RADIUS.getDescriptiveName(),
				RADIUS.canHaveNullValueObject(this.isForReproducing()), this,
				RADIUS.getNonNullValueAdditionalConstraints(),
//				e->{return e>0;},
				"radius must be positive number!"
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
			this.getChildrenNodeBuilderNameMap().get(RADIUS.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				CircularLayout2DOperation circularLayout2DOperation = (CircularLayout2DOperation)value;
				
				this.getChildrenNodeBuilderNameMap().get(RADIUS.getName().getStringValue()).setValue(
						circularLayout2DOperation.getRadius(), isEmpty);
				
				/////////////////
				this.checkIfForReproducing(circularLayout2DOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}
	

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since circularLayout2DOperation type does not have parameter dependent on input data table content; 
	}
	
	
	@Override
	protected CircularLayout2DOperation build() {
		return new CircularLayout2DOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSingleGenericGraphAsInputOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildInputGraphTypeBoundedOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildGraphNode2DLayoutOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildCircularLayout2DOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder ???
				);
	}

}
