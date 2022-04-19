package core.pipeline.fileformat;

import java.sql.SQLException;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.fileformat.steps.SelectImportModeStepManager;
import fileformat.FileFormat;


/**
 * process manager to import a new FileFormat into the DB of a VisProjectDBContext;
 * 
 * root step controller is {@link SelectImportModeStepManager}
 * @author tanxu
 * 
 */
public class FileFormatProcessMainManager extends AbstractProcessMainManager<FileFormat>{
	private final static String TITLE = "Make new File Format";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws SQLException 
	 */
	public FileFormatProcessMainManager() {
		super(FileFormat.class, SelectImportModeStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}
	
}
