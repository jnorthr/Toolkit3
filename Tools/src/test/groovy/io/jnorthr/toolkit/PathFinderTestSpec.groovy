package io.jnorthr.toolkit;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import io.jnorthr.tools.configuration.*;
import java.util.logging.Logger;
import spock.lang.*
import java.lang.*;
import io.jnorthr.toolkit.PathFinder;

class PathFinderTestSpec extends Specification 
{
  // fields
  //PathFinder pf;
  
  //@Shared
  //String ourPath  	    

  // Fixture Methods
  
  // run before every feature method
  def setup() 
  { 
  }          

  // run after every feature method
  def cleanup() {}
  
  
  // Note: The setupSpec() and cleanupSpec() methods may not reference instance fields.
  def setupSpec() 
  {
	 PathFinder pf = new PathFinder();
     String ourPath = pf.homePath
  } // run before the first feature method
  
  
  def cleanupSpec() {}   // run after the last feature method}


/*
Feature methods are the heart of a specification. They describe the features (properties, aspects) that you expect to find in the system under specification. By convention, feature methods are named with String literals. Try to choose good names for your feature methods, and feel free to use any characters you like!

Conceptually, a feature method consists of four phases:

 . Set up the feature's fixture
 . Provide a stimulus to the system under specification
 . Describe the response expected from the system
 . Clean up the feature's fixture
 . Whereas the first and last phases are optional, the stimulus and response phases are always present (except in interacting feature methods), and may occur more than once.

*/

  // Feature Methods

  // First Test
  def "1st Test: Ask PathFinder for default path to user.home"() {
    given:
		println "1st Test: Ask PathFinder for default path to user.home"
        String homePath  = System.getProperty("user.home") + File.separator;

    when:
   	    PathFinder pf = new PathFinder();
		def myPath = pf.getHomePath();
	    String ourPath = pf.homePath
		
    then:
		// Asserts are implicit and not need to be stated.
		ourPath == myPath
  } // end of test



  // 2nd Test
  def "2nd Test: Ask PathFinder to return currentWorkingDirectory"() {
    given:
		println "2nd Test: Ask PathFinder to return currentWorkingDirectory"
	    String currentWorkingDirectory  = System.getProperty("user.dir") + File.separator;

    when:
   	    PathFinder pf = new PathFinder();
		def pwd = pf.getPWD()
 
    then:
    	// Asserts are implicit and do not need to be stated.
    	// Change "==" to "!=" and see what's happening!
		pf.currentWorkingDirectory == pwd
  }

  // 3rd Test
  def "3rd Test: Pass PathFinder an illegal constructor"() {
    given:
		println "3rd Test: Pass PathFinder an illegal constructor"
 
    when:
   	    PathFinder pf = new PathFinder(".PathFinderTestSpec3.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	thrown groovy.lang.GroovyRuntimeException
  } // end of test
  
  
  // 4th Test
  def "4th Test: Pass PathFinder an illegal constructor - alt syntax"() {
    given:
		println "4th Test: Pass PathFinder an illegal constructor - alt syntax"
 
    when:
		PathFinder pf = new PathFinder(".PathFinderTestSpec4.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	groovy.lang.GroovyRuntimeException ex = thrown()
    	
        // Alternative syntax: def ex = thrown(InvalidDeviceException)
        ex.message == 'Could not find matching constructor for: io.jnorthr.toolkit.PathFinder(java.lang.String)'
  } // end of test
  

  /* Fifth Test
  def "5th Test: load Response from PathFinder method"() {
    given:
		println "\n5th Test: load Response from PathFinder method"
        PathFinder pf = new PathFinder();
 		def home = pf.getHomePath()
 		println "... 5th Test: homePath=[${home}]"
 		
    when:
		Response re = new Response();  
		re = pf.getResponse(re);
		
    then:
		// Asserts are implicit and not need to be stated.
		re.toString() == """chosen=false
returncode=-1
path=${home}
artifact=myfile.txt 
fullname=${home}myfile.txt
found=false
isDir=false
multipleFilesWereSelected=false
multiFileCount=0
cancelled=false"""
  } // end of test
*/



//==========================================

/*------------
  // Sixth Test
  def "6th Test: PathFinder "() {
    given:
		println "\n6th Test: PathFinder"

    when:
		PathFinder pf = new PathFinder();  
		def txt = pf.toString() 
    then:
		// Asserts are implicit and not need to be stated.
		txt == """homePath=/Users/jimnorthrop/
currentWorkingDirectory=/Users/jimnorthrop/Dropbox/Projects/Toolkit3/Tools/
""".toString()
  } // end of test


  // Test Seven
  def "7th Test: PathFinder "() {
    given:
		println "\n7th Test: PathFinder"

    when:
		PathFinder pf = new PathFinder();  
		//def myPath = pf.getHomePath();
		
    then:
		// Asserts are implicit and not need to be stated.
		pf != null
  } // end of test


  // Test Eight
  def "8th Test: PathFinder "() {
    given:
		println "\n8th Test: PathFinder"

    when:
		PathFinder pf = new PathFinder();  
		def myPath = pf.getHomePath();
		
    then:
		// Asserts are implicit and not need to be stated.
		ourPath == myPath
  } // end of test


  // Test Nine
  def "9th Test: PathFinder "() {
    given:
		println "\n9th Test: PathFinder"

    when:
		PathFinder pf = new PathFinder();  
		def myPath = pf.getHomePath();
		
    then:
		// Asserts are implicit and not need to be stated.
		ourPath == myPath
  } // end of test


  // Test Ten
  def "10th Test: PathFinder "() {
    given:
		println "\n10th Test: PathFinder"

    when:
		PathFinder pf = new PathFinder();  
		def myPath = pf.getHomePath();
		
    then:
		// Asserts are implicit and not need to be stated.
		ourPath == myPath
  } // end of test


  // Test Eleven
  def "11th Test: PathFinder "() {
    given:
		println "\n11th Test: PathFinder"

    when:
		PathFinder pf = new PathFinder();  
		def myPath = pf.getHomePath();
		
    then:
		// Asserts are implicit and not need to be stated.
		ourPath == myPath
  } // end of test


----------------------------
*/

} // end of spec
