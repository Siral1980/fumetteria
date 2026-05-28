-- TRUNCATE TABLE comic RESTART IDENTITY CASCADE;

INSERT INTO comic (title, author, price, genre, quantity, out_of_stock) VALUES
-- Genere: Manga
('One Piece Vol. 1', 'Eiichiro Oda', 5.20, 'Manga', 25, false),
('Naruto Vol. 1', 'Masashi Kishimoto', 4.90, 'Manga', 15, false),
('Death Note Vol. 1', 'Tsugumi Ohba', 5.90, 'Manga', 12, false),
('L''Attacco dei Giganti Vol. 1', 'Hajime Isayama', 4.90, 'Manga', 18, false),
('Demon Slayer Vol. 1', 'Koyoharu Gotouge', 5.20, 'Manga', 30, false),
('Berserk Vol. 1', 'Kentaro Miura', 7.00, 'Manga', 8, false),
('Jujutsu Kaisen Vol. 1', 'Gege Akutami', 5.20, 'Manga', 22, false),
('My Hero Academia Vol. 1', 'Kohei Horikoshi', 4.90, 'Manga', 14, false),
('Dragon Ball Evergreen Edition Vol. 1', 'Akira Toriyama', 4.30, 'Manga', 40, false),
('Fullmetal Alchemist Vol. 1', 'Hiromu Arakawa', 5.90, 'Manga', 10, false),
('Chainsaw Man Vol. 1', 'Tatsuki Fujimoto', 5.20, 'Manga', 19, false),
('Tokyo Ghoul Vol. 1', 'Sui Ishida', 5.90, 'Manga', 7, false),
('Monster Deluxe Vol. 1', 'Naoki Urasawa', 14.00, 'Manga', 5, false),
('Vagabond Vol. 1', 'Takehiko Inoue', 7.00, 'Manga', 6, false),
('Akira Vol. 1', 'Katsuhiro Otomo', 22.00, 'Manga', 4, false),

-- Genere: Supereroi
('The Amazing Spider-Man: Origini', 'Stan Lee', 19.00, 'Supereroi', 10, false),
('Batman: Anno Uno', 'Frank Miller', 15.00, 'Supereroi', 12, false),
('Watchmen', 'Alan Moore', 35.00, 'Supereroi', 6, false),
('Il Ritorno del Cavaliere Oscuro', 'Frank Miller', 28.00, 'Supereroi', 5, false),
('All-Star Superman', 'Grant Morrison', 30.00, 'Supereroi', 4, false),
('Civil War', 'Mark Millar', 25.00, 'Supereroi', 11, false),
('Daredevil: Rinascita', 'Frank Miller', 20.00, 'Supereroi', 8, false),
('X-Men: Conflitto Finale', 'Chris Claremont', 18.00, 'Supereroi', 3, false),
('Infinity Gauntlet', 'Jim Starlin', 27.00, 'Supereroi', 7, false),
('Flashpoint', 'Geoff Johns', 22.00, 'Supereroi', 9, false),
('Sandman Vol. 1: Preludi e Notturni', 'Neil Gaiman', 25.00, 'Supereroi', 8, false),
('Invincible Omnibus Vol. 1', 'Robert Kirkman', 35.00, 'Supereroi', 14, false),

-- Genere: Fumetto Italiano
('Dylan Dog N. 1: L''alba dei morti viventi', 'Tiziano Sclavi', 4.90, 'Fumetto Italiano', 50, false),
('Tex N. 1: Il totem misterioso', 'Gianluigi Bonelli', 4.90, 'Fumetto Italiano', 45, false),
('Diabolik N. 1: Il re del terrore', 'Angela Giussani', 3.50, 'Fumetto Italiano', 30, false),
('Nathan Never N. 1: Agente Speciale', 'Antonio Serra', 4.90, 'Fumetto Italiano', 15, false),
('Zagor N. 1: La foresta degli agguati', 'Guido Nolitta', 4.90, 'Fumetto Italiano', 20, false),
('Corto Maltese: Una ballata del mare salato', 'Hugo Pratt', 24.00, 'Fumetto Italiano', 6, false),
('Mister No N. 1: Mister No', 'Guido Nolitta', 4.90, 'Fumetto Italiano', 12, false),
('Alan Ford N. 1: Gruppo TNT', 'Max Bunker', 4.00, 'Fumetto Italiano', 18, false),

-- Genere: Graphic Novel
('Maus', 'Art Spiegelman', 22.00, 'Graphic Novel', 15, false),
('Persepolis', 'Marjane Satrapi', 20.00, 'Graphic Novel', 11, false),
('Blankets', 'Craig Thompson', 29.00, 'Graphic Novel', 5, false),
('La Profezia dell''Armadillo', 'Zerocalcare', 16.00, 'Graphic Novel', 25, false),
('Dimentica il mio nome', 'Zerocalcare', 18.00, 'Graphic Novel', 20, false),
('Un polpo alla gola', 'Zerocalcare', 17.00, 'Graphic Novel', 14, false),
('Portugal', 'Cyril Pedrosa', 30.00, 'Graphic Novel', 4, false),
('Il muretto', 'Celine Fraipont', 19.00, 'Graphic Novel', 7, false),
('Fumetti di menare', 'Autori Vari', 15.00, 'Graphic Novel', 9, false),

-- Genere: Altro / Underground
('The Walking Dead Vol. 1: Giorni perduti', 'Robert Kirkman', 14.00, 'Altro', 13, false),
('Saga Vol. 1', 'Brian K. Vaughan', 16.00, 'Altro', 10, false),
('Scott Pilgrim Vol. 1', 'Bryan Lee O''Malley', 12.50, 'Altro', 8, false),
('Paperone e i dollari di ghiaccio', 'Carl Barks', 8.90, 'Altro', 16, false),
('Topolino e il mistero di Macchia Nera', 'Floyd Gottfredson', 12.00, 'Altro', 11, false),
('Sceriffo Fox: Giustizia a Solitude', 'Giorgio Pezzin', 9.50, 'Altro', 5, false)

ON CONFLICT (title) DO NOTHING;
