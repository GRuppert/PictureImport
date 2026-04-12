SELECT count(*) FROM devpictureorganizer.media_file_instance WHERE credate IS NOT NULL;


SELECT media_file_id FROM devpictureorganizer.media_file_version WHERE file_type = 'RAW' GROUP BY media_file_id HAVING count(*)>2 ORDER BY media_file_id;
SELECT count(*) FROM (SELECT media_file_id FROM devpictureorganizer.media_file_version WHERE file_type = 'RAW' GROUP BY media_file_id HAVING count(*)>2 ORDER BY media_file_id) ins;