package io.jnorthr.toolkit;

import java.io.*
import java.io.File;
import java.io.IOException;

import org.slf4j.*
import groovy.util.logging.Slf4j

/**
* The Walker program implements a support application that allows user to pick a single file or folder directory and then
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
* @since   2017-02-22
*/
@Slf4j
public class PathFinder 
{       
    /**
     * This name points to platform-independent folder path location of the user's core folder.  
     */
    String homePath  = System.getProperty("user.home") + File.separator;


    /**
     * This name points to current platform-independent folder path location the user is now working in.  
     */
    String currentWorkingDirectory  = System.getProperty("user.dir") + File.separator;

    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to provide user.home path to a folder
    */
    public PathFinder()
    {
        homePath = homePath.collectReplacements(replacement);
        currentWorkingDirectory = currentWorkingDirectory.collectReplacements(replacement);
    } // end of constructor


   /** 
    * Provides the calling logic with a platform-independent pointer to the folder the present user owns
    */
    public String getHomePath()
    {
        return homePath;
    } // end of method

   /** 
    * Provides the calling logic with a platform-independent pointer to the current folder the user is working in
    */
    public String getPWD()
    {
        return currentWorkingDirectory;
    } // end of method
    

   /** 
    * Produce log messages using .info method
    */
    // replace windows \ values in homePath with /
    def replacement = 
    {
          // Change \\ to /
         if (it == '\\') 
         {
            '/'
         }
         // Do not transform
         else {
            null
         }
       } // end of replacement
    
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
        PathFinder pf;
        
        pf = new PathFinder();
 
        println "user.home (homePath) ="+pf.getHomePath();
        println "user.dir (pwd)="+pf.getPWD();
         
        println "\n---------------\n--- the end ---"
        //System.exit(0);
    } // end of main    
    
} // end of class