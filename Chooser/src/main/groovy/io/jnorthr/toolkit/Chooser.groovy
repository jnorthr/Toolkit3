//@Grab('log4j:log4j:1.2.17')  use this when running outside gradle or groovyConsole

// https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html

package io.jnorthr.toolkit;
// groovy sample to choose one file using java's  JFileChooser
// would only allow choice of a single directory by setting another JFileChooser feature
// http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
// see more examples in above link to include a file filter
// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
// **************************************************************
import io.jnorthr.toolkit.PathFinder;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.*
import groovy.util.logging.Slf4j

/**
* The Chooser program implements a support application that
* allows user to pick a single file or single folder directory.
*
* Initially starts to choose artifacts from program working directory and saves user
* choice of path in a local text file 
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-01 
*/
@Slf4j
public class Chooser 
{
    /**
     * A class reflecting values chosen by the user of JFileChooser.
     */
    Response re = new Response();


    /**
     * True if the user can select a single local file artifact.
     */
    boolean fileSelect = true;


    /**
     * True if the user can select a single local directory folder artifact.
     */
    boolean pathSelect = true;
	
    /**
     * Handle to component used by the chooser dialog.
     */
    JFileChooser fc = null;
    
    
    /**
     * Integer value to influence the dialog of what's allowed in the user's inter-action with the chooser. 
     * For example: JFileChooser.FILES_AND_DIRECTORIES, JFileChooser.DIRECTORIES_ONLY, JFileChooser.FILES_ONLY 
     */
    java.lang.Integer mode = JFileChooser.FILES_AND_DIRECTORIES;


    /**
     * This is the title to appear at the top of user's dialog. It confirms what we expect from the user.  
     */
    String menuTitle = "Make a Selection";
    

    /**
     * This is logic to get the name of the home folder used by this user.  
     */
    PathFinder pf = new PathFinder();
    
    
   // =========================================================================
   /** 
    * Class constructor.
    * defaults to let user pick either a file or a folder
    */
    public Chooser()
    {
    	log.info("this is an .info msg from the Chooser default constructor");
        setup();
    } // endof constructor
    

   /** 
    * Ask JFileChooser to only allow user to pick a local file but not folder.
    */
    public void setTitle(String newTitle)
    {
    	log.info("setTitle(String ${newTitle})");
        menuTitle = newTitle;
        fc.setDialogTitle(menuTitle);
    } // end of method

   /** 
    * Ask JFileChooser to only allow user to pick a local file but not folder.
    */
    public void selectFileOnly()
    {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        pathSelect = false;
        fileSelect = true;
    } // endof method


   /** 
    * Ask JFileChooser to only allow user to pick a local directory folder but not a file.
    */
    public void selectFolderOnly()
    {
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileSelect = false;
        pathSelect = true;
    } // endof method


   /** 
    * Ask JFileChooser to only allow user to pick a local directory folder but not a file.
    */
    public void selectBoth()
    {
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileSelect = true;
        pathSelect = true;
    } // endof method
    

   /** 
    * Ask JFileChooser to allow user to pick more than a single file.
    */
    public void selectMany()
    {
		fc.setMultiSelectionEnabled(true);
    } // endof method
    
   /**
    * Method to prepare class variables by reading a possibly non-existent cache file written in prior run.
    */
    public void setup()
    {
    	pf.getResponse(re);

        fc = new JFileChooser();
        mode = JFileChooser.DIRECTORIES_ONLY;
		fc.setMultiSelectionEnabled(false);

        if (pathSelect)
        {
            if (fileSelect)
            {
                mode = JFileChooser.FILES_AND_DIRECTORIES;
            }
            else
            {
                mode = JFileChooser.DIRECTORIES_ONLY;
            }
        }
        else
        {
            mode = JFileChooser.FILES_ONLY;
        }       

        fc.setFileSelectionMode(this.mode);
        fc.setDialogTitle(menuTitle);
        
		// this is the only 'set' that makes JFileChooser point to right folder when opening next time
		setPath(re.path)
		fc.setSelectedFile(new File(re.artifact));
    } // end of setup


   /**
    * Method to force JFileChooser to start from a known folder.
    */
    public void setPath(String pa)
    {
		def xx = pa +' '
		println "... Chooser.setPath(String [${pa}])"
    	fc.setSelectedFile(new File(xx))    	
    } // end of setPath

