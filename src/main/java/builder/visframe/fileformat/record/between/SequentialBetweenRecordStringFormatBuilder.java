package builder.visframe.fileformat.record.between;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NonLeafNodeBuilder;
import fileformat.record.between.SequentialBetweenRecordStringFormat;
import fileformat.record.utils.PlainStringMarker;
import fileformat.record.utils.StringMarker;

public class SequentialBetweenRecordStringFormatBuilder extends BetweenRecordStringFormatBaseBuilder<SequentialBetweenRecordStringFormat>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SequentialBetweenRecordStringFormatBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	/////////////////////////////////////
	
	//no level specific constructor parameter
	
	
	@Override
	protected SequentialBetweenRecordStringFormat build() {
		int numberOfHeadingLinesToSkipValue = (int) this.getChildrenNodeBuilderNameMap().get(numberOfHeadingLinesToSkip).getCurrentValue(); 
		PlainStringMarker commentStringMarkerValue = (PlainStringMarker) this.getChildrenNodeBuilderNameMap().get(commentStringMarker).getCurrentValue();
		StringMarker recordDelimiterValue = (StringMarker) this.getChildrenNodeBuilderNameMap().get(recordDelimiter).getCurrentValue();
		boolean toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecordValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord).getCurrentValue();
		
		
		return new SequentialBetweenRecordStringFormat(
				numberOfHeadingLinesToSkipValue,commentStringMarkerValue,
				recordDelimiterValue,toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecordValue
				);
	}
	
}
