package anarchy.minesky.plugin.utils;

public interface Requirement<T> {
    boolean fulfilledBy(T testSubject);
}
