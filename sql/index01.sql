SELECT count(*) FROM tickets;

-- No Index vs Index
CREATE TABLE tickets0 AS TABLE tickets;

---- Read from inside to outside
---- startup cost is not accurate here
EXPLAIN SELECT * FROM tickets0 WHERE ticket_no = '0005435998385';

EXPLAIN ANALYSE SELECT * FROM tickets0 WHERE ticket_no = '0005435998385';

-- B+Tree by default
---- it takes some time to create index
-- CREATE INDEX tickets0_ticket_no_idx using btree ON tickets0(ticket_no);
CREATE INDEX tickets0_ticket_no_idx ON tickets0(ticket_no);

EXPLAIN ANALYSE SELECT * FROM tickets0 WHERE ticket_no = '0005435998385';


-- B+Tree vs Hash
---- Equal
CREATE TABLE tickets1 AS TABLE tickets;
CREATE INDEX tickets1_ticket_no_idx ON tickets1 using hash (ticket_no);

EXPLAIN ANALYSE SELECT * FROM tickets1 WHERE ticket_no = '0005435998385';

EXPLAIN ANALYSE SELECT * FROM tickets0 WHERE ticket_no = '0005435998385';
---- Hash index has lower cost

---- Range
EXPLAIN ANALYSE SELECT * FROM tickets0 WHERE ticket_no >= '0005435998300';

EXPLAIN ANALYSE SELECT * FROM tickets1 WHERE ticket_no >= '0005435998300';
---- BTree index has lower cost


-- LILKE
create table tickets2 as table tickets;

create index tickets2_passenger_id on tickets2 (passenger_id COLLATE "C");

explain analyse
select * from tickets2
where passenger_id like '%53 429919';

explain analyse
select * from tickets2
where passenger_id like '3453 4299%';


-- Large Result Set does not take index
CREATE TABLE bookings0 AS TABLE bookings;
CREATE INDEX bookings0_book_date_idx ON bookings0 (book_date);

---- Question Before Execute
EXPLAIN ANALYSE SELECT * FROM bookings0
                WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp;

---- Question Before Execute
EXPLAIN ANALYSE SELECT * FROM bookings0
                WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2018-01-01 00:00:00'::timestamp;

---- Why?

-- High Cardinality First
EXPLAIN ANALYSE
SELECT * FROM bookings0
WHERE total_amount >= 67600.00
  AND book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp;

---- Question Here: How to create an index? (total_amount, book_date) or (book_date, total_amount)

SELECT count(distinct book_date) FROM bookings0; -- 540474
SELECT count(distinct total_amount) FROM bookings0; -- 5667

CREATE TABLE bookings1 AS TABLE bookings;

CREATE INDEX bookings0_book_date_total_amount_idx ON bookings0 (book_date, total_amount);
CREATE INDEX bookings1_total_amount_book_date_idx ON bookings1 (total_amount, book_date);


EXPLAIN ANALYSE
SELECT * FROM bookings0
WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp
AND total_amount >= 67600.00;

EXPLAIN ANALYSE
SELECT * FROM bookings1
WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp
AND total_amount >= 67600.00;


-- Index Condition Pushdown
EXPLAIN ANALYSE
SELECT * FROM bookings0
WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp
AND total_amount >= 67600.00;

EXPLAIN ANALYSE
SELECT * FROM bookings0
WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp
ORDER BY book_ref;

---- How to create an index?

CREATE INDEX bookings0_book_date_book_ref_index ON bookings0 (book_date, book_ref);

EXPLAIN ANALYSE
SELECT * FROM bookings0
WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp
ORDER BY book_ref;

---- Question: What wrong with 2 separate indexes?

DROP INDEX bookings0_book_date_total_amount_idx;
DROP INDEX bookings0_book_date_book_ref_index;

SELECT count(distinct book_ref) FROM bookings0; -- 2111110

CREATE INDEX bookings0_book_date_total_amount_book_ref
    on bookings0 (book_date, book_ref, total_amount);

EXPLAIN ANALYSE
SELECT * FROM bookings0
WHERE book_date >= '2017-01-01 00:00:00'::timestamp
AND book_date < '2017-01-02 00:00:00'::timestamp
ORDER BY book_ref;


-- Covering Index
CREATE TABLE seats0 AS TABLE seats;

---- Show default index
create unique index seats0_pkey
    on seats0 (aircraft_code, seat_no);

EXPLAIN ANALYSE SELECT aircraft_code, seat_no, fare_conditions
                FROM seats0
                WHERE aircraft_code = '733';

---- How to improve this query?

CREATE INDEX seats0_covering_idx on seats0(aircraft_code, seat_no) include (fare_conditions);

EXPLAIN ANALYSE SELECT aircraft_code, seat_no, fare_conditions
                FROM seats
                WHERE aircraft_code = '733';

