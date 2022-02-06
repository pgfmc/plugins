package net.pgfmc.core.file.config;

public class Entry<T> {
	
	final String key;
	private Serializer<T> serializer;
	
	private T data = null;
	
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
	
	public void setDefault(T data) {
		
		if (this.data == null) {
			this.data = data;
		}
	}
	
	public void setValue(T data) {
		this.data = data;
	}
	
	public T getValue() {
		return data;
	}
}
