create table groups(
	gid int not null,
	name varchar(256) not null,
	secret varchar(256)
);
create table memberships(
	parent_gid int not null,
	child_gid int not null
);
create table ids(
	i int not null
);
insert into ids(i) values (0)