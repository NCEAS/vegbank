READ_ME.txt

This folder contains HTML files that reference a "media" folder.

These demos created with Camtasia Studio.  Nifty Program.

However, in the current version, 2.1.0, the media files are automatically stored in another folder, filename_media.
  To change this, I search for all files in Windows Explorer in the parent folder of these filename_media folders,
  then cut and paste them into the media folder.
  
  Then I search for 
    <movieURL>[a-z0-9_]*[/\\]
    replace with
    <movieURL>media/
    in all xml files in the media folder
    
  Next, I search and replace:
    vegbranch_[a-z_0-9]*_media/
    with 
    media/
    in the HTML files.  Then I edit the headers and other things in the HTML files to link between things.  This may
    be handled subsequently in another manner.
