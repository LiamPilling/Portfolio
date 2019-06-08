import java.util.*;

public class RoverCamera extends Camera
{
   private List<CameraObserver> observers;
   
   public RoverCamera()
   {
      super();
      observers = new LinkedList<CameraObserver>();
   }
   
   public void addObserver(CameraObserver obs)
   {
      observers.add(obs);
   }
   
   public void removeObserver()
   {
      observers.remove(0);
   }
   
   protected void photoReady(char[] photoData)
   {
      for(CameraObserver obs : observers)
      {
         obs.cameraFin(photoData);
      }
   }
}
