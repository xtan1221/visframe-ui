package builder.visframe.importer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import builder.visframe.importer.graph.GraphDataImporterBaseBuilder;
import builder.visframe.importer.graph.GraphDataImporterBaseBuilderFactory;
import builder.visframe.importer.record.RecordDataImporterBuilder;
import builder.visframe.importer.vftree.VfTreeDataImporterBaseBuilder;
import builder.visframe.importer.vftree.VfTreeDataImporterBaseBuilderFactory;
import context.project.VisProjectDBContext;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import importer.DataImporter;
import importer.graph.GraphDataImporterBase;
import importer.record.RecordDataImporter;
import importer.vftree.VfTreeDataImporterBase;

/**
 * 
 * @author tanxu
 *
 */
public class DataImporterBuilderFactory implements VisframeUDTTypeBuilderFactory<DataImporter>{
	private static Map<VisProjectDBContext, DataImporterBuilderFactory> projectSingletonMap;
	
	public static DataImporterBuilderFactory singleton(VisProjectDBContext project) {
		if(projectSingletonMap==null) {
			projectSingletonMap = new HashMap<>();
		}
		
		if(!projectSingletonMap.containsKey(project)) {
			projectSingletonMap.put(project, new DataImporterBuilderFactory(project));
		}
		
		return projectSingletonMap.get(project);
	}
	
	////////////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * private constructor
	 * @param hostVisProjectDBContext
	 */
	private DataImporterBuilderFactory(VisProjectDBContext hostVisProjectDBContext){
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	
	@Override
	public AbstractDataImporterBuilder<?> build(DataImporter entity) throws IOException, SQLException {
		
		if(entity instanceof RecordDataImporter) {
			RecordDataImporter rdi = (RecordDataImporter)entity;
			//boolean canBeNull,RecordDataImporter defaultValue,VisProjectDBContext visProjectDBContext
			RecordDataImporterBuilder builder = new RecordDataImporterBuilder(this.hostVisProjectDBContext);
			
			builder.setValue(rdi, false);
			
			return builder;
			
		}else if(entity instanceof GraphDataImporterBase){
			GraphDataImporterBase gdi = (GraphDataImporterBase)entity;
//			System.out.println(gdi.getFileFormat());
//			System.out.println(gdi.getFileFormat().getFileType());
			GraphDataImporterBaseBuilder<? extends GraphDataImporterBase> builder = GraphDataImporterBaseBuilderFactory.make(
					gdi.getFileFormat().getFileType(), this.hostVisProjectDBContext);
			
			builder.setValue(gdi, false);
			
			return builder;
			
		}else if(entity instanceof VfTreeDataImporterBase){
			VfTreeDataImporterBase tdi = (VfTreeDataImporterBase)entity;
//			System.out.println(tdi.getFileFormat());
//			System.out.println(tdi.getFileFormat().getFileType());
			VfTreeDataImporterBaseBuilder<? extends VfTreeDataImporterBase> builder = VfTreeDataImporterBaseBuilderFactory.make(
					tdi.getFileFormat().getFileType(), this.hostVisProjectDBContext);
			
			builder.setValue(tdi, false);
			
			return builder;
			
		}else {//vftree
			throw new UnsupportedOperationException("un-implemented for this type of DataImporter");
		}
		
		
		
	}
	

}
