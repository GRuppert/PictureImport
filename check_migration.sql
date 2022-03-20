SELECT * FROM pictureorganizer.media_file_old WHERE filehash = '7de57a51db8410b813f5741959049c99';
SELECT d.*, f.* FROM pictureorganizer.media_file_instance mfi LEFT JOIN folder f ON f.id = mfi.folder_id LEFT JOIN drive d ON d.id = f.drive_id WHERE mfi.media_file_id IN (1421263, 1798841);

SELECT count(*) FROM pictureorganizer.media_image; -- 375472
SELECT count(distinct(image_id)) FROM pictureorganizer.media_image; -- 251986
SELECT count(*) FROM (SELECT image_id, count(*) FROM pictureorganizer.media_image GROUP BY image_id HAVING count(*) < 1) a; -- 0
SELECT count(*) FROM (SELECT image_id, count(*) FROM pictureorganizer.media_image GROUP BY image_id HAVING count(*) = 1) a; -- 163064
SELECT count(*) FROM (SELECT image_id, count(*) FROM pictureorganizer.media_image GROUP BY image_id HAVING count(*) > 1) a; -- 88922

SELECT media_file_id, image_id, count(*) FROM pictureorganizer.media_image GROUP BY media_file_id, image_id HAVING count(*) > 1 ORDER BY count(*) DESC; -- should be zero
SELECT * FROM pictureorganizer.media_image mi LEFT JOIN meta_data mm ON  mm.id = mi.meta_data_id WHERE media_file_id = 225737 AND image_id  = 44871;
SELECT * FROM pictureorganizer.media_image mi LEFT JOIN meta_data mm ON  mm.id = mi.meta_data_id WHERE media_file_id = 230204 AND image_id  = 44871;
SELECT * FROM pictureorganizer.media_image mi LEFT JOIN meta_data mm ON  mm.id = mi.meta_data_id WHERE media_file_id = 73119 AND image_id  = 73951;
SELECT * FROM pictureorganizer.media_image mi LEFT JOIN meta_data mm ON  mm.id = mi.meta_data_id WHERE media_file_id = 71821 AND image_id  = 172608;
SELECT a.filehash, count(*) FROM (SELECT filehash, exifbackup FROM media_file_old GROUP BY filehash, exifbackup) a GROUP BY a.filehash ORDER BY count(*) DESC;


SELECT num, count(*) FROM (SELECT image_id, count(*) num FROM pictureorganizer.media_image GROUP BY image_id HAVING count(*) > 1) a GROUP by num ORDER BY num;