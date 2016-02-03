/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/1/17 22:02:06                           */
/*==============================================================*/


drop table if exists SYS_ACCOUNT;

drop table if exists SYS_ACCOUNT_FEATURE;

drop table if exists SYS_ACCOUNT_ROLE;

drop table if exists SYS_BRANCH;

drop table if exists SYS_MENU;

drop table if exists SYS_ROLE;

drop table if exists SYS_ROLE_MENU;

/*==============================================================*/
/* Table: SYS_ACCOUNT                                           */
/*==============================================================*/
create table SYS_ACCOUNT
(
   ACCOUNTID            varchar(20) not null,
   ACCOUNTNAME          varchar(40) not null,
   PASSWORD             varchar(20) not null,
   BRANCHID             varchar(20) not null,
   AREAID               varchar(36),
   BIRTHDAY             date,
   ADDRESS              varchar(100),
   PHONE                varchar(20),
   EMAIL                varchar(40),
   PROPERTY             varchar(5),
   STATUS               varchar(2),
   EXPIREDATE           date,
   primary key (ACCOUNTID)
);

alter table SYS_ACCOUNT comment '账户信息';

/*==============================================================*/
/* Table: SYS_ACCOUNT_FEATURE                                   */
/*==============================================================*/
create table SYS_ACCOUNT_FEATURE
(
   ACCOUNTID            varchar(20) not null,
   TYPE                 varchar(2) not null,
   VALUE                varchar(2) not null,
   primary key (ACCOUNTID, TYPE, VALUE)
);

alter table SYS_ACCOUNT_FEATURE comment '用户特征信息';

/*==============================================================*/
/* Table: SYS_ACCOUNT_ROLE                                      */
/*==============================================================*/
create table SYS_ACCOUNT_ROLE
(
   ACCOUNTID            varchar(20) not null,
   ROLEID               varchar(10) not null,
   primary key (ACCOUNTID, ROLEID)
);

alter table SYS_ACCOUNT_ROLE comment '用户角色信息表';

/*==============================================================*/
/* Table: SYS_BRANCH                                            */
/*==============================================================*/
create table SYS_BRANCH
(
   BRANCHID             varchar(20) not null,
   BRANCHNAME           varchar(100) not null,
   PARENTID             varchar(20) not null,
   PHONE                varchar(20),
   ADDRESS              varchar(100),
   ALIAS                varchar(50),
   primary key (BRANCHID)
);

alter table SYS_BRANCH comment '机构信息表';

/*==============================================================*/
/* Table: SYS_MENU                                              */
/*==============================================================*/
create table SYS_MENU
(
   MENUID               varchar(20) not null,
   MENUNAME             varchar(40) not null,
   PARENTMENUID         varchar(20) not null,
   PAGEURL              varchar(100),
   ISACTION             varchar(2) not null,
   ACTIONNAME           varchar(100),
   primary key (MENUID)
);

alter table SYS_MENU comment '系统菜单';

/*==============================================================*/
/* Table: SYS_ROLE                                              */
/*==============================================================*/
create table SYS_ROLE
(
   ROLEID               varchar(10) not null,
   ROLENAME             varchar(40) not null,
   primary key (ROLEID)
);

alter table SYS_ROLE comment '角色信息表';

/*==============================================================*/
/* Table: SYS_ROLE_MENU                                         */
/*==============================================================*/
create table SYS_ROLE_MENU
(
   ROLEID               varchar(10) not null,
   MENUID               varchar(20) not null,
   primary key (ROLEID, MENUID)
);

alter table SYS_ROLE_MENU comment '角色权限表';

alter table SYS_ACCOUNT add constraint FK_Reference_1 foreign key (BRANCHID)
      references SYS_BRANCH (BRANCHID) on delete restrict on update restrict;

alter table SYS_ACCOUNT_ROLE add constraint FK_Reference_3 foreign key (ROLEID)
      references SYS_ROLE (ROLEID) on delete restrict on update restrict;

alter table SYS_ACCOUNT_ROLE add constraint FK_Reference_6 foreign key (ACCOUNTID)
      references SYS_ACCOUNT (ACCOUNTID) on delete restrict on update restrict;

alter table SYS_ROLE_MENU add constraint FK_Reference_4 foreign key (ROLEID)
      references SYS_ROLE (ROLEID) on delete restrict on update restrict;

alter table SYS_ROLE_MENU add constraint FK_Reference_5 foreign key (MENUID)
      references SYS_MENU (MENUID) on delete restrict on update restrict;

