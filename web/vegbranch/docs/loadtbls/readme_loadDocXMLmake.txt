How to create loading table documentation:
First, you need to create the loadmodule_doc.xml file this way:
1) edit Z_TableDescription and Z_FieldDescription in Access to get it right
2) import the query "temp_fieldsByUseType_newfield" into XML via XMLSpy 
2.4) following transformation ,accomplished by loaddoc_xmlManip macro for textpad
   row>             field>
   \n (null)
   replace <field> with \n&
   <field>.*<newFld>true</newFld>        @KEEP-start@&   
   @Keep-start@     @keep-end@&
   <Import> *\n@Keep-end@    <Import>\n
   </Import>   @Keep-end@&
   ^<field>.*<newFld>false</newFld>    <field>
   @Keep-start@<field>      @Keep-start@<table>
   <newFld>true</newFld>   <field>
   @Keep-end@     </table>
   @Keep-start@  (null)
2.9) replace Import> with LoadModDoc>
2.95) replace &#13; with ' ' and &#10; with ' '
3) SAVE as above titled XML file.
4) run ant antfile=loadbuild.xml dist-loaddoc (doesn't really distribute the loading docs, but does build many html pages for that, which is dist'd by main vegbranch build.xml file)