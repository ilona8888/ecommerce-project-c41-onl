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
    /*Роль*/
    ROLE          text      not null,
    /*Дата создания*/
    CREATED_DATE  timestamp not null default now()
);

comment on table users is 'Пользователи';
comment on column users.ID is 'Идентификатор';
comment on column users.USER_NAME is 'Имя пользователя';
comment on column users.EMAIL is 'Электронная почта';
comment on column users.PASSWORD_HASH is 'Hash-пароля';
comment on column users.STATUS is 'Статус пользователя (active or inactive)';
comment on column users.FIRST_NAME is 'Имя';
comment on column users.LAST_NAME is 'Фамилия';
comment on column users.BIRTHDAY is 'Дата рождения';
comment on column users.ROLE is 'Роль';
comment on column users.CREATED_DATE is 'Дата создания';

/*Таблица файлов*/
create table FILES
(
    /*Идентификатор -- первичный ключ*/
    ID           bigserial not null
        constraint files_pk
            primary key,
    /*Имя файла*/
    FILE_NAME    varchar   not null,
    /*Данные файла*/
    DATA         bytea     not null,
    /*Дата создания*/
    CREATED_DATE timestamp not null default now()
);

comment on table files is 'Файлы';
comment on column files.ID is 'Идентификатор';
comment on column files.FILE_NAME is 'Имя файла';
comment on column files.DATA is 'Данные файла';
comment on column files.CREATED_DATE is 'Дата создания';

/*Таблица категорий*/
create table CATEGORIES
(
    /*Идентификатор -- первичный ключ*/
    ID   bigserial not null
        constraint categories_pk
            primary key,
    /*Название категории*/
    NAME text      not null
);

comment on table categories is 'Категории';
comment on column categories.ID is 'Идентификатор';
comment on column categories.NAME is 'Название категории';

/*Таблица токенов*/
create table TOKENS
(
    /*Идентификатор -- первичный ключ*/
    ID           uuid      not null
        constraint tokens_pk
            primary key,
    /*Тип токена*/
    TYPE         text      not null,
    /*Использован*/
    IS_ACTIVE    bool      not null,
    /*ID пользователя -- внешний ключ (USERS)*/
    USER_ID      bigint    not null
        constraint tokens_users_id_fk
            references USERS on delete cascade,
    /*Дата создания*/
    CREATED_DATE timestamp not null default now()
);

comment on table tokens is 'Токены';
comment on column tokens.ID is 'Токен';
comment on column tokens.TYPE is 'Тип токена';
comment on column tokens.IS_ACTIVE is 'Использован';
comment on column tokens.USER_ID is 'ID пользователя';
comment on column tokens.CREATED_DATE is 'Дата создания';

/*Таблица продавцов*/
create table SELLERS
(
    /*Идентификатор -- первичный ключ*/
    USERS_ID     bigserial not null
        constraint sellers_pk
            primary key
        /*Внешний ключ (USERS)*/
        constraint sellers_users_id_fk
            references USERS on delete cascade,
    /*О продавце*/
    DETAILS      text      not null,
    /*Контакты*/
    CONTACT_INFO varchar   not null
);

comment on table sellers is 'Продавцы';
comment on column sellers.USERS_ID is 'ID пользователя';
comment on column sellers.DETAILS is 'О продавце';
comment on column sellers.CONTACT_INFO is 'Контакты';

/*Таблица товаров*/
create table PRODUCTS
(
    /*Идентификатор -- первичный ключ*/
    ID            bigserial      not null
        constraint products_pk
            primary key,
    /*Название*/
    NAME          text           not null,
    /*Описание*/
    DESCRIPTION   text           not null,
    /*Цена*/
    PRICE         decimal(10, 2) not null
        /*Цена не может быль отрицательной*/
        check (PRICE >= 0),
    /*ID продавца -- внешний ключ (SELLERS)*/
    SELLERS_ID    bigint         not null
        constraint products_sellers_id_fk
            references SELLERS on delete cascade,
    /*ID категории -- внешний ключ (CATEGORIES)*/
    CATEGORIES_ID bigint         not null
        constraint products_categories_id_fk
            references CATEGORIES on delete cascade
);

