/*
@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.7.21'),
    @Grab(group='ch.qos.logback', module='logback-classic', version='1.2.3')
])
*/
package io.jnorthr.wow;

//import org.apache.log4j.*
//import groovy.util.logging.*  
import io.jnorthr.toolkit.Saver;
import io.jnorthr.toolkit.PathFinder;

import org.slf4j.*
import groovy.util.logging.Slf4j

/**
* The program implements an application that allows user to pick images for a Photo carousel web page.
*
* Initially starts to choose artifacts from program working directory and saves user
* choice of path in a local text file 
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-01 
*/
@Slf4j
public class WOW
{

    /**
     * bean to hold row data - one <LI> per row
     */
    class Dta
    {
        String title = "";
        String alt = "";
        String href = "";
        String imgsrc = "";
        int imagenumber = 0;
    
    /**
      * class toBullet() method
      * <a href="#" title="Lake"><span><img src="data1/tooltips/lake.jpg" alt="Lake"/>1</span></a>    
      */
        String toBullet() {
"""         <a href="${href}" title="${title}"><span><img src="tooltips/${imgsrc}" alt="${alt}"/>${imagenumber+1}</span></a>    """
        }


    /**
      * class toString() method
      */
        String toString() {
"""        <li><img src="images/${imgsrc}" alt="${alt}" title="${title}" id="wows1_${imagenumber}"/></li>""".toString()
         } // end of toString()    
         
    }// end of class
    // ----------------------------------------------------------


    /**
     * output directory user chose - output file is written here
     */
    def cp= "."
    
    /**
     * Flag set to generate a complete HTML file  
     */
    boolean addWrapper = true;


    /**
     * Flag set true to cause audit printlines
     */
    boolean audit = true;


    /**
     * Output file handle
     */
    File fo;

    

    /**
     * list holding rows of beans - one each per <LI> image
     */
    def dta=[];
    
    
    /**
     * working internal counter to provide images ID tie-breaker plus bullet number
     */
    int imagenumber = 0;
    

 
    /**
     * holds string of HTML text per LI for each image
     */
    StringBuffer sb = ''<<'' ;
    
    
    /**
     * holds string of HTML text per LI for each bullet-point
     */
    StringBuffer bullets = ''<<'' ;


    /**
     * Tool to get system values.  
     */
    PathFinder pf = new PathFinder();

    /**
     * These default names point to a groovy script-structured configuration cache where user values are stored between sessions.  
     */
    String homePath;

   // =========================================================================
   /** 
    * Default Class constructor.
    * defaults to let user pick either a file or a folder
    */
    public WOW()
    {
        homePath = pf.getHomePath();
        cp = homePath;
        buildTemplates();
        def ti = new Date().getTime();
        setup("src/main/war/wow${ti}.html");
    } // end of constructor


   // =========================================================================
   /** 
    * Non-Default Class constructor.
    * defaults to let user provided output filename without a path choice as that goes to src/main/war folder
    */
    public WOW(String addr)
    {
    	def ix = addr.indexOf(File.separator);
    	if (ix > -1) { throw new RuntimeException("WOW constructor can only take simple output filenames not paths.") }
    	
        homePath = pf.getHomePath();
        cp = homePath;

        buildTemplates();
        setup("src/main/war/${addr}");
    } // end of constructor


   // =========================================================================
   /** 
    * This method optionally generates full HTML file in one go if addWrapper is true
    * defaults to do it all in a single run
    */
    public save()
    {
        if (addWrapper) { write(startHTML.toString()+"\n"); }
        write(head.toString()+"\n");

        if (addWrapper) { write(endHead.toString()+"\n"); }
        write(body.toString()+"\n");

        if (addWrapper) { write(endBody.toString()+"\n"); }
    } // end of constructor
   
    
   /** 
    * This method turns off the wrapper flag so that a complete full HTML file is not generated in one go 
    */
    public stopWrapper()
    {
        addWrapper = false;    
    }    
    
    
   /** 
    * This method establishes <LI> entries in one go 
    */
    def buildTemplates()
    {
        dta += make(title:"Lake",href:"Lake.com",imgsrc:"lake.png",alt:"This is a lake",imagenumber:imagenumber);
        dta += make(title:"Landscape",alt:"landscape",href:"Landscape.com",imgsrc:"landscape.png",imagenumber:imagenumber);
        dta += make(title:"Sunset",href:"Sunset.com",imgsrc:"sunset.png",imagenumber:imagenumber);
        dta.each{e-> say "dta="+e; 
            sb << '\n'+e.toString();
            bullets << '\n'+e.toBullet();
        }
        say "StringBuffer bullets="+bullets.toString();
    } // end of constructor



