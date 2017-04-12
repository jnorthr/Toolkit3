//@Grab('log4j:log4j:1.2.17')  use this when running outside gradle or groovyConsole

package org.jnorthr.wow;
// groovy sample to choose one file using java's  JFileChooser
// would only allow choice of a single directory by setting another JFileChooser feature
// http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
// see more examples in above link to include a file filter
// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
// **************************************************************

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.*
import groovy.util.logging.*  

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
@Log4j
public class Chooser 
{
    /**
     * A class reflecting values chosen by the user of JFileChooser.
     */
    Response re = new Response();

    /**
     * The kind of JFileChooser to show the user.
     */
    boolean openOrSave = true;

    /**
     * True if the user can select a single local file artifact.
     */
    boolean fileSelect = true;

    /**
     * True if the user can select a single local directory folder artifact.
     */
    boolean pathSelect = true;

    /**
     * A path value to influence the JFileChooser as where to allow the user to initially pick a local file artifact.
     * Can be over-written by a value chosen in the previous run of this module. See 'rememberpath' below
     */
    def initialPath = System.getProperty("user.dir");

    /**
     * A path value to influence the JFileChooser as where to allow the user to initially pick a local file artifact.
     * Can be over-written by a value chosen in the previous run of this module. See 'rememberpath' below
     */
	def initialFile = "fileToSave.txt";

	
    /**
     * Parent component of the dialog.
     */
    JFrame frame = new JFrame();
    
    
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
     * Temp work area holding a default file path and file name. This name points to a cache where the selected 
     * path from a prior run is stored.  
     */
    String rememberpath = System.getProperty("user.home") + File.separator  +".path.txt";


    /**
     * Temp work area holding a default file path and file name. This name points to a cache where the selected 
     * full filename from a prior run is stored.  
     */
    String rememberfile = System.getProperty("user.home") + File.separator  +".file.txt";


