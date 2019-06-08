public abstract class SoilAnalyser
{
   public SoilAnalyser() {}
   public void analyse()
   {
      System.out.println("Analysing soil");
      analysisReady("Dirt");
   }
   protected abstract void analysisReady(String analysis);
}
