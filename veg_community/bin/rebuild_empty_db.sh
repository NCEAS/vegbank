#!/bin/sh

dropdb communities_dev
createdb communities_dev
psql communities_dev < ../sql/community_implementation.sql
psql communities_dev < ../sql/createCommunitySummary_pg.sql
psql communities_dev < ../sql/createEcoartTables_pg.sql
      
