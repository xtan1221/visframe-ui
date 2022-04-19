package builder.visframe.importer.vftree;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.importer.vftree.newick.SimpleNewickVfTreeDataImporterBuilder;
import context.project.VisProjectDBContext;
import exception.VisframeException;
import fileformat.vftree.VfTreeDataFileFormatType;
import importer.vftree.VfTreeDataImporterBase;

public class VfTreeDataImporterBaseBuilderFactory {
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
	public static VfTreeDataImporterBaseBuilder<? extends VfTreeDataImporterBase> make(
			VfTreeDataFileFormatType type,
			VisProjectDBContext visProjectDBContext) throws SQLException, IOException{
		
		if(type.equals(VfTreeDataFileFormatType.SIMPLE_NEWICK_1)) {
			return new SimpleNewickVfTreeDataImporterBuilder(
					visProjectDBContext, 
					true);
		}else if(type.equals(VfTreeDataFileFormatType.SIMPLE_NEWICK_2)) {
			return new SimpleNewickVfTreeDataImporterBuilder(
					visProjectDBContext, 
					false);
		}else{
			throw new VisframeException("selected GraphDataFileFormatType is not implemented yet!");
		}
	}
}
