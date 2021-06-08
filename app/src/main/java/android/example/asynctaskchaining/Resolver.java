package android.example.asynctaskchaining;

/**
 *  Override it to grab the result of card operation
 */
@FunctionalInterface
public interface Resolver {
  void resolve(String value);
}
