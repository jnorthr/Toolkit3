package io.jnorthr.toolkit.eater;

import java.io.*
import javax.swing.JFileChooser;	//OptionPane;
import javax.swing.JOptionPane
import javax.swing.UIManager;

// stuff for regular expression filtering
import java.util.regex.* 

// configuration support utility
import io.jnorthr.toolkit.PathFinder;
import io.jnorthr.toolkit.Response;
import io.jnorthr.toolkit.Chooser;

// groovy code to choose one folder to walk thru the files found within it
// **************************************************************
import java.io.File;
import java.io.IOException;

//import org.apache.log4j.*

import org.slf4j.*
import groovy.util.logging.Slf4j

/**
* The Eater program implements a support application that allows user to pick a single file or folder directory and then
* step thru that folder. Options allow drill-down into sub-folders or not; can provide a RegEx expression to choose target files;
* can provide optional Closure to use against each chosen file.
*
* Initially starts to choose artifacts from program working directory and saves user
* choice of path in a local text file 
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2017-04-22
*/
@Slf4j
public class EaterHelper
{       
    /**
     * Handle to Chooser utility.
     */
    Chooser ch;
    
    /**
     * Modal choices of Chooser utility.
     */
    Object[] options = ["Reuse Path", "Choose Path"];    
   
    /**
     * Handle to Response object.
     */
    Response re = new Response();
           
	
    /**
     * Handle to configure paths.
     */
	 PathFinder pf = new PathFinder();
	
    /**
     * Handle to hold prior path choice - if any.
     */
     String priorPath = "";	
	
	
    /**
     * Identify the name of the metadata file holding prior path chosen - if any.
     */
	 String eaterMetadata = ".eater.path";
	 
	 
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to let user pick either a file or a folder
    */
    public EaterHelper()
    {
    	say "\nEaterHelper constructor()"
    	UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
    	priorPath = pf.getHomePath();
    	
        if ( !pf.hasPath(eaterMetadata) )
        {
        	say "... pf.hasPath(${eaterMetadata}) ? NO"
			re.returncode = JOptionPane.NO_OPTION;
        } // end of if
        else
        {
            priorPath = pf.getPath(eaterMetadata);
            say "... pf.getPath($eaterMetadata) gives priorPath of "+ priorPath
			re.found = new File(priorPath).exists()
			say "... does priorPath ${priorPath} exist? "+re.found
	    	if (re.found)
	    	{
	    		say "... re.found !" 	    	
	        	re.returncode = JOptionPane.showOptionDialog(null,
    	        	"Re-use prior path ${priorPath} ?",
        	    	"Use Prior Path", // frame title
            		JOptionPane.YES_NO_OPTION,
	            	JOptionPane.QUESTION_MESSAGE,
    	        	null,     //do not use a custom Icon
        	    	options,  //the titles of buttons
            		options[0]
            	); //default button title
            	
	            say "... JOptionPane.showOptionDialog() gaves returncode="+re.returncode
    	        if (re.returncode==-1)
        	    {
            		System.exit(0);
            	} // end of if
        	} // end of if            	
        } // end of else
                    
                    
        if (re.returncode==JOptionPane.NO_OPTION) 
        {
        	say "... re.returncode==JOptionPane.NO_OPTION"
	        ch = new Chooser();
	        ch.setTitle("Pick a Folder to Walk Thru");
    	    ch.selectBoth();		//FolderOnly();
    		ch.setPath(priorPath)    	

			// ask user here:
        	re = ch.getChoice();
        	say "... ch.getChoice() gave "+re.returncode;
        	
        	// stop if red X button on dialog
        	if (re.returncode==-1)
            {
            	System.exit(0);
            } // end of if

        	if (re.chosen)
        	{
        		say "... re.chosen"
            	say "... path="+re.path;
            	say "... file name="+re.artifact;
            	say "... fullname="+re.fullname;
            	//re.path = re.fullname+File.separator+" ";
            	//re.artifact = "";
            	
            	//say "... Ready to parse input folder "+re.fullname;
            	re.isDir = new File(re.fullname).isDirectory();    
            	say "... output fullname is "+re.fullname
            	say "... output path="+re.path;
            	say "... output artifact name="+re.artifact;    
            	pf.setPath(eaterMetadata, re.path );            	
        	}
        	else
        	{
        		say "... not re.chosen = "+re.returncode
            	say "... User made no choice"
            	say "... output fullname is "+re.fullname
            	say "... output path="+re.path;
            	say "... output artifact name="+re.artifact;    
            	
            	re.abort = true;
            	System.exit(0);
        	} // end of else
        } // end of NO
        
        // if YES take same from prior run
        else
        {
        	say "... re.chosen YES = "+re.returncode
	        re.chosen = true;
			re.path = priorPath;        
			re.artifact = "";
			re.fullname = priorPath;
	        re.found = new File(re.fullname).exists()
    	    if (re.found)
        	{
            	re.isDir = new File(re.fullname).isDirectory();
        	}
            say "... output fullname is "+re.fullname
            say "... output path="+re.path;
            say "... output artifact name="+re.artifact;    
        } // end of YES
        
        
        // report on Response vales after GUI
        say "... re said:\n"+re.toString();

    } // end of constructor


   // =========================================================================
   /** 
    * Non-default class constructor.
    *
    * user provides a string of either a file or a folder
    */
    public EaterHelper(String pathname)
    {
    	if (!(new File(pathname).exists()) || !(new File(pathname).isDirectory()) )
    	{
    		throw new java.lang.IllegalArgumentException("EaterHelper constructor needs name of folder that exists; ${pathname} does not.")
    	} // end of if
    	
    	priorPath = pathname;
        re.chosen = true;
        re.returncode = JOptionPane.YES_OPTION;
        re.path = pathname;    
        re.artifact = "";
        re.fullname = pathname;
        re.found = new File(re.fullname).exists()
        if (re.found)
        {
            re.isDir = new File(re.fullname).isDirectory();
        } // end of if

    } // end of constructor



   /**
    * Method to examine the chosen folder.
    */
    public Response resolve()
    {
    	say "... resolve()"
        say "... ---  end of resolve ---"
        return re;
    } // end of resolve


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
	String homePath  = System.getProperty("user.home") + File.separator;
        EaterHelper wh = new EaterHelper(); //"${homePath}Dropbox/Share");        

        println "\n---------------\n"
        Response re = wh.resolve();
        println "\n---------------\n"
        println re;
        println "\n---------------\n"

        System.exit(0);
    } // end of main    
    
} // end of class