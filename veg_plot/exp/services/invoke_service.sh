#!/bin/sh
. setclasspath
java -cp ${CP} electric.glue.tools.Invoke http://localhost:8004/vegbank/exchange.wsdl  getPlotAccessionNumber %latifolia% 
