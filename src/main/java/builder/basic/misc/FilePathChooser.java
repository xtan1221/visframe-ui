package builder.basic.misc;

import java.nio.file.Path;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;

public class FilePathChooser extends LeafNodeBuilder<Path, FilePathChooserEmbeddedUIContentController>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public FilePathChooser(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, FilePathChooserEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
}
