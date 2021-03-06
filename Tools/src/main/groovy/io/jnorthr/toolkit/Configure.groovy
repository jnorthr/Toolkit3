/*
@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.7.21'),
    @Grab(group='ch.qos.logback', module='logback-classic', version='1.2.3')
])
*/
// need imports to parse JSON samples bottom of this script
package io.jnorthr.toolkit;
import io.jnorthr.toolkit.PathFinder;

import org.slf4j.*
import groovy.util.logging.Slf4j

/*
 * Feature to confirm a groovy configuration script file exists or build a simple one if it does not
 */
@Slf4j
public class Configure{

    /**
     * The name of the configuration file we load/save
     */
	String configFullName="";
	
    /**
     * The handle on a groovy ConfigSlurper
     */
    ConfigSlurper config = new ConfigSlurper();
    
    
    /**
     * Map-like structure after using handle on a groovy script consumer to parse a script payload
     */
    groovy.util.ConfigObject dataObject;


    /**
     * This is logic to get the name of the home folder used by this user.  
     */
    PathFinder pf;
    


    /**
     * This is logic to make default groovy script config payload.  
     */
    String payload = """setup {
  output {
    path = ''
    file = ''
  }
  input {
    path = ''
    file = ''
  }
}""".toString();


	// --------------------------------------------------------------------------------------------------
    /*
     * Default constructor builds/or confirms existence of the default groovy configuration script file
     */
    public Configure()
    {
        say "Default constructor Configure.groovy" 
        pf = new PathFinder();        
        config = new ConfigSlurper();  

        try{
            dataObject = config.parse(payload);
        }
        catch (Exception e)
        {
            say "... parse() exception due to malformed groovy script payload:"+e.message
            throw new RuntimeException(e.message) 
        } // end of catch

		configFullName = getFullName();
		say "... configFullName=[${configFullName}]"

        load()    
    } // end of default constructor
    

    /*
     * Method to produce a text stream of the current payload in a man-readable format 
     */
    String prettyPrint()
    {
        return dataObject.prettyPrint().trim()+'\n';
    } // end of method

        
    /*
     * Convenience method to log and print messages to console 
     */
    def say(txt) { println txt; }
        

    /* ----------------------------------------------------
     * Load and save configuration file methods follow ----
     * ----------------------------------------------------
     */
                
    /*
     * Method to gather full name of most recent configuration file saved 
     */
    public String getFullName()
    {       
		String name = ".configure.config"
        
        if ( pf.hasFile(name) )
        {
			say "... hasFile(${name}) ? =" + pf.hasFile(name);         
        } // end of if
        else
        {
			pf.setFile(name, payload);                 
        } // end of else
        
    	return pf.homePath + name;
    } // end of method

        
     
    /*
     * Method to fill config text area from most recently known external file, if it exists, else default to skeleton  
     */
	public load()
    {
        load(configFullName);
    } // end of read    
          
          
    /*
     * Method to  fill config text area from external file, if it exists, else default to skeleton
     */
    public load(String fn)
    {    
        def fi = new File(fn);
         
        say "... read "+fn;
        if ( fi.exists() )
        {
            say "... config.file "+fn+" exists - getting it's text";
            payload = fi.getText("UTF-8");
            try{
                dataObject = config.parse(payload);
                //say prettyPrint()
            }
            catch (Exception e)
            {
                say "... parse() exception due to malformed groovy script payload:"+e.message
                throw new RuntimeException(e.message) 
            } // end of catch
            
            //say "------------------\npayload set to :"
            //say payload
            //say "------------------"
        }
        else
        {
            say "... groovy config script "+fn+" does not exists, will build it."
            save(fn,payload);
        }
    } // end of read    



    /*
     * Save a string of data to a writer as external UTF-8 file for already loaded file;
     * use path+filename of most recently loaded config file
     */
    public boolean save()
    {
        say "... default saving only to "+configFullName;
        save(configFullName,payload);
    } // end of method
        

    /*
     * Save a config-like string of data to a writer as external UTF-8 file for already loaded file
     * using path+filename of most recently loaded config file
     */
    public boolean save(String data)
    {
        say "... saving script data only to "+configFullName;
        save(configFullName,data);
    } // end of method
        

