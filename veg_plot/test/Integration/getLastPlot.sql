--SELECT * from observation WHERE observation.observation_id = max(observation.observation_id);
SELECT * FROM observation, plot WHERE observation_id = ( SELECT max(observation_id)
FROM observation) and observation.plot_id = plot.plot_id;
