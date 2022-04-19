package builder.visframe.metadata;

import java.util.Set;
import java.util.function.Predicate;

import basic.lookup.project.type.udt.VisProjectMetadataManager;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import metadata.DataType;
import metadata.Metadata;
import metadata.MetadataID;

public class MetadataIDSelector extends LeafNodeBuilder<MetadataID, MetadataIDSelectorEmbeddedUIContentController> {

	private final VisProjectMetadataManager visProjectMetadataManager;
	private final Set<DataType> allowedDataTypeSet;
	private final Predicate<Metadata> filteringCondition;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param visProjectMetadataManager
	 * @param allowedDataTypeSet
	 * @param filteringCondition
	 */
	public MetadataIDSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectMetadataManager visProjectMetadataManager,
			Set<DataType> allowedDataTypeSet,
			Predicate<Metadata> filteringCondition) {
		super(name, description, canBeNull, parentNodeBuilder, MetadataIDSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		if(allowedDataTypeSet==null||allowedDataTypeSet.isEmpty()) {
			throw new IllegalArgumentException("given allowedDataTypeSet cannot be null or empty!");
		}
		
		this.visProjectMetadataManager = visProjectMetadataManager;
		this.allowedDataTypeSet = allowedDataTypeSet;
		this.filteringCondition = filteringCondition;
	}

	public VisProjectMetadataManager getVisProjectMetadataManager() {
		return visProjectMetadataManager;
	}

	public Set<DataType> getAllowedDataTypeSet() {
		return allowedDataTypeSet;
	}

	public Predicate<Metadata> getFilteringCondition() {
		return filteringCondition;
	}
}
