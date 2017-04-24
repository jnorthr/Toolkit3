/*
@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.7.21'),
    @Grab(group='ch.qos.logback', module='logback-classic', version='1.2.3')
])
*/

package io.jnorthr.toolkit;

import java.io.*
import java.io.File;
import java.io.IOException;

import org.slf4j.*
import groovy.util.logging.Slf4j

import io.jnorthr.toolkit.Response;

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
        currentWorkingDirectory = currentWorkingDirectory.collectReplacements(replacement);
        homePath = (System.getProperty("user.home")==null) ? currentWorkingDirectory.collectReplacements(replacement) : homePath.collectReplacements(replacement);
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


       
   /**
    * Method to comfirm if prior path from cache file was written in prior run.
    */
    public boolean hasPath()
    {        
        return hasPath(".path.txt");
    } // end of hasPath
    
    
   /**
    * Method to comfirm if prior path from cache file was written in prior run.
    */
    public boolean hasPath(String name)
    {        
    	String configPath = homePath +name;		// like: /User/jim/.path.txt
        return new File(configPath).exists();
    } // end of hasPath

       
   /**
    * Method to comfirm if prior file choice from cache file was written in prior run.
    */
    public boolean hasFile()
    {        
        return hasPath(".file.txt");
    } // end of hasFile
    
    
   /**
    * Method to comfirm if prior file choice from cache file was written in prior run.
    */
    public boolean hasFile(String name)
    {        
    	String configFile = homePath +name;		// like: /User/jim/.file.txt
        return new File(configFile).exists();
    } // end of hasFile
        
       
   /**
    * Method to influence Chooser to suggest prior path from cache file written in prior run.
    */
    public String getPath()
    {        
        return getPath(".path.txt");
    } // end of getPath
    
       
   /**
    * Method to influence Chooser to suggest prior path from named cache file written in prior run.
    */
    public String getPath(String name)
    {        
    	String configPath = homePath +name;		// like: /User/jim/.path.txt
    	String initialPath = homePath;
    	
		say "... getPath() configPath=${configPath}"
        if (new File(configPath).exists()) 
        { 
        	initialPath = new File(configPath).getText(); 
        	say "... initialPath=${initialPath}"
        } // end of if
        
        say "... getPath() starting Chooser at folder "+initialPath
        return initialPath;
    } // end of getPath


   /**
    * Method to influence Chooser to suggest prior filename chosen using a cache file written in prior run.
    */
    public String getFile()
    {        
    	return getFile(".file.txt");
    } // end of getFile

    
   /**
    * Method to influence Chooser to suggest prior filename chosen using a named cache file written in prior run.
    */
    public String getFile(String name)
    {        
    	String configFile = homePath +name;
        String initialFile = "myfile.txt"

		say "... getFile() configFile=${configFile}"
        if (new File(configFile).exists()) 
        { 
        	initialFile = new File(configFile).getText(); 
        	say "... getFile() initialFile=${initialFile}"
        } // end of if
        
        say "... getFile() suggests user use "+initialFile
        return initialFile;
    } // end of getFile


   /**
    * Method to fill in several values into the Response.
    */
    public Response getResponse(Response re)
    {        
    	re.path = getPath().trim();
    	re.artifact = getFile().trim();
    	re.fullname = re.path + re.artifact;  // File.separator +   
        return re;
    } // end of getResponse
        

   /**
    * Method to influence subsequent Choosers to suggest chosen path by writing cache file here.
    */
    public boolean setPath(String payload)
    {        
		return setPath(".path.txt", payload);    	
    } // end of setPath

        
   /**
    * Method to influence subsequent Choosers to suggest chosen path by writing cache file here.
    */
    public boolean setPath(String name, String payload)
    {        
    	String configPath = homePath +name;
		def fi = new File(configPath);
    	say "... setPath() configPath=${configPath}"
		
        // Use a writer object:
		fi.withWriter('UTF-8') { writer ->
    		writer.write( payload )
    	} // end of withWriter

    	return true;
    } // end of setPath



        
   /**
    * Method to influence subsequent Choosers to suggest a filename by writing cache file here.
    */
    public boolean setFile(def payload)
    {        
		return setFile(".file.txt", payload);    	
    } // end of setFile
        	

        
   /**
    * Method to influence subsequent Choosers to suggest a filename by writing cache file here.
    */
    public boolean setFile(String name, def payload)
    {        
    	String configFile = homePath +name;
    	say "... setFile() configFile=${configFile}"
		def fi = new File(configFile);
		
        // Use a writer object:
		fi.withWriter('UTF-8') { writer ->
    		writer.write( payload )
    	} // end of withWriter

    	return true;    	
    } // end of setFile

/*        	
    // produce a set of path and config file names from a given string
    String computePath(String fn)
    {
        def x = fn.lastIndexOf(File.separator);
        configPath = (x < 0) ? pf.getHomePath() : fn.substring(0,x+1);
        configPath = (new File(configPath).exists()) ? configPath : pf.getHomePath(); // if folder exists use that name else user.home
        
        def t2 =  (x < 0) ? fn : fn.substring(x+1) ;  
        configFileName = (t2.size() < 1) ? configFileName : t2; // if filename missing or blank keep existing name else use new one
        
        configFullName  = configPath + configFileName;
        println "... fn=|${fn}| configPath=|${configPath}| configFileName=|${configFileName}| configFullName=|${configFullName}|\n"
    } // end of method
*/

   /** 
    * Produce messages using println method
    */
    public void say(String msg)
    {
    	println msg;
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
        PathFinder pf;
        
        pf = new PathFinder();
 
        println "... user.home (homePath) =" + pf.getHomePath();
        println "... user.dir (pwd)=" + pf.getPWD();

        
		println "... hasPath('.path2.txt') ? =" + pf.hasPath(".path2.txt");         
		println "... hasFile('.file2.txt') ? =" + pf.hasFile(".file2.txt");         
       
        if ( pf.hasPath(".path2.txt") )
        {
            	String configPath = pf.homePath + ".path2.txt";
		        if (new File(configPath).exists()) 
				{
					new File(configPath).delete();
				} // end of if
        } // end of if
        
        if ( pf.hasFile(".file2.txt") )
        {
            	String configFile = pf.homePath + ".file2.txt";
		        if (new File(configFile).exists()) 
				{
					new File(configFile).delete();
				} // end of if
        } // end of if
        
        
		println "... getPath('.path2.txt')=" + pf.getPath(".path2.txt");         
		println "... getFile('.file2.txt')=" + pf.getFile(".file2.txt");         
        
    	// Default groovy script config payload
    	String payload = """setup {
}""".toString();

		println "... setPath('.path2.txt',payload)=" + pf.setPath(".path2.txt", payload);         
		println "... setFile('.file2.txt',payload)=" + pf.setFile(".file2.txt", payload);         

        
		println "... again getPath('.path2.txt')=" + pf.getPath(".path2.txt");         
		println "... again getFile('.file2.txt')=" + pf.getFile(".file2.txt");         

		Response re = new Response();        
		re = pf.getResponse(re);
		println re.toString();
		
        println "\n---------------\n--- the end ---"
        //System.exit(0);
    } // end of main    
    
} // end of class