echo 'Generating DB for Vegbank version @vegbankVersion@'
psql $1 < vegbank.sql
psql $1 < create_aggregrates.sql
psql $1 < create_extras.sql
psql $1 < createIndices.sql
psql $1 < create_vegbank_views.sql
psql $1 < vegbank_populate_configtables.sql
psql $1 < vegbank-changes-@vegbankVersion@.sql
psql $1 < create_admin_user.sql
