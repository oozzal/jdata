Requirements :
1. mysql-J-connector
2. max_allowed_packet variable = "the max size(upto 1GB) u wish to upload in database"
3. create required virtual machine at runtime to avoid java's out of memory exception  
	i.e. run the program with the arguments "java -Xms 1g _Xmx 1g login"
4. change MySQL variable ............... (yet finding out :p) to use more RAM to avoid SQLException "Out of Memory"
	while uploading larger file in database
	


to use innodb

# Uncomment the following if you are using InnoDB tables
# skip-innodb
delete # from skip -innodb in my.ini file

Or just to make a table use innodb use the following MySQL query:

alter table $database.$tablename engine=innodb;


INNODB confg in my.ini file

# Uncomment the following if you are using InnoDB tables
skip-innodb
innodb_data_home_dir            = "C:/x/xampp/mysql/data/"
innodb_data_file_path           = ibdata1:100M:autoextend
innodb_log_group_home_dir       = "C:/x/xampp/mysql/data/"
# You can set .._buffer_pool_size up to 50 - 80 %
# of RAM but beware of setting memory usage too high
innodb_buffer_pool_size         = 250M
innodb_additional_mem_pool_size = 20M
# Set .._log_file_size to 25 % of buffer pool size
innodb_log_file_size            = 60M
innodb_log_buffer_size          = 8M
innodb_flush_log_at_trx_commit  = 2
innodb_lock_wait_timeout        = 500



	Thank you!
	Uzzal Devkota