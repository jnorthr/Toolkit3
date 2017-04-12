package io.jnorthr.wow;
import io.jnorthr.wow.*;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import java.util.logging.Logger;
import spock.lang.*
import javax.swing.JFileChooser;
import spock.lang.Shared
//import CacheEntry;
//import groovy.transform.Canonical
//import groovy.transform.ToString

class WOWTestSpec extends Specification 
{
  // fields
  //static Logger log = Logger.getLogger(CacheManagerTestSpock.class.getName());
  @Shared
  WOW ch;
  
  // fixture methods
  // Note: The setupSpec() and cleanupSpec() methods may not reference instance fields.
  def setup() 
  {
      //ch = new Chooser();  
  } // run before every feature method
  
  def cleanup() {}        // run after every feature method
  def setupSpec() {}     // run before the first feature method
  def cleanupSpec() {}   // run after the last feature method}

  // feature methods

  def "pushing an element on the stack"() {
  	// blocks go here
  }  

/*
Feature methods are the heart of a specification. They describe the features (properties, aspects) that you expect to find in the system under specification. By convention, feature methods are named with String literals. Try to choose good names for your feature methods, and feel free to use any characters you like!

Conceptually, a feature method consists of four phases:

Set up the feature's fixture
Provide a stimulus to the system under specification
Describe the response expected from the system
Clean up the feature's fixture
Whereas the first and last phases are optional, the stimulus and response phases are always present (except in interacting feature methods), and may occur more than once.

  // First Test
  def "Setup JFileChooser to save a file"() { 
    setup: 
  		Chooser ch= new Chooser();
    when:
		ch.setOpenOrSave(false);
 
    then:
	    // Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
	    ch.fc != null;
    	ch.openOrSave == false
    	ch.mode == JFileChooser.FILES_AND_DIRECTORIES;
    	ch.fc.getFileSelectionMode()==ch.mode
  } // end of spec

  // 2nd Test
  def "Set JFileChooser initial folder to known value"() {
    when:
  		Chooser ch= new Chooser();
		ch.setPath("/Fred");
 
    then:
	    // Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
	    ch.fc != null;
    	def e = thrown(RuntimeException)
		e.cause == null
    	ch.fc.getCurrentDirectory()!="/Fred";
  } // end of spec


  // 3rd Test
  def "Ask JFileChooser to allow both files and folder selection by user"() {
    when:
  		Chooser ch= new Chooser();
		ch.selectBoth();
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	ch.fc.getFileSelectionMode()==JFileChooser.FILES_AND_DIRECTORIES;
    	ch.fileSelect == true;
    	ch.pathSelect == true;

  } // end of spec
*/


}  // end of class
