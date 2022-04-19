package core.table.hasIDTypeRelationalTableSchema.export;

import java.io.IOException;
import java.sql.SQLException;

import core.table.hasIDTypeRelationalTableSchema.HasIDTypeRelationalTableContentViewer;
import javafx.fxml.FXMLLoader;

/**
 * build output file path and file name;
 * 
 * @author tanxu
 *
 */
public class ExporterManager {
	private final HasIDTypeRelationalTableContentViewer hasIDTypeRelationalTableContentViewer;
	
	////////////////////
	private ExporterController controller;
	
	
	public ExporterManager(HasIDTypeRelationalTableContentViewer hasIDTypeRelationalTableContentViewer){
		this.hasIDTypeRelationalTableContentViewer = hasIDTypeRelationalTableContentViewer;
	}
	
	
	////////////////////
	/**
	 * @return the controller
	 * @throws SQLException 
	 */
	public ExporterController getController() throws SQLException {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(ExporterController.FXML_FILE_DIR_STRING));
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		return controller;
	}

	////////////////////
	/**
	 * @return the hasIDTypeRelationalTableContentViewer
	 */
	public HasIDTypeRelationalTableContentViewer getHasIDTypeRelationalTableContentViewer() {
		return hasIDTypeRelationalTableContentViewer;
	}
	
}
