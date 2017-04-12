package io.jnorthr.toolkit;

import java.io.*
import javax.swing.JOptionPane;

// stuff for regular expression filtering
import java.util.regex.* 

// configuration support utility
import io.jnorthr.tools.configuration.Configurator;
import io.jnorthr.toolkit.Chooser;

// groovy code to choose one folder to walk thru the files found within it
// **************************************************************
import java.io.File;
import java.io.IOException;

//import org.apache.log4j.*
//import groovy.util.logging.*  
//import javax.swing.JFileChooser;

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
* @since   2017-01-22
*/
@Slf4j
public class Walker 
{       
    /**
     * List to hold user selected files from the chooser dialog.
     */
    def files = []
    
    /**
     * Keeps name of folder being traversed.
     */
    String fn = '~';

    /**
     * Count of selected target files in folder being traversed.
     */
    def count = 0;

    /**
     * List of selected target file names in folder being traversed.
     */
    def result

    /**
     * Map of selected target file suffixes in folder being traversed.
     */
    def map=[:]

    /**
     * Temporary boolean used in provided Closure
     */
    boolean yn;

    /**
     * Temporary string variable.
     */
    def ss;

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
    Response re;
           
    // include files using this RegEx pattern    
	Pattern pattern = ~/^.*\.groovy$/

    /**
     * flag that is true when file walking should be influenced by select/omit options.
     */
	boolean includeFlag = false;
	
	
    /**
     * Handle to Configuration object.
     */
	Configurator c = new Configurator('.walker.json');
	
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to let user pick either a file or a folder
    */
    public Walker()
    {
    	String priorPath = c.getInputPath();
    	re = new Response();

        re.returncode = JOptionPane.showOptionDialog(null,
            "Re-use prior path ${priorPath} ?",
            "Use Prior Path", // frame title
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  //the titles of buttons
            options[0]); //default button title
                    
        if (re.returncode==JOptionPane.NO_OPTION) 
        {
	        ch = new Chooser();
    	    ch.selectFolderOnly();
        	re = ch.getChoice();
        	if (re.chosen)
        	{
            	say "path="+re.path;
            	say "file name="+re.artifact;    
            	say "Ready to parse input folder "+re.fullname;    
            	fn = re.path;
        	}
        	else
        	{
            	say "User made no choice, so output fullame is "+re.fullname+" and path="+re.path;
            	say "artifact name="+re.artifact;    
            	fn = re.path;
            	System.exit(0);
        	} // end of else
        } // end of NO
        
        else
        {
	        re.chosen = true;
			re.path = priorPath;        
			re.artifact = "";
			re.fullname = priorPath;
	        re.found = new File(re.fullname).exists()
    	    if (re.found)
        	{
            	re.isDir = new File(re.fullname).isDirectory();
        	}
        } // end of YES
        
        fn = re.path;
        
    } // end of constructor


   // =========================================================================
   /** 
    * Non-default class constructor.
    *
    * user provides a string of either a file or a folder
    */
    public Walker(String pathname)
    {
    	this.fn = pathname;
        re = new Response();
        re.chosen = true;
        re.returncode = JFileChooser.APPROVE_OPTION;
        re.path = pathname;    
        re.artifact = null;
        re.fullname = pathname;
        re.found = new File(re.fullname).exists()
        if (re.found)
        {
            re.isDir = new File(re.fullname).isDirectory();
        }
    } // end of constructor



   // -------------------------------------------------------        
   /** 
    * Closure.
    *
    * defaults to walking the folder looking for files that match a RegEx value
    */
    Closure findTxtFileClos = {
        it.eachDir(findTxtFileClos);
        it.eachFile() {file ->            //Match(~/.*.adoc/) {file ->
                yn = (file.name.startsWith('.')) ? true :  false;
                if (!yn && !file.absolutePath.contains('/.git'))
                {
                    count++;
                    say file.absolutePath;
                    int i = file.name.lastIndexOf('.');                                                                
                    if (i>-1) 
                    {
                        ss = file.name.substring(i+1).toLowerCase()
                        println "i=$i "+ss+" ="+file.name;
                        if(map.containsKey(ss)) {
                          map[ss]+=1;
                        }
                        else
                        {
                            map[ss]=1;
                        }
                    }
                    result += "${file.absolutePath}\n"
                }
        }
    } // end of closure


   /**
    * Method to examine the chosen folder.
    */
    public void parse()
    {
        say "\n------------------------\nparse() ----->"
        //new File('/Users/jimnorthrop/Dropbox/Projects/FileWalker/src/main/groovy/net/jnorthr/support').eachFileMatch(~/^.*\.groovy$/) { files << it.name };
/*
        def transform = { str, transformation ->
              transformation(str)
        }
*/
		boolean hasPattern = includeFlag; 
		if (hasPattern)
		{
           new File(fn).eachDirRecurse { dir ->
                dir.eachFileMatch(pattern) 
                {myfile ->
                	say  "$myfile matched pattern "+pattern;
                	files << myfile;
            	} // eachFileMatch
           } // end of eachDir
		} // end of if
		
		else
		{
           new File(fn).eachDirRecurse { dir ->
                dir.eachFile 
                {myfile ->
                	say  "$myfile matched pattern "+pattern;
                	files << myfile;
            	} // eachFileMatch
           } // end of eachDir
		} // end of else
		
		
        count = 0
        files.each
        {
            count++;
            println it;
        } // end of each

        println "\n===================================\nFound $count files\n"
    } // end of run


    
   /**
    * Method to examine the chosen folder.
    */
    public void run()
    {
        findTxtFileClos(new File(fn))
        say "\n===================================\nFound $count files\nMap contains:"
        map.each{ k, v -> say "${k}:${v}" }
        say "---  end of map ---"
    } // end of run


   /** 
    * Produce log messages using .info method
    */
    public void say(String msg)
    {
        log.info msg;
    } // end of say
    
    
   /** 
    * Set or reset RegEx include pattern flag
    */
    public include(Pattern pat)
    {
        this.pattern = pat;
        includeFlag = ( pat!=null && pat!='' );
    } // end of include
    
   /** 
    * Set or reset RegEx include pattern flag
    */
    public include(String pat)
    {
    	say "pattern:"+pat
        this.pattern = Pattern.compile(pat, Pattern.CASE_INSENSITIVE);
        includeFlag = ( pat!=null && pat!='' );
    } // end of include
    
    
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
        Walker ch;
        
        /*
         * need to test get image only files like .jpg using Filter class
         */
        if (args.size() > 0) 
        { 
            println "... loading Walker from  args[0]="+args[0]+"\n"
            ch = new Walker(args[0]);
            if (args.size() > 0) { ch.include( args[1] ); }
        }
        else
        {  
            ch = new Walker();
        } // end of 
        
        println "\n---------------\n"
        ch.run();
        println "\n---------------\n"
        ch.parse();
        System.exit(0);
    } // end of main    
    
} // end of class