package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.bo.FileBO;
import io.github.talelin.latticy.mapper.FileMapper;
import io.github.talelin.latticy.model.FileDO;
import io.github.talelin.latticy.module.file.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 文件服务实现类单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("文件服务实现类测试")
class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private Uploader uploader;

    @Mock
    private FileProperties fileProperties;

    private FileDO fileDO;

    @BeforeEach
    void setUp() throws Exception {
        // 使用反射设置baseMapper
        Field baseMapperField = fileService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(fileService, fileMapper);

        // 使用反射设置uploader和fileProperties
        Field uploaderField = fileService.getClass().getDeclaredField("uploader");
        uploaderField.setAccessible(true);
        uploaderField.set(fileService, uploader);

        Field filePropertiesField = fileService.getClass().getDeclaredField("fileProperties");
        filePropertiesField.setAccessible(true);
        filePropertiesField.set(fileService, fileProperties);

        fileDO = new FileDO();
        fileDO.setId(1);
        fileDO.setName("test.jpg");
        fileDO.setPath("/upload/test.jpg");
        fileDO.setExtension("jpg");
        fileDO.setSize(1024);
        fileDO.setMd5("abc123");
        fileDO.setType("LOCAL");
    }

    @Test
    @DisplayName("检查文件是否存在 - 文件存在")
    void checkFileExistByMd5_WithExistingFile_ShouldReturnTrue() {
        when(fileMapper.selectCountByMd5("abc123")).thenReturn(1);

        boolean result = fileService.checkFileExistByMd5("abc123");

        assertThat(result).isTrue();
        verify(fileMapper).selectCountByMd5("abc123");
    }

    @Test
    @DisplayName("检查文件是否存在 - 文件不存在")
    void checkFileExistByMd5_WithNonExistingFile_ShouldReturnFalse() {
        when(fileMapper.selectCountByMd5("nonexistent")).thenReturn(0);

        boolean result = fileService.checkFileExistByMd5("nonexistent");

        assertThat(result).isFalse();
        verify(fileMapper).selectCountByMd5("nonexistent");
    }

    @Test
    @DisplayName("检查文件是否存在 - 空MD5")
    void checkFileExistByMd5_WithEmptyMd5_ShouldReturnFalse() {
        when(fileMapper.selectCountByMd5("")).thenReturn(0);

        boolean result = fileService.checkFileExistByMd5("");

        assertThat(result).isFalse();
        verify(fileMapper).selectCountByMd5("");
    }

    @Test
    @DisplayName("检查文件是否存在 - null MD5")
    void checkFileExistByMd5_WithNullMd5_ShouldReturnFalse() {
        when(fileMapper.selectCountByMd5(null)).thenReturn(0);

        boolean result = fileService.checkFileExistByMd5(null);

        assertThat(result).isFalse();
        verify(fileMapper).selectCountByMd5(null);
    }

    @Test
    @DisplayName("文件上传 - 新文件上传成功")
    void upload_WithNewFile_ShouldReturnFileBO() {
        // 准备测试数据
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "test content".getBytes());
        MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
        fileMap.add("file", multipartFile);

        // 模拟文件不存在
        when(fileMapper.selectByMd5(anyString())).thenReturn(null);
        when(fileMapper.insert(any(FileDO.class))).thenReturn(1);
        when(fileProperties.getServePath()).thenReturn("/assets");
        when(fileProperties.getDomain()).thenReturn("http://localhost:5000");

        // 模拟uploader行为
        doAnswer(invocation -> {
            UploadHandler handler = invocation.getArgument(1);
            File file = new File();
            file.setName("test.jpg");
            file.setPath("/upload/test.jpg");
            file.setExtension("jpg");
            file.setSize(1024);
            file.setMd5("abc123");
            file.setType("LOCAL");
            file.setKey("file");

            // preHandle返回true表示是新文件
            boolean shouldUpload = handler.preHandle(file);
            if (shouldUpload) {
                handler.afterHandle(file);
            }
            return null;
        }).when(uploader).upload(any(), any(UploadHandler.class));

        List<FileBO> result = fileService.upload(fileMap);

        assertThat(result).hasSize(1);
        verify(fileMapper).insert(any(FileDO.class));
    }

    @Test
    @DisplayName("文件上传 - 文件已存在")
    void upload_WithExistingFile_ShouldReturnExistingFileBO() {
        // 准备测试数据
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "test content".getBytes());
        MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
        fileMap.add("file", multipartFile);

        // 模拟文件已存在
        when(fileMapper.selectByMd5(anyString())).thenReturn(fileDO);
        when(fileProperties.getServePath()).thenReturn("/assets");
        when(fileProperties.getDomain()).thenReturn("http://localhost:5000");

        // 模拟uploader行为
        doAnswer(invocation -> {
            UploadHandler handler = invocation.getArgument(1);
            File file = new File();
            file.setName("test.jpg");
            file.setPath("/upload/test.jpg");
            file.setExtension("jpg");
            file.setSize(1024);
            file.setMd5("abc123");
            file.setType("LOCAL");
            file.setKey("file");

            // preHandle返回false表示文件已存在
            handler.preHandle(file);
            return null;
        }).when(uploader).upload(any(), any(UploadHandler.class));

        List<FileBO> result = fileService.upload(fileMap);

        assertThat(result).hasSize(1);
        // 文件已存在，不应该插入
        verify(fileMapper, never()).insert(any(FileDO.class));
    }

    @Test
    @DisplayName("文件上传 - 云存储文件")
    void upload_WithCloudStorage_ShouldReturnCorrectUrl() {
        // 准备测试数据
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "test content".getBytes());
        MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
        fileMap.add("file", multipartFile);

        // 云存储文件
        FileDO cloudFile = new FileDO();
        cloudFile.setId(1);
        cloudFile.setName("test.jpg");
        cloudFile.setPath("https://cdn.example.com/test.jpg");
        cloudFile.setExtension("jpg");
        cloudFile.setSize(1024);
        cloudFile.setMd5("abc123");
        cloudFile.setType("QINIU");

        when(fileMapper.selectByMd5(anyString())).thenReturn(null);
        when(fileMapper.insert(any(FileDO.class))).thenReturn(1);

        // 模拟uploader行为
        doAnswer(invocation -> {
            UploadHandler handler = invocation.getArgument(1);
            File file = new File();
            file.setName("test.jpg");
            file.setPath("https://cdn.example.com/test.jpg");
            file.setExtension("jpg");
            file.setSize(1024);
            file.setMd5("abc123");
            file.setType("QINIU");
            file.setKey("file");

            handler.preHandle(file);
            handler.afterHandle(file);
            return null;
        }).when(uploader).upload(any(), any(UploadHandler.class));

        List<FileBO> result = fileService.upload(fileMap);

        assertThat(result).hasSize(1);
        // 云存储直接使用原始路径
        assertThat(result.get(0).getUrl()).isEqualTo("https://cdn.example.com/test.jpg");
    }

    @Test
    @DisplayName("文件上传 - 空文件列表")
    void upload_WithEmptyFileMap_ShouldReturnEmptyList() {
        MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();

        doAnswer(invocation -> {
            // 不调用handler，模拟没有文件上传
            return null;
        }).when(uploader).upload(any(), any(UploadHandler.class));

        List<FileBO> result = fileService.upload(fileMap);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("文件上传 - 多个文件")
    void upload_WithMultipleFiles_ShouldReturnMultipleFileBOs() {
        MockMultipartFile file1 = new MockMultipartFile(
                "file", "test1.jpg", "image/jpeg", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile(
                "file", "test2.jpg", "image/jpeg", "content2".getBytes());
        MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
        fileMap.add("file", file1);
        fileMap.add("file", file2);

        when(fileMapper.selectByMd5(anyString())).thenReturn(null);
        when(fileMapper.insert(any(FileDO.class))).thenReturn(1);
        when(fileProperties.getServePath()).thenReturn("/assets");
        when(fileProperties.getDomain()).thenReturn("http://localhost:5000");

        // 模拟上传两个文件
        doAnswer(invocation -> {
            UploadHandler handler = invocation.getArgument(1);

            File f1 = new File();
            f1.setName("test1.jpg");
            f1.setPath("/upload/test1.jpg");
            f1.setExtension("jpg");
            f1.setSize(1024);
            f1.setMd5("md5_1");
            f1.setType("LOCAL");
            f1.setKey("file");

            handler.preHandle(f1);
            handler.afterHandle(f1);

            File f2 = new File();
            f2.setName("test2.jpg");
            f2.setPath("/upload/test2.jpg");
            f2.setExtension("jpg");
            f2.setSize(2048);
            f2.setMd5("md5_2");
            f2.setType("LOCAL");
            f2.setKey("file");

            handler.preHandle(f2);
            handler.afterHandle(f2);

            return null;
        }).when(uploader).upload(any(), any(UploadHandler.class));

        List<FileBO> result = fileService.upload(fileMap);

        assertThat(result).hasSize(2);
        verify(fileMapper, times(2)).insert(any(FileDO.class));
    }
}
