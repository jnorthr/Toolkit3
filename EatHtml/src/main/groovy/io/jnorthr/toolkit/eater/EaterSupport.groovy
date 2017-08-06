package io.jnorthr.toolkit.eater;

import java.io.*

// stuff for regular expression filtering
import java.util.regex.* 

// configuration support utility
import io.jnorthr.toolkit.Response;

// groovy code to choose one folder to walk thru the files found within it
// **************************************************************
import java.io.File;
import java.io.IOException;

import org.slf4j.*
import groovy.util.logging.Slf4j

/**
* This Eater support program implements a support application to examine a given html file for any HREF links
* and write those links to an asciidoctor formatted output file whose file name matches input filename except that
* the suffix is changed from .html to .adoc
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2017-05-22
*/
@Slf4j
public class EaterSupport
{       
    /**
     * Handle to HTML file to examine.
     */
	String fn = "/Users/jimnorthrop/Dropbox/Projects/Toolkit3/EatHtml/bookmarks_19_05_2017.html"

    /**
     * Handle to asciidoctor output filename.
     */
	String fo;


    /**
     * Handle to asciidoctor output file.
     */
	File file3
	

    /**
     * Handle to input filename.
     */
	File fi;
	

    /**
     * Counter of links discovered.
     */
	int ct = 0;


    /**
     * Handle to Response object.
     */
    Response re = new Response();
           

   // =========================================================================
   /** 
    * Default class constructor.
    */
    public EaterSupport()
    {
		def i = fn.toLowerCase().indexOf(".html")
		if (i<0)
		{
   			println "... cannot find .html in $fn"
    		System.exit(0);
		}
		else
		{
    		fo = fn.substring(0,i)
    		fo+="index.adoc"
    		say "... output will be written to "+fo;
		} // end of else

		file3 = new File(fo)
		fi = new File(fn)
    } // end of constructor


   // =========================================================================
   /** 
    * Non-default class constructor.
    *
    * user provides a string of either a file or a folder
    */
    public EaterSupport(String filename)
    {
    	fn = filename;

		def i = fn.toLowerCase().indexOf(".html")
		if (i<0)
		{
   			println "... cannot find .html in $fn"
    		System.exit(0);
		}
		else
		{
    		fo = fn.substring(0,i)
    		fo+="index.adoc"
    		say "... output will be written to "+fo;
		} // end of else


		file3 = new File(fo)
		fi = new File(fn)
    } // end of non-constructor


   /**
    * Method to examine the chosen html file for known HREF links.
    */
    public Response resolve()
    {
    	say "... resolve()"

		// write header lines to output file
		file3.withWriter('UTF-8') { writer ->
    		writer.write('= Bookmarks\n:icons: font\n\n')

		    fi.eachLine{e->
    			def el = e.toLowerCase()
    			def ix = el.indexOf(' href');
    			if (ix > -1)
    			{
        			def tx = e.substring(ix+7)
        			def jx = tx.indexOf('"')
        			def kx = tx.indexOf('>')

			        //say "... tx="+tx.substring(0,jx);

			        def title = tx.substring(kx+1);
        			def mx = title.indexOf('</A')
        			//say "... mx=${mx} title="+title.substring(0,mx);  //x.substring(kx+1);

	    			ct++;
			        writer.write(" * ${tx.substring(0,jx)}[${title.substring(0,mx)}]\n")
        			//say " * ${tx.substring(0,jx)}[${title.substring(0,mx)}]"
    			} // end of if

		    } // end of eachLine

			writer.write("\n\n''''\n\nTIP: Found ${ct} html links\n\n''''\n")
		} // end of file3

        say "... ---  end of resolve ---"
        return re;
    } // end of resolve


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
        EaterSupport es = new EaterSupport();         

        println "\n---------------\n"
        Response re = es.resolve();
        println "\n---------------\n"
        println re;
        println "\n---------------\n"

		println "--- the end ---"

        System.exit(0);
    } // end of main    
    
} // end of class
