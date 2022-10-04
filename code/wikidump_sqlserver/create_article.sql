USE Wikipedia;
DROP TABLE Real_Article;
CREATE TABLE [dbo].[Real_Article](
   [Title] [varchar](255) NULL,
   [Text] [varchar](max) NULL
);
INSERT INTO Real_Article(Title, Text)
VALUES ('Example', 'Fulltext');
SELECT *
FROM Real_Article;
DELETE FROM Real_Article
WHERE Title = 'Example';