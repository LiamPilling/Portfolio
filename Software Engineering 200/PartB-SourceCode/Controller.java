
import java.util.*;

public class Controller implements CommObserver, SoilObserver, CameraObserver, DriverObserver
{

    private RoverDriver driver;
    private RoverCamera camera;
    private RoverSoil soil;
    private RoverComm comm;
    private boolean isBusy;
    private boolean error;
    private Map<Integer, Queue<Job>> jobListMap;

    public Controller(RoverComm inComm, RoverCamera inCam, RoverSoil inSoil, RoverDriver inDriver)
    {
        comm = inComm;
        comm.addObserver(this);
        camera = inCam;
        camera.addObserver(this);
        soil = inSoil;
        soil.addObserver(this);
        driver = inDriver;
        driver.addObserver(this);
        isBusy = false;
        error = false;
        jobListMap = new HashMap<Integer, Queue<Job>>();
    }

    public void newMessage(String message)
    {
        error = false;
        Queue<Job> tasks = new LinkedList<Job>();
        Job job;
        StringTokenizer st = new StringTokenizer(message, ",");
        String taskString;
        int identifier = 0, i = 0;

        while (st.hasMoreTokens())
        {
            taskString = st.nextToken();
            if (i == 0)
            {
                identifier = Integer.parseInt(taskString);
            }
            else if(taskString.charAt(0) == 'L')
            {
                job = new Job(taskString);
                tasks.add(job); 
            }
            else
            {
                job = new Job(taskString);
                tasks.add(job);  
            }
            i++;
        }

        jobListMap.put(identifier, tasks);
        performTasks(identifier);
    }

    public void soilFin(String analysis)
    {
        System.out.println("Analysis ready: " + analysis);
        isBusy = false;
    }

    public void cameraFin(char[] photodata)
    {
        System.out.println("Photo data ready");
        isBusy = false;
    }

    public void moveFin()
    {
        System.out.println("Move finished");
        isBusy = false;
    }

    public void mechError()
    {
        System.out.println("Mechanical error, abandoning rest of list");
        error = true;
    }

    private int performTasks(int taskNumber)
    {
        double distance, angle;
        Queue<Job> tasks = new LinkedList<Job>(jobListMap.get(taskNumber));
        try
        {
            while (tasks.size() != 0)
            {
                System.out.flush();
                if (error == true)
                {
                    isBusy = false;
                    return 0;
                } 
                else
                {
                    switch (tasks.peek().getName().charAt(0))
                    {
                        case 'M': // Moving
                        {
                            if (isBusy == false)
                            {
                                isBusy = true;
                                distance = Double.parseDouble(tasks.peek().getName().substring(1,
                                        tasks.peek().getName().length()));
                                if((distance < -100) || (distance > 100))
                                    throw new TaskException();
                                driver.drive(distance);
                                tasks.remove();
                            } 
                            else if (tasks.peek().getChecked() == false)
                            {
                                Job j = tasks.remove();
                                j.setChecked(true);
                                tasks.add(j);
                            }
                        }
                        break;
                        case 'T': // Turning
                        {
                            if (isBusy == false)
                            {
                                isBusy = true;
                                angle = Double.parseDouble(tasks.peek().getName().substring(1,
                                        tasks.peek().getName().length()));
                                if((angle < -180) ||(angle > 180))
                                    throw new TaskException();
                                driver.turn(angle);
                                tasks.remove();
                            } 
                            else if (tasks.peek().getChecked() == false)
                            {
                                Job j = tasks.remove();
                                j.setChecked(true);
                                tasks.add(j);
                            }
                        }
                        break;
                        case 'P': // Photo analysis
                        {
                            if (isBusy == false)
                            {
                                isBusy = true;
                                camera.takePhoto();
                                tasks.remove();
                            } 
                            else if (tasks.peek().getChecked() == false)
                            {
                                Job j = tasks.remove();
                                j.setChecked(true);
                                tasks.add(j);
                            }
                        }
                        break;
                        case 'S': // Soil analysis
                        {
                            if (isBusy == false)
                            {
                                isBusy = true;
                                soil.analyse();
                                tasks.remove();
                            } 
                            else if (tasks.peek().getChecked() == false)
                            {
                                Job j = tasks.remove();
                                j.setChecked(true);
                                tasks.add(j);
                            }
                        }
                        break;
                        case 'L': // Input list
                        {
                            if (isBusy == false)
                            {
                                int i = Integer.parseInt(tasks.peek().getName().substring(1,
                                        tasks.peek().getName().length()));
                                if((i == taskNumber) || (!jobListMap.containsKey(i)))
                                    throw new TaskException();
                                performTasks(i);
                                tasks.remove();
                            } 
                            else if (tasks.peek().getChecked() == false)
                            {
                                Job j = tasks.remove();
                                j.setChecked(true);
                                tasks.add(j);
                            }
                        }
                        break;
                        default:
                            throw new TaskException();
                    }
                }
            }
        } 
        catch (TaskException e)
        {
            System.out.println("Invalid task id: " + tasks.peek().getName());
        }
        return 0;
    }
}