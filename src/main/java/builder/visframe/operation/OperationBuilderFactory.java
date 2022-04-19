package builder.visframe.operation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import builder.visframe.operation.graph.build.BuildGraphFromSingleExistingRecordOperationBuilder;
import builder.visframe.operation.graph.build.BuildGraphFromTwoExistingRecordOperationBuilder;
import builder.visframe.operation.graph.layout.JUNG.FRLayout2DOperationBuilder;
import builder.visframe.operation.graph.layout.JUNG.SpringLayout2DOperationBuilder;
import builder.visframe.operation.graph.layout.jgrapht.CircularLayout2DOperationBuilder;
import builder.visframe.operation.graph.transform.TransformGraphOperationBuilder;
import builder.visframe.operation.sql.generic.GenericSQLOperationBuilder;
import builder.visframe.operation.sql.predefined.type.AddNumericCumulativeColumnOperationBuilder;
import builder.visframe.operation.sql.predefined.type.GroupAndBinCountOperationBuilder;
import builder.visframe.operation.vftree.trim.RerootTreeOperationBuilder;
import builder.visframe.operation.vftree.trim.SiblingNodesReorderOperationBuilder;
import builder.visframe.operation.vftree.trim.SubTreeOperationBuilder;
import context.project.VisProjectDBContext;
import operation.Operation;
import operation.graph.InputGraphTypeBoundedOperation;
import operation.graph.SingleGenericGraphAsInputOperation;
import operation.graph.build.BuildGraphFromExistingRecordOperationBase;
import operation.graph.build.BuildGraphFromSingleExistingRecordOperation;
import operation.graph.build.BuildGraphFromTwoExistingRecordOperation;
import operation.graph.layout.GraphNode2DLayoutOperationBase;
import operation.graph.layout.JUNG.FRLayout2DOperation;
import operation.graph.layout.JUNG.SpringLayout2DOperation;
import operation.graph.layout.jgrapht.CircularLayout2DOperation;
import operation.graph.transform.TransformGraphOperation;
import operation.sql.SQLOperationBase;
import operation.sql.generic.GenericSQLOperation;
import operation.sql.predefined.PredefinedSQLBasedOperation;
import operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperation;
import operation.sql.predefined.type.AddNumericCumulativeColumnOperation;
import operation.sql.predefined.type.GroupAndBinCountOperation;
import operation.vftree.VfTreeTrimmingOperationBase;
import operation.vftree.trim.RerootTreeOperation;
import operation.vftree.trim.SiblingNodesReorderOperation;
import operation.vftree.trim.SubTreeOperation;


/**
 * factory class for making {@link AbstractOperationBuilder} for generic purpose including view, creating from scratch and modification;
 * 
 * 
 * @author tanxu
 *
 */
public class OperationBuilderFactory{
	private static Map<VisProjectDBContext, OperationBuilderFactory> projectFactoryMap;
	
	public static OperationBuilderFactory singleton(VisProjectDBContext project) {
		if(projectFactoryMap==null) {
			projectFactoryMap = new HashMap<>();
		}
		
		if(!projectFactoryMap.containsKey(project)) {
			projectFactoryMap.put(project, new OperationBuilderFactory(project));
		}
		
		return projectFactoryMap.get(project);
	}
	
	
	////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	
	
