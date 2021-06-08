package android.example.asynctaskchaining;

/**
 *  Callback entity to grab the resulting data from the card/error message
 *  The main purpose of creation was to make more uniform and convenient way of working with React native (RN) library for cards
 *  In RN lib we override resolve and reject using standard Promises of RN bridge
 */
public class Callback {
  private Resolver resolve;
  private Rejecter reject;

  public Callback(Resolver resolve, Rejecter reject) {
    set(resolve, reject);
  }

  public void set(Resolver resolve, Rejecter reject) {
    this.resolve = resolve;
    this.reject = reject;
  }

  public Resolver getResolve() {
    return resolve;
  }

  public Rejecter getReject() {
    return reject;
  }

}
