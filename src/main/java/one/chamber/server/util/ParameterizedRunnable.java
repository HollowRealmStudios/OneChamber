package one.chamber.server.util;

@FunctionalInterface
public interface ParameterizedRunnable<T> {

	void run(T t);

}
