import mraa.Aio;
import mraa.Gpio;
import mraa.Pwm;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.io.BufferedReader;

public class SmartDoorLock {
  static {
    try {
      System.loadLibrary("mraajava");
    } catch (UnsatisfiedLinkError e) {
      System.err.println(
          "Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" +
          e);
      System.exit(1);
    }
  }
  public static void main(String[] args){
    //instantiate the sensors/motor
    Gpio button = new Gpio(3);
    Aio light = new Aio(3);
    Pwm servo = new Pwm(6);

    //setting default password
    int[] password = {1,1,1,1};
    int[] enteredPassword;

    //test to see if it works

    lock(servo);
    enteredPassword = changePassword(button);

    if(equals(password,enteredPassword)) {
      System.out.println("Correct password.");
      unlock(servo);
    }
    else {
      System.out.println("Incorrect password.")
    }

  }

  public static void unlock(Pwm door) {
    door.enable(true);
    door.period_ms(20);
    door.pulsewidth_us(500);
    try {
      TimeUnit.SECONDS.sleep(1);
    }catch (InterruptedException e) {
    }
    door.enable(false);
  }

  public static void lock(Pwm door) {
    door.enable(true);
    door.period_ms(20);
    door.pulsewidth_us(2500);
    try {
      TimeUnit.SECONDS.sleep(1);
    }catch (InterruptedException e) {
    }
    door.enable(false);
  }

  public static int changePassword(Gpio doorButton) {
    int[] generatedPassword;
    int passLength;
    int value;
    String continue = "y";

    BufferedReader length = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("How long is the password?");
    passLength = Integer.parseInt(length.readLine());

    for (int i = 0; i < passLength, i++) {
      BufferedReader cont = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Type y to continue recording the password: ");
      continue = cont.readLine();

      if (continue == "y") {
        value = doorButton.read();
        generatedPassword[i] = value;
      }
      else {
        break;
      }

    }
    System.out.println("The password is " + Arrays.toString(generatedPassword));
    return generatedPassword;
  }

}
