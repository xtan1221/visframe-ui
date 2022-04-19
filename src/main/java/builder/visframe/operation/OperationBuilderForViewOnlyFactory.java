package builder.visframe.operation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import context.project.VisProjectDBContext;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import operation.Operation;

/**
 * factory builder class for a {@link AbstractOperationBuilder} to view the details of an existing {@link Operation} instance from a {@link VisProjectDBContext}
 * 
 * delegate to {@link OperationBuilderFactory}
 * 
 * @author tanxu
 *
 */
public class OperationBuilderForViewOnlyFactory implements VisframeUDTTypeBuilderFactory<Operation>{
	private static Map<VisProjectDBContext, OperationBuilderForViewOnlyFactory> projectSingletonMap;
	
	public static OperationBuilderForViewOnlyFactory singleton(VisProjectDBContext project) {
		if(projectSingletonMap==null) {
			projectSingletonMap = new HashMap<>();
		}
		
		if(!projectSingletonMap.containsKey(project)) {
			projectSingletonMap.put(project, new OperationBuilderForViewOnlyFactory(project));
		}
		
		return projectSingletonMap.get(project);
	}
	
	////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	private OperationBuilderFactory operationBuilderFactory;
	
	
	/**
	 * private constructor
	 * @param hostVisProjectDBContext
	 */
	private OperationBuilderForViewOnlyFactory(VisProjectDBContext hostVisProjectDBContext){
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		
		this.operationBuilderFactory = OperationBuilderFactory.singleton(this.hostVisProjectDBContext);
	}
	
	
	/**
	 * since the built AbstractOperationBuilder is for viewing the existing Operation, thus use the reproduced field of the operation instance itself;
	 */
	@Override
	public AbstractOperationBuilder<?> build(Operation operationInstance) throws IOException, SQLException {
		return this.operationBuilderFactory.build(operationInstance, false);
	}
	
}
