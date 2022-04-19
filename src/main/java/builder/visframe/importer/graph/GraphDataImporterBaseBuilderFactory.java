package builder.visframe.importer.graph;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.importer.graph.gexf.SimpleGEXFGraphDataImporterBuilder;
import context.project.VisProjectDBContext;
import exception.VisframeException;
import fileformat.graph.GraphDataFileFormatType;
import importer.graph.GraphDataImporterBase;


/**
 * 
 * @author tanxu
 *
 */
public class GraphDataImporterBaseBuilderFactory {
	/**
	 * build and return a GraphDataImporterBaseBuilder based on the given parameters;
	 * @param type
	 * @param canBeNull
	 * @param defaultValue
	 * @param visProjectDBContext
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static GraphDataImporterBaseBuilder<? extends GraphDataImporterBase> make(
			GraphDataFileFormatType type,
			VisProjectDBContext visProjectDBContext) throws SQLException, IOException{
		
		if(type.equals(GraphDataFileFormatType.SIMPLE_GEXF)) {
			return new SimpleGEXFGraphDataImporterBuilder(visProjectDBContext);
		}else {
			throw new VisframeException("selected GraphDataFileFormatType is not implemented yet!");
		}
	}
}
