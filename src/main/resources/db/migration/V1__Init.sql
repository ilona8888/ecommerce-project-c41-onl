
/*Таблица пользователей*/
CREATE TABLE USERS
(
    /*Идентификатор -- первичный ключ*/
    ID            bigserial not null
        constraint users_pk
            primary key,
    /*Имя пользователя -- уникальное*/
    USER_NAME     varchar   not null
        constraint users_pk_2
            unique,
    /*email -- уникальное*/
    EMAIL         varchar   not null
        constraint users_pk_3
            unique,
    /*Hash-пароля*/
    PASSWORD_HASH text      not null,
    /*Статус пользователя (active or inactive)*/
    STATUS        bool      not null,
    /*Имя*/
    FIRST_NAME    text      not null,
    /*Фамилия*/
    LAST_NAME     text,
    /*Дата рождения*/
    BIRTHDAY      date,
    /*Пол*/
    GENDER        text,
    /*О себе*/
    ABOUT         text,
    /*Дата создания*/
    CREATED_DATE  timestamp not null default now()
);

comment on table users is 'Пользователи';
comment on column users.ID is 'Идентификатор';
comment on column users.USER_NAME is 'Имя пользователя';
comment on column users.EMAIL is 'Email';
comment on column users.PASSWORD_HASH is 'Hash-пароля';
comment on column users.STATUS is 'Статус пользователя (active or inactive)';
comment on column users.FIRST_NAME is 'Имя';
comment on column users.LAST_NAME is 'Фамилия';
comment on column users.BIRTHDAY is 'Дата рождения';
comment on column users.GENDER is 'Пол';
comment on column users.ABOUT is 'О себе';
comment on column users.CREATED_DATE is 'Дата создания';