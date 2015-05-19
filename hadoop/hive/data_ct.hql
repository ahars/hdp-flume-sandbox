drop table if exists data;
create external table data ( 
	cat_1	string, 
	cat_2	string, 
	cat_3	string, 
	cat_4	string, 
	cat_5	int, 
	cat_6	int,
	cat_7	string,
	cat_8	string,
	cat_9	string,
	cat_10	string,
	cat_11	int,
	cat_12	int,
	cat_13	int,
	cat_14	string,
	cat_15	string,
	cat_16	string,
	cat_17	int,
	cat_18	int,
	timestamp string
) 
row format delimited fields terminated by '\;' 
location '/user/root/data/ACCEPTED/';

