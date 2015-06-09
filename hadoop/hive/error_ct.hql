drop table if exists error;
create external table error ( 
	cat_1	string, 
	cat_2	string, 
	cat_3	string
)
partitioned by (
	mois_error string,
	date_error string
)
row format delimited fields terminated by '\;' 
location '/user/root/data/REJECTED/';

