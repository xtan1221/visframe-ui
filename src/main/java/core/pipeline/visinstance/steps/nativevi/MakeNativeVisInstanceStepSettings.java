package core.pipeline.visinstance.steps.nativevi;

import core.pipeline.ProcessStepSettings;
import visinstance.NativeVisInstance;

public class MakeNativeVisInstanceStepSettings implements ProcessStepSettings{
	
	private NativeVisInstance nativeVisInstance;
	
	
	////
	MakeNativeVisInstanceStepSettings(){
		
	}
	
	MakeNativeVisInstanceStepSettings(NativeVisInstance nativeVisInstance){
		this.nativeVisInstance = nativeVisInstance;
	}

	public NativeVisInstance getNativeVisInstance() {
		return nativeVisInstance;
	}
	
	
}
