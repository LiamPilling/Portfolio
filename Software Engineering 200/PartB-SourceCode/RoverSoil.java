import java.util.*;

public class RoverSoil extends SoilAnalyser
{
   private List<SoilObserver> observers;
   
   public RoverSoil()
   {
      super();
      observers = new LinkedList<SoilObserver>();
   }
   
   public void addObserver(SoilObserver obs)
   {
      observers.add(obs);
   }
   
   public void removeObserver()
   {
      observers.remove(0);
   }
   
   protected void analysisReady(String analysis)
   {
      for(SoilObserver obs : observers)
      {
         obs.soilFin(analysis);
      }
   }
}
