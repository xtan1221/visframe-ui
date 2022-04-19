package session.toolpanel.process.fileformat;

import core.pipeline.fileformat.FileFormatProcessMainManager;
import fileformat.FileFormat;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;

public class FileFormatToolPanelManager extends AbstractProcessToolPanelManager<FileFormat,FileFormatToolPanelController,FileFormatProcessMainManager>{
	
	public FileFormatToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, FileFormatToolPanelController.FXML_FILE_DIR, new FileFormatProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
