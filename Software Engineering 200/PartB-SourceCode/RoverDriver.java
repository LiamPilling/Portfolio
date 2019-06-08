import java.util.*;

public class RoverDriver extends Driver
{
   private List<DriverObserver> observers;
   
   public RoverDriver()
   {
      super();
      observers = new LinkedList<DriverObserver>();
   }
   
   public void addObserver(DriverObserver obs)
   {
      observers.add(obs);
   }
   
   public void removeObserver()
   {
      observers.remove(0);
   }
   
   protected void moveFinished()
   {
      for(DriverObserver obs : observers)
      {
         obs.moveFin();
      }
   }
   
   protected void mechanicalError()
   {
      for(DriverObserver obs : observers)
      {
         obs.mechError();
      }
   }
}
