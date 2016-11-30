create table user_baseinfo_table(user_id int primary key not null auto_increment,user_name varchar(12)
user_pwd varchar(12)
);

create table income_record_table(inc_id int primary key not null auto_increment,inc_name varchar(50),
	inc_money real, inc_date date, user_id int, foreign key (user_id) references user_baseinfo_table(user_id),
	category_id int, foreign key(category_id) references account_category_table(category_id)
);

create table pay_record_table(pay_id int primary key not null auto_increment,pay_name varchar(50),
	pay_money real, pay_date date, user_id int ,foreign key (user_id) references user_baseinfo_table(user_id),
		category_id int, foreign key(category_id) references account_category_table(category_id)
	);

create table account_category_table(category_id int primary key not null auto_increment,category_name varchar(20),
	category_flag int,
	user_id int, foreign key (user_id) references user_baseinfo_table(user_id)
);