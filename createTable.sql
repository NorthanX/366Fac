create table "user" (
UserID varchar(200) primary key,
Usermail varchar(200),
Userpic varchar(200),
Userpwd varchar(200),
Userinfo varchar(400),
Regtime Date,
Role varchar(200),
);
create table blog (
BlogID int primary key,
AuthorID varchar(200) references "user"(UserID) ,
ContentMD TEXT,
ContentHTML TEXT,
CommentCount int,
CreateDate Date,
Title varchar(100),
"Type" varchar(100),
Likes int,
LikeID varchar(max),
);

create table loginticket(
id int primary key,
userid varchar(200) references "user"(userid),
expired Date,
"status" int,
ticket varchar(200),
);