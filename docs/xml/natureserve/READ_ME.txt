http://gyro.nceas.ucsb.edu/~lee/xml/ns/ 
  contains:

NS_VB_common.xsd: schema that validates data files to pass back & forth
vb_ns_exampleData.xml: an example file, technically OK, not ecologically
db_model_ns_vb_common.xml: doc that defines our common model: used to create NS_VB_common.xsd-- can be used to generate java beans and data dictionary.

db_model_vegbank_schema.xsd: validates the above xml file
entity_schema.xsd: imported by above

dbmodel-to-schema2.xsl: used to create xsd from the xml documentation.

sampleVegBankXml_20040728.zip contains these files (also extracted here):
 sampleVegBank.xml : Maggie Woo's output from NatureServe (Explorer?) to be styled into the common model docs
 sampleVegBank.xsl : does the styling of the above document to arrive at common model xml doc.
 sampleVegBankOutput.xml : the common model result from the above xsl transformation.