package builder.visframe.fileformat;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.fileformat.record.RecordDataFileFormatBuilder;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import fileformat.FileFormat;
import fileformat.record.RecordDataFileFormat;

public class FileFormatBuilderFactory implements VisframeUDTTypeBuilderFactory<FileFormat>{
	private static FileFormatBuilderFactory SINGLETON;
	
	public static FileFormatBuilderFactory singleton() {
		if(SINGLETON==null) {
			SINGLETON = new FileFormatBuilderFactory();
		}
		return SINGLETON;
	}
	
	//////////////////////////////////////////////
	/**
	 * private constructor
	 */
	private FileFormatBuilderFactory(){
		//
	}
	
	@Override
	public AbstractFileFormatBuilder<? extends FileFormat> build(FileFormat entity) throws IOException, SQLException {
		if(entity instanceof RecordDataFileFormat) {
			RecordDataFileFormat rff = (RecordDataFileFormat) entity;
			RecordDataFileFormatBuilder builder = new RecordDataFileFormatBuilder(
					rff.getBetweenRecordStringFormat().isSequential(),
					rff.getWithinRecordAttributeStringFormat().isStringDelimited());
			
			builder.setValue(rff, false);
			
			return builder;
			
		}else {
			throw new UnsupportedOperationException("un-implemented for this type of FileFormat");
		}
		
	}

	
}
