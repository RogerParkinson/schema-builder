If you are using Eclipse then please run the test_copy target in build.xml using ant
This will populate your ivy cache correctly and compiles from Eclipse will work then.

The build file contains the following targets:

test_copy:
This builds the project and leaves the results in temp/results

local-repo:
Builds the project including documentation and javadocs and copies it all
to a local ivy repo (specify it in repo.dir). You only need this if you
are making changes to the project.

distribute_copy:
Used to build and publish to google code.

Each the resulting jar file(s) have a file named ${ant.project.name}.properties
This has the revision.number (svn revision) as well as the
build date and build version.

The build.version is defined in the build.properties file.
In there you'll find various other compile properties etc.

Files and directories:

ant-targets.xml: 
This is an ant build file that is called from the main build. It contains standard
build targets so that the main build.xml file can be kept short.

build.properties:
Defines the properties used by the build. There are some externally defined properties
as well though such as the SVN password.

build.xml:
The main build file. You only need to invoke these targets, not the ones in ant-targets.xml
You probably only want the default target.

ivy-template.xml:
Only used when packaging the build results for publishing to ivyroundup

ivysettings.xml
This defines the ivy settings used to resolve the libraries.

LICENCE-2.0.html:
Text of Apache License, Version 2.0 which is packaged with the project when it is built.

packager-template.xml:
Only used when packaging the build results for publishing to ivyroundup

ivy/:
Holds the ivy jar file

docs/:
Holds the documentation files.

temp/:
Scratch directory that us used during the build. Subdirectories that may be of interest are:
package/: this is where the files packaged for distribution to ivyroundup are placed
pdf/: generated documentation
results/: jar files, source zip and javadocs zip

src/:
The java etc source files

test/:
Unit test files
