SET SQL_SAFE_UPDATES = 0;

CREATE TABLE media_directory  (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
)  
SELECT STR_TO_DATE(SUBSTRING(folder_name, 1, 10), '%Y-%m-%d') as from_date, STR_TO_DATE(SUBSTRING(folder_name, 14, 10), '%Y-%m-%d') as to_date, SUBSTRING(folder_name, 25) as title, folder_name FROM (
SELECT distinct(REGEXP_SUBSTR(name, '20[0-9]{2}-[0-9]{2}-[0-9]{2} - 20[0-9]{2}-[0-9]{2}-[0-9]{2} ?[^/]*$')) folder_name FROM folder WHERE REGEXP_LIKE (name, '20[0-9]{2}-[0-9]{2}-[0-9]{2} - 20[0-9]{2}-[0-9]{2}-[0-9]{2} ?[^/]*$', 'i')) innerSel ORDER BY from_date;

ALTER TABLE `pictureorganizer`.`media_file` 
ADD INDEX `media_file_fk_media_directory_idx` (`media_directory_id` ASC) VISIBLE;
;
ALTER TABLE `pictureorganizer`.`media_file` 
ADD CONSTRAINT `media_file_fk_media_directory`
  FOREIGN KEY (`media_directory_id`)
  REFERENCES `pictureorganizer`.`media_directory` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

UPDATE media_file mf SET mf.media_directory_id = (
	SELECT distinct(md.id) FROM media_directory md LEFT JOIN folder f ON f.name = md.folder_name LEFT JOIN media_file_instance mfi ON mfi.folder_id = f.id WHERE mfi.media_file_id = mf.id
);

UPDATE media_file mf SET mf.media_directory_id = (SELECT distinct(md.id) FROM media_directory md LEFT JOIN folder f ON f.name = md.folder_name LEFT JOIN media_file_instance mfi ON mfi.folder_id = f.id WHERE mfi.media_file_id = mf.id)
WHERE 1 =
	(SELECT count(distinct(md.id)) FROM media_directory md LEFT JOIN folder f ON f.name = md.folder_name LEFT JOIN media_file_instance mfi ON mfi.folder_id = f.id WHERE mfi.media_file_id = mf.id)
;


SELECT * FROM pictureorganizer.media_directory a1 JOIN pictureorganizer.media_directory a2 WHERE a1.from_date <= a2.from_date AND a1.to_date >= a2.to_date AND a1.id <> a2.id;


ALTER TABLE `pictureorganizer`.`folder`
    ADD COLUMN `raw_folder` BIT(1) NULL;
ALTER TABLE `pictureorganizer`.`folder`
    ADD COLUMN `media_directory_id` INT UNSIGNED NULL;
UPDATE folder f SET f.media_directory_id = (SELECT md.id FROM media_directory md WHERE md.folder_name = f.name);

ALTER TABLE `pictureorganizer`.`folder` ADD INDEX `folder_fk_media_directory_idx` (`media_directory_id` ASC) VISIBLE;

ALTER TABLE `pictureorganizer`.`folder` 
ADD CONSTRAINT `folder_fk_media_directory`
  FOREIGN KEY (`media_directory_id`)
  REFERENCES `pictureorganizer`.`media_directory` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
