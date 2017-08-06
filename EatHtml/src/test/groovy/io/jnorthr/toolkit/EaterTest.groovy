package io.jnorthr.toolkit.eater;


// groovy code to choose one folder to walk thru the files found within it
// **************************************************************
import java.io.File;
import java.io.IOException;
import java.io.*
import spock.lang.*

import org.slf4j.*
import groovy.util.logging.Slf4j

//import io.jnorthr.toolkit.PathFinder;

/**
* The eaterTest program implements a support test application that allow uses the Spock
* framework to do is biz
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2017-05-22
*/
//@Slf4j
public class EaterTest extends Specification 
{       
  // fields
  
  //@Shared

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
  } // run before the first feature method
  
  
  def cleanupSpec() 
  {
  }   // run after the last feature method}

    
  // Feature Methods

  // First Test
  def "1st Test: Test Eater default constructor"() {
    given:
		println "\n1st Test: Test Eater default constructor"
 
    when:
		Eater ck = new Eater();  
		println ck;
		
    then:
		// Asserts are implicit and not need to be stated.
    	ck.getClass() == io.jnorthr.toolkit.eater.Eater;    	    	
  } // end of test


} // end of class