	/**
	 * private constructor
	 * @param hostVisProjectDBContext
	 */
	private OperationBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	
	/**
	 * initialize and build an AbstractOperationBuilder with the given operation Instance;
	 * 
	 * 1. to build a AbstractOperationBuilder with a template Operation instance thus to create a new Operation based on the template
	 * 		thus the given forReproducing must be false;
	 * 2. to build a AbstractOperationBuilder with an existing Operation for view-only mode;
	 * 		thus the given forReproducing must be false;
	 * 3. to build a AbstractOperationBuilder for a reproduced Operation;
	 * 		thus the given forReproducing must be true;
	 * 
	 * @param operationInstance
	 * @param forReproducing
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public AbstractOperationBuilder<?> build(Operation operationInstance, boolean forReproducing) throws IOException, SQLException {
		if(operationInstance==null)
			throw new IllegalArgumentException("given operationInstance cannot be null!");
		
		///////
		AbstractOperationBuilder<?> builder = this.initializeOperationBuilder(operationInstance.getClass(), forReproducing);
		//the parameters will be dealt with in the setValue method;
		builder.setValue(operationInstance, false);
		
		return builder;
	}
	
	
	/**
	 * initialize and return a AbstractOperationBuilder of the given type with an empty default status;
	 * 
	 * used to facilitate
	 * 1. create a new Operation
	 * 		from scratch
	 * 		with a template Operation
	 * 
	 * 2. simply view a reproduced Operation without any parameter dependent on input data table content;
	 * 		after the AbstractOperationBuilder is built, its value will be set with {@link AbstractOperationBuilder#setValue(Object, boolean)} with the reproduced Operation;
	 * 
	 * 3. for a reproduced Operation with parameter dependent on input data table content, the resulted AbstractOperationBuilder will be used to set up the values of such parameters;
	 * 		after the AbstractOperationBuilder is built, its value will be set with {@link AbstractOperationBuilder#setValue(Object, boolean)} with the reproduced Operation;
	 * 
	 * 
	 * note that since the reproduced Operation (if in that case) is not provided in this method, whether the target Operation is reproduced should explicitly given as a boolean parameter;
	 * 
	 * @param type
	 * @param forReproducing
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public AbstractOperationBuilder<?> initializeOperationBuilder(Class<? extends Operation> type, boolean forReproducing) throws SQLException, IOException{
		if(BuildGraphFromExistingRecordOperationBase.class.isAssignableFrom(type)) {
			if(BuildGraphFromSingleExistingRecordOperation.class.isAssignableFrom(type)) {
				return new BuildGraphFromSingleExistingRecordOperationBuilder(this.hostVisProjectDBContext, forReproducing);
			}else if(BuildGraphFromTwoExistingRecordOperation.class.isAssignableFrom(type)){
				return new BuildGraphFromTwoExistingRecordOperationBuilder(this.hostVisProjectDBContext, forReproducing);
			}else {
				//unrecognized
				throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
			}
		}else if(SingleGenericGraphAsInputOperation.class.isAssignableFrom(type)){
			if(InputGraphTypeBoundedOperation.class.isAssignableFrom(type)) {
				if(GraphNode2DLayoutOperationBase.class.isAssignableFrom(type)) {
					if(CircularLayout2DOperation.class.isAssignableFrom(type)) {
						return new CircularLayout2DOperationBuilder(this.hostVisProjectDBContext, forReproducing);
					}else if(FRLayout2DOperation.class.isAssignableFrom(type)){
						return new FRLayout2DOperationBuilder(this.hostVisProjectDBContext, forReproducing);
					}else if(SpringLayout2DOperation.class.isAssignableFrom(type)){
						return new SpringLayout2DOperationBuilder(this.hostVisProjectDBContext, forReproducing);
					}else {
						//unrecognized
						throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
					}
				}else {
					//unrecognized
					throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
				}
			}else if(TransformGraphOperation.class.isAssignableFrom(type)){
				return new TransformGraphOperationBuilder(this.hostVisProjectDBContext, forReproducing);
			}else {
				//unrecognized
				throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
			}
		}else if(SQLOperationBase.class.isAssignableFrom(type)){
			if(GenericSQLOperation.class.isAssignableFrom(type)) {
				//
				return new GenericSQLOperationBuilder(this.hostVisProjectDBContext, forReproducing);
			}else if(PredefinedSQLBasedOperation.class.isAssignableFrom(type)){
				if(SingleInputRecordDataPredefinedSQLOperation.class.isAssignableFrom(type)) {
					if(AddNumericCumulativeColumnOperation.class.isAssignableFrom(type)) {
						return new AddNumericCumulativeColumnOperationBuilder(this.hostVisProjectDBContext, forReproducing);
					}else if(GroupAndBinCountOperation.class.isAssignableFrom(type)){
						return new GroupAndBinCountOperationBuilder(this.hostVisProjectDBContext, forReproducing);
					}else {
						//unrecognized
						throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
					}
				}else {
					//unrecognized
					throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
				}
			}else {
				//unrecognized
				throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
			}
		}else if(VfTreeTrimmingOperationBase.class.isAssignableFrom(type)){
			if(RerootTreeOperation.class.isAssignableFrom(type)) {
				return new RerootTreeOperationBuilder(this.hostVisProjectDBContext, forReproducing);
			}else if(SiblingNodesReorderOperation.class.isAssignableFrom(type)){
				return new SiblingNodesReorderOperationBuilder(this.hostVisProjectDBContext, forReproducing);
			}else if(SubTreeOperation.class.isAssignableFrom(type)){
				return new SubTreeOperationBuilder(this.hostVisProjectDBContext, forReproducing);
			}else {
				//unrecognized
				throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
			}
		}else {
			//unrecognized
			throw new IllegalArgumentException("given operation type is invalid or not supported yet: "+type.getSimpleName());
		}
		
		
	}
	
	
	
	
}
