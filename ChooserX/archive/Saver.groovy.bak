//@Grab('log4j:log4j:1.2.17')  use this when running outside gradle or groovyConsole

package org.jnorthr.wow;
// groovy code to choose one image file using our Chooser feature
// **************************************************************
import java.io.File;
import java.io.IOException;
import org.apache.log4j.*
import groovy.util.logging.*  
import org.jnorthr.wow.Response;

/**
* The Saver program implements a support application that
* allows user to choose an output folder for the project.
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-17
*/
@Log4j
public class Saver 
{       
    /**
     * Handle to component used to hold user selected values from the chooser dialog.
     */
    Response re = new Response();
    
    
    /**
     * Handle to component used by the chooser dialog.
     */
    Chooser ch = null;

    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to let user pick either a file or a folder
    */
    public Saver()
    {
    	log.info("this is an .info msg from the Saver default constructor");
        setup();
    } // endof constructor    
    
    
   /**
    * Method to prepare class variables by reading a possibly non-existent cache file written in prior run.
    *
    * Then run logic to ask user to pick an output folder.
    */
    public void setup()
    {
        ch = new Chooser();
        ch.saveAs("fred.html");
        ch.setOpenOrSave(false);
        ch.setTitle("Save Filename in this Output Folder");
        say "trying to pick a project output folder-only";
    } // endof setup



	// =============================================================================
    /**
     * Returns a boolean to indicate what the user did in the Chooser dialog. 
     * 
     * This method always returns true if user clicked the APPROVE button indicating 
     * an actual choice was made else returns false if user aborted and failed to make a choice.
     *
     * @param  menuname the title of the dialog shown to the user
     * @return boolean true if user clicked the APPROVE button
     *                false if user did not make a choice
     */
    public Response getChoice()
    {
		re = ch.getChoice();
		def x = re.artifact.trim().size();
		if (x < 1) { re.chosen = false; }
		return re;
	} // end of getChoice


    /** 
     * to get user selection of path of a known local folder minus trailing artifact.
     */
    public String getPath()
    {
    	return re.path;
    } // end of getPath


    /** 
     * To get user selection of  full name of a known local folder.
     */
    public String getName()
    {
    	return re.fullname;
    } // end of getName


    /** 
     * To get user selection of file name in a known local folder.
     */
    public String getArtifact()
    {
    	return re.artifact
    } // end of getArtifact


    /** 
     * To get user selection: true if user APPROVE.
     */
    public boolean getChosen()
    {
    	return re.chosen;
    } // end of getChosen


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
     * @param  args a list of possibly zero entries from the command line
     */
    public static void main(String[] args)
    {
		/*
		 * need to test the feature to allow user to choose a filename to saveAs
		 */    
        def obj = new Saver();
        Response re = obj.getChoice();
        def msg = (re.chosen) ? "response="+re.toString() : "no choice was made so output is "+re.fullname+" and path="+re.path;
        println msg;
        System.exit(0);
    } // end of main    
    
} // end of class