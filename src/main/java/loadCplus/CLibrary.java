package loadCplus;

import com.sun.jna.Callback;
import com.sun.jna.Library;

public interface CLibrary extends Library {

  void printf(String format, Object... args);

  int atoi(String value);

  public interface SignalFunction extends Callback {
    void invoke(int signal);
  }

  SignalFunction signal(int signal, SignalFunction func);
}
