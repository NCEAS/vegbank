This file talks about noncvs files in VegBank.

Many files, especially large binary files, are not stored in CVS, but are instead in the noncvs directory.

This is generally in /usr/www/vegbank/htdocs/noncvs, but is configured in build.properties

All files in this dir (and subdirs) should be listed in the file: vegbank/noncvs/noncvsfile.xml

That file IS in CVS so that one could see updates and revisions to files and added files.