package io.jnorthr.toolkit;
import io.jnorthr.toolkit.configuration.*;
import org.slf4j.*
import groovy.util.logging.Slf4j
@Slf4j
public class ConfigTester{

    public static void main(String[] args)
    {
        println "Hello from ConfigTester.groovy"

        args.eachWithIndex{e,ix-> println "arg${ix}="+e}
        println "... running default constructor"
        Configurator c = new Configurator(); 
        println c.toString();
        c.putInput('file','.dolly.json');
        println c.ck.prettyPrint();
        println "--- the end ---"
    } // end of main 

} // end of class