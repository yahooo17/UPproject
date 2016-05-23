SELECT name FROM chat.users as one where((SELECT count(user_id) 
from chat.messages where user_id=one.id) >3);