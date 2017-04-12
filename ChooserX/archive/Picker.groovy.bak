//@Grab('log4j:log4j:1.2.17')  use this when running outside gradle or groovyConsole

/* select multi files ? see: http://stackoverflow.com/questions/11922152/jfilechooser-to-open-multiple-txt-files
JFileChooser chooser = new JFileChooser();
chooser.setMultiSelectionEnabled(true);
chooser.showOpenDialog(frame);
File[] files = chooser.getSelectedFiles();
if(files.length >= 2) 
*/

package org.jnorthr.wow;
// groovy code to choose one image file using our Chooser feature
// **************************************************************
import java.io.File;
import java.io.IOException;
import org.apache.log4j.*
import groovy.util.logging.*  

/**
* The Picker program implements a support application that
* allows user to pick a single image file or series of images from a folder directory.
*
* Initially starts to choose artifacts from program working directory and saves user
* choice of path in a local text file 
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-17
*/
@Log4j
public class Picker 
{       
    /**
     * Handle to component used to hold user selected values from the chooser dialog.
     */
    Response re = new Response();
    
    /**
     * Handle to component used by the chooser dialog.
     */
    Chooser ch = null;
    
    // ==============================================
    // Following values set after user choice is made
    
    /**
     * t/f indicator of the user's inter-action with the Picker. True when JFileChooser.APPROVE_OPTION
     */
    boolean result = false;


   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to let user pick either a file or a folder
    */
    public Picker()
    {
    	log.info("this is an .info msg from the Picker default constructor");
        setup();
    } // endof constructor    
    
    
   /**
    * Method to prepare class variables by reading a possibly non-existent cache file written in prior run.
    */
    public void setup()
    {
        ch = new Chooser();
        ch.allowImagesOnly();
        ch.selectFileOnly();
        ch.setTitle("Pick an image");
    	log.info("setup changed ch.setCurrentDirectory to ${ch.initialPath}");
    } // endof setup


	// =============================================================================
    /**
     * Returns a Response object to indicate what the user did in the JFileChooser dialog. 
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
		if (getDir()) { re.chosen = false; }
		return re;
	} // end of getChoice


    /** 
     * to get user selection of path of a known local folder.
     */
    public String getPath()
    {
    	return re.path;
    } // end of getPath


    /** 
     * To get user selection of file but not path of a known local folder.
     */
    public String getArtifact()
    {
    	return re.artifact;
    } // end of getFile


    /** 
     * To get user selection of  full name of a known local folder.
     */
    public String getName()
    {
    	return re.fullname;
    } // end of getName


    /** 
     * To see if user selection is a known local folder.
     */
    public getDir()
    {	
    	return re.isDir;
    } // end of getDir


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
		 * need to test get image only files like .jpg using Filter class
		 */
        def ch = new Picker();
        ch.say "trying to pick a file-only image as png, jpg, gif";
        Response re = ch.getChoice();
        if (re.chosen)
        {
            ch.say "path="+re.path;
            ch.say "file name="+re.artifact;    
            ch.say "the full name of the selected file is "+re.fullname;    
        }
        else
        {
        	ch.say "no choice was made so output filename is "+re.fullname+" and path="+re.path;
            ch.say "artifact name="+re.artifact;    
        }
        
		ch.say "------------------------\n"

       System.exit(0);
    } // end of main    
    
} // end of class