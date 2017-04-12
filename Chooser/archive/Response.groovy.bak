package org.jnorthr.wow;
import org.apache.log4j.*
import groovy.util.logging.*  

/**
* A class that contains results of most recent user dialog
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-27
*/
@Log4j
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
    def path = null;


    /**
     * Temp work area holding only the name of the file the user's artifact selected with the chooser, 
     * but not it's path. Holds a value when Directory_Only choice is in effect of lowest level folder name
     * and parent path is in 'path' variable above.
     */
    def artifact = null;
    
    /**
     * Temp work area holding the full and complete absolute path plus file name of the user's artifact 
     * selected with the chooser. 
     */
    def fullname = null;


    /**
     * Flag set when name of the user's artifact 
     * selected with the chooser is a folder directory 
     */
    boolean isDir = true;


    /**
      * class toString() method
      */
        String toString() {
"""chosen=${chosen} 
returncode=${returncode}
path=${path}
artifact=${artifact} 
fullname=${fullname} 
isDir=${isDir}     
""".toString()
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
        println "Response object=\n"+obj.toString();
	} // end of main
	
} // end of class 