    /**
     * This is the title to appear at the top of user's dialog. It confirms what we expect from the user.  
     */
    String menuTitle = "Make a Selection";
    
    
   // =========================================================================
   /** 
    * Class constructor.
    * defaults to let user pick either a file or a folder
    */
    public Chooser()
    {
    	log.info("this is an .info msg from the Chooser default constructor");
    	re.fullname = initialPath;
    	re.path = initialPath;
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
    * Influences JFileChooser to use either the 'Open' dialog if true or the 'Save' dialog if false.
    */
    public void setOpenOrSave(boolean oos)
    {
    	log.info("setOpenOrSave(String ${oos})");
        openOrSave = oos;
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
    * Method to prepare class variables by reading a possibly non-existent cache file written in prior run.
    */
    public void setup()
    {
        boolean present = new File(rememberpath).exists()
        if (present) 
        { 
        	initialPath = new File(rememberpath).getText(); 
        	re.path = initialPath; 
        	re.fullname = re.path;
        } // end of if
        
        present = new File(rememberfile).exists()
        if (present) 
        { 
        	initialFile = new File(rememberfile).getText(); 
        	re.artifact = initialFile; 
        	if (initialFile.size() > 0)
        	{
	        	re.fullname = re.path+File.separator+initialFile;
	        } // end of if
        } // end of if 

        fc = new JFileChooser();
        mode = JFileChooser.DIRECTORIES_ONLY;

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

        File workingDirectory = new File(initialPath); 
        fc.setCurrentDirectory(workingDirectory);
    	log.info("setup changed fc.setCurrentDirectory to ${initialPath}");
    } // endof setup


   /** 
    * Influence JFileChooser to allow user selection to begin from a known local folder.
    */
    public void setPath(String newPath)
    {
        initialPath = newPath;
        File workingDirectory = new File(initialPath);
        if ( !workingDirectory.exists() ) { throw new RuntimeException("Cannot setPath to non-existence path:"+newPath)} 
        fc.setCurrentDirectory(workingDirectory);
    } // endof setPath


   /** 
    * Influence JFileChooser to suggest to user the name of the output file to Save As.
    */
    public saveAs(String filename)
    {
        initialFile = filename;
    } // endof setPath


   /** 
    * Influence JFileChooser to only allow image files to be selected by the user.
    */
    public allowImagesOnly()
    {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG & GIF Images", "png", "jpg", "gif");
	    fc.setFileFilter(filter);
    } // endof setPath


	// =============================================================================
    /**
     * Returns a boolean to indicate what the user did in the JFileChooser dialog. 
     * 
     * argument is a string specifier of the title to show the user on the dialog. 
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
        if (!openOrSave) { fc.setSelectedFile(new File(initialFile)); }
        re.returncode = (!openOrSave) ? fc.showSaveDialog(frame) : fc.showOpenDialog(frame) ;
        re.chosen = false;
        switch ( re.returncode )
        {
            case JFileChooser.APPROVE_OPTION:
                  File file = fc.getSelectedFile();
				  re.returncode = JFileChooser.APPROVE_OPTION;
				  // was this a directory folder ?
                  re.isDir = new File(file.toString()).isDirectory();

                  re.fullname = file.toString();
                  re.path = (re.isDir) ? re.fullname : fc.getCurrentDirectory().getAbsolutePath();
                  re.artifact = (re.isDir) ? "" : file.name; 

                  log.info "APPROVE path="+re.path+" artifact="+re.artifact+" fullname="+re.fullname+" rememberpath="+rememberpath+" isDir="+re.isDir;
                  
                  // keep path and filename for next time
                  def fo = new File(rememberpath)
                  fo.text = (re.isDir) ? re.fullname : re.path;
				  fo = new File(rememberfile)
                  fo.text = (re.isDir) ? "" : re.artifact;
                   
                  re.chosen = true;
                  break;

            case JFileChooser.CANCEL_OPTION:
				  re.returncode = JFileChooser.CANCEL_OPTION;
                  re.chosen = false;
                  log.info "user cancelled action";
                  break;

            case JFileChooser.ERROR_OPTION:
				  re.returncode = JFileChooser.ERROR_OPTION;
                  re.chosen = false;
                  log.info "user action caused error";
                  break;
        } // end of switch
        println "Chooser getChoice Response="+re.toString();
        return re;
    } // end of pick


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
    public String getFile()
    {
    	return re.artifact;
    } // end of getFile


    /** 
     * To get user selection of  full name of a known local folder.
     */
    public String getFullName()
    {
    	return re.fullname;
    } // end of getFullName


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
        def ch = new Chooser();
        ch.say "trying a SAVE feature"
        ch.saveAs("mark.html");
        ch.setOpenOrSave(false);
        ch.setTitle("Pick a Folder and Filename to save");
        Response re = ch.getChoice(); 
        println "Chooser Response="+re.toString();
        if (re.chosen)
        {
        	ch.say "path="+re.path+"\nartifact name="+re.artifact;    
            ch.say "the full name of the selected file is "+re.fullname;
            ch.say "isDir ? = "+re.isDir;    
        }
        else
        {
            ch.say "no choice was made so output path is "+re.path+" and name="+re.fullname;
		}
       System.exit(0);


		// ---------------------------------------------------------------------
		/*
		 * need to test the default to allow user to choose a folder OR a file
		 */
		ch.say "------------------------\n"
        ch = new Chooser();
        ch.say "trying the default feature"
        re = ch.getChoice(); 
        if (re.chosen)
        {
        	ch.say "path="+re.path+"\nartifact name="+re.artifact;    
            ch.say "the full name of the selected file is "+re.fullname;
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
		 */
        ch = new Chooser();
        ch.say "trying to pick a folder-only feature"
        ch.setTitle("Pick input Folder");
        ch.selectFolderOnly();
    	re = ch.getChoice(); 
        if (re.chosen)
        {
            ch.say "path="+re.path+"\nfile name="+re.artifact;    
            ch.say "the full name of the selected file is "+re.fullname;    
            ch.say "isDir ? = "+re.isDir;    
        }
        else
        {
            ch.say "no choice was made so output path is "+re.path+" and name="+re.fullname;
		}
		ch.say "------------------------\n"


		// ---------------------------------------------------------------------
		/*
		 * need to test get image only files like .jpg using Filter class
		 */
        ch = new Chooser();
        ch.say "trying to pick a file-only image as png, jpg, gif"
        ch.setTitle("Pick input image");
        ch.selectFileOnly();
        ch.allowImagesOnly();
    	re = ch.getChoice();         
        if (re.chosen)
        {
            ch.say "path="+re.path+"\nfile name="+re.artifact;    
            ch.say "the full name of the selected file is "+re.fullname;    
            ch.say "isDir ? = "+re.isDir;   
        }
        else
        {
            ch.say "no choice was made so output path is "+re.path+" and name="+re.fullname;
		}
		ch.say "------------------------\n"

       System.exit(0);
    } // end of main

    
    
} // end of class