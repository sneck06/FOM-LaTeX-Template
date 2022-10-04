USE Wikipedia;
BULK
INSERT Real_Article
FROM 'C:\Users\light\Documents\Wikipedia\enwiki-20220901-pages-articles-multistream-no-redirect.csv' WITH (
            FIRSTROW = 1,
            FIELDQUOTE = '\'
      , FIELDTERMINATOR = ',
            '
      , ROWTERMINATOR = ' \ n ',
	  TABLOCK);