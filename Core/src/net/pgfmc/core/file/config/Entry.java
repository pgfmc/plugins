package net.pgfmc.core.file.config;

@Deprecated
public class Entry<T> {
	
	final String key;
	private Serializer<T> serializer;
	
	private Object data = null;
	
	protected Entry(String key) {
		this.key = key;
	}
	
	public void setSerializer(Serializer<T> ser) {
		
		if (serializer != null) {
			serializer = ser;
			return;
		}
		System.out.println("Serializer has already been set!");
	}
	
	public void setDefault(Object data) {
		
		if (this.data == null) {
			if (data == null) {
				throw new IllegalArgumentException("\"data\" in Entry.setDefault() cannot be null!");
			}
			@SuppressWarnings("unchecked")
			T gamer = (T) data;
			
			
			this.data = gamer;
		}
	}
	
	public void setValue(Object data) {
		
		
		
		
		this.data = data;
	}
	
	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T) data;
	}
}
