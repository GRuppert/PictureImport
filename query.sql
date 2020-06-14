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


SELECT * FROM pictureorganizer.image as i WHERE i.type <> 'mp4' AND 0 = (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = i.id AND (i.duration + 2) > ABS(TIMEDIFF(m2.date_mod, m2.date_stored_local)));
SELECT (0 + 1) < ABS(TIMEDIFF(m2.date_mod, m2.date_stored_local)), ABS(TIMEDIFF(m2.date_stored_local, m2.date_mod)), m2.* FROM media_file AS m2 WHERE m2.image_id = 21;
SELECT duration FROM image WHERE id = 21;


SELECT i.id FROM digikam.images as i LEFT JOIN digikam.albums as a ON i.album = a.id LEFT JOIN digikam.albumroots as ar ON a.albumRoot = ar.id WHERE CONCAT(UPPER(SUBSTRING(ar.identifier, 16)), CONCAT(ar.specificPath, a.relativePath), i.name) = (SELECT CONCAT(SUBSTRING(d.volumeSN, 1, 4), SUBSTRING(d.volumeSN, 6), f.path, '/', m.filename) from pictureorganizer.media_file as m LEFT JOIN pictureorganizer.folder as f ON m.folder_id = f.id LEFT JOIN pictureorganizer.drive as d ON m.drive_id = d.id WHERE m.id = 1042780);

ALTER DATABASE pictureorganizer CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
ALTER DATABASE digikam CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
ALTER TABLE pictureorganizer.folder CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
ALTER TABLE pictureorganizer.media_file CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
ALTER TABLE pictureorganizer.drive CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
ALTER TABLE digikam.albums CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
ALTER TABLE digikam.albumroots CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
ALTER TABLE digikam.images CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;

CREATE VIEW pictureorganizer.images AS SELECT * FROM digikam.images;
CREATE VIEW pictureorganizer.imagepositions AS SELECT * FROM digikam.imagepositions;#gps
CREATE VIEW pictureorganizer.imageinformation AS SELECT * FROM digikam.imageinformation;#rating, orientation
CREATE VIEW pictureorganizer.imagetags AS SELECT * FROM digikam.imagetags;
CREATE VIEW pictureorganizer.tags AS SELECT * FROM digikam.tags;
CREATE VIEW pictureorganizer.imagecomments AS SELECT * FROM digikam.imagecomments;#comments
CREATE VIEW pictureorganizer.imagehistory AS SELECT * FROM digikam.imagehistory;
CREATE VIEW pictureorganizer.videometadata AS SELECT * FROM digikam.videometadata;#duration
CREATE VIEW pictureorganizer.imagemetadata AS SELECT * FROM digikam.imagemetadata;#camera spec

UPDATE pictureorganizer.media_file SET type = 'VID' WHERE LOWER(RIGHT(RTRIM(filename), 3)) = 'mp4'; -- 9795
UPDATE pictureorganizer.media_file SET type = 'VID' WHERE LOWER(RIGHT(RTRIM(filename), 3)) = 'mov'; -- 435
UPDATE pictureorganizer.media_file SET type = 'VID' WHERE LOWER(RIGHT(RTRIM(filename), 3)) = 'avi'; -- 560
UPDATE pictureorganizer.media_file SET type = 'VID' WHERE LOWER(RIGHT(RTRIM(filename), 3)) = 'mts'; -- 576
UPDATE pictureorganizer.media_file SET type = 'VID' WHERE LOWER(RIGHT(RTRIM(filename), 3)) = 'mpg'; -- 259
UPDATE pictureorganizer.media_file SET type = 'VID' WHERE LOWER(RIGHT(RTRIM(filename), 3)) = '3gp'; -- 663
