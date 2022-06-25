SELECT umfo.original_filename, umfo.image_id, umfo.shotnumber, umfv.filetype, umfv.standalone, umfv.id as original_version 
	FROM media_file_version umfv LEFT JOIN media_file_instance umfi ON umfi.media_file_id = umfv.id LEFT JOIN media_file_old umfo ON umfi.media_file_old_id = umfo.id
WHERE umfv.id IN (
	SELECT orig.original_version FROM (
			SELECT mfo.original_filename, mfo.shotnumber, mfv.filetype, mfv.standalone, mfv.id as original_version 
				FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id
                WHERE image_id > 3
				GROUP BY mfo.original_filename, mfo.shotnumber, mfv.filetype, mfv.standalone, mfv.id
		) as orig
		group by orig.original_version HAVING count(*)>1
	)
	GROUP BY umfo.original_filename, umfo.shotnumber, umfv.filetype, umfv.standalone, umfv.id;


SELECT distinct original_filename FROM media_file_old WHERE image_id < 4 AND date_stored IS NULL;

