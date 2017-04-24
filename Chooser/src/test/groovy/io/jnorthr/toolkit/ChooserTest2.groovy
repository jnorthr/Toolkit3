package io.jnorthr.toolkit;

/**
* The ChooserTest2 program runs a single test against the Chooser class
*
* Test alows user to choose which folder and also name of file to write to
* @author  jnorthr
* @version 1.0
* @since   2017-04-11 
*/
public class ChooserTest2
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
		 * need to test the feature to allow user to choose a filename to saveAs
		 */    
		Response re;  
        Chooser ch = new Chooser();

		println "------------------------\nRunning ChooserTest2 ---"
        println "... Try a write feature"
        //ch.saveAs("save.me");
        //ch.setOpenOrSave(false);
        ch.setTitle("Pick a Folder to Save to");
        ch.selectFolderOnly();
        
        re = ch.getChoice(); 
    	re.reveal();

		println "------------------------\n--- the end ---"

       System.exit(0);
    } // end of main
    
} // end of class