CREATE TABLE media_file  (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  main_id INT UNSIGNED NULL,
  image_id INT UNSIGNED NULL COMMENT 'temp for migration',
  shotnumber INT UNSIGNED NULL
) select
mfo.original_filename, mfo.title_in_filename, mfo.type as file_type, mfo.standalone, mfi.id as original_version_id 
FROM media_file_old mfo LEFT JOIN media_file_version mfi ON mfi.filehash = mfo.filehash WHERE mfo.filehash IN 
(
SELECT mfox.filehash FROM media_file_old mfox WHERE mfox.image_id < 4 GROUP BY mfox.filehash
)
GROUP BY mfo.original_filename, mfo.title_in_filename, mfo.type, mfo.standalone, mfi.id;
    
INSERT INTO media_file (original_filename, title_in_filename, file_type, standalone, image_id, shotnumber, original_version_id)
SELECT firsttry.original_filename, firsttry.title_in_filename, firsttry.file_type, firsttry.standalone, firsttry.image_id, firsttry.shotnumber, NULL FROM (
SELECT mfo.image_id, mfo.type as file_type, mfo.shotnumber, mfo.original_filename, mfo.title_in_filename, mfo.standalone FROM media_file_old mfo WHERE mfo.image_id > 3
GROUP BY mfo.image_id, mfo.type, mfo.shotnumber, mfo.original_filename, mfo.title_in_filename, mfo.standalone
# SELECT mfo.image_id, mfo.type, mfo.shotnumber FROM media_file_old mfo WHERE mfo.image_id > 3 GROUP BY mfo.image_id, mfo.type, mfo.shotnumber
) firsttry;
    
SELECT count(*) FROM (
SELECT mfo.image_id, mfo.type, mfo.shotnumber FROM media_file_old mfo WHERE mfo.image_id > 3 GROUP BY mfo.image_id, mfo.type, mfo.shotnumber
) firsttry;
SELECT count(*) FROM (
SELECT mfox.filehash FROM media_file_old mfox WHERE mfox.image_id < 4 GROUP BY mfox.filehash
) nonhashed;

SELECT * FROM media_file_old mfox WHERE mfox.image_id < 4;

SELECT nonhash.filehash FROM (
SELECT mfox.filehash, mfox.original_filename, mfox.title_in_filename, mfox.type, mfox.standalone FROM media_file_old mfox WHERE mfox.image_id < 4 GROUP BY mfox.filehash, mfox.original_filename, mfox.title_in_filename, mfox.type, mfox.standalone) nonhash
GROUP BY nonhash.filehash HAVING count(*) > 1;


SELECT mfo.image_id, mfo.type, mfo.shotnumber, mfo.original_filename, mfo.title_in_filename, mfo.standalone FROM media_file_old mfo WHERE mfo.image_id IN (
SELECT dups.image_id FROM (
SELECT firsttry.image_id, firsttry.shotnumber FROM (
SELECT imfo.image_id, imfo.type, imfo.shotnumber, imfo.original_filename, imfo.title_in_filename, imfo.standalone FROM media_file_old imfo WHERE imfo.image_id > 3
 GROUP BY imfo.image_id, imfo.type, imfo.shotnumber, imfo.original_filename, imfo.title_in_filename, imfo.standalone
) firsttry
GROUP BY firsttry.image_id, firsttry.shotnumber HAVING count(*) > 1) dups
)
GROUP BY mfo.image_id, mfo.type, mfo.shotnumber, mfo.original_filename, mfo.title_in_filename, mfo.standalone ORDER BY 1;


UPDATE media_file_old mf SET
mf.title_in_filename =
(SELECT * FROM (SELECT distinct(mf3.title_in_filename) FROM media_file_old mf3 WHERE mf.image_id = mf3.image_id AND mf3.title_in_filename IS NOT NULL) AS hidit2 )
 WHERE mf.image_id > 3 AND 1 < (SELECT count(*) FROM (SELECT mfo.title_in_filename FROM media_file_old mfo WHERE mfo.image_id = mf.image_id GROUP BY mfo.title_in_filename) AS hidit);

