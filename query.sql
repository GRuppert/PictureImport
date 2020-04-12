-- Fájlok száma egy könyvtárban, ami csak a G:-n van meg és a Porsche-n nem
SELECT m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.drive_id = 3 AND 0 = (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id in (4,5)) group by m.folder_id;
-- Fájlok száma egy könyvtárban, ami többször van meg egy meghajtón
SELECT m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE 1 < (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id = m.drive_id) group by m.folder_id;
-- Fájlok száma egy könyvtárban, ami a G:-n más filehash-el van meg mint a Porsche-n
SELECT m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.drive_id = 3 AND 0 < (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id in (4,5) AND m2.filehash <> m.filehash) group by m.folder_id;
-- Fájlok, ami a G:-n más filehash-el van meg mint a Porsche-n
SELECT m.folder_id, f.path, m.filename FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.drive_id = 3 AND 0 < (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id in (4,5) AND m2.filehash <> m.filehash);
-- Fájlok, amik csak egyszer szereplnek
SELECT m.folder_id, f.path, m.filename FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.image_id IN (SELECT image_id FROM media_file group by image_id having count(*) = 1);


SELECT  d.description, m.folder_id, f.path FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id LEFT JOIN pictureorganizer.drive d ON d.id = m.drive_id where  (date_mod BETWEEN '2014-09-09 00:15:55' AND '2014-09-12 10:15:55') group by m.folder_id;
SELECT * FROM pictureorganizer.folder WHERE path like '%Babi%';
SELECT d.description, m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id LEFT JOIN pictureorganizer.drive d ON d.id = m.drive_id WHERE m.drive_id <> 3 AND m.folder_id IN (SELECT id FROM pictureorganizer.folder WHERE path like '%Babi%') AND 0 = (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id = 3) group by m.folder_id;
SELECT m.id, d.description, f.path, m.filename FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id LEFT JOIN pictureorganizer.drive d ON d.id = m.drive_id WHERE m.folder_id = 14500 AND 0 = (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id = 3);

SELECT d.description, m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id LEFT JOIN pictureorganizer.drive d ON d.id = m.drive_id WHERE m.drive_id <> 3 AND m.folder_id IN (SELECT id FROM pictureorganizer.folder WHERE path like '%Babi%') AND 0 < (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id = 3 AND m2.filehash <> m.filehash) group by m.folder_id;

-- datemod fájl módosítás dátuma
SELECT count(*) FROM pictureorganizer.media_file WHERE date_stored IS NULL OR date_mod IS NULL;
-- képek aminél nincs fájl egyező dátummal
SELECT * FROM pictureorganizer.image as i WHERE 0 = (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = i.id AND m2.date_mod = CONCAT(SUBSTRING(m2.date_stored, 1, 10), ' ', SUBSTRING(m2.date_stored, 12, 8)));
-- különböző fájlok egyező dátumok egy képhez
SELECT * FROM pictureorganizer.image as i WHERE 1 < (SELECT count(*) FROM (SELECT m2.filehash FROM media_file AS m2 WHERE m2.image_id = i.id AND m2.date_mod = CONCAT(SUBSTRING(m2.date_stored, 1, 10), ' ', SUBSTRING(m2.date_stored, 12, 8)) group by filehash) as uni);
SELECT CONCAT(SUBSTRING(date_mod, 1, 10), 'T', SUBSTRING(date_mod, 12, 8)), SUBSTRING(date_stored, 1, 19) FROM media_file m where image_id = 43905;
SELECT count(*) FROM media_file m where image_id = 3103 AND CONCAT(SUBSTRING(date_stored, 1, 10), ' ', SUBSTRING(date_stored, 12, 8)) = date_mod group by filehash;
