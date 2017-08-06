package io.jnorthr.toolkit;

import org.slf4j.*
import groovy.util.logging.Slf4j

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* A class that contains results of most recent user dialog
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-27
*/
 
// Use annotation to inject log field into the class.
@Slf4j
public class Response
{
    /**
     * t/f indicator of the user's inter-action with the dialog. True when user hits JFileSaver.APPROVE_OPTION
     */
    boolean chosen = false;


    /**
     * Integer indicator of the user's inter-action with the chooser. For example: JFileChooser.APPROVE_OPTION
     */
    int returncode = -1;


    /**
     * Temp work area holding the absolute path to the user's artifact selected with the chooser. 
     * For example: fc.getCurrentDirectory().getAbsolutePath() 
	 *
	 * example choosing a file artifact:
	 * APPROVE path=/Users/jimnorthrop/Dropbox/Projects/Web/config artifact=logback.xml 
	 * fullname=/Users/jimnorthrop/Dropbox/Projects/Web/config/logback.xml 
	 * rememberpath=/Users/jimnorthrop/.path.txt 
	 * isDir=false
     */
    String path = "";


    /**
     * Temp work area holding only the name of the file the user's artifact selected with the chooser, 
     * but not it's path. Holds a value when Directory_Only choice is in effect of lowest level folder name
     * and parent path is in 'path' variable above.
     */
    String artifact = "";
    
    /**
     * Temp work area holding the full and complete absolute path plus file name of the user's artifact 
     * selected with the chooser. 
     */
    String fullname = "";


    /**
     * Flag set when name of the user's artifact 
     * selected with the chooser is a folder directory 
     */
    boolean isDir = false;

    /**
     * Flag set when name of the user's artifact 
     * points to an actual file or folder that really does exist 
     */
    boolean found = false;


    /**
     * Flag set when user hits cancel button on dialog 
     */
    boolean abort = false;


    /**
     * Flag set true when user chooses several files at once 
     */
    boolean multipleFilesWereSelected = false;


    /**
     * This value is the number of files selected by the user's when .setMultiSelectionEnabled(true);
     */
	int multiFileCount = 0;

	
    /**
     * This work area holds the names of all the files selected by the user when .setMultiSelectionEnabled(true);
     */
    File[] multiFileSelect = [];


    /**
     * This logic keeps the list of zero or possibly more files chosen by the user and sets associated flags,etc;
     */
	public boolean setMany(File[] selected)
	{ 
		multiFileSelect = selected;
		multiFileCount = multiFileSelect.size()
		multipleFilesWereSelected = ( multiFileCount > 0 ) ? true : false;
		return multipleFilesWereSelected;
	} // end of method


    /**
     * This method walks thru the files were chosen in the most recent user interaction applying closure
     * logic to each entry;
     */
	public boolean parseEach(Closure logic)
	{
		multiFileSelect.each{e-> logic(e)}
		return true;
	} // end of method

    /**
     * This method is applying closure logic to Response object;
     */
	public boolean parse(Closure logic)
	{
		logic()
		return true;
	} // end of method

    /**
     * This method is applying closure logic to Response object;
     */
	public boolean parse(def obj,Closure logic)
	{
		logic(obj)
		return true;
	} // end of method


    /**
     * This flag is true when more than zero files were chosen in the most recent user interaction;
     */
	public boolean hasMany()
	{
		return multipleFilesWereSelected;
	} // end of method
	
	
    /**
      * default class constructor
      */
    public Response()
    {
		
    }

    /**
      * class method to pump out log entries as 'info'
      */
    def say()
    {
		log.info this.toString();
    }


    /**
      * reveal() method
      */
    public void reveal() 
    {
		if (abort)
    	{
    		log.info  "... user clicked 'cancel' button"
    	} // end of if
    	
        if (chosen && !abort)
        {
        	log.info  "... path=["+path+"] artifact name=["+artifact+"]";    
            log.info  "    the full name of the output file is ["+fullname+"]";
            log.info  "... isDir ? = "+isDir;    
        }
        else
        {
            log.info  "... no choice was made so output path is ["+path+"] and name=["+artifact+"]";
		}
	} // end of method
	
	
    /**
      * class toString() method
      */
    String toString() {
"""chosen=${chosen}
returncode=${returncode}
path=${path}
artifact=${artifact} 
fullname=${fullname}
found=${found}
isDir=${isDir}
multipleFilesWereSelected=${multipleFilesWereSelected}
multiFileCount=${multiFileCount}
cancelled=${abort}""".toString()
         } // end of toString()    
         

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
        def obj = new Response();
        
        File f1 = new File("queenmary.txt");
        File f2 = new File("bosco.xml");
        File[] picks=[];  //[f1,f2]
		picks + f1;
		picks + f2;
        obj.setMany(picks);
        
        if ( obj.hasMany() )
        {
        	println "obj.hasMany() ? said "+obj.hasMany()
        	println "obj.multiFileCount = "+obj.multiFileCount
        	obj.multiFileSelect.each{e->  println "file "+e; }
        } // end of if
        
        
        println "Response object="+obj.toString();
        obj.reveal();
        println "--- Response end ---\n";
	} // end of main
	
} // end of class 