SELECT
(SELECT * FROM (SELECT distinct(mf3.title_in_filename) FROM media_file_old mf3 WHERE mf.image_id = mf3.image_id AND mf3.title_in_filename IS NOT NULL) AS hidit2 )
FROM  media_file_old mf
 WHERE mf.image_id > 3 AND 1 < (SELECT * FROM (SELECT count(distinct(mfo.title_in_filename)) FROM media_file_old mfo WHERE mf.image_id = mfo.image_id) AS hidit);
 
 
 UPDATE media_file_old mfi SET mfi.standalone = 1 WHERE mfi.image_id IN (116910, 116912, 176981);
 
SELECT omi.media_file_version_id, count(*) FROM media_image omi WHERE omi.image_id IN (
SELECT distinct(mf.image_id) FROM media_file mf WHERE mf.original_version_id IS NULL AND 1 < (SELECT count(distinct(mi.media_file_version_id)) FROM media_image mi WHERE mi.image_id = mf.image_id))
GROUP BY omi.media_file_version_id ORDER BY 2 DESC;
 
SELECT * FROM media_image omi WHERE media_file_version_id IN (73119, 71821, 230204, 225737);
DELETE FROM media_image WHERE id = 297143;
SELECT * FROM meta_data WHERE id IN (9, 19, 81);
SELECT * FROM media_file_version WHERE id = 230204;

UPDATE media_file_old mfi SET mfi.exifbackup = 1 WHERE mfi.filehash = '9d4a49eac2a2f1881ec75b8ec7cc3303';
 
SELECT omi.media_file_version_id, count(*) FROM media_image omi WHERE omi.image_id IN (
SELECT distinct(mf.image_id) FROM media_file mf WHERE mf.original_version_id IS NULL AND 1 < (SELECT count(distinct(mi.media_file_version_id)) FROM media_image mi WHERE mi.image_id = mf.image_id))
GROUP BY omi.media_file_version_id ORDER BY 2 DESC;
 
SELECT * FROM media_image omi WHERE omi.image_id IN (
SELECT mi.image_id FROM media_image mi LEFT JOIN image i ON i.id = mi.image_id WHERE image_id>3 AND i.type = 'tif' GROUP BY image_id HAVING count(*) > 1
) ORDER BY omi.image_id;

SELECT count(*) FROM (
SELECT mi.image_id FROM media_image mi LEFT JOIN image i ON i.id = mi.image_id WHERE image_id>3 AND i.type = 'mp4' GROUP BY image_id HAVING count(*) > 1
) as dups;

SELECT distinct(type) FROM image;

SELECT image_id, count(*) FROM media_image GROUP BY image_id HAVING count(*) > 1 ORDER BY 2 DESC;
SELECT * FROM media_file_version WHERE id IN (14933,144682,144683);








SELECT distinct(f.path) FROM media_image omi LEFT JOIN media_file_instance omfi ON omfi.media_file_id = omi.media_file_version_id LEFT JOIN folder f ON f.id = omfi.folder_id WHERE f.drive_id = 3 AND omi.image_id IN (
SELECT mi.image_id FROM media_image mi LEFT JOIN image i ON i.id = mi.image_id WHERE image_id>3 AND i.type = 'tif' GROUP BY image_id HAVING count(*) > 1
);

SELECT distinct(f.path) FROM media_image omi LEFT JOIN media_file_instance omfi ON omfi.media_file_id = omi.media_file_version_id LEFT JOIN folder f ON f.id = omfi.folder_id WHERE f.drive_id = 3 AND omi.image_id IN (
SELECT mi.image_id FROM media_image mi LEFT JOIN image i ON i.id = mi.image_id WHERE image_id>3 AND i.type = 'mp4' GROUP BY image_id HAVING count(*) > 1
);

SELECT distinct(f.path) FROM media_image omi LEFT JOIN media_file_instance omfi ON omfi.media_file_id = omi.media_file_version_id LEFT JOIN folder f ON f.id = omfi.folder_id WHERE f.drive_id = 3 AND omi.image_id IN (
SELECT mi.image_id FROM media_image mi LEFT JOIN image i ON i.id = mi.image_id WHERE image_id>3 AND i.type = 'jpg' GROUP BY image_id HAVING count(*) > 1
);
