public class Main
{
   public static void main(String[] args)
   {
      RoverComm testComm = new RoverComm();
      RoverDriver testDriver = new RoverDriver();
      RoverSoil testSoil = new RoverSoil();
      RoverCamera testCam = new RoverCamera();
      Controller cont = new Controller(testComm, testCam, testSoil, testDriver);
      testComm.receive("0,T20,P,S,M10");   // Test List 0
      testComm.receive("1,L0,M5,L0");      // Test List 1
     // testComm.receive("2,L1,S");        // Test List 2
   }
}
