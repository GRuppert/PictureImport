SELECT count(*) FROM devpictureorganizer.media_file_instance WHERE credate IS NOT NULL;

SELECT media_file_id FROM devpictureorganizer.media_file_version WHERE file_type = 'RAW' GROUP BY media_file_id HAVING count(*)>2 ORDER BY media_file_id;
SELECT count(*) FROM (SELECT media_file_id FROM devpictureorganizer.media_file_version WHERE file_type = 'RAW' GROUP BY media_file_id HAVING count(*)>2 ORDER BY media_file_id) ins;
SELECT * FROM devpictureorganizer.media_file mf LEFT JOIN media_directory md ON mf.media_directory_id = md.id WHERE mf.id IN (SELECT media_file_id FROM devpictureorganizer.media_file_version WHERE file_type = 'RAW' GROUP BY media_file_id HAVING count(*)>2 ORDER BY media_file_id);

SELECT * FROM devpictureorganizer.media_file_instance mfi LEFT JOIN folder ON folder.id = mfi.folder_id LEFT JOIN drive ON drive.id = folder.drive_id WHERE mfi.media_file_id = 19028;
SELECT * FROM devpictureorganizer.media_file_instance mfi LEFT JOIN folder ON folder.id = mfi.folder_id LEFT JOIN drive ON drive.id = folder.drive_id WHERE mfi.media_file_id = 46967;
