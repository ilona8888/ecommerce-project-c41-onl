/*Добавить покупателя*/
    INSERT INTO USERS (USER_NAME,
                   EMAIL,
                   PASSWORD_HASH,
                   STATUS,
                   FIRST_NAME,
                   LAST_NAME,
                   BIRTHDAY,
                   ROLE)
VALUES ('buyer',
        'buyer@gmail.com',
        1234,
        true,
        'Elon',
        'Musk',
        DATE '1971-06-11',
        'buyer');

/*Добавить продавца*/
WITH inserted_user AS (
    INSERT INTO USERS (USER_NAME,
                       EMAIL,
                       PASSWORD_HASH,
                       STATUS,
                       FIRST_NAME,
                       LAST_NAME,
                       BIRTHDAY,
                       ROLE)
        VALUES ('seller',
                'seller@gmail.com',
                1234,
                true,
                'Jeffrey',
                'Bezos',
                DATE '1964-01-12',
                'seller')
        RETURNING id)

/*Добавить продавца в таблицу*/
INSERT
INTO SELLERS (USERS_ID,
              DETAILS,
              contact_info)
SELECT id,
       'Торгует в Химках',
       123456789
FROM inserted_user;
/*Добавить админа в таблицу*/
INSERT INTO USERS (
    USER_NAME,
    EMAIL,
    PASSWORD_HASH,
    STATUS,
    FIRST_NAME,
    LAST_NAME,
    BIRTHDAY,
    ROLE
) VALUES (
             'admin',
             'admin@gmail.com',
             '1234',
             TRUE,
             'System',
             'Admin',
             DATE '1990-01-01',
             'admin'
         )