/**
 * this is the c program that starts the installer on the client machine
 *
 */
#include <stdio.h>
#include <io.h>
#include <stdlib.h>
#include <unistd.h>

main (int argc, char *argv[])
{
	int ii;
	static char *args[] = 
	{
		"java",
		"-classpath",
		"$CLASSPATH;Installer.jar",
		"vegclient.installer.InstallerInterface",
		NULL
	};
	printf("Starting The ESA Plots Database Installer\n");

//		if ( fork() == 0 )
//		{
			ii=execvp("java", args);
			printf("%s\n", ii);
//		}
//		else
//		{
//			printf("%s\n", ii);
//			printf("Started", getpid() );
//		}
	sleep(1);
//		printf ("%s\n", ii);
		//printf ("%s\n", fork() );	
}



	
