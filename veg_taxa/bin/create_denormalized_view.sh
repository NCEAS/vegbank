#!/bin/sh -ev  


# This script will create a view that represents
# the plant taxa database in a denormalized structure
# that will be used for simplifying query creation

DENORMSCRIPT=../lib/denormalize.sql

# Remove any existing tables
sqlplus nvc/jhhtpass @$DENORMSCRIPT <<EOF
EOF













