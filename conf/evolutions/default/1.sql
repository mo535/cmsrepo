# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table page (
  id                        bigint auto_increment not null,
  is_active                 tinyint(1) default 0,
  page_name                 varchar(255),
  create_date               datetime,
  constraint pk_page primary key (id))
;

create table settings (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  address                   varchar(255),
  name                      varchar(255),
  pagename                  varchar(255),
  constraint pk_settings primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  password                  varchar(255),
  is_active                 tinyint(1) default 0,
  firstname                 varchar(255),
  lastname                  varchar(255),
  createdate                datetime,
  constraint pk_user primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table page;

drop table settings;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

