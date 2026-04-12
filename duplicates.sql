SELECT umfo.filehash, umfo.original_filename, umfo.image_id, umfo.shotnumber, umfo.type, umfo.standalone
	FROM media_file_old umfo
WHERE umfo.filehash IN (
	SELECT orig.filehash FROM (
			SELECT mfo.original_filename, mfo.filehash
				FROM media_file_old mfo
##                WHERE image_id > 3
				GROUP BY mfo.original_filename, mfo.filehash
		) as orig
		group by orig.filehash HAVING count(*)>1
	)
	GROUP BY umfo.filehash, umfo.original_filename, umfo.image_id, umfo.shotnumber, umfo.type, umfo.standalone ORDER BY 1;    

SELECT f.path, mfo.* FROM media_file_old mfo LEFT JOIN folder f ON mfo.folder_id = f.id WHERE mfo.filehash = 'd41d8cd98f00b204e9800998ecf8427e';

UPDATE media_file_old mfo SET mfo.title_in_filename = 'arata-bed-top-lg', mfo.original_filename = 'WP_20141025_20_42_31_Raw.jpg' WHERE mfo.filehash = 'd41d8cd98f00b204e9800998ecf8427e';

SELECT * FROM image i WHERE i.id > 3 AND i.original_filename <> (SELECT mf.original_filename FROM media_file mf WHERE mf.image_id = i.id GROUP BY mf.original_filename);

SELECT count(*) FROM image i WHERE i.id > 3 AND i.original_filename IS NOT NULL;

SELECT * FROM media_file omf WHERE omf.image_id IN (SELECT imf.image_id FROM (SELECT mf.image_id, mf.original_filename FROM media_file mf WHERE mf.image_id > 3 GROUP BY mf.image_id, mf.original_filename) imf GROUP BY imf.image_id HAVING count(*) > 1);

SELECT * FROM media_file mf LEFT JOIN image i ON i.id = mf.image_id WHERE mf.image_id > 3 AND i.date_taken IS NOT NULL AND mf.original_filename IN (SELECT imf.original_filename FROM media_file imf LEFT JOIN image ii ON ii.id = imf.image_id WHERE ii.date_taken IS NOT NULL AND 2 > DATEDIFF(ii.date_taken, i.date_taken));

SELECT * FROM media_file mf LEFT JOIN image i ON i.id = mf.image_id WHERE mf.original_filename = 'D5C09294.jpg';

SELECT * FROM media_file_version mfv LEFT JOIN media_image mi ON mi.media_file_version_id = mfv.id WHERE mfv.media_file_id IS NULL AND mi.image_id IN 
(SELECT distinct(image_id) FROM pictureorganizer.media_file WHERE shotnumber IS NOT NULL);

SELECT folders, count(*) FROM (
SELECT dups.media_file_id, group_concat(dups.place separator  ':') folders, group_concat(dups.date_mod separator  ',') timestamps FROM (
	SELECT mfv.media_file_id, CONCAT(d.description, f.path) place, mfi.date_mod FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_version_id = mfv.id LEFT JOIN folder f ON f.id = mfi.folder_id LEFT JOIN drive d ON d.id = f.drive_id WHERE
    1 = 1
--    AND d.id = 3 
    AND mfv.media_file_id IN (
		SELECT distinct(media_file_version.media_file_id)  FROM media_file_version LEFT JOIN media_file ON  media_file_version.media_file_id = media_file.id WHERE media_file.file_type = 'RAW' GROUP BY media_file_version.media_file_id HAVING count(*) > 1)
        ORDER BY 1, 3
) dups
GROUP BY media_file_id
ORDER BY 2
) as dupl
GROUP BY folders;
