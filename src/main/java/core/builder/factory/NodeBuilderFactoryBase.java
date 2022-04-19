package core.builder.factory;

import core.builder.NonLeafNodeBuilder;
import core.builder.ui.embedded.content.EmbeddedUIContentController;

public abstract class NodeBuilderFactoryBase<T,E extends EmbeddedUIContentController<T>> implements NodeBuilderFactory<T,E>{
	
//	private final Class<T> type;
	private final String name;
	private final String description;
	private final boolean canBeNull;
	private final String embeddedUIContentFXMLFileDirString;
	
	//////////////
	private NonLeafNodeBuilder<?> parentNodeBuilder;
	
	/**
	 * constructor
	 * @param type
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param embeddedUIContentFXMLFileDirString
	 */
	protected NodeBuilderFactoryBase(
//			Class<T> type,
			String name, String description, boolean canBeNull, 
			String embeddedUIContentFXMLFileDirString){
//		this.type = type;
		this.name = name;
		this.description = description;
		this.canBeNull = canBeNull;
//		this.parentNodeBuilder = parentNodeBuilder;
		this.embeddedUIContentFXMLFileDirString = embeddedUIContentFXMLFileDirString;
	}

	@Override
	public void setParent(NonLeafNodeBuilder<?> parent) {
		this.parentNodeBuilder = parent;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @return the canBeNull
	 */
	@Override
	public boolean canBeNull() {
		return canBeNull;
	}


	/**
	 * @return the parentNodeBuilder
	 */
	public NonLeafNodeBuilder<?> getParentNodeBuilder() {
		return parentNodeBuilder;
	}


	/**
	 * @return the embeddedUIContentFXMLFileDirString
	 */
	public String getEmbeddedUIContentFXMLFileDirString() {
		return embeddedUIContentFXMLFileDirString;
	}
	
	
}
