package at.ac.tuwien.mnsa.converter;

public interface ByteConverter<T> {
	T convert(byte[] bytes);
	byte[] convert(T t);
}
