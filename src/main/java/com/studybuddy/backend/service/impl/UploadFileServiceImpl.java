package com.studybuddy.backend.service.impl;

import com.studybuddy.backend.object.FileUpload;
import com.studybuddy.backend.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {
	private final String UPLOAD_PATH = "/image/subject_id_$1/question_id_$2";

	@Override
	public void uploadFile(MultipartFile file, int subjectId, int questionId, String endPath) {
		try {
			String path = (UPLOAD_PATH + endPath).replace("$1", String.valueOf(subjectId)).replace("$2", String.valueOf(questionId));
			Files.createDirectories(Paths.get(path));

			File saveFile = new File(path + "/" + file.getOriginalFilename());
			file.transferTo(saveFile);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<FileUpload> getFile(int subjectId, int questionId, String endPath) {
		List<FileUpload> fileUploads = new ArrayList<>();
		try {
			String path = (UPLOAD_PATH + endPath).replace("$1", String.valueOf(subjectId)).replace("$2", String.valueOf(questionId));
			File directoryPath = new File(path);
			File filesList[] = directoryPath.listFiles();
			for(File file : filesList) {
				log.info("File name: " + file.getName());
				String extension = FilenameUtils.getExtension(file.getName());

				fileUploads.add(new FileUpload(file.getName(), "data:image/" + extension + ";base64," + Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file))));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return fileUploads;
	}

	@Override
	public void deleteFile(int subjectId, int questionId, String endPath) {
		try {
			String path = (UPLOAD_PATH + endPath).replace("$1", String.valueOf(subjectId)).replace("$2", String.valueOf(questionId));
			File directoryPath = new File(path);
			FileUtils.cleanDirectory(directoryPath);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