    /*
     * Save a string of data to a writer as external UTF-8 file with provided path+filename 'fn'
     */
    public boolean save(String fn, String data)
    {
        say "... saving as UTF-8 to "+fn;

        // With a writer object:
        new File(fn).withWriter('UTF-8') { writer ->
                writer.write(data)
        } // end of write    
        
        payload = data;
        try{
            dataObject = config.parse(payload);
        }
        catch (Exception e)
        {
            say "... parse() exception due to malformed groovy script payload:"+e.message
            throw new RuntimeException(e.message) 
        }
        
        say "------------------\npayload set to :"
        say payload
        say "------------------"

        say "... saved ${data.size()} bytes to "+fn+'\n-----------------------\n';
        
        //computePath(fn);
        
        return true;
    } // end of method
    
    
    
    /* ----------------------------------------------------------
     * Get and put key/values into current configuration object -
     * ----------------------------------------------------------
     */
        
    /*
     * Method to take key and see if it's in our groovy ConfigSlurper ConfigObject.setup.input sub-map  
     */    
    public boolean hasInput(def k)
    {
        dataObject.with{
            setup.input.containsKey(k)
        }
    } // end of method
     
     
    /*
     * Method to  use 'path' key and get it's value from our ConfigObject.setup.input map
     */    
    public getInputPath()
    {
        return getInput('path');
    } // end of method


    /*
     * Method to use 'file' key and get it's value from our ConfigObject.setup.input map     
     */
    public getInputFile()
    {
        return getInput('file');
    } // end of method


    /*
     * Method to use two keys to form a resulting whole filename 
     */    
    public getInputFileName()
    {
        return getInputPath()+getInputFile();
    } // end of method

     
    /*
     * Method to take key and get it's value from our ConfigObject.setup.input map     
     */
    public getInput(String ky)
    {
        dataObject.with {
            setup.input.get(ky);
        }
    } // end of method
        
        
    /*
     * Method to take key and see if it's in our groovy ConfigSlurper ConfigObject.setup.output sub-map 
     */    
    public boolean hasOutput(def k)
    {
        dataObject.with{
            setup.output.containsKey(k)
        }
    } // end of method
     
     
    /*
     * Method to use 'path' key and get it's value from our ConfigObject.setup.output map  
     */    
    public getOutputPath()
    {
        return getOutput('path');
    } // end of method


    /*
     * Method to use 'file' key and get it's value from our ConfigObject.setup.output map  
     */
    public getOutputFile()
    {
        return getOutput('file');
    } // end of method


    /*
     * Method to use two keys to form a resulting whole filename  
     */    
    public getOutputFileName()
    {
        return getOutputPath()+getOutputFile();
    } // end of method

     
    /*
     * Method to take key and get it's value from our ConfigObject.setup.output map
     */
    public getOutput(String ky)
    {
        dataObject.with {
            setup.output.get(ky);
        }
    } // end of method

     
    /*
     * Method to take key and get it's value from our ConfigObject.setup map ROOT 
     */    
    public get(String ky)
    {
        dataObject.with {
            setup.get(ky);
        }
    } // end of method


    /*
     * Method to insert new properties at the ROOT of our ConfigSlurper object tree 
     * take key and insert this va into our ConfigObject.setup{} map root - NOT into the 'input' sub-groovy script closure 
     */
    public boolean put(String ky, String va)
    {
        dataObject.with {
            setup.put(ky,va);
        }
        payload = prettyPrint();
        save();
    } // end of method


    /*
     * Method to inserts new objects, i.e. dates,numbers,etc at the ROOT of our ConfigSlurper object tree 
	 * takes key to insert this va into our ConfigObject.setup{} map root - 
	 * NOT into the 'input' sub-groovy script closure 
     */
    public boolean put(String ky, Object va)
    {
        dataObject.with {
            setup.put(ky,va);
        }
        payload = prettyPrint();
        save();
    } // end of method
     

    /*
     * Method to take key and insert this va into our ConfigObject.setup.input map sub-root 
     */    
    public boolean putInput(String ky, String va)
    {
        say "... putInput("+ky+","+va+")"
        dataObject.with {
            setup.input.put(ky,va);
        }
        
        payload = prettyPrint();
        say "... putInput prettyPrint:\n"+payload
        save();
    } // end of method


