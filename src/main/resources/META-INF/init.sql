CREATE TABLE IF NOT EXISTS author (
  id int auto_increment PRIMARY KEY,
  name CHAR(100)
);
INSERT INTO author VALUES (null, 'Дж. К. Роулинг');
INSERT INTO author VALUES (null, 'Стивен Кинг');
INSERT INTO author VALUES (null, 'Маргарет Митчелл');

CREATE TABLE IF NOT EXISTS book (
  id int auto_increment PRIMARY KEY,
  name CHAR(200),
  author_id INT8
);
INSERT INTO book VALUES (null, 'Гарри Поттер и узник Азкабана', 1);
INSERT INTO book VALUES (null, 'Зеленая миля', 2);
INSERT INTO book VALUES (null, 'Побег из Шоушенка', 2);
INSERT INTO book VALUES (null, 'Унесенные ветром', 3);
