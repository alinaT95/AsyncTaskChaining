package android.example.asynctaskchaining;

/**
 *  Override it to grab error messages happened during card operation
 */
@FunctionalInterface
public interface Rejecter {
  void reject(String errorMsg);
}
