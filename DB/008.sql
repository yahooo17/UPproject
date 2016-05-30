SELECT u.name, m.text, m.date FROM messages AS m 
	LEFT JOIN users AS u ON m.user_id = u.id ORDER BY date;