   /** 
    * This method asks user for user to pick partial output path and filename of a complete full HTML file to be written as output 
    */
    def setup()
    {
        // need more work here to clean up and allow Saver module to provide guidance    
        //def ch = new Chooser();
        def ch = new Saver();
        if (ch.chosen)
        {
            say "the full name of the selected output path is "+ch.getName();    
            say "path="+ch.getPath()+"\nartifact name="+ch.getArtifact();    
            say "the full name of the selected file is "+ch.getName();    
            cp = ch.getPath()
            name = ch.getArtifact();
            say "user said path=[${cp}] and name=[${name}]"
        }
        else
        {
            say "no choice was made so output path is "+ch.getName()+" and path="+ch.getPath();
            System.exit(0);
        }
        
        ch.saveAs(name);
        ch.setOpenOrSave(false);
        ch.setTitle("Pick a Folder and Filename to save");
        if (ch.getChoice())
        {
            say "path="+ch.path+"\nartifact name="+ch.artifact.toString();    
            say "the full name of the selected file is "+ch.fullname;    
            cp = ch.path
            name = ch.artifact;
            say "user said path=[${cp}] and name=[${name}]"
        }
        else
        {
            System.exit(0);
        }

        say "File.separator="+File.separator;
        def fn = cp+File.separator+name
        say "looking for fn="+fn;
		this.setup(fn);        
    } // end of setup()


   /** 
    * This method asks user for partial path and filename of a complete full HTML file to be written as output 
    */
    def setup(String name)
    {
        // find what path we are executing in and remove pre-existing file of same name
        fo = new File(name);        

        if (fo.exists()) { fo.delete() }
        fo.write('');
    } // end of setup()



    // return a Dta object bean with data for each template
    def make(def d)
    {
        def dd = new Dta(d);
        ++imagenumber;
        return dd;
    }  // end of make()



   /** 
    * This method appends txt to existing file of a complete full HTML file written as output 
    */
    def write(String txt)
    {
        try{
            fo.append(txt);
        }
        catch(any)
        {
            println "... failed to append to fo file";
            println "    due to :"+any.message;
        } // end of catch
        
    } // end of write()    


    // utility logging
    def say(def txt) { if (audit) log.info txt; }


    /**
     * holds string of HTML text for start of output file
     */
    def startHTML = """<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->
<meta name="viewport" content="width=device-width, initial-scale=1.0">"""

    def endHead = """</head>
<body>"""

    def endBody = """</body>
</html>"""


    /**
     * holds string of HTML text for WOW include files
     * HEAD template for WOW slider is copied into output .html 
     * but you need to get the WOW pieces of .js and .css
     */
    def head = """<!-- Start WOWSlider.com HEAD section -->
<link rel="stylesheet" type="text/css" href="engine1/style.css" />
<script type="text/javascript" src="engine1/jquery.js"></script>
<!-- End WOWSlider.com HEAD section -->"""


    /**
     * holds string of HTML text for WOW included images plus javascript includes
     *
     * feeds LI lines for each image and companion bullet LI
     * This template for WOW slider is copied into output .html 
     */
    def body = """<!-- Start WOWSlider.com BODY section -->
<div id="wowslider-container1">
  <div class="ws_images">
    <ul>${sb}
    </ul>
  </div>
  
  <div class="ws_bullets">
    <div>${bullets}    
    </div>
  </div>
  
  <div class="ws_shadow"></div>
</div>    
<script type="text/javascript" src="engine1/wowslider.js"></script>
<script type="text/javascript" src="engine1/script.js"></script>
<!-- End WOWSlider.com BODY section -->
"""


    // =============================================================================    
    /**
     * The primary method to execute this class. 
     * Can be used to test and examine logic and performance issues. 
     * 
     * argument is a list of strings provided as command-line parameters. 
     * 
     * @param  args a list of possibly zero entries from the command line
     */
    public static void main(String[] args)
    {
/*    
        WOW w = new WOW();
        w.setup('src/main/war/kids.html');
        w.save();
        
        w.setup('src/main/war/partial.html');
        w.write(w.head.toString()+"\n");
        w.write(w.body.toString()+"\n");
        
        w = new WOW('max.html');
        w.save();
*/
        // allow user to choose output path and filename
        WOW x = new WOW();
        x.save();
        
        println "--- the end ---"
        //System.exit(0);
    } // end of main
    
} // end of class