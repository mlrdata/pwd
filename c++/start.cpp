#include <iostream>
#include <cstdlib>
#include <string>
 
int main() {


    
    std::string script2 = "start /b ./jdk1.8.0_311/bin/javaw -jar  ./password-0.0.1-SNAPSHOT.jar --server.port=35589" ;
    system(script2.c_str());
 

    std::string script3 = "start /b ./pwd-win32-x64/pwd.exe";
    system(script3.c_str());
 
    return 0;
}
