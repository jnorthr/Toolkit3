package io.jnorthr.toolkit;

// stuff for regular expression filtering
import java.util.regex.* 


// groovy code to choose one folder to walk thru the files found within it
// **************************************************************
import java.io.File;
import java.io.IOException;
import java.io.*
import groovy.io.FileType;

import org.slf4j.*
import groovy.util.logging.Slf4j

/**
* The Walker program implements a support application that allows user to pick a single folder directory and then
* step thru that folder loking at each artifact. 
* Options allow drill-down into sub-folders or not; can provide a RegEx expression to choose target files;
* can provide optional Closure to use against each chosen file.
*
* Initially starts to choose artifacts from program working directory or prior folder choice and saves user
* choice of path in a local text file. Done by asking WalkerHelper for folder name
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2017-04-22
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
     * Temporary boolean used in provided Closure; true if filename starts with a dot
     */
    boolean yn;

    /**
     * Temporary string variable.
     */
    def ss;

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
	
	
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to let user pick either a file or a folder
    */
    public Walker()
    {
    	say "\nWalker constructor()"
    	//String home  = System.getProperty("user.home") + File.separator;
        WalkerHelper wh = new WalkerHelper();  //"${home}Dropbox/Share");        
        re = wh.resolve();
        fn = re.path;
        
        // report on Response vales after GUI
        say re.toString();
    } // end of constructor


   // =========================================================================
   /** 
    * Non-default class constructor.
    *
    * user provides a string of either a file or a folder
    */
    public Walker(String pathname)
    {
    	say "\nWalker constructor()"
        WalkerHelper wh = new WalkerHelper(pathname);        
        re = wh.resolve();
        fn = re.path;        
        
        // report on Response vales after GUI
        say re.toString();
    } // end of non-default constructor



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
                    say "... "+file.absolutePath;
                    int i = file.name.lastIndexOf('.');                                                                
                    if (i>-1) 
                    {
                    	// take trailing filename suffix
                        ss = file.name.substring(i+1).toLowerCase()
                        //println "... i=$i "+ss+" ="+file.name;
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
        say "\n------------------------\n... parse(${fn}) includeFlag=${includeFlag} ----->"

        //new File('/Users/jimnorthrop/Dropbox/Projects/FileWalker/src/main/groovy/net/jnorthr/support').eachFileMatch(~/^.*\.groovy$/) { files << it.name };

		if (includeFlag)
		{
           new File(fn).eachFileRecurse(FileType.FILES) { dir ->
           		say "... eachFileRecurse(FILES) dir="+dir.toString();
           		
                dir.eachFileMatch(pattern) 
                {myfile ->
                	say  "... $myfile matched pattern "+pattern;
                	files << myfile;
            	} // eachFileMatch
           } // end of eachDir
		} // end of if
		
		else
		{
           new File(fn).eachDirRecurse(FileType.FILES) { dir ->
           		say "... 182 dir="+dir.toString();
                dir.eachFile 
                {myfile ->
                	say  "... $myfile matched pattern "+pattern;
                	files << myfile;
            	} // eachFileMatch
           } // end of eachDir
		} // end of else
		
		
        count = 0
        files.each
        {
            count++;
            say "... "+it;
        } // end of each

        say "\n===================================\n... found $count files\n"
    } // end of parse()


    
   /**
    * Method to examine map of file suffixes found in the chosen folder.
    */
    public void run()
    {
    	say "... run()"
        findTxtFileClos(new File(fn))
        say "\n===================================\nFound $count files\nMap contains:"
        //map.each{ k, v -> say "... ${k}:${v}" }
        say "... ---  end of map ---"
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
    	say "... pattern:"+pat
        this.pattern = Pattern.compile(pat, Pattern.CASE_INSENSITIVE);
        includeFlag = ( pat!=null && pat!='' );
        say "... Walker.include(${pat}) gave includeFlag="+includeFlag; 
    } // end of include
    
    
   /** 
    * Produce messages using println method
    */
 	@Override
    public String toString()
    {
    	return """files.size=${files.size()}
fn=${fn}
count=${count}
result=${result}
map=${map}
yn=${yn}
ss=${ss}
pattern=${pattern}
includeFlag=${includeFlag}""".toString();
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
        Walker walk;
        
        /*
         * need to test get image only files like .jpg using Filter class
         */
        if (args.size() > 0) 
        { 
            println "... loading Walker from  args[0]="+args[0]+"\n"
            walk = new Walker(args[0]);
            if (args.size() > 1) { walk.include( args[1] ); }
            println walk;
        }
        else
        {  
            walk = new Walker();
            println walk;
        } // end of 
        println "\n---------------\n"
        walk.parse();
        walk.run();
        println "\n---------------\n"
        System.exit(0);
    } // end of main    
    
} // end of class