    /*
     * Method to take key and insert this va into our ConfigObject.setup.output map sub-root  
     */
    public boolean putOutput(String ky, String va)
    {
        say "... putOutput("+ky+","+va+")"
        dataObject.with {
            setup.output.put(ky,va);
        }
        
        payload = prettyPrint();
        say "... putOutput prettyPrint:\n"+payload
        save();
    } // end of method

	// =============================================================================        


    // =============================================================================    
    /**
      * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
      * 
      * argument is a list of strings provided as command-line parameters. 
      * 
      * @param  args a list of possibly zero entries from the command line; first arg[0] if present, is
      *         taken as a simple file name of a groovy script-structured configuration file;
      */
    public static void main(String[] args)
    {
        println "Hello from Configure.groovy"
        Configure ck;

/*
        // normal test flow of groovy script.config filename with a single argument from the build.gradle script
        if (args.size() > 0) 
        { 
            println "\n... checking .config = "+args[0]+"\n"
            String cfn = args[0];
            ck = new Configure(cfn);
            println "... file "+ck.configFullName+" contains "+ck.payload.size()+" bytes";
            ck.save(ck.configFullName, "setup { }");
        } 
        else 
        { 
             println "... default constructor .configure.config"
             ck = new Configure(); 

             println "... file "+ck.configFullName+" contains "+ck.payload.size()+" bytes";
             
             ck.putInput('file','.default.config');
             println "path:"+ ck.getInput('path')

             ck.putOutput('file','.default.config');
             println "path:"+ ck.getOutput('path')
             
             // insert key into root of ConfigObject tree
             ck.put("mobile","078557654321"); 
             ck.save();
             
             ck.prettyPrint();
        }

        // sample test of alternate .config filename
        //String cn = ".configureAlternate.config";
*/
        
        ck = new Configure();
        println "... file "+ck.configFullName+" contains "+ck.payload.size()+" bytes";
        ck.putInput('fullname',ck.configFullName);
        ck.putInput('file','.input.file');
    	ck.putOutput('file','.output.file');
             
        // insert key into root of ConfigObject tree
        ck.put("mobile","078557654321"); 

        println ck.prettyPrint();
        ck.save();
        
/*        
        ck.putInput('path',ck.configPath);
        ck.putInput('file',ck.configFileName);
        ck.putOutput('path',ck.configPath);
        ck.putOutput('file',".fred.txt");  //ck.configFileName);
        
        println "... has setup.input.path ? " + ck.hasInput('path');
        println ck.payload;
        
        println "... getInputPath()    : " + ck.getInputPath()
        println "... getInputFile()    : " + ck.getInputFile()
        println "... getInputFileName(): " + ck.getInputFileName()
        println "... getOutputPath()    : " + ck.getOutputPath()
        println "... getOutputFile()    : " + ck.getOutputFile()
        println "... getOutputFileName(): " + ck.getOutputFileName()
        
        println "\n-----------------------\nprettyPrint:\n"+ck.prettyPrint();
        println "\n-----------------------\n"
        ck.save();
        println "\n-----------------------"
*/
        /*
         * sample to store a test version of a config in .fred.txt file
         */
        def pl2 = """setup {
    output {
        path='/Users/jimnorthrop/'
        file='.mark.txt'
    }
    input {
        path='/Users/jimnorthrop/'
        file='.dan.config'
    }
}
""".toString();
/*
        ck.save(ck.configPath+".fred.txt", pl2 );
        println "\n-----------------------"
        ck.load();   //ck.configPath+".fred.txt" );
        
        println "\n-----------------------"
        ck.put('id','jnorthr');
        ck.save();        
        // ['.max.txt', '/Users/jimnorthrop/.stacy.txt', '/Users/jimnorthropX/.jnorthr.txt','/Users/jimnorthrop//.stax.txt',''].each{e->  ck.computePath(e);}
*/
        
        println "\n-----------------------"
        ck.say "--- the end ---"        
    } // end of main 

} // end of class

     /*             	String configFile = pf.homePath + name;
		        if (new File(configFile).exists()) 
				{
					say "... ${configFile}.exists()"
					payload = new File(configFile).text;
				} // end of if
	*/
	
