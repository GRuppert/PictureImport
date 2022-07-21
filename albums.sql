CREATE TABLE album  (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
)  
SELECT STR_TO_DATE(SUBSTRING(folder_name, 1, 10), '%Y-%m-%d') as from_date, STR_TO_DATE(SUBSTRING(folder_name, 14, 10), '%Y-%m-%d') as to_date, SUBSTRING(folder_name, 25) as title, folder_name FROM (
SELECT distinct(REGEXP_SUBSTR(name, '20[0-9]{2}-[0-9]{2}-[0-9]{2} - 20[0-9]{2}-[0-9]{2}-[0-9]{2} ?[^/]*$')) folder_name FROM folder WHERE REGEXP_LIKE (name, '20[0-9]{2}-[0-9]{2}-[0-9]{2} - 20[0-9]{2}-[0-9]{2}-[0-9]{2} ?[^/]*$', 'i')) innerSel ORDER BY from_date;

ALTER TABLE `pictureorganizer`.`media_file` 
ADD INDEX `media_file_fk_album_idx` (`album_id` ASC) VISIBLE;
;
ALTER TABLE `pictureorganizer`.`media_file` 
ADD CONSTRAINT `media_file_fk_album`
  FOREIGN KEY (`album_id`)
  REFERENCES `pictureorganizer`.`album` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

UPDATE media_file mf SET mf.album_id = (
	SELECT distinct(a.id) FROM album a LEFT JOIN folder f ON f.name = a.folder_name LEFT JOIN media_file_instance mfi ON mfi.folder_id = f.id LEFT JOIN media_file_version mfv ON mfv.id = mfi.media_file_version_id WHERE mfv.media_file_id = mf.id
);

SELECT * FROM pictureorganizer.album a1 JOIN pictureorganizer.album a2 WHERE a1.from_date <= a2.from_date AND a1.to_date >= a2.to_date AND a1.id <> a2.id;
