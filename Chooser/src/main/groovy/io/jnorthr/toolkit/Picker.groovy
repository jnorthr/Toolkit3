//@Grab('log4j:log4j:1.2.17')  use this when running outside gradle or groovyConsole
package io.jnorthr.toolkit;

/* select multi files ? see: http://stackoverflow.com/questions/11922152/jfilechooser-to-open-multiple-txt-files
JFileChooser chooser = new JFileChooser();
chooser.setMultiSelectionEnabled(true);
chooser.showOpenDialog(frame);
File[] files = chooser.getSelectedFiles();
if(files.length >= 2) 
*/

// **************************************************************
// Groovy code to choose one image file using our Chooser feature
// **************************************************************
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.*
import groovy.util.logging.Slf4j

//import org.apache.log4j.*
//import groovy.util.logging.*  

/**
* The Picker program implements a support application to allow a
* user to pick a single image file or series of images from a folder directory.
*
* Initially starts to choose artifacts from program working directory and saves user
* choice of path and filename into local text files 
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-17
*/
@Slf4j
public class Picker 
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

    
    /**
     * This is logic to only permit certain files with specific suffixes.  
     */
	FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG, SVG & GIF Images", "png", "jpg", "gif", "svg");


   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to let user pick either a file or a folder
    */
    public Picker()
    {
    	log.info("this is an .info msg from the Image Picker default constructor");
        setup();
    } // endof constructor    
    
    
   /**
    * Method to prepare class variables by reading a possibly non-existent cache file written in prior run.
    */
    public void setup()
    {
        ch = new JFileChooser();
        re = pf.getResponse(re);
        println "Picker response set to :\n"+re.toString();
		ch.setFileFilter(filter);
		ch.setMultiSelectionEnabled(true);
        ch.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ch.setDialogTitle("Pick an image");
        
		// this is the only 'set' that makes JFileChooser point to right folder when opening next time
		def xx = re.path +' '
    	ch.setSelectedFile(new File(xx))    	
		ch.setSelectedFile(new File(re.artifact));
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
		re.returncode = ch.showDialog(null,"");
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
    	  		  say "... re.path=${re.path}" 
		
		 		  // what was complete folder+filename user finally picked ?
				  re.fullname = ch.getSelectedFile().toString();
    			  say "... re.fullname=${re.fullname}" 
     	
				  // pick apart the filename from it's path and put that into the artifact field
				  def ix = re.fullname.lastIndexOf(File.separator);
    			  re.artifact = (ix < 0) ? re.fullname : re.fullname.substring(ix+1);    	
				  if (re.artifact==null) { re.artifact = ""; }
    			  say "... re.artifact=${re.artifact}" 

		  		  File[] files = ch.getSelectedFiles();
				  say "... ${files.size()} multiple files chosen"
		
                  // to force next time's JFileChooser to pick drilled-down folder, must add final /
                  pf.setPath(re.path.toString()+'/ '+'\n');
                  pf.setFile(re.artifact.toString()); 

		  	      say "... APPROVE_OPTION setPath(${re.path.toString()})" 
		  	      say "... APPROVE_OPTION setFile(${re.artifact.toString()})" 
				  re.setMany(files);
				  re.parse(){f-> println "... hi kids=${f}"; };
				  say "... re=\n"+re.toString();

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
     * @param  args a list of possibly zero entries from the command line
     */
    public static void main(String[] args)
    {
	/*
	 * need to test get image only files like .jpg using Filter class
	 */
        def ch = new Picker();
        ch.say "trying to pick a file-only image as png, jpg, gif, .svg";
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