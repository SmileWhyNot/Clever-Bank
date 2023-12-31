-- Создание таблиц согласно композиции БД
CREATE TABLE bank(
    id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY ,
    name varchar(100) NOT NULL UNIQUE
);

CREATE TABLE person(
    id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY ,
    name varchar(100) NOT NULL ,
    email varchar(100) NOT NULL UNIQUE
);

CREATE TABLE account(
    id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY ,
    account_number varchar(30) NOT NULL UNIQUE ,
    balance DECIMAL(10,2),
    last_interest_calculation_date date,
    person_id int NOT NULL REFERENCES person(id) ON DELETE CASCADE ,
    bank_id int NOT NULL REFERENCES bank(id) ON DELETE CASCADE
);

CREATE TABLE transaction(
    id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY ,
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('пополнение', 'снятие', 'перевод')),
    amount DECIMAL(10, 2) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    sender_account_id int NOT NULL REFERENCES account(id) ON DELETE CASCADE ,
    receiver_account_id int NOT NULL REFERENCES account(id) ON DELETE CASCADE
);

-- Заполнение таблицы bank (5 банков)
INSERT INTO bank (name) VALUES
    ('Clever-Bank'),
    ('BankTrust'),
    ('Liberty Bank'),
    ('Central Finance'),
    ('Unity Bank');

-- Заполнение таблицы person (20 пользователей)
DO $$
    BEGIN
        FOR i IN 1..20 LOOP
                INSERT INTO person (name, email)
                VALUES ('User ' || i, 'user' || i || '@example.com');
            END LOOP;
    END $$;

-- Заполнение таблицы account (40 счетов)
-- Предположим, что каждому пользователю принадлежит 2 счета в разных банках
INSERT INTO account (account_number, balance, person_id, bank_id)
SELECT
    -- генерация случайного номера счета
    'AS' || floor(random() * 1000)::INTEGER ||
    LEFT(MD5(random()::TEXT), 4) ||
    LPAD(floor(random() * 10000)::INTEGER::TEXT, 4, '0') ||
    LPAD(floor(random() * 10000)::INTEGER::TEXT, 4, '0') ||
    LEFT(MD5(random()::TEXT), 4) ||
    LEFT(MD5(random()::TEXT), 4) ||
    LPAD(floor(random() * 10000)::INTEGER::TEXT, 4, '0') AS account_number,

    -- генерация баланса
    (random() * 5000 + 1000)::NUMERIC(10, 2) AS balance,

    p.id AS person_id,
    b.id AS bank_id
FROM
    -- Декартово произведение двух таблиц
    person p
        CROSS JOIN bank b
WHERE
    p.id BETWEEN 1 AND 20
  AND b.id BETWEEN 1 AND 5;


-- Заполнение таблицы transaction
INSERT INTO transaction (transaction_type, amount, timestamp, sender_account_id, receiver_account_id)
SELECT
    CASE
        WHEN (row_number() OVER ()) % 6 = 1 THEN 'пополнение'
        WHEN (row_number() OVER ()) % 6 = 2 THEN 'снятие'
        ELSE 'перевод'
        END AS transaction_type,

    (random() * 10000 + 100)::numeric(10, 2) AS amount,
    now() - (random() * 365 * interval '1 day') - (random() * 12 * interval '1 month') - (random() * 5 * interval '1 year') AS timestamp,
    a1.id AS sender_account_id,
    a2.id AS receiver_account_id
FROM
    account a1
        JOIN
    account a2
    ON
            a1.id <> a2.id
ORDER BY
    random()
LIMIT
    200; -- 200 случайных транзакций
