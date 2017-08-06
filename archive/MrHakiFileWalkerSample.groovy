// http://mrhaki.blogspot.fr/2010/04/groovy-goodness-working-on-files-or.html
// http://mrhaki.blogspot.fr/2010/04/groovy-goodness-traversing-directory.html
// bootstrap 4: https://plus.google.com/u/0/collection/0w8EZ?cfem=1
// and http://vegibit.com/what-is-new-in-bootstrap-4/
import static groovy.io.FileType.*
import static groovy.io.FileVisitResult.*
import groovy.io.FileType
String name = '/Users/jimnorthrop/Dropbox/Projects/Toolkit3/WOW'; 
def groovySrcDir = new File(name) // System.env['GROOVY_HOME'], 

println "Test 1 -------------------"  
def countFilesAndDirs = 0
groovySrcDir.traverse { countFilesAndDirs++ }

println "Total files and directories in ${groovySrcDir.name}: $countFilesAndDirs\n-----------------------------------\n"
 
// -------------------------- 
println "\nTest 2 -------------------" 
def totalFileSize = 0
def groovyFileCount = 0
def sumFileSize = {
    totalFileSize += it.size()
    groovyFileCount++
    println "... sumFileSize found "+it;
}
def filterGroovyFiles = ~/.*\.groovy$/
groovySrcDir.traverse type: FILES, visit: sumFileSize, nameFilter: filterGroovyFiles

println "\nTotal file size for $groovyFileCount Groovy source files is: $totalFileSize\n--------------------------------\n"

// -------------------------- 
// Test 3 ------------------- 
println "\nTest 3 -------------------" 
def countSmallFiles = 0
def postDirVisitor = {
    if (countSmallFiles > 0) {
        println "Found $countSmallFiles files with small filenames in ${it.name}"
    }
    countSmallFiles = 0
} // end of post

// "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)" // ~/.*\.groovy$/
groovySrcDir.traverse(type: FILES, postDir: postDirVisitor, nameFilter: ~/.*\.groovy$/) {
    println "... Test 3 found file:"+it;
    if (it.name.size() < 15) {
     countSmallFiles++
    }
}
// -------------------------------------------
// First create sample dirs and files.
println "\nTest 4 -------------------" 
(1..3).each {
 new File("dir$it").delete()    //mkdir()
}

(1..3).each {
 def file = new File("file$it")
 file << "Sample content for ${file.absolutePath}"
}
 
def currentDir = new File('.')
def dirs = []
currentDir.eachFile FileType.DIRECTORIES, {
    dirs << it.name
}
//assert 'dir1,dir2,dir3' == dirs.join(',')
 
def files = []
currentDir.eachFile(FileType.FILES) {
    files << it.name
}
//assert 'file1,file2,file3' == files.join(',')
 
def found = []
currentDir.eachFileMatch(FileType.ANY, ~/.*jpg/) {
   found << it.name
}
 
//assert 'dir2,file2' == found.join(',')

// ----------------------------------------
// -------------------------- 
// Test 5 ------------------- 
println "\nTest 5 -------------------" 
//import static groovy.io.FileTypes.FILES
new File(name).eachFileRecurse(FILES) {
    if (it.name.endsWith('.groovy')) { println it; }
} // end of test 5


// -------------------------- 
// Test 6 ------------------- 
println "\nTest 6 -------------------" 
// groovy 2.4.7
println "\n-----------------------------------------"
countSmallFiles = 0
// optional filter (type:FileType.FILES,nameFilter: ~/.*.txt/)
new File(name).traverse(type:FileType.FILES,nameFilter: ~/.*.groovy/) { it->
    println it;
    countSmallFiles++;
} // end of test 6
println "---------------------------\nFound $countSmallFiles files in ${name}\n----------------------------------"

// -------------------------- 
// Test 7 ------------------- 
println "\nTest 7 -------------------" 
def result=""
logic={
    it.eachDir(logic);
    it.eachFileMatch(~/.*groovy/){f->
        result += "${f.absolutePath}\n"
    } // end of each
} // end of logic
logic(new File(name));
println result;
println "---------------------------"

println "\n------ the end ---"