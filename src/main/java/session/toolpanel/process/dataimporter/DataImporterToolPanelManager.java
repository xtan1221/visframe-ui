package session.toolpanel.process.dataimporter;

import core.pipeline.dataimporter.DataImporterProcessMainManager;
import importer.DataImporter;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;

public class DataImporterToolPanelManager extends AbstractProcessToolPanelManager<DataImporter,DataImporterToolPanelController,DataImporterProcessMainManager>{

	public DataImporterToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, DataImporterToolPanelController.FXML_FILE_DIR, new DataImporterProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
