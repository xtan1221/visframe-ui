package core.builder;

import com.google.common.base.Objects;

/**
 * 
 * @author tanxu
 *
 * @param <T> type of the property; hashCode() and equals() methods are employed
 */
public class Status<T> {
	private boolean defaultEmpty;
	private boolean setToNull;
	private T value;
	
	/**
	 * a default status
	 */
	Status(){
		this.defaultEmpty = true;
		this.setToNull = false;
		this.value = null;
	}
	
	
	/**
	 * 
	 * @param defaultEmpty
	 * @param setToNull
	 * @param value
	 */
	Status(boolean defaultEmpty, boolean setToNull, T value){
		//TODO validations
		this.defaultEmpty = defaultEmpty;
		this.setToNull = setToNull;
		this.value = value;
	}

	
	///////////////////////////////////
	//
	/**
	 * change the status to null;
	 * 
	 * return whether the status is changed;
	 * @return
	 */
	boolean setToNull() {
		if(this.setToNull) {
			return false;
		}else {
			this.setToNull = true;
			return true;
		}
	}
	/**
	 * set the status to non-null;
	 * 
	 * return whether the status is changed;
	 * @return
	 */
	boolean setToNonNull() {
		if(this.setToNull) {
			this.setToNull = false;
			return true;
		}else {
			return false;
		}
		
	}
	/**
	 * 
	 * @return
	 */
	public boolean setToDefaultEmpty() {
		if(this.defaultEmpty) {
			return false;
		}else {
			this.setToNull = false;
			this.defaultEmpty = true;
			this.value = null;
			return true;
		}
	}
	
	/**
	 * set the value to the given non-null object;
	 * 
	 * if success, must set the {@link #defaultEmpty} to false if not;
	 * @param value
	 */
	boolean setValue(T value) {
		if(value==null) {
			throw new IllegalArgumentException();
		}
		
		
		if(this.isSetToNull()) {
			throw new UnsupportedOperationException("cannot set value when set to null is true!");
//			this.setToNull = false;
		}
		
		if(Objects.equal(this.value,value)) {
			return false;
		}else {
			this.value = value;
			this.defaultEmpty = false;
			
			return true;
		}
	}
	
	///////////////////////////////////
	/**
	 * 
	 * @param <T>
	 * @param status
	 * @return
	 */
	static <T> Status<T> copy(Status<T> status){
		return new Status<>(status.isDefaultEmpty(), status.isSetToNull(), status.getValue());
	}
	
	
	
	
	/**
	 * @return the defaultEmpty
	 */
	public boolean isDefaultEmpty() {
		return defaultEmpty;
	}


	/**
	 * @return the setToNull
	 */
	public boolean isSetToNull() {
		return setToNull;
	}
	

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
	
	
	
	
	public boolean hasValidValue() {
		return !this.isDefaultEmpty() && ! this.isSetToNull();
	}

	//////////////////////////////////


	@Override
	public String toString() {
		return "Status [defaultEmpty=" + defaultEmpty + ", setToNull=" + setToNull + ", value=" + value + "]";
	}
	
	
}
