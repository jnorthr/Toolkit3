package io.jnorthr.toolkit;
 
/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import java.util.logging.Logger;
import spock.lang.*
import java.lang.*;

class ResponseTestSpec extends Specification 
{
  // fields
  
  //@Shared
  //String homePath  	    

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
  } // run after the last feature method}


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
  def "\n1st Test: Setup Response for default consructor"() {
    given:
		println "1st Test: Setup Response for default constructor"
 
    when:
		Response re = new Response();  

    then:
		// Asserts are implicit and not need to be stated.
    	re.chosen == false
		re.returncode == -1
		re.path == ""
		re.artifact == ""
		re.fullname == ""
		re.isDir == false
		re.found == false
		re.abort == false
		re.multipleFilesWereSelected == false
		re.multiFileCount == 0
		re.multiFileSelect == []
		re.hasMany() == false;		
  } // end of test

  // Second Test
  def "2nd Test: SetMany() method Response"() {
    given:
		println "\n2nd Test: SetMany()up Response for default constructor"
 
    when:
		Response re = new Response();  
		File[] selected = []
    then:
		// Asserts are implicit and not need to be stated.
		re.setMany(selected) == false
		re.multipleFilesWereSelected == false
		re.multiFileCount == 0
		re.multiFileSelect == []
		re.hasMany() == false;		
  } // end of test

  // third Test
  def "3rd Test: SetMany() method with one entry"() {
    given:
		println "\n3rd Test: SetMany() method with one entry"
 
    when:
		Response re = new Response();  
		File[] selected = [new File('.fred.txt')]
		
    then:
		// Asserts are implicit and not need to be stated.
		re.setMany(selected) == true
		re.multipleFilesWereSelected == true
		re.multiFileCount == 1
		re.multiFileSelect != []
		re.hasMany() == true;		
		re.multiFileSelect == [new File('.fred.txt')]
  } // end of test

  // fourth Test
  def "4th Test: SetMany() method with two entries"() {
    given:
		println "\n4th Test: SetMany() method with two entries"
 
    when:
		Response re = new Response();  
		File[] selected = [new File('.fred.txt'),new File('.mary.txt')]
		
    then:
		// Asserts are implicit and not need to be stated.
		re.setMany(selected) == true
		re.multipleFilesWereSelected == true
		re.multiFileCount == 2
		re.multiFileSelect != []
		re.hasMany() == true;		
		re.multiFileSelect == [new File('.fred.txt'),new File('.mary.txt')]
  } // end of test


  // fifth Test
  def "5th Test: Parse() method with a closure"() {
    given:
		println "\n5th Test: Parse() method with a closure"
 
    when:
		Response re = new Response();  
		re.path = "xxx"
    then:
		// Asserts are implicit and not need to be stated.
		re.parse(){println "hi kids";} == true
		re.parse(){println "path="+re.path;} == true
		re.parse(re){it->println it.toString();} == true
  } // end of test


  // sixth Test
  def "6th Test: ParseEach() method with a closure"() {
    given:
		println "\n6th Test: ParseEach() method with a closure"
 
    when:
		Response re = new Response();  
		File[] selected = [new File('.fred.txt'),new File('.mary.txt')]
		
    then:
		// Asserts are implicit and not need to be stated.
		re.setMany(selected) == true
		re.parseEach(){ println(it); } == true
  } // end of test

  // seventh Test
  def "7th Test: Parse() method with a closure print object.toString()"() {
    given:
		println "\n7th Test: Parse() method with a closure print object.toString()"
 
    when:
		Response re = new Response();  
		println re.toString();
		
    then:
		// Asserts are implicit and not need to be stated.
		re.toString() == """chosen=false
returncode=-1
path=
artifact= 
fullname=
found=false
isDir=false
multipleFilesWereSelected=false
multiFileCount=0
cancelled=false""" 
  } // end of test


  // eighth Test
  def "8th Test: reveal() method"() {
    given:
		println "\n8th Test: reveal()"
 
    when:
		Response re = new Response();  
		
    then:
		// Asserts are implicit and not need to be stated.
		re.reveal() == null
  } // end of test


  /* Ninth Test
  def "9th Test: load Response from PathFinder method"() {
    given:
		println "\n9th Test: load Response from PathFinder method"
        PathFinder pf = new PathFinder();
 		def home = pf.getHomePath()
 		println "... 9th Test: homePath=[${home}]"
 		
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



} // end of spec
