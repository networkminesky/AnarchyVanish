package anarchy.minesky.plugin.hooks;

public class InvalidPluginHookException extends RuntimeException {

    public InvalidPluginHookException() {
    }

    public InvalidPluginHookException(String message) {
        super(message);
    }

    public InvalidPluginHookException(Throwable cause) {
        super(cause);
    }
}
