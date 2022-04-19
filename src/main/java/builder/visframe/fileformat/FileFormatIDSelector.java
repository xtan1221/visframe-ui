package builder.visframe.fileformat;

import java.util.function.Predicate;

import basic.lookup.project.type.udt.VisProjectFileFormatManager;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.FileFormat;
import fileformat.FileFormatID;
import metadata.DataType;


/**
 * 
 * @author tanxu
 *
 */
public class FileFormatIDSelector extends LeafNodeBuilder<FileFormatID, FileFormatIDSelectorEmbeddedUIContentController>{
	private final VisProjectFileFormatManager visProjectFileFormatManager;
	private final DataType dataType;
	private final Predicate<FileFormat> filteringCondition;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param visProjectFileFormatManager
	 * @param dataType
	 * @param filteringCondition filtering condition for the queried FileFormats; only those passes this condition will be shown to be selected; can be null;
	 */
	public FileFormatIDSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectFileFormatManager visProjectFileFormatManager,
			DataType dataType,
			Predicate<FileFormat> filteringCondition) {
		super(name, description, canBeNull, parentNodeBuilder, FileFormatIDSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		
		this.visProjectFileFormatManager = visProjectFileFormatManager;
		this.dataType = dataType;
		this.filteringCondition = filteringCondition;
	}

	
	public VisProjectFileFormatManager getVisProjectFileFormatManager() {
		return visProjectFileFormatManager;
	}


	public DataType getDataType() {
		return dataType;
	}


	public Predicate<FileFormat> getFilteringCondition() {
		return filteringCondition;
	}
	
}
