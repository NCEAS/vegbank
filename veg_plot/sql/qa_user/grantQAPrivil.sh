#!/bin/sh
# Grant read access to the tables in all the vegbank databases to the user qa

psql plots_dev < grant_privil_plots_dev.sql
psql plants_dev < grant_privil_plants_dev.sql
psql communities_dev < grant_privil_communities_dev.sql
psql framework < grant_privil_framework.sql

