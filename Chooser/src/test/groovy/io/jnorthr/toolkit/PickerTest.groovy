package io.jnorthr.toolkit;

/**
* The PickerTest program runs a single test against the image Picker class
*
* Test alows user to choose one input IMAGE filename
*
* @author  jnorthr
* @version 1.0
* @since   2017-04-11 
*/
public class PickerTest
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
		 * need to test the feature to allow user to choose an image filename 
		 */    
        def ch = new Picker();
        ch.say "trying to pick a file-only image as png, jpg, gif, .svg";
        Response re = ch.getChoice();
        if (re.chosen)
        {
            ch.say "path="+re.path;
            ch.say "file name="+re.artifact;    
            ch.say "the full name of the selected file is "+re.fullname;    
        }
        else
        {
            ch.say "no choice was made so output filename is "+re.fullname+" and path="+re.path;
            ch.say "artifact name="+re.artifact;    
        }

		println "------------------------\n--- the end ---"

       System.exit(0);
    } // end of main
    
} // end of class