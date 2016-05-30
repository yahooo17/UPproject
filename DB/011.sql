SELECT DATEDIFF(CURRENT_TIMESTAMP,
(SELECT u.date FROM chat.messages AS u order by date  limit 1)) AS m;