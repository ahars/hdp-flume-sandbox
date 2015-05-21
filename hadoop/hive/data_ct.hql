drop table if exists data;
create external table data ( 
	cat_1	string, 
	cat_2	string, 
	cat_3	string, 
	cat_4	string, 
	cat_5	string, 
	cat_6	string,
	cat_7	string,
	cat_8	string,
	cat_9	string,
	cat_10	string,
	cat_11	string,
	cat_12	string,
	cat_13	string,
	cat_14	string,
	cat_15	string,
	cat_16	string,
	cat_17	string,
	cat_18	string,
	timestamp string
) 
row format delimited fields terminated by '\;' 
location '/user/root/data/ACCEPTED/';

