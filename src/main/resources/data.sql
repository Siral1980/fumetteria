-- TRUNCATE TABLE comic RESTART IDENTITY CASCADE;

INSERT INTO comic (title, author, price, genre, quantity) VALUES
-- Genere: Manga
('One Piece Vol. 1', 'Eiichiro Oda', 5.20, 'Manga', 25),
('Naruto Vol. 1', 'Masashi Kishimoto', 4.90, 'Manga', 15),
('Death Note Vol. 1', 'Tsugumi Ohba', 5.90, 'Manga', 12),
('L''Attacco dei Giganti Vol. 1', 'Hajime Isayama', 4.90, 'Manga', 18),
('Demon Slayer Vol. 1', 'Koyoharu Gotouge', 5.20, 'Manga', 30),
('Berserk Vol. 1', 'Kentaro Miura', 7.00, 'Manga', 8),
('Jujutsu Kaisen Vol. 1', 'Gege Akutami', 5.20, 'Manga', 22),
('My Hero Academia Vol. 1', 'Kohei Horikoshi', 4.90, 'Manga', 14),
('Dragon Ball Evergreen Edition Vol. 1', 'Akira Toriyama', 4.30, 'Manga', 40),
('Fullmetal Alchemist Vol. 1', 'Hiromu Arakawa', 5.90, 'Manga', 10),
('Chainsaw Man Vol. 1', 'Tatsuki Fujimoto', 5.20, 'Manga', 19),
('Tokyo Ghoul Vol. 1', 'Sui Ishida', 5.90, 'Manga', 7),
('Monster Deluxe Vol. 1', 'Naoki Urasawa', 14.00, 'Manga', 5),
('Vagabond Vol. 1', 'Takehiko Inoue', 7.00, 'Manga', 6),
('Akira Vol. 1', 'Katsuhiro Otomo', 22.00, 'Manga', 4),

-- Genere: Supereroi
('The Amazing Spider-Man: Origini', 'Stan Lee', 19.00, 'Supereroi', 10),
('Batman: Anno Uno', 'Frank Miller', 15.00, 'Supereroi', 12),
('Watchmen', 'Alan Moore', 35.00, 'Supereroi', 6),
('Il Ritorno del Cavaliere Oscuro', 'Frank Miller', 28.00, 'Supereroi', 5),
('All-Star Superman', 'Grant Morrison', 30.00, 'Supereroi', 4),
('Civil War', 'Mark Millar', 25.00, 'Supereroi', 11),
('Daredevil: Rinascita', 'Frank Miller', 20.00, 'Supereroi', 8),
('X-Men: Conflitto Finale', 'Chris Claremont', 18.00, 'Supereroi', 3),
('Infinity Gauntlet', 'Jim Starlin', 27.00, 'Supereroi', 7),
('Flashpoint', 'Geoff Johns', 22.00, 'Supereroi', 9),
('Sandman Vol. 1: Preludi e Notturni', 'Neil Gaiman', 25.00, 'Supereroi', 8),
('Invincible Omnibus Vol. 1', 'Robert Kirkman', 35.00, 'Supereroi', 14),

-- Genere: Fumetto Italiano
('Dylan Dog N. 1: L''alba dei morti viventi', 'Tiziano Sclavi', 4.90, 'Fumetto Italiano', 50),
('Tex N. 1: Il totem misterioso', 'Gianluigi Bonelli', 4.90, 'Fumetto Italiano', 45),
('Diabolik N. 1: Il re del terrore', 'Angela Giussani', 3.50, 'Fumetto Italiano', 30),
('Nathan Never N. 1: Agente Speciale', 'Antonio Serra', 4.90, 'Fumetto Italiano', 15),
('Zagor N. 1: La foresta degli agguati', 'Guido Nolitta', 4.90, 'Fumetto Italiano', 20),
('Corto Maltese: Una ballata del mare salato', 'Hugo Pratt', 24.00, 'Fumetto Italiano', 6),
('Mister No N. 1: Mister No', 'Guido Nolitta', 4.90, 'Fumetto Italiano', 12),
('Alan Ford N. 1: Gruppo TNT', 'Max Bunker', 4.00, 'Fumetto Italiano', 18),

-- Genere: Graphic Novel
('Maus', 'Art Spiegelman', 22.00, 'Graphic Novel', 15),
('Persepolis', 'Marjane Satrapi', 20.00, 'Graphic Novel', 11),
('Blankets', 'Craig Thompson', 29.00, 'Graphic Novel', 5),
('La Profezia dell''Armadillo', 'Zerocalcare', 16.00, 'Graphic Novel', 25),
('Dimentica il mio nome', 'Zerocalcare', 18.00, 'Graphic Novel', 20),
('Un polpo alla gola', 'Zerocalcare', 17.00, 'Graphic Novel', 14),
('Portugal', 'Cyril Pedrosa', 30.00, 'Graphic Novel', 4),
('Il muretto', 'Celine Fraipont', 19.00, 'Graphic Novel', 7),
('Fumetti di menare', 'Autori Vari', 15.00, 'Graphic Novel', 9),

-- Genere: Altro / Underground
('The Walking Dead Vol. 1: Giorni perduti', 'Robert Kirkman', 14.00, 'Altro', 13),
('Saga Vol. 1', 'Brian K. Vaughan', 16.00, 'Altro', 10),
('Scott Pilgrim Vol. 1', 'Bryan Lee O''Malley', 12.50, 'Altro', 8),
('Paperone e i dollari di ghiaccio', 'Carl Barks', 8.90, 'Altro', 16),
('Topolino e il mistero di Macchia Nera', 'Floyd Gottfredson', 12.00, 'Altro', 11),
('Sceriffo Fox: Giustizia a Solitude', 'Giorgio Pezzin', 9.50, 'Altro', 5)

ON CONFLICT (title) DO NOTHING;
