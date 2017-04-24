package io.jnorthr.toolkit;
import io.jnorthr.toolkit.PathFinder;
 
/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import io.jnorthr.tools.configuration.*;
import java.util.logging.Logger;
import spock.lang.*
import java.lang.*;
import io.jnorthr.toolkit.PathFinder;

class ConfigureTestSpec extends Specification 
{
  // fields
  Configure ck;
  
  @Shared
  String homePath  	    

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
     homePath = pf.getHomePath();
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
  def "1st Test: Setup Configure for default path"() {
    given:
		println "1st Test: Setup Configure for default path"
	  	new File(homePath+".configure.config").delete()
 
    when:
		ck = new Configure();  
    then:
		// Asserts are implicit and not need to be stated.
    	ck.config.getClass() == groovy.util.ConfigSlurper;

    	true == new File(homePath+".configure.config").exists()
    	
    	// Change "==" to "!=" and see what's happening!
    	//ck.configPath == homePath
    	//ck.configFileName == ".configure.config"
    	ck.configFullName == homePath+".configure.config";
  } // end of test


/*
  // 2nd Test
  def "2nd Test: Set Configure initial config filename to .configureTestSpec2.config"() {
    given:
		println "2nd Test: Set Configure initial config filename to .configureTestSpec2"
	  	new File(homePath+".configureTestSpec2.config").delete()
 
    when:
		ck = new Configure(".configureTestSpec2.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	// Change "==" to "!=" and see what's happening!
    	true == new File(homePath+".configureTestSpec2.config").exists()
    	ck.configPath == homePath
    	ck.configFileName == ".configureTestSpec2.config"
    	ck.configFullName == homePath+".configureTestSpec2.config";
  } // end of test


  // 3rd Test
  def "3rd Test: Use config filename .configureTestSpec3 to save default empty config"() {
    given:
		println "3rd Test: Use config filename .configureTestSpec3 to save default empty config"
	  	new File(homePath+".configureTestSpec3.config").delete()
 
    when:
		ck = new Configure(".configureTestSpec3.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	true == new File(homePath+".configureTestSpec3.config").exists()
    	
    	// Change "==" to "!=" and see what's happening!
    	ck.configFullName == homePath+".configureTestSpec3.config";
		true == ck.save(ck.configFileName, "setup { }");
		ck.payload == "setup { }"
		ck.configFullName.endsWith(".configureTestSpec3.config") == true;
  } // end of test


  // 4th Test
  def "4th Test: Use config filename .configureTestSpec4 to save default empty config"() {
    given:
		println "4th Test: Use config filename .configureTestSpec4 to save default empty config"
	  	new File(homePath+".configureTestSpec4.config").delete()
        def pl2 = """setup {
    output {
        path='/Users/fred/'
        file='.marky.txt'
    }
    input {
        path='/Users/fred/'
        file='.danny.config'
    }
}
""".toString();

    when:
		ck = new Configure(".configureTestSpec4.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	true == new File(homePath+".configureTestSpec4.config").exists()
    	
    	// Change "==" to "!=" and see what's happening!
    	ck.configFullName == homePath+".configureTestSpec4.config";
		true == ck.save(ck.configFileName, "setup { }");
    	
		true == ck.save(pl2);
		ck.payload == pl2;
		new File(ck.configFullName).text == pl2;
  } // end of test



  // 5th Test
  def "5th Test: Use config filename .configureTestSpec5 to save default non-empty config"() {
    given:
		println "5th Test: Use config filename .configureTestSpec5 to save default nonempty config"
	  	new File(homePath+".configureTestSpec5.config").delete()

    when:
		ck = new Configure(".configureTestSpec5.config");
 
    then:
    	// Asserts are implicit and not need to be stated.
    	true == new File(homePath+".configureTestSpec5.config").exists()
    	
    	// Change "==" to "!=" and see what's happening!
    	ck.configFullName == homePath+".configureTestSpec5.config";
		true == ck.save(ck.configFileName, "setup { }");
    	true == ck.put('id','jnorthr');
		true == ck.save();
		ck.payload == "setup.id='jnorthr'\n";
		new File(ck.configFullName).text == "setup.id='jnorthr'\n";
  } // end of test

*/

} // end of spec
