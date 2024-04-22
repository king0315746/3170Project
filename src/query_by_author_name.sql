SELECT 
    b.title AS "Book Title", 
    b.ISBN, 
    b.unit_price AS "Unit Price", 
    b.no_of_copies AS "No of Copies Available",
    GROUP_CONCAT(DISTINCT ba.author_name ORDER BY ba.author_name ASC SEPARATOR ', ') AS "A List of Authors"
FROM book b
JOIN book_author ba ON b.ISBN = ba.ISBN
WHERE b.ISBN IN (
    SELECT DISTINCT ba.ISBN
    FROM book_author ba
    WHERE ba.author_name LIKE ? 
)
GROUP BY b.title, b.ISBN, b.unit_price, b.no_of_copies
ORDER BY b.title ASC, b.ISBN ASC;