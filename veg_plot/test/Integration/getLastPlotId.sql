SELECT max(observation_id) FROM observation, plot 
  WHERE observation_id = ( SELECT max(observation_id) FROM observation) 
   and observation.plot_id = plot.plot_id;
