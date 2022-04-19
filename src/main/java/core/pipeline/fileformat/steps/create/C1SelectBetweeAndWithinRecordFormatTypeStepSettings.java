package core.pipeline.fileformat.steps.create;

import core.pipeline.ProcessStepSettings;

public class C1SelectBetweeAndWithinRecordFormatTypeStepSettings implements ProcessStepSettings {
	private BetweenRecordFormatType betweenRecordFormatType;
	private WithinRecordFormatType withinRecordFormatType;
	
	C1SelectBetweeAndWithinRecordFormatTypeStepSettings(BetweenRecordFormatType betweenRecordFormatType, WithinRecordFormatType withinRecordFormatType){
		this.setBetweenRecordFormatType(betweenRecordFormatType);
		this.setWithinRecordFormatType(withinRecordFormatType);
	}
	
	
	public WithinRecordFormatType getWithinRecordFormatType() {
		return withinRecordFormatType;
	}

	public void setWithinRecordFormatType(WithinRecordFormatType withinRecordFormatType) {
		this.withinRecordFormatType = withinRecordFormatType;
	}


	public BetweenRecordFormatType getBetweenRecordFormatType() {
		return betweenRecordFormatType;
	}


	public void setBetweenRecordFormatType(BetweenRecordFormatType betweenRecordFormatType) {
		this.betweenRecordFormatType = betweenRecordFormatType;
	}


	public static enum BetweenRecordFormatType{
		SEQUENTIAL,
		INTERLEAVING;
	}
	
	public static enum WithinRecordFormatType{
		DELIMITED_BY_STRING;
	}
}
