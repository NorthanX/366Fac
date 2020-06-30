create table user (
UserID varchar(200) primary key,
Usermail varchar(200),
Userpic varchar(200),
Userpwd varchar(200),
Userinfo varchar(400),
Regtime Date,
Role varchar(200));

create table blog (
BlogID int primary key AUTO_INCREMENT,
AuthorID varchar(200) references user(UserID) ,
Content TEXT,
CommentCount int,
CreateDate Date,
Title varchar(100),
Type varchar(100),
Likes int,
LikeID varchar(500));

create table loginticket(
id int primary key AUTO_INCREMENT,
userid varchar(200) references user(userid),
expired Date,
status int,
ticket varchar(200));

create table tag(
TagID int primary key AUTO_INCREMENT,
TagName varchar(100),
TagNum int);

create table category(
ID int primary key AUTO_INCREMENT,
BlogID int references blog(BlogID) on delete cascade,
TagID int references tag(TagID)
TagName varchar(100));

create table comment(
CommentID int  primary key AUTO_INCREMENT,
BlogID int references blog(BlogID) on delete cascade,
UserID varchar(200),
CommentContent text,
CommentTime Date,
Userpic varchar(100),
ParentCommentID int);

create table follow(
FollowerId varchar(200),
BloggerId varchar(200));

create table accessory(
blogId int references blog(BlogID) on delete cascade,
Address varchar(200));
