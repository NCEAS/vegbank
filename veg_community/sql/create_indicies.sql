--   Authors: @author@
--   Release: @release@
--	
-- '$Author: harris $'
-- '$Date: 2001-10-20 00:55:59 $'
--	'$Revision: 1.1 $'

create index commPartyUsage_abiCode_idx on commPartyUsage  (abiCode);
create index abiCode_idx on commPartyUsage  (abiCode);
create index commConceptabiCode_idx on commConcept  (abiCode);

