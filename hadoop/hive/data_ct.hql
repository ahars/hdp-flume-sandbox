drop table if exists data;
create external table data (
	date		string	comment '',
	datetime	string	comment '',
	libelle		string	comment '',
	lib_action	string	comment '',
	id_icare	int  	comment '',
	id_profil	int	comment '',
	lib_media	string	comment '',
	lib_catalogue	string	comment '',
	flag_detail	string	comment '',
	universe	string	comment '',
	id_start	int	comment '',
	id_end		int	comment '',
	id_limit	int	comment '',
	flag_flat	string	comment '',
	lib_lists	string	comment '',
	id_previous	string	comment '',
	id_film		string	comment '',
	id_serie	string	comment ''
) comment 'data'
row format delimited fields terminated by '\;'
stored as textfile
location '/user/root/data/ACCEPTED/dest_1';
