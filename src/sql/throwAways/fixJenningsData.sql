update project set projectName='Composition and function of vegetation alliances in the Interior Northwest, USA', 
                   projectDescription='Improved policy and management to reduce the rate of human-induced loss of biodiversity depends on basic knowledge of distribution, status, and trends in species and their habitats. Vegetation monitoring provides a practical means of tracking many components of biodiversity over space and time. Until recently, we lacked a standardized set of vegetation classes that are useful in predicting species distributions and habitat conditions and that can be repeatedly mapped over large areas using remote sensing. However, an international standardized classification now exists and one particular level of the classification, vegetation "alliances," may prove especially useful for biodiversity monitoring. Alliances are the most general units of vegetation that distinguish plant communities. Since alliances are characterized primarily by overstory species, they can usually be observed with remotely sensed imagery. There has been little previous work on the compositional, structural, or functional properties of alliances as ecological units. This study characterizes and analyzes vegetation alliances across a large geographic region, the Interior Northwestern United States. Almost 40,000 vegetation field plots were collated and screened for quality. About 22% of the plots were retained and classified to 49 alliances of the U.S. National Vegetation Classification. Modeled values of climate and Net Primary Productivity were attributed to each plot, as were morphological traits of each species. The roles of dominant and subdominant species in determining the floristic identity of alliances was measured with a multi-response permutation procedure of (a) an alliance''s entire plot data, and (b) derived plot data where the dominant species were removed. There is significant variation among alliances in the degree of affinity between dominant and subdominant species, suggesting that additional refinements of alliances are needed if they are to be used for biodiversity inventory and monitoring. The form of the relationship between species diversity and biomass productivity was examined within and across alliances with generalized linear models. Results confirm scale dependence in the diversity-productivity relationship. The identities of alliances along the productivity gradient indicate that at regional landscape scales and low to moderate productivity values, moisture may limit species diversity and productivity. Increased canopy complexity may allow tighter packing of species in three dimensions and increase the  environmental heterogeneity within plots.'  
                   where accessionCode='VB.Pj.339.MICHAELJENNINGS';
                   
UPDATE reference SET
  shortname='Jennings 2003. Vegetation alliances: composition and function.',
  title='Vegetation alliances: composition and function',
  referenceType='thesis',
  fulltext='Jennings, M.D. 2003. Vegetation alliances: composition and function. Ph.D. dissertation, University of California, Santa Barbara. 162p.',
  pubDate='2003-01-01',
  totalPages='162',
  publisher='University of California at Santa Barbara',
  publicationPlace='Santa Barbara, California, United States',
  degree='Ph.D.'
  WHERE accessionCode='VB.Rf.331.MIKEJENNINGSREF';

INSERT INTO referenceParty (givenName, surname) SELECT 'M.D.','Jennings' ;
INSERT INTO referenceContributor (reference_ID, referenceParty_id, roleType) 
  SELECT (select referencE_ID from reference where accessionCode='VB.Rf.331.MIKEJENNINGSREF'),
  (select min(referenceParty_ID) from referenceParty where surname='Jennings' and givenName='M.D.'),'Author';
,  