-- Number of files and images in different drives
SELECT b.description, b.files, c.images FROM
(SELECT a.description, a.files FROM (SELECT d.description, m.drive_id, COUNT(*) OVER (PARTITION BY m.drive_id) files FROM pictureorganizer.media_file m LEFT JOIN drive d ON m.drive_id = d.id) a GROUP BY a.description, a.files) b LEFT JOIN
(SELECT a.description, a.images FROM (SELECT d.description, m.drive_id, m.image_id, COUNT(*) OVER (PARTITION BY m.drive_id) images FROM pictureorganizer.media_file m LEFT JOIN drive d ON m.drive_id = d.id GROUP BY m.drive_id, m.image_id) a GROUP BY a.description, a.images) c ON b.description = c.description;


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

-- 2017-08-24_20.45.33.jpg
-- 2019_0922_150428_412.MOV
-- PICT_20151101_111321.JPG
-- PANO_20141028_163707.jpg
-- PhotoNote_WP_20150328_13_35_15_Pro_9859.jpg
-- ContactPhoto-IMG_20140502_161421.jpg
-- 2014_08_20_08_16_35_ProShot.jpg 2014_09_21_12_26_56_HDR.jpg
-- 20181007_165559402_iOS.jpg
-- 20160410_125658_LLS.jpg
-- Office Lens_20151211_124602.jpg
-- IMG_20131212_181620.963.jpg
-- IMG_20101120_103708.jpg
-- VID_20101118_172251.3gp
-- SELECT count(*) FROM (
SELECT m.original_filename FROM pictureorganizer.media_file m
WHERE 1 = 1
AND original_filename NOT LIKE 'C00%' -- Nem kell 99
AND original_filename NOT LIKE 'SDC%' -- 1602
AND original_filename NOT LIKE 'DSC%' -- 84616
AND original_filename NOT LIKE '_DSC%' -- 7648
AND original_filename NOT LIKE 'HPIM%' -- 2275
AND original_filename NOT LIKE 'dscf%' -- 505
AND original_filename NOT LIKE 'D5C%' -- 130
AND original_filename NOT LIKE '200%' -- 1525 Ez ok
AND original_filename NOT LIKE '201%' -- 24376 Ez ok
AND original_filename NOT LIKE 'PICT%' -- 11
AND original_filename NOT LIKE 'Picture%' -- 404
AND original_filename NOT LIKE 'picture%' -- 229
AND original_filename NOT LIKE '100_%' -- 1204
AND NOT (length(original_filename) = 12 AND original_filename LIKE 'P%') -- 2560
-- AND filename NOT LIKE '_%'
AND original_filename NOT LIKE '_IGP%' -- 24
AND original_filename NOT LIKE 'img%' -- 413
AND original_filename NOT LIKE 'image%' -- 29
AND original_filename NOT LIKE 'IMG%' -- 10724
AND original_filename NOT LIKE '_MG%' -- 10773
AND original_filename NOT LIKE 'IMAG%' -- 681
AND original_filename NOT LIKE 'SAM%' -- 928
AND original_filename NOT LIKE 'Fénykép%' -- 120
AND original_filename NOT LIKE '_T9A%' -- 57
AND original_filename NOT LIKE 'Kép%' -- 2055
AND original_filename NOT LIKE 'WP_%' -- 22879 Ez ok
AND NOT (REGEXP_LIKE(m.original_filename, '^[0-9]{6}_WP') = 1 OR REGEXP_LIKE(m.original_filename, '^[0-9]{6}_DSC') = 1) -- Amerika sorrend 10434
AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NULL) GROUP BY m.original_filename ORDER BY m.original_filename DESC
-- ) a
;

SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE original_filename LIKE '_DSC%' AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NULL) GROUP BY m.filename) a;
SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE (REGEXP_LIKE(m.original_filename, '^[0-9]{6}_WP') = 1 OR REGEXP_LIKE(m.original_filename, '^[0-9]{6}_DSC') = 1) AND SUBSTRING(original_filename, 7, 1) = "_" AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NULL) GROUP BY m.filename) a;

SELECT count(*) id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NULL;

SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE original_filename LIKE 'WP_%' AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NULL) GROUP BY m.filename) a;
SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE original_filename LIKE 'WP_%' AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NOT NULL) GROUP BY m.filename) a;

SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE original_filename LIKE '200%' AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NULL) GROUP BY m.filename) a;
SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE original_filename LIKE '200%' AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NOT NULL) GROUP BY m.filename) a;

SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE original_filename LIKE '201%' AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NULL) GROUP BY m.filename) a;
SELECT count(*) FROM (SELECT m.filename FROM pictureorganizer.media_file m WHERE original_filename LIKE '201%' AND m.image_id IN (SELECT id FROM pictureorganizer.image WHERE type = "jpg" and original_filename IS NOT NULL) GROUP BY m.filename) a;


-- SELECT count(*) FROM (
SELECT m.original_filename, COUNT(*) OVER (PARTITION BY m.original_filename) colliding, m.date_stored, m.filehash FROM pictureorganizer.media_file m
WHERE length(m.original_filename) > 16
AND duration + 3 > ABS(TIMESTAMPDIFF(SECOND, m.date_mod, CONVERT(CONCAT(SUBSTRING(m.original_filename, 1, 4), '-', SUBSTRING(m.original_filename, 5, 2), '-', SUBSTRING(m.original_filename, 7, 2), ' ', SUBSTRING(m.original_filename, 10, 2), ':', SUBSTRING(m.original_filename, 12, 2), ':', SUBSTRING(m.original_filename, 14, 2)), datetime)))
-- AND CONCAT(SUBSTRING(m.original_filename, 1, 8), SUBSTRING(m.original_filename, 10, 6)) = CONCAT(SUBSTRING(m.date_mod, 1, 4), SUBSTRING(m.date_mod, 6, 2), SUBSTRING(m.date_mod, 9, 2), SUBSTRING(m.date_mod, 12, 2), SUBSTRING(m.date_mod, 15, 2), SUBSTRING(m.date_mod, 18, 2))
GROUP BY m.original_filename, m.date_stored, m.filehash ORDER BY colliding DESC
-- ) a
;

 SELECT count(*) FROM (
SELECT m.original_filename, m.date_mod, duration + 3, ABS(TIMESTAMPDIFF(SECOND, m.date_mod, CONVERT(CONCAT(SUBSTRING(m.original_filename, 1, 4), '-', SUBSTRING(m.original_filename, 5, 2), '-', SUBSTRING(m.original_filename, 7, 2), ' ', SUBSTRING(m.original_filename, 10, 2), ':', SUBSTRING(m.original_filename, 12, 2), ':', SUBSTRING(m.original_filename, 14, 2)), datetime))) FROM
 pictureorganizer.media_file m WHERE
 length(m.original_filename) > 16
 AND duration + 3 > ABS(TIMESTAMPDIFF(SECOND, m.date_mod, CONVERT(CONCAT(SUBSTRING(m.original_filename, 1, 4), '-', SUBSTRING(m.original_filename, 5, 2), '-', SUBSTRING(m.original_filename, 7, 2), ' ', SUBSTRING(m.original_filename, 10, 2), ':', SUBSTRING(m.original_filename, 12, 2), ':', SUBSTRING(m.original_filename, 14, 2)), datetime)))
 ) a
 ;

-- SELECT count(*) FROM (
SELECT m.original_filename, m.date_mod, duration + 3, ABS(TIMESTAMPDIFF(SECOND, m.date_mod, CONVERT(CONCAT(SUBSTRING(m.original_filename, 1, 4), '-', SUBSTRING(m.original_filename, 5, 2), '-', SUBSTRING(m.original_filename, 7, 2), ' ', SUBSTRING(m.original_filename, 10, 2), ':', SUBSTRING(m.original_filename, 12, 2), ':', SUBSTRING(m.original_filename, 14, 2)), datetime))) FROM pictureorganizer.media_file m
 WHERE
 length(m.original_filename) > 16
 AND duration + 3 > ABS(3600 - ABS(TIMESTAMPDIFF(SECOND, m.date_mod, CONVERT(CONCAT(SUBSTRING(m.original_filename, 1, 4), '-', SUBSTRING(m.original_filename, 5, 2), '-', SUBSTRING(m.original_filename, 7, 2), ' ', SUBSTRING(m.original_filename, 10, 2), ':', SUBSTRING(m.original_filename, 12, 2), ':', SUBSTRING(m.original_filename, 14, 2)), datetime))))
-- ) a
 ;

-- Képenkénti eltérő fájlok
1	163104
2	67348
3	17371
4	1335
5	84
6	2429
7	301
8	6
12	6
41	1
2912	1

SELECT duplicates, count(*) FROM (
SELECT image_id, count(*) duplicates FROM (SELECT f.image_id, f.filehash FROM MEDIA_FILE f
-- LEFT JOIN FOLDER fo ON fo.id = f.folder_id WHERE f.drive_id = 3 AND fo.path LIKE '/Pictures/Photos/DBSaved/%'
GROUP BY f.image_id, f.filehash) subq GROUP BY image_id
-- HAVING duplicates > 1
) a GROUP BY duplicates;