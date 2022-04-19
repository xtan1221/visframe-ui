package builder.visframe.visinstance;

import java.io.IOException;
import java.sql.SQLException;
import context.project.VisProjectDBContext;
import core.builder.LeafNodeBuilder;
import visinstance.VisInstance;

/**
 * builder for a VisSchemeAppliedArchive with a pre-selected applied VisScheme and an assigned UID for VisSchemeAppliedArchive;
 * 
 * @author tanxu
 *
 */
public abstract class VisInstanceBuilderBase<T extends VisInstance, C extends VisInstanceBuilderEmbeddedUIContentControllerBase<T>> 
	extends LeafNodeBuilder<T, C> {

	////////////////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	private final int visInstanceUID;
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param ownerCompositionFunctionGroup
	 * @param indexID
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public VisInstanceBuilderBase(
			String name, String description, String embeddedUIContentFXMLFileDirString,
			
			VisProjectDBContext hostVisProjectDBContext,
			int visInstanceUID
			) throws SQLException, IOException {
		super(name, description, false, null, embeddedUIContentFXMLFileDirString);
		
		//
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.visInstanceUID = visInstanceUID;
	}

	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	
	/**
	 * @return the visInstanceUID
	 */
	public int getVisInstanceUID() {
		return visInstanceUID;
	}

}
