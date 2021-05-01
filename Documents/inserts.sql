INSERT INTO gma.user (id, username, email, password, type) VALUES (1, 'admin', 'admin', 'admin', 2);
INSERT INTO gma.user (id, username, email, password, type) VALUES (2, 'user', 'user', 'user', 1);
INSERT INTO gma.user (id, username, email, password, type) VALUES (3, 'ban', 'ban', 'ban', 0);

INSERT INTO gma.review (id, idUser, idProduct, text) VALUES (1, 2, 1, 'Best iphone ever!');
INSERT INTO gma.review (id, idUser, idProduct, text) VALUES (2, 2, 1, 'I think that stonex one is better!');
