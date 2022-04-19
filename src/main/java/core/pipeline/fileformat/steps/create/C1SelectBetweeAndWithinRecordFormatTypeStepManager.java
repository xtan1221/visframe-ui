package core.pipeline.fileformat.steps.create;

import java.io.IOException;

import core.pipeline.AbstractProcessStepManager;
import core.pipeline.fileformat.steps.create.C1SelectBetweeAndWithinRecordFormatTypeStepSettings.BetweenRecordFormatType;
import core.pipeline.fileformat.steps.create.C1SelectBetweeAndWithinRecordFormatTypeStepSettings.WithinRecordFormatType;
import fileformat.FileFormat;

/**
 * step to select the type of between and within record structure types;
 * 
 * previous step is {@link BSelectRecordBuildingModeStepManager};
 * 
 * next step is {@link DBuildRecordFileFormatWithTemplateStepManager}
 * @author tanxu
 *
 */
public class C1SelectBetweeAndWithinRecordFormatTypeStepManager extends AbstractProcessStepManager<FileFormat, C1SelectBetweeAndWithinRecordFormatTypeStepSettings,C1SelectBetweeAndWithinRecordFormatTypeStepController> {
	private final static String DESCRIPTION = "Select type of record file format";
	private final static C1SelectBetweeAndWithinRecordFormatTypeStepSettings DEFAULT_SETTINGS = 
			new C1SelectBetweeAndWithinRecordFormatTypeStepSettings(BetweenRecordFormatType.SEQUENTIAL, WithinRecordFormatType.DELIMITED_BY_STRING);	
	
	
	static C1SelectBetweeAndWithinRecordFormatTypeStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 */
	public static C1SelectBetweeAndWithinRecordFormatTypeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new C1SelectBetweeAndWithinRecordFormatTypeStepManager();
			
			SINGLETON.addPreviousStep(BSelectRecordBuildingModeStepManager.SINGLETON);
			
			SINGLETON.addNextStep(DBuildRecordFileFormatFromScratchStepManager.singleton());

			SINGLETON.finishInitialization();
			
		}
		
		return SINGLETON;
	}
	
	/**
	 * constructor
	 * @param possibleNextStepControllerIndexMap
	 * @param possiblePreviousStepControllerIndexMap
	 */
	protected C1SelectBetweeAndWithinRecordFormatTypeStepManager() {
		super(FileFormat.class, C1SelectBetweeAndWithinRecordFormatTypeStepController.FXML_FILE_DIR_STRING,
				DESCRIPTION,
				DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		// TODO Auto-generated method stub
		return this.getPossibleNextStepList().indexOf(DBuildRecordFileFormatFromScratchStepManager.singleton());
	}

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
