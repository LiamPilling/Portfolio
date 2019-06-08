import java.util.*;

public class RoverComm extends Comm
{
   private List<CommObserver> observers;
   
   public RoverComm()
   {
      super();
      observers = new LinkedList<CommObserver>();
   }
   
   public void addObserver(CommObserver obs)
   {
      observers.add(obs);
   }
   
   public void removeObserver()
   {
      observers.remove(0);
   }
   
   protected void receive(String message)
   {
      for(CommObserver obs : observers)
      {
         obs.newMessage(message);
      }
   }
}
