public abstract class Camera
{
   public Camera() {}
   public void takePhoto() 
   {
      char[] data = {'c','c'};
      System.out.println("Taking photo");
      photoReady(data);
   }
   protected abstract void photoReady(char[] photoData);
}
