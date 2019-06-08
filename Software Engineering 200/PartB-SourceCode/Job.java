/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 15520564
 */
public class Job
{
   private String jobName;
   private boolean checked;

   public Job(String inName)
   {
      jobName = inName;
      checked = false;
   }

   public void setChecked(boolean inChecked)
   {
      checked = inChecked;
   }

   public void setName(String inName)
   {
      jobName = inName;
   }

   public boolean getChecked()
   {
      return checked;
   }

   public String getName()
   {
      return jobName;
   }
}
