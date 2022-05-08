ALTER TABLE media_file RENAME media_file_old;

CREATE TABLE media_file  (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  commit VARCHAR(45) NULL DEFAULT NULL,
  invalid TINYINT UNSIGNED,
  parent_id INT UNSIGNED NULL,
  main_id INT UNSIGNED NULL  
) select
filehash, type as filetype, parent_id, size, standalone, date_stored, date_stored_local, date_stored_utc, date_stored_tz
FROM media_file_old 
GROUP BY
filehash, type, size, standalone, date_stored, date_stored_local, date_stored_utc, date_stored_tz;

ALTER TABLE `pictureorganizer`.`media_file` 
ADD UNIQUE INDEX `filehash_unique` (`filehash` ASC, `filetype` ASC) VISIBLE;
ALTER TABLE `pictureorganizer`.`media_file` 
ADD INDEX `filehash_idx` (`filehash` ASC) VISIBLE;
ALTER TABLE `pictureorganizer`.`media_file` 
ADD INDEX `parent_filehash_idx` (`parent_filehash` ASC) VISIBLE;


CREATE TABLE meta_data (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
) SELECT
duration, latitude, longitude, altitude, orientation, keyword, rating, title
FROM media_file_old
GROUP BY
duration, latitude, longitude, altitude, orientation, keyword, rating, title;

CREATE TABLE media_image (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  invalid TINYINT UNSIGNED
) SELECT
mf.id as media_file_id, mfo.image_id, mm.id as meta_data_id, mfo.exifbackup
FROM media_file_old mfo
LEFT JOIN media_file mf ON mfo.filehash = mf.filehash AND mfo.type = mf.filetype
LEFT JOIN meta_data mm ON  mfo.duration = mm.duration
 AND mfo.latitude <=> mm.latitude
 AND mfo.longitude <=> mm.longitude
 AND mfo.altitude <=> mm.altitude
 AND mfo.orientation <=> mm.orientation
 AND mfo.keyword <=> mm.keyword
 AND mfo.rating <=> mm.rating
 AND mfo.title <=> mm.title
GROUP BY
mf.id, mfo.image_id, mm.id, mfo.exifbackup
;

SET @rownr=0;
CREATE TABLE media_file_instance (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
) SELECT id as media_file_old_id, folder_id, filename, name_version, date_mod, XMPattached FROM media_file;

UPDATE media_file_instance mfi SET mfi.date_mod = (SELECT mf.date_mod FROM media_file mf WHERE mf.id = mfi.media_file_id); 
UPDATE media_file_instance mfi SET mfi.new_media_id = (SELECT m.id FROM media m WHERE m.filehash = (SELECT mf.filehash FROM media_file mf WHERE mf.id = mfi.media_file_id)); 
UPDATE media_file_instance mfi SET mfi.new_media_id = 375427 WHERE mfi.id = 1162568;

SET SQL_SAFE_UPDATES = 0;
UPDATE media_file mf SET mf.date_stored = (SELECT * FROM (SELECT DISTINCT(mf1.date_stored) FROM media_file mf1 WHERE mf1.filehash = mf.filehash AND mf1.date_stored IS NOT NULL) as t) WHERE mf.date_stored IS NULL;
UPDATE media_file mf SET mf.date_stored_local = (SELECT * FROM (SELECT DISTINCT(mf1.date_stored_local) FROM media_file mf1 WHERE mf1.filehash = mf.filehash AND mf1.date_stored_local IS NOT NULL) as t) WHERE mf.date_stored_local IS NULL;
UPDATE media_file mf SET mf.date_stored_utc = (SELECT * FROM (SELECT DISTINCT(mf1.date_stored_utc) FROM media_file mf1 WHERE mf1.filehash = mf.filehash AND mf1.date_stored_utc IS NOT NULL) as t) WHERE mf.date_stored_utc IS NULL;
UPDATE media_file mf SET mf.date_stored_tz = (SELECT * FROM (SELECT DISTINCT(mf1.date_stored_tz) FROM media_file mf1 WHERE mf1.filehash = mf.filehash AND mf1.date_stored_tz IS NOT NULL) as t) WHERE mf.date_stored_tz IS NULL;
UPDATE media_file mf SET mf.date_stored_tz = 'Europe/Berlin' WHERE mf.date_stored_tz IN ('+01:00', '+02:00');
UPDATE media_file_old mf SET mf.orientation = (SELECT * FROM (SELECT DISTINCT(mf1.orientation) FROM media_file_old mf1 WHERE mf1.filehash = mf.filehash AND mf1.orientation IS NOT NULL) as t) WHERE mf.orientation IS NULL;
UPDATE media_file_old mf SET mf.rating = (SELECT * FROM (SELECT DISTINCT(mf1.rating) FROM media_file_old mf1 WHERE mf1.filehash = mf.filehash AND mf1.rating IS NOT NULL) as t) WHERE mf.rating IS NULL;
UPDATE media_file_old mf SET mf.exifbackup = (SELECT * FROM (SELECT DISTINCT(mf1.exifbackup) FROM media_file_old mf1 WHERE mf1.filehash = mf.filehash AND mf1.exifbackup IS NOT NULL) as t) WHERE mf.exifbackup IS NULL;
UPDATE media_file_old mf SET mf.title = (SELECT * FROM (SELECT DISTINCT(mf1.title) FROM media_file_old mf1 WHERE mf1.filehash = mf.filehash AND mf1.title IS NOT NULL) as t) WHERE mf.title IS NULL;