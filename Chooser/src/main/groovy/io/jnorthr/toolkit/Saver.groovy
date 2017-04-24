//@Grab('log4j:log4j:1.2.17')  use this when running outside gradle or groovyConsole
package io.jnorthr.toolkit;

// groovy code to choose a folder and filename to save
// **************************************************************
import java.io.File;
import java.io.IOException;

import io.jnorthr.toolkit.PathFinder;
import io.jnorthr.toolkit.Response;
import javax.swing.JFileChooser;

import org.slf4j.*
import groovy.util.logging.Slf4j

/**
* The Saver program implements a support application that
* allows user to choose an output folder for the project.
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2017-04-20
*/
@Slf4j
public class Saver 
{       
    /**
     * Handle to component used to hold user selected values from the chooser dialog.
     */
    Response re = new Response();
    
    
    /**
     * Handle to component used by the chooser dialog.
     */
    JFileChooser ch = null;

   /**
     * This is logic to get the name of the home folder used by this user.  
     */
    PathFinder pf = new PathFinder();


   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to let user pick either a file or a folder
    */
    public Saver()
    {
    	log.info("This is an .info msg from the Saver default constructor");

        setup(true);
    } // endof constructor    
    
   /** 
    * Class constructor.
    *
    * non-defaults to let user pick a folder only when false
    */
    public Saver(boolean tf)
    {
    	log.info("This is an .info msg from the Saver default constructor");

        setup(tf);
    } // endof constructor    
    
    
   /**
    * Method to prepare class variables
    *
    */
    public void setup(boolean tf)
    {
        ch = new JFileChooser();
		pf.getResponse(re);

		// this is the only 'set' that makes JFileChooser point to right folder when opening next time
		def xx = re.path +' '
    	ch.setSelectedFile(new File(xx))    	
    	// if true, we are doing a file save or false to only pick an output folder
    	if (tf)
    	{
        	ch.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        ch.setDialogTitle("Pick Output Folder & Save Filename");
	    	ch.setSelectedFile(new File(re.artifact));
        }
        else
    	{
	        ch.setDialogTitle("Choose An Output Folder");
        	ch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        
    	ch.setFileHidingEnabled(false);
    } // endof setup
    
    

	// =============================================================================
    /**
     * Returns a boolean to indicate what the user did in the Chooser dialog. 
     * 
     * This method always returns true if user clicked the APPROVE button indicating 
     * an actual choice was made else returns false if user aborted and failed to make a choice.
     *
     * @return Response object filled in if user clicked the APPROVE button
     *                  or set to default values if user did not make a choice
     */
    public Response getChoice()
    {
		re.returncode = ch.showDialog(null,"Save This");

		// now decide what to do with user choice and fill ine Response object depending
        switch ( re.returncode )
        {
            case JFileChooser.APPROVE_OPTION:
                  File file = ch.getSelectedFile();
                  re.chosen = true;
				  re.found = file.exists();
                  re.isDir = file.isDirectory();
                  
				  // what folder did user finally pick ?
				  re.path = ch.getCurrentDirectory()
		
				  // what was complete folder+filename user finally picked ?
				  re.fullname = ch.getSelectedFile().toString();
     	
				  // pick apart the filename from it's path and put that into the artifact field
				  def ix = re.fullname.lastIndexOf(File.separator);
    			  re.artifact = (ix < 0) ? re.fullname : re.fullname.substring(ix+1);
    	
		    	  say "... re.artifact=${re.artifact}" 
				  if (re.artifact==null) { re.artifact = ""; }

                  // to force next time's JFileChooser to pick drilled-down folder, must add final /
                  pf.setPath(re.path.toString()+'/ '+'\n');
                  pf.setFile(re.artifact.toString()); 

		  	      say "... APPROVE_OPTION setPath(${re.path.toString()})" 
		  	      say "... APPROVE_OPTION setFile(${re.artifact.toString()})" 

            	  break;

            case JFileChooser.CANCEL_OPTION:	
		    	  //re.fullname = re.path.toString()+'/'+re.artifact;
                  re.chosen = false;
                  re.found = new File(re.fullname).exists();
                  re.abort = true;
                  re.isDir = new File(re.fullname).isDirectory();
		  	      say "... CANCEL_OPTION has re.fullname as (${re.fullname.toString()})" 

                  break;

            case JFileChooser.ERROR_OPTION:
		  	      say "... ERROR_OPTION happened" 
                  re.found = false;
                  re.chosen = false;
                  re.abort = true;
                  re.isDir = false;
                  break;

		} // end of switch
		
		return re;
	} // end of getChoice


   /** 
    * Produce log messages using .info method
    */
    public void say(String msg)
    {
    	log.info msg;
    } // end of say
    
    
    
	// =============================================================================    
    /**
     * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
     * 
     * argument is a list of strings provided as command-line parameters. 
     * 
     * @param  args a list of possibly zero entries from the command line - not used in this test
     */
    public static void main(String[] args)
    {
		/*
		 * need to test the feature to allow user to choose a filename to saveAs
		 */    
        def obj = new Saver();
        Response re = obj.getChoice();
        re.reveal();
        
        println "\n-------------------------------"
        obj = new Saver(false); // pick output folder only 
        Response re2 = obj.getChoice();
        re2.reveal();
        
        System.exit(0);
    } // end of main    
    
} // end of class