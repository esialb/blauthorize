create table groups(
	gid int not null,
	name varchar(256) not null,
	secret varchar(256),
	primary key (gid, name)
);
create table memberships(
	parent_gid int not null,
	child_gid int not null,
	primary key (parent_gid, child_gid)
);
create table ids(
	id int not null
);
insert into ids (id) values (0)