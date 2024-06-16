-- Insert roles
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- Insert users
INSERT INTO users (username, password) VALUES 
('admin1', '$2a$10$c6TslXBF7lZ6CtkNXgknAeirhvdipHyFV9CwnLcslx/xxUT6pBD9u'), 
('admin2', '$2a$10$nEXu6ls8C5CdWNWSd6q6k.mUUaPAMW1IFFSipsxK69Heh.IUN/Ebi'), 
('user1', '$2a$10$ksKir4nyqrttUil.xbDn5ewCPSbiCVVX1V8FUbeNXoIjSQhyf7Gta'), 
('user2', '$2a$10$1Kk91DWmRK4zM5SUf1Yabu9Ahpg6XLglwSOIqgjpklrA4rQ8XVsZ.'), 
('user3', '$2a$10$pQ4DOFWtkkuAJY9RRBc8.eGnS694Ml848K9s9YIEnQ8rN5THG6Vtm'), 
('user4', '$2a$10$6/ibA5s4rqduYsV9cQRlR.6HTrauNpMnrT3u8zHItUbNtLoCDqPMm');

-- Assign roles to users
INSERT INTO users_roles (user_id, role_id) VALUES 
(1, 2), 
(2, 2), 
(3, 1), 
(4, 1), 
(5, 1), 
(6, 1);

-- Insert suburbs
INSERT INTO suburbs (name) VALUES 
('Sydney'),
('Newcastle'),
('Byron Bay'),
('Chatswood'),
('Double Bay'),
('Blacktown'),
('Penrith'),
('Wollongong'),
('Liverpool'),
('Parramatta'),
('Vineyard'),
('Box Hill'),
('Nelson'),
('Lismore'),
('Booerie Creek'),
('Bexhill');

-- Insert postcodes
INSERT INTO postcodes (code) VALUES 
('2000'),
('2001'),
('2002'),
('2067'),
('2057'),
('2028'),
('1360'),
('2148'),
('2147'),
('2300'),
('2302'),
('2750'),
('2751'),
('2500'),
('2502'),
('2170'),
('2171'),
('2150'),
('2151'),
('2765'),
('2480');

-- Assign postcodes to suburbs
INSERT INTO suburbs_postcodes (suburb_id, postcode_id) VALUES 
(1, 1), -- Sydney - 2000
(1, 2), -- Sydney - 2001
(1, 3), -- Sydney - 2002
(2, 10), -- Newcastle - 2300
(2, 11), -- Newcastle - 2302
(3, 5), -- Byron Bay - 2057
(4, 4), -- Chatswood - 2067
(4, 5), -- Chatswood - 2057
(5, 6), -- Double Bay - 2028
(5, 7), -- Double Bay - 1360
(6, 8), -- Blacktown - 2148
(6, 9), -- Blacktown - 2147
(7, 12), -- Penrith - 2750
(7, 13), -- Penrith - 2751
(8, 14), -- Wollongong - 2500
(8, 15), -- Wollongong - 2502
(9, 16), -- Liverpool - 2170
(9, 17), -- Liverpool - 2171
(10, 18), -- Parramatta - 2150
(10, 19), -- Parramatta - 2151
(11, 20), -- Vineyard - 2765
(12, 20), -- Box Hill - 2765
(13, 20), -- Nelson - 2765
(14, 21), -- Lismore - 2480
(15, 21), -- Booerie Creek - 2480
(16, 21); -- Bexhill - 2480