	// =============================================================================
    /**
     * Returns a Response object to indicate what the user did in the JFileChooser dialog. 
     * 
     * This method always returns true if user clicked the APPROVE button indicating 
     * an actual choice was made else returns false if user aborted and failed to make a choice.
     *
     * @return Response object including a boolean true if user clicked the APPROVE button
     *                false if user did not make a choice
     */
    public Response getChoice()
    {
        re.returncode = fc.showOpenDialog(null) ;
        re.chosen = false;
        
        switch ( re.returncode )
        {
            case JFileChooser.APPROVE_OPTION:
                  File file = fc.getSelectedFile();
                  re.chosen = true;
				  re.found = file.exists();
                  re.isDir = file.isDirectory();
                  say "... file.isDirectory() ? ="+re.isDir;
                  
				  // what folder did user finally pick ?
				  re.path = fc.getCurrentDirectory()
    	  		  say "... re.path=${re.path}" 
		
		 		  // what was complete folder+filename user finally picked ?
				  re.fullname = fc.getSelectedFile().toString();
    			  say "... re.fullname=${re.fullname}" 
 
     			
     			  if (re.isDir)
     			  {
     			  	re.path = re.fullname;
     			  	re.artifact = "";
     			  }
     			  else
     			  {
				  	// pick apart the filename from it's path and put that into the artifact field
				  	def ix = re.fullname.lastIndexOf(File.separator);
    			  	re.artifact = (ix < 0) ? re.fullname : re.fullname.substring(ix+1);    	
				  	if (re.artifact==null) { re.artifact = ""; }
    			  	say "... re.artifact=${re.artifact}" 
				  } // end of else
				  
				  // Multiple file selections captured here
		  		  File[] files = fc.getSelectedFiles();
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
				  //re.returncode = JFileChooser.CANCEL_OPTION;
                  re.chosen = false;
                  re.found = new File(re.fullname).exists();
                  re.abort = true;
                  re.isDir = new File(re.fullname).isDirectory();

                  log.info "user cancelled action";
                  break;


            case JFileChooser.ERROR_OPTION:
				  re.returncode = JFileChooser.ERROR_OPTION;
                  re.found = false;
                  re.chosen = false;
                  log.info "user action caused error";
                  break;
        } // end of switch
        
        println "... Chooser getChoice Response=\n"+re.toString();
        return re;
    } // end of pick


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

		// ---------------------------------------------------------------------
		/*
		 * need to test the feature to allow user to choose a filename to saveAs
		 */    
		Response re;  
        def ch = new Chooser();
        ch.selectFileOnly();
        //ch.selectMany();
        re = ch.getChoice(); 
		re.reveal();

/*
		ch.say "------------------------"
        ch.say "Try a SAVE feature"
        ch.saveAs("save.me");
        ch.setOpenOrSave(false);
        ch.setTitle("Pick a Folder and Filename to save");
        re = ch.getChoice(); 
    	if (re.abort)
    	{
    		println "... user clicked 'cancel' button"
    	} // end of if
    	

        if (re.chosen && !re.abort)
        {
        	ch.say "path="+re.path+"\nartifact name="+re.artifact;    
            ch.say "the full name of the output file is "+re.fullname;
            ch.say "isDir ? = "+re.isDir;    
        }
        else
        {
            ch.say "no choice was made so output path is "+re.path+" and name="+re.fullname;
		}


		// ---------------------------------------------------------------------
		/*
		 * need to test the default to allow user to choose a folder OR a file
		 *
		println "\n------------------------\n"
        ch = new Chooser();
        ch.say "Try the default feature"
        re = ch.getChoice(); 
        if (re.abort)
    	{
    		println "user clicked 'cancel' button"
    	} // end of if
    	
		println re;

        if (re.chosen && !re.abort)
        {
        	ch.say "path="+re.path+"\nartifact name="+re.artifact;    
            ch.say "the full name of the selected artifact is "+re.fullname;
            ch.say "isDir ? = "+re.isDir;    
        }
        else
        {
            ch.say "no choice was made so output path is "+re.path+" and name="+re.fullname;
		}
		ch.say "------------------------\n"

		// ---------------------------------------------------------------------
		/*
		 * need to test selecting folders only
		 *
		ch.say "\n------------------------\n"
        ch = new Chooser();
        ch.say "Pick a folder-only test"
        ch.setTitle("Pick input Folder");
        ch.selectFolderOnly();
    	re = ch.getChoice(); 
    	if (re.abort)
    	{
    		println "user clicked 'cancel' button"
    	} // end of if
    	
		println re.reveal();

        if (re.chosen && !re.abort)
        {
            ch.say "path="+re.path+"\nfile name="+re.artifact;    
            ch.say "the full name of the selected folder is "+re.fullname;    
            ch.say "isDir ? = "+re.isDir;    
        }
        else
        {
            ch.say "no choice was made so folder will be "+re.path+" and name="+re.fullname;
		}
		
		ch.say "------------------------\n"


		// ---------------------------------------------------------------------
		/*
		 * need to test get image only files like .jpg using Filter class
		 *
		ch.say "\n------------------------\n"
        ch = new Chooser();
        ch.say "trying to pick a file-only image as png, jpg, gif"
        ch.setTitle("Pick input image");
        ch.selectFileOnly();
        ch.allowImagesOnly();
    	re = ch.getChoice();
    	
		re.reveal();

    	if (re.abort)
    	{
    		println "user clicked 'cancel' button"
    	} // end of if
    	         
        if (re.chosen && !re.abort)
        {
            ch.say "path="+re.path+"\nfile name="+re.artifact;    
            ch.say "the full name of the selected image is "+re.fullname;    
            ch.say "isDir ? = "+re.isDir;   
        }
        else
        {
            ch.say "no choice was made so image file is "+re.path+" and name="+re.fullname;
		}

		ch.say "------------------------\n"
*/

       System.exit(0);
    } // end of main

    
    
} // end of class