comment on table products is 'Товары';
comment on column products.ID is 'Идентификатор';
comment on column products.NAME is 'Название';
comment on column products.DESCRIPTION is 'Описание';
comment on column products.PRICE is 'Цена';
comment on column products.SELLERS_ID is 'ID продавца';
comment on column products.CATEGORIES_ID is 'ID категории';

/*Таблица фото товаров*/
create table PRODUCT_PHOTOS
(
    /*Идентификатор -- первичный ключ*/
    ID           bigserial not null
        constraint product_photos_pk
            primary key,
    /*Дата создания*/
    CREATED_DATE timestamp not null default now(),
    /*ID файла -- внешний ключ (FILES)*/
    FILE_ID      bigint    not null
        constraint product_photos_files_id_fk
            references FILES on delete cascade
        constraint product_photos_pk_2
            unique,
    /*ID товара -- внешний ключ (PRODUCTS)*/
    PRODUCTS_ID  bigint    not null
        constraint product_photos_products_id_fk
            references PRODUCTS on delete cascade
);

comment on table product_photos is 'Фото товаров';
comment on column product_photos.ID is 'Идентификатор';
comment on column product_photos.CREATED_DATE is 'Дата создания';
comment on column product_photos.FILE_ID is 'ID файла';
comment on column product_photos.PRODUCTS_ID is 'ID товара';

/*Таблица покупок*/
create table PURCHASES
(
    /*Идентификатор -- первичный ключ*/
    ID            bigserial      not null
        constraint purchases_pk
            primary key,
    /*Стоимость*/
    COST          decimal(10, 2) not null
        /*Стоимость не может быль отрицательной*/
        check (COST >= 0),
    /*Дата покупки*/
    PURCHASE_DATE timestamp      not null default now(),
    /*ID пользователя -- внешний ключ (USERS)*/
    USERS_ID      bigint         not null
        constraint purchases_users_id_fk
            references USERS on delete cascade,
    /*ID товара -- внешний ключ (PRODUCTS)*/
    PRODUCTS_ID   bigint         not null
        constraint purchases_products_id_fk
            references PRODUCTS on delete cascade
);

comment on table purchases is 'Покупки';
comment on column purchases.ID is 'Идентификатор';
comment on column purchases.COST is 'Стоимость';
comment on column purchases.PURCHASE_DATE is 'Дата покупки';
comment on column purchases.USERS_ID is 'ID пользователя';
comment on column purchases.PRODUCTS_ID is 'ID товара';

/*Таблица избранного*/
create table FAVORITES
(
    /*ID пользователя -- первичный ключ*/
    USERS_ID    bigint    not null
        constraint favorites_pk
            primary key
        /*Внешний ключ (USERS)*/
        constraint favorites_users_id_fk
            references USERS on delete cascade,
    /*ID товара -- внешний ключ (PRODUCTS)*/
    PRODUCTS_ID bigint    not null
        constraint favorites_products_id_fk
            references PRODUCTS on delete cascade,
    /*Дата добавления*/
    DATE_ADDED  timestamp not null default now()
);

comment on table favorites is 'Избранные';
comment on column favorites.USERS_ID is 'ID пользователя';
comment on column favorites.PRODUCTS_ID is 'ID товара';
comment on column favorites.DATE_ADDED is 'Дата добавления';

/*Таблица отзывов*/
create table REVIEWS
(
    /*Идентификатор -- первичный ключ*/
    ID          bigserial not null
        constraint reviews_pk
            primary key,
    RATING      smallint  not null
        check ((RATING >= 1) and (RATING <= 5)),
    /*Комментарий*/
    COMMENT     text,
    /*ID пользователя -- внешний ключ (USERS)*/
    USERS_ID    bigint    not null
        constraint reviews_users_id_fk
            references USERS on delete cascade,
    /*ID товара -- внешний ключ (PRODUCTS)*/
    PRODUCTS_ID bigint    not null
        constraint reviews_products_id_fk
            references PRODUCTS on delete cascade,
    /* Составное ограничение уникальности: пользователь + товар */
    constraint reviews_users_products_uc
        unique (USERS_ID, PRODUCTS_ID)
);

comment on table reviews is 'Отзывы';
comment on column reviews.ID is 'Идентификатор';
comment on column reviews.RATING is 'Оценка';
comment on column reviews.COMMENT is 'Комментарий';
comment on column reviews.USERS_ID is 'ID пользователя';
comment on column reviews.PRODUCTS_ID is 'ID товара';
