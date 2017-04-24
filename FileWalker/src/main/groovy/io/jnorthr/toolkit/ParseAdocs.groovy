package io.jnorthr.toolkit;

import java.io.*
files = []
fn = '/Users/jim/Dropbox/Projects/Toolkit3';
/*
new File(fn).eachFileMatch(~/^.*\.adoc$/) { files << it.name };

count = 0
files.each{
    count++;
    println it;
}

println "\n===================================\nFound $count files\n"
*/

def result
folders=0;
count = 0;
map=[:]

findTxtFileClos = {

        it.eachDir(findTxtFileClos);
        it.eachFile() {file ->            //Match(~/.*.adoc/) {file ->
                yn = (file.name.startsWith('.')) ? true :  false;
                if (!yn && !file.absolutePath.contains('/.git'))
                {
                    count++;
                    //println file.absolutePath;
                    int i = file.name.lastIndexOf('.');                                                                
                    if (i>-1) 
                    {
                        ss = file.name.substring(i+1).toLowerCase()
                        println "i=$i "+ss+" ="+file.name;
                        if(map.containsKey(ss)) {
                          map[ss]+=1;
                        }
                        else
                        {
                            map[ss]=1;
                        }
                    }
                    //result += "${file.absolutePath}\n"
                }
        }
    }

// Apply closure
findTxtFileClos(new File(fn))
println "\n===================================\nFound $count files\n"
map.sort().each{ k, v -> println "${k}:${v}" }


println "---  the end ---"
//assert ['groovy1.txt', 'groovy2.txt', groovy3.txt'] == files