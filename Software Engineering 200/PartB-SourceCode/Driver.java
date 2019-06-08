import java.util.*;

public abstract class Driver 
{
   public Driver() {}
   public void drive(double distance)
   {
      System.out.println("Moving " + distance + " units");
      moveFinished();
   }
   public void turn(double angle) 
   {
      System.out.println("Turning " + angle + " degrees");
      moveFinished();
   }
   protected abstract void moveFinished();
   protected abstract void mechanicalError();
}
