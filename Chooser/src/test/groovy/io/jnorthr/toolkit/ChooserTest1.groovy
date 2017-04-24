package io.jnorthr.toolkit;

/**
* The ChooserTest1 program runs a single test against the Chooser class
*
* allows user to choose a single folder to write to
*
* @author  jnorthr
* @version 1.0
* @since   2017-04-11 
*/
public class ChooserTest1 
{
    
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
		 * need to test the feature to allow user to choose a single folder to write to
		 */    
		Response re;  
        def ch = new Chooser();

		ch.say "------------------------\nRunning ChooserTest1 ---"
        ch.say "Select output folder feature"
        ch.setTitle("Select a Folder to Write To");
        ch.selectFolderOnly();
        
        re = ch.getChoice(); 
    	//println re;

		if (re.abort)
    	{
    		println "... user clicked 'cancel' button"
    	} // end of if
    	
        if (re.chosen && !re.abort)
        {
        	ch.say "... path="+re.path+" artifact name="+re.artifact;    
            ch.say "    the full name of the output file is "+re.fullname;
            ch.say "... isDir ? = "+re.isDir;    
        }
        else
        {
            ch.say "... no choice was made so output path is "+re.path+" and name="+re.artifact;
		}

		println "------------------------\n--- he end ---"

       System.exit(0);
    } // end of main
    
} // end of class